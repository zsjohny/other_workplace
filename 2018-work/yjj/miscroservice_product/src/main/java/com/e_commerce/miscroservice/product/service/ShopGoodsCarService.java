package com.e_commerce.miscroservice.product.service;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 17:32
 * @Copyright 玖远网络
 */
public interface ShopGoodsCarService{

    /**
     * 将正常状态的购物车状态设为失效
     *
     * @param shopProductId shopProductId
     * @param storeId 门店用户id
     * @author Charlie
     * @date 2018/11/27 17:39
     */
    void adviceGoodsCarThisProductHasDisabled(Long shopProductId, Long storeId);


    /**
     * 将购物车设为失效
     *
     * @param skuIds skuIds
     * @param storeId storeId
     * @author Charlie
     * @date 2018/9/4 19:33
     */
    void adviceGoodsCarSkuHasDisabled(List<Long> skuIds, Long storeId);



    /**
     * 小程序用户购物车列表
     *
     * @param memberId memberId
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/21 9:42
     */
    List<Map<String, Object>> listShopCar(Long memberId);

}
