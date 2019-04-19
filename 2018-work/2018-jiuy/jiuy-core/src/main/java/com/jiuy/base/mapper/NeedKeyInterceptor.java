package com.jiuy.base.mapper;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.jiuy.base.util.Biz;




/***
 *@author Aison
 *这个拦截器的作用：
 *由于数据库设计的时候字段太大.. 如果都是selebyid 这种整行查询出来的话
 *会耗费大量资源..所有加了此拦截器只有传递了needKeys这个参数的时候才会
 *走此拦截器
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args={Connection.class}) })
public class NeedKeyInterceptor implements Interceptor {

	
	@SuppressWarnings("unchecked")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		if(invocation.getTarget() instanceof RoutingStatementHandler){    
	            RoutingStatementHandler statementHandler = (RoutingStatementHandler)invocation.getTarget();    
	            StatementHandler delegate = (StatementHandler) ReflectHelper.getFieldValue(statementHandler, "delegate");    
	            BoundSql boundSql = delegate.getBoundSql();  
	            Object obj = boundSql.getParameterObject();  
	           
	            if(obj instanceof Map){
	            	Map<String,Object> param = (Map<String, Object>) obj;
	            	if(param.keySet().contains("needKeys")){
	            		String[] keys = (String[]) param.get("needKeys");
	    	            //获取当前要执行的Sql语句，也就是我们直接在Mapper映射语句中写的Sql语句    
	    	            String sql = filed2Column(boundSql.getSql(),keys);         
	    	            ReflectHelper.setFieldValue(boundSql, "sql", sql);    
	            	}
	            }
	        }    
		return invocation.proceed();
	}

	
	/**
	 *将对象的属性名转换成列的名称
	 *规范是驼峰法.. 
	 **/
	public String   filed2Column(String sql,String[] needKeys){
		int from = sql.indexOf("FROM");
		String afterFrom = sql.substring(from);
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		for(String key:needKeys){
			sb.append(Biz.camelToUnderline(key)).append(",");
		}
		sb.delete(sb.length()-1, sb.length()).append(" ");
		sb.append(afterFrom);
		/*for(String f:fileds){
			if(Biz.valid(f)){
				sql = sql.replace(f.trim(), Biz.camelToUnderline(f));
			}
		}*/
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		
		NeedKeyInterceptor  nk = new NeedKeyInterceptor();
		
		String sql = nk.filed2Column("SELECT * FROM T", new String[]{"id","userName","userAge"});
		
		System.out.println(sql);
	}
	
	@Override
	public Object plugin(Object paramObject) {
		if (paramObject instanceof StatementHandler) {
			return Plugin.wrap(paramObject, this);
		} else {
			return paramObject;
		}
	}
	@Override
	public void setProperties(Properties paramProperties) {

	}
	// @Override
	// public Object intercept(Invocation invocation) throws Throwable {
	//
	// Object[] args = invocation.getArgs();
	// MappedStatement ms = (MappedStatement) args[0];
	// MetaObject msObject = SystemMetaObject.forObject(ms);
	//
	// String id = (String) msObject.getValue("id");
	//
	// // if(id.substring(id.lastIndexOf(".")+1).indexOf("Page")!=-1){
	// // Object parameterObject = args[1];
	// // System.out.println(parameterObject);
	// //
	// // SqlSource sqlSource1 = ms.getSqlSource();
	// //
	// // MetaObject metaObject = SystemMetaObject.forObject(sqlSource1);
	// // SqlNode root = ((SqlNode)metaObject.getValue("rootSqlNode"));
	// //
	// //
	// // Configuration configuration = (Configuration)
	// metaObject.getValue("configuration");
	// // DynamicContext context = new DynamicContext(configuration,
	// parameterObject);
	// // root.apply(context);
	// // SqlSourceBuilder sqlSourceParser = new
	// SqlSourceBuilder(configuration);
	// // Class parameterType = parameterObject == null ? Object.class :
	// parameterObject.getClass();
	// // SqlSource sqlSource = sqlSourceParser.parse(context.getSql(),
	// parameterType, context.getBindings());
	// // BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
	// //
	// // boundSql = sqlSource.getBoundSql(parameterObject);
	// //
	// // for (Map.Entry entry : context.getBindings().entrySet()) {
	// // boundSql.setAdditionalParameter((String)entry.getKey(),
	// entry.getValue());
	// // }
	// //
	// // MappedStatement nms = buildCount(ms);
	// //
	// // SqlSource sqlSource2 = nms.getSqlSource();
	// // System.out.println(nms);
	// // //生成一条新的sql
	// // }
	// return invocation.proceed();
	// }
	//
	// private static final List<ResultMapping> EMPTY_RESULTMAPPING = new
	// ArrayList<ResultMapping>(0);
	//
	// // public static MappedStatement buildCount(MappedStatement ms){
	// // MappedStatement.Builder builder = new
	// MappedStatement.Builder(ms.getConfiguration(), new
	// StringBuilder().append(ms.getId()).append("_COUNT").toString(),
	// ms.getSqlSource(),ms.getSqlCommandType());
	// // builder.resource(ms.getResource());
	// // builder.fetchSize(ms.getFetchSize());
	// // builder.statementType(ms.getStatementType());
	// // builder.keyGenerator(ms.getKeyGenerator());
	// // if ((ms.getKeyProperties() != null)
	// // && (ms.getKeyProperties().length != 0)) {
	// // StringBuilder keyProperties = new StringBuilder();
	// // for (String keyProperty : ms.getKeyProperties()) {
	// // keyProperties.append(keyProperty).append(",");
	// // }
	// // keyProperties.delete(keyProperties.length() - 1,
	// // keyProperties.length());
	// // builder.keyProperty(keyProperties.toString());
	// // }
	// // builder.timeout(ms.getTimeout());
	// // builder.parameterMap(ms.getParameterMap());
	// // List resultMaps = new ArrayList();
	// // ResultMap resultMap = new
	// ResultMap.Builder(ms.getConfiguration(),ms.getId(), Integer.TYPE,
	// EMPTY_RESULTMAPPING).build();
	// // resultMaps.add(resultMap);
	// // builder.resultMaps(resultMaps);
	// // builder.resultSetType(ms.getResultSetType());
	// // builder.cache(ms.getCache());
	// // builder.flushCacheRequired(ms.isFlushCacheRequired());
	// // builder.useCache(ms.isUseCache());
	// //
	// // return builder.build();
	// //
	// // }
	//
	// // 拦截类型StatementHandler
	// @Override
	// public Object plugin(Object target) {
	//
	// if (target instanceof Executor) {
	// return Plugin.wrap(target, this);
	// } else {
	// return target;
	// }
	// }
	//
	// @Override
	// public void setProperties(Properties properties) {
	// }
	//
	// public String concatCountSql(String sql) {
	// StringBuffer sb = new StringBuffer("select count(*) from ");
	// sql = sql.toLowerCase();
	//
	// if (sql.lastIndexOf("order") > sql.lastIndexOf(")")) {
	// sb.append(sql.substring(sql.indexOf("from") + 4,
	// sql.lastIndexOf("order")));
	// } else {
	// sb.append(sql.substring(sql.indexOf("from") + 4));
	// }
	// return sb.toString();
	// }
	//
	// public String concatPageSql(String sql, Page<?> page) {
	// StringBuffer sb = new StringBuffer();
	// sb.append(sql);
	// sb.append(" limit ").append(page.getBeginIndex()).append(" , ")
	// .append(page.getPageSize());
	// return sb.toString();
	// }

}