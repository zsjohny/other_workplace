/**
 * 
 */
package com.ouliao.service.versionfirst;

import com.ouliao.domain.versionfirst.ServiceRecordTime;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: ServiceRecordTimeService.java, 2016年3月10日 下午5:19:51
 */

public interface ServiceRecordTimeService {
	void createServiceRecordTime(ServiceRecordTime serviceRecordTime);

	int updateUserCallTimeByUserId(Long userCallTime, Integer userId);

	int deleteSysSendByUserId(Integer userId);

	ServiceRecordTime queryUserRecordIsExistByUserId(Integer userId);
}
