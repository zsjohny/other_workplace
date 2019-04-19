package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.ClickStatisticsDao;
import com.jiuy.core.meta.clickstatistics.ClickStatistics;
import com.jiuy.core.meta.clickstatistics.ClickStatisticsSearch;
import com.jiuyuan.entity.query.PageQuery;

@Repository
public class ClickStatisticsDaoSqlImpl implements ClickStatisticsDao{

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int addStatistics(ClickStatistics statistics) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.ClickStatisticsDaoSqlImpl",statistics);
	}

	@Override
	public List<ClickStatistics> search(ClickStatisticsSearch clickStatisticsSearch, long startTime, long endTime,
			PageQuery pageQuery,int sort) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		
		params.put("type", clickStatisticsSearch.getType());
		params.put("statisticsId", clickStatisticsSearch.getId());
		params.put("code", clickStatisticsSearch.getCode());
		params.put("onlineStartTime", startTime);
		params.put("onlineEndTime", endTime);
		params.put("startFloorId", clickStatisticsSearch.getStartFloorId());
		params.put("endFloorId", clickStatisticsSearch.getEndFloorId());
		params.put("floorIds", clickStatisticsSearch.getFloorIds());
		
		if(StringUtils.equals(clickStatisticsSearch.getTemplateId(), "")){
			clickStatisticsSearch.setTemplateId(".*");
		}
		params.put("templateId", clickStatisticsSearch.getTemplateId());
		
		if(StringUtils.equals(clickStatisticsSearch.getSerialNumber(), ""))
			clickStatisticsSearch.setSerialNumber(".*");
		params.put("serialNumber", clickStatisticsSearch.getSerialNumber());
		
		if(StringUtils.equals(clickStatisticsSearch.getElementId(), ""))
			clickStatisticsSearch.setElementId(".*");
		params.put("elementId", clickStatisticsSearch.getElementId());
		
		params.put("startRelatedOrderCount", clickStatisticsSearch.getStartRelatedOrderCount());
		params.put("endRelatedOrderCount", clickStatisticsSearch.getEndRelatedOrderCount());
		params.put("startClickCount", clickStatisticsSearch.getStartClickCount());
		params.put("endClickCount", clickStatisticsSearch.getEndClickCount());
		
		params.put("pageQuery", pageQuery);
		params.put("sort", sort);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.ClickStatisticsDaoSqlImpl.search",params);
	}

	@Override
	public int searchCount(ClickStatisticsSearch clickStatisticsSearch, long startTime, long endTime) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		
		params.put("type", clickStatisticsSearch.getType());
		params.put("statisticsId", clickStatisticsSearch.getId());
		params.put("code", clickStatisticsSearch.getCode());
		params.put("onlineStartTime", startTime);
		params.put("onlineEndTime", endTime);
		params.put("startFloorId", clickStatisticsSearch.getStartFloorId());
		params.put("endFloorId", clickStatisticsSearch.getEndFloorId());
		params.put("floorIds", clickStatisticsSearch.getFloorIds());
		params.put("templateId", clickStatisticsSearch.getTemplateId());
		params.put("serialNumber", clickStatisticsSearch.getSerialNumber());
		params.put("elementId", clickStatisticsSearch.getElementId());
		params.put("startRelatedOrderCount", clickStatisticsSearch.getStartRelatedOrderCount());
		params.put("endRelatedOrderCount", clickStatisticsSearch.getEndRelatedOrderCount());
		params.put("startClickCount", clickStatisticsSearch.getStartClickCount());
		params.put("endClickCount", clickStatisticsSearch.getEndClickCount());
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.ClickStatisticsDaoSqlImpl.searchCount",params);
	}

	@Override
	public String searchFloorName(String id) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("id", id);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.ClickStatisticsDaoSqlImpl.searchFloorName",params);
	}

	@Override
	public List<Long> getIdsOfCode(String code) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("code", code);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.ClickStatisticsDaoSqlImpl.getIdsOfCode",params);
	}

	@Override
	public String searchTemplateImgUrl(String id) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("id", id);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.ClickStatisticsDaoSqlImpl.searchTemplateImgUrl",params);
	}

	@Override
	public List<Long> getIdsOfFloorName(String name) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("name", name);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.ClickStatisticsDaoSqlImpl.getIdsOfFloorName",params);
	}

}
