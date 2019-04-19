package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.GrantJiuCoinLogDao;
import com.jiuyuan.entity.GrantJiuCoinLog;
import com.jiuyuan.entity.query.PageQuery;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月23日 下午5:54:57
*/
@Repository
public class GrantJiuCoinLogDaoSqlImpl implements GrantJiuCoinLogDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public long addGrantJiuCoinLog(GrantJiuCoinLog grantJiuCoinLog) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.GrantJiuCoinLogDaoSqlImpl.addGrantJiuCoinLog", grantJiuCoinLog);
	}

	@Override
	public List<GrantJiuCoinLog> getGrantJiuCoinLog(PageQuery pageQuery) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		
		params.put("pageQuery", pageQuery);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.GrantJiuCoinLogDaoSqlImpl.getGrantJiuCoinLog",params);
	}

	@Override
	public int getGrantJiuCoinLogCount(){
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.GrantJiuCoinLogDaoSqlImpl.getGrantJiuCoinLogCount");
	}
}
