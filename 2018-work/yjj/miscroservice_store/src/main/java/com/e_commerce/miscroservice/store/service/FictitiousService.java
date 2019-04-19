package com.e_commerce.miscroservice.store.service;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;

public interface FictitiousService {
    /**
     * 根据用户storeId查询账户待结算余额
     * @return
     */
     Response selectMoney(Long storeId);
    /**
     * 使用虚拟待结算资金进行付款
     */
     Response selectMoney(Long orderNo,Long storeId,Double paymoney);
    /**
     * 使用已结算资金进行付款
     */
    Response selectRealMoney(Long orderNo,Long storeId);
    /**
     * 选择支付方式
     */
    Response selectPay(Long storeId,Long orderNo,Long blend);
}
