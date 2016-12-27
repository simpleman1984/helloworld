package test;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

public class SimpleApp {
	transient static Logger logger = org.apache.log4j.LogManager.getLogger("myLogger");
	
	public static void main(String[] args) {
		
		System.setProperty("hadoop.home.dir", "D:\\workspace\\workspace_bzt_lab\\spark-test\\hadoop");
		
		String logFile = "assets/README.md"; // Should be some file on your system
		//本地调试模式，运行Spark程序
		SparkConf conf = new SparkConf().setMaster("local").setAppName("Simple Application");
		
		//远程提交任务到服务器
//		SparkConf conf = new SparkConf().setMaster("spark://192.168.233.128:7077")
//				.setJars(new String[]{"D:\\workspace\\workspace_bzt_lab\\spark-test\\target\\spark-test-0.0.1-SNAPSHOT-shaded.jar"})
//				.setAppName("Simple Application");
		
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		JavaRDD<String> logData = sc.textFile(logFile).cache();

		long numAs = logData.filter(new Function<String, Boolean>() {
			public Boolean call(String s) {
				logger.warn("ddd111111111111111111111111111111111111" + s);
				return s.contains("a");
			}
		}).count();

		long numBs = logData.filter(new Function<String, Boolean>() {
			public Boolean call(String s) {
				logger.warn("eeee2222222222222222222222222222222222222" + s);
				return s.contains("b");
			}
		}).count();

		System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
	}
}
