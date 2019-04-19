package com.e_commerce.miscroservice.product.dao;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyGoods;

import java.util.List;

public interface ProxyGoodsDao {

    List<ProxyGoods> getGoodsList(int page);

}
