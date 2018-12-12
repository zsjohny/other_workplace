package com.ouliao.service.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserSayContentSecondDao;
import com.ouliao.domain.versionfirst.UserCommont;
import com.ouliao.domain.versionfirst.UserSayContent;
import com.ouliao.domain.versionfirst.UserSupportSay;
import com.ouliao.service.versionsecond.UserSayContentSecondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by nessary on 16-5-14.
 */
@Transactional
@Service
public class UserSayContentServiceImpl implements UserSayContentSecondService {

    @Autowired
    private UserSayContentSecondDao userSayContentSecondDao;

    @Override
    public List<UserSayContent> queryUserSayContentBySubjectOrContent(Integer startCount, Integer pageSize, String word, Integer userId) {
        return userSayContentSecondDao.queryUserSayContentBySubjectOrContent((startCount - 1) * pageSize, pageSize, word, userId);
    }

    @Override
    public List<UserSupportSay> querySupporAllByUserId(Integer startCount,
                                                       Integer pageSize, Integer userSupportId) {
        return userSayContentSecondDao.querySupporAllByUserId((startCount - 1) * pageSize, pageSize, userSupportId);
    }

    @Override
    public UserSayContent queryUserSayContentByUserSayContentId(Integer userSayContentId) {
        return userSayContentSecondDao.queryUserSayContentByUserSayContentId(userSayContentId);
    }

    @Override
    public List<UserCommont> queryUserCommontAllByUserId(Integer startCount, Integer pageSize, Integer userContractId) {
        return userSayContentSecondDao.queryUserCommontAllByUserId((startCount - 1) * pageSize, pageSize, userContractId);
    }

    @Override
    public List<UserSupportSay> querySupporAllByUserSayContentId(Integer startCount, Integer pageSize, Integer userSayContentId) {
        return userSayContentSecondDao.querySupporAllByUserSayContentId((startCount - 1) * pageSize, pageSize, userSayContentId);
    }

    @Override
    public void updateUserSupportIsReadByUserSupportId(Integer userSupportId) {
        userSayContentSecondDao.updateUserSupportIsReadByUserSupportId(userSupportId);
    }

    @Override
    public void updateUserCommonttIsReadByUserContractId(Integer userContractId) {
        userSayContentSecondDao.updateUserCommonttIsReadByUserContractId(userContractId);
    }

    @Override
    public Map<String, Integer> queryNewSupportCommontAndSupportByUserId(Integer userId) {
        return userSayContentSecondDao.queryNewSupportCommontAndSupportByUserId(userId);
    }
}
