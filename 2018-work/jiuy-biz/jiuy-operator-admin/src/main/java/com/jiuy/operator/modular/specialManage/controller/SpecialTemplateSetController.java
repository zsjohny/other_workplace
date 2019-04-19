package com.jiuy.operator.modular.specialManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 楼层模板设置控制器
 *
 * @author fengshuonan
 * @Date 2018-06-08 15:37:47
 */
@Controller
@RequestMapping("/specialTemplateSet")
public class SpecialTemplateSetController extends BaseController {

    private String PREFIX = "/specialManage/specialTemplateSet/";

    /**
     * 跳转到楼层模板设置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "specialTemplateSet.html";
    }

    /**
     * 跳转到添加楼层模板设置
     */
    @RequestMapping("/specialTemplate/{index}")
    public String specialTemplateSetAdd(@PathVariable("index") Integer index) {
        return PREFIX + "specialTemplate8-"+ index +".html";
    }

    /**
     * 跳转到修改楼层模板设置
     */
    @RequestMapping("/specialTemplateSet_update/{specialTemplateSetId}")
    public String specialTemplateSetUpdate(@PathVariable Integer specialTemplateSetId, Model model) {
        return PREFIX + "specialTemplateSet_edit.html";
    }

    /**
     * 获取楼层模板设置列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增楼层模板设置
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除楼层模板设置
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改楼层模板设置
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 楼层模板设置详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
