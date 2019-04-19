package com.e_commerce.miscroservice.operate.dao;

import com.e_commerce.miscroservice.commons.entity.application.order.RefundOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.RefundOrderResponce;
import com.e_commerce.miscroservice.operate.entity.request.RefundOrderFindReqeust;

import java.util.List;

/**
 * Create by hyf on 2018/11/30
 */

public interface RefundOrderDao {
    /**
     * 售后订单管理
     * @param obj
     * @return
     */
    List<RefundOrderResponce> findAllRefundOrder(RefundOrderFindReqeust obj);

    /**
     * 根据id查询 售后订单
     * @param id
     * @return
     */
    RefundOrder findRefundOrderById(Long id);

    /**
     * 更新售后订单
     * @param refundOrder
     */
    void updateRefundOrder(Long id, Double money, String msg);
}
