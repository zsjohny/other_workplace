package com.jiuy.operator.modular.wxaManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 渠道地推管理控制器
 *
 * @author fengshuonan
 * @Date 2018-12-24 11:37:57
 */
@Controller
@RequestMapping("/channelPushManage")
public class ChannelPushManageController extends BaseController {

    private String PREFIX = "/wxaManage/channelPushManage/";

    /**
     * 跳转到渠道地推管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "channelPushManage.html";
    }

    /**
     * 跳转到渠道地推管理用户数
     */
    @RequestMapping("/channelPushManage_user")
    public String channelPushManageUser() {
        return PREFIX + "channelPushManage_user.html";
    }

}
