package com.e_commerce.miscroservice.distribution.mapper;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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
    int insertSelective(ShopMemberAccountCashOutIn cashOutIn);
    /**
     * 今日总进账金额
     *
     * @param userId
     * @return
     */
    Double findTodayCountMoney(@Param("userId") Long userId);

    /**
     * 今日佣金 进账
     * @param userId
     * @return
     */
    Double findCommissionTodayCountMoney(@Param("userId") Long userId);

    /**
     * 今日管理奖 进账
     *
     * @param userId
     * @return
     */
    Double findManageTodayCountMoney(@Param("userId") Long userId);
    /**
     * 今日金币进账
     *
     * @param userId
     * @return
     */
    Double findTodayGoldCountMoney(@Param("userId") Long userId);

    /**
     * 当月签到阶段奖励查询
     * @param userId
     * @return
     */
    List<ShopMemberAccountCashOutIn> findPeriodicalPrizeMonthLog(@Param("userId") Long userId);

    /**
     * 更新创建时间
     *
     * @param id id
     * @param createTime 时间戳
     * @return int
     * @author Charlie
     * @date 2018/10/18 16:19
     */
    int updCreateTime(@Param ("id") Long id, @Param ("createTime") String createTime);



    /**
     * 历史分享收益
     *
     * @param userId userId
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/14 18:08
     */
    Map<String,BigDecimal> findHistoryShareEarnings(@Param ("userId") Long userId);


    /**
     * 平台最近五条分享
     *
     * @return java.util.List
     * @author Charlie
     * @date 2018/12/15 11:50
     */
    List<Map<String,Object>> recentlyPlatformShares();

}
