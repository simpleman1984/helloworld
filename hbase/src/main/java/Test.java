import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class Test
{
    public static Configuration configuration;

    //https://hbase.apache.org/book.html#_data_model_operations
    public static void main(String[] args) throws IOException {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new HttpHandler() {

                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        String uri = exchange.getRequestURI();
                        if("/create".equals(uri))
                        {
                            createTable();
                        }
                        if("/get".equals(uri))
                        {
                            get();
                        }
                        if("/put".equals(uri))
                        {
                            put();
                        }

                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                        exchange.getResponseSender().send("Hello World");
                    }

                })
                .build();
        server.start();

        System.err.println("方法执行完成~~");
        initServer();
    }

    public static final byte[] CF = "name".getBytes();
    public static final byte[] ATTR = "first".getBytes();

    private static Connection connection;
    private static Admin admin;
    private static String _tableName;

    private static void initServer() throws IOException {
        System.setProperty("hadoop.home.dir","D:\\softwaredev\\hbase-1.3.0\\hadoop");

        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "192.168.31.247");
//        config.set("hbase.table.sanity.checks","true");
        //config.addResource(new Path("/core-site.xml"));

        connection = ConnectionFactory.createConnection(config);
        admin = connection.getAdmin();
         _tableName   = "Person";

        //createTable(admin,_tableName);

        //put(connection,admin,_tableName);

//        scan(connection,admin,_tableName);

        //get(connection,admin,_tableName);
    }


    private static void scan(Connection connection,Admin admin,String _tableName) throws IOException {
        Table table = connection.getTable(TableName.valueOf(_tableName));
        Scan scan = new Scan();
        scan.addColumn(CF, ATTR);
        scan.setRowPrefixFilter(Bytes.toBytes("rowkey_"));
        ResultScanner rs = table.getScanner(scan);
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                System.err.println(r.getColumnCells(Bytes.toBytes("name"), Bytes.toBytes("first")));
            }
        } finally {
            rs.close();  // always close the ResultScanner!
        }
        table.close();
    }

    private static void get() throws IOException {
        Table table = connection.getTable(TableName.valueOf(_tableName));
        Get get = new Get(Bytes.toBytes("rowkey_1"));
        Result r = table.get(get);
        byte[] b = r.getValue(CF, ATTR);
        table.close();
        System.err.println(b);
    }

    private static void put() throws IOException {
        Table table = connection.getTable(TableName.valueOf(_tableName));
        Put person = new Put(Bytes.toBytes("rowkey_1"));
        person.addColumn(Bytes.toBytes("name"), Bytes.toBytes("first"), Bytes.toBytes("名字第一个字"));
        person.addColumn(Bytes.toBytes("name"), Bytes.toBytes("last"), Bytes.toBytes("名字第二个字"));
        person.addColumn(Bytes.toBytes("contactinfo"), Bytes.toBytes("email"), Bytes.toBytes("75971994@qq.com"));
        table.put(person);
        table.close();
    }

    private static void createTable() throws IOException {
        TableName tableName = TableName.valueOf(_tableName);
//        admin.disableTable(tableName);
        HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
//        hTableDescriptor.setConfiguration("hbase.table.sanity.checks","false");

        HColumnDescriptor columnFamily1 = new HColumnDescriptor("name");
        columnFamily1.setMaxVersions(3);
        hTableDescriptor.addFamily(columnFamily1);

        HColumnDescriptor columnFamily2 = new HColumnDescriptor("contactinfo");
        columnFamily2.setMaxVersions(3);
        hTableDescriptor.addFamily(columnFamily2);
//        hTableDescriptor.setValue("COPROCESSOR$1",  "C|"
//                + Test.class.getCanonicalName() + "|"
//                + Coprocessor.PRIORITY_USER);
//        admin.modifyTable(tableName, hTableDescriptor);
//        admin.enableTable(tableName);
        admin.createTable(hTableDescriptor);
    }

}