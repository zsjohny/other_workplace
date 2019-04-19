package com.e_commerce.miscroservice.payment.wx.config;

/**
 * 描述 wx的一些重要参数配置
 * @author hyq
 * @date 2018/9/21 13:46
 * @return
 */
public class WeChatConfig {

	// 公众账号ID
	public static final String APPID = "wx6ad169bccc57554a";
	// 商户号
	public static final String MCH_ID = "1403320302";
	// 商户密钥
	public static final String KEY = "ebdd1da629156627139d0b5be22bee67";
	
	//APP和网页支付提交用户端ip, Native支付填调用微信支付API的机器IP, 即：服务器ip地址
	public static final String SPBILL_CREATE_IP = "113.215.181.172";
	// 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。（需要配置）
	//http://192.168.1.188:8091/pay/wx/yjjnotify
	public static final String NOTIFY_URL = "http://47.98.179.176:1111/app/response/xingJieOrderResult";
	public static final String NOTIFY_WX_RECHAEGE_URL = "https://local.yujiejie.com/user/user/callback/recharge/weixin";
	// 支付方式，取值如下：JSAPI，NATIVE，APP
	public static final String TRADE_TYPE = "JSAPI";
	public static final String TRADE_APP_TYPE = "APP";

	// 微信支付 - 统一下单地址
	public static final String PLACEANORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
}
