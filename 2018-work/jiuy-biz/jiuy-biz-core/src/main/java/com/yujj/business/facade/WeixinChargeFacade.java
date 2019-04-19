package com.yujj.business.facade;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.Client;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.OrderType;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.entity.newentity.weixinpay.Signature;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayConfig;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayCore;
import com.jiuyuan.service.common.HttpClientService;
import com.jiuyuan.util.http.component.CachedHttpResponse;
import com.jiuyuan.util.http.component.HttpClientQuery;
import com.jiuyuan.util.http.log.LogBuilder;
import com.yujj.business.service.AfterSaleService;
import com.yujj.business.service.NotificationService;
import com.yujj.business.service.OrderService;
import com.yujj.business.service.UserService;
import com.yujj.business.service.UserSharedService;
import com.yujj.dao.mapper.StoreBusinessMapper;
import com.yujj.dao.mapper.UserMapper;
import com.yujj.entity.StoreBusiness;
import com.yujj.entity.account.User;
import com.yujj.entity.order.Order;
import com.yujj.entity.order.OrderNew;

@Service
public class WeixinChargeFacade extends ChargeFacade {

    private static final Logger logger = LoggerFactory.getLogger("PAY");
    @Autowired
    private UserSharedService userSharedService;
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private AfterSaleService afterSaleService;

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private StoreBusinessMapper storeBusinessMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private HttpClientService httpClientService;
    //删除旧表
//    private Map<String, String> prepay(final long orderNo, final String ip, final String tradeType) {
//        final Map<String, String> result = new HashMap<String, String>();
//
//        httpClientService.execute(new HttpClientQuery("prepay") {
////        	long orderNo = orderOld.getId();
//        	OrderNew orderNew = orderService.getUserOrderNewByNo(String.valueOf(orderNo));
////     	   String total_fee = PayUtil.formatPrice(order.getPayAmountInCents());
//     		//删除就表示做了修改，从新表中获取金额
//     	   String total_fee = String.valueOf(orderNew.getUserPracticalPayMoneyOfFen());
//            
//            @Override
//            public void initLog(LogBuilder log) {
////            	log.append("orderId", order.getId());
//            	log.append("orderNo", orderNew.getOrderNo());
//            }
//            
//            @Override
//            public CachedHttpResponse sendRequest() throws Exception {
//                boolean app = StringUtils.equals(tradeType, "APP");
//                String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//                List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
//                packageParams.add(new BasicNameValuePair("appid", WeixinPayConfig.getAppId(app)));
//                packageParams.add(new BasicNameValuePair("body", "俞姐姐平台支付订单"));
//                packageParams.add(new BasicNameValuePair("mch_id", WeixinPayConfig.getMchId(app)));
//                packageParams.add(new BasicNameValuePair("nonce_str", WeixinPayCore.genNonceStr()));
//                packageParams.add(new BasicNameValuePair("notify_url", WeixinPayConfig.getNotifyUrl(app)));
////                packageParams.add(new BasicNameValuePair("out_trade_no","49687"));
//                packageParams.add(new BasicNameValuePair("out_trade_no", orderNew.getOrderNo()+""));
//                packageParams.add(new BasicNameValuePair("spbill_create_ip", ip));
////                packageParams.add(new BasicNameValuePair("total_fee", String.valueOf(order.getPayAmountInCents())));
//                
//                packageParams.add(new BasicNameValuePair("total_fee", total_fee));
//                packageParams.add(new BasicNameValuePair("trade_type", tradeType));
//                packageParams.add(new BasicNameValuePair("sign", WeixinPayCore.genSign(packageParams,
//                    WeixinPayConfig.getApiKey(app))));
//
//                HttpEntity entity =
//                    new StringEntity(new String(WeixinPayCore.toXml(packageParams).getBytes(), "ISO8859-1"));
//                CachedHttpResponse response = httpClientService.post(url, entity);
//                logger.debug("response:{}", response.getResponseText());
//                return response;
//            }
//            
//            @Override
//            public boolean readResponse(String responseText, LogBuilder errorLog) {
//                result.putAll(WeixinPayCore.decodeXml(responseText));
//                return result.containsKey("prepay_id");
//            }
//        });
//
//        return result;
//    }
    private Map<String, String> prepayNew(final OrderNew orderNew, final String ip, final String tradeType) {
    	final Map<String, String> result = new HashMap<String, String>();
    	
    	httpClientService.execute(new HttpClientQuery("prepay") {
    		
    		@Override
    		public void initLog(LogBuilder log) {
    			log.append("orderNo", orderNew.getOrderNo());
    		}
    		
    		@Override
    		public CachedHttpResponse sendRequest() throws Exception {
    			
    			String total_fee = String.valueOf(orderNew.getUserPracticalPayMoneyOfFen());// String.valueOf((int)(100 * (order.getTotalPay() + order.getTotalExpressMoney())));
    			boolean app = StringUtils.equals(tradeType, "APP");
    			String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
    			packageParams.add(new BasicNameValuePair("appid", WeixinPayConfig.getAppId(app)));
    			packageParams.add(new BasicNameValuePair("body", "俞姐姐平台支付订单"));
    			packageParams.add(new BasicNameValuePair("mch_id", WeixinPayConfig.getMchId(app)));
    			packageParams.add(new BasicNameValuePair("nonce_str", WeixinPayCore.genNonceStr()));
    			packageParams.add(new BasicNameValuePair("notify_url", WeixinPayConfig.getNotifyUrl(app)));
    			packageParams.add(new BasicNameValuePair("out_trade_no", orderNew.getOrderNo()+""));
    			packageParams.add(new BasicNameValuePair("spbill_create_ip", ip));
    			//packageParams.add(new BasicNameValuePair("total_fee", "8000"));
    			packageParams.add(new BasicNameValuePair("total_fee",total_fee));
    			packageParams.add(new BasicNameValuePair("trade_type", tradeType));
    			packageParams.add(new BasicNameValuePair("sign", WeixinPayCore.genSign(packageParams,
    					WeixinPayConfig.getApiKey(app))));
    			
    			HttpEntity entity =
    					new StringEntity(new String(WeixinPayCore.toXml(packageParams).getBytes(), "ISO8859-1"));
    			CachedHttpResponse response = httpClientService.post(url, entity);
    			logger.debug("response:{}", response.getResponseText());
    			return response;
    		}
    		
    		@Override
    		public boolean readResponse(String responseText, LogBuilder errorLog) {
    			result.putAll(WeixinPayCore.decodeXml(responseText));
    			logger.debug ("====== 微信预支付preId: ======>  "+responseText+"  <================");
    			return result.containsKey("prepay_id");
    		}
    	});
    	
    	return result;
    }

    //删除旧表
//    public String getCodeUrl(long orderNo, String ip) {
//        Map<String, String> map = prepay(orderNo, ip, "NATIVE");
//        return map.get("code_url");
//    }

    //删除旧表
//    public Map<String, Object> genPayData4App(long orderNo, String ip) {
//    	String prepayId = prepay(orderNo, ip, "APP").get("prepay_id");
//    	if (StringUtils.isBlank(prepayId)) {
//    		String msg = "get prepay id error, order no:" + orderNo;
//    		logger.error(msg);
//    		throw new IllegalStateException(msg);
//    	}
//    	
//    	boolean app = true;
//    	Map<String, Object> result = new HashMap<String, Object>();
//    	result.put("appId", WeixinPayConfig.getAppId(app));
//    	result.put("partnerId", WeixinPayConfig.getMchId(app));
//    	result.put("prepayId", prepayId);
//    	String packageValue = "Sign=WXPay";
//    	result.put("packageValue", packageValue);
//    	String nonceStr = WeixinPayCore.genNonceStr();
//    	result.put("nonceStr", nonceStr);
//    	long timeStamp = WeixinPayCore.genTimeStamp();
//    	result.put("timeStamp", timeStamp);
//    	
//    	List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//    	signParams.add(new BasicNameValuePair("appid", WeixinPayConfig.getAppId(app)));
//    	signParams.add(new BasicNameValuePair("noncestr", nonceStr));
//    	signParams.add(new BasicNameValuePair("package", packageValue));
//    	signParams.add(new BasicNameValuePair("partnerid", WeixinPayConfig.getMchId(app)));
//    	signParams.add(new BasicNameValuePair("prepayid", prepayId));
//    	signParams.add(new BasicNameValuePair("timestamp", String.valueOf(timeStamp)));
//    	
//    	String sign = WeixinPayCore.genSign(signParams, WeixinPayConfig.getApiKey(app));
//    	result.put("sign", sign);
//    	
//    	return result;
//    }
    
    public Map<String, Object> genPayData4AppNew(OrderNew order, String ip) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	String prepayId = prepayNew(order, ip, "APP").get("prepay_id");
    	if (StringUtils.isBlank(prepayId)) {
    		String msg = "get prepay id error, order no:" + order.getOrderNo();
    		logger.error(msg);
    		result.put("errorMsg", "商户订单号重复");
    		return result;
    		//throw new IllegalStateException(msg);
    	}
    	
    	boolean app = true;
    	result.put("appId", WeixinPayConfig.getAppId(app));
    	result.put("partnerId", WeixinPayConfig.getMchId(app));
    	result.put("prepayId", prepayId);
    	String packageValue = "Sign=WXPay";
    	result.put("packageValue", packageValue);
    	String nonceStr = WeixinPayCore.genNonceStr();
    	result.put("nonceStr", nonceStr);
    	long timeStamp = WeixinPayCore.genTimeStamp();
    	result.put("timeStamp", timeStamp);
    	
    	List<NameValuePair> signParams = new LinkedList<NameValuePair>();
    	signParams.add(new BasicNameValuePair("appid", WeixinPayConfig.getAppId(app)));
    	signParams.add(new BasicNameValuePair("noncestr", nonceStr));
    	signParams.add(new BasicNameValuePair("package", packageValue));
    	signParams.add(new BasicNameValuePair("partnerid", WeixinPayConfig.getMchId(app)));
    	signParams.add(new BasicNameValuePair("prepayid", prepayId));
    	signParams.add(new BasicNameValuePair("timestamp", String.valueOf(timeStamp)));
    	
    	String sign = WeixinPayCore.genSign(signParams, WeixinPayConfig.getApiKey(app));
    	result.put("sign", sign);
    	
    	return result;
    }

    public String weixinPayCallbackDispose(String requestBody, PaymentType paymentType) {
        logger.info("weixin pay callback log:" + requestBody);

        Map<String, String> params = WeixinPayCore.decodeXml(requestBody);
        logger.info("<测试分享下单2params>:"+params.toString());
        String out_trade_no = params.get("out_trade_no");
        OrderNew order = orderService.getUserOrderNewByNo(out_trade_no);
        logger.info("<测试分享下单3order>:"+order.toString());
        if (order == null) {
            String msg = "can not find order of " + out_trade_no;
            logger.error(msg);
            throw new IllegalStateException(msg);
        }
        
        String transactionId = params.get("transaction_id");
        logger.debug("微信订单号：{}", transactionId);

        try {
            if (Signature.checkIsSignValidFromResponseString(params, WeixinPayConfig.getApiKey(paymentType))) {
                String resultCode = params.get("result_code");
                logger.error("微信支付回调结果resultCode：{}, out_trade_no:{}, transactionId:{}",resultCode, out_trade_no, transactionId);
                if (resultCode.equals("SUCCESS") && order.getOrderStatus() == OrderStatus.UNPAID) {
                    long time = System.currentTimeMillis();
//                    PaymentType paymentType = app ? PaymentType.WEIXINPAY_SDK : PaymentType.WEIXINPAY_NATIVE;
                    
//                    if (order.getOrderType() == OrderType.SEND_BACK) {
//                        // 如果是回寄订单，则将状态更新为待审核状态
//                        orderService.updateOrderPayStatus(order, transactionId, paymentType, OrderStatus.UNCHECK,
//                            OrderStatus.UNPAID, time);
//                    } else {
                    //如果是单面付订单直接交易成功
                    logger.info("<测试分享下单4order.getOrderType>:"+order.getOrderType());
                    logger.info("<测试分享下单4order.getOrderStatus>:"+order.getOrderStatus());
                    if (order.getOrderType() == 2){
                        orderService.updateOrderPayStatus(order, transactionId, paymentType, OrderStatus.SUCCESS,OrderStatus.UNPAID, time);                        
                        facePayCallbackProcess(order, time);
                    } else {               
                        // 如果是普通直购订单，则将状态更新为已支付状态
                        orderService.updateOrderPayStatus(order, transactionId, paymentType, OrderStatus.PAID,OrderStatus.UNPAID, time);
                      //如果是售后订单，同步更新售后表信息
    					if(order.getOrderType() == 1){
    						int count = afterSaleService.updateServiceOrderPaid(order.getOrderNo());
    					}
                    }
                   
                    
                }else{
                	logger.error("微信支付回调后未进行修改订单状态，结果resultCode：{}, out_trade_no:{}, transactionId:{}",resultCode, out_trade_no, transactionId);
                }
            } else {
                logger.error("回调验证失败, out_trade_no:{}, transactionId:{}", out_trade_no, transactionId);
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        return out_trade_no;
    }

}
