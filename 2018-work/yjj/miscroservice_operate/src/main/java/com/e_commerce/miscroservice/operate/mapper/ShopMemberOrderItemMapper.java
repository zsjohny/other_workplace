package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderItemQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/8 19:16
 * @Copyright 玖远网络
 */
@Mapper
public interface ShopMemberOrderItemMapper{


    /**
     * 查询订单的商品信息
     *
     * @param orderNo 订单编号
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderItemQuery>
     * @author Charlie
     * @date 2018/11/9 10:38
     */
    List<ShopMemberOrderItemQuery> findProductInfoByOrderNo(@Param ("orderNo") String orderNo);
}
