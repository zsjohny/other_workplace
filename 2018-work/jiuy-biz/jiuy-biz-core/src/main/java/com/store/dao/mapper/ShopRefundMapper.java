package com.store.dao.mapper;


import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ShopOrderAfterSale;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.store.StoreWxa;
import com.store.entity.member.ShopMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@DBMaster
public interface ShopRefundMapper  {
    ShopOrderAfterSale selectRefund(@Param("orderId") Long orderId, @Param("skuId") Long skuId);
    ShopOrderAfterSale selectRefundOrder(@Param("orderId")Long orderId,@Param("storeId")Long storeId);
    List<ShopOrderAfterSale> selectRefundOrderList (@Param("orderId")Long orderId,@Param("storeId")Long storeId);

    ShopOrderAfterSale applyRefund(@Param("orderId")String orderId,@Param("skuId")Long skuId);

    Integer selectRefundCount(Long memberId);

    List<String> selectInShopMemberId(Long memberId);

    Integer selectAllOrderCount(@Param("list")List list,@Param("orderStatus")Integer orderStatus);
    /**
     * 查询小程序
     */
    List<StoreWxa> selectStoreWxaList(Long storeId);
    List<StoreWxa> selectAppid(@Param("storeId") Long storeId);
    /**
     * 根据memberId查询会员信息
     */
    ShopMember selectShopMember(Long memberId);
    ShopMember selectBindWeixin(@Param("memberId") Long memberId);
    /**
     * 根据storeId查询商家信息
     */
    StoreBusiness selectType(Long storeId);

    Integer selectStoreBusinessNew(Long storeId);

    Long selectIsAppOrder(@Param("id") Long id);
}
