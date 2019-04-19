package com.jiuy.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.mapper.StatisticsDao;
import com.jiuyuan.constant.StatisticsPageId;
import com.jiuyuan.constant.StatisticsType;
import com.jiuyuan.entity.Statistics;

@Service
public class StatisticsServiceImpl implements StatisticsService {
	
	@Autowired
	private StatisticsDao statisticsDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Statistics add(long floorId, long templateId, int sort, String imgUrl) {
		long time = System.currentTimeMillis();
		Statistics statistics = new Statistics();
		statistics.setCreateTime(time);
		statistics.setUpdateTime(time);
		statistics.setType(StatisticsType.HOME_PAGE.getIntValue());
		statistics.setPageId(StatisticsPageId.HOME_PAGE.getIntValue());
		statistics.setRelatedId(floorId);
		statistics.setCode("");
		statistics.setElement(imgUrl);
		
		statisticsDao.add(statistics);
		
		long id = statistics.getId();
		String code = "L" + floorId + "M" + templateId + "N" + sort + "ID" + id;
		statisticsDao.update(id, code);
		
		statistics.setCode(code);
		
		return statistics;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Statistics add(int pageId,long floorId, long templateId, int sort, String imgUrl) {
		long time = System.currentTimeMillis();
		Statistics statistics = new Statistics();
		statistics.setCreateTime(time);
		statistics.setUpdateTime(time);
		statistics.setType(StatisticsType.HOME_PAGE.getIntValue());
		statistics.setPageId(pageId);
		statistics.setRelatedId(floorId);
		statistics.setCode("");
		statistics.setElement(imgUrl);
		
		statisticsDao.add(statistics);
		long id = statistics.getId();
		String code = "P" + pageId + "L" + floorId + "M" + templateId + "N" + sort + "ID" + id;
		statisticsDao.update(id, code);
		statistics.setCode(code);
		
		return statistics;
	}

	

}
