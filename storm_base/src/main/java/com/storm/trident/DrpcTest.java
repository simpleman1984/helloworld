package com.storm.trident;

import org.apache.storm.Config;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.DRPCExecutionException;
import org.apache.storm.thrift.TException;
import org.apache.storm.utils.DRPCClient;

public class DrpcTest {

	public static void main(String[] args) throws DRPCExecutionException, AuthorizationException, TException {
		Config conf = new Config();
		conf.setDebug(true);
//		conf.put("storm.thrift.transport","org.apache.storm.security.auth.plain.PlainSaslTransportPlugin");
//		conf.put(Config.STORM_NIMBUS_RETRY_TIMES, 3);
//		conf.put(Config.STORM_NIMBUS_RETRY_INTERVAL, 10);
//		conf.put(Config.STORM_NIMBUS_RETRY_INTERVAL_CEILING, 20);
		
		conf.put("storm.thrift.transport", "org.apache.storm.security.auth.SimpleTransportPlugin");
        conf.put(Config.STORM_NIMBUS_RETRY_TIMES, 3);
        conf.put(Config.STORM_NIMBUS_RETRY_INTERVAL, 10);
        conf.put(Config.STORM_NIMBUS_RETRY_INTERVAL_CEILING, 20);
        conf.put(Config.DRPC_MAX_BUFFER_SIZE, 1048576);
        
		DRPCClient client = new DRPCClient(conf,"127.0.0.1", 3772);
		System.out.println(client.execute("words", "and the jumped and"));
	}

}
