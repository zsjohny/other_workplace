package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.HuanXin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = HuanXin.class, idClass = Integer.class)
public interface HuanXinRepository {
	@Query("from HuanXin where ownerId=?1 ")
	HuanXin queryIsExist(Integer ownerId);

	@Query("from HuanXin where huaXinName=?1 ")
	HuanXin queryHuanXinByName(String huaXinName);
}
