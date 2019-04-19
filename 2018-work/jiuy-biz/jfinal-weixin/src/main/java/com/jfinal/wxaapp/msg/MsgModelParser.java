/**
 * Copyright (c) 2011-2014, L.cm 卢春梦 (qq596392912@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.wxaapp.msg;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.wxaapp.msg.bean.WxaImageMsg;
import com.jfinal.wxaapp.msg.bean.WxaMsg;
import com.jfinal.wxaapp.msg.bean.WxaTextMsg;
import com.jfinal.wxaapp.msg.bean.WxaUserEnterSessionMsg;

/**
 * 用户model转为msg对象
 * @author L.cm
 *
 */
class MsgModelParser {

	private static enum MsgType {
		text, image, event
	}
	
	protected WxaMsg parserMsg(MsgModel msgModel) {
		String msgTypeStr = msgModel.getMsgType().toLowerCase();
		MsgType msgType = MsgType.valueOf(msgTypeStr);
		if (MsgType.text == msgType) {
			return new WxaTextMsg(msgModel);
		}
		if (MsgType.image == msgType) {
			return new WxaImageMsg(msgModel);
		}
		if (MsgType.event == msgType) {
			if ("user_enter_tempsession".equalsIgnoreCase(msgModel.getEvent())) {
				return new WxaUserEnterSessionMsg(msgModel);
			}
		}
		
//    	<xml><ToUserName><![CDATA[gh_61a5b0fe7de3]]></ToUserName>
//    	<FromUserName><![CDATA[oPsob0Y6moyDXv28Yk8hfCmOO02I]]></FromUserName>
//    	<CreateTime>1499938327</CreateTime>
//    	<MsgType><![CDATA[event]]></MsgType>
//    	<Event><![CDATA[weapp_audit_fail]]></Event>
//    	<Reason><![CDATA[1:账号信息不符合规范:<br>(1):简介未体现小程序功能和内容<br>如有疑问，请查看<a href="http://kf.qq.com/faq/1702227BraeM170222URzQrY.html">详情与反馈。</a>]]></Reason>
//    	<FailTime>1499938327</FailTime>
//    	</xml>
		throw new RuntimeException("JFinal-weixin 暂不支持该类型的小程序消息！msgModel:"+JSONObject.toJSONString(msgModel));
	}

}
