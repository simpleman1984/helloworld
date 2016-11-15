package com.storm.storm_test.mqtt;

import org.apache.storm.mqtt.MqttMessage;
import org.apache.storm.mqtt.MqttMessageMapper;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomMessageMapper implements MqttMessageMapper {
	private static final Logger LOG = LoggerFactory.getLogger(CustomMessageMapper.class);

	public Values toValues(MqttMessage message) {
        return new Values(message.getTopic(), new String(message.getMessage()));
    }

	public Fields outputFields() {
		return new Fields("topic", "message");
	}
	
}