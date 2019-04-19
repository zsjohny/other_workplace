package com.jiuy.operator.modular.tagManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 标签组控制器
 *
 * @author fengshuonan
 * @Date 2018-06-13 13:41:08
 */
@Controller
@RequestMapping("/tagGroup")
public class TagGroupController extends BaseController {

    private String PREFIX = "/tagManage/tagGroup/";

    /**
     * 跳转到标签组首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "tagGroup.html";
    }

    /**
     * 跳转到添加标签组
     */
    @RequestMapping("/tagGroup_add")
    public String tagGroupAdd() {
        return PREFIX + "tagGroup_add.html";
    }

    /**
     * 跳转到修改标签组
     */
    @RequestMapping("/tagGroup_update/{tagGroupId}")
    public String tagGroupUpdate(@PathVariable Integer tagGroupId, Model model) {
        return PREFIX + "tagGroup_edit.html";
    }

    /**
     * 获取标签组列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增标签组
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除标签组
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改标签组
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 标签组详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
