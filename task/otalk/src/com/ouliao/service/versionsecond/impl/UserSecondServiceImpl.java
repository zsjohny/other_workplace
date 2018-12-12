package com.ouliao.service.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserSecondDao;
import com.ouliao.domain.versionfirst.User;
import com.ouliao.service.versionsecond.UserSecondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by nessary on 16-5-12.
 */
@Transactional
@Service
public class UserSecondServiceImpl implements UserSecondService {
    @Autowired
    private UserSecondDao userSecondDao;

    @Override
    public void updateUserEmailCodeAndUserEmailByUserNum(String eamil, String emailCode, String userNum) {
        userSecondDao.updateUserEmailCodeAndUserEmailByUserNum(eamil, emailCode, userNum);
    }

    @Override
    public void updateUserPassByUserNum(String userPass, String userNum) {
        userSecondDao.updateUserPassByUserNum(userPass, userNum);
    }

    @Override
    public void updateUserBackPicUrlByUserNum(String backPicUrl, String userNum) {
        userSecondDao.updateUserBackPicUrlByUserNum(backPicUrl, userNum);
    }

    @Override
    public int updateUserGreetlByUserNum(String userGreet, String userNum) {
        return userSecondDao.updateUserGreetlByUserNum(userGreet, userNum);
    }

    @Override
    public User queryUserByEamil(String eamil) {
        return userSecondDao.queryUserByEamil(eamil);
    }

    @Override
    public void updateUserheadPicUrlByUserNum(String headPicUrl, String userNum) {
        userSecondDao.updateUserheadPicUrlByUserNum(headPicUrl,userNum);
    }
}
