package com.tunnel.repository

import com.tunnel.domain.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/11.
 */
interface UserRepository extends Repository<User, Integer> {

    @Modifying
    @Query("update User  set password=:password where name=:name  ")
    void updateUserPassword(@Param("name") String name, @Param("password") String password);

    @Query(value = "select  * from user  limit :page ,:pageSize  ", nativeQuery = true)
    List<User> findUssrALlByPage(@Param("page") Integer page, @Param("pageSize") Integer pageSize);

    @Modifying
    @Query("update User  set loadTime=:loadTime where name=:name  ")
    void updateUserPassByLoadTimeAndName(@Param("name") String name, @Param("loadTime") Date loadTime);


    @Modifying
    @Query("update User  set realName=:realName , name=:name ,password=:password,authorLevel=:authorLevel,passKey=:passKey ,status=:status where id=:id ")
    void updateUserAllByLoadTimeAndName(
            @Param("realName") String realName,
            @Param("name") String name, @Param("password") String password, @Param("authorLevel") Integer authorLevel,
            @Param("passKey") String passKey, @Param("status") Boolean status, @Param("id") Integer id);

    @Query(value = "select  * from user  where name like %:name% limit :page ,:pageSize  ", nativeQuery = true)
    List<User> findUserALlByNameAndPage(
            @Param("page") Integer page, @Param("pageSize") Integer pageSize, @Param("name") String name);

    @Query(value = "select  count(id) from user  where name like %:name%   ", nativeQuery = true)
    Integer findUserCountALlByNameAndPage(@Param("name") String name);


}
