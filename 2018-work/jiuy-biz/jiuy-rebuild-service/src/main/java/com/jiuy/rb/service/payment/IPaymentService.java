package com.jiuy.rb.service.payment;

import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.entity.newentity.ShopMemberOrder;

/**
 * 支付接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/17 11:21
 * @Copyright 玖远网络
 */
public interface IPaymentService {

    /**
     * 小程序回调
     *
     * @param shopMemberOrder shopMemberOrder
     * @param paymentNo paymentNo
     * @param paymentType paymentType
     * @author Aison
     * @date 2018/7/17 11:29
     * @return int
     */
     int updateOrderAlreadyPayStatus(ShopMemberOrder shopMemberOrder, String paymentNo, PaymentType paymentType);
}
