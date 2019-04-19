package com.jiuyuan.dao.mapper.supplier;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SupplierOrderNewMapper {
    Double select(Long orderNo);
}
