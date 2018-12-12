package com.ouliao.service.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserSureMsgCountDao;
import com.ouliao.domain.versionsecond.UserSureMsgCount;
import com.ouliao.service.versionsecond.UserSureMsgCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by nessary on 16-5-19.
 */
@Service
@Transactional
public class UserSureMsgCountServiceImpl implements UserSureMsgCountService {
    @Autowired
    private UserSureMsgCountDao userSureMsgCountDao;


    @Override
    public void saverUserSureMsgCountDao(UserSureMsgCount userSureMsgCount) {
        userSureMsgCountDao.saverUserSureMsgCountDao(userSureMsgCount);
    }

    @Override
    public Integer findUserSureMsgCountByUserId(Integer userId) {
        return userSureMsgCountDao.findUserSureMsgCountByUserId(userId);
    }

    @Override
    public void updateUSerSureMsgCountByUserId(Integer readCount, Integer userId) {
        userSureMsgCountDao.updateUSerSureMsgCountByUserId(readCount, userId);
    }
}
