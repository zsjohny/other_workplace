package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.UserSharedRecordOrderNew;

@DBMaster
public interface UserSharedRecordOrderNewMapper {

	void insertUserSharedRecordOrderNew(UserSharedRecordOrderNew userSharedRecordOrderNew);

	UserSharedRecordOrderNew getUserSharedRecordOrderNew(long orderNo);

}
