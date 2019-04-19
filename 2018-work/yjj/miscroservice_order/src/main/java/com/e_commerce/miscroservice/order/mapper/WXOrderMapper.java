package com.e_commerce.miscroservice.order.mapper;

import com.e_commerce.miscroservice.order.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface WXOrderMapper {

    /**
     * 根据订单查询商品
     */
    ShopMemberOrder selectOrder(Long orderId);


    SecondBuyActivity selectActive(Long secondId);

    TeamBuyActivity selectTeam(Long teanId);


    /**
     * 查询小程序
     */
       List<StoreWxa> selectStoreWxaList(String storeId);
        List<StoreWxa> selectStoreWxaListNew(String storeId);
    /**
     * 修改数据
     */
     int updateById(@Param("shopMemberOrder") ShopMemberOrder shopMemberOrder);

    /**
     * 查询订单集合
     */
    List<ShopMemberOrderItem> selectByOrderNo(Long orderId);

    /**
     * 根据memberId查询会员信息
     */
    ShopMember  selectShopMember(Long memberId);

    /**
     * 根据storeId查询商家信息
     */
    StoreBusiness selectStoreBusiness(Long storeId);

    Long selectOppenId(@Param("bindWeixin") String bindWeixin,@Param("storeId") Long storeId);
}
