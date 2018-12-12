package org.dream.utils.jms;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Boyce
 * 2016年6月21日 下午1:31:45 
 */
public class TestJMS implements Listener{
	
public static void main(String[] args) {
	ApplicationContext ctx=new ClassPathXmlApplicationContext("classpath:/app*.xml");
	JmsSender sender=(JmsSender) ctx.getBean("testSender");
	sender.sendMessage("text");
	while(true){}
}

@Override
public void onMessage(String message) {
		System.out.println(message);
}
}
