package com.e_commerce.miscroservice.store.controller;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.store.entity.vo.StoreBusiness;
import com.e_commerce.miscroservice.store.entity.vo.UserDetail;
import com.e_commerce.miscroservice.store.service.StoreRefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 门店售后订单
 *
 * @author hyf
 * @version V1.0
 * @date 2018/9/26 17:22
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/shop/refundOrder")
public class ShopRefundOrderController {

    private Log logger = Log.getInstance(ShopRefundOrderController.class);
    @Autowired
    private StoreRefundOrderService shopRefundOrderService;

    /**
     * 获取售后订单列表
     *
     * @return
     */
    @RequestMapping("/order/list")
    public Response getRefundOrderList(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                       @RequestParam(value = "storeId",defaultValue = "7") Long storeId) {
        // TODO: 2018/9/26  用户id 暂时 写死
        return shopRefundOrderService.getRefundOrderList(storeId, pageNum, pageSize);
    }

    /**
     * 获取售后订单详情
     * @param refundOrderId
     * @return
     */
//    @RequestMapping("/order/detailed")
//    public Response getRefundOrderDetailed(@RequestParam("refundOrderId") Long refundOrderId,
//                                           @RequestParam(value = "storeId",defaultValue = "7")Long storeId){
//        return shopRefundOrderService.getRefundOrderDetailed(refundOrderId);
//    }


    /**
     * 查看订单售后详情
     */
    @RequestMapping("/refund/detail")
    public Response refundOrderDetail(@RequestParam("refundOrderId") Long refundOrderId,//售后订单编号不是订单号
                                      @RequestParam(value = "storeId",defaultValue = "7")Long storeId){


        return shopRefundOrderService.selectDetail(refundOrderId);
    }
    /**
     * 申请售后
     */
    @RequestMapping("/applyMoney")
    public Response applyMoney(@RequestParam("orderNo") Long orderNo,//订单orderNo
                                @RequestParam("skuId") Long skuId,//退款商品的skuId
                                HttpServletRequest request){
        return shopRefundOrderService.applyRefund(orderNo,skuId);
    }
    /**
     * 申请售后
     *
     * 门店宝用户的售后申请按指定SKU发起，对于某个SKU申请售后的条件必须满足：
     * 1、该SKU所属订单无任何处理中的售后申请，
     * 2、该SKU所属订单无任何已成功的售后退款记录，
     * 3、该SKU所属订单状态已付款且尚未交易完成时
     * @return
     */
    @RequestMapping("/applyRefund")
    public Response applyRefund(@RequestParam("orderNo") Long orderNo,//订单orderNo
                                 @RequestParam("refundType") Integer refundType,//退款类型 1:仅退款 2:退货退款
                                 @RequestParam("skuId") Long skuId,//退款商品的skuId
                                 @RequestParam(value = "storeId",defaultValue = "2") Long storeId
    ) {

        return shopRefundOrderService.applyRefund1(orderNo, storeId, refundType,skuId);

    }

    /**
     * 提交售后订单
     *
     * @return
     */
    @RequestMapping("/submitRefundOrder")
    public Response submitRefundOrder(@RequestParam(value = "refundType",required = false) Integer refundType,//售后类型 1：仅退款 2：退货退款
                                      @RequestParam(value = "refundReason",required = false) String refundReason,//退款原因
                                      @RequestParam(value = "refundReasonId",required = false) Long refundReasonId,//退款原因ID
                                      @RequestParam(value = "orderNo",required = false) Long orderNo,//订单ID
                                      @RequestParam(value = "orderItemId",required = false) Long orderItemId,//订单详情ID(不需要)
                                      @RequestParam(value = "refundCost",required = false) Double refundCost,//退款申请金额
                                      @RequestParam(value = "refundRemark", required = false) String refundRemark,//退款说明
                                      @RequestParam(value = "skuId",required = false) Long skuId,//商品skuid
                                      @RequestParam(value = "refundProofImages", required = false,defaultValue = "") String refundProofImages,//退款证明图片,
                                      @RequestParam(value = "count",required = false)Integer count,//退款数量
                                      @RequestParam(value = "storeId",required = false)Long storeId,//用户id
                                      HttpServletRequest request
    ) {
        Integer version = 0;
        return shopRefundOrderService.submitRefundOrder(refundType, refundReason, refundReasonId, orderNo, refundCost, refundRemark, refundProofImages, storeId,
                version,orderItemId,skuId,count);


    }

    /**卖家同意退款 买家发货
     * 立即发货
     */

    @RequestMapping("/rightSend")
    public  Response sendOrder(@RequestParam("storeId")Long storeId,
                               @RequestParam("refundOrderNo")Long refundOrderNo){
        Response response = shopRefundOrderService.sendOrder(storeId, refundOrderNo);
        return response;
    }
    /**
     *撤销售后
     *
     */

    @RequestMapping("/cancelRefundOrder")
    public Response cancelRefundOrder(@RequestParam("refundOrderId") Long refundOrderId,
                                      HttpServletRequest request,
                                      @RequestParam(value = "storeId",defaultValue = "7")Long storeId){
        try {
           shopRefundOrderService.cancelRefundOrder(refundOrderId);
            return Response.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.errorMsg(e.getMessage());
        }

    }
}
