package com.jiuyuan.dao.mapper.supplier;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.RefundOrder;

import com.jiuyuan.entity.newentity.YjjStoreBusinessAccountNew;
import com.jiuyuan.entity.newentity.YjjStoreBusinessAccountLogNew;
import org.apache.ibatis.annotations.Param;


@DBMaster
public interface RefundOrderMapper extends BaseMapper<RefundOrder> {

	/**
	 * 获取售后原因
	 * @return
	 */
	List<String> getRefundReasonList();

	YjjStoreBusinessAccountNew selectMoney(@Param("storeId") Long storeId);

	/**
	 * 更新账户
	 */
	int updateMoney(@Param("storeId")Long storeId,@Param("waitMoney")Double waitMoney,@Param("totalMoney")Double totalMoney );
	int insertInto(@Param("yjjStoreBusinessAccountLog")YjjStoreBusinessAccountLogNew yjjStoreBusinessAccountLog);
	/**
	 * 关闭订单
	 */
	int updateOrderStatus(@Param("orderStatus")Integer orderStatus,@Param("orderNo")Long orderNo);

	int updateSaleCount(@Param( "id" ) long id, @Param( "by" ) int by);



	int updateOrderPayStatus(@Param("orderNo") long orderNo, @Param("paymentNo") String paymentNo,
							 @Param("paymentType") int paymentType, @Param("newStatus") int newStatus,
							 @Param("oldStatus") int oldStatus, @Param("time") long time);

	List<YjjStoreBusinessAccountLogNew> selectAccountLogByShopOrderNo(@Param( "orderNo" ) String orderNo);
}
