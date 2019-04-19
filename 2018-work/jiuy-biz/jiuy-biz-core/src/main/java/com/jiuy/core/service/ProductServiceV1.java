/**
 * 
 */
package com.jiuy.core.service;

import java.util.List;

import com.jiuyuan.entity.ClassificationDefinition;
import com.jiuyuan.entity.Dictionary;
import com.jiuyuan.entity.ProductV1;
import com.jiuyuan.entity.TreeDictionary;

/**
 * @author LWS
 *
 */
public interface ProductServiceV1 {

	
	/*******************************************************************/
	/*
	 * 						商品逻辑操作，使用旧的产品模型
	 * 						创建时间： 2015.6.2
	 * 						创建人：     LiuWeisheng
	 *
	/******************************************************************/
	public long addProduct(ProductV1 product, String adminAccountEmail);

	public List<ProductV1> getAllProducts();


	public ProductV1 getProductByProperty(String property);

	public boolean increaseFavorite(String productid);

	public List<ProductV1> getProductList(String startPage, String pageSize, String productType);


	public List<ClassificationDefinition> getClassificationDefs();

	public List<Dictionary> getDictionaries(String groupId);

	public List<TreeDictionary> getTreeDictionaries(String aDDRESS_ID,
			String parentid);
	
}
