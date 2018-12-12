/**
 *
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserSupportSay;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 *
 * @author xiaoluo
 * @version $Id: UserSupportSayRepository.java, 2016年2月19日 下午9:22:48
 */
@RepositoryDefinition(domainClass = UserSupportSay.class, idClass = Integer.class)

public interface UserSupportSayRepository {
	@Modifying
	@Query("update UserSupportSay set isDeleted =:isDeleted  where userId =:userId and userSayContentId=:userSayContentId")
	void updateSupportSayContentById(@Param("isDeleted") String isDeleted, @Param("userId") Integer userId,
									 @Param("userSayContentId") Integer userSayContentId);

	@Query("from UserSupportSay  where  isDeleted='0'  and userSayContentId=:userSayContentId")
	List<UserSupportSay> querySupporIsDeletedByUserId(@Param("userSayContentId") Integer userSayContentId);

	@Query("from UserSupportSay  where  userId =:userId and userSayContentId=:userSayContentId")
	UserSupportSay querySupportUniqueExpecIsDeletedById(@Param("userId") Integer userId,
														@Param("userSayContentId") Integer userSayContentId);

	@Query("from UserSupportSay  where   isDeleted='0'  and userId =:userId and userSayContentId=:userSayContentId")
	UserSupportSay querySupportUniqueById(@Param("userId") Integer userId,
										  @Param("userSayContentId") Integer userSayContentId);

//	@Query("select count(userId) from UserSupportSay  where   isDeleted='0'  and userSayContentId=:userSayContentId")
//	Integer querySupportCountById(@Param("userSayContentId") Integer userSayContentId);

	@Query(value = "select count(userId) from usersupportsay  where   isDeleted='0'  and userSayContentId=:userSayContentId",nativeQuery = true)
	Integer querySupportCountById(@Param("userSayContentId") Integer userSayContentId);
}
