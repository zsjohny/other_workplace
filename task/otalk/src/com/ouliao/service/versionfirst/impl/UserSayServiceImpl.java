/**
 * 
 */
package com.ouliao.service.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserSayDao;
import com.ouliao.domain.versionfirst.UserCommont;
import com.ouliao.domain.versionfirst.UserSayContent;
import com.ouliao.domain.versionfirst.UserSupportSay;
import com.ouliao.service.versionfirst.UserSayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserCommontServiceImpl.java, 2016年2月19日 下午7:01:47
 */
@Service
@Transactional
public class UserSayServiceImpl implements UserSayService {
	@Autowired
	private UserSayDao userSayDao;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public boolean createUserCommontByUserId(UserCommont userCommont) {

		return userSayDao.createUserCommontByUserId(userCommont);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public boolean createUserSayContentByUserId(UserSayContent userSayContent) {

		return userSayDao.createUserSayContentByUserId(userSayContent);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int deleteUserSayContentByUserId(Integer userId, Integer userSayContentId) {
		return userSayDao.deleteUserSayContentByUserId(userId, userSayContentId);

	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public List<UserSayContent> querySayContentByUserId(Integer userId) {

		return userSayDao.querySayContentByUserId(userId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Iterable<UserSupportSay> querySayAllSupports() {

		return userSayDao.querySayAllSupports();
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void saveSupportsByUserId(UserSupportSay userSupportSay) {
		userSayDao.saveSupportsByUserId(userSupportSay);

	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public List<UserSupportSay> querySupporIsDeletedByUserId(Integer userSayContentId) {

		return userSayDao.querySupporIsDeletedByUserId(userSayContentId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void updateSupportSayContentById(String isDeleted, Integer userId, Integer userSayContentId) {
		userSayDao.updateSupportSayContentById(isDeleted, userId, userSayContentId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserSupportSay querySupportUniqueById(Integer userId, Integer userSayContentId) {

		return userSayDao.querySupportUniqueById(userId, userSayContentId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int deleteCommontById(Integer userCommontId, Integer userId, Integer ownerId) {
		return userSayDao.deleteCommontById(userCommontId, userId, ownerId);

	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Page<UserCommont> querySayCommontAllByUserSayContentId(Integer startPage, Integer pageCount,
			Integer userSayContentId) {

		return userSayDao.querySayCommontAllByUserSayContentId(startPage, pageCount, userSayContentId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserSayContent querySayContentByUserSayContentId(Integer userSayContentId) {

		return userSayDao.querySayContentByUserSayContentId(userSayContentId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserSupportSay querySupportUniqueExpecIsDeletedById(Integer userId, Integer userSayContentId) {

		return userSayDao.querySupportUniqueExpecIsDeletedById(userId, userSayContentId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserSayContent querySayContentUniqueById(Integer userId, Integer userSayContentId) {

		return userSayDao.querySayContentUniqueById(userId, userSayContentId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserCommont querySayCommontOneByUserCommontId(Integer userCommontId) {

		return userSayDao.querySayCommontOneByUserCommontId(userCommontId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void deleteCommontAllByUserCommontId(Integer userCommontId) {
		userSayDao.deleteCommontAllByUserCommontId(userCommontId);

	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public List<UserCommont> querySayCommontAllByUserSayContentId(Integer userSayContentId) {
		return userSayDao.querySayCommontAllByUserSayContentId(userSayContentId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Integer querySupportCountById(Integer userSayContentId) {

		return userSayDao.querySupportCountById(userSayContentId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Page<UserSayContent> querySayContentAllIsDeletedByUserId(Integer startPage, Integer pageCount,
																	Integer userId) {

		return userSayDao.querySayContentAllIsDeletedByUserId(startPage, pageCount, userId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Integer querySayCommontCountCountByUserSayContentId(Integer userSayContentId) {

		return userSayDao.querySayCommontCountCountByUserSayContentId(userSayContentId);
	}


}
