package com.jfinal.weixin.jiuy.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
import com.jfinal.wxaapp.msg.bean.WxaImageMsg;
import com.jfinal.wxaapp.msg.bean.WxaTextMsg;
import com.xiaoleilu.hutool.json.JSONUtil;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;
/**
 * 商家服务接口（http协议进行对接）
 * @author Administrator
 *
 */
public class ShopService {
//	PropKit.get("store_api_url");


    static Log logger = Log.getLog(MsgService.class);
    static final String version = "1.0.0"; 
    /**
     * 小程序授权通知
     * @param params
     */
	public void wxaAuthNotification(Map<String, String> params) {
	 	Map<String, String> map = new HashMap<String, String>();
	 	Map<String, String> headers = new HashMap<String, String>();
		String sign = ParamSignUtils.getSign(map);
	 	headers.put("sign", sign);
	 	headers.put("version", version);
		Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.wxaAuthNotificationUrl).headers(headers).params(params).text();
	 	String body = resp.getBody();
//		Map<String, Object> map = new HashMap<String, Object>();
//		String param = JSONObject.toJSONString(map);
//    	String body = WapPayHttpUtil.sendPostHttp(PropKit.get("wxa_api_url")+ApiManager.wxaAuthNotificationUrl, param);
		logger.info("小程序授权通知:"+body);
	}
	/**
     * 小程序授权
     * @param params
     */
	public String wxaAuth(String storeId,String authorizer_appid,String authorizer_info_jsonstr) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("storeId", storeId);
		map.put("authorizer_appid", authorizer_appid);
		map.put("authorizer_info_jsonstr", authorizer_info_jsonstr);
		Map<String, String> headers = new HashMap<String, String>();
		String sign = ParamSignUtils.getSign(map);
	 	headers.put("sign", sign);
	 	headers.put("version", version);
    	Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.wxaAuthUrl).headers(headers).params(map).text();
	 	String body = resp.getBody();
	 	logger.info("调用小程序API工程进行小程序授权返回结果body："+body);
//		String param = JSONObject.toJSONString(map);
//    	String body = WapPayHttpUtil.sendPostHttp(PropKit.get("wxa_api_url")+ApiManager.wxaAuthUrl, param);
//	 	logger.info("小程序授权body:"+body);
	 	return body;
	}
   
}
