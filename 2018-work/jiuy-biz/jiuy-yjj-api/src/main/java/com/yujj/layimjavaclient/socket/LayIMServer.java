package com.yujj.layimjavaclient.socket;

//import pojo.SocketUser;
//import pojo.message.ToServerTextMessage;
//import socket.sender.MessageSender;
//import socket.manager.OnLineUserManager;
//import util.LayIMFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yujj.layimjavaclient.pojo.SocketUser;
import com.yujj.layimjavaclient.pojo.message.ToServerMessageMine;
import com.yujj.layimjavaclient.pojo.message.ToServerMessageTo;
import com.yujj.layimjavaclient.pojo.message.ToServerTextMessage;
import com.yujj.layimjavaclient.socket.manager.OnLineUserManager;
import com.yujj.layimjavaclient.socket.sender.MessageSender;
import com.yujj.layimjavaclient.util.LayIMFactory;
import com.yujj.web.controller.shop.msg.WeiXinMsgService;

import net.sf.json.JSONObject;

/**
 * Created by pz on 16/11/23.
 */
@ServerEndpoint("/websocket/{uid}")
public class LayIMServer {
	  private static final Logger logger = LoggerFactory.getLogger(LayIMServer.class);

    @OnOpen
    public  void open(Session session, @PathParam("uid") int uid){
        SocketUser user = new SocketUser();
        user.setSession(session);
        user.setUserId(uid);

        //保存在线列表
        LayIMFactory.createManager().addUser(user);
        print("当前在线用户："+LayIMFactory.createManager().getOnlineCount());
        print("缓存中的用户个数："+new OnLineUserManager().getOnLineUsers().size());
    }
    /**
     * 接收消息
     * @param message
     * @param session
     */
    @OnMessage
    public void receiveMessage(String message,Session session){
    	logger.info("接收消息，receiveMessage,message:"+message);
        
        //try {
    		//message = "{\"mine\":{\"username\":\"\",\"avatar\":\"\",\"id\":\"1\",\"mine\":true,\"content\":\"\"},\"to\":{\"username\":\"\",\"id\":\"2\",\"avatar\":\"\",\"sign\":\"没有签名\",\"name\":\"\",\"type\":\"friend\"}}";
            ToServerTextMessage toServerTextMessage = LayIMFactory.createSerializer().toObject(message,ToServerTextMessage.class);
//            ToServerTextMessage toServerTextMessage = new ToServerTextMessage();//LayIMFactory.createSerializer().toObject("",ToServerTextMessage.class);
//    		ToServerMessageMine mine = new ToServerMessageMine();
//    		mine.setId(2);
//    		mine.setUsername("董仲");
//    		mine.setContent("测试消息！！！");
//    		mine.setAvatar("");
//    		toServerTextMessage.setMine(mine);
//    		
//    		ToServerMessageTo to = new ToServerMessageTo();
//    		to.setId(1);
//    		to.setName("赵兴林");
//    		to.setSign("没有签名");
//    		to.setType("friend");
//    		to.setAvatar("");
//    		toServerTextMessage.setTo(to);
             String json = JSONObject.fromObject(toServerTextMessage).toString();
    		
            logger.info("接收消息，receiveMessage,json:"+json);
            //得到接收的对象
            MessageSender sender = new MessageSender();

            sender.sendMessage(toServerTextMessage);
            logger.info("接收消息成功");
            
        //}catch (Exception e){
          //  e.printStackTrace();
        //}
    }

    @OnError
    public void error(Throwable t) {
        print(t.getMessage());
    }

    @OnClose
    public void close(Session session){

        SocketUser user = new SocketUser();
        user.setSession(session);
        user.setUserId(0);
        print("用户掉线");
        //移除该用户
        LayIMFactory.createManager().removeUser(user);
       //print("当前在线用户："+LayIMFactory.createManager().getOnlineCount());

    }

    private void print(String msg){
        System.out.println(msg);
        logger.info(msg);
    }
}
