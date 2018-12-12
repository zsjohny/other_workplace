package com.ouliao.repository.versionsecond;

import com.ouliao.domain.versionsecond.UserDisconery;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by nessary on 16-5-18.
 */
public interface UserDisconeryRepository extends Repository<UserDisconery, Long> {

    @Query(value = "select * from userdisconery   where  isDeleted='0'   and isContract='true'  order by creatTime desc limit :start,:pagecount ", nativeQuery = true)
    List<UserDisconery> queryUserDisconveryAll(@Param("start") Integer start, @Param("pagecount") Integer pageCount);


    @Query("from UserDisconery   where  isDeleted='0' and userId=:userId  ")
    UserDisconery queryUserDisconveryByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query("update UserDisconery set isContract='false' where isDeleted='0' and userId =:userId ")
    void cancelUserContractByUserId(@Param("userId") Integer userId);


    @Modifying
    @Query("update UserDisconery set isDeleted='1' where isDeleted='0' and userId =:userId ")
    void deleteUserDiscoveryByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query("update UserDisconery set classicProgram=:classicProgram , disPicUrls=:disPicUrls,creatTime=now() ,listenProgram=:listenProgram where isDeleted='0'  and isContract='true' and userId =:userId ")
    void updateUserDiscoveryByUserId(@Param("classicProgram") String classicProgram, @Param("disPicUrls") String disPicUrls, @Param("listenProgram") String listenProgram, @Param("userId") Integer userId);


}
