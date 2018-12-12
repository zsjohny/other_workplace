package com.finace.miscroservice.activity.service;

import com.finace.miscroservice.commons.utils.Response;

public interface ShopService {

    Response showCommodities(Integer page);

    Response showShopHome(String userId);

    Response buyCommodity(Integer id, String userId);

    Response commodityRecord(Integer page, String userId);

    Response showCommodityDetailed(String userId, Integer id);

    Response buyRecord(Integer page, String userId);
}
