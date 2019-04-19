package com.jiuyuan.service.shop;

import java.text.ParseException;
import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.ShopMemberReservationsOrder;
import com.jiuyuan.entity.newentity.StoreBusiness;

public interface IShopMemberReservationsOrderService {

	/**
	 * 添加预约试穿订单
	 * @param id
	 * @param id2
	 * @param shopProductId
	 * @param platformProductSkuId
	 * @param shopProductSizeName
	 * @param shopProductColorName
	 * @param shopMemberName
	 * @param shopMemberPhone
	 * @param appointmentTime
	 * @param userCID
	 * @throws ParseException
	 * @throws Exception 
	 */
	void addSubscribeOrder(long storeId, long memberId, long shopProductId, long platformProductSkuId,
						   String shopProductSizeName, String shopProductColorName, String shopMemberName, String shopMemberPhone,
						   String appointmentTime, String userCID) throws Exception;

	/**
	 * 获取预约试穿订单管理列表
	 * @param storeId
	 * @param keyWord
	 * @param page 
	 * @return
	 */
	List<ShopMemberReservationsOrder> getReservationsOrderList(long storeId, String keyWord,
			Page<ShopMemberReservationsOrder> page);

	/**
	 * 获取预约试穿订单详情
	 * @param shopMemberReservationsOrderId
	 * @return
	 */
	ShopMemberReservationsOrder getReservationsOrderInfo(long shopMemberReservationsOrderId);
}