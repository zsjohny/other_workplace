/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserCallMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserCallMarkCrudRepository.java, 2016年2月27日 上午7:29:58
 */

public interface UserCallMarkPageRepository
		extends JpaRepository<UserCallMark, Integer>, JpaSpecificationExecutor<UserCallMark> {

}
