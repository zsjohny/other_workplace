package com.e_commerce.miscroservice.operate.dao;


import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderItemQuery;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/8 19:16
 * @Copyright 玖远网络
 */
public interface ShopMemberOrderItemDao{



    /**
     * 查询订单的商品信息
     *
     * @param orderNo 订单编号
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderItemQuery>
     * @author Charlie
     * @date 2018/11/9 10:38
     */
    List<ShopMemberOrderItemQuery> findProductInfoByOrderNo(String orderNo);
}
