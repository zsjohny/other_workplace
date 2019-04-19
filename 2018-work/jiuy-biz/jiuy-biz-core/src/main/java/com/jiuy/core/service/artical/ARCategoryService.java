package com.jiuy.core.service.artical;

import java.util.List;

import com.jiuy.core.meta.artical.ARCategoryVO;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.article.ARCategory;
import com.jiuyuan.entity.query.PageQuery;

public interface ARCategoryService {

	ResultCode addcat(ARCategory arCategory);

	ResultCode rmCategory(long id);

	ResultCode updateCategory(ARCategory arCategory);

	List<ARCategoryVO> searchCat(PageQuery query, String name);

	int searchCatCount(String name);

	List<ARCategory> loadParentCat();

	List<Long> getSubCats(long parentId);


}
