package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.product.ProductPropName;

@DBMaster
public interface PropertyNameMapper {

    @MapKey("id")
    Map<Long, ProductPropName> getPropertyNames(@Param("ids") Collection<Long> ids);
    
    List<ProductPropName> getPropertyNamesListByIds(@Param("ids") Collection<Long> ids);

}
