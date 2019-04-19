package com.e_commerce.miscroservice.distribution.service;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DstbEarningsStrategy;
import com.e_commerce.miscroservice.commons.entity.distribution.EarningsStrategyVo;
import com.e_commerce.miscroservice.commons.entity.distribution.EarningsVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/8 15:12
 * @Copyright 玖远网络
 */
public interface DstbEarningsStrategyService{


    /**
     * 计算
     *
     * @param strategyList strategyList
     * @param earningsType 0:本人下单返利,1:一级粉丝下单返利,2:二级粉丝下单返利,10:分销商团队收益
     * @param userRoleType 0 无等级 1 店长 2分销商 3合伙人
     * @param realPay 实际支付
     * @param goldCoinExchangeRate goldCoinExchangeRate
     * @return 收益情况
     * @author Charlie
     * @date 2018/10/9 19:05
     */
    Optional<EarningsVo> calculateEarnings(List<DstbEarningsStrategy> strategyList, Integer earningsType, Integer userRoleType, BigDecimal realPay, BigDecimal goldCoinExchangeRate);

    /**
     * 查找所有策略
     *
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DstbEarningsStrategy>
     * @author Charlie
     * @date 2018/10/17 13:48
     */
    List<DstbEarningsStrategy> findAll();


    /**
     * 更新策略
     *
     * @param strategyRequestList 待更新信息
     * @author Charlie
     * @date 2018/10/17 13:49
     */
    void update(List<EarningsStrategyVo> strategyRequestList);


    /**
     * 查询所有策略
     *
     * @author Charlie
     * @date 2018/10/17 14:58
     */
    Map<String, EarningsStrategyVo> allStrategy();
}
