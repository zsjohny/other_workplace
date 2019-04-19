package com.jiuy.supplier.modular.myOrder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.core.base.controller.BaseController;

/**
 * 发货订单控制器
 *
 * @author fengshuonan
 * @Date 2017-12-12 09:51:02
 */
@Controller
@RequestMapping("/sendOrder")
public class SendOrderController extends BaseController {

    private String PREFIX = "/myOrder/sendOrder/";

    /**
     * 跳转到发货订单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "sendOrder.html";
    }

    /**
     * 详跳转到发货订单情
     */
    @RequestMapping("/sendOrder_detail")
    public String sendOrderAdd() {
        return PREFIX + "sendOrder_detail.html";
    }

    /**
     * 跳转到修改发货订单
     */
    @RequestMapping("/sendOrder_update/{sendOrderId}")
    public String sendOrderUpdate(@PathVariable Integer sendOrderId, Model model) {
        return PREFIX + "sendOrder_edit.html";
    }

    /**
     * 获取发货订单列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增发货订单
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除发货订单
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改发货订单
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 发货订单详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
