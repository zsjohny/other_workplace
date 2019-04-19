package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.UserSharedRecord;

@DBMaster
public interface UserSharedMapper {

	int addUserSharedRecord(UserSharedRecord userSharedRecord);

	UserSharedRecord getRecordBySharedId(@Param("sharedId")long sharedId);

	List<UserSharedRecord> getUserSharedList(@Param("userId")long userId);
	
}
