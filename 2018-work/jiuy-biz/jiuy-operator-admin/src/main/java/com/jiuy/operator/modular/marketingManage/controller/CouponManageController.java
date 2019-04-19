package com.jiuy.operator.modular.marketingManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 优惠券管理控制器
 *
 * @author fengshuonan
 * @Date 2018-12-19 16:05:09
 */
@Controller
@RequestMapping("/couponManage")
public class CouponManageController extends BaseController {

    private String PREFIX = "/marketingManage/couponManage/";

    /**
     * 跳转到优惠券管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "couponManage.html";
    }

    /**
     * 跳转到添加优惠券管理
     */
    @RequestMapping("/couponManage_add")
    public String couponManageAdd() {
        return PREFIX + "couponManage_add.html";
    }
    /**
     * 跳转到优惠券发放记录列表
     */
    @RequestMapping("/couponManage_record")
    public String couponManageRecord() {
        return PREFIX + "couponManage_record.html";
    }
}




