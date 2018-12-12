package com.ouliao.service.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserActiveDao;
import com.ouliao.domain.versionfirst.UserActive;
import com.ouliao.service.versionfirst.UserActiveServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by nessary on 16-5-10.
 */
@Service
@Transactional
public class UserActiveServerImpl implements UserActiveServer {
    @Autowired
    private UserActiveDao userActiveDao;


    @Override
    public void saveUserCountByUserActive(UserActive userActive) {
        userActiveDao.saveUserCountByUserActive(userActive);
    }

    @Override
    public void updateUserCountByUserId(Integer userCount, Integer userId) {
        userActiveDao.updateUserCountByUserId(userCount, userId);

    }

    @Override
    public List<UserActive> queryUserActiveAllByUserId(Integer userId) {
        return userActiveDao.queryUserActiveAllByUserId(userId);
    }
}
