package com.jiuy.supplier.modular.myFinance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.core.base.controller.BaseController;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.common.IMyFinanceSupplierService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 申请提现控制器
 *
 * @author fengshuonan
 * @Date 2017-10-19 14:23:10
 */
@Controller
@RequestMapping("/applyCash")
public class ApplyCashController extends BaseController {
    
	private static final Log logger = LogFactory.get("ApplyCashController");
		
    private String PREFIX = "/myFinance/applyCash/";
    
	@Autowired
	private IMyFinanceSupplierService myFinanceSupplierService;
    
    /**
	 * 提交提现按钮
	 * @param applyMoney
	 * @return
	 */
	@RequestMapping("/submitApply")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse submitApply(@RequestParam("applyMoney") double applyMoney){
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if(supplierId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			myFinanceSupplierService.submitApply(supplierId,applyMoney);
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.error(e);
			return jsonResponse.setError(e.getMessage());
		}
	}

    /**
     * 跳转到申请提现首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "applyCash.html";
    }

    /**
     * 跳转到添加申请提现
     */
    @RequestMapping("/applyCash_add")
    public String applyCashAdd() {
        return PREFIX + "applyCash_add.html";
    }

    /**
     * 跳转到修改申请提现
     */
    @RequestMapping("/applyCash_update/{applyCashId}")
    public String applyCashUpdate(@PathVariable Integer applyCashId, Model model) {
        return PREFIX + "applyCash_edit.html";
    }

    /**
     * 获取申请提现列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增申请提现
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除申请提现
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改申请提现
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 申请提现详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
