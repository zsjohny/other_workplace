package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.Statistics;

@Repository
public class StatisticsDaoImpl implements StatisticsDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int add(List<Statistics> statistics_list) {
		Map<String, Object> params = new HashMap<>();
		params.put("statistics_list", statistics_list);
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.StatisticsDaoImpl.add", params);
	}

	@Override
	public Statistics add(Statistics statistics) {
		sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.StatisticsDaoImpl.addOne", statistics);
		return statistics;
	}

	@Override
	public int update(long id, String code) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("code", code);
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.StatisticsDaoImpl.update", params);
	}

	@Override
	public int closeCode(Collection<Long> codes, long time) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("ids", codes);
		params.put("time", time);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.StatisticsDaoImpl.closeCode", params);
	}

	@Override
	public int startCode(Collection<Long> codes, long time) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("ids", codes);
		params.put("time", time);
		
		sqlSessionTemplate.update("com.jiuy.core.dao.mapper.StatisticsDaoImpl.removeLifeEndTime", params);
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.StatisticsDaoImpl.startCode", params);
	}

	@Override
	public int closeCodeByRemove(String halfCode, long time) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("halfCode", halfCode);
		params.put("time", time);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.StatisticsDaoImpl.closeCodeByRemove", params);
	}
}
