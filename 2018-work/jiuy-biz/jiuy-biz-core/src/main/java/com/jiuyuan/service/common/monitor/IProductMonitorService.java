package com.jiuyuan.service.common.monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.ProductNew;
import com.store.entity.SalesVolumeProduct;

import java.util.List;
import java.util.Map;

/**
 * 商品统计相关的接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/19 22:11
 * @Copyright 玖远网络
 */
public interface IProductMonitorService {

    /**
     * 填充销量
     *
     * @param productMaps productMaps
     * @param keyId keyId
     * @author Aison
     * @date 2018/6/19 22:24
     */
    void fillProductMonitor(List<Map<String,Object>> productMaps,String keyId);


    /**
     * 返回某一批商品的统计信息
     *
     * @param productIds productIds
     * @param productType productType
     * @author Aison
     * @date 2018/6/19 22:17
     */
    Map<Long,SalesVolumeProduct> productMonitorMap(List<Long> productIds, Integer productType);


    /**
     * 为某个商品添加统计信息
     *
     * @param proMap proMap
     * @param activityId 是否是活动商品
     * @param salesVolumeProduct salesVolumeProduct
     * @author Aison
     * @date 2018/6/20 9:06
     */
     void fillProMonitorSingle(Map<String,Object> proMap,SalesVolumeProduct salesVolumeProduct,Long activityId);


    /**
     * 某个商品的map添加销量
     *
     * @param productMap productMap
     * @param key id的key
     * @author Aison
     * @date 2018/6/20 17:32
     */
    void fillProductMonitorProductMap(Map<String,Object> productMap,String key);


    /**
     * 活动商品添加销量
     *
     * @param activity activity
     * @param  key id的key
     * @author Aison
     * @date 2018/6/20 18:05
     */
    void fillActivityProduct(List<Map<String,Object>> activity,String key);


    /**
     * 通过商品id和商品类型获取统计信息
     *
     * @param id id
     * @param type type
     * @author Aison
     * @date 2018/6/20 18:21
     */
     SalesVolumeProduct getByProductIdAndType (Long id,Integer type);


    /**
     * 分页的添加商品统计信息
     *
     * @param page page
     * @author Aison
     * @date 2018/6/20 19:16
     */
     Page<Map<String,Object>> fillPageProduct(Page<ProductNew> page);

    /**
     * 模板那边的统计填充
     *
     * @param jsonObject jsonObject
     * @author Aison
     * @date 2018/6/21 9:02
     */
    void fillTemplateProduct(JSONArray jsonObject);


    /**
     * 后台商品列表数据填充
     * @param productMaps productMaps
     * @param keyId keyId
     * @author Aison
     * @date 2018/6/25 11:40
     */
    void fillOperatorProductList(List<Map<String,Object>> productMaps,String keyId);



}
