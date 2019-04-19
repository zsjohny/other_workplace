package com.jiuy.rb.service.product;

import com.jiuy.rb.model.product.BrandRb;

import java.util.List;

/**
 * 品牌service
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/14 16:30
 * @Copyright 玖远网络
 */
public interface IBrandService {

    /**
     * 根据品牌ids查询
     *
     * @param ids 品牌ids
     * @return java.util.List<com.jiuy.rb.model.product.BrandRb>
     * @author Charlie(唐静)
     * @date 2018/6/21 19:02
     */
    List<BrandRb> selectByIds(List<Long> ids);

    /**
     * 根据商品id 获取品牌信息
     * @param productId
     * @return
     */
    BrandRb findBrandByProductId(Long productId);
}
