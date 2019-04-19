package com.store.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.newentity.ProductSkuNew;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.product.ProductSKUsPropVO;
import com.store.entity.StoreProductSKUsPropVO;


@DBMaster
public interface ProductSKUMapper {

    List<ProductSKU> getProductSKUs(@Param("ids") Collection<Long> ids);
    
    List<ProductSKU> getProductSKUsOfProduct(long productId);

    ProductSKU getProductSKU(long id);

    int updateRemainCount(@Param("id") long id, @Param("by") int by);
    
    int updateRemainCountSecond(@Param("id") long id, @Param("by") int by);
    
    List<StoreProductSKUsPropVO> getProductSKUsPropVO();

	ProductSKU getByProductIdPropertyIds(@Param("propertyIds") String propertyIds, @Param("productId") long productId);

	List<ProductSKU> getAllProductSKUsOfProduct(@Param("productId") long productId);

	List<ProductSKU> getProductSKUsByProductId(Long productId);

	int getSaleStartProductNums(@Param("storeId") long storeId);
	/**
	 * 获取需要上架的商品Id集合不包含已经同步的商品
	 * @param storeId
	 * @return
	 */
	List<Long> getAllSynchronousUpdateProductIds(@Param("storeId") long storeId);

	/**
	 * 获取上架的限购活动商品
	 * @param restrictionActivityProductId
	 * @return
	 */
	List<ProductSKU> getProductSKUsByRestrictionActivityProductId(long restrictionActivityProductId);

	/**
	 * 供应商商品总库存
	 *
	 * @param productId productId
	 * @return int
	 * @author Charlie
	 * @date 2018/9/7 11:05
	 */
	int supplierProductInventory(@Param ("productId") Long productId);

	/**
	 * 门店商品总库存
	 *
	 * @param shopProductId shopProductId
	 * @return int
	 * @author Charlie
	 * @date 2018/9/7 11:05
	 */
	int storeProductInventory(@Param ("shopProductId") Long shopProductId);
}
