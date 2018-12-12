/**
 *
 */
package com.ouliao.dao.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserCallRoomDao;
import com.ouliao.domain.versionfirst.UserCallRoom;
import com.ouliao.repository.versionfirst.UserCallRoomCrudRepository;
import com.ouliao.repository.versionfirst.UserCallRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xiaoluo
 * @version $Id: UserCallRoomDaoImpl.java, 2016年3月1日 下午6:19:22
 */
@Repository
public class UserCallRoomDaoImpl implements UserCallRoomDao {
    @Autowired
    private UserCallRoomRepository userCallRoomRepository;
    @Autowired
    private UserCallRoomCrudRepository userCallRoomCrudRepository;

    /**
     * @param
     * @return
     */
    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public UserCallRoom createUserCallRoomBy(UserCallRoom userCallRoom) {
//        return userCallRoomRepository.createUserCallRoomBy(userCallRoom.getUserCalledId(), userCallRoom.getUserCreateTime(), userCallRoom.getUserId());
        return userCallRoomCrudRepository.saveAndFlush(userCallRoom);
    }

    /**
     * @param
     * @return
     */

    @Override
    public UserCallRoom queryByUserCallRoomByUserCalledId(Integer userCalledId) {
        return userCallRoomRepository.queryByUserCallRoomByUserCalledId(userCalledId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public void deleteUserCallRoomByUserCallRoomId(Integer userCallRoomId) {

        userCallRoomCrudRepository.delete(userCallRoomId);

    }

    /**
     * @param
     * @return
     */

    @Override
    public UserCallRoom queryByUserCallRoomIsExistById(Integer id) {
        return userCallRoomRepository.queryByUserCallRoomIsExistById(id);
    }

    /**
     * @param
     * @return
     */

    @Override
    public UserCallRoom queryByUserCallRoomById(Integer userId, Integer userCalledId) {
        return userCallRoomRepository.queryByUserCallRoomById(userId, userCalledId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public void deleteAllById(Integer id) {
        userCallRoomRepository.deleteAllById(id);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<UserCallRoom> queryByUserCallRoomAllById(Integer userId, Integer userCalledId) {
        return userCallRoomRepository.queryByUserCallRoomAllById(userId, userCalledId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<UserCallRoom> queryByUserCallRoomAllIsExistById(Integer id) {
        return userCallRoomRepository.queryByUserCallRoomAllIsExistById(id);
    }
}
