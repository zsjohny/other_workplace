package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.GlobalSettingDao;
import com.jiuy.core.meta.global.ProductSeasonWeight;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.entity.GlobalSetting;

@Repository
public class GlobalSettingSqlImpl implements GlobalSettingDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int update(GlobalSetting globalSetting) {
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.GlobalSettingSqlImpl.update", globalSetting);
	}
	
	@Override
	public int deleteSetting(String propertyName) {
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.GlobalSettingSqlImpl.deleteSetting", propertyName);
	}

	@Override
	public GlobalSetting getSettingByPropertyName(String propertyName) {
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.GlobalSettingSqlImpl.getSettingByPropertyName", propertyName);
	}

	@Override
	public int add(GlobalSetting globalSetting) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.GlobalSettingSqlImpl.add", globalSetting);
	}

	@Override
	public List<GlobalSetting> getItems(Collection<String> propertyNames) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("propertyNames", propertyNames);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.GlobalSettingSqlImpl.settingsOfName", params);
	}

	@Override
	public String getSetting(GlobalSettingName name) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("propertyName", name.getStringValue());
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.GlobalSettingSqlImpl.search", params);
	}

	@Override
	public int update(String propertyName, String propertValue) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("propertyName", propertyName);
		params.put("propertyValue", propertValue);
		params.put("updateTime", System.currentTimeMillis());
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.GlobalSettingSqlImpl.update", params);
	}

	@Override
	public int addProductSeasonWeight(Collection<ProductSeasonWeight> productSeasonWeights) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productSeasonWeights", productSeasonWeights);
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.GlobalSettingSqlImpl.addProductSeasonWeight", params);
	}

	@Override
	public int resetProductSeasonWeight() {
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.GlobalSettingSqlImpl.resetProductSeasonWeight");
	}

	@Override
	public String getSettingByStringPropertyName(String propertyName) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("propertyName", propertyName);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.GlobalSettingSqlImpl.search", params);
	}
	
}
