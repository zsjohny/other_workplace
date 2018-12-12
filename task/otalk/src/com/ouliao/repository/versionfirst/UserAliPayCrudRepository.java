/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserAliPay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserAliPayCrudRepository.java, 2016年2月25日 下午9:16:41
 */

public interface UserAliPayCrudRepository
		extends JpaRepository<UserAliPay, Long>, JpaSpecificationExecutor<UserAliPay> {

}
