/**
 * 
 */
package com.ouliao.dao.versionfirst;

import com.ouliao.domain.versionfirst.UserCallRoom;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserCallRoomDao.java, 2016年3月1日 下午6:18:55
 */

public interface UserCallRoomDao {
	UserCallRoom createUserCallRoomBy(UserCallRoom userCallRoom);

	UserCallRoom queryByUserCallRoomById(Integer userId, Integer userCalledId);

	UserCallRoom queryByUserCallRoomByUserCalledId(Integer userCalledId);

	void deleteUserCallRoomByUserCallRoomId(Integer userCallRoomId);

	UserCallRoom queryByUserCallRoomIsExistById(Integer id);

	public void deleteAllById(Integer id);

	public List<UserCallRoom> queryByUserCallRoomAllById(Integer userId, Integer userCalledId);

	List<UserCallRoom> queryByUserCallRoomAllIsExistById(Integer id);
}
