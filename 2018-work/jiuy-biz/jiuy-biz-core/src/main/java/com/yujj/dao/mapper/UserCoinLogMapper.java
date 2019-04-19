package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.entity.account.UserCoinLog;

@DBMaster
public interface UserCoinLogMapper {

    int addUserCoinLog(UserCoinLog userCoinLog);

    int getUserCoinLogCount(@Param("userId") long userId, @Param("startTime") long startTime,
                            @Param("endTime") long endTime, @Param("operation") UserCoinOperation operation);

    int getUserCoinLogCountByRelatedId(@Param("userId") long userId, @Param("relatedId") String relatedId,
                                       @Param("operation") UserCoinOperation operation);
    
    public List<UserCoinLog> increaseList(@Param("userId") Long userId, @Param("pageQuery") PageQuery pageQuery);
    
    public int getIncreaseListCount(@Param("userId") Long userId);
    
    public int updateReadStatus(@Param("userId") Long userId, @Param("status") int status);
    
    public List<UserCoinLog> reduceList(@Param("userId") Long userId, @Param("pageQuery") PageQuery pageQuery);
    
    public List<UserCoinLog> exchangeList(@Param("userId") Long userId, @Param("pageQuery") PageQuery pageQuery, @Param("type") int type);
    
    public int getReduceListCount(@Param("userId") Long userId);
    
    public int getExchangeListCount(@Param("userId") Long userId, @Param("type") int type);
    
    public int countNewReadStatus(@Param("userId") Long userId);

    public List<UserCoinLog> getAllList(@Param("userId") Long userId, @Param("pageQuery") PageQuery pageQuery);
    
    public int getAllListCount(@Param("userId") Long userId);

	Integer getHistoryTotalCoin(@Param("userId") long userId);

	int getUserCoinSameLogCount(@Param("userId") long userId, @Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("operation") UserCoinOperation operation, @Param("relatedId") long relatedId);

	Integer getTotalUserCoin(@Param("userId") long userId, @Param("startTime") long startTime, @Param("endTime") long endTime, @Param("operation") UserCoinOperation operation);
    
}
