package com.e_commerce.miscroservice.store.dao.impl;


import com.e_commerce.miscroservice.store.dao.ProductDao;
import com.e_commerce.miscroservice.store.entity.vo.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * 产品表
 */
@Repository
public class ProductDaoImpl implements ProductDao {


    /**
     * 产品列表
     *
     * @param userId
     * @param orderNOs
     * @return
     */
    @Override
    public List<Product> getOrderProducts(Long userId, Set<Long> orderNOs) {
        return null;
    }
}
