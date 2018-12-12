package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.UserChannelDao;
import com.finace.miscroservice.activity.mapper.UserChannelMapper;
import com.finace.miscroservice.activity.po.UserChannelPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserChannelDaoImpl implements UserChannelDao {

    @Autowired
    private UserChannelMapper userChannelMapper;


    @Override
    public void addUserChannel(UserChannelPO userChannelPO) {
        userChannelMapper.addUserChannel(userChannelPO);
    }

    @Override
    public List<UserChannelPO> getUserChannelByPhone(String phone) {
        return userChannelMapper.getUserChannelByPhone(phone);
    }

    @Override
    public List<UserChannelPO> getUserChannelByUid(String uid) {
        return userChannelMapper.getUserChannelByUid(uid);
    }
}
