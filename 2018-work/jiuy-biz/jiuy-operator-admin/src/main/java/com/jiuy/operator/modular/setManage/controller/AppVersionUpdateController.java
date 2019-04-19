package com.jiuy.operator.modular.setManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * APP版本更新控制器
 *
 * @author fengshuonan
 * @Date 2018-06-27 17:24:46
 */
@Controller
@RequestMapping("/appVersionUpdate")
public class AppVersionUpdateController extends BaseController {

    private String PREFIX = "/setManage/appVersionUpdate/";

    /**
     * 跳转到APP版本更新首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "appVersionUpdate.html";
    }

    /**
     * 跳转到添加APP版本更新
     */
    @RequestMapping("/appVersionUpdate_add")
    public String appVersionUpdateAdd() {
        return PREFIX + "appVersionUpdate_add.html";
    }

    /**
     * 跳转到修改APP版本更新
     */
    @RequestMapping("/appVersionUpdate_update/{appVersionUpdateId}")
    public String appVersionUpdateUpdate(@PathVariable Integer appVersionUpdateId, Model model) {
        return PREFIX + "appVersionUpdate_edit.html";
    }

    /**
     * 获取APP版本更新列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增APP版本更新
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除APP版本更新
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改APP版本更新
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * APP版本更新详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
