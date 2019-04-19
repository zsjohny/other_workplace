package com.jiuyuan.dao.mapper.supplier;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ProductNew;

@DBMaster
public interface ShopNotificationNewMapper extends BaseMapper<ProductNew> {

	List<ProductNew> getSearchProductList(@Param("page") Page<ProductNew> page, @Param("keyword") String keyword,
			@Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

	// ProductNew getProductById(@Param("productId") long productId);

	List<ProductNew> getAllProducts();

	List<ProductNew> productOfIds(@Param("ids") Set<Long> productIds);

	List<Map<String, Object>> selectPageList(@Param("page") Page<Map<String, Object>> page,
			@Param("supplierId") long supplierId, @Param("clothesNumber") String clothesNumber,
			@Param("productName") String productName, @Param("state") long state,
			@Param("upSoldTimeBegin") long upSoldTimeBegin, @Param("upSoldTimeEnd") long upSoldTimeEnd,
			@Param("priceBegin") double priceBegin, @Param("priceEnd") double priceEnd,
			@Param("salesCountBegin") int salesCountBegin, @Param("salesCountEnd") int salesCountEnd,
			@Param("totalSkuCountBegin") int totalSkuCountBegin, @Param("totalSkuCountEnd") int totalSkuCountEnd);

	int bindBadgeProduct(@Param("badgeId")long badgeId,@Param("badgeImage")String badgeImage,@Param("badgeName")String badgeName ,@Param("productIds")Set<Long> set);

	void clearProductBadge(@Param("productIds")Set<Long> set);

	int bindProductBadgeCondition(
			@Param("badgeId")long badgeId, 
			@Param("badgeImage")String badgeImage,
			@Param("badgeName")String badgeName, 
			@Param("brandName")String brandName, 
			@Param("state")int state,
			@Param("minLadderPriceStart")double minLadderPriceStart, 
			@Param("minLadderPriceEnd")double minLadderPriceEnd, 
			@Param("name")String name, 
			@Param("categoryIds")String[] categoryIds, 
			@Param("clothesNumber")String clothesNumber,
			@Param("badgeStatus")long badgeStatus,
			@Param("productIds")Set<Long> set
			);
	

	int clearProductBadgeCondition(@Param("brandName")String brandName,
			@Param("state")int state, 
			@Param("minLadderPriceStart")double minLadderPriceStart, 
			@Param("minLadderPriceEnd")double minLadderPriceEnd,
			@Param("name")String name,
			@Param("categoryIds")String[] categoryIds,
			@Param("clothesNumber")String clothesNumber, 
			@Param("badgeStatus")long badgeStatus,
			@Param("productIds")Set<Long> set
			);
	
	void updateProductBrandInfo(@Param("brandId")long brandId, @Param("brandName")String brandName, @Param("brandLogo")String brandLogo,@Param("productIds")List<Map<String, Object>> productIds);

	List<Map<String,Object>> getAllProductIds();

}
