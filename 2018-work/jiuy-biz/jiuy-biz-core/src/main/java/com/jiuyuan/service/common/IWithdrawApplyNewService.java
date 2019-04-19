package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.WithdrawApplyNew;

public interface IWithdrawApplyNewService {

	WithdrawApplyNew getWithdrawApplyInfoById(long withDrawApplyId);

	List<WithdrawApplyNew> search(Page<Map<String,Object>> page, long tradeId, String tradeNo, int status, int type,
			double startApplyMoney, double endApplyMoney, long startCreateTimeL, long endCreateTimeL, long supplierId);

	void withDrawConfirm(long id, Long adminId, double money, int type, String tradeNo, int tradeWay, String remark,
			int status);

	List<WithdrawApplyNew> getNoDealWithdrawApplyInfoById(long withDrawApplyId);

}