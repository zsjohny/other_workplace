/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserConcern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserConcernRepositoryPageRepository.java, 2016年2月23日 下午10:02:58
 */

public interface UserConcernPageRepository
		extends JpaRepository<UserConcern, Integer>, JpaSpecificationExecutor<UserConcern> {

}
