package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.SupplierCustomerGroup;

/**
 * <p>
 * 供应商分组表 服务类
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-13
 */
public interface ISupplierCustomerGroupService  {

	void insert(SupplierCustomerGroup customerGroup);

	List<Map<String, Object>> selectList(long supplierId, String groupName, String remark, Page<Map<String, Object>> page);

	long insertAndGetId(SupplierCustomerGroup customerGroup);

	SupplierCustomerGroup getCustomerGroupById(long id);

	List<Map<String, Object>> getGroupList(long userId);

	int selectCustomerCount(long groupId, long supplierId);

	void update(SupplierCustomerGroup customerGroup);

	SupplierCustomerGroup selectById(long groupId);

	int selectCount(long supplierId);

	int selectGroupCustomerCount(long supplierId);

	SupplierCustomerGroup getDefaultGroup(long supplierId);

	boolean validateGroupName(String groupName, long supplierId, long groupId);
	
}
