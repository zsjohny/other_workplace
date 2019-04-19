package com.jiuyuan.util.oauth.sns.weixin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.function.ToDoubleBiFunction;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.StreamingHttpOutputMessage;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.util.EncodeUtil;
import com.jiuyuan.constant.Tristate;
import com.jiuyuan.constant.WapPublicConstants;
import com.jiuyuan.service.common.HttpClientService;
import com.jiuyuan.util.http.component.CachedHttpResponse;
import com.jiuyuan.util.oauth.common.Display;
import com.jiuyuan.util.oauth.common.credential.IClientCredentials;
import com.jiuyuan.util.oauth.common.credential.ICredentials;
import com.jiuyuan.util.oauth.common.credential.IRawDataCredentials;
import com.jiuyuan.util.oauth.sns.common.response.ISnsResponse;
import com.jiuyuan.util.oauth.sns.common.status.IImageStatus;
import com.jiuyuan.util.oauth.sns.common.status.IStatus;
import com.jiuyuan.util.oauth.sns.common.user.AccessToken;
import com.jiuyuan.util.oauth.sns.common.user.IAccessToken;
import com.jiuyuan.util.oauth.sns.common.user.ISnsEndUser;
import com.jiuyuan.util.oauth.sns.common.user.SnsEndUser;
import com.jiuyuan.util.oauth.sns.common.user.WapWeixinUser;
import com.jiuyuan.util.oauth.sns.weixin.WapWeiXinResponse;
import com.jiuyuan.util.oauth.sns.weixin.WeiXinV2Response;
import com.jiuyuan.util.oauth.v2.V2Config;
import com.yujj.util.oauth.sns.common.api.ISnsV2API;
import com.yujj.util.oauth.v2.AbstractV2API;
import com.yujj.util.uri.UriBuilder;
import com.yujj.util.uri.UriParams;
//import com.yujj.web.controller.wap.pay2.WapPublicConstants;

/**
 * <pre>
 * 微信接口
 * 
 * 注意：微信所有接口的token credentials，都需要指定一个openid。
 * </pre>
 */
public class WeiXinV2API extends AbstractV2API implements ISnsV2API {
	
	//公众号的唯一标识
//	private static String wapAppid = "wxe1169bab39d015c6";//测试
//	private static String wapAppid = "wx95c37c75c641bc5e";//正式
	//公众号的appsecret
//	private static String wapSecret = "3c7958b79744fcba4dcf0db802781e9f";//测试
//	private static String wapSecret = "53d5e770f10dc22567480538f3a4d676";//正式
	

    public WeiXinV2API(HttpClientService httpClientService, String authorizeUri, String authorizeScope,
                       IClientCredentials clientCredentials) {
        super(httpClientService, buildConfig(authorizeUri, authorizeScope), clientCredentials);
    }

    private static V2Config buildConfig(String authorizeUri, String authorizeScope) {
        V2Config config = new V2Config();
        config.setAuthorizeUri(authorizeUri);
        config.setAccessTokenUri("https://api.weixin.qq.com/sns/oauth2/access_token");
        config.setDefaultCharset("UTF-8");
        config.setDefaultHttpParams(null);
        config.setAuthorizeScope(authorizeScope);
        return config;
    }

    @Override
    protected void updateAuthorizeUrl(UriBuilder builder, Display display) {
        builder.set("appid", builder.getParams().getSingle(CLIENT_ID));
        builder.remove(CLIENT_ID);
    }

    // 微信授权链接，尤其注意：由于授权操作安全等级较高，所以在发起授权请求时，微信会对授权链接做正则强匹配校验，如果链接的参数顺序不对，授权页面将无法正常访问
    public String getAuthorizeUrlFit(UriParams callbackParams, String state, Display display) {
        StringBuilder urlBuilder = new StringBuilder(config.getAuthorizeUri());
        urlBuilder.append("?appid=").append(clientCredentials.getIdentifier());
        String callbackUrl = getCallbackUrl(callbackParams);
        String charset = config.getDefaultCharset();
        urlBuilder.append("&redirect_uri=").append(EncodeUtil.encodeURL(callbackUrl, charset));
        urlBuilder.append("&response_type=code");
        urlBuilder.append("&scope=").append(config.getAuthorizeScope());
        urlBuilder.append("&state=").append(EncodeUtil.encodeURL(state, charset));
        return urlBuilder.toString();
    }
    
   

    @Override
    protected void updateAccessTokenParams(UriParams params) {
        params.set("appid", params.getSingle(CLIENT_ID)).remove(CLIENT_ID);
        params.set("secret", params.getSingle(CLIENT_SECRET)).remove(CLIENT_SECRET);
    }

    protected String extractOpenId(ICredentials tokenCredentials) {
        IRawDataCredentials rawDataCredentials = (IRawDataCredentials) tokenCredentials;
        String openId = rawDataCredentials.getRawDataAsString("openid");
        if (StringUtils.isNotBlank(openId)) {
            return openId;
        }
        throw new IllegalArgumentException("Token credentials must contain a non-empty value for key 'openid'.");
    }

    @Override
    public WeiXinV2Response<Void> publish(ICredentials tokenCredentials, IStatus status, String clientIP) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ISnsResponse<Void> publishWithImage(ICredentials tokenCredentials, IStatus status, String imageFileName,
                                               byte[] imageBytes, String clientIP) {
        throw new UnsupportedOperationException();
    }

    public WeiXinV2Response<Void> publishWithImage(ICredentials tokenCredentials, IImageStatus status, String clientIP) {
        throw new UnsupportedOperationException();
    }

    
    @Override
    public WeiXinV2Response<ISnsEndUser> getEndUser(ICredentials tokenCredentials, String clientIP) {
        String openId = extractOpenId(tokenCredentials);
        String baseUri = "https://api.weixin.qq.com/sns/userinfo";
        UriParams params = new UriParams().set("access_token", tokenCredentials.getIdentifier()).set("openid", openId);

        WeiXinV2Response<ISnsEndUser> response =
            new WeiXinV2Response<ISnsEndUser>(httpGet(baseUri, params, tokenCredentials));
        if (response.getResponseType().isSuccessful()) {
            try {
                JSONObject obj = JSONObject.parseObject(response.getResponseText());
                SnsEndUser user = buildEndUser(obj);
                response.setData(user);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to get user, response: " + response.getResponseText(), e);
            }
        }

        return response;
    }
    /**
     * 获取静默授权URL
     * @return
     */
    public String getWapAuthorizeUrlBySilent(HttpServletRequest request,String redirect_uri) throws UnsupportedEncodingException {
    	return getWapAuthorizeUrl( request, redirect_uri,  "1" ,  "1");
    }
    
    /**
     * 获取非静默授权URL
     * @return
     */
    public String getWapAuthorizeUrlByNoSilent(HttpServletRequest request,String redirect_uri) throws UnsupportedEncodingException {
    	return getWapAuthorizeUrl( request, redirect_uri,  "1" ,  "0");
    }

    	
    
    /**
     * 第一步：用户同意授权，获取code
     * var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1169bab39d015c6&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
     *  如果是非静默登陆则需要在授权之后跳转到手机绑定页面（/static/wap/bound1.html）
     * @param redirect_uri
     * @param state 授权成功后的状态码
     * @param isSilent 是否静默 
     * @return
     */
    public String getWapAuthorizeUrl(HttpServletRequest request,String redirect_uri, String state , String isSilent) throws UnsupportedEncodingException {
    	// TODO路径先写死后期写在配置文件中
      
    	StringBuilder urlBuilder = new StringBuilder(WapPublicConstants.authorizeUrl);
    	//公众号的唯一标识
        urlBuilder.append("?appid=").append(WapPublicConstants.APPID);
//        String callbackUrl = getCallbackUrl(callbackParams);
//        String charset = config.getDefaultCharset();
        String scope = "snsapi_base";
        if(isSilent.equals("1")){
       	 	scope = "snsapi_base";
       }else{
    	    scope = "snsapi_userinfo";
       }
        //授权后重定向的回调链接地址，请使用urlEncode对链接进行处理
        redirect_uri = EncodeUtil.encodeURL(redirect_uri);
        urlBuilder.append("&redirect_uri=").append(redirect_uri);//前端已经urlEncode//.append(EncodeUtil.encodeURL(callbackUrl, charset));
        //返回类型，请填写code
        urlBuilder.append("&response_type=code");
        //应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
       
        
        urlBuilder.append("&scope="+scope);//.append(config.getAuthorizeScope());
        //重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
        urlBuilder.append("&state=").append(state);//.append(EncodeUtil.encodeURL(state, charset));
        //无论直接打开还是做页面302重定向时候，必须带此参数
        urlBuilder.append("#wechat_redirect");
        return urlBuilder.toString();
    }
    


	/**
	 * 第四步：拉取用户信息(需scope为 snsapi_userinfo)
	 * 如果网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openid拉取用户信息了。
	 * 请求方法：http：GET（请使用https协议） https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN 
	 * https://mp.weixin.qq.com/wiki
	 * @param weixinId
	 * //2、拉取用户信息
//        		String access_token = accessToken.getAccessToken();
//        		WapWeixinUser wapWeixinUser = weiXinV2API.getWapWeixinUser(access_token,weixinId);
//               	if(wapWeixinUser == null ){
//               		return jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);
//               	}
	 * @return
	 */
	public WapWeixinUser getWapWeixinUser(String access_token ,String weixinId) {
		 String wapUserinfoUrl = getWapUserinfoUrl(access_token, weixinId);
		 CachedHttpResponse httpGetResponse = httpGet(wapUserinfoUrl,new UriParams());
		 WapWeiXinResponse<WapWeixinUser> response =new WapWeiXinResponse<WapWeixinUser>(httpGetResponse);
		 if (response.getResponseType().isSuccessful()) {
			 try {
	                JSONObject obj = JSONObject.parseObject(response.getResponseText());
	                WapWeixinUser user = buildWapWeixinUser(obj);
	                response.setData(user);
	            } catch (Exception e) {
	                throw new IllegalStateException("getWapAccessToken: Failed to get AccessToken, response: " + response.getResponseText(), e);
	            }
	     }
		 return response.getData();
	}
	
	/**
     * 第二步：通过code换取网页授权access_token
     * 获取code后，请求以下链接获取access_token：  https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code 
     * @param code
     * @return
     */
	public IAccessToken getWapAccessToken(String code) {
		String accessTokenUrl = getWapAccessTokenUrl(code);
		 CachedHttpResponse httpGetResponse = httpGet(accessTokenUrl,new UriParams());
		 WapWeiXinResponse<IAccessToken> response =new WapWeiXinResponse<IAccessToken>(httpGetResponse);
		 if (response.getResponseType().isSuccessful()) {
			 try {
	                JSONObject obj = JSONObject.parseObject(response.getResponseText());
	                AccessToken accessToken = buildAccessToken(obj);
	                response.setData(accessToken);
	            } catch (Exception e) {
	                throw new IllegalStateException("getWapAccessToken: Failed to get AccessToken, response: " + response.getResponseText(), e);
	            }
	     }
		 return response.getData();
	}
	/**
     * 通过code换取网页授权access_tokenURL路径
     * 获取code后，请求以下链接获取access_token：  https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code 
     * @param code
     * @param isSilent 是否静默
     * @return
     */
	private String getWapAccessTokenUrl(String code) {
		StringBuilder urlBuilder = new StringBuilder(WapPublicConstants.accessTokenUrl);
		//公众号的唯一标识
		urlBuilder.append("?appid=").append(WapPublicConstants.APPID);
		//公众号的appsecret
		urlBuilder.append("&secret=").append(WapPublicConstants.SECRET);
		//填写第一步获取的code参数
		urlBuilder.append("&code=").append(code);
		urlBuilder.append("&grant_type=authorization_code");
        return urlBuilder.toString();
	}
	/**
	 * 拉取用户信息URL
     * http：GET（请使用https协议） https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
     * @return
     */
	private String getWapUserinfoUrl(String access_token,String openid) {
		StringBuilder urlBuilder = new StringBuilder(WapPublicConstants.userinfoUrl);
		//网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
		urlBuilder.append("?access_token=").append(access_token);
		//用户的唯一标识
		urlBuilder.append("&openid=").append(openid);
		//返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
		urlBuilder.append("&lang=zh_CN");
        return urlBuilder.toString();
	}
	
	/**
	 * 解析账号令牌信息
	 * @param obj
	 * @return
	 */
	private AccessToken buildAccessToken(JSONObject obj) {
		AccessToken accessToken = new AccessToken();
		accessToken.setAccessToken(obj.getString("access_token"));
		accessToken.setExpiresIn(obj.getString("expires_in"));
		accessToken.setRefreshToken(obj.getString("refresh_token"));
		accessToken.setOpenid(obj.getString("openid"));
		accessToken.setScope(obj.getString("scope"));
        return accessToken;
    }
	
	
	/**
	 * 解析用户信息
	 * @param obj
	 * @return
	 */
	private WapWeixinUser buildWapWeixinUser(JSONObject obj) {
		WapWeixinUser user = new WapWeixinUser();
		user.setOpenid(obj.getString("openid"));//用户的唯一标识
		user.setNickname(escapeNickName(obj.getString("nickname")));//用户昵称
		user.setSex(obj.getString("sex"));//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
		user.setProvince(obj.getString("province"));//用户个人资料填写的省份
		user.setCity(obj.getString("city"));//普通用户个人资料填写的城市
		user.setCountry(obj.getString("country"));//国家，如中国为CN
		user.setHeadimgurl(obj.getString("headimgurl"));//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
		user.setPrivilege(obj.getString("privilege"));//用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
		user.setUnionid(obj.getString("unionid"));//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
        return user;
    }
	
	protected SnsEndUser buildEndUser(JSONObject obj) {
        SnsEndUser user = new SnsEndUser();

        String head = obj.getString("headimgurl");
        if (StringUtils.isNotBlank(head)) {
            user.setAvatar(head);
        }

        user.setSymbolicName(null);
        user.setHomePage(null);
        user.setRegisterTime(null);
        user.setDescription(null);
        user.setEmail(null);
        user.setId(obj.getString("openid"));
        user.setPlatformIndependentId(obj.getString("unionid"));
        user.setNickName(escapeNickName(obj.getString("nickname")));
        user.setRealName(null);

        Tristate male = Tristate.UNCERTAIN;
        int gender = obj.getIntValue("sex");
        if (gender == 1) {
            male = Tristate.YES;
        } else if (gender == 2) {
            male = Tristate.NO;
        }
        user.setMale(male);

        user.setVerified(Tristate.UNCERTAIN);
        return user;
    }

	/**
	 * 昵称转换
	 * @param text
	 * @return
	 */
	 public String escapeNickName(String text) {
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < text.length(); i++) {
	            char ch = text.charAt(i);
	            if (!Character.isHighSurrogate(ch) && !Character.isLowSurrogate(ch)) {
	                sb.append(ch);
	            }
	        }
	        return sb.toString();
	    }
	
}
