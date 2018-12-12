/**
 * 
 */
package com.ouliao.dao.versionfirst.impl;

import com.ouliao.dao.versionfirst.ServiceRecordTimeDao;
import com.ouliao.domain.versionfirst.ServiceRecordTime;
import com.ouliao.repository.versionfirst.ServiceRecordTimePageRepository;
import com.ouliao.repository.versionfirst.ServiceRecordTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: ServiceRecordTimeImpl.java, 2016年3月10日 下午5:19:28
 */
@Repository
public class ServiceRecordTimeDaoImpl implements ServiceRecordTimeDao {
	@Autowired
	private ServiceRecordTimeRepository serviceRecordTimeRepository;
	@Autowired
	private ServiceRecordTimePageRepository serviceRecordTimePageRepository;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void createServiceRecordTime(ServiceRecordTime serviceRecordTime) {

		serviceRecordTimePageRepository.save(serviceRecordTime);

	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int updateUserCallTimeByUserId(Long userCallTime, Integer userId) {
		return serviceRecordTimeRepository.updateUserCallTimeByUserId(userCallTime, userId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public ServiceRecordTime queryUserRecordIsExistByUserId(Integer userId) {
		return serviceRecordTimeRepository.queryUserRecordIsExistByUserId(userId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int deleteSysSendByUserId(Integer userId) {
		return serviceRecordTimeRepository.deleteSysSendByUserId(userId);
	}
}
