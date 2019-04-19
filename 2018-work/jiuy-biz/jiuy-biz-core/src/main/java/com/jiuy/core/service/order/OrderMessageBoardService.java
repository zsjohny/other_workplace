package com.jiuy.core.service.order;

import java.util.List;

import com.jiuy.core.meta.order.OrderMessageBoard;
import com.jiuyuan.entity.query.PageQuery;

public interface OrderMessageBoardService {

    int add(OrderMessageBoard orderMessageBoard);

    List<OrderMessageBoard> search(PageQuery pageQuery, long orderNo, int type, long adminId, long startTimeL,
                                   long endTime);

    int searchCount(long orderNo, int type, long adminId, long startTimeL, long endTime);

}
