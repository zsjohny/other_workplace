/**
 * 
 */
package com.ouliao.service.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserBlackListDao;
import com.ouliao.domain.versionfirst.UserBlackList;
import com.ouliao.service.versionfirst.UserBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserBlackListServiceImpl.java, 2016年2月23日 下午6:28:30
 */
@Service
@Transactional
public class UserBlackListServiceImpl implements UserBlackListService {
	@Autowired
	private UserBlackListDao userBlackListDao;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int updateUserBlackListByUserBlackListId(String isDeleted, Integer userBlackListId) {
		return userBlackListDao.updateUserBlackListByUserBlackListId(isDeleted, userBlackListId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserBlackList createUserBlackList(UserBlackList userBlackList) {
		return userBlackListDao.createUserBlackList(userBlackList);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserBlackList queryUserIsBlackListById(Integer userId, Integer userBlackId) {
		return userBlackListDao.queryUserIsBlackListById(userId, userBlackId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Page<UserBlackList> queryUserBlackListByUserBlackId(Integer startPage, Integer pageSize,
			Integer userBlackId) {
		return userBlackListDao.queryUserBlackListByUserBlackId(startPage, pageSize, userBlackId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Integer queryBlackListCountByUserId(Integer userId) {
		return userBlackListDao.queryBlackListCountByUserId(userId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int updateUserBlackListIsDeletedAllByUserBlackByIds(Integer userId, List<Integer> ids) {
		return userBlackListDao.updateUserBlackListIsDeletedAllByUserBlackByIds(userId, ids);
	}

}
