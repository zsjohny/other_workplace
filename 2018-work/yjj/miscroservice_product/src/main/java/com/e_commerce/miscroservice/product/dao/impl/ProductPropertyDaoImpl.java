package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.product.dao.ProductPropertyDao;
import com.e_commerce.miscroservice.product.mapper.ProductPropertyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/16 18:15
 * @Copyright 玖远网络
 */
@Component
public class ProductPropertyDaoImpl implements ProductPropertyDao {


    @Autowired
    private ProductPropertyMapper productPropertyMapper;


    /**
     * 查询商品属性
     *
     * @param productId productId
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/16 18:17
     */
    @Override
    public List<Map<String, String>> listByProduct(Long productId) {
        return productPropertyMapper.listByProduct(productId);
    }
}
