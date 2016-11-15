package com.storm.storm_test.mqtt.bolt;

import org.apache.storm.mqtt.MqttTupleMapper;
import org.apache.storm.mqtt.bolt.MqttBolt;
import org.apache.storm.mqtt.common.MqttOptions;
import org.apache.storm.tuple.Tuple;

public class LogInfoBolt extends MqttBolt{

	public LogInfoBolt(MqttOptions options, MqttTupleMapper mapper) {
		super(options, mapper);
	}

	@Override
	public void execute(Tuple input) {
		System.err.println("   -----------------------topic :" + input.getStringByField("topic") + "-------------------" + input.getStringByField("message"));
	}

	
}
