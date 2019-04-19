package com.e_commerce.miscroservice.store.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.store.entity.vo.RefundRquest;
import com.e_commerce.miscroservice.store.entity.vo.ShopMemberOrderRequest;
import com.e_commerce.miscroservice.store.entity.vo.ShopOrderWXRequest;
import com.e_commerce.miscroservice.store.service.ShopOrderWXService;
import com.e_commerce.miscroservice.store.service.WXOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/store/order")
public class ShopOrderWXController {


    @Autowired
    private ShopOrderWXService shopOrderWXService;
    /**
     * 获取我的销售订单
     * orderStatus  0:待付款、1:待提货、4:订单完成、3:订单关闭、5:待发货;6:已发货
     */
    @Consume(ShopMemberOrderRequest.class)
    @RequestMapping("/myOrderList")
    public Response myOrderList(@RequestParam(value = "orderStatus",required = false)Integer orderStatus,//订单状态
                                @RequestParam("sotreId")Long shopId,//商家id,
                                @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "pageNumber",defaultValue = "1") Integer pageNumber
    ){
        Response response = shopOrderWXService.getRefundOrderList((ShopMemberOrderRequest) ConsumeHelper.getObj());
        return response;
    }

    /**
     * 获取我的售后订单列表
     */
    @Consume(RefundRquest.class)
    @RequestMapping("/myRefundList")
    public Response myRefundList(@RequestParam(value = "status",required = false)Integer status,//售后订单状态  退款状态：0 默认 退款中，1 退货成功 2 退款失败',
                                 @RequestParam("storeId")Long storeId,//商家id
                                 @RequestParam(value = "type",required = false)Integer type,//'退款类型：0 默认 退款，1 退货退款',
                                 @RequestParam(value = "orderId",required = false)Long orderId,//订单号
                                 Integer pageSize,
                                 Integer pageNumber
    ){
        Long timeMillis = System.currentTimeMillis();
        String string=timeMillis.toString();
        return  shopOrderWXService.selectMyRefundList((RefundRquest) ConsumeHelper.getObj());
    }

    /**
     * 我的售后订单详情
     */
    @RequestMapping("/myRefundDetail")
    public Response myRefundDetail(@RequestParam("afterSaleId")Long afterSaleId,//售后单号
                                   @RequestParam("storeId")Long storeId//商家id
    ){

        return  shopOrderWXService.selectOrderDetail(afterSaleId,storeId);

    }

    /**
     * 处理我的售后
     */

    @RequestMapping("/dealRefundOrder")
    public Response dealRefundOrder(@RequestParam(value = "status",required = false)Integer status,//处理状态  1同意  2 拒绝
                                    @RequestParam(value = "type",required = false)Integer type,//退款类型  0 退款    1退货退款
                                    @RequestParam("storeId")Long storeId,//商家id
                                    @RequestParam("refundOrderNo")Long refundOrderNo//售后编号
    ){
        return shopOrderWXService.dealOrder(type,status,storeId,refundOrderNo);
    }


    /**
     * 处理情况
     */
    @Consume(ShopOrderWXRequest.class)
    @RequestMapping("/store/deal")
    public Response storeDeal(@RequestParam(value = "type",required = false)Integer type,//退款类型  0 退款    1退货退款
                               @RequestParam(value = "status")Integer status,//处理状态  1退款  2 拒绝  3同意
                               @RequestParam("storeId")Long storeId,//商家id
                               @RequestParam("refundOrderNo")Long refundOrderNo,//售后编号
                               @RequestParam("msg")String msg,//处理理由
                               @RequestParam(value = "realMoney",required = false)Double realMoney//商家填写退款金额
                             ){

        return shopOrderWXService.dealOrderResult((ShopOrderWXRequest) ConsumeHelper.getObj());
    }


    @RequestMapping("/test")
    public Response test(){
        return shopOrderWXService.test();
    }
    /**
     * 删除订单
     */
    @RequestMapping("/deleteMyOrder")
    public Response deleteMyorder(@RequestParam("refundOrderNo")Long refundOrderNo,
                                   @RequestParam("storeId")Long storeId){

        return shopOrderWXService.deleteMyOrder(refundOrderNo,storeId);

    }

    /**
     * 确认收货
     */
    @RequestMapping("/refuodOrder")
    public Response refuodOrder(@RequestParam("refundOrderNo")Long refundOrderNo,
                                      @RequestParam("storeId")Long storeId){
        return shopOrderWXService.refoundOrder(refundOrderNo,storeId);
    }

}
