/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.demo;





import java.util.HashMap;
import java.util.Map;

import com.jfinal.log.Log;
import com.jfinal.weixin.jiuy.service.MsgService;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.CustomServiceApi;
import com.jfinal.weixin.sdk.jfinal.MsgControllerAdapter;
import com.jfinal.weixin.sdk.kit.MsgEncryptKit;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InLocationMsg;
import com.jfinal.weixin.sdk.msg.in.InShortVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.in.event.InCustomEvent;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InLocationEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMassEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.in.event.InQrCodeEvent;
import com.jfinal.weixin.sdk.msg.in.event.InTemplateMsgEvent;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import com.jfinal.weixin.sdk.msg.out.OutCustomMsg;
import com.jfinal.weixin.sdk.msg.out.OutMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import com.jfinal.wxaapp.jfinal.WxaMsgControllerAdapter;
import com.jfinal.wxaapp.msg.bean.WxaImageMsg;
import com.jfinal.wxaapp.msg.bean.WxaMsg;
import com.jfinal.wxaapp.msg.bean.WxaOutMsg;
import com.jfinal.wxaapp.msg.bean.WxaOutTextMsg;
import com.jfinal.wxaapp.msg.bean.WxaTextMsg;
import com.jfinal.wxaapp.msg.bean.WxaUserEnterSessionMsg;


/**
 * 赵兴林
 * 将此 DemoController 在YourJFinalConfig 中注册路由，
 * 并设置好weixin开发者中心的 URL 与 token ，使 URL 指向该
 * DemoController 继承自父类 WeixinController 的 index
 * 方法即可直接运行看效果，在此基础之上修改相关的方法即可进行实际项目开发
 */
public class WeixinWxaMsgController extends WxaMsgControllerAdapter {

    static Log logger = Log.getLog(WeixinWxaMsgController.class);
    
    /**
     * 处理接收到的文本消息
     * @param textMsg 处理接收到的文本消息
     */
    protected  void processTextMsg(WxaTextMsg textMsg){
    	//转发门店接口系统处理
    	MsgService.processTextMsg(textMsg);
    	
    	WxaOutTextMsg outMsg = new WxaOutTextMsg(textMsg);
    	outMsg.setContent("文本消息~");
        render(outMsg);
    }
    
    /**
     * 处理接收到的图片消息
     * @param imageMsg 处理接收到的图片消息
     */
    protected  void processImageMsg(WxaImageMsg imageMsg){
    	//转发门店接口系统处理
    	MsgService.processImageMsg(imageMsg);
    	
    	WxaOutTextMsg outMsg = new WxaOutTextMsg(imageMsg);
    	outMsg.setContent("图片消息~");
        render(outMsg);
    }
    
    /**
     * 处理接收到的进入会话事件（会员进入客服会话触发）
     * @param userEnterSessionMsg 处理接收到的进入会话事件
<xml><ToUserName><![CDATA[gh_e714f01ae6ad]]></ToUserName>
<FromUserName><![CDATA[o2Kn-0GB5SOFo4bLtN5v9lO8nStQ]]></FromUserName>
<CreateTime>1498886986</CreateTime>
<MsgType><![CDATA[event]]></MsgType>
<Event><![CDATA[user_enter_tempsession]]></Event>
<SessionFrom><![CDATA[weapp]]></SessionFrom>
</xml>
     */
    protected  void processUserEnterSessionMsg(WxaUserEnterSessionMsg userEnterSessionMsg){
    	logger.info("处理接收到的进入会话事件：processUserEnterSessionMsg:"+userEnterSessionMsg.toString());
    	//转发门店接口系统处理
    	MsgService.processUserEnterSessionMsg(userEnterSessionMsg);
    }
    
    
   

    /**
     * 在接收到微信服务器的 InMsg 消息后后响应 OutMsg 消息
     *
     * @param outMsg 输出对象
     */
    public void render(WxaOutMsg wxaOutMsg) {
        String outMsgXml = wxaOutMsg.toXml();
        // 开发模式向控制台输出即将发送的 OutMsg 消息的 xml 内容
        if (ApiConfigKit.isDevMode()) {
            System.out.println("发送消息:");
            System.out.println(outMsgXml);
            System.out.println("小程序--------------------------------------------------------------------------------\n");
        }
       
        // 是否需要加密消息
        if (ApiConfigKit.getApiConfig().isEncryptMessage()) {
            outMsgXml = MsgEncryptKit.encrypt(outMsgXml, getPara("timestamp"), getPara("nonce"));
        }

        renderText(outMsgXml, "text/xml");
    }
    
 
    
    
    /**
     * 消息输出
     * @param content 输出的消息
     */
    public void renderOutTextMsg(String content,WxaMsg wxaMsg) {
    	WxaOutTextMsg outMsg = new WxaOutTextMsg(wxaMsg);
        outMsg.setContent(content);
        render(outMsg);
        
       
    }

}






