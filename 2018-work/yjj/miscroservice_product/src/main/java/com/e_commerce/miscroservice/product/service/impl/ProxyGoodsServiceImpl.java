package com.e_commerce.miscroservice.product.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyGoods;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.service.impl.BaseServiceImpl;
import com.e_commerce.miscroservice.product.dao.ProxyGoodsDao;
import com.e_commerce.miscroservice.product.service.proxy.ProxyGoodsService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class ProxyGoodsServiceImpl extends BaseServiceImpl<ProxyGoods> implements ProxyGoodsService {

    @Autowired
    ProxyGoodsDao proxyGoodsDao;

    /**
     * 描述 查询商品列表
     *
     * @param page
     * @param res
     * @param req
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyq
     * @date 2018/9/18 17:20
     */
    @Override
    public Response getGoodsList(int page, HttpServletRequest res, HttpServletResponse req) {

        List<ProxyGoods> goodsList = proxyGoodsDao.getGoodsList(page);
        PageInfo<ProxyGoods> goodsPageInfo = new PageInfo<ProxyGoods>(goodsList);
        return Response.success(goodsPageInfo);
    }
}
