package com.yujj.business.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.FavoriteType;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.entity.MemberStoreRelation;
import com.jiuyuan.entity.account.UserMember;
import com.jiuyuan.entity.favorite.UserFavorite;
import com.jiuyuan.util.CollectionUtil;
import com.yujj.dao.mapper.MemberStoreRelationMapper;
import com.yujj.dao.mapper.StoreBusinessMapper;
import com.yujj.dao.mapper.UserFavoriteMapper;
import com.yujj.dao.mapper.UserMemberMapper;

/**
 * @author jeff.zhan
 * @version 2016年10月26日 下午4:25:45
 * 
 */
@Service
public class UserMemberService {
	
	@Autowired
	private UserMemberMapper userMemberMapper;
	
	@Autowired
	private MemberStoreRelationMapper memberStoreRelationMapper;
	
	@Autowired
	private UserFavoriteMapper userFavoriteMapper;
	
	@Autowired
	private StoreBusinessMapper storeBusinessMapper;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private StoreCouponService storeCouponService;
	
    @Autowired
    private OrderCouponService orderCouponService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserMemberService.class);

	@Transactional(rollbackFor = Exception.class)
	public void add(UserMember userMember, long currentTime) {
		
		UserMember um = userMemberMapper.getByUserId(userMember.getUserId());
		if (um == null || um.getBelongStoreId() == 0) {		//非门店用户 绑定时 送B端用户（门店） 邀请会员优惠券
			if (um == null) {
				userMemberMapper.add(userMember);
			} else if (um.getBelongStoreId() == 0) {
				userMemberMapper.changeBelongStoreId(um.getId(), userMember.getBelongStoreName(), userMember.getBelongStoreId(), currentTime);
			}
			MemberStoreRelation memberStoreRelation = new MemberStoreRelation();
			memberStoreRelation.setType(0);
			memberStoreRelation.setStatus(0);
			memberStoreRelation.setMemberUserId(userMember.getUserId());
			memberStoreRelation.setCreateTime(currentTime);
			memberStoreRelation.setBusinessId(userMember.getBelongStoreId());
			memberStoreRelationMapper.add(memberStoreRelation);
			storeBusinessMapper.addMemberNumber(userMember.getBelongStoreId());
			
			sendStoreCoupon(userMember.getBelongStoreId(),2);
		}
		
		UserFavorite userFavorite = new UserFavorite();
		userFavorite.setCreateTime(currentTime);
		userFavorite.setRelatedId(userMember.getBelongStoreId());
		userFavorite.setStatus(0);
		userFavorite.setType(FavoriteType.STORE_BUSINESS.getIntValue());
		userFavorite.setUpdateTime(userMember.getCreateTime());
		userFavorite.setUserId(userMember.getUserId());
		
		//判断以前是否已经关注过门店
		List<UserFavorite> userFavoriteStores = userFavoriteMapper.getUserFavorite(CollectionUtil.createList(userMember.getBelongStoreId()), FavoriteType.STORE_BUSINESS.getIntValue(), userMember.getUserId());
				
		int i = userFavoriteMapper.addFavorite(userFavorite);
		System.out.println("===============================插入关注"+i);
		//从前未关注且插入成功    用户 关注 送 C端（会员） 关注门店优惠券
		if(userFavoriteStores!=null && userFavoriteStores.size()==0 && i==1){
			sendUserCoupon(userMember.getUserId(),2);
		}
		
	}
	
	/**
	 * 
	 * @param businessId
	 * @param typeInt		1.新注册用户		2.邀请会员绑定
	 */
	public void sendStoreCoupon(long businessId,int typeInt) {
		 
		String propertyName = "";
		try {
			JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.STORE_COUPON_SEND_SETTING);
			JSONArray jsonArray = jsonObject.getJSONArray("setting");
			propertyName = "";
			for (Object object2 : jsonArray) {
				JSONObject jObject = (JSONObject)object2;
				Integer type = jObject.getInteger("type");
				if(type==typeInt){		//type=2为邀请用户相关配置
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
						(templateId, count, businessId, typeInt==1?CouponGetWay.REGISTER:CouponGetWay.STORE_INVITE_USER, true);
					
				}
			} catch (Exception e) {
				logger.error("发放代金券失败!   businessId:"+businessId);
			}
		}
	}
	
	/**
	 * 向用户赠送优惠券
	 * @param userId
	 * @param typeInt	1.新注册用户		2.关注门店
	 */
	public void sendUserCoupon(long userId,int typeInt){
		String propertyName = "";
		try {
			JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.YJJ_COUPON_SEND_SETTING);
			JSONArray jsonArray = jsonObject.getJSONArray("setting");
			propertyName = "";
			for (Object object2 : jsonArray) {
				JSONObject jObject = (JSONObject)object2;
				Integer type = jObject.getInteger("type");
				if(type==typeInt){		//type=2为关注门店相关配置
					propertyName = jObject.getString("propertyName");
					break;
				}
			}
		} catch (Exception e) {
			logger.error("获取发放代金券全局propertyName错误");
		}
		
		JSONObject jsonObjectCoupons = null;
		try{
			GlobalSettingName globalSettingName = GlobalSettingName.getByStringValue(propertyName);
			jsonObjectCoupons = globalSettingService.getJsonObject(globalSettingName);
		}catch(Exception e){
			logger.error("发放代金券失败!   后台数据库用户优惠券设置字段不存在");
		}
		
		if(jsonObjectCoupons!=null){
			try {
				JSONArray jsonArrayCoupon = jsonObjectCoupons.getJSONArray("setting");
				for (Object object : jsonArrayCoupon) {
					JSONObject jObject = (JSONObject)object;
					Integer count = jObject.getInteger("coupon_count");
					Long templateId = jObject.getLong("coupon_template_id");
					orderCouponService.getCoupon(templateId, count, userId, typeInt==1?CouponGetWay.REGISTER:CouponGetWay.FAVORITE_STORE, true);
				}
			} catch (Exception e) {
				logger.error("发放代金券失败!   userId:"+userId);
			}
		}
		
	}
}
