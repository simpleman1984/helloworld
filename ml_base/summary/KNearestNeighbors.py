#coding:utf-8
from random import random,randint
import math

#k-最近邻
#示例脚本
#cameras=[{'input':(7.1,3.8),'result':399},{'input':(5.0,2.4),'result':299},{'input':(6.0,4.0),'result':349},{'input':(6.0,12.0),'result':399},{'input':(10.0,3.0),'result':449}]
#import KNearestNeighbors as knn
#knn.knnestimate(cameras,(6.0,6.0),k=2)
#knn.weightedknn(cameras,(6.0,6.0),k=3)
#scc=knn.rescale(cameras,(1,2))
#scc
#def knn1(d,v):return knn.knnestimate(d,v,k=1)
#knn.crossvalidate(knn1,cameras,test=0.3,trials=2)
#knn.crossvalidate(knn1,scc,test=0.3,trials=2)

#交叉验证
def crossvalidate(algf,data,trials=100,test=0.1):
  error=0.0
  for i in range(trials):
    trainset,testset=dividedata(data,test)
    error+=testalgorithm(algf,trainset,testset)
  return error/trials

def dividedata(data,test=0.05):
  trainset=[]
  testset=[]
  for row in data:
    if random()<test:
      testset.append(row)
    else:
      trainset.append(row)
  return trainset,testset

def testalgorithm(algf,trainset,testset):
  error=0.0
  for row in testset:
    guess=algf(trainset,row['input'])
    error+=(row['result']-guess)**2
    #print row['result'],guess
  #print error/len(testset)
  return error/len(testset)

#Scale 之后再看看
def rescale(data,scale):
  scaleddata=[]
  for row in data:
    scaled=[scale[i]*row['input'][i] for i in range(len(scale))]
    scaleddata.append({'input':scaled,'result':row['result']})
  return scaleddata

#反函数（为近邻分配权重）
def inverseweight(dist,num=1.0,const=0.1):
  return num/(dist+const)

#减法函数
def subtractweight(dist,const=1.0):
  if dist>const:
    return 0
  else:
    return const-dist

#高斯函数
def gaussian(dist,sigma=5.0):
  return math.e**(-dist**2/(2*sigma**2))

#加权KNN算法---
def weightedknn(data,vec1,k=5,weightf=gaussian):
  # Get distances
  dlist=getdistances(data,vec1)
  avg=0.0
  totalweight=0.0

  # Get weighted average
  for i in range(k):
    dist=dlist[i][0]
    idx=dlist[i][1]
    weight=weightf(dist)
    avg+=weight*data[idx]['result']
    totalweight+=weight
  if totalweight==0: return 0
  avg=avg/totalweight
  return avg

#K-最近邻算法（普通KNN）---
def knnestimate(data,vec1,k=5):
  # Get sorted distances
  dlist=getdistances(data,vec1)
  avg=0.0

  # Take the average of the top k results
  for i in range(k):
    idx=dlist[i][1]
    avg+=data[idx]['result']
  avg=avg/k
  return avg

def euclidean(v1,v2):
  d=0.0
  for i in range(len(v1)):
    d+=(v1[i]-v2[i])**2
  return math.sqrt(d)

#获取数据与矢量的距离列表
def getdistances(data,vec1):
  distancelist=[]

  # Loop over every item in the dataset
  for i in range(len(data)):
    vec2=data[i]['input']

    # Add the distance and the index
    distancelist.append((euclidean(vec1,vec2),i))

  # Sort by distance
  distancelist.sort()
  return distancelist



