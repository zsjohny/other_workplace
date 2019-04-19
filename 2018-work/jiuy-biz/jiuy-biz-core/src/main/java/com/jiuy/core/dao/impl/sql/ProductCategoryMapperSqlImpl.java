package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.modelv2.ProductCategoryMapper;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.ProductCategory;
import com.store.entity.ProductCategoryVO;

public class ProductCategoryMapperSqlImpl extends SqlSupport implements ProductCategoryMapper {

    @Override
    public int batchAdd(List<ProductCategoryVO> pcvos) {
        if (null == pcvos || pcvos.size() == 0) {
            return 0;
        }
        return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.insertBatch", pcvos);
    }

	@Override
	public int deleteProductCategory(long productId) {
		return getSqlSession().delete("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.deleteProductCategory", productId);
	}
	
	@Override
	public int deleteWholeCategory(long productId) {
		return getSqlSession().delete("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.deleteWholeSaleCategory", productId);
	}

	@Override
	public int addProductCategory(long productId, long[] classificationArrayInt, long createTime) {
		Map<String,Object> map = new HashMap<>();
		map.put("productId", productId);
		map.put("classificationArrayInt", classificationArrayInt);
		map.put("createTime", createTime);
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.addProductCategory", map);
	}
	
	@Override
	public int addWholeSaleCategory(long productId, long[] classificationArrayInt, long createTime) {
		Map<String,Object> map = new HashMap<>();
		map.put("productId", productId);
		map.put("classificationArrayInt", classificationArrayInt);
		map.put("createTime", createTime);
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.addWholeSaleCategory", map);
	}

	@Override
	public List<Map<String, Object>> loadProductCategoryNames(List<Integer> productIdList) {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.loadProductCategoryNames", productIdList);
	}

	@Override
	public List<Map<String, Object>> getCatNameById(long productId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("productId", productId);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.getCatNameById", params);
	}

	@Override
	public List<Long> productsOfCategory(Collection<Long> categoryIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("categoryIds", categoryIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.productsOfCategory", params);
	}

	@Override
	public int addVirtualProduct(long categoryId, Collection<Long> productIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("categoryId", categoryId);
		params.put("productIds", productIds);
		
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.addVirtualProduct", params);
	}

	@Override
	public List<ProductCategory> getByCategoryIds(Collection<Long> categoryIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("categoryIds", categoryIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.getByCategoryIds", params);
	}

	@Override
	public int rmRelatedProducts(long categoryId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("categoryId", categoryId);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.rmRelatedProducts", params);
	}

	@Override
	public List<Map<String, Object>> getErpCat() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.getErpCat");
	}

	@Override
	public List<ProductCategory> itemsOfCategoryIds(Collection<Long> categoryIds) {
		Map<String, Object> params = new HashMap<String, Object>();
	
		params.put("collection", categoryIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.itemsOfCategoryIds", params);
	}

	@Override
	public List<ProductCategory> itemsOfProductIds(Collection<Long> productIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("productIds", productIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.itemsOfProductIds", params);
	}
	
	@Override
	public List<ProductCategory> itemsWholeSaleProductIds(Collection<Long> productIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("productIds", productIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.itemsWholeSaleOfProductIds", params);
	}

	@Override
	public int delete(long productId, long categoryId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("productId", productId);
		params.put("categoryId", categoryId);
		params.put("now", System.currentTimeMillis());
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.delete", params);
	}

	@Override
	public List<ProductCategory> search(Long productId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("productId", productId);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.search", params);
	}

	@Override
	public int batchAddItems(List<ProductCategory> productCategories) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("productCategories", productCategories);
		
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductCategoryMapperSqlImpl.batchAddItems", params);
	}
	
}
