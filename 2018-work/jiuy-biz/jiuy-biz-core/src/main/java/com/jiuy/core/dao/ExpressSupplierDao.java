package com.jiuy.core.dao;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.newentity.ExpressSupplier;


public interface ExpressSupplierDao {

	List<ExpressSupplier> search();

	Map<String, ExpressSupplier> itemByEngName();

	Map<String, ExpressSupplier> itemByCnName();

}
