/**
 * 
 */
package com.ouliao.service.versionfirst;

import com.ouliao.domain.versionfirst.UserCallRoom;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserCallRoomService.java, 2016年3月1日 下午6:19:44
 */

public interface UserCallRoomService {
	UserCallRoom createUserCallRoomBy(UserCallRoom userCallRoom);

	UserCallRoom queryByUserCallRoomByUserCalledId(Integer userCalledId);

	UserCallRoom queryByUserCallRoomById(Integer userId, Integer userCalledId);

	void deleteUserCallRoomByUserCallRoomId(Integer userCallRoomId);

	UserCallRoom queryByUserCallRoomIsExistById(Integer id);

	public void deleteAllById(Integer id);

	public List<UserCallRoom> queryByUserCallRoomAllById(Integer userId, Integer userCalledId);

	List<UserCallRoom> queryByUserCallRoomAllIsExistById(Integer id);
}
