package com.jiuy.operator.modular.wxaManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 收益管理控制器
 *
 * @author fengshuonan
 * @Date 2018-10-12 14:59:57
 */
@Controller
@RequestMapping("/profitManage")
public class ProfitManageController extends BaseController {

    private String PREFIX = "/wxaManage/profitManage/";

    /**
     * 跳转到收益管理首页-现金收支明细查询
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "profitManage.html";
    }

    /**
     * 跳转到现金账户提现详情
     */
    @RequestMapping("/profitManage_detail")
    public String profitManageDetail() {
        return PREFIX + "profitManage_detail.html";
    }

    /**
     * 跳转到金币收支明细查询
     */
    @RequestMapping("/profitManage_coin")
    public String profitManageCoin() {
        return PREFIX + "profitManage_coin.html";
    }

    /**
     * 跳转到业绩统计查询
     */
    @RequestMapping("/profitManage_statistics")
    public String profitManageStatistics() {
        return PREFIX + "profitManage_statistics.html";
    }

    /**
     * 跳转到分销业绩查询：订单返利明细
     */
    @RequestMapping("/profitManage_rebate")
    public String profitManageRebate() {
        return PREFIX + "profitManage_rebate.html";
    }

    /**
     * 跳转到分销业绩查询：管理奖金明细
     */
    @RequestMapping("/profitManage_bonus")
    public String profitManageBonus() {
        return PREFIX + "profitManage_bonus.html";
    }

    /**
     * 跳转到团队订单返佣明细
     */
    @RequestMapping("/profitManage_team")
    public String profitManageTeam() {
        return PREFIX + "profitManage_team.html";
    }
}
