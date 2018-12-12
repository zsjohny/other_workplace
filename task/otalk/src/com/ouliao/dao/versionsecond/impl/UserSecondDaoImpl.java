package com.ouliao.dao.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserSecondDao;
import com.ouliao.domain.versionfirst.User;
import com.ouliao.repository.versionsecond.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by nessary on 16-5-12.
 */
@Repository
public class UserSecondDaoImpl implements UserSecondDao {
    @Autowired
    private UserRepository userRepository;


    @Override
    public void updateUserEmailCodeAndUserEmailByUserNum(String eamil, String emailCode, String userNum) {

        userRepository.updateUserEmailCodeAndUserEmailByUserNum(eamil, emailCode, userNum);


    }

    @Override
    public void updateUserPassByUserNum(String userPass, String userNum) {
        userRepository.updateUserPassByUserNum(userPass, userNum);
    }

    @Override
    public void updateUserBackPicUrlByUserNum(String backPicUrl, String userNum) {
        userRepository.updateUserBackPicUrlByUserNum(backPicUrl, userNum);
    }

    @Override
    public void updateUserheadPicUrlByUserNum(String headPicUrl, String userNum) {
        userRepository.updateUserheadPicUrlByUserNum(headPicUrl, userNum);
    }

    @Override
    public int updateUserGreetlByUserNum(String userGreet, String userNum) {
        return userRepository.updateUserGreetlByUserNum(userGreet, userNum);
    }

    @Override
    public User queryUserByEamil(String eamil) {
        return userRepository.queryUserByEamil(eamil);
    }
}
