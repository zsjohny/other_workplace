package com.ouliao.dao.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserVisitRecordDao;
import com.ouliao.domain.versionsecond.UserVisitRecord;
import com.ouliao.repository.versionsecond.UserVisitRecordPageRepository;
import com.ouliao.repository.versionsecond.UserVisitRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nessary on 16-5-17.
 */
@Repository

public class UserVisitRecordDaoImpl implements UserVisitRecordDao {
    @Autowired
    private UserVisitRecordPageRepository userVisitRecordPageRepository;
    @Autowired
    private UserVisitRecordRepository userVisitRecordRepository;

    @Override
    public void saveUserVisitRecord(UserVisitRecord userVisitRecord) {
        userVisitRecordPageRepository.saveAndFlush(userVisitRecord);
    }

    @Override
    public List<UserVisitRecord> findUserVisitRecordAllByVisitId(Integer start, Integer pagecount, Integer visitUserId) {
        return userVisitRecordRepository.findUserVisitRecordAllByVisitId((start - 1) * pagecount, pagecount, visitUserId);
    }

    @Override
    public void deleteUserVisitRecordById(List<Long> userVisitRecordId, Integer visitUserId) {
        userVisitRecordRepository.deleteUserVisitRecordById(userVisitRecordId, visitUserId);

    }

    @Override
    public void updateUserVisitRecordIsReadByVisitId(Integer visitUserId) {
        userVisitRecordRepository.updateUserVisitRecordIsReadByVisitId(visitUserId);
    }

    @Override
    public Integer findUserVisitRecordCountByVisitId(Integer visitUserId) {
        return userVisitRecordRepository.findUserVisitRecordCountByVisitId(visitUserId);
    }

    @Override
    public UserVisitRecord findUserVisitRecordfirstByVisitId(Integer visitUserId) {
        return userVisitRecordRepository.findUserVisitRecordfirstByVisitId(visitUserId);
    }


}
