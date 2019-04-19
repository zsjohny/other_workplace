package com.e_commerce.miscroservice.product.service.proxy;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyGoods;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述 代理商品查询
 *
 * @date 2018/9/18 17:23
 * @return
 */
public interface ProxyGoodsService extends BaseService<ProxyGoods> {

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
    Response getGoodsList(int page, HttpServletRequest res, HttpServletResponse req);

}
