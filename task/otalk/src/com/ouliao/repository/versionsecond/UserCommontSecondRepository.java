package com.ouliao.repository.versionsecond;

import com.ouliao.domain.versionfirst.UserCommont;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by nessary on 16-5-17.
 */
public interface UserCommontSecondRepository extends Repository<UserCommont, Integer> {

    @Query(value = "select  * from userCommont  where  isDeleted='0'  and userContractId=:userContractId  order by userCreateTime desc  limit :startCount,:pageSize", nativeQuery = true)
    List<UserCommont> queryUserCommontAllByUserId(@Param("startCount") Integer startCount,
                                                  @Param("pageSize") Integer pageSize, @Param("userContractId") Integer userContractId);

    @Modifying
    @Query("update UserCommont set isReader='true'  where   isDeleted='0' and   userContractId =:userContractId ")
    void updateUserCommonttIsReadByUserContractId(@Param("userContractId") Integer userContractId);

    @Query("select count(*)   from  UserCommont where   isDeleted='0' and  isReader ='false' and  userContractId =:userContractId ")
    Integer queryUserCommontCountByUserContractId(@Param("userContractId") Integer userContractId);


}