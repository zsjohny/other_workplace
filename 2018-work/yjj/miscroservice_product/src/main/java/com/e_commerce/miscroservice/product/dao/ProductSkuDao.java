package com.e_commerce.miscroservice.product.dao;

import com.e_commerce.miscroservice.product.entity.ProductSku;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/28 16:01
 * @Copyright 玖远网络
 */
public interface ProductSkuDao{


    /**
     * 查询小程序自营商品的sku
     *
     * @param shopProductId 门店商品id
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ProductSku>
     * @author Charlie
     * @date 2018/11/28 16:05
     */
    List<ProductSku> findShopProductSku(Long shopProductId, List<Integer> statusList);



    /**
     * 更新库存
     *
     * @param modifySku modifySku
     * @return int
     * @author Charlie
     * @date 2018/11/28 16:44
     */
    int updRemainCountById(ProductSku modifySku);


    /**
     * 查找sku
     *
     * @param skuId skuId
     * @return com.e_commerce.miscroservice.product.entity.ProductSku
     * @author Charlie
     * @date 2018/12/7 14:26
     */
    ProductSku findById(Long skuId);



    /**
     * 批量删除sku
     *
     * @param skuIds skuIds
     * @author Charlie
     * @date 2018/9/4 21:37
     */
    int deleteShopProductSkuByIds(List<Long> skuIds);

    /**
     * 根据颜色尺码查询小程序用户的sku
     *
     * @param shopProductId shopProductId
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ProductSku>
     * @author Charlie
     * @date 2018/12/7 14:59
     */
    List<ProductSku> findShopProductSkus(Long shopProductId);


    /**
     * 查找小程序商品sku
     * <p>不会查找那些奇奇怪怪的字段</p>
     *
     * @param shopProductId shopProductId
     * @param statusList 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ProductSku>
     * @author Charlie
     * @date 2019/1/16 17:07
     */
    List<ProductSku> findSimpleOfShopProductId(Long shopProductId, List<Integer> statusList);



    /**
     * 查找供应商商品sku
     * <p>不会查找那些奇奇怪怪的字段</p>
     *
     * @param supplierProductId supplierProductId
     * @param statusList 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ProductSku>
     * @author Charlie
     * @date 2019/1/16 17:07
     */
    List<ProductSku> findSimpleOfSupplierProductId(Long supplierProductId, List<Integer> statusList);
}
