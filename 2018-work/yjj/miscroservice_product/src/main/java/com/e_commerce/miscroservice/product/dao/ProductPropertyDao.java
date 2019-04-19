package com.e_commerce.miscroservice.product.dao;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/16 18:15
 * @Copyright 玖远网络
 */
public interface ProductPropertyDao {


    /**
     * 查询商品属性
     *
     * @param productId productId
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/16 18:17
     */
    List<Map<String,String>> listByProduct(Long productId);
}
