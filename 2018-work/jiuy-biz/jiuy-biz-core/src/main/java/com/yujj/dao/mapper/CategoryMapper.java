package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.product.Category;

@DBMaster
public interface CategoryMapper {

    List<Category> getCategories();
    
    List<Category> getCategoriesByIdsArr(@Param("arr") String [] arr);
    
    
    List<Category> getParentCategories();

}
