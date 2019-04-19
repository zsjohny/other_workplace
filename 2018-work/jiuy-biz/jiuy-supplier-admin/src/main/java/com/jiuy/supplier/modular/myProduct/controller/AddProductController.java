package com.jiuy.supplier.modular.myProduct.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.core.base.controller.BaseController;

/**
 * 新建商品控制器
 *
 * @author fengshuonan
 * @Date 2017-10-12 18:00:16
 */
@Controller
@RequestMapping("/addProduct")
public class AddProductController extends BaseController {

    private String PREFIX = "/myProduct/addProduct/";

    /**
     * 跳转到新建商品首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "addProduct.html";
    }

    /**
     * 跳转到添加新建商品2
     */
    @RequestMapping("/addProduct_add")
    public String addProductAdd() {
        return PREFIX + "addProduct_add.html";
    }
    /**
     * 跳转到添加新建商品3
     */
    @RequestMapping("/addProduct_edit")
    public String addProduct_edit() {
        return PREFIX + "addProduct_edit.html";
    }
    /**
     * 获取新建商品列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增新建商品
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除新建商品
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改新建商品
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 新建商品详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
