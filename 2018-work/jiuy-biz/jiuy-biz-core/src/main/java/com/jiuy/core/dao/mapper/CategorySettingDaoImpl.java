package com.jiuy.core.dao.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.CategorySetting;
import com.jiuyuan.entity.query.PageQuery;

@Repository
public class CategorySettingDaoImpl implements CategorySettingDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int searchCount(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.CategorySettingDaoImpl.searchCount", params);
	}

	@Override
	public List<CategorySetting> search(PageQuery pageQuery, String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		params.put("pageQuery", pageQuery);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.CategorySettingDaoImpl.search", params);
	}

	@Override
	public int add(CategorySetting categorySetting) {
		long time = System.currentTimeMillis();
		categorySetting.setCreateTime(time);
		categorySetting.setUpdateTime(time);
		
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.CategorySettingDaoImpl.add", categorySetting);
	}

	@Override
	public int remove(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("status", -1);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.CategorySettingDaoImpl.update", params);
	}

	@Override
	public int update(CategorySetting categorySetting) {
		long time = System.currentTimeMillis();
		categorySetting.setUpdateTime(time);
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("categorySetting", categorySetting);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.CategorySettingDaoImpl.update", params);
	}

	@Override
	public int update(Long id, String content) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("content", content);
		params.put("updateTime", System.currentTimeMillis());
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.CategorySettingDaoImpl.update", params);
	}

	@Override
	public CategorySetting search(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.CategorySettingDaoImpl.search", params);
	}

	@Override
	public List<CategorySetting> search(Integer linkType) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("linkType", linkType);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.CategorySettingDaoImpl.search", params);
	}
	
}
