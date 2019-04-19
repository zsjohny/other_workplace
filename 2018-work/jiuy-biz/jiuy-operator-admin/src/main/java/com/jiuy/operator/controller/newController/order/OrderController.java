package com.jiuy.operator.controller.newController.order;


import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.model.order.*;
import com.jiuy.rb.enums.MemberOrderStatusEnum;
import com.jiuy.rb.service.order.IExpressService;
import com.jiuy.rb.service.order.IOrderService;;
import com.jiuy.rb.service.order.IMemberOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

;

/**
 * 订单controller
 * @author Aison
 * @version V1.0
 * @date 2018/6/8 14:13
 * @Copyright 玖远网络
 */
@Controller
@RequestMapping("/admin")
public class OrderController {

    @Resource(name = "orderServiceRb")
    private IOrderService orderService;

    @Resource(name = "expressServiceRb")
    private IExpressService expressService;

    @Resource(name = "memberOrderService")
    private IMemberOrderService memberOrderService;

    /**
     * 查询C端消费订单管理
     * @param query
     * @return
     */
    @RequestMapping("/memberOrderList")
    @ResponseBody
    public ResponseResult memberOrderList(ShopMemberOrderRbQuery query) {
        return ResponseResult.instance().success(memberOrderService.orderList(query));
    }

    /**
     * 查询C端售后订单管理
     * @param query
     * @return
     */
    @RequestMapping("/refundMemberOrderList")
    @ResponseBody
    public ResponseResult refundMemberOrderList(ShopMemberOrderRbQuery query) {
        return ResponseResult.instance().success(memberOrderService.refundOrderList(query));
    }

    /**
     *
     * 描述:  获取C端订单详情 传id
     *
     * @param [orderId]
     * @return com.jiuy.base.util.ResponseResult
     * @auther hyq
     * @date 2018/7/29 19:34
     */
    @RequestMapping("/memberOrderInfo")
    @ResponseBody
    public ResponseResult memberOrderInfo(Long orderId) {

        return ResponseResult.instance().success(memberOrderService.orderInfo(orderId));
    }

    /**
     *
     * 描述: 更新C端订单信息
     *
     * @param [query]
     * @return com.jiuy.base.util.ResponseResult
     * @auther hyq
     * @date 2018/7/29 19:48
     */
    @ResponseBody
    @RequestMapping("/updateMemberOrderInfo")
    public ResponseResult updateMemberOrderInfo(ShopMemberOrderRb query) {

        //这样会更新全部的信息。 可以把更新的信息单独拿出来

        ShopMemberOrderRb newOrder = new ShopMemberOrderRb();

        newOrder.setId(query.getId());
        newOrder.setOrderNumber(query.getOrderNumber());
        newOrder.setOrderStatus(query.getOrderStatus());
        newOrder.setExpreeSupplierCnname(query.getExpreeSupplierCnname());
        newOrder.setExpressSupplier(query.getExpressSupplier());
        newOrder.setExpressNo(query.getExpressNo());
        newOrder.setReceiverName(query.getReceiverName());
        newOrder.setReceiverPhone(query.getReceiverPhone());
        newOrder.setReceiverAddress(query.getReceiverAddress());
        newOrder.setRemark(query.getRemark());
        newOrder.setExpressInfo(query.getExpressInfo());

        memberOrderService.updateOrder(newOrder,UserSession.getUserSession());
        return ResponseResult.SUCCESS;

    }

    /**
     *描述  C端订单发货
     * @param query
     * @author hyq
     * @date 2018/7/30 11:24
     * @return com.jiuy.base.util.ResponseResult
     */
    @ResponseBody
    @RequestMapping("/deliverMemberOrder")
    public ResponseResult submitDelivery(ShopMemberOrderRbQuery query) {

        memberOrderService.deliverOrder(query,UserSession.getUserSession());
        return ResponseResult.SUCCESS;
    }

    /**
     *描述 订单的退款操作
     ** @param query
     * @param userSession
     * @author hyq
     * @date 2018/7/30 16:23
     * @return int
     */
    @ResponseBody
    @RequestMapping("/refundMoneyMemberOrder")
    public ResponseResult refundMoneyMemberOrder(ShopMemberOrderRb query) {

        //这样会更新全部的信息。 可以把更新的信息单独拿出来

        ShopMemberOrderRb newOrder = new ShopMemberOrderRb();

        if(!(MemberOrderStatusEnum.REFUNDING.isThis(query.getOrderStatus()) || MemberOrderStatusEnum.CLOSED.isThis(query.getOrderStatus()))){
            return ResponseResult.FAILED.setMsg("状态传递有误！");
        }

        newOrder.setId(query.getId());
        //newOrder.setOrderNumber(query.getOrderNumber());
        newOrder.setOrderStatus(query.getOrderStatus());
        newOrder.setUpdateTime(System.currentTimeMillis());

        int i = memberOrderService.refundMoneyOrder(newOrder, UserSession.getUserSession());
        return ResponseResult.SUCCESS;

    }

    /**
     *描述  C端订单取消
     * @param query
     * @author hyq
     * @date 2018/7/30 11:24
     * @return com.jiuy.base.util.ResponseResult
     */
    @ResponseBody
    @RequestMapping("/stopMemberOrder")
    public ResponseResult stopMemberOrder(ShopMemberOrderRbQuery query) {

        //int i = memberOrderService.stopOrder(query, UserSession.getUserSession());
        memberOrderService.cancelOrder(UserSession.getUserSession(),query.getId(),5,"平台客服关闭订单");

        return ResponseResult.SUCCESS;
    }

    /**
     * 查询订单信息
     * @param query request对象
     * @author Aison
     * @date 2018/6/8 14:16
     */
    @RequestMapping("/orderList")
    @ResponseBody
    public ResponseResult orderList(StoreOrderRbQuery query) {

        return ResponseResult.instance().success(orderService.orderList(query));
    }

    /**
     * 订单详情
     * @param orderId orderId
     * @author Aison
     * @date 2018/6/28 18:04
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("/orderInfo")
    @ResponseBody
    public ResponseResult orderInfo(Long orderId) {

        return ResponseResult.instance().success(orderService.orderInfo(orderId));

    }

    /**
     * 获取物流公司列表
     * @param query query
     * @author Aison
     * @date 2018/6/28 18:02
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("/expressList")
    @ResponseBody
    public ResponseResult expressList(ExpressSupplierRbQuery query) {

        return ResponseResult.instance().success(expressService.expressSupplierRbList(query));
    }


    /**
     * 修改订单信息
     *
     * @param storeOrderRb storeOrderRb
     * @author Aison
     * @date 2018/6/29 10:23
     * @return com.jiuy.base.util.ResponseResult
     */
    @ResponseBody
    @RequestMapping("/updateOrderSupplierRemark")
    public ResponseResult updateOrderSupplierRemark(StoreOrderRb storeOrderRb) {

        StoreOrderRb storeOrderRbNNew = new StoreOrderRb();
        storeOrderRbNNew.setOrderNo(storeOrderRb.getOrderNo());
        storeOrderRbNNew.setOrderSupplierRemark(storeOrderRb.getOrderSupplierRemark());
        orderService.updateOrder(storeOrderRbNNew,UserSession.getUserSession());
        return ResponseResult.SUCCESS;
    }


    /**
     * 发货
     *
     * @param storeOrderRb storeOrderRb
     * @author Aison
     * @date 2018/6/29 14:11
     * @return com.jiuy.base.util.ResponseResult
     */
    @ResponseBody
    @RequestMapping("/deliverOrder")
    public ResponseResult deliverGoods(StoreOrderRbQuery storeOrderRb) {

        orderService.deliverOrder(storeOrderRb,UserSession.getUserSession());
        return ResponseResult.SUCCESS;
    }


}
