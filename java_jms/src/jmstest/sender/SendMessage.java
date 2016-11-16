package jmstest.sender;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import jmstest.HttpJmsTemplate;
import jmstest.JmsConnectionFactory;
import jmstest.JmsDestination;
import jmstest.JmsMessageCreator;

public class SendMessage {
	
	public void simpleSend(){
		ConnectionFactory connectionFactory = new JmsConnectionFactory();
		JmsTemplate jmsTemplate = new HttpJmsTemplate(connectionFactory);
		Destination destination = new JmsDestination();
		MessageCreator messageCreator = new JmsMessageCreator();
		
		jmsTemplate.send(destination, messageCreator);
		jmsTemplate.sendAndReceive(destination, messageCreator);
	}
	
	public static void main(String [] args)
	{
		SendMessage sendMessage= new SendMessage();
		sendMessage.simpleSend();
	}
}
