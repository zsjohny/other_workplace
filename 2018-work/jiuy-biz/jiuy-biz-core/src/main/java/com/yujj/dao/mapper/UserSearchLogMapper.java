package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.search.UserSearchLog;

@DBMaster
public interface UserSearchLogMapper {

	/**
	 * @author DongZhong
	 */
	List<UserSearchLog> getUserSearchLogs(@Param("userId") long userId, @Param("pageQuery") PageQuery pageQuery);

	/**
	 * @param userSearchLog
	 * @return
	 */
	int addUserSearchLog(UserSearchLog userSearchLog);
}
