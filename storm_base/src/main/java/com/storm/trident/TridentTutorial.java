package com.storm.trident;

import java.util.Arrays;

import org.apache.storm.Config;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.operation.builtin.Count;
import org.apache.storm.trident.operation.builtin.FilterNull;
import org.apache.storm.trident.operation.builtin.MapGet;
import org.apache.storm.trident.operation.builtin.Sum;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.testing.MemoryMapState;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.DRPCClient;

import com.storm.utils.RemoteSubmitter;

public class TridentTutorial {

	public static class Split extends BaseFunction {
		   public void execute(TridentTuple tuple, TridentCollector collector) {
		       String sentence = tuple.getString(0);
		       System.err.println("=====================拆分语句:" + sentence);
		       for(String word: sentence.split(" ")) {
		           collector.emit(new Values(word));                
		       }
		   }
		}
	
	public static void main(String[] args) {
		//========================================================================拷贝其他地方的
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 3,
	               new Values("the cow jumped over the moon"),
	               new Values("the man went to the store and bought some candy"),
	               new Values("four score and seven years ago"),
	               new Values("how many apples can you eat"));
		spout.setCycle(false);
	
		TridentTopology topology = new TridentTopology();
		
		TridentState wordCounts =
		     topology.newStream("spout1", spout)
		       .each(new Fields("sentence"), new Split(), new Fields("word"))
		       .groupBy(new Fields("word"))
		       .persistentAggregate(new MemoryMapState.Factory(), new Count(), new Fields("count"))                
		       .parallelismHint(6);
		
		topology.newDRPCStream("words")
	       .each(new Fields("args"), new Split(), new Fields("word"))
	       .groupBy(new Fields("word"))
	       .stateQuery(wordCounts, new Fields("word"), new MapGet(), new Fields("count"))
	       .each(new Fields("count"), new FilterNull())
	       .project(new Fields("word","count"));
	       //.aggregate(new Fields("count"), new Sum(), new Fields("sum"));
		
		//以上的默认功能为查询当前语句中的单词，有多少个是在已经计算过的仓库中！！！！！！！！！！！！
		
		//目前的功能为将输入的语句，按照存在的单词进行一个简单的汇总，输出每个单词以及该单词出现的次数！！！！！！！！！！！！
		
//========================================================================拷贝其他地方的
		
		Config conf = new Config();
		conf.setNumWorkers(20);
		conf.setMaxSpoutPending(5000);
		
		Config config = new Config();
	    config.put(Config.NIMBUS_HOST,"127.0.0.1");   
	    config.put(Config.NIMBUS_THRIFT_PORT, 6627);
	    config.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList("127.0.0.1")); 
	    config.put(Config.STORM_ZOOKEEPER_PORT,2181);
	    config.put(Config.DRPC_SERVERS, Arrays.asList("127.0.0.1"));

	    config.put(Config.TOPOLOGY_WORKERS, 3);

	    RemoteSubmitter.submitLocalTopologyWay1("word-count", config, 
	    		topology.build(), // your topology
	            "E:\\workspace-lab\\storm-test\\target\\storm-test-0.0.1-SNAPSHOT.jar");//the JAR file
	}

}
