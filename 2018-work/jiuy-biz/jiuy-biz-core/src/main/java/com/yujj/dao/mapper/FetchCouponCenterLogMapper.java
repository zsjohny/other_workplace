package com.yujj.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.FetchCouponCenterLog;

@DBMaster
public interface FetchCouponCenterLogMapper {

	int add(@Param("fetchCouponCenterLog") FetchCouponCenterLog fetchCouponCenterLog);

	List<Map<String, Object>> getFetchCount(@Param("userId") long userId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

	int getCount(@Param("userId") long userId, @Param("couponTemplateId") Long coupon_template_id, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

}
