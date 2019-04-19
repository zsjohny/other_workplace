package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.UserSharedClickRecord;
@DBMaster
public interface UserSharedClickMapper {

	int addUserSharedClickRecord(UserSharedClickRecord userSharedClickRecord);

	List<UserSharedClickRecord> getUserSharedClickRecordList(@Param("userId")long userId);

	int getJiuCoinCount(@Param("sharedUserId")long sharedUserId, @Param("time")long time,@Param("type")int type);

	List<UserSharedClickRecord> getUserSharedClickRecordListBeforeTime(@Param("sharedUserId")long sharedUserId,  @Param("time")long time, @Param("type")int type);

	List<UserSharedClickRecord> getUserSharedClickRecordListBySharedId(@Param("sharedId")long sharedId);

}
