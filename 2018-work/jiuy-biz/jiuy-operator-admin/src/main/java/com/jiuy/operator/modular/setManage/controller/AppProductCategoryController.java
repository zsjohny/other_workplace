package com.jiuy.operator.modular.setManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * APP商品分类页控制器
 *
 * @author fengshuonan
 * @Date 2018-07-05 14:07:58
 */
@Controller
@RequestMapping("/appProductCategory")
public class AppProductCategoryController extends BaseController {

    private String PREFIX = "/setManage/appProductCategory/";

    /**
     * 跳转到APP商品分类页首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "appProductCategory.html";
    }

    /**
     * 跳转到添加APP商品分类页
     */
    @RequestMapping("/appProductCategory_add")
    public String appProductCategoryAdd() {
        return PREFIX + "appProductCategory_add.html";
    }

    /**
     * 跳转到修改APP商品分类页
     */
    @RequestMapping("/appProductCategory_update/{appProductCategoryId}")
    public String appProductCategoryUpdate(@PathVariable Integer appProductCategoryId, Model model) {
        return PREFIX + "appProductCategory_edit.html";
    }

    /**
     * 获取APP商品分类页列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增APP商品分类页
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除APP商品分类页
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改APP商品分类页
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * APP商品分类页详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
