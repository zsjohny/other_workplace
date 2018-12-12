package com.ouliao.dao.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserDisconeryDao;
import com.ouliao.domain.versionsecond.UserDisconery;
import com.ouliao.repository.versionsecond.UserDisconeryPageRepository;
import com.ouliao.repository.versionsecond.UserDisconeryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nessary on 16-5-18.
 */
@Repository
public class UserDisconeryDaoImpl implements UserDisconeryDao {
    @Autowired
    private UserDisconeryPageRepository userDisconeryPageRepository;

    @Autowired
    private UserDisconeryRepository userDisconeryRepository;

    @Override
    public void saveUserDiconvery(UserDisconery userDisconery) {
        userDisconeryPageRepository.saveAndFlush(userDisconery);
    }

    @Override
    public List<UserDisconery> queryUserDisconveryAll(Integer start, Integer pageCount) {
        return userDisconeryRepository.queryUserDisconveryAll(start, pageCount);
    }

    @Override
    public UserDisconery queryUserDisconveryByUserId(Integer userId) {
        return userDisconeryRepository.queryUserDisconveryByUserId(userId);
    }

    @Override
    public void cancelUserContractByUserId(Integer userId) {
        userDisconeryRepository.cancelUserContractByUserId(userId);
    }

    @Override
    public void deleteUserDiscoveryByUserId(Integer userId) {

        userDisconeryRepository.deleteUserDiscoveryByUserId(userId);

    }

    @Override
    public void updateUserDiscoveryByUserId(String classicProgram, String disPicUrls, String listenProgram, Integer userId) {
        userDisconeryRepository.updateUserDiscoveryByUserId(classicProgram, disPicUrls, listenProgram, userId);
    }
}
