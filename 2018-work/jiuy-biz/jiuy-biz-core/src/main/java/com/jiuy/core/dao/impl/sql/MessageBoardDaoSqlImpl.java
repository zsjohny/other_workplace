package com.jiuy.core.dao.impl.sql;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.MessageBoardDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuy.core.meta.aftersale.MessageBoard;
import com.jiuyuan.entity.query.PageQuery;

public class MessageBoardDaoSqlImpl extends DomainDaoSqlSupport<MessageBoard, Long> implements MessageBoardDao {

	@Override
	public List<Map<String, Object>> search(PageQuery pageQuery, long serviceId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("pageQuery", pageQuery);
		params.put("serviceId", serviceId);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.MessageBoardDaoSqlImpl.search", params);
	}

	@Override
	public int searchCount(long serviceId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("serviceId", serviceId);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.MessageBoardDaoSqlImpl.searchCount", params);
	}

	@Override
	public int addMessage(MessageBoard messageBoard) {
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.MessageBoardDaoSqlImpl.add", messageBoard);
	}	
}
