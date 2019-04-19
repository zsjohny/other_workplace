package com.e_commerce.miscroservice.distribution.service;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.entity.distribution.CashOutInUpdVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/10 20:01
 * @Copyright 玖远网络
 */
public interface ShopMemberAccountCashOutInService{


    int insertSelective(ShopMemberAccountCashOutIn cashOutIn);


    /**
     * 查询某一订单的分销返利未结算的流水
     *
     * @param orderNo 订单号
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn>
     * @author Charlie
     * @date 2018/10/10 20:59
     */
    List<ShopMemberAccountCashOutIn> selectOrderWaitCommission(String orderNo);




    /**
     * 查询所有待结算的团队收益流水
     *
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn>
     * @author Charlie
     * @date 2018/10/10 21:48
     */
    List<ShopMemberAccountCashOutIn> listWaitTeamIn();


    /**
     * 提现成功
     *
     * @param history   history
     * @param updVo     updVo
     * @param balanceCash 提现的余额
     * @param isSuccess   1成功,-1失败
     * @return int
     * @author Charlie
     * @date 2018/10/14 16:12
     */
    int cashOutSuccess(ShopMemberAccountCashOutIn history, CashOutInUpdVo updVo, BigDecimal balanceCash, Integer isSuccess);



    /**
     * 根据id查找
     *
     * @param cashOutId cashOutId
     * @return com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn
     * @author Charlie
     * @date 2018/10/14 22:18
     */
    ShopMemberAccountCashOutIn findById(Long cashOutId);



    /**
     * 根据订单号查询预结算的管理金
     *
     * @param orderNo orderNo
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn>
     * @author Charlie
     * @date 2018/10/16 18:06
     */
    List<ShopMemberAccountCashOutIn> selectOrderWaitManager(String orderNo);


    /**
     * 更新状态
     *
     * @param id id
     * @param sourceStatus 原来状态
     * @param targetStatus 更新后状态
     * @param operTime 操作时间
     * @param inOutType 收入支出类型
     * @return int
     * @author Charlie
     * @date 2018/10/16 20:06
     */
    int updStatus(Long id, int sourceStatus, int targetStatus, long operTime, Integer inOutType, int sourceDetailStatus, int targetDetailStatus);




//    /**
//     * 查询该用户在订单下该订单下已结算的分佣流水数量
//     *
//     * @param userId  userId
//     * @param orderNo orderNo
//     * @return int
//     * @author Charlie
//     * @date 2018/10/16 23:27
//     */
//    long countUserHasSettleOutInByOrder(Long userId, String orderNo);

    /**
     * 根据页码 用户id分销账户日志
     * @return
     */
    List<ShopMemberAccountCashOutIn> findLogList(Integer page, Long userId);
    /**
     * 当月 签到阶段奖励
     * @param userId
     * @return
     */
    List<ShopMemberAccountCashOutIn> findPeriodicalPrizeMonthLog(Long userId);

    /**
     * 保存账户流水
     * @return
     */
    void saveAccountLog(ShopMemberAccountCashOutIn shopMemberAccount);




    /**
     * 历史分享收益
     *
     * @param userId userId
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/14 18:00
     */
    Map<String,BigDecimal> findHistoryShareEarnings(Long userId);


    /**
     * 平台最近五条分享
     *
     * @return java.util.List
     * @author Charlie
     * @date 2018/12/15 11:50
     */
    List<Map<String,Object>> recentlyPlatformShares();

}
