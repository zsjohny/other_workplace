package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.InvitedUserActionLog;
import com.jiuyuan.entity.query.PageQuery;

@DBMaster
public interface InvitedUserActionLogMapper {

	int add(@Param("invitedUserActionLog") InvitedUserActionLog invitedUserActionLog);

	int searchCount(@Param("invitor") long userId);

	List<InvitedUserActionLog> search(@Param("pageQuery") PageQuery pageQuery, @Param("invitor") long invitor);

	InvitedUserActionLog getByUserId(@Param("userId") long userId, @Param("action") int action);

	List<InvitedUserActionLog> getByInvitor(@Param("invitor") long invitor, @Param("action") int action);

	int getNewInvitedOrderCount(@Param("invitor") long invitor, @Param("action") int action, @Param("startTime") long startTime, @Param("expiredTime") long expiredTime);

	int getInvitedCount(@Param("invitor") long invitor, @Param("action") int action, @Param("startTime") long startTime, @Param("endTime") long endTime);
	
}
