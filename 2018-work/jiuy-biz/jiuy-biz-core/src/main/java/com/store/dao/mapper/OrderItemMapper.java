package com.store.dao.mapper;

import java.util.Collection;
import java.util.List;

import com.jiuyuan.entity.newentity.StoreBusiness;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.store.entity.ShopStoreOrderItem;

/**
 * @author jeff.zhan
 * @version 2016年11月10日 下午2:10:00
 * 
 */

@DBMaster
public interface OrderItemMapper {

    int insertOrderItems(@Param("orderId") long orderId, @Param("orderItems") List<ShopStoreOrderItem> orderItems);

    List<ShopStoreOrderItem> getOrderItems(@Param("storeId") long storeId, @Param("orderIds") Collection<Long> orderIds);

	int updateOrderNo(@Param("itemId") long itemId, @Param("orderNo") long orderNo, @Param("time") long time);

	List<ShopStoreOrderItem> getOrderNewItems(@Param("storeId") long storeId, @Param("orderNos") Collection<Long> orderNos);

	List<ShopStoreOrderItem> getOrderNewItemsByOrderNO(@Param("storeId") long storeId, @Param("orderNo") long orderNo);

	ShopStoreOrderItem getOrderItemById(@Param("storeId") long storeId, @Param("orderItemId") long orderItemId);

	List<ShopStoreOrderItem> getOrderNewItemsOnlyByOrderNO(@Param("orderNo")long orderNo);

    Integer selectStoreById(@Param("storeId")Long storeId);

	Integer selectOwn(@Param("orderNo") Long orderNo);

	Integer selectType(@Param("orderNo") Long orderNo);
}
