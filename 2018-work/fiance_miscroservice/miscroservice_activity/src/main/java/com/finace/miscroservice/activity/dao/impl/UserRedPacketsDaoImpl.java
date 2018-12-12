package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.UserRedPacketsDao;
import com.finace.miscroservice.activity.mapper.UserRedPacketsMapper;
import com.finace.miscroservice.activity.po.UserRedPacketsPO;
import com.finace.miscroservice.commons.entity.BasePage;
import com.finace.miscroservice.commons.entity.UserRedPackets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户红包dao层实现类
 */
@Component
public class UserRedPacketsDaoImpl implements UserRedPacketsDao {

    @Autowired
    private UserRedPacketsMapper userRedPacketsMapper;


    @Override
    public int getCountRedPacketsByUserId(Map<String, Object> map) {

        return userRedPacketsMapper.getCountRedPacketsByUserId(map);
    }

    @Override
    public List<UserRedPackets> getRpByUserId(Map<String, Object> map, int page) {
        BasePage.setPage(page);
        List<UserRedPackets> list = userRedPacketsMapper.getRpByUserId(map);
        return list;
    }

    @Override
    public List<UserRedPacketsPO> getRedPacketsByUserId(Map<String, Object> map) {
        return  userRedPacketsMapper.getRedPacketsByUserId(map);
    }

    @Override
    public UserRedPackets getRpById(int id) {
        return userRedPacketsMapper.getRpById(id);
    }

    @Override
    public List<UserRedPacketsPO> getHbByParam(Map<String, Object> map) {
        return userRedPacketsMapper.getHbByParam(map);
    }

    @Override
    public int getCountHbByParam(Map<String, Object> map) {
        return userRedPacketsMapper.getCountHbByParam(map);
    }

    @Override
    public void updateRedPacketsStatus(Map<String, Object> map) {
        userRedPacketsMapper.updateRedPacketsStatus(map);
    }

    @Override
    public void addUserRedPackets(UserRedPacketsPO userRedPacketsPO) {
        userRedPacketsMapper.addUserRedPackets(userRedPacketsPO);
    }

    @Override
    public UserRedPackets getInviterCountSumHb(int userId) {
        return userRedPacketsMapper.getInviterCountSumHb(userId);
    }

    @Override
    public List<UserRedPackets> getInviterList(int userId) {
        return userRedPacketsMapper.getInviterList(userId);
    }

    @Override
    public UserRedPackets getUserIdInviter(int userId, int inviter) {
        Map<String, Integer> map = new HashMap<>();
        map.put("userId",userId);
        map.put("inviter",inviter);
        return userRedPacketsMapper.getUserIdInviter(map);
    }
	
	@Override
	public List<UserRedPackets> getEndedUserRedPackets() {
		return this.userRedPacketsMapper.getEndedUserRedPackets();
	}

    @Override
    public List<String> getWillExpiredUserId() {
        return userRedPacketsMapper.getWillExpiredUserId();
    }
}


