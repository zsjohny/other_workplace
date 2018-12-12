package com.finace.miscroservice.authorize.dao;

import com.finace.miscroservice.commons.entity.UserAuth;

import java.util.List;

/**
 * ldap的curd操做的dao层
 */

public interface LdapCurdDao {
    /**
     * 插入用户
     *
     * @param users 用户的实体数组集合
     */
    Boolean insertUsers(List<UserAuth> users);


    /**
     * 更新用户
     *
     * @param user 用户的实体
     */
    void updateUser(UserAuth user);


    /**
     * 删除用户
     *
     * @param user 用户的实体
     */
    void deleteUser(UserAuth user);


    /**
     * 验证用户的密码
     *
     * @param user 用户的实体
     */
    Boolean checkUserPass(UserAuth user);


    /**
     * 根据用户名查询用户信息
     *
     * @param userName 用户名
     * @return
     */
    UserAuth findUserByName(String userName);

}
