package com.jiuyuan.constant;

import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayConfig;
/**
 * 微信公众号相关常量配置
 * @author zhaoxinglin
 *
 * @version 2017年4月13日上午10:58:46
 */
public class WapPublicConstants{
	//公众账号ID
	public static String APPID = WeixinPayConfig.getAppId(PaymentType.WEIXINPAY_PUBLIC);
	//微信支付分配的商户号
	public static String MCH_ID =  WeixinPayConfig.getMchId(PaymentType.WEIXINPAY_PUBLIC);
	//异步回调地址
	public static String NOTIFY_URL =  WeixinPayConfig.getNotifyUrl(PaymentType.WEIXINPAY_PUBLIC);
	//商户支付密钥API Key。审核通过后，在微信发送的邮件中查看
	public static String WX_PAY_KEY = WeixinPayConfig.getApiKey(PaymentType.WEIXINPAY_PUBLIC);
	//公众账号ID 
	public static String SECRET = WeixinPayConfig.getPublicSecret();//"53d5e770f10dc22567480538f3a4d676";//正式
	
	
	public static String authorizeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize";
	public static String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
	public static String userinfoUrl = "https://api.weixin.qq.com/sns/userinfo";
	
	
	
	
	
	
	
	
	//public static String APPID = "wx95c37c75c641bc5e";//正式
//	public static String APPID = "wxe1169bab39d015c6";//测试
//	public static String SECRET = "3c7958b79744fcba4dcf0db802781e9f";//测试
	
		
	//微信支付分配的商户号
//	public static String MCH_ID = "1459044102";
	//异步回调地址
//	public static String PAY_NOTIFY_URL = "http://test.yujiejie.com//m/pay/wap/wxPayNotify.do";
	//商户支付密钥API Key。审核通过后，在微信发送的邮件中查看
//	public static String WX_PAY_KEY = "NnZ7chTcyBgGNPM3Yk2cESzmes53gqDV";

	
//	public static String APPID = "XXXXXXXXXXXX";
//	//受理商ID，身份标识
//	public static String MCHID = "XXXXXXXXXXXXxx";
//	//商户支付密钥Key。审核通过后，在微信发送的邮件中查看
//	public static String KEY = "XXXXXXXXXXXXXXXXX";
//	//JSAPI接口中获取openid，审核后在公众平台开启开发模式后可查看
//	public static String APPSECRET = "xxxxxxxxxxxxxx";
//	//重定向地址
//	public static String REDIRECT_URL = "http://XXXXXXXXXXXXXXXXXXX/callWeiXinPay";
//	//异步回调地址
//	public static String NOTIFY_URL = "http://XXXXXXXXXXXXXXXXXXXXXX/weixinPay_notify";
//	//web回调地址
//	public static String WEB_NOTIFY_URL = "http://XXXXXXXXXXXXXXXXXXXXXXXXX/weixinPay_notify";
	 
}
