package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuyuan.entity.brand.PartnerCatManage;

public interface PartnerCatManageDao  extends DomainDao<PartnerCatManage, Long>{

	List<PartnerCatManage> loadHomeCat();

}
