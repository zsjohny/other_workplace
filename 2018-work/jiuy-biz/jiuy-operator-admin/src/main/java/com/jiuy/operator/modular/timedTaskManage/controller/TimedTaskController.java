package com.jiuy.operator.modular.timedTaskManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 定时任务控制器
 *
 * @author fengshuonan
 * @Date 2018-06-28 11:55:22
 */
@Controller
@RequestMapping("/timedTask")
public class TimedTaskController extends BaseController {

    private String PREFIX = "/timedTaskManage/timedTask/";

    /**
     * 跳转到定时任务首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "timedTask.html";
    }

    /**
     * 跳转到添加定时任务
     */
    @RequestMapping("/timedTask_info")
    public String timedTaskAdd() {
        return PREFIX + "timedTask_info.html";
    }

    /**
     * 跳转到查看定时任务
     */
    @RequestMapping("/timedTask_detail")
    public String timedTaskDetail() {
        return PREFIX + "timedTask_detail.html";
    }


    /**
     * 获取定时任务列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增定时任务
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除定时任务
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改定时任务
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 定时任务详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
