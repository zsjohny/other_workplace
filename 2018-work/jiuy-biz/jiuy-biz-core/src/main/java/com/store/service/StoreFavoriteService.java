package com.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.store.dao.mapper.StoreFavoriteMapper;
import com.store.entity.Brand;
import com.store.entity.StoreFavorite;
import com.store.service.brand.ShopBrandService;


/**
 * @author jeff.zhan
 * @version 2016年12月10日 下午2:43:35
 * 
 */

@Service
public class StoreFavoriteService {

	@Autowired
	private StoreFavoriteMapper storeFavoriteMapper;
	
	@Autowired
	private ProductServiceShop productService;
	
	@Autowired
	private StoreBusinessServiceShop storeBusinessService;
	
	@Autowired
	private ShopBrandService shopBrandService;
	
	public StoreFavorite getFavorite(long storeId, long relatedId, int type) {
		// TODO Auto-generated method stub
		return storeFavoriteMapper.getFavorite(storeId, relatedId, type);
	}

	public int searchCountByType(long storeId, int type) {
		// TODO Auto-generated method stub
		return storeFavoriteMapper.searchCountByType(storeId, type);
	}

	public List<Map<String, Object>> searchByType(PageQuery pageQuery, long storeId, int type) {
		// TODO Auto-generated method stub
		List<StoreFavorite> storeFavorites = storeFavoriteMapper.searchByType(pageQuery, storeId, type);
		switch (type) {
		case 0:
			StoreBusiness storeBusiness = storeBusinessService.getById(storeId);
			return assembleProducts(storeFavorites, storeBusiness);
		case 1:
			return assembleBrands(storeFavorites);
		default:
			return new ArrayList<>();
		}
	}

	private List<Map<String, Object>> assembleBrands(List<StoreFavorite> storeFavorites) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> results = new ArrayList<>();
		for (StoreFavorite storeFavorite : storeFavorites) {
			Brand brand = shopBrandService.getBrand(storeFavorite.getRelatedId());
			Map<String, Object> result = new HashMap<>();
			result.put("image", brand.getBrandIdentity() == null ? brand.getLogo() : brand.getBrandIdentity());
			result.put("description", brand.getDescription());
			result.put("name", brand.getBrandName());
			result.put("brand_id", brand.getBrandId());
			
			results.add(result);
		}
		return results;
	}

	private List<Map<String, Object>> assembleProducts(List<StoreFavorite> storeFavorites, StoreBusiness storeBusiness) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> results = new ArrayList<>();
		for (StoreFavorite storeFavorite : storeFavorites) {
			Product product = productService.getProduct(storeFavorite.getRelatedId());
			Map<String, Object> result = new HashMap<>();
			result.put("product_image", product.getImage());
			result.put("cash", product.getCash());
			result.put("name", product.getName());
			result.put("product_id", product.getId());
			result.put("cloth_no", product.getClothesNumber());
			result.put("comission", String.format("%.2f", storeBusiness.getCommissionPercentage() * product.getCash() / 100));
			
			results.add(result);
		}
		return results;
	}

}
