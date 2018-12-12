package com.tunnel.service.impl

import com.tunnel.dao.UserDao
import com.tunnel.domain.User
import com.tunnel.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.stereotype.Service

/**
 * Created by Ness on 2016/10/8.
 */
//@Transactional
@Service
class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class)


    void saveUser(User user) {
        try {
            logger.info("开始保存用户信息,user={}", user)
            userDao.saveUser(user)
            logger.info("结束保存用户信息,user={}", user)
        } catch (Exception e) {
            logger.info("保存用户信息出错,user={}", user, e)
        }
    }

    public static void main(String[] args) {
        org.hibernate.cfg.annotations.reflection.JPAMetadataProvider
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:app*.xml");
        UserService service = context.getBean("userServiceImpl")
        User user = new User("name": "tome", "password": "12345", "createTime": new Date())
        service.saveUser(user)

    }

}
