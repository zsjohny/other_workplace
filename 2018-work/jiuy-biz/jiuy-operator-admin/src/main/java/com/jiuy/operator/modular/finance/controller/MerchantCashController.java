package com.jiuy.operator.modular.finance.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商家提现控制器
 *
 * @author fengshuonan
 * @Date 2018-11-18 09:56:03
 */
@Controller
@RequestMapping("/merchantCash")
public class MerchantCashController extends BaseController {

    private String PREFIX = "/finance/merchantCash/";

    /**
     * 跳转到商家提现首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "merchantCash.html";
    }

}
