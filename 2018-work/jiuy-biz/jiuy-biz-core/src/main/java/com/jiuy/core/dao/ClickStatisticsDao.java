package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.meta.clickstatistics.ClickStatistics;
import com.jiuy.core.meta.clickstatistics.ClickStatisticsSearch;
import com.jiuyuan.entity.query.PageQuery;

public interface ClickStatisticsDao {
	
	int addStatistics(ClickStatistics statistics);
	
	List<ClickStatistics> search(ClickStatisticsSearch clickStatisticsSearch,long startTime, long endTime, PageQuery pageQuery,int sort);
	
	int searchCount(ClickStatisticsSearch clickStatisticsSearch,long startTime, long endTime);
	
	String searchFloorName(String id);
	
	List<Long> getIdsOfCode(String code);
	
	String searchTemplateImgUrl(String id);
	
	List<Long> getIdsOfFloorName(String name);
	
}
