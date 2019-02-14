package cn.henu.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class TestActiveMq {

	/**
	 * 点到点的方式发送消息
	 */
	@Test
	public void testQueue() throws Exception{
		//1.创建一个工厂对象,需要指定服务的ip和端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//2.使用工厂对象创建一个connection
		Connection connection = connectionFactory.createConnection();
		//3.开启连接,调用connection的start方法
		connection.start();
		//4.创建一个session,第一个参数是否开启事务，如果开启事务第二个参数没有意义，一般不开启
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//第二个参数为应答模式，手动和自动
		//5.使用session对象创建一个destination对象(目的地),有两种形式，queue和topic
		Queue queue = session.createQueue("testqueue");
		//6.使用session创建一个procedure对象
		MessageProducer producer = session.createProducer(queue);
		//7.创建一个message对象，可以使用TextMessage
		TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello");//表示要发送的消息
		//TextMessage textMessage2 = session.createTextMessage("world");//和上两行作用一样
		//8.发送消息
		producer.send(textMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testQueueRecive() throws Exception{
		//创建一个connectionFactory连接Mq服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用connection对象创建一个session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个destination对象
		Queue queue = session.createQueue("testqueue");//这里接收消息要和上面一致
		//使用session创建一个消费者
		MessageConsumer consumer = session.createConsumer(queue);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印结构
				TextMessage textMessage	=(TextMessage) message;
				String text="";
				try {
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		//等待接收消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopic() throws Exception{
		//1.创建一个工厂对象,需要指定服务的ip和端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//2.使用工厂对象创建一个connection
		Connection connection = connectionFactory.createConnection();
		//3.开启连接,调用connection的start方法
		connection.start();
		//4.创建一个session,第一个参数是否开启事务，如果开启事务第二个参数没有意义，一般不开启
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//第二个参数为应答模式，手动和自动
		//5.使用session对象创建一个destination对象(目的地),有两种形式，queue和topic
		Topic topic = session.createTopic("testTopic");
		//6.使用session创建一个procedure对象
		MessageProducer producer = session.createProducer(topic);
		//7.创建一个message对象，可以使用TextMessage
		TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello");//表示要发送的消息
		//TextMessage textMessage2 = session.createTextMessage("world");//和上两行作用一样
		//8.发送消息
		producer.send(textMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicRecive() throws Exception{
		//创建一个connectionFactory连接Mq服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用connection对象创建一个session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个destination对象
		Topic topic = session.createTopic("testTopic");//这里接收消息要和上面一致
		//使用session创建一个消费者
		MessageConsumer consumer = session.createConsumer(topic);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印结构
				TextMessage textMessage	=(TextMessage) message;
				String text="";
				try {
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		//等待接收消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	
}
