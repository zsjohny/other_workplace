package com.finace.miscroservice.commons.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.atomikos.icatch.config.UserTransactionServiceImp;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.finace.miscroservice.commons.annotation.DependsOnSql;
import com.finace.miscroservice.commons.init.AutoInitSql;
import com.finace.miscroservice.commons.utils.BeanNameConstant;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.sql.SQLException;
import java.util.*;

import static com.atomikos.icatch.config.imp.AbstractUserTransactionServiceFactory.*;
import static com.finace.miscroservice.commons.utils.BeanNameConstant.SQL_INIT_BEAN_NAME;

/**
 * 数据源使用
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@ConditionalOnExpression("${datasource.enabled}")
public class DbConfig {


    public final static String READ_DATA_SOURCE = "read";
    public final static String WRITE_DATA_SOURCE = "write";


    @Bean(name = WRITE_DATA_SOURCE, initMethod = "init", destroyMethod = "close")
    public DruidDataSource getDataSource(@Value("${jdbc.write.class}")
                                                 String className, @Value("${jdbc.write.url}") String url,
                                         @Value("${jdbc.write.user}") String username,
                                         @Value("${jdbc.write.pass}") String password) throws SQLException {


        return createDataSource(className, url, username, password);
    }


    @Bean(name = READ_DATA_SOURCE)
    public DataSource createAtomikosXADataSource(@Value("${jdbc.read.url}") String databaseUrl,
                                                 @Value("${jdbc.read.user}") String userName,
                                                 @Value("${jdbc.read.pass}") String password) {
        return createXADataSource(databaseUrl, userName, password);

    }


    /**
     * 数据源管理中心
     *
     * @return
     */
    @Bean
    @Primary
    public AbstractDataSource createAbstractDataSource(DataSource write, DataSource read) {
        CreateDataSourceRouting createDataSourceRouting = new CreateDataSourceRouting();
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(WRITE_DATA_SOURCE, write);
        dataSources.put(READ_DATA_SOURCE, read);
        createDataSourceRouting.setTargetDataSources(dataSources);
        createDataSourceRouting.setDefaultTargetDataSource(write);
        return createDataSourceRouting;
    }


    /**
     * 创建普通数据源
     *
     * @param className 加载驱动的名称
     * @param url       数据链接地址
     * @param username  用户名
     * @param password  用户密码
     * @return
     * @throws SQLException
     */
    private DruidDataSource createDataSource(String className, String url, String username, String password) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(className);
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
        chartSet.add("set names  utf8;");
        dataSource.setConnectionInitSqls(chartSet);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(50);

        return dataSource;
    }


    /**
     * 创建XA数据源
     *
     * @param databaseUrl 数据连接地址
     * @param userName    用户名
     * @param password    用户密码
     * @return
     */
    private DataSource createXADataSource(String databaseUrl, String userName,
                                          String password) {
        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setUrl(databaseUrl);
        mysqlXADataSource.setUser(userName);
        mysqlXADataSource.setPassword(password);
        AtomikosDataSourceBean atomikosDataSource = new AtomikosDataSourceBean();
        atomikosDataSource.setUniqueResourceName(UUID.randomUUID().toString());
        atomikosDataSource.setXaDataSource(mysqlXADataSource);
        atomikosDataSource.setMinPoolSize(5);
        atomikosDataSource.setMaxPoolSize(20);
        atomikosDataSource.setMaxIdleTime(150);
        atomikosDataSource.setMaintenanceInterval(60);
        atomikosDataSource.setMaxLifetime(20000);

        return atomikosDataSource;

    }

    /**
     * xa事物配置------start--------------------
     */


    @Bean(initMethod = "init", destroyMethod = "shutdownForce")
    public UserTransactionServiceImp createTransactionServiceImp() {
        Properties properties = new Properties();

        String log_dir = System.getProperty("java.io.tmpdir");
        properties.setProperty(LOG_BASE_DIR_PROPERTY_NAME, log_dir);
        properties.setProperty(OUTPUT_DIR_PROPERTY_NAME, log_dir);
        properties.setProperty(OUTPUT_DIR_PROPERTY_NAME, log_dir);
        properties.setProperty(LOG_BASE_NAME_PROPERTY_NAME, UUIdUtil.generateName());
        properties.setProperty(ENABLE_LOGGING_PROPERTY_NAME, "false");
        properties.setProperty(MAX_TIMEOUT_PROPERTY_NAME, "300000");
        UserTransactionServiceImp transactionServiceImp = new UserTransactionServiceImp(properties);
        return transactionServiceImp;
    }

    @Bean
    public UserTransaction createUserTransaction() throws Throwable {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(60);
        return userTransactionImp;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public TransactionManager createTransactionManager() throws Throwable {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(Boolean.TRUE);
        return userTransactionManager;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() throws Throwable {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager(createUserTransaction(), createTransactionManager());
        jtaTransactionManager.setAllowCustomIsolationLevels(Boolean.TRUE);
        return jtaTransactionManager;
    }


    /**
     * xa事物配置-------end------------------
     */

    /**
     * 创建自动更新sql
     */
    @Bean(name = SQL_INIT_BEAN_NAME, initMethod = "init")
    @ConditionalOnExpression("${auto.init.sql.enabled}")
    public AutoInitSql createAutoInitSql(@Value("${jdbc.write.class}")
                                                 String className, @Value("${jdbc.write.url}") String url,
                                         @Value("${jdbc.write.user}") String username,
                                         @Value("${jdbc.write.pass}") String password) {
        return createInitScript(className, url, username, password);
    }

    /**
     * 创建自动初始化执行sql
     *
     * @param className 加载驱动的名称
     * @param url       数据链接地址
     * @param username  用户名
     * @param password  用户密码
     * @return
     */
    public AutoInitSql createInitScript(String className, String url, String username, String password) {
        return new AutoInitSql(className, url, username, password);
    }


    /**
     * sql动态创建
     *
     * @param dataSource
     * @return
     */
    @Bean(initMethod = "migrate", name = BeanNameConstant.FLYWAY_BEAN_NAME)
    @DependsOnSql
    @ConditionalOnExpression("${open.datasource}")
    public Flyway getMigration(DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setValidateOnMigrate(Boolean.FALSE);
        flyway.setLocations("migration");
        flyway.setBaselineOnMigrate(Boolean.TRUE);
        return flyway;

    }

    /**
     * 动态切换数据库的设置
     */
    public static class DataSourceHolder {
        private static ThreadLocal<String> holder = new ThreadLocal<>();


        public static void setHolder(String key) {
            holder.set(key);

        }

        public static String getHolder() {
            String dynamicDataSourceKey = holder.get();
            holder.remove();
            return dynamicDataSourceKey;
        }


    }


    public class CreateDataSourceRouting extends AbstractRoutingDataSource {


        @Override
        protected Object determineCurrentLookupKey() {
            return DataSourceHolder.getHolder();
        }
    }


}
