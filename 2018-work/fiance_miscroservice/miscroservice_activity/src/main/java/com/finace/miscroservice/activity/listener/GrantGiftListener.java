package com.finace.miscroservice.activity.listener;

import com.finace.miscroservice.activity.po.UserJiangPinPO;
import com.finace.miscroservice.activity.service.UserJiangPinService;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 2018年新年活动，邀请人投资情况送被邀请人礼物
 */
@Component
@RefreshScope
public class  	GrantGiftListener extends MqListenerConvert{
    private static Log logger = Log.getInstance(GrantGiftListener.class);

    @Autowired
	private UserJiangPinService userJiangPinService;

	@Value("${activity.start.time}")
	private String startTime;

	@Value("${activity.end.time}")
	private String endTime;

	@Override
	protected void transferTo(String transferData) {
		logger.info("2018年新年活动，投资人满足条件送100元京东卡,接受数据={}", transferData);
		if(StringUtils.isEmpty(transferData)) {
			logger.warn("2018年新年活动，投资人满足条件送100元京东卡,接受数据失败", transferData);
			return;
		}
		try {
			if(!DateUtils.compareDate(startTime, DateUtils.getNowDateStr()) && DateUtils.compareDate(endTime, DateUtils.getNowDateStr())) {
				UserJiangPinPO userJiangPinPO = new UserJiangPinPO();
				userJiangPinPO.setAddTime(DateUtils.getNowDateStr());
				userJiangPinPO.setUserId(String.valueOf(transferData));
				userJiangPinPO.setRemark("100");//奖品100元京东卡
				userJiangPinPO.setJiangPinName("100元京东卡");
				userJiangPinService.addUserJiangPin(userJiangPinPO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("2018年新年活动，投资人满足条件送100元京东卡异常：", e);
		}
		return;
	}

}
