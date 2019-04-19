package com.store.dao.mapper;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.RefundOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@DBMaster
public interface RefundMapperNew {
    /**
     * 根据订单号和skuid查询商品信息
     */
    RefundOrder selectRefundOrder(@Param("orderNo")Long orderNo, @Param("skuId")Long skuId);
}

