package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.modelv2.PropertyValueMapper;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.ProductPropValue;

public class PropertyValueMapperSqlImpl extends SqlSupport implements PropertyValueMapper {

    @Override
    public Map<Long, ProductPropValue> getPropertyValues() {
        Map<Long, ProductPropValue> propValueList = getSqlSession().selectMap("com.jiuy.core.dao.impl.sql.PropertyValueMapperSqlImpl.getPropertyValues", "id");
        
        return propValueList;
    }

    @Override
    public Map<String, ProductPropValue> getPropertyValueMap() {
    	return getSqlSession().selectMap("com.jiuy.core.dao.impl.sql.PropertyValueMapperSqlImpl.getPropertyValueMap","propertyValue");
    }

	@Override
	public List<Integer> getBrandIds() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.PropertyValueMapperSqlImpl.getBrandIds");
    }

	@Override
	public List<Map<String, Object>> getColors() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.PropertyValueMapperSqlImpl.getColors");
	}

	@Override
	public List<Map<String, Object>> getBrands() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.PropertyValueMapperSqlImpl.getBrands");
	}

	@Override
	public int remove(Collection<Long> brandIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("ids", brandIds);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.PropertyValueMapperSqlImpl.remove", params);
	}
	
	@Override
	public ProductPropValue addBrand(ProductPropValue ppv) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.PropertyValueMapperSqlImpl.addBrand", ppv);
		return ppv;
	}

	@Override
	public int chkRepeat(int propertyNameId, String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		
        params.put("propertyNameId", propertyNameId);
        params.put("name", name);
        
        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.PropertyValueMapperSqlImpl.chkRepeat", params);
	}

	@Override
	public List<ProductPropValue> getValueMap(Collection<Long> propertyNameIds) {
		Map<String, Object> params = new HashMap<String, Object>(); 
		
		params.put("propertyNameIds", propertyNameIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.PropertyValueMapperSqlImpl.getValueMap", params);
	}

	@Override
	public List<ProductPropValue> getPropertyValuesByNameId(Collection<Long> propertyNameIds) {
		Map<String,Object> params = new HashMap<String,Object>();
		
        params.put("propertyNameIds", propertyNameIds);
        
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.PropertyValueMapperSqlImpl.getPropertyValuesByNameId", params);
	}

	@Override
	public ProductPropValue add(ProductPropValue ppv) {
        getSqlSession().insert("com.jiuy.core.dao.impl.sql.PropertyValueMapperSqlImpl.add", ppv);
        
        return ppv;
	}

}
