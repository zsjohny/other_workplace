package com.jiuyuan.service.common;

import java.util.Map;

public interface IPushInstallService {
	/**
	 * 添加用户激活规则
	 * @param stage1
	 * @param stage2
	 * @param stage3
	 * @return
	 */
	Map<String ,Object>addUserActiveRule(int stage1,int stage2,int stage3);
	
	/**
	 * 添加奖金规则
	 * @param userType
	 * @param bounsType
	 * @param sourceType
	 * @param bonus
	 * @return
	 */
	Map<String, Object> addBonusRule(int userType, int bounsType, int sourceType, double bonus);
}
