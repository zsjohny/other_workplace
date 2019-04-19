package com.e_commerce.miscroservice.publicaccount.config;

//import com.e_commerce.miscroservice.commons.handler.DbHandler;
//import com.e_commerce.miscroservice.commons.plug.mybatis.config.MybatisConfig;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.config.MybatisConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * mybatisv插件的配置
 */
@Configuration
public class MybatisPlugConfig {

    @Bean
    public MybatisConfig createMybatisConfig() {
        MybatisConfig mybatisConfig = new MybatisConfig();
        mybatisConfig.setIsCamel(Boolean.TRUE);
        return mybatisConfig;
    }

    @Bean(initMethod = "init")
    public DbHandler createDbHandler(DataSource dataSource) {
        DbHandler dbHandler = new DbHandler();
        dbHandler.setIsCamel(Boolean.TRUE);
        dbHandler.setAlwaysDrop(Boolean.FALSE);
        dbHandler.setDataSource(dataSource);
//        dbHandler.setPackageBase("com.e_commerce.miscroservice.commons.entity.application.proxy");
        dbHandler.setPackageBase ("com.e_commerce.miscroservice.commons.entity.application.user");
        dbHandler.setShowSql(Boolean.TRUE);
        dbHandler.setAlwaysInit(Boolean.TRUE);
        return dbHandler;
    }


}
