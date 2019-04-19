package com.jiuy.core.service;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.newentity.ExpressSupplier;


public interface ExpressSupplierService {

	List<ExpressSupplier> search();

	Map<String, ExpressSupplier> itemByCnName();

	Map<String, ExpressSupplier> itemByEngName();

}
