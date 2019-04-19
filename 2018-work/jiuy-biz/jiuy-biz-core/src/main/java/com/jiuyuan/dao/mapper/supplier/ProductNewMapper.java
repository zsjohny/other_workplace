package com.jiuyuan.dao.mapper.supplier;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ProductNew;

@DBMaster
public interface ProductNewMapper extends BaseMapper<ProductNew> {
	
	List<ProductNew> getProductNewListByTagId(@Param("limit") int limit,@Param("offset")  int offset,@Param("tagId")  int tagId);

	int getProductNewListByTagIdCount(@Param("tagId") int tagId);
	
	Integer getSkuCountByProductId(@Param("productId") long productId);
	Integer getTotalSkuCountByProductId(@Param("productId") long productId);

	ProductNew findById(Long id);

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
			@Param("totalSkuCountBegin") int totalSkuCountBegin, @Param("totalSkuCountEnd") int totalSkuCountEnd, @Param("orderType")int orderType);

	/**
	 *
	 * @param page
	 * @param supplierId
	 * @param clothesNumber
	 * @param productName
	 * @param state
	 * @param upSoldTimeBegin
	 * @param upSoldTimeEnd
	 * @param priceBegin
	 * @param priceEnd
	 * @param salesCountBegin
	 * @param salesCountEnd
	 * @param totalSkuCountBegin
	 * @param totalSkuCountEnd
	 * @param orderType
	 * @param timingPutawayTimeFloor     定时上架最小值(查询条件)
	 * @param timingPutawayTimeCeil      定时上架最大值(查询条件)
	 * @return java.util.List<java.util.Map < java.lang.String , java.lang.Object>>
	 * update By Charlie(唐静)
	 * @date 2018/6/15 9:46
	 */
	List<Map<String, Object>> selectPageList2(@Param("page") Page<Map<String, Object>> page,
			@Param("supplierId") long supplierId, @Param("clothesNumber") String clothesNumber,
			@Param("productName") String productName, @Param("state") long state,
			@Param("upSoldTimeBegin") long upSoldTimeBegin, @Param("upSoldTimeEnd") long upSoldTimeEnd,
			@Param("priceBegin") double priceBegin, @Param("priceEnd") double priceEnd,
			@Param("salesCountBegin") int salesCountBegin, @Param("salesCountEnd") int salesCountEnd,
			@Param("totalSkuCountBegin") int totalSkuCountBegin, @Param("totalSkuCountEnd") int totalSkuCountEnd, @Param("orderType")int orderType,
			@Param("timingPutawayTimeFloor") Long timingPutawayTimeFloor, @Param("timingPutawayTimeCeil") Long timingPutawayTimeCeil
											  );

	
	
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

	void batchUpdateBrandType(@Param("productIdList") List<Long> productIdList, @Param("brandType") int brandType, @Param("brandName")String brandName, @Param("brandLogo")String brandLogo);

	void batchUpdateRelativeInfo(@Param("brandId")long brandId, @Param("brandType")int brandType, @Param("brandName")String brandName, @Param("brandLogo")String brandLogo);

	void batchProductCategoryName(@Param("categoryId")long categoryId, @Param("categoryName")String categoryName);

	/**
	 * 修改对应二级分类的一级分类信息
	 * @param firstCategoryId
	 * @param firstCategoryName
	 * @param categoryId
	 */
	void updateProductFirstCategoryInfo(@Param("firstCategoryId")long firstCategoryId, 
			@Param("firstCategoryName")String firstCategoryName, @Param("categoryId")long categoryId);

	/**
	 * 修改对应三级分类的二级分类信息
	 * @param secondCategoryId
	 * @param secondCategoryName
	 * @param categoryId
	 */
	void updateProductSecondCategoryInfo(@Param("secondCategoryId")long secondCategoryId, 
			@Param("secondCategoryName")String secondCategoryName, @Param("categoryId")long categoryId);

	List<Long> productsOfCategory(@Param("list")List<Long> list);

	/**
	 * 通过动态属性 list 的查询条件做UNION
	 * @param param map中 brandIds 品牌id的List dynPropId 属性的大类id  dynPropValId属性的小类id
	 * @date:   2018/5/10 17:55
	 * @author: Aison
	 */
	List<ProductNew> selectByDynamics(Pagination page,List<Map<String, Object>> param);

	/**
	 * 获取某一商品的库存总数
	 * @param productId
	 * @return int
	 * @auther Charlie(唐静)
	 * @date 2018/5/30 17:03
	 */
    int countSkusRemain(@Param("productId")Long productId);


	/**
	 * 查询商品列表带商品统计信息的
	 *
	 * @author Aison
	 * @date 2018/6/20 13:30
	 */
	List<ProductNew>  selectProductWidthMonitor(Pagination page,Map<String,Object> param);



	List<ProductNew> selectByBrandIds(Map<String,Object> param);


	@MapKey("brandId")
	Map<BigInteger,Map<String,Long>> selectProductCount(@Param("brandIds") Set<Long> brandIds);

	/**
	 * 查询商品sku数量,和商品总库存
	 *
	 * @param productIds productIds
	 * @return java.util.List
	 * @author Charlie
	 * @date 2018/7/30 19:31
	 */
	List<Map<String,Object>> querySkuCountAndRemainCount(@Param ("productIds") List<Long> productIds);

	/**
	 *	重置排名
	 * @return
	 * @author hyf
	 * @date 2018/8/29 16:34
	 */
    int resetRank();
	/**
	 *
	 * @param page
	 * @param supplierId
	 * @param clothesNumber
	 * @param productName
	 * @param state
	 * @param upSoldTimeBegin
	 * @param upSoldTimeEnd
	 * @param priceBegin
	 * @param priceEnd
	 * @param salesCountBegin
	 * @param salesCountEnd
	 * @param totalSkuCountBegin
	 * @param totalSkuCountEnd
	 * @param orderType
	 * @param timingPutawayTimeFloor     定时上架最小值(查询条件)
	 * @param timingPutawayTimeCeil      定时上架最大值(查询条件)
	 * @return java.util.List<java.util.Map < java.lang.String , java.lang.Object>>
	 * update By Charlie(唐静)
	 * @date 2018/6/15 9:46
	 */
	int selectPageListSize(@Param("page") Page<Map<String, Object>> page,
											  @Param("supplierId") long supplierId, @Param("clothesNumber") String clothesNumber,
											  @Param("productName") String productName, @Param("state") long state,
											  @Param("upSoldTimeBegin") long upSoldTimeBegin, @Param("upSoldTimeEnd") long upSoldTimeEnd,
											  @Param("priceBegin") double priceBegin, @Param("priceEnd") double priceEnd,
											  @Param("salesCountBegin") int salesCountBegin, @Param("salesCountEnd") int salesCountEnd,
											  @Param("totalSkuCountBegin") int totalSkuCountBegin, @Param("totalSkuCountEnd") int totalSkuCountEnd, @Param("orderType")int orderType,
											  @Param("timingPutawayTimeFloor") Long timingPutawayTimeFloor, @Param("timingPutawayTimeCeil") Long timingPutawayTimeCeil
	);
}