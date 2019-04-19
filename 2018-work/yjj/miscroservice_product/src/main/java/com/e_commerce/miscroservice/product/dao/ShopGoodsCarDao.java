package com.e_commerce.miscroservice.product.dao;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 18:04
 * @Copyright 玖远网络
 */
public interface ShopGoodsCarDao{

    /**
     * 批量更新购物车sku状态
     *
     * @param carIds 购物车id
     * @param status 修改的状态
     * @return int
     * @author Charlie
     * @date 2018/11/27 18:08
     */
    void batchSetStatusByIds(List<Long> carIds, int status);
}
