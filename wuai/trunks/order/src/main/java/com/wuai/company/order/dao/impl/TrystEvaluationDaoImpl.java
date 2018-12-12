package com.wuai.company.order.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wuai.company.order.dao.TrystEvaluationDao;
import com.wuai.company.order.entity.TrystEvaluation;
import com.wuai.company.order.mapper.TrystEvaluationMapper;
/**
 * Created by zTerry on 2018/3/14.
 * 用户评价dao层实现类
 */
@Repository
public class TrystEvaluationDaoImpl implements TrystEvaluationDao{
   
	@Autowired
	private TrystEvaluationMapper trystEvaluationMapper;

	@Override
	public int insertSelective(TrystEvaluation record) {
		return trystEvaluationMapper.insertSelective(record);
	}
	
	
	
	
	
}
