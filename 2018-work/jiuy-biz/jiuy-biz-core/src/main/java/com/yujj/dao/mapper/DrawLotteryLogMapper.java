package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.DrawLotteryLog;
import com.jiuyuan.entity.query.PageQuery;

/**
 * @author jeff.zhan
 * @version 2016年11月2日 下午4:58:51
 * 
 */

@DBMaster
public interface DrawLotteryLogMapper {

	int add(DrawLotteryLog drawLotteryLog);

	List<DrawLotteryLog> getDrawLottery(@Param("userId") long userId, @Param("startTime") long startTime, @Param("endTime") long endTime);

	List<DrawLotteryLog> getFirstPrice(@Param("relatedId") Long id,@Param("limit") int limit);

	List<DrawLotteryLog> getOtherPrice(@Param("relatedId") Long id,@Param("limit") int limit);

	List<DrawLotteryLog> getByUser(@Param("userId") long userId, @Param("pageQuery") PageQuery pageQuery, @Param("status") int status, @Param("type") int type);

	int getByUserCount(@Param("userId") long userId, @Param("status") int status, @Param("type") int type);

	Integer getTotalJiuCoinByUser(@Param("userId") long userId);

	int getTotalLuckyTimes(@Param("userId") long userId);

	int getAllUserCount(@Param("status") int status, @Param("type") int type);

	List<DrawLotteryLog> getAllUser(@Param("pageQuery") PageQuery pageQuery, @Param("status") int status, @Param("type") int type);

}
