package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.ClickStatisticsDao;
import com.jiuy.core.meta.clickstatistics.ClickStatistics;
import com.jiuy.core.meta.clickstatistics.ClickStatisticsSearch;
import com.jiuyuan.entity.query.PageQuery;

@Service("clickStatisticsService")
public class ClickStatisticsServiceImpl implements ClickStatisticsService {
	
	@Resource
	private ClickStatisticsDao csDao;

	@Override
	public List<ClickStatistics> search(ClickStatisticsSearch clickStatisticsSearch, long startTime, long endTime,
			PageQuery pageQuery,int sort) {
		return csDao.search(clickStatisticsSearch, startTime, endTime, pageQuery,sort);
	}

	@Override
	public int searchCount(ClickStatisticsSearch clickStatisticsSearch, long startTime, long endTime) {
		return csDao.searchCount(clickStatisticsSearch, startTime, endTime);
	}

	@Override
	public String searchFloorName(String id) {
		if(StringUtils.equals(id, "")){
			return "";
		}
		return csDao.searchFloorName(id);
	}

	@Override
	public String searchTemplateImgUrl(String id) {
		if(StringUtils.equals("", id)){
			return "";
		}
		return csDao.searchTemplateImgUrl(id);
	}

	@Override
	public List<Long> getIdsOfFloorName(String name) {
		if(StringUtils.equals("", name)){
			return new ArrayList<Long>();
		}
		return csDao.getIdsOfFloorName(name);
	}

}
