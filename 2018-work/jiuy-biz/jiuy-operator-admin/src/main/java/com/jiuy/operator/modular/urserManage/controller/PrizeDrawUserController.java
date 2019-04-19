package com.jiuy.operator.modular.urserManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 抽奖参与用户控制器
 *
 * @author fengshuonan
 * @Date 2018-12-19 14:20:38
 */
@Controller
@RequestMapping("/prizeDrawUser")
public class PrizeDrawUserController extends BaseController {

    private String PREFIX = "/urserManage/prizeDrawUser/";

    /**
     * 跳转到抽奖参与用户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "prizeDrawUser.html";
    }

    /**
     * 跳转到添加抽奖参与用户
     */
    @RequestMapping("/prizeDrawUser_add")
    public String prizeDrawUserAdd() {
        return PREFIX + "prizeDrawUser_add.html";
    }

    /**
     * 跳转到修改抽奖参与用户
     */
    @RequestMapping("/prizeDrawUser_update/{prizeDrawUserId}")
    public String prizeDrawUserUpdate(@PathVariable Integer prizeDrawUserId, Model model) {
        return PREFIX + "prizeDrawUser_edit.html";
    }

    /**
     * 获取抽奖参与用户列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增抽奖参与用户
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除抽奖参与用户
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改抽奖参与用户
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 抽奖参与用户详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
