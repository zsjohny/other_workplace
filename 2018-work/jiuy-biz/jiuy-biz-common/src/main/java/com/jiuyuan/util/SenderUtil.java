/**
 * 
 */
package com.jiuyuan.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import net.dongliu.requests.Requests;

/**
 * @author DongZhong
 * @version 创建时间: 2017年8月15日 下午2:30:55
 */
public class SenderUtil {

	static String EmailUrl = "http://118.178.232.186:4000/sender/mail";
	static String WeixinUrl = "http://118.178.232.186:4567/send";

	private static void sendPost(String url, Map<String, String> map) {
		Requests.post(url).data(map).text();
	}

	public static void sendAudit(JSONObject jsonObject) {
		Map<String, String> map = new HashMap<String, String>();
		try {

			if (!StringUtils.equals("true", jsonObject.getString("enable")))
				return;

			map.put("tos", jsonObject.getString("tosWeixin"));
			map.put("content", jsonObject.getString("content"));
			sendAuditWeixin(map);
			map.put("tos", jsonObject.getString("tosEmail"));
			map.put("subject", jsonObject.getString("content"));
			sendAuditMail(map);

		} catch (Exception e) {
			System.out.println("post 发送失败:" + jsonObject);
		}
	}

	/*
	 * 邮件通知
	 */
	public static void sendAuditMail(Map<String, String> map) {
		sendPost(EmailUrl, map);
	}

	/*
	 * 微信通知
	 */
	public static void sendAuditWeixin(Map<String, String> map) {
		sendPost(WeixinUrl, map);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// JSONObject jsonObject = JSONObject.parseObject(
		// "{\"enable\":\"true\",\"tosEmail\":\"bensoho@qq.com,1322630341@qq.com\",\"tosWeixin\":\"DingBin,YeLuYing\",\"content\":\"俞姐姐门店宝新用户注册申请通知，
		// 请尽快处理！\"}");
		JSONObject jsonObject = JSONObject.parseObject(
				"{\"enable\":\"true\",\"tosEmail\":\"1317634820@qq.com,2595954688@qq.com\",\"tosWeixin\":\"YueZhiJuan,DongZhong\",\"content\":\"test1俞姐姐门店宝新用户注册申请通知， 请尽快处理！\"}");
		sendAudit(jsonObject);
		// sendAuditWeixin();
	}

}
