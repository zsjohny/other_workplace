
package com.finace.miscroservice.user.entity.beans;

import com.finace.miscroservice.commons.handler.DbHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class BeanFactories {

    @Bean(initMethod = "init")
    public DbHandler register(DataSource dataSource){
        DbHandler dbHandler=new DbHandler();
        dbHandler.setAlwaysDrop(Boolean.FALSE);
//        dbHandler.setAlwaysDrop(Boolean.TRUE);    //删除重建
        dbHandler.setDataSource(dataSource);
        dbHandler.setShowSql(Boolean.TRUE);
        dbHandler.setPackageBase("com.finace.miscroservice.user.entity.po");
        return dbHandler;
    }
}

