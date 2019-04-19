package com.jiuy.core.dao.modelv2;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.ProductPropName;

public interface PropertyNameMapper {

    Map<Long, ProductPropName> getPropertyNames();

    int getPropertyNameIdByName(String propertyName);

	List<String> search();

}
