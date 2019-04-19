/**
 * 
 */
package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.ServiceTicketStatus;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.ChargeFacade;
import com.yujj.business.service.AfterSaleService;
import com.yujj.business.service.OrderService;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.afterSale.ServiceTicket;
import com.yujj.entity.order.OrderNew;
import com.yujj.web.controller.delegate.ChargeDelegator;

/**
 * @author LWS
 */
@Login
@Controller
@RequestMapping("/mobile/pay")
public class MobileChargeController {

    private static final Logger logger = LoggerFactory.getLogger("PAY");

    @Autowired
    private OrderService orderService;

    @Autowired
    private ChargeDelegator chargeDelegator;
    
    @Autowired
    private ChargeFacade chargeFacade;
    
    @Autowired
    private AfterSaleService afterSaleService;

    @Autowired
    private MemcachedService memcachedService;
    
    @RequestMapping("/directpay")
    @ResponseBody
    public JsonResponse payDispose(@RequestParam("out_trade_no") String out_trade_no, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        long userId = userDetail.getUserId();
//        Order order = orderService.getUserOrderByNo(userId, out_trade_no);
        OrderNew orderNew = orderService.getUserOrderNewByNo(userId, out_trade_no);
        
        if (null == orderNew || orderNew.getOrderStatus() != OrderStatus.UNPAID) {
            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
        }
        String toClient = chargeDelegator.mobilePayDispose(orderNew.getOrderNo());
        logger.debug("send to client :" + toClient);
        return jsonResponse.setSuccessful().setData(toClient);
    }
    
    @RequestMapping("/directpaynew")
    @ResponseBody
    public JsonResponse payDisposeNew(@RequestParam("out_trade_no") String out_trade_no, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long userId = userDetail.getUserId();
    	OrderNew order = orderService.getUserOrderNewByNo(userId, out_trade_no);
    	if (null == order || order.getOrderStatus() != OrderStatus.UNPAID || System.currentTimeMillis() >  order.getExpiredTime()) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
    	}
    	String toClient = chargeDelegator.mobilePayDisposeNew(order);
    	logger.debug("send to client :" + toClient);
    	return jsonResponse.setSuccessful().setData(toClient);
    }
    
    @RequestMapping("/afterSale/auth")
    @ResponseBody
    public JsonResponse payDisposeAftersSale(@RequestParam("service_id") String service_id, @RequestParam("addr_id") long addrId, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	ServiceTicket serviceTicket = afterSaleService.getServiceTicketDetailById(userDetail.getUserId(), Long.parseLong(service_id));
    	if (null == serviceTicket || serviceTicket.getProcessOrderNo() == 0 || serviceTicket.getStatus() != 4) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
    	}
    	String toClient = "";
    	if(serviceTicket.getProcessOrderNo() > 0){
    		OrderNew orderNew = orderService.getUserOrderNewByNo(serviceTicket.getProcessOrderNo() + "");
    		if (addrId > 0 && serviceTicket.getStatus() == ServiceTicketStatus.UNPAID.getIntValue()) {
    			orderService.updateOrderAddressAfterSale(userDetail.getUserId() ,orderNew.getOrderNo(), addrId);
    		}
    		toClient = chargeDelegator.mobilePayDisposeNew(orderNew);
    	}
    	logger.debug("send to client :" + toClient);
    	return jsonResponse.setSuccessful().setData(toClient);
    }
    
    @RequestMapping("/directpay/bankcard")
    @ResponseBody
    public JsonResponse bankCardPayDispose(@RequestParam("out_trade_no") String out_trade_no, UserDetail userDetail,@ClientIp String ip) {
    	JsonResponse jsonResponse = new JsonResponse();
    	//long userId = userDetail.getUserId();
//    	Order order = orderService.getUserOrderByNo(userDetail.getUserId(), out_trade_no);
    	//删除旧表
    	OrderNew orderNew = orderService.getUserOrderNewByNo(userDetail.getUserId(), out_trade_no);
    	
    	if (null == orderNew || orderNew.getOrderStatus() != OrderStatus.UNPAID ) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
    	}
    	String toClient = chargeDelegator.mobileBankCardPayDispose(Long.parseLong(out_trade_no), ip);
    	logger.debug("send to client :" + toClient);
    	return jsonResponse.setSuccessful().setData(toClient);
    }
    
    @RequestMapping("/directpay/bankcardpay")
    @ResponseBody
    public JsonResponse bankCardPayDisposeNew(@RequestParam("out_trade_no") String out_trade_no, UserDetail userDetail,@ClientIp String ip) {
    	JsonResponse jsonResponse = new JsonResponse();
    	//long userId = userDetail.getUserId();
    	OrderNew order = orderService.getUserOrderNewByNo(userDetail.getUserId(), out_trade_no);
    	if (null == order || order.getOrderStatus() != OrderStatus.UNPAID || System.currentTimeMillis() >  order.getExpiredTime()) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
    	}
    	String toClient = chargeDelegator.mobileBankCardPayDisposeNew(order, ip);
    	logger.debug("send to client :" + toClient);
    	return jsonResponse.setSuccessful().setData(toClient);
    }
    
    @RequestMapping("/afterSale/bankcardpay")
    @ResponseBody
    public JsonResponse bankCardPayAfterSale(@RequestParam("service_id") String service_id, @RequestParam("addr_id") long addrId, UserDetail userDetail, @ClientIp String ip) {
    	JsonResponse jsonResponse = new JsonResponse();
    	//long userId = userDetail.getUserId();
    	ServiceTicket serviceTicket = afterSaleService.getServiceTicketDetailById(userDetail.getUserId(), Long.parseLong(service_id));
    	if (null == serviceTicket || serviceTicket.getProcessOrderNo() == 0 || serviceTicket.getStatus() != 4) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
    	}
//    	if (null == serviceTicket || serviceTicket.getOrderStatus() != OrderStatus.UNPAID || System.currentTimeMillis() >  order.getExpiredTime()) {
//    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
//    	}
    	
    	String toClient = chargeDelegator.mobileBankCardPayAfterSale(serviceTicket, addrId, ip);
    	logger.debug("send to client :" + toClient);
    	return jsonResponse.setSuccessful().setData(toClient);
    }
    
    @RequestMapping(value = "/queryPayResult")
    @ResponseBody
    public JsonResponse queryPayResult(@RequestParam("order_no") String orderNo, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, String> data = chargeDelegator.queryPayResultBankCard(orderNo, userDetail);
//    	OrderNew order = orderService.getUserOrderNewByNo(userDetail.getUserId(), orderNo);
//    	if(order != null){
//    		if(order.getOrderStatus().equals(OrderStatus.PAID) && order.getPaymentType() != null && order.getPaymentNo() != null && order.getPaymentNo().length() > 0){
//    			data.put("payResult", "0");
//    		} else {	
//    			data = chargeDelegator.queryPayResult(order);
//    		}
//
//    	}else{
//    		data.put("payResult", "99");	
//    	}

    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping(value = "/queryPayResultAfterSale")
    @ResponseBody
    public JsonResponse queryPayResultByServiceId(@RequestParam("service_id") String serviceId, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, String> data = chargeDelegator.queryPayResultBankCardByServiceId(serviceId, userDetail);

    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
//    @RequestMapping("/checkResult")
//    @ResponseBody
//    public JsonResponse checkResult(@RequestParam("out_trade_no") String out_trade_no, UserDetail userDetail,@ClientIp String ip) {
//    	JsonResponse jsonResponse = new JsonResponse();
//    	//long userId = userDetail.getUserId();
//    	Order order = orderService.getUserOrderByNo(userDetail.getUserId(), out_trade_no);
//    	if (null == order || order.getOrderStatus() != OrderStatus.UNPAID) {
//    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
//    	}
//    	Map<String, String> result=chargeFacade.checkResult(order);
//    	
//    	return jsonResponse.setSuccessful().setData(result);
//    }

    @RequestMapping("/directpay/weixin")
    @ResponseBody
    public JsonResponse weixinPayDispose(@RequestParam("out_trade_no") String out_trade_no, UserDetail userDetail,
                                         @ClientIp String ip) {
        JsonResponse jsonResponse = new JsonResponse();
        long userId = userDetail.getUserId();
//        Order order = orderService.getUserOrderByNo(userId, out_trade_no);
        OrderNew orderNew = orderService.getUserOrderNewByNo(userDetail.getUserId(), out_trade_no);
        if (null == orderNew || orderNew.getOrderStatus() != OrderStatus.UNPAID) {
            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
        }

        Map<String, Object> data = chargeDelegator.weixinPayDispose(Long.parseLong(out_trade_no), ip);
        logger.debug("send to client :" + data);
        return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping("/directpay/weixin/new")
    @ResponseBody
    public JsonResponse weixinPayDisposeNew(@RequestParam("out_trade_no") String out_trade_no, UserDetail userDetail,
    		@ClientIp String ip) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long userId = userDetail.getUserId();
    	OrderNew order = orderService.getUserOrderNewByNo(userId, out_trade_no);
    	if (null == order || order.getOrderStatus() != OrderStatus.UNPAID || System.currentTimeMillis() >  order.getExpiredTime()) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
    	}
    	
    	Map<String, Object> data = chargeDelegator.weixinPayDisposeNew(order, ip);
    	if(data.get("errorMsg") != null && data.get("errorMsg").toString().length() > 0){
    		return jsonResponse.setError((String) data.get("errorMsg")).setResultCode(ResultCode.ORDER_NUM_DUPLICATE);
    	}
    	logger.debug("send to client :" + data);
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping("/afterSale/weixin/new/auth")
    @ResponseBody
    public JsonResponse weixinPayDisposeAfterSale(@RequestParam("service_id") String service_id, @RequestParam("addr_id") int addrId, UserDetail userDetail, @ClientIp String ip) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long userId = userDetail.getUserId();
    	
    	ServiceTicket serviceTicket = afterSaleService.getServiceTicketDetailById(userDetail.getUserId(), Long.parseLong(service_id));
    	if (null == serviceTicket || serviceTicket.getProcessOrderNo() == 0 || serviceTicket.getStatus() != 4) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
    	}
    	Map<String, Object> data = new HashMap<String, Object>();
    	if(serviceTicket.getProcessOrderNo() > 0){
    		OrderNew orderNew = orderService.getUserOrderNewByNo(serviceTicket.getProcessOrderNo() + "");
    		if (addrId > 0 && serviceTicket.getStatus() == ServiceTicketStatus.UNPAID.getIntValue()) {
    			orderService.updateOrderAddressAfterSale(userDetail.getUserId() ,orderNew.getOrderNo(), addrId);
    		}
    		data = chargeDelegator.weixinPayDisposeNew(orderNew, ip);
    	}
    	logger.debug("send to client :" + data);
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping("/directpay/success")
    @ResponseBody
    public JsonResponse paySuceess(@RequestParam(value = "order_no")String orderNo, UserDetail userDetail, ClientPlatform clientPlatform) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	long userId = userDetail.getUserId();
    	
    	if(orderNo != null && orderNo.length() >= 24 ){
    		data = chargeFacade.paySuceess(orderNo, userId);  //老版本订单号
    	}else {
    		data = chargeFacade.paySuceessNew(orderNo, userId, clientPlatform); //新版本订单号
    	}
        
        // 添加add by dongzhong 20160530    	
        	// 活动抽奖标志
            String groupKey = MemcachedKey.GROUP_KEY_ACTIVITY;
            memcachedService.set(groupKey, orderNo, DateConstants.SECONDS_PER_DAY, 1);
        
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping("/test")
    @ResponseBody
    public JsonResponse paySuceess() {
    	JsonResponse jsonResponse = new JsonResponse();
//    	chargeFacade.paySuceessNew("76", 206254L);
    	return jsonResponse.setSuccessful();
    }
    
}
