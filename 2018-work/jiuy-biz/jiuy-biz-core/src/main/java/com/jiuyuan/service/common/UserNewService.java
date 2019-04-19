package com.jiuyuan.service.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.BrandNewMapper;
import com.jiuyuan.dao.mapper.supplier.LOWarehouseNewMapper;
import com.jiuyuan.dao.mapper.supplier.ShopUserMapper;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.LOWarehouseNew;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.UserNew;

@Service
public class UserNewService implements IUserNewService {
	private static final Logger logger = LoggerFactory.getLogger(UserNewService.class);
   @Autowired
   private UserNewMapper supplierUserMapper;
   
   @Autowired
   private BrandNewMapper supplierBrandMapper;
   
   @Autowired
   private LOWarehouseNewMapper supplierLOWarehouseMapper;
   
   @Autowired
	private ShopUserMapper shopUserMapper;
   

	
	public StoreBusiness getStoreBusinessByStoreId(long storeId) {
		return shopUserMapper.getStoreBusinessByStoreId(storeId);
	}	
   

	/* (non-Javadoc)
	 * @see com.supplier.service.ISupplierUserService#getSupplierUserInfo(long)
	 */
	@Override
	public UserNew getSupplierUserInfo(long id) {
		return supplierUserMapper.selectById(id);
	}
	
	/* (non-Javadoc)
	 * @see com.supplier.service.ISupplierUserService#getSupplierBrandInfo(long)
	 */
	@Override
	public BrandNew getSupplierBrandInfo(long brandId) {
		Wrapper<BrandNew> wrapper = new EntityWrapper<BrandNew>().eq("BrandId", brandId);
		List<BrandNew> list = supplierBrandMapper.selectList(wrapper);
		BrandNew supplierBrand = null ;
		if(list.size() >0){
			supplierBrand = list.get(0);
		}
		return supplierBrand;
	
	}
	/* (non-Javadoc)
	 * @see com.supplier.service.ISupplierUserService#getSupplierBrandInfoBySupplierId(long)
	 */
	@Override
	public BrandNew getSupplierBrandInfoBySupplierId(long supplierId) {
		UserNew supplierUser = getSupplierUserInfo(supplierId);
	    long BrandId = supplierUser.getBrandId();
		BrandNew supplierBrand= getSupplierBrandInfo(BrandId);
		return supplierBrand;
	}

	/* (non-Javadoc)
	 * @see com.supplier.service.ISupplierUserService#getSupplierLowarehouse(long)
	 */
	@Override
	public LOWarehouseNew getSupplierLowarehouse(long lowarehouseId) {
		
		return supplierLOWarehouseMapper.selectById(lowarehouseId);
	}

	@Override
	public List<UserNew> getSupplierUserInfoByPhoneNumber(String phoneNumber) {
		Wrapper<UserNew> wrapper = new EntityWrapper<UserNew>().eq("phone", phoneNumber);
		return supplierUserMapper.selectList(wrapper);
	}
	/**
	 * 根据品牌ID获取供应商信息
	 */
	@Override
	public UserNew getSupplierByBrandId(long brandId) {
		//UserNew ---> supplier_user表
		Wrapper<UserNew> wrapper = new EntityWrapper<UserNew>().eq("brand_id", brandId);
		List<UserNew> list = supplierUserMapper.selectList(wrapper);
		UserNew supplier = null ;
		if(list.size() >0){
			supplier = list.get(0);
		}
		return supplier;
	}

	/**
	 * 根据品牌ID获取供应商信息
	 */
	@Override
	public Map<Long,UserNew> getSupplierUsers(Set<Long> brandIds) {

		Wrapper<UserNew> wrapper = new EntityWrapper<UserNew>().in("brand_id", brandIds);
		List<UserNew> list = supplierUserMapper.selectList(wrapper);
		Map<Long,UserNew> userNewMap = new HashMap<>();
		list.forEach(action->{
			userNewMap.putIfAbsent(action.getBrandId(), action);
		});
		return userNewMap;
	}

	/**
	 * 根据仓库ID获得对应供应商信息
	 * @param lowarehouseId
	 * @return
	 */
	public UserNew getSupplierByLowarehouseId(long lowarehouseId){
		Wrapper<UserNew> wrapper = new EntityWrapper<UserNew>().eq("lowarehouse_id", lowarehouseId);
		List<UserNew> list = supplierUserMapper.selectList(wrapper);
		UserNew supplier = null ;
		if(list.size() >0){
			supplier = list.get(0);
		}
		return supplier;
	}


	public void addReceiverInfo(long supplierId, String receiver, String supplierReceiveAddress, String receiverPhone) {
		UserNew userNew = new UserNew();
		userNew.setId(supplierId);
//		userNew.setReceiver(receiver);
//		userNew.setSupplierReceiveAddress(supplierReceiveAddress);
//		userNew.setReceiverPhone(receiverPhone);
		int i = supplierUserMapper.updateById(userNew);
		if(i == -1){
			logger.error("供应商收货信息添加失败！");
			throw new RuntimeException("供应商收货信息添加失败！");
		}
	}


	@Override
	
	public void insertUser() {
		insert();

		
		
	}

	@Transactional(rollbackFor=Exception.class)
	private void insert() {
		try {
			UserNew user = new UserNew();
			user.setName("抛runtimeException测试！11");
			supplierUserMapper.insert(user);
			Long a = Long.parseLong("a");
		} catch (Exception e) {
			logger.info("开始抛RuntimeException");
			throw new RuntimeException("开始抛RuntimeException");
		}

		
	}



   
   
}
