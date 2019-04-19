package com.jiuy.operator.modular.staticRefer.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.operator.core.shiro.ShiroKit;
import com.jiuy.operator.core.shiro.ShiroUser;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.common.PushPerforReferService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 统计查询控制器
 *
 * @author fengshuonan
 * @Date 2017-11-02 14:35:14
 */
@Controller
@RequestMapping("/pushPerforRefer")
@Login
public class PushPerforReferController extends BaseController {

    private String PREFIX = "/staticRefer/pushPerforRefer/";
    
    private static final Log logger = LogFactory.get("统计查询Controller");
    
    @Autowired
    private PushPerforReferService pushPerforReferService;

    /**
     * 跳转到统计查询首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "pushPerforRefer.html";
    }

    /**
     * 跳转到添加统计查询
     */
    @RequestMapping("/pushPerforRefer_add")
    public String pushPerforReferAdd() {
        return PREFIX + "pushPerforRefer_add.html";
    }

    /**
     * 跳转到修改统计查询
     */
    @RequestMapping("/pushPerforRefer_update/{pushPerforReferId}")
    public String pushPerforReferUpdate(@PathVariable Integer pushPerforReferId, Model model) {
        return PREFIX + "pushPerforRefer_edit.html";
    }

    /**
     * 获取统计查询列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增统计查询
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除统计查询
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改统计查询
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 统计查询详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
    
    /**
     * 获取地推业绩查询列表
     * @return
     */
    @RequestMapping("/getSupplierOrderList")
    @ResponseBody
    public Object getPushStatisticList(@RequestParam(value="groundUserId", required=false, defaultValue = "0") long groundUserId,
    		@RequestParam(value="statisticsTimeStart", required=false, defaultValue = "") String statisticsTimeStart,
			@RequestParam(value="statisticsTimeEnd", required=false, defaultValue = "") String statisticsTimeEnd,
			@RequestParam(value="name", required=false, defaultValue = "") String name,
			@RequestParam(value="phone", required=false, defaultValue = "") String phone,
			@RequestParam(value="grandRoleType", required=false, defaultValue = "0") int grandRoleType,
			@RequestParam(value="province", required=false, defaultValue = "") String province,
			@RequestParam(value="city", required=false, defaultValue = "") String city,
			@RequestParam(value="district", required=false, defaultValue = "") String district,
			@RequestParam(value="administratorId", required=false, defaultValue = "0") long administratorId,
			@RequestParam(value="pPhone", required=false, defaultValue = "") String pPhone,
			@RequestParam(value="individualTotalSaleAmountMin", required=false, defaultValue = "0") double individualTotalSaleAmountMin,
			@RequestParam(value="individualTotalSaleAmountMax", required=false, defaultValue = "0") double individualTotalSaleAmountMax,
			@RequestParam(value="customerRegisterCountMin", required=false, defaultValue = "0") int customerRegisterCountMin,
			@RequestParam(value="customerRegisterCountMax", required=false, defaultValue = "0") int customerRegisterCountMax,
			@RequestParam(value="customerActiveCountMin", required=false, defaultValue = "0") int customerActiveCountMin,
			@RequestParam(value="customerActiveCountMax", required=false, defaultValue = "0") int customerActiveCountMax
			) {
    	Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			int statisticsTimeStartInt = 0;
			int statisticsTimeEndInt = 0;
			//获取统计结果
			if (!StringUtils.isEmpty(statisticsTimeStart)) {
				statisticsTimeStartInt=DateUtil.strToDateInt(statisticsTimeStart);
			}
			if (!StringUtils.isEmpty(statisticsTimeEnd)) {
				statisticsTimeEndInt = DateUtil.strToDateInt(statisticsTimeEnd);
			}
			
			List<Map<String,Object>> pushStatisticList = pushPerforReferService.getPushStatisticList(groundUserId,statisticsTimeStartInt,statisticsTimeEndInt,
					name,phone,grandRoleType,province,city,district,administratorId,pPhone,individualTotalSaleAmountMin,individualTotalSaleAmountMax,
					customerRegisterCountMin,customerRegisterCountMax,customerActiveCountMin,customerActiveCountMax,page);
			page.setRecords(pushStatisticList);
			return super.packForBT(page);
		} catch (Exception e) {
			logger.error("获取供应商列表:"+e.getMessage());
			throw new RuntimeException("获取供应商列表:"+e.getMessage());
		}
    }
}
