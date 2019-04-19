package com.jiuy.operator.modular.statisticsManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 页面统计控制器
 *
 * @author fengshuonan
 * @Date 2018-04-18 16:12:16
 */
@Controller
@RequestMapping("/appPageCount")
public class AppPageCountController extends BaseController {

    private String PREFIX = "/statisticsManage/appPageCount/";

    /**
     * 跳转到页面统计首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "appPageCount.html";
    }

    /**
     * 跳转到添加页面统计
     */
    @RequestMapping("/appPageCount_add")
    public String appPageCountAdd() {
        return PREFIX + "appPageCount_add.html";
    }

    /**
     * 跳转到修改页面统计
     */
    @RequestMapping("/appPageCount_update/{appPageCountId}")
    public String appPageCountUpdate(@PathVariable Integer appPageCountId, Model model) {
        return PREFIX + "appPageCount_edit.html";
    }

    /**
     * 获取页面统计列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增页面统计
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除页面统计
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改页面统计
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 页面统计详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
