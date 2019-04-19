package com.e_commerce.miscroservice.distribution.dao;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/10 20:01
 * @Copyright 玖远网络
 */
public interface ShopMemberAccountCashOutInDao{
    int insertSelective(ShopMemberAccountCashOutIn cashOutIn);
    /**
     * 今日总进账金额
     *
     * @param userId
     * @return
     */
    Double findTodayCountMoney(Long userId);

    /**
     * 今日佣金进账
     * @param userId
     * @return
     */
    Double findCommissionTodayCountMoney(Long userId);

    /**
     * 今日管理奖 进账
     * @param userId
     * @return
     */
    Double findManageTodayCountMoney(Long userId);

    /**
     * 账单信息
     *
     * @param choose
     * @param type
     * @param inOutType
     * @param status
     * @param userId
     * @param page
     * @return
     */
    List<ShopMemberAccountCashOutIn> findBillDetails(Integer choose, Integer type, Integer inOutType, Integer status, Long userId, Integer page);

    /**
     * 今日金币进账
     * @param userId
     * @return
     */
    Double findTodayGoldCountMoney(Long userId);
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
     * 将结算条件设为成功
     *
     * @param outInId outInId
     * @return int
     * @author Charlie
     * @date 2018/10/20 14:00
     */
    int conditionIsOk(Long outInId);

    /**
     * 根据订单查询流水表
     * @param orderNo
     * @param code
     * @return
     */
    List<ShopMemberAccountCashOutIn> findByOrderNo(String orderNo, int code);

    /**
     * 锁
     *
     * @param cashOutId cashOutId
     * @return int
     * @author Charlie
     * @date 2018/11/5 14:43
     */
    int lock(Long cashOutId);



    /**
     * 查找提现露水
     *
     * @param cashOutId cashOutId
     * @return com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn
     * @author Charlie
     * @date 2018/11/19 10:33
     */
    ShopMemberAccountCashOutIn findCashOutById(Long cashOutId);




    /**
     * 历史分享收益
     *
     * @param userId userId
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/14 18:08
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
