package com.finace.miscroservice.activity.listener;

import com.finace.miscroservice.activity.rpc.UserRpcService;
import com.finace.miscroservice.activity.service.UserRedPacketsService;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.enums.MsgCodeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.finace.miscroservice.commons.utils.Constant.SERVICE_PHONE;

@Component
public class NewUserGrantHbListener extends MqListenerConvert{
    private static Log logger = Log.getInstance(NewUserGrantHbListener.class);

    @Autowired
    private UserRedPacketsService userRedPacketsService;

	@Override
	protected void transferTo(String transferData) {
		logger.info("给新用户={}，发福利券开始", transferData);
		if(StringUtils.isEmpty(transferData) && "0".equals(transferData)) {
			logger.warn("给新用户={}，发红包失败", transferData);
			return;
		}
        //给新手发红包
		try {
			userRedPacketsService.grantXsFlq(Integer.valueOf(transferData));

			logger.info("给新用户={}，发福利券结束", transferData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

}
