package com.ouliao.service.versionsecond.impl;

import com.ouliao.dao.versionsecond.UserVisitRecordDao;
import com.ouliao.domain.versionsecond.UserVisitRecord;
import com.ouliao.service.versionsecond.UserVistRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by nessary on 16-5-17.
 */
@Transactional
@Service
public class UserVistRecordServiceImpl implements UserVistRecordService {
    @Autowired
    private UserVisitRecordDao userVisitRecordDao;

    @Override
    public void saveUserVisitRecord(UserVisitRecord userVisitRecord) {
        userVisitRecordDao.saveUserVisitRecord(userVisitRecord);
    }

    @Override
    public List<UserVisitRecord> findUserVisitRecordAllByVisitId(Integer start, Integer pagecount, Integer visitUserId) {
        return userVisitRecordDao.findUserVisitRecordAllByVisitId(start, pagecount, visitUserId);
    }

    @Override
    public void deleteUserVisitRecordById(List<Long> userVisitRecordId, Integer visitUserId) {
        userVisitRecordDao.deleteUserVisitRecordById(userVisitRecordId, visitUserId);

    }

    @Override
    public void updateUserVisitRecordIsReadByVisitId(Integer visitUserId) {
        userVisitRecordDao.updateUserVisitRecordIsReadByVisitId(visitUserId);
    }

    @Override
    public Integer findUserVisitRecordCountByVisitId(Integer visitUserId) {
        return userVisitRecordDao.findUserVisitRecordCountByVisitId(visitUserId);
    }

    @Override
    public UserVisitRecord findUserVisitRecordfirstByVisitId(Integer visitUserId) {
        return userVisitRecordDao.findUserVisitRecordfirstByVisitId(visitUserId);
    }
}
