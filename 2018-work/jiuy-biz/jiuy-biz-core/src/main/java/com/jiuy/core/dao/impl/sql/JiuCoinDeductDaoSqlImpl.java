package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.JiuCoinDeductDao;
import com.jiuyuan.entity.JiuCoinDeductStatisticsDayBean;

/**
* @author WuWanjian
* @version 创建时间: 2017年4月20日 上午9:32:04
*/
@Repository
public class JiuCoinDeductDaoSqlImpl implements JiuCoinDeductDao{

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public JiuCoinDeductStatisticsDayBean sumDayStatistics(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		JiuCoinDeductStatisticsDayBean result = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.JiuCoinDeductDaoSqlImpl.sumDayStatistics",params);
		return result == null ? new JiuCoinDeductStatisticsDayBean():result;
	}

	@Override
	public JiuCoinDeductStatisticsDayBean sumTimeStatistics(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.JiuCoinDeductDaoSqlImpl.sumTimeStatistics",params);
	}
	
}
