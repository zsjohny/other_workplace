/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.ServiceRecordTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: ServiceRecordTimePageRepository.java, 2016年3月10日 下午5:20:56
 */

public interface ServiceRecordTimePageRepository
		extends JpaRepository<ServiceRecordTime, Integer>, JpaSpecificationExecutor<ServiceRecordTime> {

}
