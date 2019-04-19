package com.jiuy.operator.modular.supplierOrderManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 发货订单控制器
 *
 * @author fengshuonan
 * @Date 2018-06-08 09:57:31
 */
@Controller
@RequestMapping("/sendOrder")
public class SendOrderController extends BaseController {

    private String PREFIX = "/supplierOrderManage/sendOrder/";

    /**
     * 跳转到发货订单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "sendOrder.html";
    }

    /**
     * 跳转到添加发货订单
     */
    @RequestMapping("/sendOrder_add")
    public String sendOrderAdd() {
        return PREFIX + "sendOrder_add.html";
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
