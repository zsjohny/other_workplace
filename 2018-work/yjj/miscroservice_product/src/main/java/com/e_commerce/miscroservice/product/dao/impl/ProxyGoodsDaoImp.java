package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.BasePage;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyGoods;
import com.e_commerce.miscroservice.product.dao.ProxyGoodsDao;
import com.e_commerce.miscroservice.product.mapper.ProxyGoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProxyGoodsDaoImp implements ProxyGoodsDao {

    @Autowired
    private ProxyGoodsMapper proxyGoodsMapper;

    @Override
    public List<ProxyGoods> getGoodsList(int page) {
        BasePage.setPage(page);
        return proxyGoodsMapper.getGoodsList();
    }
}
