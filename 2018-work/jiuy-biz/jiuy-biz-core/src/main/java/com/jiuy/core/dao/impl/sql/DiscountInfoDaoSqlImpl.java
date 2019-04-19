package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.DiscountInfoDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.shopping.DiscountInfo;

public class DiscountInfoDaoSqlImpl extends SqlSupport implements DiscountInfoDao {

    @Override
    public int batchAdd(Collection<DiscountInfo> multipleDiscounts) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("collection", multipleDiscounts);
        params.put("currentTime", System.currentTimeMillis());

        return getSqlSession().insert("com.jiuy.core.dao.impl.sql.DiscountInfoDaoSqlImpl.batchAdd", params);
    }

    @Override
    public int delete(int type, long relatedId) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("type", type);
        params.put("relatedId", relatedId);
        params.put("currentTime", System.currentTimeMillis());

        return getSqlSession().update("com.jiuy.core.dao.impl.sql.DiscountInfoDaoSqlImpl.delete", params);
    }

    @Override
    public List<DiscountInfo> getDiscount(int type, long relatedId) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("type", type);
        params.put("relatedId", relatedId);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.DiscountInfoDaoSqlImpl.getDiscount", params);
    }

    @Override
    public List<DiscountInfo> discountsOfType(int type) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("type", type);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.DiscountInfoDaoSqlImpl.discountsOfType", params);
    }

	@Override
	public int batchDelete(List<DiscountInfo> discounts) {
        return getSqlSession().update("com.jiuy.core.dao.impl.sql.DiscountInfoDaoSqlImpl.batchDelete");
	}

	@Override
	public List<DiscountInfo> itemsOfRelatedIdType(int type, Collection<Long> relatedIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("type", type);
		params.put("relatedIds", relatedIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.DiscountInfoDaoSqlImpl.itemsOfRelatedIdType", params);
	}

}
