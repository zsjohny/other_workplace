package com.ouliao.dao.versionsecond;

import com.ouliao.domain.versionsecond.UserMissCallRecord;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by nessary on 16-5-14.
 */
public interface UserMissCallRecordDao {
    void createUserMissCallRecord(UserMissCallRecord userMissCallRecord);

    List<UserMissCallRecord> queryUserMissCallRecordByUserCallId(Integer start, Integer pageCount, String cid,Integer userCalledId);

    List<UserMissCallRecord> queryUserMissCallRecordByUserCalledId(Integer start, Integer pageCount, Integer userCalledId);

    void deleUserMissCallIsReadRecordById(List<Long> userMissCallRecordIds, Integer userCalledId);


    void deleteUserMissCallRecordByUserMissCallRecordId(List<Long> userMissCallRecordIds, Integer userCallId);
    List<UserMissCallRecord> queryUserMissCallRecordAllById(Integer userCallId, Integer userCalledId);
}
