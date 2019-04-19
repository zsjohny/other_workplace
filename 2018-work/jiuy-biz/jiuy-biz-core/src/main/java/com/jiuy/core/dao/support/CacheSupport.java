package com.jiuy.core.dao.support;

import com.jiuy.core.dao.support.util.DAOCacheManager;
import com.jiuyuan.entity.BaseMeta;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * ************
 * 所有cache dao实现的基类
 *
 * @param <TObject>
 * @param <TId>
 * @param <TDaoSql>
 * @author zhuliming
 */
public abstract class CacheSupport<TObject extends BaseMeta<TId>, TId extends Object, TDaoSql extends SqlSupport> {

    protected TDaoSql sqlDao; // sql实现

    protected DAOCacheManager cacheManager; // 缓存处理器

    protected String tableName; // 数据库表名

    private static final Logger logger = Logger.getLogger(CacheSupport.class);

    /**
     * ************
     * 获取表名
     *
     * @return
     */
    public final String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * ************
     * dao-sql实现
     *
     * @return
     */
    public final TDaoSql getSqlDao() {
        return sqlDao;
    }

    public void setSqlDao(TDaoSql sqlDao) {
        this.sqlDao = sqlDao;
    }

    /**
     * ********
     * cache manager
     *
     * @return
     */
    public final DAOCacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(DAOCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public TObject getObjectFromCache(Object cacheId) {
        String key = BaseMeta.getObjectCacheKey(tableName, cacheId);
        final TObject object = this.cacheManager.getObjectFromCache(key);
        if (object != null) {
            logger.info("GetObjectFromCache hit, key is " + key);
        }
        return object;
    }

    public List<TObject> getObjectsFromCache(Object[] cacheIds) {
        return this.cacheManager.getObjectsFromCache(BaseMeta.getObjectCacheKeys(tableName, cacheIds));
    }

    public void saveObjectIntoCache(TObject object) {
        this.cacheManager.saveObjectIntoCache(
                object.getObjectCacheKey(tableName), object);
    }

    public void saveObjectIntoCache(TObject object, int expireTimeSeconds) {
        this.cacheManager.saveObjectIntoCache(
                object.getObjectCacheKey(tableName), object, expireTimeSeconds);
    }

    public void deleteObjectFromCache(Object cacheId) {
        this.cacheManager.deleteObjectFromCache(BaseMeta.getObjectCacheKey(
                tableName, cacheId));
    }

    public void deleteObjectsFromCache(Object[] cacheIds) {
        this.cacheManager.deleteObjectsFromCache(BaseMeta.getObjectCacheKeys(tableName, cacheIds));
    }
}