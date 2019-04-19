package com.yujj.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.UserInviteRewardLog;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.dao.mapper.UserInviteRewardLogMapper;

@Service
public class UserInviteRewardLogService {
	@Autowired
	private UserInviteRewardLogMapper userInviteRewardLogMapper;

	public int searchCount(long userId) {
		return userInviteRewardLogMapper.searchCount(userId);
	}

	public List<UserInviteRewardLog> search(PageQuery pageQuery, long userId) {
		return userInviteRewardLogMapper.search(pageQuery, userId);
	}

	public int add(UserInviteRewardLog userInviteRewardLog) {
		return userInviteRewardLogMapper.add(userInviteRewardLog);
	}
}
