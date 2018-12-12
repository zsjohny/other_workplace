package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.CommoditiesLogPO;
import com.finace.miscroservice.activity.po.CommoditiesPO;
import com.finace.miscroservice.activity.po.CommodityBuyRecordsPO;

import java.util.List;

public interface ShopDao {
    List<CommoditiesPO> showCommodities();

    CommoditiesPO findCommodityByid(Integer id);

    void subCommodityCount(Integer id);

    void addShopLog(Integer id, String userId, String orderNo, String nowDateStr, Integer peas, String context,Integer status);

    List<CommoditiesLogPO> commodityRecord(String userId);

    CommoditiesPO showCommodityDetailed(Integer id);

    List<CommodityBuyRecordsPO> buyRecord(String userId);

    Integer CountShopLogByUserId(String userId);
}
