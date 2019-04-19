package com.jiuy.supplier.modular.myAccount.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.core.base.controller.BaseController;
import com.jiuyuan.util.anno.Login;

/**
 * 我的账户控制器
 *
 * @author fengshuonan
 * @Date 2017-10-10 11:16:42
 */
@Controller
@RequestMapping("/myInfo")
@Login
public class MyInfoController extends BaseController {

    private String PREFIX = "/myAccount/myInfo/";

    /**
     * 跳转到我的账户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "myInfo.html";
    }

    /**
     * 跳转到添加我的账户
     */
    @RequestMapping("/myInfo_add")
    public String myInfoAdd() {
        return PREFIX + "myInfo_add.html";
    }

    /**
     * 跳转到修改我的账户
     */
    @RequestMapping("/myInfo_update/{myInfoId}")
    public String myInfoUpdate(@PathVariable Integer myInfoId, Model model) {
        return PREFIX + "myInfo_edit.html";
    }

    /**
     * 获取我的账户列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增我的账户
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除我的账户
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改我的账户
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 我的账户详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
