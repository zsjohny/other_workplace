/**
 *
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserCallRoom;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author xiaoluo
 * @version $Id: UserCallRoomRepository.java, 2016年3月1日 下午6:13:27
 */
@RepositoryDefinition(domainClass = UserCallRoom.class, idClass = Integer.class)
public interface UserCallRoomRepository {
    @Query("from UserCallRoom where userCalledId =:userCalledId")
    UserCallRoom queryByUserCallRoomByUserCalledId(@Param("userCalledId") Integer userCalledId);

    @Query("from UserCallRoom where userCalledId =:id OR  userId=:id ")
    UserCallRoom queryByUserCallRoomIsExistById(@Param("id") Integer id);

    @Query("from UserCallRoom where userCalledId =:userCalledId and  userId=:userId ")
    public UserCallRoom queryByUserCallRoomById(@Param("userId") Integer userId,
                                                @Param("userCalledId") Integer userCalledId);

    @Query("from UserCallRoom where userCalledId =:userCalledId and  userId=:userId ")
    public List<UserCallRoom> queryByUserCallRoomAllById(@Param("userId") Integer userId,
                                                         @Param("userCalledId") Integer userCalledId);

    @Modifying
    @Query("delete from UserCallRoom where userCalledId =:id OR  userId=:id ")
    public void deleteAllById(@Param("id") Integer id);

    @Query("from UserCallRoom where userCalledId =:id OR  userId=:id ")
    List<UserCallRoom> queryByUserCallRoomAllIsExistById(@Param("id") Integer id);


    @Modifying
    @Query(value = "insert into usercallroom (userCalledId,userCreateTime,userId) values(:userCalledId,:userCreateTime,:userId)", nativeQuery = true)
    UserCallRoom createUserCallRoomBy(@Param("userCalledId") Integer userCalledId, @Param("userCreateTime") Date userCreateTime, @Param("userId") Integer userId);

}
