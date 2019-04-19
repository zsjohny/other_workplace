package com.jiuyuan.service.common;

import java.util.Collection;
import java.util.Map;

import com.jiuyuan.entity.logistics.LOWarehouse;

public interface ILOWarehouseNewService {

	/**
	 * 获取对应的仓库并封装
	 * @param ids
	 * @return
	 */
	Map<Long, LOWarehouse> getWarehouseMap(Collection<Long> ids);

	LOWarehouse getById(long id);

}