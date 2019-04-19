package com.jiuy.rb.service.product;

import com.jiuy.rb.model.product.ProductSkuRbNew;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/4 19:21
 * @Copyright 玖远网络
 */
public interface IShopGoodsCarService{


    /**
     * 将购物车设为失效
     *
     * @param skuIds skuIds
     * @param storeId storeId
     * @author Charlie
     * @date 2018/9/4 19:33
     */
    void adviceGoodsCarSkuHasDisabled(List<Long> skuIds, Long storeId);
}
