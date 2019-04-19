package com.e_commerce.miscroservice.order.dao;

import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.TeamOrder;
import com.e_commerce.miscroservice.commons.entity.order.CountTeamOrderMoneyCoinVo;
import com.e_commerce.miscroservice.commons.entity.order.OrderAccountDetailsResponse;
import com.e_commerce.miscroservice.commons.entity.order.OrderItemSkuVo;

import java.util.List;

/**
 * Create by hyf on 2018/10/12
 */
public interface ShopMemberOrderDao {
    /**
     * 当月 粉丝 团队 自购总金额
     * @param userId
     * @param type
     * @return
     */
    Double storeTheMoneyTotalMoney(Long userId, Integer type);

    /**
     * 分销商当月总购金额
     * @param userId
     * @param type
     * @return
     */
    Double findCountMoneyShopMemberOrderByUser(Long userId, Integer type);

    /**
     * 查询订单列表
     * @param userId
     * @param page
     * @return
     */
    List<ShopMemberOrder> findOrderList(Long userId, Integer page);

    /**
     * 查找 团队订单列表
     * @param userId
     * @param page
     * @param orderNo
     * @return
     */
    List<TeamOrder> findTeamOrderList(Long userId, Integer page, String orderNo);

    /**
     * 今日团队订单新增
     * @param userId
     * @return
     */
    Integer findTodayTeamOrderSize(Long userId);
    /**
     * 查找 团队订单信息
     * @param userId
     * @param orderNo
     * @return
     */
    OrderAccountDetailsResponse findTeamOrder(Long userId, String orderNo);

    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    ShopMemberOrder findOrderByOrderNo(String orderNo);

    /**
     * 团队总数
     * @param userId
     * @return
     */
    Integer findCountOrderSize(Long userId);
    /**
     * 团队总金币，现金
     * @param userId
     * @return
     */
    CountTeamOrderMoneyCoinVo findCountTeamMoneyAndCoin(Long userId);
    /**
     * 团队订单 商品信息
     * @param userId
     * @param orderNo
     * @return
     */
    List<OrderItemSkuVo> findTeamOrderItemSku(Long userId, String orderNo);
}
