/**
 *
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserCommont;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author xiaoluo
 * @version $Id: UserCommontRepository.java, 2016年2月19日 下午6:41:07
 */
@RepositoryDefinition(domainClass = UserCommont.class, idClass = Integer.class)
public interface UserCommontRepository {

    // @Modifying
    // @Query("update UserCommont set isDeleted ='1' where ( userCommontId
    // =:userCommontId and userId =:userId ) OR (userSayContentId =:ownerId )")
    // int deleteCommontById(@Param("userCommontId") Integer userCommontId,
    // @Param("userId") Integer userId,
    // @Param("ownerId") Integer ownerId);
    @Modifying
    @Query(value = "update usercommont set isDeleted ='1' where ( userCommontId =:userCommontId and userId =:userId ) OR (userSayContentId =:ownerId  and userCommontId =:userCommontId)", nativeQuery = true)
    int deleteCommontById(@Param("userCommontId") Integer userCommontId, @Param("userId") Integer userId,
                          @Param("ownerId") Integer ownerId);

    @Query("from UserCommont where isDeleted='0' and userCommontId =:userCommontId")
    UserCommont querySayCommontOneByUserCommontId(@Param("userCommontId") Integer userCommontId);

    @Query("from UserCommont where  userSayContentId =:userSayContentId")
    List<UserCommont> querySayCommontAllByUserSayContentId(@Param("userSayContentId") Integer userSayContentId);

//	@Query("select count(userCommontId) from UserCommont  where   isDeleted='0'  and userSayContentId=:userSayContentId")
//	Integer querySayCommontCountCountByUserSayContentId(@Param("userSayContentId") Integer userSayContentId);

    @Query(value = "select count(userCommontId) from usercommont  where   isDeleted='0'  and userSayContentId=:userSayContentId", nativeQuery = true)
    Integer querySayCommontCountCountByUserSayContentId(@Param("userSayContentId") Integer userSayContentId);

}