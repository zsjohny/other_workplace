package com.jiuyuan.dao.mapper.supplier;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.GroundUser;

/**
 * <p>
  * 地推用户表 Mapper 接口
 * </p>
 *
 * @author nijin
 * @since 2017-11-14
 */
@DBMaster
public interface GroundUserMapper extends BaseMapper<GroundUser> {
	void setAvailableBalance(@Param("groundUserId")long groundUserId,@Param("withdrawalMoney")double withdrawalMoney);
}