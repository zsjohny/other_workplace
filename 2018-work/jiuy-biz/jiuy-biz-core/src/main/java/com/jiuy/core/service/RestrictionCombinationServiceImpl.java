package com.jiuy.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.RestrictionCombinationDao;
import com.jiuyuan.entity.product.RestrictionCombination;

@Service
public class RestrictionCombinationServiceImpl implements RestrictionCombinationService {

	@Autowired
	private RestrictionCombinationDao restrictionCombinationDao;
	
	@Override
	public int add(RestrictionCombination restrictionCombination) {
		long time = System.currentTimeMillis();
		
		restrictionCombination.setCreateTime(time);
		restrictionCombination.setUpdateTime(time);
		
		return restrictionCombinationDao.add(restrictionCombination);
	}

	@Override
	public int update(RestrictionCombination restrictionCombination) {
		long time = System.currentTimeMillis();
		restrictionCombination.setUpdateTime(time);
		
		return restrictionCombinationDao.update(restrictionCombination);
	}
	
}
