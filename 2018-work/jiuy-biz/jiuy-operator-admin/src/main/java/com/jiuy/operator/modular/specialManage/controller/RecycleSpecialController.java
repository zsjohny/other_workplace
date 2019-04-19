package com.jiuy.operator.modular.specialManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 回收站控制器
 *
 * @author fengshuonan
 * @Date 2018-06-08 15:26:18
 */
@Controller
@RequestMapping("/recycleSpecial")
public class RecycleSpecialController extends BaseController {

    private String PREFIX = "/specialManage/recycleSpecial/";

    /**
     * 跳转到回收站首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "recycleSpecial.html";
    }

    /**
     * 跳转到添加回收站
     */
    @RequestMapping("/recycleSpecial_add")
    public String recycleSpecialAdd() {
        return PREFIX + "recycleSpecial_add.html";
    }

    /**
     * 跳转到修改回收站
     */
    @RequestMapping("/recycleSpecial_update/{recycleSpecialId}")
    public String recycleSpecialUpdate(@PathVariable Integer recycleSpecialId, Model model) {
        return PREFIX + "recycleSpecial_edit.html";
    }

    /**
     * 获取回收站列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增回收站
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除回收站
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改回收站
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 回收站详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
