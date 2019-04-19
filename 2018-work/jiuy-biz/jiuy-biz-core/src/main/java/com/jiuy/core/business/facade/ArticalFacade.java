package com.jiuy.core.business.facade;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.service.artical.ARCategoryService;
import com.jiuy.core.service.artical.ArticalService;
import com.jiuyuan.constant.ResultCode;

@Service
public class ArticalFacade {
	@Resource
	private ARCategoryService arCategoryService;
	
	@Resource
	private ArticalService articalServiceImpl;

	public ResultCode removeCat(long id) {
		if(hasSubCat(id)) {
			return ResultCode.DELETE_ERROR_SUB_EXIST;
		}
		if(hasArtical(id)) {
			return ResultCode.DELETE_ERROR_SUB_EXIST;
		}
		arCategoryService.rmCategory(id);
		
		return ResultCode.COMMON_SUCCESS;
	}
	
	private boolean hasArtical(long id) {
		
		return getARCount(id) > 0 ? true : false;
	}

	private boolean hasSubCat(long id) {
		return arCategoryService.getSubCats(id).size() > 0 ? true : false;
	}

	private int getARCount(long id) {
		List<Long> catIds = arCategoryService.getSubCats(id);
		
		catIds.add(id);
		
		return articalServiceImpl.getCatARCount(catIds);
	}
	
}
