package com.tunnel.service.impl

import com.tunnel.dao.BaseDomainDao
import com.tunnel.domain.TunnelExamineRelationQuery
import com.tunnel.domain.User
import com.tunnel.service.BaseDomainServer
import com.tunnel.util.BaseDomain
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by Ness on 2016/10/8.
 */
@Transactional
@Service
public class BaseDomainServerImpl implements BaseDomainServer {

    @Autowired
    private BaseDomainDao baseDomainDao

    private Logger logger = LoggerFactory.getLogger(BaseDomainServerImpl.class)

    @Override
    void saveTunnelExmaineRelationQuery(TunnelExamineRelationQuery tunnelExamineRelationQuery) {
        try {
            logger.info("开始保存调查跟查询的关系 ,model={}", tunnelExamineRelationQuery)
            baseDomainDao.saveTunnelExmaineRelationQuery(tunnelExamineRelationQuery)
            logger.info("结束保存调查跟查询的关系 ,model={}", tunnelExamineRelationQuery)
        } catch (Exception e) {
            logger.warn("结束保存调查跟查询的关系出错 ,model={}", tunnelExamineRelationQuery, e)
        }
    }

    void saveBaseDomain(BaseDomain baseDomain) {
        try {
            logger.info("开始保存信息,baseDomain={}", baseDomain)
            baseDomainDao.saveBaseDomain(baseDomain)
            logger.info("结束保存信息,baseDomain={}", baseDomain)
        } catch (Exception e) {
            logger.warn("保存信息出错,baseDomain={}", baseDomain, e)
        }
    }

    @Override
    void saveBaseDomainAll(List<BaseDomain> baseDomains) {
        try {
            logger.info("开始保存所有信息,baseDomain={}", baseDomains)
            baseDomainDao.saveBaseDomainAll(baseDomains)
            logger.info("结束保存所有信息,baseDomain={}", baseDomains)
        } catch (Exception e) {
            logger.warn("保存所有信息出错,baseDomain={}", baseDomains, e)
        }
    }

    BaseDomain findBaseDomainByBaseDomain(BaseDomain baseDomain) {

        try {
            logger.info("开始查找信息,baseDomain={}", baseDomain)
            baseDomain = baseDomainDao.findBaseDomainByBaseDomain(baseDomain)
            logger.info("结束查找信息,baseDomain={}", baseDomain)
        } catch (Exception e) {
            logger.warn("查找信息出错,baseDomain={}", baseDomain, e)
        }



        return baseDomain
    }

    @Override
    List<BaseDomain> findBaseDomainALlByPage(BaseDomain baseDomain) {

        List<BaseDomain> list = null

        try {
            if (baseDomain == null) {
                return list
            }
            logger.info("开始查找信息,baseDomain={}", baseDomain)
            list = baseDomainDao.findBaseDomainALlByPage(baseDomain)
            logger.info("结束查找信息,list={}", list)
        } catch (Exception e) {
            logger.warn("查找信息出错,baseDomain={}", baseDomain, e)
        }

        return list
    }

    @Override
    List<BaseDomain> findBaseDomainALl(BaseDomain baseDomain) {
        List<BaseDomain> list = null

        try {
            if (baseDomain == null) {
                return list
            }
            logger.info("开始查找所有信息,baseDomain={}", baseDomain)
            list = baseDomainDao.findBaseDomainALl(baseDomain)
            logger.info("结束查找所有信息,list={}", list)
        } catch (Exception e) {
            logger.warn("查找所有信息出错,baseDomain={}", baseDomain, e)
        }

        return list
    }

    void updateBaseDomainByBaseDomain(BaseDomain baseDomain) {
        try {
            if (baseDomain == null) {
                return
            }
            logger.info("开始修改信息,baseDomain={}", baseDomain)
            baseDomainDao.updateBaseDomainByBaseDomain(baseDomain)
            logger.info("结束修改信息,baseDomain={}", baseDomain)
        } catch (Exception e) {
            logger.warn("修改信息出错,baseDomain={}", baseDomain, e)
        }

    }

    void deleteBaseDomainById(BaseDomain baseDomain) {
        try {
            if (baseDomain == null) {
                return
            }
            logger.info("开始删除信息,baseDomain={}", baseDomain)
            baseDomainDao.deleteBaseDomainById(baseDomain)
            logger.info("结束删除信息,baseDomain={}", baseDomain)
        } catch (Exception e) {
            logger.warn("删除出错,baseDomain={}", baseDomain, e)
        }


    }

    Integer findBaseDomainCountByBaseDomain(BaseDomain baseDomain) {

        try {
            return baseDomainDao.findBaseDomainCountByBaseDomain(baseDomain)


        } catch (Exception e) {

            logger.warn("查找 baseDomain={},数量出错", baseDomain, e)

        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:app*.xml");
        BaseDomainServer service = context.getBean("baseDomainServerImpl")
//        User user = new User("name": "tome", "password": "12345", "createTime": new Date())
        User user = new User("name": "tome", "password": "12345", "createTime": new Date())
        service.findBaseDomainByBaseDomain(user)


    }

}
