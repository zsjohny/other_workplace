package com.jiuy.operator.modular.setManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 通用设置控制器
 *
 * @author fengshuonan
 * @Date 2018-10-15 10:28:34
 */
@Controller
@RequestMapping("/commonSet")
public class CommonSetController extends BaseController {

    private String PREFIX = "/setManage/commonSet/";

    /**
     * 跳转到通用设置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "commonSet.html";
    }

}
