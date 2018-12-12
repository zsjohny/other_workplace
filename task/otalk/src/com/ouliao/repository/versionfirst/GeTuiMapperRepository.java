/**
 *
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.GeTuiMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

/**
 *
 *
 * @author xiaoluo
 * @version $Id: GeTuiMapperRepository.java, 2016年2月29日 下午10:43:19
 */
@RepositoryDefinition(domainClass = GeTuiMapper.class, idClass = Integer.class)
public interface GeTuiMapperRepository {
	@Query("from GeTuiMapper where clientId=?1  ")
	GeTuiMapper queryIsExist(String clientId);

	@Query("from GeTuiMapper where userId=?1  ")
	GeTuiMapper queryGeiTuiByUserId(Integer userId);

	@Query("from GeTuiMapper where clientId=?1  ")
	List<GeTuiMapper> queryAllIsExist(String clientId);

	@Query("from GeTuiMapper where userId=?1  ")
	List<GeTuiMapper> queryGeiTuiAllByUserId(Integer userId);

}
