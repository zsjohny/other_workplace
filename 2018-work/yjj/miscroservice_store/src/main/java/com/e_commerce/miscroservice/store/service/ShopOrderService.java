package com.e_commerce.miscroservice.store.service;


import com.e_commerce.miscroservice.commons.helper.util.service.Response;

/**
 * 个人中心-订单
 * @author hyf
 * @version V1.0
 * @date 2018/9/26 17:27
 * @Copyright 玖远网络
 */
public interface ShopOrderService {

    /**
     * 查找订单列表
     * @param status
     * @param type
     * @param userId
     * @param supplierId
     * @param pageNum
     * @return
     */
    Response findOrderList(Integer status, Integer type, Long userId, Long supplierId, Integer pageNum);

    /**
     * 订单详情
     * @param userId
     * @param orderNo
     * @return
     */
    Response  getOrderDetail(Long userId, Long orderNo);
}
