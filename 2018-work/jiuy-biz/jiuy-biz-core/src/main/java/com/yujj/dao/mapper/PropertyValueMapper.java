package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.product.ProductPropValue;

@DBMaster
public interface PropertyValueMapper {

    @MapKey("id")
    Map<Long, ProductPropValue> getPropertyValues(@Param("ids") Collection<Long> ids);
    
    List<ProductPropValue> getPropertyValuesByNameIds(@Param("ids") Collection<Long> ids);

	/**
	 * @return
	 */
    @MapKey("id")
	Map<Long, ProductPropValue> getValues();
}
