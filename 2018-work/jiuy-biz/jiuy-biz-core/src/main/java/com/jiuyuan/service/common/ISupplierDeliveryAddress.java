package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.SupplierDeliveryAddress;

public interface ISupplierDeliveryAddress {

	/**
	 * 判断是否有默认收货地址
	 * @param supplierId
	 * @return
	 */
	boolean checkDefault(long supplierId);

	/**
	 * 修改默认收货地址
	 * @param supplierDeliveryAddress
	 */
	void cancelDefault(SupplierDeliveryAddress supplierDeliveryAddress);

	/**
	 * 获取默认收货地址
	 * @param supplierId
	 * @return
	 */
	SupplierDeliveryAddress getDefaultAddress(long supplierId);

	/**
	 * 添加收货地址
	 * @param supplierDeliveryAddress
	 */
	void add(SupplierDeliveryAddress supplierDeliveryAddress);

	SupplierDeliveryAddress selectById(long addressId);

	/**
	 * 修改收货地址
	 * @param supplierDeliveryAddress
	 */
	void update(SupplierDeliveryAddress supplierDeliveryAddress);

	/**
	 * 收货地址列表
	 * @param supplierId
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> selectList(long supplierId, Page<Map<String, Object>> page);

	List<SupplierDeliveryAddress> selectListBySupplierId(Long supplierId);

}