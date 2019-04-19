package com.jiuy.operator.modular.specialManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 系统专场控制器
 *
 * @author fengshuonan
 * @Date 2018-06-08 15:22:01
 */
@Controller
@RequestMapping("/systemSpecial")
public class SystemSpecialController extends BaseController {

    private String PREFIX = "/specialManage/systemSpecial/";

    /**
     * 跳转到系统专场首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "systemSpecial.html";
    }

    /**
     * 跳转到编辑专场-系统、自定义、回收站共用的页面
     */
    @RequestMapping("/commonSpecial_edit")
    public String commonSpecialEdit() {
        return PREFIX + "commonSpecial_edit.html";
    }

    /**
     * 跳转到修改系统专场
     */
    @RequestMapping("/systemSpecial_update/{systemSpecialId}")
    public String systemSpecialUpdate(@PathVariable Integer systemSpecialId, Model model) {
        return PREFIX + "systemSpecial_edit.html";
    }

    /**
     * 获取系统专场列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增系统专场
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除系统专场
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改系统专场
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 系统专场详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
