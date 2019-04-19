package com.jiuyuan.service.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.RefundStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.dao.mapper.supplier.OrderItemNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.RefundOrderActionLogMapper;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.ShopStoreAuthReasonMapper;
import com.jiuyuan.dao.mapper.supplier.StoreMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierDeliveryAddressMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.RefundOrderActionLogEnum;
import com.jiuyuan.entity.newentity.ShopStoreAuthReason;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.StoreOrderItemNew;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.SupplierDeliveryAddress;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.SmallPage;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 
 */
@Service
public class RefundOrderSupplierFacade implements IRefundOrderSupplierFacade{

	private static final Log logger = LogFactory.get(RefundOrderSupplierFacade.class);
	
	private static final double TEN_THOUSAND = 10000.00;
	private static final double ONE = 1.00;
	
	@Autowired
	private RefundOrderMapper refundOrderMapper;

	@Autowired
	private StoreOrderItemNewService storeOrderItemService;
	
	@Autowired
	private OrderItemNewMapper orderItemNewMapper;
	
	@Autowired
	private ProductNewMapper productNewMapper;
	
	@Autowired
	private ProductSkuNewMapper productSkuNewMapper;
	
	@Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private RefundService refundService;
	
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;
	
	@Autowired
	private RefundOrderActionLogMapper refundOrderActionLogMapper;
	
	@Autowired
	private RefundOrderActionLogService refundOrderActionLogService;
	
	@Autowired
	private RefundOrderService refundOrderService;
	
	@Autowired
	private RefundSMSNotificationService refundSMSNotificationService;
	
	@Autowired
	private StoreOrderItemNewService storeOrderItemNewService;
	
	@Autowired
	private ShopStoreAuthReasonMapper shopStoreAuthReasonMapper;
	
	@Autowired
	private IRefundOrderFacade refundOrderFacade;
	
	@Autowired
	private IRefundOrderFacadeNJ refundOrderFacadeNJ;

	@Autowired
	private IMyAccountSupplierService myAccountSupplierService;
	
	@Autowired
	private UserNewMapper userNewMapper;
	
	@Autowired
	private SupplierDeliveryAddressMapper supplierDeliveryAddressMapper;
	
	@Autowired
	private ISupplierDeliveryAddress supplierDeliveryAddressService;
	
	/**
	 * 供应商申请平台介入
	 *  7、	更改售后订单平台介入状态为“卖家申请介入中”
		8、	在售后订单表中记录卖家申请平台介入时间
		9、	添加售后订单操作日志

	 */
	public void supplierApplyPlatformIntervene(long refundOrderId){
		//0、  校验售后单状态
		RefundOrder refundOrderOld = refundOrderMapper.selectById(refundOrderId);
		int platformInterveneState = refundOrderOld.getPlatformInterveneState();
		if(platformInterveneState != RefundOrder.PLATFORM_NOT_INTERVENE){//未介入
			throw new RuntimeException("已经申请平台介入，无需重复申请。");
		}
		
		//1、更改售后订单平台介入状态为买家平台介入中
		refundOrderService.supplierApplyPlatformIntervene(refundOrderId);
				
		//2、添加操作日志
		refundOrderActionLogService.addActionLog(RefundOrderActionLogEnum.L,refundOrderId);
		
		//3、结束卖家自动确认收货时间
		startSupplierAutoTakeDeliveryPauseTime(refundOrderId);
	}
	
	
	/**
	 * 结束卖家自动确认收货时间
	 * @param refundOrderId
	 */
	private void startSupplierAutoTakeDeliveryPauseTime(long refundOrderId) {
		RefundOrder refundOrderOld = refundOrderMapper.selectById(refundOrderId);
		long supplierAutoTakeDeliveryPauseTime = refundOrderOld.getSupplierAutoTakeDeliveryPauseTime();//卖家自动确认收货暂停时间，为0是则表示未暂停，大于0表示已暂停
		if(supplierAutoTakeDeliveryPauseTime > 0){
			logger.info("结束卖家自动确认收货时间失败，请尽快排查问题！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！,refundOrderId:"+
					refundOrderId+",supplierAutoTakeDeliveryPauseTime:"+supplierAutoTakeDeliveryPauseTime+",refundOrderOld:"+JSON.toJSONString(refundOrderOld));
		}else{
			RefundOrder refundOrder = new RefundOrder();
			refundOrder.setId(refundOrderId);
			long time = System.currentTimeMillis();
			refundOrder.setSupplierAutoTakeDeliveryPauseTime(time);
			refundOrderMapper.updateById(refundOrder);
			logger.info("结束卖家自动确认收货时间成功,refundOrderId:"+refundOrderId+",supplierAutoTakeDeliveryPauseTime:"+supplierAutoTakeDeliveryPauseTime+",refundOrderOld:"+JSON.toJSONString(refundOrderOld));
		}
	}



	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IRefundOrderSupplierFacade#applyRefund(java.lang.Long, java.lang.Long, java.lang.Integer)
	 */
	@Override
	public Map<String, Object> applyRefund(Long itemId, Long storeId, Integer count) {
		Map<String,Object> map = new HashMap<String,Object>();
		//获取订单item详情
		//Map<String,Object> itemMap = storeOrderItemService.getItemInfo(itemId,storeId);
		//获取退款原因列表
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IRefundOrderSupplierFacade#getRefundOrderInfo(long)
	 */
	@Override
	public Map<String,Object> getRefundOrderInfo(long refundOrderId) {
		RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);
		if(refundOrder == null){
			throw new RuntimeException("未找到该售后单");
		}
		Map<String,Object> refundOrderData = new HashMap<String,Object>();
		
		//填充售后订单数据
		int refundStatus = refundOrder.getRefundStatus();
		refundOrderData.put("refundOrderId", refundOrder.getId());//售后单ID
		refundOrderData.put("refundOrderNo", refundOrder.getRefundOrderNo());//售后单编号
		refundOrderData.put("brandName", refundOrder.getBrandName());//订单品牌名称
		refundOrderData.put("applyTime", DateUtil.parseLongTime2Str(refundOrder.getApplyTime()));//申请时间
		long storeId = refundOrder.getStoreId();
		StoreBusiness storeBusiness = storeMapper.selectById(storeId);
		refundOrderData.put("phoneNumber", storeBusiness.getPhoneNumber());//门店手机号（申请人手机号码电话）
		refundOrderData.put("legalPerson", storeBusiness.getLegalPerson());//店主姓名(申请人姓名)
		refundOrderData.put("refundStatus", refundStatus);//售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(买家主动关闭)、8、（平台客服主动关闭）
		refundOrderData.put("refundStatusName", buildInfoRefundStatusName(refundStatus));//售后订单状态名称
		refundOrderData.put("refundType", refundOrder.getRefundType());//退款类型：1.仅退款  2.退货退款
		refundOrderData.put("refundCost", refundOrder.getRefundCost());//退款金额
		
		refundOrderData.put("surplusAffirmTime", refundOrderFacade.buildSurplusAffirmTime(refundOrder));//剩余卖家确认时间毫秒数
		refundOrderData.put("surplusDeliverTIme", refundOrderFacade.buildSurplusDeliverTime(refundOrder));//剩余买家发货时间毫秒数
		
		//填充订单数据
		refundOrderData.put("orderNo", refundOrder.getOrderNo());//订单Id
		refundOrderData.put("takeProductStateName", buildTakeProductStateName(refundOrder));//货物状态名称：已收到货、未收到货
		refundOrderData.put("refundReason", refundOrder.getRefundReason());//退款原因
		refundOrderData.put("refundRemark", refundOrder.getRefundRemark());//退款说明
		refundOrderData.put("handlingSuggestion", refundOrder.getHandlingSuggestion());//平台介入处理意见
		refundOrderData.put("surplusSupplierAutoTakeTime", refundOrderFacade.buildSurplusSupplierAutoTakeTime(refundOrder));//剩余卖家自动确认收货时间毫秒数
		
		String refundProofImages = refundOrder.getRefundProofImages();
		refundOrderData.put("refundProofImages", refundProofImages);//退款凭证
		if(StringUtils.isNotEmpty(refundProofImages)){
			refundOrderData.put("refundProofImagesArr", JSON.toJSONString(refundProofImages.split(",")));//退款凭证图片数组
		}else{
			String[] refundProofImagesArr = {};
			refundOrderData.put("refundProofImagesArr", JSON.toJSONString(refundProofImagesArr));//退款凭证图片数组
		}
		
		//填充流程相关字段
		refundOrderData.put("acceptTime", buildAcceptTime(refundOrder));//受理时间
		refundOrderData.put("acceptNote", buildAcceptNote(refundOrder));//受理说明
		refundOrderData.put("refundTime", DateUtil.parseLongTime2Str(refundOrder.getRefundTime()));//退款时间
		refundOrderData.put("platformInterveneState", refundOrder.getPlatformInterveneState());//平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中、3买家申请平台介入结束、4卖家申请平台介入结束
		refundOrderData.put("platformInterveneStateName", refundOrder.buildPlatformInterveneStateName());//平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中、3买家申请平台介入结束、4卖家申请平台介入结束结束
		refundOrderData.put("platformInterveneTime", DateUtil.parseLongTime2Str(refundOrder.getPlatformInterveneTime()));//平台介入时间
		refundOrderData.put("closeTime", buildCloseTime(refundOrder));//关闭时间
		refundOrderData.put("closeCause", buildCloseCause(refundOrder));//关闭原因
		refundOrderData.put("closeNote", buildCloseNote(refundOrder));//关闭备注
		refundOrderData.put("customerReturnTime", DateUtil.parseLongTime2Str(refundOrder.getCustomerReturnTime()));//买家发货时间
		refundOrderData.put("customerExpressNo", refundOrder.getCustomerExpressNo());//买家发货物流单号
		refundOrderData.put("customerExpressCompany", refundOrder.getCustomerExpressCompany());//买家发货物流公司
		refundOrderData.put("customerExpressCompanyName", refundOrder.getCustomerExpressCompanyName());// 买家发货快递公司名称
		
		//添加是否有收货地址信息
		List<SupplierDeliveryAddress> supplierDeliveryAddressList =  supplierDeliveryAddressService.selectListBySupplierId(refundOrder.getSupplierId());
		//是否有收货信息，有：1 无：0
		int hasDeliveryAddressInfo = 0;
		if(supplierDeliveryAddressList.size() > 0){
			hasDeliveryAddressInfo = 1;
		}
		List<Map<String,Object>> deliveryAddressList = new ArrayList<>();
		for(SupplierDeliveryAddress supplierDeliveryAddress : supplierDeliveryAddressList){
			Map<String,Object> map = new HashMap<>();
			String receiptInfoName = supplierDeliveryAddress.getReceiptInfoName();
			String address = supplierDeliveryAddress.getAddress();
			String receiverName = supplierDeliveryAddress.getReceiverName();
			String phoneNumber = supplierDeliveryAddress.getPhoneNumber();
			int defaultAddress = supplierDeliveryAddress.getDefaultAddress();
			
			map.put("receiptInfoName", receiptInfoName);//仓库名称
			map.put("address", address);//地址
			map.put("receiverName", receiverName);//收货人信息
			map.put("phoneNumber", phoneNumber);//电话
			map.put("defaultAddress", defaultAddress);//状态 0：非默认收货地址，1：默认收货地址
			map.put("id", supplierDeliveryAddress.getId());//收货信息ID
			deliveryAddressList.add(map);
		}
		refundOrderData.put("hasDeliveryAddressInfo", hasDeliveryAddressInfo);//是否有收货信息，有：1 无：0
		//收货信息列表
		refundOrderData.put("deliveryAddressList", deliveryAddressList);//收货信息列表
		
		return refundOrderData;
	}
	
	/**
	 * 订单关闭备注:平台介入时，由平台操作关闭售后单时填写的备注信息。若数据为空则不显示。
	 * @param refundOrder
	 * @return
	 */
	private String buildCloseNote(RefundOrder refundOrder) {
		return refundOrder.getPlatformCloseReason();
	}


	/**
	 * 订单关闭原因,2种情况：买家撤销售后申请/买家超时未发货/平台介入
	 * 售后订单状态：6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
	 * @param refundOrder
	 * @return
	 */
	private String buildCloseCause(RefundOrder refundOrder) {
		String closeCause = "";
		int refundStatus = refundOrder.getRefundStatus();
		if(refundStatus == 6){
			closeCause = "买家超时未发货";
		}else if(refundStatus == 7){
			closeCause = "买家撤销售后申请";
		}else if(refundStatus == 8){
			closeCause = "平台介入";
		}else if(refundStatus == 9){
			closeCause = "买家撤销售后申请";
		}
		return closeCause;
	}


	/**
	 * 售后订单关闭时间关闭时间
	 * @param refundOrder
	 * @return
	 */
	private String buildCloseTime(RefundOrder refundOrder) {
		long closeTime = 0;
		int refundStatus = refundOrder.getRefundStatus();// 售后订单状态：6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
		if(refundStatus == 6){
			closeTime = refundOrder.getCustomerOvertimeTimeNoDelivery();//买家超时未发货时间
		}else if(refundStatus == 7){
			closeTime = refundOrder.getCustomerCancelTime();//买家撤销售后订单时间
		}else if(refundStatus == 8){
			closeTime = refundOrder.getPlatformInterveneCloseTime();//平台客服关闭时间
		}else if(refundStatus == 9){
			closeTime = refundOrder.getCustomerCancelTime();//买家撤销售后订单时间
		}
		
		return DateUtil.parseLongTime2Str(closeTime);
		
	}



	/**
	 * 获取受理说明
	 * @param refundOrder
	 * @return
	 */
	private String buildAcceptNote(RefundOrder refundOrder) {
		String storeAgreeRemark = refundOrder.getStoreAgreeRemark();
		String storeRefuseReason = refundOrder.getStoreRefuseReason();
		String acceptNote = "";
		if(StringUtils.isNotEmpty(storeAgreeRemark)){//卖家同意退款备注
			acceptNote = storeAgreeRemark;
		}else if(StringUtils.isNotEmpty(storeRefuseReason)){//卖家拒绝退款原因、拒绝理由：卖家确认拒绝退款时填写的理由
			acceptNote = storeRefuseReason;
		}
		return acceptNote;
	}

	/**
	 * 获取受理时间
	 * @param refundOrder
	 * @return
	 */
	private String buildAcceptTime(RefundOrder refundOrder) {
		long storeAllowRefundTime = refundOrder.getStoreAllowRefundTime();
		long storeRefuseRefundTime = refundOrder.getStoreRefuseRefundTime();
		String acceptTime = "";
		if(storeAllowRefundTime > 0){
			acceptTime = DateUtil.parseLongTime2Str(storeAllowRefundTime);//卖家同意时间
		}else if(storeRefuseRefundTime > 0){
			acceptTime = DateUtil.parseLongTime2Str(storeRefuseRefundTime);//卖家拒绝时间
		}
		return acceptTime;
	}

	

	


	/**
	 * 货物状态
	 */
	private String buildTakeProductStateName(RefundOrder refundOrder) {
		String takeProductState = "未收货"; 
		int refundType = refundOrder.getRefundType();
		if(refundType == RefundOrder.refundType_refund_and_product){//2.退货退款
			 takeProductState = "已收货"; 
		};
		return takeProductState;
	}
	/**
	 * 根据列表售后订单状态返回显示名称
	 * @param refundStatus
	 * @return
	 */
	private String buildInfoRefundStatusName(int refundStatus) {
	//  售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
		String refundStatusName = ""; 
		if(refundStatus == 1 ){//1(待卖家确认、默认)、
			refundStatusName = "待卖家确认";
		}else if(refundStatus == 2 ){//2（待买家发货）
			refundStatusName = "待买家发货";
		}else if(refundStatus == 3 ){//、3（待卖家确认收货）、
			refundStatusName = "待卖家收货";
		}else if(refundStatus == 4 ){//4(退款成功)、
			refundStatusName = "退款成功";
		}else if(refundStatus == 5 ){//5(卖家拒绝售后关闭)、
			refundStatusName = "卖家已拒绝";
		}else if(refundStatus == 6 ){//6（买家超时未发货自动关闭）、
			refundStatusName = "已关闭";
		}else if(refundStatus == 7 ){// 7(卖家同意前买家主动关闭)、
			refundStatusName = "已关闭";
		}else if(refundStatus == 8 ){//8（平台客服主动关闭）
			refundStatusName = "已关闭";
		} else if (refundStatus == 9) {// 9(卖家同意后买家主动关闭)、
			refundStatusName = "已关闭";
		}else{
			logger.info("未知售后订单状态,请尽快处理");
			throw new RuntimeException("未知售后订单状态");
		}
		return refundStatusName;
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IRefundOrderSupplierFacade#getRefundOrderList(long, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public SmallPage getRefundOrderList(long storeId, Page<RefundOrder> page){
		SmallPage smallPage = new SmallPage(page);
		//1、查询数据
   		Wrapper<RefundOrder> wrapper = new EntityWrapper<RefundOrder>().eq("store_id", storeId);
   		wrapper.orderBy("apply_time", false);
   		List<RefundOrder> refundOrderList = refundOrderMapper.selectPage(page, wrapper);
   		//2、组装数据
   		List<Map<String,Object>> refundOrderListDataList = new ArrayList<Map<String,Object>>();
   		for (RefundOrder refundOrder : refundOrderList) {
   			Map<String,Object> refundOrderData = new HashMap<String,Object>();
   			
   			//填充售后订单数据
   			refundOrderData.put("refundOrderId", refundOrder.getId());//售后单ID
   			refundOrderData.put("refundType", refundOrder.getRefundType());//退款类型：1.仅退款  2.退货退款
   			int refundStatus = refundOrder.getRefundStatus();
   			refundOrderData.put("refundStatus", refundStatus);//售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(买家主动关闭)、8、（平台客服主动关闭）
   			refundOrderData.put("refundStatusName", buildListRefundStatusName(refundStatus));//售后订单状态名称
   			
   			
   			//填充订单数据
   			refundOrderData.put("orderNo", refundOrder.getOrderNo());//订单Id
   			refundOrderData.put("brandName", refundOrder.getBrandName());//订单品牌名称
   			
   			//填充订单明细
   			List<StoreOrderItemNew> itemList = storeOrderItemNewService.getItemListByOrderNo(refundOrder.getOrderNo());
   			StoreOrderItemNew item = itemList.get(0);
   			
   			long orderItemId = item.getId();
   			StoreOrderItemNew StoreOrderItemNew = orderItemNewMapper.selectById(orderItemId);
   			refundOrderData.put("orderItemId", orderItemId);//订单明细ID
   			refundOrderData.put("money", StoreOrderItemNew.getMoney());//商品订单单价
   			refundOrderData.put("buyCount", StoreOrderItemNew.getBuyCount());//购买数量
   			
   			//填充sku数据
   			long skuId = item.getSkuId();
   			ProductSkuNew productSkuNew = productSkuNewMapper.selectById(skuId);
   			refundOrderData.put("colorName", productSkuNew.getColorName());//颜色
   			refundOrderData.put("sizeName", productSkuNew.getSizeName());//尺码
   			
   			//填充商品数据
   			long productId = item.getProductId();
   			ProductNew product = productNewMapper.selectById(productId);
   			refundOrderData.put("productId", productId);//商品ID
   			refundOrderData.put("clothesNumber", product.getClothesNumber());//商品款号
   			refundOrderData.put("productName", product.getName());//商品标题
   			refundOrderData.put("mainImg", product.getMainImg());//商品主图
   		
   			//放入List
   			refundOrderListDataList.add(refundOrderData);
   		}
   		smallPage.setRecords(refundOrderListDataList);
   		return smallPage;
   	}
	
	/**
	 * 获取关闭理由
	 * @param refundOrder
	 * @return
	 */
	private String buildRefundOrderCloseReason(RefundOrder refundOrder) {
		int refundStatus = refundOrder.getRefundStatus();
		String refundOrderCloseReason = ""; 
		if(refundStatus == 6 ){//6（买家超时未发货自动关闭）、
			refundOrderCloseReason = "买家超时未发货";
		}else if(refundStatus == 7 ){//7(买家主动关闭)、
			refundOrderCloseReason = "买家主动关闭售后单";
		}else if(refundStatus == 8 ){//8（平台客服主动关闭）
			refundOrderCloseReason = refundOrder.getPlatformCloseReason();
		}else{
			logger.info("未知售后订单状态,请尽快处理");
			throw new RuntimeException("未知售后订单状态");
		}
		
		return refundOrderCloseReason;
	}
	/**
	 * 根据列表售后订单状态返回显示名称
	 * @param refundStatus
	 * @return
	 */
	private String buildListRefundStatusName(int refundStatus) {
//		售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(买家主动关闭)、8、（平台客服主动关闭）
		String refundStatusName = ""; 
		if(refundStatus == 1 ){//1(待卖家确认、默认)、
			refundStatusName = "待卖家确认";
		}else if(refundStatus == 2 ){//2（待买家发货）
			refundStatusName = "卖家同意";
		}else if(refundStatus == 3 ){//、3（待卖家确认收货）、
			refundStatusName = "待卖家收货";
		}else if(refundStatus == 4 ){//4(退款成功)、
			refundStatusName = "已退款";
		}else if(refundStatus == 5 ){//5(卖家拒绝售后关闭)、
			refundStatusName = "卖家拒绝";
		}else if(refundStatus == 6 ){//6（买家超时未发货自动关闭）、
			refundStatusName = "已失效";
		}else if(refundStatus == 7 ){//7(买家主动关闭)、
			refundStatusName = "已撤销";
		}else if(refundStatus == 8 ){//8（平台客服主动关闭）
			refundStatusName = "平台关闭";
		}else{
			logger.info("未知售后订单状态,请尽快处理");
			throw new RuntimeException("未知售后订单状态");
		}
		return refundStatusName;
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IRefundOrderSupplierFacade#getRefundOrderList(long, long, long, long, java.lang.String, java.lang.String, int, int, java.lang.String, double, double, int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public List<Map<String, Object>> getRefundOrderList(long userId, long refundOrderNo, long orderNo,
			String storePhone, String storeName, int refundStatus, int refundType, String refundReason,
			double refundCostMin, double refundCostMax, int returnCountMin, int returnCountMax, String applyTimeMin,
			String applyTimeMax, String storeDealRefundTimeMin, String storeDealRefundTimeMax,
			Page<Map<String, Object>> page) {
		Wrapper<RefundOrder> wrapper = getRefundOrderWrapper(userId,refundOrderNo,orderNo,storePhone,storeName,
					refundStatus,refundType,refundReason,refundCostMin,refundCostMax,returnCountMin,returnCountMax,applyTimeMin,applyTimeMax,
					storeDealRefundTimeMin,storeDealRefundTimeMax);
		
		List<RefundOrder> selectList = refundOrderMapper.selectPage(page, wrapper);
		List<Map<String,Object>> refundOrderList = new ArrayList<Map<String,Object>>();
		for (RefundOrder refundOrder : selectList) {
			Map<String, Object> refundOrderMap = getEncapsulationRefundOrderResult(refundOrder);
			refundOrderList.add(refundOrderMap);
		}
		return refundOrderList;
	}
	
	/**
	 * 获取售后单列表的wrapper
	 * @param userId
	 * @param refundOrderNo
	 * @param orderNo
	 * @param skuId
	 * @param customerPhone
	 * @param customerName
	 * @param refundStatus
	 * @param refundType
	 * @param refundReasonId
	 * @param refundCostMin
	 * @param refundCostMax
	 * @param returnCountMin
	 * @param returnCountMax
	 * @param applyTimeMin
	 * @param applyTimeMax
	 * @param storeDealRefundTimeMin
	 * @param storeDealRefundTimeMax
	 * @return
	 */
	private Wrapper<RefundOrder> getRefundOrderWrapper(long userId, long refundOrderNo, long orderNo, 
			String storePhone, String storeName, int refundStatus, int refundType, String refundReason,
			double refundCostMin, double refundCostMax, int returnCountMin, int returnCountMax, String applyTimeMin,
			String applyTimeMax, String storeDealRefundTimeMin, String storeDealRefundTimeMax){
		//转换时间格式为long型
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Wrapper<RefundOrder> wrapper = 
					new EntityWrapper<RefundOrder>().eq("supplier_id",userId).orderBy("apply_time", false);
			if(refundOrderNo>0){
				wrapper.like("refund_order_no", refundOrderNo+"");
			}
			if(orderNo>0){
				wrapper.like("order_no", orderNo+"");
			}
			if(!StringUtils.isEmpty(storePhone)){
				wrapper.like("store_phone", storePhone);
			}
			if(!StringUtils.isEmpty(storeName)){
				wrapper.like("store_name", storeName);
			}
			if(refundStatus>0){
				if(refundStatus==6){
					List<Integer> list = new ArrayList<Integer>();
					list.add(RefundStatus.CUSTOMER_OVERTIME_UNDELIVERY.getIntValue());
					list.add(RefundStatus.CUSTOMER_CLOSE.getIntValue());
					list.add(RefundStatus.ADMIN_CLOSE.getIntValue());
					list.add(RefundStatus.CUSTOMER_CLOSE_AFTER_AGREE.getIntValue());
					wrapper.in("refund_status", list);
				}else{
					wrapper.eq("refund_status", refundStatus);
				}
			}
			if(refundType>0){
				wrapper.eq("refund_type", refundType);
			}
			if(!StringUtils.isEmpty(refundReason)){
				wrapper.like("refund_reason", refundReason);
			}
			if(refundCostMin>0){
				wrapper.ge("refund_cost", refundCostMin);
			}
			if(refundCostMax>0){
				wrapper.le("refund_cost", refundCostMax);
			}
			if(returnCountMin>0){
				wrapper.ge("return_count", returnCountMin);
			}
			if(returnCountMax>0){
				wrapper.le("return_count", returnCountMax);
			}
			
			if(!StringUtils.isEmpty(applyTimeMin)){
				long applyTimeMinLong = simpleDateFormat.parse(applyTimeMin).getTime();
				wrapper.ge("apply_time", applyTimeMinLong);
			}
			if(!StringUtils.isEmpty(applyTimeMax)){
				long applyTimeMaxLong = simpleDateFormat.parse(applyTimeMax).getTime();
				wrapper.le("apply_time", applyTimeMaxLong);
			}
			if(!StringUtils.isEmpty(storeDealRefundTimeMin)){
				long storeDealRefundTimeMinLong = simpleDateFormat.parse(storeDealRefundTimeMin).getTime();
				if(!StringUtils.isEmpty(storeDealRefundTimeMax)){
					long storeDealRefundTimeMaxLong = simpleDateFormat.parse(storeDealRefundTimeMax).getTime();
					wrapper.and(" ((store_allow_refund_time <= " + storeDealRefundTimeMaxLong + " and store_allow_refund_time >= " + storeDealRefundTimeMinLong
							+ ") or (store_refuse_refund_time <= " + storeDealRefundTimeMaxLong 
							+ " and store_refuse_refund_time >= " + storeDealRefundTimeMinLong + "))");
				}
				wrapper.and(" (store_allow_refund_time >= " + storeDealRefundTimeMinLong + " or store_refuse_refund_time >= " + storeDealRefundTimeMinLong + ")");
			}
			
			return wrapper;
		} catch (Exception e) {
			e.printStackTrace();
    		logger.error("获取售后订单列表:"+e.getMessage());
    		throw new RuntimeException("获取售后订单列表:"+e.getMessage());
		}
	}
	
	public static void main(String[] args){
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			System.out.println(simpleDateFormat.format(new Date(1515405401274L)));
			System.out.println(simpleDateFormat.format(new Date(1515168000000L)));
			System.out.println(simpleDateFormat.parse("2018-01-06 00:00").getTime());
			System.out.println(simpleDateFormat.parse("2018-01-07 00:00").getTime());
			System.out.println(simpleDateFormat.parse("2018-01-08 00:00").getTime());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	/**
	 * 封装RefundOrder数据
	 * @param refundOrder
	 */
	private Map<String,Object> getEncapsulationRefundOrderResult(RefundOrder refundOrder){
		//转换时间格式为long型
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Map<String,Object> refundOrderMap = new HashMap<String,Object>();
		refundOrderMap.put("id", refundOrder.getId());
		refundOrderMap.put("refundOrderNo", refundOrder.getRefundOrderNo());
		refundOrderMap.put("orderNo", refundOrder.getOrderNo());
//		refundOrderMap.put("skuId", refundOrder.getSkuId());
		refundOrderMap.put("storePhone", refundOrder.getStorePhone());
		refundOrderMap.put("storeName", refundOrder.getStoreName());
		
		//类型
		//1:仅退款;2:退货退款
		String refundTypeString = "";
			switch (refundOrder.getRefundType()) {
		case RefundOrder.refundType_refund:
			refundTypeString = "仅退款";
			break;
		case RefundOrder.refundType_refund_and_product:
			refundTypeString = "退货退款";
			break;
		}
		refundOrderMap.put("refundType", refundTypeString);
		
		//状态
		//售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(买家主动关闭)、8、（平台客服主动关闭）
		String refundStatusString = "";
		switch (refundOrder.getRefundStatus()) {
		case 2:
			refundStatusString = "待买家发货";
			break;
		case 3:
			refundStatusString = "待卖家确认收货";
			break;
		case 4:
			refundStatusString = "退款成功";
			break;
		case 5:
			refundStatusString = "卖家拒绝售后关闭";
			break;
		case 6:
			refundStatusString = "已关闭";
			break;
		case 7:
			refundStatusString = "已关闭";
			break;
		case 8:
			refundStatusString = "已关闭";
			break;
		case 9:
			refundStatusString = "已关闭";
			break;
		default:
			refundStatusString = "待卖家确认";
			break;
		}
		refundOrderMap.put("refundStatus", refundStatusString);
		
		refundOrderMap.put("refundCost", refundOrder.getRefundCost());
		refundOrderMap.put("returnCount", refundOrder.getReturnCount());
		//申请日期
		Long applyTime = refundOrder.getApplyTime();
		refundOrderMap.put("applyTime", DateUtil.parseLongTime2Str(refundOrder.getApplyTime()));
		
		//若4小时后仍未被卖家受理的售后单，1:超过;0:未超过;
		if(refundOrder.getRefundStatus()==1 && applyTime>0 && (System.currentTimeMillis()-applyTime)>(4*60*60*1000)){
			refundOrderMap.put("untreatedTimeMoreThan4Hours", 1);
		}else{
			refundOrderMap.put("untreatedTimeMoreThan4Hours", 0);
		}
		
		//受理时间
		refundOrderMap.put("storeDealRefundTime", buildAcceptTime(refundOrder));
		
//		if(refundOrder.getStoreAllowRefundTime()>0){
//			if(refundOrder.getStoreRefuseRefundTime()>0){
//				refundOrderMap.put("storeDealRefundTime", 
//						refundOrder.getStoreAllowRefundTime()>=refundOrder.getStoreRefuseRefundTime()?
//						simpleDateFormat.format(new Date(refundOrder.getStoreAllowRefundTime())):
//						simpleDateFormat.format(new Date(refundOrder.getStoreRefuseRefundTime())));
//			}else{
//				refundOrderMap.put("storeDealRefundTime", simpleDateFormat.format(new Date(refundOrder.getStoreAllowRefundTime())));
//			}	
//		}else{
//			if(refundOrder.getStoreRefuseRefundTime()>0){
//				refundOrderMap.put("storeDealRefundTime",simpleDateFormat.format(new Date(refundOrder.getStoreRefuseRefundTime())));
//			}else{
//				refundOrderMap.put("storeDealRefundTime", "无");
//			}	
//		}
		
		
		return refundOrderMap;
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IRefundOrderSupplierFacade#dealRefundOrder(long, int, java.lang.String)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int dealRefundOrder(long refundOrderId, int dealType, String dealRemark, String phoneNumber, String verifyCode, String sendType, String receiver, String receiverPhone, String supplierReceiveAddress) throws Exception {
		Wrapper<RefundOrder> wrapper = 
				new EntityWrapper<RefundOrder>().eq("id",refundOrderId);
		List<RefundOrder> refundOrderList = refundOrderMapper.selectList(wrapper);
		long orderNo = 0;
		RefundOrder refundOrderNew = new RefundOrder();
		if(refundOrderList!=null && refundOrderList.size()>0){
			refundOrderNew = refundOrderList.get(0);
			
//			if(refundOrderNew.getRefundStatus()==2 && refundOrderNew.getRefundStatus()==3){
//				logger.error("受理售后订单:该订单已处理:refundOrderId:"+refundOrderId+";refundStatus:"+refundOrderNew.getRefundStatus());
//				throw new RuntimeException("受理售后订单:该订单已处理:refundOrderId:"+refundOrderId);
//			}
			if(refundOrderNew.getRefundStatus()!=1){
				logger.error("受理售后订单:该订单已处理:refundOrderId:"+refundOrderId+";refundStatus:"+refundOrderNew.getRefundStatus());
				throw new RuntimeException("受理售后订单:该订单已处理:refundOrderId:"+refundOrderId);
			}
			
			orderNo = refundOrderNew.getOrderNo();
			
			RefundOrder refundOrder = new RefundOrder();
			if(dealType==1){
				//1：同意
				if(!StringUtils.isEmpty(phoneNumber)){
					//检验验证码是否正确
					//3.5版本 退款金额大于10000
					if(refundOrderNew.getRefundCost() >= TEN_THOUSAND){
						myAccountSupplierService.checkVerifyCode(phoneNumber, verifyCode, sendType);
					}
				}
				if(refundOrderNew.getRefundType()==RefundOrder.refundType_refund){
					//退款
					refundOrderFacade.refundSuccessOperation(refundOrderId);
//					//仅退款
//					StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(orderNo);
//					if(storeOrderNew.getPaymentType()==PaymentType.ALIPAY_SDK.getIntValue()){
//						refundService.alipayRefund(storeOrderNew, refundOrderNew.getRefundCost()+"", refundOrderNew.getRefundReason(),
//								String.valueOf(refundOrderId));
//					}else if(storeOrderNew.getPaymentType()==PaymentType.WEIXINPAY_SDK.getIntValue()){
//						refundService.weixinRefund(storeOrderNew, refundOrderNew.getRefundCost()+"", refundOrderNew.getRefundReason(), true,
//								String.valueOf(refundOrderId));
//					}else{
//						logger.error("订单的支付方式未知PaymentType为："+storeOrderNew.getPaymentType());
//						throw new RuntimeException("受理售后订单:订单的支付方式未知PaymentType为："+storeOrderNew.getPaymentType());
//					}
//					refundOrder.setRefundStatus(4);
//					//2、退款成功：【俞姐姐门店宝】您提交的退货申请，款项已到帐。希望您的退货体验还不错，一定要继续进货哦。
//					refundSMSNotificationService.sendSMSNotificationAndGEPush(null, 6, refundOrderNew.getStoreId(), null);
//					//3、添加售后订单操作日志
//					refundOrderActionLogService.addActionLog(RefundOrderActionLogEnum.F,refundOrderId);
					return 1;
				}else if(refundOrderNew.getRefundType()==RefundOrder.refundType_refund_and_product){
					//退货退款
					long supplierId = refundOrderNew.getSupplierId();
					UserNew userNew = userNewMapper.selectById(supplierId);
					
					
					//3.5注释掉#######
//					if(StringUtils.isEmpty(userNew.getReceiver()) || StringUtils.isEmpty(userNew.getReceiverPhone()) || StringUtils.isEmpty(userNew.getSupplierReceiveAddress())){
//						logger.error("受理售后订单:收货人信息不全:收货人姓名:"+userNew.getReceiver()+";收货人电话:"+userNew.getReceiverPhone()+";收货人地址:"+userNew.getSupplierReceiveAddress());
//						throw new RuntimeException("受理售后订单:收货人信息不全");
//					}
					
					
					//1、设置售后单状态：（待买家发货）
					refundOrder.setRefundStatus(2);
					refundOrder.setReceiver(receiver);
					refundOrder.setReceiverPhone(receiverPhone);
					refundOrder.setSupplierReceiveAddress(supplierReceiveAddress);
					
					//2、通知买家发货（短信&APP消息：【俞姐姐门店宝】您提交的退货卖家已经同意了哦，请在3天内填写物流信息。3天内无物流更新系统将关闭您的退货申请，需要重新申请退货哦。）
					refundSMSNotificationService.sendSMSNotificationAndGEPush(null, 3, refundOrderNew.getStoreId(), null);
					//3、添加售后订单操作日志
					refundOrderActionLogService.addActionLog(RefundOrderActionLogEnum.C,refundOrderId);
					refundOrder.setStoreAllowRefundTime(System.currentTimeMillis());
					refundOrder.setStoreAgreeRemark(dealRemark);
					return refundOrderMapper.update(refundOrder, wrapper);
				}
				
			}else if(dealType==2){
				//2：拒绝
				/**
				 *  
					申请+重新申请总次数达3次后：disable 重新申请售后 按钮
				 */
				//(5)更改售后订单信息拒绝信息
				refundOrder.setRefundStatus(5);
				refundOrder.setStoreRefuseRefundTime(System.currentTimeMillis());
				refundOrder.setStoreRefuseReason(dealRemark);
				int record = refundOrderMapper.update(refundOrder, wrapper);
				
				//(2)更改订单售后状态（遍历所有该订单的售后单，是否全部处理，未售后或售后中，注意一起考虑平台介入状态）
				boolean flag = true;
				Wrapper<RefundOrder> allRefundOrderWrapper = 
						new EntityWrapper<RefundOrder>().eq("order_no",orderNo);
				List<RefundOrder> allRefundOrderList = refundOrderMapper.selectList(allRefundOrderWrapper);
				for (RefundOrder refundOrderEach : allRefundOrderList) {
					//遍历所有该订单的售后单，是否全部处理，未售后或售后中，注意一起考虑平台介入状态
					if(refundOrderEach.getPlatformInterveneState()==1 || refundOrderEach.getPlatformInterveneState()==2 || refundOrderEach.getRefundStatus()<4){
						flag = false;
						break;
					}
				}
				//更改订单售后状态
				if(flag){
//					StoreOrderNew storeOrderNewUpdate = new StoreOrderNew();
//					storeOrderNewUpdate.setOrderNo(orderNo);
//					//设置为不在售后中
//					storeOrderNewUpdate.setRefundUnderway(0);
//					supplierOrderMapper.updateById(storeOrderNewUpdate);
					
					//TODO (3)统计订单暂停自动确认收货时长维护到订单表对应字段中（拒绝时间-申请时间）
//					refundOrderService.settlementAutoDeliveryPauseTime(refundOrderId);
					
					//更改订单状态以及计算自动确认收货总暂停时长
					refundOrderFacadeNJ.addAutoTakeGeliveryPauseTimeAndClearSign(orderNo);
				}
				//(4)添加售后订单操作日志
				refundOrderActionLogService.addActionLog(RefundOrderActionLogEnum.B,refundOrderId);
				//(6)更改售后订单详情表中售后状态
//				storeOrderItemService.updateStoreOrderItemRefundStatus(refundOrderNew.getOrderItemId());
				//(7)短信&？等通知买家（【俞姐姐门店宝】您提交的退货申请卖家拒绝了哦。可以联系客服帮您处理。）
				refundSMSNotificationService.sendSMSNotificationAndGEPush(null, 2, refundOrderNew.getStoreId(), null);
				return record;
			}
			return 1;
		}else{
			logger.error("受理售后订单的订单不存在订单号refundOrderId为："+refundOrderId);
			throw new RuntimeException("受理售后订单:订单不存在订单号refundOrderId为："+refundOrderId);
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void confirmReceipt(long refundOrderId) {
		RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);
		if(refundOrder.getRefundStatus()!=RefundStatus.CUSTOMER_DELIVERY.getIntValue()){
			logger.error("该售后单状态有误refundOrderId为："+refundOrderId+";refundStatus:"+refundOrder.getRefundStatus());
			throw new RuntimeException("该售后单状态有误");
		}
		//退款
		refundOrderFacade.refundSuccessOperation(refundOrderId);
	}

	@Override
	public List<String> getRefundReasonList() {
   		List<String> refundReasonList = refundOrderMapper.getRefundReasonList();
		return refundReasonList;
	}


	@Override
	public Map<String, Object> getSupplierDeliveryAddressList(int userId) {
		Map<String,Object> refundOrderData = new HashMap<>();
		List<SupplierDeliveryAddress> supplierDeliveryAddressList =  supplierDeliveryAddressService.selectListBySupplierId(new Long(userId));
		// TODO
		List<Map<String,Object>> deliveryAddressList = new ArrayList<>();
		for(SupplierDeliveryAddress supplierDeliveryAddress : supplierDeliveryAddressList){
			Map<String,Object> map = new HashMap<>();
			String receiptInfoName = supplierDeliveryAddress.getReceiptInfoName();
			String address = supplierDeliveryAddress.getAddress();
			String receiverName = supplierDeliveryAddress.getReceiverName();
			String phoneNumber = supplierDeliveryAddress.getPhoneNumber();
			int defaultAddress = supplierDeliveryAddress.getDefaultAddress();
			
			map.put("receiptInfoName", receiptInfoName);//仓库名称
			map.put("address", address);//地址
			map.put("receiverName", receiverName);//收货人信息
			map.put("phoneNumber", phoneNumber);//电话
			map.put("defaultAddress", defaultAddress);//状态 0：非默认收货地址，1：默认收货地址
			map.put("id", supplierDeliveryAddress.getId());//收货信息ID
			deliveryAddressList.add(map);
		}
		//收货信息列表
			refundOrderData.put("deliveryAddressList", deliveryAddressList);//收货信息列表
		return refundOrderData;
	}

}