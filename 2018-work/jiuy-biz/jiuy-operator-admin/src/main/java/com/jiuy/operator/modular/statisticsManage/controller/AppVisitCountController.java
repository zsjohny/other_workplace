package com.jiuy.operator.modular.statisticsManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * APP访问概况控制器
 *
 * @author fengshuonan
 * @Date 2018-04-18 16:11:40
 */
@Controller
@RequestMapping("/appVisitCount")
public class AppVisitCountController extends BaseController {

    private String PREFIX = "/statisticsManage/appVisitCount/";

    /**
     * 跳转到APP访问概况首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "appVisitCount.html";
    }

    /**
     * 跳转到添加APP访问概况
     */
    @RequestMapping("/appVisitCount_add")
    public String appVisitCountAdd() {
        return PREFIX + "appVisitCount_add.html";
    }

    /**
     * 跳转到修改APP访问概况
     */
    @RequestMapping("/appVisitCount_update/{appVisitCountId}")
    public String appVisitCountUpdate(@PathVariable Integer appVisitCountId, Model model) {
        return PREFIX + "appVisitCount_edit.html";
    }

    /**
     * 获取APP访问概况列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增APP访问概况
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除APP访问概况
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改APP访问概况
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * APP访问概况详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
