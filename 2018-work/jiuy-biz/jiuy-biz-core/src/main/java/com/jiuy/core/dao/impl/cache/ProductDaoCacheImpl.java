package com.jiuy.core.dao.impl.cache;

import com.jiuy.core.dao.ProductDao;
import com.jiuy.core.dao.impl.sql.ProductDaoSqlImpl;
import com.jiuy.core.dao.support.DomainDaoCacheSupport;
import com.jiuyuan.entity.ProductV1;

import java.util.List;

/**
 * Created by Jinwu Zhan on 15/6/2.
 */
public class ProductDaoCacheImpl extends DomainDaoCacheSupport<ProductV1, Long, ProductDaoSqlImpl> implements ProductDao {
    @Override
    public List<ProductV1> loadAll() {
        return sqlDao.loadAll();
    }

	@Override
	public ProductV1 getProductByProperty(String property) {
		// 目前暂时不实现缓存
		return sqlDao.getProductByProperty(property);
	}

	@Override
	public List<ProductV1> getProductList(String startPage, String pageSize,
			String productType) {
		// 目前暂不实现缓存
		return sqlDao.getProductList(startPage, pageSize, productType);
	}
}
