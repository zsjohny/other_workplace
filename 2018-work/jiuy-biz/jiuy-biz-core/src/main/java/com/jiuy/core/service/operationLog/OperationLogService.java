package com.jiuy.core.service.operationLog;

import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.operationLog.OperationLogVO;
import com.jiuyuan.entity.query.PageQuery;

public interface OperationLogService {

	List<Map<String, Object>> search(PageQuery query, OperationLogVO op, String startTime, String endTime);

	int searchCount(OperationLogVO op, String startTime, String endTime);

}
