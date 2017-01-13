package test.hbase

import org.apache.spark.SparkContext
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.spark.SparkConf
import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase.client.HBaseAdmin
import org.apache.htrace.Trace
import org.apache.hadoop.hbase.HColumnDescriptor
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.client.HTable
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.HTableDescriptor
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.filter.RegexStringComparator
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp
import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.util.GenericOptionsParser
import org.apache.hadoop.hbase.client.Connection
import org.apache.spark.rdd.JdbcRDD.ConnectionFactory
import org.apache.hadoop.hbase.client.ConnectionFactory
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.hadoop.hbase.client.Table

object Basic {

  def main(args: Array[String]) {
    //https://www.mapr.com/developercentral/code/loading-hbase-tables-spark#.WGt5aPmEC0E
    //http://hbase.apache.org/
    //https://docs.microsoft.com/en-us/azure/hdinsight/hdinsight-hbase-build-java-maven-linux

    System.setProperty("hadoop.home.dir", "D:/workspace/git_lab/spark-test/hadoop");

    //屏蔽不必要的日志显示在终端上
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.apache.eclipse.jetty.server").setLevel(Level.OFF)
    Logger.getLogger("org.spark_project.jetty.server").setLevel(Level.OFF)
    Logger.getLogger("org.apache.zookeeper.ZooKeeper").setLevel(Level.WARN)

    val sparkConf = new SparkConf().setAppName("MovieLensALS").setMaster("local[5]")
    val sc = new SparkContext(sparkConf)

    val config = HBaseConfiguration.create()
    config.addResource(new Path("/src/test/hbase/core-site.xml"));

    //hbase客户端示例
    val connection = ConnectionFactory.createConnection(config)
    val admin = connection.getAdmin();

    //    val tableName = TableName.valueOf("test");
    //    val columnDesc = new HColumnDescriptor("cf1");
    //    admin.enableTable(tableName);
    //    //    admin.disableTable(tableName);
    //    admin.addColumnFamily(tableName, columnDesc);

    val tables = admin.listTables();
    tables.foreach(println)

    //没有表people则创建
    val tableName = TableName.valueOf("test1")
    //创建表，加入默认的簇
    val tableDescriptor = new HTableDescriptor(tableName);
    tableDescriptor.addFamily(new HColumnDescriptor("name"));
    tableDescriptor.addFamily(new HColumnDescriptor("contactinfo"));
    //    admin.createTable(tableDescriptor);

    //往表中插入数据
    val peoples = List(
      Array("1", "Marcel", "Haddad", "marcel@fabrikam.com"),
      Array("2", "Franklin", "Holtz", "franklin@contoso.com"),
      Array("3", "Dwayne", "McKee", "dwayne@fabrikam.com"),
      Array("4", "Rae", "Schroeder", "rae@contoso.com"),
      Array("5", "Rosalie", "burton", "rosalie@fabrikam.com"),
      Array("6", "Gabriela", "Ingram", "gabriela@contoso.com"));

    val table = connection.getTable(tableName);

    // Add each person to the table
    //   Use the `name` column family for the name
    //   Use the `contactinfo` column family for the email
    //向数据库中插入数据~
    peoples.foreach { (_people) =>
      val person = new Put(Bytes.toBytes(_people(0)))
      person.addColumn(Bytes.toBytes("name"), Bytes.toBytes("first"), Bytes.toBytes(_people(1)))
      person.addColumn(Bytes.toBytes("name"), Bytes.toBytes("last"), Bytes.toBytes(_people(2)))
      person.addColumn(Bytes.toBytes("contactinfo"), Bytes.toBytes("email"), Bytes.toBytes(_people(3)))
      table.put(person);
    }
    // flush commits and close the table
    //    admin.flush(tableName)
    table.close();

    //解析命令行參數
    var _args = Array("fabrikam");
    val otherArgs = new GenericOptionsParser(config, _args).getRemainingArgs();
    if (otherArgs.length != 1) {
      System.out.println("usage: [regular expression]");
      System.exit(-1);
    }

    //设置查询参数
    val contactFamily = Bytes.toBytes("contactinfo")
    val emailQualifier = Bytes.toBytes("email")
    val nameFamily = Bytes.toBytes("name")
    val firstNameQualifier = Bytes.toBytes("first")
    val lastNameQualifier = Bytes.toBytes("last")

    // Create a new regex filter
    val emailFilter = new RegexStringComparator(otherArgs(0));
    // Attach the regex filter to a filter
    //   for the email column
    val filter = new SingleColumnValueFilter(
      contactFamily,
      emailQualifier,
      CompareOp.EQUAL,
      emailFilter);

    // Create a scan and set the filter
    val scan = new Scan();
    scan.setFilter(filter);

    // Get the results
    val results = table.getScanner(scan);
    // Iterate over results and print  values
    var it = results.iterator();
    while (it.hasNext()) {
      var result = results.next();
      if (result != null) {
        val id = new String(result.getRow());
        val firstNameObj = result.getValue(nameFamily, firstNameQualifier);
        val firstName = new String(firstNameObj);
        val lastNameObj = result.getValue(nameFamily, lastNameQualifier);
        val lastName = new String(lastNameObj);
        System.out.println("查询结果:     " + firstName + " " + lastName + " - ID: " + id);
        val emailObj = result.getValue(contactFamily, emailQualifier);
        val email = new String(emailObj);
        System.out.println("查询结果:     " + firstName + " " + lastName + " - " + email + " - ID: " + id);
      }
    }

    results.close();
    table.close();
  }

}