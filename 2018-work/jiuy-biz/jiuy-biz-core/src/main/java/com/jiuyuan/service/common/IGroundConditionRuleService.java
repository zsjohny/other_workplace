package com.jiuyuan.service.common;

import java.util.List;

import com.jiuyuan.entity.newentity.UserActiveRule;
import com.jiuyuan.entity.newentity.UserTimeRule;

public interface IGroundConditionRuleService {
	/**
	 * 获取启用的用户激活规则列表
	 * @return
	 */
	public List<UserActiveRule> getUserActiveRuleList() ;
	/**
	 * 获取用户时间规则列表
	 * @return
	 */
	public UserTimeRule getUserTimeRule() ;
	/**
	 * 获取注册X天后发放奖金天数
	 * @return  天数
	 */
	public int getUserRegAfterDayGrantBonus();
	
}