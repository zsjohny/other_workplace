package com.finace.miscroservice.activity.service.impl;

import com.finace.miscroservice.activity.dao.UserChannelDao;
import com.finace.miscroservice.activity.po.UserChannelPO;
import com.finace.miscroservice.activity.service.UserChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserChannelServiceImpl implements UserChannelService {

    @Autowired
    private UserChannelDao userChannelDao;


    @Override
    public void addUserChannel(UserChannelPO userChannelPO) {
        userChannelDao.addUserChannel(userChannelPO);
    }

    @Override
    public List<UserChannelPO> getUserChannelByPhone(String phone) {
        return userChannelDao.getUserChannelByPhone(phone);
    }

    @Override
    public List<UserChannelPO> getUserChannelByUid(String uid) {
        return userChannelDao.getUserChannelByUid(uid);
    }
}
