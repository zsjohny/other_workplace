package com.jiuy.core.service.homepage;

import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.homepage.HomeFloor;
import com.jiuy.core.meta.homepage.HomeFloorVO;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;

public interface HomeFloorService {

	List<HomeFloorVO> search(PageQuery query, String name, int sequence, long activityPlaceId);

	int searchCount(String name, long activityPlaceId);

	ResultCode addHomeFloor(HomeFloor homeFloor);

	ResultCode removeHomeFloor(long id);

	ResultCode updateHomeFloor(HomeFloor homeFloor);

	HomeFloorVO searchById(long floorId);

	ResultCode publishHomeFloor(long activityPlaceId);

	List<Map<String, Object>> preview(long activityPlaceId);

	List<HomeFloorVO> search(PageQuery pageQuery, String name, Integer sequence, Integer type, Long relatedId);

	int searchCount(String name, Integer type, Long relatedId);

	int add(HomeFloor homeFloor2);

	int remove(Long id);

	int update(HomeFloor homeFloor2);

	HomeFloorVO search(Long floorId);

	void publish(Integer type, Long relatedId);

	List<Map<String, Object>> preview(Integer type, Long relatedId);


}
