package com.jiuy.operator.modular.specialManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 自定义专场控制器
 *
 * @author fengshuonan
 * @Date 2018-06-08 15:23:33
 */
@Controller
@RequestMapping("/customizeSpecial")
public class CustomizeSpecialController extends BaseController {

    private String PREFIX = "/specialManage/customizeSpecial/";

    /**
     * 跳转到自定义专场首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "customizeSpecial.html";
    }

    /**
     * 跳转到添加自定义专场
     */
    @RequestMapping("/customizeSpecial_add")
    public String customizeSpecialAdd() {
        return PREFIX + "customizeSpecial_add.html";
    }

    /**
     * 跳转到修改自定义专场
     */
    @RequestMapping("/customizeSpecial_update/{customizeSpecialId}")
    public String customizeSpecialUpdate(@PathVariable Integer customizeSpecialId, Model model) {
        return PREFIX + "customizeSpecial_edit.html";
    }

    /**
     * 获取自定义专场列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增自定义专场
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除自定义专场
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改自定义专场
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 自定义专场详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
