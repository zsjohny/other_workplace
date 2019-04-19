package com.e_commerce.miscroservice.product.service.impl;

import com.e_commerce.miscroservice.product.mapper.ProductSkuMapper;
import com.e_commerce.miscroservice.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/26 15:16
 * @Copyright 玖远网络
 */
@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductSkuMapper productSkuMapper;

    /**
     * 批量查询商品库存
     *
     * @param ids ids
     * @return java.util.List<java.util.Map   <   java.lang.String   ,   java.lang.Object>>
     * @author Charlie
     * @date 2018/11/26 15:43
     */
    @Override
    public List<Map<String, Object>> findInventoryByProductIds(Collection<Long> ids) {
        return productSkuMapper.findInventoryByProductIds(ids);
    }
}
