package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.WithdrawApplyNew;

public interface IMyFinanceSupplierService {

	/**
	 * 获取订单销售总额
	 * @return
	 * 
	 */
	Map<String, Object> getTotalOrderAmount(long supplierId);
	  
	  

	/**
	 * 提现订单，包含   提现订单列表 和 可提现金额
	 * @param status 
	 * @param maxCreateTime 
	 * @param minCreateTime 
	 * @param maxApplyMoney 
	 * @param minApplyMoney 
	 * @return
	 */

	List<WithdrawApplyNew> getWithdrawOrderList(Page<Map<String,Object>> page, long supplierId, double minApplyMoney,
			double maxApplyMoney, String minCreateTime, String maxCreateTime, int status);

	/**
	 * 获取提现订单详情
	 */

	WithdrawApplyNew getWithdrawOrderInfo(long id);

	/**
	 * 获取正在处理的提现订单数目
	 * @param supplierId
	 * @return
	 */
	int getCountOfDealingWDOrder(long supplierId);

	/**
	 * 提交提现申请
	 * @param supplierId
	 * @param applyMoney
	 */
	void submitApply(long supplierId, double applyMoney);

	/**
	 * 可提现金额数
	 * @param supplierId
	 * @return
	 */
	double getAvailableBalance(long supplierId);


    /**
     * 获取待结算金额
     * @param supplierId
     * @return
     */
	Map<String, Object> getSettlingAmount(long supplierId);

}