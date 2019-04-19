package com.jiuy.operator.modular.urserManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 小程序用户控制器
 *
 * @author fengshuonan
 * @Date 2018-10-11 15:12:55
 */
@Controller
@RequestMapping("/wxaUser")
public class WxaUserController extends BaseController {

    private String PREFIX = "/urserManage/wxaUser/";

    /**
     * 跳转到小程序用户查询首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "wxaUser.html";
    }

    /**
     * 跳转到修改小程序用户详情的分销直属上级
     */
    @RequestMapping("/wxaUser_edit")
    public String wxaUserUpdate() {
        return PREFIX + "wxaUser_edit.html";
    }
	
	/**
     * 跳转到小程序用户详情
     */
    @RequestMapping("/wxaUser_detail")
    public String wxaUserDetail() {
        return PREFIX + "wxaUser_detail.html";
    }

    /**
     * 跳转到小程序用户团队查询
     */
    @RequestMapping("/wxaUser_team")
    public String wxaUserTeam() {
        return PREFIX + "wxaUser_team.html";
    }

    /**
     * 跳转到小程序用户粉丝查询
     */
    @RequestMapping("/wxaUser_fans")
    public String wxaUserFans() { return PREFIX + "wxaUser_fans.html"; }

    /**
     * 跳转到申请单查询
     */
    @RequestMapping("/wxaUser_apply")
    public String wxaUserApply() { return PREFIX + "wxaUser_apply.html"; }

    /**
     * 跳转到审核弹窗（分销角色升级申请）
     */
    @RequestMapping("/wxaUser_auditing")
    public String wxaUserAuditing() { return PREFIX + "wxaUser_auditing.html"; }

}
