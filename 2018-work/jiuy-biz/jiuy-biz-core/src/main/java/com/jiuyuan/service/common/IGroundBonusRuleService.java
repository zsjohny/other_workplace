package com.jiuyuan.service.common;

public interface IGroundBonusRuleService {
	/**
	 * 获取地推奖金规则
	 * @param userType  用户类型：1(大区总监)、2(省区经理)、3(区域主管)、4(城市经理)
	 * @param bonusType  奖金类型：1(门店注册奖金)、2（门店激活） 、3（第一阶段门店交易奖金），4（第二阶段门店交易奖金）、5（第三阶段门店交易奖金） 可以调用bean中的常量
	 * @param sourceType 奖金来源类型：0个人、1团体
	 * @return
	 */
	double getGroundBonusRule(int userType,int bonusType,int sourceType);
}
