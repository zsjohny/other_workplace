
package com.jiuy.supplier.modular.myOrder.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuy.supplier.core.shiro.ShiroUser;
import com.jiuy.web.controller.util.ExcelUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.StoreExpressInfoMapper;
import com.jiuyuan.dao.mapper.supplier.StoreMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierExpressMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.newentity.ExpressSupplier;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.StoreExpressInfo;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.SupplierCustomer;
import com.jiuyuan.service.common.IExpressNewService;
import com.jiuyuan.service.common.IOrderNewService;
import com.jiuyuan.service.common.IStoreBusinessNewService;
import com.jiuyuan.service.common.ISupplierCustomer;
import com.jiuyuan.service.common.ShopExpressService;
import com.jiuyuan.service.common.SupplierCustomerGroupService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.ResultCodeException;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 我的订单控制器
 *
 * @author fengshuonan
 * @Date 2017-10-19 14:25:08
 */
@Controller
@RequestMapping("/allOrder")
public class AllOrderController extends BaseController {

    private String PREFIX = "/myOrder/allOrder/";

    private static final Log logger = LogFactory.get("供应商订单Controller"); 
	@Autowired
	private StoreExpressInfoMapper storeExpressInfoMapper;
	@Autowired
	private IOrderNewService supplierOrderService;
	@Autowired
	private RefundOrderMapper refundOrderMapper;
	@Autowired
	private IStoreBusinessNewService storeBusinessNewService;
	
	@Autowired
	private IExpressNewService supplierExpressService;

	@Autowired
	private ShopExpressService expressService;

	@Autowired
	private StoreMapper storeMapper;
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;
	@Autowired
	private SupplierExpressMapper supplierExpressMapper;
	
	@Autowired
	private ISupplierCustomer supplierCustomerService;
	/**
     * 跳转到我的订单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "allOrder.html";
    }

    /**
     * 跳转到物流信息详情
     */
    @RequestMapping("/expressDetail")
    public String allOrderExpress() {
        return PREFIX + "expressDetail.html";
    }
    /**
     * 跳转到添加我的订单
     */
    @RequestMapping("/allOrder_add")
    public String allOrderAdd() {
        return PREFIX + "allOrder_add.html";
    }

	/**
	 * 跳转到添加我的订单
	 */
	@RequestMapping("/allOrder_send")
	public String allOrderSend() {
		return PREFIX + "allOrder_send.html";
	}

    /**
     * 跳转到订单改价页面
     */
    @RequestMapping("/allOrder_edit")
    public String allOrderUpdate() {
        return PREFIX + "allOrder_edit.html";
    }

    /**
     * 获取我的订单列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增我的订单
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除我的订单
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改我的订单
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 我的订单详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
    
    /**
	 * 获取供应商订单列表
	 * @return
	 */
    @RequestMapping("/getSupplierOrderList")
    @ResponseBody
    public Object getSupplierOrderList(@RequestParam(value="orderNo", required=false, defaultValue = "0") long orderNo,
    		@RequestParam(value="orderStatus", required=false, defaultValue = "-1") int orderStatus,
    		@RequestParam(value="addresseeName", required=false, defaultValue = "") String addresseeName,
    		@RequestParam(value="addresseeTelePhone", required=false, defaultValue = "") String addresseeTelePhone,
			@RequestParam(value="clothesNumbers", required=false, defaultValue = "") String clothesNumbers,
			@RequestParam(value="updateTimeBegin", required=false, defaultValue = "") String updateTimeBegin,
			@RequestParam(value="updateTimeEnd", required=false, defaultValue = "") String updateTimeEnd,
			@RequestParam(value="remark", required=false, defaultValue = "") String remark,
			@RequestParam(value="customerName", required=false, defaultValue = "") String customerName,
			@RequestParam(value="customerPhone", required=false, defaultValue = "") String customerPhone,
			@RequestParam(value="expressNo", required=false, defaultValue = "") String expressNo,
			@RequestParam(value="createTimeBegin", required=false, defaultValue = "") String createTimeBegin,
			@RequestParam(value="createTimeEnd", required=false, defaultValue = "") String createTimeEnd,
			@RequestParam(value="refundUnderway", required=false, defaultValue = "-1") int refundUnderway) {
    	Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			//获取所有订单
			List<Map<String,Object>> selectList = supplierOrderService.getSupplierOrderList(userId,orderNo,orderStatus,addresseeName,addresseeTelePhone,clothesNumbers,
					updateTimeBegin,updateTimeEnd,remark,customerName,customerPhone,expressNo,createTimeBegin,createTimeEnd,refundUnderway,page);
			page.setRecords(selectList);
			return super.packForBT(page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取供应商列表:"+e.getMessage());
			throw new RuntimeException("获取供应商列表:"+e.getMessage());
		}
    }
    
    /**
     * 获取供应商待发货订单列表
     * @return
     */
    @RequestMapping("/getSupplierOrderListPendingDelivery")
    @ResponseBody
    public Object getSupplierOrderListPendingDelivery(@RequestParam(value="orderNo", required=false, defaultValue = "0") long orderNo,
    		@RequestParam(value="addresseeName", required=false, defaultValue = "") String addresseeName,
    		@RequestParam(value="addresseeTelePhone", required=false, defaultValue = "") String addresseeTelePhone,
			@RequestParam(value="clothesNumbers", required=false, defaultValue = "") String clothesNumbers,
			@RequestParam(value="payTimeBegin", required=false, defaultValue = "") String payTimeBegin,
			@RequestParam(value="payTimeEnd", required=false, defaultValue = "") String payTimeEnd,
			@RequestParam(value="remark", required=false, defaultValue = "") String remark,
			@RequestParam(value="customerName", required=false, defaultValue = "") String customerName,
			@RequestParam(value="customerPhone", required=false, defaultValue = "") String customerPhone,
			@RequestParam(value="expressNo", required=false, defaultValue = "") String expressNo) {
    	Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			//获取供应商待发货订单列表
			List<Map<String,Object>> selectList = supplierOrderService.getSupplierOrderListPendingDelivery(userId,orderNo,addresseeName,addresseeTelePhone,clothesNumbers,
					payTimeBegin,payTimeEnd,remark,customerName,customerPhone,expressNo,page);
			page.setRecords(selectList);
			return super.packForBT(page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取供应商列表:"+e.getMessage());
			throw new RuntimeException("获取供应商列表:"+e.getMessage());
		}
    }
    
    /**
     * 供应商修改订单价格
     */
    @RequestMapping("/changeOrderPrice")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse changeOrderPrice(@RequestParam(value = "orderNo") long orderNo ,
    		                             @RequestParam(value = "changePrice") double changePrice){
    	JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	try {
   	 	    supplierOrderService.changeOrderPrice(orderNo, userId,changePrice);
			return jsonResponse.setSuccessful();
		}catch(TipsMessageException e){
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		}catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("系统繁忙，请稍后再试！");
		}
   	 	
    }
    
    /**
     * 供应商恢复订单价格
     */
    @RequestMapping("/restoreOrderPrice")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse restoreOrderPrice(@RequestParam(value = "orderNo") long orderNo ){
    	JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	try {
   	 	    supplierOrderService.restoreOrderPrice(orderNo, userId);
			return jsonResponse.setSuccessful();
		}catch(TipsMessageException e){
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		}catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("系统繁忙，请稍后再试！");
		}
   	 	
    }
    
    
    
    /**
     * 获取供应商订单详情
     * @return
     */
    @RequestMapping("/getOrderInfo")
    @ResponseBody
    public JsonResponse getOrderInfo(@RequestParam(value="orderNo") long orderNo) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			Map<String,Object> data = new HashMap<String,Object>();
			//1、订单信息
			StoreOrderNew storeOrder =  supplierOrderMapper.selectById(orderNo);
			data.put("orderNo", storeOrder.getOrderNo());//订单编号
			data.put("totalBuyCount", storeOrder.getTotalBuyCount());//商品件数
			data.put("orderStatus", storeOrder.getOrderStatus());//订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭
			data.put("orderStatusName", getOrderStatusName(storeOrder.getOrderStatus(),storeOrder.getOrderCloseType()));//订单状态名称
			   //改价字段
			double supplierPreferential = storeOrder.getSupplierPreferential();//商家店铺优惠
			double supplierChangePrice = storeOrder.getSupplierChangePrice();//商家店铺改价
			double supplierAddPrice = 0;
			if(supplierChangePrice >= 0 ){
				supplierPreferential += supplierChangePrice;
			}else{
				supplierAddPrice = 0-supplierChangePrice;
			}
			data.put("totalMoney", storeOrder.getTotalMoney());//订单原价
			data.put("platformPreferential",storeOrder.getPlatformTotalPreferential());//平台优惠金额
			data.put("supplierPreferential", supplierPreferential);//商家优惠金额
			data.put("supplierAddPrice", supplierAddPrice);//订单加价金额
			data.put("totalPay", storeOrder.getTotalPay());//订单改价后待付款金额
			data.put("originalPrice", storeOrder.getOriginalPrice());//订单改价前原始待付款金额
			data.put("totalExpressMoney",storeOrder.getTotalExpressMoney());
			//判断下单人是不是该供应商的客户
			Long supplierId = storeOrder.getSupplierId();
			Long storeId = storeOrder.getStoreId();
			
			StoreBusiness storeBusiness = storeMapper.selectById(storeId);
			String phoneNumber = storeBusiness.getPhoneNumber();
			SupplierCustomer supplierCustomer = supplierCustomerService.getCustomerByStoreIdOrPhoneNumber(phoneNumber, storeId, supplierId);
			if(supplierCustomer == null){
				data.put("customerId", 0);
				data.put("isCustomer", 0);
			}else{
				data.put("customerId", supplierCustomer.getId());
				data.put("isCustomer", 1);
			}
			data.put("createTime", DateUtil.parseLongTime2Str(storeOrder.getCreateTime()));//下单时间
			data.put("payTime", DateUtil.parseLongTime2Str(storeOrder.getPayTime()));//付款时间
			data.put("sendTime", DateUtil.parseLongTime2Str(storeOrder.getSendTime()));//发货时间
			data.put("finishTime", DateUtil.parseLongTime2Str(storeOrder.getConfirmSignedTime()));//完成时间   买家确认收货提交成功时间
			data.put("closeTime", DateUtil.parseLongTime2Str(storeOrder.buildOrderCloseTime(storeOrder)));//订单关闭时间
			data.put("haveRefund", getHaveRefund(storeOrder.getOrderNo()));//是否有售后：0没有售后、1有售后
			StoreBusiness store = storeMapper.selectById(storeId);
			String businessName = store.getBusinessName();//商家名称
			data.put("storeId", storeId);//门店ID
			data.put("businessName", businessName);//客户名称、商家名称
			
			//物流信息 TODO V3.2 物流信息待确认    ？？？？？？？？？？？？？？？
//			Map<String, Object> firstExpressInfo = expressService.getFirstExpressInfo(orderNo);
//			data.put("expressInfo", firstExpressInfo);
			//2、收货人信息
			String expressInfo = storeOrder.getExpressInfo();
			if(StringUtils.isEmpty(expressInfo)){
				throw new RuntimeException("收件人信息为空");
			}
			String[] expressInfoArr = expressInfo.split(",");
			if(expressInfoArr.length != 3){
				throw new RuntimeException("收件人信息错误，请排查问题。expressInfo："+expressInfo);
			}
//			Map<String,Object> expressInfoMap = new HashMap<String,Object>();
			data.put("receiverName", expressInfoArr[0]);//收货人姓名
			data.put("receiverPhone", expressInfoArr[1]);//收货人手机号
			data.put("receiverAddress", expressInfoArr[2]);//收货人地址
			
			//3、发货信息
			StoreExpressInfo entity = new StoreExpressInfo();
			entity.setOrderNo(orderNo);
			entity.setStatus(0);
			StoreExpressInfo storeExpressInfo = storeExpressInfoMapper.selectOne(entity);
			if(storeExpressInfo!=null){
				String EngName = storeExpressInfo.getExpressSupplier();
				Wrapper<ExpressSupplier> wrapper = new EntityWrapper<ExpressSupplier>();
				wrapper.eq("EngName", EngName);
				List<ExpressSupplier> list = supplierExpressMapper.selectList(wrapper);
				if(list == null || list.size() == 0){
					data.put("expressNo", storeExpressInfo.getExpressOrderNo());//物流单号
					data.put("expressCompamyName", storeExpressInfo.getExpressSupplier());//物流公司
					data.put("expressCnName", "");//物流公司
				}else{
					data.put("expressNo", storeExpressInfo.getExpressOrderNo());//物流单号
					data.put("expressCompamyName", storeExpressInfo.getExpressSupplier());//物流公司、快递提供商
					data.put("expressCnName", list.get(0).getCnName());//物流公司、快递供应商中文名
				}
			}else{
				data.put("expressNo", "");//物流单号
				data.put("expressCompamyName", "");//物流公司
				data.put("expressCnName", "");//物流公司
			}
			data.put("remark", storeOrder.getRemark());//发货说明
			data.put("orderSupplierRemark", storeOrder.getOrderSupplierRemark());//订单供应商备注
			
			//4、订单商品信息
			List<Map<String,Object>> storeOrderItemList = supplierOrderService.getSupplierOrderItemByOrderNo(orderNo);
			data.put("storeOrderItemList", storeOrderItemList);
			
			//5、获取邮寄公司名称列表,用于回显数据
			List<Map<String,Object>> allExpressCompanys = supplierExpressService.getAllExpressCompanyNames();
			data.put("allExpressCompanys", allExpressCompanys);
			
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取供应商订单详情:"+e.getMessage());
			throw new RuntimeException("获取供应商订单详情:"+e.getMessage());
		}
    }
    
	/**
     * 是否有售后：0没有售后、1有售后
     * @param orderNo
     * @return
     */
    private int getHaveRefund(long orderNo) {
    	Wrapper<RefundOrder> wrapper = new EntityWrapper<RefundOrder>().eq("order_no", orderNo);
		List<RefundOrder> list = refundOrderMapper.selectList(wrapper);
		  if(list.size() > 0){
			  return 1;
		  }else{
			  return 0;
		  }
	}

   

	/**
     * 获取供应商订单详情
     * @return
     */
    @RequestMapping("/getSupplierOrderInfo")
    @ResponseBody
    public JsonResponse getSupplierOrderInfo(@RequestParam(value="orderNo") long orderNo) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			Map<String,Object> data = new HashMap<String,Object>();
			//获取订单详情
			Map<String,Object> storeOrder = supplierOrderService.getSupplierOrderByOrderNo(orderNo);
			data.put("storeOrder", storeOrder);
			//获取订单item详情
			List<Map<String,Object>> storeOrderItemList = supplierOrderService.getSupplierOrderItemByOrderNo(orderNo);
			data.put("storeOrderItemList", storeOrderItemList);
			//获取买家信息
			StoreBusiness storeBusiness = storeBusinessNewService.getById((Long)storeOrder.get("storeId"));
			Map<String,Object> storeBusinessMap = new HashMap<String,Object>();
			storeBusinessMap.put("legalPerson", storeBusiness.getLegalPerson());
			storeBusinessMap.put("phoneNumber", storeBusiness.getPhoneNumber());
			storeBusinessMap.put("address", storeBusiness.getProvince()+storeBusiness.getCity()+storeBusiness.getCounty()+storeBusiness.getBusinessAddress());
			data.put("storeBusiness", storeBusinessMap);
			//收件人信息
			String expressInfo = (String) storeOrder.get("expressInfo");
			String[] info = expressInfo.split(",");
			Map<String,Object> expressInfoMap = new HashMap<String,Object>();
			expressInfoMap.put("name", info[0]);
			expressInfoMap.put("phone", info[1]);
			expressInfoMap.put("address", info[2]);
			data.put("consignee", expressInfoMap);
			//获取邮寄公司名称列表
			List<Map<String,Object>> allExpressCompanys = supplierExpressService.getAllExpressCompanyNames();
			data.put("allExpressCompanys", allExpressCompanys);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取供应商订单详情:"+e.getMessage());
			throw new RuntimeException("获取供应商订单详情:"+e.getMessage());
		}
    }
    
    /**
     * 
	 * 根据int状态值获取供应商订单列表对应的中文状态值
	 * @param orderStatus 订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭
	 * @param orderCloseType  订单关闭类型：0(无需关闭)、101买家主动取消订单、102超时未付款系统自动关闭订单、103全部退款关闭订单、104卖家关闭订单、105、平台客服关闭订单
	 * @return
	 */
	private String getOrderStatusName(int orderStatus,int orderCloseType){
		String orderStatusName = "";
		switch (orderStatus) {
		case 0:
			orderStatusName = "待付款（未付款，请勿发货）";
			break;
		case 10:
			orderStatusName = "待发货";
			break;
		case 50:
			orderStatusName = "已发货";
			break;
		case 70:
			orderStatusName = "交易完成";
			break;
		case 100:
//			待实现根据三种情况分别显示详细关闭交易原因，关闭的3种情况是：1、买家取消待付款的订单，2、平台取消已付款的订单，3、通过申请售后退货退款全部商品的订单 4、买家未付款超时
			orderStatusName = "交易关闭";//交易关闭（买家未付款超时/买家取消未付款/平台取消已付款/售后退单）
			if(orderCloseType == 101){//101买家主动取消订单、
				orderStatusName = orderStatusName + "（买家取消未付款）";
			}else if(orderCloseType == 102){//102超时未付款系统自动关闭订单、
				orderStatusName = orderStatusName + "（买家未付款超时）";
			}else if(orderCloseType == 103){//103全部退款关闭订单、
				orderStatusName = orderStatusName + "（售后退单）";
			}else if(orderCloseType == 104){//104卖家关闭订单
				orderStatusName = orderStatusName + "";
			}else if(orderCloseType == 105){//105、平台客服关闭订单
				orderStatusName = orderStatusName + "（平台取消已付款）";
			}
			break;
		}
		return orderStatusName;
	}
    
	
	/**
     * 修改订单供应商备注
     * @param orderNo
     * @param expressInfo
     * @param expressNo
     * @param remark
     * @return
     */
    @RequestMapping("/updateOrderSupplierRemark")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse updateOrderSupplierRemark(@RequestParam(value="orderNo") long orderNo,
    		@RequestParam(value="orderSupplierRemark",required = false,defaultValue = "") String orderSupplierRemark) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   		StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(orderNo);
		//判定该订单是否能够被该供应商修改
		if(storeOrderNew.getSupplierId() != userId){
			logger.info("该供应商没有权限操作该订单！orderNo="+orderNo+"supplierId="+userId);
			return jsonResponse.setError("没有权限操作该订单！");
		}
		try {
			supplierOrderService.updateOrderSupplierRemark(orderNo,orderSupplierRemark);
			return jsonResponse.setSuccessful();
	    } catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
    /**
     * 供应商订单发货
     * @param orderNo
     * @param expressNo
     * @param remark
     * @param expressCompanyChineseName 快递的汉语
     * @return
     */
    @RequestMapping("/deliverGoods")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse deliverGoods(@RequestParam(value="orderNo") long orderNo,
    		@RequestParam(value="expressInfo") String expressCompamyName,
    		@RequestParam(value="expressCompanyChineseName", required = false) String expressCompanyChineseName,
    		@RequestParam(value="expressNo") String expressNo,
    		@RequestParam(value="remark",required = false,defaultValue = "") String remark) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(orderNo);
		//判定该订单是否能够被该供应商修改
		if(storeOrderNew.getSupplierId() != userId){
			logger.info("该供应商没有权限操作该订单！orderNo="+orderNo+"supplierId="+userId);
			return jsonResponse.setError("没有权限操作该订单！");
		}
		try {
			supplierOrderService.deliverGoods(orderNo,expressCompamyName,expressNo,remark, expressCompanyChineseName);
			return jsonResponse.setSuccessful();
	    } catch (Exception e) {
			e.printStackTrace();
			logger.info("供应商订单发货出现异常，请尽快排查问题:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
//    /**
//	 * 修复storeOrder表收件人姓名收件人号码收件人地址数据
//	 */
//    @RequestMapping("/a")
//    @ResponseBody
//    public JsonResponse a() {
//		JsonResponse jsonResponse = new JsonResponse();
//		supplierOrderService.a();
//		return jsonResponse.setSuccessful();
//    }
    
    /**
     * 供应商订单退单
     * @param orderNo
     * @param cancelReason
     * @return
     */
    @RequestMapping("/chargeback")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse chargeback(@RequestParam(value="orderNo") long orderNo,
    		@RequestParam(value="cancelReason") String cancelReason) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(orderNo);
		//判定该订单是否能够被该供应商修改
		if(storeOrderNew.getSupplierId() != userId){
			logger.info("该供应商没有权限操作该订单！orderNo="+orderNo+"supplierId="+userId);
			return jsonResponse.setError("没有权限操作该订单！");
		}
		try {
			supplierOrderService.chargeback(orderNo,cancelReason);
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("供应商订单退单:"+e.getMessage());
			throw new RuntimeException("供应商订单退单:"+e.getMessage());
		}
    }
    
    /**
     * 导出供应商订单列表EXCEL
     * @param response
     * @param startTime
     * @param endTime
     * @param type 0:自建仓 1:其他仓
     * @throws IOException
     * @throws ParseException 
     */
    @RequestMapping("/delivery/excel")
    @AdminOperationLog
    @ResponseBody
	public JsonResponse outExcel(HttpServletResponse response,
			@RequestParam(value="orderNo", required=false, defaultValue = "0") long orderNo,
    		@RequestParam(value="orderStatus", required=false, defaultValue = "-1") int orderStatus,
    		@RequestParam(value="addresseeName", required=false, defaultValue = "") String addresseeName,
    		@RequestParam(value="addresseeTelePhone", required=false, defaultValue = "") String addresseeTelePhone,
			@RequestParam(value="clothesNumbers", required=false, defaultValue = "") String clothesNumbers,
			@RequestParam(value="updateTimeBegin", required=false, defaultValue = "") String updateTimeBegin,
			@RequestParam(value="updateTimeEnd", required=false, defaultValue = "") String updateTimeEnd,
			@RequestParam(value="remark", required=false, defaultValue = "") String remark,
			@RequestParam(value="customerName", required=false, defaultValue = "") String customerName,
			@RequestParam(value="customerPhone", required=false, defaultValue = "") String customerPhone,
			@RequestParam(value="expressNo", required=false, defaultValue = "") String expressNo,
			@RequestParam(value="createTimeBegin", required=false, defaultValue = "") String createTimeBegin,
			@RequestParam(value="createTimeEnd", required=false, defaultValue = "") String createTimeEnd,
			@RequestParam(value="refundUnderway", required=false, defaultValue = "-1") int refundUnderway) throws IOException, ParseException {
    	JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	try {
	   	 	 List<Map<String,Object>> selectList = supplierOrderService.outExcel(userId,orderNo,orderStatus,addresseeName,addresseeTelePhone,clothesNumbers,
					updateTimeBegin,updateTimeEnd,remark,customerName,customerPhone,expressNo,createTimeBegin,createTimeEnd, refundUnderway);

			//列名
			String[] columnNames = {"订单编号", "用户ID", "产品ID", "产品名称", "颜色", "尺码",  "销售数量", "发货留言", "订单状态", "拍下时间", "付款时间", "收货人姓名", "收货人电话", "收货人地址",
					"物流单号","物流公司","单款总价","平台优惠金额","商家优惠金额","订单运费"};
			//map中的key
			String[] keys = {"orderNo","storeId", "productId","productName","colorName","sizeName","saleCount","remark",
					"orderStatus","createTime", "payTime","expressName","expressPhone","expressAddress","expressNo","expressCompanyName",
					"totalMoney","patformTotalPreferential","supplierTotalPreferential","totalExpressMoney"
			};

		     ExcelUtil.exportExcel(response, selectList, keys, columnNames, "自建仓");
		     
		     return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出EXCEL:"+e.getMessage());
			throw new RuntimeException("导出EXCEL:"+e.getMessage());
		}
	}
    
    /**
     * 导出供应商待发货订单列表EXCEL
     * @param response
     * @param startTime
     * @param endTime
     * @param type 0:自建仓 1:其他仓
     * @throws IOException
     * @throws ParseException 
     */
    @RequestMapping("/delivery/outPendingDeliveryOrderExcel")
    @AdminOperationLog
    @ResponseBody
	public JsonResponse outPendingDeliveryOrderExcel(HttpServletResponse response,
			@RequestParam(value="orderNo", required=false, defaultValue = "0") long orderNo,
    		@RequestParam(value="addresseeName", required=false, defaultValue = "") String addresseeName,
    		@RequestParam(value="addresseeTelePhone", required=false, defaultValue = "") String addresseeTelePhone,
			@RequestParam(value="clothesNumbers", required=false, defaultValue = "") String clothesNumbers,
			@RequestParam(value="payTimeBegin", required=false, defaultValue = "") String payTimeBegin,
			@RequestParam(value="payTimeEnd", required=false, defaultValue = "") String payTimeEnd,
			@RequestParam(value="remark", required=false, defaultValue = "") String remark,
			@RequestParam(value="customerName", required=false, defaultValue = "") String customerName,
			@RequestParam(value="customerPhone", required=false, defaultValue = "") String customerPhone,
			@RequestParam(value="expressNo", required=false, defaultValue = "") String expressNo) throws IOException, ParseException {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			//获取供应商待发货订单列表
			List<Map<String,Object>> selectList = supplierOrderService.outPendingDeliveryOrderExcel(userId,orderNo,addresseeName,addresseeTelePhone,clothesNumbers,
					payTimeBegin,payTimeEnd,remark,customerName,customerPhone,expressNo);
			//列名
			String[] columnNames = {"订单编号", "用户ID", "产品ID", "产品名称", "颜色", "尺码",  "销售数量", "发货留言", "订单状态", "拍下时间", "付款时间", "收货人姓名", "收货人电话", "收货人地址",
					"物流单号","物流公司","单款总价","平台优惠金额","商家优惠金额","订单运费"};
			//map中的key
			String[] keys = {"orderNo","storeId", "productId","productName","colorName","sizeName","saleCount","remark",
	        		"orderStatus","createTime", "payTime","expressName","expressPhone","expressAddress","expressNo","expressCompanyName",
					"totalMoney","patformTotalPreferential","supplierTotalPreferential","totalExpressMoney"
	        };
	        
	        ExcelUtil.exportExcel(response, selectList, keys, columnNames, "自建仓");
	        
	        return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出EXCEL:"+e.getMessage());
			throw new RuntimeException("导出EXCEL:"+e.getMessage());
		}
	}
    
    /**
     * 获取供应商订单数量
     * @return
     */
    @RequestMapping("/getSupplierOrderCount")
    @ResponseBody
    public JsonResponse getSupplierOrderCount() {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			Map<String,Object> data = new HashMap<String,Object>();
			//获取订单总数，今日新增个数，待处理个数
			Map<String,Object> supplierOrderCount = supplierOrderService.getSupplierOrderCount(userId);
			data.put("supplierOrderCount", supplierOrderCount);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取供应商订单数量:"+e.getMessage());
			throw new RuntimeException("获取供应商订单数量:"+e.getMessage());
		}
    }
}
