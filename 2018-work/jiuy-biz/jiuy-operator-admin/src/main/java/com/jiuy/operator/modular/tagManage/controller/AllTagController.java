package com.jiuy.operator.modular.tagManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 标签控制器
 *
 * @author fengshuonan
 * @Date 2018-06-13 13:42:40
 */
@Controller
@RequestMapping("/allTag")
public class AllTagController extends BaseController {

    private String PREFIX = "/tagManage/allTag/";

    /**
     * 跳转到标签首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "allTag.html";
    }

    /**
     * 跳转到添加标签
     */
    @RequestMapping("/allTag_add")
    public String allTagAdd() {
        return PREFIX + "allTag_add.html";
    }

    /**
     * 跳转到修改标签
     */
    @RequestMapping("/allTag_update/{allTagId}")
    public String allTagUpdate(@PathVariable Integer allTagId, Model model) {
        return PREFIX + "allTag_edit.html";
    }

    /**
     * 获取标签列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增标签
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除标签
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改标签
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 标签详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
