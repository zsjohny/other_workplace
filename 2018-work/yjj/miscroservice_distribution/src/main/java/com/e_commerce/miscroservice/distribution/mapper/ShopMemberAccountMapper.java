package com.e_commerce.miscroservice.distribution.mapper;

import com.e_commerce.miscroservice.commons.entity.order.OrderAccountDetailsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/10 18:50
 * @Copyright 玖远网络
 */
@Mapper
public interface ShopMemberAccountMapper{



    /**
     * 待结算分佣入账
     *
     * @param id           id
     * @param operCash     待结算金额
     * @param operGoldCoin 待结算金币
     * @return int
     * @author Charlie
     * @date 2018/10/10 18:47
     */
    int updateCommissionWaitCashAndGoldCoin(@Param( "id" ) Long id, @Param( "operCash" ) BigDecimal operCash, @Param( "operGoldCoin" ) BigDecimal operGoldCoin);


    /**
     * 待结算管理金入账
     *
     * @param id           id
     * @param operCash     待结算金额
     * @param operGoldCoin 待结算金币
     * @return int
     * @author Charlie
     * @date 2018/10/10 18:49
     */
    int updateManagerWaitCashAndGoldCoin(@Param( "id" ) Long id, @Param( "operCash" ) BigDecimal operCash, @Param( "operGoldCoin" ) BigDecimal operGoldCoin);


    /**
     * 分销返现待结算入账
     *
     * @param id           id
     * @param operCash     operCash
     * @param operGoldCoin operGoldCoin
     * @return void
     * @author Charlie
     * @date 2018/10/10 21:18
     */
    int updateWaitCommission2Alive(@Param( "id" ) Long id, @Param( "operCash" ) BigDecimal operCash, @Param( "operGoldCoin" ) BigDecimal operGoldCoin);



    /**
     * 分销管理金待结算入账
     *
     * @param id 账户id
     * @param operCash operCash
     * @param operGoldCoin operGoldCoin
     * @return int
     * @author Charlie
     * @date 2018/10/10 21:53
     */
    int updateWaitTeamIn2Alive(@Param( "id" ) Long id, @Param( "operCash" ) BigDecimal operCash, @Param( "operGoldCoin" ) BigDecimal operGoldCoin);




    /**
     * 获取锁
     *
     * @param id 主键
     * @author Charlie
     * @date 2018/10/12 17:57
     */
    int lock(@Param( "id" ) Long id);


    /**
     * 账户预提现
     *
     * @param id 账户id
     * @param operCash 提现金额
     * @param fromCommission 从佣金提现
     * @param fromTeamIn 用管理金提现
     * @return int
     * @author Charlie
     * @date 2018/10/13 22:56
     */
    int accountPreCashOut(
            @Param( "id" ) Long id,
            @Param( "operCash" ) BigDecimal operCash,
            @Param ("fromCommission") BigDecimal fromCommission,
            @Param ("fromTeamIn") BigDecimal fromTeamIn
    );

    int cashOutSuccess(@Param ("id") Long id, @Param ("operCash") BigDecimal operCash);

    /**
     * 账户金额-详情-收支详情
     *
     * @param id
     * @return
     */
    OrderAccountDetailsResponse findOrderAccountDetails(@Param("id") Long id);

    /**
     * 账户增加可用的金币或现金
     *
     * @param id 账号id
     * @param operCash 操作金额
     * @param operGoldCoin 操作现金
     * @author Charlie
     * @date 2018/11/22 16:52
     */
    int addAliveGoldCashAndCommissionCashGold(@Param ("id") Long id, @Param ("operCash") BigDecimal operCash, @Param ("operGoldCoin") BigDecimal operGoldCoin);
}
