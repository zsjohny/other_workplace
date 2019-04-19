package com.jiuy.operator.modular.businessManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商家展示信息控制器
 *
 * @author fengshuonan
 * @Date 2018-08-21 11:01:18
 */
@Controller
@RequestMapping("/businessInformation")
public class BusinessInformationController extends BaseController {

    private String PREFIX = "/businessManage/businessInformation/";

    /**
     * 跳转到商家展示信息首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "businessInformation.html";
    }

    /**
     * 跳转到添加商家展示信息
     */
    @RequestMapping("/businessInformation_add")
    public String businessInformationAdd() {
        return PREFIX + "businessInformation_add.html";
    }

    /**
     * 跳转到修改商家展示信息
     */
    @RequestMapping("/businessInformation_edit")
    public String businessInformationUpdate() {
        return PREFIX + "businessInformation_edit.html";
    }

    /**
     * 获取商家展示信息列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增商家展示信息
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除商家展示信息
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改商家展示信息
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 商家展示信息详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
