/**
 * 
 */
package com.ouliao.service.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserCallMarkDao;
import com.ouliao.domain.versionfirst.UserCallMark;
import com.ouliao.service.versionfirst.UserCallMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserCallMarkServiceImpl.java, 2016年2月27日 上午7:34:12
 */
@Service
@Transactional
public class UserCallMarkServiceImpl implements UserCallMarkService {
	@Autowired
	private UserCallMarkDao userCallMarkDao;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */
	
	@Override
	public void createUserCallMark(UserCallMark userCallMark) {
		userCallMarkDao.createUserCallMark(userCallMark);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Page<UserCallMark> queryUserCallMarkByUserId(Integer starPage, Integer pageNum, Integer userId) {
		return userCallMarkDao.queryUserCallMarkByUserId(starPage, pageNum, userId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int updateUserCallMarkIsDeletedByUserCallMarkId(Integer userCallMarkId, Double userCallCost,
			String userCallTime, Double userCallEarn) {
		return userCallMarkDao.updateUserCallMarkIsDeletedByUserCallMarkId(userCallMarkId, userCallCost, userCallTime,
				userCallEarn);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserCallMark queryUserCallMarkIsDeletedById(Integer userCalledId, Integer userId) {
		return userCallMarkDao.queryUserCallMarkIsDeletedById(userCalledId, userId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserCallMark queryUserCallMarkByUserCallMarkId(Integer userCallMarkId) {
		return userCallMarkDao.queryUserCallMarkByUserCallMarkId(userCallMarkId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int updateUserCallMarkIsScoreByUserCallMarkId(Integer userCallMarkId) {
		return userCallMarkDao.updateUserCallMarkIsScoreByUserCallMarkId(userCallMarkId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void deleteUserCallMarkIsDeletedByCallMarkId(Integer userCallMarkId) {
		userCallMarkDao.deleteUserCallMarkIsDeletedByCallMarkId(userCallMarkId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public List<UserCallMark> queryUserCallMarkAllByUserCallMarkId(Integer userCallMarkId) {
		return userCallMarkDao.queryUserCallMarkAllByUserCallMarkId(userCallMarkId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public List<UserCallMark> queryUserCallMarkIsDeletedAllById(Integer userCalledId, Integer userId) {
		return userCallMarkDao.queryUserCallMarkIsDeletedAllById(userCalledId, userId);
	}
}
