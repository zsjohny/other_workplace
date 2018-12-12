package com.ouliao.repository.versionsecond;

import com.ouliao.domain.versionsecond.UserSureMsgCount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;


/**
 * Created by nessary on 16-5-19.
 */
public interface UserSureMsgCountRepository extends Repository<UserSureMsgCount, Long> {


    @Query("select  readCount from UserSureMsgCount   where  isDeleted='0'    and userId=:userId  ")
    Integer findUserSureMsgCountByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query("update UserSureMsgCount set readCount=:readCount where userId =:userId and isDeleted='0'    ")
    void updateUSerSureMsgCountByUserId(@Param("readCount") Integer readCount, @Param("userId") Integer userId);

}


