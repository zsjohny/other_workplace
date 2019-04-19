package com.e_commerce.miscroservice.crm.mapper;

import com.e_commerce.miscroservice.crm.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Create by hyf on 2018/9/18
 */
@Mapper
public interface UserMapper {
    /**
     * 根据用户名查找
     * @param phone
     * @return
     */
    User findUserByPhone(@Param("phone") String phone);

    /**
     * 注册
     * @param phone
     * @param doubleMd5Pass
     * @param name
     */
    void insertUser(@Param("userName") String userName,@Param("phone") String phone, @Param("doubleMd5Pass") String doubleMd5Pass, @Param("name") String name);

    /**
     *  根据姓名查询
     * @param allotName
     * @return
     */
    User findUserByName(@Param("allotName") String allotName);

    /**
     * 根据用户id 查询用户
     * @param userId
     * @return
     */
    User findUserByUserId(@Param("userId") Long userId);

    /**
     * 根据用户名查找
     * @param userName
     * @return
     */
    User findUserByUserName(@Param("userName") String userName);
}
