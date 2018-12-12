package com.finace.miscroservice.authorize.controller;

import com.finace.miscroservice.authorize.dao.LdapCurdDao;
import com.finace.miscroservice.commons.annotation.Auth;
import com.finace.miscroservice.commons.entity.UserAuth;
import com.finace.miscroservice.commons.log.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ldap的操作控制层
 */
@RestController
@Auth
public class LdapController {
    @Autowired
    private LdapCurdDao ldapCurdDao;

    private Log log = Log.getInstance(LdapController.class);

    /**
     * 插入用户
     *
     * @param users 用户的po数组集合
     */
    @RequestMapping(value = "saves", method = RequestMethod.POST)
    public Boolean insertUsers(@RequestBody List<UserAuth> users) {

        if (users == null || users.isEmpty()) {
            log.warn("开入用户数据参数为空");
            return Boolean.FALSE;
        }
        try {

            log.info("开始插入用户数据");

            return ldapCurdDao.insertUsers(users);

        } catch (Exception e) {
            log.error("插入用户数据出错", e);
            return Boolean.FALSE;
        }
    }


    /**
     * 更新用户
     *
     * @param user 用户
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public void updateUser(@RequestBody UserAuth user) {
        if (user == null || user.wasEmpty()) {
            log.info("开始更新用户={} 数据参数为空", user);
            return;
        }
        try {

            log.info("开始更新用户={}数据", user.getName());

            ldapCurdDao.updateUser(user);
        } catch (Exception e) {

            log.error("更新用户={}数据出错", user.getName(), e);
        }
    }


    /**
     * 删除用户
     *
     * @param user 用户
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public void deleteUser(@RequestBody UserAuth user) {
        if (user == null || StringUtils.isEmpty(user.getName())) {
            log.info("开始删除用户={} 数据参数为空", user);
            return;
        }
        try {

            log.info("开始删除用户={}数据", user.getName());
            ldapCurdDao.deleteUser(user);

        } catch (Exception e) {
            log.error("删除用户={}数据异常", user.getName(), e);
        }
    }


    /**
     * 验证用户的密码
     *
     * @param user 用户
     */
    @RequestMapping(value = "auth", method = RequestMethod.POST)
    public Boolean checkUserPass(@RequestBody UserAuth user) {

        if (user == null || StringUtils.isAnyEmpty(user.getName(), user.getPass())) {
            log.info("开始验证用户={} 数据参数为空", user);
            return Boolean.FALSE;
        }
        try {

            log.info("开始验证用户={}数据", user.getName());
            return ldapCurdDao.checkUserPass(user);
        } catch (Exception e) {
            log.error("验证用户={}信息出错", user.getName(), e);
            return Boolean.FALSE;
        }
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param userName 用户名
     * @return
     */
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public UserAuth findUserByName(String userName) {

        UserAuth user = null;

        try {
            if (StringUtils.isEmpty(userName)) {
                log.info("开始查询用户数据参数为空");
                return user;
            }
            log.info("开始查询用户={}数据", userName);
            user = ldapCurdDao.findUserByName(userName);

        } catch (Exception e) {
            log.error("查询用户={}信息出错", userName, e);

        }
        return user;
    }
}
