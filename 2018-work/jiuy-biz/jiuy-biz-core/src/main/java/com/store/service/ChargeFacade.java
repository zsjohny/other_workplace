/**
 * 
 */
package com.store.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.dao.mapper.supplier.*;
import com.store.entity.YjjStoreBusinessAccountLog;
import com.util.CallBackUtil;
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
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.newentity.RestrictionActivityProduct;
import com.jiuyuan.entity.newentity.StoreCouponNew;
import com.jiuyuan.entity.newentity.StoreCouponUseLogNew;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.SupplierCustomer;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.newentity.alipay.direct.AlipayConfig;
import com.jiuyuan.entity.newentity.alipay.direct.AlipayNotify;
import com.jiuyuan.entity.newentity.alipay.direct.mobile.MobileAlipayNotify;
import com.jiuyuan.entity.newentity.alipay.direct.mobile.RSA;
import com.jiuyuan.service.common.GroundBonusGrantFacade;
import com.jiuyuan.service.common.IStoreCouponNewService;
import com.jiuyuan.service.common.IStoreOrderNewService;
import com.jiuyuan.service.common.StoreOrderNewService;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.util.EncodeUtil;
import com.store.entity.ShopStoreOrder;
import com.store.entity.ShopStoreOrderItem;
import com.store.enumerate.OrderType;
import com.yujj.util.uri.UriParams;

/**
 * @author LWS
 */
@Service
public class ChargeFacade {
    private static final Logger logger = LoggerFactory.getLogger("PAY");
    
    public static final int templateId = 4032201;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private AfterSaleService afterSaleService;
    
    @Autowired
    private ShopProductService shopProductService;
    
	@Autowired(required = false)
    private List<OrderHandler> orderHandlers;
	
	@Autowired
	private GroundUserMapper groundUserMapper;
	
	@Autowired
	private IStoreOrderNewService storeOrderNewService;
	
	@Autowired
	private YunXinSmsService yunXinSmsService;
	
//	@Autowired
//	private StoreBusinessNewMapper storeBusinessNewMapper;
//	
	@Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private UserNewMapper userNewMapper;
	
	@Autowired
	private GroundBonusGrantFacade groundBonusGrantFacade;
	
	@Autowired
	private RestrictionActivityProductMapper restrictionActivityProductMapper;
	
    @Autowired
    private IStoreCouponNewService storeCouponNewService;
    
    @Autowired
    private StoreCouponUseLogNewMapper storeCouponUseLogNewMapper;
    
    @Autowired
    private StoreCouponNewMapper storeCouponNewMapper;

    @Autowired
    private SupplierOrderMapper supplierOrderMapper;


    private final String _SUBJECT = "俞姐姐门店宝支付订单";

    private final String _BODY = _SUBJECT + ":用于购买【俞姐姐门店宝】官网商品";
    
    public String buildRequestParaString4Mobile(ShopStoreOrder order) {
        StringBuilder builder = new StringBuilder();
        // 签约合作者身份ID
        builder.append("partner=\"").append(AlipayConfig.partner).append("\"");
        // 签约卖家支付宝账号
        builder.append("&seller_id=\"").append(AlipayConfig.seller_email).append("\"");
        // 商户网站唯一订单号
        builder.append("&out_trade_no=\"").append("S").append(order.getOrderNo()).append("\"");
        // 商品名称
        builder.append("&subject=\"").append(_SUBJECT).append("\"");
        // 商品详情
        builder.append("&body=\"").append(_BODY).append("\"");
        // 商品金额
        double fee = order.getTotalPay()+ order.getTotalExpressMoney();
        BigDecimal freeBig = new BigDecimal(fee);
        fee = freeBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        builder.append("&total_fee=\"").append(fee).append("\"");
        // 服务器异步通知页面路径
        builder.append("&notify_url=\"").append(AlipayConfig.notify_url).append("\"");
        // 服务接口名称， 固定值
        builder.append("&service=\"mobile.securitypay.pay\"");
        // 支付类型， 固定值
        builder.append("&payment_type=\"").append(AlipayConfig.payment_type).append("\"");
        // 参数编码， 固定值
        builder.append("&_input_charset=\"").append(AlipayConfig.input_charset).append("\"");
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        // orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        builder.append("&return_url=\"").append(AlipayConfig.return_url).append("\"");
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        // 对订单做RSA 签名
        String sign = RSA.sign(builder.toString(), AlipayConfig.rsa_private_key, "UTF-8");
        // 仅需对sign 做URL编码
        sign = EncodeUtil.encodeURL(sign, "UTF-8");

        // 完整的符合支付宝参数规范的订单信息
        return builder.append("&sign=\"").append(sign).append("\"").append("&sign_type=\"RSA\"").toString();
	}



    public String 生成支付宝支付签名测试模拟方法(String out_trade_no) {
        BizUtil.test (378 ,"生成支付宝支付签名测试模拟方法");
        StringBuilder builder = new StringBuilder();
        // 签约合作者身份ID
        builder.append("partner=\"").append(AlipayConfig.partner).append("\"");
        // 签约卖家支付宝账号
        builder.append("&seller_id=\"").append(AlipayConfig.seller_email).append("\"");
        // 商户网站唯一订单号
        builder.append("&out_trade_no=\"").append("S").append(out_trade_no).append("\"");
        // 商品名称
        builder.append("&subject=\"").append(_SUBJECT).append("\"");
        // 商品详情
        builder.append("&body=\"").append(_BODY).append("\"");
        // 商品金额
        double fee = 0.01;
        BigDecimal freeBig = new BigDecimal(fee);
        fee = freeBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        builder.append("&total_fee=\"").append(fee).append("\"");
        // 服务器异步通知页面路径
        builder.append("&notify_url=\"").append(AlipayConfig.notify_url).append("\"");
        // 服务接口名称， 固定值
        builder.append("&service=\"mobile.securitypay.pay\"");
        // 支付类型， 固定值
        builder.append("&payment_type=\"").append(AlipayConfig.payment_type).append("\"");
        // 参数编码， 固定值
        builder.append("&_input_charset=\"").append(AlipayConfig.input_charset).append("\"");
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        // orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        builder.append("&return_url=\"").append(AlipayConfig.return_url).append("\"");
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        // 对订单做RSA 签名
        String sign = RSA.sign(builder.toString(), AlipayConfig.rsa_private_key, "UTF-8");
        // 仅需对sign 做URL编码
        sign = EncodeUtil.encodeURL(sign, "UTF-8");

        // 完整的符合支付宝参数规范的订单信息
        return builder.append("&sign=\"").append(sign).append("\"").append("&sign_type=\"RSA\"").toString();
	}

	public String mobilePayCallbackDispose(UriParams uriParams, String version) {
    	if(uriParams == null){
    		return "";
    	}
        logger.info("1-pay callback log:" + uriParams);
        
        String trade_status = uriParams.getSingle("trade_status");
        //3个月回调空处理
        if (trade_status != null && trade_status.equals("TRADE_FINISHED")){
        	return "";
        }
        
        String out_trade_no = uriParams.getSingle("out_trade_no");
        logger.info("1-out_trade_no:" + out_trade_no);
        if(out_trade_no != null && out_trade_no.startsWith("S")){
        	out_trade_no = out_trade_no.substring(1, out_trade_no.length());
        }
        ShopStoreOrder order = orderService.getUserOrderNewByNo(out_trade_no);
        logger.info("1-order:" + order);
        if (order == null) {
            String msg = "can not find order of " + out_trade_no;
            logger.error(msg);
            throw new IllegalStateException(msg);
        }

        try { 
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
            // 商户订单号
            // 支付宝交易号
            String trade_no = uriParams.getSingle("trade_no");
            logger.debug("1-支付宝订单号：" + trade_no);
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
            // 计算得出通知验证结果
            boolean verify_result = MobileAlipayNotify.verify(uriParams.asSingleValueMap(","));
            logger.info("验证成功 1-verify_result:" + verify_result);
            if (verify_result) {// 验证成功
                // ////////////////////////////////////////////////////////////////////////////////////////
                // 请在这里加上商户的业务逻辑程序代码

                // ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
                // 交易状态
                trade_status = uriParams.getSingle("trade_status");
                logger.info("1-trade_status 交易状态 =" + trade_status);
                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                    // 判断该笔订单是否在商户网站中已经做过处理
                    // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    // 如果有做过处理，不执行商户的业务程序

                    // 设置返回显示
                    // add by LWS
                    // 更新订单状态
                	logger.info("更新订单状态 1-getOrderStatus:" + order.getOrderStatus());
                    if (order.getOrderStatus() == OrderStatus.UNPAID.getIntValue()) {
                        long time = System.currentTimeMillis();
                        PaymentType paymentType = PaymentType.ALIPAY_SDK;
                        
//                        if(order.getOrderType() == OrderType.SEND_BACK){ 
//                         // 如果是回寄订单，则将状态更新为待审核状态
//                            orderService.updateOrderPayStatus(order, trade_no, paymentType, OrderStatus.UNCHECK,
//                                OrderStatus.UNPAID, time);
//                        }else{
                            // 如果是普通直购订单，则将状态更新为已支付状态
                        	logger.info("1-修改状态:");
                        	orderService.updateOrderPayStatus(order, trade_no, paymentType, OrderStatus.PAID,OrderStatus.UNPAID, time ,version);
                            logger.info("1-修改状态:成功");
                            //支付成功会的回调
                            shopProductService.updateTabTypeAfterPaySuccess(out_trade_no);
                            // ==================================================================
                            //购买会员, 直接返回
                            if (order.getClassify ()!= null && order.getClassify () > 2){
                                return out_trade_no;
                            }
                            // ==================================================================
                            logger.info("1-店家精选:成功");
                            //商家优惠券已使用,做统计
                            long orderNo = order.getOrderNo();
                            Wrapper<StoreCouponUseLogNew> wrapper = new EntityWrapper<StoreCouponUseLogNew>();
                            wrapper.eq("OrderNo", orderNo)
                                   .eq("Status", 0)
                                   .ne("supplier_id", 0);

//                            List<StoreCouponUseLogNew> storeCouponUseLogNewList = storeCouponUseLogNewMapper.selectList(wrapper);
//                            if(storeCouponUseLogNewList.size()>0){
//                            	StoreCouponNew storeCouponNew = storeCouponNewMapper.selectById(storeCouponUseLogNewList.get(0).getCouponId());
//                            	long couponTemplateId = storeCouponNew.getCouponTemplateId();
//                            	storeCouponNewService.doStatisticsByCouponTemplateIdWhenUse(couponTemplateId);
//                            }


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
                          StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
                          long supplierId = storeOrderNew.getSupplierId();
                          UserNew userNew = userNewMapper.selectById(supplierId);
                          sendText(userNew.getPhone(),userNew.getBusinessName(),templateId);
                          //如果是售后订单，同步更新售后表信息
        					if(order.getOrderType() == OrderType.AFTERSALE.getIntValue()){
        						int count = afterSaleService.updateServiceOrderPaid(order.getOrderNo());
        						System.out.println("afterSaleService update num:" + count);
        					}
//                        }
                        // 给订单订单表加一个字段
                        upSendCoupon(orderNo);

                    }
                    // String payinfoAfter = makePaymentFromAlipay(params);
                    // logger.info(payinfoAfter);
                }
                // 该页面可做页面美工编辑

                // ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
                // 支付成功页面
                // ////////////////////////////////////////////////////////////////////////////////////////
                logger.debug("验证成功");
            } else {
                // 该页面可做页面美工编辑
                logger.error("回调验证失败, out_trade_no:{}, trade_no:{}", out_trade_no, trade_no);
                // 支付失败页面
            }
        } catch (Exception e) {
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

	//发送短信


    /**===========================================
     * 测试接口, 可以删除
     */
    @Deprecated
    public String 测试会员支付的逻辑(String out_trade_no) {
        BizUtil.test (378, "测试会员支付的逻辑");
        ShopStoreOrder order = orderService.getUserOrderNewByNo(out_trade_no);
        logger.info("1-getOrderStatus:" + order.getOrderStatus());
        if (order.getOrderStatus() == OrderStatus.UNPAID.getIntValue()) {
            long time = System.currentTimeMillis();
            PaymentType paymentType = PaymentType.ALIPAY_SDK;
            logger.info("1-修改状态:");
            orderService.updateOrderPayStatus(order, out_trade_no, paymentType, OrderStatus.PAID,OrderStatus.UNPAID, time ,null);
            logger.info("1-修改状态:成功");
            //支付成功会的回调
            shopProductService.updateTabTypeAfterPaySuccess(out_trade_no);
            // ==================================================================
            //购买会员, 直接返回
            if (order.getClassify ()!= null && order.getClassify () == 2){
                return out_trade_no;
            }
            // ==================================================================
            logger.info("1-店家精选:成功");
            //商家优惠券已使用,做统计
            long orderNo = order.getOrderNo();
            Wrapper<StoreCouponUseLogNew> wrapper = new EntityWrapper<StoreCouponUseLogNew>();
            wrapper.eq("OrderNo", orderNo)
                    .eq("Status", 0)
                    .ne("supplier_id", 0);
            List<StoreCouponUseLogNew> storeCouponUseLogNewList = storeCouponUseLogNewMapper.selectList(wrapper);
            if(storeCouponUseLogNewList.size()>0){
                StoreCouponNew storeCouponNew = storeCouponNewMapper.selectById(storeCouponUseLogNewList.get(0).getCouponId());
                long couponTemplateId = storeCouponNew.getCouponTemplateId();
                storeCouponNewService.doStatisticsByCouponTemplateIdWhenUse(couponTemplateId);

            }
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
            StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
            long supplierId = storeOrderNew.getSupplierId();
            UserNew userNew = userNewMapper.selectById(supplierId);
            sendText(userNew.getPhone(),userNew.getBusinessName(),templateId);
            //如果是售后订单，同步更新售后表信息
            if(order.getOrderType() == OrderType.AFTERSALE.getIntValue()){
                int count = afterSaleService.updateServiceOrderPaid(order.getOrderNo());
                System.out.println("afterSaleService update num:" + count);
            }
//                        }
        }
        return out_trade_no;
    }

    //发送短信
	private void sendText(String phoneNumber, String businessName, int templateNumber) {
		if (phoneNumber == null || "".equals(phoneNumber)) {
			logger.info("该门店号码为空");
		} else {
			JSONArray param = new JSONArray();
			param.add(businessName);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			param.add(sdf.format(new Date()));
			yunXinSmsService.sendNotice(phoneNumber, param, templateNumber);
		}
	}

	/**
     * 同步回调处理逻辑
     *
     * @return
     */
    public String payCallbackDispose(UriParams uriParams,String version) {
        logger.info("pay callback log:" + uriParams);

        String out_trade_no = uriParams.getSingle("out_trade_no");
        ShopStoreOrder order = orderService.getUserOrderNewByNo(out_trade_no);
        if (order == null) {
            String msg = "can not find order of " + out_trade_no;
            logger.error(msg);
            throw new IllegalStateException(msg);
        }

        try {
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
            // 商户订单号
            // 支付宝交易号
            String trade_no = uriParams.getSingle("trade_no");
            logger.debug("支付宝订单号：" + trade_no);
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
            // 计算得出通知验证结果
            boolean verify_result = AlipayNotify.verify(uriParams.asSingleValueMap(","));
            if (verify_result) {// 验证成功
                // ////////////////////////////////////////////////////////////////////////////////////////
                // 请在这里加上商户的业务逻辑程序代码

                // ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
                // 交易状态
                String trade_status = uriParams.getSingle("trade_status");
                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                    // 判断该笔订单是否在商户网站中已经做过处理
                    // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    // 如果有做过处理，不执行商户的业务程序

                    // 设置返回显示
                    // add by LWS
                    // 更新订单状态
                    if (order.getOrderStatus() == OrderStatus.UNPAID.getIntValue()) {
                        long time = System.currentTimeMillis();
                        PaymentType paymentType = PaymentType.ALIPAY;

//                        if(order.getOrderType() == OrderType.SEND_BACK){
//                         // 如果是回寄订单，则将状态更新为待审核状态
//                            orderService.updateOrderPayStatus(order, trade_no, paymentType, OrderStatus.UNCHECK,
//                                OrderStatus.UNPAID, time);
//                        }else{
                            // 如果是普通直购订单，则将状态更新为已支付状态
                        	orderService.updateOrderPayStatus(order, trade_no, paymentType, OrderStatus.PAID, OrderStatus.UNPAID, time, version);
                            shopProductService.updateTabTypeAfterPaySuccess(out_trade_no);
                            // ==================================================================
                            //购买会员, 直接返回
                            if (order.getClassify ()!= null && order.getClassify () > 2){
                                return out_trade_no;
                            }
                            // ==================================================================
                            //优惠券已使用,做统计
                            long orderNo = order.getOrderNo();
                            Wrapper<StoreCouponUseLogNew> wrapper = new EntityWrapper<StoreCouponUseLogNew>();
                            wrapper.eq("OrderNo", orderNo)
                                   .eq("Status", 0)
                                   .ne("supplier_id", 0);
                            List<StoreCouponUseLogNew> storeCouponUseLogNewList = storeCouponUseLogNewMapper.selectList(wrapper);
                            if(storeCouponUseLogNewList.size()>0){
                            	StoreCouponNew storeCouponNew = storeCouponNewMapper.selectById(storeCouponUseLogNewList.get(0).getCouponId());
                            	long couponTemplateId = storeCouponNew.getCouponTemplateId();
                            	storeCouponNewService.doStatisticsByCouponTemplateIdWhenUse(couponTemplateId);

                            }
//                            //支付成功时发放交易奖金
//                            logger.info("支付成功时发放交易奖金:开始orderNo："+order.getOrderNo());
//                            grantDealBonuses(order);

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
                            StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
                            long supplierId = storeOrderNew.getSupplierId();
                            UserNew userNew = userNewMapper.selectById(supplierId);
                            sendText(userNew.getPhone(),userNew.getBusinessName(),templateId);
//                        }

                        upSendCoupon(orderNo);
                    }
                    // String payinfoAfter = makePaymentFromAlipay(params);
                    // logger.info(payinfoAfter);
                }
                // 该页面可做页面美工编辑

                // ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
                // 支付成功页面
                // ////////////////////////////////////////////////////////////////////////////////////////
                logger.debug("验证成功");
            } else {
                // 该页面可做页面美工编辑
                logger.error("回调验证失败, out_trade_no:{}, trade_no:{}", out_trade_no, trade_no);
                // 支付失败页面
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        return out_trade_no;
    }

    @Transactional(rollbackFor = Exception.class)
	public Map<String, Object> paySuceessNew(String orderNo, long userId) {
//		logger.error("paySuceessNew first--------------------orderNo:" + orderNo + " userId" + userId);
		Map<String, Object> data = new HashMap<String, Object>();
		ShopStoreOrder order = orderService.getUserOrderNewByNo(userId, orderNo);
		long currentTime = System.currentTimeMillis();
		if(order !=null) {
			
			Map<String, Object> map = new HashMap<String, Object>();
			String buyer = "";
			String address = "";
			
			String expressInfo = order.getExpressInfo();
			if(expressInfo != null){
				
				String[] expressInfoArray = expressInfo.split(",");
				data.put("orderNoStr", order.getOrderNoStr());
				
				map.put("orderNo", order.getOrderNo());
				map.put("totalMoney", order.getTotalExpressMoney() + order.getTotalPay());//总付款金额
				if(expressInfo.length() > 2) {
					buyer = expressInfoArray[0];
					address = expressInfoArray[2];
				}
				
				map.put("buyer", buyer);
				map.put("address", address);
				//		data.put("order", orderService.getOrderByNo(orderNo));
				data.put("order", map);
//				data.put("buyAlsoProduct", dataminingAdapter.getBuyAlsoProductNew(userId, order, new PageQuery(1, 4)));
			}
			
			/**
			 * @author Jeff.Zhan
			 * 邀请有礼规则2：被邀请人在注册后每X个成功订单送邀请人玖币
			 */
//			InvitedUserActionLog actionLog = invitedUserActionLogService.getByUserId(userId, 0);
//			if (actionLog != null) {
//				//记录
//				long invitorId = actionLog.getInvitor();
//				User user = userService.getUser(invitorId);
//				long lastInviteOrderTime = user.getLastInviteOrderTime();
//				int weekInviteOrderCount = user.getWeekInviteOrderCount();
//				if (lastInviteOrderTime < DateUtil.getWeekStart().getTime()) {
//					weekInviteOrderCount = 1;
//				} else {
//					weekInviteOrderCount++;
//				}
//				userService.updateWeekInviteOrderCount(invitorId, weekInviteOrderCount);
				
				//被邀请人行为记录表
//				InvitedUserActionLog invitedUserActionLog = new InvitedUserActionLog();
//				invitedUserActionLog.setAction(1);
//				invitedUserActionLog.setUserId(userId);
//				invitedUserActionLog.setInvitor(invitorId);
//				invitedUserActionLog.setRelatedId(user.getyJJNumber());
//				invitedUserActionLog.setCreateTime(currentTime);
//				invitedUserActionLogService.add(invitedUserActionLog);
				
//				JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.INVITE_GIFT_2);
//				for (Object object : jsonArray) {
//					JSONObject jsonObject = (JSONObject)object;
//					int expired_days = Integer.parseInt(jsonObject.get("expired_days").toString());
//					long createTime = actionLog.getCreateTime();
//					long currenTime = System.currentTimeMillis();
//					
//					long expiredTime = expired_days * DateUtils.MILLIS_PER_DAY;	
//					
//					if (currenTime - createTime <= expiredTime) {
//						int orderCount = invitedUserActionLogService.getNewInvitedOrderCount(invitorId, 1, expiredTime);
//						
//						int every_order_count = Integer.parseInt(jsonObject.get("every_order_count").toString());
//						
//						if (orderCount != 0 && orderCount % every_order_count == 0) {
//							int week_limit_time = Integer.parseInt(jsonObject.get("week_limit_time").toString());
//							
//							if (weekInviteOrderCount < week_limit_time) {
//								int jiuCoin = Integer.parseInt(jsonObject.get("jiuCoin").toString());
//								int coupon_count = Integer.parseInt(jsonObject.get("coupon_count").toString());
//								long coupon_template_id = Long.parseLong(jsonObject.get("coupon_template_id").toString());
//								
////								userCoinService.updateUserCoin(invitorId, 0, 0, "inviteGift_2 rule", currenTime, UserCoinOperation.REGISTER_GRANT);
//								
//								//发放代金券
////								try {
////									orderCouponService.getCoupon(coupon_template_id, coupon_count, invitorId, CouponGetWay.INVITE, true);
////								} catch (Exception e) {
////									// TODO: handle exception
////									logger.error("com.yujj.business.facade.ChargeFacade 规则二代金券发放失败，模板id:" + coupon_template_id);
////								}
//								
//								UserInviteRewardLog userInviteRewardLog = new UserInviteRewardLog();
//						        userInviteRewardLog.setCount(coupon_count);
//						        userInviteRewardLog.setCouponTemplateId(coupon_template_id);
//						        userInviteRewardLog.setJiuCoin(jiuCoin);
//						        userInviteRewardLog.setCreateTime(currentTime);
//						        userInviteRewardLog.setUserId(invitorId);
//						        userInviteRewardLogService.add(userInviteRewardLog);
//							} 
//						}
//					}
//				}
			}
//		}
		
		return data;
	}
    
//    /**
//     * 支付成功时发放交易奖金
//     * @param order
//     */
//    private void grantDealBonuses(ShopStoreOrder order){
//    	//1.确认收货获取地推人员id和上级ids
//    	GroundUser groundUserById = null;
//		Long groundUserId = order.getGroundUserId();
//		logger.info("支付成功时发放交易奖金:groundUserId："+groundUserId);
//		if(groundUserId!=null && groundUserId>0){
//			//2.获取对应的地推人员信息
//			Wrapper<GroundUser> wrapper = new EntityWrapper<GroundUser>().eq("id", groundUserId);
//			List<GroundUser> groundUserList = groundUserMapper.selectList(wrapper);
//			logger.info("支付成功时发放交易奖金:groundUserList.size()："+groundUserList.size());
//			if(groundUserList.size()>0){
//				groundUserById = groundUserList.get(0);
//			}
//		}
//		logger.info("支付成功时发放交易奖金:groundUserById："+groundUserById.getId());
//    	
//    	//门店订单确认收货时发放个人门店订单交易奖金和团队订单交易奖金
//  		//获取对应的门店
//  		Wrapper<StoreBusiness> storeBusinessNewWrapper = new EntityWrapper<StoreBusiness>().eq("id", order.getStoreId());
//  		List<StoreBusiness> storeBusinessNewList = storeMapper.selectList(storeBusinessNewWrapper);
//  		logger.info("支付成功时发放交易奖金:storeBusinessNewList.size()："+storeBusinessNewList.size());
//  		if(storeBusinessNewList.size()>0){
//  			StoreBusiness storeBusinessNew = storeBusinessNewList.get(0);
//  			//判断门店当前处于第一阶段
//  			logger.info("支付成功时发放交易奖金:第一阶段时间："+storeBusinessNew.getOneStageTime());
//  			if(storeBusinessNew.getOneStageTime()>=DateUtil.getToday()){
//  				groundBonusGrantFacade.grantBonus(groundUserById, GroundBonusGrant.BONUS_TYPE_FIRST_STAGE, order.getStoreId(), 
//  						order.getOrderNo(), "");
//  			//判断门店当前处于第二阶段
//  			logger.info("支付成功时发放交易奖金:第二阶段时间："+storeBusinessNew.getOneStageTime());	
//  			}else if(storeBusinessNew.getOneStageTime()>=DateUtil.getToday()){
//  				groundBonusGrantFacade.grantBonus(groundUserById, GroundBonusGrant.BONUS_TYPE_SECOND_STAGE, order.getStoreId(), 
//  						order.getOrderNo(), "");
//  			//判断门店当前处于第阶段
//  			logger.info("支付成功时发放交易奖金:第三阶段时间："+storeBusinessNew.getOneStageTime());
//  			}else if(storeBusinessNew.getOneStageTime()>=DateUtil.getToday()){
//  				groundBonusGrantFacade.grantBonus(groundUserById, GroundBonusGrant.BONUS_TYPE_THIRD_STAGE, order.getStoreId(), 
//  						order.getOrderNo(), "");
//  			}
//  		}
//    }
    
}
