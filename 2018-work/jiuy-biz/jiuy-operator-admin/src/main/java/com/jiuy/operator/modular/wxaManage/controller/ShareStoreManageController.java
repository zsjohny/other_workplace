package com.jiuy.operator.modular.wxaManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 小程序店中店管理控制器
 *
 * @author fengshuonan
 * @Date 2018-12-10 16:32:00
 */
@Controller
@RequestMapping("/shareStoreManage")
public class ShareStoreManageController extends BaseController {

    private String PREFIX = "/wxaManage/shareStoreManage/";

    /**
     * 跳转到小程序店中店店铺管理
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "shareStoreManage.html";
    }

    /**
     * 跳转到小程序店中店店铺详情
     */
    @RequestMapping("/shareStoreManage_detail")
    public String shareStoreManageDetail() {
        return PREFIX + "shareStoreManage_detail.html";
    }

    /**
     * 跳转到小程序店中店商家提现
     */
    @RequestMapping("/shareStoreManage_cash")
    public String shareStoreManageCash() {
        return PREFIX + "shareStoreManage_cash.html";
    }
	
	/**
     * 跳转到添加小程序店中店小程序开通引导页
     */
    @RequestMapping("/shareStoreManage_guide")
    public String shareStoreManageAddAuide() {
        return PREFIX + "shareStoreManage_guide.html";
    }
	
	/**
     * 跳转到添加小程序店中店专享开通
     */
    @RequestMapping("/shareStoreManage_add")
    public String shareStoreManageAdd() {
        return PREFIX + "shareStoreManage_add.html";
    }
		
}
