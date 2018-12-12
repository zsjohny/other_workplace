package com.ouliao.dao.versionsecond.impl;


import com.ouliao.dao.versionsecond.UserSureMsgCountDao;
import com.ouliao.domain.versionsecond.UserSureMsgCount;
import com.ouliao.repository.versionsecond.UserSureMsgCountPageRepository;
import com.ouliao.repository.versionsecond.UserSureMsgCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by nessary on 16-5-19.
 */
@Repository
public class UserSureMsgDaoImpl implements UserSureMsgCountDao {

    @Autowired
    private UserSureMsgCountRepository userSureMsgCountRepository;


    @Autowired
    private UserSureMsgCountPageRepository userSureMsgCountPageRepository;

    @Override
    public void saverUserSureMsgCountDao(UserSureMsgCount userSureMsgCount) {
        userSureMsgCountPageRepository.saveAndFlush(userSureMsgCount);
    }

    @Override
    public Integer findUserSureMsgCountByUserId(Integer userId) {
        return userSureMsgCountRepository.findUserSureMsgCountByUserId(userId);
    }

    @Override
    public void updateUSerSureMsgCountByUserId(Integer readCount, Integer userId) {
        userSureMsgCountRepository.updateUSerSureMsgCountByUserId(readCount, userId);
    }
}
