package com.e_commerce.miscroservice.product.dao;

import com.e_commerce.miscroservice.commons.entity.application.order.Product;
import com.e_commerce.miscroservice.commons.entity.product.ShopProductQuery;
import com.e_commerce.miscroservice.product.entity.ProductSku;
import com.e_commerce.miscroservice.product.entity.ShopProduct;
import com.e_commerce.miscroservice.product.vo.ShopProductVO;
import com.e_commerce.miscroservice.product.vo.SkuOfProductDTO;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/26 20:56
 * @Copyright 玖远网络
 */
public interface ShopProductDao{

    /**
     * 根据id更新
     *
     * @param updInfo updInfo
     * @return int
     * @author Charlie
     * @date 2018/11/26 21:01
     */
    int updateById(ShopProduct updInfo);


    /**
     * 根据id批量查询
     *
     * @param ids ids
     * @param storeId 门店用户id
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ShopProduct>
     * @author Charlie
     * @date 2018/11/27 11:30
     */
    List<ShopProduct> findByIds(List<Long> ids, Long storeId);

    /**
     * 批量推荐
     *
     * @param request           ids
     * @param isCancleRecommend 是否取消推荐
     * @author Charlie
     * @date 2018/11/27 13:48
     */
    void batchRecommend(ShopProductQuery request, boolean isCancleRecommend);


    /**
     * 查询商品(未删除)
     *
     * @param shopProductId shopProductId
     * @param userId userId
     * @return com.e_commerce.miscroservice.product.entity.ShopProduct
     * @author Charlie
     * @date 2018/11/27 20:03
     */
    ShopProduct findById(Long shopProductId, Long userId);



    /**
     * 查找门店用户的sku
     *
     * @param storeId storeId
     * @param skuId skuId
     * @return com.e_commerce.miscroservice.product.entity.ProductSku
     * @author Charlie
     * @date 2018/12/7 14:14
     */
    ProductSku findSkuBySkuId(Long storeId, Long skuId);



    /**
     * 直播商品选择列表
     *
     * @param query query
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/14 16:25
     */
    List<Map<String,Object>> listWxaLiveSelectProducts(ShopProductQuery query);

    /**
     * 查询上架小程序商品的sku
     *
     * @param shopProductVOList shopProductVOList
     * @return key 小程序商品id, value sku信息
     * @author Charlie
     * @date 2019/1/15 17:22
     */
    Map<Long, SkuOfProductDTO> listOnSaleWxaSkuByShopProductIds(List<ShopProductVO> shopProductVOList);

    /**
     * 获取初始化小程序直播商品的信息
     *
     * @param shopProductIds shopProductIds
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.order.Product>
     * @author Charlie
     * @date 2019/1/15 10:58
     */
    List<ShopProduct> listByIds4InitLiveProduct(List<Long> shopProductIds);


    /**
     * 查询部分字段
     *
     * @param ids ids
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ShopProduct>
     * @author Charlie
     * @date 2019/1/16 11:54
     */
    List<ShopProduct> findSimpleInfoByIds(List<Long> ids);


    /**
     * 查询展示直播商品的信息
     *
     * @param id id
     * @return com.e_commerce.miscroservice.product.entity.ShopProduct
     * @author Charlie
     * @date 2019/1/16 16:37
     */
    ShopProduct findLiveProductIntroById(Long id);

    /**
     * 查找图片
     *
     * @param shopProductId shopProductId
     * @return com.e_commerce.miscroservice.commons.entity.application.order.Product
     * @author Charlie
     * @date 2019/1/17 10:09
     */
    ShopProduct findImg(Long shopProductId);


    ShopProduct findSimpleInfoById(Long shopProductId);
}
