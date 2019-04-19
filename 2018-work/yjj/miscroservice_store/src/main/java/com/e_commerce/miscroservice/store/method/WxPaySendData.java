package com.e_commerce.miscroservice.store.method;

import lombok.Data;

/**
 * 微信统一订单发送参数
 * 官方文档https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
 * @author Administrator
 *
 */
@Data
public class WxPaySendData {
    private String appid;//微信分配的公众账号ID（企业号corpid即为此appId）  小程序ID	appid	是	String(32)	wxd678efh567hg6787	微信分配的小程序ID
    private String mch_id;//微信支付分配的商户号 	商户号	mch_id	是	String(32)	1230000109	微信支付分配的商户号
    private String device_info;//终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"	设备号	device_info	否	String(32)	013467007045764	终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
    private String nonce_str;//随机字符串，不长于32位	随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
    private String sign;//签名	签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
    private String sign_type;//签名类型	sign_type	否	String(32)	HMAC-SHA256	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
    private String body;//商品描述 商品或支付单简要描述	商品描述	body	是	String(128)	腾讯充值中心-QQ会员充值	 商品简单描述，该字段须严格按照规范传递，具体请见参数规定
    private String detail;//商品名称明细列表	商品详情	detail	否	String(6000)		单品优惠字段(暂未上线)	
    private String attach;//附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据	附加数据	attach	否	String(127)	深圳分店	附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
    private String out_trade_no;//商户系统内部的订单号,32个字符内、可包含字母	商户订单号	out_trade_no	是	String(32)	20150806125346	商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
    private String fee_type;//符合ISO 4217标准的三位字母代码，默认人民币：CNY		货币类型	fee_type	否	String(16)	CNY	符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    private Integer total_fee;//订单总金额	总金额	total_fee	是	Int	888	订单总金额，单位为分，详见支付金额
    private String spbill_create_ip;//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。	终端IP	spbill_create_ip	是	String(16)	123.12.12.123	APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
    private String time_start;//订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010		交易起始时间	time_start	否	String(14)	20091225091010	订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
    private String time_expire;//订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010		交易结束时间	time_expire	否	String(14)	20091227091010 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则 注意：最短失效时间间隔必须大于5分钟
    private String goods_tag;//商品标记，代金券或立减优惠功能的参数	商品标记	goods_tag	否	String(32)	WXG	商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
    private String notify_url;//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。	通知地址	notify_url	是	String(256)	http://www.weixin.qq.com/wxpay/pay.php	接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
    private String trade_type;//交易类型,取值如下：JSAPI，NATIVE，APP	交易类型	trade_type	是	String(16)	JSAPI	小程序取值如下：JSAPI，详细说明见参数规定
    private String product_id;//trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
    private String limit_pay;//no_credit--指定不能使用信用卡支付	指定支付方式	limit_pay	否	String(32)	no_credit	no_credit--指定不能使用信用卡支付
    private String openid;//trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识		用户标识	openid	否	String(128)	oUpF8uMuAJO_M2pxb1Q9zNjWeS6o	trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。


}
