package com.jiuyuan.service.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.StoreOrderItemNew;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.StoreRefundOrderActionLog;

public interface IOrderNewService {

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
	 * @param
	 * @return 
	 * @return
	 */
	List<StoreOrderNew> getSupplierOrderList(long userId, long orderNo, int orderStatus, String phoneNumber,
			String clothesNumbers, long updateTimeStart, long updateTimeEnd, Page<StoreOrderNew> page);

	/**
	 * 获取订单总数，今日新增个数，待处理个数
	 * @param userId
	 * @return
	 */
	Map<String, Object> getSupplierOrderCount(long userId);
	
	/**
	 * 获取售后中订单个数
	 * @param userId
	 * @return
	 */
	public Object getSupplierOrderCountUnDealRefundOrderCount(long userId);

	//获取待处理个数
	Object getSupplierOrderCountUnDealWithCount(long userId);

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
	 * @param expressCompanyChineseName
     * @throws Exception
	 */
	void deliverGoods(long orderNo, String expressCompamyName, String expressNo, String remark, String expressCompanyChineseName) throws Exception;

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
	 * @param businessName 
	 * @param moneyMin
	 * @param moneyMax
	 * @param countMin
	 * @param countMax
	 * @param province
	 * @param city
	 * @param
	 * @return
	 */
	List<Map<String, Object>> getSupplierCustomerList(long userId, String businessName,String phoneNumber,  double moneyMin, double moneyMax,
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
			String clothesNumbers, long updateTimeStart, long updateTimeEnd, Page<StoreOrderNew> page);

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
	
	/**
	 * 新的写法的确认收货
	 */
	public void updateOrderNewStatus(StoreOrderNew order, OrderStatus newStatus, OrderStatus oldStatus,long time);
	
	/**
	 * 根据订单OrderNo和StoreId获取对应的OrderItem
	 * @param storeId
	 * @param orderNo
	 * @return
	 */
	public List<StoreOrderItemNew> getOrderNewItemsByOrderNO(long storeId, long orderNo);

	/**
	 * 门店订单确认收货时发放个人门店激活奖金和团队激活奖金和修改个人门店订单交易奖金和团队订单交易奖金时间
	 * @param orderNo
	 * @param storeId
	 * @param groundUserId
	 */
	public void grantBonuses(long orderNo,long storeId,Long groundUserId);

	/**
	 * 获取供应商待发货订单列表
	 * @param userId
	 * @param orderNo
	 * @param addresseeName
	 * @param addresseeTelePhone
	 * @param clothesNumbers
	 * @param payTimeBegin
	 * @param payTimeEnd
	 * @param remark
	 * @param customerName
	 * @param customerPhone
	 * @param expressNo
	 * @param page
	 * @return
	 */
	List<Map<String,Object>> getSupplierOrderListPendingDelivery(long userId, long orderNo, String addresseeName,
			String addresseeTelePhone, String clothesNumbers, String payTimeBegin, String payTimeEnd, String remark,
			String customerName, String customerPhone, String expressNo, Page<Map<String, Object>> page);

	/**
	 * 导出供应商待发货订单列表EXCEL
	 * @param userId
	 * @param orderNo
	 * @param addresseeName
	 * @param addresseeTelePhone
	 * @param clothesNumbers
	 * @param payTimeBegin
	 * @param payTimeEnd
	 * @param remark
	 * @param customerName
	 * @param customerPhone
	 * @param expressNo
	 * @param
	 * @return
	 */
	List<Map<String, Object>> outPendingDeliveryOrderExcel(long userId, long orderNo, String addresseeName,
			String addresseeTelePhone, String clothesNumbers, String payTimeBegin, String payTimeEnd, String remark,
			String customerName, String customerPhone, String expressNo);
	
	/**
	 * 剩余卖家自动确认收货时间毫秒数
	 * @param sendTime 买家发货时间
	 * @param refundStartTime 卖家自动确认收货暂停时间，为0是则表示未暂停，大于0表示已暂停
	 * @param autoTakeDeliveryPauseTimeLength 卖家自动确认收货总暂停时长（毫秒）
	 * @return
	 */
	public long buildSurplusSupplierAutoTakeTime(long sendTime,long refundStartTime,long autoTakeDeliveryPauseTimeLength);

	/**
	 * 获取供应商列表
	 * @param userId
	 * @param orderNo
	 * @param orderStatus
	 * @param addresseeName
	 * @param addresseeTelePhone
	 * @param clothesNumbers
	 * @param updateTimeBegin
	 * @param updateTimeEnd
	 * @param remark
	 * @param customerName
	 * @param customerPhone
	 * @param expressNo
	 * @param createTimeBegin
	 * @param createTimeEnd
	 * @param
	 * @param pageMap
	 * @return
	 */
	List<Map<String,Object>> getSupplierOrderList(long userId, long orderNo, int orderStatus, String addresseeName,
			String addresseeTelePhone, String clothesNumbers, String updateTimeBegin, String updateTimeEnd,
			String remark, String customerName, String customerPhone, String expressNo, String createTimeBegin,
			String createTimeEnd, int refundUnderway, Page<Map<String, Object>> pageMap);

	/**
	 * 导出供应商订单列表EXCEL
	 * @param userId
	 * @param orderNo
	 * @param orderStatus
	 * @param addresseeName
	 * @param addresseeTelePhone
	 * @param clothesNumbers
	 * @param updateTimeBegin
	 * @param updateTimeEnd
	 * @param remark
	 * @param customerName
	 * @param customerPhone
	 * @param expressNo
	 * @param createTimeBegin
	 * @param createTimeEnd
	 * @param
	 * @return
	 */
	List<Map<String, Object>> outExcel(long userId, long orderNo, int orderStatus, String addresseeName,
			String addresseeTelePhone, String clothesNumbers, String updateTimeBegin, String updateTimeEnd,
			String remark, String customerName, String customerPhone, String expressNo, String createTimeBegin,
			String createTimeEnd,int refundUnderway);
	/**
	 * 修改订单供应商备注
	 * @param orderNo
	 * @param orderSupplierRemark
	 */
	void updateOrderSupplierRemark(long orderNo, String orderSupplierRemark);
	
	/**
	 * app获取供应商订单列表
	 * @param orderStatus
	 * @param page
	 * @param userDetail
	 * @return
	 */
	public List<StoreOrderNew> getSupplierOrderListByOrderStatus(int orderStatus, Page<StoreOrderNew> page,
			UserDetail<StoreBusiness> userDetail);

	/**
	 * 获取到供应商订单详情
	 * @param userDetail
	 * @param orderNo
	 * @return
	 */
	StoreOrderNew getSupplierOrderDetail(UserDetail<StoreBusiness> userDetail, long orderNo);

	void changeOrderPrice(long orderNo, long userId, double changePrice);

	void restoreOrderPrice(long orderNo, long userId);

	StoreOrderNew selectById(long orderNo);

    List<StoreRefundOrderActionLog> selectRefundLog(Long orderNo);

//	void a();
	
}