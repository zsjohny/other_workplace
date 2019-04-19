package com.jiuy.core.service.task;

import java.util.List;

import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.entity.order.ShopMemberOrderLog;
import com.store.entity.coupon.ShopMemberCoupon;

public interface ShopMemberOrderService {
	List<ShopMemberOrder> getWaitPayTipOrderList();

	int storeOrderByOrderId(ShopMemberOrder shopMemberOrder);

	int addShopMemberOrderLog(ShopMemberOrderLog shopMemberOrderLog);

	int updateOrderSendMessage(Long orderId);

	int updateShopMemberCouponStatus(ShopMemberCoupon shopMemberCoupon);

	List<ShopMemberOrder> getUnPaidMemberOrderList();

	List<ShopMemberOrder> getWaitPaySecondOrderList();

	List<ShopMemberOrder> stopMemberTeamOrderOvertime(long nowTime);

	List<ShopMemberOrder> getMemberSecondOvertimeOrder(long nowTime);

}