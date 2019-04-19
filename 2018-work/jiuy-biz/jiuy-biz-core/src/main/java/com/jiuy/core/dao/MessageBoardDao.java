package com.jiuy.core.dao;

import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuy.core.meta.aftersale.MessageBoard;
import com.jiuyuan.entity.query.PageQuery;

public interface MessageBoardDao extends DomainDao<MessageBoard, Long>{


	List<Map<String, Object>> search(PageQuery query, long serviceId);

	int searchCount(long serviceId);

	int addMessage(MessageBoard messageBoard);
	
}
