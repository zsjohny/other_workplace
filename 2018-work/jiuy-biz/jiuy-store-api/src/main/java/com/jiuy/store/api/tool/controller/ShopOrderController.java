package com.jiuy.store.api.tool.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.ExpressInfo;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.ShopStoreOrder;
import com.store.enumerate.OrderType;
import com.store.service.ExpressInfoService;
import com.store.service.ExpressService;
import com.store.service.OrderDelegator;
import com.store.service.OrderInfoDelegator;
import com.store.service.OrderListDelegator;
import com.store.service.OrderService;
import com.store.service.ShopOrderService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 门店订单Controller
 *
 * @author Qiuyuefan
 */
@Login
@Controller
@RequestMapping( "/shop/order" )
public class ShopOrderController{

    private static final Log logger = LogFactory.get();

    @Autowired
    private ShopOrderService shopOrderService;

    @Autowired
    private OrderDelegator orderDelegator;

    @Autowired
    private OrderInfoDelegator orderInfoDelegator;


    @Autowired
    private OrderListDelegator orderListDelegator;


    @Autowired
    private OrderService orderService;

    @Autowired
    private ExpressInfoService expressInfoService;

    @Autowired
    private ExpressService expressService;


    /**
     * 门店订单确认（3.0已经被新接口替代）
     *
     * @param userDetail
     * @return
     */
    @RequestMapping( "/build/auth" )
    @ResponseBody
    public JsonResponse build(@RequestParam( value = "sid_count" ) String[] skuCountPairArray, UserDetail userDetail,
                              @RequestParam( value = "cityName", required = false, defaultValue = "" ) String cityName, ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            logger.info("确认订单入参：skuCountPairArray：" + JSON.toJSONString(skuCountPairArray) + ",cityName:" + cityName);
            return shopOrderService.buildOrder185(userDetail, cityName, skuCountPairArray, clientPlatform);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 生成订单
     * 优惠后的商品价格 + 邮费价格
     *
     * @param skuCountPairArray
     * @param addressId
     * @param orderType
     * @param expressSupplier
     * @param expressOrderNo
     * @param phone
     * @param remark
     * @param cartIds
     * @param clientPlatform
     * @param ip
     * @param userDetail
     * @return
     */
    @RequestMapping( value = "/confirm/auth" )//, method = RequestMethod.POST
    @ResponseBody
    public JsonResponse confirm(@RequestParam( "sid_count" ) String[] skuCountPairArray,
                                @RequestParam( "addr_id" ) int addressId, @RequestParam( "type" ) OrderType orderType,
                                @RequestParam( value = "express_supplier", required = false ) String expressSupplier,
                                @RequestParam( value = "express_order_no", required = false ) String expressOrderNo,
                                @RequestParam( value = "phone", required = false ) String phone, String remark,
                                @RequestParam( value = "cartIds", required = false ) Long[] cartIds,
                                @RequestParam( value = "couponId", required = false ) String couponId,
                                @RequestParam( value = "statisticsIds", required = false ) String statisticsIds, //应为CODE
                                @RequestParam( value = "payCash", required = false, defaultValue = "0" ) double payCash,
                                ClientPlatform clientPlatform, @ClientIp String ip, UserDetail userDetail) {

//	    	DefaultStoreUserDetail defaultStoreUserDetail = new DefaultStoreUserDetail();
//	    	StoreBusiness storeBusiness = new StoreBusiness();
//	    	storeBusiness.setId(164);
//	    	defaultStoreUserDetail.setStoreBusiness(storeBusiness);
//	    	userDetail = defaultStoreUserDetail;
        JsonResponse jsonResponse = new JsonResponse();
        try {
            logger.error("----------------confirm = " + "sid_count:" + Arrays.toString(skuCountPairArray) + ", payCash:" + payCash);
            return shopOrderService.confirmOrder185(skuCountPairArray, addressId, orderType, expressSupplier, expressOrderNo,
                    phone, remark, cartIds, clientPlatform, ip, userDetail, payCash, couponId, statisticsIds);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * http://localhost:8080/shop/order/orderListNew.json
     * storelocal.yujiejie.com/shop/order/orderListNew.json
     * 获取到订单列表
     *
     * @param pageQuery
     * @param userDetail
     * @return
     */
    @RequestMapping( "/orderListNew/auth" )
    @ResponseBody
    public JsonResponse orderlistNew(@RequestParam( value = "status", required = false, defaultValue = "5" ) int status,
                                     PageQuery pageQuery, UserDetail<StoreBusiness> userDetail,
                                     @RequestParam(value = "storeId",required = false) Long storeId) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
//            StoreBusiness userDetail1 = userDetail.getUserDetail();
//            userDetail1.setId(storeId);
            logger.info("新获取到订单列表orderListNew开始");
            long time1 = System.currentTimeMillis();
            Map<String, Object> result = orderListDelegator.getNewOrderList(OrderStatus.getByIntValue(status), pageQuery, userDetail, storeId);
            long time2 = System.currentTimeMillis();
            long time3 = time2 - time1;
            logger.info("获取到订单列表orderListNew结束，总耗时time3：" + time3);
            return jsonResponse.setSuccessful().setData(result);
        } catch (Exception e) {
            e.printStackTrace ();
            logger.error("获取到订单列表orderListNew:" + e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 获取到订单列表
     *
     * @param type       1:进货订单列表；2：供应商订单列表
     * @param pageQuery
     * @param userDetail
     * @return
     */
    @RequestMapping( "/orderlist/auth" )
    @ResponseBody
    public JsonResponse newOrderList(@RequestParam( value = "status", required = false, defaultValue = "5" ) int status,
                                     @RequestParam( value = "type", required = false, defaultValue = "1" ) int type,
                                     PageQuery pageQuery, UserDetail<StoreBusiness> userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            logger.info("旧获取到订单列表orderlist开始");
            long time1 = System.currentTimeMillis();
            Map<String, Object> result = orderDelegator.getNewOrderList(OrderStatus.getByIntValue(status), pageQuery,
                    userDetail, type);
            long time2 = System.currentTimeMillis();
            long time3 = time2 - time1;
            logger.info("获取到订单列表orderlist结束，总耗时time3：" + time3);
            return jsonResponse.setSuccessful().setData(result);
        } catch (Exception e) {
            logger.error("获取到订单列表orderlist:" + e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 获取到订单详情
     *
     * @param orderNo
     * @param userDetail
     * @return
     */
    @RequestMapping( value = "/queryOrderDetail/auth" )
    @ResponseBody
    public JsonResponse queryOrderDetail(@RequestParam( "order_no" ) String orderNo,
                                         UserDetail<StoreBusiness> userDetail) {
//    	DefaultStoreUserDetail defaultStoreUserDetail = new DefaultStoreUserDetail();
//		StoreBusiness storeBusiness1 = new StoreBusiness();
//		storeBusiness1.setId(3187L);
//		defaultStoreUserDetail.setStoreBusiness(storeBusiness1);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            Map<String, Object> result = orderDelegator.getOrderDetail(userDetail, orderNo);
            return jsonResponse.setSuccessful().setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 获取到订单详情
     *
     * @param orderNo
     * @param userDetail
     * @return
     */
    @RequestMapping( value = "/getOrderInfo/auth" )
    @ResponseBody
    public JsonResponse getOrderInfo(@RequestParam( "order_no" ) long orderNo,
                                     UserDetail<StoreBusiness> userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            long storeId = userDetail.getId();
            Map<String, Object> result = orderInfoDelegator.getOrderInfo(storeId, orderNo);
            return jsonResponse.setSuccessful().setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userDetail
     * @return
     */
    @RequestMapping( value = "/received/auth" )
    @ResponseBody
    public JsonResponse confirmReceiveNew(@RequestParam( "order_no" ) String orderNo, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            ShopStoreOrder order = orderService.getUserOrderNewByNo(userDetail.getId(), orderNo);
            if (null == order || order.getOrderStatus() != OrderStatus.DELIVER.getIntValue()) {
                logger.info("order空或者订单状态不为已发货");
                return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
            }
            long time = System.currentTimeMillis();
            orderService.updateOrderNewStatus(order, OrderStatus.SUCCESS, OrderStatus.DELIVER, time);
            return jsonResponse.setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 查看物流页面
     *
     * @param orderNo
     * @param serviceId
     * @param userDetail
     * @return
     */
    @RequestMapping( value = "/orderExpress/auth" )
    @ResponseBody
    public JsonResponse expressQueryDo(@RequestParam( value = "order_no", defaultValue = "0", required = false ) long orderNo, @RequestParam( value = "service_id", defaultValue = "0", required = false ) long serviceId, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            ExpressInfo info = null;
            if (orderNo != 0)
                info = expressInfoService.getUserExpressInfoByOrderNo(userDetail.getId(), orderNo);
            else if (serviceId != 0)
                info = expressInfoService.getUserExpressInfoByServiceId(userDetail.getId(), serviceId);
            if (null == info) {
                logger.info("info不能为空");
                return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
            }
            Map<String, Object> data = new HashMap<String, Object>();
            String supplier = info.getExpressSupplier();
            String expressOrderNo = info.getExpressOrderNo();
            if (! StringUtils.isBlank(expressOrderNo) && ! StringUtils.isBlank(supplier)) {
                JSON expressData = expressService.queryExpressInfo(supplier, expressOrderNo);
                data.put("data", expressData);
            }
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }



    /**
     * 取消订单
     *
     * @param orderNo
     * @param userDetail
     * @param cancelReason
     * @param clientPlatform
     * @return
     */
    @RequestMapping( value = "/cancel/auth" )//, method = RequestMethod.POST
    @ResponseBody
    public JsonResponse cancelOrderNew(@RequestParam( "order_no" ) String orderNo, UserDetail userDetail, @RequestParam( "cancel_reason" ) String cancelReason, ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            return orderDelegator.cancelOrderNew(orderNo, userDetail, cancelReason, clientPlatform);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 获取支付方式
     *
     * @param userDetail
     * @return
     */
    @RequestMapping( value ="/payChoose/auth" )
    @ResponseBody
    public JsonResponse orderPayChoose(UserDetail userDetail) {

        JsonResponse jsonResponse = new JsonResponse();
        try {
            Map<String, Object> result = orderDelegator.orderPayChoose(userDetail);

            return jsonResponse.setSuccessful().setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 查看物流
     *
     * @param orderNo
     * @param userDetail
     * @return
     */
    @RequestMapping( value = "/expressNewJS/auth" )
    @ResponseBody
    public JsonResponse expressQueryNew(@RequestParam( "order_no" ) long orderNo, UserDetail userDetail) {

        JsonResponse jsonResponse = new JsonResponse();
        try {
            return orderDelegator.getExpressInfo(userDetail, orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    private long checkStoreId(UserDetail<StoreBusiness> userDetail) {
        long storeId = userDetail.getUserDetail().getId();
        if (storeId == 0) {
            logger.info("门店商家ID不存在！请排查！");
            throw new RuntimeException("门店商家ID不存在！请排查！");
        }
        return storeId;
    }



    /**
     * 查询用户历史订单
     * @param keyword 查询关键字
     * @param userDetail
     * @return com.jiuyuan.web.help.JsonResponse
     * @auther Charlie(唐静)
     * @date 2018/5/28 15:04
     */
    @RequestMapping( "search/auth" )
    @ResponseBody
    public JsonResponse listSearch(@RequestParam( value = "keyword", defaultValue = "" ) String keyword, UserDetail<StoreBusiness> userDetail, PageQuery pageQuery) {
        try {
            long start = System.currentTimeMillis();
            Map<String, Object> result = orderListDelegator.listSearch(userDetail, keyword, pageQuery);
            long end = System.currentTimeMillis();
            logger.info("查询用户历史订单, 耗时:" + (end - start));

            return JsonResponse.getInstance().setSuccessful().setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.getInstance().setError("查询历史订单失败");
        }
    }



}