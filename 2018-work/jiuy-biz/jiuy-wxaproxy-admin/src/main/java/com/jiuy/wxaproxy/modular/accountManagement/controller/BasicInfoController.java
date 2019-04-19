package com.jiuy.wxaproxy.modular.accountManagement.controller;

import com.admin.core.base.controller.BaseController;
import com.jiuy.wxaproxy.core.shiro.ShiroKit;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.common.IProxyUserService;
import com.jiuyuan.service.common.ProxyUserService;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.web.help.JsonResponse;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 基本信息控制器
 *
 * @author fengshuonan
 * @Date 2018-04-03 11:48:50
 */
@Controller
@RequestMapping("/basicInfo")
public class BasicInfoController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(BasicInfoController.class);

    private String PREFIX = "/accountManagement/basicInfo/";
    
    @Autowired
    private IProxyUserService proxyUserService;

    /**
     * 跳转到基本信息首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "basicInfo.html";
    }

    /**
     * 跳转到添加基本信息
     */
    @RequestMapping("/basicInfo_add")
    public String basicInfoAdd() {
        return PREFIX + "basicInfo_add.html";
    }

    /**
     * 跳转到修改基本信息
     */
    @RequestMapping("/basicInfo_update/{basicInfoId}")
    public String basicInfoUpdate(@PathVariable Integer basicInfoId, Model model) {
        return PREFIX + "basicInfo_edit.html";
    }

    /**
     * 获取基本信息列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增基本信息
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除基本信息
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改基本信息
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 基本信息详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
    	JsonResponse jsonResponse = new JsonResponse();
    	long proxyUserId = ShiroKit.getUser().getId();
    	try {
			Map<String,Object> data = proxyUserService.detail(proxyUserId);
    		return jsonResponse.setSuccessful().setData(data);
		} catch(TipsMessageException e){
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }
}
