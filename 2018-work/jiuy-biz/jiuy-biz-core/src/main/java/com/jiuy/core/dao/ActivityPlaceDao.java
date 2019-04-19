package com.jiuy.core.dao;

import java.util.List;

import com.jiuyuan.entity.ActivityPlace;
import com.jiuyuan.entity.query.PageQuery;

public interface ActivityPlaceDao {

	ActivityPlace add(String name, String description, long currentTime);

	int update(long id, String name, String description, long currentTime);

	int updateUrl(long id, String url);

	List<ActivityPlace> search(String name, int type, PageQuery pageQuery);

	int searchCount(String name, int type);

	int delete(long activityPlaceId);

	int updateActivityPlaceType(long activityPlaceId);

	ActivityPlace getActivityPlaceById(long activityPlaceId);

	int restore(long activityPlaceId);

}
