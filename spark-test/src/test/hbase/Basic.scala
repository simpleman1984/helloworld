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

object Basic {

  def main(args: Array[String]) {
    //https://www.mapr.com/developercentral/code/loading-hbase-tables-spark#.WGt5aPmEC0E
    //http://hbase.apache.org/
    //https://docs.microsoft.com/en-us/azure/hdinsight/hdinsight-hbase-build-java-maven-linux

    System.setProperty("hadoop.home.dir", "D:/workspace/git_lab/spark-test/hadoop");

    val sparkConf = new SparkConf().setAppName("MovieLensALS").setMaster("local[5]")
    val sc = new SparkContext(sparkConf)

    val config = HBaseConfiguration.create()
    config.addResource(new Path("/src/test/hbase/core-site.xml"));

    //hbase客户端示例
    //    val admin = new HBaseAdmin(config)

    //    val tableName = "test"
    //    val columnDesc = new HColumnDescriptor("cf1");
    //    admin.disableTable(Bytes.toBytes(tableName));
    //    admin.addColumn(tableName, columnDesc);
    //    admin.enableTable(Bytes.toBytes(tableName));

    //    val tables = admin.listTables();
    //    tables.foreach(println)

    // 以下代码示例为插入新的数据
    // create the table...
    //     val tableDescriptor = new HTableDescriptor(TableName.valueOf("people"));
    //     // ... with two column families
    //     tableDescriptor.addFamily(new HColumnDescriptor("name"));
    //     tableDescriptor.addFamily(new HColumnDescriptor("contactinfo"));
    //     admin.createTable(tableDescriptor);
    //
    //     // define some people
    //     val peoples = List(
    //         Array( "1", "Marcel", "Haddad", "marcel@fabrikam.com"),
    //         Array( "2", "Franklin", "Holtz", "franklin@contoso.com" ),
    //         Array( "3", "Dwayne", "McKee", "dwayne@fabrikam.com" ),
    //         Array( "4", "Rae", "Schroeder", "rae@contoso.com" ),
    //         Array( "5", "Rosalie", "burton", "rosalie@fabrikam.com"),
    //         Array( "6", "Gabriela", "Ingram", "gabriela@contoso.com") );
    //
    //     val table = new HTable(config, "people");

    // Add each person to the table
    //   Use the `name` column family for the name
    //   Use the `contactinfo` column family for the email
    //     peoples.foreach{(_people) =>
    //       val person = new Put(Bytes.toBytes(_people(0)))
    //       person.add(Bytes.toBytes("name"), Bytes.toBytes("first"), Bytes.toBytes(_people(1)))
    //       person.add(Bytes.toBytes("name"), Bytes.toBytes("last"), Bytes.toBytes(_people(2)))
    //       person.add(Bytes.toBytes("contactinfo"), Bytes.toBytes("email"), Bytes.toBytes(_people(3)))
    //       table.put(person);
    //     }
    //     // flush commits and close the table
    //     table.flushCommits();
    //     table.close();
    //     
    // Use GenericOptionsParser to get only the parameters to the class
    // and not all the parameters passed (when using WebHCat for example)
    var _args = Array("fabrikam");
    val otherArgs = new GenericOptionsParser(config, _args).getRemainingArgs();
    if (otherArgs.length != 1) {
      System.out.println("usage: [regular expression]");
      System.exit(-1);
    }

    // Open the table
    //    val table = new HTable(config, "people");

    // Define the family and qualifiers to be used
    //    val contactFamily = Bytes.toBytes("contactinfo")
    //    val emailQualifier = Bytes.toBytes("email")
    //    val nameFamily = Bytes.toBytes("name")
    //    val firstNameQualifier = Bytes.toBytes("first")
    //    val lastNameQualifier = Bytes.toBytes("last")

    // Create a new regex filter
    //    val emailFilter = new RegexStringComparator(otherArgs(0));
    // Attach the regex filter to a filter
    //   for the email column
    //    val filter = new SingleColumnValueFilter(
    //      contactFamily,
    //      emailQualifier,
    //      CompareOp.EQUAL,
    //      emailFilter);

    // Create a scan and set the filter
    //    val scan = new Scan();
    //    scan.setFilter(filter);

    // Get the results
    //    val results = table.getScanner(scan);
    //    // Iterate over results and print  values
    //    var it = results.iterator();
    //    while (it.hasNext()) {
    //      var result = results.next();
    //      val id = new String(result.getRow());
    //      val firstNameObj = result.getValue(nameFamily, firstNameQualifier);
    //      val firstName = new String(firstNameObj);
    //      val lastNameObj = result.getValue(nameFamily, lastNameQualifier);
    //      val lastName = new String(lastNameObj);
    //      System.out.println(firstName + " " + lastName + " - ID: " + id);
    //      val emailObj = result.getValue(contactFamily, emailQualifier);
    //      val email = new String(emailObj);
    //      System.out.println(firstName + " " + lastName + " - " + email + " - ID: " + id);
    //    }
    //
    //    results.close();
    //    table.close();
  }

}