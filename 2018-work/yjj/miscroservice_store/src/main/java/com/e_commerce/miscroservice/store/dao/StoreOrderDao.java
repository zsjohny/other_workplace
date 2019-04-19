package com.e_commerce.miscroservice.store.dao;


import com.e_commerce.miscroservice.store.entity.response.OrderListResponse;
import com.e_commerce.miscroservice.store.entity.vo.OrderDetailsVo;
import com.e_commerce.miscroservice.store.entity.vo.StoreOrder;

import java.util.List;

/**
 * 个人中心-订单
 */
public interface StoreOrderDao {

    /**
     * 查询 用户订单
     * @param userId
     * @param status
     * @param pageNum
     * @return
     */
    List<OrderListResponse> getUserOrders(Long userId, Integer status, Integer pageNum);

    /**
     * 供应商订单列表
     * @param supplierId
     * @param status
     * @param pageNum
     * @return
     */
    List<StoreOrder> getSupplierOrders(Long supplierId, Integer status, Integer pageNum);

    /**
     * 获取子订单列表
     * @param userId
     * @param orderNo
     * @return
     */
    List<OrderDetailsVo> getChildOrders(Long userId, Long orderNo);

    /**
     * 根据订单号查询 订单
     * @param orderNo
     * @return
     */
    StoreOrder getStoreOrderByOrderNo(Long orderNo);

    Integer addRefundSign(Long orderNo);

    /**
     * 根据 订单号以及 订单详情号 查询订单
     * @param orderNo
     * @param orderItemId
     * @return
     */
    StoreOrder getStoreOrderByOrderNoOrderItemId(Long orderNo, Long orderItemId);
}
