package com.e_commerce.miscroservice.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/16 18:18
 * @Copyright 玖远网络
 */
@Mapper
public interface ProductPropertyMapper {



    /**
     * 查询商品属性
     *
     * @param productId productId
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/16 18:17
     */
    List<Map<String,String>> listByProduct(@Param( "productId" ) Long productId);
}
