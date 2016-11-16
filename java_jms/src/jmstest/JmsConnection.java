package jmstest;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;

public class JmsConnection implements Connection{

	@Override
	public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
		return new JmsSession();
	}

	@Override
	public String getClientID() throws JMSException {
		return "clientId";
	}

	@Override
	public void setClientID(String clientID) throws JMSException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ConnectionMetaData getMetaData() throws JMSException {
		return null;
	}

	@Override
	public ExceptionListener getExceptionListener() throws JMSException {
		return null;
	}

	@Override
	public void setExceptionListener(ExceptionListener listener) throws JMSException {
	}

	@Override
	public void start() throws JMSException {
	}

	@Override
	public void stop() throws JMSException {
	}

	@Override
	public void close() throws JMSException {
	}

	@Override
	public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector,
			ServerSessionPool sessionPool, int maxMessages) throws JMSException {
		return null;
	}

	@Override
	public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName,
			String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
		return null;
	}

}
