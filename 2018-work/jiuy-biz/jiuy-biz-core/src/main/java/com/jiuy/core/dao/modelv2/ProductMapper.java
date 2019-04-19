package com.jiuy.core.dao.modelv2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
//import org.htmlparser.lexer.Page;

import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.product.DeductProductVO;
import com.jiuyuan.entity.product.ProductShare;
import com.jiuyuan.entity.product.ProductWindow;
import com.jiuyuan.entity.query.PageQuery;

public interface ProductMapper {

    public Product getPromotionProductById(long productId, String promoSetting);
    
    public Product getProductById(long productId);
    
    public ProductShare getProductShareByProId(long productId);

    public List<Product> getProductByCategoryId(@Param("categoryId") long categoryId, @Param("pageQuery") PageQuery pageQuery);

    public List<Product> getProductByCategoryIds(@Param("categoryIds") Collection<Long> categoryIds,
                                          @Param("pageQuery") PageQuery pageQuery);
    
    public long add(Product product);

	public List<Product> loadList(@Param("pageQuery")PageQuery pageQuery, @Param("status")Integer status);

    public int getProductCount(@Param("pageQuery")PageQuery query, @Param("status")Integer prodStatus);

    public int deactivateProductByIds(Collection<Long> ids);

    /**
     * 更新产品
     * author:Jeff.Zhan
     * @param pro 产品的id以及各个更新后的属性
     * @return
     * @throws Exception 
     */
    public int updateProduct(Product prod) throws Exception;
    /**
     * 更新产品分享
     * author:chen
     * @param pro 产品分享信息
     * @return
     * @throws Exception 
     */
    public int updateProductShare(ProductShare productShare) throws Exception;

    /**
     * 校验衣服款号的唯一性
     * author:Jeff.Zhan
     * @param clothesNumber
     * @return
     */
	public List<Integer> chkProductClothesNum(String clothesNumber);

	/**
	 * 在修改产品页面 根据商品id 获得其所属分类
	 * author:Jeff.Zhan
	 * @param productId
	 * @return
	 */
	public List<Map<String, Object>> getCategoriesInfo(long productId);

	/**
	 * 在修改产品页面 根据商品id 获得其上架年份
	 * author:Jeff.Zhan
	 * @param productid
	 * @return
	 */
	public List<Map<String, Object>> getPropertyInfo(long productid);

	/**
	 * 在修改页面获取商品详情
	 * @param productid
	 * @return
	 */
	public List<String> getDescription(long productid);

	/**
	 * 获取分页中一段商品id
	 * @param query
	 * @param prodStatus
	 * @return
	 */
	public List<Integer> loadproductIdList(PageQuery query, Integer prodStatus);


	/**
	 * 修改产品状态为下架状态
	 */
    public int updateProductStatusOut();

	public int updateProductBrandId(int brandId);

	public ProductWindow searchProWinByClothesNum(String clothesNum);

	public List<Product> getIdsByRootPath(String oldImgRootPath);

	public List<Map<String, Object>> searchOverview(long brandId, String clothesNo, PageQuery pageQuery, String orderSql, String saleStatusSql, int skuStatusIntValue);

	public int searchOverviewCount(long brandId, String clothesNo, String saleStatusSql, int skuStatus);

	public int getMaxWeight();

	public boolean isOnSale(long productId);

	public List<Product> getByClothesNums(Collection<String> clothesNums);

	public List<Product> productOfIds(Collection<Long> productIds);

	public List<Product> getAllProducts();

    public long insertProduct(Product product);

	public Product getBySkuId(long skuId);

	List<String> getRemark(long productid);

	public List<Product> productsOfRestrctIds(Collection<Long> restrictionIds);
	
	public int batchUpdate(Long restrictId, Long vCategoryId, Long subscriptId, Collection<Long> productIds);

	public int batchRemoveRestrictId(long restrictId);

	public List<Product> getByWarehouse(Collection<Long> erpWarehouseIdList);

	public List<Product> checkInfoCompleted(long productId);

	public int updateWarehouseId(Collection<String> createList, long warehouseId);

	public List<Product> search(String clothesNo, Long brandId);

	public int update(Long id, Integer status, Integer weight, Integer promotionSaleCount, Integer promotionVisitCount);

	public int update(Long id, Integer status, Long saleStartTime, Long saleEndTime);
	
	public int update(Long id, Long brandId);

	public void executeRcmdSttstcs();
	
	public int batchUpdateSubscriptId(Long restrictId, Collection<Long> productIds);
	
	public int batchRemoveSubscriptId(long subscriptId);
	
	public List<Product> productsOfSubscriptIds(Collection<Long> subscriptIds);
	/**
	 * 修改商品的批发价
	 * @param prod
	 * @return
	 */
	public int updateProductWholeSaleCash(Product prod) throws Exception;

	public List<Product> getBySeasonOnSale();

	public List<Product> searchPrice(Integer minCash, Integer maxCash);

	public int updateProductPromotionInfo(ArrayList<Long> productIds, double promotionCash, int promotionJiuCoin,
			long promotionStartTime, long promotionEndTime, int promotionSetting);

	public int updateProductPromotionInfoByDiscount(ArrayList<Long> productIds, double discount,
			long promotionStartTime, long promotionEndTime, int promotionSetting);

	public List<Product> getQianmiProduct();

	/**
	 * @param categoryId
	 */
	public int batchDeleteVCategory(long categoryId);

	public List<Product> getByWarehouseId(Long warehouseId);

	public List<Product> getByWarehouseIds(Collection<Long> warehouseIds);

	public List<Product> getByNotInWarehouseIds(List<Long> warehouseIds);

	/**
	 * @return
	 */
	public List<Map<String, Object>> productMap(Collection<Long> seasonIds);
	
	/**
	 * 查询可以玖币抵扣的商品
	 * @return
	 */
	public List<DeductProductVO> searchJiuCoinDeductProduct(PageQuery pageQuery, long id,long season,long year,String name,
			String clothesNumber,String brandName,String saleStatus,int parentCategoryId,long categoryId,int skuStatus);
	
	public int searchJiuCoinDeductProductCount(long id, long season, long year, String name, String clothesNumber,
			String brandName, String saleStatus, int parentCategoryId, long categoryId, int skuStatus);
	
	public int updateProductDeductPercent(Collection<Long> productIds,double deductPercent);
	
	public List<Product> getProductByName(String name);

	public int updateShopProduct(ShopProduct shopProduct);

	public List<ShopProduct> getShopProductByProductId(long productId);

	public int soldOutShopProductByProductId(Long productId);
	/**
	 * 下架商家商品
	 * @param shopProductId
	 * @return
	 */
	public int soldOutShopProduct(Long shopProductId);

	public List<ShopProduct>  getShopProduct(long storeId, long productId);

	public long addShopProduct(ShopProduct shopProduct);

	public int updateProductSaleCount(long productId, int count);
}
