package com.jiuy.operator.modular.afterSaleManage.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.service.common.IRefundOrderAdminFacade;
import com.jiuyuan.service.common.IRefundOrderFacadeNJ;
import com.jiuyuan.service.common.IRefundOrderService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 售后管理控制器
 *
 * @author fengshuonan
 * @Date 2017-12-08 10:41:46
 */
@Controller
@RequestMapping("/afterSaleOdd")
@Login
public class AfterSaleOddController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(AfterSaleOddController.class);

    private String PREFIX = "/afterSaleManage/afterSaleOdd/";
    
	@Autowired
	private RefundOrderMapper refundOrderMapper;
	
//	@Autowired
//	private IRefundOrderFacade refundOrderFacade;
	@Autowired
	private IRefundOrderAdminFacade refundOrderAdminFacade;
	@Autowired
	private IRefundOrderFacadeNJ refundOrderFacadeNJ;
	
	
	
	@Autowired
	private IRefundOrderService refundOrderService;
	
	/**
     * 平台关闭售后订单
     * @return
     */
    @RequestMapping("/platformCloseRefundOrder")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse platformCloseRefundOrder(long refundOrderId,
    		@RequestParam(value="closeReason",required=false,defaultValue="")String closeReason) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		refundOrderAdminFacade.platformCloseRefundOrder(refundOrderId,closeReason);
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("平台关闭售后订单:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 结束平台介入
     * @return
     */
    @RequestMapping("/stopPlatformIntervene")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse stopPlatformIntervene(long refundOrderId,
    		@RequestParam(value="handlingSuggestion",required=false,defaultValue="")String handlingSuggestion) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		refundOrderAdminFacade.stopPlatformIntervene(refundOrderId,handlingSuggestion);
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("结束平台介入:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
	
	 /**
     * 根据订单获取售后订单列表
     */
    @RequestMapping(value = "/getRefundOrderListByOrderNo")
    @ResponseBody 
    public JsonResponse getRefundOrderListByOrderNo(long orderNo //订单号（订单id）
    		){
    	Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
		JsonResponse jsonResponse = new JsonResponse();
		try {
			//获取所有订单
			List<Map<String,Object>> data = refundOrderAdminFacade.getRefundOrderListByOrderNo(orderNo);
			return jsonResponse.setSuccessful().setData(data);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("根据订单获取售后订单列表:"+e.getMessage());
    		throw new RuntimeException("根据订单获取售后订单列表:"+e.getMessage());
		}
    }
	
	  /**
     * 获取售后订单详情运营平台
     * @return
     */
    @RequestMapping("/getRefundOrderInfoAdmin")
    @ResponseBody
    public JsonResponse getRefundOrderInfoAdmin(long refundOrderId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		Map<String,Object> data = refundOrderAdminFacade.getRefundOrderInfoAdmin(refundOrderId);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("获取售后订单详情:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
	

    /**
     * 跳转到售后管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "afterSaleOdd.html";
    }

    /**
     * 跳转到添加售后管理
     */
    @RequestMapping("/afterSaleOdd_add")
    public String afterSaleOddAdd() {
        return PREFIX + "afterSaleOdd_add.html";
    }


    /**
     * 获取售后管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody 
    public Object list(@RequestParam(value="refundOrderNo",required=false,defaultValue="")String refundOrderNo,//售后单号
    		@RequestParam(value="brandName",required=false,defaultValue="")String brandName,//品牌名
    		@RequestParam(value="receiver",required=false,defaultValue="")String receiver,//收货人姓名
    		@RequestParam(value="refundType",required=false,defaultValue="-1")int refundType,//售后类型
    		@RequestParam(value="refundStatus",required=false,defaultValue="-1")int refundStatus,//售后状态
    		@RequestParam(value="beginApplyTime",required=false,defaultValue="-1")long beginApplyTime,//申请时间起 
    		@RequestParam(value="endApplyTime",required=false,defaultValue="-1")long endApplyTime,//申请时间止
    		@RequestParam(value="beginReturnCount",required=false,defaultValue="-1")int beginReturnCount,//退货数量起
    		@RequestParam(value="endReturnCount",required=false,defaultValue="-1")int endReturnCount,//退货数量止
    		@RequestParam(value="beginRefundCost",required=false,defaultValue="-1")double beginRefundCost,//退款金额起
    		@RequestParam(value="endReturnCost",required=false,defaultValue="-1")double endReturnCost,//退款金额止
    		@RequestParam(value="platformInterveneState",required=false,defaultValue="-1")int  platformInterveneState,//平台介入
    		@RequestParam(value="orderNo",required=false,defaultValue="-1")long orderNo //订单号（订单id）
    		){
    	Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
		try {
			//获取所有订单
			List<Map<String,Object>> selectList = refundOrderAdminFacade.getRefundOrderList(page,refundOrderNo,brandName,receiver,refundType,refundStatus,beginApplyTime,endApplyTime,beginReturnCount,endReturnCount,beginRefundCost,endReturnCost,platformInterveneState,orderNo);
			page.setRecords(selectList);
			return super.packForBT(page);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取售后订单列表:"+e.getMessage());
    		throw new RuntimeException("获取售后订单列表:"+e.getMessage());
		}
    }

    /**
     * 新增售后管理
     * 
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除售后管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改售后管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 售后管理详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
    /**
     * 跳转到修改售后管理
     */
    @RequestMapping("/afterSaleOdd_update")
    public String afterSaleOddUpdate() {
        return PREFIX + "afterSaleOdd_edit.html";
    }
}
