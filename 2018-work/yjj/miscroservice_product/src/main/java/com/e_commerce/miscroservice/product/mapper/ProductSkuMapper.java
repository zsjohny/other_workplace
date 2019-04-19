package com.e_commerce.miscroservice.product.mapper;

import com.e_commerce.miscroservice.product.entity.ProductSku;
import com.e_commerce.miscroservice.product.vo.ProductSkuQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/26 15:30
 * @Copyright 玖远网络
 */
@Mapper
public interface ProductSkuMapper {

    /**
     * 批量查询商品库存
     *
     * @param ids ids
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/26 15:43
     */
    List<Map<String, Object>> findInventoryByProductIds(@Param( "ids" ) Collection<Long> ids);


    /**
     * 供应商商品总库存
     *
     * @param productId productId
     * @return int
     * @author Charlie
     * @date 2018/9/7 11:05
     */
    int supplierProductInventory(@Param( "productId" ) Long productId);

    /**
     * 门店商品总库存
     *
     * @param shopProductId shopProductId
     * @return int
     * @author Charlie
     * @date 2018/9/7 11:05
     */
    int storeProductInventory(@Param( "shopProductId" ) Long shopProductId);


    /**
     * 按条件查询
     *
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ProductSku>
     * @author Charlie
     * @date 2018/11/29 14:07
     */
    List<ProductSku> selectList(ProductSkuQuery query);

    /**
     * 根据id更新
     *
     * @param modifySku modifySku
     * @return int
     * @author Charlie
     * @date 2018/11/29 14:07
     */
    int updateByPrimaryKey(ProductSku modifySku);

    /**
     * 批量删除sku
     *
     * @param skuIds skuIds
     * @author Charlie
     * @date 2018/9/4 21:37
     */
    int deleteShopProductSkuByIds(@Param( "skuIds" ) List<Long> skuIds);


    /**
     * 查询供应商商品的sku
     *
     * @param productIds productIds
     * @param current current
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ProductSku>
     * @author Charlie
     * @date 2019/1/15 14:57
     */
    List<ProductSku> listSkuBySupplierProductIds(
            @Param( "productIds" ) List<Long> productIds,
            @Param( "current" ) Long current);



    /**
     * 查询小程序商品sku
     *
     * @param shopProductIds shopProductIds
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ProductSku>
     * @author Charlie
     * @date 2019/1/15 16:42
     */
    List<ProductSku> listSkuByShopProductIds(@Param( "shopProductIds" ) List<Long> shopProductIds);

    /**
     * 查询商品sku
     *
     * @param productId 小程序或者供应商商品id
     * @param statusList sku状态
     * @param isShopElseSupplier true:查询小程序,false:查询供应商
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ProductSku>
     * @author Charlie
     * @date 2019/1/16 17:11
     */
    List<ProductSku> findSimpleOfProductId(@Param( "productId" ) Long productId, @Param( "statusList" ) List<Integer> statusList, @Param( "isShopElseSupplier" ) boolean isShopElseSupplier);


}
