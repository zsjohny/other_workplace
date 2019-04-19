package com.yujj.business.service;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.entity.account.UserInvite;
import com.jiuyuan.entity.account.UserInviteRecord;
import com.yujj.dao.mapper.UserInviteMapper;

@Service
public class UserInviteService {

    @Autowired
    private UserInviteMapper userInviteMapper;

    public UserInvite getUserInvite(long userId) {
        return userInviteMapper.getUserInvite(userId);
    }

    public UserInvite getUserInviteByCode(String inviteCode) {
        return userInviteMapper.getUserInviteByCode(inviteCode);
    }


    public int addUserInvite(UserInvite userInvite) {
        return userInviteMapper.addUserInvite(userInvite);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserInviteRecord addUserInviteRecord(long userId, long invitedUserId, long time) {
    	
    	
    	
    	userInviteMapper.incrUserInviteCount(userId);

        UserInviteRecord record = new UserInviteRecord();
        record.setUserId(userId);
        record.setInvitedUserId(invitedUserId);
        record.setStatus(0);
        record.setCreateTime(time);
        record.setUpdateTime(time);
        userInviteMapper.addUserInviteRecord(record);
        return record;
    }

	public UserInviteRecord getByInvitedUserId(long userId) {
		return userInviteMapper.getByInvitedUserId(userId);
	}
}
