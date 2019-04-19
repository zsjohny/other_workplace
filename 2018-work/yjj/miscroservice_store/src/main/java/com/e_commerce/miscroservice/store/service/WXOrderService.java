package com.e_commerce.miscroservice.store.service;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;

import com.e_commerce.miscroservice.store.entity.vo.RefundRquest;
import com.e_commerce.miscroservice.store.entity.vo.ShopMemberOrderRequest;
import com.e_commerce.miscroservice.store.entity.vo.SubmitRequest;

public interface WXOrderService {
    /**
     * 查询订单列表
     * @param request
     * @return
     */
    Response getRefundOrderList(ShopMemberOrderRequest request);

    /**
     * 查询订单详情
     */
    Response selectOrderItem1(Long orderNo,Long userId);

    /**
     * 根据订单号和sku查询订单详情
     */
    Response selectItem(Long orderId,Long skuId);
    /**
     * 提交售后申请
     */
    Response  submit(SubmitRequest request);
    /**
     * 查询所有的售后列表
     */
    Response selectRefundList(RefundRquest request);

    /**
     * 根据售后订单编号查询售后订单详情
     */
    Response selectRefundItem(Long afterSaleId,Long userId);

    /**
     * 查询订单详情
     */
    Response selectOrderItem(Long id,Long memberId,Long storeId);

    /**
     * 商家查询订单列表
     */
    Response deleteOrder(Long afterSaleId,Long userId );
    /**
     * 查询订单
     */
    Response selecrTime(Long afterSaleId,Long userId);

    /**
     * 更改订单状态
     */
    Response orderDelete(Long id,Long userId);
}
