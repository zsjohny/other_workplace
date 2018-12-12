package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.UserHeadlineChannelDao;
import com.finace.miscroservice.activity.mapper.UserHeadlineChannelMapper;
import com.finace.miscroservice.activity.po.UserHeadlineChannelPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 */
@Component
public class UserHeadlineChannelDaoImpl implements UserHeadlineChannelDao{

    @Autowired
    private UserHeadlineChannelMapper userHeadlineChannelMapper;


    @Override
    public UserHeadlineChannelPO getByImei(Map<String, Object> map) {
        return userHeadlineChannelMapper.getByImei(map);
    }

    @Override
    public int updateStatusByImei(Map<String, Object> map) {
        return userHeadlineChannelMapper.updateStatusByImei(map);
    }

    @Override
    public void addUserHeadlineChannel(UserHeadlineChannelPO userHeadlineChannelPO) {
        userHeadlineChannelMapper.addUserHeadlineChannel(userHeadlineChannelPO);
    }



}
