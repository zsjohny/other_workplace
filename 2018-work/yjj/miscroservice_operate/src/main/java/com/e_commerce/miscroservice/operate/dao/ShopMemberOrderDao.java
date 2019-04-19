package com.e_commerce.miscroservice.operate.dao;

import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderQuery;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/8 19:16
 * @Copyright 玖远网络
 */
public interface ShopMemberOrderDao{



    /**
     * 小程序订单查询
     *
     * @param query query
     * @author Charlie
     * @date 2018/11/8 19:12
     */
    PageInfo<Map<String, Object>> listOrderPage(ShopMemberOrderQuery query);



}
