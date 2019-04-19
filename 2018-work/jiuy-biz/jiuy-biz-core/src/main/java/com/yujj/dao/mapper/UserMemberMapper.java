package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.account.UserMember;

/**
 * @author jeff.zhan
 * @version 2016年10月26日 下午3:47:36
 * 
 */
@DBMaster
public interface UserMemberMapper {

	int add(UserMember userMember);

	UserMember getByUserId(@Param("userId") long userId);

	int changeBelongStoreId(@Param("id") Long id, @Param("belongStoreName") String belongStoreName, @Param("belongStoreId") Long belongStoreId, @Param("currentTime") long currentTime);

	int addDistributionPartners(@Param("id") Long id);

}
