package com.jiuy.operator.modular.orderManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 小程序订单管理控制器
 *
 * @author fengshuonan
 * @Date 2018-07-27 16:53:22
 */
@Controller
@RequestMapping("/wxaOrderManage")
public class WxaOrderManageController extends BaseController {

    private String PREFIX = "/orderManage/wxaOrderManage/";

    /**
     * 跳转到小程序订单管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "wxaOrderManage.html";
    }

    /**
     * 跳转到添加小程序订单管理详情
     */
    @RequestMapping("/wxaOrderManage_detail")
    public String wxaOrderManageDetail() {
        return PREFIX + "wxaOrderManage_detail.html";
    }

    /**
     * 跳转到修改小程序订单管理
     */
    @RequestMapping("/wxaOrderManage_profit")
    public String wxaOrderManageProfit() {
        return PREFIX + "wxaOrderManage_profit.html";
    }

}
