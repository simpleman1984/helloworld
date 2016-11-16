package test.stream;

import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.mqtt.MQTTUtils;

import scala.Tuple2;

public class StreamTest {

	// private static Logger logger = LogManager.getRootLogger();
	transient static Logger logger = org.apache.log4j.LogManager.getLogger("myLogger");

	public static void main(String[] args) {
		logger.setLevel(Level.DEBUG);

		SparkConf conf = new SparkConf().setMaster("spark://192.168.233.128:7077")
				.setJars(new String[] {
						"D:\\workspace\\workspace_bzt_lab\\spark-test\\target\\spark-test-0.0.1-SNAPSHOT-shaded.jar" })
				.setAppName("NetworkWordCount");

//		 SparkConf conf = new
//		 SparkConf().setMaster("local[2]").setAppName("NetworkWordCount");

		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));

		// socket 输入源
		// JavaReceiverInputDStream<String> lines =
		// jssc.socketTextStream("192.168.0.138", 9999);

		JavaReceiverInputDStream<String> lines = MQTTUtils.createStream(jssc, "tcp://192.168.0.138:1883", "test",
				StorageLevel.MEMORY_ONLY());

		JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String x) {
				System.out.println("--------------------------开始拆分句子 ----------------------" + x);
				logger.warn("--------------------------开始拆分句子 ----------------------" + x);
				return Arrays.asList(x.split(" "));
			}
		});

		// Count each word in each batch
		JavaPairDStream<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(String s) {
				return new Tuple2<String, Integer>(s, 1);
			}
		});
		JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer i1, Integer i2) {
				System.err.println("====" + i1 + "===============" + i2 + "===========" + (i1 + i2));
				logger.warn("====" + i1 + "===============" + i2 + "===========" + (i1 + i2));
				return i1 + i2;
			}
		});

		logger.warn("Hello demo11111111111111111111111111111111");

		// Print the first ten elements of each RDD generated in this DStream to
		// the console
		wordCounts.print();

		logger.warn("Hello demo2222222222222222222222222222222");
		
		jssc.start();

		jssc.awaitTermination();
	}

}
