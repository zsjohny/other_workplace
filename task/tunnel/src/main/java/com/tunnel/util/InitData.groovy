package com.tunnel.util

import com.tunnel.dao.BaseDomainDao
import com.tunnel.domain.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.time.LocalDateTime

/**
 * 初始化用户数据
 * Created by Ness on 2016/10/11.
 */
@Component
class InitData {

    @Autowired
    private BaseDomainDao baseDomainDao


    private String NAME = "admin"
    private String PASSWORD = "admin"

    private Logger logger = LoggerFactory.getLogger(InitData.class)

    @PostConstruct
    void initData() {

       /* if (!CreatDatabase.register) {
            System.exit(-1);
        }*/
        if (baseDomainDao.findBaseDomainByBaseDomain(new User("name": "admin")) == null) {
            String key = String.valueOf(UUID.randomUUID())
            baseDomainDao.saveBaseDomain(new User("name": NAME, "realName": "admin", "status": true, "authorLevel": User.UserType.superUser.getValue(), "password": DesUtil.encrypt(Md5.MD5(PASSWORD).toLowerCase(), key), "passKey": key, "createTime": LocalDateTime.now()))

            logger.info("已经重新初始化超级管理员账号")
        }



    }

    public static void main(String[] args) {
        //
        println Md5.MD5("admin").toLowerCase().equals("21232f297a57a5a743894a0e4a801fc3")
    }


}
