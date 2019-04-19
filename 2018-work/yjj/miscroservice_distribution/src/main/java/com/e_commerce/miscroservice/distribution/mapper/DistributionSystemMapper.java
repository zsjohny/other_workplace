package com.e_commerce.miscroservice.distribution.mapper;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DistributionSystem;
import com.e_commerce.miscroservice.commons.entity.distribution.UnderMyClassAResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Create by hyf on 2018/10/10
 */
@Mapper
public interface DistributionSystemMapper {
    /**
     * 合伙人今日添加总数
     * @param userId
     * @return
     */
    Integer findUnderPartnerCountToday(@Param("userId") Long userId);

    /**
     * 分销商旗下今日添加人数
     * @param userId
     * @return
     */
    Integer findUnderDistributorCountToday(@Param("userId") Long userId);

    /**
     * 今日新增粉丝数
     * @param userId
     * @return
     */
    Integer todayIncrease(@Param("userId") Long userId);

    /**
     * 我的一级粉丝
     *
     * @param userId
     * @return
     */
    List<UnderMyClassAResponse> findFollowerDetails(@Param("userId") Long userId);


    UnderMyClassAResponse findHigherInformation(@Param("userId") Long userId);
}
