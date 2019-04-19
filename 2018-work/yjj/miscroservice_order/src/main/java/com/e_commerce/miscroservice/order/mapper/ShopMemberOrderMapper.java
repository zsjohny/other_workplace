package com.e_commerce.miscroservice.order.mapper;

import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.TeamOrder;
import com.e_commerce.miscroservice.commons.entity.order.CountTeamOrderMoneyCoinVo;
import com.e_commerce.miscroservice.commons.entity.order.OrderAccountDetailsResponse;
import com.e_commerce.miscroservice.commons.entity.order.OrderItemSkuVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/11 17:38
 * @Copyright 玖远网络
 */
@Mapper
public interface ShopMemberOrderMapper{


    ShopMemberOrder selectOne(ShopMemberOrder query);

    int updateByPrimaryKeySelective(ShopMemberOrder shopMemberOrder);

    /**
     * 当月 粉丝 团队 自购总金额
     * @param userId
     * @param type
     * @return
     */
    Double storeTheMoneyTotalMoney(@Param("userId") Long userId, @Param("type") Integer type);
    /**
     * 分销商当月总购金额
     * @param userId
     * @param type
     * @return
     */
    Double findCountMoneyShopMemberOrderByUser(@Param("userId") Long userId, @Param("type") Integer type);

    /**
     * 查找 订单列表
     * @param userId
     * @return
     */
    List<ShopMemberOrder> findOrderList(@Param("userId") Long userId);

    /**
     * 查找团队订单列表
     * @param userId
     * @param orderNo
     * @return
     */
    List<TeamOrder> findTeamOrderList(@Param("userId") Long userId, @Param("orderNo") String orderNo);

    /**
     * 今日团队订单新增
     * @param userId
     * @return
     */
    Integer findTodayTeamOrderSize(@Param("userId") Long userId);

    /**
     * 查找 团队订单信息
     * @param userId
     * @param orderNo
     * @return
     */
    OrderAccountDetailsResponse findTeamOrder(@Param("userId") Long userId, @Param("orderNo") String orderNo);

    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    ShopMemberOrder findOrderByOrderNo(@Param("orderNo") String orderNo);
    /**
     * 团队总数
     * @param userId
     * @return
     */
    Integer findCountOrderSize(@Param("userId") Long userId);
    /**
     * 团队总金币，现金
     * @param userId
     * @return
     */
    CountTeamOrderMoneyCoinVo findCountTeamMoneyAndCoin(@Param("userId") Long userId);

    /**
     * 团队订单 商品信息
     * @param userId
     * @param orderNo
     * @return
     */
    List<OrderItemSkuVo> findTeamOrderItemSku(@Param("userId") Long userId, @Param("orderNo") String orderNo);
}
