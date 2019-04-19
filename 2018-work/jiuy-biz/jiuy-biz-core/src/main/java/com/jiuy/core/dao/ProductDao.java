package com.jiuy.core.dao;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuyuan.entity.ProductV1;

import java.util.List;

/**
 * Created by Jinwu Zhan on 15/6/2.
 */
public interface ProductDao extends DomainDao<ProductV1, Long> {

    public List<ProductV1> loadAll();

	public ProductV1 getProductByProperty(String property);

	public List<ProductV1> getProductList(String startPage, String pageSize,
			String productType);

}
