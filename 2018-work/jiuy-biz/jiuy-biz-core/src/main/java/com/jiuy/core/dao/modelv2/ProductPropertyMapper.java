package com.jiuy.core.dao.modelv2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.entity.ProductProp;
import com.jiuyuan.entity.ProductPropValue;
import com.store.entity.ProductPropVO;

public interface ProductPropertyMapper {

    List<ProductProp> getOrderedProductProperties(@Param("productId") long productId, @Param("isSKU") boolean isSKU);

    List<ProductProp> getProductProperties(@Param("productId") long productId,
                                           @Param("propertyIds") Collection<Long> propertyIds);

    int batchAdd(List<ProductPropVO> basics);

    int removeProductPropertiesByProductIds(Collection<Long> ids);

    /**
     * 传产品id和产品属性id查找是否已存在该商品的该属性
     * @param productId
     * @param propertyNameId
     * @return
     */
	int getPropertyByIds(long productId, long propertyNameId);

	int updateProductProperty(int productPropertyId, long[] seasonArrayInt);

	int addProductProperty(long productId, long[] seasonArrayInt);

    int updateProductBrand(int ppmId, long brandId);

    int addProductBrand(long productId, long brand_property, long brandId);

	List<ProductPropValue> propertiesOfProductIds(Collection<Long> createList);

	Map<Long, ProductProp> valueOfNameIdMap(long id);

}
