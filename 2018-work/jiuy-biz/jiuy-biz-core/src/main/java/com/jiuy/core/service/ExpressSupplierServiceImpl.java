package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.ExpressSupplierDao;
import com.jiuyuan.entity.newentity.ExpressSupplier;

@Service
public class ExpressSupplierServiceImpl implements ExpressSupplierService{

	@Autowired
	private ExpressSupplierDao expressSupplierDao;
	
	@Override
	public List<ExpressSupplier> search() {
		return expressSupplierDao.search();
	}

	@Override
	public Map<String, ExpressSupplier> itemByCnName() {
		Map<String, ExpressSupplier> map = expressSupplierDao.itemByCnName();
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (ConcurrentHashMap.Entry<String, ExpressSupplier> entry : map.entrySet()) {
			String key = entry.getKey();
			ExpressSupplier expressSupplier = entry.getValue();
			if (key.contains("/")) {
				String[] keys = key.split("/");
				for (String supplier : keys) {
					Map<String, Object> subMap = new HashMap<String, Object>();
					subMap.put("supplier", supplier);
					subMap.put("object", expressSupplier);
					
					list.add(subMap);
				}
			}
		}
		
		for (Map<String, Object> subMap : list) {
			map.put(subMap.get("supplier").toString(), (ExpressSupplier) subMap.get("object"));
		}
		
		return map;
	}

	@Override
	public Map<String, ExpressSupplier> itemByEngName() {
		return expressSupplierDao.itemByEngName();
	}
	
}
