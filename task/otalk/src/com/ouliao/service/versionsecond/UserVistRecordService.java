package com.ouliao.service.versionsecond;

import com.ouliao.domain.versionsecond.UserVisitRecord;

import java.util.List;

/**
 * Created by nessary on 16-5-17.
 */
public interface UserVistRecordService {
    void saveUserVisitRecord(UserVisitRecord userVisitRecord);

    List<UserVisitRecord> findUserVisitRecordAllByVisitId(Integer start, Integer pagecount, Integer visitUserId);

    void deleteUserVisitRecordById(List<Long> userVisitRecordId, Integer visitUserId);

    void updateUserVisitRecordIsReadByVisitId(Integer visitUserId);


    Integer findUserVisitRecordCountByVisitId(Integer visitUserId);

    UserVisitRecord findUserVisitRecordfirstByVisitId(Integer visitUserId);

}
