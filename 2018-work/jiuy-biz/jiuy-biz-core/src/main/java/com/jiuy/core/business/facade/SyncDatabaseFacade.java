package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.modelv2.ProductCategoryMapper;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.dao.modelv2.ProductSKUMapper;
import com.jiuy.core.service.product.ProductSKUService;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductCategory;
import com.jiuyuan.entity.ProductSKU;

/**
 * @author jeff.zhan
 * @version 2016年10月25日 下午2:24:23
 * 
 */
@Service
public class SyncDatabaseFacade {
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private ProductSKUService productSKUService;
	
	@Autowired
	private ProductSKUMapper productSKUMapper;
	
	@Autowired
	private ProductCategoryMapper productCategoryMapper;
	
	@Transactional(rollbackFor = Exception.class)
	public void bindCategory() {
		productCategoryMapper.batchAddItems(assembleProductIds(productMapper.searchPrice(null, 50), 139));
		productCategoryMapper.batchAddItems(assembleProductIds(productMapper.searchPrice(51, 100), 140));
		productCategoryMapper.batchAddItems(assembleProductIds(productMapper.searchPrice(101, 200), 141));
		productCategoryMapper.batchAddItems(assembleProductIds(productMapper.searchPrice(201, 500), 142));
		productCategoryMapper.batchAddItems(assembleProductIds(productMapper.searchPrice(500, null), 143));
	}

	private List<ProductCategory> assembleProductIds(List<Product> searchPrice, int categoryId) {
		List<ProductCategory> items = new ArrayList<>();
		long time = System.currentTimeMillis();
		for (Product product : searchPrice) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setCategoryId(categoryId);
			productCategory.setCreateTime(time);
			productCategory.setProductId(product.getId());
			productCategory.setStatus(0);
			productCategory.setUpdateTime(time);
			
			items.add(productCategory);
		}
		return items;
	}

	public void randomUpdate() {
		List<ProductSKU> skus = productSKUMapper.searchPrice(21, 50);
		updatePromotionCount(20, 40, skus);
	}

	private void updatePromotionCount(int min, int max, List<ProductSKU> skus) {
		Random random = new Random();
		for (ProductSKU sku : skus) {
			int num = (int) (random.nextDouble() * (max-min) + min);
			productSKUService.updatePromotionCount(sku.getSkuNo(), num, null);
		}
	}

}
