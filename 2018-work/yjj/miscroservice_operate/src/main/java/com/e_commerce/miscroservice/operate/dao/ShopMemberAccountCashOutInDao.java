package com.e_commerce.miscroservice.operate.dao;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/23 11:02
 * @Copyright 玖远网络
 */
public interface ShopMemberAccountCashOutInDao{


    /**
     * 根据id查询
     *
     * @param id id
     * @return com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn
     * @author Charlie
     * @date 2018/10/23 11:03
     */
    ShopMemberAccountCashOutIn findById(Long id);

    /**
     * 查询某订单收益数量
     *
     * @param orderNumber orderNumber
     * @return java.lang.Integer
     * @author Charlie
     * @date 2018/11/9 14:33
     */
    Long findEarningCount(String orderNumber);




    /**
     * 订单收益列表
     *
     * @param query query
     * @return java.util.List<java.util.Map   <   java.lang.String   ,   java.util.Objects>>
     * @author Charlie
     * @date 2018/11/9 16:54
     */
    List<Map<String,Object>> listOrderEarnings(ShopMemAcctCashOutInQuery query);


    ShopMemberOrderDstbRecord findOrderRecord(String orderNo);
}
