package com.goldplusgold.td.sltp.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.goldplusgold.mq.pubsub.PubSubChannels;
import com.goldplusgold.mq.pubsub.simple_impl.MsgChannelBusSimpleImpl;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

/**
 * 进行Db的操作类
 * Created by Ness on 2017/5/10.
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class DbConfig {


    @Bean(initMethod = "init", destroyMethod = "close")
    public DruidDataSource getDataSource(@Value("${jdbc.url}") String url,
                                         @Value("${jdbc.user}") String username, @Value("${jdbc.pass}") String password) throws SQLException {
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
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(50);

        return dataSource;
    }

    @Bean(initMethod = "migrate")
    public Flyway getMigration(@Autowired DruidDataSource druidDataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(druidDataSource);

        flyway.setLocations("migration");


        return flyway;

    }


}
