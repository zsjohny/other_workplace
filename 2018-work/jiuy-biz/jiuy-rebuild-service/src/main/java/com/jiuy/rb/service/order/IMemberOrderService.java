package com.jiuy.rb.service.order;

import com.jiuy.base.annotation.Nullable;
import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.model.UserSession;
import com.jiuy.rb.model.order.ShopMemberOrderItemRbQuery;
import com.jiuy.rb.model.order.ShopMemberOrderRb;
import com.jiuy.rb.model.order.ShopMemberOrderRbQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * TOTO
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/23 9:25
 * @Copyright 玖远网络
 */
public interface IMemberOrderService {

    /**
     *  通过订单号来查询订单
     *
     * @param orderNo orderNo
     * @author Aison
     * @date 2018/7/23 9:26
     * @return com.jiuy.rb.model.order.ShopMemberOrderRb
     */
    ShopMemberOrderRb getOrderByOrderNo(String orderNo);


    /**
     *
     * 功能描述: 订单列表
     *
     * @param: [query]
     * @return: com.jiuy.base.model.MyPage<java.util.Map<java.lang.String,java.lang.Object>>
     * @auther: hyq
     * @date: 2018/7/27 9:58
     */
    MyPage<Map<String,Object>> orderList(ShopMemberOrderRbQuery query);

    /**
     *
     * 功能描述: 售后订单列表
     *
     * @param: [query]
     * @return: com.jiuy.base.model.MyPage<java.util.Map<java.lang.String,java.lang.Object>>
     * @auther: hyq
     * @date: 2018/7/27 9:58
     */
    MyPage<Map<String,Object>> refundOrderList(ShopMemberOrderRbQuery query);

    /**
     *
     * 描述:  获取C端订单详情 传id
     *
     * @param: [orderId]
     * @return: com.jiuy.base.util.ResponseResult
     * @auther: hyq
     * @date: 2018/7/29 19:34
     */
    ShopMemberOrderRbQuery orderInfo(Long orderId);

    /**
     *
     * 描述:  获取C端订单明细
     *
     * @param: [orderNum]
     * @return: java.util.List<com.jiuy.rb.model.order.ShopMemberOrderItemRbQuery>
     * @auther: hyq
     * @date: 2018/7/29 19:51
     */
    List<ShopMemberOrderItemRbQuery> getOrderItem(String orderNo) ;

    /**
     *
     * 描述: 更新C端订单信息
     *
     * @param: [query]
     * @return: com.jiuy.base.util.ResponseResult
     * @auther: hyq
     * @date: 2018/7/29 19:48
     */
    MyLog<Long> updateOrder(ShopMemberOrderRb query, UserSession userSession);

    /**
     *描述 C端订单发货
     * @param query
     * @param userSession
     * @author hyq
     * @date 2018/7/30 11:24
     * @return com.jiuy.base.model.MyLog<java.lang.Long>
     */
    MyLog<Long> deliverOrder(ShopMemberOrderRbQuery query, UserSession userSession);

    /**
     *描述 C端订单取消
     ** @param query
    * @param userSession
     * @author hyq
     * @date 2018/7/30 15:29
     * @return int
     */
    int stopOrder(ShopMemberOrderRbQuery query ,UserSession userSession);

    /**
     *描述 订单的退款操作
     ** @param query
    * @param userSession
     * @author hyq
     * @date 2018/7/30 16:23
     * @return int
     */
    int refundMoneyOrder(ShopMemberOrderRb query ,UserSession userSession);


    /**
     * 用户即将成团的订单
     *
     * @param query query
     *                必填:storeId
     *                必填:memberId
     * @return com.jiuy.base.model.MyPage
     * @author Charlie
     * @date 2018/7/29 22:33
     */
    MyPage<Map<String, Object>> teamBuyActivityUnderwayList(ShopMemberOrderRbQuery query);


    /**
     * 用户已经成团的订单
     * @param query query
     *                必填:storeId
     *                必填:memberId
     * @return com.jiuy.base.model.MyPage
     * @author Charlie
     * @date 2018/7/29 22:33
     */
    MyPage<Map<String, Object>> teamBuyActivityOKList(ShopMemberOrderRbQuery query);



    /**
     * 取消订单
     *
     * @param userSession storeId,memberId
     * @param orderId 订单id
     * @param reasonType 取消类型：0无、1会员取消、2商家取消、3系统自动取消 4商家手动结束活动，关闭订单 5 平台客服关闭订单
     * @param reason 取消原因
     * @author Charlie
     * @date 2018/8/3 15:33
     */
    void cancelOrder(UserSession userSession, Long orderId, Integer reasonType, String reason);

    /**
     * 确认收货
     *
     * @param user user
     * @param shopMemberOrderId 小程序订单id
     * @author Charlie
     * @date 2018/8/5 20:48
     */
    void confirmReceipt(UserSession user, Long shopMemberOrderId);

    /**
     * 获取提示语
     *
     * @param route route
     * @return 提示语
     * @author Charlie
     * @date 2018/8/9 16:27
     */
    String tip(String route);

    /**
     * 即将成团数量
     *
     * @param query query
     * @return java.lang.Integer
     * @author Charlie
     * @date 2018/8/10 8:33
     */
    Integer teamBuyActivityUnderwayCount(ShopMemberOrderRbQuery query);


    /**
     * 确认提货
     *
     * @param storeId storeId
     * @param orderId orderId
     * @return void
     * @author Charlie
     * @date 2018/8/27 23:13
     */
    void confirmDelivery(Long storeId, Long orderId);


    /**
     * 同步订单状态
     * @param shopMemberOrderId
     */
    void syncOrderType(long shopMemberOrderId);
}
