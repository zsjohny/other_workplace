package com.jiuyuan.dao.mapper.supplier;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.newentity.ProductNew;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ShopProduct;


/**
 * 
 * @author QiuYuefan
 *
 */
@DBMaster
public interface ShopProductMapper extends BaseMapper<ShopProduct> {

    List<ShopProduct> selectHomepageProductList(@Param("categoryId") Integer categoryId, @Param("offset") Integer offset, @Param("limit") Integer limit,
                                                @Param("storeId") Long storeId);

    List<ShopProduct> getShopProductListByProIds(@Param("storeId") long storeId, @Param("proIds") Collection<Long> proIds);

    List<Long> findAllIdsByStoreIdAndTypes(@Param("storeId") Long storeId, @Param("type") Integer type,
                                           @Param("start") Integer start, @Param("end") Integer end);

    ShopProduct findProductIdAndOwnById(@Param("id") Long id);

	ShopProduct selectShopProductNew(@Param("id")Long id,@Param("status")Integer status);
	ShopProduct selectShopProduct(@Param("shopProductId") Long shopProductId);

    List<Long> getProductIdsByCategoryIds(@Param("categoryIds") List<Long> categoryIdListAll);

    int increaseShowCount(@Param("shopProductId") long shopProductId);

    int increaseFavoriteCount(@Param("shopProductId") long shopProductId);

    int reduceFavoriteCount(@Param("shopProductId") long shopProductId);

    Integer getByStoreId(@Param("storeId") Long storeId);


    /**
     * 获取商品分类ID集合
     *
     * @param productId
     * @return
     */
    List<Long> getCategoryIdsByProductId(long productId);

    /**
     * 批量插入ShopProduct
     * @param shopProductList
     * @return
     */
    int insertShopProductsBatch(@Param("shopProductList") List<ShopProduct> shopProductList);

    List<Map<String, Object>> searchValidShopProductList(@Param("keyWords") String keyWords, @Param("storeId") long storeId, @Param("page") Page<Map<String, Object>> page);


	List<ShopProduct> getNavigationProductList(@Param("tagId")long tagId, @Param("storeId")long storeId, Page<Map<String,Object>> page);


	/**
     * 随机获取门店的一件上架商品,并且库存>0
     *
     * @param query 请求参数封装
     * @return com.jiuyuan.entity.newentity.ShopProduct
     * @author Charlie(唐静)
     * @date 2018/7/3 15:58
     */
    ShopProduct randomProduct(ShopProduct query);

	/**
	 *
	 * @param page page
	 * @param storeId storeId
	 * @param status status
	 * @param soldOut soldOut
	 * @param secondBuyShopProductId 参与描述活动的商品id
	 * @param teamBuyShopProductId 参与活动商品的id
	 * @return java.util.List<com.jiuyuan.entity.newentity.ShopProduct>
	 * @author Charlie
	 * @date 2018/7/13 18:36
	 */
	List<Map<String, Object>> getShopProductAndActivityPage(
    		@Param ("page") Page page,
			@Param ("storeId") Long storeId,
			@Param ("status") int status,
			@Param ("soldOut") int soldOut,
			@Param ("secondBuyShopProductId") Long secondBuyShopProductId,
			@Param ("teamBuyShopProductId")Long teamBuyShopProductId);

    /**
     * 收藏,状态
     *
     * @param shopProductId shopProductId
     * @param memberId memberId
     * @param type type
     * @param storeId storeId
     * @return java.util.List<java.lang.Integer>
     * @author Charlie
     * @date 2019/1/2 22:36
     */
    List<Integer> listFavoriteStatus(@Param("shopProductId") Long shopProductId,
                                     @Param("memberId") Long memberId,
                                     @Param("type") int type,
                                     @Param("storeId") Long storeId);

	/**
	 * 根据产品id 获取
	 * @param shopProductId
	 * @return
	 */
    ShopProduct findProductForFavoriteById(@Param("shopProductId") long shopProductId);

	/**
	 * 查询是否为自营
	 * @param productId
	 * @return
	 */
	Integer findOwnProductByIdOwn(@Param("productId") long productId, @Param("own") Integer own);

	/**
	 * 查询商品
	 * @param shopProductIds
	 * @return
	 */
    List<ShopProduct> listProductForFavoriteByIds(@Param("shopProductIds") List<Long> shopProductIds);

	/**
	 * 查询商品图片
	 * @param productIds
	 * @return
	 */
	List<ProductNew> listImgs(@Param("productIds") List<Long> productIds);

    Map<String,Object> findLiveProductByLiveProductId(@Param("liveProductId") Long liveProductId);
}