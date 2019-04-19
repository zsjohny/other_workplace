package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.HomeFloorDao;
import com.jiuy.core.meta.homepage.HomeFloor;
import com.jiuy.core.meta.homepage.HomeFloorVO;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.entity.query.PageQuery;

@Repository
public class HomeFloorDaoSqlImpl implements HomeFloorDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<HomeFloorVO> search(PageQuery query, String name, int sequence, long activityPlaceId, Collection<String> excludeTemplateNames) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("query", query);
		params.put("name", name);
		params.put("sequence", sequence);
		params.put("activityPlaceId", activityPlaceId);
		params.put("templateNames", excludeTemplateNames);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.search", params);
	}

	@Override
	public int searchCount(String name, long activityPlaceId, Collection<String> excludeTemplateNames) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		params.put("activityPlaceId", activityPlaceId);
		params.put("templateNames", excludeTemplateNames);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.searchCount", params);
	}

	@Override
	public HomeFloor addHomeFloor(HomeFloor homeFloor) {
		sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.addHomeFloor", homeFloor);
		
		return homeFloor;
	}

	@Override
	public int removeHomeFloor(long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.removeHomeFloor", params);
	}

	@Override
	public int updateHomeFloor(HomeFloor homeFloor) {
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.updateHomeFloor", homeFloor);
	}

	@Override
	public HomeFloorVO searchById(long floorId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", floorId);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.searchById", params);
	}

	@Override
	public int updateHomeTemplateId(Long id, Long nextHomeTemplateId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("nextHomeTemplateId", nextHomeTemplateId);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.updateHomeTemplateId", params);
	}

	@Override
	public int publishHomeFloor(long activityPlaceId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("activityPlaceId", activityPlaceId);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.publishHomeFloor", params);
	}

	@Override
	public List<Map<String, Object>> preview(long activityPlaceId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("activityPlaceId", activityPlaceId);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.preview", params);
	}

	@Override
	public List<HomeFloorVO> search(PageQuery pageQuery, String name, Integer sequence, Integer type, Long relatedId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("pageQuery", pageQuery);
		params.put("name", name);
		params.put("sequence", sequence);
		params.put("type", type);
		params.put("relatedId", relatedId);
		params.put("templateNames", AdminConstants.EXCLUDE_TEMPLATE_NAMES);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.search18", params);
	}

	@Override
	public int searchCount(String name, Integer type, Long relatedId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		params.put("type", type);
		params.put("relatedId", relatedId);
		params.put("templateNames", AdminConstants.EXCLUDE_TEMPLATE_NAMES);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.searchCount18", params);
	}

	@Override
	public int add(HomeFloor homeFloor2) {
		long time = System.currentTimeMillis();
		homeFloor2.setCreateTime(time);
		homeFloor2.setUpdateTime(time);
		
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.add", homeFloor2);
	}

	@Override
	public int remove(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("status", -1);
		params.put("updateTime", System.currentTimeMillis());
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.update", params);
	}

	@Override
	public int update(HomeFloor homeFloor2) {
		homeFloor2.setUpdateTime(System.currentTimeMillis());
		Map<String, Object> params = new HashMap<>();
		
		params.put("homeFloor", homeFloor2);
		params.put("updateTime", System.currentTimeMillis());
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.update", params);
	}

	@Override
	public HomeFloorVO search(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("templateNames", AdminConstants.EXCLUDE_TEMPLATE_NAMES);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.search18", params);
	}

	@Override
	public int publish(Integer type, Long relatedId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("type", type);
		params.put("relatedId", relatedId);
		params.put("updateTime", System.currentTimeMillis());
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.publish", params);
	}

	@Override
	public List<Map<String, Object>> preview(Integer type, Long relatedId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("type", type);
		params.put("relatedId", relatedId);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.HomeFloorDaoSqlImpl.preview18", params);
	}

}
