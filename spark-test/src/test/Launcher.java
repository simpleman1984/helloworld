package test;

import java.io.IOException;

import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;

public class Launcher {

	public static void main(String[] args) throws IOException {
		SparkAppHandle handle = new SparkLauncher()
		         .setAppResource("D:/workspace/workspace_bzt_lab/spark-test/target/spark-test-0.0.1-SNAPSHOT.jar")
		         .setMainClass("test.SimpleApp")
		         .setMaster("local")
		         .setConf(SparkLauncher.DRIVER_MEMORY, "2g")
		         .startApplication();
	}

}
