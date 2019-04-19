package com.e_commerce.miscroservice.supplier.service;


import com.e_commerce.miscroservice.commons.entity.application.order.*;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.supplier.entity.request.StoreRefundOrderActionLog;

import java.util.List;
import java.util.Map;

public interface IOrderNewService {


	/**
	 * 获取订单总数，今日新增个数，待处理个数
	 * @param userId
	 * @return
	 */
	Map<String, Object> getSupplierOrderCount(long userId);

	//获取待处理个数
	Integer getSupplierOrderCountUnDealWithCount(long userId);

	/**
	 * 根据订单号获取商品的详情信息
	 */
	StoreOrder selectInfo(Long orderNo);
	/**
	 *根据用户id查询号码
	 */
	String selectById(Long storeId);

	/**
	 * 根据phoneNumber,storeId,supplierId查询信息
	 * @param phoneNumber
	 * @param storeId
	 * @param supplierId
	 * @return
	 */
	public SupplierCustomer getCustomerByStoreIdOrPhoneNumber(String phoneNumber, Long storeId, Long supplierId);

	/**
	 * 是否有售后：0没有售后、1有售后
	 * @param orderNo
	 * @return
	 */
	 Integer getHaveRefund(long orderNo);

	/**
	 * 根据用户id查询商家名称
	 * @param orderNo
	 * @return
	 */
	String selectBusinessNameById(Long orderNo);

	/**
	 * 根据订单查询物流信息
	 * @param expressInfo
	 * @return
	 */
	StoreExpressInfo selectOne(StoreExpressInfo expressInfo);

	/**
	 * 根据快递名查询快递商信息
	 * @param EngName
	 * @return
	 */
	List<ExpressSupplier> selectList(String EngName);


	/**
	 * 查询订单列表
	 * @param orderNo
	 * @return
	 */
	public List<Map<String,Object>> getSupplierOrderItemByOrderNo(long orderNo);

	/**
	 * 获取邮递公司名称列表用户数据回显
	 */
	public List<Map<String,Object>> getAllExpressCompanyNames();

	/**
	 * 获取所有订单
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
	 * @param refundUnderway
	 * @return
	 */
	List<Map<String,Object>> getSupplierOrderList(long userId, long orderNo, int orderStatus, String addresseeName,
												  String addresseeTelePhone, String clothesNumbers, String updateTimeBegin, String updateTimeEnd,
												  String remark, String customerName, String customerPhone, String expressNo, String createTimeBegin,
												  String createTimeEnd, int refundUnderway);

	/**
	 * 根据订单号查询售后信息
	 */
	List<Map<String, Object>> getRefundItenList(Long orderNo);

	/**
	 * 立即发货
	 */
	//Response sendStore(long orderNo, String expressCompamyName, String expressNo, String remark,int type,List<StoreSend> storeSendsList);
	Response sendStore(Long orderNo, String expressInfo,String expressNo, String storeSendList, String remark,Integer type);














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


/**
 * 恢复价格
 */
	void changeOrderPrice(Long orderNo,Long userId);


	/**
	 * 获取所有的子订单
	 */
	List<StoreOrder> selectOrderSonAll(Long orderNo);

	/**
	 * 获取供应商的supplierId
	 */
	Long getSupplierId(Long userId);

    List<StoreRefundOrderActionLog> selectRefundLog(Long orderNo);
}