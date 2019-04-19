package com.e_commerce.miscroservice.supplier.service;

import com.e_commerce.miscroservice.commons.entity.application.order.RefundOrderRequest;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.supplier.entity.request.RefundGoodsMoneyRequest;

import java.util.Map;

public interface RefundOrderService {
    /**
     * 查询所有售后工单
     */
    Response findAllRefundOrder(RefundOrderRequest orderRequest);

    /**
     * 查询售后订单详情
     */
    Map<String,Object> getRefundOrderInfo(long refundOrderId);

    /**
     * 校验金额
     */
    Integer equalsMoney(Long orderNo,Long skuId,Double money);

    /**
     * 售后处理 退货退款
     * @param obj
     * @return
     */
    Response refundGoodsMoney(RefundGoodsMoneyRequest obj);

    /**
     * 售后处理 退款
     * @param obj
     * @return
     */
    Response refundMoney(RefundGoodsMoneyRequest obj);

    /**
     * 确认收货
     * @param id
     * @return
     */
    Response confirmTackGoods(Long id);
}
