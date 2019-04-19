package com.jiuy.supplier.modular.setManage.controller;

import com.admin.core.base.controller.BaseController;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuy.supplier.core.shiro.ShiroUser;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.service.common.UserNewService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 订单起订设置控制器
 *
 * @author fengshuonan
 * @Date 2018-01-11 11:05:08
 */
@Controller
@RequestMapping("/orderQuantitySet")
public class OrderQuantitySetController extends BaseController {

    private String PREFIX = "/setManage/orderQuantitySet/";
    
    @Autowired
    private UserNewService userNewService;
    
    @Autowired
    private UserNewMapper userNewMapper;

    /**
     * 跳转到订单起订设置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "orderQuantitySet.html";
    }

    /**
     * 跳转到添加订单起订设置
     */
    @RequestMapping("/orderQuantitySet_add")
    public String orderQuantitySetAdd() {
        return PREFIX + "orderQuantitySet_add.html";
    }

    /**
     * 跳转到修改订单起订设置
     */
    @RequestMapping("/orderQuantitySet_update/{orderQuantitySetId}")
    public String orderQuantitySetUpdate(@PathVariable Integer orderQuantitySetId, Model model) {
        return PREFIX + "orderQuantitySet_edit.html";
    }

    /**
     * 获取订单起订设置列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增订单起订设置
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @AdminOperationLog
    public Object add(@RequestParam(value = "wholesaleCost",required = false,defaultValue = "0") double wholesaleCost,//起定额
    		@RequestParam(value = "wholesaleCount",required = false,defaultValue = "0") int wholesaleCount) {//起定量
    	JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
		try {
			UserNew supplierUserInfo = userNewService.getSupplierUserInfo(userId);
			if (supplierUserInfo == null ) {
				return jsonResponse.setError("请核实身份信息！");
			}
				supplierUserInfo.setId(userId);
				supplierUserInfo.setWholesaleCost(wholesaleCost);
				supplierUserInfo.setWholesaleCount(wholesaleCount);
				userNewMapper.updateById(supplierUserInfo);
				return jsonResponse.setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError("添加订单起订信息失败！");
		}
    }

    /**
     * 删除订单起订设置
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改订单起订设置
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @AdminOperationLog
    public Object update(){
    	JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
		try {
			UserNew supplierUserInfo = userNewService.getSupplierUserInfo(userId);
			if (supplierUserInfo == null ) {
				return jsonResponse.setError("请核实身份信息！");
			}
			Map<String,Object> map = new HashMap<>();
			map.put("wholesaleCount", supplierUserInfo.getWholesaleCount()==null?0:supplierUserInfo.getWholesaleCount());
			map.put("wholesaleCost", supplierUserInfo.getWholesaleCost());
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			return jsonResponse.setError("获取订单起订信息失败！");
		}
    }

    /**
     * 订单起订设置详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
