package com.e_commerce.miscroservice.activity.mapper;

import com.e_commerce.miscroservice.commons.entity.application.activity.SignLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Create by hyf on 2018/10/8
 */
@Mapper
public interface ShopSignMapper {


    /**
     *  获取今日签到
     *  @param userId
     * @return
     */
    Integer getNowSignByUser(@Param("userId") Long userId);

    /**
     *  当月签到天数
     *  @param userId
     * @return
     */
    List<String> getSignLogMonthByUser(@Param("userId") Long userId);

    /**
     * 获取签到日期最近一天
     * @param userId
     * @return
     */
    SignLog getSignLogLimitDescByUser(@Param("userId") Long userId);
}
