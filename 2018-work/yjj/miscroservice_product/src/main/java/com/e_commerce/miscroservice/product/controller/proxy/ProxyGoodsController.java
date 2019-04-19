package com.e_commerce.miscroservice.product.controller.proxy;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyGoods;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyGoodsType;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.controller.BaseController;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.page.DataGrid;
import com.e_commerce.miscroservice.product.service.proxy.ProxyGoodsService;
import com.e_commerce.miscroservice.product.service.proxy.ProxyGoodsTypeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述 运营平台代理商品controller
 *
 * @author hyq
 * @date 2018/9/18 16:04
 */
@RestController
@RequestMapping("/product/proxy")
public class ProxyGoodsController extends BaseController {

    @Autowired
    ProxyGoodsService proxyGoodsService;

    @Autowired
    ProxyGoodsTypeService proxyGoodsTypeService;

    private Log logger = Log.getInstance(ProxyGoodsController.class);

    /**
     * 描述
     *
     * @param pageNum 当前页
     * @param pageNum 当前页
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyq
     * @date 2018/9/18 16:49
     */
    @RequestMapping("getGoodsList")
    public PageInfo<ProxyGoods> getGoodsList(int pageNum, int pageSize, ProxyGoods proxyGoods) {
        logger.info ("商品列表 proxyGoods={}", proxyGoods);
        DataGrid grid = new DataGrid();
        grid.setPageNum(pageNum);
        grid.setPageSize(pageSize);
        proxyGoods.setDelStatus(0);
        //proxyGoods.setStatus(1);
        List<ProxyGoods> result = proxyGoodsService.listForDataGridL (grid, proxyGoods);
        logger.info ("result.size={}", result.size ());
        return new PageInfo<> (result);
    }

    /**
     * 描述 添加/修改商品
     *
     * @param proxyGoods
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyq
     * @date 2018/9/19 13:49
     */
    @RequestMapping("saveGoods")
    public Response saveGoods(@RequestBody ProxyGoods proxyGoods) {
        if (proxyGoods.getId() != null) {
            this.proxyGoodsService.updateSelectiveById(proxyGoods);
            return Response.success();
        }

        this.proxyGoodsService.insertSelective(proxyGoods);
        return Response.success();
    }

    /**
     * 描述 编辑商品
     *
     * @param id
     * @return java.lang.String
     * @author hyq
     * @date 2018/9/19 14:22
     */
    @RequestMapping("selectById")
    public ProxyGoods selectById(Long id) {
        if (id != null) {
            return this.proxyGoodsService.selectById(id).orElseThrow(() -> new RuntimeException("未找到记录"));
        }
        return null;
    }

    /**
     * 描述 获取商品类型
     *
     * @param pageNum 当前页
     * @param pageNum 当前页
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyq
     * @date 2018/9/18 16:49
     */
    @RequestMapping("getGoodsTypeList")
    public List<ProxyGoodsType> getGoodsTypeList(int pageNum,int pageSize) {
        DataGrid grid = new DataGrid();
        grid.setPageNum(pageNum);
        grid.setPageSize(pageSize);
        return this.proxyGoodsTypeService.listForDataGridL(grid);
    }

    @RequestMapping("selectGoodsType")
    public ProxyGoodsType selectGoodsType(Long id) {
        if (id != null) {
            return this.proxyGoodsTypeService.selectById(id).orElseThrow(() -> new RuntimeException("未找到记录"));
        }
        return null;
    }

    @RequestMapping("delGoodsList")
    public Response delGoodsList(String ids) {

        List<Long> longList = new ArrayList<>();
        for(String s : ids.split(",")){
            longList.add(Long.parseLong(s));
        }

        Example example = new Example(ProxyGoods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",longList);

        ProxyGoods proxyGoods = new ProxyGoods();
        proxyGoods.setDelStatus(1);

        int i = proxyGoodsService.updateByExampleSelective(proxyGoods, example);


        return null;
    }


}
