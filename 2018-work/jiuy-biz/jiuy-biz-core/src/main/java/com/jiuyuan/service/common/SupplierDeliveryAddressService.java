package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.SupplierDeliveryAddressMapper;
import com.jiuyuan.entity.newentity.SupplierDeliveryAddress;

/**
 * <p>
 * 供应商收货地址表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-23
 */
@Service
public class SupplierDeliveryAddressService implements ISupplierDeliveryAddress {
	@Autowired
	private SupplierDeliveryAddressMapper deliveryAddressMapper;
	
	
	
	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.ISupplierDeliveryAddress#checkDefault(long)
	 */
	@Override
	public boolean checkDefault(long supplierId) {
		Wrapper<SupplierDeliveryAddress> wrapper = new EntityWrapper<SupplierDeliveryAddress>();
		wrapper.eq("supplier_id", supplierId).eq("default_address", 1).eq("status", 0);
		Integer count = deliveryAddressMapper.selectCount(wrapper);
		if (count > 0) {
			return true;
		}
		return false;
	}


	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.ISupplierDeliveryAddress#cancelDefault(com.jiuyuan.entity.newentity.SupplierDeliveryAddress)
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void cancelDefault(SupplierDeliveryAddress supplierDeliveryAddress) {
		SupplierDeliveryAddress entity = new SupplierDeliveryAddress();
		entity.setId(supplierDeliveryAddress.getId());
		entity.setDefaultAddress(0);
		entity.setUpdateTime(System.currentTimeMillis());
		deliveryAddressMapper.updateById(entity);
		
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.ISupplierDeliveryAddress#getDefaultAddress(long)
	 */
	@Override
	public SupplierDeliveryAddress getDefaultAddress(long supplierId) {
		Wrapper<SupplierDeliveryAddress> wrapper = new EntityWrapper<SupplierDeliveryAddress>();
		wrapper.eq("supplier_id", supplierId).eq("default_address", 1).eq("status", 0);
		List<SupplierDeliveryAddress> list = deliveryAddressMapper.selectList(wrapper);
		SupplierDeliveryAddress supplierDeliveryAddress = null ;
//		if (list.size()<1) {
//			throw new RuntimeException("无默认收货地址！");
//		}
		if (list.size()>0) {
			return  list.get(0);
		}else{
			return supplierDeliveryAddress;
		}
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.ISupplierDeliveryAddress#add(com.jiuyuan.entity.newentity.SupplierDeliveryAddress)
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void add(SupplierDeliveryAddress supplierDeliveryAddress) {
		deliveryAddressMapper.insert(supplierDeliveryAddress);
	}

	
	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.ISupplierDeliveryAddress#selectById(long)
	 */
	@Override
	public SupplierDeliveryAddress selectById(long addressId) {
		return deliveryAddressMapper.selectById(addressId);
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.ISupplierDeliveryAddress#update(com.jiuyuan.entity.newentity.SupplierDeliveryAddress)
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void update(SupplierDeliveryAddress supplierDeliveryAddress) {
		deliveryAddressMapper.updateById(supplierDeliveryAddress);
		
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.ISupplierDeliveryAddress#selectList(long, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public List<Map<String, Object>> selectList(long supplierId, Page<Map<String, Object>> page) {
		Wrapper<SupplierDeliveryAddress> wrapper = new EntityWrapper<SupplierDeliveryAddress>();
		wrapper.eq("supplier_id", supplierId).eq("status", 0).orderBy("default_address",false);
		List<Map<String, Object>> selectMapsPage = deliveryAddressMapper.selectMapsPage(page, wrapper );
		return selectMapsPage;
	}

    /**
     * 获取该商家的收货信息
     */
	@Override
	public List<SupplierDeliveryAddress> selectListBySupplierId(Long supplierId) {
		Wrapper<SupplierDeliveryAddress> wrapper = new EntityWrapper<SupplierDeliveryAddress>();
		wrapper.eq("supplier_id", supplierId)
		       .eq("status", SupplierDeliveryAddress.NORMAL_STATUS)
		       .orderBy("create_time",false);
		List<SupplierDeliveryAddress> list = deliveryAddressMapper.selectList(wrapper);
		return list;
	}

}
