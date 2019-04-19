package com.jiuy.core.service.partner;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.PartnerInnerCatDao;
import com.jiuyuan.entity.brand.PartnerInnerCat;

@Service
public class PartnerInnerCatServiceImpl implements PartnerInnerCatService {

	@Resource
	private PartnerInnerCatDao partnerInnerCatDaoSqlImpl;
	
	@Override
	public int searchVirtualCat(long id) {
		return partnerInnerCatDaoSqlImpl.searchVirtualCat(id);
	}

	@Override
	public int addVirtualCat(long id) {
		return partnerInnerCatDaoSqlImpl.addVirtualCat(id);
	}

	@Override
	public List<PartnerInnerCat> search(String name, long partnerId) {
		return partnerInnerCatDaoSqlImpl.search(name, partnerId);
	}

	@Override
	public int searchCount(String name, long partnerId) {
		return partnerInnerCatDaoSqlImpl.searchCount(name, partnerId);
	}

	@Override
	public boolean addInnerCat(PartnerInnerCat pic) {
		return partnerInnerCatDaoSqlImpl.addInnerCat(pic) > 0 ? true : false;
	}

	@Override
	public boolean updateInnerCat(PartnerInnerCat pic) {
		return partnerInnerCatDaoSqlImpl.updateInnerCat(pic) > 0 ? true : false;
	}

	@Override
	public boolean removeInnerCat(long id) {
		//判断该分类下是否还存在数据
		
		int row = partnerInnerCatDaoSqlImpl.removeInnerCat(id);
		return  row > 0 ? true : false;
	}

	
}
