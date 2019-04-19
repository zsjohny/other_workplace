package com.e_commerce.miscroservice.operate.dao.impl;


import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderItemQuery;
import com.e_commerce.miscroservice.operate.mapper.ShopMemberOrderItemMapper;
import com.e_commerce.miscroservice.operate.dao.ShopMemberOrderItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/8 19:16
 * @Copyright 玖远网络
 */
@Component
public class ShopMemberOrderItemDaoImpl implements ShopMemberOrderItemDao{

    @Autowired
    private ShopMemberOrderItemMapper shopMemberOrderItemMapper;



    /**
     * 查询订单的商品信息
     *
     * @param orderNo 订单编号
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderItemQuery>
     * @author Charlie
     * @date 2018/11/9 10:38
     */
    @Override
    public List<ShopMemberOrderItemQuery> findProductInfoByOrderNo(String orderNo) {
        return shopMemberOrderItemMapper.findProductInfoByOrderNo (orderNo);
    }
}
