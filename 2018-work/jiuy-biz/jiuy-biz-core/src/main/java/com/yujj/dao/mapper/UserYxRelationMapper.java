package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.yunxin.UserYxRelation;

@DBMaster
public interface UserYxRelationMapper {


	public int saveOrUpdate(UserYxRelation relation);

	@MapKey("userId")
	public Map<Long, UserYxRelation> getUserYxRelations(
			@Param("collection") Collection<Long> userId);

	public int updateUserRelationStatus(@Param("userId") Long userId,
			@Param("status") Integer status);

	public int updateUserToken(@Param("userId") Long userId,
			@Param("token") String token);
}
