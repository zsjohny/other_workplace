package com.e_commerce.miscroservice.supplier.dao;

import com.e_commerce.miscroservice.commons.entity.application.order.StoreBusiness;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.supplier.entity.request.RefundGoodsMoneyRequest;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/12/4 10:15
 * @Copyright 玖远网络
 */
public interface RefundOrderDao {
    /**
     * 售后处理 退货退款
     *
     * @param obj
     * @return
     */
    void refundGoodsMoney(RefundGoodsMoneyRequest obj);


    /**
     * 确认收货
     * @param id
     * @param code
     * @return
     */
    Response confirmTackGoods(Long id, Integer code);

    /**
     * 根据售后订单查询 用户
     * @param id
     */
    StoreBusiness findRefundUser(Long id);
}
