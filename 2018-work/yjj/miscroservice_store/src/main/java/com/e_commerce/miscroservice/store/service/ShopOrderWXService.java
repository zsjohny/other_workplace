package com.e_commerce.miscroservice.store.service;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.store.entity.vo.RefundRquest;
import com.e_commerce.miscroservice.store.entity.vo.ShopMemberOrderRequest;
import com.e_commerce.miscroservice.store.entity.vo.ShopOrderWXRequest;

public interface ShopOrderWXService {
    /**
     * 查询我的订单列表
     * @param request
     * @return
     */
    Response getRefundOrderList(ShopMemberOrderRequest request);

    /**
     * 查询售后订单列表
     */
    Response selectMyRefundList(RefundRquest request);

    /**
     * 查询订单详情
     */
    Response selectOrderDetail(Long afterSaleId,Long storeId);

    /**
     * 处理订单
     */
    Response dealOrder(Integer type,Integer status,Long storeId,Long refundOrderNo);

    /**
     * 提交处理结果
     */
    Response dealOrderResult(ShopOrderWXRequest  request);

    /**
     * 删除我的订单
     */
    Response deleteMyOrder(Long refundOrderNo,Long storeId);

    /**
     * 确认收货
     */

    Response refoundOrder(Long refundOrderNo,Long storeId);

    Response test();
}
