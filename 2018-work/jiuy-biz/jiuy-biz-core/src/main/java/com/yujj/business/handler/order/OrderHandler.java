package com.yujj.business.handler.order;

import com.yujj.entity.order.Order;
import com.yujj.entity.order.OrderNew;

public interface OrderHandler {

    void postOrderCreation(Order order, String version);
    
//    void postOrderNewCreation(OrderNew order, String version);

//    void postOrderCancel(Order order, String version);
    
    void postOrderNewCancel(OrderNew order, String version);

}
