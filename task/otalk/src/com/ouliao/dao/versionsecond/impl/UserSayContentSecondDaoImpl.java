package com.ouliao.dao.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserSayContentSecondDao;
import com.ouliao.domain.versionfirst.UserCommont;
import com.ouliao.domain.versionfirst.UserSayContent;
import com.ouliao.domain.versionfirst.UserSupportSay;
import com.ouliao.repository.versionsecond.UserCommontSecondRepository;
import com.ouliao.repository.versionsecond.UserSayContentSeconRepository;
import com.ouliao.repository.versionsecond.UserSupportSaySecondRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nessary on 16-5-14.
 */
@Repository
public class UserSayContentSecondDaoImpl implements UserSayContentSecondDao {
    @Autowired
    private UserSayContentSeconRepository userSayContentSeconRepository;
    @Autowired
    private UserSupportSaySecondRepository userSupportSaySecondRepository;
    @Autowired
    private UserCommontSecondRepository userCommontSecondRepository;

    @Override
    public List<UserSayContent> queryUserSayContentBySubjectOrContent(Integer startCount, Integer pageSize, String word, Integer userId) {
        return userSayContentSeconRepository.queryUserSayContentBySubjectOrContent(startCount, pageSize, word, userId);
    }

    @Override
    public List<UserSupportSay> querySupporAllByUserId(Integer startCount,
                                                       Integer pageSize, Integer userSupportId) {
        return userSupportSaySecondRepository.querySupporAllByUserId(startCount, pageSize, userSupportId);
    }

    @Override
    public UserSayContent queryUserSayContentByUserSayContentId(Integer userSayContentId) {
        return userSayContentSeconRepository.queryUserSayContentByUserSayContentId(userSayContentId);
    }

    @Override
    public List<UserCommont> queryUserCommontAllByUserId(Integer startCount,  Integer pageSize, Integer userContractId) {
        return userCommontSecondRepository.queryUserCommontAllByUserId(startCount, pageSize, userContractId);
    }

    @Override
    public List<UserSupportSay> querySupporAllByUserSayContentId(Integer startCount, Integer pageSize, Integer userSayContentId) {
        return userSupportSaySecondRepository.querySupporAllByUserSayContentId(startCount, pageSize, userSayContentId);
    }

    @Override
    public void updateUserSupportIsReadByUserSupportId(Integer userSupportId) {
        userSupportSaySecondRepository.updateUserSupportIsReadByUserSupportId(userSupportId);
    }

    @Override
    public void updateUserCommonttIsReadByUserContractId(Integer userContractId) {
        userCommontSecondRepository.updateUserCommonttIsReadByUserContractId(userContractId);
    }

    @Override
    public Map<String, Integer> queryNewSupportCommontAndSupportByUserId(Integer userId) {
        Map<String, Integer> map = new HashMap<>();


        Integer count = userCommontSecondRepository.queryUserCommontCountByUserContractId(userId);

        map.put("commont", count == null ? 0 : count);
        count = userSupportSaySecondRepository.queryUserSupportCountByUserSupportId(userId);

        map.put("support", count == null ? 0 : count);

        return map;
    }

}
