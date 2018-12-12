/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserReflect;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserReflectRepository.java, 2016年2月26日 下午1:44:55
 */
@RepositoryDefinition(domainClass = UserReflect.class, idClass = Integer.class)
public interface UserReflectRepository {
	@Modifying
	@Query("update UserReflect set isDeleted='1' where userReflectId in(:ids) ")
	int updateIsDeletedByUserReflectId(@Param("ids") List<Integer> ids);

//	@Query("select count(userId) from UserReflect  where isDeleted='0' ")
//	Long queryCountByIsDeleted();


	@Query(value = "select count(userId) from userreflect  where isDeleted='0' ",nativeQuery = true)
	Long queryCountByIsDeleted();

	@Query(" from UserReflect  where isDeleted='0' ")
	List<UserReflect> queryUserReflectAllByIsDeleted();

	@Query(" from UserReflect  where isDeleted='1' and userId=:userId")
	List<UserReflect> queryUserReflectWithDrawAllByUserId(@Param("userId") Integer userId);

}
