package com.jiuy.store.api.tool.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyuan.util.BizUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.mapper.supplier.OrderItemNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.StoreCouponUseLogNewMapper;
import com.jiuyuan.dao.mapper.supplier.StoreExpressInfoMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierExpressMapper;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.ExpressSupplier;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.StoreCouponUseLogNew;
import com.jiuyuan.entity.newentity.StoreExpressInfo;
import com.jiuyuan.entity.newentity.StoreOrderItemNew;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.IOrderNewService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
* 供应商的列表Controller
* @author Qiuyuefan
*/
@Login
@Controller
@RequestMapping("/supplier/order")
public class SupplierOrderController {

	private static final Log logger = LogFactory.get();
	
	@Autowired
	private IOrderNewService orderNewService;
	
	@Autowired
	private OrderItemNewMapper orderItemNewMapper;
	
	@Autowired
	private ProductNewMapper productNewMapper;
	
	@Autowired
	private ProductSkuNewMapper productSkuNewMapper;
	
	@Autowired
	private StoreExpressInfoMapper storeExpressInfoMapper;
	
	@Autowired
	private SupplierExpressMapper supplierExpressMapper;
	
	@Autowired
	private StoreCouponUseLogNewMapper storeCouponUseLogNewMapper;
	
    /**
     * 获取到供应商订单列表
     * @param orderStatus
     * @param pageQuery
     * @param userDetail
     * @return
     */
    @RequestMapping("/list/auth")
    @ResponseBody
    public JsonResponse newOrderList(@RequestParam(value = "status", required = false,defaultValue="5") int status,
    		PageQuery pageQuery, UserDetail<StoreBusiness> userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		Page<StoreOrderNew> page = new Page<StoreOrderNew>(pageQuery.getPage(),pageQuery.getPageSize());
    		List<StoreOrderNew> supplierOrderList = orderNewService.getSupplierOrderListByOrderStatus(status, page,
					userDetail);
    		Map<String,Object> result = new HashMap<String,Object>();
    		result = this.encapsulateSupplierOrderListData(supplierOrderList);
    		result.put("pageQuery", pageQuery);
    		result.put("isMore", page.hasNext());
        	return jsonResponse.setSuccessful().setData(result);
		} catch (Exception e) {
			logger.error("获取到订单列表:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 封装供应商订单列表数据
     * @param supplierOrderList
     * @return
     */
    private Map<String, Object> encapsulateSupplierOrderListData(List<StoreOrderNew> supplierOrderList) {
    	Map<String,Object> result = new HashMap<String,Object>();
    	List<Map<String,Object>> storeOrderList = new ArrayList<Map<String,Object>>();
		for (StoreOrderNew storeOrderNew : supplierOrderList) {
			Map<String,Object> storeOrder = this.encapsulateSupplierOrderInfo(storeOrderNew);
			storeOrderList.add(storeOrder);
		}
		result.put("storeOrderList", storeOrderList);
		return result;
	}
    
    /**
     * 封装供应商订单数据
     * @param storeOrderNew
     * @return
     */
    private Map<String, Object> encapsulateSupplierOrderInfo(StoreOrderNew storeOrderNew) {
    	Map<String,Object> storeOrder = new HashMap<String,Object>();
		storeOrder.put("orderNo", storeOrderNew.getOrderNo());//订单号
		storeOrder.put("createTime", DateUtil.parseLongTime2Str(storeOrderNew.getCreateTime()));//下单时间
		storeOrder.put("orderStatusStr", this.getOrderStatus(storeOrderNew.getOrderStatus()));//订单状态
		storeOrder.put("orderStatus", storeOrderNew.getOrderStatus());//订单状态
		
		//封装对应的item信息
		List<Map<String,Object>> storeOrderItemList = new ArrayList<Map<String,Object>>();
		Wrapper<StoreOrderItemNew> storeOrderItemNewWrapper = 
				new EntityWrapper<StoreOrderItemNew>().eq("OrderNo", storeOrderNew.getOrderNo());
		List<StoreOrderItemNew> storeOrderItemNewList = orderItemNewMapper.selectList(storeOrderItemNewWrapper);
		int storeOrderItemCount = 0;
		for (StoreOrderItemNew storeOrderItemNew : storeOrderItemNewList) {
			Map<String,Object> storeOrderItem = new HashMap<String,Object>();
			ProductNew productNew = productNewMapper.selectById(storeOrderItemNew.getProductId());
			ProductSkuNew productSkuNew = productSkuNewMapper.selectById(storeOrderItemNew.getSkuId());
			storeOrderItem.put("mainImg", productNew.getMainImg());//商品主图
			storeOrderItem.put("name", productNew.getName());//商品名称
			storeOrderItem.put("price", storeOrderItemNew.getMoney());//商品价格
			storeOrderItem.put("skuCount", storeOrderItemNew.getBuyCount());//商品购买件数
			storeOrderItem.put("clothesNumber", productNew.getClothesNumber());//商品款号
			storeOrderItem.put("color", productSkuNew.getColorName());//商品颜色
			storeOrderItem.put("size", productSkuNew.getSizeName());//商品尺码
			storeOrderItemList.add(storeOrderItem);
			storeOrderItemCount += storeOrderItemNew.getBuyCount();
		}
		storeOrder.put("storeOrderItemList", storeOrderItemList);//订单item
		
		storeOrder.put("storeOrderItemCount", storeOrderItemCount);//商品总的件数
		storeOrder.put("totalPay", storeOrderNew.getTotalPay()-storeOrderNew.getTotalRefundCost()+storeOrderNew.getTotalExpressMoney());//实付金额
		storeOrder.put("totalExpressMoney", storeOrderNew.getTotalExpressMoney());//邮费
		
		//获取快递信息
		if(storeOrderNew.getOrderStatus()==OrderStatus.UNPAID.getIntValue() || storeOrderNew.getOrderStatus()==OrderStatus.CLOSED.getIntValue()){
			storeOrder.put("expressName", "");//快递姓名
			storeOrder.put("expressPhone", "");//快递号码
			storeOrder.put("expressAddress", "");//快递地址
		}else{
			storeOrder.put("expressName", storeOrderNew.getExpressName());//快递姓名
			storeOrder.put("expressPhone", storeOrderNew.getExpressPhone());//快递号码
			storeOrder.put("expressAddress", storeOrderNew.getExpressAddress());//快递地址
		}
		//获取邮寄公司名称和快递号
		if(storeOrderNew.getOrderStatus()==OrderStatus.DELIVER.getIntValue() || storeOrderNew.getOrderStatus()==OrderStatus.SUCCESS.getIntValue()){
			Wrapper<StoreExpressInfo> storeExpressInfoWrapper = 
					new EntityWrapper<StoreExpressInfo>().eq("OrderNo", storeOrderNew.getOrderNo()).eq("Status", 0);
			List<StoreExpressInfo> storeExpressInfoList = storeExpressInfoMapper.selectList(storeExpressInfoWrapper);
			if(storeExpressInfoList.size()>0){
				StoreExpressInfo storeExpressInfo = storeExpressInfoList.get(0);
				Wrapper<ExpressSupplier> wrapper = 
						new EntityWrapper<ExpressSupplier>().eq("status", 0).eq("EngName", storeExpressInfo.getExpressSupplier());
				List<ExpressSupplier> expressSupplierList = supplierExpressMapper.selectList(wrapper);
				storeOrder.put("expressNo", storeExpressInfo.getExpressOrderNo());//快递单号
				if(expressSupplierList.size()>0){
					ExpressSupplier expressSupplier = expressSupplierList.get(0);
					if(expressSupplier!=null){
						storeOrder.put("expressCompanyName", expressSupplier.getCnName());//快递公司名称
					}else{
						storeOrder.put("expressCompanyName", "");//快递公司名称
					}
				}else{
					storeOrder.put("expressCompanyName", "");//快递公司名称
				}
			}else{
				storeOrder.put("expressNo", "");//快递单号
				storeOrder.put("expressCompanyName", "");//快递公司名称
			}
		}else{
			storeOrder.put("expressNo", "");//快递单号
			storeOrder.put("expressCompanyName", "");//快递公司名称
		}
		return storeOrder;
	}

	/**
	 * 根据int状态值获取对应的中文状态值
	 * @param status
	 * @return
	 */
	public String getOrderStatus(int status){
		String orderStatus = "";
		switch (status) {
		case 0:
			orderStatus = "待付款";
			break;
		case 10:
			orderStatus = "待发货";
			break;
		case 50:
			orderStatus = "已发货";
			break;
		case 70:
			orderStatus = "已完成";
			break;
		case 100:
			orderStatus = "已关闭";
			break;
		}
		return orderStatus;
	}

	/**
     * 获取到供应商订单详情
     * @param orderNo
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/queryOrderDetail/auth")
    @ResponseBody
	public JsonResponse queryOrderDetail(@RequestParam("order_no") long orderNo,
			UserDetail<StoreBusiness> userDetail) {
//    	DefaultStoreUserDetail defaultStoreUserDetail = new DefaultStoreUserDetail();
//		StoreBusiness storeBusiness1 = new StoreBusiness();
//		storeBusiness1.setId(3187L);
//		defaultStoreUserDetail.setStoreBusiness(storeBusiness1);
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		StoreOrderNew storeOrderNew = orderNewService.getSupplierOrderDetail(userDetail ,orderNo);
    		Map<String,Object> result = new HashMap<String,Object>();
    		result = this.encapsulateSupplierOrderData(storeOrderNew);
	    	return jsonResponse.setSuccessful().setData(result);
    	} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }

    /**
     * 封装供应商订单详情数据
     * @param storeOrderNew
     * @return
     */
	private Map<String, Object> encapsulateSupplierOrderData(StoreOrderNew storeOrderNew) {
		Map<String, Object> result = new HashMap<String,Object>();
		Map<String,Object> storeOrder = new HashMap<String,Object>();
		Long orderNo = storeOrderNew.getOrderNo();
		Integer orderStatus = storeOrderNew.getOrderStatus();
		storeOrder.put("orderNo", orderNo);//订单编号
		storeOrder.put("orderStatusStr", this.getOrderStatus(orderStatus));//订单状态
		storeOrder.put("orderStatus", storeOrderNew.getOrderStatus());//订单状态
		
		//封装对应的item信息
		List<Map<String,Object>> storeOrderItemList = new ArrayList<Map<String,Object>>();
		Wrapper<StoreOrderItemNew> storeOrderItemNewWrapper = 
				new EntityWrapper<StoreOrderItemNew>().eq("OrderNo", orderNo);
		List<StoreOrderItemNew> storeOrderItemNewList = orderItemNewMapper.selectList(storeOrderItemNewWrapper);
		int storeOrderItemCount = 0;
		for (StoreOrderItemNew storeOrderItemNew : storeOrderItemNewList) {
			Map<String,Object> storeOrderItem = new HashMap<String,Object>();
			ProductNew productNew = productNewMapper.selectById(storeOrderItemNew.getProductId());
			ProductSkuNew productSkuNew = productSkuNewMapper.selectById(storeOrderItemNew.getSkuId());
			storeOrderItem.put("productId", productNew.getId());//商品ID
			storeOrderItem.put("mainImg", productNew.getMainImg());//商品主图
			storeOrderItem.put("name", productNew.getName());//商品名称
			storeOrderItem.put("price", storeOrderItemNew.getMoney());//商品价格
			storeOrderItem.put("skuCount", storeOrderItemNew.getBuyCount());//商品件数
			storeOrderItem.put("clothesNumber", productNew.getClothesNumber());//商品款号
			storeOrderItem.put("color", productSkuNew.getColorName());//商品颜色
			storeOrderItem.put("size", productSkuNew.getSizeName());//商品尺码
			storeOrderItemList.add(storeOrderItem);
			storeOrderItemCount += storeOrderItemNew.getBuyCount();
		}
		storeOrder.put("storeOrderItemList", storeOrderItemList);//订单item
		
		storeOrder.put("storeOrderItemCount", storeOrderItemCount);//商品总的件数
		storeOrder.put("totalMoney", storeOrderNew.getTotalMoney());//商品总价
		//获取优惠券信息
		Wrapper<StoreCouponUseLogNew> storeCouponUseLogNewWrapper = 
				new EntityWrapper<StoreCouponUseLogNew>().eq("OrderNo", orderNo).eq("Status", 0);
		List<StoreCouponUseLogNew> storeCouponUseLogNewList = storeCouponUseLogNewMapper.selectList(storeCouponUseLogNewWrapper);
		double actualDiscountAll = 0.0;
		for (StoreCouponUseLogNew storeCouponUseLogNew : storeCouponUseLogNewList) {
			actualDiscountAll += storeCouponUseLogNew.getActualDiscount();
		}
		actualDiscountAll = BizUtil.savepoint(actualDiscountAll,2);
		storeOrder.put("actualDiscountAll", actualDiscountAll);//商品优惠金额
		storeOrder.put("totalRefundCost", storeOrderNew.getTotalRefundCost());//商品退款金额
		storeOrder.put("totalExpressMoney", storeOrderNew.getTotalExpressMoney());//商品邮费
		storeOrder.put("totalPay", storeOrderNew.getTotalPay()-storeOrderNew.getTotalRefundCost()+storeOrderNew.getTotalExpressMoney());//商品实付金额
		
		//获取邮寄公司名称和快递号
		if(orderStatus==OrderStatus.UNPAID.getIntValue() || orderStatus==OrderStatus.CLOSED.getIntValue()){
			storeOrder.put("expressName", "");//快递姓名
			storeOrder.put("expressPhone", "");//快递号码
			storeOrder.put("expressAddress", "");//快递地址
		}else{
			storeOrder.put("expressName", storeOrderNew.getExpressName());//快递姓名
			storeOrder.put("expressPhone", storeOrderNew.getExpressPhone());//快递号码
			storeOrder.put("expressAddress", storeOrderNew.getExpressAddress());//快递地址
		}
		if(orderStatus==OrderStatus.DELIVER.getIntValue() || orderStatus==OrderStatus.SUCCESS.getIntValue()){
			Wrapper<StoreExpressInfo> storeExpressInfoWrapper = 
					new EntityWrapper<StoreExpressInfo>().eq("OrderNo", orderNo).eq("Status", 0);
			List<StoreExpressInfo> storeExpressInfoList = storeExpressInfoMapper.selectList(storeExpressInfoWrapper);
			if(storeExpressInfoList.size()>0){
				StoreExpressInfo storeExpressInfo = storeExpressInfoList.get(0);
				Wrapper<ExpressSupplier> wrapper = 
						new EntityWrapper<ExpressSupplier>().eq("status", 0).eq("EngName", storeExpressInfo.getExpressSupplier());
				List<ExpressSupplier> expressSupplierList = supplierExpressMapper.selectList(wrapper);
				storeOrder.put("expressNo", storeExpressInfo.getExpressOrderNo());//快递单号
				storeOrder.put("expressCompanyName", expressSupplierList.get(0).getCnName());//快递公司名称
			}else{
				storeOrder.put("expressNo", "");//快递单号
				storeOrder.put("expressCompanyName", "");//快递公司名称
			}
		}else{
			storeOrder.put("expressNo", "");//快递单号
			storeOrder.put("expressCompanyName", "");//快递公司名称
		}
		result.put("storeOrder", storeOrder);
		
		//封装订单
		List<Map<String,Object>> dateList = new ArrayList<Map<String,Object>>();
		if(orderStatus>-1){
			Map<String,Object> date = new HashMap<String,Object>();
			date.put("timeName", "创建时间");
			date.put("time", DateUtil.parseLongTime2Str(storeOrderNew.getCreateTime()));//下单时间
			dateList.add(date);
		}
		if(orderStatus>OrderStatus.UNPAID.getIntValue()){
			Map<String,Object> date = new HashMap<String,Object>();
			date.put("timeName", "付款时间");
			date.put("time", DateUtil.parseLongTime2Str(storeOrderNew.getPayTime()));//支付时间
			dateList.add(date);
		}
		if(orderStatus>OrderStatus.PAID.getIntValue()){
			Map<String,Object> date = new HashMap<String,Object>();
			date.put("timeName", "发货时间");
			date.put("time", DateUtil.parseLongTime2Str(storeOrderNew.getSendTime()));//发货时间
			dateList.add(date);
		}
		if(orderStatus==OrderStatus.SUCCESS.getIntValue()){
			Map<String,Object> date = new HashMap<String,Object>();
			date.put("timeName", "完成时间");
			date.put("time", DateUtil.parseLongTime2Str(storeOrderNew.getConfirmSignedTime()));//完成时间
			dateList.add(date);
		}
		if(orderStatus==OrderStatus.CLOSED.getIntValue()){
			Map<String,Object> date = new HashMap<String,Object>();
			date.put("timeName", "关闭时间");
			date.put("time", DateUtil.parseLongTime2Str(storeOrderNew.getOrderCloseTime()));//关闭时间
			dateList.add(date);
		}
		result.put("dateList", dateList);//时间列表
		return result;
	}
    
}