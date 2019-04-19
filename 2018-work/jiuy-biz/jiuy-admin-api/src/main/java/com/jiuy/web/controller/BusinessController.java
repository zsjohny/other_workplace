package com.jiuy.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.core.business.facade.StoreFinanceLogFacade;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.aftersale.StoreSettlement;
import com.jiuy.core.meta.aftersale.StoreSettlementVO;
import com.jiuy.core.meta.order.OrderNew;
import com.jiuy.core.service.StoreFinanceLogService;
import com.jiuy.core.service.aftersale.BusinessWithDrawService;
import com.jiuy.core.service.aftersale.SettlementService;
import com.jiuy.core.service.aftersale.StoreSettlementService;
import com.jiuy.core.service.order.OrderItemService;
import com.jiuy.core.service.order.OrderOldService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuy.web.controller.util.ExcelUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.afterSale.SettlementOrderNewVO;
import com.jiuyuan.entity.newentity.WithdrawApplyNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.entity.withdraw.WithDrawApply;
import com.jiuyuan.entity.withdraw.WithDrawApplyVO;
import com.jiuyuan.service.common.IWithdrawApplyNewService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/business")
@Login
public class BusinessController {
	
	@Resource
	private BusinessWithDrawService businessWithDrawService;
	
	@Resource
	private SettlementService settlementService;
	
	@Resource
	private StoreSettlementService storeSettlementService;
	
	@Resource
	private StoreFinanceLogService storeFinanceLogService;
	
	@Resource
	private OrderOldService orderOldService;
	
	@Resource
	private OrderItemService orderItemService;
	
	@Autowired
	private StoreFinanceLogFacade storeFinanceLogFacade;
	
	@Autowired
	private IWithdrawApplyNewService withdrawApplyNewService;
	

	
    
	
	private static final Logger logger = LoggerFactory.getLogger(BusinessController.class);
	

	
	
	
    /**
     * 门店提现的反馈
     * @param id
     * @param relatedId
     * @param money
     * @param remark
     * @return
     */
	@RequestMapping(value = "/withdraw/feedBack")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse withDrawFeedBack(
			@RequestParam(value = "id") long id,
			@RequestParam(value="related_id") long relatedId,
			@RequestParam(value = "money") double money,
			@RequestParam(value = "remark") String remark) {            
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			storeFinanceLogFacade.withdrawFeedBack(id,relatedId,money,remark);
		} catch (ParameterErrorException e) {
			jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
			logger.error(e.getMessage());
		}
		
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
	

	@RequestMapping(value="/settlement/detail/search")
	@ResponseBody	
	public JsonResponse settlementDetailSearch(PageQuery pageQuery,
			@RequestParam(value="business_id") long businessId,
			@RequestParam(value="start_time") long startTime,
			@RequestParam(value="end_time") long endTime,
			@RequestParam(value="order_type",required=false,defaultValue="-1") int orderType,
			@RequestParam(value="business_number",required=false,defaultValue="0") long businessNumber,
			@RequestParam(value="business_name",required=false,defaultValue="") String businessName,
			@RequestParam(value="order_no", required=false, defaultValue="0") long orderNo,
			@RequestParam(value="yjj_number", required=false, defaultValue="0") long yjjNumber,
			@RequestParam(value="start_total_pay", required=false, defaultValue="0") double startTotalPay,
			@RequestParam(value="end_total_pay", required=false, defaultValue="0") double endTotalPay,
			@RequestParam(value="start_commission", required=false, defaultValue="0") double startCommission,
			@RequestParam(value="end_commission", required=false, defaultValue="0") double endCommission,
			@RequestParam(value="start_ac", required=false, defaultValue="0") double startAvailableCommission,
			@RequestParam(value="end_ac", required=false, defaultValue="0") double endAvailableCommission
			) {
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();
		
		SettlementOrderNewVO settlementVO = new SettlementOrderNewVO(businessId,orderNo,yjjNumber,startTotalPay,endTotalPay,startCommission,endCommission,startAvailableCommission,endAvailableCommission,startTime,endTime,orderType);
        int totalCount = settlementService.searchCount(settlementVO);
       
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

		List<OrderNew> list = settlementService.search(pageQuery, settlementVO);
		
		data.put("list", list);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/settlement/detail/excel")
	@ResponseBody
    @AdminOperationLog
	public void excelOfDetail(HttpServletResponse response,
			@RequestParam(value="business_id") long businessId,
			@RequestParam(value="start_time") long startTime,
			@RequestParam(value="end_time") long endTime,
			@RequestParam(value="business_number") long businessNumber,
			@RequestParam(value="business_name") String businessName,
			@RequestParam(value="order_type",required=false,defaultValue="-1") int orderType,
			@RequestParam(value="order_no", required=false, defaultValue="0") long orderNo,
			@RequestParam(value="yjj_number", required=false, defaultValue="0") long yjjNumber,
			@RequestParam(value="start_total_pay", required=false, defaultValue="0") double startTotalPay,
			@RequestParam(value="end_total_pay", required=false, defaultValue="0") double endTotalPay,
			@RequestParam(value="start_commission", required=false, defaultValue="0") double startCommission,
			@RequestParam(value="end_commission", required=false, defaultValue="0") double endCommission,
			@RequestParam(value="start_ac", required=false, defaultValue="0") double startAvailableCommission,
			@RequestParam(value="end_ac", required=false, defaultValue="0") double endAvailableCommission
			) throws IOException {
    	 
    	SettlementOrderNewVO settlementVO = new SettlementOrderNewVO(businessId,orderNo,yjjNumber,startTotalPay,endTotalPay,startCommission,endCommission,startAvailableCommission,endAvailableCommission,startTime,endTime,orderType);
    	List<OrderNew> listAll = settlementService.searchAll(settlementVO);
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();   

    	for (OrderNew orderNew : listAll) {
    		List<Long> list2 = orderOldService.searchOfParentId(orderNew.getOrderNo());
    		int buyCounts = orderItemService.searchOfOrderNos(list2);
    		Map<String, Object> map = new HashMap<>();
    		map.put("orderNo", orderNew.getOrderNo());
    		map.put("payTime", orderNew.getPayTime() != 0 ?DateUtil.convertMSEL(orderNew.getPayTime()):"无");
    		map.put("yjjNumber", orderNew.getYjjNumber());
    		map.put("buyCounts", buyCounts);
    		map.put("totalMoney", orderNew.getTotalMoney());
    		map.put("coupon", orderNew.getTotalMoney()-orderNew.getTotalPay());
    		map.put("totalPay",orderNew.getTotalPay());
    		map.put("commission", orderNew.getCommission());
    		map.put("afterSellNum", orderNew.getAfterSellNum());
    		map.put("returnMoney",orderNew.getReturnMoney());
    		map.put("availableCommission", orderNew.getAvailableCommission());
    		list.add(map);
		}
        String columnNames[] =
            {  "订单编号", "下单时间", "下单用户俞姐姐号", "订单件数", "订单金额（元）", "优惠金额（元）", "实际金额（元）", 
            		"提成金额（元）" ,"售后申请（个）", "退款总计（元）", "可提现金额（元）"};// 列名
        String keys[] = {"orderNo" ,"payTime","yjjNumber","buyCounts","totalMoney","coupon", "totalPay", "commission", 
        			"afterSellNum" , "returnMoney", "availableCommission"};//map中的key
       
        ExcelUtil.exportExcel(response, list, keys, columnNames, "门店商家号："+businessNumber +"门店账户名称："+businessName+" 统计时间："+DateUtil.convertMSEL(startTime).split(" ")[0]+"至"+DateUtil.convertMSEL(endTime).split(" ")[0]+"）");
	}
	
	@RequestMapping(value="/settlement/search")
	@ResponseBody	
	public JsonResponse settlementSearch(PageQuery pageQuery,
			@RequestParam(value="start_time") String startSettlementTime,
			@RequestParam(value="end_time") String endSettlementTime,
			@RequestParam(value="business_ids", required=false, defaultValue="") String businessIds,
			@RequestParam(value="start_member",required=false, defaultValue = "-1") long startMember,
			@RequestParam(value="end_member",required=false , defaultValue =" -1") long endMember,
			@RequestParam(value = "start_order",required = false ,defaultValue =" -1") long startOrder,
			@RequestParam(value = "end_order" , required = false , defaultValue = "-1") long endOrder,
			@RequestParam(value = "start_pay" , required =false , defaultValue = "0") double startPay,
			@RequestParam(value = "end_pay" , required = false ,defaultValue = "0") double endPay,
			@RequestParam(value = "start_commission" , required = false ,defaultValue =" 0") double startCommission,
			@RequestParam(value = "end_commission" , required = false ,defaultValue ="0") double endCommission,
			@RequestParam(value = "start_balance" , required = false ,defaultValue ="0") double startBalance,
			@RequestParam(value= "end_balance" , required = false ,defaultValue = "0") double endBalance
			) {
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();

        long startSettlementTimeL = 0L;
        long endSettlementTimeL = 0L;
    	try {
    		startSettlementTimeL = DateUtil.convertToMSEL(startSettlementTime);		
    		endSettlementTimeL = DateUtil.convertToMSEL(endSettlementTime);
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
		} 
        
    	List<String> listId =null;
    	if(!StringUtils.equals(businessIds, "")){
    		listId = new ArrayList<>();
    		String[] busIds = businessIds.split(",");
    		for (String business : busIds) {
				listId.add(business);
			}
    	}
    	StoreSettlementVO storeSettlementVO = new StoreSettlementVO(startSettlementTimeL, endSettlementTimeL, listId,startMember,endMember,startOrder,endOrder,startPay,endPay,startCommission,endCommission,startBalance,endBalance);
        int totalCount = storeSettlementService.searchCount(storeSettlementVO);
        
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

		List<StoreSettlement> list = storeSettlementService.search(pageQuery, storeSettlementVO);
		
		data.put("list", list);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}

	@RequestMapping(value="/settlement/excel")
	@ResponseBody	
	public void excelOfSettlement(HttpServletResponse response,
			@RequestParam(value="start_time",required=false,defaultValue="") String startSettlementTime,
			@RequestParam(value="end_time",required=false,defaultValue="") String endSettlementTime,
			@RequestParam(value="business_ids", required=false, defaultValue="") String businessIds,
			@RequestParam(value="start_member",required=false, defaultValue = "-1") long startMember,
			@RequestParam(value="end_member",required=false , defaultValue =" -1") long endMember,
			@RequestParam(value = "start_order",required = false ,defaultValue =" -1") long startOrder,
			@RequestParam(value = "end_order" , required = false , defaultValue = "-1") long endOrder,
			@RequestParam(value = "start_pay" , required =false , defaultValue = "0") double startPay,
			@RequestParam(value = "end_pay" , required = false ,defaultValue = "0") double endPay,
			@RequestParam(value = "start_commission" , required = false ,defaultValue =" 0") double startCommission,
			@RequestParam(value = "end_commission" , required = false ,defaultValue ="0") double endCommission,
			@RequestParam(value = "start_balance" , required = false ,defaultValue ="0") double startBalance,
			@RequestParam(value= "end_balance" , required = false ,defaultValue = "0") double endBalance) throws IOException {
		JsonResponse jsonResponse = new JsonResponse();

        long startSettlementTimeL = 0L;
        long endSettlementTimeL = 0L;
    	try {
    		startSettlementTimeL = DateUtil.convertToMSEL(startSettlementTime);		
    		endSettlementTimeL = DateUtil.convertToMSEL(endSettlementTime);
		} catch (ParseException e) {
			jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
		} 
        
    	List<String> listId =null;
    	if(!StringUtils.equals(businessIds, "")){
    		listId = new ArrayList<>();
    		String[] busIds = businessIds.split(",");
    		for (String business : busIds) {
				listId.add(business);
			}
    	}
    	StoreSettlementVO storeSettlementVO = new StoreSettlementVO(startSettlementTimeL, endSettlementTimeL, listId,startMember,endMember,startOrder,endOrder,startPay,endPay,startCommission,endCommission,startBalance,endBalance);
		List<StoreSettlement> listAll = storeSettlementService.searchAll(storeSettlementVO);
		 List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	    	
	    	for (StoreSettlement storeSettlement : listAll) {
	    		Map<String, Object> map = new HashMap<>();
	    		map.put("businessNumber",storeSettlement.getBusinessNumber());
	    		map.put("businessName", storeSettlement.getBusinessName());
	    		map.put("memberNum", storeSettlement.getMemberNumber());
	    		map.put("orderNum", storeSettlement.getOrderNum());
	    		map.put("totalPay", storeSettlement.getTotalPay());
	    		map.put("totalCommission", storeSettlement.getTotalCommission());
	    		map.put("availableBalance", storeSettlement.getAvailableBalance());
	    		list.add(map);
			}
	        String columnNames[] =
	            { "门店商家号", "商家名称", "门店会员数", "订单总数", "消费总金额（元）", "提成总额（元）", "可提成总额（元）"};// 列名
	        String keys[] = {"businessNumber" ,"businessName","memberNum","orderNum","totalPay","totalCommission", "availableBalance"};//map中的key
	        ExcelUtil.exportExcel(response, list, keys, columnNames, "门店商家数据统计（"+startSettlementTime.split(" ")[0]+"至"+endSettlementTime.split(" ")[0]+"）");
	}
}
