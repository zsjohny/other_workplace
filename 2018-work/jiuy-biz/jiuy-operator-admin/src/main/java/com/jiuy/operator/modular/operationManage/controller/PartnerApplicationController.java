package com.jiuy.operator.modular.operationManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 合伙人申请管理控制器
 *
 * @author fengshuonan
 * @Date 2018-11-22 17:20:31
 */
@Controller
@RequestMapping("/partnerApplication")
public class PartnerApplicationController extends BaseController {

    private String PREFIX = "/operationManage/partnerApplication/";

    /**
     * 跳转到合伙人申请管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "partnerApplication.html";
    }

    /**
     * 跳转到添加合伙人申请
     */
    @RequestMapping("/partnerApplication_add")
    public String partnerApplicationAdd() {
        return PREFIX + "partnerApplication_add.html";
    }

    /**
     * 跳转到编辑合伙人申请
     */
    @RequestMapping("/partnerApplication_edit")
    public String partnerApplicationEdit() {
        return PREFIX + "partnerApplication_edit.html";
    }
}
