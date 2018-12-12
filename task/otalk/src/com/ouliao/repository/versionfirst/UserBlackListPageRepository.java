/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserBlackListPageRepository.java, 2016年2月23日 下午8:02:27
 */

public interface UserBlackListPageRepository
		extends JpaRepository<UserBlackList, Integer>, JpaSpecificationExecutor<UserBlackList> {

}
