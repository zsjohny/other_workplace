package com.jiuy.operator.modular.wxaManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 佣金管理控制器
 *
 * @author fengshuonan
 * @Date 2018-07-04 15:36:53
 */
@Controller
@RequestMapping("/commisionManage")
public class CommisionManageController extends BaseController {

    private String PREFIX = "/wxaManage/commisionManage/";

    /**
     * 跳转到佣金管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "commisionManage.html";
    }

    /**
     * 跳转到提现记录
     */
    @RequestMapping("/withdrawRecord")
    public String withdrawRecord() {
        return PREFIX + "withdrawRecord.html";
    }

    /**
     * 跳转到修改佣金管理
     */
    @RequestMapping("/commisionManage_update/{commisionManageId}")
    public String commisionManageUpdate(@PathVariable Integer commisionManageId, Model model) {
        return PREFIX + "commisionManage_edit.html";
    }

    /**
     * 获取佣金管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增佣金管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除佣金管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改佣金管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 佣金管理详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
