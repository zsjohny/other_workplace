package com.jiuy.core.service.artical;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.ARCategoryDao;
import com.jiuy.core.meta.artical.ARCategoryVO;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.article.ARCategory;
import com.jiuyuan.entity.query.PageQuery;

@Service("arCategoryService")
public class ARCategoryServiceImpl implements ARCategoryService{
	
	@Resource
	private ARCategoryDao arCategoryDaoSqlImpl;

	@Override
	public ResultCode addcat(ARCategory arCategory) {
		long createTime = System.currentTimeMillis();
		
		arCategory.setCreateTime(createTime);
		arCategory.setUpdateTime(createTime);
		
		arCategoryDaoSqlImpl.addcat(arCategory);
	
		return ResultCode.COMMON_SUCCESS;
	}

	@Override
	public ResultCode rmCategory(long id) {
		arCategoryDaoSqlImpl.rmCategory(id);
		
		return ResultCode.COMMON_SUCCESS;
	}

	@Override
	public ResultCode updateCategory(ARCategory arCategory) {
		arCategoryDaoSqlImpl.updateCategory(arCategory);
		
		return ResultCode.COMMON_SUCCESS;
	}

	@Override
	public List<ARCategoryVO> searchCat(PageQuery query, String name) {
		return arCategoryDaoSqlImpl.searchCat(query, name);
	}

	@Override
	public int searchCatCount(String name) {
		return arCategoryDaoSqlImpl.searchCatCount(name);
	}

	@Override
	public List<ARCategory> loadParentCat() {
		return arCategoryDaoSqlImpl.loadParentCat();
	}

	@Override
	public List<Long> getSubCats(long parentId) {
		return arCategoryDaoSqlImpl.getSubCats(parentId);
	}

}
