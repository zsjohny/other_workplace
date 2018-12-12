package com.wuai.company.order.service;

import com.wuai.company.order.entity.TrystEvaluation;
import com.wuai.company.util.Response;

/**
 * Created by zTerry on 2018/3/14.
 * 用户评价service
 */
public interface TrystEvaluationService {
	
	Response createEvaluation(Integer ownUserId,Integer targetUserId,Integer type,String serviceAttitude,String serviceLabel,
			String similarity,String similarityLabel);

}
