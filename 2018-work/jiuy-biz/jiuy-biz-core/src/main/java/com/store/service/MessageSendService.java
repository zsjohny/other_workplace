package com.store.service;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayConfig;
import com.jiuyuan.util.EncodeUtil;
import com.store.dao.mapper.MessageMapper;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;

/**
 * <p>
 * 接收消息服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-06-21
 */
@Service
public class MessageSendService {
	 private static final Log logger = LogFactory.get();
	 
	 
	public static String weixinServiceUrl = WeixinPayConfig.getWeiXinServerUrl();
	public static String sendTextUrl = "/servermsg/sendText";
	public static String sendImageUrl = "/servermsg/sendImage";

	@Autowired
	private StoreUserService storeUserService;
	
	@Autowired
	
	MessageMapper messageMapper;
	@Autowired
	MemberService memberService;
	
	/**
	 * 发送文本客服消息
	 */
	public String sendText(String content, String bindWeixin,String appId) {
		if(StringUtils.isEmpty(weixinServiceUrl)){
			logger.info("向微信会员发送图片客服消息weixinServiceUrl为空，请检查配置！！！！！！");
			return null;
		}
		logger.info("向微信会员发送客服消息,sendText,appId:"+appId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("content", content);
 	 	map.put("toUserName", bindWeixin);
 	 	Response<String> resp = Requests.get(weixinServiceUrl + sendTextUrl).params(map).text();
 	 	String ret = resp.getBody();
 		logger.info("向微信会员发送文本客服消息body:"+ret);
		return ret;
	}
	/**
	 * 发送图片客服消息
	 */
	public String sendImage(String imgUrl, String bindWeixin,String appId) {
		if(StringUtils.isEmpty(weixinServiceUrl)){
			logger.info("向微信会员发送图片客服消息weixinServiceUrl为空，请检查配置！！！！！！");
			return null;
		}
		
		logger.info("向微信会员发送客服消息,sendImage");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("imgUrl", EncodeUtil.encodeURL(imgUrl));
 	 	map.put("toUserName", bindWeixin);
 	 	String url = weixinServiceUrl + sendImageUrl;
 	 	logger.info("发送图片客服消息开始，url:"+url+",map:"+JSONObject.toJSONString(map));
 	 	Response<String> resp = Requests.get(url).params(map).text();
 	 	logger.info("向微信会员发送图片客服消息成功！");
 	 	String ret = resp.getBody();
 		logger.info("向微信会员发送图片客服消息返回数据body:"+ret);
		return ret;
	}


}
