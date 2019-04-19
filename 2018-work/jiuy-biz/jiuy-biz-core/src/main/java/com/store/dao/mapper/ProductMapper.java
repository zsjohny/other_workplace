package com.store.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.product.SortType;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.order.RestrictProductVO;
import com.jiuyuan.entity.product.ProductShare;
import com.jiuyuan.entity.product.RestrictionCombination;
import com.jiuyuan.entity.product.Subscript;
import com.jiuyuan.entity.query.PageQuery;
import com.store.entity.ProductVOShop;


@DBMaster
public interface ProductMapper{

    @MapKey( "id" )
    Map<Long, Product> getProducts();

    @MapKey( "id" )
    Map<Long, ProductVOShop> getProductByIds(@Param( "ids" ) Collection<Long> ids);

    @MapKey( "id" )
    Map<Long, RestrictionCombination> getRestrictByIdSet(@Param( "ids" ) Collection<Long> ids);

    int getProductCountOfCategory(@Param( "categoryIds" ) Collection<Long> categoryIds);

    int getProductCountOfCategoryByFilter(
            @Param( "vip" ) int vip,
            @Param( "storeId" ) long storeId, @Param( "brandId" ) long brandId,
            @Param( "categoryIds" ) Collection<Long> categoryIds,
            @Param( "filterMap" ) Map<String, String[]> filterMap,
            @Param( "tagFilterMap" ) Map<String, String[]> tagFilterMap,
            @Param( "colorSizeMap" ) Map<String, String[]> colorSizeMap,
            @Param( "minPrice" ) double minPrice,
            @Param( "maxPrice" ) double maxPrice,
            @Param( "inStock" ) Boolean inStock,
            @Param( "onSale" ) Boolean onSale,
            @Param( "guideFlag" ) int guideFlag,
            @Param( "keyWords" ) String keyWords,
            @Param( "brandType" ) int brandType
    );


    List<Product> getProductOfCategory(@Param( "categoryIds" ) Collection<Long> categoryIds,
                                       @Param( "sortType" ) SortType sortType,
                                       @Param( "pageQuery" ) PageQuery pageQuery);

    List<ProductVOShop> getProductByFilter(
            @Param( "vip" ) int vip,
            @Param( "storeId" ) long storeId, @Param( "brandId" ) long brandId,
            @Param( "categoryIds" ) Collection<Long> categoryIds,
            @Param( "filterMap" ) Map<String, String[]> filterMap,
            @Param( "tagFilterMap" ) Map<String, String[]> tagFilterMap,
            @Param( "colorSizeMap" ) Map<String, String[]> colorSizeMap,
            @Param( "sortType" ) SortType sortType,
            @Param( "pageQuery" ) PageQuery pageQuery,
            @Param( "minPrice" ) double minPrice,
            @Param( "maxPrice" ) double maxPrice,
            @Param( "inStock" ) Boolean inStock,
            @Param( "onSale" ) Boolean onSale,
            @Param( "guideFlag" ) int guideFlag,
            @Param( "keyWords" ) String keyWords,
            @Param( "brandType" ) int brandType

    );


    int getProductCountOfBrand(@Param( "brandId" ) Long brandId);

    List<Product> getProductOfBrand(@Param( "brandId" ) Long brandId,
                                    @Param( "sortType" ) SortType sortType,
                                    @Param( "pageQuery" ) PageQuery pageQuery);

    int getProductCountOfCategoryProperty(@Param( "categoryIds" ) Collection<Long> categoryIds,
                                          @Param( "propertyValueIds" ) Collection<Long> propertyValueIds);

    List<Product> getProductOfCategoryProperty(@Param( "categoryIds" ) Collection<Long> categoryIds,
                                               @Param( "propertyValueIds" ) Collection<Long> propertyValueIds,
                                               @Param( "sortType" ) SortType sortType,
                                               @Param( "pageQuery" ) PageQuery pageQuery);

    int getBestSellerProductCount();

    List<Product> getBestSellerProductList(@Param( "pageQuery" ) PageQuery pageQuery);

    List<ProductVOShop> getUserBestSellerProductList186(@Param( "storeId" ) long storeId, @Param( "pageQuery" ) PageQuery pageQuery, @Param( "guideFlag" ) int guideFlag);

    List<ProductVOShop> getUserBuyGuessProduct186(@Param( "storeId" ) long storeId, @Param( "pageQuery" ) PageQuery pageQuery);
//    
//    List<Product> getBestSellerProductList186(@Param("pageQuery")PageQuery pageQuery);
//    
//    List<Product> getBuyGuessProduct186(@Param("pageQuery")PageQuery pageQuery);

    List<ProductVOShop> getBestSellerProductList185(@Param( "pageQuery" ) PageQuery pageQuery, @Param( "sortType" ) SortType sortType);

    int updateSaleCount(@Param( "id" ) long id, @Param( "by" ) int by);

    List<Product> getProductBySaleTime(@Param( "categoryIds" ) Collection<Long> categoryIds,
                                       @Param( "startTimeBegin" ) long startTimeBegin,
                                       @Param( "startTimeEnd" ) long startTimeEnd,
                                       @Param( "sortType" ) SortType sortType);


    List<Long> getBrandIds(@Param( "productIds" ) Collection<Long> productIds);

    RestrictionCombination getRestrictById(@Param( "restrictId" ) long restrictId);

    Subscript getSubscriptById(@Param( "id" ) long id);

    List<Product> getBuyAlsoProduct(@Param( "productIds" ) Collection<Long> productIds, @Param( "pageQuery" ) PageQuery pageQuery, @Param( "count" ) int count);

    List<ProductVOShop> getProductListByIds(@Param( "productIds" ) Collection<Long> productIds);


    List<ProductVOShop> getProductMapByOrderNo(@Param( "orderNo" ) String orderNo);

    List<RestrictProductVO> getBuyerLogByStoreOrder(@Param( "storeId" ) long storeId, @Param( "productIds" ) Collection<Long> productIds);

    int getZeroBuyerLog(@Param( "storeId" ) long storeId, @Param( "time" ) long time);

    int getZeroBuyerMonthly(@Param( "storeId" ) long storeId, @Param( "startTime" ) long startTime, @Param( "endTime" ) long endTime);

    int getUserRestrictBuy(@Param( "storeId" ) long storeId, @Param( "restrictId" ) long restrictId, @Param( "startTime" ) long startTime, @Param( "endTime" ) long endTime);

    int getUserRestrictBuyByStoreOrder(@Param( "storeId" ) long storeId, @Param( "restrictId" ) long restrictId, @Param( "startTime" ) long startTime, @Param( "endTime" ) long endTime);

    List<Product> getProductOfWarehouse(@Param( "loWarehouseId" ) Long loWarehouseId,
                                        @Param( "sortType" ) SortType sortType,
                                        @Param( "pageQuery" ) PageQuery pageQuery);

    int getProductCountOfWarehouse(@Param( "loWarehouseId" ) Long loWarehouseId);

    Product getProductBySkuNo(@Param( "skuNo" ) long skuNo);


    List<ProductVOShop> getProductByBrandIds(@Param( "brandIds" ) Collection<Long> brandIds, @Param( "type" ) long type, @Param( "guideFlag" ) int guideFlag, @Param( "categoryIds" ) Collection<Long> categoryIds);

    /**
     * 获取商品
     * @param: brandIds 商品所属品牌
     * @param: type  type=0 ---> "order by CartSttstcs desc"  type>0 ---> "order by HotSttstcs desc"
     * @param: guideFlag  guideFlag=0 ---> "where (Type = 1 or Type = 2)"  guideFlag=1 ---> "where (Type = 0 or Type = 1)"
     * @param: categoryIds 分类ids
     * @param: states 商品状态： 0（编辑中，未完成编辑）、1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、5（待上架，审核通过、待上架）、6（上架，审核通过、已上架）、7（下架，审核通过、已下架）
     * @return: java.util.List<com.store.entity.ProductVOShop>
     * @auther: Charlie(唐静)
     * @date: 2018/5/23 9:58
     */
    List<ProductVOShop> getProductByBrands(@Param( "brandIds" ) Collection<Long> brandIds, @Param( "type" ) long type, @Param( "guideFlag" ) int guideFlag, @Param( "categoryIds" ) Collection<Long> categoryIds, @Param( "states" ) Collection<Integer> states);

    String getStoreAvailableBrandStr(@Param( "storeId" ) long storeId);

    public ProductShare getProductShareByProId(long productId);

    Product getProduct(@Param( "id" ) long id);

    List<Long> getAllProductIdsByUser(@Param( "storeId" ) long id, @Param( "restrictIds" ) List<Long> restrictIds);

    int getProductCountsByRestrictId(@Param( "storeId" ) long id, @Param( "restrictDayTime" ) long restrictDayTime, @Param( "restrictionCombinationId" ) Long restrictionCombinationId);

    List<ProductVOShop> getTagProducts(@Param( "tagId" ) long tagId, @Param( "storeId" ) long storeId, @Param( "pageQuery" ) PageQuery pageQuery);

    int getTagProductsCount(@Param( "tagId" ) long tagId, @Param( "storeId" ) long storeId);

    List<Product> getProductsByIds(@Param( "ids" ) List<Long> productIdList);

    List<ProductVOShop> getProductByFilterAndTagIds(
            @Param( "vip" ) int vip,
            @Param( "storeId" ) long storeId, @Param( "brandId" ) long brandId,
            @Param( "tagIds" ) Collection<Long> tagIds,
            @Param( "filterMap" ) Map<String, String[]> filterMap,
            @Param( "tagFilterMap" ) Map<String, String[]> tagFilterMap,
            @Param( "colorSizeMap" ) Map<String, String[]> colorSizeMap,
            @Param( "sortType" ) SortType sortType,
            @Param( "pageQuery" ) PageQuery pageQuery,
            @Param( "minPrice" ) double minPrice,
            @Param( "maxPrice" ) double maxPrice,
            @Param( "inStock" ) Boolean inStock,
            @Param( "onSale" ) Boolean onSale,
            @Param( "guideFlag" ) int guideFlag,
            @Param( "keyWords" ) String keyWords,
            @Param( "brandType" ) int brandType

    );

    int getProductCountOfCategoryByFilterAndTagIds(
            @Param( "vip" ) int vip,
            @Param( "storeId" ) long storeId, @Param( "brandId" ) long brandId,
            @Param( "tagIds" ) Collection<Long> tagIds,
            @Param( "filterMap" ) Map<String, String[]> filterMap,
            @Param( "tagFilterMap" ) Map<String, String[]> tagFilterMap,
            @Param( "colorSizeMap" ) Map<String, String[]> colorSizeMap,
            @Param( "minPrice" ) double minPrice,
            @Param( "maxPrice" ) double maxPrice,
            @Param( "inStock" ) Boolean inStock,
            @Param( "onSale" ) Boolean onSale,
            @Param( "guideFlag" ) int guideFlag,
            @Param( "keyWords" ) String keyWords,
            @Param( "brandType" ) int brandType
    );

    /**
     * 查询查询合格商品Id (商品上架, 并且库存量>0)
     *
     * @param ids 商品ids
     * @return java.util.Set<java.lang.Long>
     * @auther Charlie(唐静)
     * @date 2018/6/8 15:49
     */
    List<Long> checkProduct(@Param("productIds")Collection<Long> ids);


}