package com.jiuy.core.dao;

import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuy.core.meta.aftersale.StoreMessageBoard;
import com.jiuyuan.entity.query.PageQuery;

public interface StoreMessageBoardDao extends DomainDao<StoreMessageBoard, Long>{


	List<Map<String, Object>> search(PageQuery query, long serviceId);

	int searchCount(long serviceId);

	int addMessage(StoreMessageBoard messageBoard);
	
}
