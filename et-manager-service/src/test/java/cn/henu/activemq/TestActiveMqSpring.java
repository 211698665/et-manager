package cn.henu.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestActiveMqSpring {

	@Test
	public void sendMessage() throws Exception{
		//1.初始化spring 容器
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//2.从容器中或者jmsTemplate
		JmsTemplate template = context.getBean(JmsTemplate.class);
		//3.从容器中获取destination,可以根据bean中的ID来取
		Destination destination = (Destination) context.getBean("queueDestination");
		//4.发送消息
		template.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("hhhhmq");
			}
		});
	}
}
