package com.e_commerce.miscroservice.store.dao;


import com.e_commerce.miscroservice.store.entity.response.RefundOrderListResponse;
import com.e_commerce.miscroservice.store.entity.vo.RefundOrderNewL;
import com.e_commerce.miscroservice.store.entity.vo.StoreRefundOrder;
import com.github.pagehelper.PageInfo;

/**
 * 售后订单
 */
public interface StoreRefundOrderDao {
    /**
     * 根据用户id 查询列表
     *  @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    PageInfo<RefundOrderListResponse> findRefundOrderListByUserId(Integer pageNum, Integer pageSize, Long userId);

    StoreRefundOrder getRefundOrderByOrderNoUnderWayOrSuccess(Long orderNo);

    int insertOneRefundOrder(StoreRefundOrder refundOrder);

    /**
     * 查询订单详情
     * @param refundOrderId
     * @return
     */
    StoreRefundOrder findRefundOrderById(Long refundOrderId);
}
