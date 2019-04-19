package com.jfinal.third.controller;


import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;


import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.third.ThirdWxaConfigKit;
import com.jfinal.third.api.MsgCallbackInterceptor;
import com.jfinal.third.api.ThirdApi;
import com.jfinal.third.api.ThirdMsgApi;
import com.jfinal.third.jfinal.ThirdWxaMsgControllerAdapter;
import com.jfinal.third.util.CheckUtil;
import com.jfinal.weixin.jiuy.service.MsgService;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.kit.MsgEncryptKit;
//import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.kit.ThirdWxaMsgEncryptKit;
import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.out.OutMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import com.jfinal.wxaapp.jfinal.WxaMsgControllerAdapter;
import com.jfinal.wxaapp.jfinal.WxaMsgInterceptor;
import com.jfinal.wxaapp.msg.bean.WxaImageMsg;
import com.jfinal.wxaapp.msg.bean.WxaMsg;
import com.jfinal.wxaapp.msg.bean.WxaOutMsg;
import com.jfinal.wxaapp.msg.bean.WxaOutTextMsg;
import com.jfinal.wxaapp.msg.bean.WxaTextMsg;
import com.jfinal.wxaapp.msg.bean.WxaUserEnterSessionMsg;

/**
 * 第三方消息相关接口相关接口（提供微信调用接口内部接口，仅供微信系统消息相关回调）
 * @author zhaoxinglin
 */
public class WeixinThirdWxaMsgController  extends ThirdWxaMsgControllerAdapter {
	static Log logger = Log.getLog(WeixinThirdWxaMsgController.class);
	protected ThirdApi thirdApi = Duang.duang(ThirdApi.class);
	protected ThirdMsgApi thirdMsgApi = Duang.duang(ThirdMsgApi.class);
	
	  
    /**
     * 处理接收到的文本消息
     * @param textMsg 处理接收到的文本消息
     */
    protected  void processTextMsg(WxaTextMsg textMsg){
    	// 文本消息检查是否是全网发布用于全网发布
    	String Content = textMsg.getContent();
    	String FromUserName = textMsg.getFromUserName();
    	String returnContent = thirdMsgApi.textCheckRelease(Content, FromUserName);
    	if(StringUtils.isNotEmpty(returnContent)){
//    		 WxaOutTextMsg outMsg = new WxaOutTextMsg(textMsg);
//    	     outMsg.setContent(returnContent);
//    	     //检测全网发布
//    	     logger.info("检测全网发布,outMsg:"+outMsg.toString());
    	     renderOutTextMsg(returnContent,textMsg);
    		 return;
    	}
    	
    	//转发门店接口系统处理
    	String returnMsg = MsgService.processTextMsg(textMsg);
    	
//    	OutTextMsg outMsg = new OutTextMsg(textMsg);
//    	outMsg.setContent("欢迎使用门店版小程序");
//        render(outMsg);
//    	renderOutTextMsg("欢迎使用门店版小程序",textMsg);
//    	renderTextSuccess();
    	renderByReturnMsg(returnMsg,textMsg);
    	
    }
	

	/**
     * 处理接收到的图片消息
     * @param imageMsg 处理接收到的图片消息
     */
    protected  void processImageMsg(WxaImageMsg imageMsg){
    	//转发门店接口系统处理
    	String returnMsg = MsgService.processImageMsg(imageMsg);
    	
//    	OutTextMsg outMsg = new OutTextMsg(imageMsg);
//    	outMsg.setContent("图片消息~");
//        render(outMsg);
//    	renderTextSuccess();
    	renderByReturnMsg(returnMsg,imageMsg);
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
    	
    	String returnContent = "";
    	returnContent = userEnterSessionMsg.getEvent()+"from_callback";
//    	OutTextMsg outMsg = new OutTextMsg(userEnterSessionMsg);
//    	outMsg.setContent(returnContent);
//    	render(outMsg);
    	
    	
    	renderOutTextMsg(returnContent,userEnterSessionMsg);
    }
    
    
    /**
     * TODO 接收处理审核通过通知
	 * 6、获取审核结果（是通过地址回调接收的）
	 * 
	 * 注意：当小程序有审核结果后，第三方平台将可以通过开放平台上填写的回调地址，获得审核结果通知。
	 * 除了消息通知之外，第三方平台也可通过接口查询审核状态。
	 * 
	 * 审核通过时，接收到的推送XML数据包示例如下：
		<xml><ToUserName><![CDATA[gh_fb9688c2a4b2]]></ToUserName>
		<FromUserName><![CDATA[od1P50M-fNQI5Gcq-trm4a7apsU8]]></FromUserName>
		<CreateTime>1488856741</CreateTime>
		<MsgType><![CDATA[event]]></MsgType>
		<Event><![CDATA[weapp_audit_success]]></Event>
		<SuccTime>1488856741</SuccTime>
		</xml>
		审核不通过时，接收到的推送XML数据包示例如下：
		<xml><ToUserName><![CDATA[gh_fb9688c2a4b2]]></ToUserName>
		<FromUserName><![CDATA[od1P50M-fNQI5Gcq-trm4a7apsU8]]></FromUserName>
		<CreateTime>1488856591</CreateTime>
		<MsgType><![CDATA[event]]></MsgType>
		<Event><![CDATA[weapp_audit_fail]]></Event>
		<Reason><![CDATA[1:账号信息不符合规范:<br>
		(1):包含色情因素<br>
		2:服务类目"金融业-保险_"与你提交代码审核时设置的功能页面内容不一致:<br>
		(1):功能页面设置的部分标签不属于所选的服务类目范围。<br>
		(2):功能页面设置的部分标签与该页面内容不相关。<br>
		</Reason>
		<FailTime>1488856591</FailTime>
		</xml>
	 * @param appId
	 * @param storeId
	 * @param templateId
	 * @return
	 */
	
    
    

    
    /**
     * 在接收到微信服务器的 InMsg 消息后后响应 OutMsg 消息
     *
     * @param outMsg 输出对象
     */
    public void render(WxaOutMsg waxOutMsg) {
        String outMsgXml = waxOutMsg.toXml();
        // 开发模式向控制台输出即将发送的 OutMsg 消息的 xml 内容
        if (ThirdWxaConfigKit.isDevMode()) {
            System.out.println("第三方回应消息（加密前）:");
            System.out.println(outMsgXml);
            //System.out.println("小程序--------------------------------------------------------------------------------\n");
        }
       
        // 是否需要加密消息
        if (ThirdWxaConfigKit.getThirdWxaConfig().isMessageEncrypt()) {
            outMsgXml = ThirdWxaMsgEncryptKit.encrypt(outMsgXml, getPara("timestamp"), getPara("nonce"));
        }
        
     // 开发模式向控制台输出即将发送的 OutMsg 消息的 xml 内容
        if (ThirdWxaConfigKit.isDevMode()) {
            System.out.println("第三方回应消息（加密后）:");
            System.out.println(outMsgXml);
            //System.out.println("小程序--------------------------------------------------------------------------------\n");
        }
       

        renderText(outMsgXml, "text/xml");
    }
    
    /**
     * 消息输出
     * @param content 输出的消息
     */
    public void renderTextSuccess() {
    	 renderText("success", "text/xml");
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
    /**
	 * 根据回复的文本信息进行自动回复
	 * @param ret
	 */
    private void renderByReturnMsg(String returnMsg,WxaMsg wxaMsg) {
    	if(StringUtils.isEmpty(returnMsg)){
    		renderTextSuccess();
//    		renderOutTextMsg("",wxaMsg);
    	}else{
    		renderOutTextMsg(returnMsg,wxaMsg);
    	}
	}
}
