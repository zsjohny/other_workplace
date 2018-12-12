package com.ouliao.repository.versionsecond; /**
 *
 */

import com.ouliao.domain.versionfirst.UserSupportSay;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author xiaoluo
 * @version $Id: UserSupportSayRepository.java, 2016年2月19日 下午9:22:48
 */
@RepositoryDefinition(domainClass = UserSupportSay.class, idClass = Integer.class)
public interface UserSupportSaySecondRepository {

    @Query(value = "select  * from usersupportsay  where  isDeleted='0'  and userSupportId=:userSupportId  order by createTime desc  limit :startCount,:pageSize", nativeQuery = true)
    List<UserSupportSay> querySupporAllByUserId(@Param("startCount") Integer startCount,
                                                @Param("pageSize") Integer pageSize, @Param("userSupportId") Integer userSupportId);

    @Query(value = "select  * from usersupportsay  where  isDeleted='0'  and userSayContentId=:userSayContentId  order by createTime desc  limit :startCount,:pageSize", nativeQuery = true)
    List<UserSupportSay> querySupporAllByUserSayContentId(@Param("startCount") Integer startCount,
                                                          @Param("pageSize") Integer pageSize, @Param("userSayContentId") Integer userSayContentId);


    @Modifying
    @Query("update UserSupportSay set isReader='true'  where   isDeleted='0' and   userSupportId =:userSupportId ")
    void updateUserSupportIsReadByUserSupportId(@Param("userSupportId") Integer userSupportId);

    @Query("select count(*) from  UserSupportSay  where   isDeleted='0' and   isReader ='false'  and  userSupportId =:userSupportId ")
    Integer queryUserSupportCountByUserSupportId(@Param("userSupportId") Integer userSupportId);


}
