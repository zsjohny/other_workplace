package com.ouliao.repository.versionsecond;

import com.ouliao.domain.versionsecond.UserVisitRecord;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by nessary on 16-5-17.
 */
public interface UserVisitRecordRepository extends Repository<UserVisitRecord, Long> {

    @Query(value = "select * from uservisitrecord   where  isDeleted='0' and visitUserId=:visitUserId  order by creatTime desc limit :start,:pagecount ", nativeQuery = true)
    List<UserVisitRecord> findUserVisitRecordAllByVisitId(@Param("start") Integer start, @Param("pagecount") Integer pagecount, @Param("visitUserId") Integer visitUserId);

    @Modifying
    @Query("update UserVisitRecord set isDeleted='1' where userVisitRecordId in (:userVisitRecordId) and visitUserId =:visitUserId ")
    void deleteUserVisitRecordById(@Param("userVisitRecordId") List<Long> userVisitRecordId, @Param("visitUserId") Integer visitUserId);


    @Modifying
    @Query("update UserVisitRecord set isReader='true'  where   isDeleted='0' and   visitUserId =:visitUserId ")
    void updateUserVisitRecordIsReadByVisitId(@Param("visitUserId") Integer visitUserId);


    @Query("select count(*) from UserVisitRecord   where  isDeleted='0' and isReader='false' and visitUserId=:visitUserId  ")
    Integer findUserVisitRecordCountByVisitId(@Param("visitUserId") Integer visitUserId);

    @Query(value = "select * from uservisitrecord   where  isDeleted='0'   and isReader='false' and visitUserId=:visitUserId  order by creatTime desc limit 0,1 ", nativeQuery = true)
    UserVisitRecord findUserVisitRecordfirstByVisitId(@Param("visitUserId") Integer visitUserId);

}
