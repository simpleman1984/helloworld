package test.hbase

import org.apache.spark.SparkContext
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.spark.SparkConf
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase.spark.HBaseContext
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.CellUtil
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.client.Result

object SparkHBase {
  def main(args: Array[String]) {
    //有比较详细的spark和hbase的例子
    //http://blog.cloudera.com/blog/2015/08/apache-spark-comes-to-apache-hbase-with-hbase-spark-module/
    //github上各种示例
    //https://github.com/apache/hbase/blob/master/hbase-spark/src/main/java/org/apache/hadoop/hbase/spark/example/hbasecontext/JavaHBaseBulkGetExample.java
    //https://github.com/tmalaska/SparkOnHBase/blob/master/src/main/scala/org/apache/hadoop/hbase/spark/example/hbasecontext/HBaseBulkGetExample.scala

    System.setProperty("hadoop.home.dir", "D:/workspace/git_lab/spark-test/hadoop");
    //屏蔽不必要的日志显示在终端上
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.apache.eclipse.jetty.server").setLevel(Level.OFF)
    Logger.getLogger("org.spark_project.jetty.server").setLevel(Level.OFF)

    val sparkConf = new SparkConf().setAppName("MovieLensALS").setMaster("local[5]")
    val sc = new SparkContext(sparkConf)

    val config = HBaseConfiguration.create()
    config.addResource(new Path("/src/test/hbase/core-site.xml"));

    val textFile = sc.textFile("src/test/hbase/hbase.dat")

    val tableName = TableName.valueOf("test1");
    val hbaseContext = new HBaseContext(sc, config)

    val list = List(Bytes.toBytes("1"), Bytes.toBytes("2"));
    val rdd = sc.parallelize(Array(
      Bytes.toBytes("1"),
      Bytes.toBytes("2"),
      Bytes.toBytes("3"),
      Bytes.toBytes("4"),
      Bytes.toBytes("5"),
      Bytes.toBytes("6"),
      Bytes.toBytes("7")))

    //Future Work
    //未来Work数据

    val getRdd = hbaseContext.bulkGet[Array[Byte], String](tableName,
      2,
      rdd,
      record => {
        System.out.println("making Get:" + record)
        new Get(record)
      },
      (result: Result) => {

        val it = result.listCells().iterator()
        val b = new StringBuilder

        b.append(Bytes.toString(result.getRow) + ":")

        while (it.hasNext) {
          val cell = it.next()
          val q = Bytes.toString(CellUtil.cloneQualifier(cell))
          if (q.equals("counter")) {
            b.append("(" + q + "," + Bytes.toLong(CellUtil.cloneValue(cell)) + ")")
          } else {
            b.append("(" + q + "," + Bytes.toString(CellUtil.cloneValue(cell)) + ")")
          }
        }
        b.toString()
      })

    getRdd.collect().foreach(v => println(v))

    hbaseContext.bulkPut[String](textFile, tableName, (putRecord) => {
      System.out.println("hbase-" + putRecord)
      val put = new Put(Bytes.toBytes("Value- " + putRecord))
      put.addColumn(Bytes.toBytes("c"), Bytes.toBytes("1"),
        Bytes.toBytes(putRecord.length()))
      put
    });

  }

}