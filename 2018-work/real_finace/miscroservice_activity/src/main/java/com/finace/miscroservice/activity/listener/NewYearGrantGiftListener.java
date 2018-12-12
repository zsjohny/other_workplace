package com.finace.miscroservice.activity.listener;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.activity.po.UserJiangPinPO;
import com.finace.miscroservice.activity.service.UserJiangPinService;
import com.finace.miscroservice.activity.service.UserRedPacketsService;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * 2018年新年活动，邀请人投资情况送被邀请人礼物
 */
@Component
@RefreshScope
public class NewYearGrantGiftListener extends MqListenerConvert{
    private static Log logger = Log.getInstance(NewYearGrantGiftListener.class);

    @Autowired
    private UserRedPacketsService userRedPacketsService;

    @Autowired
	private UserJiangPinService userJiangPinService;

	@Autowired
	@Qualifier("userStrHashRedisTemplate")
	private ValueOperations<String, String> userStrHashRedisTemplate;

	@Value("${activity.start.time}")
	private String startTime;

	@Value("${activity.end.time}")
	private String endTime;

	@Override
	protected void transferTo(String transferData) {
		logger.info("2018年新年活动，邀请人投资情况送被邀请人礼物,接受数据={}", transferData);
		if(StringUtils.isEmpty(transferData)) {
			logger.warn("2018年新年活动，邀请人投资情况送被邀请人礼物,接受数据失败", transferData);
			return;
		}

		//活动时间 2018-02-08 到 2018-02-25
		if(!DateUtils.compareDate(startTime, DateUtils.getNowDateStr()) && DateUtils.compareDate(endTime, DateUtils.getNowDateStr())){
			JSONObject jsonData = JSONObject.parseObject(transferData);
			Double amt = Double.valueOf(jsonData.getString("buyamt")); //首投金额
			Integer userid = Integer.valueOf(jsonData.getString("userid")); //用户id
			Integer inviter = Integer.valueOf(jsonData.getString("inviter")); //邀请人id
			Integer day = Integer.valueOf(jsonData.getString("timeLimitDay")); //投资天数
			logger.info("2018年新年活动amt={},userid={},inviter={}, day={}", amt, userid, inviter, day);
			try {
				UserJiangPinPO userJiangPinPO = new UserJiangPinPO();
				userJiangPinPO.setAddTime(DateUtils.getNowDateStr());
				userJiangPinPO.setUserId(String.valueOf(inviter));
				int toType = 0;
				if( day >= 7 && amt >= 5000 ){
					toType = 1;  //送20元话费
				}else if( day >= 60 && amt >= 1000 ){
					toType = 2;  //送50元京东卡
				}

				if(toType > 0){
					if( toType == 1 ){
						logger.info("送被邀请人{},获得20元话费", inviter);
						userJiangPinPO.setRemark("20");//奖品20元话费
						userJiangPinPO.setJiangPinName("20元话费");
						userJiangPinService.addUserJiangPin(userJiangPinPO);
					}

					if( toType == 2 ){
						logger.info("送被邀请人{},获得50元京东卡", inviter);
						userJiangPinPO.setRemark("50");//奖品50元京东卡
						userJiangPinPO.setJiangPinName("50元京东卡");
						userJiangPinService.addUserJiangPin(userJiangPinPO);
					}

					String numStr = userStrHashRedisTemplate.get("inviter"+inviter) != null ? userStrHashRedisTemplate.get("inviter"+inviter) : "0";
					Integer num = Integer.valueOf(numStr) + 1;
					//被邀请人人数达到投资条件5次，送100元京东卡
					if( num >= 5 ){
						num = num - 50000;
						logger.info("送被邀请人{},获得100元京东卡,num={}", inviter, num);
						userStrHashRedisTemplate.set("inviter"+inviter, num.toString());
						userJiangPinPO.setRemark("100");//奖品100元京东卡
						userJiangPinPO.setJiangPinName("100元京东卡");
						userJiangPinService.addUserJiangPin(userJiangPinPO);
					}else{
						logger.info("被邀请人邀请好友满足活动条件次数,num={}", inviter, num);
						userStrHashRedisTemplate.set("inviter"+inviter, num.toString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("2018年新年活动异常：", e);
			}
		}
		return;
	}

}
