package com.jiuy.operator.modular.operationManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 小程序的帮助问题控制器
 *
 * @author fengshuonan
 * @Date 2018-11-22 10:24:32
 */
@Controller
@RequestMapping("/helpQuestion")
public class HelpQuestionController extends BaseController {

    private String PREFIX = "/operationManage/helpQuestion/";

    /**
     * 跳转到帮助问题首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "helpQuestion.html";
    }

    /**
     * 跳转到添加和修改帮助问题
     */
    @RequestMapping("/helpQuestion_add")
    public String helpQuestionAdd() {
        return PREFIX + "helpQuestion_add.html";
    }

    /**
     * 跳转到帮助问题类型
     */
    @RequestMapping("/helpQuestionType")
    public String helpQuestionTypeList() {
        return PREFIX + "helpQuestionType.html";
    }

    /**
     * 跳转到添加和编辑帮助问题类型
     */
    @RequestMapping("/helpQuestionType_add")
    public String helpQuestionTypeAdd() {
        return PREFIX + "helpQuestionType_add.html";
    }

}
