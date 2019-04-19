package com.jiuy.supplier.modular.platformNotice.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 我的报名控制器
 *
 * @author fengshuonan
 * @Date 2018-03-13 10:45:48
 */
@Controller
@RequestMapping("/myEnroll")
public class MyEnrollController extends BaseController {

    private String PREFIX = "/platformNotice/myEnroll/";

    /**
     * 跳转到我的报名首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "myEnroll.html";
    }

    /**
     * 跳转到添加我的报名
     */
    @RequestMapping("/myEnroll_add")
    public String myEnrollAdd() {
        return PREFIX + "myEnroll_add.html";
    }

    /**
     * 跳转到修改我的报名
     */
    @RequestMapping("/myEnroll_update/{myEnrollId}")
    public String myEnrollUpdate(@PathVariable Integer myEnrollId, Model model) {
        return PREFIX + "myEnroll_edit.html";
    }

    /**
     * 获取我的报名列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增我的报名
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除我的报名
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改我的报名
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 我的报名详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
