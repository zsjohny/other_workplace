package com.e_commerce.miscroservice.product.service;

import com.e_commerce.miscroservice.commons.enums.product.ShopProductUpdTypeEnum;
import com.e_commerce.miscroservice.product.entity.ShopProduct;
import com.e_commerce.miscroservice.commons.entity.product.ShopProductQuery;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/22 17:32
 * @Copyright 玖远网络
 */
public interface ShopProductService{

    /**
     * 查询一个商品
     */
    ShopProduct selectOne(ShopProductQuery query);



    /**
     * APP用户的小程序商品管理列表
     * <p>query.store != null </p>
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/26 11:09
     */
    Map<String, Object> manageList(ShopProductQuery query);



    /**
     * 批量更新
     * <p>
     *     对商品的更新,编辑的统一接口
     * </p>
     *
     * @param updateType 更新类型
     * @param shopProductQuery 更新的参数
     * @return boolean
     * @author Charlie
     * @date 2018/11/26 20:14
     */
    Map<String, Object> update(ShopProductUpdTypeEnum updateType, ShopProductQuery shopProductQuery);

    /**
     * 商品sku列表
     *
     * @param storeId 门店id
     * @param shopProductId 商品id
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/27 19:39
     */
    Map<String,Object> listSkuByShopProductId(Long storeId, Long shopProductId);
}
