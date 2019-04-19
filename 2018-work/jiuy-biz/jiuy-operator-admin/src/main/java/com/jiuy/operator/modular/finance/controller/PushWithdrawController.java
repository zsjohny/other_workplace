package com.jiuy.operator.modular.finance.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.operator.common.system.service.IPushWithdrawService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 财务管理控制器
 *
 * @author fengshuonan
 * @Date 2017-11-02 14:43:07
 */
@Controller
@RequestMapping("/pushWithdraw")
@Login
public class PushWithdrawController extends BaseController {
	
	private static final Log logger = LogFactory.get();
	
    private String PREFIX = "/finance/pushWithdraw/";
    
    @Autowired
    private IPushWithdrawService pushWithdrawService;
    
    /**
     * 跳转到财务管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "pushWithdraw.html";
    }

    /**
     * 跳转到添加财务管理
     */
    @RequestMapping("/pushWithdraw_add")
    public String pushWithdrawAdd() {
        return PREFIX + "pushWithdraw_add.html";
    }

    /**
     * 跳转到修改财务管理
     */
    @RequestMapping("/pushWithdraw_edit")
    public String pushWithdrawEdit() {
        return PREFIX + "pushWithdraw_edit.html";
    }

    /**
     * 获取财务管理列表
     * @throws ParseException 
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(
    		@RequestParam(value = "groundWithdrawCashRecordId",required = false,defaultValue="-1") long groundWithdrawCashRecordId,
    		@RequestParam(value = "transactionNo",required = false,defaultValue="") String transactionNo,
    		@RequestParam(value = "groundUserId",required = false,defaultValue="-1") long groundUserId,
    		@RequestParam(value = "applyBeginTime",required = false,defaultValue="") String applyBeginTime,
    		@RequestParam(value = "applyEndTime",required = false,defaultValue="") String applyEndTime,
    		@RequestParam(value = "dealBeginTime",required = false,defaultValue="") String dealBeginTime,
    		@RequestParam(value = "dealEndTime",required = false,defaultValue="") String dealEndTime,
    		@RequestParam(value = "transactionAmountMin",required = false,defaultValue="-1") double transactionAmountMin,
    		@RequestParam(value = "transactionAmountMax",required = false,defaultValue="-1") double transactionAmountMax,
    		@RequestParam(value = "withdrawCashAmountMin",required = false,defaultValue="-1") double withdrawCashAmountMin,
    		@RequestParam(value = "withdrawCashAmountMax",required = false,defaultValue="-1") double withdrawCashAmountMax,
    		@RequestParam(value = "status",required = false) Integer status )  {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		
		
    	try{ 
    		data = pushWithdrawService.listPage(page,groundWithdrawCashRecordId,transactionNo,groundUserId,applyBeginTime,applyEndTime,dealBeginTime,dealEndTime,
    				transactionAmountMin,transactionAmountMax,withdrawCashAmountMin,withdrawCashAmountMax,status);
    		page.setRecords(data);
			return super.packForBT(page);
    	}catch (Exception e) {
    		logger.error("获取财务管理列表e:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }

    /**
     * 新增财务管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除财务管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改财务管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 财务管理详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public JsonResponse detail(@RequestParam(value="groundWithdrawCashRecordId",required = true) long groundWithdrawCashRecordId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
			Map<String, Object> groundWithdrawCashRecord = pushWithdrawService.getDetailById(groundWithdrawCashRecordId);
			return jsonResponse.setSuccessful().setData(groundWithdrawCashRecord);
		} catch (Exception e) {
			logger.error("提现记录详情e:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 打款
     * @param groundWithdrawCashRecordId
     * @return
     */
    @RequestMapping(value = "/playMoney")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse playMoney(@RequestParam(required = true) long groundWithdrawCashRecordId,
    		 					  @RequestParam(required = true) String bankName,
    		 					  @RequestParam(required = true) String bankAcountName,
    		 					  @RequestParam(required = true) String bankAcountNo,
    		 					  @RequestParam(required = false,defaultValue="") String transactionWay,
						    	  @RequestParam(required = true) double withdrawCashAmount,
						    	  @RequestParam(required = true) double transactionAmount,
						    	  @RequestParam(required = true) String transactionNo,
						    	  @RequestParam(required = false,defaultValue="") String remark) {
    	JsonResponse jsonResponse = new JsonResponse();
    	if(groundWithdrawCashRecordId==0){
			logger.error("确认打款groundWithdrawCashRecordId:"+groundWithdrawCashRecordId);
			jsonResponse.setError("确认打款提现申请Id为空，请确认");
		}
		try {
			boolean result = pushWithdrawService.playMoneyById(groundWithdrawCashRecordId,bankName,bankAcountName,bankAcountNo,withdrawCashAmount,
					transactionAmount,transactionNo,remark);
			return jsonResponse.setSuccessful().setData(result);
		} catch (Exception e) {
			logger.error("确认打款e:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
		
    }
   
}
