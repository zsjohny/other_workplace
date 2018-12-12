package com.finace.miscroservice.borrow.listener;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.borrow.service.FinanceBidService;
import com.finace.miscroservice.borrow.service.FuiouH5PayService;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.entity.TimerScheduler;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Component
public class OrderDelayListener extends MqListenerConvert{
    private static Log logger = Log.getInstance(OrderDelayListener.class);

    @Autowired
    private FinanceBidService financeBidService;
    
	@Override
	protected void transferTo(String transferData) {
		logger.info("订单失效任务处理开始transferData={}", transferData);
//		FuiouH5PayService.Param param = JSONObject.parseObject(transferData, FuiouH5PayService.Param.class);
		if( transferData == null ) {
			logger.warn("订单失效任务处理,解析参数为空={}", transferData);
			return;
		}

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("pay", -1);
			map.put("orderId", transferData);
			FinanceBidPO financeBidPO = financeBidService.getFidByOrderId(transferData);
			if( null != financeBidPO && financeBidPO.getPay() == 0 ){
				if( financeBidService.updatePayFinanceBidByOrderId(map) > 0 ){
					logger.warn("订单失效任务处理成功transferData={}", transferData);
				}else{
					logger.warn("订单失效任务处理失败transferData={}", transferData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("订单{}失效处理。异常信息：{}", transferData, e);
		}
	}

}
