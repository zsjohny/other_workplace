package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.SupplierCustomer;

public interface ISupplierCustomer {

	/**
	 * 新增客户
	 * @param supplierCustomer
	 */
	void insert(SupplierCustomer supplierCustomer);

	/**
	 * 更新客户信息
	 * @param supplierCustomer
	 */
	void update(SupplierCustomer supplierCustomer);

	Map<String, Object> getCustomerDetailById(long customerId);

	SupplierCustomer getCustomerByPhone(String phoneNumber, long userId);

	SupplierCustomer getCustomerById(long customerId);

	List<Map<String, Object>> getCustomerList(long userId, String customerName,String remarkName, String businessName, String phoneNumber,
			double moneyMin, double moneyMax, int countMin, int countMax, long groupId, String province, String city,
			int orderType, int customerType, Page<Map<String, Object>> page);

	Map<String, Object> getNewAndOldCustomerCount(long userId);

	SupplierCustomer getCustomerByStoreId(long storeId, long supplierId);

	List<SupplierCustomer> selectListByStoreId(long storeId);

	void updateCustomerStatus(long storeId, int status);

	SupplierCustomer getCustomerByStoreIdOrPhoneNumber(String phoneNumber, Long storeId, Long supplierId);


}