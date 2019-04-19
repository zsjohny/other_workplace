/**
 * 
 */
package com.yujj.web.controller.shop;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.util.CheckUtil;
import com.jiuyuan.util.WebUtil;
import com.jiuyuan.util.spring.ControllerUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.web.controller.shop.msg.WeiXinMsgService;
import com.yujj.web.controller.shop.weixin.util.WXBizMsgCrypt;

/**
 * https://mp.weixin.qq.com/wiki
 * 微信第三方授权
 * 文档地址：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=beb28404001a834eb809cd59e8275e8ce5c4ed12&lang=zh_CN
 */
@Controller
@RequestMapping("/weixin")
public class WeiXinMsgController {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinMsgController.class);

  
    
    @Autowired
    private WeiXinMsgService weiXinMsgService;
    
    
    
    
    
    
    public static void main(String[] args) {
    	logger.info("开始");
      	
    	//3.异步回复
    	new Thread(){
            public void run(){
               try {
            	   	Thread.sleep(1000*10);
            	   	logger.info("消息与事件接收callback成功，异步调用发送客服消息成功");
               } catch (InterruptedException e) { 
            	   e.printStackTrace();
               }
            }
         }.start(); 
	}
    
    /**
     * 公众号消息与事件接收URL
     * /weixin/wx95c37c75c641bc5e/callback.do
     * ?signature=59afe78ce80e3b51794220d569e8e7119e0e99ca
     * &timestamp=1497238755
     * &nonce=445538741
     * &openid=ownIvw58c-GHXtNlC1IeCMoQt89c
     * &encrypt_type=aes
     * &msg_signature=3b5cbf1873a4deed41df087200fe2f32c26c74cf
     * @param APPID
     * @param requestBody
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{APPID}/callback")
    @ResponseBody
    public String msgCallback(@PathVariable("APPID") String APPID,String signature,String timestamp,String nonce,String encrypt_type,String msg_signature,@RequestBody String requestBody) throws Exception {//
    	
    	String returnXmlStr = weiXinMsgService.msgCallback(APPID,requestBody,signature,timestamp,nonce,encrypt_type,msg_signature);//requestBody
    	
//    	if(StringUtils.isEmpty(returnXmlStr)){
//    		returnXmlStr = "success";
//    	}
    	logger.info("消息与事件接收callback成功!!!!!,响应数据returnXmlStr: " + returnXmlStr);
        
    	return returnXmlStr;
    }
    /**
     * 公众号消息与事件接收URL
     * /weixin/123456/callback
     * @param APPID
     * @param requestBody
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/callback")
    @ResponseBody
    public String callback(
    		HttpServletRequest request, 
    		HttpServletResponse response,
			@RequestParam("signature") String signature, 
			@RequestParam("timestamp") String timestamp, 
            @RequestParam(value = "nonce") String nonce,
            @RequestParam(value = "echostr", required = false, defaultValue = "") String echostr ,
            @RequestParam(value = "openid", required = false, defaultValue = "") String openid,
            @RequestParam(value = "encrypt_type", required = false, defaultValue = "") String encrypt_type,
            @RequestParam(value = "msg_signature", required = false, defaultValue = "") String msg_signature,
            @RequestBody(required = false) String requestBody) throws Exception {//@RequestParam

    	
    	logger.info("消息与事件接收callback,signature:{},timestamp:{},nonce:{},echostr:{},openid{},encrypt_type{},msg_signature{}",
    			signature,timestamp,nonce,echostr,openid,encrypt_type,msg_signature);
    	
    	//校验服务器可用性
    	if(StringUtils.isNotEmpty(echostr)){
    		PrintWriter out = response.getWriter();
            if(CheckUtil.checkSignature(signature, timestamp, nonce)){
                out.print(echostr);
                logger.info("校验通过，request.getContextPath():" + request.getContextPath());
            }else{
            	logger.info("不是微信发来的请求，request.getContextPath():" + request.getContextPath());
            	response.getWriter().append("Served at: ").append(request.getContextPath());
            }
            
            out.close();
            out = null;
            return null;
    	}
    	
    	//处理微信发来的情况
    	String returnXmlStr = weiXinMsgService.msgCallback(null,requestBody,signature,timestamp,nonce,encrypt_type,msg_signature);//requestBody
    	if(StringUtils.isEmpty(returnXmlStr)){
    		returnXmlStr = "success";
    	}
    	logger.info("消息与事件接收callback成功!!!!!,响应数据returnXmlStr: " + returnXmlStr);
//    	return returnXmlStr;
    	PrintWriter out = response.getWriter();
    	 out.print(returnXmlStr);
    	 out.close();
         out = null;
    	 return null;
    }
    
   
    /**
     * 发送客服消息
     */
    @RequestMapping("/send_service_msg")
    @ResponseBody
    public JsonResponse send_service_msg(String OPENID,String content) {
    	//获取第三方平台component_access_token
    	String ret = weiXinMsgService.send_service_msg(OPENID,content);
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String,String> returnMap = new HashMap();
    	returnMap.put("ret", ret);
    	jsonResponse.setData(returnMap);
    	return jsonResponse.setSuccessful();
    }
   
   
}
