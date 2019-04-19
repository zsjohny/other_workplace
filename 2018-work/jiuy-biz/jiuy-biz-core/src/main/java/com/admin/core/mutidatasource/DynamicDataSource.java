package com.admin.core.mutidatasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 动态数据源
 *
 * @author fengshuonan
 * @date 2017年3月5日 上午9:11:49
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	private DruidDataSource dataSourceGuns;
	private DruidDataSource bizDataSource;

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getDataSourceType();
	}

	public void init(DruidDataSource dataSourceGuns, DruidDataSource bizDataSource) {
		this.dataSourceGuns = dataSourceGuns;
		this.bizDataSource = bizDataSource;
	}

	// 销毁方法
	public void destroy() {
		System.out.println("----------------------destroy executed----------------------------");
		dataSourceGuns.close();
		bizDataSource.close();
	}
}
