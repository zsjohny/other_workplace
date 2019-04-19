package com.jiuy.core.dao.impl.sql;

import java.util.List;

import com.jiuy.core.dao.PartnerCatManageDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.brand.PartnerCatManage;

public class PartnerCatManageDaoSqlImpl extends DomainDaoSqlSupport<PartnerCatManage, Long> implements PartnerCatManageDao{

	@Override
	public List<PartnerCatManage> loadHomeCat() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.PartnerCatManageDaoSqlImpl.loadHomeCat");
	}

}
