package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.meta.order.OrderMessageBoard;
import com.jiuyuan.entity.query.PageQuery;

public interface OrderMessageBoardDao {

    int add(OrderMessageBoard orderMessageBoard);

    List<OrderMessageBoard> search(PageQuery pageQuery, long orderNo, int type, long adminId, long startTime,
                                   long endTime);

    int searchCount(long orderNo, int type, long adminId, long startTime, long endTime);

}
