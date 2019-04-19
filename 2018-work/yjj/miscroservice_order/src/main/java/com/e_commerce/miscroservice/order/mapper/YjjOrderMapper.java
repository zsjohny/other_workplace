package com.e_commerce.miscroservice.order.mapper;

import com.e_commerce.miscroservice.commons.entity.order.YjjOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Create by hyf on 2018/11/13
 */
@Mapper
public interface YjjOrderMapper {
    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    YjjOrder findYjjOrderByOrderNo(@Param("orderNo") String orderNo);
}
