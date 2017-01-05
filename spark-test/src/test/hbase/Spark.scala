package test.hbase

import org.apache.log4j.{ Level, Logger }
import org.apache.spark.SparkContext
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.spark.SparkConf
import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase.spark.HBaseContext
import java.util.ArrayList
import org.apache.hadoop.hbase.util.Bytes

object Spark {
  def main(args: Array[String]) {
    //https://www.mapr.com/developercentral/code/loading-hbase-tables-spark#.WGt5aPmEC0E
    //http://hbase.apache.org/
    //https://docs.microsoft.com/en-us/azure/hdinsight/hdinsight-hbase-build-java-maven-linux
    //https://github.com/apache/spark/tree/master/examples/src/main/scala/org/apache/spark/examples
    //https://databricks.gitbooks.io/databricks-spark-knowledge-base/content/best_practices/prefer_reducebykey_over_groupbykey.html
    //基础方法使用介绍
    //http://www.cnblogs.com/MOBIN/p/5373256.html#11
    
    System.setProperty("hadoop.home.dir", "D:/workspace/git_lab/spark-test/hadoop");
    //屏蔽不必要的日志显示在终端上
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.apache.eclipse.jetty.server").setLevel(Level.OFF)
    Logger.getLogger("org.spark_project.jetty.server").setLevel(Level.OFF)

    val sparkConf = new SparkConf().setAppName("MovieLensALS").setMaster("local[5]")
    val sc = new SparkContext(sparkConf)

    val config = HBaseConfiguration.create()
    config.addResource(new Path("/src/test/hbase/core-site.xml"));

    //    val hbaseContext = new HBaseContext(sc, config)

    println("===============================================")
    val textFile = sc.textFile("src/test/hbase/hbase.dat")
    val counts = textFile.flatMap(line => line.split("::")).map(word => (word, 1)).reduceByKey(_ + _)
    println("====================" + counts);
    //以下为计算pi的示例
    val count = sc.parallelize(1 to 1).map { i =>
      val x = Math.random()
      val y = Math.random()
      if (x * x + y * y < 1) 1 else 0
    }.reduce(_ + _)
    println("Pi is roughly " + 4.0 * count / 1)

    //DataFrame API Examples
    //    val data = "i am from china i am 10 years old"
    //    val text = sc.parallelize(data)
    val words = textFile
      //filter(func) 根据条件过滤是否进行显示
      //Return a new dataset formed by selecting those elements of the source on which func returns true.
      .filter(line => line.length() > 10)
      //map(func) 映射成新的数据输出
      //Return a new distributed dataset formed by passing each element of the source through a function func.
      .map { s => s }
      //flatMap(func) 先map，然后再平铺下来，再返回
      //Similar to map, but each input item can be mapped to 0 or more output items (so func should return a Seq rather than a single item).
      .flatMap(s => s.split(" "));

    val words2 = sc.textFile("src/test/hbase/hbase2.dat").flatMap(s => s.split(" "))

    //对句子按单词进行计数的两种方式~~~~~~~
    //先定义key，然后再根据当前key进行分组
    //或者 a.groupBy(x => { if (x % 2 == 0) "even" else "odd" }).collect/
    //Avoid GroupByKey
    words.keyBy(s => s).groupByKey
      .map(a => wordCount(a._1, a._2))
      .map(println).collect()

    println("此处为分界线============")

    words.map(word => (word, 1))
      .reduceByKey(_ + _)
      .map(println).collect()

    println("此处为分界线============")

    //Similar to map, but runs separately on each partition (block) of the RDD, so func must be of type Iterator<T> => Iterator<U> when running on an RDD of type T.
    words.mapPartitions { x =>
      {
        var result = List[Int]()
        var i = 0
        while (x.hasNext) {
          var word = x.next();
          i += word.length()
          println(word)
        }
        result.::(i).iterator
      }
    }.map(println).collect()

    println("此处为分界线============")

    //Similar to mapPartitions, but also provides func with an integer value representing the index of the partition, so func must be of type (Int, Iterator<T>) => Iterator<U> when running on an RDD of type T.
    words.mapPartitionsWithIndex { (x, iter) =>
      {
        var result = List[String]()
        var i = 0
        while (iter.hasNext) {
          i += iter.next().length()
        }
        result.::(x + "|" + i).iterator
      }
    }.map(println).collect()

    println("此处为分界线============")

    //该函数比较简单，就是将两个RDD进行合并，不去重。
    words.union(words2).map(println).collect()

    println("此处为分界线============")
    //该函数返回两个RDD的交集，并且去重。
    words.intersection(words2).collect.map(println)

    println("此处为分界线============")
    //该函数类似于intersection，但返回在RDD中出现，并且不在otherRDD中出现的元素，不去重。
    words.subtract(words2).collect.map(println)

    println("此处为分界线============")
    words.distinct(2).collect.map(println)

    println("此处为分界线============")
    val data = sc.parallelize(List((1, 3), (1, 2), (1, 4), (2, 3)))
    //在data数据集中，按key将value进行分组合并，合并时在seq函数与指定的初始值0进行比较，保留大的值；然后在comb中来处理合并的方式。
    val result = data.aggregateByKey(0)(seq, comb)
    result.collect().foreach(println)

    println("此处为分界线============")
    val data1 = List(3, 1, 90, 3, 5, 12)
    val rdd = sc.parallelize(data1)
    rdd.sortBy(x => x).collect.foreach(println)

    println("此处为分界线============")
    rdd.sortBy(x => x, false).collect.foreach(println)

    println("此处为分界线============")
    val a = sc.parallelize(List("wyp", "iteblog", "com", "397090770", "test"), 2)
    val b = sc.parallelize(List(3, 1, 9, 12, 4))
    val c = b.zip(b)
    c.sortByKey().collect.foreach(println)

    println("此处为分界线============")

    //修改默认规则，将整形改成了字符串排序
    implicit val sortIntegersByString = new Ordering[Int] {
      override def compare(a: Int, b: Int) =
        a.toString.compare(b.toString)
    }
    c.sortByKey().collect.foreach(println)

    println("此处为分界线============")

    val idName = sc.parallelize(Array((1, "zhangsan"), (2, "lisi"), (3, "wangwu")))
    val idAge = sc.parallelize(Array((1, 30), (2, 29), (4, 21)))
    idName.join(idAge).collect().foreach(println)

    println("此处为分界线============")
    //左外关联（left out join）
    idName.leftOuterJoin(idAge).collect().foreach(println)

    println("此处为分界线============")
    //右外关联（right outer join）
    idName.rightOuterJoin(idAge).collect().foreach(println)

    println("此处为分界线============")
    //全外关联（full outer join）
    idName.fullOuterJoin(idAge).collect().foreach(println)

    println("此处为分界线============")
    //当出现相同Key时, join会出现笛卡尔积, 而cogroup的处理方式不同
    //When called on datasets of type (K, V) and (K, W), returns a dataset of (K, (Iterable<V>, Iterable<W>)) tuples. This operation is also called groupWith.
    idName.cogroup(idAge).collect().foreach(println)

    println("此处为分界线============")
    val rdd1 = sc.parallelize(1 to 3)
    val rdd2 = sc.parallelize(2 to 5)
    rdd1.cartesian(rdd2).collect().foreach(println)

    println("此处为分界线============")
    //对RDD的分区进行重新分区，shuffle默认值为false,当shuffle=false时，不能增加分区数目,但不会报错，只是分区个数还是原来的
    val rdd3 = sc.parallelize(1 to 16, 4)
    rdd3.coalesce(3).map(println).collect()

    println("此处为分界线============")
    //将RDD的每个分区中的类型为T的元素转换换数组Array[T]
    val rdd4 = sc.parallelize(1 to 16, 4)
    rdd4.glom().collect().foreach(println) //RDD[Array[T]]

    println("此处为分界线============")
    //根据weight权重值将一个RDD划分成多个RDD,权重越高划分得到的元素较多的几率就越大
    val rdd5 = sc.parallelize(1 to 10)
    val randomSplitRDD = rdd5.randomSplit(Array(1.0, 2.0, 7.0))
    randomSplitRDD(0).foreach(x => print(x + " p1 "))
    randomSplitRDD(1).foreach(x => print(x + " p2 "))
    randomSplitRDD(2).foreach(x => print(x + " p3 "))
  }

  def seq(a: Int, b: Int): Int = {
    println("seq: " + a + "\t " + b)
    math.max(a, b)
  }

  def comb(a: Int, b: Int): Int = {
    println("comb : " + a + "\t" + b)
    a + b
  }

  /**
   * 输出分组汇总的字符串发生频率
   */
  def wordCount(key: String, groupInfo: Iterable[String]) = {
    (key, groupInfo.size)
  }

}