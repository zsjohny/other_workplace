package com.e_commerce.miscroservice.product.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/26 15:16
 * @Copyright 玖远网络
 */
public interface ProductService{

    /**
     * 批量查询商品库存
     *
     * @param ids ids
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/26 15:43
     */
    List<Map<String,Object>> findInventoryByProductIds(Collection<Long> ids);
}
