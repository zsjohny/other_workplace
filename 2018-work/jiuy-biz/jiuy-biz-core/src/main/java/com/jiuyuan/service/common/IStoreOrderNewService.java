package com.jiuyuan.service.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.MemberPackageType;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.newentity.*;

public interface IStoreOrderNewService {

	/**
	 * 获取供应商列表
	 * @param updateTimeEnd 
	 * @param updateTimeStart 
	 * @param clothesNumbers 
	 * 
	 * @param phoneNumber
	 * @param orderStatus 
	 * @param orderNo 
	 * @param page 
	 * @return
	 * @return
	 */
	List<StoreOrderNew> getSupplierOrderList(long userId, long orderNo, int orderStatus, String phoneNumber,
			String clothesNumbers, String updateTimeStart, String updateTimeEnd, Page<StoreOrderNew> page);

	/**
	 * 获取订单总数，今日新增个数，待处理个数
	 * @param userId
	 * @return
	 */
	Map<String, Object> getSupplierOrderCount(long userId);

	//获取待处理个数
	Object getSupplierOrderCountUnDealWithCount(long userId);
	
	public Object getSupplierOrderCountHasDelivered(long supplierId);
	
	public Object getSupplierOrderCountRefunded(long supplierId);

	/**
	 * 根据orderNo获取订单详情
	 * @param orderNo
	 * @return
	 */
	Map<String, Object> getSupplierOrderByOrderNo(long orderNo);

	/**
	 * 根据orderNo获取订单item详情
	 * @param orderNo
	 * @return
	 */
	List<Map<String, Object>> getSupplierOrderItemByOrderNo(long orderNo);

	/**
	 * 供应商订单发货
	 * @param orderNo
	 * @param expressCompamyName
	 * @param expressNo
	 * @param remark
	 */
	void deliverGoods(long orderNo, String expressCompamyName, String expressNo, String remark);

	/**
	 * 供应商订单退单
	 * @param orderNo
	 * @param cancelReason
	 */
	void chargeback(long orderNo, String cancelReason);

	/**
	 * 获取所有客户
	 * @param userId
	 * @param phoneNumber
	 * @param moneyMin
	 * @param moneyMax
	 * @param countMin
	 * @param countMax
	 * @param province
	 * @param city
	 * @return
	 */
	List<Map<String, Object>> getSupplierCustomerList(long userId, String businessName,String phoneNumber, double moneyMin, double moneyMax,
			int countMin, int countMax, String province, String city, Page<Map<String, Object>> page);

	/**
	 * 获取客户总数
	 * @param userId
	 * @return
	 */
	int getSupplierCustomerAllCount(long userId);

	/**
	 * 今日新增个数
	 * @param userId
	 * @return
	 */
	int getSupplierCustomerTodayNewCount(long userId);

	/**
	 * 导出EXCEL
	 * @param userId
	 * @param orderNo
	 * @param orderStatus
	 * @param phoneNumber
	 * @param clothesNumbers
	 * @param updateTimeStart
	 * @param updateTimeEnd
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> deliveryExcel(long userId, long orderNo, int orderStatus, String phoneNumber,
			String clothesNumbers, String updateTimeStart, String updateTimeEnd, Page<StoreOrderNew> page);

	/**
	 * 根据OrderNo获取对应的StoreOrderItem
	 * @param orderNos
	 * @return
	 */
	Map<Long, List<StoreOrderItemNew>> orderItemMapOfOrderNos(Collection<Long> orderNos);

	/**
	 * 根据OrderNo获取对应的订单并封装
	 * @param orderNos
	 * @return
	 */
	Map<Long, StoreOrderNew> orderNewMapOfOrderNos(Collection<Long> orderNos);

	Address getAddrByExp(List<Address> addresses, String expressInfo);

	List<StoreCouponNew> availableCoupon(long storeId, long brandId, double orderAmount);

	StoreCouponNew getCouponById(long couponId);

	/**
	 * 修改已使用优惠券信息
	 * @param cidArr
	 * @param orderNo
	 */
	void updateCouponUsed(String[] cidArr, long orderNo);

	/**
	 * 生成优惠券使用日志
	 * @param storeCouponUseLogNew
	 */
	void insertCouponUseLog(StoreCouponUseLogNew storeCouponUseLogNew);

	List<StoreOrderNew> getStoreOrderByOrderStatus(int intValue);
	/**
	 * 设置订单为售后中
	 */
	void addRefundSign(long orderNo);

	StoreOrderNew getStoreOrderByOrderNo(long parseLong);

	List<StoreOrderNew> ordersOfOrderNos(Set<Long> freezeOrderNos1);

	/**
	 * 售后订单退全部金额并且有优惠券时退优惠券
	 */
	public void retreatingCoupons(StoreOrderNew order,double refundMoney);
	/**
	 * 关闭订单
	 * @param storeOrderNew
	 */
	void closeOrder(StoreOrderNew storeOrderNew);
	/**
	 * 挂起订单
	 * @param storeOrderNew
	 */
	void hangUpOrder(StoreOrderNew storeOrderNew);

	void lockStoreOrder(long orderNo, long userId);

	void unlockStoreOrder(long orderNo, long userId);

	List<StoreOrderNew> getSuborderByParentOrder(long orderNo);

	List<Map<String, Object>> exportOrderData(long beginTime, long endTime);

	/**
	 * 获取当前用户的限购活动总购买量
	 * @param restrictionActivityProductId
	 * @param storeId
	 * @return
	 */
	int getRestrictionActivityProductAllBuyCount(long restrictionActivityProductId, long storeId);

	void updatePaytime(Long orderNo);

	/**
	 * 查询用户未支付的会员订单
	 *
	 * @param user user
	 * @param memberPackageType memberPackageType
	 * @param waitPayTotalPrice waitPayTotalPrice
	 * @param clientPlatform clientPlatform
	 * @param ip ip
	 * @return com.jiuyuan.entity.newentity.StoreOrderNew
	 * @author Charlie
	 * @date 2018/8/18 13:01
	 */
	StoreOrderNewSon findNoPayMemberOrder(StoreBusiness user, Integer memberPackageType, Double waitPayTotalPrice, ClientPlatform clientPlatform, String ip);

	List<StoreOrderNew> findHistorySuccessMemberOrder(Long storeId, Integer packageType);
}