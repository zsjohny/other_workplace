package com.ground.web.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.GroundConditionRuleMapper;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.GroundConditionRule;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.entity.newentity.GroundWithdrawCashRecord;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.GroundWithdrawCashRecordService;
import com.jiuyuan.service.common.IGroundWithdrawCashRecordService;
import com.jiuyuan.service.common.MyIncomeService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Controller
@RequestMapping(value="/myInfo")
@Login
public class MyIncomeController {
	private static final Log logger = LogFactory.get();
	
	@Autowired
	private MyIncomeService myIncomeService;
	
	@Autowired
	private IGroundWithdrawCashRecordService groundWithdrawCashRecordService;
	
	@Autowired
	private GroundConditionRuleMapper groundConditionRuleMapper;
	/**
	 * 我的收入
	 * @param userDetail
	 * @return
	 */
	@RequestMapping(value = "/getMyIncome")
	@ResponseBody
	public JsonResponse getMyIncome(UserDetail<GroundUser> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		long grandUserId = userDetail.getId();
		if(grandUserId==0){
			logger.error("我的收入grandUserId:"+grandUserId);
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			Map<String,Object> myIncome = myIncomeService.getMyIncome(grandUserId);
			return jsonResponse.setSuccessful().setData(myIncome);
		} catch (Exception e) {
			logger.error("我的收入e:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	/**
	 * 我的奖金(简化版)
	 * @param userDetail
	 * @return
	 */
	@RequestMapping(value = "/getMyBonusList")
	@ResponseBody
	public JsonResponse getMyBonusList(UserDetail<GroundUser> userDetail,
			@RequestParam(value="searchTimeStart",required=false,defaultValue="")String searchTimeStart,
			@RequestParam(value="searchTimeEnd",required=false,defaultValue="")String searchTimeEnd,
			@RequestParam(value="current",required=false,defaultValue="1")Integer current,
			@RequestParam(value="size",required=false,defaultValue="10")Integer size) {
		JsonResponse jsonResponse = new JsonResponse();
		long grandUserId = userDetail.getId();
		if(grandUserId==0){
			logger.error("我的奖金grandUserId:"+grandUserId);
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			List<Map<String,Object>> bonusList = myIncomeService.getMyBonusList(grandUserId,searchTimeStart,searchTimeEnd,current,size);
			return jsonResponse.setSuccessful().setData(bonusList);
		} catch (Exception e) {
			logger.error("我的奖金e:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	/**
	 * 我的奖金动态(详细版)
	 * @param userDetail
	 * @return
	 */
	@RequestMapping(value = "/getMyDetailedBonusList")
	@ResponseBody
	public JsonResponse getMyDetailedBonusList(UserDetail<GroundUser> userDetail,
			@RequestParam(value="searchTimeStart",required=false,defaultValue="")String searchTimeStart,
			@RequestParam(value="searchTimeEnd",required=false,defaultValue="")String searchTimeEnd,
			@RequestParam(value="current",required=false,defaultValue="1")Integer current,
			@RequestParam(value="size",required=false,defaultValue="10")Integer size) {
		JsonResponse jsonResponse = new JsonResponse();
		long grandUserId = userDetail.getId();
		if(grandUserId==0){
			logger.error("我的奖金动态grandUserId:"+grandUserId);
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			List<Map<String,Object>> bonusList = myIncomeService.getMyDetailedBonusList(grandUserId,searchTimeStart,searchTimeEnd,current,size);
			return jsonResponse.setSuccessful().setData(bonusList);
		} catch (Exception e) {
			logger.error("我的奖金动态e:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	/**
	 * 提现记录
	 * @param userDetail
	 * @return
	 */
	@RequestMapping(value = "/getMyWithdrawalList")
	@ResponseBody
	public JsonResponse getMyWithdrawalList(UserDetail<GroundUser> userDetail,
			    @RequestParam(value="applyNo",required=false,defaultValue="0")long applyNo,
				@RequestParam(value="type",required=false)Integer type,
				@RequestParam(value="current",required=false,defaultValue="1")Integer current,
				@RequestParam(value="size",required=false,defaultValue="20")Integer size) {
		JsonResponse jsonResponse = new JsonResponse();
		long grandUserId = userDetail.getId();
		try {
			if(grandUserId==0){
				logger.error("提现记录grandUserId:"+grandUserId);
				jsonResponse.setError("我的Id为空，请确认");
			}
			//获取提现记录
			Map<String,Object> data = new HashMap<String,Object>();
			List<Map<String,Object>> withdrawalList = groundWithdrawCashRecordService.getMyWithdrawalList(applyNo,type,grandUserId,new Page<GroundWithdrawCashRecord>(current,size));
			data.put("withdrawalList", withdrawalList);
			//获取未处理提现记录数
			int unWithdrawalCount = groundWithdrawCashRecordService.getUnWithdrawalCount(grandUserId);
			data.put("unWithdrawalCount", unWithdrawalCount);
			data.put("total", unWithdrawalCount);
			data.put("unWithdrawalCount", unWithdrawalCount);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("提现记录e:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	/**
	 * 提现记录详情
	 * @param userDetail
	 * @return
	 */
	@RequestMapping(value = "/getMyWithdrawalInfo")
	@ResponseBody
	public JsonResponse getMyWithdrawalInfo(UserDetail<GroundUser> userDetail,
			@RequestParam(value="groundWithdrawCashRecordId",required=true)long groundWithdrawCashRecordId) {
		JsonResponse jsonResponse = new JsonResponse();
		long grandUserId = userDetail.getId();
		if(grandUserId==0){
			logger.error("提现记录详情grandUserId:"+grandUserId);
			jsonResponse.setError("我的Id为空，请确认");
		}
		try {
			//获取提现记录详情
			Map<String,Object> withdrawalInfo = groundWithdrawCashRecordService.getMyWithdrawalInfo(groundWithdrawCashRecordId);
			return jsonResponse.setSuccessful().setData(withdrawalInfo);
		} catch (Exception e) {
			logger.error("提现记录详情e:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	
	/**
	 * 申请提现
	 * @param withdrawalMoney 申请提现金额
	 * @param userDetail
	 * @return
	 */
	@RequestMapping(value = "/applicationWithdrawal")
	@ResponseBody
	public JsonResponse applicationWithdrawal(@RequestParam(value="withdrawalMoney",required=true)double withdrawalMoney,UserDetail<GroundUser> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			long grandUserId = userDetail.getId();
			if(grandUserId==0){
				logger.error("申请提现grandUserId不能为0，grandUserId"+grandUserId);
				jsonResponse.setError("地推人员ID为空，请登陆");
			}
			//申请提现
			GroundUser groundUser =userDetail.getUserDetail();
			double availableBalance = groundWithdrawCashRecordService.applicationWithdrawal(groundUser,withdrawalMoney);
			DecimalFormat df =new DecimalFormat("#0.00");
			Map<String,Object> date = new HashMap<String,Object>();
			date.put("balance", df.format(availableBalance));//余额
			date.put("withdrawalMoney", df.format(-withdrawalMoney));//申请提现金额
			return jsonResponse.setSuccessful().setData(date);
		} catch (Exception e) {
			logger.error("申请提现e:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
	/**
	 * 可提现余额
	 * @param userDetail
	 * @return
	 */
	@RequestMapping(value = "/getAbleBalance")
	@ResponseBody
	public JsonResponse getAbleBalance(UserDetail<GroundUser> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		long grandUserId = userDetail.getId();
		if(grandUserId==0){
			logger.error("获取可提现余额grandUserId:"+grandUserId);
			jsonResponse.setError("我的Id为空，请确认");
		}
		try {
			Wrapper<GroundConditionRule> wrapper = new EntityWrapper<GroundConditionRule>();
			wrapper.eq("rule_type", 4).eq("status", 0);
			List<GroundConditionRule> groundConditionRuleList = groundConditionRuleMapper.selectList(wrapper);
			String minWithdrawal = groundConditionRuleList.get(0).getContent();
			GroundUser groundUser=userDetail.getUserDetail();
			double availableBalance = groundUser.getAvailableBalance();
//			double minWithdrawal = groundUser.getMinWithdrawal();
			Map<String,Object> ableBalance = new HashMap<String,Object>();
			DecimalFormat df =new DecimalFormat("#0.00");
			ableBalance.put("ableBalance", df.format(availableBalance));//可提现余额
			ableBalance.put("minWithdrawal", groundWithdrawCashRecordService.minWithdrawal());//最低提现额
			return jsonResponse.setSuccessful().setData(ableBalance);
		} catch (Exception e) {
			logger.error("获取可提现余额e:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
			}
		}
}
