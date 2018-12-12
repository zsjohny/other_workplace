package com.ouliao.service.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserDisconeryDao;
import com.ouliao.domain.versionsecond.UserDisconery;
import com.ouliao.service.versionsecond.UserDisconeryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by nessary on 16-5-18.
 */
@Transactional
@Service
public class UserDisconeryServiceImpl implements UserDisconeryService {

    @Autowired
    private UserDisconeryDao userDisconeryDao;

    @Override
    public void saveUserDiconvery(UserDisconery userDisconery) {
        userDisconeryDao.saveUserDiconvery(userDisconery);
    }

    @Cacheable(value = "userDiscovery", key = "#start")
    @Override
    public List<UserDisconery> queryUserDisconveryAll(Integer start, Integer pageCount) {
        return userDisconeryDao.queryUserDisconveryAll((start - 1) * pageCount, pageCount);
    }

    @Override
    public UserDisconery queryUserDisconveryByUserId(Integer userId) {
        return userDisconeryDao.queryUserDisconveryByUserId(userId);
    }

    @Override
    public void cancelUserContractByUserId(Integer userId) {
        userDisconeryDao.cancelUserContractByUserId(userId);


    }

    @Override
    public void deleteUserDiscoveryByUserId(Integer userId) {

        userDisconeryDao.deleteUserDiscoveryByUserId(userId);

    }

    @Override
    public void updateUserDiscoveryByUserId(String classicProgram, String disPicUrls, String listenProgram, Integer userId) {
        userDisconeryDao.updateUserDiscoveryByUserId(classicProgram, disPicUrls, listenProgram, userId);
    }
}
