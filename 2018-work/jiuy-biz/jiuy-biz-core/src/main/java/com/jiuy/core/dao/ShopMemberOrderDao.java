package com.jiuy.core.dao;

import java.util.List;

import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.store.entity.coupon.ShopMemberCoupon;

public interface ShopMemberOrderDao{
	
	List<ShopMemberOrder> getWaitPayTipOrderList(long twentyThreeBefore);

	int storeOrderByOrderId(ShopMemberOrder shopMemberOrder);

	int updateOrderSendMessage(Long orderId);

	int updateShopMemberCouponStatus(ShopMemberCoupon shopMemberCoupon);

	List<ShopMemberOrder> getUnPaidMemberOrderList(long outTime);
}