package com.songxm.credit.dao.credit.deversion.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Configuration
@ConditionalOnProperty("risk.mysql.url")
@ComponentScan(basePackages = {"com.songxm.credit.dao.credit.deversion.persistence", "com.songxm.credit.dao.credit.deversion.aspect"})
@MapperScan(basePackages = "com.songxm.credit.dao.credit.deversion.persistence", sqlSessionFactoryRef = "sqlSessionFactory")
public class DbConfig {
    private Map<String, Object> datasourceMap;
    @Value("${risk.mysql.url}")
    private String url;
    @Value("${risk.mysql.user}")
    private String user;
    @Value("${risk.mysql.pass}")
    private String pass;
    @Value("${risk.mysql.max.active:10}")
    private String maxActive;

    @PostConstruct
    public void init() {
        datasourceMap = new HashMap<>();
        datasourceMap.put("driverClassName", "com.mysql.jdbc.Driver");
        datasourceMap.put("initialSize", "5");
        datasourceMap.put("minIdle", "1");
        datasourceMap.put("maxWait", "20000");
        datasourceMap.put("removeAbandoned", "true");
        datasourceMap.put("removeAbandonedTimeout", "180");
        datasourceMap.put("timeBetweenEvictionRunsMillis", "60000");
        datasourceMap.put("minEvictableIdleTimeMillis", "300000");
        datasourceMap.put("validationQuery", "SELECT 1");
        datasourceMap.put("testWhileIdle", "true");
        datasourceMap.put("testOnBorrow", "false");
        datasourceMap.put("testOnReturn", "false");
        datasourceMap.put("poolPreparedStatements", "true");
        datasourceMap.put("maxPoolPreparedStatementPerConnectionSize", "50");
        datasourceMap.put("initConnectionSqls", "SELECT 1");
        datasourceMap.put("maxActive", maxActive + "");
        datasourceMap.put("url", url);
        datasourceMap.put("username", user);
        if (StringUtils.isNotBlank(pass)) {
            datasourceMap.put("password", pass);
        }
    }

    @Bean(name = "datasourceRisk")
    public DataSource dataSource() {
        log.info("初始化risk-service数据源");
        try {
            return DruidDataSourceFactory.createDataSource(datasourceMap);
        } catch (Exception e) {
            log.error("无法获得数据源[{}]:[{}]", url, ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("无法获得数据源.");
        }
    }

    @Bean(name = "templateRisk")
    public JdbcTemplate templateXbRisk() {
        return new JdbcTemplate(this.dataSource());
    }

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory riskSqlSessionFactory(@Qualifier("datasourceRisk") DataSource riskDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(riskDataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sessionFactory.setMapperLocations(resolver.getResources("classpath:sqlmapper/*.xml"));
            sessionFactory.setConfigLocation(resolver.getResource("classpath:/spring/mybatis-config.xml"));
            return sessionFactory.getObject();
        } catch (Exception e) {
            log.error("sqlSessionFactory 初始化失败！");
            throw new RuntimeException(e);
        }
    }
    @Bean(name = "tmRisk")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(this.dataSource());
    }
}
