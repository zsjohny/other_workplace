package com.yujj.business.handler.account;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.entity.Activity;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.InvitedUserActionLog;
import com.jiuyuan.entity.UserInviteRewardLog;
import com.jiuyuan.entity.account.UserInviteRecord;
import com.yujj.business.service.InvitedUserActionLogService;
import com.yujj.business.service.OrderCouponService;

import com.yujj.business.service.ActivityService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.UserInviteRewardLogService;
import com.yujj.business.service.UserInviteService;
import com.yujj.business.service.UserService;
import com.yujj.dao.mapper.UserInviteMapper;
import com.yujj.dao.mapper.UserMapper;
import com.yujj.entity.account.User;
@Service
public class GrantCoinHandler implements UserHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GrantCoinHandler.class);
	
    private static  int GRANT_COINS = 99 * 5;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private UserInviteService userInviteService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserInviteMapper userInviteMapper;
    
    @Autowired
    private GlobalSettingService globalSettingService;
    
    @Autowired
    private OrderCouponService orderCouponService;
    
    @Autowired
    private UserInviteRewardLogService userInviteRewardLogService;

    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private InvitedUserActionLogService invitedUserActionLogService;


    @Override
    public void postUserCreation(User user, String inviteCode, ClientPlatform clientPlatform) {
        long userId = user.getUserId();
        long time = System.currentTimeMillis();
        
        Activity activity = activityService.getActivity("reg2step");
    	if (activity != null ) {		
    		GRANT_COINS = activity.getGrantAmountRandom();
		}
        
        userCoinService.updateUserCoin(userId, 0, 0, "-1", time, UserCoinOperation.REGISTER_GRANT, null, clientPlatform.getVersion());

       
        
    	handleInvite(userId, inviteCode, time, clientPlatform);
    }

	@Override
	public void postUserCreation(User user, long yJJNumber, ClientPlatform clientPlatform) {
		long userId = user.getUserId();
        long time = System.currentTimeMillis();

        Activity activity = activityService.getActivity("reg2step");
    	if (activity != null ) {		
    		GRANT_COINS = activity.getGrantAmountRandom();
		}
        
    	JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
    	int registerJiuCoinSetting = jiucoin_global_setting.getInteger("registerJiuCoinSetting");
    	String version = "0";
    	if(clientPlatform != null){
    		version = clientPlatform.getVersion();
    	}
    	userCoinService.updateUserCoin(userId, 0, registerJiuCoinSetting, "-1", time, UserCoinOperation.REGISTER_GRANT, null, version);
    	
//    	if (VersionUtil.compareVersion(clientPlatform.getVersion(), "1.8.11") < 0) {
//    		userCoinService.updateUserCoin(userId, 0, GRANT_COINS, "-1", time, UserCoinOperation.REGISTER_GRANT);
//    	} else {
//    		userCoinService.updateUserCoin(userId, 0, 0, "-1", time, UserCoinOperation.REGISTER_GRANT);
//    	}
//    	System.out.println("yJJNumberyJJNumber:"+yJJNumber);
    	if(yJJNumber > 0){
    		
    		User hostUser = userMapper.getUserByYJJNumber(yJJNumber);
    		UserInviteRecord record = new UserInviteRecord();
    		record.setInvitedUserId(userId);
    		if(hostUser != null){
    			record.setUserId(hostUser.getUserId());	
    		}else{
    			record.setUserId(yJJNumber);	
    			
    		}
    		record.setStatus(0);
    		record.setCreateTime(time);
    		record.setUpdateTime(time);
    		userInviteMapper.addUserInviteRecord(record);
    	}
    	long nowTime = System.currentTimeMillis();
    	long startTime = 0;
    	long endTime = 0;
    	try {
			startTime = DateUtil.parseStrTime2Long("2017-02-20 00:00:00");
//			startTime = DateUtil.parseStrTime2Long("2017-02-20 00:00:00");
			endTime = DateUtil.parseStrTime2Long("2017-02-28 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	//活动期间需要注册并且登录才增加邀请数量
    	if(nowTime < startTime || nowTime > endTime){
    		
    		handleInvite(user, yJJNumber, time, clientPlatform);
    	}
        
	}
    
    @Override
    public void postUserCreation(User user, String inviteCode, String relatedId, ClientPlatform clientPlatform) {
        long userId = user.getUserId();
        long time = System.currentTimeMillis();
        
        Activity activity = activityService.getActivity("reg2step");
    	if (activity != null ) {	
    		GRANT_COINS = activity.getGrantAmountRandom();
		}
        userCoinService.updateUserCoin(userId, 0, 0, relatedId, time, UserCoinOperation.REGISTER_GRANT, null, clientPlatform.getVersion());

        handleInvite(userId, inviteCode, time, clientPlatform);
    }

    private void handleInvite(long userId, String inviteCode, long time, ClientPlatform clientPlatform) {
        if (StringUtils.isBlank(inviteCode)) {
            return;
        }

//        UserInvite userInvite = userInviteService.getUserInviteByCode(inviteCode);
//        if (userInvite == null) {
//            return;
//        }
        User user = userMapper.getUserByYJJNumber(Long.parseLong(inviteCode));
        if (user == null || user.getUserId() == 0) {
          return;
        }

        UserInviteRecord record = userInviteService.addUserInviteRecord(user.getUserId(), userId, time);
        String relatedId = String.valueOf(record.getId());
        
        int invitCoins = 20; 
        int invitedCoins = 50; 
        Activity activity = activityService.getActivity("invited");
    	if (activity != null ) {
    		invitedCoins = activity.getGrantAmountRandom();
		}
    	activity = activityService.getActivity("inviteOther");
    	if (activity != null ) {
    		invitCoins = activity.getGrantAmountRandom();
		}
//        userCoinService.updateUserCoin(userId, 0, invitedCoins, relatedId, time, UserCoinOperation.INVITED);
        userCoinService.updateUserCoin(userId, 0, 0, relatedId, time, UserCoinOperation.INVITED, null, clientPlatform.getVersion());
//        userCoinService.updateUserCoin(userInvite.getUserId(), 0, invitCoins, relatedId, time, UserCoinOperation.INVITE);
        userCoinService.updateUserCoin(user.getUserId(), 0, 0, relatedId, time, UserCoinOperation.INVITE, null, clientPlatform.getVersion());
    }

	public void handleInvite(User user, long yJJNumber, long time, ClientPlatform clientPlatform) {
		if (yJJNumber == -1) {
            return;
        }
		
		long userId = user.getUserId();
		long currentTime = System.currentTimeMillis();
		User invitor = null;
		invitor = userService.getUserByYJJNumber(yJJNumber);
		if (invitor == null) {
			return ;
		}


//    	userCoinService.updateUserCoin(userId, 0, invitedCoins, activity.getMemo(), time, UserCoinOperation.INVITED);
//    	userCoinService.updateUserCoin(userId, 0, 0, activity.getMemo(), time, UserCoinOperation.INVITED, null, clientPlatform.getVersion());

//        userCoinService.updateUserCoin(invitor.getUserId(), 0, invitCoins, activity.getMemo(), time, UserCoinOperation.INVITE);
//    	userCoinService.updateUserCoin(userId, 0, 0, activity.getMemo(), time, UserCoinOperation.INVITED, null, clientPlatform.getVersion());
//		int weekInviteCount = 0;
//		synchronized (this) {
//			//再次通过数据库找user，这里需要同步，主要取inviteCount数据的准确性
//			invitor = userService.getUserByYJJNumber(yJJNumber);
//			if (invitor == null) {
//				return;
//			}
//			
//			/**
//			 * 邀请统计记录
//			 */
//			long lastInviteTime = invitor.getLastInviteTime();
//			weekInviteCount = invitor.getWeekInviteCount();
//			if (lastInviteTime >= DateUtil.getWeekStart().getTime()) {
//				weekInviteCount += 1;
//			} else {
//				weekInviteCount = 1;
//			}
//			userService.updateUserInvite(invitor.getUserId(), weekInviteCount);
//		}
		
		
		InvitedUserActionLog invitedUserActionLog = new InvitedUserActionLog();
		invitedUserActionLog.setAction(0);
		invitedUserActionLog.setUserId(userId);
		invitedUserActionLog.setInvitor(invitor.getUserId());
		invitedUserActionLog.setRelatedId(user.getyJJNumber());
		invitedUserActionLog.setCreateTime(currentTime);
		
		invitedUserActionLogService.add(invitedUserActionLog);		
        

        /**
         * 满足邀请规则送玖币(积分)
         */
        JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
        JSONObject invitationSetting = jiucoin_global_setting.getJSONObject("invitationSetting");
        int invitationCycle = invitationSetting.getInteger("invitationCycle");
        int maxCountCycle = invitationSetting.getInteger("maxCountCycle");
        
        int invite_count = invitedUserActionLogService.getInvitedUserCount(invitor.getUserId(), DateUtil.getTodayEnd() - invitationCycle * DateUtils.MILLIS_PER_DAY, DateUtil.getTodayEnd());
        if (maxCountCycle >= invite_count) {
        	int eachObtain = invitationSetting.getInteger("eachObtain");
        	userCoinService.updateUserCoin(invitor.getUserId(), 0, eachObtain, UserCoinOperation.INVITE.getIntValue() + "", time, UserCoinOperation.INVITE, null, clientPlatform.getVersion());
		}
        
		
		//获取邀请有礼时间范围
		long start_time = 0;
		long end_time = 0;
		try {

	        JSONObject inviteGift_setting = globalSettingService.getJsonObject(GlobalSettingName.INVITE_GIFT_SETTING);
	        start_time = DateUtil.parseStrTime2Long(inviteGift_setting.getString("start_time"));
	        end_time = DateUtil.parseStrTime2Long(inviteGift_setting.getString("end_time"));
	        
	        //活动过期
	        if (System.currentTimeMillis() > end_time || System.currentTimeMillis() < start_time) return;
	        
		} catch (Exception e) {
			return;
		}

        int inviteCount = invitedUserActionLogService.getInvitedUserCount(invitor.getUserId(), start_time, end_time);

        /**
         * 满足邀请规则送代金券-规则1
         */
        JSONArray jsonArray_1 = globalSettingService.getJsonArray(GlobalSettingName.INVITE_GIFT_1);
        for (Object object : jsonArray_1) {
			JSONObject jsonObject = (JSONObject)object;
			int invited_count = Integer.parseInt(jsonObject.get("invited_count").toString());
			
			if (invited_count == inviteCount) {
//				int jiuCoin = Integer.parseInt(jsonObject.get("jiuCoin").toString());
				long coupon_template_id = Long.parseLong(jsonObject.get("coupon_template_id").toString());
				int coupon_count = Integer.parseInt(jsonObject.get("coupon_count").toString());
				
				try {
					orderCouponService.getCoupon(coupon_template_id, coupon_count, invitor.getUserId(), CouponGetWay.INVITE, true);
				} catch (Exception e) {
					logger.error("GrantCoinHandler：规则1代金券领取失败。" + e.getMessage());
				}
				
//				userCoinService.updateUserCoin(invitor.getUserId(), 0, jiuCoin, GlobalSettingName.INVITE_GIFT_1.getStringValue(), time, UserCoinOperation.INVITE);
				userCoinService.updateUserCoin(invitor.getUserId(), 0, 0, GlobalSettingName.INVITE_GIFT_1.getStringValue(), time, UserCoinOperation.INVITE, null, clientPlatform.getVersion());
				
		        UserInviteRewardLog userInviteRewardLog = new UserInviteRewardLog();
		        userInviteRewardLog.setCount(coupon_count);
		        userInviteRewardLog.setCouponTemplateId(coupon_template_id);
//		        userInviteRewardLog.setJiuCoin(jiuCoin);
		        userInviteRewardLog.setJiuCoin(0);
		        userInviteRewardLog.setCreateTime(currentTime);
		        userInviteRewardLog.setUserId(invitor.getUserId());
		        userInviteRewardLogService.add(userInviteRewardLog);
			}
		}
        
        
        /**
         * 满足邀请规则送代金券- 规则3
         */
        JSONArray jsonArray_3 = globalSettingService.getJsonArray(GlobalSettingName.INVITE_GIFT_3);
        for (Object object : jsonArray_3) {
			JSONObject jsonObject = (JSONObject)object;
			int every_week_invited = Integer.parseInt(jsonObject.get("every_week_invited").toString());

			long week_start_time = DateUtil.getWeekStart().getTime();
			
			if (start_time > week_start_time) week_start_time = start_time;
			
	        int weekInviteCount = invitedUserActionLogService.getInvitedUserCount(invitor.getUserId(), week_start_time, end_time);
	        
			if (weekInviteCount == every_week_invited) {
				long coupon_template_id = Long.parseLong(jsonObject.get("coupon_template_id").toString());
				int coupon_count = Integer.parseInt(jsonObject.get("coupon_count").toString());
				
				try {
					orderCouponService.getCoupon(coupon_template_id, coupon_count, invitor.getUserId(), CouponGetWay.INVITE, true);
				} catch (Exception e) {
					logger.error("GrantCoinHandler：规则3代金券领取失败。" + e.getMessage());
				}
//				userCoinService.updateUserCoin(invitor.getUserId(), 0, jiuCoin, GlobalSettingName.INVITE_GIFT_3.getStringValue(), time, UserCoinOperation.INVITE);
				userCoinService.updateUserCoin(invitor.getUserId(), 0, 0, GlobalSettingName.INVITE_GIFT_3.getStringValue(), time, UserCoinOperation.INVITE, null, clientPlatform.getVersion());
			
				UserInviteRewardLog userInviteRewardLog = new UserInviteRewardLog();
		        userInviteRewardLog.setCount(coupon_count);
		        userInviteRewardLog.setCouponTemplateId(coupon_template_id);
//		        userInviteRewardLog.setJiuCoin(jiuCoin);
		        userInviteRewardLog.setJiuCoin(0);
		        userInviteRewardLog.setCreateTime(currentTime);
		        userInviteRewardLog.setUserId(invitor.getUserId());
		        userInviteRewardLogService.add(userInviteRewardLog);
			}
		}
        
	}
	
	public static void main(String[] args) {
	}
}
