package com.jiuy.operator.modular.strategyManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 刷销量策略配置控制器
 *
 * @author fengshuonan
 * @Date 2018-06-14 17:48:57
 */
@Controller
@RequestMapping("/salesConfigure")
public class SalesConfigureController extends BaseController {

    private String PREFIX = "/strategyManage/salesConfigure/";

    /**
     * 跳转到刷销量策略配置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "salesConfigure.html";
    }

    /**
     * 跳转到添加刷销量策略配置
     */
    @RequestMapping("/salesConfigure_add")
    public String salesConfigureAdd() {
        return PREFIX + "salesConfigure_add.html";
    }

    /**
     * 跳转到修改刷销量策略配置
     */
    @RequestMapping("/salesConfigure_update/{salesConfigureId}")
    public String salesConfigureUpdate(@PathVariable Integer salesConfigureId, Model model) {
        return PREFIX + "salesConfigure_edit.html";
    }

    /**
     * 获取刷销量策略配置列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增刷销量策略配置
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除刷销量策略配置
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改刷销量策略配置
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 刷销量策略配置详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
