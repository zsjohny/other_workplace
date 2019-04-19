package com.e_commerce.miscroservice.product.mapper;

import com.e_commerce.miscroservice.product.vo.StoreShoppingCartQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/30 17:53
 * @Copyright 玖远网络
 */
@Mapper
public interface StoreShoppingCartMapper{


    /**
     * 查询购物车的品牌(按修改时间降序)--brandId,brandName
     *
     * @param query query
     * @return java.util.List<java.util.Map
     * @author Charlie
     * @date 2018/12/3 9:36
     */
    List<Map<String, Object>> listMyCartBrand(StoreShoppingCartQuery query);


    /**
     * 	购物车商品列表
     *
     * @param brandIdList brandIdList
     * @param storeId storeId
     * @return java.util.List
     * @author Charlie
     * @date 2018/12/3 10:52
     */
    List<Map<String,Object>> listMyCartProduct(@Param ("brandIdList") List<Long> brandIdList, @Param ("storeId") Long storeId);


    /**
     * 	购物车sku列表
     *
     * @param productIdList brandIdList
     * @param storeId storeId
     * @return java.util.List
     * @author Charlie
     * @date 2018/12/3 10:52
     */
    List<Map<String,Object>> listMyCartSku(@Param ("productIdList") List<Long> productIdList, @Param ("storeId") Long storeId);



    /**
     * 	删除所有失效的购物车
     *
     * @param query storeId 必填
     * @return java.util.List
     * @author Charlie
     * @date 2018/12/3 10:52
     */
    int deleteCarts(StoreShoppingCartQuery query);


    /**
     * 根据用户id,购物车id, 更新用户购物车购买数量
     *
     * @param storeId 门店id
     * @param updateTime 更新时间
     * @param id 购物车id
     * @param buyCount 购买数量
     * @return int
     * @author Charlie
     * @date 2018/12/3 16:58
     */
    int updateBuyCountById(@Param ("storeId") Long storeId,
                           @Param ("updateTime") long updateTime,
                           @Param ("id") Long id,
                           @Param ("buyCount") Integer buyCount);

    /**
     * 商品款号数量
     *
     * @param storeId storeId
     * @return int
     * @author Charlie
     * @date 2019/2/16 16:21
     */
    int clothesNumberCount(@Param( "storeId" ) Long storeId);


}
