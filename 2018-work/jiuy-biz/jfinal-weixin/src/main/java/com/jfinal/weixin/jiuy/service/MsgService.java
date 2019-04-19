package com.jfinal.weixin.jiuy.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.third.util.weixinpay.WapPayHttpUtil;
import com.jfinal.weixin.demo.WeixinMsgController;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InShortVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import com.jfinal.wxaapp.msg.bean.WxaImageMsg;
import com.jfinal.wxaapp.msg.bean.WxaTextMsg;
import com.jfinal.wxaapp.msg.bean.WxaUserEnterSessionMsg;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;
/**
 * 玖远门店版相关服务接口（http协议进行对接）
 * @author Administrator
 *
 */
public class MsgService {

	  
    static Log logger = Log.getLog(MsgService.class);
    static final String version = "1.0.0"; 
    
    /**
     * 接收小程序文本消息
     * @param textMsg
     */
    public static String processTextMsg(WxaTextMsg textMsg) {
    	Map<String, String> map = new HashMap<String, String>();
	 	map.put("toUserName", textMsg.getToUserName());
	 	map.put("fromUserName", textMsg.getFromUserName());
	 	map.put("createTime", String.valueOf(textMsg.getCreateTime()));
	 	map.put("msgType", textMsg.getMsgType());
	 	map.put("content", textMsg.getContent());
	 	map.put("msgId", String.valueOf(textMsg.getMsgId()));
	 	Map<String, String> headers = new HashMap<String, String>();
		String sign = ParamSignUtils.getSign(map);
	 	headers.put("sign", sign);
	 	headers.put("version", version);
	 	Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.processInTextMsgUrl).headers(headers).params(map).text();
	 	String body = resp.getBody();
//    	String param = JSONObject.toJSONString(map);
//    	String body = WapPayHttpUtil.sendPostHttp(PropKit.get("wxa_api_url")+ApiManager.processInTextMsgUrl, param);
//    	String body = HttpUtils.get(PropKit.get("wxa_api_url")+ApiManager.processInTextMsgUrl, map);
	 	logger.info("body:"+body);
	 	return resolverReturnMsg(body);
	}
    
   
    
    
    /**
     * 解析出 returnMsg 
     * 例子JSON："{\"successful\":true,\"error\":null,\"code\":0,\"data\":{ \"returnMsg\":\"测试\"},\"html\":null}";
     * @param ret
     * @return
     */
	private static String resolverReturnMsg(String ret) {
		if(StringUtils.isEmpty(ret)){
			logger.info("发送消息小程序返回信息为空，请排查问题ret："+ret);
			return null;
		}
		JSONObject retJSON = JSONObject.parseObject(ret);
		JSONObject data = retJSON.getJSONObject("data");
		String returnMsg = data.getString("returnMsg");
		return returnMsg;
	}
	public static void main(String[] args) {
		String ret = "{\"successful\":true,\"error\":null,\"code\":0,\"data\":{ \"returnMsg\":\"测试\"},\"html\":null}";
		logger.info("returnMsg:"+resolverReturnMsg(ret));
	}
    /**
	 * 接收小程序图片消息
	 * @param inImageMsg
	 */
	public static String processImageMsg(WxaImageMsg imageMsg) {
		Map<String, String> map = new HashMap<String, String>();
	 	map.put("toUserName", imageMsg.getToUserName());
	 	map.put("fromUserName", imageMsg.getFromUserName());
	 	map.put("createTime", String.valueOf(imageMsg.getCreateTime()));
	 	map.put("msgType", imageMsg.getMsgType());
	 	map.put("picUrl", imageMsg.getPicUrl());
	 	map.put("mediaId", imageMsg.getMediaId());
	 	map.put("msgId", String.valueOf(imageMsg.getMsgId()));
	 	Map<String, String> headers = new HashMap<String, String>();
		String sign = ParamSignUtils.getSign(map);
	 	headers.put("sign", sign);
	 	headers.put("version", version);
	 	Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.processInImageMsgUrl).headers(headers).params(map).text();
	 	String body = resp.getBody();
//    	String param = JSONObject.toJSONString(map);
//    	String body = WapPayHttpUtil.sendPostHttp(PropKit.get("wxa_api_url")+ApiManager.processInImageMsgUrl, param);
	 	logger.info("body:"+body);
	 	
	 	return resolverReturnMsg(body);
	}
	
	
	
	 /**
     * 处理接收到的进入会话事件（会员进入客服会话触发）
     * @param userEnterSessionMsg 处理接收到的进入会话事件
     * 
<xml><ToUserName><![CDATA[gh_e714f01ae6ad]]></ToUserName>
<FromUserName><![CDATA[o2Kn-0GB5SOFo4bLtN5v9lO8nStQ]]></FromUserName>
<CreateTime>1498886986</CreateTime>
<MsgType><![CDATA[event]]></MsgType>
<Event><![CDATA[user_enter_tempsession]]></Event>
<SessionFrom><![CDATA[weapp]]></SessionFrom>
</xml>
     */
	public static void processUserEnterSessionMsg(WxaUserEnterSessionMsg userEnterSessionMsg) {
		logger.info("处理接收到的进入会话事件（会员进入客服会话触发）userEnterSessionMsg:"+JSON.toJSONString(userEnterSessionMsg));
		Map<String, String> params = new HashMap<String, String>();
		params.put("toUserName", userEnterSessionMsg.getToUserName());
		params.put("fromUserName", userEnterSessionMsg.getFromUserName());
		params.put("createTime", String.valueOf(userEnterSessionMsg.getCreateTime()));
		params.put("msgType", userEnterSessionMsg.getMsgType());
		params.put("event", userEnterSessionMsg.getEvent());
		params.put("sessionFrom", userEnterSessionMsg.getSessionFrom());
	 	Map<String, String> headers = new HashMap<String, String>();
	 	String sign = ParamSignUtils.getSign(params);
	 	headers.put("sign", sign);
	 	headers.put("version", version);
	 	String url = PropKit.get("wxa_api_url")+ApiManager.processUserEnterSessionMsgUrl;
	 	
	 	logger.info("处理接收到的进入会话事件（会员进入客服会话触发）url："+url+",headers"+JSON.toJSONString(headers)+",map："+JSON.toJSONString(params));
	 	Response<String> resp = Requests.get(url).headers(headers).params(params).text();
	 	String body = resp.getBody();
//    	String param = JSONObject.toJSONString(map);
//    	String body = WapPayHttpUtil.sendPostHttp(PropKit.get("wxa_api_url")+ApiManager.processInImageMsgUrl, param);
	 	logger.info("处理接收到的进入会话事件（会员进入客服会话触发）body:"+body);
	}
	
	
	
	
    
 	/**
 	 *  接收文本消息
 	 */
	 public static void processInTextMsg(InTextMsg inTextMsg) {
		Map<String, String> map = new HashMap<String, String>();
	 	map.put("toUserName", inTextMsg.getToUserName());
	 	map.put("fromUserName", inTextMsg.getFromUserName());
	 	map.put("createTime", String.valueOf(inTextMsg.getCreateTime()));
	 	map.put("msgType", inTextMsg.getMsgType());
	 	map.put("content", inTextMsg.getContent());
	 	map.put("msgId", inTextMsg.getMsgId());
	 	Map<String, String> headers = new HashMap<String, String>();
		String sign = ParamSignUtils.getSign(map);
	 	headers.put("sign", sign);
	 	headers.put("version", version);
	 	Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.processInTextMsgUrl).headers(headers).params(map).text();
	 	String body = resp.getBody();
	 	
//	 	String param = JSONObject.toJSONString(map);
//    	String body = WapPayHttpUtil.sendPostHttp(PropKit.get("wxa_api_url")+ApiManager.processInTextMsgUrl, param);
	 	logger.info("body:"+body);
    }


	 /**
	 	 *    接收链接消息
	 	 * 
	 	 */
	public static void processInLinkMsg(InLinkMsg inLinkMsg) {
		Map<String, String> map = new HashMap<String, String>();
	 	map.put("toUserName", inLinkMsg.getToUserName());
	 	map.put("fromUserName", inLinkMsg.getFromUserName());
	 	map.put("createTime", String.valueOf(inLinkMsg.getCreateTime()));
	 	map.put("msgType", inLinkMsg.getMsgType());
	 	map.put("title", inLinkMsg.getTitle());
	 	map.put("description", inLinkMsg.getDescription());
	 	map.put("url", inLinkMsg.getUrl());
	 	map.put("msgId", inLinkMsg.getMsgId());
	 	Map<String, String> headers = new HashMap<String, String>();
		String sign = ParamSignUtils.getSign(map);
	 	headers.put("sign", sign);
	 	headers.put("version", version);
	 	Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.processInLinkMsgUrl).headers(headers).params(map).text();
	 	String body = resp.getBody();
//	 	String param = JSONObject.toJSONString(map);
//    	String body = WapPayHttpUtil.sendPostHttp(PropKit.get("wxa_api_url")+ApiManager.processInLinkMsgUrl, param);
	 	logger.info("body:"+body);
	}
	

	/**
	 * 接收图片消息
	 * @param inImageMsg
	 */
	public static void processInImageMsg(InImageMsg inImageMsg) {
		Map<String, String> map = new HashMap<String, String>();
	 	map.put("toUserName", inImageMsg.getToUserName());
	 	map.put("fromUserName", inImageMsg.getFromUserName());
	 	map.put("createTime", String.valueOf(inImageMsg.getCreateTime()));
	 	map.put("msgType", inImageMsg.getMsgType());
	 	map.put("picUrl", inImageMsg.getPicUrl());
	 	map.put("mediaId", inImageMsg.getMediaId());
	 	map.put("msgId", inImageMsg.getMsgId());
	 	Map<String, String> headers = new HashMap<String, String>();
		String sign = ParamSignUtils.getSign(map);
	 	headers.put("sign", sign);
	 	headers.put("version", version);
	 	Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.processInImageMsgUrl).headers(headers).params(map).text();
	 	String body = resp.getBody();
//	 	String param = JSONObject.toJSONString(map);
//    	String body = WapPayHttpUtil.sendPostHttp(PropKit.get("wxa_api_url")+ApiManager.processInImageMsgUrl, param);
	 	logger.info("body:"+body);
	}

	/**
	 *  接收语音消息
	 * @param inVoiceMsg
	 */
	public static void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
		Map<String, String> map = new HashMap<String, String>();
	 	map.put("toUserName", inVoiceMsg.getToUserName());
	 	map.put("fromUserName", inVoiceMsg.getFromUserName());
	 	map.put("createTime", String.valueOf(inVoiceMsg.getCreateTime()));
	 	map.put("mediaId", inVoiceMsg.getMediaId());
	 	map.put("format", inVoiceMsg.getFormat());
	 	map.put("msgId", inVoiceMsg.getMsgId());
	 	Map<String, String> headers = new HashMap<String, String>();
		String sign = ParamSignUtils.getSign(map);
	 	headers.put("sign", sign);
	 	headers.put("version", version);
	 	Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.processInVoiceMsgUrl).headers(headers).params(map).text();
	 	String body = resp.getBody();
//	 	String param = JSONObject.toJSONString(map);
//    	String body = WapPayHttpUtil.sendPostHttp(PropKit.get("wxa_api_url")+ApiManager.processInVoiceMsgUrl, param);
	 	logger.info("body:"+body);
	}

	/**
	 * 接收视频消息
	 * @param inVideoMsg
	 */
	public static void processInVideoMsg(InVideoMsg inVideoMsg) {
		Map<String, String> map = new HashMap<String, String>();
	 	map.put("toUserName", inVideoMsg.getToUserName());
	 	map.put("fromUserName", inVideoMsg.getFromUserName());
	 	map.put("createTime", String.valueOf(inVideoMsg.getCreateTime()));
	 	
	 	map.put("mediaId", inVideoMsg.getMediaId());
	 	map.put("thumbMediaId", inVideoMsg.getThumbMediaId());
	 	
	 	map.put("msgId", inVideoMsg.getMsgId());
	 	
	 	Map<String, String> headers = new HashMap<String, String>();
		String sign = ParamSignUtils.getSign(map);
	 	headers.put("sign", sign);
	 	headers.put("version", version);
	 	Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.processInVideoMsgUrl).headers(headers).params(map).text();
	 	String body = resp.getBody();
//	 	String param = JSONObject.toJSONString(map);
//    	String body = WapPayHttpUtil.sendPostHttp(PropKit.get("wxa_api_url")+ApiManager.processInVideoMsgUrl, param);
	 	logger.info("body:"+body);
	}
	/**
	 * 接收小视频消息
	 * @param inShortVideoMsg
	 */
	public static void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg) {
		Map<String, String> map = new HashMap<String, String>();
	 	map.put("toUserName", inShortVideoMsg.getToUserName());
	 	map.put("fromUserName", inShortVideoMsg.getFromUserName());
	 	map.put("createTime", String.valueOf(inShortVideoMsg.getCreateTime()));
		map.put("mediaId", inShortVideoMsg.getMediaId());
	 	map.put("thumbMediaId", inShortVideoMsg.getThumbMediaId());
	 	map.put("msgId", inShortVideoMsg.getMsgId());
	 	Map<String, String> headers = new HashMap<String, String>();
		String sign = ParamSignUtils.getSign(map);
	 	headers.put("sign", sign);
	 	headers.put("version", version);
	 	Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.processInShortVideoMsgUrl).headers(headers).params(map).text();
	 	String body = resp.getBody();
//	 	String param = JSONObject.toJSONString(map);
//    	String body = WapPayHttpUtil.sendPostHttp(PropKit.get("wxa_api_url")+ApiManager.processInShortVideoMsgUrl, param);
	 	logger.info("body:"+body);
	}



	 
    

	


	


	

}
