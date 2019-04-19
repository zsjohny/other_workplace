package com.jiuy.core.dao.impl.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;

import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.product.DeductProductVO;
import com.jiuyuan.entity.product.ProductShare;
import com.jiuyuan.entity.product.ProductWindow;
import com.jiuyuan.entity.query.PageQuery;

public class ProductMapperSqlImpl extends SqlSupport implements ProductMapper{

    @Override
    public Product getProductById(long productId) {
        Map<String,Object> params = new HashMap<String,Object>();
        
        params.put("productId", productId);
        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getProductById", params);
    }
    @Override
    public Product getPromotionProductById(long productId, String promoSetting) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	
    	params.put("productId", productId);
    	params.put("promoSetting", promoSetting);
    	
    	return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getPromotionProductById", params);
    }
    
    @Override
    public ProductShare getProductShareByProId(long productId){
    	Map<String,Object> params = new HashMap<String,Object>();
    	
    	params.put("productId", productId);
    	
    	return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getProductShareByProId", params);
    }

    @Override
    public List<Product> getProductByCategoryId(long categoryId, PageQuery pageQuery) {
        return null;
    }

    @Override
    public List<Product> getProductByCategoryIds(Collection<Long> categoryIds, PageQuery pageQuery) {
        return null;
    }

    @Override
    public long add(Product product) {
        return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.add", product);
    }

	@Override
	public List<Product> loadList(@Param("pageQuery")PageQuery pageQuery, @Param("status")Integer status) {
	    Map<String,Object> params = new HashMap<String,Object>();
	    params.put("pageQuery", pageQuery);
	    params.put("status", status);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getProductList",params);
	}

    @Override
    public int getProductCount(PageQuery pageQuery, Integer status) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("pageQuery", pageQuery);
        params.put("status", status);
        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getProductCount",params);
    }

    @Override
    public int deactivateProductByIds(Collection<Long> ids) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("ids", ids);
        return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.deactivateProductByIds", params);
    }

	@Override
	public int updateProduct(Product prod) throws Exception {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.updateProduct", prod);
	}
	
	
	@Override
    public int updateProductShare(ProductShare productShare) throws Exception {
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.insertProductShare", productShare);
	}
	
	

	@Override
	public List<Integer> chkProductClothesNum(String clothesNumber) {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.chkProductClothesNum", clothesNumber);
	}

	@Override
	public List<Map<String, Object>> getCategoriesInfo(long productId) {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getCategoriesInfo", productId);
	}

	@Override
	public List<Map<String,Object>> getPropertyInfo(long productId) {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getPropertyInfo", productId);
	}

	@Override
	public List<String> getDescription(long productId) {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getDescription", productId);
	}
	
	@Override
	public List<String> getRemark(long productId) {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getRemark", productId);
	}

	@Override
	public List<Integer> loadproductIdList(PageQuery pageQuery, Integer status) {
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("pageQuery", pageQuery);
        params.put("status", status);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.loadproductIdList", params);
	}

    @Override
    public int updateProductStatusOut() {
		return 0;
        /*return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.deactivateProductAuto");*/
    }

	@Override
	public int updateProductBrandId(int brandId) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.updateProductBrandId", brandId);
	}

	@Override
	public ProductWindow searchProWinByClothesNum(String clothesNum) {
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.searchProWinByClothesNum", clothesNum);
	}

	@Override
	public List<Product> getIdsByRootPath(String oldImgRootPath) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("oldImgRootPath", oldImgRootPath);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getIdsByRootPath", params);
	}

	@Override
	public List<Map<String, Object>> searchOverview(long brandId, String clothesNo, PageQuery pageQuery, String orderSql, String saleStatusSql, int skuStatusIntValue) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("brandId", brandId);
		params.put("clothesNo", clothesNo);
		params.put("pageQuery", pageQuery);
		params.put("orderSql", StringUtils.replace(orderSql, "CreateTime", "a.CreateTime"));
		params.put("saleStatusSql", saleStatusSql);
		params.put("skuStatus", skuStatusIntValue);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.searchOverview", params);
	}

	@Override
	public int searchOverviewCount(long brandId, String clothesNo, String saleStatusSql, int skuStatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("brandId", brandId);
		params.put("clothesNo", clothesNo);
		params.put("saleStatusSql", saleStatusSql);
		params.put("skuStatus", skuStatus);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.searchOverviewCount", params);	
	}

	@Override
	public int getMaxWeight() {
		Integer max =  getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getMaxWeight");	
		return max == null ? 0 : max;
	}

	@Override
	public boolean isOnSale(long productId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", productId);
		int count = getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.isOnSale", params);

		return count > 0 ? true : false;
	}

	@Override
	public List<Product> getByClothesNums(Collection<String> clothesNums) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("clothesNums", clothesNums);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getByClothesNums", params);	
	}

	@Override
	public List<Product> productOfIds(Collection<Long> productIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("ids", productIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.productOfIds", params);
	}

	@Override
	public List<Product> getAllProducts() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getAllProducts");
	}

    @Override
    public long insertProduct(Product product) {
        getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.insertProduct", product);
        
        return product.getId();
    }
    
    @Override
    public long addShopProduct(ShopProduct shopProduct) {
        getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.addShopProduct", shopProduct);
        return shopProduct.getId();
    }

	@Override
	public Product getBySkuId(long skuId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("skuId", skuId);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getBySkuId", params);
	}

	@Override
	public List<Product> productsOfRestrctIds(Collection<Long> restrictionIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("restrictionIds", restrictionIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.productsOfRestrctIds", params);
	}

	@Override
	public int batchUpdate(Long restrictId, Long vCategoryId, Long subscriptId, Collection<Long> productIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("restrictId", restrictId);
		params.put("vCategoryId", vCategoryId);
		params.put("productIds", productIds);
		params.put("subscriptId", subscriptId);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.batchUpdate", params);
	}

	@Override
	public int batchRemoveRestrictId(long restrictId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("restrictId", restrictId);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.batchRemoveRestrictId", params);
	}

	@Override
	public List<Product> getByWarehouse(Collection<Long> warehouseIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("warehouseIds", warehouseIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getByWarehouse", params);
	}

	@Override
	public List<Product> checkInfoCompleted(long productId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", productId);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.checkInfoCompleted", params);
	}

	@Override
	public int updateWarehouseId(Collection<String> clothesNos, long warehouseId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("clothesNos", clothesNos);
		params.put("warehouseId", warehouseId);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.updateWarehouseId", params);
	}

	@Override
	public List<Product> search(String clothesNo, Long brandId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("clothesNo", clothesNo);
		params.put("brandId", brandId);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.search", params);
	}

	@Override
	public int update(Long id, Integer status, Integer weight, Integer promotionSaleCount, Integer promotionVisitCount) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("status", status);
		params.put("weight", weight);
		params.put("promotionSaleCount", promotionSaleCount);
		params.put("promotionVisitCount", promotionVisitCount);
		params.put("updateTime", System.currentTimeMillis());
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.update", params);
	}

	@Override
	public int update(Long id, Integer status, Long saleStartTime, Long saleEndTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("status", status);
		params.put("saleStartTime", saleStartTime);
		params.put("saleEndTime", saleEndTime);
		params.put("updateTime", System.currentTimeMillis());
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.update", params);
	}
	
	@Override
	public int update(Long id, Long brandId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("brandId", brandId);
		params.put("updateTime", System.currentTimeMillis());
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.update", params);
	}
	
	@Override
	public void executeRcmdSttstcs() {
		getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.executeRcmdSttstcs");
	}

	@Override
	public List<Product> productsOfSubscriptIds(Collection<Long> subscriptIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("subscriptIds", subscriptIds);
																						   
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.productsOfSubscriptIds", params);
	}
	
	@Override
	public int batchRemoveSubscriptId(long subscriptId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("subscriptId", subscriptId);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.batchRemoveSubscriptId", params);
	}

	@Override
	public int batchUpdateSubscriptId(Long subscriptId, Collection<Long> productIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("subscriptId", subscriptId);
		params.put("productIds", productIds);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.batchUpdateSubscriptId", params);
	}

	@Override
	public int updateProductWholeSaleCash(Product prod) throws Exception {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.updateProductWholeSaleCash", prod);
	}

	@Override
	public List<Product> getBySeasonOnSale() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getBySeasonOnSale");
	}

	@Override
	public List<Product> searchPrice(Integer minCash, Integer maxCash) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("minCash", minCash);
		params.put("maxCash", maxCash);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.searchPrice", params);
	}

	@Override
	public int updateProductPromotionInfo(ArrayList<Long> productIds, double promotionCash, int promotionJiuCoin,
			long promotionStartTime, long promotionEndTime, int promotionSetting) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("productIds", productIds);
		params.put("promotionCash", promotionCash);
		params.put("promotionJiuCoin", promotionJiuCoin);
		params.put("promotionStartTime", promotionStartTime);
		params.put("promotionEndTime", promotionEndTime);
		params.put("promotionSetting", promotionSetting);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.updateProductPromotionInfo", params);
	}

	@Override
	public int updateProductPromotionInfoByDiscount(ArrayList<Long> productIds, double discount,
			long promotionStartTime, long promotionEndTime, int promotionSetting) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("productIds", productIds);
		params.put("discount", discount);
		params.put("promotionStartTime", promotionStartTime);
		params.put("promotionEndTime", promotionEndTime);
		params.put("promotionSetting", promotionSetting);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.updateProductPromotionInfoByDiscount", params);
	}

	@Override
	public List<Product> getQianmiProduct() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getQianmiProduct");
	}

	/* (non-Javadoc)
	 * @see com.jiuy.core.dao.modelv2.ProductMapper#batchDeleteVCategory(long)
	 */
	@Override
	public int batchDeleteVCategory(long categoryId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("vCategoryId", categoryId);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.batchDeleteVCategory", params);		
	}

	@Override
	public List<Product> getByWarehouseId(Long warehouseId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("warehouseId", warehouseId);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getByWarehouseId", params);	
	}

	@Override
	public List<Product> getByWarehouseIds(Collection<Long> warehouseIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("warehouseIds", warehouseIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getByWarehouseIds", params);	
	}

	@Override
	public List<Product> getByNotInWarehouseIds(List<Long> warehouseIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("warehouseIds", warehouseIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getByNotInWarehouseIds", params);	
	}

	/* (non-Javadoc)
	 * @see com.jiuy.core.dao.modelv2.ProductMapper#productMap()
	 */
	@Override
	public List<Map<String, Object>> productMap(Collection<Long> seasonIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("seasonIds", seasonIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.productMap", params);		
	}

	@Override
	public List<DeductProductVO> searchJiuCoinDeductProduct(PageQuery pageQuery,long id, long season, long year, String name, String clothesNumber,
			String brandName, String saleStatus, int parentCategoryId, long categoryId, int skuStatus) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pageQuery", pageQuery);
		params.put("name", name);
		params.put("clothesNumber", clothesNumber);
        params.put("brandName", brandName);
        params.put("saleStatusSql", saleStatus);
        params.put("parentCategoryId", parentCategoryId);
        params.put("categoryId", categoryId);
        params.put("skuStatus", skuStatus);
        params.put("id", id);
        String propertySQL = assembleSQL(season, year, parentCategoryId, categoryId);
        params.put("properties", propertySQL);
        
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.searchJiuCoinDeductProduct", params);
	}
	
	@Override
	public int searchJiuCoinDeductProductCount(long id, long season, long year, String name, String clothesNumber,
			String brandName, String saleStatus, int parentCategoryId, long categoryId, int skuStatus){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name", name);
		params.put("clothesNumber", clothesNumber);
        params.put("brandName", brandName);
        params.put("saleStatusSql", saleStatus);
        params.put("parentCategoryId", parentCategoryId);
        params.put("categoryId", categoryId);
        params.put("skuStatus", skuStatus);
        params.put("id", id);
        String propertySQL = assembleSQL(season, year, parentCategoryId, categoryId);
        params.put("properties", propertySQL);
        
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.searchJiuCoinDeductProductCount", params);
	}
	
	private String assembleSQL(long season, long year, long parentCategoryId, long categoryId) {
		// TODO Auto-generated method stub
        StringBuilder properties = new StringBuilder();     
        
        int k = 0;
        
        if (season != -1) {
        	properties.append(" union all SELECT distinct ProductId FROM yjj_ProductProperty where PropertyNameId=9 and PropertyValueId=");
        	properties.append(season);
        	k++;
        }
        if (year != -1) {
        	properties.append(" union all SELECT distinct ProductId FROM yjj_ProductProperty where PropertyNameId=10 and PropertyValueId=");
        	properties.append(year);
        	k++;
        }        
        if (parentCategoryId != -1) {
        	properties.append(" union all SELECT distinct ProductId FROM yjj_ProductCategory where Status > -1 and (CategoryId in (SELECT Id FROM yjj_Category WHERE ParentId=");
        	properties.append(parentCategoryId + ") OR CategoryId=" + parentCategoryId+")");
        	k++;
        }      
        if (categoryId != -1) {
        	properties.append(" union all SELECT distinct ProductId FROM yjj_ProductCategory where Status > -1 and  CategoryId=");
        	properties.append(categoryId);
        	k++;
        }
        
        String propertySQL = null;
        if (properties.length() > 11)    
        	propertySQL = "select ProductId FROM  ("+properties.substring(11).toString() +") t1 group by ProductId having count(ProductId)=" + k;
		
        return propertySQL;
	}

	@Override
	public int updateProductDeductPercent(Collection<Long> productIds, double deductPercent) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("productIds", productIds);
		params.put("deductPercent", deductPercent);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.updateProductDeductPercent", params);
	}

	@Override
	public List<Product> getProductByName(String name) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name", name);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getProductByName", params);
	}
	@Override
	public int updateShopProduct(ShopProduct shopProduct) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.updateShopProduct", shopProduct);
	}
	@Override
	public List<ShopProduct> getShopProductByProductId(long productId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("productId", productId);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getShopProductByProductId", params);
	}
	@Override
	public int soldOutShopProductByProductId(Long productId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("productId", productId);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.soldOutShopProductByProductId", params);
	}
	/**
	 * 下架商家商品
	 */
	@Override
	public int soldOutShopProduct(Long shopProductId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("shopProductId", shopProductId);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.soldOutShopProduct", params);
	}
	
	public List<ShopProduct> getShopProduct(long storeId, long productId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("storeId", storeId);
		params.put("productId", productId);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.getShopProduct", params);
	}
	@Override
	public int updateProductSaleCount(long productId, int count) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productId", productId);
		params.put("count", count);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductMapperSqlImpl.updateProductSaleCount", params);
	}
}
