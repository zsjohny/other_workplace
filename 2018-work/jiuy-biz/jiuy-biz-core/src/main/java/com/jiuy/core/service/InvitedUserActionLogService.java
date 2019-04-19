package com.jiuy.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.mapper.InvitedUserActionLogMapper;
import com.jiuyuan.entity.InvitedUserActionLog;


@Service
public class InvitedUserActionLogService {
	
	@Autowired
	private InvitedUserActionLogMapper invitedUserActionLogMapper;

	public InvitedUserActionLog getByUserId(long userId, int action) {
		return invitedUserActionLogMapper.getByUserId(userId, action);
	}


}
