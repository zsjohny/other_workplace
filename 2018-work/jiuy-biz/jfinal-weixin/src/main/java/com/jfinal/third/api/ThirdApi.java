
package com.jfinal.third.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Duang;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.third.ThirdWxaConfig;
import com.jfinal.third.ThirdWxaConfigKit;
import com.jfinal.third.util.AesException;
import com.jfinal.third.util.WXBizMsgCrypt;
import com.jfinal.third.util.weixinpay.WapPayHttpUtil;
import com.jfinal.third.util.weixinpay.WeixinPayCore;
import com.jfinal.weixin.jiuy.cache.MemcacheApi;
import com.jfinal.weixin.jiuy.service.MemberService;
import com.jfinal.weixin.jiuy.service.ShopService;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.encrypt.WxaBizDataCrypt;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

//import com.jfinal.weixin.sdk.api.ApiConfigKit;
//import com.jfinal.weixin.sdk.cache.IAccessTokenCache;

/**
 * 微信 第三方api接口
 *
 * @author L.cm
 */
public class ThirdApi {
    static Log logger = Log.getLog(ThirdApi.class);


    protected static MemcacheApi memcacheApi = Duang.duang(MemcacheApi.class);
//	protected static ThirdApi thirdApi = Duang.duang(ThirdApi.class);


    private static String jsCode2sessionUrl = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 1、推送component_verify_ticket协议（每隔10分钟推送一次）
     * 使用应用内部缓存存储ticket，应用重启会出现一段时间内ticket无法获取，因为微信每个10分钟才推送一次，建议采用外部缓存进行存储
     *
     * @param fromXML
     * @param signature
     * @param timestamp
     * @param nonce
     * @param encrypt_type
     * @param msg_signature
     * @return
     */
    public void component_verify_ticket(String fromXML, String signature, String timestamp, String nonce, String encrypt_type, String msg_signature) {
//    	logger.info("微信推送component_verify_ticket成功, fromXML:{"+fromXML+"},signature:{"+signature+"},timestamp:{"+timestamp+"},nonce:{"+nonce+"},encrypt_type:{"+encrypt_type+"}");
        WXBizMsgCrypt pc;
        String result = null;
        String third_token = PropKit.get("third_token");
        String third_encodingAesKey = PropKit.get("third_encodingAesKey");
        String third_appId = PropKit.get("third_appId");
        logger.info("读取配置中的第三方信息 third_token=" + third_token +
            ",third_encodingAesKey=" + third_encodingAesKey +
            ",third_appId=" + third_appId);
        try {
            pc = new WXBizMsgCrypt(third_token, third_encodingAesKey, third_appId);
            if (StringUtils.isNotEmpty(fromXML)) {
                result = pc.decryptMsg(msg_signature, timestamp, nonce, fromXML);
            } else {
                logger.info("微信推送component_verify_ticket时fromXML为空，请排查问题，fromXML:{" + fromXML + "},signature:{" + signature + "},timestamp:{" + timestamp + "},nonce:{" + nonce + "},encrypt_type:{" + encrypt_type + "}");
            }
        } catch (AesException e) {
            logger.info(third_token + "  " + third_encodingAesKey + "  " + third_appId + "微信推送component_verify_ticket成功,解析时出现错误，请排除问题， fromXML:{" + fromXML + "},signature:{" + signature + "},timestamp:{" + timestamp + "},nonce:{" + nonce + "},encrypt_type:{" + encrypt_type + "}");
            e.printStackTrace();
        }
//		logger.info("component_verify_ticket解密后明文: " + result);
		/* <xml><AppId><![CDATA[wxddd55d6028f404ab]]></AppId>
		 <CreateTime>1496654711</CreateTime>
		 <InfoType><![CDATA[component_verify_ticket]]></InfoType>
		 <ComponentVerifyTicket><![CDATA[ticket@@@6qAXiAW7TwQJadv89I93Mlb-E8Y_HMysAFgnaK8wxuVxUWUihh4rPSDqPnnd6dbTsShn_K32nzk_kFHKVm_tIw]]></ComponentVerifyTicket>
		 </xml>*/
        //解析收到信息
        Map<String, String> params = WeixinPayCore.decodeXml(result);
        String AppId = params.get("AppId");
        String ComponentVerifyTicket = params.get("ComponentVerifyTicket");
        //将ComponentVerifyTicket放入缓存
        if (StringUtils.isNotEmpty(AppId) && StringUtils.isNotEmpty(ComponentVerifyTicket)) {
            memcacheApi.setTicket(AppId, ComponentVerifyTicket);
        }
    }


    /**
     * 2、获取第三方平台component_access_token
     *
     * @return
     */
    public String get_component_access_token() {
        String url = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";


        //共缓存中获取token
//		String memcachedToken = memcacheApi.getThirdToken(PropKit.get("third_appId"));
//		if(StringUtils.isNotEmpty(memcachedToken)){
//			logger.info("从缓存中获取了component_access_token,memcachedToken:{"+memcachedToken+"}" );
//			return memcachedToken;
//		}

        //1、从缓存中获取ticket
        String component_verify_ticket = memcacheApi.getTicket(PropKit.get("third_appId"));
        if (StringUtils.isEmpty(component_verify_ticket)) {
            logger.info("获取第三方平台component_verify_ticket为空请排查问题,third_appId：" + PropKit.get("third_appId"));
            return null;
        }
        String component_access_token = memcacheApi.getThirdToken(PropKit.get("third_appId"));
        if (component_access_token != null && !component_access_token.isEmpty()) {
            logger.info("appId:" + PropKit.get("third_appId") + ":从缓存获取成功");
            return component_access_token;
        }

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("component_appid", PropKit.get("third_appId"));
        paramMap.put("component_appsecret", PropKit.get("third_appSecret"));
        paramMap.put("component_verify_ticket", component_verify_ticket);
        String param = JSONObject.toJSONString(paramMap);
        logger.info("开始获取第三方平台component_access_token, url:{" + url + "}, param:{" + param + "}");
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);

        component_access_token = (String) retMap.get("component_access_token");
        //2、将token存入缓存{errcode=61004, errmsg=access clientip is not registered hint: [x0ZhJA0817e575] requestIP: 58.101.18.64}
        if (StringUtils.isEmpty(component_access_token)) {
            logger.info("获取第三方平台component_access_token失败, url:{" + url + "}, param:{" + param + "}，retMap:" + retMap.toString());
            return null;
        } else {
            Integer expires_in = (Integer) retMap.get("expires_in");//7200
            memcacheApi.setThirdToken(PropKit.get("third_appId"), component_access_token, expires_in);
            return component_access_token;
        }

    }


    /**
     * 3、获取预授权码pre_auth_code
     *
     * @return
     */
    public String get_pre_auth_code() {
        String component_access_token = get_component_access_token();
        if (StringUtils.isEmpty(component_access_token)) {
            logger.info("获取component_access_token为空,请排查原因");
            return "";
        }
        String url = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=" + component_access_token;

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("component_appid", PropKit.get("third_appId"));
        String param = JSONObject.toJSONString(paramMap);
        logger.info("获取预授权码pre_auth_code, url:{" + url + "}, param:{" + param + "}");
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("获取预授权码pre_auth_code成功,ret:{" + retMap.toString() + "}");

        return (String) retMap.get("pre_auth_code");
    }


    /**
     * 组装授权路径
     *
     * @return
     */
    public String buildAuthUrl() {
        //1、准备数据
        String weixin_server_url = PropKit.get("weixin_server_url");
//		String redirect_uri = weixin_server_url + "/third/gotoAuthCallback?storeId="+storeId;
        //2、组装路径返回
        String url = "https://mp.weixin.qq.com/cgi-bin/componentloginpage";
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("?component_appid=").append(PropKit.get("third_appId"));
        urlBuilder.append("&pre_auth_code=").append(get_pre_auth_code());
//   	 	urlBuilder.append("&redirect_uri=").append(redirect_uri);
//   	 redirect_uri = URLEncoder.encode(URLEncoder.encode(redirect_uri,"UTF-8"));
        return urlBuilder.toString();
    }

    /**
     * 授权成功回调处理
     *
     * @param auth_code
     */
    public String gotoAuthCallback(String auth_code, String auth_code_expires_in, String storeId) {
        String tipMsg = "";
        logger.info("======该处打印说明授权回调通知接收完成======================================================================"
            + "============================"
            + "授权回调gotoAuthCallback成功,auth_code:{" + auth_code + "}, auth_code_expires_in:{" + auth_code_expires_in + "}");

        String third_appId = PropKit.get("third_appId");
        if (StringUtils.isEmpty(auth_code)) {
            return "授权码不能为空";
        }

        //1、根据授权码换取获取授权者信息
        String returnStr = get_authorization_info(auth_code);
        if (StringUtils.isEmpty(returnStr)) {
            return "获取授权者信息失败";
        }
        JSONObject jsonObj = JSONObject.parseObject(returnStr);
        JSONObject authorization_info_jsonObj = jsonObj.getJSONObject("authorization_info");
        if (authorization_info_jsonObj == null) {
            return "获取授权信息失败";
        }
        String authorizer_appid = authorization_info_jsonObj.getString("authorizer_appid");
        String authorizer_access_token = authorization_info_jsonObj.getString("authorizer_access_token");
        Integer expires_in = (Integer) authorization_info_jsonObj.get("expires_in");
        String authorizer_refresh_token = authorization_info_jsonObj.getString("authorizer_refresh_token");
        JSONArray func_info_array = authorization_info_jsonObj.getJSONArray("func_info");
        logger.info("authorizer_appid{" + authorizer_appid + "}，authorizer_access_token{" + authorizer_access_token + "},expires_in{" + expires_in + "},"
            + "authorizer_refresh_token{" + authorizer_refresh_token + "}，func_info{" + func_info_array.toJSONString() + "}");

        //2、获取授权方的帐号基本信息、进行授权
        if (storeId != null) {
            tipMsg = api_get_authorizer_info(authorizer_appid, storeId);
        }

        //3、将token放入缓存并将刷新缓存放入数据库
        memcacheApi.setAccessTokenAndRefreshToken(authorizer_appid, authorizer_access_token, expires_in, authorizer_refresh_token);
        logger.info("token放入了缓存码完成，authorizer_access_token:{" + authorizer_access_token + "}，authorizer_refresh_token：" + authorizer_refresh_token);


        try {
            xbWebViewDomain(authorizer_appid, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tipMsg;
    }


    /**
     * 给某个小程序设置业务域名
     *
     * @param appId appId
     * @return java.lang.String
     * @author Aison
     * @date 2018/7/26 13:34
     */
    public String xbWebViewDomain(String appId, String domain) {


        logger.info("配置域名domain=" + domain);
        domain = domain == null ? PropKit.get("webviewdomain") : domain;
        logger.info("最终域名domain=" + domain);
        String accessToken = get_authorizer_token(appId);
        String url = "https://api.weixin.qq.com/wxa/setwebviewdomain?access_token=" + accessToken;
        Map<String, Object> paramMap = new HashMap<String, Object>(2);
        paramMap.put("action", "add");
        paramMap.put("webviewdomain", domain.split(","));
        String param = JSONObject.toJSONString(paramMap);
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("添加业务域名返回结果 :" + retMap.toString());
        return retMap.toString();
    }


    /**
     * 4、使用授权码换取公众号或小程序的接口调用凭据和授权信息
     *
     * @param auth_code
     * @return 授权者APPID authorizer_appid
     */
    public String get_authorization_info(String auth_code) {
        String third_appId = PropKit.get("third_appId");
        //获取第三方平台component_access_token
        String component_access_token = get_component_access_token();
        String url = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=" + component_access_token;
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("component_appid", third_appId);
        paramMap.put("authorization_code", auth_code);
        String param = JSONObject.toJSONString(paramMap);
//		String param = "{\"component_appid\":"+component_appid+",\"authorization_code\":"+authorization_code+"}";
        logger.info("开始获取授权信息, url:{" + url + "}, param:{" + param + "}");

        String returnStr = WapPayHttpUtil.sendPostHttp(url, param);

        logger.info("获取授权信息authorization_info成功, url:{" + url + "}, param:{" + param + "}，returnStr：{" + returnStr + "}");

        return returnStr;
    }

    /**
     * 5、获取小程序授权方的接口调用凭据（令牌）
     */
    public String get_authorizer_token(String authorizer_appid) {

//		String third_appId = PropKit.get("third_appId");
//		IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
//		String authorizer_access_token = accessTokenCache.get(authorizer_access_token_groupKey  + third_appId  + authorizer_appid);

        String authorizer_access_token = memcacheApi.getAccessToken(authorizer_appid);
        if (StringUtils.isEmpty(authorizer_access_token)) {
            //刷新token
            refresh_authorizer_token(authorizer_appid);
            authorizer_access_token = memcacheApi.getAccessToken(authorizer_appid);
            logger.info("刷新了缓存，authorizer_appid：" + authorizer_appid + ",authorizer_access_token=" + authorizer_access_token);
        }
        return authorizer_access_token;

    }

//    public static void main(String[] args) {
//    	thirdApi.refresh_authorizer_token("wxfc022fadb7c14600");
//	}

    /**
     * 5、刷新token授权公众号或小程序的接口调用凭据（令牌）
     * TODO 需要采用刷新token进行刷新最新token 待实现
     * refreshtoken@@@gx2gqYP5IpxaTjNTMH62OxlrpQJaDvL_pybg7EzOOHc
     */
    public Map<String, Object> refresh_authorizer_token(String authorizer_appid) {
        //1、准备数据
        String third_appId = PropKit.get("third_appId");
//    	String third_authorizer1_appId = PropKit.get("third_authorizer1_appId");
        String component_access_token = get_component_access_token();
        String url = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=" + component_access_token;
//		IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
//		String authorizer_refresh_token = accessTokenCache.get(authorizer_refresh_token_groupKey  + third_appId  + authorizer_appid);
//		String authorizer_refresh_token = memcachedService.getStr(authorizer_refresh_token_groupKey, authorizer_refresh_token_groupKey + authorizer2_appid);
        String authorizer_refresh_token = memcacheApi.getRefreshToken(authorizer_appid);

        //组装参数调用接口
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("component_appid", third_appId);
        paramMap.put("authorizer_appid", authorizer_appid);
        paramMap.put("authorizer_refresh_token", authorizer_refresh_token);
        String param = JSONObject.toJSONString(paramMap).toString();
        logger.info("开始获取小程序接口调用凭证api_authorizer_token, url:{" + url + "}, param:{" + param + "}");
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("获取小程序接口调用凭证api_authorizer_token, url:{" + url + "}, param:{" + param + "}，注意排查是否有错误retMap{" + retMap + "}");
        //解析返回值，将token放入缓存
        String new_authorizer_access_token = (String) retMap.get("authorizer_access_token");
        Integer expires_in = (Integer) retMap.get("expires_in");
        String new_authorizer_refresh_token = (String) retMap.get("authorizer_refresh_token");
        if (StringUtils.isNotEmpty(new_authorizer_access_token)) {
            memcacheApi.setAccessTokenAndRefreshToken(authorizer_appid, new_authorizer_access_token, expires_in, authorizer_refresh_token);
            logger.info("token放入了缓存码，new_authorizer_access_token:{" + new_authorizer_access_token + "}，new_authorizer_refresh_token：" + new_authorizer_refresh_token);
            //将缓存存入数据库
            MemberService.setRefreshToken(authorizer_appid, authorizer_refresh_token);
        } else {
            logger.info("刷新缓存失败!!!!!!!!!!!!!!!!!!!!!!!尽快排查问题，建议该小程序尽快重新进行授权,retMap{" + retMap + "}");
        }


        return retMap;
    }


    /**
     * 6、获取授权方的帐号基本信息
     * 返回结果示例
     * {
     * "authorizer_info": {
     * "nick_name": "微信SDK Demo Special",//授权方昵称
     * "head_img": "http://wx.qlogo.cn/mmopen/GPy",//授权方头像
     * "service_type_info": {//授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
     * "id": 2
     * },
     * "verify_type_info": {//授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
     * "id": 0
     * },
     * "user_name": "gh_eb5e3a772040",//授权方公众号的原始IDgh_eb5e3a772040
     * "principal_name": "腾讯计算机系统有限公司",//公众号的主体名称
     * "alias": "paytest01",//授权方公众号所设置的微信号，可能为空
     * "business_info": {"open_store": 0, "open_scan": 0, "open_pay": 0, "open_card": 0,"open_shake": 0
     * },//用以了解以下功能的开通状况（0代表未开通，1代表已开通）：open_store:是否开通微信门店功能、 open_scan:是否开通微信扫商品功能、 open_pay:是否开通微信支付功能、 open_card:是否开通微信卡券功能、open_shake:是否开通微信摇一摇功能
     * "qrcode_url": "URL", //二维码图片的URL，开发者最好自行也进行保存
     * },
     * "authorization_info": {//授权信息
     * "appid": "wxf8b4f85f3a794e77",//授权方appid
     * "func_info": [//公众号授权给开发者的权限集列表，ID为1到15时分别代表：1消息管理权限、2用户管理权限、3帐号服务权限、4网页服务权限、5微信小店权限、6微信多客服权限、7群发与通知权限、8微信卡券权限、9微信扫一扫权限、10微信连WIFI权限、11素材管理权限、12微信摇周边权限、13微信门店权限、14微信支付权限、15自定义菜单权限
     * {"funcscope_category": {"id": 1 }},{ "funcscope_category": {"id": 2}}, { "funcscope_category": { "id": 3}}
     * ]
     * }
     * }
     *
     * @return
     */
    public String api_get_authorizer_info(String authorizer_appid, String storeId) {
        String tipMsg = null;
        //1、准备数据
        String third_appId = PropKit.get("third_appId");
        String component_access_token = get_component_access_token();
        String url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=" + component_access_token;
        //2、组装参数，发送请求
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("component_appid", third_appId);
        paramMap.put("authorizer_appid", authorizer_appid);
        String param = JSONObject.toJSONString(paramMap);
//		String param = "{\"component_appid\":"+component_appid+",\"authorizer_appid\":"+authorizer_appid1+"}";
        logger.info("开始获取授权方的帐号基本信息api_get_authorizer_info, url:{" + url + "}, param:{" + param + "}");
        String returnStr = WapPayHttpUtil.sendPostHttp(url, param);
//    	Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("获取授权方的帐号基本信息api_get_authorizer_info成功, url:{" + url + "}, param:{" + param + "}，returnStr{" + returnStr + "}");
        //3、解析JSON
        if (returnStr != null) {//具体JSON见文件底部
//    		JSONObject jsonObj = JSONObject.parseObject(returnStr);
//    		JSONObject authorizer_info = jsonObj.getJSONObject("authorizer_info");
//    		String nick_name = null;
//			String head_img = null;;
//			String user_name = null;;
//			String principal_name = null;;
//			String alias = null;;
//			String qrcode_url = null;;
//    		if(authorizer_info != null){
//    			nick_name = authorizer_info.getString("nick_name");
//    			head_img = authorizer_info.getString("head_img");
//    			user_name = authorizer_info.getString("user_name");
//    			principal_name = authorizer_info.getString("principal_name");
//    			alias = authorizer_info.getString("alias");
//    			qrcode_url = authorizer_info.getString("qrcode_url");
//    		}
//    		JSONObject authorization_info = jsonObj.getJSONObject("authorization_info");

            //4、判断是否是小程序授权
            JSONObject jsonObj = JSONObject.parseObject(returnStr);
            JSONObject authorizer_info = jsonObj.getJSONObject("authorizer_info");
            JSONObject MiniProgramInfo = authorizer_info.getJSONObject("MiniProgramInfo");
            if (MiniProgramInfo != null) {//有该字段则为小程序授权，TODO 暂时这样判断，后续继续查找有哪个字段可以表示是小程序授权
                //小程序授权
                String ret = new ShopService().wxaAuth(storeId, authorizer_appid, returnStr);

                tipMsg = ret;
            } else {
                logger.info("授权方不为小程序不能进行绑定，请提醒用户！");
                tipMsg = "您授权的不是小程序，请重新选择小程序授权！";
            }
        } else {
            tipMsg = "获取授权基本信息失败，请排查问题！";
        }
        return tipMsg;
    }


    /**
     * 7、获取授权方的选项设置信息 TODO 待使用
     *
     * @param option_name
     * @return
     */
    public Map<String, Object> api_get_authorizer_option(String option_name) {
        String third_appId = PropKit.get("third_appId");
        String third_authorizer1_appId = PropKit.get("third_authorizer1_appId");
        String component_access_token = get_component_access_token();
        String url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_option?component_access_token=" + component_access_token;

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("component_appid", third_appId);
        paramMap.put("authorizer_appid", third_authorizer1_appId);
        paramMap.put("option_name", option_name);
        String param = JSONObject.toJSONString(paramMap).toString();
        logger.info("开始授权信息api_get_authorizer_option, url:{" + url + "}, param:{" + param + "}");

        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("获取授权信息api_get_authorizer_option成功, url:{" + url + "}, param:{" + param + "}，retMap{" + retMap.toString() + "}");

        String new_authorizer_appid = (String) retMap.get("authorizer_appid");
        String new_option_name = (String) retMap.get("option_name");
        String option_value = (String) retMap.get("option_value");

        return retMap;
    }

    /**
     * 8、设置授权方的选项信息
     *
     * @param option_name
     * @param option_value
     * @return
     */
    public Map<String, Object> api_set_authorizer_option(String option_name, String option_value) {
        String third_appId = PropKit.get("third_appId");
        String third_authorizer1_appId = PropKit.get("third_authorizer1_appId");
        //获取第三方平台component_access_token
        String component_access_token = get_component_access_token();
        String url = "https://api.weixin.qq.com/cgi-bin/component/api_set_authorizer_option?component_access_token=" + component_access_token;

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("component_appid", third_appId);
        paramMap.put("authorizer_appid", third_authorizer1_appId);
        paramMap.put("option_name", option_name);
        paramMap.put("option_value", option_value);
        String param = JSONObject.toJSONString(paramMap);
        logger.info("开始api_set_authorizer_option, url:{" + url + "}, param:{" + param + "}");

        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("api_set_authorizer_option成功, url:{" + url + "}, param:{" + param + "}，retMap{" + retMap.toString() + "}");

        String errcode = (String) retMap.get("errcode");
        String errmsg = (String) retMap.get("errmsg");

        return retMap;
    }

    /**
     * 9、推送授权相关通知（暂时没有用到）TODO 待实现取消授权更改授权等业务
     * 当公众号对第三方平台进行授权、取消授权、更新授权后，微信服务器会向第三方平台方的授权事件接收URL（创建第三方平台时填写）推送相关通知。
     * 第三方平台方在收到授权相关通知后也需进行解密（详细请见【消息加解密接入指引】），接收到后之后只需直接返回字符串success。为了加强安全性，postdata中的xml将使用服务申请时的加解密key来进行加密，具体请见【公众号第三方平台的加密解密技术方案】
     * POST数据示例（取消授权通知）
     * <xml>
     * <AppId>第三方平台appid</AppId>//第三方平台appid
     * <CreateTime>1413192760</CreateTime>//时间戳
     * <InfoType>unauthorized</InfoType>//unauthorized是取消授权，updateauthorized是更新授权，authorized是授权成功通知
     * <AuthorizerAppid>公众号appid</AuthorizerAppid>公众号或小程序
     * </xml>
     * POST数据示例（授权成功通知）
     * <xml>
     * <AppId>第三方平台appid</AppId>
     * <CreateTime>1413192760</CreateTime>
     * <InfoType>authorized</InfoType>
     * <AuthorizerAppid>公众号appid</AuthorizerAppid>
     * <AuthorizationCode>授权码（code）</AuthorizationCode>//授权码，可用于换取公众号的接口调用凭据，详细见上面的说明
     * <AuthorizationCodeExpiredTime>过期时间</AuthorizationCodeExpiredTime>//授权码过期时间
     * </xml>
     * POST数据示例（授权更新通知）
     * <xml>
     * <AppId>第三方平台appid</AppId>
     * <CreateTime>1413192760</CreateTime>
     * <InfoType>updateauthorized</InfoType>
     * <AuthorizerAppid>公众号appid</AuthorizerAppid>
     * <AuthorizationCode>授权码（code）</AuthorizationCode>
     * <AuthorizationCodeExpiredTime>过期时间</AuthorizationCodeExpiredTime>
     * </xml>
     */
    public void wxaAuthNotification(String bodyData) {
        logger.info("微信推送授权相关通知成功, bodyData:{" + bodyData + "}");
        Map<String, String> params = WeixinPayCore.decodeXml(bodyData);
        String AppId = params.get("AppId");
        String CreateTime = params.get("CreateTime");
        String InfoType = params.get("InfoType");
        String AuthorizerAppid = params.get("AuthorizerAppid");
        String AuthorizationCode = params.get("AuthorizationCode");
        String AuthorizationCodeExpiredTime = params.get("AuthorizationCodeExpiredTime");
        logger.info("微信推送component_verify_ticket成功, AppId:{" + AppId + "}, CreateTime:{" + CreateTime
            + "}, InfoType:{" + InfoType + "}, AuthorizerAppid:{" + AuthorizerAppid + "},AuthorizationCode{" + AuthorizationCode
            + "},AuthorizationCodeExpiredTime{" + AuthorizationCodeExpiredTime + "}");

        new ShopService().wxaAuthNotification(params);
    }

    /**
     * 获取sessionKey
     *
     * @param jsCode 登录时获取的 code
     * @return ApiResult
     */
    public ApiResult getSessionKey(String jsCode) {
        ThirdWxaConfig twc = ThirdWxaConfigKit.getThirdWxaConfig();
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", twc.getAppId());
        params.put("secret", twc.getAppSecret());
        params.put("js_code", jsCode);
        params.put("grant_type", "authorization_code");
        String para = PaymentKit.packageSign(params, false);
        // 构造url
        String url = jsCode2sessionUrl + "?" + para;
        return new ApiResult(HttpUtils.get(url));
    }

    /**
     * 解密用户敏感数据
     *
     * @param sessionKey    会话密钥
     * @param encryptedData 明文
     * @param ivStr         加密算法的初始向量
     * @return {ApiResult}
     */
    public ApiResult getUserInfo(String sessionKey, String encryptedData, String ivStr) {
        WxaBizDataCrypt dataCrypt = new WxaBizDataCrypt(sessionKey);
        String json = dataCrypt.decrypt(encryptedData, ivStr);
        return new ApiResult(json);
    }

    /**
     * 验证用户信息完整性
     *
     * @param sessionKey 会话密钥
     * @param rawData    微信用户基本信息
     * @param signature  数据签名
     * @return {boolean}
     */
    public boolean checkUserInfo(String sessionKey, String rawData, String signature) {
        StringBuffer sb = new StringBuffer(rawData).append(sessionKey);
        String encryData = HashKit.sha1(sb.toString());
        return encryData.equals(signature);
    }


//    小程序授权获取基本信息返回json
//    {
//        "authorizer_info":{
//            "nick_name":"俞姐姐",
//            "head_img":"http://wx.qlogo.cn/mmopen/prr6sKAYIZfFVPIDX8pwLmXALNicofERID5KibBnUJ2ZgVAMibXSViccevntjAr7sozYHPFx8iatPjqgCiafogTqJKJMcIicFxhwRMZ/0",
//            "service_type_info":{
//                "id":0
//            },
//            "verify_type_info":{
//                "id":0
//            },
//            "user_name":"gh_8403ef7eae6a",
//            "alias":"",
//            "qrcode_url":"http://mmbiz.qpic.cn/mmbiz_jpg/mK33gUmo8axz5qJCnA72YpiahbmnnNlkqu6FTaibVTeUNJ2ibnZ3gPmxyc3QMLww65bo2qLnibJOSnv2QkwUobVmicA/0",
//            "business_info":{
//                "open_pay":0,
//                "open_shake":0,
//                "open_scan":0,
//                "open_card":0,
//                "open_store":0
//            },
//            "idc":1,
//            "principal_name":"杭州玖远网络科技有限公司",
//            "signature":"俞姐姐女装购物平台",
//            "MiniProgramInfo":{
//                "network":{
//                    "RequestDomain":[
//                        "https://weixinlocal.yujiejie.com"
//                    ],
//                    "WsRequestDomain":[
//                        "wss://weixinlocal.yujiejie.com"
//                    ],
//                    "UploadDomain":[
//                        "https://weixinlocal.yujiejie.com"
//                    ],
//                    "DownloadDomain":[
//                        "https://weixinlocal.yujiejie.com"
//                    ]
//                },
//                "categories":[
//
//                ],
//                "visit_status":0
//            }
//        },
//        "authorization_info":{
//            "authorizer_appid":"wx0927d4005f4e45df",
//            "authorizer_refresh_token":"refreshtoken@@@m_zQjCkbwUMZd7YOx7kR2N1WFwTUBdzpZ-iRikSprRI",
//            "func_info":[
//                {
//                    "funcscope_category":{
//                        "id":17
//                    }
//                },
//                {
//                    "funcscope_category":{
//                        "id":18
//                    }
//                },
//                {
//                    "funcscope_category":{
//                        "id":19
//                    }
//                }
//            ]
//        }
//    }


//    {公众号授权获取基本信息返回json
//        "authorizer_info":{
//            "nick_name":"董仲",
//            "head_img":"http://wx.qlogo.cn/mmopen/prr6sKAYIZfFVPIDX8pwLtMODpZdz4Yu9ykaCnr7XnwuId0bpZ8Qjyz4REMxD5LmPBHQzdKmBia04urUicMo7iaUF7IlPvgHX68/0",
//            "service_type_info":{
//                "id":1
//            },
//            "verify_type_info":{
//                "id":-1
//            },
//            "user_name":"gh_c72098fb7f1a",
//            "alias":"dongzhong0613",
//            "qrcode_url":"http://mmbiz.qpic.cn/mmbiz_jpg/ojb264SFhxnfItW2ibxonyPI3zLdvjQLqHa8htWfKqj7PWbtffAKTWXnoNUsAJoZq66yUXxEbbxOn8TrYkMsG1w/0",
//            "business_info":{
//                "open_pay":0,
//                "open_shake":0,
//                "open_scan":0,
//                "open_card":0,
//                "open_store":0
//            },
//            "idc":1,
//            "principal_name":"个人",
//            "signature":"个人玩玩"
//        },
//        "authorization_info":{
//            "authorizer_appid":"wxaecba8efaa94e2a1",
//            "authorizer_refresh_token":"refreshtoken@@@QNjizj-Zehp219WrP8I6VHBLotqWyADKIGW6duRwyi8",
//            "func_info":[ ]
//        }
//    }


}
