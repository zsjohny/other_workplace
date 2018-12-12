/**
 * 
 */
package com.ouliao.dao.versionfirst;

import com.ouliao.domain.versionfirst.ServiceRecordTime;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: ServiceRecordTimeDao.java, 2016年3月10日 下午5:19:15
 */

public interface ServiceRecordTimeDao {

	void createServiceRecordTime(ServiceRecordTime serviceRecordTime);

	int updateUserCallTimeByUserId(Long userCallTime, Integer userId);

	int deleteSysSendByUserId(Integer userId);

	ServiceRecordTime queryUserRecordIsExistByUserId(Integer userId);
}
