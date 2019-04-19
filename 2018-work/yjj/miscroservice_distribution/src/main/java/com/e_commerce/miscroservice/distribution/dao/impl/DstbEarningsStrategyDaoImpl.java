package com.e_commerce.miscroservice.distribution.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DstbEarningsStrategy;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.distribution.dao.DstbEarningsStrategyDao;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 14:39
 * @Copyright 玖远网络
 */
@Component
public class DstbEarningsStrategyDaoImpl implements DstbEarningsStrategyDao{


    /**
     * 根据 UserRoleType 和 EarningsType 更新策略
     *
     * @param strategy strategy
     * @return int
     * @author Charlie
     * @date 2018/10/17 14:48
     */
    @Override
    public int updateByUserRoleTypeAndEarningsType(DstbEarningsStrategy strategy) {
        strategy.setId (null);
        return MybatisOperaterUtil.getInstance ().update (
                strategy,
                new MybatisSqlWhereBuild (DstbEarningsStrategy.class)
                        .eq (DstbEarningsStrategy::getDelStatus, StateEnum.NORMAL)
                        .eq (DstbEarningsStrategy::getUserRoleType, strategy.getUserRoleType ())
                        .eq (DstbEarningsStrategy::getEarningsType, strategy.getEarningsType ())
        );
    }



    /**
     * 查询所有策略
     *
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DstbEarningsStrategy>
     * @author Charlie
     * @date 2018/10/17 14:58
     */
    @Override
    public List<DstbEarningsStrategy> findAll() {
        return MybatisOperaterUtil.getInstance ().finAll (
                new DstbEarningsStrategy(),
                new MybatisSqlWhereBuild (DstbEarningsStrategy.class)
                        .eq (DstbEarningsStrategy::getDelStatus, StateEnum.NORMAL)
        );
    }
}
