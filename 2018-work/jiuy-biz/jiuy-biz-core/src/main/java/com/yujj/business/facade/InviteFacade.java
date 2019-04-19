package com.yujj.business.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.constant.coupon.RangeType;
import com.jiuyuan.entity.InviteGiftShareLog;
import com.jiuyuan.entity.InvitedUserActionLog;
import com.jiuyuan.entity.UserInviteRewardLog;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.util.DateUtil;
import com.yujj.business.service.CouponTemplateService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.InvitedUserActionLogService;
import com.yujj.business.service.UserInviteRewardLogService;
import com.yujj.business.service.UserService;
import com.yujj.dao.mapper.InviteGiftShareLogMapper;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.order.CouponTemplate;
import com.yujj.exception.ParameterErrorException;

@Service
public class InviteFacade {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GlobalSettingService globalSettingService;

	@Autowired
	private CouponTemplateService couponTemplateService;
	
	@Autowired
	private UserInviteRewardLogService userInviteRewardLogService;
	
	@Autowired
	private InvitedUserActionLogService invitedUserActionLogService;
	
	@Autowired
	private InviteGiftShareLogMapper inviteGiftShareLogMapper;
	
	public Map<String, Object> remainInvite(long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = userService.getUser(userId);

		map.put("user", user);
//		int inivteCount = user.getInviteCount();
		
		//获取邀请有礼时间范围
		long start_time = 0;
		long end_time = 0;
		try {

	        JSONObject inviteGift_setting = globalSettingService.getJsonObject(GlobalSettingName.INVITE_GIFT_SETTING);
	        start_time = DateUtil.parseStrTime2Long(inviteGift_setting.getString("start_time"));
	        end_time = DateUtil.parseStrTime2Long(inviteGift_setting.getString("end_time"));

			map.put("inviteGift_setting", inviteGift_setting);
	        
		} catch (Exception e) {
			return map;
		}

		
		
		int inivteCount = invitedUserActionLogService.getInvitedUserCount(user.getUserId(), start_time, end_time);
		long week_start_time = DateUtil.getWeekStart().getTime();
		
		if (start_time > week_start_time) week_start_time = start_time;
		
        int weekInviteCount = invitedUserActionLogService.getInvitedUserCount(user.getUserId(), week_start_time, end_time);		
	
		
		JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.INVITE_GIFT_1);
		Integer proximalRemain = null;
		int maxInviteRule = 0;
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject)object;
			Integer invited_count = Integer.parseInt(jsonObject.get("invited_count").toString());
			int remain = invited_count - inivteCount;
			if (proximalRemain == null && remain > 0) {
				proximalRemain = remain;
			} else if(remain > 0 && proximalRemain > remain) {
				proximalRemain = remain;
			}
			
			if (maxInviteRule < invited_count) {
				maxInviteRule = invited_count;
			}
		}
		
		int inviteCountRule = 0;
		if (proximalRemain == null) {
			inviteCountRule = maxInviteRule;
			map.put("is_full", "YES");
		} else {
			inviteCountRule = proximalRemain + inivteCount;
			map.put("is_full", "NO");
		}
		
		List<Map<String, Object>> counpons = new ArrayList<Map<String, Object>>();
		map.put("coupons", counpons);
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject)object;
			Integer invited_count = Integer.parseInt(jsonObject.get("invited_count").toString());
			Integer coupon_count = Integer.parseInt(jsonObject.get("coupon_count").toString());
			Long coupon_template_id = Long.parseLong(jsonObject.get("coupon_template_id").toString());
			if (inviteCountRule == invited_count) {
				Map<String, Object> rule = new HashMap<String, Object>();
				
				map.put("invite_count_rule", invited_count);
				map.put("invite_count_actual", inivteCount);
				rule.put("coupon_count", coupon_count);
				rule.put("coupon_template_id", coupon_template_id);
				CouponTemplate couponTemplate = couponTemplateService.search(coupon_template_id);
				if (couponTemplate == null) {
					throw new ParameterErrorException("代金券模板id参数有误!" + coupon_template_id);
				}
				
				String description = couponTemplate.getName() + couponTemplate.getMoney() + "元" + RangeType.getByValue(couponTemplate.getRangeType()).getDescription() + "代金券";
				rule.put("coupon_template_info", description);
				counpons.add(rule);
				
			}
		}
	
		int invite_1_count = assembleInviteGift_1(jsonArray, map);
		int invite_2_count = assembleInviteGift_2(map, invite_1_count, userId, start_time);
		assembleInviteGift_3(map, invite_2_count, weekInviteCount);
		
		return map;
	}

	private void assembleInviteGift_3(Map<String, Object> map, int ruleCount, int weekInviteCount) {
		List<JSONObject> jsonObjects = new ArrayList<>();
		JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.INVITE_GIFT_3);
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject)object;
			Integer every_week_invited = Integer.parseInt(jsonObject.get("every_week_invited").toString());
			Long coupon_template_id = Long.parseLong(jsonObject.get("coupon_template_id").toString());
			Integer coupon_count = Integer.parseInt(jsonObject.get("coupon_count").toString());
			
			CouponTemplate couponTemplate = couponTemplateService.search(coupon_template_id);
			if (couponTemplate == null) {
				throw new ParameterErrorException("代金券模板id参数有误!" + coupon_template_id);
			}
			
			String couponTemplateInfo = couponTemplate.getName() + RangeType.getByValue(couponTemplate.getRangeType()).getDescription() + "代金券" + couponTemplate.getMoney() + "元" ;
			jsonObject.put("coupon_template_info", couponTemplateInfo);
			
			String ruleDescription = ruleCount + ".每周邀请满" + every_week_invited + "人送" + couponTemplate.getMoney() + "元现金券" + coupon_count +"张";
			jsonObject.put("ruleDescription", ruleDescription);
			jsonObject.put("weekInviteCount", weekInviteCount);

			jsonObjects.add(jsonObject);
			
			
			ruleCount ++;
		}
		map.put("inviteGift_3",	jsonObjects);
	}

	private int assembleInviteGift_2(Map<String, Object> map, int ruleCount, long userId, long start_time) {
		List<JSONObject> jsonObjects = new ArrayList<>();
		JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.INVITE_GIFT_2);
		
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject)object;
			Integer expired_days = Integer.parseInt(jsonObject.get("expired_days").toString());
			Integer every_order_count = Integer.parseInt(jsonObject.get("every_order_count").toString());
			Long coupon_template_id = Long.parseLong(jsonObject.get("coupon_template_id").toString());
			Integer coupon_count = Integer.parseInt(jsonObject.get("coupon_count").toString());
			Integer week_limit_time = Integer.parseInt(jsonObject.get("week_limit_time").toString());
			
			CouponTemplate couponTemplate = couponTemplateService.search(coupon_template_id);
			if (couponTemplate == null) {
				throw new ParameterErrorException("代金券模板id参数有误!" + coupon_template_id);
			}
			
			String couponTemplateInfo = couponTemplate.getName() + RangeType.getByValue(couponTemplate.getRangeType()).getDescription() + "代金券" + couponTemplate.getMoney() + "元" ;
			jsonObject.put("coupon_template_info", couponTemplateInfo);
			
			String ruleDescription = ruleCount + ".被邀请人在注册后的" + expired_days + "天内，每" + every_order_count + "个成功订单送代金券" + coupon_count +"张"
					+ "，每周上限" + week_limit_time + "次";
			jsonObject.put("ruleDescription", ruleDescription);
			
			long expiredTime = expired_days * DateUtils.MILLIS_PER_DAY;	
			int orderCount = invitedUserActionLogService.getNewInvitedOrderCount(userId, 1, start_time, expiredTime);
			
			jsonObject.put("order_count", orderCount);
			jsonObjects.add(jsonObject);
			
			ruleCount ++;
		}
		map.put("inviteGift_2",	jsonObjects);
		return ruleCount;
	}

	private int assembleInviteGift_1(JSONArray jsonArray, Map<String, Object> map) {
		List<JSONObject> jsonObjects = new ArrayList<>();
		int ruleCount = 1;
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject)object;
			Integer invited_count = Integer.parseInt(jsonObject.get("invited_count").toString());
			Integer coupon_count = Integer.parseInt(jsonObject.get("coupon_count").toString());
			Long coupon_template_id = Long.parseLong(jsonObject.get("coupon_template_id").toString());
			
			CouponTemplate couponTemplate = couponTemplateService.search(coupon_template_id);
			if (couponTemplate == null) {
				throw new ParameterErrorException("代金券模板id参数有误!" + coupon_template_id);
			}
			
			String couponTemplateInfo = couponTemplate.getName() + RangeType.getByValue(couponTemplate.getRangeType()).getDescription() + "代金券" + couponTemplate.getMoney() + "元" ;
			jsonObject.put("coupon_template_info", couponTemplateInfo);
			
			String ruleDescription = ruleCount + ".邀请满" + invited_count + "人送" + couponTemplate.getMoney() + "元现金券" + coupon_count +"张";
			jsonObject.put("ruleDescription", ruleDescription);
			
			jsonObjects.add(jsonObject);
			
			ruleCount ++;
		}
		map.put("inviteGift_1",	jsonObjects);
		
		return ruleCount;
	}

	public List<String> search(PageQuery pageQuery, long userId) {
		List<UserInviteRewardLog> userInviteRewards = userInviteRewardLogService.search(pageQuery, userId);
		List<String> descriptions = new ArrayList<String>();
		for (UserInviteRewardLog userInviteRewardLog : userInviteRewards) {
			long couponTemlateId = userInviteRewardLog.getCouponTemplateId();
			CouponTemplate couponTemplate = couponTemplateService.search(couponTemlateId);
			
			int count = userInviteRewardLog.getCount();
			descriptions.add(couponTemplate.getName() + couponTemplate.getMoney() + "元" 
					+ RangeType.getByValue(couponTemplate.getRangeType()).getDescription() + count + "张");
		}
		return descriptions;
	}

	public StringBuilder loadInviteRules() {
		StringBuilder builder = new StringBuilder();
		JSONArray jsonArray_1 = globalSettingService.getJsonArray(GlobalSettingName.INVITE_GIFT_1);
		JSONArray jsonArray_2 = globalSettingService.getJsonArray(GlobalSettingName.INVITE_GIFT_2);
		JSONArray jsonArray_3 = globalSettingService.getJsonArray(GlobalSettingName.INVITE_GIFT_3);
		
		if (jsonArray_1.size() < 1 && jsonArray_2.size() < 1 && jsonArray_3.size() < 1) {
			return builder.append("暂无邀请规则");
		}
		
		builder.append("邀请用户注册,");
		for (Object object : jsonArray_1) {
			JSONObject jsonObject = (JSONObject) object;
			long templateId = Long.parseLong(jsonObject.get("coupon_template_id").toString());
			CouponTemplate couponTemplate = couponTemplateService.search(templateId);
			int coupon_count = Integer.parseInt(jsonObject.get("coupon_count").toString());
			int invited_count = Integer.parseInt(jsonObject.get("invited_count").toString());
			
			builder.append("邀请者邀请" + invited_count + "人送" + couponTemplate.getMoney() + "元代金券" + coupon_count + "张。");
		}
		for (Object object : jsonArray_2) {
			JSONObject jsonObject = (JSONObject) object;
			long templateId = Long.parseLong(jsonObject.get("coupon_template_id").toString());
			CouponTemplate couponTemplate = couponTemplateService.search(templateId);
			int coupon_count = Integer.parseInt(jsonObject.get("coupon_count").toString());
			int expired_days = Integer.parseInt(jsonObject.get("expired_days").toString());
			int every_order_count = Integer.parseInt(jsonObject.get("every_order_count").toString());
			int week_limit_time = Integer.parseInt(jsonObject.get("week_limit_time").toString());
			
			builder.append("被邀请人在" + expired_days + "天内在受邀注册期在俞姐姐下单，每" + every_order_count 
					+ "个订单再送邀请人" + coupon_count + "张" + couponTemplate.getMoney() + "元的现金券，每周上限" + week_limit_time + "次。");
		}
		
		for (Object object : jsonArray_3) {
			JSONObject jsonObject = (JSONObject) object;
			long templateId = Long.parseLong(jsonObject.get("coupon_template_id").toString());
			CouponTemplate couponTemplate = couponTemplateService.search(templateId);
			int coupon_count = Integer.parseInt(jsonObject.get("coupon_count").toString());
			int every_week_invited = Integer.parseInt(jsonObject.get("every_week_invited").toString());
			
			builder.append("邀请者每周邀请" + every_week_invited + "人送" + couponTemplate.getMoney() + "元代金券" + coupon_count + "张。");
		}
 		
		return builder;
	}

	public List<Map<String, Object>> loadLogs(PageQuery pageQuery, long userId) {
		List<InvitedUserActionLog> invitedUserActionLogs = invitedUserActionLogService.search(pageQuery, userId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (InvitedUserActionLog invitedUserActionLog : invitedUserActionLogs) {
			Map<String, Object> map = new HashMap<String, Object>();
			User user = userService.getUser(invitedUserActionLog.getUserId());
			if (user.getUserType() == UserType.PHONE) {
				String phone = user.getUserRelatedName();
				map.put("userInfo", "手机尾号为" + phone.substring(phone.length()-4, phone.length()) + "的用户");
			} else if (user.getUserType() == UserType.WEIXIN) {
				String nickName = user.getUserNickname();
				map.put("userInfo", "微信名为" + nickName + "的用户");
			}
			
			if (invitedUserActionLog.getAction() == 0) {
				map.put("action", "成功注册");
			} else if (invitedUserActionLog.getAction() == 1) {
				map.put("action", "成功下单");
			}
			long time = invitedUserActionLog.getCreateTime();
			map.put("createTime", DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
			list.add(map);
		}
		
		return list;
	}

	public List<Map<String, Object>> loadRewards(PageQuery pageQuery, long userId) {
		List<UserInviteRewardLog> userInviteRewardLogs = userInviteRewardLogService.search(pageQuery, userId);
		
		Set<Long> couponTemplateIds = new HashSet<>();
		for (UserInviteRewardLog userInviteRewardLog : userInviteRewardLogs) {
			couponTemplateIds.add(userInviteRewardLog.getCouponTemplateId());
		}
		
		Map<Long, CouponTemplate> couponTemplateMap = couponTemplateService.searchMap(couponTemplateIds);
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (UserInviteRewardLog userInviteRewardLog : userInviteRewardLogs) {
			Map<String, Object> map = new HashMap<String, Object>();
			CouponTemplate couponTemplate = couponTemplateMap.get(userInviteRewardLog.getCouponTemplateId());
			map.put("coupon", couponTemplate.description() + userInviteRewardLog.getCount() + "张");
			map.put("createTime", DateUtil.format(userInviteRewardLog.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			list.add(map);
		}
		
		return list;
	}

	public void shareStatistics(int type, UserDetail userDetail) {
		InviteGiftShareLog inviteGiftShareLog = new InviteGiftShareLog();
		inviteGiftShareLog.setCreateTime(System.currentTimeMillis());
		inviteGiftShareLog.setType(type);
		inviteGiftShareLog.setCount(1);
		inviteGiftShareLog.setUserId(userDetail.getUserId());
		
		inviteGiftShareLogMapper.add(inviteGiftShareLog);
	}
	
	
}
