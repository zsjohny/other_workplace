package com.yujj.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.InvitedUserActionLog;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.dao.mapper.InvitedUserActionLogMapper;

@Service
public class InvitedUserActionLogService {
	
	@Autowired
	private InvitedUserActionLogMapper invitedUserActionLogMapper;

	public int add(InvitedUserActionLog invitedUserActionLog) {
		return invitedUserActionLogMapper.add(invitedUserActionLog);
	}

	public int searchCount(long userId) {
		return invitedUserActionLogMapper.searchCount(userId);
	}

	public List<InvitedUserActionLog> search(PageQuery pageQuery, long invitor) {
		return invitedUserActionLogMapper.search(pageQuery, invitor);
	}

	public InvitedUserActionLog getByUserId(long userId, int action) {
		return invitedUserActionLogMapper.getByUserId(userId, action);
	}

	public List<InvitedUserActionLog> getByInvitor(long invitor, int action) {
		return invitedUserActionLogMapper.getByInvitor(invitor, action);
	}

	public int getNewInvitedOrderCount(long invitor, int action, long startTime, long expiredTime) {
		return invitedUserActionLogMapper.getNewInvitedOrderCount(invitor, action, startTime, expiredTime);
	}

	public int getInvitedUserCount(long invitorId, long startTime, long endTime) {
		return invitedUserActionLogMapper.getInvitedCount(invitorId, 0, startTime, endTime);
	}
	
	public int getInvitedOrderCount(long invitorId, long startTime, long endTime) {
		return invitedUserActionLogMapper.getInvitedCount(invitorId, 1, startTime, endTime);
	}

}
