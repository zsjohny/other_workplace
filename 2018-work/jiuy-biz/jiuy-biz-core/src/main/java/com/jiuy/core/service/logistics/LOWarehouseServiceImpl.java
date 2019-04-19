package com.jiuy.core.service.logistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.LOLocationDao;
import com.jiuy.core.dao.LOWarehouseDao;
//import com.jiuy.core.service.common.MemcachedService;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.logistics.LOLocation;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.MemcachedService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class LOWarehouseServiceImpl implements LOWarehouseService{

	private static final Log logger = LogFactory.get();
	@Autowired
	private LOWarehouseDao lOWarehouseDao;

	@Autowired
	private MemcachedService memcachedService;
	
	@Autowired
	private LOLocationDao lOLocationDao;
	
//	public List<GlobalSetting> getNavigationSetting() {
//		String groupKey = MemcachedKey.GROUP_KEY_GLOBAL_SETTING;
//        String key = "Navigation";
//        Object obj = memcachedService.get(groupKey, key);
//        if (obj != null) {
//            return (List<GlobalSetting>) obj;
//        }
//
//        List<GlobalSetting> setting = globalSettingDao.getNavigationSetting();
//        if (setting != null) {
//            memcachedService.set(groupKey, key, 1000, setting);
//        }
//		return setting;
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LOWarehouse> srchWarehouse(PageQuery pageQuery, String name, boolean isCache) {
		List<LOWarehouse> warehouses = new ArrayList<>();
		if (isCache) {
			String groupKey = MemcachedKey.GROUP_KEY_LOWAREHOUSE;
			String suffix = name == null ? "" : name;
			String key = "all" + suffix;
			
			Object obj = memcachedService.get(groupKey, key);
			if (obj != null) {
				return (List<LOWarehouse>)obj;
			} 
			
			warehouses = lOWarehouseDao.srchWarehouse(pageQuery, name);
			addDeliveryLocation(warehouses);
			if (CollectionUtils.isNotEmpty(warehouses)) {
				memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, warehouses);
			}
			
		} else {
			warehouses = lOWarehouseDao.srchWarehouse(pageQuery, name);
			addDeliveryLocation(warehouses);
		}
		
		return warehouses;
	}
	
	public void addDeliveryLocation(List<LOWarehouse> warehouses){
		for(LOWarehouse warehouse:warehouses){
			long deliveryLocation = warehouse.getDeliveryLocation();
			if(deliveryLocation != 0){
//				logger.info("物流仓库id，deliveryLocation:"+deliveryLocation);
				LOLocation lOLocation = lOLocationDao.getById(deliveryLocation);
				if(lOLocation == null){
					logger.info("物流仓库id，deliveryLocation:"+deliveryLocation+",物流仓库为空，lOLocation"+lOLocation);
				}else{
					warehouse.setCityName(lOLocation.getCityName());
					warehouse.setProvinceName(lOLocation.getProvinceName());
				}
			}else{
				logger.info("LOWarehouse的deliveryLocation字段为0，仓库所属城市ID不能为0，请尽快排查问题！！！！！！！！！！！！！！！！！！！！！！！");
			}
		}
	}

	@Override
	public int srcWarehouseCount(String name) {
		return lOWarehouseDao.srcWarehouseCount(name);
	}

	@Override
	public LOWarehouse loadById(long id) {
		return lOWarehouseDao.loadById(id);
	}

	@Override
	public int add(LOWarehouse warehouse) {
		return lOWarehouseDao.add(warehouse);
	}

	@Override
	public int update(LOWarehouse warehouse) {
		return lOWarehouseDao.update(warehouse);
	}

	@Override
	public int remove(List<Long> ids) {
		return lOWarehouseDao.remove(ids);
	}

	@Override
	public List<LOWarehouse> warehouseOfIds(Collection<Long> ids) {
		return lOWarehouseDao.warehouseOfIds(ids);
	}

	@Override
	public Map<Long, LOWarehouse> getWarehouseMap(Collection<Long> ids) {
		Map<Long, LOWarehouse> warehouseMap = new HashMap<Long, LOWarehouse>();
		
		if(ids == null) {
			List<LOWarehouse> loWarehouses = lOWarehouseDao.srchWarehouse(null, "");
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

	@Override
	public int equalWarehouseCount(String name) {
		return lOWarehouseDao.equalWarehouseCount(name);
	}

	@Override
	public int updateEqualWarehouseCount(long id, String name) {
		return lOWarehouseDao.updateEqualWarehouseCount(id,name);
	}

}
