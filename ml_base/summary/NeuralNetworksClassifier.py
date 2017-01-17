#coding:utf-8

#一层神经网络(名称看似很牛逼）
#示例代码
#import NeuralNetworksClassifier as nn
#online,pharmacy=1,2
#spam,notspam=1,2
#possible=[spam,notspam]
#x=nn.searchnet('nn.db')
#x.maketables()
#x.traingquery([online],possible,notspam)
#x.trainquery([online,pharmacy],possible,spam)
#x.trainquery([pharmacy],possible,notspam)
#x.trainquery([online,pharmacy],possible)
#x.getresult([online,pharmacy],possible)
#x.getresult([online],possible)


from math import tanh
from sqlite3 import dbapi2 as sqlite


def dtanh(y):
    return 1.0-y*y

#一层神经网络基本算法示例
class searchnet:
    def __init__(self,dbname):
      self.con=sqlite.connect(dbname)

    def __del__(self):
      self.con.close()

    def maketables(self):
      self.con.execute('create table hiddennode(create_key)')
      self.con.execute('create table wordhidden(fromid,toid,strength)')
      self.con.execute('create table hiddenurl(fromid,toid,strength)')
      self.con.commit()

    #判断当前连接的强度
    def getstrength(self,fromid,toid,layer):
      if layer==0: table='wordhidden'
      else: table='hiddenurl'
      res=self.con.execute('select strength from %s where fromid=%d and toid=%d' % (table,fromid,toid)).fetchone()
      if res==None:
          if layer==0: return -0.2
          if layer==1: return 0
      return res[0]

    #用以判断连接是否已存在，并利用新的强度值更新连接或创建连接
    def setstrength(self,fromid,toid,layer,strength):
      if layer==0: table='wordhidden'
      else: table='hiddenurl'
      res=self.con.execute('select rowid from %s where fromid=%d and toid=%d' % (table,fromid,toid)).fetchone()
      if res==None:
        self.con.execute('insert into %s (fromid,toid,strength) values (%d,%d,%f)' % (table,fromid,toid,strength))
      else:
        rowid=res[0]
        self.con.execute('update %s set strength=%f where rowid=%d' % (table,strength,rowid))

    def generatehiddennode(self,wordids,urls):
      if len(wordids)>3: return None
      # Check if we already created a node for this set of words
      # 检查我们是否已经为这组单词建好了一个节点
      sorted_words=[str(id) for id in wordids]
      sorted_words.sort()
      createkey='_'.join(sorted_words)
      res=self.con.execute(
      "select rowid from hiddennode where create_key='%s'" % createkey).fetchone()

      # If not, create it
      if res==None:
        cur=self.con.execute(
        "insert into hiddennode (create_key) values ('%s')" % createkey)
        hiddenid=cur.lastrowid
        # Put in some default weights
        #设置默认权重
        for wordid in wordids:
          #保存到wordhidden
          self.setstrength(wordid,hiddenid,0,1.0/len(wordids))
        for urlid in urls:
          #保存到hiddenurl
          self.setstrength(hiddenid,urlid,1,0.1)
        self.con.commit()

    #从隐藏层中找出与某项查询相关的所有节点
    def getallhiddenids(self,wordids,urlids):
      l1={}
      for wordid in wordids:
        cur=self.con.execute(
        'select toid from wordhidden where fromid=%d' % wordid)
        for row in cur: l1[row[0]]=1
      for urlid in urlids:
        cur=self.con.execute(
        'select fromid from hiddenurl where toid=%d' % urlid)
        for row in cur: l1[row[0]]=1
      return l1.keys()

    # 利用数据库中保存的信息，建立起包括所有当前权重值在内的相应网络
    def setupnetwork(self,wordids,urlids):
        # value lists
        #单词列表，查询节点及URL
        self.wordids=wordids
        self.hiddenids=self.getallhiddenids(wordids,urlids)
        self.urlids=urlids

        # node outputs
        #节点输出
        self.ai = [1.0]*len(self.wordids)
        self.ah = [1.0]*len(self.hiddenids)
        self.ao = [1.0]*len(self.urlids)

        # create weights matrix
        # 建立权重矩阵
        self.wi = [[self.getstrength(wordid,hiddenid,0)
                    for hiddenid in self.hiddenids]
                   for wordid in self.wordids]
        self.wo = [[self.getstrength(hiddenid,urlid,1)
                    for urlid in self.urlids]
                   for hiddenid in self.hiddenids]

    #前馈算法
    #1.循环遍历所有位于隐藏层中的节点
    #2.将所有来自输入层的输出结果乘以连接强度之后累加起来
    #3.每个节点的输出等于所有输入之和经过tanh函数计算之后的结果
    #4.输出层的处理过程类似，也是将上一层的输出结果乘以强度值，然后应用tanh函数给出最终的输出结果
    def feedforward(self):
        # the only inputs are the query words
        # 查询单词是仅有的输入
        for i in range(len(self.wordids)):
            self.ai[i] = 1.0

        # hidden activations
        # 隐藏层节点的活跃程度
        for j in range(len(self.hiddenids)):
            sum = 0.0
            for i in range(len(self.wordids)):
                sum = sum + self.ai[i] * self.wi[i][j]
            self.ah[j] = tanh(sum)

        # output activations
        # 输出层节点的活跃程序
        for k in range(len(self.urlids)):
            sum = 0.0
            for j in range(len(self.hiddenids)):
                sum = sum + self.ah[j] * self.wo[j][k]
            self.ao[k] = tanh(sum)

        return self.ao[:]

    def getresult(self,wordids,urlids):
      self.setupnetwork(wordids,urlids)
      return self.feedforward()

    #利用反向传播法进行训练
    #对于输出层的每个节点
    #1.计算节点当前输出结果与期望结果之间的差距
    #2.利用dtanh函数确定节点的总输入须要如何改变
    #3.改变每个外部回指链接的强度值，其值与链接的当前强度及学习速率成一定比例。
    #对于隐藏层的每个节点
    #1.将每个输出链接(output link)的强度值乘以其目标节点所需的改变量，再累加求和，从而改变节点的输出结果
    #2.使用dtanh函数确定节点的总输入所需的该变量
    #3.改变每个输入链接（input link）的强度值，其值与链接的当前强度及学习速率成一定比例
    def backPropagate(self, targets, N=0.5):
        # calculate errors for output
        #计算输出层的误差
        output_deltas = [0.0] * len(self.urlids)
        for k in range(len(self.urlids)):
            error = targets[k]-self.ao[k]
            output_deltas[k] = dtanh(self.ao[k]) * error

        # calculate errors for hidden layer
        #计算隐藏层的误差
        hidden_deltas = [0.0] * len(self.hiddenids)
        for j in range(len(self.hiddenids)):
            error = 0.0
            for k in range(len(self.urlids)):
                error = error + output_deltas[k]*self.wo[j][k]
            hidden_deltas[j] = dtanh(self.ah[j]) * error

        # update output weights
        # 更新输出权重
        for j in range(len(self.hiddenids)):
            for k in range(len(self.urlids)):
                change = output_deltas[k]*self.ah[j]
                self.wo[j][k] = self.wo[j][k] + N*change

        # update input weights
        # 更新输入权重
        for i in range(len(self.wordids)):
            for j in range(len(self.hiddenids)):
                change = hidden_deltas[j]*self.ai[i]
                self.wi[i][j] = self.wi[i][j] + N*change

    #训练
    #参数1.word列表 2.url列表 3.选中的那个url
    def trainquery(self,wordids,urlids,selectedurl):
      # generate a hidden node if necessary
      self.generatehiddennode(wordids,urlids)

      self.setupnetwork(wordids,urlids)
      self.feedforward()
      targets=[0.0]*len(urlids)
      targets[urlids.index(selectedurl)]=1.0
      error = self.backPropagate(targets)
      self.updatedatabase()

    def updatedatabase(self):
      # set them to database values
      for i in range(len(self.wordids)):
          for j in range(len(self.hiddenids)):
              self.setstrength(self.wordids[i],self. hiddenids[j],0,self.wi[i][j])
      for j in range(len(self.hiddenids)):
          for k in range(len(self.urlids)):
              self.setstrength(self.hiddenids[j],self.urlids[k],1,self.wo[j][k])
      self.con.commit()