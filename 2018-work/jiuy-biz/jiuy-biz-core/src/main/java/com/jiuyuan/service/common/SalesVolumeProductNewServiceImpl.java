package com.jiuyuan.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.store.SalesVolumeProductMapper;
import com.store.entity.SalesVolumeProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/7/31 16:29
 * @Copyright 玖远网络
 */
@Service
public class SalesVolumeProductNewServiceImpl implements ISalesVolumeProductNewService{
    @Autowired
    private SalesVolumeProductMapper salesVolumeProductMapper;

    /**
     * 查询
     *
     * @param type 1app普通商品,2app限时抢购商品,50门店用户商品
     * @param productIds 商品主键
     * @return java.util.List<com.store.entity.SalesVolumeProduct>
     * @author Charlie
     * @date 2018/7/31 16:31
     */
    @Override
    public List<SalesVolumeProduct> listSalesVolumeService(Integer type, List<Long> productIds) {
        if (productIds.isEmpty ()) {
            return new ArrayList<> ();
        }

        Wrapper<SalesVolumeProduct> query = new EntityWrapper<> ();
        query.in ("product_id", productIds);
        query.eq ("product_type", type);
        return salesVolumeProductMapper.selectList (query);
    }
}
