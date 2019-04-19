package com.jiuy.operator.modular.liveSystem.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 主播管理控制器
 *
 * @author fengshuonan
 * @Date 2019-01-28 09:46:52
 */
@Controller
@RequestMapping("/anchorManage")
public class AnchorManageController extends BaseController {

    private String PREFIX = "/liveSystem/anchorManage/";

    /**
     * 跳转到官方主播
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "anchorManage.html";
    }

    /**
     * 跳转到添加官方主播
     */
    @RequestMapping("/anchorManage_add")
    public String anchorManageAdd() {
        return PREFIX + "anchorManage_add.html";
    }

    /**
     * 跳转到门店主播
     */
    @RequestMapping("/anchorManage_store")
    public String anchorManageStore() {
        return PREFIX + "anchorManage_store.html";
    }

    /**
     * 跳转到普通主播
     */
    @RequestMapping("/anchorManage_general")
    public String anchorManageGeneral() {
        return PREFIX + "anchorManage_general.html";
    }


}
