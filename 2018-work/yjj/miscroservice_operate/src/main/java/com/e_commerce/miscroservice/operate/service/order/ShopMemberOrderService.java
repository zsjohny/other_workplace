package com.e_commerce.miscroservice.operate.service.order;

import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;
import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderQuery;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/8 18:54
 * @Copyright 玖远网络
 */
public interface ShopMemberOrderService{


    /**
     * 小程序订单查询
     *
     * @param query query
     * @author Charlie
     * @date 2018/11/8 19:12
     */
    Map<String, Object> listOrder(ShopMemberOrderQuery query);


    /**
     * 订单详情
     *
     * @param orderNo 订单编号
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/11/8 19:50
     */
    ShopMemberOrderQuery orderDetailByOrderNo(String orderNo);



    /**
     * 订单收益明细
     *
     * @param query query
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @author Charlie
     * @date 2018/11/9 15:33
     */
    Map<String, Object> orderEarningDetail(ShopMemAcctCashOutInQuery query);




}
