package com.jiuy.core.service.aftersale;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.MessageBoardDao;
import com.jiuy.core.meta.aftersale.MessageBoard;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class MessageBoardServiceImpl implements MessageBoardService{
	
	@Resource
	private MessageBoardDao MessageBoardDao;
	
	@Override
	public List<Map<String, Object>> search(PageQuery pageQuery, long serviceId) {
		return MessageBoardDao.search(pageQuery, serviceId);
	}

	@Override
	public int searchCount(long serviceId) {
		return MessageBoardDao.searchCount(serviceId);
	}

	@Override
	public int add(MessageBoard messageBoard) {
		long time = System.currentTimeMillis();
		messageBoard.setCreateTime(time);
		return MessageBoardDao.addMessage(messageBoard);
	}

}
