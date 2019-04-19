package com.jiuy.operator.modular.productCategoryManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商品分类管理控制器
 *
 * @author fengshuonan
 * @Date 2018-03-20 16:00:50
 */
@Controller
@RequestMapping("/allCategory")
public class AllCategoryController extends BaseController {

    private String PREFIX = "/productCategoryManage/allCategory/";

    /**
     * 跳转到商品分类管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "allCategory.html";
    }

    /**
     * 跳转到添加商品分类管理
     */
    @RequestMapping("/allCategory_add")
    public String allCategoryAdd() {
        return PREFIX + "allCategory_add.html";
    }

    /**
     * 跳转到修改商品分类管理
     */
    @RequestMapping("/allCategory_update/{allCategoryId}")
    public String allCategoryUpdate(@PathVariable Integer allCategoryId, Model model) {
        return PREFIX + "allCategory_edit.html";
    }

    /**
     * 获取商品分类管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增商品分类管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除商品分类管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改商品分类管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 商品分类管理详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
