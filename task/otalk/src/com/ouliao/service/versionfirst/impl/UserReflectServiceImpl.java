/**
 * 
 */
package com.ouliao.service.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserReflectDao;
import com.ouliao.domain.versionfirst.UserReflect;
import com.ouliao.service.versionfirst.UserReflectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserReflectCrudServiceImpl.java, 2016年2月26日 下午1:29:46
 */
@Service
@Transactional
public class UserReflectServiceImpl implements UserReflectService {
	@Autowired
	private UserReflectDao userReflectDao;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void creatUserReflect(UserReflect userReflect) {
		userReflectDao.creatUserReflect(userReflect);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Long queryReflectCountByAll() {
		return userReflectDao.queryReflectCountByAll();
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int updateIsDeletedByUserReflectId(List<Integer> ids) {
		return userReflectDao.updateIsDeletedByUserReflectId(ids);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public List<UserReflect> queryUserReflectAllByIsDeleted() {
		return userReflectDao.queryUserReflectAllByIsDeleted();
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Page<UserReflect> queryUserReflectWithDrawByUserId(Integer starPage, Integer pageNum, Integer userId) {
		return userReflectDao.queryUserReflectWithDrawByUserId(starPage, pageNum, userId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public List<UserReflect> queryUserReflectWithAllDrawByUserId(Integer userId) {
		return userReflectDao.queryUserReflectWithDrawAllByUserId(userId);
	}
}
