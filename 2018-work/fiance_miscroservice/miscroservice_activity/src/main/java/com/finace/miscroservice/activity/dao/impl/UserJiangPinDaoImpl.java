package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.UserJiangPinDao;
import com.finace.miscroservice.activity.mapper.UserJiangPinMapper;
import com.finace.miscroservice.activity.po.GiftPO;
import com.finace.miscroservice.activity.po.InvitationPO;
import com.finace.miscroservice.activity.po.UserJiangPinPO;
import com.finace.miscroservice.commons.entity.User;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class UserJiangPinDaoImpl implements UserJiangPinDao {


    @Autowired
    private UserJiangPinMapper userJiangPinMapper;

    @Override
    public void addUserJiangPin(UserJiangPinPO userJiangPinPO) {
           this.userJiangPinMapper.addUserJiangPin(userJiangPinPO);
    }

    @Override
    public List<UserJiangPinPO> getUserJiangPinByUserId(String userId) {
        return userJiangPinMapper.getUserJiangPinByUserId(userId);
    }

    @Override
    public List<UserJiangPinPO> getAllUserJiangPin() {
        return userJiangPinMapper.getAllUserJiangPin();
    }

    @Override
    public List<UserJiangPinPO> getUserJplx(String userId) {
        return userJiangPinMapper.getUserJplx(userId);
    }

    @Transactional
    @Override
    public void addUserAward(Integer underUser,Integer userId, String jiangPinName, String addTime, String remark, Integer code, Integer isSend) {
        userJiangPinMapper.addUserAward(underUser,userId,jiangPinName,addTime,remark,code,isSend);
    }

    @Override
    public List<UserJiangPinPO> findUserAward(Integer underUser,Integer userId, Integer code) {
        return userJiangPinMapper.findUserAward(underUser,userId,code);
    }

    @Override
    public List<GiftPO> findUserAwards(int userId, Integer code,String starttime,String endtime) {
        return userJiangPinMapper.findUserAwards(userId,code,starttime,endtime);
    }

    @Override
    public Integer findJdCardMoneyAmtByUserId(int userId) {
        return userJiangPinMapper.findJdCardMoneyAmtByUserId(userId);
    }

    @Override
    public InvitationPO getInvitations(Integer userId,String date,String endTime) {
        return userJiangPinMapper.getInvitations(userId, date, endTime);
    }
}
