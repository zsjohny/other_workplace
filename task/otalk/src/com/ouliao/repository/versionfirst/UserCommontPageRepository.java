/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserCommont;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserCommontPageRepository.java, 2016年2月20日 上午10:57:35
 */
public interface UserCommontPageRepository
		extends JpaRepository<UserCommont, Integer>, JpaSpecificationExecutor<UserCommont> {

}
