package com.jiuy.core.service;

import com.jiuyuan.constant.StatisticsPageId;
import com.jiuyuan.entity.Statistics;

public interface StatisticsService {

	Statistics add(long floorId, long templateId, int sort, String imgUrl);
	
	Statistics add(int pageId,long floorId, long templateId, int sort, String imgUrl);

}
