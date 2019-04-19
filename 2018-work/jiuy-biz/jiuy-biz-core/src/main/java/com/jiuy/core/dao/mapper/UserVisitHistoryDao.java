package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserVisitHistoryDao {

	List<Map<String, Object>> searchGroupCount(Collection<Long> relatedIds, int type);

}
