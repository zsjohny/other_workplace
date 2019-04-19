package com.jiuy.core.service.storeaftersale;

import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.aftersale.StoreMessageBoard;
import com.jiuyuan.entity.query.PageQuery;

public interface StoreMessageBoardService {

	List<Map<String, Object>> search(PageQuery pageQuery, long serviceId);

	int searchCount(long serviceId);

	int add(StoreMessageBoard messageBoard);

}
