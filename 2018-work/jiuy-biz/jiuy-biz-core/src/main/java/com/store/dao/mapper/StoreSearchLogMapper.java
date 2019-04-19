package com.store.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.store.entity.StoreSearchLog;

@DBMaster
public interface StoreSearchLogMapper {

	/**
	 * @author DongZhong
	 */
	List<StoreSearchLog> getUserSearchLogs(@Param("storeBusinessId") long userId, @Param("pageQuery") PageQuery pageQuery);

	/**
	 * @param userSearchLog
	 * @return
	 */
	int addUserSearchLog(StoreSearchLog userSearchLog);
}
