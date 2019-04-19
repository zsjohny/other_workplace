package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.homepage.HomeFloor;
import com.jiuy.core.meta.homepage.HomeFloorVO;
import com.jiuyuan.entity.query.PageQuery;

public interface HomeFloorDao {

	List<HomeFloorVO> search(PageQuery query, String name, int sequence, long activityPlaceId, Collection<String> templateNames);

	int searchCount(String name, long activityPlaceId, Collection<String> excludeTemplateNames);

	HomeFloor addHomeFloor(HomeFloor homeFloor);

	int removeHomeFloor(long id);

	int updateHomeFloor(HomeFloor homeFloor);

	HomeFloorVO searchById(long floorId);

	int updateHomeTemplateId(Long id, Long nextHomeTemplateId);

	int publishHomeFloor(long activityPlaceId);

	List<Map<String, Object>> preview(long activityPlaceId);

	List<HomeFloorVO> search(PageQuery pageQuery, String name, Integer sequence, Integer type, Long relatedId);

	int searchCount(String name, Integer type, Long relatedId);

	int add(HomeFloor homeFloor2);

	int remove(Long id);

	int update(HomeFloor homeFloor2);

	HomeFloorVO search(Long id);

	int publish(Integer type, Long relatedId);

	List<Map<String, Object>> preview(Integer type, Long relatedId);

}
