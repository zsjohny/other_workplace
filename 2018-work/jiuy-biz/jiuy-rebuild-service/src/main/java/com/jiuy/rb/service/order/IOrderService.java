package com.jiuy.rb.service.order;

import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.model.UserSession;
import com.jiuy.rb.model.order.*;

import java.util.List;
import java.util.Map;

/**
 * 订单相关的service
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/12 16:58
 * @Copyright 玖远网络
 */
public interface IOrderService {

    /**
     * 获取订单信息
     *
     * @param orderNo orderNo
     * @author Aison
     * @date 2018/6/29 15:16
     * @return com.jiuy.rb.model.order.StoreOrderRb
     */
    StoreOrderRb getByOrderNo(Long orderNo);


    /**
     * 查询订单的列表
     * @param query 查询参数
     * @author Aison
     * @date 2018/6/8 14:23
     * @return MyPage<Map<String,Object>>
     */
    MyPage<Map<String,Object>> orderList(StoreOrderRbQuery query);


    /**
     * 订单详情
     *
     * @param orderId orderId
     * @author Aison
     * @date 2018/6/28 15:37
     * @return StoreOrderRbQuery
     */
    StoreOrderRbQuery orderInfo(Long orderId);

    /**
     * 获取订单详情
     *
     * @param orderNo orderNo
     * @author Aison
     * @date 2018/6/28 16:56
     * @return java.util.List<com.jiuy.rb.model.order.StoreOrderItemRb>
     */
    List<StoreOrderItemRbQuery> getOrderItem(Long orderNo) ;

    /**
     * 修改订单信息
     *
     * @param storeOrderRb storeOrderRb
     * @param userSession userSession
     * @author Aison
     * @date 2018/6/29 10:24
     * @return com.jiuy.base.model.MyLog<java.lang.Long>
     */
    MyLog<Long> updateOrder(StoreOrderRb storeOrderRb,UserSession userSession);

    /**
     * 发货
     *
     * @param query query  查询条件 如果是供应商的则赋值供应商值
     * @param userSession userSession
     * @author Aison
     * @date 2018/6/29 10:46
     * @return com.jiuy.base.model.MyLog<java.lang.Long>
     */
    MyLog<Long> deliverOrder(StoreOrderRbQuery query,UserSession userSession);


    /**
     * 修改订单的优惠券发放状态
     *
     * @param orderNo orderNo
     * @author Aison
     * @date 2018/8/3 18:28
     * @return int
     */
    int updateSendCoupon(String orderNo);
}
