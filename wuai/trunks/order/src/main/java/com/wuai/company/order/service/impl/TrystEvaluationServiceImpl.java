package com.wuai.company.order.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wuai.company.enums.ResponseTypeEnum;
import com.wuai.company.order.dao.TrystEvaluationDao;
import com.wuai.company.order.entity.TrystEvaluation;
import com.wuai.company.order.service.TrystEvaluationService;
import com.wuai.company.util.Response;
import com.wuai.company.util.UserUtil;
/**
 * Created by zTerry on 2018/3/14.
 * 用户评价service实现类
 */
@Service
@Transactional
public class TrystEvaluationServiceImpl implements TrystEvaluationService{
	
	@Autowired
    private TrystEvaluationDao trystEvaluationDao;
    
	
	private Logger logger = LoggerFactory.getLogger(TrystEvaluationServiceImpl.class);
	
	@Override
	public Response createEvaluation(Integer ownUserId, Integer targetUserId,
			Integer type, String serviceAttitude, String serviceLabel,
			String similarity, String similarityLabel) {
		//判断必要参数
		if(null == ownUserId || null == targetUserId || null == serviceAttitude || "".equals(serviceAttitude) || null == type){
			logger.warn("创建新的评价 所传参数为空 ownUserId={} targetUserId={} serviceAttitude={} type={}", ownUserId, targetUserId,serviceAttitude,type);
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
		}
		
		TrystEvaluation evaluation = new TrystEvaluation();
		evaluation.setUuid(UserUtil.generateUuid());
		evaluation.setOwnUserId(ownUserId);
		evaluation.setTargetUserId(targetUserId);
		evaluation.setType(type);
		evaluation.setServiceAttitude(serviceAttitude);
		if(null != serviceLabel && !"".equals(similarityLabel)){
			evaluation.setServiceLabel(serviceLabel);
		}
		
		if(serviceAttitude.equals("好评")){
			//服务态度为好评的情况下 才有相似度评价
			if(null != similarity && !"".equals(similarity)){
				evaluation.setSimilarity(similarity);
			}
			if(null != similarityLabel && !"".equals(similarityLabel)){
				evaluation.setSimilarityLabel(similarityLabel);
			}
		}
		
		int i = trystEvaluationDao.insertSelective(evaluation);
		if(0 >= i){
			//插入失败
            return Response.response(ResponseTypeEnum.ERROR_CODE.toCode());
		}
		
		
		return Response.success();
	}


}
