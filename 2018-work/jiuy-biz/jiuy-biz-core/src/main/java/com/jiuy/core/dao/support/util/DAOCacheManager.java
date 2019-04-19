package com.jiuy.core.dao.support.util;
import java.io.Serializable;
import java.util.List;

public interface DAOCacheManager{
	
	/**************
	 * 从缓存中获取对象	
	 * @param key
	 * @return
	 */
	public <T> T getObjectFromCache(final String key);
	
	/****************
	 * 批量获取对象, 有几个就返回几个
	 * @param keys
	 * @return
	 */
	public <T> List<T> getObjectsFromCache(final String[] keys);

	/***********
	 * 将对象保存到缓存中
	 * @param key
	 * @param obj
	 * @param expireTimeSeconds
	 */
	public void saveObjectIntoCache(final String key, final Serializable obj);
	
	/***********
	 * 将对象保存到缓存中
	 * @param key
	 * @param obj
	 * @param expireTimeSeconds
	 */
	public void saveObjectIntoCache(final String key, final Serializable obj, final int expireTimeSeconds);
	
	/*****************
	 * 从缓存中删除批量对象
	 * @param keys
	 */
	public void deleteObjectsFromCache(final String[] keys);
	
	/*************
	 * 从缓存中删除对象
	 * @param key
	 */
	public void deleteObjectFromCache(final String key);
	
	/******************
	 * 增加计数缓存，如果不存在就设置为defaultValue
	 * @param key
	 * @param increment
	 * @param defaultValue
	 */
	public void incrementObjectCount(final String key, int increment,
                                     int defaultValue);
	
	/***************
	 * 获取缓存命中信息
	 * @return
	 */
	public String getCacheHitsInformation();
}
