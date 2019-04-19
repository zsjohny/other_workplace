package com.jiuy.core.dao.support.util;

import java.io.Serializable;
import java.util.List;

/*************
 * tair缓存使用的DAO
 * <property key="tairTimeOutMiliSeconds">5000</property>
 * <property key="tairNameSpace">100</property>
 * <property key="tairGroupName">lvguanjia</property>
 * @author zhuliming
 */
public class DAOCacheManagerMemcachedImpl implements DAOCacheManager{

    @Override
    public <T> T getObjectFromCache(String key) {
        return null;
    }

    @Override
    public <T> List<T> getObjectsFromCache(String[] keys) {
        return null;
    }

    @Override
    public void saveObjectIntoCache(String key, Serializable obj) {
        
    }

    @Override
    public void saveObjectIntoCache(String key, Serializable obj, int expireTimeSeconds) {
        
    }

    @Override
    public void deleteObjectsFromCache(String[] keys) {
        
    }

    @Override
    public void deleteObjectFromCache(String key) {
        
    }

    @Override
    public void incrementObjectCount(String key, int increment, int defaultValue) {
        
    }

    @Override
    public String getCacheHitsInformation() {
        return null;
    }

	

}
