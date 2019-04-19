package com.store.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.product.BrandFilter;
import com.jiuyuan.entity.product.CategoryFilter;



@DBMaster
public interface CategoryFilterMapper {

    List<CategoryFilter> getProductFilterByCatId(@Param("id") long id);
    
    List<CategoryFilter> getProductFilterByCatIds(@Param("ids") Collection<Long> ids);
    
    List<BrandFilter> getProductFilterByBrandIds(@Param("ids") Collection<Long> ids);

}