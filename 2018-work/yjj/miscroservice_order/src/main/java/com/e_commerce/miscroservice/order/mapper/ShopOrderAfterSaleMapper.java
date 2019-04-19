package com.e_commerce.miscroservice.order.mapper;

import com.e_commerce.miscroservice.order.entity.ShopOrderAfterSale;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/22 14:21
 */
@Mapper
public interface ShopOrderAfterSaleMapper {

    List<ShopOrderAfterSale> listByShopMemberOrderNo(@Param( "orderId" ) Long orderId);
}
