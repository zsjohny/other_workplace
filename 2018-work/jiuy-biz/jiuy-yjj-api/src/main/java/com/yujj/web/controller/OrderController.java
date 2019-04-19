package com.yujj.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
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
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.OrderType;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.freemarker.FreeMarkerTemplateRenderer;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.assembler.ProductPropAssembler;
import com.yujj.business.facade.OrderFacade;
import com.yujj.business.service.ExpressInfoService;
import com.yujj.business.service.ExpressService;
import com.yujj.business.service.OrderService;
import com.yujj.business.service.ProductSKUService;
import com.yujj.business.service.ProductService;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.YJJUserAddressService;
//import com.yujj.entity.account.Address;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.order.ExpressInfo;
import com.yujj.entity.order.OrderItem;
import com.yujj.entity.order.OrderItemVO;
import com.yujj.entity.order.OrderNew;
import com.yujj.entity.order.OrderNewVO;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductPropVO;
import com.yujj.entity.product.ProductSKU;

import freemarker.template.TemplateException;

@Login
@Controller
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger("PAYMENT");
    
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSKUService productSKUService;

    @Autowired
    private YJJUserAddressService userAddressService;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private ProductPropAssembler productPropAssembler;
    
    @Autowired
    private OrderService orderService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private OrderFacade orderFacade;
    
    @Autowired
    private ExpressInfoService expressInfoService;
    
    @Autowired
    private FreeMarkerTemplateRenderer freeMarkerTemplateRenderer;

    @RequestMapping(value = "/build", method = RequestMethod.POST)
    public String build(@RequestParam("sku_id") long skuId, @RequestParam("count") int count, UserDetail userDetail,
                        Map<String, Object> model) {
        ProductSKU productSKU = productSKUService.getProductSKU(skuId);
        //屏蔽 2016-06-08
        if (1==1 || productSKU == null || productSKU.getRemainCount() < count) {
            logger.warn("sku is not available, skuId:{}", skuId);
            return Constants.ERROR_MAINTENANCE;
        }
        model.put("sku", productSKU);

        Product product = productService.getProductById(productSKU.getProductId());
        if (product == null || !productSKU.isOnSaling()) {
            logger.warn("product is not available, productId:{}", productSKU.getProductId());
            return Constants.ERROR_MAINTENANCE;
        }
        model.put("product", product);

        List<ProductPropVO> skuPropVOs = productSKU.getProductProps();
        productPropAssembler.assemble(skuPropVOs);
        model.put("skuProps", skuPropVOs);

        long userId = userDetail.getUserId();
        List<Address> addresses = userAddressService.getUserAddresses(userId);
        model.put("addresses", addresses);
        
        UserCoin userCoin = userCoinService.getUserCoin(userId);
        if (userCoin != null) {
            model.put("avalCoins", userCoin.getAvalCoins());
            model.put("unavalCoins", userCoin.getUnavalCoins());
        }

        return "order/build";
    }

    
    /**
     *  该接口初步判断已经废弃，注意测试，如果确定是在使用则打开
     * @param skuCountPairArray
     * @param addressId
     * @param avalCoinUsed
     * @param unavalCoinUsed
     * @param orderType
     * @param remark
     * @param clientPlatform
     * @param ip
     * @param userDetail
     * @return
     */
//    @SuppressWarnings("deprecation")
//	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResponse confirm(@RequestParam("sid_count") String[] skuCountPairArray,
//                                @RequestParam("addr_id") int addressId,
//                                @RequestParam("aval_coin_used") int avalCoinUsed,
//                                @RequestParam("unaval_coin_used") int unavalCoinUsed,
//                                @RequestParam("type") OrderType orderType, String remark,
//                                ClientPlatform clientPlatform, @ClientIp String ip, UserDetail userDetail) {
//        JsonResponse jsonResponse = new JsonResponse();
//
//        //屏蔽 2016-06-08
//        if (1==1 || orderType == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        long userId = userDetail.getUserId();
//        Address address = userAddressService.getUserAddress(userId, addressId);
//        if (address == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
//        for (String skuCountPair : skuCountPairArray) {
//            String[] parts = StringUtils.split(skuCountPair, ":");
//            skuCountMap.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
//        }
//
//        Order order = orderFacade.buildOrderXX(userId, skuCountMap, new ConsumeWrapper());
//        if (order == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        if (order.getAvalCoinUsed() != avalCoinUsed || order.getUnavalCoinUsed() != unavalCoinUsed) {
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_OUTDATED);
//        }
//
//        orderFacade.createOrder(order, orderType, remark, address, clientPlatform, ip);
//
//        Map<String, Object> data = new HashMap<String, Object>();
//        if (order.getPayAmountInCents() > 0) {
//            /*************modification on 2015/08/02***********************/
//            /**************************************************************
//             * 1. 增加支付请求跳转路径
//             * 2. 增加订单信息到返回值
//            /*************************************************************/
//            UriBuilder builder = new UriBuilder("/pay/directpay.do");
//            builder.set("out_trade_no", order.getOrderNo());
//            data.put("redirectUrl", builder.toUri());
//            /**************end modification********************************/
//        } else {
//            UriBuilder builder = new UriBuilder("/order/result.do");
//            builder.set("order_no", order.getOrderNo());
//            data.put("redirectUrl", builder.toUri());
//        }
//
//        return jsonResponse.setSuccessful().setData(data);
//    }
//    删除旧表 很明显该接口已经废弃
//    @SuppressWarnings("deprecation")
//	@RequestMapping(value = "/confirmv2", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResponse confirmV2(@RequestParam("sid_count") String[] skuCountPairArray,
//                                  @RequestParam("addr_id") int addressId,
//                                  @RequestParam("aval_coin_used") int avalCoinUsed,
//                                  @RequestParam("unaval_coin_used") int unavalCoinUsed,
//                                  @RequestParam("type") OrderType orderType,
//                                  @RequestParam(value = "express_supplier", required = false) String expressSupplier,
//                                  @RequestParam(value = "express_order_no", required = false) String expressOrderNo,
//                                  @RequestParam(value = "phone", required = false) String phone, String remark,
//                                  ClientPlatform clientPlatform, @ClientIp String ip, UserDetail userDetail) {
//        JsonResponse jsonResponse = new JsonResponse();
//
//        //屏蔽 2016-06-08
//        if (1==1 || orderType == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        long userId = userDetail.getUserId();
//        Address address = userAddressService.getUserAddress(userId, addressId);
//        if (address == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
//        for (String skuCountPair : skuCountPairArray) {
//            String[] parts = StringUtils.split(skuCountPair, ":");
//            skuCountMap.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
//        }
//
//        Order order = orderFacade.buildOrderXX(userId, skuCountMap, new ConsumeWrapper());
//        if (order == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        if (order.getAvalCoinUsed() != avalCoinUsed || order.getUnavalCoinUsed() != unavalCoinUsed) {
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_OUTDATED);
//        }
//
//        orderFacade.createOrder(order, orderType, expressSupplier, expressOrderNo, phone, remark, address,
//            clientPlatform, ip);
//
//        Map<String, Object> data = new HashMap<String, Object>();
//        if (order.getPayAmountInCents() > 0) {
//            /************* modification on 2015/08/02 ***********************/
//            /**************************************************************
//             * 1. 增加支付请求跳转路径 2. 增加订单信息到返回值 /
//             *************************************************************/
//            UriBuilder builder = new UriBuilder("/pay/directpay.do");
//            builder.set("out_trade_no", order.getOrderNo());
//            data.put("redirectUrl", builder.toUri());
//            /************** end modification ********************************/
//        } else {
//            UriBuilder builder = new UriBuilder("/order/result.do");
//            builder.set("order_no", order.getOrderNo());
//            data.put("redirectUrl", builder.toUri());
//        }
//
//        return jsonResponse.setSuccessful().setData(data);
//    }

    @RequestMapping("/result")
    public String result(@RequestParam("order_no") String orderNo, UserDetail userDetail, Map<String, Object> model) {
        long userId = userDetail.getUserId();
//        Order order = orderService.getUserOrderByNo(userId, orderNo);
        OrderNew orderNew = orderService.getUserOrderNewByNo(userId, orderNo);
        if (orderNew == null) {
            return Constants.ERROR_PAGE_NOT_FOUND;
        }
        model.put("order", orderNew);

        return "order/result";
    }
    
    /**
     * 根据快递供应商和快递单号查询快递详情
     * 
     * @param supplier 物流公司编码
     * @param orderNo 物流公司快递单号
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    @RequestMapping(value = "/express")
    @ResponseBody
    public JsonResponse expressQuery(@RequestParam("supplier") String supplier, @RequestParam("order_no") String orderNo)
        throws IOException, TemplateException {
        JSON expressData = expressService.queryExpressInfo(supplier, orderNo);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("data", expressData);
        String html = freeMarkerTemplateRenderer.processTemplate("order/express_info.ftl", data);
        return new JsonResponse().setSuccessful().setHtml(html);
    }
    
    /**
     * 根据YJJ订单号查询物流信息
     * 
     * @param orderNo
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    @RequestMapping(value = "/expressv2")
    @ResponseBody
    public JsonResponse expressQuery(@RequestParam("orderNo") String orderNo, UserDetail userDetail)
        throws IOException, TemplateException {
    	 
//        Order order = orderService.getUserOrderByNo(userDetail.getUserId(), orderNo);
        OrderNew orderNew = orderService.getUserOrderNewByNo(userDetail.getUserId(), orderNo);
        JsonResponse jsonResponse = new JsonResponse();
        if (null != orderNew) {
        	  ExpressInfo info = expressInfoService.getUserExpressInfoByOrderNo(userDetail.getUserId(), Long.parseLong(orderNo));
              String supplier = info.getExpressSupplier();
              String expressOrderNo = info.getExpressOrderNo();
//            String supplier = order.getExpressSupplier();
//            String expressOrderNo = order.getExpressOrderNo();
            if(!StringUtils.isBlank(expressOrderNo) && !StringUtils.isBlank(supplier)){
                JSON expressData = expressService.queryExpressInfo(supplier, expressOrderNo);
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("data", expressData);
                String html = freeMarkerTemplateRenderer.processTemplate("order/express_info.ftl", data);
                jsonResponse.setHtml(html);
            }
            return jsonResponse.setSuccessful();
        }
        return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_NO_ORDER_FOUND);
    }
    
    

    @RequestMapping("/detail")
    public String detail(@RequestParam("order_no") String orderNo, UserDetail userDetail, Map<String, Object> model) {
        long userId = userDetail.getUserId();
        
//        Order order = orderService.getUserOrderByNo(userId, orderNo);
        OrderNew orderNew = orderService.getUserOrderNewByNo(userId, orderNo);
        if (orderNew == null) {
            return Constants.ERROR_PAGE_NOT_FOUND;
        }
        model.put("order", orderNew);
        //删除旧表，改为调用新方法注意测试
//        List<OrderItemVO> orderItems = orderFacade.getOrderItemVOs(userId, order);
        List<OrderItemVO> orderItems = orderFacade.getOrderNewItemVOs(userId, Long.parseLong(orderNo));
        
        model.put("orderItems", orderItems);

        return "order/detail";
    }

    
    @RequestMapping("/list")
    public String orderList(@RequestParam(value = "status", required = false) OrderStatus orderStatus,
                            PageQuery pageQuery, UserDetail userDetail, Map<String, Object> model) {
    	logger.info("orderList 旧表删除是这个方法做了修改，如果该语句打印出来则表示该接口正在使用，请认真排查该接口返回数据是否有变化");
    	
    	
        long userId = userDetail.getUserId();

//        int totalCount = orderService.getUserOrderCount(userId, orderStatus);
        int totalCount = orderService.getUserNewOrderCount(userId, orderStatus);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        model.put("pageQuery", pageQueryResult);

//        List<Order> orders = orderService.getUserOrders(userId, orderStatus, pageQuery);
        List<OrderNewVO> orderNews = orderService.getUserOrdersNew(userId, orderStatus, pageQuery);
        
        model.put("orders", orderNews);

        if (CollectionUtils.isEmpty(orderNews)) {
            return "order/list";
        }

        Set<Long> orderNos = new HashSet<Long>();
        for (OrderNewVO orderNew : orderNews) {
        	orderNos.add(orderNew.getOrderNo());//.getId()
        }
        Map<Long, List<OrderItemVO>> orderItemsMap = orderFacade.getOrderNewItemVOMap(userId, orderNos);
        for (OrderNewVO orderNew : orderNews) {
            List<OrderItemVO> orderItems = orderItemsMap.get(orderNew.getOrderNo());//order.getId()
            orderNew.setOrderItems(new ArrayList<OrderItem>(orderItems));
        }
        return "order/list";
    }

    //改该接口带有可能已经废弃
    @RequestMapping(value = "/sendback")
    public String sendBack(@RequestParam("order_no") String orderNo, UserDetail userDetail) {
        long userId = userDetail.getUserId();
        
//      Order order = orderService.getUserOrderByNo(userId, orderNo);
        OrderNew orderNew = orderService.getUserOrderNewByNo(userId, orderNo);
        if (orderNew == null || orderNew.getOrderType() != OrderType.SEND_BACK.getIntValue() || orderNew.getOrderStatus() != OrderStatus.UNCHECK ) { //|| orderNew.isSended()) {
            return Constants.ERROR_PAGE_NOT_FOUND;
        }

        return "order/send_back";
    }

    @RequestMapping(value = "/sendbackv2", params = { "type=1" })
    public String sendBackV2(@RequestParam("sid_count") String[] skuCountPairArray,
                             @RequestParam("addr_id") int addressId,
                             @RequestParam("aval_coin_used") int avalCoinUsed,
                             @RequestParam("unaval_coin_used") int unavalCoinUsed, String remark) {
        return "order/send_backv2";
    }

    @RequestMapping(value = "/sendback/confirm", method = RequestMethod.POST)
	@ResponseBody
    public JsonResponse sendBackConfirm(@RequestParam("order_no") String orderNo,
                                        @RequestParam("express_supplier") String expressSupplier,
                                        @RequestParam("express_order_no") String expressOrderNo,
                                        @RequestParam("phone") String phone, UserDetail userDetail) {
    	logger.info("该接口已经删除已经被其他接口替代，如果被调用到请尽快更改调用新接口进行取消订单");
		JsonResponse jsonResponse = new JsonResponse();
		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
		
//		if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(expressSupplier) ||
//            StringUtils.isBlank(expressOrderNo) || StringUtils.isBlank(phone)) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//        
//        long userId = userDetail.getUserId();
//        Order order = orderService.getUserOrderByNo(userId, orderNo);
//        if (order == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//        //删除旧表
//        orderService.addSendBack(order, expressSupplier, expressOrderNo, phone);
//
//        return jsonResponse.setSuccessful();
    }
    //删除旧表该接口已经被cancelNewOrder替代
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse cancelOrder(@RequestParam("order_no") String orderNo, UserDetail userDetail, ClientPlatform clientPlatform) {
    	
    	logger.info("cancel该接口已经删除已经被其他接口cancelNewOrder替代，如果被调用到请尽快更改调用新接口进行取消订单");
        JsonResponse jsonResponse = new JsonResponse();
        return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        long userId = userDetail.getUserId();
//        Order order = orderService.getUserOrderByNo(userId, orderNo);
//        if (order == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        orderFacade.cancelOrder(order, clientPlatform.getVersion());

//        return jsonResponse.setSuccessful();
    }
    
    @RequestMapping(value = "/cancelNewOrder", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse cancelNewOrder(@RequestParam("order_no") String orderNo, UserDetail userDetail , ClientPlatform clientPlatform) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	long userId = userDetail.getUserId();
    	OrderNew orderNew = orderService.getUserOrderNewByNo(userId, orderNo);
    	if (orderNew == null) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
    	}
    	
    	orderFacade.cancelOrderNew(orderNew, clientPlatform.getVersion());
    	
    	return jsonResponse.setSuccessful();
    }

//    @RequestMapping(value = "/received", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResponse confirmReceive(@RequestParam("order_no") String orderNo, UserDetail userDetail) {
//    	//删除旧表，这个方法中只对老订单表进行了操作，很明显是老接口
//    	logger.info("received该接口已经被其他接口提替代请尽快修改调用新基恩看，如果被调用到请尽快更改调用新接口");
//        JsonResponse jsonResponse = new JsonResponse();
//        return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
////        Order order = orderService.getUserOrderByNo(userDetail.getUserId(), orderNo);
////        if (null == order || order.getOrderStatus() != OrderStatus.DELIVER) {
////            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
////        }
////
////        long time = System.currentTimeMillis();
////        orderService.updateOrderStatus(order, OrderStatus.SUCCESS, OrderStatus.DELIVER, time);
////        return jsonResponse.setSuccessful();
//    }
}
