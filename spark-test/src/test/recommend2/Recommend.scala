package test.recommend2

import org.apache.spark.mllib.recommendation.{ ALS, Rating }
import org.apache.log4j.{ Logger, Level }
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.apache.spark.rdd.RDD
import org.jblas.DoubleMatrix

/**
 * 强烈推荐，该方法~~~（还没学习完）
 * http://blog.javachen.com/2015/06/01/how-to-implement-collaborative-filtering-using-spark-als.html
 */
object Recommend {
  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "D:/workspace/git_lab/spark-test/hadoop");

    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    Logger.getLogger("org.spark_project.jetty.server").setLevel(Level.WARN)

    //设置运行环境
    val sparkConf = new SparkConf().setAppName("MovieLensALS").setMaster("local[5]")
    val sc = new SparkContext(sparkConf)

    val data = sc.textFile("src/test/recommend2/ratings.dat")

    val ratings = data.map(_.split("::") match {
      case Array(user, item, rate, ts) =>
        Rating(user.toInt, item.toInt, rate.toDouble)
    }).cache()

    println(ratings.first)

    val users = ratings.map(_.user).distinct()
    val products = ratings.map(_.product).distinct()
    println("Got " + ratings.count() + " ratings from " + users.count + " users on " + products.count + " products.")

    val numPartitions = 2
    //对评分数据生成训练集和测试集，例如：训练集和测试集比例为8比2：
    val splits = ratings.randomSplit(Array(0.8, 0.2), seed = 111l)
    val training = splits(0).repartition(numPartitions)
    val test = splits(1).repartition(numPartitions)

    //调用ALS.train()方法，进行模型训练：
    val rank = 12
    val lambda = 0.01
    val numIterations = 20
    val model = ALS.train(ratings, rank, numIterations, lambda)

    //训练完后，我们看看model中的用户和商品特征向量：
    model.userFeatures
    //res82: org.apache.spark.rdd.RDD[(Int, Array[Double])] = users MapPartitionsRDD[400] at mapValues at ALS.scala:218
    println(model.userFeatures)

    model.userFeatures.count
    //res84: Long = 6040
    println(model.userFeatures.count)

    model.productFeatures
    //res85: org.apache.spark.rdd.RDD[(Int, Array[Double])] = products MapPartitionsRDD[401] at mapValues at ALS.scala:222
    println(model.productFeatures)

    model.productFeatures.count
    //res86: Long = 3706
    println(model.productFeatures.count)

    //对测试集进行评分预测并计算相似度
    computeRmse(model, ratings)

    //查找5个用户
    val _users = users.take(5);

    //查看用户编号为4904的用户的预测结果中预测评分排前10的商品：
    val userId = users.take(1)(0) //4904
    println(userId)
    val K = 10
    val topKRecs = model.recommendProducts(userId, K)
    println(topKRecs.mkString("\n"))

    //该用户的评分记录：
    val productsForUser = ratings.keyBy(_.user).lookup(4904)
    println(productsForUser);

    productsForUser.size //Int = 22
    productsForUser.sortBy(-_.rating).take(10).map(rating => (rating.product, rating.rating)).foreach(println)

    //我们可以该用户对某一个商品的实际评分和预测评分方差为多少：
    val actualRating = productsForUser.take(1)(0)
    val predictedRating = model.predict(4904, actualRating.product)
    val squaredError = math.pow(predictedRating - actualRating.rating, 2.0)

    //以2964商品为例，计算实际评分和预测评分相似度
    val itemId = 2055
    val itemFactor = model.productFeatures.lookup(itemId).head
    val itemVector = new DoubleMatrix(itemFactor)
    println(cosineSimilarity(itemVector, itemVector))

    //找到和该商品最相似的10个商品：
    val sims = model.productFeatures.map {
      case (id, factor) =>
        val factorVector = new DoubleMatrix(factor)
        val sim = cosineSimilarity(factorVector, itemVector)
        (id, sim)
    }
    val sortedSims = sims.top(K)(Ordering.by[(Int, Double), Double] { case (id, similarity) => similarity })
    println(sortedSims.mkString("\n"))

    val sortedSims2 = sims.top(K + 1)(Ordering.by[(Int, Double), Double] { case (id, similarity) => similarity })
    println( sortedSims2.slice(1, 11).map{ case (id, sim) => (id, sim) }.mkString("\n"))
    
  }

  //使用余弦相似度来计算：
  /* Compute the cosine similarity between two vectors */
  def cosineSimilarity(vec1: DoubleMatrix, vec2: DoubleMatrix): Double = {
    vec1.dot(vec2) / (vec1.norm2() * vec2.norm2())
  }

  def computeRmse(model: MatrixFactorizationModel, data: RDD[Rating]) = {
    //我们将训练集当作测试集来进行对比测试。从训练集中获取用户和商品的映射：
    val usersProducts = data.map {
      case Rating(user, product, rate) =>
        (user, product)
    }

    //测试集的记录数等于评分总记录数，验证一下
    usersProducts.count //Long = 1000209

    //推荐模型对用户商品进行预测评分，得到预测评分的数据集：
    val predictions = model.predict(usersProducts).map {
      case Rating(user, product, rate) =>
        ((user, product), rate)
    }
    predictions.count //Long = 1000209

    //将真实评分数据集与预测评分数据集进行合并，这样得到用户对每一个商品的实际评分和预测评分：
    val ratesAndPreds = data.map {
      case Rating(user, product, rate) =>
        ((user, product), rate)
    }.join(predictions)

    //计算根均方差：
    val rmse = math.sqrt(ratesAndPreds.map {
      case ((user, product), (r1, r2)) =>
        val err = (r1 - r2)
        err * err
    }.mean())

    //对测试集进行评分预测并计算相似度
    println(s"RMSE = $rmse")

    //保存用户对商品的真实评分和预测评分记录到本地文件：
    //先按用户排序，然后重新分区确保目标目录中只生成一个文件。如果你重复运行这段代码，则需要先删除目标路径：
    //    ratesAndPreds.sortByKey().repartition(1).sortBy(_._1).map({
    //      case ((user, product), (rate, pred)) => (user + "," + product + "," + rate + "," + pred)
    //    }).saveAsTextFile("/tmp/result")

  }
}