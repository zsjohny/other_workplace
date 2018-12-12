/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserReflect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserReflectPageRepository.java, 2016年3月7日 下午5:43:05
 */

public interface UserReflectPageRepository
		extends JpaRepository<UserReflect, Integer>, JpaSpecificationExecutor<UserReflect> {

}
