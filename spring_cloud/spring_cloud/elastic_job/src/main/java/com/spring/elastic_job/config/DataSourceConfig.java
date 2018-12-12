package com.spring.elastic_job.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//@Configuration
public class DataSourceConfig {

    @Bean(initMethod = "init", destroyMethod = "close", name = "dev")

    public DruidDataSource getDataSource(@Value("${jdbc.url}") String url,
                                         @Value("${jdbc.user}") String username, @Value("${jdbc.pass}") String password) throws SQLException {

        return createDruidData(url, username, password);
    }


    private DruidDataSource createDruidData(String url, String username, String password) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setFilters("stat");
        dataSource.setMaxActive(50);
        dataSource.setInitialSize(10);
        dataSource.setMaxWait(60000);
        dataSource.setMinIdle(10);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(true);
        List<String> chartSet = new ArrayList<>();
        chartSet.add("set names  utf8mb4;");
        dataSource.setConnectionInitSqls(chartSet);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(50);

        return dataSource;
    }

}
