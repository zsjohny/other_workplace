package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.uitls.MD5Util;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.coupon.StoreCouponService;
import com.jiuy.core.service.member.BrandBusinessService;
import com.jiuy.core.service.member.StoreBusinessService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.entity.StoreRegister;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.YunXinSmsService;

@Service
public class StoreBusinessFacade {
	private static final String BUSINESS_NUMBER_PREFIX = "800";
	
	private static final int DEFAULT_NUMBER_SIZE = 6;
	@Resource
	private StoreBusinessDao storeBusinessDao;
	
	@Resource
	private YunXinSmsService yunXinSmsService;
	
	@Resource
	private BrandBusinessService brandbusinessService;
	
	@Resource
	private StoreBusinessService storeBusinessService;
		
	@Autowired
	private StoreCouponService storeCouponService;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	private static final Logger logger = LoggerFactory.getLogger(StoreBusinessFacade.class);
	
	
	@Transactional(rollbackFor = Exception.class)
	synchronized public long add(StoreBusiness storeBusiness,StoreRegister storeRegister,AdminUser userinfo,String applyMemo) {
		long currentTimeMillis = System.currentTimeMillis();
		storeBusiness.setCreateTime(currentTimeMillis);
		storeBusiness.setUpdateTime(currentTimeMillis);
		
		long superBusinessId = storeRegister.getSuperBusinessId();
		if(superBusinessId != 0){//有上级商家号
			StoreBusiness superStoreBusiness =	storeBusinessDao.searchBusinessById(superBusinessId);
			String superIds = superStoreBusiness.getSuperBusinessIds();
			if(StringUtils.isNotEmpty(superIds)){//上级门店不是1级门店
				storeBusiness.setSuperBusinessIds(superStoreBusiness.getSuperBusinessIds()+superStoreBusiness.getId()+",");
			}else {//上级门店是1级门店
				storeBusiness.setSuperBusinessIds(","+superStoreBusiness.getId()+",");
				storeBusiness.setMemberCommissionPercentage(superStoreBusiness.getDefaultCommissionPercentage());
			}
			storeBusiness.setDeep(superStoreBusiness.getDeep() + 1);
		}else{
			storeBusiness.setMemberCommissionPercentage(storeBusiness.getCommissionPercentage());
			storeBusiness.setDeep(1L);
		}
		storeBusinessDao.add(storeBusiness);
		long businessId = storeBusiness.getId();
		//生成商家号并写入数据库
		String businessNumber = generateBusinessNumber(String.valueOf(businessId));
		String password = generatePassword();
		String passwordMD5 = MD5Util.getMD5Format(password);
		storeBusinessDao.updateBusinessNumberAndUserName(businessNumber, businessNumber,passwordMD5,businessId);
		
		//默认关联所有品牌
		List<BrandLogo> brandList = brandbusinessService.getBrandList();
		ArrayList<Long> brandIds = new ArrayList<Long>();
		for(BrandLogo brandLogo : brandList){
			brandIds.add(brandLogo.getBrandId());
		}
	
		storeBusinessDao.addBrandRelationOfStoreId(String.valueOf(businessId),0,System.currentTimeMillis(), brandIds);		
			if(storeRegister != null && userinfo != null){
				long nowTime = System.currentTimeMillis();
				int statu = storeRegister.getStatus();
				
				storeRegister.setBusinessId(businessId);
				storeRegister.setStatus(2);
				storeRegister.setCreateId(userinfo.getUserId());
				storeRegister.setCreateName(userinfo.getUserName());
				storeRegister.setApplyMemo(applyMemo);
				if(statu ==0){//通过并创建
					storeRegister.setAdminId(userinfo.getUserId());
					storeRegister.setAdminName(userinfo.getUserName());
					storeRegister.setApplyTime(nowTime);
				}else if(statu == 1){//创建						
				}
				storeBusinessService.upDateStoreRegister(storeRegister);
				
				StoreRegister newStoreRegister = storeBusinessService.getStoreRegisterByPhoneNumber(storeBusiness.getPhoneNumber());
				if(newStoreRegister.getStatus() == 2){
						JSONArray params = new JSONArray();
						params.add(businessNumber);//初始账号
						params.add(password);//初始密码
				    	yunXinSmsService.sendNotice(storeBusiness.getPhoneNumber(), params, 3057416);
				}else {
					throw new ParameterErrorException("创建账号失败");
				}
			}else {
				throw new ParameterErrorException("请求参数异常");
			}
			sendCoupon(businessId);
			return businessId;
	}

	public void sendCoupon(long businessId) {
		 //新注册用户赠送优惠券
		String propertyName = "";
		try {
			JSONObject jsonObject = globalSettingService.getJsonObject(com.jiuyuan.constant.GlobalSettingName.STORE_COUPON_SEND_SETTING);
			JSONArray jsonArray = jsonObject.getJSONArray("setting");
			propertyName = "";
			for (Object object2 : jsonArray) {
				JSONObject jObject = (JSONObject)object2;
				Integer type = jObject.getInteger("type");
				if(type==1){
					propertyName = jObject.getString("propertyName");
					break;
				}
			}
		} catch (Exception e) {
			logger.error("获取发放代金券全局propertyName错误");
		}
        GlobalSettingName globalSettingName = GlobalSettingName.getByStringValue(propertyName);
		JSONObject jsonObjectCoupons = globalSettingService.getJsonObject(globalSettingName);
		if(jsonObjectCoupons!=null){
			try {
				JSONArray jsonArrayCoupon = jsonObjectCoupons.getJSONArray("setting");
				for (Object object : jsonArrayCoupon) {
					JSONObject jObject = (JSONObject)object;
					Integer count = jObject.getInteger("coupon_count");
					Long templateId = jObject.getLong("coupon_template_id");
						storeCouponService.getCoupon
						(templateId, count, businessId, CouponGetWay.REGISTER, true);
					
				}
			} catch (Exception e) {
				logger.error("发放代金券失败!   businessId:"+businessId);
			}
		}
	}
		
		//生成商家号
		private String generateBusinessNumber(String id){
			int count = DEFAULT_NUMBER_SIZE - id.length();
			StringBuffer stringBuffer = new StringBuffer();
			if(count>0){
				for(int i=0;i<count;i++){
					stringBuffer.append("0");
				}
			}
			
			return BUSINESS_NUMBER_PREFIX+stringBuffer.toString()+id;
		}
		
		//生成6位随机密码
		private String generatePassword(){
			Random random = new Random();
			String password = "";
			for(int i=0;i<6;i++){
				int nextInt = random.nextInt(10);
				password += nextInt;
			}
			return password;
		}
}
