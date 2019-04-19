package com.jiuy.operator.modular.setManage.controller;

import com.admin.core.base.controller.BaseController;
import com.jiuy.operator.core.shiro.ShiroKit;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.common.IOperatorSeoService;
import com.jiuyuan.service.common.OperatorSeoService;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * SEO信息设置控制器
 *
 * @author fengshuonan
 * @Date 2018-04-13 15:33:56
 */
@Controller
@RequestMapping("/setSeo")
public class SetSeoController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(SetSeoController.class);

    private String PREFIX = "/setManage/setSeo/";
    
    @Autowired
    private IOperatorSeoService OperatorSeoService;

    /**
     * 跳转到SEO信息设置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "setSeo.html";
    }

    /**
     * 跳转到添加SEO信息设置
     */
    @RequestMapping("/setSeo_add")
    public String setSeoAdd() {
        return PREFIX + "setSeo_add.html";
    }

    /**
     * 跳转到修改SEO信息设置
     */
    @RequestMapping("/setSeo_update/{setSeoId}")
    public String setSeoUpdate(@PathVariable Integer setSeoId, Model model) {
        return PREFIX + "setSeo_edit.html";
    }

    /**
     * 获取SEO信息设置列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增SEO信息设置
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除SEO信息设置
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改SEO信息设置
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @AdminOperationLog
    public Object update(@RequestParam("seoHomeTitle") String seoHomeTitle,
    		             @RequestParam("seoHomeDescriptor") String seoHomeDescriptor,
    		             @RequestParam("seoHomeKeywords") String seoHomeKeywords,
    		             @RequestParam("seoDefaultTitle") String seoDefaultTitle,
    		             @RequestParam("seoDefaultDescriptor") String seoDefaultDescriptor,
    		             @RequestParam("seoDefaultKeywords") String seoDefaultKeywords) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long operatorUserId = ShiroKit.getUser().getId();
    	try {
			OperatorSeoService.update(operatorUserId,seoHomeTitle,seoHomeDescriptor,seoHomeKeywords,seoDefaultTitle,seoDefaultDescriptor,seoDefaultKeywords);
    		return jsonResponse.setSuccessful();
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }

    /**
     * SEO信息设置详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		List<Map<String, Object>> data = OperatorSeoService.detail();
    		return jsonResponse.setSuccessful().setData(data);
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }
}
