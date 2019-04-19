package com.jiuy.core.service;

import java.util.List;

import com.jiuy.core.meta.clickstatistics.ClickStatistics;
import com.jiuy.core.meta.clickstatistics.ClickStatisticsSearch;
import com.jiuyuan.entity.query.PageQuery;

public interface ClickStatisticsService {
	
	List<ClickStatistics> search(ClickStatisticsSearch clickStatisticsSearch, long startTime, long endTime,
			PageQuery pageQuery,int sort);
	
	int searchCount(ClickStatisticsSearch clickStatisticsSearch, long startTime, long endTime);
	
	String searchFloorName(String id);
	
	String searchTemplateImgUrl(String id);
	
	List<Long> getIdsOfFloorName(String name);
}
