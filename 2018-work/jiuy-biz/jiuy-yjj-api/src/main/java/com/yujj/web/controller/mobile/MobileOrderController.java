/**
 * 
 */
package com.yujj.web.controller.mobile;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.OrderCouponStatus;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.OrderType;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.ExpressInfoService;
import com.yujj.business.service.ExpressService;
import com.yujj.business.service.OrderService;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.order.ExpressInfo;
import com.yujj.entity.order.OrderNew;
import com.yujj.web.controller.delegate.OrderDelegator;

/**
 * @author LWS
 */
@Login
@Controller
@RequestMapping("/mobile/order")
public class MobileOrderController {

    @Autowired
    private OrderDelegator orderDelegator;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private ExpressInfoService expressInfoService;
  
    private static final Logger logger = LoggerFactory.getLogger("MobileOrderController");

    //删除旧表
//    @RequestMapping("/list")
//    @ResponseBody
//    public JsonResponse orderList(@RequestParam(value = "status", required = false) OrderStatus orderStatus,
//                                  PageQuery pageQuery, UserDetail userDetail) {
//        JsonResponse jsonResponse = new JsonResponse();
//        Map<String, Object> result = orderDelegator.getOrderList(orderStatus, pageQuery, userDetail);
//        return jsonResponse.setSuccessful().setData(result);
//    }
    
    @RequestMapping("/newOrderList")
    @ResponseBody
    public JsonResponse newOrderList(@RequestParam(value = "status", required = false) OrderStatus orderStatus,
    		PageQuery pageQuery, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> result = orderDelegator.getNewOrderList(orderStatus, pageQuery, userDetail);
        
    	
    	return jsonResponse.setSuccessful().setData(result);
    }
    
    @RequestMapping("/newOrderListAfterSale")
    @ResponseBody
    public JsonResponse newOrderListAfterSale(PageQuery pageQuery, UserDetail userDetail, @RequestParam(value = "order_search_no", required = false) String orderSearchNo) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> result = orderDelegator.getNewOrderListAfterSale(pageQuery, userDetail, orderSearchNo);
    	
    	
    	return jsonResponse.setSuccessful().setData(result);
    }
    
    
    @RequestMapping("/couponList")
    @ResponseBody
    public JsonResponse couponList(@RequestParam(value = "status", required = false) OrderCouponStatus status,
    		PageQuery pageQuery, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> result = orderDelegator.getCouponList(status, pageQuery, userDetail);
        
    	
    	return jsonResponse.setSuccessful().setData(result);
    }
    
    @RequestMapping("/couponListHistory")
    @ResponseBody
    public JsonResponse couponListHistory(PageQuery pageQuery, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> result = orderDelegator.getCouponListHistory(pageQuery, userDetail);
    	
    	
    	return jsonResponse.setSuccessful().setData(result);
    }
    
    //现金券提交
    @RequestMapping(value = "/couponExchange/commit")
    @ResponseBody
    public JsonResponse phoneNumberCommit(@RequestParam("exchange_code") String exchangeCode,
                                            UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> result = orderDelegator.userExchangeCoupon(exchangeCode, userDetail);
    	return jsonResponse.setSuccessful().setData(result);
    }

    //删除旧表
//    /**
//     * 构建订单
//     * 
//     * @param reqdev
//     * @param skuId
//     * @param count
//     * @param userDetail
//     * @return
//     */
//    @Deprecated
//    @RequestMapping("/build")
//    @ResponseBody
//    public JsonResponse build(@RequestParam("sku_id") long skuId, @RequestParam("count") int count,
//                              UserDetail userDetail) {
//        JsonResponse jsonResponse = new JsonResponse();
//        Map<String, Object> orderResult = orderDelegator.buildOrder(skuId, count, userDetail);
//        return jsonResponse.setSuccessful().setData(orderResult);
//    }

    //删除旧表
//    @Deprecated
//    @RequestMapping("/buildv2")
//    @ResponseBody
//    public JsonResponse build(@RequestParam("sid_count") String[] skuCountPairArray, UserDetail userDetail, 
//    							@RequestParam(value = "cityName", required = false, defaultValue = "")String cityName, ClientPlatform clientPlatform) {
//    	if (clientPlatform.getVersion().compareTo("1.7.0") >= 0) {
//        	logger.error("----------------buildv2 = "+"sid_count:"+Arrays.toString(skuCountPairArray) + ", userDetail:"+userDetail.getUserId()+", cityName:"+cityName);
//    		return orderDelegator.buildOrder17(userDetail.getUserId(), cityName, skuCountPairArray);
//    	} else {
//
//        	return new JsonResponse().setResultCode(ResultCode.ORDER_ERROR_VERSION_NOT_MATCH);
//    	}
//    }
    
    /**
     * 
     * @param takeGood   1选择门店自取
     * @return
     */
    @RequestMapping("/buildv185")
    @ResponseBody
    public JsonResponse buildv185(@RequestParam("sid_count") String[] skuCountPairArray, UserDetail userDetail, 
    		@RequestParam(value = "cityName", required = false, defaultValue = "")String cityName,
    		@RequestParam(value = "take_good", required = false, defaultValue = "0") int takeGood,ClientPlatform clientPlatform) {
    	return new JsonResponse().setResultCode(ResultCode.ORDER_OUT_OF_SERVICE);
    	
//    	if (clientPlatform.getVersion().compareTo("1.7.0") >= 0) {
//    		logger.error("----------------buildv2 = "+"sid_count:"+Arrays.toString(skuCountPairArray) + ", userDetail:"+userDetail.getUserId()+", cityName:"+cityName+", takeGood:"+takeGood);
//    		return orderDelegator.buildOrder185(userDetail.getUserId(), cityName, skuCountPairArray, clientPlatform,takeGood);
//    	} else {
//    		return new JsonResponse().setResultCode(ResultCode.ORDER_ERROR_VERSION_NOT_MATCH);
//    	}
    }

    /**
     * 确认订单（包括选择订单类型）
     * 
     * @param skuCountPairArray
     * @param addressId
     * @param avalCoinUsed
     * @param unavalCoinUsed
     * @param orderType
     * @param remark
     * @param userDetail
     * @param model
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse confirm(@RequestParam("sid_count") String[] skuCountPairArray,
                                @RequestParam("addr_id") int addressId, @RequestParam("type") OrderType orderType,
                                String remark, ClientPlatform clientPlatform, @ClientIp String ip, UserDetail userDetail) {
        	return new JsonResponse().setResultCode(ResultCode.ORDER_ERROR_VERSION_NOT_MATCH);
    }

    /**
     * TODO该接口已经废弃，如果有用再打开
     * @param skuCountPairArray
     * @param addressId
     * @param orderType
     * @param expressSupplier
     * @param expressOrderNo
     * @param phone
     * @param remark
     * @param cartIds
     * @param totalCash 优惠后的商品价格 + 邮费价格
     * @param clientPlatform
     * @param ip
     * @param userDetail
     * @return
     */
//    @Deprecated
//    @RequestMapping(value = "/confirmv2", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResponse confirmV2(@RequestParam("sid_count") String[] skuCountPairArray,
//                                  @RequestParam("addr_id") int addressId, @RequestParam("type") OrderType orderType,
//                                  @RequestParam(value = "express_supplier", required = false) String expressSupplier,
//                                  @RequestParam(value = "express_order_no", required = false) String expressOrderNo,
//                                  @RequestParam(value = "phone", required = false) String phone, String remark,
//                                  @RequestParam(value = "cartIds", required = false) Long[] cartIds,
//                                  @RequestParam(value = "payCash", required = false, defaultValue = "0") double payCash,
//                                  ClientPlatform clientPlatform, @ClientIp String ip, UserDetail userDetail) {
//
//        if (clientPlatform.getVersion().compareTo("1.7.0") >= 0) {
//        	logger.error("----------------confirmv2 = "+"sid_count:"+Arrays.toString(skuCountPairArray) + ", payCash:"+payCash);
//    		return orderDelegator.confirmOrder17(skuCountPairArray, addressId, orderType, expressSupplier, expressOrderNo,
//    	            phone, remark, cartIds, clientPlatform, ip, userDetail, payCash);
//        } else {
//        	return new JsonResponse().setResultCode(ResultCode.ORDER_ERROR_VERSION_NOT_MATCH);
////            return orderDelegator.confirmOrderXX(skuCountPairArray, addressId, orderType, expressSupplier,
////                expressOrderNo, phone, remark, cartIds, clientPlatform, ip, userDetail);
//        }
//    }
    
    /**
     * 手机端创建订单接口
     * @param skuCountPairArray
     * @param addressId
     * @param orderType
     * @param expressSupplier
     * @param expressOrderNo
     * @param phone
     * @param remark
     * @param cartIds
     * @param totalCash 优惠后的商品价格 + 邮费价格
     * @param clientPlatform
     * @param ip
     * @param userDetail
     * @param takeGood     1自提取货
     * @return
     */
    @RequestMapping(value = "/confirmv185", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse confirmV185(@RequestParam("sid_count") String[] skuCountPairArray,
    		@RequestParam("addr_id") int addressId, @RequestParam("type") OrderType orderType,
    		@RequestParam(value = "express_supplier", required = false) String expressSupplier,
    		@RequestParam(value = "express_order_no", required = false) String expressOrderNo,
    		@RequestParam(value = "phone", required = false) String phone, String remark,
    		@RequestParam(value = "cartIds", required = false) Long[] cartIds,
    		@RequestParam(value = "couponId", required = false) String couponId,
    		@RequestParam(value = "statisticsIds", required = false) String statisticsIds, //应为CODE
    		@RequestParam(value = "payCash", required = false, defaultValue = "0") double payCash,
    		@RequestParam(value = "coinDeductFlag", required = false, defaultValue = "0") int coinDeductFlag,
    		@RequestParam(value = "take_good", required = false, defaultValue = "0") int takeGood,
    		ClientPlatform clientPlatform, @ClientIp String ip, UserDetail userDetail) {
    	return new JsonResponse().setResultCode(ResultCode.ORDER_OUT_OF_SERVICE);
//    	if (clientPlatform.getVersion().compareTo("1.7.0") >= 0) {
//    		logger.error("----------------confirmv2 = "+"sid_count:"+Arrays.toString(skuCountPairArray) + ", payCash:"+payCash);
//    		return orderDelegator.confirmOrder185(skuCountPairArray, addressId, orderType, expressSupplier, expressOrderNo,
//    				phone, remark, cartIds, clientPlatform, ip, userDetail, payCash, couponId, statisticsIds,0L, coinDeductFlag,takeGood);
//    	} else {
//    		return new JsonResponse().setResultCode(ResultCode.ORDER_ERROR_VERSION_NOT_MATCH);
////            return orderDelegator.confirmOrderXX(skuCountPairArray, addressId, orderType, expressSupplier,
////                expressOrderNo, phone, remark, cartIds, clientPlatform, ip, userDetail);
//    	}
    }
    
    //该接口已经废弃
//    @SuppressWarnings("deprecation")
//	@RequestMapping(value = "/precalc")
//    @ResponseBody
//    public JsonResponse preCalculatePaymentAmount(@RequestParam("sid_count") String[] skuCountPairArray,
//                                                  UserDetail userDetail) {
//        return orderDelegator.preCalcPrice(userDetail.getUserId(), skuCountPairArray);
//    }
    
    //删除旧表  改接口已废弃，如果发现有用到则打开
//    /**
//     * 
//     * @param skuCountPairArray
//     * @param addressId
//     * @param orderType
//     * @param expressSupplier
//     * @param expressOrderNo
//     * @param phone
//     * @param remark
//     * @param cartIds
//     * @param totalCash 玖币抵扣  首单优惠改版
//     * @param clientPlatform
//     * @param ip
//     * @param userDetail
//     * @return
//     */
//    @RequestMapping(value = "/confirmv213", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResponse confirmV213(@RequestParam("sid_count") String[] skuCountPairArray,
//    		@RequestParam("addr_id") int addressId, @RequestParam("type") OrderType orderType,
//    		@RequestParam(value = "express_supplier", required = false) String expressSupplier,
//    		@RequestParam(value = "express_order_no", required = false) String expressOrderNo,
//    		@RequestParam(value = "phone", required = false) String phone, String remark,
//    		@RequestParam(value = "cartIds", required = false) Long[] cartIds,
//    		@RequestParam(value = "couponId", required = false) String couponId,
//    		@RequestParam(value = "statisticsIds", required = false) String statisticsIds, //应为CODE
//    		@RequestParam(value = "payCash", required = false, defaultValue = "0") double payCash,
//    		ClientPlatform clientPlatform, @ClientIp String ip, UserDetail userDetail) {
//    	
//    	if (clientPlatform.getVersion().compareTo("1.7.0") >= 0) {
//    		logger.error("----------------confirmv2 = "+"sid_count:"+Arrays.toString(skuCountPairArray) + ", payCash:"+payCash);
//    		return orderDelegator.confirmOrder213(skuCountPairArray, addressId, orderType, expressSupplier, expressOrderNo,
//    				phone, remark, cartIds, clientPlatform, ip, userDetail, payCash, couponId, statisticsIds,0L);
//    	} else {
//    		return new JsonResponse().setResultCode(ResultCode.ORDER_ERROR_VERSION_NOT_MATCH);
////            return orderDelegator.confirmOrderXX(skuCountPairArray, addressId, orderType, expressSupplier,
////                expressOrderNo, phone, remark, cartIds, clientPlatform, ip, userDetail);
//    	}
//    }
    

    
    //删除旧表
//    /**
//     * 回寄订单填写物流等相关信息
//     * 
//     * @param orderNo
//     * @param expressSupplier
//     * @param expressOrderNo
//     * @param phone
//     * @param userDetail
//     * @return
//     */
//    @Deprecated
//    @RequestMapping(value = "/sendback/confirm", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResponse sendBackConfirm(@RequestParam("order_no") String orderNo,
//                                        @RequestParam("express_supplier") String expressSupplier,
//                                        @RequestParam("express_order_no") String expressOrderNo,
//                                        @RequestParam("phone") String phone, UserDetail userDetail) {
//        return orderDelegator.sendBackConfirm(orderNo, expressSupplier, expressOrderNo, phone, userDetail);
//    }

    @RequestMapping(value = "/express")
    public String expressQuery(@RequestParam("groupId") long groupId, UserDetail userDetail, Map<String, Object> model) {
        ExpressInfo info = expressInfoService.getUserExpressInfo(userDetail.getUserId(), groupId);
        if (null == info) {
            return Constants.ERROR_PAGE_NOT_FOUND;
        }

        String supplier = info.getExpressSupplier();
        String expressOrderNo = info.getExpressOrderNo();
        if (!StringUtils.isBlank(expressOrderNo) && !StringUtils.isBlank(supplier)) {
            JSON expressData = expressService.queryExpressInfo(supplier, expressOrderNo);
            model.put("data", expressData);
        }
        return "mobile/express_info";
    }
    
    @RequestMapping(value = "/expressNewJS")
    @ResponseBody
    public JsonResponse expressQueryNew(@RequestParam("order_no") long orderNo, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	return orderDelegator.getExpressInfo(userDetail.getUserId(), orderNo);
//    	Map<String, String> data = orderDelegator.getExpressInfo(userDetail.getUserId(), orderNo);
//    
//   
//    	return jsonResponse.setSuccessful().setData(data);
    }
    @RequestMapping(value = "/expressNew")
    public String expressQueryNewDo(@RequestParam("order_no") long orderNo, UserDetail userDetail, Map<String, Object> model) {
    	ExpressInfo info = expressInfoService.getUserExpressInfoByOrderNo(userDetail.getUserId(), orderNo);
    	if (null == info) {
    		return Constants.ERROR_PAGE_NOT_FOUND;
    	}
    	
    	String supplier = info.getExpressSupplier();
    	String expressOrderNo = info.getExpressOrderNo();
    	if (!StringUtils.isBlank(expressOrderNo) && !StringUtils.isBlank(supplier)) {
    		JSON expressData = expressService.queryExpressInfo(supplier, expressOrderNo);
    		model.put("data", expressData);
    	}
    	return "mobile/express_info";
    }

    @RequestMapping(value = "/expressv2")
    public String expressQueryV2(@RequestParam("orderNo") String orderNo, UserDetail userDetail,
                                 Map<String, Object> model) {
    	//删除旧表
    	logger.info("该接口很明显已经删除，如果有调用到，请联系联系相关开发人员进行修改");
//        Order order = orderService.getUserOrderByNo(userDetail.getUserId(), orderNo);
//        if (null == order) {
//            return Constants.ERROR_PAGE_NOT_FOUND;
//        }
//
//        String supplier = order.getExpressSupplier();
//        String expressOrderNo = order.getExpressOrderNo();
//        if (!StringUtils.isBlank(expressOrderNo) && !StringUtils.isBlank(supplier)) {
//            JSON expressData = expressService.queryExpressInfo(supplier, expressOrderNo);
//            model.put("data", expressData);
//        }
        return "mobile/express_info";
    }
    
    
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse cancelOrder(@RequestParam("order_no") String orderNo, UserDetail userDetail , ClientPlatform clientPlatform) {
    	//删除旧表
    	logger.info("该接口很明显已经删除，如果有调用到，请联系联系相关开发人员进行修改");
//    	return orderDelegator.cancelOrder(orderNo, userDetail,  clientPlatform);
    	JsonResponse jsonResponse = new JsonResponse();
    	return jsonResponse.setSuccessful();
    }
    
    @RequestMapping(value = "/cancelOrderNew", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse cancelOrderNew(@RequestParam("order_no") String orderNo, UserDetail userDetail, @RequestParam("cancel_reason") String cancelReason , ClientPlatform clientPlatform) {
    	return orderDelegator.cancelOrderNew(orderNo, userDetail, cancelReason,  clientPlatform);
    }
    
//    /**
//     * 删除旧表
//     * @param orderNo
//     * @param userDetail
//     * @return
//     */
//    @RequestMapping(value = "/queryOrderStatus")
//    @ResponseBody
//    public JsonResponse queryOrderStatus(@RequestParam("order_no") String orderNo, UserDetail userDetail) {
//    	
//    	
//    	JsonResponse jsonResponse = new JsonResponse();
//    	Map<String, Object> data = new HashMap<String, Object>();
//    	//删除旧表
////    	Order order = orderService.getUserOrderByNoAll(userDetail.getUserId(), orderNo);
////    	if(order!=null){
////    		data.put("orderStatus", order.getOrderStatus());
////    		
////    		//orderService.createNewOrderFromOld(order);
////    	//	orderService.splitOrderNew(userDetail.getUserId()+"",order);
////    		
////    	}
//    	
//    	return jsonResponse.setSuccessful().setData(data);
//    }
    
    /**
     * 
     * @param takeGood   1.到店取货，没有邮费信息
     * @return
     */
    @RequestMapping(value = "/queryOrderDetail")
    @ResponseBody
    public JsonResponse queryOrderDetail(@RequestParam("order_no") String orderNo, 
    		@RequestParam(value = "take_good", required = false, defaultValue = "0") int takeGood,UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> result = orderDelegator.getOrderDetail( userDetail ,orderNo);
    	
    	
    	return jsonResponse.setSuccessful().setData(result);
    }
    
    @RequestMapping(value = "/payChoose")
    @ResponseBody
    public JsonResponse orderPayChoose(UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> result = orderDelegator.orderPayChoose( userDetail);
    	
    	
    	return jsonResponse.setSuccessful().setData(result);
    }
    
    @RequestMapping(value = "/queryOrderSplitDetail")
    @ResponseBody
    public JsonResponse queryOrderSplitDetail(@RequestParam("order_no") String orderNo, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	Map<String, Object> result = orderDelegator.getOrderSplitDetail( userDetail ,orderNo);

    	return jsonResponse.setSuccessful().setData(result);
    }

    @RequestMapping(value = "/received", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse confirmReceive(@RequestParam("order_no") String orderNo, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        //删除旧表
//        Order order = orderService.getUserOrderByNo(userDetail.getUserId(), orderNo);
//        if (null == order || order.getOrderStatus() != OrderStatus.DELIVER) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
//        }

        long time = System.currentTimeMillis();
        //删除旧表
//        orderService.updateOrderStatus(order, OrderStatus.SUCCESS, OrderStatus.DELIVER, time);
        
        //兼容性操作，更新新订单状态
//        OrderNew orderNew = orderService.queryOrderNewFromOld(order);
        OrderNew orderNew = orderService.getUserOrderNewByNo(orderNo);
        
        orderService.updateOrderNewStatus(orderNew, OrderStatus.SUCCESS, OrderStatus.DELIVER, time);
        return jsonResponse.setSuccessful();
    }
    
    @RequestMapping(value = "/receivedNew")
    @ResponseBody
    public JsonResponse confirmReceiveNew(@RequestParam("order_no") String orderNo, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	OrderNew order = orderService.getUserOrderNewByNo(userDetail.getUserId(), orderNo);
    	if (null == order || order.getOrderStatus() != OrderStatus.DELIVER) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
    	}
    	
    	long time = System.currentTimeMillis();
    	orderService.updateOrderNewStatus(order, OrderStatus.SUCCESS, OrderStatus.DELIVER, time);
    	
    	 //兼容性操作，更新旧订单状态
    	//删除旧表
//        Order orderOld = orderService.queryOrderOldFromNew(order);
//        orderService.updateOrderStatus(orderOld, OrderStatus.SUCCESS, OrderStatus.DELIVER, time);
    	return jsonResponse.setSuccessful();
    }
}
