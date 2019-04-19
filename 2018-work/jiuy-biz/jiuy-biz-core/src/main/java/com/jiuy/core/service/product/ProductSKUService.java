package com.jiuy.core.service.product;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductSKU;

public interface ProductSKUService {

	Map<Long, ProductSKU> skuMapOfIds(Set<Long> skuIds);

	int getRemainCount(long productId);

	Map<Long, Map<String, Object>> getHotSaleMapOfIds(Set<Long> skuIds);

	Map<Long, Integer> remainCountOfProducts(Collection<Long> allProductIds);

	int syncRemainCount(String string, String string2);

	Map<Long, List<ProductSKU>> pushedSkusOfProductIdMap();

	int syncCount();

	int unSyncCount();

	long lastSyncTime();

	int updatePushTime(Collection<String> clothesNos);

	Map<Long, ProductSKU> skuByNo(Collection<Long> skuNos);

	List<ProductSKU> skuOfNo(Collection<Long> nos);

	int updateSkuSaleStatus(Collection<Long> matched, int status, long saleStartTime, long saleEndTime);

	List<ProductSKU> skusOfWarehouse(Collection<Long> erpWarehouseIdList);

	List<ProductSKU> skusOfWarehouse2(Collection<Long> erpWarehouseIdList);

	List<ProductSKU> skusInfo();

	int batchUpdate(Collection<Long> skuNos, int status);

	int batchValidityUpdate(Collection<Long> skuNos, int status);

	int recoverSale(Collection<Long> saleSkuNos);

	Product getProductBySkuId(long id);

	ProductSKU searchById(Long productSKUId);

	List<ProductSKU> search(Collection<Long> brandIds, boolean needOnSale);

	int updatePromotionCount(Long id, Integer promotionSaleCount, Integer promotionVisitCount);

	Map<Long, List<ProductSKU>> onSaleSKUOfProductId(Collection<Long> productIds);

	List<Map<String, Object>> exportskudata(String warehouseIds, Collection<Long> seasonIds);

	void deleteVideo(long productId);

	boolean comfirmSaleEndStatus(Long id);

	// void saleEndProduct(Long id);

}