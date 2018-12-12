package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.UserChannelGeneralizeDao;
import com.finace.miscroservice.activity.mapper.UserChannelGeneralizeMapper;
import com.finace.miscroservice.activity.po.entity.UserChannelGeneralize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
public class UserChannelGeneralizeDaoImpl implements UserChannelGeneralizeDao {

    @Autowired
    private UserChannelGeneralizeMapper userChannelGeneralizeMapper;

    @Transactional
    @Override
    public void insertUserChannelGeneralize(UserChannelGeneralize userChannelGeneralize) {
        userChannelGeneralizeMapper.insertUserChannelGeneralize( userChannelGeneralize);
    }

    @Override
    public UserChannelGeneralize findChannelGeneralize(String idfa, String phone) {
        return userChannelGeneralizeMapper.findChannelGeneralize(idfa,phone);
    }

    @Override
    public List<String> findChannelGeneralizes() {
            return userChannelGeneralizeMapper.findChannelGeneralizes();
    }

    @Override
    public void upUserChannelGeneralize(String idfa) {
        userChannelGeneralizeMapper.upUserChannelGeneralize(idfa);
    }


}
