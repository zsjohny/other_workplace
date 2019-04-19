package com.jiuy.wxaproxy.common.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.wxaproxy.common.system.persistence.model.OperationLog;
import com.jiuyuan.dao.annotation.DBMirror;

/**
 * 日志记录dao
 *
 * @author jiuyuan
 * @Date 2017/4/16 23:44
 */
@DBMirror
public interface LogDao {

	/**
	 * 获取操作日志
	 *
	 * @author jiuyuan
	 * @Date 2017/4/16 23:48
	 */
	List<Map<String, Object>> getOperationLogs(@Param("page") Page<OperationLog> page,
			@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("logName") String logName,
			@Param("logType") String logType, @Param("orderByField") String orderByField,
			@Param("isAsc") boolean isAsc);

	/**
	 * 获取登录日志
	 *
	 * @author jiuyuan
	 * @Date 2017/4/16 23:48
	 */
	List<Map<String, Object>> getLoginLogs(@Param("page") Page<OperationLog> page, @Param("beginTime") String beginTime,
			@Param("endTime") String endTime, @Param("logName") String logName,
			@Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);
}
