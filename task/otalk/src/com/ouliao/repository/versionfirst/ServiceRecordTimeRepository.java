/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.ServiceRecordTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: ServiceRecordTimeRepository.java, 2016年3月10日 下午5:22:19
 */
@RepositoryDefinition(domainClass = ServiceRecordTime.class, idClass = Integer.class)
public interface ServiceRecordTimeRepository {
	@Modifying
	@Query("update ServiceRecordTime set userCallTime=:userCallTime where userId =:userId")
	int updateUserCallTimeByUserId(@Param("userCallTime") Long userCallTime, @Param("userId") Integer userId);

	@Modifying
	@Query("update ServiceRecordTime set isSysSend='false' where userId =:userId")
	int deleteSysSendByUserId(@Param("userId") Integer userId);

	@Query("from ServiceRecordTime  where  userId=:userId ")
	ServiceRecordTime queryUserRecordIsExistByUserId(@Param("userId") Integer userId);

}
