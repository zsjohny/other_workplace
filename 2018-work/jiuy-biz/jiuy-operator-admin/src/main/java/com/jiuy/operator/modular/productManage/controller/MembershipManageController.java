package com.jiuy.operator.modular.productManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 会员套餐管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-25 09:52:46
 */
@Controller
@RequestMapping("/membershipManage")
public class MembershipManageController extends BaseController {

    private String PREFIX = "/productManage/membershipManage/";

    /**
     * 跳转到会员套餐管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "membershipManage.html";
    }

    /**
     * 跳转到添加会员套餐管理
     */
    @RequestMapping("/membershipManage_add")
    public String membershipManageAdd() {
        return PREFIX + "membershipManage_add.html";
    }

    /**
     * 跳转到添加会员套餐管理
     */
    @RequestMapping("/membershipManage_detail")
    public String membershipManageDetail() {
        return PREFIX + "membershipManage_detail.html";
    }

    /**
     * 跳转到修改会员套餐管理
     */
    @RequestMapping("/membershipManage_update/{membershipManageId}")
    public String membershipManageUpdate(@PathVariable Integer membershipManageId, Model model) {
        return PREFIX + "membershipManage_edit.html";
    }

    /**
     * 获取会员套餐管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增会员套餐管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除会员套餐管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改会员套餐管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 会员套餐管理详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
