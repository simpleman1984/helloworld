package jmstest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

public class JmsMessageCreator implements MessageCreator {

	@Override
	public Message createMessage(Session session) throws JMSException {
		return new JmsMessage();
	}

}
