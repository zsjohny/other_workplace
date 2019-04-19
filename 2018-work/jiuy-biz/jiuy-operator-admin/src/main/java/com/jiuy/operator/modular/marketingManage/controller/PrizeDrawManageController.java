package com.jiuy.operator.modular.marketingManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 抽奖商品管理控制器
 *
 * @author fengshuonan
 * @Date 2018-12-19 17:19:44
 */
@Controller
@RequestMapping("/prizeDrawManage")
public class PrizeDrawManageController extends BaseController {

    private String PREFIX = "/marketingManage/prizeDrawManage/";

    /**
     * 跳转到抽奖商品管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "prizeDrawManage.html";
    }

    /**
     * 跳转到添加抽奖商品管理
     */
    @RequestMapping("/prizeDrawManage_add")
    public String prizeDrawManageAdd() {
        return PREFIX + "prizeDrawManage_add.html";
    }

    /**
     * 跳转到修改抽奖商品管理
     */
    @RequestMapping("/prizeDrawManage_update/{prizeDrawManageId}")
    public String prizeDrawManageUpdate(@PathVariable Integer prizeDrawManageId, Model model) {
        return PREFIX + "prizeDrawManage_edit.html";
    }

    /**
     * 获取抽奖商品管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增抽奖商品管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除抽奖商品管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改抽奖商品管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 抽奖商品管理详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
