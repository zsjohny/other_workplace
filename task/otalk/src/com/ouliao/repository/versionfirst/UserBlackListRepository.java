/**
 *
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserBlackList;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author xiaoluo
 * @version $Id: UserBlackListRepository.java, 2016年2月23日 下午6:10:48
 */
@RepositoryDefinition(domainClass = UserBlackList.class, idClass = Integer.class)
public interface UserBlackListRepository {
    @Modifying
    @Query("update UserBlackList set isDeleted =:isDeleted where  userBlackListId =:userBlackListId")
    int updateUserBlackListByUserBlackListId(@Param("isDeleted") String isDeleted,
                                             @Param("userBlackListId") Integer userBlackListId);

    @Query("from UserBlackList  where userId =:userId and userBlackId=:userBlackId ")
    UserBlackList queryUserIsBlackListById(@Param("userId") Integer userId, @Param("userBlackId") Integer userBlackId);

//	@Query("select count(userBlackListId)  from UserBlackList  where userId =:userId and isDeleted='0' ")
//	Integer queryBlackListCountByUserId(@Param("userId") Integer userId);

    @Query(value = "select count(userBlackListId)  from userblacklist  where userId =:userId and isDeleted='0' ", nativeQuery = true)
    Integer queryBlackListCountByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query("update UserBlackList set isDeleted ='1' where   userId =:userId  and userBlackId in :ids")
    int updateUserBlackListIsDeletedAllByUserBlackByIds(@Param("userId") Integer userId,
                                                        @Param("ids") List<Integer> ids);
}
