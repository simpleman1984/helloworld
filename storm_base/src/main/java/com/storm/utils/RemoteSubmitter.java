package com.storm.utils;

import java.util.Map;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.Nimbus.Client;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.shade.org.json.simple.JSONValue;
import org.apache.storm.utils.NimbusClient;
import org.apache.storm.utils.Utils;

public class RemoteSubmitter {
	/**
	 * 远程提交本地任务
	 * @param topologyName
	 * @param topologyConf
	 * @param topology
	 * @param localJar
	 */
	public static void submitLocalTopologyWay1(String topologyName, Config topologyConf, StormTopology topology,
			String localJar) {
		try {
			// get default storm config
			Map defaultStormConf = Utils.readStormConfig();
			defaultStormConf.putAll(topologyConf);

			// set JAR
			System.setProperty("storm.jar", localJar);

			// submit topology
			StormSubmitter.submitTopology(topologyName, defaultStormConf, topology);

		} catch (Exception e) {
			String errorMsg = "can't deploy topology " + topologyName + ", " + e.getMessage();
			System.out.println(errorMsg);
			e.printStackTrace();
		}
	}
	
	public static void submitLocalTopologyWay2(String topologyName, Config topologyConf, 
	        StormTopology topology, String localJar) {
	    try {
	        //get nimbus client
	        Map defaultStormConf = Utils.readStormConfig();
	        defaultStormConf.putAll(topologyConf);
	        Client client = NimbusClient.getConfiguredClient(defaultStormConf).getClient();

	        //upload JAR
	        String remoteJar = StormSubmitter.submitJar(defaultStormConf, localJar);

	        //submit topology
	        client.submitTopology(topologyName, remoteJar, JSONValue.toJSONString(topologyConf), topology);

	    } catch (Exception e) {
	        String errorMsg = "can't deploy topology " + topologyName + ", " + e.getMessage();
	        System.out.println(errorMsg);
	        e.printStackTrace();
	    } 
	}
}
