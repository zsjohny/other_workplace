package com.e_commerce.miscroservice.store.method;
/**
 * 
 * 解析微信下单接口返回的XML 
 * 官方文档地址：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
<?xml version="1.0" encoding="utf-8"?>
<xml>
  <return_code><![CDATA[SUCCESS]]></return_code>
  <return_msg><![CDATA[OK]]></return_msg>
  <appid><![CDATA[wx95c37c75c641bc5e]]></appid>
  <mch_id><![CDATA[1459044102]]></mch_id>
  <nonce_str><![CDATA[L5TQlHHBcXqa2kbH]]></nonce_str>
  <sign><![CDATA[5036FB0407F78182D754893DCF88BEC8]]></sign>
  <result_code><![CDATA[SUCCESS]]></result_code>
  <prepay_id><![CDATA[wx20170412104933bb7a2e9f2e0523033865]]></prepay_id>
  <trade_type><![CDATA[JSAPI]]></trade_type>
</xml>

<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[商户号mch_id与appid不匹配]]></return_msg></xml>
 * @author zhaoxinglin
 *
 * @version 2017年4月12日上午10:44:22
 */
public class WxPayResultData {
	
	/**
	 * 返回状态码
	 */
	private String return_code;
	/**
	 * 返回信息
	 */
	private String return_msg;
	/**
	 * 公众账号ID（调用接口提交的公众账号ID）	小程序ID	appid	是	String(32)	wx8888888888888888	调用接口提交的小程序ID
	 */
	private String appid;
	/**
	 * 商户号（调用接口提交的商户号）	商户号	mch_id	是	String(32)	1900000109	调用接口提交的商户号
	 */
	private String mch_id;
	/**
	 * 设备号	device_info	否	String(32)	013467007045764	调用接口提交的终端设备号，
	 */
	private String device_info;
	/**
	 * 随机字符串（微信返回的随机字符串）	随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	微信返回的随机字符串
	 */
	private String nonce_str;
	/**
	 * 签名（微信返回的签名值）签名算法官方文档地址：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
	 * 签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	微信返回的签名，详见签名算法
	 */
	private String sign;
	/**
	 * 业务结果（SUCCESS/FAIL）
	 * 业务结果	result_code	是	String(16)	SUCCESS	SUCCESS/FAIL
	 */
	private String result_code;
	/**
	 * 错误代码
	 * 错误代码	err_code	否	String(32)	SYSTEMERROR	详细参见第6节错误列表
	 * 错误代码描述	err_code_des	否	String(128)	系统错误	错误返回的信息描述
	 * 
错误码
名称	描述	原因	解决方案
NOAUTH	商户无此接口权限	商户未开通此接口权限	请商户前往申请此接口权限
NOTENOUGH	余额不足	用户帐号余额不足	用户帐号余额不足，请用户充值或更换支付卡后再支付
ORDERPAID	商户订单已支付	商户订单已支付，无需重复操作	商户订单已支付，无需更多操作
ORDERCLOSED	订单已关闭	当前订单已关闭，无法支付	当前订单已关闭，请重新下单
SYSTEMERROR	系统错误	系统超时	系统异常，请用相同参数重新调用
APPID_NOT_EXIST	APPID不存在	参数中缺少APPID	请检查APPID是否正确
MCHID_NOT_EXIST	MCHID不存在	参数中缺少MCHID	请检查MCHID是否正确
APPID_MCHID_NOT_MATCH	appid和mch_id不匹配	appid和mch_id不匹配	请确认appid和mch_id是否匹配
LACK_PARAMS	缺少参数	缺少必要的请求参数	请检查参数是否齐全
OUT_TRADE_NO_USED	商户订单号重复	同一笔交易不能多次提交	请核实商户订单号是否重复提交
SIGNERROR	签名错误	参数签名结果不正确	请检查签名参数和方法是否都符合签名算法要求
XML_FORMAT_ERROR	XML格式错误	XML格式错误	请检查XML参数格式是否正确
REQUIRE_POST_METHOD	请使用post方法	未使用post传递参数 	请检查请求参数是否通过post方法提交
POST_DATA_EMPTY	post数据为空	post数据不能为空	请检查post数据是否为空
NOT_UTF8	编码格式错误	未使用指定编码格式	请使用UTF-8编码格式
	 */
	private String err_code;
	/**
	 * 错误代码描述
	 */
	private String err_code_des;
	/**
	 * 交易类型
	 */
	private String trade_type;
	/**
	 * 预支付交易会话标识
	 */
	private String prepay_id;
	/**
	 * 二维码链接 trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
	 */
	private String code_url;
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	public String getErr_code_des() {
		return err_code_des;
	}
	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getPrepay_id() {
		return prepay_id;
	}
	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}
	public String getCode_url() {
		return code_url;
	}
	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	
	
}
