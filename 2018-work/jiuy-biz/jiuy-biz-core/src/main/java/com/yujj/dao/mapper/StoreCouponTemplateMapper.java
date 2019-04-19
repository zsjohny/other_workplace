package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.dao.annotation.DBMaster;

/**
* @author WuWanjian
* @version 创建时间: 2017年5月27日 下午5:47:10
*/
@DBMaster
public interface StoreCouponTemplateMapper {
	
	StoreCouponTemplate searchValidity(@Param("templateId") Long templateId, @Param("time") long time);

	int updateCount(@Param("templateId") Long templateId, @Param("count") Integer count);
	
	int updateGrant(@Param("templateId") Long templateId, @Param("count") Integer count);
}
