package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserActive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Created by nessary on 16-5-10.
 */
public interface UserActiveRepository extends JpaRepository<UserActive, Long> {
    @Modifying
    @Query("update UserActive set userCount =:userCount  where  userId=:userId")
    void updateUserCountByUserId(@Param("userCount") Integer userCount, @Param("userId") Integer userId);

    @Query("from UserActive  where  userId=:userId")
    List<UserActive> queryUserActiveAllByUserId(@Param("userId") Integer userId);

}
