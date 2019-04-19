package com.jiuy.core.service.logistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.LOLocationDao;
import com.jiuyuan.entity.logistics.LOLocation;

@Service
public class LOLocationServiceImpl implements LOLocationService{
	
	@Autowired
	private LOLocationDao lOLocationDao;

	@Override
	public List<LOLocation> search(int type) {
		return lOLocationDao.search(type);
	}
	
}
