package com.jiuy.core.service;

import java.util.Collection;
import java.util.Map;

public interface UserVisitHistoryService {

	Map<Long, Integer> visitCountById(Collection<Long> productIds, int type);

}
