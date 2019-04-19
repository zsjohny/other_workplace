package com.jiuy.core.service;

import java.util.List;

import com.jiuyuan.entity.ActivityPlace;
import com.jiuyuan.entity.query.PageQuery;

public interface ActivityPlaceService {

	int add(String name, String description);

	int update(long id, String name, String description);

	List<ActivityPlace> search(String name, int type, PageQuery pageQuery);

	int searchCount(String name, int type);

	int delete(long activityPlaceId);

	int restore(long activityPlaceId);

}
