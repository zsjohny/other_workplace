package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.OrderMessageBoardDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuy.core.meta.order.OrderMessageBoard;
import com.jiuyuan.entity.query.PageQuery;

public class OrderMessageBoardDaoSqlImpl extends SqlSupport implements OrderMessageBoardDao{

    @Override
    public int add(OrderMessageBoard orderMessageBoard) {
        return getSqlSession().insert("com.jiuy.core.dao.impl.sql.OrderMessageBoardDaoSqlImpl.add", orderMessageBoard);
    }

    @Override
    public List<OrderMessageBoard> search(PageQuery pageQuery, long orderNo, int type, long adminId, long startTime,
                                          long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("pageQuery", pageQuery);
        params.put("orderNo", orderNo);
        params.put("type", type);
        params.put("adminId", adminId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderMessageBoardDaoSqlImpl.search", params);
    }

	@Override
    public int searchCount(long orderNo, int type, long adminId, long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNo", orderNo);
        params.put("type", type);
        params.put("adminId", adminId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
		
        Integer count = getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderMessageBoardDaoSqlImpl.searchCount", params);

        return count == null ? 0 : count;
	}

}
