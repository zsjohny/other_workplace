package com.jiuy.supplier.modular.logisticsManage.controller;

import com.admin.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 运费模板设置控制器
 *
 * @author fengshuonan
 * @Date 2018-04-20 09:36:51
 */
@Controller
@RequestMapping("/freightTemplateSet")
public class FreightTemplateSetController extends BaseController {

    private String PREFIX = "/logisticsManage/freightTemplateSet/";

    /**
     * 跳转到运费模板设置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "freightTemplateSet.html";
    }
    /**
     * 跳转到添加运费模板设置
     */
    @RequestMapping("/freightTemplateSet_add")
    public String freightTemplateSetAdd() {
        return PREFIX + "freightTemplateSet_add.html";
    }
    /**
     * 跳转到修改运费模板设置
     */
    @RequestMapping("/freightTemplateSet_update/{freightTemplateSetId}")
    public String freightTemplateSetUpdate(@PathVariable Integer freightTemplateSetId, Model model) {
        return PREFIX + "freightTemplateSet_edit.html";
    }

    /**
     * 获取运费模板设置列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增运费模板设置
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除运费模板设置
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改运费模板设置
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 运费模板设置详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
