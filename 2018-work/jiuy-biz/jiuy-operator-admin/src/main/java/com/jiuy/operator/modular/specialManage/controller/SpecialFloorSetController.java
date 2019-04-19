package com.jiuy.operator.modular.specialManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 设置楼层控制器
 *
 * @author fengshuonan
 * @Date 2018-06-08 15:33:09
 */
@Controller
@RequestMapping("/specialFloorSet")
public class SpecialFloorSetController extends BaseController {

    private String PREFIX = "/specialManage/specialFloorSet/";

    /**
     * 跳转到设置楼层首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "specialFloorSet.html";
    }

    /**
     * 跳转到添加设置楼层
     */
    @RequestMapping("/specialFloorSet_add")
    public String specialFloorSetAdd() {
        return PREFIX + "specialFloorSet_add.html";
    }

    /**
     * 跳转到修改设置楼层
     */
    @RequestMapping("/specialFloorSet_update/{specialFloorSetId}")
    public String specialFloorSetUpdate(@PathVariable Integer specialFloorSetId, Model model) {
        return PREFIX + "specialFloorSet_edit.html";
    }

    /**
     * 获取设置楼层列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增设置楼层
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除设置楼层
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改设置楼层
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 设置楼层详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
