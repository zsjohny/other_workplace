package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.LOWarehouseNew;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.UserNew;

public interface IUserNewService {

	UserNew getSupplierUserInfo(long id);

	BrandNew getSupplierBrandInfo(long brandId);

	/**
	 * 根据供应商ID获取供应商品牌
	 * @param supplierId
	 * @return
	 */
	BrandNew getSupplierBrandInfoBySupplierId(long supplierId);

	LOWarehouseNew getSupplierLowarehouse(long lowarehouseId);

	List<UserNew> getSupplierUserInfoByPhoneNumber(String phoneNumber);

	UserNew getSupplierByBrandId(long brandId);
	
	/**
	 * 根据仓库ID获得对应供应商信息
	 * @param lowarehouseId
	 * @return
	 */
	public UserNew getSupplierByLowarehouseId(long lowarehouseId);
	
	public StoreBusiness getStoreBusinessByStoreId(long storeId);

	void insertUser();

	void addReceiverInfo(long supplierId, String receiver, String supplierReceiveAddress, String receiverPhone);


	/**
	 * 根据品牌ID获取供应商信息
	 */
	Map<Long,UserNew> getSupplierUsers(Set<Long> brandIds);

}