package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.JiuCoinExchangeLog;
import com.jiuyuan.entity.query.PageQuery;

/**
 * @author jeff.zhan
 * @version 2016年12月16日 下午5:05:52
 * 
 */

@DBMaster
public interface JiuCoinExchangeLogMapper {

	int batchAdd(@Param("jiuCoinExchangeLogs") List<JiuCoinExchangeLog> jiuCoinExchangeLogs);

	void add(JiuCoinExchangeLog jiuCoinExchangeLog);

	List<JiuCoinExchangeLog> search(@Param("userId") long userId, @Param("pageQuery") PageQuery pageQuery, @Param("type") int type);

	int searchCount(@Param("userId") long userId, @Param("type") int type);

	Integer getCount(@Param("userId") Long userId, @Param("type") Integer type, @Param("relatedId") Long relatedId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);
	
}
