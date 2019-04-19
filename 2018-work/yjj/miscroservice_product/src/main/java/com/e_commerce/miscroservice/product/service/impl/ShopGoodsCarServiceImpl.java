package com.e_commerce.miscroservice.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.e_commerce.miscroservice.commons.enums.product.ShopGoodsCarStatusEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.product.dao.LiveProductDao;
import com.e_commerce.miscroservice.product.dao.ShopGoodsCarDao;
import com.e_commerce.miscroservice.product.entity.LiveProduct;
import com.e_commerce.miscroservice.product.entity.ShopGoodsCar;
import com.e_commerce.miscroservice.product.mapper.ShopGoodsCarMapper;
import com.e_commerce.miscroservice.product.service.ShopGoodsCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 17:32
 * @Copyright 玖远网络
 */
@Service
public class ShopGoodsCarServiceImpl implements ShopGoodsCarService{

    /**
     * 购物车状态:正常
     */
    private static final int CAR_SKU_STATUS_NORMAL = 1;


    private Log logger = Log.getInstance(ShopGoodsCarServiceImpl.class);

    @Autowired
    private LiveProductDao liveProductDao;
    @Autowired
    private ShopGoodsCarMapper shopGoodsCarMapper;

    @Autowired
    private ShopGoodsCarDao shopGoodsCarDao;



    /**
     * 将正常状态的购物车状态设为失效
     *
     * @param shopProductId shopProductId
     * @param storeId 门店用户id
     * @author Charlie
     * @date 2018/11/27 17:39
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void adviceGoodsCarThisProductHasDisabled(Long shopProductId, Long storeId) {
        //不要直接改, 先查再改, 避免sql没执行
        List<ShopGoodsCar> shopGoodsCars = MybatisOperaterUtil.getInstance ().finAll (
                new ShopGoodsCar (),
                new MybatisSqlWhereBuild (ShopGoodsCar.class)
                        .eq (ShopGoodsCar::getShopProductId, shopProductId)
                        .eq (ShopGoodsCar::getStoreId, shopProductId)
                        .eq (ShopGoodsCar::getCarSukStatus, CAR_SKU_STATUS_NORMAL)
        );

        logger.info ("将正常状态的购物车状态设为失效 size={}", shopGoodsCars.size ());
        if (shopGoodsCars.isEmpty ()) {
            return;
        }

        List<Long> carIds = new ArrayList<> (shopGoodsCars.size ());
        shopGoodsCars.forEach (car->{
            carIds.add (car.getId ());
        });

        shopGoodsCarDao.batchSetStatusByIds (carIds, ShopGoodsCarStatusEnum.DISABLED.getCode ());
    }




    /**
     * 将购物车设为失效
     *
     * @param skuIds skuIds
     * @param storeId storeId
     * @author Charlie
     * @date 2018/9/4 19:33
     */
    @Override
    public void adviceGoodsCarSkuHasDisabled(List<Long> skuIds, Long storeId) {
        int rec = 0;
        if (! skuIds.isEmpty ()) {
            ShopGoodsCar updInfo = new ShopGoodsCar ();
            updInfo.setCarSukStatus (2);
            updInfo.setLastUpdateTime (System.currentTimeMillis ());
            rec = MybatisOperaterUtil.getInstance ().update (
                updInfo,
                    new MybatisSqlWhereBuild (ShopGoodsCar.class)
                    .in (ShopGoodsCar::getProductSkuId, skuIds.toArray ())
                    .eq (ShopGoodsCar::getStoreId, storeId)
                    .eq (ShopGoodsCar::getCarSukStatus, 1)
            );

        }
        logger.info ("将关联sku的购物车设为失效 skuIds[{}].storeId[{}].rec[{}]", skuIds, storeId, rec);
    }






    /**
     * 小程序用户购物车列表
     *
     * @param memberId memberId
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/21 9:42
     */
    @Override
    public List<Map<String, Object>> listShopCar(Long memberId) {
        List<Map<String,Object>> cartList = shopGoodsCarMapper.shopGoodsCarList(memberId, Arrays.asList(1,2));
        for (Map<String, Object> stringObjectMap : cartList) {
            Object mainImg =  stringObjectMap.get("mainImg");
            if (mainImg != null) {
                JSONArray array =  JSONArray.parseArray(mainImg.toString());
                if(array!=null && array.size()>0) {
                    stringObjectMap.put("mainImg",array.get(0));
                }
            }
        }

        //覆盖填充直播商品
        List<Long> lpIdList = cartList.stream().mapToLong(carMap -> Long.parseLong(carMap.get("liveProductId").toString())).boxed().collect(Collectors.toList());
        //删除默认值
        lpIdList.remove(0L);
        if (! lpIdList.isEmpty()) {
            List<LiveProduct> lpList = liveProductDao.listByIds(lpIdList);
            Map<Long, List<LiveProduct>> lpGroupByLpId = lpList.stream().collect(Collectors.groupingBy(lp -> lp.getId()));
            //填充
            cartList.stream().forEach(cartMap->{
                Long liveProductId = Long.parseLong(cartMap.get("liveProductId").toString());
                if (liveProductId > 0) {
                    List<LiveProduct> liveProducts = lpGroupByLpId.get(liveProductId);
                    if (! liveProducts.isEmpty()) {
                        LiveProduct liveProduct = liveProducts.get(0);
                        cartMap.put("shopPrice", liveProduct.getLivePrice());
                    }
                }
            });
        }
        return cartList;
    }


}
