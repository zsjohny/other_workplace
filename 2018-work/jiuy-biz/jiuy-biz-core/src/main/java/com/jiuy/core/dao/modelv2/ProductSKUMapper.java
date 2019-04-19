package com.jiuy.core.dao.modelv2;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuy.core.meta.product.ProductBoutique;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.product.ProductSKUVO;
import com.jiuyuan.entity.query.PageQuery;

public interface ProductSKUMapper {

	int batchAdd(List<ProductSKUVO> skus);

	int removeProductSKUSByProductIds(Collection<Long> ids);

	int batchUpdate(List<ProductSKUVO> skuvosForUpdate);

	int addProductSKUCountById(long skuId, int totalCount);

	int getRemainCountById(long id);

	boolean getSkuStatus(long productId);

	List<ProductSKU> srchSkuInfo(PageQuery pageQuery, long id, long season, long year, String name,
			String clothesNumber, int remainCountMin, int remainCountMax, long skuId, String brandName, int sortType,
			String saleStatus, long parentCategoryId, long categoryId, int skuStatus, int validity, int type,
			List<Long> warehouseIds, int isBoutique);

	int srchSkuInfoCount(long id, long season, long year, String name, String clothesNumber, int remainCountMin,
			int remainCountMax, long skuId, String brandName, String saleStatus, long parentCategoryId, long categoryId,
			int skuStatus, int validity, int type, List<Long> warehouseIds);

	int insertSKUs(String name, long productId, Collection<String> propertyIds, double marketPrice, double costPrice,
			double weight, String position, String clothesNumber, long warehouseId, int remainKeepTime, long brandId,
			int status);

	int uptProductSku(long id, String name, double marketPrice, double costPrice, double weight, String position,
			int sort, int remainCount, int remainCount2, int remainKeepTime, long skuNo, int remainCountLock,
			long remainCountStartTime, long remainCountEndTime, int isRemainCountLock);

	int uptProductSku(long id, int status, long saleStartTime, long saleEndTime);

	List<ProductSKU> productSkuOfIds(Collection<Long> skuIds);

	List<ProductSKU> loadAllSkus();

	int getRemainCount(long productId);

	List<Map<String, Object>> hotSaleMapOfIds(Set<Long> skuIds);

	List<ProductSKU> getProductSKUs(long productId, String clothesNum);

	List<ProductSKU> productSkuOfBlurClothesNo8SkuNo(String clothesNum, String skuNo);

	List<Map<String, Object>> remainCountOfProducts(Collection<Long> productIds);

	int deactivateProductSKUByIds(Collection<Long> ids);

	int syncRemainCount(String nos, String conditions);

	List<ProductSKU> getPushedSkus(Collection<Long> erpWarehouseIdList);

	int syncCount();

	int unSyncCount();

	long lastSyncTime();

	int updatePushTime(Collection<String> clothesNos, long time);

	List<ProductSKU> skuOfNo(Collection<Long> skuNos);

	List<ProductSKU> skusOfClothesNos(Collection<String> createList);

	/**
	 * @param productIds
	 * @param brandId
	 */
	int updateByProductId(Collection<Long> productIds, long brandId);

	int updateByProductId(Collection<Long> productIds, int status);

	int updateByProductId(Collection<Long> productIds, String clothesNumber, long warehouseId, long warehouseId2,
			long setWarehouseId2, String name, long brandId, double cash, int weight, long price, long marketPrice);

	int updateSkuSaleStatus(Collection<Long> matched, int status, long saleStartTime, long saleEndTime);

	List<ProductSKU> skusOfWarehouse(Collection<Long> warehouseIds);

	List<ProductSKU> skusOfWarehouse2(Collection<Long> warehouseIds);

	List<ProductSKU> skusInfo();

	int batchUpdate(Collection<Long> skuNos, int status);

	List<ProductSKU> skusOfProductIds(Collection<Long> createList);

	int batchValidityUpdate(Collection<Long> skuNos, int status);

	int getProductSaleStatus(long productId);

	int recoverSale(Collection<Long> skuNos);

	Product getProductBySkuId(long skuId);

	List<ProductSKU> search(Long productId, boolean needOnSale);

	ProductSKU searchById(Long id);

	List<ProductSKU> search(Collection<Long> brandIds, boolean needOnSale);

	int update(Long skuNo, Integer promotionSaleCount, Integer promotionVisitCount);

	ProductSKU searchOne(Long skuNo);

	List<ProductSKU> searchPrice(Integer minCash, Integer maxCash);

	List<ProductSKU> getOnSaleByProductId(Collection<Long> productIds);

	List<ProductSKU> getProductSKUByProductId(long productId);

	List<Long> getSaleSkuNos(long startTime, long endTime);

	List<Long> getSuccessSKUNo(long startTime, long endTime);

	int updateRemainCountSecond(long id, int buyCount);

	int updateRemainCount(long id, int buyCount);

	List<Map<String, Object>> exportskudata(String warehouseIds, Collection<Long> seasonIds);

	int searchBoutiqueProductCount(Long id, String name, String clothesNumber, String brandName, Integer skuStatus,
			Integer parentCategoryId, Integer categoryId, Integer sortType, int vip);

	List<ProductBoutique> searchBoutiqueProductInfo(PageQuery pageQuery, Long id, String name, String clothesNumber,
			String brandName, Integer skuStatus, Integer parentCategoryId, Integer categoryId, Integer sortType,
			int vip);

	int updateBoutiqueProduct(Long productId, Integer status, Long updateTime);

	int insertBoutiqueProduct(Long productId, Integer status, Long createTime, Long updateTime);

	int udpateBoutiqueProductPrice(String productId, Double price);

	int udpateBoutiqueSetVip(long productId, int vip);

	int updateSaleStartTime(String productId, long updateTime);

	ProductBoutique getBoutiqueProductByProductId(String productId);

	int updateBoutiqueProductUpdateTime(String productId, Long updateTime);

	int udpateProductWholeSaleCash(String productId, Double price);

	int deleteVideo(long productId);

	int deleteShopProductVideo(long shopProductId);

	List<Long> getProductIdBySKUIDS(List<Long> productSKUIds);

	List<Long> getProductIdBySKUID(Long productSKUId);

	List<Long> getStoreIdsByProductId(Long productId);

	/**
	 * 获取上架指定商品的商家ID集合
	 * 
	 * @param productId
	 * @return
	 */

	List<Long> getOnShelfStoreIdsByProductId(Long productId);

	List<Long> getProductIdBySKUNOS(List<Long> ids);

}
