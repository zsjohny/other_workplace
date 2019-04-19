package com.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.coupon.StoreCoupon;
import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.entity.GlobalSetting;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.store.entity.ShopCoupon;
import com.store.service.coupon.ShopCouponTemplateService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.order.OrderCouponStatus;
import com.jiuyuan.entity.query.PageQuery;
import com.store.dao.mapper.OrderCouponMapper;

/**
* @author QiuYuefan
*/

@Service
public class ShopCouponService{

	private static final Log logger = LogFactory.get();

	@Autowired
	ShopCouponTemplateService shopCouponTemplateService;
	@Autowired
    private OrderCouponMapper orderCouponMapper;
	@Autowired
	private ShopGlobalSettingService globalSettingService;

	/**
	 * 获取可用的优惠券列表数量
	 * @param storeId
	 * @return
	 */
	public int getUnusedShopCouponListCount(long storeId) {
		return orderCouponMapper.getUnusedShopCouponListCount(storeId);
	}

	/**
	 * 获取可用的优惠券列表
	 * @return
	 */
	public List<ShopCoupon> getUnusedShopCouponList(long storeId, OrderCouponStatus status, PageQuery pageQuery) {
		return orderCouponMapper.getUnusedShopCouponList(storeId, status.getIntValue(),pageQuery);
	}

	/**
	 * 获取失效的优惠券列表数量
	 * @param storeId
	 * @return
	 */
	public int getUsedShopCouponListCount(long storeId) {
		return orderCouponMapper.getUsedShopCouponListCount(storeId);
	}

	/**
	 * 获取失效的优惠券列表
	 * @return
	 */
	public List<ShopCoupon> getUsedShopCouponList(long storeId, OrderCouponStatus status, PageQuery pageQuery) {
		return orderCouponMapper.getUsedShopCouponList(storeId, status.getIntValue(),pageQuery);
	}

	/**
	 * 删除优惠券
	 * @param shopCouponId
	 * @return
	 */
	public int deleteShopCoupon(long shopCouponId) {
		return orderCouponMapper.deleteShopCoupon(shopCouponId,System.currentTimeMillis());
	}

	/**
	 * 给新注册并且第一次通过审核的用户发送优惠券
	 * @param couponCount
	 * @param storeBusiness
	 * @param storeCouponTemplateId
	 * @return
	 */
	public boolean batchStoreCouponToNewStoreAudit(int couponCount, long storeCouponTemplateId, StoreBusiness storeBusiness) {
		int i = 0;
		long time = System.currentTimeMillis();
		StoreCouponTemplate storeCouponTemplate = shopCouponTemplateService.getById(storeCouponTemplateId);
		if(storeCouponTemplate.getValidityEndTime()>time || storeCouponTemplate.getValidityEndTime()==0){
			shopCouponTemplateService.updateSpecial(storeCouponTemplateId, null, couponCount, couponCount, couponCount);
			List<StoreCoupon> storeCouponList = new ArrayList<StoreCoupon>();
			while(i<couponCount){
				StoreCoupon storeCoupon = new StoreCoupon();
				storeCoupon.setCouponTemplateId(storeCouponTemplate.getId());
				storeCoupon.setTemplateName(storeCouponTemplate.getName());
				storeCoupon.setType(storeCouponTemplate.getType());
				storeCoupon.setMoney(storeCouponTemplate.getMoney());
				storeCoupon.setRangeType(storeCouponTemplate.getRangeType());
				storeCoupon.setRangeTypeIds(storeCouponTemplate.getRangeTypeIds());
				storeCoupon.setRangeTypeNames(storeCouponTemplate.getRangeTypeNames());
				storeCoupon.setRangeContent(storeCouponTemplate.getRangeContent());
				storeCoupon.setValidityStartTime(storeCouponTemplate.getValidityStartTime());
				storeCoupon.setValidityEndTime(storeCouponTemplate.getValidityEndTime());
				storeCoupon.setIsLimit(storeCouponTemplate.getIsLimit());
				storeCoupon.setCoexist(storeCouponTemplate.getCoexist());
				storeCoupon.setGrantAdminId(-1L);
				storeCoupon.setPublishAdminId(-1L);
				storeCoupon.setPushStatus(-1);
				storeCoupon.setStoreId(storeBusiness.getId());
				storeCoupon.setBusinessNumber(storeBusiness.getBusinessNumber());
				storeCoupon.setCreateTime(time);
				storeCoupon.setUpdateTime(time);
				storeCoupon.setGetWay(0);
				storeCoupon.setLimitMoney(storeCouponTemplate.getLimitMoney());
				storeCouponList.add(storeCoupon);
				i++;
			}
			orderCouponMapper.batchAddByCoupons(storeCouponList);

			//更新优惠券可用发行余额，已发行余额
			updateGlobalSettingStoreCoupon(storeCouponTemplate.getMoney()*couponCount);
			return true;
		}else{
			return false;
		}
	}


	/**
	 * 更新优惠券可用发行余额，已发行余额
	 * @param money
	 */
	private void updateGlobalSettingStoreCoupon(double money){
		//更新优惠券可用发行余额，已发行余额
		GlobalSetting globalSetting = globalSettingService.getItem(GlobalSettingName.STORE_COUPON);
		Double publishedMoney = 0.0;
		Double totalMoney = 0.0;
		if (globalSetting != null) {
			String propertyValue = globalSetting.getPropertyValue();
			if(!StringUtils.isEmpty(propertyValue)){
				JSONObject jsonObject = JSONObject.parseObject(propertyValue);
				if (jsonObject != null) {
					publishedMoney = Double.parseDouble(jsonObject.get("published_money").toString());
					totalMoney = Double.parseDouble(jsonObject.get("total_money").toString());
				}
			}
		}

		Map<String,String> property = new HashMap<String,String>();
		property.put("published_money", String.format("%.2f", money+publishedMoney));
		property.put("total_money", String.format("%.2f", money+totalMoney));
		String json = JSONObject.toJSON(property).toString();
		globalSetting.setPropertyValue(json);
		globalSetting.setUpdateTime(System.currentTimeMillis());

		globalSettingService.update(globalSetting);
	}

}