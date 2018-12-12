package com.ouliao.service.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserPersionalShowDao;
import com.ouliao.domain.versionsecond.UserPersionalShow;
import com.ouliao.service.versionsecond.UserPersionalShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by nessary on 16-5-17.
 */
@Service
@Transactional
public class UserPersionalServiceImpl implements UserPersionalShowService {
    @Autowired
    private UserPersionalShowDao userPersionalShowDao;

    @Override
    public void createUserPersionalShow(UserPersionalShow userPersionalShow) {
        userPersionalShowDao.createUserPersionalShow(userPersionalShow);
    }

    @Override
    public void deleteUserPersionalShowById(List<Long> userPersionalShowIds, Integer userId) {
        userPersionalShowDao.deleteUserPersionalShowById(userPersionalShowIds, userId);
    }

    @Override
    public List<UserPersionalShow> queryUserPersionalAllByUserId(Integer start, Integer pagecount, Integer userId) {
        return userPersionalShowDao.queryUserPersionalAllByUserId(start, pagecount, userId);
    }
}
