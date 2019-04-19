package com.e_commerce.miscroservice.order.mapper;

import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.TeamOrder;
import com.e_commerce.miscroservice.commons.entity.order.CountTeamOrderMoneyCoinVo;
import com.e_commerce.miscroservice.commons.entity.order.OrderAccountDetailsResponse;
import com.e_commerce.miscroservice.commons.entity.order.OrderItemSkuVo;
import com.e_commerce.miscroservice.order.entity.ShopMemberOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/11 17:38
 * @Copyright 玖远网络
 */
@Mapper
public interface ShopMemberOrderItemMapper {


    /**
     * 根据id查询
     *
     * @param id id
     * @return com.e_commerce.miscroservice.order.entity.ShopMemberOrderItem
     * @author Charlie
     * @date 2019/1/30 10:27
     */
    ShopMemberOrderItem findBySql(@Param( "id" ) Long id);

    /**
     * 根据id更新
     *
     * 手动扩展
     * @param item item
     * @return int
     * @author Charlie
     * @date 2019/2/17 12:44
     */
    int updateById(ShopMemberOrderItem item);
}
