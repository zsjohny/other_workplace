package com.e_commerce.miscroservice.distribution.dao;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DstbEarningsStrategy;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 14:39
 * @Copyright 玖远网络
 */
public interface DstbEarningsStrategyDao{

    /**
     * 根据 UserRoleType 和 EarningsType 更新策略
     *
     * @param strategy strategy
     * @return int
     * @author Charlie
     * @date 2018/10/17 14:48
     */
    int updateByUserRoleTypeAndEarningsType(DstbEarningsStrategy strategy);


    /**
     * 查询所有策略
     *
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DstbEarningsStrategy>
     * @author Charlie
     * @date 2018/10/17 14:58
     */
    List<DstbEarningsStrategy> findAll();


}
