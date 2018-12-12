/**
 * 
 */
package com.ouliao.service.versionfirst.impl;

import com.ouliao.dao.versionfirst.ServiceRecordTimeDao;
import com.ouliao.domain.versionfirst.ServiceRecordTime;
import com.ouliao.service.versionfirst.ServiceRecordTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: ServiceRecordTimeServiceImpl.java, 2016年3月10日 下午5:20:01
 */
@Transactional
@Service
public class ServiceRecordTimeServiceImpl implements ServiceRecordTimeService {
	@Autowired
	private ServiceRecordTimeDao serviceRecordTimeDao;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void createServiceRecordTime(ServiceRecordTime serviceRecordTime) {
		serviceRecordTimeDao.createServiceRecordTime(serviceRecordTime);

	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int updateUserCallTimeByUserId(Long userCallTime, Integer userId) {
		return serviceRecordTimeDao.updateUserCallTimeByUserId(userCallTime, userId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public ServiceRecordTime queryUserRecordIsExistByUserId(Integer userId) {
		return serviceRecordTimeDao.queryUserRecordIsExistByUserId(userId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int deleteSysSendByUserId(Integer userId) {
		return serviceRecordTimeDao.deleteSysSendByUserId(userId);
	}
}
