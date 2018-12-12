/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.SysMsgShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: SysMsgShowPageRepository.java, 2016年3月13日 下午6:57:40
 */

public interface SysMsgShowRepository extends JpaRepository<SysMsgShow, Long> {

	@Query("select count(*) from SysMsgShow where userId =:userId")
	Long queryCountBySysMsgByUserId(@Param("userId") Integer userId);

}
