package com.jiuy.core.dao;

import java.util.List;

import com.jiuyuan.entity.product.RestrictionCombination;
import com.jiuyuan.entity.query.PageQuery;

public interface RestrictionCombinationDao {

	List<RestrictionCombination> search(PageQuery pageQuery, String name);

    int update(RestrictionCombination restrictionCombination);

	int searchCount(String name);

	int add(RestrictionCombination restrictionCombination);
	
}
