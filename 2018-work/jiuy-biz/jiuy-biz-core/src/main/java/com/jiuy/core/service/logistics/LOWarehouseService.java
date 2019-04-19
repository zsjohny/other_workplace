package com.jiuy.core.service.logistics;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.query.PageQuery;

public interface LOWarehouseService {

	List<LOWarehouse> srchWarehouse(PageQuery pageQuery, String name, boolean isCache);

	int srcWarehouseCount(String name);

	LOWarehouse loadById(long id);

	int add(LOWarehouse warehouse);

	int update(LOWarehouse warehouse);

	int remove(List<Long> ids);

	List<LOWarehouse> warehouseOfIds(Collection<Long> ids);

	Map<Long, LOWarehouse> getWarehouseMap(Collection<Long> ids);

	int equalWarehouseCount(String name);

	int updateEqualWarehouseCount(long id, String name);

}
