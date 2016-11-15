package com.storm;

import java.util.Arrays;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.mqtt.bolt.MqttBolt;
import org.apache.storm.mqtt.common.MqttOptions;
import org.apache.storm.mqtt.mappers.StringMessageMapper;
import org.apache.storm.mqtt.spout.MqttSpout;
import org.apache.storm.topology.TopologyBuilder;

import com.storm.storm_test.mqtt.CustomMessageMapper;
import com.storm.storm_test.mqtt.MyTupleMapper;
import com.storm.storm_test.mqtt.bolt.LogInfoBolt;

public class MqttBoltTest {

	public static void main(String[] args) {
		TopologyBuilder builder = new TopologyBuilder();
		MqttOptions options = new MqttOptions();
		options.setTopics(Arrays.asList("test"));
		options.setCleanConnection(false);

		MqttSpout spout = new MqttSpout(new CustomMessageMapper(), options);

		MyTupleMapper tupleMapper = new MyTupleMapper();
		MqttBolt bolt = new LogInfoBolt(options, tupleMapper);

		builder.setSpout("mqtt-spout", spout);
		builder.setBolt("log-bolt", bolt).shuffleGrouping("mqtt-spout");

		Config conf = new Config();
		conf.setNumWorkers(20);
		conf.setMaxSpoutPending(5000);
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("mytopology", conf, builder.createTopology());
	}

}
