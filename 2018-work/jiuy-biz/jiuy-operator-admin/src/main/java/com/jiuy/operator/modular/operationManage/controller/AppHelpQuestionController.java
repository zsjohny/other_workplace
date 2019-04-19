package com.jiuy.operator.modular.operationManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * app的帮助问题控制器
 *
 * @author fengshuonan
 * @Date 2018-11-22 10:24:32
 */
@Controller
@RequestMapping("/appHelpQuestion")
public class AppHelpQuestionController extends BaseController {

    private String PREFIX = "/operationManage/appHelpQuestion/";

    /**
     * 跳转到帮助问题列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "appHelpQuestion.html";
    }

    /**
     * 跳转到添加和修改帮助问题
     */
    @RequestMapping("/appHelpQuestion_add")
    public String appHelpQuestionAdd() {
        return PREFIX + "appHelpQuestion_add.html";
    }

    /**
     * 跳转到帮助问题类型
     */
    @RequestMapping("/appHelpQuestionType")
    public String appHelpQuestionTypeList() {
        return PREFIX + "appHelpQuestionType.html";
    }

    /**
     * 跳转到添加和编辑帮助问题类型
     */
    @RequestMapping("/appHelpQuestionType_add")
    public String appHelpQuestionTypeAdd() {
        return PREFIX + "appHelpQuestionType_add.html";
    }

}
