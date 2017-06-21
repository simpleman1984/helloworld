import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.coprocessor.ColumnInterpreter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

/**
 * Created by xuaihua on 2017/3/25.
 * http://192.168.177.128:50070/explorer.html#/
 * http://192.168.177.128:60010/master-status
 */
public class HbaseSumDemo {
    static{
        System.setProperty("hadoop.home.dir","D:\\softwaredev\\hbase-1.3.0\\hadoop");
    }
    static Configuration hbaseConfiguration = HBaseConfiguration.create();
    static{
//        hbaseConfiguration.addResource("/hbase-site.xml");
        hbaseConfiguration.set("hbase.zookeeper.quorum", "192.168.177.129");
        hbaseConfiguration.set("fs.hdfs.impl","org.apache.hadoop.hdfs.DistributedFileSystem");
    }

    /**
     * 创建表
     * @param tablename 表名
     * @param columnFamily 列族
     * @throws IOException
     * @throws ZooKeeperConnectionException
     * @throws MasterNotRunningException
     */
    public static void CreateTable(String tablename,String columnFamily) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
//        HBaseAdmin admin = new HBaseAdmin(hbaseConfiguration);
        Connection connection = ConnectionFactory.createConnection(hbaseConfiguration);
        Admin admin = connection.getAdmin();
        if(admin.tableExists(TableName.valueOf(tablename))){//如果表已经存在
            System.out.println(tablename+"表已经存在!");
        }else{
            TableName tableName = TableName.valueOf(tablename);
            HTableDescriptor tableDesc = new HTableDescriptor(tableName);
            tableDesc.addCoprocessor("org.apache.hadoop.hbase.coprocessor.AggregateImplementation");
            tableDesc.addFamily(new HColumnDescriptor(columnFamily));
            admin.createTable(tableDesc);
            System.out.println(tablename+"表已经成功创建!");
        }
        admin.close();
        connection.close();
    }

    /**
     * 向表中插入一条新数据
     * @param tableName 表名
     * @param row 行键key
     * @param columnFamily 列族
     * @param column 列名
     * @param data 要插入的数据
     * @throws IOException
     */
    public static void PutData(String tableName,String row,String columnFamily,String column,long data) throws IOException{
        HTable table = new HTable(hbaseConfiguration, tableName);
        Put put = new Put(Bytes.toBytes(row));
        put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(data));
        table.put(put);
        System.out.println("put '"+row+"','"+columnFamily+":"+column+"','"+data+"'");
    }

    /**
     * 获取指定行的所有数据
     * @param tableName 表名
     * @param row 行键key
     * @param columnFamily 列族
     * @param column 列名
     * @throws IOException
     */
    public static void GetData(String tableName,String row,String columnFamily,String column) throws IOException{
        HTable table = new HTable(hbaseConfiguration, tableName);
        // Scan scan = new Scan();
        // ResultScanner  result = table.getScanner(scan);
        Get get = new Get(Bytes.toBytes(row));
//        get.setMaxVersions(5)
        Result result = table.get(get);
        byte[] rb = result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(column));// returns current version of value
//        List<KeyValue> kv = r.getColumn(CF, ATTR);  // returns all versions of this column
        String value = new String(rb,"UTF-8");
        System.out.println("get         "+ value);
    }

    /**
     * 获取指定表的所有数据
	 * @param tableName 表名
	 * @throws IOException
	 */
    public static void ScanAll(String tableName) throws IOException{
        HTable table = new HTable(hbaseConfiguration, tableName);
        Scan scan = new Scan();
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result : resultScanner) {
            List<Cell> cells= result.listCells();
            for (Cell cell : cells) {
                byte[] rb = cell.getValueArray();
                String row = new String(result.getRow(),"UTF-8");
                String family = new String(CellUtil.cloneFamily(cell),"UTF-8");
                String qualifier = new String(CellUtil.cloneQualifier(cell),"UTF-8");
                String value = new String(CellUtil.cloneValue(cell),"UTF-8");
                System.out.println("scan____[row:"+row+"],[family:"+family+"],[qualifier:"+qualifier+"],[value:"+value+"]");
            }
        }
    }

    /**
     * 扫描表格，部分满足条件的数据 <br/>
     * http://hbase.apache.org/book.html#conceptual.view
     * @param tableName
     * @throws IOException
     */
    public static void Scan(String tableName) throws IOException{
        HTable table = new HTable(hbaseConfiguration, tableName);
        Scan scan = new Scan();
        //以下其实为数据的查询条件
//        scan.addColumn("baseinfo".getBytes(),"vio".getBytes());
        scan.setRowPrefixFilter(Bytes.toBytes("row4_"));

        boolean hasResult = false;
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result : resultScanner) {
            List<Cell> cells= result.listCells();
            for (Cell cell : cells) {
                byte[] rb = cell.getValueArray();
                String row = new String(result.getRow(),"UTF-8");
                String family = new String(CellUtil.cloneFamily(cell),"UTF-8");
                String qualifier = new String(CellUtil.cloneQualifier(cell),"UTF-8");
                String value = new String(CellUtil.cloneValue(cell),"UTF-8");
                System.out.println("scanSome____[row:"+row+"],[family:"+family+"],[qualifier:"+qualifier+"],[value:"+value+"]");
            }
            hasResult = true;
        }
        if(!hasResult){
            System.out.println("没有数据！");
        };
    }


    /**
     * 删除指定行数据
     * @param tableName
     * @param row
     * @throws IOException
     */
    public static void Delete(String tableName,String row) throws IOException {
        HTable table = new HTable(hbaseConfiguration, tableName);
        Delete delete = new Delete(row.getBytes());
        table.delete(delete);
        System.out.println("删除行" + row);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            System.setProperty("hadoop.home.dir","D:\\softwaredev\\hbase-1.3.0\\hadoop");

            String tablename = "v";

            HbaseSumDemo.CreateTable(tablename, "basevalue");

            HbaseSumDemo.PutData(tablename, "row4_1", "basevalue", "v1" , 10L);
            HbaseSumDemo.PutData(tablename, "row4_1", "basevalue", "v2" , 10L);
            HbaseSumDemo.PutData(tablename, "row4_1", "basevalue", "v3" , 10L);

            HbaseSumDemo.PutData(tablename, "row4_2", "basevalue", "v1" , 10L);
            HbaseSumDemo.PutData(tablename, "row4_2", "basevalue", "v2" , 10L);
            HbaseSumDemo.PutData(tablename, "row4_2", "basevalue", "v3" , 10L);

            HbaseSumDemo.GetData(tablename, "row4_1","basevalue","v1");

            HbaseSumDemo.ScanAll(tablename);

            HbaseSumDemo.Scan(tablename);

            //求和测试
            AggregationClient c = new AggregationClient(hbaseConfiguration);

            HTable table = new HTable(hbaseConfiguration, tablename);
            Scan scan = new Scan();
            //以下其实为数据的查询条件
            scan.addColumn("basevalue".getBytes(),"v2".getBytes());
//            scan.setRowPrefixFilter(Bytes.toBytes("row4_"));

            DoubleColumnInterpreter interpreter = new DoubleColumnInterpreter();

            double result = c.rowCount(table, interpreter,scan);
            System.out.println("记录条数:" + result);

            result = c.sum(table, interpreter,scan);
            System.out.println("记录合计:" + result);

            result = c.avg(table, interpreter,scan);
            System.out.println("记录平均数:" + result);

//            for(int i=0;i<100000;i++){
//                HbaseDemo.PutData("userinfo", "row4_"+i, "baseinfo", "vio" + i, "驾驶车辆违法信息：" + i);
//            }
//            HbaseDemo.GetData("userinfo", "row4_1","baseinfo","vio1");
//            HbaseDemo.ScanAll("userinfo");
            //HbaseDemo.Scan("userinfo");

            //HbaseDemo.Delete("userinfo","row4_2");
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
         }

    }

}
