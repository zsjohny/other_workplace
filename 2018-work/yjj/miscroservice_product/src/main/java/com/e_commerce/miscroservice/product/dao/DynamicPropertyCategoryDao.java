package com.e_commerce.miscroservice.product.dao;

import com.e_commerce.miscroservice.product.vo.ProductPropertyDTO;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/31 16:11
 */
public interface DynamicPropertyCategoryDao {


    /**
     * 查询动态属性
     *
     * @param productId productId
     * @param categoryId categoryId
     * @return java.util.List<com.e_commerce.miscroservice.product.vo.ProductPropertyDTO>
     * @author Charlie
     * @date 2019/1/31 16:22
     */
    List<ProductPropertyDTO> findByProductId(Long productId, Long categoryId);


}
