package com.jiuy.rb.service.impl.product;

import com.jiuy.rb.mapper.product.ShopGoodsCarRbMapper;
import com.jiuy.rb.service.product.IShopGoodsCarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/4 19:21
 * @Copyright 玖远网络
 */
@Service("shopGoodsCarServiceImpl")
public class ShopGoodsCarServiceImpl implements IShopGoodsCarService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopGoodsCarServiceImpl.class);

    @Autowired
    private ShopGoodsCarRbMapper shopGoodsCarMapper;

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
            rec = shopGoodsCarMapper.updateGoodsCarDisabled (storeId, skuIds);
        }
        LOGGER.info ("将关联sku的购物车设为失效 skuIds[{}].storeId[{}].rec[{}]", skuIds, storeId, rec);
    }
}
