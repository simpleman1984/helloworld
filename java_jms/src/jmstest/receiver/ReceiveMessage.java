package jmstest.receiver;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.jms.listener.DefaultMessageListenerContainer;

import jmstest.JmsConnectionFactory;
import jmstest.JmsDestination;

public class ReceiveMessage {
	public void simpleRecv(){
		ConnectionFactory connectionFactory = new JmsConnectionFactory();
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		Destination destination = new JmsDestination();
		
		container.setConnectionFactory(connectionFactory);
		container.setDestination(destination);
		container.setMessageListener(new JmsListener());
	}
	
	public static void main(String [] args)
	{
		ReceiveMessage receiveMessage= new ReceiveMessage();
		receiveMessage.simpleRecv();
	}
	
	class JmsListener implements MessageListener
	{

		@Override
		public void onMessage(Message message) {
			
		}
		
	}
}
