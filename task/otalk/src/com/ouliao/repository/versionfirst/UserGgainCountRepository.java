/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserGainCount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserGgainCountRepository.java, 2016年2月18日 下午11:06:12
 */
@RepositoryDefinition(domainClass = UserGainCount.class, idClass = Integer.class)

public interface UserGgainCountRepository {
	@Query("from UserGainCount  where  isDeleted='0'  and userRealIp =:userRealIp")
	UserGainCount queryUserByIp(@Param("userRealIp") String userRealIp);

	@Modifying
	@Query("update UserGainCount set userGainCount =:userGainCount  where  isDeleted='0'  and userRealIp =:userRealIp")
	void updateCountByIp(@Param("userGainCount") Integer userGainCount, @Param("userRealIp") String userRealIp);

}
