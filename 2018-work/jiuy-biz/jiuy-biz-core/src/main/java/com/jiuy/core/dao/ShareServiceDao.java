package com.jiuy.core.dao;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.query.PageQuery;


public interface ShareServiceDao {

	Map<String, Object> getDayReportData(long dayTime, long endTime);

	Map<String, Object> getTotayData(long startTime, long endTime);

	int getRecordCount(long yjjNumber, long productId, int type, long startTime, long endTime, long phone,
			String clothesNumber, long startJiuBi, long endJiuBi);

	List<Map<String, String>> searchShareRecord(PageQuery pageQuery, long yjjNumber, long productId, int type,
			long startTime, long endTime, long phone, String clothesNumber, long startJiuBi, long endJiuBi);

	int getClickRecordCount(long shareYjjNumber, long yjjNumber, int type, long startTime, long endTime, long phone,
			long sharePhone,long shareId);

	List<Map<String, String>> searchClickShareRecord(PageQuery pageQuery, long shareYjjNumber, long yjjNumber, int type,
			long startTime, long endTime, long phone, long sharePhone,long shareId);

}
