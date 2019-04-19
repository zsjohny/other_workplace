package com.jiuy.core.service.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.dao.modelv2.ProductSKUMapper;
import com.jiuy.core.service.ProductService;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.newentity.ShopProduct;

@Service
public class ProductSKUServiceImpl implements ProductSKUService {

	// @Autowired
	// private ShopProductMapper shopProductMapper;

	@Autowired
	private ProductSKUMapper productSKUMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private ProductService productService;

	@Override
	public Map<Long, ProductSKU> skuMapOfIds(Set<Long> skuIds) {
		if (skuIds.size() < 1) {
			return new HashMap<Long, ProductSKU>();
		}
		Map<Long, ProductSKU> productSkuMap = new HashMap<Long, ProductSKU>();
		List<ProductSKU> skus = productSKUMapper.productSkuOfIds(skuIds);

		for (ProductSKU productSKU : skus) {
			productSkuMap.put(productSKU.getId(), productSKU);
		}

		return productSkuMap;
	}

	@Override

	public int getRemainCount(long productId) {
		return productSKUMapper.getRemainCount(productId);
	}

	public Map<Long, Map<String, Object>> getHotSaleMapOfIds(Set<Long> skuIds) {
		if (skuIds.size() < 1) {
			return new HashMap<Long, Map<String, Object>>();
		}
		Map<Long, Map<String, Object>> hotSaleMap = new HashMap<Long, Map<String, Object>>();
		List<Map<String, Object>> hotSaleGroup = productSKUMapper.hotSaleMapOfIds(skuIds);

		for (Map<String, Object> hotSaleItem : hotSaleGroup) {
			hotSaleMap.put(Long.parseLong(hotSaleItem.get("Id").toString()), hotSaleItem);
		}

		return hotSaleMap;

	}

	@Override
	public Map<Long, Integer> remainCountOfProducts(Collection<Long> productIds) {
		if (productIds.size() < 1) {
			return new HashMap<Long, Integer>();
		}

		Map<Long, Integer> remainCountOfProduct = new HashMap<Long, Integer>();
		List<Map<String, Object>> list = productSKUMapper.remainCountOfProducts(productIds);

		for (Map<String, Object> map : list) {
			long productId = (Long) map.get("ProductId");
			int totalCount = Integer.parseInt(map.get("totalCount").toString());
			remainCountOfProduct.put(productId, totalCount);
		}

		return remainCountOfProduct;
	}

	@Override
	public int syncRemainCount(String nos, String conditions) {
		return productSKUMapper.syncRemainCount(nos, conditions);
	}

	@Override
	public Map<Long, List<ProductSKU>> pushedSkusOfProductIdMap() {
		List<ProductSKU> productSKUs = productSKUMapper.getPushedSkus(AdminConstants.PUSH_TO_ERP_WAREHOUSE_ID_LIST);

		Map<Long, List<ProductSKU>> map = new HashMap<Long, List<ProductSKU>>();

		if (productSKUs.size() < 1) {
			return new HashMap<Long, List<ProductSKU>>();
		}

		List<ProductSKU> skus = null;
		long lastProductId = 0;
		for (ProductSKU productSKU : productSKUs) {
			long productId = productSKU.getProductId();
			if (productId != lastProductId) {
				lastProductId = productId;
				skus = new ArrayList<ProductSKU>();
				map.put(lastProductId, skus);
			}

			skus.add(productSKU);
		}

		return map;
	}

	@Override
	public int syncCount() {
		return productSKUMapper.syncCount();
	}

	@Override
	public int unSyncCount() {
		return productSKUMapper.unSyncCount();
	}

	@Override
	public long lastSyncTime() {
		return productSKUMapper.lastSyncTime();
	}

	@Override
	public int updatePushTime(Collection<String> clothesNos) {
		return productSKUMapper.updatePushTime(clothesNos, System.currentTimeMillis());
	}

	@Override
	public Map<Long, ProductSKU> skuByNo(Collection<Long> skuNos) {
		Map<Long, ProductSKU> skuByNo = new HashMap<Long, ProductSKU>();
		List<ProductSKU> productSKUs = skuOfNo(skuNos);

		for (ProductSKU productSKU : productSKUs) {
			skuByNo.put(productSKU.getSkuNo(), productSKU);
		}

		return skuByNo;
	}

	@Override
	public List<ProductSKU> skuOfNo(Collection<Long> skuNos) {
		if (skuNos.size() < 1) {
			return new ArrayList<ProductSKU>();
		}

		return productSKUMapper.skuOfNo(skuNos);
	}

	@Override
	public int updateSkuSaleStatus(Collection<Long> matched, int status, long saleStartTime, long saleEndTime) {
		if (matched.size() < 1) {
			return 0;
		}

		return productSKUMapper.updateSkuSaleStatus(matched, status, saleStartTime, saleEndTime);
	}

	@Override
	public List<ProductSKU> skusOfWarehouse(Collection<Long> warehouseIds) {
		if (warehouseIds.size() < 1) {
			return new ArrayList<ProductSKU>();
		}

		return productSKUMapper.skusOfWarehouse(warehouseIds);
	}

	@Override
	public List<ProductSKU> skusOfWarehouse2(Collection<Long> warehouseIds) {
		if (warehouseIds.size() < 1) {
			return new ArrayList<ProductSKU>();
		}

		return productSKUMapper.skusOfWarehouse2(warehouseIds);
	}

	@Override
	public List<ProductSKU> skusInfo() {
		return productSKUMapper.skusInfo();
	}

	@Override
	public int batchUpdate(Collection<Long> skuNos, int status) {
		if (skuNos.size() < 1) {
			return 0;
		}

		return productSKUMapper.batchUpdate(skuNos, status);
	}

	@Override
	public int batchValidityUpdate(Collection<Long> skuNos, int status) {
		if (skuNos.size() < 1) {
			return 0;
		}

		return productSKUMapper.batchValidityUpdate(skuNos, status);
	}

	@Override
	public int recoverSale(Collection<Long> skuNos) {
		if (skuNos.size() < 1) {
			return 0;
		}
		return productSKUMapper.recoverSale(skuNos);
	}

	@Override
	public Product getProductBySkuId(long skuId) {
		return productSKUMapper.getProductBySkuId(skuId);
	}

	@Override
	public ProductSKU searchById(Long id) {
		return productSKUMapper.searchById(id);
	}

	@Override
	public List<ProductSKU> search(Collection<Long> brandIds, boolean needOnSale) {
		if (brandIds.size() < 1) {
			return new ArrayList<>();
		}
		return productSKUMapper.search(brandIds, needOnSale);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updatePromotionCount(Long skuNo, Integer promotionSaleCount, Integer promotionVisitCount) {
		productSKUMapper.update(skuNo, promotionSaleCount, promotionVisitCount);
		ProductSKU productSKU = productSKUMapper.searchOne(skuNo);
		List<ProductSKU> skus = productSKUMapper.search(productSKU.getProductId(), false);
		int total_promotion_sale_count = 0;
		int total_promotion_visit_count = 0;

		for (ProductSKU item : skus) {
			if (item.getStatus() <= -2) {
				continue;
			}
			total_promotion_sale_count += item.getPromotionSaleCount();
			total_promotion_visit_count += item.getPromotionVisitCount();
		}
		return productMapper.update(productSKU.getProductId(), null, null, total_promotion_sale_count,
				total_promotion_visit_count);
	}

	@Override
	public Map<Long, List<ProductSKU>> onSaleSKUOfProductId(Collection<Long> productIds) {
		List<ProductSKU> productSKUs = productSKUMapper.getOnSaleByProductId(productIds);
		Map<Long, List<ProductSKU>> map = new HashMap<>();
		List<ProductSKU> skus = null;
		long lastProductId = -1;
		for (ProductSKU productSKU : productSKUs) {
			if (productSKU.getProductId() != lastProductId) {
				skus = new ArrayList<>();
				lastProductId = productSKU.getProductId();
				map.put(lastProductId, skus);
			}
			skus.add(productSKU);
		}

		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jiuy.core.service.product.ProductSKUService#exportskudata(java.lang.
	 * String)
	 */
	@Override
	public List<Map<String, Object>> exportskudata(String warehouseIds, Collection<Long> seasonIds) {
		return productSKUMapper.exportskudata(warehouseIds, seasonIds);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteVideo(long productId) {
		productSKUMapper.deleteVideo(productId);
		List<ShopProduct> shopProductList = productService.getShopProductByProductId(productId);
		for (ShopProduct shopProduct : shopProductList) {
			productSKUMapper.deleteShopProductVideo(shopProduct.getId());
		}
	}

	/**
	 * 确认该商品Id的是否下架
	 */
	@Override
	public boolean comfirmSaleEndStatus(Long id) {
		// 获取该商品Id的上架的所有ProductSKU
		List<ProductSKU> list = productSKUMapper.search(id, true);
		// 如果该list有元素存在说明，不能下架
		if (list == null || list.size() <= 0) {
			return true;
		}
		return false;
	}
	// /**
	// * 同步下架该ProductId商品
	// */
	// @Override
	// public void saleEndProduct(Long id) {
	// //为所有上架了该ProductId商品的商家更改状态
	// int i = shopProductMapper.underCarrigeProductByProductId(id);
	// }

}
