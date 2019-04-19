package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.StoreOrderMessageBoardDao;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeorder.StoreOrderMessageBoard;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月11日 下午5:18:00
*/
@Repository
public class StoreOrderMessageBoardDaoSqlImpl implements StoreOrderMessageBoardDao {
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int add(StoreOrderMessageBoard storeOrderMessageBoard) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreOrderMessageBoardDaoSqlImpl.add", storeOrderMessageBoard);
	}

	@Override
	public List<StoreOrderMessageBoard> search(PageQuery pageQuery, long orderNo, List<Integer> types, long adminId,
			long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("pageQuery", pageQuery);
        params.put("orderNo", orderNo);
        params.put("types", types);
        params.put("adminId", adminId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderMessageBoardDaoSqlImpl.search", params);
	}

	@Override
	public int searchCount(long orderNo, List<Integer> types, long adminId, long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNo", orderNo);
        params.put("types", types);
        params.put("adminId", adminId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
		Integer count = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreOrderMessageBoardDaoSqlImpl.searchCount", params);
		return count == null ? 0 : count;
	}

	@Override
	public StoreOrderMessageBoard getCheckInfo(long orderNo) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNo", orderNo);
        return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreOrderMessageBoardDaoSqlImpl.getCheckInfo", params);
	}

	@Override
	public List<StoreOrderMessageBoard> getLastType(List<Integer> types) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("types", types);
        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderMessageBoardDaoSqlImpl.getLastType", params);
	}

	@Override
	public StoreOrderMessageBoard getLastByOrderNo(long orderNo) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNo", orderNo);
        return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreOrderMessageBoardDaoSqlImpl.getLastByOrderNo", params);
	}
}
