/**
 * 
 */
package com.jiuy.core.dao.modelv2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.ProductCategory;
import com.store.entity.ProductCategoryVO;

/**
 * @author LWS
 *
 */
public interface ProductCategoryMapper {

    int batchAdd(List<ProductCategoryVO> basics);

//    int removeProductCategoriesByProductIds(Collection<Long> ids);

	int deleteProductCategory(long productId);
	
	int deleteWholeCategory(long productId);

	int addProductCategory(long id, long[] classificationArrayInt, long createTime);
	
	int addWholeSaleCategory(long id, long[] classificationArrayInt, long createTime);

	/**
	 * 根据一组产品Id,显示这些产品所属的分类
	 * @param productIdList
	 * @return
	 */
	List<Map<String, Object>> loadProductCategoryNames(List<Integer> productIdList);

	List<Map<String, Object>> getCatNameById(long productId);

	List<Long> productsOfCategory(Collection<Long> categoryIds);

	int addVirtualProduct(long categoryId, Collection<Long> productIds);

	List<ProductCategory> getByCategoryIds(Collection<Long> categoryIds);

	int rmRelatedProducts(long categoryId);

	List<Map<String, Object>> getErpCat();

	List<ProductCategory> itemsOfCategoryIds(Collection<Long> categoryIds);

	List<ProductCategory> itemsOfProductIds(Collection<Long> createList);
	
	List<ProductCategory> itemsWholeSaleProductIds(Collection<Long> createList);

	int delete(long productId, long categoryId);

	List<ProductCategory> search(Long productId);

	int batchAddItems(List<ProductCategory> productCategories);

}
