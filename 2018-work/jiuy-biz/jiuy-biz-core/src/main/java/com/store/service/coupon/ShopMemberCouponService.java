package com.store.service.coupon;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.util.SmallPage;
import com.store.dao.mapper.coupon.ShopMemberCouponMapper;
import com.store.entity.coupon.ShopMemberCoupon;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 商家优惠券模板
 *
 * @author 赵兴林
 * @since 2017-06-21
 */
@Service
public class ShopMemberCouponService {
	private static final Log logger = LogFactory.get();
	 
	@Autowired
	private ShopMemberCouponMapper shopMemberCouponMapper;
	
	/**
	 * 获取会员可用优惠券数量
	 * @param memberId
	 * @param storeId
	 * @return
	 */
	public int getUsableCouponCount(long memberId, long storeId) {
		List<Map<String,String>> usableCouponList = getEncapsulateData(1,storeId,memberId,null);
		return usableCouponList.size();
	}

	/**
	 * 封装会员优惠券列表
	 * @param isValidityEnd  状态:’0：历史，1：可用
	 * @param storeId
	 * @param memberId
	 * @param page 
	 * @return
	 */
	public SmallPage getMemberCouponList(int isValidityEnd, long storeId, long memberId, Page<ShopMemberCoupon> page) {
		List<Map<String,String>> memberCouponList = getEncapsulateData(isValidityEnd,storeId,memberId,page);
		SmallPage smallPage = new SmallPage(page);
		smallPage.setRecords(memberCouponList);
		return smallPage;
	}
	
	/**
	 * 获取会员优惠券列表
	 * @param isValidityEnd
	 * @param storeId
	 * @param memberId
	 * @param page
	 * @return
	 */
	private List<Map<String,String>> getEncapsulateData(int isValidityEnd, long storeId, long memberId, Page<ShopMemberCoupon> page){
		List<ShopMemberCoupon> shopMemberCouponList = getMemberCouponListValue(isValidityEnd,storeId,memberId,page);
		List<Map<String,String>> memberCouponList = new ArrayList<Map<String,String>>();
		for (ShopMemberCoupon shopMemberCoupon : shopMemberCouponList) {
			if(shopMemberCoupon.isOverdue()){
				continue;
			}
//			logger.info("获取会员优惠券列表并封装id："+shopMemberCoupon.getId());
			Map<String,String> memberCoupon = new HashMap<String,String>();
			memberCoupon.put("id", shopMemberCoupon.getId()+"");
			memberCoupon.put("money", shopMemberCoupon.getStringMoney());
			Double limitMoney = shopMemberCoupon.getLimitMoney();
			if(limitMoney==null || limitMoney==0.0){
				memberCoupon.put("limitText", "无金额限制");
			}else{
				memberCoupon.put("limitText", "满"+shopMemberCoupon.getStringLimitMoney()+"元可用");
			}
			memberCoupon.put("name", shopMemberCoupon.getName());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			memberCoupon.put("validityStartTime", sdf.format(new Date(shopMemberCoupon.getValidityStartTime())));
			memberCoupon.put("validityEndTime", sdf.format(new Date(shopMemberCoupon.getValidityEndTime())));
			if(isValidityEnd==1){
				memberCoupon.put("status", "");
			}else if(shopMemberCoupon.getStatus()==ShopMemberCoupon.status_used){
				memberCoupon.put("status", "已使用");
			}else{
				memberCoupon.put("status", "已过期");
			}
			memberCouponList.add(memberCoupon);
		}
		return memberCouponList;
	}
	
	/**
	 * 查询到会员用户列表
	 * @param isValidityEnd
	 * @param storeId
	 * @param memberId
	 * @param page 
	 * @return
	 */
	private List<ShopMemberCoupon> getMemberCouponListValue(int isValidityEnd, long storeId, long memberId, Page<ShopMemberCoupon> page){
		Wrapper<ShopMemberCoupon> wrapper = null;
		if(isValidityEnd==1){//可用优惠券
			wrapper = new EntityWrapper<ShopMemberCoupon>().eq("status", 0)
					.eq("member_id", memberId).eq("store_id", storeId).ge("validity_end_time", getcurrentTimeMillis()).orderBy("validity_end_time", true);
		}else{//历史优惠券
			wrapper = new EntityWrapper<ShopMemberCoupon>()
					.eq("member_id", memberId).eq("store_id", storeId).andNew("status="+ShopMemberCoupon.status_used+" or validity_end_time<"+getcurrentTimeMillis()).orderBy("update_time", false);
		}
		if(page!=null){
			return shopMemberCouponMapper.selectPage(page, wrapper);
		}else{
			return shopMemberCouponMapper.selectList(wrapper);
		}
	}
	
	/**
	 * 获取今天零时零分的毫秒值
	 * @return
	 */
	private long getcurrentTimeMillis(){
		Calendar calendar = Calendar.getInstance();  
		//将小时至0  
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		//将分钟至0  
		calendar.set(Calendar.MINUTE, 0);  
		//将秒至0  
		calendar.set(Calendar.SECOND,0);  
		//将毫秒至0  
		calendar.set(Calendar.MILLISECOND, 0); 
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取优惠券信息
	 * @param id
	 * @return
	 */
	public ShopMemberCoupon getMemberCouponInfo(long id) {
		ShopMemberCoupon shopMemberCoupon = shopMemberCouponMapper.selectById(id);
		return shopMemberCoupon;
	}

	/**
	 * 改变优惠券状态
	 * @param id
	 * @return
	 */
	public boolean updMemberCoupon(ShopMemberCoupon shopMemberCoupon) {
//		long time = System.currentTimeMillis();
//		shopMemberCoupon.setStatus(ShopMemberCoupon.status_used);
//		shopMemberCoupon.setAdminId(1L);
//		shopMemberCoupon.setCheckMoney(shopMemberCoupon.getMoney());
//		shopMemberCoupon.setCheckTime(time);
//		shopMemberCoupon.setUpdateTime(time);
		int count = shopMemberCouponMapper.updateById(shopMemberCoupon);
		if(count==1){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 获取核销记录
	 * @param storeId
	 * @param page 
	 */
	public Page<ShopMemberCoupon> getMemberCouponUsedList(long storeId, Page<ShopMemberCoupon> page) {
		Wrapper<ShopMemberCoupon> wrapper = new EntityWrapper<ShopMemberCoupon>().eq("status", ShopMemberCoupon.status_used)
				.eq("store_id", storeId).gt("check_time", 0).and(" coupon_template_id in (select id from shop_coupon_template where store_id="+storeId+")")
				.orderBy("check_time", false);
		List<ShopMemberCoupon> shopMemberCouponList = shopMemberCouponMapper.selectPage(page, wrapper);
 		logger.info("获取核销记录shopMemberCouponList:"+shopMemberCouponList);
 		Page<ShopMemberCoupon> shopMemberCouponPage = new Page<ShopMemberCoupon>();
 		shopMemberCouponPage.setRecords(shopMemberCouponList);
		return shopMemberCouponPage;
	}

	/**
	 * 获取同一门店同一用户核销优惠券的次数
	 * @param shopMemberCoupon
	 * @return
	 */
	public int getMemberCouponTotalByMemberId(ShopMemberCoupon shopMemberCoupon) {
		Wrapper<ShopMemberCoupon> wrapper = new EntityWrapper<ShopMemberCoupon>().eq("status", ShopMemberCoupon.status_used)
				.eq("store_id", shopMemberCoupon.getStoreId()).eq("member_id", shopMemberCoupon.getMemberId()).gt("check_time", 0);
		int count = shopMemberCouponMapper.selectCount(wrapper);
		return count;
	}

	/**
	 * 获取可用优惠券的数量
	 * @param isValidityEnd
	 * @param storeId
	 * @param memberId
	 * @return
	 */
	public int getMemberCouponListSize(int isValidityEnd, long storeId, long memberId) {
		Wrapper<ShopMemberCoupon> wrapper = new EntityWrapper<ShopMemberCoupon>().eq("status", 0)
				.eq("member_id", memberId).eq("store_id", storeId).ge("validity_end_time", getcurrentTimeMillis()).orderBy("validity_end_time", true);
		return shopMemberCouponMapper.selectCount(wrapper);
	}

	/**
	 * 获取订单可用优惠券数量
	 * @param storeId
	 * @param memberId
	 * @param allProductPrice
	 * @return
	 */
	public int getUsableShopMemberCouponListCount(long storeId, long memberId, double allProductPrice) {
		Wrapper<ShopMemberCoupon> wrapper = new EntityWrapper<ShopMemberCoupon>().eq("status", ShopMemberCoupon.status_normal)
				.eq("store_id", storeId).eq("member_id", memberId).gt("validity_end_time", getZeroTime()).le("limit_money", allProductPrice);
//		int count = shopMemberCouponMapper.selectCount(wrapper);
		List<ShopMemberCoupon> shopMemberCouponList = shopMemberCouponMapper.selectList(wrapper);
		return shopMemberCouponList.size();
	}
	
	/**
	 * 判断是否已过期
	 * @return
	 */
	public long getZeroTime(){
		Calendar calendar = Calendar.getInstance();  
		//将小时至0  
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		//将分钟至0  
		calendar.set(Calendar.MINUTE, 0);  
		//将秒至0  
		calendar.set(Calendar.SECOND,0);  
		//将毫秒至0  
		calendar.set(Calendar.MILLISECOND, 0); 
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取订单可用优惠券列表
	 * @param totalExpressAndMoney
	 * @param storeId
	 * @param memberId
	 * @param page
	 * @return
	 */
	public SmallPage getAvailableMemberCouponList(double totalExpressAndMoney, long storeId, long memberId,
			Page<ShopMemberCoupon> page) {
		Wrapper<ShopMemberCoupon> wrapper = new EntityWrapper<ShopMemberCoupon>().eq("status", ShopMemberCoupon.status_normal)
				.eq("store_id", storeId).eq("member_id", memberId).gt("validity_end_time", getZeroTime()).le("limit_money", totalExpressAndMoney)
				.orderBy("money",true);
		List<ShopMemberCoupon> shopMemberCouponList = shopMemberCouponMapper.selectPage(page, wrapper);
		List<Map<String,String>> memberCouponList = new ArrayList<Map<String,String>>();
		for (ShopMemberCoupon shopMemberCoupon : shopMemberCouponList) {
			if(shopMemberCoupon.isOverdue()){
				continue;
			}
			logger.info("获取订单可用优惠券列表并封装id："+shopMemberCoupon.getId());
			Map<String,String> memberCoupon = new HashMap<String,String>();
			memberCoupon.put("id", shopMemberCoupon.getId()+"");
			memberCoupon.put("money", shopMemberCoupon.getStringMoney());
			Double limitMoney = shopMemberCoupon.getLimitMoney();
			if(limitMoney==null || limitMoney==0.0){
				memberCoupon.put("limitText", "无金额限制");
			}else{
				memberCoupon.put("limitText", "满"+shopMemberCoupon.getStringLimitMoney()+"元可用");
			}
			memberCoupon.put("name", shopMemberCoupon.getName());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			memberCoupon.put("validityStartTime", sdf.format(new Date(shopMemberCoupon.getValidityStartTime())));
			memberCoupon.put("validityEndTime", sdf.format(new Date(shopMemberCoupon.getValidityEndTime())));
			memberCouponList.add(memberCoupon);
		}
		SmallPage smallPage = new SmallPage(page);
		smallPage.setTotal(memberCouponList.size());
		smallPage.setRecords(memberCouponList);
		return smallPage;
	}
}