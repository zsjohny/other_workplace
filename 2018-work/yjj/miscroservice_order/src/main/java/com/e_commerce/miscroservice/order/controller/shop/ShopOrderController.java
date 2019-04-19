package com.e_commerce.miscroservice.order.controller.shop;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.order.entity.RequestAddress;
import com.e_commerce.miscroservice.order.service.wx.ShopMemberOrderService;
import com.e_commerce.miscroservice.order.vo.StoreBizOrderQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/10 21:03
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "order/shopOrder" )
public class ShopOrderController{


    private Log logger = Log.getInstance (ShopOrderController.class);


    @Autowired
    private ShopMemberOrderService shopMemberOrderService;


    /**
     * 下单
     *
     * @param operType   1购买店中店会员,2购买APP会员,10购买APP商品
     * @param memberType 会员类型
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/11 18:23
     */
    @RequestMapping( "createOrder" )
    public Response createOrder(
            @RequestParam( "operType" ) Integer operType,
            @RequestParam( value = "memberType", required = false ) Integer memberType
    ) {
        StoreBizOrderQuery query = new StoreBizOrderQuery ();
        query.setOperType (operType);
        query.setMemberType (memberType);
        return ResponseHelper.shouldLogin ()
                .invokeHasReturnVal (userId -> {
                    query.setInShopMemberId (userId);
                    return shopMemberOrderService.createOrder (query);
                })
                .returnResponse ();
    }


    /**
     * 预支付
     *
     * @param operType 操作类型1购买店中店会员
     * @param orderNo  订单号
     * @param payWay   支付方式1微信支付
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/12 15:08
     */
    @RequestMapping( "prePayOrder" )
    public Response prePayOrder(
            @RequestParam( "operType" ) Integer operType,
            @RequestParam( "orderNo" ) String orderNo,
            @RequestParam( value = "payWay", defaultValue = "1" ) Integer payWay
    ) {
        StoreBizOrderQuery query = new StoreBizOrderQuery ();
        query.setOrderNo (orderNo);
        query.setOperType (operType);
        query.setPayWay (payWay);
        return ResponseHelper.shouldLogin ()
                .invokeHasReturnVal (userId -> {
                    query.setInShopMemberId (userId);
                    return shopMemberOrderService.prePayOrder (query);
                })
                .returnResponse ();
    }

    /**
     * 添加小程序默认地址
     */
    @RequestMapping("/defaultAddress")
    public Response chooseAddress(
                                  @RequestParam("userId")Long memberId,
                                  @RequestParam("deliveryAddressId")Long id){
        if (memberId==null){
            logger.info("账户为空,请重新尝试");
        }
        return shopMemberOrderService.defaultAddress(memberId,id);
    }

    /**
     * 选择收货地址
     */
    @RequestMapping("/chooseDeliveryType")
    public Response chooseDeliveryType(@RequestParam(value = "orderType", required = true) int orderType, Long storeId, Long userId){
        if (storeId==null||userId==null){
            logger.info("账户为空,请重新尝试");
        }
        logger.info("小程序选择收货方式WxaMemberOrderController:" + "storeId-" + storeId + ";memberId-" + userId);
        return shopMemberOrderService.getAddress(orderType,storeId,userId);
    }


    /**
     * 添加收货地址
     * @param memberId
     * @param linkmanName
     * @param phoneNumber
     * @param location
     * @param address
     * @return
     */
    @Consume(RequestAddress.class)
    @RequestMapping("/addDeliveryAddress")
    public Response addDeliveryAddress(
            @RequestParam("userId") Long memberId,
            @RequestParam(value = "linkmanName", required = true) String linkmanName,
            @RequestParam(value = "phoneNumber", required = true) String phoneNumber,
            @RequestParam(value = "location", required = true) String location,
            @RequestParam(value = "address", required = true) String address,
            @RequestParam(value = "defaultStatus",required=false)Integer defaultStatus) {
        logger.info("小程序会员添加收货地址ShopMemberDeliveryAddressController:" + "memberId-" + memberId);
        return shopMemberOrderService.addAddress((RequestAddress) ConsumeHelper.getObj());
    }

    /**
     * 修改收货地址
     * @param memberId
     * @param deliveryAddressId
     * @return
     */
    @RequestMapping("/updateDeliveryAddress")
    public Response updateDeliveryAddress(
            @RequestParam("userId") Long memberId,
            @RequestParam(value = "deliveryAddressId", required = true) long deliveryAddressId) {
        logger.info("小程序会员修改收货地址ShopMemberDeliveryAddressController:" + "memberId-" + memberId);

        return shopMemberOrderService.selectAddress(memberId,deliveryAddressId);
    }

    /**
     * 保存修改收货地址
     * @param memberId
     * @param deliveryAddressId
     * @param linkmanName
     * @param phoneNumber
     * @param location
     * @param address
     * @return
     */
    @Consume(RequestAddress.class)
    @RequestMapping("/updateDeliveryAddressSave")
    public Response updateDeliveryAddressSave(
            @RequestParam("userId") Long memberId,
            @RequestParam(value = "deliveryAddressId", required = true) long deliveryAddressId,
            @RequestParam(value = "linkmanName", required = false, defaultValue = "") String linkmanName,
            @RequestParam(value = "phoneNumber", required = false, defaultValue = "") String phoneNumber,
            @RequestParam(value = "location", required = false, defaultValue = "") String location,
            @RequestParam(value = "address", required = false, defaultValue = "") String address,
            @RequestParam(value = "defaultStatus", required = false,defaultValue = "1") Integer defaultStatus) {
        logger.info("小程序会员修改收货地址ShopMemberDeliveryAddressController:" + "memberId-" + memberId);

        return shopMemberOrderService.updateAddress((RequestAddress) ConsumeHelper.getObj());
        }
}
