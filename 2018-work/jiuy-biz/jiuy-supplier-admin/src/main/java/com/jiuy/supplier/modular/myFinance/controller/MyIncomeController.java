package com.jiuy.supplier.modular.myFinance.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.newentity.WithdrawApplyNew;
import com.jiuyuan.service.common.IMyFinanceSupplierService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 我的收入控制器
 *
 * @author fengshuonan
 * @Date 2017-10-19 14:22:45
 */
@Controller
@RequestMapping("/myIncome")
public class MyIncomeController extends BaseController {

    private String PREFIX = "/myFinance/myIncome/";
    
    private static final Log logger = LogFactory.get("MyIncomeController");
	
	@Autowired
	private IMyFinanceSupplierService myFinanceSupplierService;
	
	/**
	 * 我的收入
	 * 销售总额
	 */
	@RequestMapping("/getTotalOrderAmount")
	@ResponseBody
	public JsonResponse getTotalOrderAmount(){
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if(supplierId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			Map<String,Object> data = myFinanceSupplierService.getTotalOrderAmount(supplierId);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error(e);
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	/**
	 * 获取待结算订单总价
	 */
	@RequestMapping("/getSettlingAmount")
	@ResponseBody
	public JsonResponse getSettlingAmount(){
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if(supplierId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			Map<String,Object> data = myFinanceSupplierService.getSettlingAmount(supplierId);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error(e);
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	/**
	 * 提现记录列表接口
	 * @param supplierId 供应商ID
	 * @param minApplyMoney 申请金额下限
	 * @param maxApplyMoney 申请金额上限
	 * @param minCreateTime 格式：yyyy-MM-dd
	 * @param maxCreateTime 格式：yyyy-MM-dd
	 * @param status -1：全部，0：未处理，1：交易完成 ，2：已拒绝，3：已冻结
	 * @return
	 */
	@RequestMapping("/getWithdrawList")
	@ResponseBody
	public Object getWithdrawList(@RequestParam(value = "minApplyMoney",required = false,defaultValue = "0.0") double minApplyMoney,
			                            @RequestParam(value = "maxApplyMoney",required = false,defaultValue = "0.0") double maxApplyMoney,
			                            @RequestParam(value = "minCreateTime",required = false,defaultValue = "1970-1-1") String minCreateTime,
			                            @RequestParam(value = "maxCreateTime",required = false,defaultValue = "") String maxCreateTime,
			                            @RequestParam(value = "status",required = false,defaultValue = "-1") int status){
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if(supplierId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
		List<Map<String,Object>> list = new ArrayList<>();
		try {
			List<WithdrawApplyNew> data = myFinanceSupplierService.getWithdrawOrderList(page,supplierId,minApplyMoney,maxApplyMoney,minCreateTime,maxCreateTime,status);
			for(WithdrawApplyNew withdrawApply:data){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("tradeId", String.valueOf(withdrawApply.getTradeId()));//提现订单号
				map.put("id", withdrawApply.getId());//提现订单主键ID
				map.put("applyMoney", withdrawApply.getApplyMoney());
				map.put("status", withdrawApply.getStatus());
				String remark = "无";
				if(withdrawApply.getRemark() != ""){
					remark = withdrawApply.getRemark();
				}
				map.put("remark", remark);
				String createTime = "无";
				String dealTime = "无";
				if(withdrawApply.getCreateTime()!=null){
					createTime = DateUtil.format(new Date(withdrawApply.getCreateTime()), "yyyy-MM-dd HH:mm:ss");
				}
				map.put("createTime", createTime);
				
				if(withdrawApply.getDealTime()!=null){
					dealTime = DateUtil.format(new Date(withdrawApply.getDealTime()), "yyyy-MM-dd HH:mm:ss");
				}
				map.put("dealTime", dealTime);
				list.add(map);
			}
			page.setRecords(list);
			return super.packForBT(page);
		} catch (Exception e) {
			logger.error(e);
			return jsonResponse.setError(e.getMessage());
		}
	}
	
    /**
     * 通过订单ID查看提现订单详情
     * @param id
     * @return
     */
	@RequestMapping("/getWithdrawOrderInfo")
	@ResponseBody
	public JsonResponse getWithdrawOrderInfo(@RequestParam("id") long id){
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if(supplierId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		WithdrawApplyNew data = myFinanceSupplierService.getWithdrawOrderInfo(id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tradeId", String.valueOf(data.getTradeId()));//提现订单号
		map.put("id", data.getId());//提现订单主键ID
		map.put("applyMoney", data.getApplyMoney());
		map.put("status", data.getStatus());
		map.put("tradeMoney", data.getMoney());
		String createTime = "无";
		String dealTime = "无";
		if(data.getCreateTime()!=null){
			createTime = DateUtil.format(new Date(data.getCreateTime()), "yyyy-MM-dd HH:mm:ss");
		}
		map.put("createTime", createTime);
		
		if(data.getDealTime()!=null){
			dealTime = DateUtil.format(new Date(data.getDealTime()), "yyyy-MM-dd HH:mm:ss");
		}
		map.put("dealTime", dealTime);
		String tradeWay = "无";
		if(data.getTradeWay()!= null){
			switch(data.getTradeWay()){
			case 1:tradeWay = "支付宝";
			break;
			case 3:tradeWay = "微信";
			break;
			case 5:tradeWay = "银行汇款";
			break;
			case 6:tradeWay = "现金交易";
			break;
			}
		}
		map.put("tradeWay", data.getTradeWay());
		map.put("tradeNo", data.getTradeNo());
		String remark = "无";
		if(data.getRemark() != ""){
			remark = data.getRemark();
		}
		map.put("remark", remark);
		return jsonResponse.setSuccessful().setData(map);
	}
//    /**
//     * 获取正在处理的提现申请订单数目
//     * @param supplierId
//     * @return
//     */
//	@RequestMapping("/getCountOfDealingWDOrder")
//	@ResponseBody
//	public JsonResponse getCountOfDealingWDOrder(){
//		JsonResponse jsonResponse = new JsonResponse();
//		long supplierId = ShiroKit.getUser().getId();
//		if(supplierId == 0){
//   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
//   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
//   	 	}
//		int count = myFinanceSupplierService.getCountOfDealingWDOrder(supplierId);
//		return jsonResponse.setSuccessful().setData(count);
//	}
	/**
	 * 可提现金额数和获取正在处理的提现申请订单数目
	 */
	@RequestMapping("/availableBalanceAndCountOfOrder")
	@ResponseBody
	public JsonResponse availableBalanceAndCountOfOrder(){
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if(supplierId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		Map<String, Object> map = new HashMap<>();
		double availableBalance = myFinanceSupplierService.getAvailableBalance(supplierId);
		int count = myFinanceSupplierService.getCountOfDealingWDOrder(supplierId);
		map.put("availableBalance", availableBalance);
		map.put("OrderCount", count);
		return jsonResponse.setData(map);
	}

    /**
     * 跳转到我的收入首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "myIncome.html";
    }

    /**
     * 跳转到添加我的收入
     */
    @RequestMapping("/myIncome_add")
    public String myIncomeAdd() {
        return PREFIX + "myIncome_add.html";
    }

    /**
     * 跳转到修改我的收入
     */
    @RequestMapping("/myIncome_update/{myIncomeId}")
    public String myIncomeUpdate(@PathVariable Integer myIncomeId, Model model) {
        return PREFIX + "myIncome_edit.html";
    }

    /**
     * 获取我的收入列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增我的收入
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除我的收入
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改我的收入
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 我的收入详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
