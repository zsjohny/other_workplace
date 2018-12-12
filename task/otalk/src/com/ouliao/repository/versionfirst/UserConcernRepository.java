/**
 *
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserConcern;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author xiaoluo
 * @version $Id: UserConcernRepository.java, 2016年2月23日 下午10:02:33
 */
@RepositoryDefinition(domainClass = UserConcern.class, idClass = Integer.class)
public interface UserConcernRepository {
    @Modifying
    @Query("update UserConcern set isDeleted =:isDeleted ,userModifyTime=now() where  userConcernId =:userConcernId")
    int updateUserConcernByUserConcernId(@Param("isDeleted") String isDeleted,
                                         @Param("userConcernId") Integer userConcernId);

//    @Query("from UserConcern  where userId =:userId and userOnfocusId=:userOnfocusId ")
//    UserConcern queryUserIsConcernById(@Param("userId") Integer userId, @Param("userOnfocusId") Integer userOnfocusId);

    @Query("from UserConcern  where userId =:userId and userOnfocusId=:userOnfocusId ")
    UserConcern queryUserIsConcernById(@Param("userId") Integer userId, @Param("userOnfocusId") Integer userOnfocusId);


//    @Query("select count(userConcernId) from UserConcern where isDeleted='0' and userId =:userId")
//    Integer queryUserConcernByUserId(@Param("userId") Integer userId);
//
//    @Query("select count(userConcernId) from UserConcern where isDeleted='0' and userOnfocusId =:userOnfocusId")
//    Integer queryUserConcernedByUserOnfocusId(@Param("userOnfocusId") Integer userOnfocusId);

    @Query(value = "select count(userConcernId) from userconcern where isDeleted='0' and userId =:userId", nativeQuery = true)
    Integer queryUserConcernByUserId(@Param("userId") Integer userId);

    @Query(value = "select count(userConcernId) from userconcern where isDeleted='0' and userOnfocusId =:userOnfocusId", nativeQuery = true)
    Integer queryUserConcernedByUserOnfocusId(@Param("userOnfocusId") Integer userOnfocusId);


    // select userId from userconcern where isDeleted='0' group by userId order
    // by count(*) desc
    @Query(value = "select userOnfocusId from userconcern where isDeleted='0' and userConract='true' group by userOnfocusId order  by count(*) desc limit :startPage,:pageSize", nativeQuery = true)
    List<Integer> queryUserConcerndByOrder(@Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize);

    @Query("from UserConcern where isDeleted='0' and userId=?1 ")
    List<UserConcern> queryUserConcerndAllByUserId(Integer userId);

    @Query("from UserConcern where isDeleted='0' and userOnfocusId=?1 ")
    List<UserConcern> queryUserConcerndedAllByUserId(Integer useredId);

    @Query(value = "select * from userconcern where isDeleted='0' and userOnfocusId =:userOnfocusId  order by userConcernId limit :startPage,:pageSize", nativeQuery = true)
    List<UserConcern> queryUserConcernedByUserConcernId(@Param("userOnfocusId") Integer userOnfocusId,
                                                        @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize);

    @Modifying
    @Query("update  UserConcern  set userConract =:userConract where isDeleted='0' and userOnfocusId=:userOnfocusId")
    int updateUserContractByUserOnfocuseId(@Param("userConract") String userConract, @Param("userOnfocusId") Integer userOnfocusId);


    //2016年5月活动持续一个月
    @Query(value = " select * from userconcern where isDeleted='0' and userOnfocusId =:userOnfocusId", nativeQuery = true)
    List<UserConcern> queryUserConcerneAlldByUserOnfocusId(@Param("userOnfocusId") Integer userOnfocusId);


    @Query(value = "select *  from userconcern where  userId=:userId order by  userCreateTime asc", nativeQuery = true)
    List<UserConcern> queryUserConcerneAlldByUserConcernId(@Param("userId") Integer userId);
}
