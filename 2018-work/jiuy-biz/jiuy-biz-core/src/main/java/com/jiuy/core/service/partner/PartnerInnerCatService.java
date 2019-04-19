package com.jiuy.core.service.partner;

import java.util.List;

import com.jiuyuan.entity.brand.PartnerInnerCat;

public interface PartnerInnerCatService {

	int searchVirtualCat(long l);

	int addVirtualCat(long id);

	List<PartnerInnerCat> search(String name, long partnerId);

	int searchCount(String name, long partnerId);

	boolean addInnerCat(PartnerInnerCat pic);

	boolean updateInnerCat(PartnerInnerCat pic);

	boolean removeInnerCat(long id);

}
