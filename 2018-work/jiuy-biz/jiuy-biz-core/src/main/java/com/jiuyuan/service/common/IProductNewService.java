package com.jiuyuan.service.common;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.ProductDetail;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.web.help.JsonResponse;

public interface IProductNewService {
	 /**
     * 获取平台商品状态
     * @param
     * @return  平台商品状态:0已上架、1已下架、2已删除
     */
	String getPlatformProductState(long productId) ;
	 /**
     * 获取平台商品状态
     * @param product
     * @return  平台商品状态:0已上架、1已下架、2已删除
     */
	String getPlatformProductState(ProductNew product) ;

	ProductNew getProductById(long productId);

	void productAuditPass(long productId);

	boolean checkClothesNumberUsable(long supplierId, long productId, String clothesNumber);

	List<ProductNew> getSearchProductList(Page<ProductNew> page, String keyword, String orderByField, boolean asc);

	ProductDetail getProductDetail(long productId);

	List<ProductNew> getSearchProductList(long supplierId, String clothesNumber, String productName, long state,
			long upSoldTimeBegin, long upSoldTimeEnd, double priceBegin, double priceEnd, int salesCountBegin,
			int salesCountEnd, Page<Map<String, Object>> page);

	/* (non-Javadoc)
		 * @see com.supplier.service.ProductSupplierService2#upSoldProduct(long)
		 */
	//	@Transactional(rollbackFor = Exception.class)
	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IProductNewService#upSoldProduct(long)
	 */
	void upSoldProduct(long productId);

	void downSoldProduct(long productId);

	void productSubmitAudit(long productId);

	Map<Long, ProductNew> productMapOfIds(Set<Long> productIds);


	ProductDetail getProductDatailByProductId(long productId);

	ProductNew getProductByClothesNumber(String clothesNumber);

	void productAuditNoPass(long productId, String noPassReason);

	void updateSaleCount(long productId, int totalBuyCount);

	List<Map<String, Object>> getSearchProductListNew(long supplierId,Long timingPutawayTimeFloor, Long timingPutawayTimeCeil, String clothesNumber, String productName, long state,
			long upSoldTimeBegin, long upSoldTimeEnd, double priceBegin, double priceEnd, int salesCountBegin,
			int salesCountEnd, Page<Map<String, Object>> page, int totalSkuCountBegin, int totalSkuCountEnd, int orderType);

	void waitProduct(long productId);


	List<Map<String, Object>> selectProductPage(BigDecimal memberLadderPriceFloor, BigDecimal memberLadderPriceCeil,String productIds, String brandName, int state, double minLadderPriceStart,
												double minLadderPriceEnd, String name, String categoryIds, String productSeq,
												long badgeStatus, Page<Map<String, Object>> page);

	int bindBadgeProduct(long badgeId ,String badgeImage,String badgeName, String productIds);

	int clearProductBadge(String productIds);

	int bindProductBadgeCondition(long badgeId, String badgeImage,String badgeName, String productIds, String brandName,  int state,
			double minLadderPriceStart, double minLadderPriceEnd, String name, String categoryIds, String productSeq,
			long badgeStatus);

	int clearProductBadgeCondition(String productIds, String brandName, int state, double minLadderPriceStart,
			double minLadderPriceEnd, String name, String categoryIds, String clothesNumber, long badgeStatus);

	int getWillClearProductCount(String productIds, String brandName, int state, double minLadderPriceStart,
			double minLadderPriceEnd, String name, String categoryIds, String clothesNumber, long badgeStatus);

	void updateProductBrandInfo(long brandId, String brandName, String brandLogo);

	List<Map<String, Object>> getAllProductIds();

	List<ProductNew> getBrandProductList(long storeId, long brandId, int type, String keyWord, Page<ProductNew> page);

	/**
	 * 获取某个品牌有多少个商品
	 * @param brandId
	 * @param keyWord
	 * @date:   2018/5/23 14:39
	 * @author: Aison
	 */
	long getBrandProducCount(long brandId, String keyWord);


	int getBrandProductListCount(long storeId, long brandId);

	List<Map<String, Object>> collocationList(String productIds, long storeId);

	Map<String, Object> getSkuList(long productId);

	boolean validateRank(long supplierId, int rank);

	void setRank(ProductNew newProduct);

	int getBrandProductListCount(long storeId, long brandId, int type, String keyWord);

	public List<ProductNew> getProductNewListByTagId(int limit, int offset, int tagId);

	public int getProductNewListByTagIdCount(int tagId);

	/**
	 * 获取某一商品的库存总数
	 * @param productId
	 * @return int
	 * @auther Charlie(唐静)
	 * @date 2018/5/30 17:03
	 */
	int countSkusRemain(Long productId);

	/**
	 * job回调,商品定时上架
	 * @param supplierId
	 * @param productId
	 * @param token 调用服务的认证信息
	 * @return void
	 * @auther Charlie(唐静)
	 * @date 2018/5/30 18:28
	 */
	void productPutawayFromJob(Long supplierId, Long productId, String token);

	/**
	 * 查询根据商品ids查询商品
	 * @param ids
	 * @return java.util.List<com.jiuyuan.entity.newentity.ProductNew>
	 * @auther Charlie(唐静)
	 * @date 2018/6/10 16:01
	 */
	List<ProductNew> selectByIds(List<Long> ids);


	/**
	 * 通过productId 查询product信息
	 *
	 * @param productIds
	 * @return Map key:productId, value object
	 * @auther Charlie(唐静)
	 * @date 2018/6/11 17:46
	 */
	Map<Long,ProductNew> productByIds(Set<Long> productIds);

	/**
	 * 活动商品专场页
	 *
	 * @param pageQuery 分页
	 * @return java.util.List<com.jiuyuan.entity.newentity.ProductNew>
	 * @author Charlie
	 * @date 2018/8/13 16:03
	 */
	List<ProductNew> memberProductSpecial(Page<ProductNew> pageQuery);


	/**
	 * 设置商品会员价
	 *
	 * @param productId 商品id
	 * @param memberPriceJson 会员价json字符串
	 * @param memberLevel 会员等级
	 * @author Charlie
	 * @date 2018/8/14 8:53
	 */
    void updateMemberPrice(Long productId, String memberPriceJson, Integer memberLevel);

    /**
     *	重置排名
	 * @param
     * @return
     * @author hyf
     * @date 2018/8/29 16:32
     */
    JsonResponse resetRank();
}