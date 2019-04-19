package com.jiuyuan.service.common;

import com.store.entity.SalesVolumeProduct;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/7/31 16:28
 * @Copyright 玖远网络
 */
public interface ISalesVolumeProductNewService{


    /**
     * 查询
     *
     * @param type 1app普通商品,2app限时抢购商品,50门店用户商品
     * @param productIds 商品主键
     * @return java.util.List<com.store.entity.SalesVolumeProduct>
     * @author Charlie
     * @date 2018/7/31 16:31
     */
    List<SalesVolumeProduct> listSalesVolumeService(Integer type, List<Long> productIds);
}
