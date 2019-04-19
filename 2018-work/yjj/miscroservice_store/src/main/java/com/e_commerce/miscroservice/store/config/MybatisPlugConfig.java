package com.e_commerce.miscroservice.store.config;

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
        dbHandler.setPackageBase("com.e_commerce.miscroservice.store.po");
        dbHandler.setShowSql(Boolean.TRUE);
        dbHandler.setAlwaysInit(Boolean.TRUE);

//                /
        //com.e_commerce.miscroservice.store.po
        /**
         * 生成实体文件
         *
         * @param blurPrefix    去除表的前缀 多个利用,分开
         * @param packageName   需要生成的包名称
         * @param needTableName 需要生成的表的名称,利用*支持模糊搜索 eg: example_table*_name(example_tableA_name,example_table_name皆匹配)
         */
//        ,yjj_Product,yjj_Brand
//        dbHandler.generate(false,"", "com.e_commerce.miscroservice.store.entity.vo", "store_refund_order");
//        dbHandler.generate("", "com.e_commerce.miscroservice.store.entity.vo", "store_refund_order");
        return dbHandler;
    }


}
