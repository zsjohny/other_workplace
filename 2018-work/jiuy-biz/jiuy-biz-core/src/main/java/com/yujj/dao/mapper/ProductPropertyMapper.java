package com.yujj.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;

import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.product.ProductProp;

@DBMaster
public interface ProductPropertyMapper {

    List<ProductProp> getOrderedProductProperties(long productId);

	/**
	 * @return
	 */

    @MapKey("productId")
    Map<Long, ProductProp> getPropsByNameId(long propertyNameId);
}