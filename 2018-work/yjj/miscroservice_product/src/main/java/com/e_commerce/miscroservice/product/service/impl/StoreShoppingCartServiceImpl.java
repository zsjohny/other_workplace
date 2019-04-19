package com.e_commerce.miscroservice.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.MapHelper;
import com.e_commerce.miscroservice.product.dao.SupplierUserProductConfigDao;
import com.e_commerce.miscroservice.product.mapper.StoreShoppingCartMapper;
import com.e_commerce.miscroservice.product.service.StoreShoppingCartService;
import com.e_commerce.miscroservice.product.vo.StoreShoppingCartQuery;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/30 17:14
 * @Copyright 玖远网络
 */
@Service
public class StoreShoppingCartServiceImpl implements StoreShoppingCartService{


    private Log logger = Log.getInstance (StoreShoppingCartServiceImpl.class);


    @Autowired
    private SupplierUserProductConfigDao supplierUserProductConfigDao;
    @Autowired
    private StoreShoppingCartMapper storeShoppingCartMapper;


    /**
     * 我的购物车
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/30 17:16
     */
    @Override
    public Map<String, Object> listMyCart(StoreShoppingCartQuery query) {

        ErrorHelper.declareNull (query.getStoreId (), "未找到当前用户信息");

        Map<String, Object> result = new HashMap<> (2);

        //排序规则,每个品牌最新的添加进购物的商品往上排
        //sql 查询购物车的品牌(按修改时间降序)--brandId,brandName
        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        List<Map<String, Object>> brandList = storeShoppingCartMapper.listMyCartBrand (query);
        if (brandList.isEmpty ()) {
            result.put("dataList", new SimplePage<>(Collections.EMPTY_LIST));
            return result;
        }

        //品牌ids
        List<Long> brandIdList = brandList.stream ()
                .mapToLong (value -> {
                    Object brandIdObj = value.get ("brandId");
                    return brandIdObj == null ? 0 : Long.parseLong (brandIdObj.toString ());
                })
                .boxed ()
                .collect (Collectors.toList ());

        //查询购物车详情
        List<Map<String, Object>> productList = storeShoppingCartMapper.listMyCartProduct (brandIdList, query.getStoreId ());
        Map<Object, List<Map<String, Object>>> productGroupByBrand = productList.stream ().collect (Collectors.groupingBy ((Function<Object, Object>) object -> ((Map) object).get ("brandId")));

        //填充商品列表
        for (Map<String, Object> brand : brandList) {
            Object brandBrandId = brand.get ("brandId");
            brand.put ("productList", productGroupByBrand.get (brandBrandId));
        }

        //填充混批
        Map<Long, String> brandTips = supplierUserProductConfigDao.listTholesaleTipByBrandIds(brandIdList);
        brandList.stream()
                .forEach(brand-> Optional.ofNullable(brand.get ("brandId"))
                        .ifPresent(brandId-> Optional.ofNullable(brandTips.get(Long.parseLong(brandId.toString())))
                                .ifPresent(tip-> brand.put("tholesaleTip", tip))));

        //查询购物车下的sku
        //商品ids
        List<Long> productIdList = productList.stream ()
                .mapToLong (value -> {
                    Object productId = value.get ("productId");
                    return productId == null ? 0 : Long.parseLong (productId.toString ());
                })
                .boxed ()
                .collect (Collectors.toList ());

        //填充sku列表
        List<Map<String, Object>> skuList = storeShoppingCartMapper.listMyCartSku (productIdList, query.getStoreId ());
        Map<Object, List<Map<String, Object>>> skuGroupByProduct = skuList.stream ().collect (Collectors.groupingBy ((Function<Object, Object>) object -> ((Map) object).get ("productId")));
        //商品中填充sku和skuIds
        for (Map<String, Object> product : productList) {
            List<Map<String, Object>> skusOfProduct = skuGroupByProduct.get (product.get ("productId"));
            //sku对象列表
            product.put ("skusOfProduct", skusOfProduct);
            List<Long> skuIdsOfProduct = skusOfProduct.stream ()
                    .mapToLong (value -> Long.parseLong (value.get ("skuId").toString ()))
                    .boxed ()
                    .collect (Collectors.toList ());
            //skuId列表
            product.put ("skuIdsOfProduct", skuIdsOfProduct);
        }

        //品牌页放入skuIds
        Map<Object, List<Map<String, Object>>> skuGroupByBrand = skuList.stream ().collect (Collectors.groupingBy ((Function<Object, Object>) object -> ((Map) object).get ("brandId")));
        for (Map<String, Object> brand : brandList) {
            Long brandId = Long.parseLong (brand.get ("brandId").toString ());
            List<Map<String, Object>> skuOfBrand = skuGroupByBrand.get (brandId);
            List<Long> skuIdsOfBrand = skuOfBrand.stream ().mapToLong (value -> Long.parseLong (value.get ("skuId").toString ())).boxed ().collect (Collectors.toList ());
            brand.put ("skuIdsOfBrand", skuIdsOfBrand);
        }

        result.put ("dataList", new SimplePage<>(brandList));
        return result;
    }


    /**
     * 删除所有失效的购物车
     *
     * @param query storeId
     * @author Charlie
     * @date 2018/12/3 15:43
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void deleteCarts(StoreShoppingCartQuery query) {
        Long storeId = query.getStoreId ();
        ErrorHelper.declareNull (storeId, "没有找到用户");
        //删除失效的购物车
        int rec = storeShoppingCartMapper.deleteCarts (query);
        logger.info ("userId={}删除失效的购物车,size={}", rec);
    }


    /**
     * 更新购物车购买数量
     *
     * @param storeId        门店用户
     * @param cartsJsonArray 购物车id和购买数量
     * @author Charlie
     * @date 2018/12/3 16:46
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void updateBuyCount(Long storeId, String cartsJsonArray) {
        logger.info ("user={} 更新购物车购买数量 cartsJsonArray={}", storeId, cartsJsonArray);

        if (StringUtils.isBlank (cartsJsonArray)) {
            return;
        }

        JSONArray cartsArray = JSONObject.parseArray (cartsJsonArray);

        //先校验一下json,尽量不要在事务中抛异常
        for (int i = 0; i < cartsArray.size (); i++) {
            JSONObject cartsAndCount = cartsArray.getJSONObject (i);

            Long cartId = cartsAndCount.getLong ("cartId");
            ErrorHelper.declareNull (cartId, "没有找到购物车");

            Integer buyCount = cartsAndCount.getInteger ("buyCount");
            ErrorHelper.declare (buyCount != null && buyCount >= 0, "未知的购买数量");
        }


        //再更新数量
        long updateTime = System.currentTimeMillis ();
        for (int i = 0; i < cartsArray.size (); i++) {
            JSONObject cartsAndCount = cartsArray.getJSONObject (i);

            Long id = cartsAndCount.getLong ("cartId");
            Integer buyCount = cartsAndCount.getInteger ("buyCount");
            int rec = storeShoppingCartMapper.updateBuyCountById (storeId, updateTime, id, buyCount);
            logger.info ("更新购物车购买数量 storeId={},id={},buyCount={}", storeId, id, buyCount);
            ErrorHelper.declare (rec == 1, "库存不足, 请稍后再试");
        }


    }

    @Override
    public Map<String, Object> clothesNumberCount(Long storeId) {
        return MapHelper.me(2).put("clothesNumberCount", storeShoppingCartMapper.clothesNumberCount(storeId));
    }
}
