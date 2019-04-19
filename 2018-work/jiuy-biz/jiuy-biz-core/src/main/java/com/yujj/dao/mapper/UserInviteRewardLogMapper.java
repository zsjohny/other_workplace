package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.UserInviteRewardLog;
import com.jiuyuan.entity.query.PageQuery;

@DBMaster
public interface UserInviteRewardLogMapper {

	int searchCount(@Param("userId") long userId);

	List<UserInviteRewardLog> search(@Param("pageQuery") PageQuery pageQuery, @Param("userId") long userId);

	int add(@Param("userInviteRewardLog") UserInviteRewardLog userInviteRewardLog);

}
