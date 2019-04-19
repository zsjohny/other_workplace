package com.jiuy.core.dao.modelv2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;

import com.jiuyuan.entity.ProductPropValue;

public interface PropertyValueMapper {

    Map<Long, ProductPropValue> getPropertyValues();

    @MapKey("propertyValue")
    Map<String, ProductPropValue> getPropertyValueMap();

    List<Integer> getBrandIds();

	List<Map<String, Object>> getColors();

	List<Map<String, Object>> getBrands();

	int remove(Collection<Long> brandIds);

	ProductPropValue addBrand(ProductPropValue ppv);

	int chkRepeat(int propertyNameId, String name);

	List<ProductPropValue> getValueMap(Collection<Long> propertyNameIds);

	List<ProductPropValue> getPropertyValuesByNameId(Collection<Long> propertyNameIds);

	ProductPropValue add(ProductPropValue ppv);

}
