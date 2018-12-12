package com.ouliao.dao.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserInfoDao;
import com.ouliao.domain.versionsecond.UserAdvice;
import com.ouliao.repository.versionsecond.UserAdviceCrudRepository;
import com.ouliao.repository.versionsecond.UserAdviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by nessary on 16-5-10.
 */
@Repository
class UserInfoDaoImpl implements UserInfoDao {
    @Autowired
    private UserAdviceRepository userAdviceRepository;
    @Autowired
    private UserAdviceCrudRepository userAdviceCrudRepository;


    @Override
    public void createUserAdvice(UserAdvice userAdvice) {
        userAdviceCrudRepository.save(userAdvice);
    }
}
