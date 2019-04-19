package com.jiuy.operator.modular.statisticsManage.controller;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.service.common.IProductSkuNewService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title sku查询
 * @package jiuy-biz
 * @description
 * @date 2018/6/5 14:00
 * @copyright 玖远网络
 */
@Controller
@RequestMapping("/skuHistory")
public class SkuHistoryController extends BaseController {

    static Log logger = LogFactory.getLog(SkuHistoryController.class);

    @Autowired
    private IProductSkuNewService iProductSkuNewService;

    private String PREFIX = "/statisticsManage/skuHistory/";

    /**
     * 跳转到sku信息查询首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "skuHistory.html";
    }

    /**
     * 跳转到添加sku信息查询
     */
    @RequestMapping("/skuHistory_add")
    public String skuHistoryAdd() {
        return PREFIX + "skuHistory_add.html";
    }

    /**
     * 跳转到修改sku信息查询
     */
    @RequestMapping("/skuHistory_update/{skuHistoryId}")
    public String skuHistoryUpdate(@PathVariable Integer skuHistoryId, Model model) {
        return PREFIX + "skuHistory_edit.html";
    }

    /**
     * 获取sku信息统计列表
     *
     * @param startTime 首次上架时间查询条件
     * @param endTime 首次上架时间查询条件
     * @param auditStartTime 审核时间查询条件
     * @param auditEndTime 审核时间查询条件
     * @return java.lang.Object
     * @auther Charlie(唐静)
     * @date 2018/6/5 17:09
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Long startTime, Long endTime, Long auditStartTime, Long auditEndTime) {
        Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();

        try {
            //根据时间查询sku详情
            List<Map<String, Object>> result = iProductSkuNewService.findSkuHistory(
                    page, startTime, endTime, auditStartTime, auditEndTime);
            page.setRecords(result);
            return super.packForBT(page);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取售后订单列表:"+e.getMessage());
            throw new RuntimeException("获取售后订单列表:"+e.getMessage());
        }
    }

    /**
     * 新增sku信息查询
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return SUCCESS_TIP;
    }

    /**
     * 删除sku信息查询
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改sku信息查询
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return SUCCESS_TIP;
    }

    /**
     * sku信息查询详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
