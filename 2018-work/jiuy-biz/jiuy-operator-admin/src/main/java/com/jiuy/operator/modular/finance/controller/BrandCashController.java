package com.jiuy.operator.modular.finance.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.newentity.WithdrawApplyNew;
import com.jiuyuan.service.common.WithdrawApplyNewService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 品牌商家提现控制器
 *
 * @author fengshuonan
 * @Date 2018-01-02 10:15:36
 */
@Controller
@RequestMapping("/brandCash")
@Login
public class BrandCashController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(BrandCashController.class);

    private String PREFIX = "/finance/brandCash/";
    
    @Autowired
    private WithdrawApplyNewService withdrawApplyNewService;

    /**
     * 跳转到品牌商家提现首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "brandCash.html";
    }

    /**
     * 跳转到添加品牌商家提现
     */
    @RequestMapping("/brandCash_add")
    public String brandCashAdd() {
        return PREFIX + "brandCash_add.html";
    }

    /**
     * 跳转到修改品牌商家提现
     */
    @RequestMapping("/brandCash_update/{brandCashId}")
    public String brandCashUpdate(@PathVariable Integer brandCashId, Model model) {
        return PREFIX + "brandCash_edit.html";
    }


    
	/**
	 * 获取品牌商家提现列表
	 * @param pageQuery
	 * @param orderId
	 * @param tradeNo
	 * @param status
	 * @param type
	 * @param startApplyMoney
	 * @param endApplyMoney
	 * @param startReturnMoney
	 * @param endReturnMoney
	 * @param startDealTime
	 * @param endDealTime
	 * @param startCreateTime
	 * @param endCreateTime
	 * @return
	 */
	@RequestMapping(value="/list")
	@ResponseBody	
	public Object withDrawSearch(
			@RequestParam(value="trade_id", required=false, defaultValue="-1") long tradeId,//提现订单号,没填就不传
			@RequestParam(value="trade_no", required=false, defaultValue="") String tradeNo,//交易编号,没填就不传
			@RequestParam(value="status", required=false, defaultValue="-1") int status,//状态 0 未处理 1 交易完成 2 已拒绝 3 已冻结 ,没填就不传,代表全部
			@RequestParam(value="type", required = false, defaultValue = "1") int type,//商家类型 0 门店 1 品牌货款 2 品牌物流 3 品牌 
			@RequestParam(value="start_apply_money", required=false, defaultValue="-1") double startApplyMoney,//申请金额下限,没填就不传
			@RequestParam(value="end_apply_money", required=false, defaultValue="-1") double endApplyMoney,//申请金额上限,没填就不传
			@RequestParam(value="start_create_time", required=false, defaultValue="") String startCreateTime,//申请时间起始,没填就不传
			@RequestParam(value="end_create_time", required=false, defaultValue="") String endCreateTime,//申请时间截止,没填就不传
			@RequestParam(value="supplierId",required = false,defaultValue = "0") long supplierId//商家号,没填就不传
			) {

		JsonResponse jsonResponse = new JsonResponse();
		Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
        long startCreateTimeL = 0L;
        long endCreateTimeL = 0L;
    	try {
    		startCreateTimeL = DateUtil.convertToMSEL(startCreateTime);		
    		endCreateTimeL = DateUtil.convertToMSEL(endCreateTime);
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
		}
    	
//    	WithDrawApplyVO businessWithDraw = new WithDrawApplyVO ( tradeId, tradeNo, status, type, startApplyMoney, endApplyMoney, startCreateTimeL, endCreateTimeL, supplierId );
//        int totalCount = businessWithDrawService.searchCount(businessWithDraw);
//        
//        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	
        try {
        	List<WithdrawApplyNew> list = withdrawApplyNewService.search(page, tradeId, tradeNo, status, type, startApplyMoney, endApplyMoney, startCreateTimeL, endCreateTimeL, supplierId);
        	List<Map<String,Object>> withdrawApplyList = new ArrayList<Map<String,Object>>();
        	for(WithdrawApplyNew withdrawApply:list){
        		Map<String,Object> map = new HashMap<String,Object>();
        		map.put("id", withdrawApply.getId());
        		map.put("relatedId", withdrawApply.getRelatedId());
        		map.put("tradeId", withdrawApply.getTradeId()+"");
        		map.put("status", withdrawApply.getStatus());
        		map.put("type", withdrawApply.getType());
        		map.put("applyMoney", withdrawApply.getApplyMoney());
        		String dealTime ="";
        		if(withdrawApply.getDealTime() != null){
        			dealTime = DateUtil.format(withdrawApply.getDealTime(), "yyyy-MM-dd HH:mm:ss");
        		}
        		map.put("dealTime", dealTime);
        		map.put("remark", withdrawApply.getRemark());
        		String createTime = DateUtil.format(withdrawApply.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
        		map.put("createTime", createTime);
        		withdrawApplyList.add(map);
        	}
        	
        	page.setRecords(withdrawApplyList);
        	return super.packForBT(page);
			
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}

    /**
     * 新增品牌商家提现
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除品牌商家提现
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改品牌商家提现
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }


    /**
	 * 提现订单详情
	 */
	@RequestMapping("/detail")
	@ResponseBody
	public JsonResponse withdrawDetail(@RequestParam("withdrawApplyId") long withDrawApplyId//提现订单ID
			                          ){
		JsonResponse jsonResponse = new JsonResponse();
		WithdrawApplyNew withDrawApplyNew = withdrawApplyNewService.getWithdrawApplyInfoById(withDrawApplyId);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tradeId", String.valueOf(withDrawApplyNew.getTradeId()));//提现订单编号
		map.put("supplierId", withDrawApplyNew.getRelatedId());//商家号
		int status = withDrawApplyNew.getStatus();
		map.put("status", status);//状态
		map.put("applyMoney", withDrawApplyNew.getApplyMoney());//申请金额
		String createTime = DateUtil.format(withDrawApplyNew.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		map.put("createTime", createTime);//申请时间
		if(status == WithdrawApplyNew.DEAL_SUCCESS){
    		String dealTime ="";
    		if(withDrawApplyNew.getDealTime() != null){
    			dealTime = DateUtil.format(withDrawApplyNew.getDealTime(), "yyyy-MM-dd HH:mm:ss");
    		}
			map.put("dealTime", dealTime);//完成时间
			map.put("remark", withDrawApplyNew.getRemark());//操作说明
			map.put("money", withDrawApplyNew.getMoney());//交易金额
			map.put("tradeWay", withDrawApplyNew.getTradeWay());//交易方式
			map.put("tradeNo", withDrawApplyNew.getTradeNo());//交易编号
		}
		if(status == WithdrawApplyNew.REFUSEED){
			String dealTime ="";
    		if(withDrawApplyNew.getDealTime() != null){
    			dealTime = DateUtil.format(withDrawApplyNew.getDealTime(), "yyyy-MM-dd HH:mm:ss");
    		}
			map.put("dealTime", dealTime);//完成时间
			map.put("remark", withDrawApplyNew.getRemark());//操作说明
		}
		if(status == WithdrawApplyNew.FREEZED){
			String freezeTime ="";
    		if(withDrawApplyNew.getDealTime() != null){
    			freezeTime = DateUtil.format(withDrawApplyNew.getDealTime(), "yyyy-MM-dd HH:mm:ss");
    		}
			map.put("freezeTime", freezeTime);//冻结时间
		}
		return jsonResponse.setSuccessful().setData(map);
	}
	
    /**
	 * 提现确认打款
	 * @param id
	 * @param relatedId
	 * @param money
	 * @param type
	 * @param tradeNo
	 * @param tradeWay 财务退款方式 1:支付宝 3:微信 5:银行汇款 6:现金交易
	 * @param remark
	 * @return
	 */
	@RequestMapping(value = "/withdraw/confirm")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse withDrawConfirm(
			@RequestParam(value = "id") long id,
//			@RequestParam(value = "adminId") long adminId,
			@RequestParam(value = "money",required = false,defaultValue = "0.00") double money,
			@RequestParam(value = "type",required = false,defaultValue = "1") int type,
			@RequestParam(value = "trade_no",required = false,defaultValue = "") String tradeNo,
			@RequestParam(value = "trade_way",required = false,defaultValue = "0" ) int tradeWay,
			@RequestParam(value = "remark",required= false,defaultValue = "") String remark,
			@RequestParam(value = "status") int status) {            
		JsonResponse jsonResponse = new JsonResponse();
		try {
//			businessWithDrawService.withDrawConfirm(id,relatedId,money,type,tradeNo,tradeWay,remark);
			withdrawApplyNewService.withDrawConfirm(id,null,money,type,tradeNo,tradeWay,remark,status);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
}
