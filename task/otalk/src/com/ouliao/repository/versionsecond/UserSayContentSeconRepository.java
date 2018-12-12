package com.ouliao.repository.versionsecond;

import com.ouliao.domain.versionfirst.UserSayContent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by nessary on 16-5-14.
 */
public interface UserSayContentSeconRepository extends Repository<UserSayContent, Integer> {

    @Query(value = "select * from userSayContent  where  isDeleted='0'  and   userId=:userId and ( userSayContentSubject like %:word% or userContent like %:word%) limit :startCount,:pageSize", nativeQuery = true)
    List<UserSayContent> queryUserSayContentBySubjectOrContent(@Param("startCount") Integer startCount,
                                                               @Param("pageSize") Integer pageSize, @Param("word") String word, @Param("userId") Integer userId);

    @Query("from UserSayContent  where  isDeleted='0'  and   userSayContentId=:userSayContentId")
    UserSayContent queryUserSayContentByUserSayContentId(@Param("userSayContentId") Integer userSayContentId);



}
