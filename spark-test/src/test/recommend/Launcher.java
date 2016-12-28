package test.recommend;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;

public class Launcher {
	transient static Logger logger = org.apache.log4j.LogManager.getLogger("myLogger");

	public static void main(String[] args) throws IOException {
		logger.setLevel(Level.DEBUG);
		
		SparkConf sparkConf = new SparkConf().setMaster("local[5]").setAppName("Simple Application");
		SparkContext sc = new SparkContext(sparkConf);


	}
}
