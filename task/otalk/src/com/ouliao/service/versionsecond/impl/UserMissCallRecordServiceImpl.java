package com.ouliao.service.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserMissCallRecordDao;
import com.ouliao.domain.versionsecond.UserMissCallRecord;
import com.ouliao.service.versionsecond.UserMissCallRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by nessary on 16-5-14.
 */
@Service
@Transactional
public class UserMissCallRecordServiceImpl implements UserMissCallRecordService {
    @Autowired
    private UserMissCallRecordDao userMissCallRecordDao;

    @Override
    public void createUserMissCallRecord(UserMissCallRecord userMissCallRecord) {
        userMissCallRecordDao.createUserMissCallRecord(userMissCallRecord);
    }

    @Override
    public List<UserMissCallRecord> queryUserMissCallRecordByUserCallId(Integer start, Integer pageCount, String cid, Integer userCalledId) {
        return userMissCallRecordDao.queryUserMissCallRecordByUserCallId(start, pageCount, cid, userCalledId);
    }

    @Override
    public List<UserMissCallRecord> queryUserMissCallRecordByUserCalledId(Integer start, Integer pageCount, Integer userCalledId) {
        return userMissCallRecordDao.queryUserMissCallRecordByUserCalledId(start, pageCount, userCalledId);
    }

    @Override
    public void deleUserMissCallIsReadRecordById(List<Long> userMissCallRecordIds, Integer userCalledId) {
        userMissCallRecordDao.deleUserMissCallIsReadRecordById(userMissCallRecordIds, userCalledId);
    }

    @Override
    public void deleteUserMissCallRecordByUserMissCallRecordId(List<Long> userMissCallRecordIds, Integer userCallId) {
        userMissCallRecordDao.deleteUserMissCallRecordByUserMissCallRecordId(userMissCallRecordIds, userCallId);
    }

    @Override
    public List<UserMissCallRecord> queryUserMissCallRecordAllById(Integer userCallId, Integer userCalledId) {
        return userMissCallRecordDao.queryUserMissCallRecordAllById(userCallId, userCalledId);
    }
}
