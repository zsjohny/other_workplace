package com.e_commerce.miscroservice.publicaccount.controller;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyGoods;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.rpc.proxy.ProxyGoodsRpcService;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述 运营平台代理商品controller
 *
 * @author hyq
 * @date 2018/9/27 16:04
 */
@RestController
@RequestMapping( "/public/proxy/goods" )
public class ProxyGoodsController extends BaseController{

    @Autowired
    ProxyGoodsRpcService proxyGoodsRpcService;

    private Log logger = Log.getInstance (ProxyGoodsController.class);

    /**
     * 描述
     *
     * @param pageNum   当前页
     * @param pageSize  每页大小
     * @param id        商品id
     * @param goodsName 商品名称
     * @param minMoney  最小金额
     * @param maxMoney  最大金额
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author hyq
     * @date 2018/9/29 10:28
     */
    @RequestMapping( "goodsList" )
    public Response goodsList(@RequestParam( value = "pageNum", defaultValue = "1" ) Integer pageNum,
                              @RequestParam( value = "pageSize", defaultValue = "10" ) Integer pageSize,
                              Long id, String goodsName, BigDecimal minMoney, BigDecimal maxMoney) {

        PageInfo<ProxyGoods> goodsList = proxyGoodsRpcService.getGoodsList (pageNum, pageSize, id, StringUtils.isEmpty (goodsName) ? null : goodsName);
        logger.info ("公众号商品列表 goodsList ==> ", goodsList == null ? null : goodsList.getList ());
        List<ProxyGoods> collect = goodsList.getList ().stream ().filter (action -> action.getStatus () != 0).collect (Collectors.toList ());
        goodsList.setList (collect);
        return Response.success (goodsList);
    }


    /**
     * 描述 商品详情
     *
     * @param id
     * @return java.lang.String
     * @author hyq
     * @date 2018/9/19 14:22
     */
    @RequestMapping( "selectById" )
    public Response selectById(Long id) {
        if (id != null) {
            return Response.success (proxyGoodsRpcService.selectById (id));
        }
        return Response.errorMsg ("未找到记录");
    }

}
