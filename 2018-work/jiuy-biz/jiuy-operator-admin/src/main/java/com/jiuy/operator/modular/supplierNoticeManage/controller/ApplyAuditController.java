package com.jiuy.operator.modular.supplierNoticeManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 报名审核记录控制器
 *
 * @author fengshuonan
 * @Date 2018-03-14 09:41:40
 */
@Controller
@RequestMapping("/applyAudit")
public class ApplyAuditController extends BaseController {

    private String PREFIX = "/supplierNoticeManage/applyAudit/";

    /**
     * 跳转到报名审核记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "applyAudit.html";
    }

    /**
     * 跳转到添加报名审核记录
     */
    @RequestMapping("/applyAudit_add")
    public String applyAuditAdd() {
        return PREFIX + "applyAudit_add.html";
    }

    /**
     * 跳转到修改报名审核记录
     */
    @RequestMapping("/applyAudit_update/{applyAuditId}")
    public String applyAuditUpdate(@PathVariable Integer applyAuditId, Model model) {
        return PREFIX + "applyAudit_edit.html";
    }

    /**
     * 获取报名审核记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增报名审核记录
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除报名审核记录
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改报名审核记录
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 报名审核记录详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
