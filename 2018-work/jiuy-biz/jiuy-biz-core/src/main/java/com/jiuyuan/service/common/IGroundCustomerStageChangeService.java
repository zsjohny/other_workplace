package com.jiuyuan.service.common;

import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.entity.newentity.UserTimeRule;

public interface IGroundCustomerStageChangeService {

	int insertGroundCustomerStageChange(long storeId, Long groundUserId, String superIds, int oneStageTime,
			int twoStageTime, int threeStageTime, UserTimeRule userTimeRule);

	void executeGroundCustomerStageChange(int getyesterday);

}