package com.jfinal.weixin.jiuy.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.third.util.weixinpay.WapPayHttpUtil;
import com.jfinal.weixin.jiuy.util.EncodeUtil;
import com.jfinal.weixin.sdk.utils.HttpUtils;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;

//import net.dongliu.requests.Requests;
//import net.dongliu.requests.Response;
/**
 * 会员相关接口（http协议进行对接）
 * @author Administrator
 *
 */
public class MemberService {
//	PropKit.get("store_api_url");


    static Log logger = Log.getLog(MsgService.class);
    static final String version = "1.0.0";

    /**
     * 调用授权接口
     * @param unionId 用户唯一表示
     * @param nickName 昵称
     * @param gender
     * @param id
     */
    public static String  authoriz(String unionId, String nickName, String headImg, String appId, String storeId, String from, String gender, String uId) {
    	logger.info("请注意测试特殊字符处理====================nickName Base64 编码前:"+nickName);
    	nickName = EncodeUtil.encodeBase64String(nickName);
    	logger.info("请注意测试特殊字符处理====================nickName  Base64 编码后:"+nickName);
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("unionId", unionId);
    	map.put("nickName", nickName);
    	map.put("headImg", headImg);
    	map.put("appId", appId);
    	map.put("storeId", storeId);
    	map.put("from",from);
    	map.put("uId",uId);
        map.put ("sex", gender);
    	logger.info("==========开始授权参数map:"+JSONObject.toJSONString(map));
//	 	Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.authorizUrl).params(map).text();
//	 	String body = resp.getBody();
//    	String param = JSONObject.toJSONString(map);
    	String body = HttpUtils.get(PropKit.get("wxa_api_url")+ApiManager.authorizUrl, map);

	 	logger.info("授权结果body:"+body);
	 	if(StringUtils.isNotEmpty(body)){
	 		JSONObject jsonObject = (JSONObject) JSONObject.parse(body);
	 		int code = (Integer) jsonObject.get("code");
	 		if(code == 0){
		 		JSONObject memberJSON =  (JSONObject) jsonObject.get("data");//
			 	return memberJSON.toJSONString();
		 	}
	 	}
	 	return "";
	}
    /**
     * 调用授权接口
     * @param unionId 用户唯一表示
     * @param from 来源 0 是自主注册 1是邀请注册
     * @param appId
     * @param loginType 0专享,1店中店
     */
	public static Map<String,String> login(String unionId, String appId,String storeId,String from, String loginType) {
		Map<String, String> memberInfoMap = new HashMap<String, String>();

		Map<String, String> map = new HashMap<String, String>();
    	map.put("unionId", unionId);
    	map.put("appId", appId);
    	map.put("storeId", storeId);
    	map.put("loginType", loginType);
    	map.put("from",from);
    	logger.info("开始登陆login-------appId:"+appId+",storeId:"+storeId);
    	Map<String, String> headers = new HashMap<String, String>();
		String sign = ParamSignUtils.getSign(map);
	 	headers.put("sign", sign);
	 	headers.put("version", version);
	 	Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.loginUrl).headers(headers).params(map).text();
	 	//{"successful":true,"error":null,"code":0,"data":{"id":2,"bindPhone":"","bindWeixin":"o2Kn-0GB5SOFo4bLtN5v9lO8nStQ","userNickname":"诸葛正雄","userIcon":"https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK7PqbGBfGAN5HsbomvUroyggljtbc0ibXvsquYSyxsINteCZJPTbC5veGgVZgmibd8NmVOfFcibnIcA/0","minicodeUrl":null,"lastMessageTime":40905213523392,"lastMessageType":9,"lastMessageContent":"订阅了您的小程序","tagIds":null,"status":0,"createTime":40905213523392,"updateTime":40905213523392},"html":null}
	 	String body = resp.getBody();

	 	logger.info("登陆返回body:"+body);
	 	if(StringUtils.isNotEmpty(body)){
	 		JSONObject jsonObject = (JSONObject) JSONObject.parse(body);
	 		int code = (Integer) jsonObject.get("code");
	 		if(code == 0){
		 		JSONObject memberJSONObject =  (JSONObject) jsonObject.get("data");
		 		memberInfoMap.put("lastMessageContent", memberJSONObject.getString("lastMessageContent"));
		 		memberInfoMap.put("userNickname", memberJSONObject.getString("userNickname"));
		 		memberInfoMap.put("bindWeixin", memberJSONObject.getString("bindWeixin"));
		 		memberInfoMap.put("lastMessageTime", memberJSONObject.getString("lastMessageTime"));
		 		memberInfoMap.put("storeId", memberJSONObject.getString("storeId"));
		 		memberInfoMap.put("createTime", memberJSONObject.getString("createTime"));
		 		memberInfoMap.put("id", memberJSONObject.getString("id"));
		 		memberInfoMap.put("userIcon", memberJSONObject.getString("userIcon"));

                String sex = memberJSONObject.getString ("sex");
                memberInfoMap.put("sex", sex);
		 	}
	 	}
	 	return memberInfoMap;
	}

	/**
	 * 保存小程序刷新token
	 * @return
	 */
	public static void setRefreshToken(String appId, String refreshToken) {
		String url = PropKit.get("wxa_api_url")+ApiManager.setRefreshTokenUrl;
		Map<String, String> map = new HashMap<String, String>();
    	map.put("appId", appId);
    	map.put("refreshToken", refreshToken);


    	Map<String, String> headers = new HashMap<String, String>();
		String sign = ParamSignUtils.getSign(map);
	 	headers.put("sign", sign);
	 	headers.put("version", version);
    	logger.info("开始保存小程序刷新token,url:"+url+",appId:"+appId+",refreshToken:"+refreshToken);
	 	Response<String> resp = Requests.get(url).headers(headers).params(map).text();
	 	String body = resp.getBody();
	 	logger.info("保存小程序刷新token返回body:"+body);
	 	if(StringUtils.isEmpty(body)){
	 		logger.info("========================保存小程序刷新token返回body:"+body+",请尽快排查问题");

	 	}
	}

	/**
	 * 从数据库获取刷新token
	 * @return
	 */
	public static String getRefreshToken(String appId) {
		Map<String, String> map = new HashMap<String, String>();
    	map.put("appId", appId);
    	logger.info("开始获取小程序刷新token-------appId:"+appId);
	 	Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.getRefreshTokenUrl).params(map).text();
	 	String body = resp.getBody();
	 	logger.info("获取小程序刷新token返回body:"+body);
		try{
            JSONObject jsonObject = JSONObject.parseObject(body);
            return jsonObject.getJSONObject("data").getString("refreshToken");
		}catch (Exception e) {
		    e.printStackTrace();
		    return "";
		}
	}

}
