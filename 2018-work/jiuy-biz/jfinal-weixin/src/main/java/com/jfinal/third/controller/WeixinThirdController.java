package com.jfinal.third.controller;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import com.jfinal.aop.Duang;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.third.api.ThirdApi;
import com.jfinal.third.api.ThirdMsgApi;
import com.jfinal.weixin.jiuy.cache.MemcacheApi;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.msg.in.InMsg;
import org.apache.commons.lang3.StringUtils;

/**
 * 第三方相关接口（提供微信调用接口内部接口，仅供微信系统回调）
 * @author zhaoxinglin
 */
public class WeixinThirdController  extends Controller {

	static Log logger = Log.getLog(WeixinThirdController.class);

    protected MemcacheApi memcacheApi = Duang.duang(MemcacheApi.class);


	// 微信用户接口api
	protected ThirdApi thirdApi = Duang.duang(ThirdApi.class);
	protected ThirdMsgApi thirdMsgApi = Duang.duang(ThirdMsgApi.class);
    private String inMsgXml = null;        // 本次请求 xml数据
    private InMsg inMsg = null;            // 本次请求 xml 解析后的 InMsg 对象


	/**
	 * http://dev.yujiejie.com/third/test
	 */
	public void test() {
		logger.info("第三方");

		 renderText("1");

		 return;
	}
//
//	 /**
//		* 消息事件回调  （消息 息与事件接收URL）
//		* @return
//	 * @throws Exception
//		*/
//		public void callback() throws Exception   {
//			String appid = getPara(0);
//			String signature = getPara("signature");
//			String timestamp = getPara("timestamp");
//			String nonce = getPara("nonce");
//			String echostr = getPara("echostr");
//			String openid = getPara("openid");
//			String encrypt_type = getPara("encrypt_type");
//			String msg_signature = getPara("msg_signature");
//			inMsgXml = HttpKit.readData(getRequest());
//
//			String third_appId = PropKit.get("third_appId");
//	    	String third_token = PropKit.get("third_token");
//	    	String third_encodingAesKey = PropKit.get("third_encodingAesKey");
//
//			//校验服务器可用性
//	    	if(StringUtils.isNotEmpty(echostr)){
//	            if(CheckUtil.checkSignature(signature, timestamp, nonce,third_token)){
//	                logger.info("校验通过");
//	                //处理微信发来的情况
//	    			String returnXmlStr = thirdMsgApi.msgCallback(appid,inMsgXml,signature,timestamp,nonce,echostr,openid,encrypt_type,msg_signature);//requestBody
//	    			renderText(returnXmlStr);
//	    			return;
//	            }else{
//	            	logger.info("校验不通过，请排查问题");
//	            }
//	    	}
//	    	renderText("success");
//	      	return;
//	    }

    /**
     * 根据APPid换取token
     *
     * @author Charlie
     * @date 2018/12/19 19:55
     */
    public void findAccessTokenByAppId() {
        String appId = getPara("appId");
        logger.info ("根据appId换取token appId={}"+appId);
        String token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(token)) {
            logger.info("getOnlineWxaQrcodeUrl获取小程序authorizer_token为空，请排查问题！！！！");
        }
        renderJson(token);
    }


    /**
     * 获取sessionKey(根据sessionId,appId)
     *
     * @param {sessionId}
     * @param {appId}
     * @author Charlie
     * @date 2018/12/27 11:50
     */
    public void findSessionKeyBySessionId() {
        logger.info("进入程序了");
        String sessionId = getPara("sessionId");
        String appId = getPara("appId");
        logger.info("获取sessionKey=sessionId={" + sessionId + "}，appId={" + appId+"}");
        if (appId==null ||"".equals(appId)) {
            Kv data = Kv.by("errcode", 500)
                .set("errmsg", "appId is blank");
            renderJson(data);
            return;
        }
        if (sessionId==null ||"".equals(sessionId)) {
            Kv data = Kv.by("errcode", 500)
                .set("errmsg", "sessionId is blank");
            renderJson(data);
            return;
        }
        String sessionJson = memcacheApi.get("wxa:session:" + sessionId + appId);
        logger.info("sessionJson from 缓存 " + sessionJson);
        if (StrKit.isBlank(sessionJson)) {
            Kv data = Kv.by("errcode", 500);
            logger.info("/findSessionKeyBySessionId 返回信息：" + data.toJson());
            renderJson(data);
            return;
        }
        ApiResult sessionResult = ApiResult.create(sessionJson);
        // 获取sessionKey
        String sessionKey = sessionResult.get("session_key");
        renderJson(sessionKey);
    }




    /**
	  *1、推送component_verify_ticket协议（每隔10分钟推送一次）
      * http://dev.yujiejie.com/third/component_verify_ticket
	 */
	public void component_verify_ticket() {
		String signature = getPara("signature");
		String timestamp = getPara("timestamp");
		String nonce = getPara("nonce");
		String encrypt_type = getPara("encrypt_type");
		String msg_signature = getPara("msg_signature");
		inMsgXml = HttpKit.readData(getRequest());
		logger.info ("接收微信推送 start inMsgXml="+inMsgXml);
		thirdApi.component_verify_ticket( inMsgXml, signature, timestamp, nonce, encrypt_type, msg_signature);
		renderText("success");
		return;
	}

	  /**
		 * 跳转到微信授权页面
		 * http://dev.yujiejie.com/third/gotoAuth
		 * http://173g7k2240.iok.la/third/gotoAuth
		 * http://173g7k2240.iok.la/jsp/auth.html
		 * https://weixintest.yujiejie.com/third/gotoAuth
		 * https://weixintest.yujiejie.com/jsp/test.jsp
		 * ticket@@@04CC4bF3wle0lmjk2h3IKin4-zM9C6-_gJColrNKcID06LFBcSuwyVryvJqVBgapckdU6zVA43GpM8d2le72Uw
		 */
//	public void gotoAuth(){
//	   String storeId = getPara("storeId");
//	   String authUrl = thirdApi.buildAuthUrl(storeId);
//	   logger.info("跳转到公众号授权页面URL:"+authUrl);
//	   redirect(authUrl);
//	   return ;
//	}







	/**
	* 接收授权通知（）
	* @return
	*/
	public void wxaAuthNotification() throws UnsupportedEncodingException {
		String bodyData = HttpKit.readData(getRequest());
    	 thirdApi.wxaAuthNotification(bodyData);
    	 renderText("成功啦！！！");
      	 return;
      }





//======================================== 获取授权方的accessToken start 18/10/12 暂时没用,测试通过 ========================================
    //    private static final String KEY = "yjj_weixin";
    /**
     * 获取授权方的accessToken
     *
     * 参数 ytoken
     * 参数 ysecret
     * 参数 appId
     * @author Charlie
     * @date 2018/10/12 11:11
     */
    /*public void componentAccessToken() {
        String appId = getPara("appId");
        String code = getPara("code");
        String ytoken = getPara("ytoken");
        logger.info ("获取授权方的accessToken -- ytoken="+ytoken+",appId="+appId+",code="+code);
        if (StrKit.isBlank(ytoken)) {
            renderJson("");
            return;
        }
        if (StrKit.isBlank(code)) {
            renderJson("");
            return;
        }
        if (StrKit.isBlank(appId)) {
            renderJson("");
            return;
        }
        //这里可以考虑的更复杂的,比如双重加密,增加签名...但是会慢,所以简单的
        String token = md5 (appId + code + KEY);
        boolean ytokenIsTrue = ytoken.equals (token);
        logger.info ("ytokenIsTrue="+ytokenIsTrue );
        if (! ytokenIsTrue) {
            renderJson("");
            return;
        }
        String authorizer_token = thirdApi.get_authorizer_token (appId);
        logger.info ("authorizer_token="+authorizer_token);
        renderJson(authorizer_token);
    }


    public static String md5(String sourceStr) {
        String result = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer("");

            for(int offset = 0; offset < b.length; ++offset) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }

            result = buf.toString();
        } catch (NoSuchAlgorithmException var7) {
            var7.printStackTrace();
        }

        return result.toUpperCase();
    }*/
    //======================================== 获取授权方的accessToken start ========================================

}
