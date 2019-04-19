package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.LOWarehouseDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.query.PageQuery;

public class LOWarehouseDaoSqlImpl extends SqlSupport implements LOWarehouseDao{

	@Override
	public List<LOWarehouse> srchWarehouse(PageQuery pageQuery, String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		params.put("pageQuery", pageQuery);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.LOWarehouseDaoSqlImpl.srchWarehouse", params);
	}

	@Override
	public int srcWarehouseCount(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.LOWarehouseDaoSqlImpl.srcWarehouseCount", params);
	}

	@Override
	public LOWarehouse loadById(long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.LOWarehouseDaoSqlImpl.loadById", params);
	}

	@Override
	public int add(LOWarehouse warehouse) {
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.LOWarehouseDaoSqlImpl.add", warehouse);
	}

	@Override
	public int update(LOWarehouse warehouse) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.LOWarehouseDaoSqlImpl.update", warehouse);
	}

	@Override
	public int remove(List<Long> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("ids", ids);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.LOWarehouseDaoSqlImpl.remove", params);
	}

	@Override
	public List<LOWarehouse> warehouseOfIds(Collection<Long> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("ids", ids);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.LOWarehouseDaoSqlImpl.warehouseOfIds", params);
	}

	@Override
	public int equalWarehouseCount(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.LOWarehouseDaoSqlImpl.equalWarehouseCount", params);
	}

	@Override
	public int updateEqualWarehouseCount(long id, String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("name", name);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.LOWarehouseDaoSqlImpl.updateEqualWarehouseCount", params);
	}

}
