package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuy.core.meta.artical.ARCategoryVO;
import com.jiuyuan.entity.article.ARCategory;
import com.jiuyuan.entity.query.PageQuery;

public interface ARCategoryDao  extends DomainDao<ARCategory, Long>{

	ARCategory addcat(ARCategory arCategory);

	int rmCategory(long id);

	int updateCategory(ARCategory arCategory);

	List<ARCategoryVO> searchCat(PageQuery query, String name);

	int searchCatCount(String name);

	List<ARCategory> loadParentCat();

	List<Long> getSubCats(long parentId);

}
