package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;

import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.query.PageQuery;

public interface LOWarehouseDao {

	List<LOWarehouse> srchWarehouse(PageQuery pageQuery, String name);

	int srcWarehouseCount(String name);

	LOWarehouse loadById(long id);

	int add(LOWarehouse warehouse);

	int update(LOWarehouse warehouse);

	int remove(List<Long> ids);

	List<LOWarehouse> warehouseOfIds(Collection<Long> ids);

	int equalWarehouseCount(String name);

	int updateEqualWarehouseCount(long id, String name);

}
