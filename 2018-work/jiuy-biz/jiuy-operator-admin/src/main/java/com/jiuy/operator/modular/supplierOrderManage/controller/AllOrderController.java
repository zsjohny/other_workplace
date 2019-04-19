package com.jiuy.operator.modular.supplierOrderManage.controller;
import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 所有订单控制器
 *
 * @author fengshuonan
 * @Date 2018-06-08 09:58:10
 */
@Controller
@RequestMapping("/allOrder")
public class AllOrderController extends BaseController {

    private String PREFIX = "/supplierOrderManage/allOrder/";

    /**
     * 跳转到所有订单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "allOrder.html";
    }

    /**
     * 跳转到添加所有订单
     */
    @RequestMapping("/allOrder_detail")
    public String allOrderAdd() {
        return PREFIX + "allOrder_detail.html";
    }

    /**
     * 跳转到修改所有订单
     */
    @RequestMapping("/allOrder_update/{allOrderId}")
    public String allOrderUpdate(@PathVariable Integer allOrderId, Model model) {
        return PREFIX + "allOrder_edit.html";
    }

    /**
     * 获取所有订单列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增所有订单
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除所有订单
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改所有订单
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 所有订单详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
