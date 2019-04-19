package com.jiuyuan.dao.mapper.supplier;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ground.GroundBonusGrant;


@DBMaster
public interface GroundBonusGrantMapper extends BaseMapper<GroundBonusGrant> {

	/**
	 * 获取待入账金额
	 * @param groundUserId
	 * @param time 
	 * @return
	 */
	double getBalanceToBeCredited(@Param("groundUserId")long groundUserId, @Param("time")long time);

//	/**
//	 * 获取已入账金额
//	 * @param groundUserId
//	 * @param time 
//	 * @return
//	 */
//	double getBalanceCredited(@Param("groundUserId")long groundUserId, @Param("time")long time);

}