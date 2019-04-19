package com.jiuy.core.service.aftersale;

import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.aftersale.MessageBoard;
import com.jiuyuan.entity.query.PageQuery;

public interface MessageBoardService {

	List<Map<String, Object>> search(PageQuery pageQuery, long serviceId);

	int searchCount(long serviceId);

	int add(MessageBoard messageBoard);

}
