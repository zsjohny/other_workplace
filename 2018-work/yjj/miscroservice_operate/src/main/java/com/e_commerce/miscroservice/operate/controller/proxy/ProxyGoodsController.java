package com.e_commerce.miscroservice.operate.controller.proxy;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyGoods;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.controller.BaseController;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.rpc.proxy.ProxyGoodsRpcService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 描述 运营平台代理商品controller
 *
 * @author hyq
 * @date 2018/9/27 16:04
 */
@RestController
@RequestMapping("/operate/proxy/goods")
public class ProxyGoodsController extends BaseController {

    @Autowired
    ProxyGoodsRpcService proxyGoodsRpcService;

    private Log logger = Log.getInstance(ProxyGoodsController.class);

    /**
     * 描述
     * @param offset 偏移量
     * @param limit  每页大小
     * @param id  商品id
     * @param goodsName  商品名称
     * @param mixMoney  最小金额
     * @param maxMoney  最大金额
     * @author hyq
     * @date 2018/9/29 10:28
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     */
    @RequestMapping("goodsList")
    public Response goodsList(int offset, @RequestParam(defaultValue = "10") int limit, Long id, String goodsName, BigDecimal minMoney,BigDecimal maxMoney){

        //int pageNum,@RequestParam(defaultValue = "10") int pageSize){
        //前端是从0 开始。

//        ProxyGoods proxyGoods = new ProxyGoods();
//        proxyGoods.setId(id);
//        proxyGoods.setGoodsName();

        int pageNum=offset/limit;
        pageNum++;
        int pageSize=limit;

        return  Response.success(proxyGoodsRpcService.getGoodsList(pageNum,pageSize,id,StringUtils.isEmpty(goodsName)?null:goodsName));
    }

    /**
     * 描述 添加/修改商品
     *
     * @param proxyGoods
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyq
     * @date 2018/9/27 13:49
     */
    @RequestMapping("saveEditeGoods")
    public Response saveEditeGoods(ProxyGoods proxyGoods) {
        proxyGoodsRpcService.saveGoods(proxyGoods);
        return Response.success();
    }

    /**
     * 描述 商品详情
     *
     * @param id
     * @return java.lang.String
     * @author hyq
     * @date 2018/9/19 14:22
     */
    @RequestMapping("selectById")
    public Response selectById(Long id) {
        if (id != null) {
            return Response.success(proxyGoodsRpcService.selectById(id));
        }
        return Response.errorMsg("未找到记录");
    }

    /**
     * 描述 获取商品类型
     * @param offset
     * @param limit
     * @author hyq
     * @date 2018/10/8 10:34
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     */
    @RequestMapping("getGoodsTypeList")
    public Response getGoodsTypeList(int offset, @RequestParam(defaultValue = "10") int limit){

        //int pageNum,@RequestParam(defaultValue = "10") int pageSize){
        //前端是从0 开始。

        int pageNum=offset/limit;
        pageNum++;
        int pageSize=limit;

        return  Response.success(new PageInfo<>(proxyGoodsRpcService.getGoodsTypeList(pageNum,pageSize)));
    }

    /**
     * 描述 删除商品
     * @param ids
     * @author hyq
     * @date 2018/9/29 9:51
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     */
    @RequestMapping("delGoodsList")
    public Response delGoodsList(String ids) {

        String[] split = ids.split(",");
        if(split!=null&&split.length>0){
            proxyGoodsRpcService.delGoodsList(ids);
        }
        return Response.success();
    }


}
