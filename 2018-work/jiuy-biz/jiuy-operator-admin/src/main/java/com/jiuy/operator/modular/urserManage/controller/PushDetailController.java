package com.jiuy.operator.modular.urserManage.controller;

import com.admin.core.base.controller.BaseController;
import com.jiuyuan.util.anno.Login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 地推人员详情控制器
 *
 * @author fengshuonan
 * @Date 2017-11-07 17:47:23
 */
@Controller
@RequestMapping("/pushDetail")
@Login
public class PushDetailController extends BaseController {

    private String PREFIX = "/urserManage/pushDetail/";

    /**
     * 跳转到地推人员详情首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "pushDetail.html";
    }

    /**
     * 跳转到添加地推人员详情
     */
    @RequestMapping("/pushDetail_add")
    public String pushDetailAdd() {
        return PREFIX + "pushDetail_add.html";
    }

    /**
     * 跳转到修改地推人员详情
     */
    @RequestMapping("/pushDetail_update/{pushDetailId}")
    public String pushDetailUpdate(@PathVariable Integer pushDetailId, Model model) {
        return PREFIX + "pushDetail_edit.html";
    }

    /**
     * 获取地推人员详情列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增地推人员详情
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除地推人员详情
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改地推人员详情
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 地推人员详情详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
