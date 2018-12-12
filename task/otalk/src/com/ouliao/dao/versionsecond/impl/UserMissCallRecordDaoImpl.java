package com.ouliao.dao.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserMissCallRecordDao;
import com.ouliao.domain.versionsecond.UserMissCallRecord;
import com.ouliao.repository.versionsecond.UserMissCallRecordCrudRepository;
import com.ouliao.repository.versionsecond.UserMissCallRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nessary on 16-5-14.
 */
@Repository
public class UserMissCallRecordDaoImpl implements UserMissCallRecordDao {
    @Autowired
    private UserMissCallRecordCrudRepository userMissCallRecordCrudRepository;
    @Autowired
    private UserMissCallRecordRepository userMissCallRecordRepository;


    @Override
    public void createUserMissCallRecord(UserMissCallRecord userMissCallRecord) {
        userMissCallRecordCrudRepository.saveAndFlush(userMissCallRecord);
    }

    @Override
    public List<UserMissCallRecord> queryUserMissCallRecordByUserCallId(Integer start, Integer pageCount, String cid, Integer userCalledId) {
        return userMissCallRecordRepository.queryUserMissCallRecordByUserCallId((start - 1) * pageCount, pageCount, cid, userCalledId);
    }

    @Override
    public List<UserMissCallRecord> queryUserMissCallRecordByUserCalledId(Integer start, Integer pageCount, Integer userCalledId) {
        return userMissCallRecordRepository.queryUserMissCallRecordByUserCalledId((start - 1) * pageCount, pageCount, userCalledId);
    }

    @Override
    public void deleUserMissCallIsReadRecordById(List<Long> userMissCallRecordIds, Integer userCalledId) {
        userMissCallRecordRepository.deleUserMissCallIsReadRecordById(userMissCallRecordIds, userCalledId);
    }

    @Override
    public void deleteUserMissCallRecordByUserMissCallRecordId(List<Long> userMissCallRecordIds, Integer userCallId) {
        userMissCallRecordRepository.deleUserMissCallRecordById(userMissCallRecordIds, userCallId);
    }

    @Override
    public List<UserMissCallRecord> queryUserMissCallRecordAllById(Integer userCallId, Integer userCalledId) {
        return userMissCallRecordRepository.queryUserMissCallRecordAllById(userCallId, userCalledId);
    }
}
