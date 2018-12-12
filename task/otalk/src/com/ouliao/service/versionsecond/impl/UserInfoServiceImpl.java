package com.ouliao.service.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserInfoDao;
import com.ouliao.domain.versionsecond.UserAdvice;
import com.ouliao.service.versionsecond.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by nessary on 16-5-10.
 */
@Transactional
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public void createUserAdvice(UserAdvice userAdvice) {
        userInfoDao.createUserAdvice(userAdvice);
    }
}
