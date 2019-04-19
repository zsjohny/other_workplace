package com.yujj.layimjavaclient.socket;

import org.apache.log4j.chainsaw.Main;

import com.yujj.layimjavaclient.pojo.message.ToServerMessageMine;
import com.yujj.layimjavaclient.pojo.message.ToServerMessageTo;
import com.yujj.layimjavaclient.pojo.message.ToServerTextMessage;
import com.yujj.layimjavaclient.socket.sender.MessageSender;
import com.yujj.layimjavaclient.util.LayIMFactory;

public class A {
	public static void main(String[] args) {
		ToServerTextMessage toServerTextMessage = new ToServerTextMessage();//LayIMFactory.createSerializer().toObject("",ToServerTextMessage.class);
		ToServerMessageMine mine = new ToServerMessageMine();
		mine.setId(2);
		mine.setUsername("董仲");
		mine.setContent("测试消息！！！");
		mine.setAvatar("");
		toServerTextMessage.setMine(mine);
		
		ToServerMessageTo to = new ToServerMessageTo();
		to.setId(1);
		to.setName("赵兴林");
		to.setSign("没有签名");
		to.setType("friend");
		to.setAvatar("");
		toServerTextMessage.setTo(to);

	    //得到接收的对象
	    MessageSender sender = new MessageSender();

	    sender.sendMessage(toServerTextMessage);
	}

}
