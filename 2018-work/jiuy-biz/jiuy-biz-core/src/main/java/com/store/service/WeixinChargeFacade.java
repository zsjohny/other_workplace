package com.store.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.util.BizUtil;
import com.store.entity.YjjStoreBusinessAccountLog;
import com.util.CallBackUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductMapper;
import com.jiuyuan.dao.mapper.supplier.StoreCouponNewMapper;
import com.jiuyuan.dao.mapper.supplier.StoreCouponUseLogNewMapper;
import com.jiuyuan.dao.mapper.supplier.StoreMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.newentity.ground.GroundBonusGrant;
import com.jiuyuan.entity.newentity.weixinpay.Signature;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayConfig;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayCore;
import com.jiuyuan.service.common.GroundBonusGrantFacade;
import com.jiuyuan.service.common.HttpClientService;
import com.jiuyuan.service.common.IStoreCouponNewService;
import com.jiuyuan.service.common.IStoreOrderNewService;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.http.component.CachedHttpResponse;
import com.jiuyuan.util.http.component.HttpClientQuery;
import com.jiuyuan.util.http.log.LogBuilder;
import com.store.entity.ShopStoreOrder;
import com.store.entity.ShopStoreOrderItem;
import com.store.enumerate.OrderType;

@Service
public class WeixinChargeFacade {

    private static final Logger logger = LoggerFactory.getLogger("PAY");
    
    private static final int templateId = 4032201;
    
    @Autowired
    private HttpClientService httpClientService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private AfterSaleService afterSaleService;
    
    @Autowired
    private ShopProductService shopProductService;
    
    @Autowired
    private RestrictionActivityProductMapper restrictionActivityProductMapper;
    
    @Autowired(required = false)
    private List<OrderHandler> orderHandlers;
    
    @Autowired
	private GroundUserMapper groundUserMapper;
    
	@Autowired
	private UserNewMapper userNewMapper;
	
	@Autowired
	private IStoreOrderNewService storeOrderNewService;
	
	@Autowired
	private YunXinSmsService yunXinSmsService;
	
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;
	

	
//    @Autowired
//	private StoreBusinessNewMapper storeBusinessNewMapper;
    @Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private GroundBonusGrantFacade groundBonusGrantFacade;
	
    @Autowired
    private IStoreCouponNewService storeCouponNewService;
    
    @Autowired
    private StoreCouponUseLogNewMapper storeCouponUseLogNewMapper;
    
    @Autowired
    private StoreCouponNewMapper storeCouponNewMapper;

    /**
     * 测试方法
     */
    public Map<String, Object> 微信支付签名模拟接口(String orderNo, String ip) {
        Map<String, Object> result = new HashMap<String, Object>();
        BizUtil.test (378, "微信支付签名模拟接口");
//        ========================================================================
        //预生成订单时，必须重新组成一个新的订单号，因为改价功能
        String dateOrderAttachment = DateUtil.getRandomTime();
        final StringBuffer stringBuffer = new StringBuffer("_");
        stringBuffer.append(dateOrderAttachment);
        Map<String, String> result2 = new HashMap<String, String>();
        httpClientService.execute(new HttpClientQuery("prepay") {

            @Override
            public void initLog(LogBuilder log) {
                log.append("orderNo", "S" + orderNo+stringBuffer.toString());
            }

            @Override
            public CachedHttpResponse sendRequest() throws Exception {
                String tradeType = "APP";
                boolean app = StringUtils.equals(tradeType, "APP");
                String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
                List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
//                packageParams.add(new BasicNameValuePair("appid", "wx6ad169bccc57554a"));
                packageParams.add(new BasicNameValuePair("appid", WeixinPayConfig.getAppId(app)));
                packageParams.add(new BasicNameValuePair("body", "hello world "));
                packageParams.add(new BasicNameValuePair("mch_id", WeixinPayConfig.getMchId(app)));
                packageParams.add(new BasicNameValuePair("nonce_str", WeixinPayCore.genNonceStr()));
                packageParams.add(new BasicNameValuePair("notify_url", WeixinPayConfig.getNotifyUrl(app)));
                logger.info("out_trade_no:"+"S" + orderNo+stringBuffer.toString());
                packageParams.add(new BasicNameValuePair("out_trade_no", "S" + orderNo+stringBuffer.toString()));
                packageParams.add(new BasicNameValuePair("spbill_create_ip", ip));

                logger.debug ("appId" + WeixinPayConfig.getAppId(app));
                logger.debug ("MchId"+WeixinPayConfig.getMchId(app));
                logger.debug ("notify_url"+WeixinPayConfig.getNotifyUrl(app));

                //计算金额
                BigDecimal sum = new BigDecimal ("0.01");
                int fee = sum.multiply (new BigDecimal ("100")).intValue ();
                packageParams.add(new BasicNameValuePair("total_fee", fee+""));
                packageParams.add(new BasicNameValuePair("trade_type", tradeType));
                packageParams.add(new BasicNameValuePair("sign", WeixinPayCore.genSign(packageParams,
                        WeixinPayConfig.getApiKey(app))));

                HttpEntity entity = new StringEntity(new String(WeixinPayCore.toXml(packageParams).getBytes(), "ISO8859-1"));
                CachedHttpResponse response = httpClientService.post(url, entity);
                logger.debug("response:{}", response.getResponseText());
                System.out.println ("response.getResponseText () = " + response.getResponseText ());
                return response;
            }

            @Override
            public boolean readResponse(String responseText, LogBuilder errorLog) {
                result2.putAll(WeixinPayCore.decodeXml(responseText));
                return result2.containsKey("prepay_id");
            }
        });

        String prepayId = result2.get("prepay_id");

//        ========================================================================
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




    public Map<String, Object> genPayData4AppNew(ShopStoreOrder order, String ip) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	String prepayId = prepayNew(order, ip, "APP").get("prepay_id");
    	logger.error("prepayId:"+prepayId+",order:"+order.getOrderNo());
    	
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
    @Transactional(rollbackFor=Exception.class)
    public Map<String, String> prepayNew(final ShopStoreOrder order, final String ip, final String tradeType) {
    	final Map<String, String> result = new HashMap<String, String>();
    	
    	//预生成订单时，必须重新组成一个新的订单号，因为改价功能
    	String dateOrderAttachment = DateUtil.getRandomTime();
    	final StringBuffer stringBuffer = new StringBuffer("_");
    	stringBuffer.append(dateOrderAttachment);
    	StoreOrderNew storeOrderNew = new StoreOrderNew();
    	storeOrderNew.setOrderNo(order.getOrderNo());
    	storeOrderNew.setOrderNoAttachmentStr(stringBuffer.toString());
    	supplierOrderMapper.updateById(storeOrderNew);
    	
    	
    	httpClientService.execute(new HttpClientQuery("prepay") {
    		
    		@Override
    		public void initLog(LogBuilder log) {
    			log.append("orderNo", "S" + order.getOrderNo()+stringBuffer.toString());
    		}
    		
    		@Override
    		public CachedHttpResponse sendRequest() throws Exception {
    			boolean app = StringUtils.equals(tradeType, "APP");
    			String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
    			packageParams.add(new BasicNameValuePair("appid", WeixinPayConfig.getAppId(app)));
    			packageParams.add(new BasicNameValuePair("body", "俞姐姐门店宝支付订单"));
    			packageParams.add(new BasicNameValuePair("mch_id", WeixinPayConfig.getMchId(app)));
    			packageParams.add(new BasicNameValuePair("nonce_str", WeixinPayCore.genNonceStr()));
    			packageParams.add(new BasicNameValuePair("notify_url", WeixinPayConfig.getNotifyUrl(app)));
    			logger.info("out_trade_no:"+"S" + order.getOrderNo()+stringBuffer.toString());
    			packageParams.add(new BasicNameValuePair("out_trade_no", "S" + order.getOrderNo()+stringBuffer.toString()));
    			packageParams.add(new BasicNameValuePair("spbill_create_ip", ip));
    			//packageParams.add(new BasicNameValuePair("total_fee", "8000"));

				//计算金额
				BigDecimal sum = new BigDecimal (order.getTotalPay ()+"").add (new BigDecimal(order.getTotalExpressMoney ()+""));
				int fee = sum.multiply (new BigDecimal ("100")).intValue ();
				packageParams.add(new BasicNameValuePair("total_fee", fee+""));

//    			packageParams.add(new BasicNameValuePair("total_fee", String.valueOf((int)(100 * (order.getTotalPay() + order.getTotalExpressMoney())))));
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
    			return result.containsKey("prepay_id");
    		}
    	});
    	
    	return result;
    }
	
    public String weixinPayCallbackDispose(String requestBody, boolean app, String version) {
        logger.info("weixin pay callback log:" + requestBody);

        Map<String, String> params = WeixinPayCore.decodeXml(requestBody);
        String out_trade_no = params.get("out_trade_no");
        String attachmentStr = "";
        if(out_trade_no != null && out_trade_no.startsWith("S")){
        	//获取该out_trade_no的订单号
        	int startIndex = out_trade_no.indexOf("S")+1;
        	int endIndex = out_trade_no.indexOf("_");
        	attachmentStr = out_trade_no.substring(endIndex);
        	out_trade_no = out_trade_no.substring(startIndex,endIndex);
//        	out_trade_no = out_trade_no.substring(1, out_trade_no.length());
        }else if(out_trade_no != null && out_trade_no.contains("_")){
        	int endIndex = out_trade_no.indexOf("_");
        	out_trade_no = out_trade_no.substring(0,endIndex);
        	attachmentStr = out_trade_no.substring(endIndex);
        }

        ShopStoreOrder order = orderService.getUserOrderNewByNo(out_trade_no);
        if(!order.getOrderNoAttachmentStr().equals(attachmentStr)){
        	StoreOrderNew storeOrderNew = new StoreOrderNew();
        	storeOrderNew.setOrderNo(order.getOrderNo());
        	storeOrderNew.setOrderNoAttachmentStr(attachmentStr);
        	supplierOrderMapper.updateById(storeOrderNew);
        }
        if (order == null) {
            String msg = "can not find order of " + out_trade_no;
            logger.error(msg);
            throw new IllegalStateException(msg);
        }
        
        String transactionId = params.get("transaction_id");
        logger.debug("微信订单号：{}", transactionId);

        try {
            if (Signature.checkIsSignValidFromResponseString(params, WeixinPayConfig.getApiKey(app))) {
                String resultCode = params.get("result_code");
                if (resultCode.equals("SUCCESS") && order.getOrderStatus() == OrderStatus.UNPAID.getIntValue()) {
                    long time = System.currentTimeMillis();
                    PaymentType paymentType = app ? PaymentType.WEIXINPAY_SDK : PaymentType.WEIXINPAY_NATIVE;
                        
//                    if (order.getOrderType() == OrderType.SEND_BACK) {
//                        // 如果是回寄订单，则将状态更新为待审核状态
//                        orderService.updateOrderPayStatus(order, transactionId, paymentType, OrderStatus.UNCHECK,
//                            OrderStatus.UNPAID, time);
//                    } else {
                        // 如果是普通直购订单，则将状态更新为已支付状态
                        orderService.updateOrderPayStatus(order, transactionId, paymentType, OrderStatus.PAID,
                            OrderStatus.UNPAID, time ,version );



                        shopProductService.updateTabTypeAfterPaySuccess(out_trade_no);
						// ==================================================================
						//购买会员, 直接返回
						if (order.getClassify ()!= null && order.getClassify () > 2){
							return out_trade_no;
						}
						// ==================================================================
					//商家优惠券已使用,做统计
                        long orderNo = order.getOrderNo();
                        Wrapper<StoreCouponUseLogNew> wrapper = new EntityWrapper<StoreCouponUseLogNew>();
                        wrapper.eq("OrderNo", orderNo)
                               .eq("Status", 0)
                               .ne("supplier_id", 0);

//                        List<StoreCouponUseLogNew> storeCouponUseLogNewList = storeCouponUseLogNewMapper.selectList(wrapper);
//                        if(storeCouponUseLogNewList.size()>0){
//                        	StoreCouponNew storeCouponNew = storeCouponNewMapper.selectById(storeCouponUseLogNewList.get(0).getCouponId());
//                        	long couponTemplateId = storeCouponNew.getCouponTemplateId();
//                        	storeCouponNewService.doStatisticsByCouponTemplateIdWhenUse(couponTemplateId);
//
//                        }


                        List<ShopStoreOrderItem> orderItemList = orderService.getOrderNewItemsOnlyByOrderNO(order.getOrderNo());
                        order.setOrderItems(orderItemList);
                        logger.info("1-修改商品销量:开始");
                        if (orderHandlers != null) {
                            for (OrderHandler handler : orderHandlers) {
                                handler.updateSaleCount(order, "");
                            }
                            logger.info("1-修改商品销量:成功");
                        }
                        
                        //倘若是限购活动商品，就添加限购活动销量
                        if(order.getRestriction_activity_product_id() > 0){
                      	  RestrictionActivityProduct restrictionActivityProduct = restrictionActivityProductMapper.selectById(order.getRestriction_activity_product_id());
                      	  int saleCount = restrictionActivityProduct.getSaleCount();
                      	  saleCount = saleCount+order.getTotalBuyCount();
                      	  RestrictionActivityProduct restrictionActivityProduct2 = new RestrictionActivityProduct();
                      	  restrictionActivityProduct2.setId(order.getRestriction_activity_product_id());
                      	  restrictionActivityProduct2.setSaleCount(saleCount);
                      	  restrictionActivityProductMapper.updateById(restrictionActivityProduct2);
                        }
                        
                      //发送短信[俞姐姐门店宝] ***供应商，您好！您有新订单，请及时关注并确保在24小时之后完成发货。通知供应商发货
                        List<StoreOrderNew> storeOrderNewLists = storeOrderNewService.getSuborderByParentOrder(orderNo);
                        for(StoreOrderNew storeOrderNew :storeOrderNewLists){
                        	long supplierId = storeOrderNew.getSupplierId();
                        	UserNew userNew = userNewMapper.selectById(supplierId);
                        	sendText(userNew.getPhone(),userNew.getBusinessName(),templateId);
                        }
                        
                      //如果是售后订单，同步更新售后表信息
    					if(order.getOrderType() == OrderType.AFTERSALE.getIntValue()){
    						int count = afterSaleService.updateServiceOrderPaid(order.getOrderNo());
    						System.out.println("afterSaleService update num:" + count);
    					}
//                    }
					upSendCoupon(orderNo);

                }
            } else {
                logger.error("回调验证失败, out_trade_no:{}, transactionId:{}", out_trade_no, transactionId);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("", e);
        }

        return out_trade_no;
    }


	/**
	 * 由于调用不到rb 所以给订单表加一个字段
	 * @param orderNo orderNo
	 * @author Aison
	 * @date 2018/8/3 18:13
	 */
	private void upSendCoupon(Long orderNo) {

		StoreOrderNew up = new StoreOrderNew();
		up.setOrderNo(orderNo);
		up.setSendCoupon(1);
		supplierOrderMapper.updateById(up);
	}

	private void sendText(String phoneNumber, String businessName, int templateNumber) {
		if (phoneNumber == null || "".equals(phoneNumber)) {
			throw new RuntimeException("该手机号码为空");
		} else {
			JSONArray param = new JSONArray();
			param.add(businessName);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			param.add(sdf.format(new Date()));
			yunXinSmsService.sendNotice(phoneNumber, param, templateNumber);
		}
	}
    
}