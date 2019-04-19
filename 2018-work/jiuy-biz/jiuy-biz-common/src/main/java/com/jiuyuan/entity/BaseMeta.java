package com.jiuyuan.entity;

import java.io.Serializable;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.jiuy.core.exception.ServerUnknownException;

public abstract class BaseMeta<TId extends Object> implements Serializable {

	private static final long serialVersionUID = -1873706224266847662L;

	public abstract TId getCacheId();

	/******************
	 * 创建缓存对象的key
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 */
	public String getObjectCacheKey(final String tableName) {
		return getObjectCacheKey(tableName, getCacheId());
	}

	/**
	 * 生成缓存对象的key，根据唯一key生成
	 * */
	public static String getObjectCacheKey(final String tableName,
			Object uniqueKey) {
		return tableName + "_" + uniqueKey;
	}
	
	/**
	 * 批量生成缓存对象的key，根据唯一key数组生成
	 * */
	public static String[] getObjectCacheKeys(final String tableName,
			Object[] uniqueKeys) {
		final String[] keys = new String[uniqueKeys.length];
		for (int i = 0; i < uniqueKeys.length; i++) {
			keys[i] = getObjectCacheKey(tableName, uniqueKeys[i]);
		}
		return keys;
	}

	/************
	 * 转化成string
	 */
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	/**************
	 * 复制对象
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final <T extends BaseMeta<?>> T cloneMetaObject(final T obj) {
		if (obj == null) {
			return null;
		}
		try {
			final T result = (T) obj.getClass().newInstance();
			PropertyUtils.copyProperties(result, obj);
			return result;
		} catch (Exception e) {
			throw new ServerUnknownException(
					"Fail to cloneObject from obj of class "
							+ obj.getClass().getName(), e);
		}
	}
}
