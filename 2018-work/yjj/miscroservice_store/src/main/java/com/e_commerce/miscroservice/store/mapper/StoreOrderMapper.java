package com.e_commerce.miscroservice.store.mapper;


import com.e_commerce.miscroservice.store.entity.response.OrderListResponse;
import com.e_commerce.miscroservice.store.entity.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 个人中心-售后
 */
@Mapper
public interface StoreOrderMapper {

    /**
     * 订单查询
     * @param userId
     * @param status
     * @param pageNum
     * @return
     */
    List<OrderListResponse> getUserOrders(@Param("userId") Long userId, @Param("status") Integer status, @Param("pageNum") Integer pageNum);

    /**
     * 供应商订单
     * @param supplierId
     * @param status
     * @param pageNum
     * @return
     */
    List<StoreOrder> getSupplierOrders(@Param("supplierId") Long supplierId, @Param("status") Integer status, @Param("pageNum") Integer pageNum);

    /**
     * 子订单列表
     * @param userId
     * @param orderNo
     * @return
     */
    List<OrderDetailsVo> getChildOrders(@Param("userId") Long userId, @Param("orderNo") Long orderNo);

    /**
     * 根据订单单号 以及 订单详情号查询
     * @param orderNo
     * @param orderItemId
     * @return
     */
    StoreOrder getStoreOrderByOrderNoOrderItemId(@Param("orderNo") Long orderNo, @Param("orderItemId") Long orderItemId);

    /**
     * 查询所有订单列表
     */
    List<StoreOrderNewResponse> selectOrderList(@Param("request") ShopOrderRequest request);
}
