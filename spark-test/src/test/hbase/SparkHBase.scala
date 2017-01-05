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

object SparkHBase {
  def main(args: Array[String]) {
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

    val tableName = TableName.valueOf("tableName");
    val hbaseContext = new HBaseContext(sc, config)
    hbaseContext.bulkPut[String](textFile, tableName, (putRecord) => {
      System.out.println("hbase-" + putRecord)
      val put = new Put(Bytes.toBytes("Value- " + putRecord))
      put.addColumn(Bytes.toBytes("c"), Bytes.toBytes("1"),
        Bytes.toBytes(putRecord.length()))
      put
    });
  }
}