package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.CommoditiesLogPO;
import com.finace.miscroservice.activity.po.CommoditiesPO;
import com.finace.miscroservice.activity.po.CommodityBuyRecordsPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopMapper {
    List<CommoditiesPO> showCommodities();

    void addShopLog(@Param("id") Integer id, @Param("userId") String userId, @Param("orderNo") String orderNo,
                    @Param("nowDateStr") String nowDateStr, @Param("peas") Integer peas,
                    @Param("content") String content,@Param("status") Integer status);

    void subCommodityCount(@Param("id")Integer id);

    CommoditiesPO findCommodityByid(@Param("id")Integer id);

    List<CommoditiesLogPO> commodityRecord(@Param("userId")String userId);

    CommoditiesPO showCommodityDetailed(@Param("id")Integer id);

    List<CommodityBuyRecordsPO> buyRecord(@Param("userId")String userId);

    Integer CountShopLogByUserId(@Param("userId") String userId);
}
