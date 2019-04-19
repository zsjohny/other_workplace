package com.e_commerce.miscroservice.order.service.yjj;

import com.e_commerce.miscroservice.commons.entity.order.YjjOrder;

/**
 * Create by hyf on 2018/11/13
 */
public interface YjjOrderService {
    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    YjjOrder findYjjOrderByOrderNo(String orderNo);
}
