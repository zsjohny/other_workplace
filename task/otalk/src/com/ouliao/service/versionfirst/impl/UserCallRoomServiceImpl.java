/**
 * 
 */
package com.ouliao.service.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserCallRoomDao;
import com.ouliao.domain.versionfirst.UserCallRoom;
import com.ouliao.service.versionfirst.UserCallRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserCallRoomServiceImpl.java, 2016年3月1日 下午6:22:09
 */
@Service
@Transactional
public class UserCallRoomServiceImpl implements UserCallRoomService {
	@Autowired
	private UserCallRoomDao userCallRoomDao;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */
	 
	@Override
	public UserCallRoom createUserCallRoomBy(UserCallRoom userCallRoom) {
		return userCallRoomDao.createUserCallRoomBy(userCallRoom);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserCallRoom queryByUserCallRoomByUserCalledId(Integer userCalledId) {
		return userCallRoomDao.queryByUserCallRoomByUserCalledId(userCalledId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void deleteUserCallRoomByUserCallRoomId(Integer userCallRoomId) {

		userCallRoomDao.deleteUserCallRoomByUserCallRoomId(userCallRoomId);

	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserCallRoom queryByUserCallRoomIsExistById(Integer id) {
		return userCallRoomDao.queryByUserCallRoomIsExistById(id);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserCallRoom queryByUserCallRoomById(Integer userId, Integer userCalledId) {
		return userCallRoomDao.queryByUserCallRoomById(userId, userCalledId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void deleteAllById(Integer id) {
		userCallRoomDao.deleteAllById(id);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public List<UserCallRoom> queryByUserCallRoomAllById(Integer userId, Integer userCalledId) {
		return userCallRoomDao.queryByUserCallRoomAllById(userId, userCalledId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public List<UserCallRoom> queryByUserCallRoomAllIsExistById(Integer id) {
		return userCallRoomDao.queryByUserCallRoomAllIsExistById(id);
	}
}
