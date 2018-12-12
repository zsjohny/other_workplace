/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserSayContent;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserSayRepository.java, 2016年2月19日 下午7:50:25
 */
@RepositoryDefinition(domainClass = UserSayContent.class, idClass = Integer.class)

public interface UserSayContentRepository extends JpaSpecificationExecutor<UserSayContent> {
	@Modifying
	@Query("update UserSayContent set isDeleted ='1'  where userId =:userId and userSayContentId=:userSayContentId")
	int deleteUserSayContentByUserId(@Param("userId") Integer userId,
									 @Param("userSayContentId") Integer userSayContentId);

	@Query("from UserSayContent  where  isDeleted='0'  and userId =:userId  order by userCreateTime desc")
	List<UserSayContent> querySayContentByUserId(@Param("userId") Integer userId);

	@Query("from UserSayContent  where  isDeleted='0'  and userSayContentId =:userSayContentId")
	UserSayContent querySayContentByUserSayContentId(@Param("userSayContentId") Integer userSayContentId);

	@Query("from UserSayContent  where  isDeleted='0'  and userId=:userId and  userSayContentId =:userSayContentId")
	UserSayContent querySayContentUniqueById(@Param("userId") Integer userId,
											 @Param("userSayContentId") Integer userSayContentId);

	// 进行数据转移的时候
	@Query("from UserSayContent  where  isDeleted='0'  and id=:id ")
	UserSayContent querySayContentById(@Param("id") Integer id);

}
