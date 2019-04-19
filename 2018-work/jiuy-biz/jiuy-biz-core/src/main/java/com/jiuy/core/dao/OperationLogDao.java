package com.jiuy.core.dao;

import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuy.core.meta.operationLog.OperationLog;
import com.jiuy.core.meta.operationLog.OperationLogVO;
import com.jiuyuan.entity.query.PageQuery;

public interface OperationLogDao extends DomainDao<OperationLog, Long>{


	List<Map<String, Object>> search(PageQuery query, OperationLogVO op);

	int searchCount(OperationLogVO op);
	
}
