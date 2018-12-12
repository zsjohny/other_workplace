/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.SysMsgShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: SysMsgShowPageRepository.java, 2016年3月13日 下午6:57:40
 */

public interface SysMsgShowPageRepository
		extends JpaRepository<SysMsgShow, Long>, JpaSpecificationExecutor<SysMsgShow> {

}
