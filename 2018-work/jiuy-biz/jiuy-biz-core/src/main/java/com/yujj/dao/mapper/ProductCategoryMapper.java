package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.product.CategoryV0;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.entity.product.ProductCategory;
import com.yujj.entity.product.ProductCategoryVO;

@DBMaster
public interface ProductCategoryMapper {

    int getProductCount(@Param("categoryIds") Collection<Long> categoryIds);

    List<Long> getProductIds(@Param("categoryIds") Collection<Long> categoryIds, @Param("pageQuery") PageQuery pageQuery);
    
    List<Long> getCategoryIdsAll(@Param("productIds") Collection<Long> productIds);
    
    List<ProductCategory> getProductCategoryListByProductIds(@Param("productIds") Collection<Long> productIds);

	List<CategoryV0> getProductVirtualCategory(@Param("productIds") Collection<Long> productIds);

	/**
	 * @author DongZhong
	 */
	@MapKey(value = "productId")
	Map<Long, ProductCategoryVO> getProductCategorys();

}
