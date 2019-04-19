package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.SupplierCustomerGroupMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierCustomerMapper;
import com.jiuyuan.entity.newentity.SupplierCustomer;
import com.jiuyuan.entity.newentity.SupplierCustomerGroup;
import com.xiaoleilu.hutool.db.Entity;

/**
 * <p>
 * 供应商分组表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-13
 */
@Service
public class SupplierCustomerGroupService implements ISupplierCustomerGroupService {

	@Autowired
	private SupplierCustomerGroupMapper customerGroupMapper;
	@Autowired
	private SupplierCustomerMapper supplierCustomerMapper;

	@Override
	public void insert(SupplierCustomerGroup customerGroup) {
		customerGroupMapper.insert(customerGroup);

	}

	@Override
	public List<Map<String, Object>> selectList(long supplierId, String groupName, String remark,
			Page<Map<String, Object>> page) {
		List<Map<String,Object>> groupList = customerGroupMapper.getGroupListBySearchArgs(supplierId, groupName, remark, page);
		for (Map<String, Object> map : groupList) {
			Wrapper<SupplierCustomer> SupplierCustomerWrapper = new EntityWrapper<>();
			SupplierCustomerWrapper.eq("supplier_id", supplierId).eq("status", 0);
			long id = (long) map.get("id");
			SupplierCustomerWrapper.eq("group_id", id);
			Integer selectCount = supplierCustomerMapper.selectCount(SupplierCustomerWrapper);
			map.put("customerCount", selectCount);
		}
		return groupList;
	}

	@Override
	public long insertAndGetId(SupplierCustomerGroup customerGroup) {
		long id = customerGroupMapper.insertAndGetId(customerGroup);
		return id;
	}

	@Override
	public SupplierCustomerGroup getCustomerGroupById(long id) {
		return customerGroupMapper.selectById(id);
	}

	@Override
	public List<Map<String, Object>> getGroupList(long userId) {
		List<Map<String, Object>> list = customerGroupMapper.getGroupList(userId);
		return list;
	}

	@Override
	public int selectCustomerCount(long groupId, long supplierId) {
		Wrapper<SupplierCustomer> SupplierCustomerWrapper = new EntityWrapper<>();
		SupplierCustomerWrapper.eq("supplier_id", supplierId).eq("status", 0);
		SupplierCustomerWrapper.eq("group_id", groupId);
		Integer selectCount = supplierCustomerMapper.selectCount(SupplierCustomerWrapper);
		return selectCount;
	}

	@Override
	public void update(SupplierCustomerGroup customerGroup) {
		customerGroupMapper.updateById(customerGroup);
	}

	@Override
	public SupplierCustomerGroup selectById(long groupId) {
		return customerGroupMapper.selectById(groupId);
	}
	/**
	 * 分组数量
	 */
	@Override
	public int selectCount(long supplierId) {
		Wrapper<SupplierCustomerGroup> wrapper = new EntityWrapper<>();
		wrapper.eq("supplier_id", supplierId).eq("status", 0);
		Integer selectCount = customerGroupMapper.selectCount(wrapper );
		return selectCount;
	}

	@Override
	public int selectGroupCustomerCount(long supplierId) {
		Wrapper<SupplierCustomerGroup> wrapper = new EntityWrapper<>();
		wrapper.eq("supplier_id", supplierId).eq("status", 0).eq("default_group", 0);
		List<SupplierCustomerGroup> selectList = customerGroupMapper.selectList(wrapper);
		int groupCustomerCount = 0;
		for (SupplierCustomerGroup supplierCustomerGroup : selectList) {
			int count = selectCustomerCount(supplierCustomerGroup.getId(), supplierId);
			groupCustomerCount += count;
		}
		return groupCustomerCount;
	}

	@Override
	public SupplierCustomerGroup getDefaultGroup(long supplierId) {
		Wrapper<SupplierCustomerGroup> wrapper = new EntityWrapper<>();
		wrapper.eq("supplier_id", supplierId).eq("status", 0).eq("default_group", 1);
		List<SupplierCustomerGroup> customerGroups = customerGroupMapper.selectList(wrapper);
		return customerGroups.size()>0?customerGroups.get(0):null;
	}

	@Override
	public boolean validateGroupName(String groupName, long supplierId,long groupId) {
		if (groupId != 0) {
			SupplierCustomerGroup customerGroup = customerGroupMapper.selectById(groupId);
			if (groupName.equals(customerGroup.getGroupName())) {
				return true;
			}
		}
		Wrapper<SupplierCustomerGroup> wrapper = new EntityWrapper<>();
		wrapper.eq("supplier_id", supplierId).eq("status", 0).eq("group_name", groupName);
		Integer count = customerGroupMapper.selectCount(wrapper);
		if (count>0) {
			return false;
		}
		return true;
	}

}
