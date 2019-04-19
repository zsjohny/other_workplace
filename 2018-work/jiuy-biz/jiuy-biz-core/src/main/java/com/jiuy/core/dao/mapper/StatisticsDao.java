package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.List;

import com.jiuyuan.entity.Statistics;

public interface StatisticsDao {

	int add(List<Statistics> statistics_list);

	Statistics add(Statistics statistics);

	int update(long id, String code);

	int closeCode(Collection<Long> codes, long time);

	int startCode(Collection<Long> codes, long time);

	int closeCodeByRemove(String halfCode, long currentTimeMillis);

}
