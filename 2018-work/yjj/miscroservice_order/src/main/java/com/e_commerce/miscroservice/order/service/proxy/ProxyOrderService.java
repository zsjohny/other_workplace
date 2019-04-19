package com.e_commerce.miscroservice.order.service.proxy;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrder;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrderQuery;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReward;
import com.e_commerce.miscroservice.commons.entity.proxy.PayResult;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.service.BaseService;
import com.e_commerce.miscroservice.order.vo.ProxyRefereeVo;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 描述 代理订单查询
 * @date 2018/9/25 11:23
 * @return
 */
public interface ProxyOrderService extends BaseService<ProxyOrder> {

    /**
     * 描述 获取订单支付后的订单详情
     * @param orderId 订单号
     * @author hyq
     * @date 2018/9/25 17:29
     * @return com.e_commerce.miscroservice.commons.utils.Response
     */
    List<ProxyOrder> getOrderByOrderId(String orderId);

    /**
     * 描述 根据订单号获取订单
     * @param userId
     * @param pageNum
     * @param pageSize
     * @author hyq
     * @date 2018/10/8 15:23
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrderQuery>
     */
    List<ProxyOrderQuery> getOrderByUserId(long userId,int pageNum,int pageSize);

    /**
     * 描述 根据用户类型获取订单列表
     * @param userId
     * @param type 1 客户 2 代理  3自己
     * @param pageNum
     * @param pageSize
     * @author hyq
     * @date 2018/10/8 15:26
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrderQuery>
     */
    List<ProxyOrderQuery> getOrderByUserIdAndType(long userId, int type, int pageNum, int pageSize);

    /**
     * 描述 获取返利的订单详情
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReward>
     * @author hyq
     * @date 2018/10/8 16:59
     */
    List<ProxyReward> getRewardOrderList(long userId, String startTime , String endTime, int pageNum, int pageSize,List<Integer> isGrants);

    List<ProxyOrder> getOrderList(ProxyOrderQuery query);

    /**
     * 描述 获取是否是收益的订单
     *
     * @param isProfit
     * @param pageNum
     * @param pageSize
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrder>
     * @author hyq
     * @date 2018/10/8 16:54
     */
    PageInfo<ProxyOrder> getProfitOrderList(int isProfit, int pageNum, int pageSize);

    /**
     * 描述 获取推荐关系
     * @param userId
     * @author hyq
     * @date 2018/10/11 11:59
     * @return com.e_commerce.miscroservice.order.vo.ProxyRefereeVo
     */
    ProxyRefereeVo getRefereeUser(long userId);

    /**
     * 描述 获取虚假推荐关系
     * @param userId
     * @author hyq
     * @date 2018/10/11 11:59
     * @return com.e_commerce.miscroservice.order.vo.ProxyRefereeVo
     */
    ProxyRefereeVo getAllRefereeUser(long userId);

    /**
     * 描述 获取代理名字
     * @param userId
     * @author hyq
     * @date 2018/10/11 12:00
     * @return java.lang.String
     */
    String getRefereeCustomerName(long userId);

    /**
     * 描述 获取公众号名字
     * @param userId
     * @author hyq
     * @date 2018/10/11 12:00
     * @return java.lang.String
     */
    String getRefereePublicName(long userId);

    /**
     * 描述 汇总收益金额
     * @param userId
     * @param isGrants
     * @author hyq
     * @date 2018/10/8 19:11
     * @return java.math.BigDecimal
     */
    BigDecimal getCollectReward(long userId,List<Integer> isGrants);

    /**
     * 描述 获取当日汇总收益
     * @param userId
     * @param isGrants
     * @author hyq
     * @date 2018/10/11 12:01
     * @return java.math.BigDecimal
     */
    public BigDecimal getTodayCollectReward(long userId, List<Integer> isGrants);


    /**
     * 支付回调后的订单操作
     *
     * @param result 支付后的回调结果
     * @return void
     * @author Charlie
     * @date 2018/10/26 14:15
     */
    void doUpdOrderAfterPay(String result);
}
