package com.e_commerce.miscroservice.store.service;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.store.entity.vo.ShopOrderRequest;

import java.util.Map;

/**
 * 售后
 * @author hyf
 * @version V1.0
 * @date 2018/9/26 17:27
 * @Copyright 玖远网络
 */
public interface StoreRefundOrderService {
    /**
     *  售后订单列表
     *  @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Response getRefundOrderList(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 申请售后
     * @param orderNo
     * @param storeId
     * @param refundType
     * @param SkuId
     * @return
     */
    Response applyRefund1(Long orderNo, long storeId, Integer refundType, Long SkuId );
    /**
     * 申请退款
     * @param orderNo
     * @param SkuId
     * @return
     */
    Response applyRefund(Long orderNo, Long SkuId );

    /**
     * 查询订单详情
     */
    public Map<String, Object> getOrderInfoMation(Long storeId, Long orderNo);
    /**
     * 提交售后
     * @param refundType
     * @param refundReason
     * @param refundReasonId
     * @param orderNo
     * @param refundCost
     * @param refundRemark
     * @param refundProofImages
     * @param storeId
     * @param version
     * @param orderItemId
     * @return
     */
    Response submitRefundOrder(Integer refundType, String refundReason, Long refundReasonId, Long orderNo, Double refundCost, String refundRemark, String refundProofImages, long storeId, Integer version, Long orderItemId,Long skuId,Integer count);

    /**
     * 售后订单详情
     * @param refundOrderId
     * @return
     */
    Response getRefundOrderDetailed(Long refundOrderId);

    /**
     * 查询订单详情
     */
    Map<String, Object> getOrderInfo(Long storeId, Long orderNo);

    /**
     * 买家撤销售后
     */
    void cancelRefundOrder(Long refundOrderNo);

    /**
     * 所有订单列表
     */
    Response selectOrderList(ShopOrderRequest request);

    /**
     * 获取订单详情
     */
    Response selectDetail(Long refundOrderId);

    /**
     * 立即发货
     */
    Response sendOrder(Long storeId,Long refundOrderNo);
}
