package com.e_commerce.miscroservice.payment.wx.service;

import com.e_commerce.miscroservice.commons.entity.proxy.PreOrderResult;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * 描述 处理微信支付的相关订单业务
 * @author hyq
 * @date 2018/9/21 14:00
 * @return
 */
public interface WxOrderService {
	

	/**
	 * 描述 调用微信接口进行统一下单
	 * @param body
	 * @param out_trade_no
	 * @param total_fee
	 * @param ip
     * @param trade_type
     * @author hyq
	 * @date 2018/9/21 14:03
	 * @return com.e_commerce.miscroservice.payment.wx.entity.PreOrderResult
	 */
	public PreOrderResult placeOrder(String body, String out_trade_no, BigDecimal total_fee, String attach, String openid, String ip, String trade_type) throws Exception;


	/**
	 * 描述 获取支付结果
	 * @param request
	 * @author hyq
	 * @date 2018/9/21 14:03
	 * @return com.e_commerce.miscroservice.payment.wx.entity.PayResult
	 */
	public String getWxPayResult(HttpServletRequest request) throws Exception;

}
