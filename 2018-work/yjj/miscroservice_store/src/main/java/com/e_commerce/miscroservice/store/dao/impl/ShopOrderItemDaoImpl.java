package com.e_commerce.miscroservice.store.dao.impl;


import com.e_commerce.miscroservice.store.dao.ShopOrderItemDao;
import com.e_commerce.miscroservice.store.mapper.ShopOrderItemMapper;
import com.e_commerce.miscroservice.store.entity.vo.StoreOrderitem;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 用户订单细目表
 */
@Repository
public class ShopOrderItemDaoImpl implements ShopOrderItemDao {

    @Resource
    private ShopOrderItemMapper shopOrderItemMapper;
    @Override
    public List<StoreOrderitem> getOrderItem(Long userId, Set<Long> orderNOs) {
        return shopOrderItemMapper.getOrderItem(userId,orderNOs);
    }
}
