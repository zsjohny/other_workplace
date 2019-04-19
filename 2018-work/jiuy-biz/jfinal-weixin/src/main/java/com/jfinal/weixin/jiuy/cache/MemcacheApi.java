package com.jfinal.weixin.jiuy.cache;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Duang;
import com.jfinal.log.Log;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.third.api.ThirdApi;
import com.jfinal.third.util.MemcachedKey;
import com.jfinal.weixin.jiuy.service.MemberService;

public class MemcacheApi {
	static Log logger = Log.getLog(ThirdApi.class);

    String ticket_groupKey = MemcachedKey.GROUP_KEY_component_verify_ticket;
	String token_groupKey = MemcachedKey.GROUP_KEY_compoment_access_token;
	String pre_auth_code_groupKey = MemcachedKey.GROUP_KEY_pre_auth_code;
	String authorizer_access_token_groupKey = MemcachedKey.GROUP_KEY_authorizer_access_token;
	String authorizer_refresh_token_groupKey = MemcachedKey.GROUP_KEY_authorizer_refresh_token;
    // 默认超时时间7200秒 5秒用于程序执行误差
	int DEFAULT_TIME_OUT = 7200 - 5;
	int DEFAULT_ERROR_RANGE = 5;
	int DEFAULT_ERROR_RANGE_50 = 50;

//    protected final MemcachedUtil memcachedUtil = Duang.duang(MemcachedUtil.class);

	public void setTicket(String appId, String componentVerifyTicket) {
		if(StringUtils.isNotEmpty(componentVerifyTicket)){
			MemcachedUtil.put(ticket_groupKey+appId, componentVerifyTicket,DEFAULT_TIME_OUT);
			logger.info("将ComponentVerifyTicket放入缓存成功,key:"+ticket_groupKey+appId +",  ticket = "+componentVerifyTicket);
			getTicket(appId);
		}
	}

	public String getTicket(String appId ) {
		 String ticket = MemcachedUtil.getStr(ticket_groupKey + appId);
		 logger.info("将ComponentVerifyTicket从缓存中取出,key:"+ticket_groupKey+appId+",ticket："+ticket);
		 return ticket;
	}

	public void setThirdToken(String third_appId,String component_access_token,int expires_in) {
		int data_expires_in = expires_in - DEFAULT_ERROR_RANGE;
		MemcachedUtil.put(token_groupKey + third_appId, component_access_token,data_expires_in);
		 logger.info("将ComponentVerifyTicket放入缓存成功,key:"+token_groupKey+third_appId+",component_access_token:"+component_access_token+",data_expires_in:"+data_expires_in);
	}
	/**
	 * 将token放入缓存并将刷新缓存放入时刻
	 * @param authorizer_appid
	 * @param authorizer_access_token
	 * @param expires_in
	 * @param authorizer_refresh_token
	 */
	public void setAccessTokenAndRefreshToken(String authorizer_appid,String authorizer_access_token,int expires_in,String authorizer_refresh_token) {
    	setAccessToken(authorizer_appid , authorizer_access_token,expires_in);
    	setRefreshToken(authorizer_appid , authorizer_refresh_token);
		MemberService.setRefreshToken(authorizer_appid,authorizer_refresh_token);
	}

	private void setAccessToken(String authorizer_appid,String authorizer_access_token,int expires_in) {
		if(StringUtils.isNotEmpty(authorizer_access_token)){
			int data_expires_in = expires_in - DEFAULT_ERROR_RANGE_50;
			MemcachedUtil.put(authorizer_access_token_groupKey + authorizer_appid, authorizer_access_token,data_expires_in);
			logger.info("将ComponentVerifyTicket放入缓存成功,key:"+authorizer_access_token_groupKey+authorizer_appid+",authorizer_access_token:"+authorizer_access_token+",data_expires_in:"+data_expires_in);
		}
	}
	public String getAccessToken(String authorizer_appid) {
		return MemcachedUtil.getStr(authorizer_access_token_groupKey + authorizer_appid);
	}


	private void setRefreshToken(String authorizer_appid,String authorizer_refresh_token) {
		if(StringUtils.isNotEmpty(authorizer_refresh_token)){
			//1、存入缓存
			MemcachedUtil.put(authorizer_refresh_token_groupKey + authorizer_appid, authorizer_refresh_token);
			logger.info("将refreshToken放入缓存成功,key:"+authorizer_refresh_token_groupKey+authorizer_appid+",authorizer_refresh_token:"+authorizer_refresh_token);

		}
	}

	public String getRefreshToken(String authorizer_appid) {
		String authorizer_refresh_token = MemcachedUtil.getStr(authorizer_refresh_token_groupKey + authorizer_appid);
		if(StringUtils.isEmpty(authorizer_refresh_token)){
			authorizer_refresh_token = MemberService.getRefreshToken(authorizer_appid);
		}
		return authorizer_refresh_token;
	}

	public String getThirdToken(String third_appId) {
		return MemcachedUtil.getStr(token_groupKey + third_appId);
	}



	public void set(String key, String value) {
		MemcachedUtil.put(key, value);
	}
	public String get(String key) {
		return MemcachedUtil.getStr(key);
	}


}
