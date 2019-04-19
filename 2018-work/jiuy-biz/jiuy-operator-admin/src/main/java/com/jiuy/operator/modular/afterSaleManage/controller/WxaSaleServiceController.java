package com.jiuy.operator.modular.afterSaleManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 小程序售后订单管理控制器
 *
 * @author fengshuonan
 * @Date 2018-07-27 16:57:55
 */
@Controller
@RequestMapping("/wxaSaleService")
public class WxaSaleServiceController extends BaseController {

    private String PREFIX = "/afterSaleManage/wxaSaleService/";

    /**
     * 跳转到小程序售后订单管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "wxaSaleService.html";
    }

    /**
     * 跳转到添加小程序售后订单管理
     */
    @RequestMapping("/wxaSaleService_add")
    public String wxaSaleServiceAdd() {
        return PREFIX + "wxaSaleService_add.html";
    }

    /**
     * 跳转到修改小程序售后订单管理
     */
    @RequestMapping("/wxaSaleService_update/{wxaSaleServiceId}")
    public String wxaSaleServiceUpdate(@PathVariable Integer wxaSaleServiceId, Model model) {
        return PREFIX + "wxaSaleService_edit.html";
    }

    /**
     * 获取小程序售后订单管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增小程序售后订单管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除小程序售后订单管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改小程序售后订单管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 小程序售后订单管理详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
