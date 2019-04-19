package com.e_commerce.miscroservice.product.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.product.ProductSkuSimpleVo;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.product.dao.ShopProductTempInfoDao;
import com.e_commerce.miscroservice.product.entity.ShopProductTempInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/26 19:23
 * @Copyright 玖远网络
 */
@Component
public class ShopProductTempInfoDaoImpl implements ShopProductTempInfoDao{

    private Log logger = Log.getInstance(ShopProductTempInfoDaoImpl.class);


    /**
     * 根据商品id查询
     *
     * @param shopProductId shopProductId
     * @return com.e_commerce.miscroservice.product.entity.ShopProductTempInfo
     * @author Charlie
     * @date 2018/11/26 19:30
     */
    @Override
    public ShopProductTempInfo findByShopProductId(Long shopProductId) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new ShopProductTempInfo (),
                new MybatisSqlWhereBuild (ShopProductTempInfo.class)
                        .eq (ShopProductTempInfo::getStatus, 1)
                        .eq (ShopProductTempInfo::getShopProductId, shopProductId)
        );
    }



    /**
     * 根据id更新
     *
     * @param updInfo 更新信息
     * @return int
     * @author Charlie
     * @date 2018/11/28 15:34
     */
    @Override
    public int updateById(ShopProductTempInfo updInfo) {
        return MybatisOperaterUtil.getInstance ().update (
                updInfo, new MybatisSqlWhereBuild (ShopProductTempInfo.class).eq (ShopProductTempInfo::getId, updInfo.getId ())
        );
    }




    /**
     * 更新sku库存, 忽视没有的没有的sku
     *
     * @param tempInfo 历史sku信息
     * @param requestSkus 新的库存信息
     * @author Charlie
     * @date 2018/11/28 16:50
     */
    @Override
    public void updateRemainCount(ShopProductTempInfo tempInfo, List<ProductSkuSimpleVo> requestSkus) {
        String skuJson = tempInfo.getSkuJson ();
        JSONArray skuArray = JSONObject.parseArray (skuJson);
        //是否修改
        AtomicBoolean isModify = new AtomicBoolean (false);

        for (int i = 0; i < skuArray.size (); i++) {
            JSONObject sku = skuArray.getJSONObject (i);
            Long sizeId = sku.getLong ("sizeId");
            Long colorId = sku.getLong ("colorId");
            Integer remainCount = sku.getInteger ("remainCount");

            //填充新的库存
            for (ProductSkuSimpleVo reqSku : requestSkus) {
                if (reqSku.getSizeId ().equals (sizeId) && reqSku.getColorId ().equals (colorId)) {
                    if (! reqSku.getRemainCount ().equals (remainCount)) {
                        //更新了库存
                        sku.put ("remainCount", reqSku.getRemainCount ());
                        isModify.set (true);
                        break;
                    }
                }
            }


        }


        String afterModify = skuArray.toJSONString ();
        boolean isModifySku = isModify.get ();
        logger.info ("isModifySku={}, 更新前的sku={}, 更新后的sku={}", isModifySku, skuJson, afterModify);
        if (isModifySku) {
            //修改了
            ShopProductTempInfo updInfo = new ShopProductTempInfo ();
            updInfo.setId (tempInfo.getId ());
            updInfo.setSkuJson (afterModify);
            int rec = updateById (updInfo);
            ErrorHelper.declare (rec == 1, "更新库存失败");
        }
    }
}
