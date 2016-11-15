package com.storm.storm_test.mqtt;

import org.apache.storm.mqtt.MqttMessage;
import org.apache.storm.mqtt.MqttTupleMapper;
import org.apache.storm.tuple.ITuple;

public class MyTupleMapper implements MqttTupleMapper {
	
	public MqttMessage toMessage(ITuple tuple) {
		String topic = "users/" + tuple.getStringByField("userId") + "/" + tuple.getStringByField("device");
		byte[] payload = tuple.getStringByField("message").getBytes();
		
		return new MqttMessage(topic, payload);
	}
	
}
