package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuyuan.entity.brand.PartnerInnerCat;

public interface PartnerInnerCatDao extends DomainDao<PartnerInnerCat, Long> {

	int searchVirtualCat(long id);

	int addVirtualCat(long id);

	List<PartnerInnerCat> search(String name, long partnerId);

	int searchCount(String name, long partnerId);

	int addInnerCat(PartnerInnerCat pic);

	int updateInnerCat(PartnerInnerCat pic);

	int removeInnerCat(long id);

}
