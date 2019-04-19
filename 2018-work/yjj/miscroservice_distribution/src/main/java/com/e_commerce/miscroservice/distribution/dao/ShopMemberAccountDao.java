package com.e_commerce.miscroservice.distribution.dao;


import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccount;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.entity.order.OrderAccountDetailsResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/10 16:34
 * @Copyright 玖远网络
 */
public interface ShopMemberAccountDao{

    /**
     *  根据id查询账户信息
     *  @param userId
     * @return
     */
    ShopMemberAccount findByUserId(Long userId);



    /**
     * 待结算分佣入账
     *
     * @param id id
     * @param operCash 待结算金额
     * @param operGoldCoin 待结算金币
     * @return int
     * @author Charlie
     * @date 2018/10/10 18:47
     */
    int updateCommissionWaitCashAndGoldCoin(Long id, BigDecimal operCash, BigDecimal operGoldCoin);



    /**
     * 待结算管理金入账
     *
     * @param id id
     * @param operCash 待结算金额
     * @param operGoldCoin 待结算金币
     * @return int
     * @author Charlie
     * @date 2018/10/10 18:49
     */
    int updateManagerWaitCashAndGoldCoin(Long id, BigDecimal operCash, BigDecimal operGoldCoin);


    /**
     * 分销返现待结算入账
     *
     * @param id 账户id
     * @param operCash operCash
     * @param operGoldCoin operGoldCoin
     * @return void
     * @author Charlie
     * @date 2018/10/10 21:18
     */
    int waitCommission2Alive(Long id, BigDecimal operCash, BigDecimal operGoldCoin);



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
    int waitTeamIn2Alive(Long id, BigDecimal operCash, BigDecimal operGoldCoin);



    /**
     * 获取锁
     *
     * @param id 主键
     * @return boolean
     * @author Charlie
     * @date 2018/10/12 17:57
     */
    boolean lock(Long id);



    /**
     * 账户预提现
     *
     * @param accountId 账户id
     * @param operCash 提现金额
     * @param fromCommission 从佣金提现
     * @param fromTeamIn 用管理金提现
     * @param isRollBack 是否回滚(提现失败,金额回滚)
     * @return int
     * @author Charlie
     * @date 2018/10/13 22:56
     */
    int accountPreCashOut(Long accountId, BigDecimal operCash, BigDecimal fromCommission, BigDecimal fromTeamIn, boolean isRollBack);




    /**
     * 更新账户总现金
     *
     * @param id id
     * @param operCash 操作金额
     * @param isAdd 加钱还是减钱
     * @return int
     * @author Charlie
     * @date 2018/10/14 16:31
     */
    int cashOutSuccess(Long id, BigDecimal operCash, boolean isAdd);

    List<ShopMemberAccountCashOutIn> selectByOrder(String orderNo, Integer status, Integer detailStatus, Integer... types);

    /**
     * 根据用户id更新
     * @param shopMemberAccount
     * @param userId
     */
    void updateShopMemberAccountByUserId(ShopMemberAccount shopMemberAccount, Long userId);

    /**
     * 保存
     * @param shopMemberAccount
     */
    void saveShopMemberAccount(ShopMemberAccount shopMemberAccount);

    /**
     * 账户金额-详情-收支详情
     *
     * @param id
     * @param userId
     * @return
     */
    OrderAccountDetailsResponse findOrderAccountDetails(Long id, Long userId);


    /**
     * 账户增加可用的金币或现金
     *
     * @param accountId 账号id
     * @param operCash 操作金额
     * @param operGoldCoin 操作现金
     * @param isRollback 是否回滚
     * @author Charlie
     * @date 2018/11/22 16:52
     */
    int addAliveGoldCashAndCommissionCashGold(Long accountId, BigDecimal operCash, BigDecimal operGoldCoin, Boolean isRollback);
}
