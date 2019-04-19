package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.log.Log;
import com.jiuyuan.entity.log.RelatedOrderLog;
import com.jiuyuan.entity.log.UserLog;

@DBMaster
public interface LogMapper {

	public int addLogs(@Param("time") Long time, @Param("logList") List<Log> logList);	
	
	public int addRelatedOrderLogs(@Param("time") Long time, @Param("logList") List<RelatedOrderLog> logList);
	
	public List<Log> getProductLogs(@Param("productId") long productId);
	
	public List<Log> getAllProductLogs(@Param("time") long time);

	public int addUserStartLog(@Param("userLog") UserLog userLog);

	public int addUserLog(@Param("userLog") UserLog userLog);

}
