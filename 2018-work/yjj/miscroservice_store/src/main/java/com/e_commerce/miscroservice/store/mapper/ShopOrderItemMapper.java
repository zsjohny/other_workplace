package com.e_commerce.miscroservice.store.mapper;


import com.e_commerce.miscroservice.store.entity.vo.StoreOrderitem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 用户订单细目表
 */
@Mapper
public interface ShopOrderItemMapper {


    /**
     * 订单详情列表
     * @param userId
     * @param orderNOs
     * @return
     */
    List<StoreOrderitem> getOrderItem(@Param("userId") Long userId, @Param("orderNOs") Set<Long> orderNOs);
}
