package com.finace.miscroservice.activity.config;

import com.finace.miscroservice.commons.config.RedisTemplateConfig;
import com.finace.miscroservice.commons.handler.DbHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

@Configuration
public class RedisConfig extends RedisTemplateConfig {



    @Bean(name = "userHashRedisTemplate")
    public HashOperations<String, String, String> createZuulHashRedisTemplate() {
//        DbHandler dbHandler=new DbHandler();
//        dbHandler.setAlwaysDrop(Boolean.FALSE);
//        dbHandler.setDataSource(dataSource);
//        dbHandler.setShowSql(Boolean.TRUE);
//        dbHandler.setPackageBase();
        return createTemplateCache(OperateEnum.HASH).opsForHash();
    }

    @Bean(name = "userStrHashRedisTemplate")
    public ValueOperations<String, String> createStrHashRedisTemplate() {
        return createTemplateCache(OperateEnum.STR).opsForValue();
    }




}
