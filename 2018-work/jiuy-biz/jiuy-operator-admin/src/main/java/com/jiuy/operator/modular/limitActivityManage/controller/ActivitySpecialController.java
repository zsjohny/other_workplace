package com.jiuy.operator.modular.limitActivityManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 活动专场配置控制器
 *
 * @author fengshuonan
 * @Date 2018-03-14 11:12:44
 */
@Controller
@RequestMapping("/activitySpecial")
public class ActivitySpecialController extends BaseController {

    private String PREFIX = "/limitActivityManage/activitySpecial/";

    /**
     * 跳转到活动专场配置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "activitySpecial.html";
    }

    /**
     * 跳转到添加活动专场配置
     */
    @RequestMapping("/activitySpecial_add")
    public String activitySpecialAdd() {
        return PREFIX + "activitySpecial_add.html";
    }

    /**
     * 跳转到修改活动专场配置
     */
    @RequestMapping("/activitySpecial_update/{activitySpecialId}")
    public String activitySpecialUpdate(@PathVariable Integer activitySpecialId, Model model) {
        return PREFIX + "activitySpecial_edit.html";
    }

    /**
     * 获取活动专场配置列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增活动专场配置
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除活动专场配置
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改活动专场配置
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 活动专场配置详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
