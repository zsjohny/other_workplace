package com.e_commerce.miscroservice.order.mapper;

import com.e_commerce.miscroservice.order.entity.StoreOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/19 13:59
 */
@Mapper
public interface StoreOrderMapper {

    /**
     * 更新时间
     * @param order order
     * @return int
     * @author Charlie
     * @date 2019/2/19 14:16
     */
    int updateTimeById(StoreOrder order);

    StoreOrder findByOrderNo(@Param( "orderNo" ) Long orderNo);

}
