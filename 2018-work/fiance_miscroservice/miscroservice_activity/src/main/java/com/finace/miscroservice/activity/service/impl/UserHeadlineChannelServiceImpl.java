package com.finace.miscroservice.activity.service.impl;

import com.finace.miscroservice.activity.dao.UserHeadlineChannelDao;
import com.finace.miscroservice.activity.po.UserHeadlineChannelPO;
import com.finace.miscroservice.activity.service.UserHeadlineChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserHeadlineChannelServiceImpl implements UserHeadlineChannelService{


    @Autowired
    private UserHeadlineChannelDao userHeadlineChannelDao;


    @Override
    public void addUserHeadlineChannel(UserHeadlineChannelPO userHeadlineChannelPO) {
        userHeadlineChannelDao.addUserHeadlineChannel(userHeadlineChannelPO);
    }

    @Override
    public UserHeadlineChannelPO getByImei(String imei, String status) {
         Map<String, Object> map = new HashMap<>();
         map.put("imei", imei);
         map.put("status", status);
        return userHeadlineChannelDao.getByImei(map);
    }

    @Override
    public int updateStatusByImei(String imei, String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("imei", imei);
        map.put("status", status);
        return userHeadlineChannelDao.updateStatusByImei(map);
    }
}
