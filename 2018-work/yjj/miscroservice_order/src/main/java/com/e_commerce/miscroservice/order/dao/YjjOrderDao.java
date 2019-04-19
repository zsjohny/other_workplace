package com.e_commerce.miscroservice.order.dao;

import com.e_commerce.miscroservice.commons.entity.order.YjjOrder;

/**
 * Create by hyf on 2018/11/13
 */
public interface YjjOrderDao {
    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    YjjOrder findYjjOrderByOrderNo(String orderNo);
}
