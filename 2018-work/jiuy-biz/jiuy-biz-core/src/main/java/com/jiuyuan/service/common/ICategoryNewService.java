package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.entity.newentity.CategoryNew;

public interface ICategoryNewService {

	/**
	    * 获取商品分类列表，用于编辑商品选择商品分类
	    */
	List<CategoryNew> getProductCategoryList();
	
	 /**
	    * 获取一级分类列表
	    */
	public List<CategoryNew> getOneCategoryList() ;

	List<Map<String, Object>> getPotentialCategoryList(String productId);

	void updatePotentialCategory(String productId, String categoryIds);

	/**
	 * 根据id查询
	 * @param targetId
	 * @return:
	 * @auther: Charlie(唐静)
	 * @date: 2018/5/16 15:14
	 */
	CategoryNew getById(long targetId);
}