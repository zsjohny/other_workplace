package com.jiuy.core.service.storeaftersale;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.StoreMessageBoardDao;
import com.jiuy.core.meta.aftersale.StoreMessageBoard;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class StoreMessageBoardServiceImpl implements StoreMessageBoardService{
	
	@Resource
	private StoreMessageBoardDao storeMessageBoardDao;
	
	@Override
	public List<Map<String, Object>> search(PageQuery pageQuery, long serviceId) {
		return storeMessageBoardDao.search(pageQuery, serviceId);
	}

	@Override
	public int searchCount(long serviceId) {
		return storeMessageBoardDao.searchCount(serviceId);
	}

	@Override
	public int add(StoreMessageBoard messageBoard) {
		long time = System.currentTimeMillis();
		messageBoard.setCreateTime(time);
		return storeMessageBoardDao.addMessage(messageBoard);
	}

}
