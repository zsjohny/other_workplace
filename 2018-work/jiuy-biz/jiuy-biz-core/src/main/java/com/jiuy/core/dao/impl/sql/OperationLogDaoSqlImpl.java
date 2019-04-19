package com.jiuy.core.dao.impl.sql;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.OperationLogDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuy.core.meta.operationLog.OperationLog;
import com.jiuy.core.meta.operationLog.OperationLogVO;
import com.jiuyuan.entity.query.PageQuery;

public class OperationLogDaoSqlImpl extends DomainDaoSqlSupport<OperationLog, Long> implements OperationLogDao {

	@Override
	public List<Map<String, Object>> search(PageQuery query, OperationLogVO op) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("pageQuery", query);
		params.put("userId", op.getUserId());
		params.put("userName", op.getUserName());
		params.put("workNo", op.getWorkNo());
		params.put("roleId", op.getRoleId());
		params.put("startTimeMillions", op.getStartTime());
		params.put("endTimeMillions", op.getEndTime());
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OperationLogDaoSqlImpl.search", params);
	}

	@Override
	public int searchCount(OperationLogVO op) {
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OperationLogDaoSqlImpl.searchCount", op);
	}

	
}
