package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.ProductDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.ProductV1;

/**
 * Created by Jinwu Zhan on 15/6/2.
 */
public class ProductDaoSqlImpl extends DomainDaoSqlSupport<ProductV1, Long>  implements ProductDao {

    @Override
    protected Class<?> getMetaClass() {
        return ProductV1.class;
    }

    @Override
    public List<ProductV1> loadAll() {
        return getSqlSession().selectList(getMetaClassName() + ".loadAll");
    }

	@Override
	public ProductV1 getProductByProperty(String property) {
		return getSqlSession().selectOne("",property);
	}

	@Override
	public List<ProductV1> getProductList(String startPage, String pageSize,
			String productType) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startPage", Integer.parseInt(startPage) * Integer.parseInt(pageSize));
		params.put("pageSize", pageSize);
		params.put("classification", productType);
		return getSqlSession().selectList(getMetaClassName() + ".loadList", params);
	}
}
