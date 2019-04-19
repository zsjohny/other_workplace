package com.jiuyuan.service.common.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.store.SalesVolumeProductMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductMapper;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.RestrictionActivityProduct;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.util.BizException;
import com.jiuyuan.util.BizUtil;
import com.store.entity.ProductVOShop;
import com.store.entity.SalesVolumeProduct;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TOTO
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/19 22:11
 * @Copyright 玖远网络
 */
@Service("productMonitorService")
public class ProductMonitorServiceImpl implements IProductMonitorService {

    @Resource(name = "salesVolumeProductMapper")
    private SalesVolumeProductMapper salesVolumeProductMapper;

    @Resource(name = "productNewMapper")
    private ProductNewMapper productNewMapper;

    @Resource(name = "restrictionActivityProductMapper")
    private RestrictionActivityProductMapper restrictionActivityProductMapper;


    /**
     * 返回某一批商品的统计信息
     *
     * @param productIds productIds
     * @param productType productType  1是限时抢购商品 2是普通商品
     * @author Aison
     * @date 2018/6/19 22:17
     */
    @Override
    public Map<Long,SalesVolumeProduct> productMonitorMap(List<Long> productIds, Integer productType) {

        if(BizUtil.hasEmpty(productIds,productType)) {
            throw BizException.defulat().msg("参数错误");
        }
        Map<String,Object> param = new HashMap<>();
        param.put("productIds",productIds);
        param.put("productType",productType);
       return salesVolumeProductMapper.selectProductMonitor(param);
    }

    /**
     * 填充销量 普通商品
     *
     * @param productMaps productMaps
     * @param keyId keyId
     * @author Aison
     * @date 2018/6/19 22:24
     */
    @Override
    public void fillProductMonitor(List<Map<String,Object>> productMaps,String keyId) {
      try{
          List<Long> ids = new ArrayList<>();
          for (Map<String, Object> productMap : productMaps) {
              ids.add(Long.valueOf(productMap.get(keyId).toString()));
          }
          Map<Long,SalesVolumeProduct> monitorMap =  productMonitorMap(ids,1);
          for (Map<String, Object> productMap : productMaps) {
              Long product =  Long.valueOf(productMap.get(keyId).toString());
              Object realCount = productMap.get("saleTotalCount");
              Long realCountLong = realCount ==null ? 0 :  Long.valueOf(realCount.toString());
              SalesVolumeProduct monitor = monitorMap.get(product);
              Long simulatedCount = monitor == null ? 0 : monitor.getSalesVolume();
              realCountLong = simulatedCount + realCountLong;
              productMap.put("salesVolume",realCountLong);
              productMap.put("simulatedCount",simulatedCount);
          }
      }catch (Exception e) {
          e.printStackTrace();
      }
    }



    @Override
    public void fillOperatorProductList(List<Map<String, Object>> productMaps, String keyId) {

        try{
            List<Long> ids = new ArrayList<>();
            for (Map<String, Object> productMap : productMaps) {
                ids.add(Long.valueOf(productMap.get(keyId).toString()));
            }
            Map<Long,SalesVolumeProduct> monitorMap =  productMonitorMap(ids,1);
            Map<Long,SalesVolumeProduct> activeMonitor =  productMonitorMap(ids,2);
            for (Map<String, Object> productMap : productMaps) {
                Long product =  Long.valueOf(productMap.get(keyId).toString());
                Object realCount = productMap.get("saleTotalCount");
                Long realCountLong = realCount ==null ? 0 :  Long.valueOf(realCount.toString());
                SalesVolumeProduct monitor = monitorMap.get(product);
                Long simulatedCount = monitor == null ? 0 : monitor.getSalesVolume();
                realCountLong = simulatedCount + realCountLong;

                // 活动商品的销量
                SalesVolumeProduct activityProduct = activeMonitor.get(product);
                Long activityCount = activityProduct == null ? 0 : activityProduct.getSalesVolume();
                productMap.put("activitySalesVolume",activityCount);

                productMap.put("salesVolume",realCountLong + activityCount);
                productMap.put("simulatedCount",simulatedCount);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 为某个商品添加统计信息
     *
     * @param proMap proMap
     * @param activityId activityId
     * @author Aison
     * @date 2018/6/20 9:06
     */
    @Override
    public void fillProMonitorSingle(Map<String,Object> proMap,SalesVolumeProduct salesVolumeProduct,Long activityId) {
       try{
           ProductVOShop product = (ProductVOShop) proMap.get("product");
           long realSealCount = product.getSaleTotalCount();
           // 是活动商品
           if(activityId !=null && activityId>0) {
               RestrictionActivityProduct activityProduct = restrictionActivityProductMapper.selectById(activityId);
               realSealCount = activityProduct == null ? 0 : activityProduct.getSaleCount();
           }
           realSealCount = salesVolumeProduct == null ?  realSealCount : salesVolumeProduct.getSalesVolume()+realSealCount;
           product.setSalesVolume(realSealCount);
       }catch (Exception e) {
           e.printStackTrace();
       }
    }

    /**
     * 某个商品的map添加销量
     *
     * @param productMap productMap
     * @author Aison
     * @date 2018/6/20 17:32
     */
    @Override
    public void fillProductMonitorProductMap(Map<String, Object> productMap,String key) {
          try{
              Object realCount = productMap.get("saleTotalCount");
              Long realCountLong = realCount ==null ? 0 :  Long.valueOf(realCount.toString());
              Long productId = Long.valueOf(productMap.get(key).toString());
              productMap.put("salesVolume",realCountLong + getSalesMaps(productId,1));
          }catch (Exception e) {
              e.printStackTrace();
          }
    }

    /**
     * 活动商品添加销量
     * activityProductId
     * @param activitys activity
     * @author Aison
     * @date 2018/6/20 18:05
     */
    @Override
    public void fillActivityProduct(List<Map<String, Object>> activitys,String keyVal) {
        try{
            if(activitys !=null) {
                for (Map<String, Object> activity : activitys) {
                    Long activityId = Long.valueOf(activity.get(keyVal).toString());
                    RestrictionActivityProduct activityProduct = restrictionActivityProductMapper.selectById(activityId);
                    Integer saleCount = activityProduct == null ? 0 : activityProduct.getSaleCount();
                    Long simulatedCount =  getSalesMaps(activityId,2);
                    activity.put("salesVolume",saleCount + simulatedCount);
                    activity.put("simulatedCount",simulatedCount);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过商品id和商品类型获取统计信息
     *
     * @param id id
     * @param type type
     * @author Aison
     * @date 2018/6/20 18:21
     */
    @Override
    public SalesVolumeProduct getByProductIdAndType (Long id,Integer type) {

        SalesVolumeProduct product = new SalesVolumeProduct();
        product.setProductId(id);
        product.setProductType(type);
        return  salesVolumeProductMapper.selectOne(product);
    }

    private Long getSalesMaps(Long id,Integer type ) {
        SalesVolumeProduct salesVolumeProduct = getByProductIdAndType(id,type);
        return salesVolumeProduct == null ? 0 : salesVolumeProduct.getSalesVolume();
    }


    /**
     * 分页的添加商品统计信息
     *
     * @param page page
     * @author Aison
     * @date 2018/6/20 19:16
     */
    @Override
    public Page<Map<String,Object>> fillPageProduct(Page<ProductNew> page) {

        List<Long> ids = new ArrayList<>();
        for (ProductNew productNew : page.getRecords()) {
            ids.add(productNew.getId());
        }

        Map<Long,SalesVolumeProduct> salesVolumeProductMap =  productMonitorMap(ids,1);
        List<Map<String,Object>> products = new ArrayList<>();
        for (ProductNew productNew : page.getRecords()) {
            Map<String,Object> productMap = BizUtil.bean2MapAllField(productNew);
            long saleCount = productNew.getSaleTotalCount();
            SalesVolumeProduct salesVolumeProduct = salesVolumeProductMap.get(productNew.getId());
            saleCount = salesVolumeProduct == null? saleCount : saleCount + salesVolumeProduct.getSalesVolume();
            productMap.put("salesVolume",saleCount);
            try{
                JSONArray array = JSON.parseArray(productNew.getDetailImages());
                if (array == null || array.size()==0) {
                    productMap.put("firstDetailImage","");
                } else {
                    productMap.put("firstDetailImage",array.get(0));
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            products.add(productMap);
        }
        Page<Map<String,Object>> newPage = new Page<>();
        newPage.setTotal(page.getTotal());
        newPage.setSize(page.getSize());
        newPage.setCurrent(page.getCurrent());
        newPage.setRecords(products);
        return newPage;
    }

    /**
     * 模板那边的统计填充
     *
     * @param jsonArray jsonArray
     * @author Aison
     * @date 2018/6/21 9:02
     */
    @Override
    public void fillTemplateProduct(JSONArray jsonArray) {

        if(jsonArray == null || jsonArray.size()==0) {
            return ;
        }
        int size = jsonArray.size();
        try{
            List<Long> ids = new ArrayList<>();
            JSONArray jsonObjects = new JSONArray();
            for(int i=0;i<size;i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray products =  jsonObject.getJSONArray("products");
                if(products ==null || products.size() == 0) {
                    return;
                }
                jsonObjects.addAll(products);
                for (int z=0; z<products.size();z++) {
                    JSONObject jsonObject1 = products.getJSONObject(z);
                    ids.add(jsonObject1.getLong("id"));
                }

            }
            EntityWrapper entityWrapper = new EntityWrapper();
            entityWrapper.in("id",ids);
            List<ProductNew> productNews =  productNewMapper.selectList(entityWrapper);
            Map<Long,ProductNew> longProductNewMap = new HashMap<>();
            for (ProductNew productNew : productNews) {
                longProductNewMap.put(productNew.getId(),productNew);
            }

            Map<Long,SalesVolumeProduct> salesVolumeProductMap =  productMonitorMap(ids,2);
            for (int i=0; i<jsonObjects.size();i++) {
                JSONObject jsonObject = jsonObjects.getJSONObject(i);
                Long id = jsonObject.getLong("id");
                SalesVolumeProduct salesVolumeProduct = salesVolumeProductMap.get(id);
                ProductNew productNew = longProductNewMap.get(id);
                long saleCount = productNew == null ? 0 : productNew.getSaleTotalCount();
                saleCount = salesVolumeProduct == null ? saleCount : salesVolumeProduct.getSalesVolume()+saleCount;
                jsonObject.put("salesVolume",saleCount);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



}
