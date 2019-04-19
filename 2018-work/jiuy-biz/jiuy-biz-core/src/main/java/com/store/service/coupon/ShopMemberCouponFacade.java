package com.store.service.coupon;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.service.common.MemcachedService;
import com.store.entity.coupon.ShopMemberCoupon;
import com.store.service.StoreBusinessServiceShop;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 商家优惠券模板
 *
 * @author 赵兴林
 * @since 2017-06-21
 */
@Service
public class ShopMemberCouponFacade {
	private static final Log logger = LogFactory.get();
	
	@Autowired
    private MemcachedService memcachedService;
	
	@Autowired
	private ShopMemberCouponService shopMemberCouponService;
	
	@Autowired
	private ShopCouponTemplateService shopCouponTemplateService;
	
	@Autowired
	private StoreBusinessServiceShop storeBusinessService;

	/**
	 * 获取优惠券信息
	 * @param id
	 * @return
	 */
	public Map<String, String> getMemberCouponInfo(long id) {
		ShopMemberCoupon shopMemberCoupon = shopMemberCouponService.getMemberCouponInfo(id);
		if(shopMemberCoupon==null){
			throw new RuntimeException("没有该优惠券信息");
		}
		logger.info("获取优惠券信息shopMemberCoupon："+shopMemberCoupon);
		Map<String,String> memberCouponInfo = new HashMap<String,String>();
		memberCouponInfo.put("money", shopMemberCoupon.getStringMoney());
		memberCouponInfo.put("name", shopMemberCoupon.getName());
		Double limitMoney = shopMemberCoupon.getLimitMoney();
		if(limitMoney==null || limitMoney == 0.0){
			memberCouponInfo.put("limitText", "无金额限制");
		}else{
			memberCouponInfo.put("limitText", "满"+shopMemberCoupon.getStringLimitMoney()+"元可用");
		}
		
		memcachedService.set(MemcachedKey.GROUP_KEY_MEMBER_COUPON_EXPIRATION_TIME, id+"", 24*60*60*1000, shopMemberCoupon);
		logger.info("获取优惠券信息memcachedService："+memcachedService.getStr(MemcachedKey.GROUP_KEY_MEMBER_COUPON_EXPIRATION_TIME, id+""));
		return memberCouponInfo;
	}

	/**
	 * 核销优惠券
	 * @param id
	 * @param storeId
	 * @param ip
	 * @param client
	 */
	@Transactional(rollbackFor = Exception.class)
	public Map<String,String> updMemberCoupon(long id, long storeId, String ip, ClientPlatform client) {
		Map<String,String> result = new HashMap<String,String>();
		logger.info("核销优惠券shopMemberCouponId："+id);
		logger.info("核销优惠券key::::::::::::::::::："+MemcachedKey.GROUP_KEY_MEMBER_COUPON_EXPIRATION_TIME);
		Object obj = memcachedService.getCommon(MemcachedKey.GROUP_KEY_MEMBER_COUPON_EXPIRATION_TIME, id+"");
		logger.info("核销优惠券obj："+obj);
		logger.info("核销优惠券shopMemberCoupon："+(ShopMemberCoupon)obj);
		ShopMemberCoupon shopMemberCoupon = shopMemberCouponService.getMemberCouponInfo(id);
		String name = shopMemberCoupon.getName()+",面值￥"+shopMemberCoupon.getStringMoney();
		Double limitMoney = shopMemberCoupon.getLimitMoney();
		if(limitMoney==null || limitMoney==0.0){
			name+="无门槛";
		}else{
			name+="满"+shopMemberCoupon.getStringLimitMoney()+"可用";
		}
		result.put("name", name);
		
		if(obj==null){//缓存时间已过
			result.put("result", "核销失败");
			result.put("text", "二维码已失效");
		}else{
			if(shopMemberCoupon.getStatus()==ShopMemberCoupon.status_used){//该优惠券已经使用
				result.put("result", "核销失败");
				result.put("text", "优惠券已被使用过了");
			}else if(shopMemberCoupon.isOverdue()){//该优惠券已经过期
				result.put("result", "核销失败");
				result.put("text", "优惠券已过期");
			}else if(shopMemberCoupon.getStatus()==ShopMemberCoupon.status_del){//该优惠券已经删除
				throw new RuntimeException("该优惠券已经删除");
			}else if(shopMemberCoupon.isNotBegin()){//该优惠券有效期还未开始
				throw new RuntimeException("该优惠券有效期还未开始");
			}else if(shopMemberCoupon.getStoreId()!=storeId){//该优惠券不是本店的
				throw new RuntimeException("该优惠券不是本店的");
			}else{ 
				long time = System.currentTimeMillis();
				shopMemberCoupon.setStatus(ShopMemberCoupon.status_used);
				shopMemberCoupon.setAdminId(1L);
				shopMemberCoupon.setCheckMoney(shopMemberCoupon.getMoney());
				shopMemberCoupon.setCheckTime(time);
				shopMemberCoupon.setUpdateTime(time);
				if(shopMemberCouponService.updMemberCoupon(shopMemberCoupon)){//核销优惠券
					shopCouponTemplateService.updCouponTemplateCount(shopMemberCoupon.getCouponTemplateId(), storeId, ip, client);
					
					//核销优惠券之后更新门店核销记录
					int count = shopMemberCouponService.getMemberCouponTotalByMemberId(shopMemberCoupon);
					if(count>0){
						storeBusinessService.updMemberCouponTotal(0,1,shopMemberCoupon.getMoney(),shopMemberCoupon.getStoreId());
					}else{
						storeBusinessService.updMemberCouponTotal(1,1,shopMemberCoupon.getMoney(),shopMemberCoupon.getStoreId());
					}
					
					result.put("result", "核销成功");
					result.put("text", "");
				}else{//其他异常
					result.put("result", "核销失败");
					result.put("text", "");
				}
			}
		}
		return result;
	}
}
