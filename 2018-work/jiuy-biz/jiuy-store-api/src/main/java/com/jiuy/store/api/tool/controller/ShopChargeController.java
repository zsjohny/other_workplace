/**
 * 
 */
package com.jiuy.store.api.tool.controller;

import java.util.Map;

import com.jiuyuan.util.BizUtil;
import com.store.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.service.common.IStoreOrderNewService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.ShopStoreOrder;

/**
 * @author LWS
 */
@Login
@Controller
@RequestMapping("/shop/pay")
public class ShopChargeController {

    private static final Logger logger = LoggerFactory.getLogger("PAY");

    @Autowired
    private ShopOrderService shopOrderService;
    
    @Autowired
    private OrderService orderService;

    @Autowired
    private ChargeDelegator chargeDelegator;
    
    @Autowired
    private ShopProductService shopProductService;
    
    @Autowired
    private IStoreOrderNewService storeOrdernewService;
    
    
    @RequestMapping("/lockStoreOrder/auth")
    @ResponseBody
    public JsonResponse lockStoreOrder(@RequestParam("orderNo") long orderNo,
    		                           UserDetail<StoreBusiness> userDetail){
    	JsonResponse jsonResponse = new JsonResponse();
    	long userId = userDetail.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	try {
   	 	    storeOrdernewService.lockStoreOrder(orderNo,userId);
   	 	    return jsonResponse.setSuccessful();
		} catch (RuntimeException e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
			
		}
    	
    }
    
    @RequestMapping("/unlockStoreOrder/auth")
    @ResponseBody
    public JsonResponse unlockStoreOrder(@RequestParam("orderNo") long orderNo,
    		UserDetail<StoreBusiness> userDetail){
    	JsonResponse jsonResponse = new JsonResponse();
    	long userId = userDetail.getId();
    	if(userId == 0){
    		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
    	}
    	try {
    		storeOrdernewService.unlockStoreOrder(orderNo,userId);
    		return jsonResponse.setSuccessful();
    	} catch (RuntimeException e) {
    		logger.info(e.getMessage());
    		return jsonResponse.setError(e.getMessage());
    	}catch (Exception e) {
    		logger.info(e.getMessage());
    		return jsonResponse.setError("服务器开小差了，请稍后再试");
    		
    	}
    	
    }
    @RequestMapping("/directpaynew/auth")
    @ResponseBody
    public JsonResponse payDisposeNew(@RequestParam("out_trade_no") String out_trade_no, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long userId = userDetail.getId();
    	ShopStoreOrder order = shopOrderService.getUserOrderNewByNo(userId, out_trade_no);
    	if (null == order || order.getOrderStatus() != OrderStatus.UNPAID.getIntValue() || System.currentTimeMillis() >  order.getExpiredTime()) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
    	}
    	if((order.getTotalPay() + order.getTotalExpressMoney())==0){
    		orderService.updateOrderPayStatus(order, null, PaymentType.UNKNOWN, OrderStatus.PAID,
                    OrderStatus.UNPAID, System.currentTimeMillis() ,"");
            shopProductService.updateTabTypeAfterPaySuccess(out_trade_no);
    	}
    	String toClient = chargeDelegator.mobilePayDisposeNew(order);
    	logger.debug("send to client :" + toClient);
    	return jsonResponse.setSuccessful().setData(toClient);
    }



	@Autowired
	private ChargeFacade chargeFacade;
	/**
	 * 支付宝测试
	 *
	 * {@link ShopChargeController#payDisposeNew(java.lang.String, com.jiuyuan.entity.UserDetail)}
	 * @param out_trade_no
	 * @param userDetail
	 * @return
	 */
//    @RequestMapping("/directpaynewTest")
//    @ResponseBody
    public JsonResponse 支付宝支付签名测试接口(@RequestParam("out_trade_no") String out_trade_no, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
		BizUtil.test (378,"支付宝支付签名测试接口");
    	String toClient = chargeFacade.生成支付宝支付签名测试模拟方法 (out_trade_no);
    	logger.debug("send to client :" + toClient);
    	return jsonResponse.setSuccessful().setData(toClient);
    }
    @Autowired
	private WeixinChargeFacade weixinChargeFacade;
    /**
	 * 微信测试
	 *
	 * {@link ShopChargeController#weixinPayDisposeNew(java.lang.String, com.jiuyuan.entity.UserDetail, java.lang.String)}
	 * @param out_trade_no
	 */
    @RequestMapping("/directpay/weixin/newTest")
    @ResponseBody
    public JsonResponse 微信支付签名测试接口(@RequestParam("out_trade_no") String out_trade_no,@ClientIp String ip) {
    	BizUtil.test (378, "微信支付签名测试接口");
    	JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = weixinChargeFacade.微信支付签名模拟接口 (out_trade_no, ip);
    	return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping("/directpay/weixin/new/auth")
    @ResponseBody
    @Transactional(rollbackFor=Exception.class)
    public JsonResponse weixinPayDisposeNew(@RequestParam("out_trade_no") String out_trade_no, UserDetail userDetail,
    		@ClientIp String ip) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long userId = userDetail.getId();
    	ShopStoreOrder order = shopOrderService.getUserOrderNewByNo(userId, out_trade_no);
    	if (null == order || order.getOrderStatus() != OrderStatus.UNPAID.getIntValue() || System.currentTimeMillis() >  order.getExpiredTime()) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
    	}
    	
//    	测试交易奖金将订单商品折后总价改为0.01
//    	order.setTotalPay(0.01);
    	
    	if((order.getTotalPay() + order.getTotalExpressMoney())==0){
    		orderService.updateOrderPayStatus(order, null, PaymentType.UNKNOWN, OrderStatus.PAID,
                    OrderStatus.UNPAID, System.currentTimeMillis() ,"");
            shopProductService.updateTabTypeAfterPaySuccess(out_trade_no);
    	}
    	Map<String, Object> data = chargeDelegator.weixinPayDisposeNew(order, ip);
//    	if(data.get("errorMsg") != null && data.get("errorMsg").toString().length() > 0){
//    		return jsonResponse.setError((String) data.get("errorMsg")).setResultCode(ResultCode.ORDER_NUM_DUPLICATE);
//    	}
    	logger.debug("send to client :" + data);
    	return jsonResponse.setSuccessful().setData(data);
    }
    
//    @RequestMapping("/afterSale/refund")
//    @ResponseBody
//    public JsonResponse alipayRefund(@RequestParam("out_trade_no") String out_trade_no,
//    		                         @RequestParam("refund_amount") String refundAmount,
//    		                         @RequestParam("refund_reason") String refundReason,
//    		                         UserDetail userDetail){
//    	JsonResponse jsonResponse = new JsonResponse();
//    	long userId = userDetail.getId();
//    	StoreOrderNew order = storeOrdernewService.getStoreOrderByOrderNo(Long.parseLong(out_trade_no));
//    	if (null == order || order.getOrderStatus() != OrderStatus.PAID.getIntValue() && order.getOrderStatus() !=OrderStatus.DELIVER.getIntValue()) {
//    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
//    	}
//    	try {
//    		//暂时定为这几个传参，等售后确定后，改变传参
//    		refundService.alipayRefund(order,refundAmount,refundReason,NumberUtil.genOrderNo(3));
//    		return jsonResponse.setSuccessful();
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return jsonResponse.setError("支付宝退款失败！");
//		}
//    	
//    }
    
//    @RequestMapping("/afterSale/weixin/refund")
//    @ResponseBody
//    public JsonResponse weixinRefund(@RequestParam("out_trade_no") String out_trade_no,
//                                     @RequestParam("refund_amount") String refundAmount,
//                                     @RequestParam("refund_reason") String refundReason,
//                                     UserDetail userDetail,
//    		                         @ClientIp String ip){
//    	JsonResponse jsonResponse = new JsonResponse();
//    	long userId = userDetail.getId();
//    	StoreOrderNew order = storeOrdernewService.getStoreOrderByOrderNo(Long.parseLong(out_trade_no));
//    	if (null == order || order.getOrderStatus() != OrderStatus.PAID.getIntValue() && order.getOrderStatus() !=OrderStatus.DELIVER.getIntValue()) {
//    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
//    	}
//    	try {
//    		Map<String,String> map = refundService.weixinRefund(order,refundAmount,refundReason,false,NumberUtil.genOrderNo(3));
//    		return jsonResponse.setSuccessful().setData(map);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return jsonResponse.setError("微信退款失败！");
//		}
//    }
}