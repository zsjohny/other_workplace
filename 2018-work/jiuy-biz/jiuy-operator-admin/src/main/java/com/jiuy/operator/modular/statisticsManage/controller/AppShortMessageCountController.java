package com.jiuy.operator.modular.statisticsManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 短信营销统计控制器
 *
 * @author fengshuonan
 * @Date 2018-04-18 16:17:31
 */
@Controller
@RequestMapping("/appShortMessageCount")
public class AppShortMessageCountController extends BaseController {

    private String PREFIX = "/statisticsManage/appShortMessageCount/";

    /**
     * 跳转到短信营销统计首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "appShortMessageCount.html";
    }

    /**
     * 跳转到添加短信营销统计
     */
    @RequestMapping("/appShortMessageCount_add")
    public String appShortMessageCountAdd() {
        return PREFIX + "appShortMessageCount_add.html";
    }

    /**
     * 跳转到修改短信营销统计
     */
    @RequestMapping("/appShortMessageCount_update/{appShortMessageCountId}")
    public String appShortMessageCountUpdate(@PathVariable Integer appShortMessageCountId, Model model) {
        return PREFIX + "appShortMessageCount_edit.html";
    }

    /**
     * 获取短信营销统计列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增短信营销统计
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除短信营销统计
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改短信营销统计
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 短信营销统计详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
