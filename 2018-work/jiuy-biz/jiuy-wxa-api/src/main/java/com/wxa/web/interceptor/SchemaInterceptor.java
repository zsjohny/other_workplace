/**
 * 
 */
package com.wxa.web.interceptor;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.toolkit.PluginUtils;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.wxa.SchemaContext;

/**
 * @author DongZhong
 * @version 创建时间: 2017年7月10日 上午11:14:06
 */
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class SchemaInterceptor implements Interceptor {
	
	private static final Log logger = LogFactory.getLog(SchemaInterceptor.class);

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
		MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);

		BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");

		String originalSql = boundSql.getSql();
		
		System.out.println("sql1:" + originalSql + "______" + SchemaContext.STORE_ID_HOLDER.get());
		originalSql = originalSql.replaceAll("shop_", "yjj2016.shop_");
		System.out.println("sql2:" + originalSql + "______" + SchemaContext.STORE_ID_HOLDER.get());
		metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	@Override
	public void setProperties(Properties prop) {

	}
	public static void main(String[] args) {
		System.out.println("SELECT COUNT(1) FROM shop_member_favorite ".replaceAll("shop_", "yjj2016.shop_"));
	}
}
