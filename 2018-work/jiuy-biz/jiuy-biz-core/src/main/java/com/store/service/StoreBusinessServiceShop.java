package com.store.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.MemcachedService;
import com.store.dao.mapper.StoreBusinessMapper;
import com.store.entity.coupon.ShopMemberCoupon;


@Service
public class StoreBusinessServiceShop {
	private static final Logger logger = LoggerFactory.getLogger(StoreBusinessServiceShop.class);
	@Autowired
	private StoreBusinessMapper storeBusinessMapper;
	
	@Autowired
	private MemcachedService memcachedService;

	public StoreBusiness getById(long storeId) {
		return storeBusinessMapper.getById(storeId);
	}
	public double getPercentById(long id) {
		if (id == 0) return 0;
		
		String groupKey = MemcachedKey.GROUP_KEY_STORE_BUSINESS;
        
        String key = "percent";
        Object obj = memcachedService.get(groupKey, key);
        
        StoreBusiness storeBusiness = null;
        if (obj != null) {
        	Map<Long, StoreBusiness> map = (Map<Long, StoreBusiness>)obj;
        	storeBusiness = map.get(id);
        	return storeBusiness.getCommissionPercentage();
        } else {
        	Map<Long, StoreBusiness> map = storeBusinessMapper.getAllMap();
        	storeBusiness = map.get(id);
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, map);
        }
        
		return storeBusiness != null ? storeBusiness.getCommissionPercentage() : 0;
	}

	/**
	 * 核销优惠券之后更新门店核销记录
	 * @param i
	 * @param j
	 * @param money
	 * @param storeId 
	 */
	public void updMemberCouponTotal(int memberCount, int couponCount, double money, long storeId) {
		int count = storeBusinessMapper.updMemberCouponTotal(memberCount,couponCount,money,storeId);
	}
	
	/**
	 * 获取全部商家列表
	 * @return
	 */
	public List<StoreBusiness> getAllOpenWxaStoreList() {
		List<StoreBusiness>  AllStoreList = storeBusinessMapper.getAllOpenWxaStoreList();
		return AllStoreList;
	}
	
	/**
	 * 保存倍率
	 * @param rate
	 * @param storeId
	 * @return
	 */
	public void saveRate(double rate, long storeId) {
		//获取
		int i = storeBusinessMapper.updateRate(rate, storeId);
	    if(i == -1){
	    	logger.info("保存倍率失败，请排查问题！！");
	    	throw new RuntimeException("保存倍率失败，请排查问题！！");
	    }
		
	}
	/**
	 * 更改同步上新按钮状态
	 * @param synchronousButtonStatus  0：关闭 1：开启
	 * @param storeId
	 * @return
	 */
	public void updateButtonStatus(int synchronousButtonStatus, long storeId) {
		//获取
		int i = storeBusinessMapper.updateButtonStatus(synchronousButtonStatus, storeId);
	    if(i == -1){
	    	logger.info("更新同步上新按钮失败，请排查问题！！");
	    	throw new RuntimeException("更新同步上新按钮失败，请排查问题！！");
	    }
	}
	
	
}