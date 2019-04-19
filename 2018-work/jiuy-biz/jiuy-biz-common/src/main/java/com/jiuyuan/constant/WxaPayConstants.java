package com.jiuyuan.constant;

import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayConfig;
/**
 * 微信公众号相关常量配置
 * @author zhaoxinglin
 *
 * @version 2017年4月13日上午10:58:46
 */
public class WxaPayConstants{
	//异步回调地址
	public static String NOTIFY_URL =  WeixinPayConfig.getNotifyUrl(PaymentType.WEIXINPAY_WXA);

}
