package com.e_commerce.miscroservice.product.dao;

import com.e_commerce.miscroservice.commons.entity.product.ProductSkuSimpleVo;
import com.e_commerce.miscroservice.product.entity.ShopProductTempInfo;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/26 19:23
 * @Copyright 玖远网络
 */
public interface ShopProductTempInfoDao{


    /**
     * 根据商品id查询
     *
     * @param shopProductId shopProductId
     * @return com.e_commerce.miscroservice.product.entity.ShopProductTempInfo
     * @author Charlie
     * @date 2018/11/26 19:30
     */
    ShopProductTempInfo findByShopProductId(Long shopProductId);




    /**
     * 根据id更新
     *
     * @param updInfo 更新信息
     * @return int
     * @author Charlie
     * @date 2018/11/28 15:34
     */
    int updateById(ShopProductTempInfo updInfo);


    /**
     * 更新sku库存, 忽视没有的没有的sku
     *
     * @param tempInfo 历史sku信息
     * @param requestSkus 新的库存信息
     * @author Charlie
     * @date 2018/11/28 16:50
     */
    void updateRemainCount(ShopProductTempInfo tempInfo, List<ProductSkuSimpleVo> requestSkus);
}
