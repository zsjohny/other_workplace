package com.e_commerce.miscroservice.product.mapper;

import com.e_commerce.miscroservice.commons.entity.application.order.Product;
import com.e_commerce.miscroservice.commons.entity.product.ShopProductQuery;
import com.e_commerce.miscroservice.product.entity.ProductSku;
import com.e_commerce.miscroservice.product.entity.ShopProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/22 17:33
 * @Copyright 玖远网络
 */
@Mapper
public interface ShopProductMapper{

    ShopProduct selectOne(ShopProductQuery query);

    /**
     * app店家的小程序商品查询
     *
     * @param query query
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/26 11:13
     */
    List<Map<String, Object>> manageList(ShopProductQuery query);

    /**
     * 查询商品详情,允许扩展
     *
     * @param query
     * @return
     */
    List<Map<String,Object>> productDetailList(ShopProductQuery query);

    /**
     * 查询是否已经上架
     */
    List<Long>  selectShopProductId(@Param("ids")List<Long> ids,@Param("storeId")Long storeId);



    /**
     * 直播商品选择列表
     *
     * @param query query
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/14 16:25
     */
    List<Map<String,Object>> listLiveSelectProducts(ShopProductQuery query);

    /**
     * 初始化直播商品需要的小程序商品信息
     *
     * @param shopProductIds shopProductIds
     * @return 部分字段
     * @author Charlie
     * @date 2019/1/15 11:16
     */
    List<ShopProduct> listByIds4InitLiveProduct(@Param( "shopProductIds" ) List<Long> shopProductIds);


    /**
     * 查询个别列
     * <p>名称,款号,价格,橱窗图</p>
     * @param ids ids
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ShopProduct>
     * @author Charlie
     * @date 2019/1/16 9:28
     */
    List<ShopProduct> findSimpleInfoByIds(@Param( "ids" ) List<Long> ids);


    /**
     * 查询展示直播商品的信息
     *
     * @param id id
     * @return com.e_commerce.miscroservice.product.entity.ShopProduct
     * @author Charlie
     * @date 2019/1/16 16:37
     */
    ShopProduct findLiveProductIntroById(@Param( "id" ) Long id);


    /**
     * 查找图片
     *
     * @param id id
     * @return com.e_commerce.miscroservice.commons.entity.application.order.Product
     * @author Charlie
     * @date 2019/1/17 10:09
     */
    ShopProduct findImg(@Param( "id" ) Long id);

    /**
     * 查询基本信息
     *
     * @param id id
     * @return com.e_commerce.miscroservice.product.entity.ShopProduct
     * @author Charlie
     * @date 2019/1/17 10:22
     */
    ShopProduct findSimpleInfoById(@Param( "id" ) Long id);
}
