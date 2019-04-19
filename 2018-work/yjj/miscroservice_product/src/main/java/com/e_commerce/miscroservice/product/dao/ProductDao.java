package com.e_commerce.miscroservice.product.dao;

import com.e_commerce.miscroservice.commons.entity.application.order.Product;
import com.e_commerce.miscroservice.product.entity.ShopProduct;
import com.e_commerce.miscroservice.product.vo.LiveProductVO;
import com.e_commerce.miscroservice.product.vo.ProductVO;
import com.e_commerce.miscroservice.product.vo.SkuOfProductDTO;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/14 16:11
 * @Copyright 玖远网络
 */
public interface ProductDao {


    /**
     * 直播商品选择列表
     *
     * @param vo vo
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/14 16:25
     */
    List<Map<String,Object>> listPlatformLiveSelectProducts(ProductVO vo);


    /**
     * 查询供应商平台商品的sku信息
     *
     * @param productIds 供应商商品id
     * @return key 供应商商品id, value sku信息
     * @author Charlie
     * @date 2019/1/15 14:43
     */
    Map<Long, SkuOfProductDTO> listSkuBySupplierProductIds(List<Long> productIds);



    /**
     * 查询商品价格
     *
     * @param shopProductIds shopProductIds
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.order.Product>
     * @author Charlie
     * @date 2019/1/14 19:22
     */
    List<Product> listByIds4InitLiveProduct(List<Long> shopProductIds);


    /**
     * 查询商品的部分信息
     *
     * @param ids ids
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.order.Product>
     * @author Charlie
     * @date 2019/1/16 10:04
     */
    List<Product> findSimpleInfoByIds(List<Long> ids);

    /**
     * 查找商品图片
     *
     * @param id id
     * @return com.e_commerce.miscroservice.commons.entity.application.order.Product
     * @author Charlie
     * @date 2019/1/17 9:57
     */
    Product findImg(Long id);
}
