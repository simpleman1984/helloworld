package com.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.redis.bolt.RedisLookupBolt;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.redis.common.mapper.RedisLookupMapper;
import org.apache.storm.topology.TopologyBuilder;

import com.storm.storm_test.bolt.PrintSpout;
import com.storm.storm_test.redis.WordCountRedisLookupMapper;
import com.storm.storm_test.spout.RandomSentenceSpout;

/**
 * redis 主入口<br>
 * http://storm.apache.org/releases/1.0.1/storm-redis.html
 * @author Administrator
 *
 */
public class RedisBoltTest {

	public static void main(String args[]) {
		JedisPoolConfig poolConfig = new JedisPoolConfig.Builder().setHost("127.0.0.1").setPort(6379).build();
		RedisLookupMapper lookupMapper = new WordCountRedisLookupMapper();
		RedisLookupBolt lookupBolt = new RedisLookupBolt(poolConfig, lookupMapper);
		
		TopologyBuilder builder = new TopologyBuilder();
		
		RandomSentenceSpout spout = new RandomSentenceSpout();
		builder.setSpout("spout", spout, 5);
		
		builder.setBolt("word", lookupBolt, 8).shuffleGrouping("spout");
		
		PrintSpout printSpout = new PrintSpout();
		builder.setBolt("print", printSpout, 8).shuffleGrouping("word");
		
		Config conf = new Config();
		conf.setNumWorkers(20);
		conf.setMaxSpoutPending(5000);
		
//		StormSubmitter.submitTopology("mytopology", conf, topology);
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("mytopology", conf, builder.createTopology());
//		cluster.shutdown();
		
//		JedisPoolConfig poolConfig = new JedisPoolConfig.Builder().setHost(host).setPort(port).build();
//		RedisStoreMapper storeMapper = new WordCountStoreMapper();
//		RedisStoreBolt storeBolt = new RedisStoreBolt(poolConfig, storeMapper);
	}
	
}