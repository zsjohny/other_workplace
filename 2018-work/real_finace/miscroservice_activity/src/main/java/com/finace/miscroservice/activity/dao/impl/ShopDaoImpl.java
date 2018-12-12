package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.ShopDao;
import com.finace.miscroservice.activity.mapper.ShopMapper;
import com.finace.miscroservice.activity.po.CommoditiesLogPO;
import com.finace.miscroservice.activity.po.CommoditiesPO;
import com.finace.miscroservice.activity.po.CommodityBuyRecordsPO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ShopDaoImpl implements ShopDao {

    @Resource
    private ShopMapper shopMapper;

    @Override
    public List<CommoditiesPO> showCommodities() {
        return shopMapper.showCommodities();
    }

    @Override
    public CommoditiesPO findCommodityByid(Integer id) {
        return shopMapper.findCommodityByid(id);
    }

    @Override
    @Transactional
    public void subCommodityCount(Integer id) {
        shopMapper.subCommodityCount(id);
    }

    @Override
    @Transactional
    public void addShopLog(Integer id, String userId, String orderNo, String nowDateStr, Integer peas, String context,Integer status) {
        shopMapper.addShopLog(id,userId,orderNo,nowDateStr,peas,context,status);
    }

    @Override
    public List<CommoditiesLogPO> commodityRecord(String userId) {
        return shopMapper.commodityRecord(userId);
    }

    @Override
    public CommoditiesPO showCommodityDetailed(Integer id) {
        return shopMapper.showCommodityDetailed(id);
    }

    @Override
    public List<CommodityBuyRecordsPO> buyRecord(String userId) {
        return shopMapper.buyRecord(userId);
    }

    @Override
    public Integer CountShopLogByUserId(String userId) {
        return shopMapper.CountShopLogByUserId(userId);
    }

}
