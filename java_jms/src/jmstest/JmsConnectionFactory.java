package jmstest;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

public class JmsConnectionFactory implements ConnectionFactory {

	@Override
	public Connection createConnection() throws JMSException {
		return new JmsConnection();
	}

	@Override
	public Connection createConnection(String arg0, String arg1) throws JMSException {
		return new JmsConnection();
	}

}
