package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuy.core.dao.modelv2.ProductSKUMapper;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuy.core.meta.product.ProductBoutique;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.product.ProductSKUVO;
import com.jiuyuan.entity.query.PageQuery;

public class ProductSKUMapperSqlImpl extends SqlSupport implements ProductSKUMapper {

	@Override
	public List<ProductSKU> getProductSKUs(long productId, String clothesNum) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productId", productId);
		params.put("clothesNum", clothesNum);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getProductSKUs", params);
	}

	@Override
	public int batchAdd(List<ProductSKUVO> skus) {
		if (null == skus || skus.size() == 0) {
			return 0;
		}
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.insertBatch", skus);
	}

	@Override
	public int removeProductSKUSByProductIds(Collection<Long> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		return getSqlSession().delete("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.deleteByProductIds", params);
	}

	@Override
	public int batchUpdate(List<ProductSKUVO> skuvosForUpdate) {
		for (ProductSKUVO psvo : skuvosForUpdate) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("psvo", psvo);
			getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.updateSku", params);
		}
		return 0;
	}

	@Override
	public int addProductSKUCountById(long skuId, int totalCount) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("skuid", skuId);
		params.put("remaincount", totalCount);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.addSKUCount", params);
	}

	@Override
	public int getRemainCountById(long id) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productId", id);

		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getRemainCountById",
				params);
	}

	@Override
	public boolean getSkuStatus(long productId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productId", productId);
		int count = getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getSkuStatus",
				params);

		return count > 0 ? false : true;
	}

	@Override
	public List<ProductSKU> srchSkuInfo(PageQuery pageQuery, long id, long season, long year, String name,
			String clothesNumber, int remainCountMin, int remainCountMax, long skuId, String brandName, int sortType,
			String saleStatus, long parentCategoryId, long categoryId, int skuStatus, int validity, int type,
			List<Long> warehouseIds, int isBoutique) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("pageQuery", pageQuery);
		params.put("name", name);
		params.put("clothesNumber", clothesNumber);
		params.put("remainCountMin", remainCountMin);
		params.put("remainCountMax", remainCountMax);
		params.put("skuId", skuId);
		params.put("brandName", brandName);
		params.put("sortType", sortType);
		params.put("saleStatusSql", saleStatus);
		params.put("parentCategoryId", parentCategoryId);
		params.put("categoryId", categoryId);
		params.put("skuStatus", skuStatus);
		params.put("id", id);
		params.put("validity", validity);
		params.put("type", type);
		params.put("isBoutique", isBoutique);

		if (warehouseIds == null || warehouseIds.size() == 0) {
			warehouseIds = null;
		}
		params.put("warehouseIds", warehouseIds);

		String propertySQL = assembleSQL(season, year, parentCategoryId, categoryId);
		params.put("properties", propertySQL);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.srchSkuInfo", params);
	}

	@Override
	public int srchSkuInfoCount(long id, long season, long year, String name, String clothesNumber, int remainCountMin,
			int remainCountMax, long skuId, String brandName, String saleStatus, long parentCategoryId, long categoryId,
			int skuStatus, int validity, int type, List<Long> warehouseIds) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("name", name);
		params.put("clothesNumber", clothesNumber);
		params.put("remainCountMin", remainCountMin);
		params.put("remainCountMax", remainCountMax);
		params.put("skuId", skuId);
		params.put("brandName", brandName);
		params.put("saleStatusSql", saleStatus);
		params.put("parentCategoryId", parentCategoryId);
		params.put("categoryId", categoryId);
		params.put("skuStatus", skuStatus);
		params.put("id", id);
		params.put("validity", validity);
		params.put("type", type);

		if (warehouseIds == null || warehouseIds.size() == 0) {
			warehouseIds = null;
		}
		params.put("warehouseIds", warehouseIds);

		String propertySQL = assembleSQL(season, year, parentCategoryId, categoryId);
		params.put("properties", propertySQL);

		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.srchSkuInfoCount", params);
	}

	private String assembleSQL(long season, long year, long parentCategoryId, long categoryId) {
		StringBuilder properties = new StringBuilder();

		int k = 0;

		if (season != -1) {
			properties.append(
					" union all SELECT distinct ProductId FROM yjj_ProductProperty where PropertyNameId=9 and PropertyValueId=");
			properties.append(season);
			k++;
		}
		if (year != -1) {
			properties.append(
					" union all SELECT distinct ProductId FROM yjj_ProductProperty where PropertyNameId=10 and PropertyValueId=");
			properties.append(year);
			k++;
		}
		if (parentCategoryId != -1) {
			properties.append(
					" union all SELECT distinct ProductId FROM yjj_ProductCategory where Status > -1 and (CategoryId in (SELECT Id FROM yjj_Category WHERE ParentId=");
			properties.append(parentCategoryId + ") OR CategoryId=" + parentCategoryId + ")");
			k++;
		}
		if (categoryId != -1) {
			properties.append(
					" union all SELECT distinct ProductId FROM yjj_ProductCategory where Status > -1 and  CategoryId=");
			properties.append(categoryId);
			k++;
		}

		String propertySQL = null;
		if (properties.length() > 11)
			propertySQL = "select ProductId FROM  (" + properties.substring(11).toString()
					+ ") t1 group by ProductId having count(ProductId)=" + k;

		return propertySQL;
	}

	@Override
	public int insertSKUs(String name, long productId, Collection<String> propertyIds, double marketPrice,
			double costPrice, double weight, String position, String clothesNumber, long warehouseId,
			int remainKeepTime, long brandId, int status) {

		Map<String, Object> params = new HashMap<String, Object>();
		long time = System.currentTimeMillis();
		params.put("name", name);
		params.put("productId", productId);
		params.put("propertyIds", propertyIds);
		params.put("marketPrice", marketPrice);
		params.put("costPrice", costPrice);
		params.put("weight", weight);
		params.put("position", position);
		params.put("clothesNumber", clothesNumber);
		params.put("warehouseId", warehouseId);
		params.put("remainKeepTime", remainKeepTime);
		params.put("brandId", brandId);
		params.put("createTime", time);
		params.put("updateTime", time);
		params.put("time", time);
		params.put("status", status);

		getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.insertSKUsUpdate", params);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.updateSKUsSkuNo", params);
	}

	@Override
	public int uptProductSku(long id, String name, double marketPrice, double costPrice, double weight, String position,
			int sort, int remainCount, int remainCount2, int remainKeepTime, long skuNo, int remainCountLock,
			long remainCountStartTime, long remainCountEndTime, int isRemainCountLock) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", id);
		params.put("name", name);
		params.put("marketPrice", marketPrice);
		params.put("costPrice", costPrice);
		params.put("weight", weight);
		params.put("position", position);
		params.put("sort", sort);
		params.put("remainCount", remainCount);
		params.put("remainCount2", remainCount2);
		params.put("remainKeepTime", remainKeepTime);
		params.put("skuNo", skuNo);
		params.put("remainCountLock", remainCountLock);
		params.put("remainCountStartTime", remainCountStartTime);
		params.put("remainCountEndTime", remainCountEndTime);
		params.put("isRemainCountLock", isRemainCountLock);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.uptProductSku", params);
	}

	@Override
	public int uptProductSku(long id, int status, long saleStartTime, long saleEndTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", id);
		params.put("status", status);
		params.put("saleStartTime", saleStartTime);
		params.put("saleEndTime", saleEndTime);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.uptProductSku", params);
	}

	@Override
	public List<ProductSKU> productSkuOfIds(Collection<Long> skuIds) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("skuIds", skuIds);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.productSkuOfIds", params);
	}

	@Override
	public List<ProductSKU> loadAllSkus() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.loadAllSkus");
	}

	@Override
	public int getRemainCount(long productId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productId", productId);

		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getRemainCount", params);
	}

	public List<Map<String, Object>> hotSaleMapOfIds(Set<Long> skuIds) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("skuIds", skuIds);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.hotSaleMapOfIds", params);
	}

	@Override
	public List<ProductSKU> productSkuOfBlurClothesNo8SkuNo(String clothesNum, String skuNo) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("clothesNum", clothesNum);
		params.put("skuNo", skuNo);

		return getSqlSession().selectList(
				"com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.productSkuOfBlurClothesNo8SkuNo", params);
	}

	@Override
	public List<Map<String, Object>> remainCountOfProducts(Collection<Long> productIds) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productIds", productIds);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.remainCountOfProducts",
				params);
	}

	@Override
	public int deactivateProductSKUByIds(Collection<Long> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.deactivateProductSKUByIds",
				params);
	}

	@Override
	public int syncRemainCount(String nos, String conditions) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("skuNos", nos);
		params.put("conditions", conditions);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.syncRemainCount", params);
	}

	@Override
	public List<ProductSKU> getPushedSkus(Collection<Long> erpWarehouseIdList) {
		Map<String, Object> params = new HashMap<>();

		params.put("warehouseIds", erpWarehouseIdList);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getPushedSkus", params);
	}

	@Override
	public int syncCount() {
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.syncCount");
	}

	@Override
	public int unSyncCount() {
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.unSyncCount");
	}

	@Override
	public long lastSyncTime() {
		Long time = getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.lastSyncTime");
		if (time == null) {
			return 0;
		}
		return time;
	}

	@Override
	public int updatePushTime(Collection<String> clothesNos, long time) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("time", time);
		params.put("clothesNos", clothesNos);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.updatePushTime", params);
	}

	@Override
	public List<ProductSKU> skuOfNo(Collection<Long> skuNos) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("skuNos", skuNos);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.skuOfNo", params);
	}

	@Override
	public List<ProductSKU> skusOfClothesNos(Collection<String> clothesNos) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("clothesNos", clothesNos);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.skusOfClothesNos",
				params);
	}

	@Override
	public int updateByProductId(Collection<Long> productIds, int status) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productIds", productIds);
		params.put("status", status);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.updateByProductId", params);
	}

	@Override
	public int updateByProductId(Collection<Long> productIds, String clothesNumber, long warehouseId, long warehouseId2,
			long setWarehouseId2, String name, long brandId, double cash, int weight, long price, long marketPrice) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productIds", productIds);
		params.put("clothesNumber", clothesNumber);
		params.put("warehouseId", warehouseId);
		params.put("warehouseId2", warehouseId2);
		params.put("setWarehouseId2", setWarehouseId2);
		params.put("name", name);
		params.put("brandId", brandId);
		params.put("cash", cash);
		params.put("weight", weight);
		params.put("price", price);
		params.put("marketPrice", marketPrice);
		params.put("pushTime", 0);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.updateByProductId", params);
	}

	@Override
	public int updateSkuSaleStatus(Collection<Long> matched, int status, long saleStartTime, long saleEndTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("skus", matched);
		params.put("status", status);
		params.put("saleStartTime", saleStartTime);
		params.put("saleEndTime", saleEndTime);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.updateSkuSaleStatus", params);
	}

	@Override
	public List<ProductSKU> skusOfWarehouse(Collection<Long> warehouseIds) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("warehouseIds", warehouseIds);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.skusOfWarehouse", params);
	}

	@Override
	public List<ProductSKU> skusOfWarehouse2(Collection<Long> warehouseIds) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("warehouseIds", warehouseIds);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.skusOfWarehouse2",
				params);
	}

	@Override
	public int batchUpdate(Collection<Long> skuNos, int status) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("skuNos", skuNos);
		params.put("status", status);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.batchUpdate", params);
	}

	@Override
	public List<ProductSKU> skusOfProductIds(Collection<Long> productIds) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productIds", productIds);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.skusOfProductIds",
				params);
	}

	@Override
	public int batchValidityUpdate(Collection<Long> skuNos, int status) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("skuNos", skuNos);
		params.put("status", status);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.batchValidityUpdate", params);
	}

	@Override
	public int getProductSaleStatus(long productId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productId", productId);

		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getProductSaleStatus",
				params);
	}

	@Override
	public int recoverSale(Collection<Long> skuNos) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("skuNos", skuNos);
		params.put("now", System.currentTimeMillis());

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.recoverSale", params);
	}

	@Override
	public Product getProductBySkuId(long skuId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("skuId", skuId);

		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getBySkuId", params);
	}

	@Override
	public List<ProductSKU> search(Long productId, boolean needOnsale) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productId", productId);
		params.put("onSale", needOnsale ? 0 : null);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.search", params);
	}

	@Override
	public ProductSKU searchById(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", id);

		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.searchById", params);
	}

	@Override
	public List<ProductSKU> search(Collection<Long> brandIds, boolean needOnsale) {
		Map<String, Object> params = new HashMap<>();

		params.put("brandIds", brandIds);
		params.put("onSale", needOnsale ? 0 : null);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.search", params);
	}

	@Override
	public int update(Long skuNo, Integer promotionSaleCount, Integer promotionVisitCount) {
		Map<String, Object> params = new HashMap<>();

		params.put("skuNo", skuNo);
		params.put("promotionSaleCount", promotionSaleCount);
		params.put("promotionVisitCount", promotionVisitCount);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.update", params);
	}

	@Override
	public ProductSKU searchOne(Long skuNo) {
		Map<String, Object> params = new HashMap<>();

		params.put("skuNo", skuNo);

		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.searchOne", params);
	}

	@Override
	public List<ProductSKU> searchPrice(Integer minCash, Integer maxCash) {
		Map<String, Object> params = new HashMap<>();

		params.put("minCash", minCash);
		params.put("maxCash", maxCash);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.searchPrice", params);
	}

	@Override
	public List<ProductSKU> getOnSaleByProductId(Collection<Long> productIds) {
		Map<String, Object> params = new HashMap<>();

		params.put("productIds", productIds);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getOnSaleByProductId",
				params);
	}

	@Override
	public List<ProductSKU> getProductSKUByProductId(long productId) {
		Map<String, Object> params = new HashMap<>();

		params.put("productId", productId);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getProductSKUByProductId",
				params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jiuy.core.dao.modelv2.ProductSKUMapper#updateByProductId(long,
	 * long)
	 */
	@Override
	public int updateByProductId(Collection<Long> productIds, long brandId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productIds", productIds);
		params.put("brandId", brandId);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.updateByProductId", params);
	}

	@Override
	public List<Long> getSaleSkuNos(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("startTime", startTime);
		params.put("endTime", endTime);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getSaleSkuNos", params);
	}

	@Override
	public List<Long> getSuccessSKUNo(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("startTime", startTime);
		params.put("endTime", endTime);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getSuccessSKUNo", params);
	}

	@Override
	public int updateRemainCountSecond(long id, int buyCount) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", id);
		params.put("buyCount", buyCount);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.updateRemainCountSecond",
				params);
	}

	@Override
	public int updateRemainCount(long id, int buyCount) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", id);
		params.put("buyCount", buyCount);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.updateRemainCount", params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jiuy.core.dao.modelv2.ProductSKUMapper#exportskudata(java.lang.
	 * String, java.util.Collection)
	 */
	@Override
	public List<Map<String, Object>> exportskudata(String warehouseIds, Collection<Long> seasonIds) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("warehouseIds", warehouseIds);
		params.put("seasonIds", seasonIds);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.exportskudata", params);
	}

	@Override
	public int searchBoutiqueProductCount(Long id, String name, String clothesNumber, String brandName,
			Integer skuStatus, Integer parentCategoryId, Integer categoryId, Integer sortType, int vip) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", id);
		params.put("name", name);
		params.put("clothesNumber", clothesNumber);
		params.put("brandName", brandName);
		params.put("skuStatus", skuStatus);
		params.put("parentCategoryId", parentCategoryId);
		params.put("categoryId", categoryId);
		params.put("sortType", sortType);
		if (vip == 0 || vip == 1) {
			params.put("vip", vip);
		}

		String propertySQL = assembleSQL(-1, -1, parentCategoryId, categoryId);
		params.put("properties", propertySQL);

		return getSqlSession()
				.selectOne("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.searchBoutiqueProductCount", params);
	}

	@Override
	public List<ProductBoutique> searchBoutiqueProductInfo(PageQuery pageQuery, Long id, String name,
			String clothesNumber, String brandName, Integer skuStatus, Integer parentCategoryId, Integer categoryId,
			Integer sortType, int vip) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("pageQuery", pageQuery);
		params.put("id", id);
		params.put("name", name);
		params.put("clothesNumber", clothesNumber);
		params.put("brandName", brandName);
		params.put("skuStatus", skuStatus);
		params.put("parentCategoryId", parentCategoryId);
		params.put("categoryId", categoryId);
		params.put("sortType", sortType);
		if (vip == 0 || vip == 1) {
			params.put("vip", String.valueOf(vip));
		}
		String propertySQL = assembleSQL(-1, -1, parentCategoryId, categoryId);
		params.put("properties", propertySQL);

		return getSqlSession()
				.selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.searchBoutiqueProductInfo", params);
	}

	@Override
	public int updateBoutiqueProduct(Long productId, Integer status, Long updateTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productId", productId);
		params.put("status", status);
		params.put("updateTime", updateTime);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.updateBoutiqueProduct",
				params);
	}

	@Override
	public int insertBoutiqueProduct(Long productId, Integer status, Long createTime, Long updateTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productId", productId);
		params.put("status", status);
		params.put("createTime", createTime);
		params.put("updateTime", updateTime);

		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.insertBoutiqueProduct",
				params);
	}

	@Override
	public int udpateBoutiqueProductPrice(String productId, Double price) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productId", productId);
		params.put("price", price);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.udpateBoutiqueProductPrice",
				params);
	}

	@Override
	public int udpateBoutiqueSetVip(long productId, int vip) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productId", productId);
		params.put("vip", vip);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.udpateBoutiqueSetVip",
				params);
	}

	@Override
	public int updateSaleStartTime(String productId, long updateTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productId", productId);
		params.put("updateTime", updateTime);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.updateSaleStartTime", params);
	}

	@Override
	public ProductBoutique getBoutiqueProductByProductId(String productId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productId", productId);
		return getSqlSession()
				.selectOne("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getBoutiqueProductByProductId", params);
	}

	@Override
	public int updateBoutiqueProductUpdateTime(String productId, Long updateTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productId", productId);
		params.put("updateTime", updateTime);
		return getSqlSession()
				.update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.updateBoutiqueProductUpdateTime", params);
	}

	@Override
	public int udpateProductWholeSaleCash(String productId, Double price) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", productId);
		params.put("wholeSaleCash", price);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.udpateProductWholeSaleCash",
				params);
	}

	@Override
	public int deleteVideo(long productId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productId", productId);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.deleteVideo", params);
	}

	@Override
	public int deleteShopProductVideo(long shopProductId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("shopProductId", shopProductId);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.deleteShopProductVideo",
				params);
	}

	@Override
	public List<Long> getProductIdBySKUIDS(List<Long> productSKUIds) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productSKUIds", productSKUIds);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getProductIdBySKUIDS",
				params);
	}

	@Override
	public List<Long> getProductIdBySKUID(Long productSKUId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productSKUId", productSKUId);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getProductIdBySKUID",
				params);
	}

	@Override
	public List<Long> getStoreIdsByProductId(Long productId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productId", productId);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getStoreIdsByProductId",
				params);
	}

	@Override
	public List<Long> getOnShelfStoreIdsByProductId(Long productId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productId", productId);
		return getSqlSession()
				.selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getOnShelfStoreIdsByProductId", params);
	}

	@Override
	public List<Long> getProductIdBySKUNOS(List<Long> productSKUNOs) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("productSKUNOs", productSKUNOs);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.getProductIdBySKUNOS",
				params);
	}

	@Override
	public List<ProductSKU> skusInfo() {
		Map<String, Object> params = new HashMap<String, Object>();

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductSKUMapperSqlImpl.skusInfo", params);
	}

}