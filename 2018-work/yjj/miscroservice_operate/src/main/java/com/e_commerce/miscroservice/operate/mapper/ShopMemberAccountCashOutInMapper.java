package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.distribution.OperUserDstbRequest;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/10 18:50
 * @Copyright 玖远网络
 */
@Mapper
public interface ShopMemberAccountCashOutInMapper{


    /**
     * 查询收支列表
     *
     * @param query query
     * @return java.util.List<java.util.Map       <       java.lang.String       ,       java.lang.Object>>
     * @author Charlie
     * @date 2018/11/9 16:56
     */
    List<Map<String, Object>> listDetail(OperUserDstbRequest query);

    /**
     * 一个订单的收支列表
     *
     * @param query query
     * @return java.util.List<java.util.Map
     * @author Charlie
     * @date 2018/11/9 16:56
     */
    List<Map<String, Object>> listOrderEarnings(ShopMemAcctCashOutInQuery query);


    /**
     * 查询用户
     *
     * @param query query
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/12 17:51
     */
    List<ShopMemAcctCashOutInQuery> findUsersByQuery(ShopMemAcctCashOutInQuery query);


    /**
     * 每个用户的订单成交数,订单成交额
     *
     * @param userIds 用户ids
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/12 17:51
     */
    List<Map<String, Object>> findOrderTotalCountAndMoneyByUserIds(@Param( "userIds" ) List<Long> userIds);


    /**
     * 用户分销总收益 -- 每个用户的 已结算总订单返佣现金,已结算总订单返佣金,已结算总订单管理现金,已结算总订单管理金币
     *
     * @param userIds userIds
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/12 23:20
     */
    List<Map<String, Object>> findDstbTotalEarningsByUserIds(@Param( "userIds" ) List<Long> userIds);


    /**
     * 查询用户ids
     *
     * @param query query
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/12 17:51
     */
    List<Long> findUserIdsByQuery(ShopMemAcctCashOutInQuery query);


    /**
     * 所有订单成交数,订单成交额
     *
     * @param userIds  用户ids
     * @param typeList 订单类型
     * @param justCash 1:只查有现金收益的
     * @param justGoldCoin 1:只查有金币收益的
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/12 17:51
     */
    Map<String, Object> countOrderTotalCountAndMoneyByUserIds(
            @Param( "userIds" ) List<Long> userIds,
            @Param( "typeList" ) List<Integer> typeList,
            @Param( "justCash" ) Integer justCash,
            @Param( "justGoldCoin" ) Integer justGoldCoin
            );


    /**
     * 用户分销总收益汇总 -- 所有用户的汇总 已结算/待结算总订单返佣现金,已结算/待结算总订单返佣金,已结算/待结算总订单管理现金,已结算/待结算总订单管理金币
     *
     * @param userIds    userIds
     * @param statusList 待结算/已结算/待结算+已结算
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/12 23:20
     */
    Map<String, Object> sumDstbTotalEarningsByUserIds(@Param( "userIds" ) List<Long> userIds, @Param( "statusList" ) List<Integer> statusList);


    /**
     * 用户分销业绩订单返佣明细
     *
     * @param query query
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/13 11:21
     */
    List<Map<String, Object>> listOrderCommissionEarnings(ShopMemAcctCashOutInQuery query);


    /**
     * 用户分销业绩订单管理收益明细
     *
     * @param query query
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/13 11:21
     */
    List<Map<String, Object>> listOrderManagerEarnings(ShopMemAcctCashOutInQuery query);


    /**
     * 一个用户的分销收益 的订单数量, 订单成交额
     * <p>
     *     查询参数: 下单时间,待结算/已结算,下单人,订单编号,收益类型list
     * </p>
     *
     * @param query query
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/13 11:21
     */
    Map<String,Object> userOrderTotalCountAndMoney(ShopMemAcctCashOutInQuery query);



    /**
     * 一个用户的分销收益 某种分销现金和金币的收益
     * <p>
     *     查询参数: 下单时间,待结算/已结算,下单人,订单编号,收益类型list
     * </p>
     *
     * @param query query
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/13 11:21
     */
    Map<String,Object> findUserCashAndGoldCoin(ShopMemAcctCashOutInQuery query);



    /**
     * 团队订单返佣明细
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/13 19:21
     */
    List<Map<String,Object>> teamOrderCommission(ShopMemAcctCashOutInQuery query);

    List<Map<String,Object>> listCashOut(ShopMemAcctCashOutInQuery query);
}
