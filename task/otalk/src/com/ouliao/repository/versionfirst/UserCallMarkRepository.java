/**
 *
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserCallMark;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author xiaoluo
 * @version $Id: UserCallMarkRepository.java, 2016年2月27日 上午7:29:26
 */
@RepositoryDefinition(domainClass = UserCallMark.class, idClass = Integer.class)
public interface UserCallMarkRepository {
    @Modifying
    @Query("update UserCallMark set isDeleted ='1',userCallTime=:userCallTime,userCallCost=:userCallCost,userCallEarn=:userCallEarn where  userCallMarkId =:userCallMarkId")
    int updateUserCallMarkIsDeletedByUserCallMarkId(@Param("userCallMarkId") Integer userCallMarkId,
                                                    @Param("userCallCost") Double userCallCost, @Param("userCallTime") String userCallTime,
                                                    @Param("userCallEarn") Double userCallEarn);

    @Modifying
    @Query("update UserCallMark set isScore ='true'where  userCallMarkId =:userCallMarkId")
    int updateUserCallMarkIsScoreByUserCallMarkId(@Param("userCallMarkId") Integer userCallMarkId);

    @Query("from UserCallMark where  userCalledId =:userCalledId  and isDeleted ='0' and userId=:userId ")
    UserCallMark queryUserCallMarkIsDeletedById(@Param("userCalledId") Integer userCalledId,
                                                @Param("userId") Integer userId);

    @Query("from UserCallMark where  userCalledId =:userCalledId  and isDeleted ='0' and userId=:userId ")
    List<UserCallMark> queryUserCallMarkIsDeletedAllById(@Param("userCalledId") Integer userCalledId,
                                                         @Param("userId") Integer userId);

    @Query("from UserCallMark where  userCallMarkId =:userCallMarkId  ")
    UserCallMark queryUserCallMarkByUserCallMarkId(@Param("userCallMarkId") Integer userCallMarkId);

    @Query("from UserCallMark where  userCallMarkId =:userCallMarkId  ")
    List<UserCallMark> queryUserCallMarkAllByUserCallMarkId(@Param("userCallMarkId") Integer userCallMarkId);


    @Modifying
    @Query(value = "insert into usercallmark (userCalledId,userCreateTime,userId,isDeleted) values(:userCalledId,:userCreateTime,:userId,'0')", nativeQuery = true)
    void createUserCallMark(@Param("userCalledId") Integer userCalledId, @Param("userCreateTime") Date userCreateTime, @Param("userId") Integer userId);


}
