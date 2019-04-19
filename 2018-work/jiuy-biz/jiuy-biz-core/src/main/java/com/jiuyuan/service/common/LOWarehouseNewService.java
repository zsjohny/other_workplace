package com.jiuyuan.service.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.dao.mapper.supplier.LOWarehouseMapper;
import com.jiuyuan.entity.logistics.LOWarehouse;

@Service
public class LOWarehouseNewService implements ILOWarehouseNewService {

	@Autowired
	LOWarehouseMapper loWarehouseMapper;

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.ILOWarehouseNewService#getWarehouseMap(java.util.Collection)
	 */
	@Override
	public Map<Long, LOWarehouse> getWarehouseMap(Collection<Long> ids) {
		Map<Long, LOWarehouse> warehouseMap = new HashMap<Long, LOWarehouse>();
		
		if(ids == null) {
			List<LOWarehouse> loWarehouses = loWarehouseMapper.srchWarehouse(null, "");
			for(LOWarehouse loWarehouse : loWarehouses) {
				warehouseMap.put(loWarehouse.getId(), loWarehouse);
			}
			return warehouseMap;
		}
		
		if(ids.size() < 1) {
			return warehouseMap;
		}
		
		return warehouseMap;
	}
	
	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.ILOWarehouseNewService#getById(long)
	 */
	@Override
	public LOWarehouse getById(long id) {
		return loWarehouseMapper.getById(id);
	}

}