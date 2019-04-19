package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;

@DBMaster
public interface StatisticsMapper {

	int updateUserClick(@Param("id") Long id);

	int updateUnKnownClick(@Param("id") Long id);
	
	int batchUpdateOrderCount(@Param("idSet") Collection<Long> idSet);

}
