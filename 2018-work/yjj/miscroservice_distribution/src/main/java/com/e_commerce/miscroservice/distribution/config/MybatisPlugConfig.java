package com.e_commerce.miscroservice.distribution.config;

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
        dbHandler.setPackageBase("com.e_commerce.miscroservice.distribution.entity");
        dbHandler.setShowSql(Boolean.TRUE);
        dbHandler.setAlwaysInit(Boolean.TRUE);

        /**
         * 生成实体文件
         *
         * @param isModify   是否需要修正表 不修正保留之前的
         * @param blurPrefix    去除表的前缀 多个利用,分开
         * @param packageName   需要生成的包名称
         * @param needTableName 需要生成的表的名称,利用*支持模糊搜索 eg: example_table*_name(example_tableA_name,example_table_name皆匹配)
         */
//        dbHandler.generate(false, "yjj", "com.e_commerce.miscroservice.crm.po", "store_order");
        return dbHandler;
    }


}
