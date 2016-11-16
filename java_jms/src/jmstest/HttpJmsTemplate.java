package jmstest;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class HttpJmsTemplate extends JmsTemplate {
	
	public HttpJmsTemplate(ConnectionFactory connectionFactory){
		super(connectionFactory);
	}
	
	/**
	 * 覆盖默认的jmsTemplate方法，修改为通过http，同步请求
	 */
	protected Message doSendAndReceive(Session session, Destination destination, MessageCreator messageCreator)
			throws JMSException {
		//根据destination获取到对应的http请求地址，然后将message发送过去，同时获取对应的消息内容
		return new JmsMessage();
	}
	
}
