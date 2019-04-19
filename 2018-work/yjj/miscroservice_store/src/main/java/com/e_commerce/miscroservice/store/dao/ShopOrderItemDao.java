package com.e_commerce.miscroservice.store.dao;


import com.e_commerce.miscroservice.store.entity.vo.StoreOrderitem;

import java.util.List;
import java.util.Set;

/**
 * 用户订单细目表
 */
public interface ShopOrderItemDao {

    /**
     * 用户订单详情 列表
     * @param userId
     * @param orderNOs
     * @return
     */
    List<StoreOrderitem> getOrderItem(Long userId, Set<Long> orderNOs);
}
