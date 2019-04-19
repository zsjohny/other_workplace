package com.jiuy.core.service.partner;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.PartnerCatManageDao;
import com.jiuyuan.entity.brand.PartnerCatManage;

@Service
public class PartnerCatManageServiceImpl implements PartnerCatManageService {

	@Resource
	private PartnerCatManageDao partnerCatManageDaoSqlImpl;
	
	@Override
	public List<PartnerCatManage> loadHomeCat() {
		return partnerCatManageDaoSqlImpl.loadHomeCat();
	}
	
}
