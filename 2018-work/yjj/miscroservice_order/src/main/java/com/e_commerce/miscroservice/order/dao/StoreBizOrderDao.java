package com.e_commerce.miscroservice.order.dao;

import com.e_commerce.miscroservice.order.entity.StoreBizOrder;

import java.math.BigDecimal;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 16:37
 * @Copyright 玖远网络
 */
public interface StoreBizOrderDao{


    /**
     * 查找还未支付的会员订单
     *
     * @param storeId storeId
     * @param memberType 会员类型
     * @param totalFee 支付金额
     * @param canal 支付来源
     * @return com.e_commerce.miscroservice.order.entity.StoreBizOrder
     * @author Charlie
     * @date 2018/12/11 16:39
     */
    StoreBizOrder findNoPayMemberOrder(Long storeId, Integer memberType, BigDecimal totalFee, Integer canal);



    /**
     * 保存
     *
     * @param order order
     * @return int
     * @author Charlie
     * @date 2018/12/11 17:36
     */
    int save(StoreBizOrder order);



    /**
     * 查询用户可用订单
     *
     * @param orderNo orderNo
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.order.entity.StoreBizOrder
     * @author Charlie
     * @date 2018/12/11 20:43
     */
    StoreBizOrder findByOrderNo(String orderNo, Long storeId);



    /**
     * 根据订单id更新
     *
     * @param order order
     * @return int
     * @author Charlie
     * @date 2018/12/12 1:09
     */
    int updateById(StoreBizOrder order);


}
