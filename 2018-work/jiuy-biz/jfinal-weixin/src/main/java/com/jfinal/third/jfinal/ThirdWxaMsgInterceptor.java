/**
 * Copyright (c) 2011-2014, L.cm 卢春梦 (qq596392912@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.third.jfinal;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.third.ThirdWxaConfigKit;
//import com.jfinal.weixin.demo.WeixinMsgController;
//import com.jfinal.weixin.sdk.api.ApiConfigKit;
//import com.jfinal.weixin.sdk.jfinal.AppIdParser;
//import com.jfinal.weixin.sdk.jfinal.MsgController;
//import com.jfinal.weixin.sdk.jfinal.MsgInterceptor;
import com.jfinal.weixin.sdk.kit.SignatureCheckKit;

/**
 * 小程序消息拦截器
 *
 * @author 赵兴林
 *
 */
public class ThirdWxaMsgInterceptor implements Interceptor {
	 private static final Log logger = Log.getLog(ThirdWxaMsgInterceptor.class);




    /**
     * 是否为开发者中心保存服务器配置的请求
     */
    private boolean isConfigServerRequest(Controller controller) {
        return StrKit.notBlank(controller.getPara("echostr"));
    }

    /**
     * 配置开发者中心微信服务器所需的 url 与 token
     *
     * @param c 控制器
     */
    public void configServer(Controller c) {
        // 通过 echostr 判断请求是否为配置微信服务器回调所需的 url 与 token
        String echostr = c.getPara("echostr");
        String signature = c.getPara("signature");
        String timestamp = c.getPara("timestamp");
        String nonce = c.getPara("nonce");
        boolean isOk = SignatureCheckKit.me.checkSignature(signature, timestamp, nonce);
        if (isOk)
            c.renderText(echostr);
        else
        	logger.error("验证失败：configServer");
    }

	@Override
	public void intercept(Invocation inv) {
		 Controller controller = inv.getController();
		if (!(controller instanceof ThirdWxaMsgController)){
            throw new RuntimeException("第三方小程序消息控制器需要继承 ThirdWxaMsgController");
		}
//		logger.info("*********************************************小程序消息拦截器WxaMsgInterceptor.intercept");


        // 如果是服务器配置请求，则配置服务器并返回
        if (isConfigServerRequest(controller)) {
            configServer(controller);
            return;
        }

		// 对开发模式时不进行签名检查测试更加友好
        if (ThirdWxaConfigKit.isDevMode()) {
        	logger.info("开发模式不进行签名验证。");
            inv.invoke();
        } else {
            // 签名检测
            if (checkSignature(controller)) {
            	logger.info("签名验证成功");
                inv.invoke();
            } else {
            	logger.info("签名验证失败，请确定是微信服务器在发送消息过来");
                controller.renderText("签名验证失败，请确定是微信服务器在发送消息过来");
            }
        }
	}

	/**
     * 检测签名
     */
    private boolean checkSignature(Controller controller) {
        String signature = controller.getPara("signature");
        String timestamp = controller.getPara("timestamp");
        String nonce = controller.getPara("nonce");
        if (StrKit.isBlank(signature) || StrKit.isBlank(timestamp) || StrKit.isBlank(nonce)) {
            controller.renderText("check signature failure");
            return false;
        }

        if (SignatureCheckKit.me.checkSignature(signature, timestamp, nonce)) {
            return true;
        } else {
        	logger.error("check signature failure: " +
                    " signature = " + controller.getPara("signature") +
                    " timestamp = " + controller.getPara("timestamp") +
                    " nonce = " + controller.getPara("nonce"));

            return false;
        }
    }





}
