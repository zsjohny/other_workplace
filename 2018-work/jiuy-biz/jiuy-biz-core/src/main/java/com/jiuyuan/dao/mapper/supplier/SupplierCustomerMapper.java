package com.jiuyuan.dao.mapper.supplier;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.SupplierCustomer;

/**
 * <p>
  * 供应商客户表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-13
 */
@DBMaster
public interface SupplierCustomerMapper extends BaseMapper<SupplierCustomer> {

	Map<String, Object> getCustomerDetailByPhone(@Param("phoneNumber")String phoneNumber, @Param("supplierId")long supplierId);

	List<Map<String, Object>> getCustomerList(
			@Param("supplierId")long supplierId, 
			@Param("customerName")String customerName,
			@Param("remarkName")String remarkName, 
			@Param("businessName")String businessName,
			@Param("phoneNumber") String phoneNumber,
			@Param("moneyMin")double moneyMin,
			@Param("moneyMax") double moneyMax,
			@Param("countMin") int countMin, 
			@Param("countMax")int countMax, 
			@Param("groupId")long groupId,
			@Param("province")String province, 
			@Param("city")String city,
			@Param("orderType")int orderType, 
			@Param("customerType")int customerType, 
			@Param("page") Page<Map<String, Object>> page);

	void updateSupplierCustomerAddressInfoByStoreIds(@Param("Set") Set<Long> Set,@Param("city") String city,@Param("province") String province,@Param("businessAddress") String businessAddress,@Param("county") String county);

	List<SupplierCustomer> getCustomerByStoreIdOrPhoneNumber(@Param("phoneNumber")String phoneNumber, @Param("storeId")Long storeId, @Param("supplierId")Long supplierId);
	

}