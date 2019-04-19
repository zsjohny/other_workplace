package com.yujj.dao.mapper;

import java.util.Calendar;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.UserGiftLog;

@DBMaster
public interface UserGiftLogMapper {

	UserGiftLog searchOne(@Param("userId") Long userId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

	int add(@Param("userGiftLog") UserGiftLog userGiftLog);

	UserGiftLog searchOne(Long userId, Calendar startMounth);

	List<UserGiftLog> getMonthLog(@Param("userId")Long userId, @Param("startTime")long startTime);
	
}
