package com.store.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.ProductCategory;
import com.store.entity.ProductCategoryVO;
import com.store.entity.ShopCategoryV0;


@DBMaster
public interface ProductCategoryMapper {

	List<ShopCategoryV0> getProductVirtualCategory(@Param("productIds") Collection<Long> productIds);
	
	List<ProductCategory> getProductCategoryListByProductIds(@Param("productIds") Collection<Long> productIds);
	
	@MapKey(value = "productId")
	Map<Long, ProductCategoryVO> getProductCategorys();

}
