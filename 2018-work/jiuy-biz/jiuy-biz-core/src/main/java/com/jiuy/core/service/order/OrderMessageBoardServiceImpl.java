package com.jiuy.core.service.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.OrderMessageBoardDao;
import com.jiuy.core.meta.order.OrderMessageBoard;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class OrderMessageBoardServiceImpl implements OrderMessageBoardService {
	
	@Autowired
	private OrderMessageBoardDao orderMessageBoardDao;

    @Override
    public int add(OrderMessageBoard orderMessageBoard) {
        orderMessageBoard.setCreateTime(System.currentTimeMillis());

        return orderMessageBoardDao.add(orderMessageBoard);
    }

    @Override
    public List<OrderMessageBoard> search(PageQuery pageQuery, long orderNo, int type, long adminId, long startTime,
                                          long endTime) {
        return orderMessageBoardDao.search(pageQuery, orderNo, type, adminId, startTime, endTime);
    }

	@Override
    public int searchCount(long orderNo, int type, long adminId, long startTime, long endTime) {
        return orderMessageBoardDao.searchCount(orderNo, type, adminId, startTime, endTime);
	}

}
