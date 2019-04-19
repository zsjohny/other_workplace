package com.e_commerce.miscroservice.store.dao;


import com.e_commerce.miscroservice.store.entity.vo.StoreRefundOrderActionLog;
import com.e_commerce.miscroservice.store.entity.emuns.RefundOrderActionLogEnum;

import java.util.List;

/**
 * 售后订单
 */
public interface StoreRefundOrderActionLogDao {

    void addActionLog(RefundOrderActionLogEnum a, Long id);

    /**
     * 查询售后日志
     * @param refundOrderId
     * @return
     */
    List<StoreRefundOrderActionLog> getRefundOrderActionLogList(Long refundOrderId);
}
