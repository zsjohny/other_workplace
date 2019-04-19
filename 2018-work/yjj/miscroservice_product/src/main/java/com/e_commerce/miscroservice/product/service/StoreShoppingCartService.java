package com.e_commerce.miscroservice.product.service;

import com.e_commerce.miscroservice.product.vo.StoreShoppingCartQuery;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/30 17:13
 * @Copyright 玖远网络
 */
public interface StoreShoppingCartService{

    /**
     * 我的购物车
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/30 17:16
     */
    Map<String, Object> listMyCart(StoreShoppingCartQuery query);


    /**
     * 删除所有失效的购物车
     *
     * @param query storeId
     * @author Charlie
     * @date 2018/12/3 15:43
     */
    void deleteCarts(StoreShoppingCartQuery query);



    /**
     * 更新购物车购买数量
     *
     * @param storeId 门店用户
     * @param cartsJsonArray 购物车id和购买数量
     * @author Charlie
     * @date 2018/12/3 16:46
     */
    void updateBuyCount(Long storeId, String cartsJsonArray);

    /**
     * 购物车商品数量
     *
     * @param userId userId
     * @return java.util.Map
     * @author Charlie
     * @date 2019/2/16 16:05
     */
    Map<String, Object> clothesNumberCount(Long userId);



}
