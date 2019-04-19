package com.e_commerce.miscroservice.payment.wx.service.impl;

import com.e_commerce.miscroservice.commons.entity.proxy.PreOrderResult;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.UUIdUtil;
import com.e_commerce.miscroservice.commons.utils.StringUtil;
import com.e_commerce.miscroservice.payment.wx.config.WeChatConfig;
import com.e_commerce.miscroservice.payment.wx.entity.PreOrder;
import com.e_commerce.miscroservice.payment.wx.service.WxOrderService;
import com.e_commerce.miscroservice.payment.wx.util.HttpUtil;
import com.e_commerce.miscroservice.payment.wx.util.Sign;
import com.e_commerce.miscroservice.payment.wx.util.XmlUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class WxOrderServiceImpl implements WxOrderService {

	@Autowired
	HttpServletRequest request;


	@Value("${public.account.appid}")
	private String publicAccountAppId;

	@Value ("${public.account.mchid}")
	private String publicAccountMchId;

	@Value ("${public.account.key}")
	private String publicAccountKey;

	@Value ("${public.account.notifyurl}")
	private String publicAccountNotifyUrl;

	private Log logger = Log.getInstance(WxOrderServiceImpl.class);
	
	/**
	 * ==========================================
	 * 微信预付单：指的是在自己的平台需要和微信进行支付交易生成的一个微信订单，称之为“预付单”
	 * 订单：指的是自己的网站平台与用户之间交易生成的订单
	 * 
	 * 1. 用户购买产品 --> 生成网站订单
	 * 2. 用户支付 --> 网站在微信平台生成预付单
	 * 3. 最终实际根据预付单的信息进行支付
	 * ==========================================
	 *
	 * https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_20&index=1  官方文档
	 *
	 * by hyq
	 */
	@Override
	public PreOrderResult placeOrder(String body, String out_trade_no, BigDecimal total_fee, String attach, String openid, String ip, String trade_type) throws Exception {
		if (StringUtils.isEmpty(trade_type)){
			trade_type=WeChatConfig.TRADE_TYPE;
		}
		// 生成预付单对象
		PreOrder o = new PreOrder();
		// 生成随机字符串
		String nonce_str = UUID.randomUUID().toString().trim().replaceAll("-", "");
//		o.setAppid(WeChatConfig.APPID);
		o.setAppid(publicAccountAppId);
		o.setBody(body);
//		o.setMch_id(WeChatConfig.MCH_ID);
		o.setMch_id(publicAccountMchId);
//		o.setNotify_url(WeChatConfig.NOTIFY_URL);
		o.setNotify_url(publicAccountNotifyUrl);
		o.setOut_trade_no(out_trade_no);
		// 判断有没有输入订单总金额，没有输入默认1分钱
		//if (total_fee != null && !total_fee.equals("")) {
		o.setTotal_fee(total_fee.intValue());
		//} else {
		//	o.setTotal_fee(1);
		//}
		o.setNonce_str(nonce_str);
		o.setTrade_type(trade_type);
//		o.setSpbill_create_ip(WeChatConfig.SPBILL_CREATE_IP);
		o.setSpbill_create_ip(ip);
//		o.setSpbill_create_ip(createIp);
		if (StringUtils.isNotEmpty(openid)){
			o.setOpenid(openid);
		}
		o.setAttach(attach);
		SortedMap<Object, Object> p = new TreeMap<Object, Object>();
		p.put("appid", publicAccountAppId);
		p.put("mch_id", publicAccountMchId);
		p.put("body", body);
		p.put("nonce_str", nonce_str);
		p.put("out_trade_no", out_trade_no);
		p.put("total_fee", total_fee.intValue());
		p.put("attach", attach);
		//HttpUtil.getClientIp(request)
//		p.put("spbill_create_ip", WeChatConfig.SPBILL_CREATE_IP);
		p.put("spbill_create_ip", ip);
//		p.put("notify_url", WeChatConfig.NOTIFY_URL);
		p.put("notify_url", publicAccountNotifyUrl);
		p.put("trade_type", WeChatConfig.TRADE_TYPE);
		p.put("openid", openid);
		// 获得签名
//		String sign = Sign.createSign("utf-8", p, WeChatConfig.KEY);
		String sign = Sign.createSign("utf-8", p, publicAccountKey);
		o.setSign(sign);
		// Object转换为XML
		String xml = XmlUtil.object2Xml(o, PreOrder.class);
		// 统一下单地址
		String url = WeChatConfig.PLACEANORDER_URL;
		// 调用微信统一下单地址
		logger.info("调用微信支付请求原始报文:"+xml);
		String returnXml = HttpUtil.post(url, xml);

		logger.info("调用微信支付返回原始报文:"+returnXml);
		
		// XML转换为Object
		PreOrderResult preOrderResult = (PreOrderResult) XmlUtil.xml2Object(returnXml, PreOrderResult.class);
		
		return preOrderResult;
	}

	/**
	 * 描述 获取支付结果
	 * @param request
	 * @author hyq
	 * @date 2018/9/21 14:03
	 * @return com.e_commerce.miscroservice.payment.wx.entity.PayResult
	 */
	@Override
	public String getWxPayResult(HttpServletRequest request) throws Exception {

		InputStream inStream = request.getInputStream();
		BufferedReader in = null;
		StringBuilder result = new StringBuilder ();
		in = new BufferedReader(
				new InputStreamReader(inStream));
		String line;
		while ((line = in.readLine()) != null) {
			result.append (line);
		}

		return result.toString ();
//		PayResult pr = (PayResult)XmlUtil.xml2Object(result, PayResult.class);
//		System.out.println(pr.toString());
//		return pr;
	}
}
