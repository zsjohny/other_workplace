package com.jiuy.core.service.logistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.LOPostageDao;
import com.jiuy.core.meta.logistics.LOLPostageVO;

@Service
public class LOPostageServiceImpl implements LOPostageService{

	@Autowired
	private LOPostageDao loPostageDao;
	
	@Override
	public List<LOLPostageVO> srchLogistics(int deliveryLocation) {
		return loPostageDao.srchLogistics(deliveryLocation);
	}

	@Override
	public int savePostage(int id, double postage) {
		return loPostageDao.savePostage(id, postage);
	}

}
