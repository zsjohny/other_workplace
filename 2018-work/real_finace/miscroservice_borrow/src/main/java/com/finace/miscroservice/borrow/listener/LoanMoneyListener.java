package com.finace.miscroservice.borrow.listener;

import com.finace.miscroservice.borrow.service.LoanMoneyService;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 标的满标之后自动放款
 */
@Component
public class LoanMoneyListener extends MqListenerConvert {
	private static Log logger = Log.getInstance(LoanMoneyListener.class);

	@Autowired
	private LoanMoneyService loanMoneyService;

	@Override
	protected void transferTo(String transferData) {
		logger.info("标的满标之后自动放款开始transferData={}", transferData);
		if (transferData == null) {
			logger.warn("标的满标之后自动放款,解析参数为空={}", transferData);
			return;
		}
		try {
			//自动放款
			loanMoneyService.LoanMoney(transferData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("标的满标之后自动放款开始处理borrowId={}。异常信息：{}", transferData, e);
		}
	}

}
