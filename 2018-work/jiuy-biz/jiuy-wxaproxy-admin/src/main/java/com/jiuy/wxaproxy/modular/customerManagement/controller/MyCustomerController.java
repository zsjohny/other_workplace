package com.jiuy.wxaproxy.modular.customerManagement.controller;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.wxaproxy.core.shiro.ShiroKit;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.newentity.ProxyCustomer;
import com.jiuyuan.service.common.IProxyCustomerService;
import com.jiuyuan.service.common.ProxyCustomerService;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.util.anno.NoLogin;
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
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 我的客户控制器
 *
 * @author fengshuonan
 * @Date 2018-04-03 11:50:00
 */
@Controller
@RequestMapping("/myCustomer")
public class MyCustomerController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(MyCustomerController.class);

    private String PREFIX = "/customerManagement/myCustomer/";
    
    @Autowired
    private IProxyCustomerService proxyCustomerService;

    /**
     * 跳转到我的客户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "myCustomer.html";
    }

    /**
     * 跳转到添加我的客户
     */
    @RequestMapping("/myCustomer_add")
    public String myCustomerAdd() {
        return PREFIX + "myCustomer_add.html";
    }
    
    /**
     * 跳转到申请小程序
     */
    @RequestMapping("/myCustomer_apply")
    @NoLogin
    public String myCustomerApply() {
        return PREFIX + "myCustomer_apply.html";
    }
    /**
     * 跳转到客户详情
     */
    @RequestMapping("/myCustomer_detail")
    public String myCustomerDetail() {
        return PREFIX + "myCustomer_detail.html";
    }
    /**
     * 跳转到修改我的客户
     */
    @RequestMapping("/myCustomer_update/{myCustomerId}")
    public String myCustomerUpdate(@PathVariable Integer myCustomerId, Model model) {
        return PREFIX + "myCustomer_edit.html";
    }

    /**
     * 获取我的客户列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(value = "customerName", required = false, defaultValue = "") String customerName,//客户姓名
    		           @RequestParam(value = "customerPhone", required = false, defaultValue = "") String customerPhone,//客户手机号码
    		           @RequestParam(value = "status", required = false, defaultValue = "-1") int status,//客户状态：已签约(0)、续约保护期中(1)、已过期(2)
    		           @RequestParam(value = "maxSurplusDays", required = false) Integer maxSurplusDays,//剩余使用天数上限
    		           @RequestParam(value = "minSurplusDays", required = false) Integer minSurplusDays//剩余使用天数下限
    		           ) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long proxyUserId = ShiroKit.getUser().getId();
    	try {
    		Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
    		List<Map<String,Object>> data = proxyCustomerService.list(customerName,customerPhone,status,maxSurplusDays,minSurplusDays,proxyUserId,page);
    		page.setRecords(data);
    		return this.packForBT(page);
		} catch(TipsMessageException e){
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    	
    }

    /**
     * 新增我的客户
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除我的客户
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改我的客户
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }
    
    

    /**
     * 我的客户详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail(@RequestParam("proxyCustomerId") long proxyCustomerId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long proxyUserId = ShiroKit.getUser().getId();
    	try {
    		Map<String,Object> data = proxyCustomerService.detail(proxyUserId,proxyCustomerId);
    		return jsonResponse.setSuccessful().setData(data);
		} catch(TipsMessageException e){
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }
    
    /**
     * 获取客户统计
     */
    @RequestMapping("/getCustomerStatistics")
    @ResponseBody
    public Object getCustomerStatistics(){
    	JsonResponse jsonResponse = new JsonResponse();
    	long proxyUserId = ShiroKit.getUser().getId();
    	try {
    		Map<String,Object> data = proxyCustomerService.getCustomerStatistics(proxyUserId);
    		return jsonResponse.setSuccessful().setData(data);
		} catch(TipsMessageException e){
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }
    /**
     * 小程序服务申请开通
     */
    @RequestMapping("/applyMiniprogram")
    @ResponseBody
    @NoLogin
    public Object  applyMiniprogram(
    		                        @RequestParam("applyName") String applyName,
    		                        @RequestParam("registerPhoneNumber") String registerPhoneNumber,
    		                        @RequestParam("comfirmPhoneNumber") String comfirmPhoneNumber,
    		                        @RequestParam("proxyUserId") long proxyUserId){
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		proxyCustomerService.applyMiniprogram(applyName, registerPhoneNumber, comfirmPhoneNumber, proxyUserId);
    		return jsonResponse.setSuccessful();
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    	
    }
    
}
