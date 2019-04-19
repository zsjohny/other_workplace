package com.jiuy.core.dao.support;

import com.jiuy.core.exception.ServerUnknownException;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/***************
 * 所有sql dao实现的基类
 * @author zhuliming
 *
 */
public class SqlSupport extends SqlSessionDaoSupport {
	/***********
	 * get the class of meta
	 * @Title: getMetaClass
	 * @Description
	 * @return
	 * @throws
	 */
	protected Class<?> getMetaClass() {
		throw new ServerUnknownException("please add method getMetaClass in subclass of SqlSupport!");
	}
	
	/******************
	 * 获取方法名
	 * @return
	 */
	protected final String getMetaClassName(){
		return getMetaClass().getSimpleName();
	}
	
	/***************
	 * cachedao 已经实现了该方法
	 */
	public final String getTableName() {
		throw new UnsupportedOperationException("SqlSupport.getTableName is not supportted for" +
				" BaseCacheDaoImpl has already implementted this method!");
	}
}