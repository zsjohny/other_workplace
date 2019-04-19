

package com.jfinal.weixin.jiuy.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Duang;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.jfinal.third.api.ThirdApi;
import com.jfinal.third.api.ThirdCustomServiceApi;
import com.jfinal.third.api.ThirdServiceAdviceApi;
import com.jfinal.third.api.WxaMaterialApi;
import com.jfinal.weixin.jiuy.util.EncodeUtil;
//import com.jfinal.weixin.jiuy.EncodeUtil;
import com.jfinal.weixin.jiuy.util.JYFileUtil;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.CustomServiceApi;
import com.xiaoleilu.hutool.date.DateUtil;

/**
 * 模板消息相关接口（提供内部接口，仅供玖远内部系统调用）
 */
public class JiuyServiceAdviceController  extends Controller {

    static Log logger = Log.getLog(JiuyServiceAdviceController.class);
	protected ThirdServiceAdviceApi thirdServiceAdviceApi = Duang.duang(ThirdServiceAdviceApi.class);
	
	 /**
     * 获取服务通知列表
     */
    public void getTemplateAdvice() {
    	String appId = getPara("appId");
    	String ret = thirdServiceAdviceApi.getTemplateAdvice(appId);
    	logger.info("发送服务通知完成，ret："+ret);
        renderText(ret);
        return;
        
    }
    
    
    /**
     * 发送服务通知
     */
    public void sendTemplateAdvice() {
    	
    	String page = getPara("page");
    	String form_id = getPara("form_id");
    	String appId = getPara("appId");
    	String openId = getPara("openId");
    	String template_id = getPara("template_id");
//    	String template_title = getPara("template_title");
//    	String template_keywordIds = getPara("template_keywordIds");
    	String keyword1 = getPara("keyword1");
    	String keyword2 = getPara("keyword2");
    	String keyword3 = getPara("keyword3");
    	String keyword4 = getPara("keyword4");
//    	String data = getPara("data");
    	
    	logger.info("发送服务通知,page:"+page);
    	logger.info("发送服务通知,form_id:"+form_id);
    	logger.info("发送服务通知,appId:"+appId);
    	logger.info("发送服务通知,openId:"+openId);
    	logger.info("发送服务通知,template_id:"+template_id);
    	logger.info("发送服务通知,keyword1:"+keyword1);
    	logger.info("发送服务通知,keyword2:"+keyword2);
    	logger.info("发送服务通知,keyword3:"+keyword3);
    	logger.info("发送服务通知,keyword4:"+keyword4);
    	String ret = thirdServiceAdviceApi.sendTemplateAdvice(page,appId, openId,template_id,form_id,keyword1,keyword2,keyword3,keyword4);
    	logger.info("发送服务通知完成，ret："+ret);
        renderText(ret);
        return;
        
    }
    /**
     * 添加服务通知模板
     */
    public void addTemplateAdvice() {
    	
    	String appId = getPara("appId");
    	String template_id = getPara("template_id");
    	String template_keywordIds = getPara("template_keywordIds");
    	
    	String ret = thirdServiceAdviceApi.addTemplateAdvice(appId,template_id, template_keywordIds);
    	logger.info("发送服务通知完成，ret："+ret);
        renderText(ret);
        return;
        
    }
    
}






