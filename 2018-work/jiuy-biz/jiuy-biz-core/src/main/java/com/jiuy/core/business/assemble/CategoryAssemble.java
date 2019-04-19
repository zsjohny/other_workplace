package com.jiuy.core.business.assemble;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.service.CategoryService;
import com.jiuyuan.entity.Category;

@Service
public class CategoryAssemble {
	
	@Resource
    private CategoryService categoryService;

	public Category getCategoryById(long id) {
		return categoryService.getCategoryById(id);
	}
	
}
