package project.config.datasource;

import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.admin.common.constant.DSEnum;
import com.admin.core.mutidatasource.DynamicDataSource;
import com.admin.core.util.ResKit;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;

/**
 * 数据源配置
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午4:55:09
 */
@Configuration
@EnableTransactionManagement(order = 2) // 由于引入多数据源，所以让spring事务的aop要在多数据源切换aop的后面
@PropertySource("classpath:jdbc.properties")
public class DataSourceConfig implements EnvironmentAware {

	private Environment em;

	/**
	 * 扫描所有mybatis的接口
	 *
	 * @author fengshuonan
	 */
	@Bean
	public MapperScannerConfigurer mappers() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage(
				"com.jiuy.wxaproxy.common.system.dao;com.jiuy.wxaproxy.common.system.persistence.dao;com.store.dao;com.jiuyuan.dao");
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		return mapperScannerConfigurer;
	}

	/**
	 * spring和MyBatis整合
	 *
	 * @author fengshuonan
	 */
	@Bean
	public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource, GlobalConfiguration globalConfig) {
		MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		Resource[] classPathResources = ResKit.getClassPathResources("classpath*:*/**/mapper/supplier/*.xml");
		sqlSessionFactory.setMapperLocations(classPathResources);

		// 以下为mybatis-plus配置
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		paginationInterceptor.setDialectType("mysql");
		sqlSessionFactory.setPlugins(new Interceptor[] { paginationInterceptor });
		sqlSessionFactory.setGlobalConfig(globalConfig);
		return sqlSessionFactory;
	}

	/**
	 * mybatis-plus的配置
	 *
	 * @author fengshuonan
	 * @date 2017年2月22日 下午3:39:28
	 */
	@Bean
	public GlobalConfiguration globalConfig() {
		GlobalConfiguration globalConfig = new GlobalConfiguration();
		/**
		 * AUTO->`0`("数据库ID自增") INPUT->`1`(用户输入ID") ID_WORKER->`2`("全局唯一ID")
		 * UUID->`3`("全局唯一ID")
		 */
		globalConfig.setIdType(1);
		globalConfig.setDbType("mysql");

		/**
		 * 全局表为下划线命名设置
		 */
		globalConfig.setDbColumnUnderline(false);

		return globalConfig;
	}

	/**
	 * 事务管理, 声明式事务的开启
	 *
	 * @author fengshuonan
	 */
	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		DataSourceTransactionManager manager = new DataSourceTransactionManager();
		manager.setDataSource(dataSource);
		return manager;
	}

	/**
	 * 第三方数据库连接池的配置
	 *
	 * @author fengshuonan
	 * @Date 2017/4/27 17:24
	 */
	public DruidDataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(em.getProperty("jdbc.url").trim());
		dataSource.setUsername(em.getProperty("jdbc.username").trim());
		dataSource.setPassword(em.getProperty("jdbc.password").trim());

		DataSourceConfigTemplate.config(dataSource);
		return dataSource;
	}

	/**
	 * 第三方数据库连接池的配置
	 *
	 * @author fengshuonan
	 * @Date 2017/4/27 17:24
	 */
	public DruidDataSource dataSource1() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(em.getProperty("jdbc1.url").trim());
		dataSource.setUsername(em.getProperty("jdbc1.username").trim());
		dataSource.setPassword(em.getProperty("jdbc1.password").trim());

		DataSourceConfigTemplate.config(dataSource);
		return dataSource;
	}

	/**
	 * 多数据源连接池配置
	 */
	@Bean(destroyMethod = "destroy")
	public DynamicDataSource mutiDataSource() {

		DruidDataSource dataSourceGuns = dataSource();
		DruidDataSource bizDataSource = dataSource1();

		try {
			dataSourceGuns.init();
			bizDataSource.init();
		} catch (SQLException sql) {
			sql.printStackTrace();
		}

		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
		hashMap.put(DSEnum.DATA_SOURCE_GUNS, dataSourceGuns);
		hashMap.put(DSEnum.DATA_SOURCE_BIZ, bizDataSource);
		dynamicDataSource.setTargetDataSources(hashMap);
		dynamicDataSource.setDefaultTargetDataSource(dataSourceGuns);

		dynamicDataSource.init(dataSourceGuns, bizDataSource);
		return dynamicDataSource;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.em = environment;
	}

}
