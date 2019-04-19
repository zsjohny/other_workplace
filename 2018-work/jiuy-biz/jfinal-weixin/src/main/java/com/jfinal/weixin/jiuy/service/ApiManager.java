package com.jfinal.weixin.jiuy.service;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.demo.WeixinMsgController;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InShortVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import com.jfinal.wxaapp.msg.bean.WxaImageMsg;
import com.jfinal.wxaapp.msg.bean.WxaTextMsg;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;
/**
 * 外部接口管理
 * @author Administrator
 *
 */
public class ApiManager {
	  	static Log logger = Log.getLog(MemberService.class);
	  	
	  	public static final String processUserEnterSessionMsgUrl =  "/wxa/processUserEnterSessionMsg.do";
	  	public static final String processInTextMsgUrl =  "/wxa/processInTextMsg.do";
	  	public static final String processInLinkMsgUrl =  "/wxa/processInLinkMsg.do";
	  	public static final String processInImageMsgUrl = "/wxa/processInImageMsg.do";
	  	
	  	public static final String processInVoiceMsgUrl =  "/wxa/processInVoiceMsg.do";
	  	public static final String processInVideoMsgUrl =  "/wxa/processInVideoMsg.do";
	  	public static final String processInShortVideoMsgUrl =  "/wxa/processInShortVideoMsg.do";
	  
	  	public static final String authorizUrl =  "/wxa/authoriz.do";
	  	public static final String loginUrl =  "/wxa/login.do";
	  	
	  	public static final String wxaAuthNotificationUrl =  "/wxa/wxaAuthNotification.do";
	  	public static final String wxaAuthUrl =  "/wxa/wxaAuth.do";
	  	
	    
	  	public static final String getRefreshTokenUrl =  "/wxa/getRefreshToken.do";
	  	public static final String setRefreshTokenUrl =  "/wxa/setRefreshToken.do";
	    
}
