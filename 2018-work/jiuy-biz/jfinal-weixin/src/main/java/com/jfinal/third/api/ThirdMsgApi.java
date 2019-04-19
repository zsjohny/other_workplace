
package com.jfinal.third.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Duang;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.third.util.WXBizMsgCrypt;
import com.jfinal.third.util.weixinpay.WapPayHttpUtil;
import com.jfinal.third.util.weixinpay.WeixinPayCore;

/**
 * 微信 第三方api接口
 * @author L.cm
 *
 */
public class ThirdMsgApi {
	static Log logger = Log.getLog(ThirdMsgApi.class);
	

    
	protected ThirdApi thirdApi = Duang.duang(ThirdApi.class);


 
    
    /**
     * 消息与事件接收
     * 
     * <xml><ToUserName><![CDATA[gh_55a07f02b729]]></ToUserName>
<FromUserName><![CDATA[oPL3EwABb3O9FlyYqrEq8s60XQHM]]></FromUserName>
<CreateTime>1453324217</CreateTime>
<MsgType><![CDATA[text]]></MsgType>
<Content><![CDATA[ 80507050055]]></Content>
<MsgId>6241979982901678088</MsgId>
</xml>
解密后：
{"ToUserName":"gh_55a07f02b729",
"FromUserName":"oPL3EwABb3O9FlyYqrEq8s60XQHM",
"CreateTime":"1453324217",
"MsgType":"text",
"MsgId":"6241979982901678088",
"Content":" 80507050055"}
     * @param APPID
     * @param fromXML
     * @param signature
     * @param timestamp
     * @param nonce
     * @param encrypt_type
     * @param msg_signature
     * @throws Exception
     */
	public String msgCallback(String APPID,String fromXML,String signature,String timestamp,String nonce,String echostr,String openid,String encrypt_type,String msg_signature) throws Exception{
		logger.info("第三方消息接收消息与事件接收callback成功,APPID:{"+APPID+"}, fromXML:{"+fromXML+"},signature:{"+signature
				+"},timestamp:{"+timestamp+"},nonce:{"+nonce+"}，echostr:"+echostr+",openid:"+openid+"encrypt_type:{"+encrypt_type+"}");
		
		if(StringUtils.isEmpty(fromXML)){
    		return null;
    	}
		String third_appId = PropKit.get("third_appId");
    	String third_token = PropKit.get("third_token");
    	String third_encodingAesKey = PropKit.get("third_encodingAesKey");
    	
//		WXBizMsgCrypt pc = new WXBizMsgCrypt(WeiXinService.authorizer1_token, WeiXinService.authorizer1_encodingAesKey, WeiXinService.authorizer1_appid);
		WXBizMsgCrypt pc = new WXBizMsgCrypt(third_token, third_encodingAesKey, third_appId);
		String result = pc.decryptMsg(msg_signature, timestamp, nonce, fromXML);
		logger.info("第三方消息与事件接收callback成功，fromXML解密后明文: "+result);
		Map<String, String> params = WeixinPayCore.decodeXml(result);
		
		String MsgType = params.get("MsgType");//消息类型
		String ToUserName = params.get("ToUserName");//开发者微信号
	    final String FromUserName = params.get("FromUserName");//发送方帐号（一个OpenID）
	    String CreateTime = params.get("CreateTime");//消息创建时间 （整型）
	    logger.info("消息与事件接收callback成功，解析出的属性为：MsgType["+MsgType+"],ToUserName["+ToUserName+"],FromUserName["+FromUserName+"],CreateTime["+CreateTime+"]");
	    //全网发布响应事件
		String returnContent = null;
		if(StringUtils.isEmpty(MsgType)){
			returnContent = null;
		}else if(MsgType.equals("text")){//   文本消息
		    String MsgId = params.get("MsgId");//消息id，64位整型
		    String Content = params.get("Content");//text  文本消息内容
		    
		    //用于全网发布
		    if("TESTCOMPONENT_MSG_TYPE_TEXT".equals(Content)){
		    	returnContent = "TESTCOMPONENT_MSG_TYPE_TEXT_callback";
		    }
		    logger.info("第三方消息与事件接收callback成功,MsgType："+MsgType);
		    
		    //异步发送客服消息
		    if(Content.indexOf("QUERY_AUTH_CODE") != -1){
//            	2、调用发送客服消息api回复文本消息给粉丝content：query_auth_code
		    	//1、文本消息中截取
            	String[] authCodeArr = Content.split(":");
            	final String authCode = authCodeArr[1];//授权码<Content><![CDATA[QUERY_AUTH_CODE:queryauthcode@@@rb4142nz3PBGMei9Z4d5ERh00JOcpuHoBUoOowzlSLlMs8QDnNyuTorBxLit62EJ7-xKQHEKItluU0jpkQlk1g]]></Content>
            	logger.info("第三方消息与事件接收callback成功，authCode["+authCode+"]");
            	//2、使用授权码换取授权信息
            	thirdApi.gotoAuthCallback(authCode,null,null);
            	
            	//3.异步发送客服消息回复
            	new Thread(){
                    public void run(){
                    	   	logger.info("第三方消息与事件接收callback成功，异步调用发送客服消息");
                          	//2.4. 通过客户Api消息接口发送文本消息给粉丝
                    	   	String content = authCode +"_from_api";
                      		send_service_msg(FromUserName,content);
                    }
                 }.start(); 
               //4、直接返回
            	return "";
            }
		    
		    //todo发送文本信息给客服
		    
//		    receiveMessage(Content);
		  //转发门店接口系统处理
		    
		}else if(MsgType.equals("image")){//    图片消息
			logger.info("第三方消息与事件接收callback成功,MsgType："+MsgType);
		}else if(MsgType.equals("voice")){//    语音消息
			logger.info("第三方消息与事件接收callback成功,MsgType："+MsgType);
		}else if(MsgType.equals("video")){//视频消息
			logger.info("第三方消息与事件接收callback成功,MsgType："+MsgType);
		}else if(MsgType.equals("shortvideo")){//小视频消息
			logger.info("第三方消息与事件接收callback成功,MsgType："+MsgType);
		}else if(MsgType.equals("location")){//    地理位置消息
			logger.info("第三方消息与事件接收callback成功,MsgType："+MsgType);
		}else if(MsgType.equals("link")){//     链接消息
			logger.info("第三方消息与事件接收callback成功,MsgType："+MsgType);
		}else if(MsgType.equals("event")){//     链接消息
			String Event = params.get("Event");//消息id，64位整型
			if(Event.equals("subscribe")){//关注/取消关注事件
				logger.info("第三方消息与事件接收callback成功,MsgType："+MsgType+",Event:"+Event);
			}else if(Event.equals("SCAN")){//扫描带参数二维码事件,2用户已关注时的事件推送
				logger.info("第三方消息与事件接收callback成功,MsgType："+MsgType+",Event:"+Event);
			}else if(Event.equals("LOCATION")){//上报地理位置事件y
				logger.info("第三方消息与事件接收callback成功,MsgType："+MsgType+",Event："+Event);
			}else if(Event.equals("CLICK")){//自定义菜单事件-1、点击菜单拉取消息时的事件推送
				logger.info("第三方消息与事件接收callback成功,MsgType："+MsgType+",Event:"+Event);
			}else if(Event.equals("VIEW")){//自定义菜单事件-2、点击菜单跳转链接时的事件推送
				logger.info("第三方消息与事件接收callback成功,MsgType："+MsgType+",Event:"+Event);
			}else{
				logger.info("*************************第三方消息与事件接收callback成功,未识别的Event："+Event+",请排查问题！！！！！");
			}
			// 用于全网发布响应事件
			returnContent = Event+"from_callback";
		}else{
			logger.info("**********************第三方消息与事件接收callback成功,不能识别的消息类型MsgType："+MsgType+"，请排查问题！");
		}
		
		if(StringUtils.isEmpty(returnContent)){
			returnContent = "你好，欢迎光临！！！！";
		}
	   
		String nowTime = String.valueOf(new Date().getTime());
//		String returnNonce = String.valueOf(new Random().nextInt(10000000));
		String returnXmlStr =  "<xml>"+
	    		"<ToUserName><![CDATA["+FromUserName+"]]></ToUserName>"+
	    		"<FromUserName><![CDATA["+ToUserName+"]]></FromUserName>"+
	    		"<CreateTime>"+nowTime+"</CreateTime>"+
	    		"<MsgType><![CDATA[text]]></MsgType>"+
	    		"<Content><![CDATA["+returnContent+"]]></Content>"+
	    "</xml>";
		
		logger.info("消息与事件接收callback成功,加密前: " + returnXmlStr);

    	
		WXBizMsgCrypt wxMC = new WXBizMsgCrypt(third_token, third_encodingAesKey, third_appId);
		String returnXmlStrAfterEncrypt = wxMC.encryptMsg(returnXmlStr, timestamp, nonce);
		logger.info("消息与事件接收callback成功,加密后: " + returnXmlStrAfterEncrypt);

		//		String xmlStr = "<xml>"+
////				"<Encrypt><![CDATA["+returnXmlStrAfterEncrypt+"]]> </Encrypt>"+
////				"<MsgSignature>"+msg_signature+"</MsgSignature>"+
////				"<TimeStamp>"+nowTime+"</TimeStamp>"+
////				"<Nonce>"+returnNonce+"</Nonce>"+
//				
//				"<ToUserName><![CDATA["+FromUserName+"]]></ToUserName>"+
//				"<FromUserName><![CDATA[zhaoXignLin]]></FromUserName>"+
//				"<CreateTime>"+nowTime+"</CreateTime>"+
//				"<MsgType><![CDATA[text]]></MsgType>"+
//				"<Content><![CDATA["+returnContent+"]]></Content>"+
//		"</xml>";
		
		
		
		System.out.println("消息与事件接收callback成功,returnXmlStrAfterEncrypt: " + returnXmlStrAfterEncrypt);
		return returnXmlStrAfterEncrypt;
	}

	/**
	 * 文本消息检查全网发布
	 * @param Content
	 * @param FromUserName
	 */
	public String textCheckRelease(String Content, final String FromUserName) {
		String returnContent = "";
		if("TESTCOMPONENT_MSG_TYPE_TEXT".equals(Content)){
	    	returnContent = "TESTCOMPONENT_MSG_TYPE_TEXT_callback";
	    }
	    //用于全网发布 异步发送客服消息
	    if(Content.indexOf("QUERY_AUTH_CODE") != -1){
//        	2、调用发送客服消息api回复文本消息给粉丝content：query_auth_code
	    	//1、文本消息中截取
        	String[] authCodeArr = Content.split(":");
        	final String authCode = authCodeArr[1];//授权码<Content><![CDATA[QUERY_AUTH_CODE:queryauthcode@@@rb4142nz3PBGMei9Z4d5ERh00JOcpuHoBUoOowzlSLlMs8QDnNyuTorBxLit62EJ7-xKQHEKItluU0jpkQlk1g]]></Content>
        	logger.info("第三方消息与事件接收callback成功，authCode["+authCode+"]");
        	//2、使用授权码换取授权信息
        	thirdApi.gotoAuthCallback(authCode,null,null);
        	
        	//3.异步发送客服消息回复
        	new Thread(){
                public void run(){
                	   	logger.info("第三方消息与事件接收callback成功，异步调用发送客服消息");
                      	//2.4. 通过客户Api消息接口发送文本消息给粉丝
                	   	String content = authCode +"_from_api";
                	   	send_service_msg(FromUserName,content);
                }
             }.start(); 
        }
	    
	    return returnContent;
	}
    


	 /**
     * 发送客服消息
     * 发送文本消息
{
    "touser":"OPENID",
    "msgtype":"text",
    "text":
    {
         "content":"Hello World"
    }
}
     * @return
     */
    public String send_service_msg(String OPENID,String content) {
		logger.info("全网发布时模拟发送客服消息");
		
//    	String authorizer_access_token_groupKey = MemcachedKey.GROUP_KEY_authorizer_access_token;
//		String authorizer_access_token = memcachedService.getStr(authorizer_access_token_groupKey, authorizer_access_token_groupKey + WeiXinService.authorizer2_appid);
		String third_wxatest_authorizer_appId = PropKit.get("third_wxatest_authorizer_appId");
    	
		String authorizer_token = thirdApi.get_authorizer_token(third_wxatest_authorizer_appId);
		logger.info("全网发布时模拟发送客服消息,authorizer_token:"+authorizer_token);
//    	
//    	String OPENID = "";
		//TODO注意这里是账号token，不是三方token
    	String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+authorizer_token;
     	Map<String, Object> paramMap = new HashMap<String, Object>();
     	paramMap.put("touser", OPENID);
     	paramMap.put("msgtype", "text");
     	Map<String, String> textMap = new HashMap<String, String>();
     	textMap.put("content",content);// "测试发送客服消息成功啦"
     	paramMap.put("text", textMap);
     	
    	
    	String param = JSONObject.toJSONString(paramMap);
    	logger.info("全网发布时模拟发送客服消息开始, url:"+url+", param:"+param );
    	
    	Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
    	logger.info("全网发布时模拟发送客服消息,返回结果ret:"+retMap.toString());
    	
    	return null;
	}
    

//  //发送客服消息
//  	public void receiveMessage(String message){//Session session
//  		logger.info("消息与事件接收callback成功，receiveMessage,message:"+message);
//          //try {
//      		//message = "{\"mine\":{\"username\":\"\",\"avatar\":\"\",\"id\":\"1\",\"mine\":true,\"content\":\"\"},\"to\":{\"username\":\"\",\"id\":\"2\",\"avatar\":\"\",\"sign\":\"没有签名\",\"name\":\"\",\"type\":\"friend\"}}";
//              //ToServerTextMessage toServerTextMessage = LayIMFactory.createSerializer().toObject(message,ToServerTextMessage.class);
//              ToServerTextMessage toServerTextMessage = new ToServerTextMessage();//LayIMFactory.createSerializer().toObject("",ToServerTextMessage.class);
//      		ToServerMessageMine mine = new ToServerMessageMine();
//      		mine.setId(2);
//      		mine.setUsername("董仲");
//      		mine.setContent(message);
//      		mine.setAvatar("");
//      		toServerTextMessage.setMine(mine);
//      		
//      		ToServerMessageTo to = new ToServerMessageTo();
//      		to.setId(1);
//      		to.setName("赵兴林");
//      		to.setSign("没有签名");
//      		to.setType("friend");
//      		to.setAvatar("");
//      		toServerTextMessage.setTo(to);
//      	  	String param = JSONObject.fromObject(toServerTextMessage).toString();
//      		logger.info("消息与事件接收callback成功，receiveMessage,toServerTextMessage:"+param);
//              //得到接收的对象
//              MessageSender sender = new MessageSender();
//
//              sender.sendMessage(toServerTextMessage);
//              logger.info("消息与事件接收callback成功， sender.sendMessage(toServerTextMessage)成功！！！");
//              
//          //}catch (Exception e){
//            //  e.printStackTrace();
//          //}
//      }

    
    
}
