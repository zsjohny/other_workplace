package com.e_commerce.miscroservice.store.dao;


import com.e_commerce.miscroservice.store.entity.vo.Product;

import java.util.List;
import java.util.Set;

/**
 * 商品表
 */
public interface ProductDao {


    /**
     * 产品列表
     * @param userId
     * @param orderNOs
     * @return
     */
    List<Product> getOrderProducts(Long userId, Set<Long> orderNOs);
}
