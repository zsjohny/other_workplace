package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.product.dao.ProductSkuDao;
import com.e_commerce.miscroservice.product.entity.ProductSku;
import com.e_commerce.miscroservice.product.mapper.ProductSkuMapper;
import com.e_commerce.miscroservice.product.vo.ProductSkuQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/28 16:01
 * @Copyright 玖远网络
 */
@Component
public class ProductSkuDaoImpl implements ProductSkuDao{

    @Autowired
    private ProductSkuMapper productSkuMapper;

    /**
     * 查询小程序自营商品的sku
     *
     * @param shopProductId shopProductId
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ProductSku>
     * @author Charlie
     * @date 2018/11/28 16:05
     */
    @Override
    public List<ProductSku> findShopProductSku(Long shopProductId, List<Integer> statusList) {
        ProductSkuQuery query = new ProductSkuQuery ();
        query.setStatusList (statusList);
        query.setOwnType (2);
        query.setWxaProductId (shopProductId);
        return productSkuMapper.selectList (query);
    }

    /**
     * 更新库存
     *
     * @param modifySku modifySku
     * @return int
     * @author Charlie
     * @date 2018/11/28 16:44
     */
    @Override
    public int updRemainCountById(ProductSku modifySku) {
        modifySku.setUpdateTime (System.currentTimeMillis ());
        return productSkuMapper.updateByPrimaryKey (modifySku);
    }



    /**
     * 查找sku
     *
     * @param skuId skuId
     * @return com.e_commerce.miscroservice.product.entity.ProductSku
     * @author Charlie
     * @date 2018/12/7 14:26
     */
    @Override
    public ProductSku findById(Long skuId) {
        ProductSkuQuery query = new ProductSkuQuery ();
        query.setId (skuId);
        List<ProductSku> skuList = productSkuMapper.selectList (query);
        return skuList.isEmpty () ? null : skuList.get (0);
    }


    /**
     * 批量删除sku
     *
     * @param skuIds skuIds
     * @author Charlie
     * @date 2018/9/4 21:37
     */
    @Override
    public int deleteShopProductSkuByIds(List<Long> skuIds) {
        return productSkuMapper.deleteShopProductSkuByIds(skuIds);
    }




    /**
     * 根据颜色尺码查询小程序用户的sku
     *
     * @param shopProductId shopProductId
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ProductSku>
     * @author Charlie
     * @date 2018/12/7 14:59
     */
    @Override
    public List<ProductSku> findShopProductSkus(Long shopProductId) {
        ProductSkuQuery query = new ProductSkuQuery ();
        query.setOwnType (2);
        query.setWxaProductId (shopProductId);
        return productSkuMapper.selectList (query);
    }

    @Override
    public List<ProductSku> findSimpleOfShopProductId(Long shopProductId, List<Integer> statusList) {
        return productSkuMapper.findSimpleOfProductId(shopProductId, statusList, true);
    }

    @Override
    public List<ProductSku> findSimpleOfSupplierProductId(Long supplierProductId, List<Integer> statusList) {
        return productSkuMapper.findSimpleOfProductId(supplierProductId, statusList, false);
    }


}
