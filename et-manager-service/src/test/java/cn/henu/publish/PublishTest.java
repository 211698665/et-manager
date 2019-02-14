package cn.henu.publish;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PublishTest {

	//测试服务的发布其实和tomcat没有关系
	//tomcat在这里的作用是初始化一个spring容器,可以不启动et-manager，通过该测试方法和et-manager-web来实现正常访问
	/*
	@Test
	public void publishService() throws Exception{
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		//方法一：
		 // while(true) {
			//Thread.sleep(1000);
		//}
		System.out.println("服务已经启动");
		System.in.read();
		System.out.println("服务关闭");
		
	}
*/
}
