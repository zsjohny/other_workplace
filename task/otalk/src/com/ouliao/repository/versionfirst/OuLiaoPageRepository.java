/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: OuLiaoPageRepository.java, 2016年2月24日 下午3:56:20
 */

public interface OuLiaoPageRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

}
