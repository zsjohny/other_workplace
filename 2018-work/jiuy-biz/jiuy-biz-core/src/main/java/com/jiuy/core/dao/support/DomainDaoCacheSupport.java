package com.jiuy.core.dao.support;


import java.util.List;

import com.jiuyuan.entity.BaseMeta;

/**
 * ************
 * 所有cache dao实现的基类
 *
 * @param <TObject>
 * @param <TId>
 * @param <TDaoSql>
 * @author zhuliming
 */
public abstract class DomainDaoCacheSupport<TObject extends BaseMeta<TId>, TId extends Object, TDaoSql extends DomainDaoSqlSupport<TObject, TId>> extends CacheSupport<TObject, TId, TDaoSql>
        implements DomainDao<TObject, TId> {
    /**
     * *******
     * Default Cache Implements
     */
    @Override
    public TObject getById(TId id) {
        TObject object = this.getObjectFromCache(id);
        if (object == null) {
            object = sqlDao.getById(id);
            if (object != null) {
                this.saveObjectIntoCache(object);
            }
        }
        return object;
    }

    @Override
    public TObject add(TObject obj) {
        return sqlDao.add(obj);
    }

    @Override
    public boolean update(TObject newObject) {
        if(sqlDao.update(newObject)) {
            this.deleteObjectFromCache(newObject.getCacheId());
            return  true;
        }
        return false;
    }

    @Override
    public boolean changeCount(TId id, String field, int deltaCount) {

        if(sqlDao.changeCount(id, field, deltaCount)) {
            this.deleteObjectFromCache(id);
            return true;
        }
        return false;
    }


    @Override
    public List<TObject> listByIds(TId[] ids) {
        List<TObject> objects = this.getObjectsFromCache(ids);
        if (objects == null || objects.size() != ids.length) {
            objects = sqlDao.listByIds(ids);
            for (TObject post : objects) {
                this.saveObjectIntoCache(post);
            }
        }
        return objects;
    }

    @SuppressWarnings("unchecked")
	@Override
    public int deleteByIds(TId... ids) {
        int count = sqlDao.deleteByIds(ids);
        if (count > 0) {
            this.deleteObjectsFromCache(ids);
        }
        return count;
    }

}