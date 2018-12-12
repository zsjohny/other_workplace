package com.ouliao.repository.versionsecond;

import com.ouliao.domain.versionsecond.UserPersionalShow;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by nessary on 16-5-17.
 */
public interface UserPersionalShowRepository extends Repository<UserPersionalShow, Long> {


    @Query(value = "select * from userpersionalshow   where  isDeleted='0' and userId=:userId  order by creatTime desc limit :start,:pagecount ", nativeQuery = true)
    List<UserPersionalShow> queryUserPersionalAllByUserId(@Param("start") Integer start, @Param("pagecount") Integer pagecount, @Param("userId") Integer userId);

    @Modifying
    @Query("update UserPersionalShow set isDeleted='1' where userPersionalShowId in (:userPersionalShowId) and userId =:userId ")
    void deleteUserPersionalShowById(@Param("userPersionalShowId") List<Long> userPersionalShowIds, @Param("userId") Integer userId);


}
