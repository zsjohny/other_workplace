/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserReflect;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserReflectRepository.java, 2016年2月26日 下午1:24:24
 */

public interface UserReflectCrudRepository
 extends JpaRepository<UserReflect, Integer> {

}
