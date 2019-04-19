package com.yujj.business.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.UserSign;
import com.yujj.dao.mapper.UserSignMapper;
//import com.yujj.entity.UserSign;

@Service
public class UserSignService {
    
    @Autowired
    private UserSignMapper userSignMapper;

    public int insertUserSign(UserSign userSign) {
        return userSignMapper.insertUserSign(userSign);
    }

    public UserSign getUserSign(long userId, int dayTime) {
        return userSignMapper.getUserSign(userId, dayTime);
    }

    public Map<Integer, UserSign> getUserSignOfWeek(long userId, int mondayTime) {
        return userSignMapper.getUserSignOfWeek(userId, mondayTime);
    }

    public int getTotalSignCount(long userId) {
        return userSignMapper.getTotalSignCount(userId);
    }

    public int getTotalSignCoins(long userId) {
        return userSignMapper.getTotalSignCoins(userId);
    }

}
