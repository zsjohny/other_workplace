package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.product.SortType;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.order.RestrictProductVO;
import com.jiuyuan.entity.product.ProductShare;
import com.jiuyuan.entity.product.RestrictionCombination;
import com.jiuyuan.entity.product.Subscript;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductVO;

@DBMaster
public interface ProductMapper {

    @MapKey("id")
    Map<Long, Product> getProducts();
	
    @MapKey("id")
    Map<Long, Product> getProductByIds(@Param("ids") Collection<Long> ids);  
    
    Product getProductById(@Param("id") long id);    
    
    ProductShare getProductShareByProId(@Param("id") Long id);
    
    @MapKey("id")
    Map<Long, RestrictionCombination> getRestrictByIdSet(@Param("ids") Collection<Long> ids);

    int getProductCountOfCategory(@Param("categoryIds") Collection<Long> categoryIds);
    
    int getProductCountOfCategoryByFilter( @Param("brandId") long brandId, @Param("categoryIds") Collection<Long> categoryIds, 
    									  @Param("filterMap") Map<String, String[]> filterMap,
    									  @Param("tagFilterMap") Map<String, String[]> tagFilterMap,
    									  @Param("colorSizeMap") Map<String, String[]> colorSizeMap,
								    	  @Param("minPrice") double minPrice,
								   		  @Param("maxPrice") double maxPrice,
								   		  @Param("inStock")  Boolean inStock,
								   		  @Param("onSale")  Boolean onSale ,
								   		  @Param("deductFlag")  Boolean deductFlag 
								   		  );

    List<Product> getProductOfCategory(@Param("categoryIds") Collection<Long> categoryIds,
                                       @Param("sortType") SortType sortType,
                                       @Param("pageQuery") PageQuery pageQuery);
    
    
    List<Product> getProductByFilter(@Param("brandId") long brandId, @Param("categoryIds") Collection<Long> categoryIds,
						    	  	 @Param("filterMap") Map<String, String[]> filterMap,
						    	  	 @Param("tagFilterMap") Map<String, String[]> tagFilterMap,
						    	  	 @Param("colorSizeMap") Map<String, String[]> colorSizeMap,
						    	  	 @Param("sortType") SortType sortType,
						    		 @Param("pageQuery") PageQuery pageQuery,
						    		 @Param("minPrice") double minPrice,
						    		 @Param("maxPrice") double maxPrice,
						    		 @Param("inStock")  Boolean inStock,
						    		 @Param("onSale")  Boolean onSale,
						    		 @Param("deductFlag")  Boolean deductFlag
						    		 );
    
    int getProductCountOfCategoryByFilter(@Param("userId") long userId, @Param("brandId") long brandId,
    		@Param("categoryIds") Collection<Long> categoryIds, 
    									  @Param("filterMap") Map<String, String[]> filterMap,
    									  @Param("tagFilterMap") Map<String, String[]> tagFilterMap,
    									  @Param("colorSizeMap") Map<String, String[]> colorSizeMap,
								    	  @Param("minPrice") double minPrice,
								   		  @Param("maxPrice") double maxPrice,
								   		  @Param("inStock")  Boolean inStock,
								   		  @Param("onSale")  Boolean onSale,
								   		@Param("deductFlag")  Boolean deductFlag
								   		  );
    
    List<ProductVO> getProductByFilter(@Param("userId") long userId, @Param("brandId") long brandId,
    		@Param("categoryIds") Collection<Long> categoryIds,
						    	  	 @Param("filterMap") Map<String, String[]> filterMap,
						    	  	 @Param("tagFilterMap") Map<String, String[]> tagFilterMap,
						    	  	 @Param("colorSizeMap") Map<String, String[]> colorSizeMap,
						    	  	 @Param("sortType") SortType sortType,
						    		 @Param("pageQuery") PageQuery pageQuery,
						    		 @Param("minPrice") double minPrice,
						    		 @Param("maxPrice") double maxPrice,
						    		 @Param("inStock")  Boolean inStock,
						    		 @Param("onSale")  Boolean onSale,
						    		 @Param("deductFlag")  Boolean deductFlag
						    		 );
    
    
    List<Product> getProductListByCollection(@Param("productIds") Collection<Long> productIds);
    
    List<Product> getProductListMostRecommendList(@Param("limit") int limit);
    List<Product> getProductListBestSell(@Param("limit") int limit, @Param("time") long time);
    List<Product> getProductListMostCollect(@Param("limit") int limit, @Param("time") long time);
    List<Product> getProductListMostView(@Param("limit") int limit, @Param("time") long time);
    
   
    
    int getProductCountOfBrand(@Param("brandId") Long brandId);

    List<Product> getProductOfBrand(@Param("brandId") Long brandId,
    									@Param("sortType") SortType sortType,
    									@Param("pageQuery") PageQuery pageQuery);
    
    int getProductCountOfCategoryProperty(@Param("categoryIds") Collection<Long> categoryIds,
                                          @Param("propertyValueIds") Collection<Long> propertyValueIds);

    List<Product> getProductOfCategoryProperty(@Param("categoryIds") Collection<Long> categoryIds,
                                               @Param("propertyValueIds") Collection<Long> propertyValueIds,
                                               @Param("sortType") SortType sortType,
                                               @Param("pageQuery") PageQuery pageQuery);

    int getBestSellerProductCount();

    List<Product> getBestSellerProductList(@Param("pageQuery")PageQuery pageQuery);
    
    List<Product> getUserBestSellerProductList186(@Param("userId") long userId, @Param("pageQuery")PageQuery pageQuery);
    
    List<Product> getUserBuyGuessProduct186(@Param("userId") long userId, @Param("pageQuery")PageQuery pageQuery);
//    
//    List<Product> getBestSellerProductList186(@Param("pageQuery")PageQuery pageQuery);
//    
//    List<Product> getBuyGuessProduct186(@Param("pageQuery")PageQuery pageQuery);

    List<Product> getBestSellerProductList185(@Param("pageQuery")PageQuery pageQuery, @Param("sortType") SortType sortType);

    int updateSaleCount(@Param("id") long id, @Param("by") int by);

    List<Product> getProductBySaleTime(@Param("categoryIds") Collection<Long> categoryIds,
                                       @Param("startTimeBegin") long startTimeBegin,
                                       @Param("startTimeEnd") long startTimeEnd,
                                       @Param("sortType") SortType sortType);

    
    List<Long> getBrandIds(@Param("productIds") Collection<Long> productIds);
    
    RestrictionCombination getRestrictById(@Param("restrictId") long restrictId);
    
    Subscript getSubscriptById(@Param("id") long id);

	List<Product> getBuyAlsoProduct(@Param("productIds") Collection<Long> productIds, @Param("pageQuery") PageQuery pageQuery, @Param("count") int count);
	//删除旧表
//	List<RestrictProductVO> getBuyerLog(@Param("userId") long userId, @Param("productIds") Collection<Long> productIds);
	List<RestrictProductVO> getBuyerLogNew(@Param("userId") long userId, @Param("productIds") Collection<Long> productIds);
	

	int getZeroBuyerLog(@Param("userId") long userId, @Param("time") long time);
	
	int getZeroBuyerMonthly(@Param("userId") long userId, @Param("startTime") long startTime, @Param("endTime") long endTime);
	
	int getUserRestrictBuy(@Param("userId") long userId, @Param("restrictId") long restrictId, @Param("startTime") long startTime, @Param("endTime") long endTime);
	
	List<Product> getProductOfWarehouse(@Param("loWarehouseId")Long loWarehouseId, 
											@Param("sortType") SortType sortType, 
											@Param("pageQuery") PageQuery pageQuery);

	int getProductCountOfWarehouse(@Param("loWarehouseId") Long loWarehouseId);
	
	List<ProductVO> getProductByBrandIds(@Param("brandIds") Collection<Long> brandIds, @Param("type") long type,  @Param("categoryIds")Collection<Long> categoryIds);

	String getStoreAvailableBrandStr(@Param("storeId") long storeId);

}
