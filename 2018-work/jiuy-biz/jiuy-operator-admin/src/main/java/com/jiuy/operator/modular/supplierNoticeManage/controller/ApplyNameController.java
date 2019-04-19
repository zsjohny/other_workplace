package com.jiuy.operator.modular.supplierNoticeManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 报名单控制器
 *
 * @author fengshuonan
 * @Date 2018-03-14 09:41:03
 */
@Controller
@RequestMapping("/applyName")
public class ApplyNameController extends BaseController {

    private String PREFIX = "/supplierNoticeManage/applyName/";

    /**
     * 跳转到报名单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "applyName.html";
    }

    /**
     * 跳转到添加报名单
     */
    @RequestMapping("/applyName_add")
    public String applyNameAdd() {
        return PREFIX + "applyName_add.html";
    }

    /**
     * 跳转到修改报名单
     */
    @RequestMapping("/applyName_update")
    public String applyNameUpdate() {
        return PREFIX + "applyName_edit.html";
    }

    /**
     * 获取报名单列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增报名单
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除报名单
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改报名单
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 报名单详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
