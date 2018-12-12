package com.ouliao.repository.versionsecond;

import com.ouliao.domain.versionsecond.UserMissCallRecord;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by nessary on 16-5-14.
 */
public interface UserMissCallRecordRepository extends Repository<UserMissCallRecord, Long> {

    @Query(value = "select * from usermisscallrecord  where  isDeleted='0'  and isRead='0' and cid=:cid and   userCalledId=:userCalledId limit :startCount,:pageSize order by creatTime desc ", nativeQuery = true)
    List<UserMissCallRecord> queryUserMissCallRecordByUserCallId(@Param("startCount") Integer startCount,
                                                                 @Param("pageSize") Integer pageSize, @Param("cid") String cid, @Param("userCalledId") Integer userCalledId);

    @Query(value = "select * from usermisscallrecord  where  isDeleted='0'  and   userCalledId=:userCalledId limit :startCount,:pageSize", nativeQuery = true)
    List<UserMissCallRecord> queryUserMissCallRecordByUserCalledId(@Param("startCount") Integer startCount,
                                                                   @Param("pageSize") Integer pageSize, @Param("userCalledId") Integer userCalledId);

    @Modifying
    @Query("update UserMissCallRecord  set  isDeleted='1' where  userMissCallRecordId in :userMissCallRecordId and   userCalledId=:userCalledId ")
    void deleUserMissCallRecordById(@Param("userMissCallRecordId") List<Long> userMissCallRecordIds, @Param("userCalledId") Integer userCalledId);

    @Modifying
    @Query("update UserMissCallRecord  set  isRead='1' where  userMissCallRecordId in :userMissCallRecordId and   userCalledId=:userCalledId ")
    void deleUserMissCallIsReadRecordById(@Param("userMissCallRecordId") List<Long> userMissCallRecordIds, @Param("userCalledId") Integer userCalledId);


    @Query(" from UserMissCallRecord  where  isDeleted='0'  and   userCallId=:userCallId and  userCalledId=:userCalledId")
    List<UserMissCallRecord> queryUserMissCallRecordAllById(@Param("userCallId") Integer userCallId, @Param("userCalledId") Integer userCalledId);


}
