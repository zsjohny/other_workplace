package com.store.service.coupon;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.util.DateUtil;
import com.store.dao.mapper.coupon.ShopCouponTemplateLogMapper;
import com.store.dao.mapper.coupon.ShopCouponTemplateMapper;
import com.store.dao.mapper.coupon.ShopMemberCouponMapper;
import com.store.entity.coupon.ShopCouponTemplate;
import com.store.entity.coupon.ShopCouponTemplateLog;
import com.store.entity.coupon.ShopMemberCoupon;
import com.store.entity.member.ShopMember;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 商家优惠券模板
 *
 * @author 赵兴林
 * @since 2017-06-21
 */
@Service
public class ShopCouponTemplateService {
	 private static final Log logger = LogFactory.get();
	 
	 @Autowired
	 private ShopCouponTemplateMapper shopCouponTemplateMapper;
	 
	 @Autowired
	 private ShopCouponTemplateLogMapper shopCouponTemplateLogMapper;
	 
	 @Autowired
	 private ShopMemberCouponMapper shopMemberCouponMapper;
	 
	 /**
		 *  获取待领取商家优惠券模板列表
		 * @param storeid
		 * @param memberId
		 * @return
		 */
	public List<ShopCouponTemplate> getWaitGetShopCouponTemplateList(long storeId, long memberId) {
		//1、获取可用优惠券列表,数据量不大
		List<ShopCouponTemplate> canUseShopCouponTemplateList = getCanUseShopCouponTemplateList(storeId);

		//剔除已经领取的优惠券
		List<ShopCouponTemplate> list = new ArrayList<ShopCouponTemplate>();
		for(ShopCouponTemplate shopCouponTemplate : canUseShopCouponTemplateList){
			ShopMemberCoupon entity = new ShopMemberCoupon();
			entity.setCouponTemplateId(shopCouponTemplate.getId());
			entity.setMemberId(memberId);
			ShopMemberCoupon shopMemberCoupon = shopMemberCouponMapper.selectOne(entity);
			if(shopMemberCoupon == null){
				list.add(shopCouponTemplate);
				
				//格式化时间
				shopCouponTemplate.setValidityStartTimeStr(DateUtil.parseLongTime2Str2(shopCouponTemplate.getValidityStartTime()));
				shopCouponTemplate.setValidityEndTimeStr(DateUtil.parseLongTime2Str2(shopCouponTemplate.getValidityEndTime()));
			}
		}
		return list;
	}
	/**
	 * 获取可领取优惠券列表
	 * @param storeId
	 * @return
	 */
	public List<ShopCouponTemplate> getCanUseShopCouponTemplateList(long storeId) {
		return getCanUseShopCouponTemplateList(storeId,0,0);
	}
	
	/**
	 * 获取指定时间创建时间段可领取优惠券列表
	 * @param storeId
	 * @return
	 */
	public List<ShopCouponTemplate> getCanUseShopCouponTemplateList(long storeId,long startCreateTime,long endCreateTime) {
		Wrapper<ShopCouponTemplate> wrapper = new EntityWrapper<ShopCouponTemplate>();
		long currentTime = System.currentTimeMillis();
		wrapper.eq("status", ShopCouponTemplate.status_normal);//正常
//		wrapper.lt("validity_start_time", currentTime);//开始时间小于当前时间
		wrapper.gt("validity_end_time", currentTime);//结束时间大于当前时间
		wrapper.eq("store_id", storeId);
		if(startCreateTime != 0){
			wrapper.gt("create_time", startCreateTime);//开始时间小于当前时间
		}
		if(endCreateTime != 0){
			wrapper.lt("create_time", endCreateTime);//结束时间大于当前时间
		}
		wrapper.orderBy("create_time",false);
		List<ShopCouponTemplate> canUseShopCouponTemplateList = shopCouponTemplateMapper.selectList(wrapper);
		for (ShopCouponTemplate shopCouponTemplate : canUseShopCouponTemplateList) {
			shopCouponTemplate.setValidityStartTimeStr(DateUtil.parseLongTime2Str2(shopCouponTemplate.getValidityStartTime()));
			shopCouponTemplate.setValidityEndTimeStr(DateUtil.parseLongTime2Str2(shopCouponTemplate.getValidityEndTime()));
		}
		return canUseShopCouponTemplateList;
	}
	 
	 /**
	  * 获取商家优惠券模板列表
	  * 
	  * tag ：可用优惠券（1可用优惠券，0失效优惠券）
	  * @param tag
	  * @param page
	  * @param storeBusinessId
	  * @return
	  */
	public Page<ShopCouponTemplate> getCouponTemplateList(int tag, Page page, long storeId) {
		long currentTime = System.currentTimeMillis();
		
		Wrapper<ShopCouponTemplate> wrapper = new EntityWrapper<ShopCouponTemplate>();
				
		if(tag == 1){//可用
			//正常状态
			wrapper.eq("status", ShopCouponTemplate.status_normal);//正常
//			wrapper.lt("validity_start_time", currentTime);//开始时间小于当前时间
			wrapper.gt("validity_end_time", currentTime);//结束时间大于当前时间
		}else{//不可用
//			状态:-1：删除，0：正常，1：停止发行，2已领完,3已失效
			//停止发行状态或已领完状态或正常状态但不在有效期
			wrapper.eq("status", ShopCouponTemplate.status_stop);
			wrapper.or("status="+ShopCouponTemplate.status_use_up);
//			wrapper.or("(status="+ShopCouponTemplate.status_normal+" and ( validity_start_time > "+ currentTime+ " or validity_end_time < "+ currentTime+"))");
			wrapper.or("(status="+ShopCouponTemplate.status_normal+" and  validity_end_time < "+ currentTime+")");
		}
		wrapper.andNew("");
		wrapper	.eq("store_id", storeId);
		wrapper.orderBy("create_time",false);
		
		List<ShopCouponTemplate> list = shopCouponTemplateMapper.selectPage(page, wrapper);
		page.setRecords(buildTemplateList(list,currentTime));
		return page;
	}
	/**
	 * 对商家优惠券进行转换Map
	 * @param list
	 * @return
	 */
	private List<ShopCouponTemplate> buildTemplateList(List<ShopCouponTemplate> list,long currentTime) {
		for(ShopCouponTemplate shopCouponTemplate : list){
			//如果状态为正常，但不在有效期则将状态设置为失效
			int status = shopCouponTemplate.getStatus();
			if(status == ShopCouponTemplate.status_normal){
				boolean isCanGet = shopCouponTemplate.checkIsCanGet(currentTime);
				if(!isCanGet){
					shopCouponTemplate.setStatus(ShopCouponTemplate.status_lose_efficacy);
				}
			}
			long validityStartTime = shopCouponTemplate.getValidityStartTime();
			long validityEndTime = shopCouponTemplate.getValidityEndTime();
			shopCouponTemplate.setValidityStartTimeStr(DateUtil.parseLongTime2Str2(validityStartTime));
			shopCouponTemplate.setValidityEndTimeStr(DateUtil.parseLongTime2Str2(validityEndTime));
			
			
//			shopCouponTemplate.setLimitmoneyduble(shopCouponTemplate.getLimitMoney());
//			shopCouponTemplate.setLimitMoneyDuble(shopCouponTemplate.getLimitMoney());
//			shopCouponTemplate.setLimitMoneyStr(String.valueOf(shopCouponTemplate.getLimitMoney()));
			
			
			
			
			
		}
		return list;
	}

	/**
	 * 全部领取商家优惠券
	 * @param storeId
	 * @param
	 */
	public int getAllShopCouponTemplate(String shopCouponTemplateIds,long storeId, ShopMember member) {
		int getSuccessCount = 0;
		String[] shopCouponTemplateIdArr = shopCouponTemplateIds.split(",");
		for(String shopCouponTemplateId : shopCouponTemplateIdArr){
			boolean getRet = getShopCouponTemplateById(Long.parseLong(shopCouponTemplateId),storeId,member);
			if(getRet){
				getSuccessCount = getSuccessCount + 1;
				logger.info("领取优惠券成功，shopCouponTemplateId："+shopCouponTemplateId);
			}else{
				logger.info("领取优惠券失败，shopCouponTemplateId："+shopCouponTemplateId);
			}
		}
		return getSuccessCount;
	}
	
	/**
	 * 根据主键领取优惠券
	 * return 
	 */
	private boolean getShopCouponTemplateById(long shopCouponTemplateId,long storeId,ShopMember member) {
		//1、校验是否可领取
		ShopCouponTemplate shopCouponTemplate = shopCouponTemplateMapper.selectById(shopCouponTemplateId);
		boolean isCanGet = checkIsCanGet(shopCouponTemplateId, shopCouponTemplate,member.getId());
		if(!isCanGet){
			return false;
		}
		//2、添加会员优惠券记录
		ShopMemberCoupon shopMemberCoupon = new ShopMemberCoupon();
		shopMemberCoupon.setStoreId(storeId);
		shopMemberCoupon.setMemberId(member.getId());
		shopMemberCoupon.setMemberNicheng(member.getUserNickname());
		shopMemberCoupon.setAdminId(1L);
		shopMemberCoupon.setCouponTemplateId(shopCouponTemplateId);
		shopMemberCoupon.setName(shopCouponTemplate.getName());
		shopMemberCoupon.setMoney(shopCouponTemplate.getMoney());
		shopMemberCoupon.setLimitMoney(shopCouponTemplate.getLimitMoney());
		shopMemberCoupon.setValidityStartTime(shopCouponTemplate.getValidityStartTime());
		shopMemberCoupon.setValidityEndTime(shopCouponTemplate.getValidityEndTime());
		long time =  System.currentTimeMillis();
		shopMemberCoupon.setCreateTime(time);
		shopMemberCoupon.setUpdateTime(time);
		shopMemberCouponMapper.insert(shopMemberCoupon);
		//3、优惠券模板的领取数量+1
		shopCouponTemplateMapper.increaseGetCount(shopCouponTemplateId);
		//4、修改已领完状态
		ShopCouponTemplate shopCouponTemplateNew = shopCouponTemplateMapper.selectById(shopCouponTemplateId);
		int getCount = shopCouponTemplateNew.getGetCount();//领取量
		int grantCount = shopCouponTemplateNew.getGrantCount();//发放量
		if(getCount >= grantCount){//如果领取量已经等于发放量则修改优惠券状态为已领完
			ShopCouponTemplate shopCouponTemplateUpdate = new ShopCouponTemplate();
			shopCouponTemplateUpdate.setId(shopCouponTemplateId);
			shopCouponTemplateUpdate.setStatus(ShopCouponTemplate.status_use_up);
			shopCouponTemplateMapper.updateById(shopCouponTemplateUpdate);
		}
		if(getCount > grantCount){
			logger.info("出现超领情况请尽快排查问题，shopCouponTemplateId："+shopCouponTemplateId+",storeId："+storeId);
		}
		
		return true;
	}

	/**
	 * 判断该优惠券模板是否可领取
	 * @param shopCouponTemplateId
	 * @param shopCouponTemplate
	 * 0领取失败、1领取成功、2已领取、3被领完、4被删除、5被停止发放、6已过期、7优惠券模板不存在
	 * @return
	 */
	private boolean checkIsCanGet(long shopCouponTemplateId, ShopCouponTemplate shopCouponTemplate,long memberId) {
		if(shopCouponTemplate == null){
			logger.info("优惠券模板不存在,shopCouponTemplateId:"+shopCouponTemplateId);
			return false;//7优惠券模板不存在
		}
	
		Wrapper<ShopMemberCoupon> wrapper = new EntityWrapper<ShopMemberCoupon>();
		wrapper.eq("coupon_template_id", shopCouponTemplateId);
		wrapper.eq("member_id", memberId);
		List<ShopMemberCoupon> shopMemberCouponList = shopMemberCouponMapper.selectList(wrapper);
		if(shopMemberCouponList != null && shopMemberCouponList.size() > 0){
			logger.info("已领取,shopCouponTemplateId:"+shopCouponTemplateId);
			return false;//1已领取
		}
		
		int status = shopCouponTemplate.getStatus();
		if(status == ShopCouponTemplate.status_stop){
			logger.info("被停止发放,shopCouponTemplateId:"+shopCouponTemplateId);
			return false;//5被停止发放
		}
		if(status == ShopCouponTemplate.status_del){
			logger.info("被删除,shopCouponTemplateId:"+shopCouponTemplateId);
			return false;//4被删除
		}
		boolean isCanGet = shopCouponTemplate.checkIsCanGet();//是否可领取
		if(!isCanGet){
			logger.info("已过期不可领取,shopCouponTemplateId:"+shopCouponTemplateId);
			return false;//6已过期
		}
		boolean isUseUp = shopCouponTemplate.checkIsUseUp();
		if(isUseUp){
			logger.info("被领完,shopCouponTemplateId:"+shopCouponTemplateId);
			return false;//2被领完
		}
		return true;
	}

	/**
  	 * 添加商家模板列表
     * @param name 优惠券标题
     * @param money  面值 
     * @param limitMoney 限额
     * @param grantCount 发放张数
     * @param validityStartTime 有效开始时间
     * @param validityEndTime 有效结束时间
     */
	public void setCouponTemplate(String name, double money, double limitMoney, int grantCount,
			long validityStartTime, long validityEndTime,long storeId, String ip, ClientPlatform client) {
		//TODO J检测商家创建的优惠券是否超过数量

		List<ShopCouponTemplate> canUseShopCouponTemplateList = getCanUseShopCouponTemplateList(storeId);
		if(canUseShopCouponTemplateList.size() >= ShopCouponTemplate.canUseMaxCount){
			throw new RuntimeException("您目前创建的优惠券过多，需先删除部分优惠券再创建！");
		}
		if(money<=0){
			throw new RuntimeException("优惠券面值必须>0");
		}
		if(limitMoney>100000){
			throw new RuntimeException("优惠券最高限额为10万");
		}
		if(grantCount>10000){
			throw new RuntimeException("优惠券发放量最高为1万");
		}
		//添加优惠券模板
		int fillinName = 0;//是否填写标题：0未填写，1已填写
		if(StringUtils.isEmpty(name)){
			if(limitMoney == 0){
				name = getStringCheckMoney(money)+"元通用优惠券";
			}else{
				name = "满"+getStringCheckMoney(limitMoney)+"元减"+getStringCheckMoney(money)+"元优惠券";
			}
		}else{
			fillinName = 1; 
		}
		
		ShopCouponTemplate shopCouponTemplate = new ShopCouponTemplate();
		shopCouponTemplate.setStoreId(storeId);
		shopCouponTemplate.setName(name);
		shopCouponTemplate.setFillinName(fillinName);
		shopCouponTemplate.setMoney(money);
		shopCouponTemplate.setLimitMoney(limitMoney);
		shopCouponTemplate.setGrantCount(grantCount);
		shopCouponTemplate.setValidityStartTime(validityStartTime);
		shopCouponTemplate.setValidityEndTime(validityEndTime);
		shopCouponTemplate.setStatus(ShopCouponTemplate.status_normal);
		long time = System.currentTimeMillis();
		shopCouponTemplate.setCreateTime(time);
		shopCouponTemplate.setUpdateTime(time);
		
		logger.info("保存商家优惠券模板，shopCouponTemplate:"+JSONObject.toJSONString(shopCouponTemplate));
		shopCouponTemplateMapper.insert(shopCouponTemplate);
		
		//添加优惠券模板日志
		setShopCouponTemplateLog(shopCouponTemplate,ShopCouponTemplateLog.type_set,storeId,ip,client);
	}
	
	/**
	 * 如果是整型，不保留小数点后的0
	 * @param money
	 * @return
	 */
	private String getStringCheckMoney(Double money) {
		if(money.intValue()-money==0){//判断是否符合取整条件  
		    return money.intValue()+"";
		}else{  
			return String.format("%.2f", money);
		}
	}

	/**
	 * 添加优惠券模板日志
	 * @param shopCouponTemplate
	 * @param action 1:创建 2:删除 3:停止发放
	 */
	private void setShopCouponTemplateLog(ShopCouponTemplate shopCouponTemplate, int type,long storeId,String ip, ClientPlatform client) {
		ShopCouponTemplateLog shopCouponTemplateLog = new ShopCouponTemplateLog();
		shopCouponTemplateLog.setStoreId(storeId);
		shopCouponTemplateLog.setCouponTemplateId(shopCouponTemplate.getId());
		shopCouponTemplateLog.setAdminId(1L);
		shopCouponTemplateLog.setType(type);
		shopCouponTemplateLog.setContent(JSONObject.toJSONString(shopCouponTemplate));
		shopCouponTemplateLog.setIp(ip);
//		shopCouponTemplateLog.setPlatform();
		shopCouponTemplateLog.setVersion(client.getVersion());
		shopCouponTemplateLog.setNet(-1);
		long time = System.currentTimeMillis();
		shopCouponTemplateLog.setCreateTime(time);
		shopCouponTemplateLogMapper.insert(shopCouponTemplateLog);
		
	}
	
	
	/**
	 * 停止发放优惠券模板
	 * @param id
	 * @param storeId
	 * @param ip
	 * @param client
	 */
	public void updStopCouponTemplate(long id, long storeId, String ip, ClientPlatform client) {
		ShopCouponTemplate entity = shopCouponTemplateMapper.selectById(id);
		logger.info("停止发放优惠券ShopCouponTemplate:"+JSONObject.toJSONString(entity));
		if(entity==null){
			throw new RuntimeException("没有该优惠券模板,请确认");
		}
		if(entity.getStatus()==ShopCouponTemplate.status_stop){
			throw new RuntimeException("该优惠券模板已经停止发放");
		}
		entity.setStatus(ShopCouponTemplate.status_stop);
		entity.setUpdateTime(System.currentTimeMillis());
		shopCouponTemplateMapper.updateById(entity);
		
		//添加优惠券模板日志
		setShopCouponTemplateLog(entity,ShopCouponTemplateLog.type_stop,storeId,ip,client);
	}

	/**
	 * 删除优惠券模板
	 * @param id
	 * @param storeId
	 * @param ip
	 * @param client
	 */
	public void delCouponTemplate(long id, long storeId, String ip, ClientPlatform client) {
		ShopCouponTemplate entity = shopCouponTemplateMapper.selectById(id);
		logger.info("停止发放优惠券ShopCouponTemplate:"+JSONObject.toJSONString(entity));
		if(entity==null){
			throw new RuntimeException("没有该优惠券模板,请确认");
		}
//		if(entity.getStatus()!=ShopCouponTemplate.status_stop){
//			throw new RuntimeException("该优惠券模板未停止发放");
//		}
		entity.setStatus(ShopCouponTemplate.status_del);
		entity.setUpdateTime(System.currentTimeMillis());
		shopCouponTemplateMapper.updateById(entity);
		
		//添加优惠券模板日志
		setShopCouponTemplateLog(entity,ShopCouponTemplateLog.type_del,storeId,ip,client);
	}

	/**
	 * 核销优惠券后修改优惠券模板使用量
	 * @param couponTemplateId
	 * @param storeId
	 * @param ip
	 * @param client
	 */
	public void updCouponTemplateCount(long couponTemplateId, long storeId, String ip, ClientPlatform client) {
		ShopCouponTemplate entity = shopCouponTemplateMapper.selectById(couponTemplateId);
		entity.setUsedCount(entity.getUsedCount()+1);
		entity.setUpdateTime(System.currentTimeMillis());
		shopCouponTemplateMapper.updateById(entity);
	}

	/**
	 * 根据id查询
	 * @param: id
	 * @return: com.jiuyuan.constant.coupon.StoreCouponTemplate
	 * @auther: Charlie(唐静)
	 * @date: 2018/5/24 0:00
	 */
	@Deprecated
	public StoreCouponTemplate getById(long id) {
		return shopCouponTemplateMapper.selectCouponTemplateById(id);
	}

	/**
	 *  参考 {@link com.jiuy.core.dao.StoreCouponTemplateDao#update}
	 * @param: id
	 * @param: money
	 * @param: publishCount
	 * @param: grantCount
	 * @param: availableCount
	 * @return: void
	 * @auther: Charlie(唐静)
	 * @date: 2018/5/24 0:05
	 */
	@Deprecated
	public void updateSpecial(Long id, Double money, Integer publishCount, Integer grantCount, Integer availableCount) {
		 shopCouponTemplateMapper.updateSpecial(id, money, publishCount, grantCount, availableCount);
	}

}

