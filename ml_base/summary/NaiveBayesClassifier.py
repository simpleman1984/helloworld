#coding:utf-8

#适用于文档分类系统；垃圾邮件过滤；根据关键字的模糊搜索来对一组分档进行划分

#此算法为贝叶斯分类器
#Pr(Category|Document)=Pr(Document|Category)*Pr(Category)/Pr(Document)
#此处
#Pr(Document|Category)=Pr(Word1|Category)*Pr(Word2|Category)*...

#示例命令如下：
#import NaiveBayesClassifier as n
#cl=n.naivebayes(n.getwords)
#cl.setdb('test.db')
#cl.train('pythons are constrictors','snake')
#cl.train('python has dynamic types','language')
#cl.train('python was developed as a scripting language','language')
#cl.classify('dynamic programming')
#cl.classify('boa constrictors')

from sqlite3 import dbapi2 as sqlite
import re
import math

def getwords(doc):
  splitter=re.compile('\\W*')
  print doc
  # Split the words by non-alpha characters
  words=[s.lower() for s in splitter.split(doc)
          if len(s)>2 and len(s)<20]

  # Return the unique set of words only
  return dict([(w,1) for w in words])


class classifier:
  def __init__(self,getfeatures,filename=None):
    # Counts of feature/category combinations
    #  统计特征/分类组合的数量
    self.fc={}
    #  统计每个分类中的文档数量
    # Counts of documents in each category
    self.cc={}
    self.getfeatures=getfeatures

  def setdb(self,dbfile):
    self.con=sqlite.connect(dbfile)
    self.con.execute('create table if not exists fc(feature,category,count)')
    self.con.execute('create table if not exists cc(category,count)')

  #增加对特征/分类组合的计数值
  def incf(self,f,cat):
    count=self.fcount(f,cat)
    if count==0:
      self.con.execute("insert into fc values ('%s','%s',1)"
                       % (f,cat))
    else:
      self.con.execute(
        "update fc set count=%d where feature='%s' and category='%s'"
        % (count+1,f,cat))

  #某一特征出现于某一分类中的次数
  def fcount(self,f,cat):
    res=self.con.execute(
      'select count from fc where feature="%s" and category="%s"'
      %(f,cat)).fetchone()
    if res==None: return 0
    else: return float(res[0])

  #增加对某一分类的计数值
  def incc(self,cat):
    count=self.catcount(cat)
    if count==0:
      self.con.execute("insert into cc values ('%s',1)" % (cat))
    else:
      self.con.execute("update cc set count=%d where category='%s'"
                       % (count+1,cat))

  # 属于某一分类的内容项数量
  def catcount(self,cat):
    res=self.con.execute('select count from cc where category="%s"'
                         %(cat)).fetchone()
    if res==None: return 0
    else: return float(res[0])

  #所有分类的列表
  def categories(self):
    cur=self.con.execute('select category from cc');
    return [d[0] for d in cur]

  # 所有内容项的数量
  def totalcount(self):
    res=self.con.execute('select sum(count) from cc').fetchone();
    if res==None: return 0
    return res[0]

  #开始训练了
  def train(self,item,cat):
    features=self.getfeatures(item)
    # Increment the count for every feature with this category
    # 针对该分类为每个特征增加计数值(f为特征；cat为分类）
    for f in features:
      self.incf(f,cat)

    # Increment the count for this category
    # 添加针对该分类的计数值
    self.incc(cat)
    self.con.commit()

  #一个单词（特征）在一篇属于某个分类的文档中出现的次数，除以该分类的文档总数，计算出单词在分类中出现的概率
  # 条件概率（在给定B的条件下A的概率）
  def fprob(self,f,cat):
    if self.catcount(cat)==0: return 0

    # The total number of times this feature appeared in this
    # category divided by the total number of items in this category
    return self.fcount(f,cat)/self.catcount(cat)

  #从一个合理推测开始，设置默认的权重
  # 1.0 为权重
  # 0.5 则为初始的概率
  def weightedprob(self,f,cat,prf,weight=1.0,ap=0.5):
    # Calculate current probability
    # 计算当前的概率
    basicprob=prf(f,cat)

    # Count the number of times this feature has appeared in
    # all categories
    #统计特征在所有分类中出现的次数
    totals=sum([self.fcount(f,c) for c in self.categories()])

    # Calculate the weighted average
    #计算加权平均
    bp=((weight*ap)+(totals*basicprob))/(weight+totals)
    return bp

# 贝叶斯定律~ 对条件概率进行调换求解
class naivebayes(classifier):

  def __init__(self,getfeatures):
    classifier.__init__(self,getfeatures)
    #设置阈值
    self.thresholds={}

  def docprob(self,item,cat):
    features=self.getfeatures(item)

    # Multiply the probabilities of all the features together
    p=1
    for f in features: p*=self.weightedprob(f,cat,self.fprob)
    return p

  def prob(self,item,cat):
    catprob=self.catcount(cat)/self.totalcount()
    docprob=self.docprob(item,cat)
    return docprob*catprob

  #设置某个分类的阈值
  def setthreshold(self,cat,t):
    self.thresholds[cat]=t

  #获取某个分类的阈值
  def getthreshold(self,cat):
    if cat not in self.thresholds: return 1.0
    return self.thresholds[cat]

  def classify(self,item,default=None):
    probs={}
    #寻找概率最大的分类器
    # Find the category with the highest probability
    max=0.0
    for cat in self.categories():
      probs[cat]=self.prob(item,cat)
      if probs[cat]>max:
        max=probs[cat]
        best=cat

    # Make sure the probability exceeds threshold*next best
    # 确保概率值超出阈值*次大概率值
    for cat in probs:
      if cat==best: continue
      if probs[cat]*self.getthreshold(best)>probs[best]: return default
    return best