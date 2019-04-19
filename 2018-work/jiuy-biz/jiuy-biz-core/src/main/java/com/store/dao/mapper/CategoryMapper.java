package com.store.dao.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.store.entity.ShopCategory;

@DBMaster
public interface CategoryMapper {

    List<ShopCategory> getCategories();
    
    /**
     * 获取指定分类ID分类列表
     * @param arr
     * @return
     */
    List<ShopCategory> getCategoriesByIdsArr(@Param("arr") String [] arr);
    
    List<ShopCategory> getParentCategories();
    
    List<ShopCategory> getAllParentCategories();
    
    List<ShopCategory> getAllProductParentCategories(@Param("storeId")Long storeId);

	List<ShopCategory> getCategoriesByParentId(@Param("parentId")long parentId);

	List<Long> getCategoriesByProductId(@Param("productId")Long productId);

	List<ShopCategory> getCategoryByIds(@Param("categoryIds")List<Long> categoryIds);

	List<ShopCategory> getChildCategoryByParentId(@Param("parentId")long parentId);

}
