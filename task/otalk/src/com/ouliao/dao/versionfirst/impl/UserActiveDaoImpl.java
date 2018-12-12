package com.ouliao.dao.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserActiveDao;
import com.ouliao.domain.versionfirst.UserActive;
import com.ouliao.repository.versionfirst.UserActiveCrudRepository;
import com.ouliao.repository.versionfirst.UserActiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nessary on 16-5-10.
 */
@Repository
public class UserActiveDaoImpl implements UserActiveDao {
    @Autowired
    private UserActiveRepository userActiveRepository;

    @Autowired
    private UserActiveCrudRepository userActiveCrudRepository;

    @Override

    public void saveUserCountByUserActive(UserActive userActive) {
        userActiveCrudRepository.save(userActive);
    }

    @Override
    public void updateUserCountByUserId(Integer userCount, Integer userId) {
        userActiveRepository.updateUserCountByUserId(userCount, userId);
    }

    @Override
    public List<UserActive> queryUserActiveAllByUserId(Integer userId) {
        return userActiveRepository.queryUserActiveAllByUserId(userId);
    }
}
