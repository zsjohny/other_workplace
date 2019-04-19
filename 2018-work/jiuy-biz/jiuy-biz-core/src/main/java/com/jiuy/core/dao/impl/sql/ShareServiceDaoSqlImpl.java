package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.ShareServiceDao;
import com.jiuyuan.entity.query.PageQuery;

@Repository
public class ShareServiceDaoSqlImpl implements ShareServiceDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public Map<String, Object> getDayReportData(long dayTime,long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("startTime", dayTime);
		params.put("endTime", endTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.ShareServiceDaoSqlImpl.getDayReportData",
				params);
	}

	@Override
	public Map<String, Object> getTotayData(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.ShareServiceDaoSqlImpl.getTotayData", params);
	}

	@Override
	public int getRecordCount(long yjjNumber, long productId, int type, long startTime, long endTime, long phone,
			String clothesNumber, long startJiuBi, long endJiuBi) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("yjjNumber", yjjNumber);
		params.put("productId", productId);
		params.put("type", type);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("phone", phone);
		params.put("clothesNumber", clothesNumber);
		params.put("startJiuBi", startJiuBi);
		params.put("endJiuBi", endJiuBi);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.ShareServiceDaoSqlImpl.getRecordCount", params);
	}

	@Override
	public List<Map<String, String>> searchShareRecord(PageQuery pageQuery, long yjjNumber, long productId, int type,
			long startTime, long endTime, long phone, String clothesNumber, long startJiuBi, long endJiuBi) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("yjjNumber", yjjNumber);
		params.put("productId", productId);
		params.put("type", type);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("phone", phone);
		params.put("clothesNumber", clothesNumber);
		params.put("startJiuBi", startJiuBi);
		params.put("endJiuBi", endJiuBi);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.ShareServiceDaoSqlImpl.searchShareRecord", params);
	}

	@Override
	public int getClickRecordCount(long shareYjjNumber, long yjjNumber, int type, long startTime, long endTime,
			long phone,long sharePhone, long shareId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("shareYjjNumber", shareYjjNumber);
		params.put("yjjNumber", yjjNumber);		
		params.put("type", type);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("phone", phone);
		params.put("sharePhone", sharePhone);
		params.put("shareId", shareId);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.ShareServiceDaoSqlImpl.getClickRecordCount", params);
	}

	@Override
	public List<Map<String, String>> searchClickShareRecord(PageQuery pageQuery, long shareYjjNumber, long yjjNumber,
			int type, long startTime, long endTime, long phone,long sharePhone, long shareId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("shareYjjNumber", shareYjjNumber);
		params.put("yjjNumber", yjjNumber);		
		params.put("type", type);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("phone", phone);
		params.put("sharePhone", sharePhone);
		params.put("shareId", shareId);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.ShareServiceDaoSqlImpl.searchClickShareRecord", params);
	}
}
