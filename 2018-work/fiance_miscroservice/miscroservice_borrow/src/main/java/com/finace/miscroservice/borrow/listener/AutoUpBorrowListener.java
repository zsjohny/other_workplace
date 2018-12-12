package com.finace.miscroservice.borrow.listener;

import com.finace.miscroservice.borrow.service.LoanMoneyService;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自动上标
 */
@Component
public class AutoUpBorrowListener extends MqListenerConvert {
	private static Log logger = Log.getInstance(AutoUpBorrowListener.class);

	@Autowired
	private LoanMoneyService loanMoneyService;

	@Override
	protected void transferTo(String transferData) {
		logger.info("自动上标开始,标的分组borrowGroup={}", transferData);
		if (transferData == null) {
			logger.warn("自动上标,解析参数为空={}", transferData);
			return;
		}
		try {
			//自动上标
			loanMoneyService.AutoUpBorrow(transferData);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("自动上标处理borrowGroup={}。异常信息：{}", transferData, e);
		}
	}

}
