package com.jiuy.wxaproxy.common.system.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;

public interface IPushWithdrawService {

	Map<String, Object> getDetailById(long groundWithdrawCashRecordId);

	boolean playMoneyById(long groundWithdrawCashRecordId, String bankName, String bankAcountName, String bankAcountNo, double withdrawCashAmount, double transactionAmount, String transactionNo, String remark);

	List<Map<String, Object>> listPage(Page<Map<String, Object>> page, long groundWithdrawCashRecordId, String transactionNo,long groundUserId,
			String applyBeginTime, String applyEndTime, String dealBeginTime, String dealEndTime,
			double transactionAmountMin, double transactionAmountMax, double withdrawCashAmountMin,
			double withdrawCashAmountMax, Integer status);

}
