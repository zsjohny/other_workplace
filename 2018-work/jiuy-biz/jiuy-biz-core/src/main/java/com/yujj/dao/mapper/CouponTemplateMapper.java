package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.order.CouponTemplate;

@DBMaster
public interface CouponTemplateMapper {

	CouponTemplate search(@Param("id") Long id);

	/**
	 * 更新发行量
	 * @param id
	 * @param count
	 * @return
	 */
	int update(@Param("id")Long id, @Param("count") Integer count);

	/**
	 * 更新发放量
	 * @param id
	 * @param count
	 * @return
	 */
	int updateGrant(@Param("id")Long id, @Param("count") int count);
	
	CouponTemplate searchValidity(@Param("id") Long id, @Param("currentTime") long time);

	@MapKey("id")
	Map<Long, CouponTemplate> searchMap(@Param("ids") Collection<Long> couponTemplateIds);

	void updateExchangeCount(@Param("id") long id);


}
