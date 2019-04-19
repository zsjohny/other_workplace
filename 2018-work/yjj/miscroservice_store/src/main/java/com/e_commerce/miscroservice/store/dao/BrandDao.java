package com.e_commerce.miscroservice.store.dao;


import com.e_commerce.miscroservice.store.entity.vo.Brand;

/**
 * 品牌表
 */
public interface BrandDao {

    /**
     * 获取品牌
     * @param brandId
     * @return
     */
    Brand getBrandByBrandId(Long brandId);
}
