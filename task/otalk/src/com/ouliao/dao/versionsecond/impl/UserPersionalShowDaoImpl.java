package com.ouliao.dao.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserPersionalShowDao;
import com.ouliao.domain.versionsecond.UserPersionalShow;
import com.ouliao.repository.versionsecond.UserPersionalShowPageRepository;
import com.ouliao.repository.versionsecond.UserPersionalShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nessary on 16-5-17.
 */
@Repository
public class UserPersionalShowDaoImpl implements UserPersionalShowDao {
    @Autowired
    private UserPersionalShowPageRepository userPersionalShowPageRepository;
    @Autowired
    private UserPersionalShowRepository userPersionalShowRepository;


    @Override
    public void createUserPersionalShow(UserPersionalShow userPersionalShow) {
        userPersionalShowPageRepository.saveAndFlush(userPersionalShow);
    }

    @Override
    public void deleteUserPersionalShowById(List<Long> userPersionalShowIds ,Integer userId) {
        userPersionalShowRepository.deleteUserPersionalShowById(userPersionalShowIds, userId);
    }

    @Override
    public List<UserPersionalShow> queryUserPersionalAllByUserId(Integer start, Integer pagecount, Integer userId) {
        return userPersionalShowRepository.queryUserPersionalAllByUserId((start - 1) * pagecount, pagecount, userId);
    }
}
