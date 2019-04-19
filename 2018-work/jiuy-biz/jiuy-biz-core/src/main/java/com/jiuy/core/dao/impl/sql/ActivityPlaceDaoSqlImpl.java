package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jiuy.core.dao.ActivityPlaceDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.ActivityPlace;
import com.jiuyuan.entity.query.PageQuery;

public class ActivityPlaceDaoSqlImpl extends SqlSupport implements ActivityPlaceDao{

	@Override
	public ActivityPlace add(String name, String description, long currentTime) {
		logger.info("ActivityPlaceServiceImpl添加模板name："+name+",description:"+description);
		ActivityPlace activityPlace = new ActivityPlace();

		activityPlace.setName(name);
		activityPlace.setType(1);
		activityPlace.setDescription(description);
		activityPlace.setCreateTime(currentTime);
		activityPlace.setUpdateTime(currentTime);
		logger.info("1ActivityPlaceDaoSqlImplactivityPlace："+JSON.toJSONString(activityPlace));
		getSqlSession().insert("ActivityPlaceMapper.add", activityPlace);
		logger.info("2ActivityPlaceDaoSqlImplactivityPlace："+JSON.toJSONString(activityPlace));
		return activityPlace;
	}

	@Override
	public int update(long id, String name, String description, long currentTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("name", name);
		params.put("description", description);
		params.put("currentTime", currentTime);
		
		return getSqlSession().update("ActivityPlaceMapper.update", params);
	}

	@Override
	public int updateUrl(long id, String url) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("url", url);
		
		return getSqlSession().update("ActivityPlaceMapper.updateUrl", params);
	}

	@Override
	public List<ActivityPlace> search(String name, int type, PageQuery pageQuery) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		params.put("type", type);
		params.put("pageQuery", pageQuery);
		
		return getSqlSession().selectList("ActivityPlaceMapper.search", params);
	}

	@Override
	public int searchCount(String name, int type) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		params.put("type", type);
		
		return getSqlSession().selectOne("ActivityPlaceMapper.searchCount", params);
	}

	@Override
	public int delete(long activityPlaceId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("activityPlaceId", activityPlaceId);
		
		return getSqlSession().update("ActivityPlaceMapper.delete", params);
	}

	@Override
	public int updateActivityPlaceType(long activityPlaceId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("activityPlaceId", activityPlaceId);
		
		return getSqlSession().update("ActivityPlaceMapper.updateActivityPlaceType", params);
	}

	@Override
	public ActivityPlace getActivityPlaceById(long activityPlaceId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("activityPlaceId", activityPlaceId);
		
		return getSqlSession().selectOne("ActivityPlaceMapper.getActivityPlaceById", params);
	}

	@Override
	public int restore(long activityPlaceId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("activityPlaceId", activityPlaceId);
		
		return getSqlSession().update("ActivityPlaceMapper.restore", params);
	}
	
}
