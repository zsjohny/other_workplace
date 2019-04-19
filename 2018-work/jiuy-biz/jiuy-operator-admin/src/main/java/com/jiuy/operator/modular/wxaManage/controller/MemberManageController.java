package com.jiuy.operator.modular.wxaManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商家会员管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-05 02:07:52
 */
@Controller
@RequestMapping("/memberManage")
public class MemberManageController extends BaseController {

    private String PREFIX = "/wxaManage/memberManage/";

    /**
     * 跳转到商家会员管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "memberManage.html";
    }

    /**
     * 跳转到添加商家会员管理
     */
    @RequestMapping("/memberManage_add")
    public String memberManageAdd() {
        return PREFIX + "memberManage_add.html";
    }

    /**
     * 跳转到修改商家会员管理
     */
    @RequestMapping("/memberManage_edit")
    public String memberManageEdit() {
        return PREFIX + "memberManage_edit.html";
    }

    /**
     * 获取商家会员管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增商家会员管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除商家会员管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改商家会员管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 商家会员管理详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
