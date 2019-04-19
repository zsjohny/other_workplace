package com.jiuy.core.dao.impl.sql;

import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.modelv2.PropertyNameMapper;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.ProductPropName;

public class PropertyNameMapperSqlImpl extends SqlSupport implements PropertyNameMapper {

    @Override
    public Map<Long, ProductPropName> getPropertyNames() {
        Map<Long,ProductPropName> propNameList = getSqlSession().selectMap("com.yujj.dao.mapper.PropertyNameMapper.getPropertyNames", "id");
        return propNameList;
    }

    @Override
    public int getPropertyNameIdByName(String propertyName) {
        return getSqlSession().selectOne("com.yujj.dao.mapper.PropertyNameMapper.getPropertyNameIdByName",
            propertyName);
    }

	@Override
	public List<String> search() {
		return getSqlSession().selectList("com.yujj.dao.mapper.PropertyNameMapper.search");
	}

}
