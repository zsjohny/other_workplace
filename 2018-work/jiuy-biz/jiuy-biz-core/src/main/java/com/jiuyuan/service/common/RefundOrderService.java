package com.jiuyuan.service.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.RefundStatus;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.mapper.supplier.OrderItemNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.SupplierDeliveryAddress;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.util.DoubleUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class RefundOrderService implements IRefundOrderService {

	private static final Log logger = LogFactory.get(RefundOrderService.class);

	@Autowired
	private RefundOrderMapper refundOrderMapper;
	
	@Autowired
	private OrderItemNewMapper orderItemNewMapper;
	
	@Autowired
	private ProductNewMapper productNewMapper;
	
	@Autowired
	private ProductSkuNewMapper productSkuNewMapper;
	
	@Autowired
	private UserNewMapper userNewMapper;
	
	@Autowired
	private IStoreOrderNewService storeOrderNewService;
	
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;
	@Autowired
	private ISupplierDeliveryAddress deliveryAddressService;
	/**
	 * 更改平台介入状态为卖家平台介入中
	 * @param refundOrderId
	 */
	public void supplierApplyPlatformIntervene(long refundOrderId) {
		applyPlatformIntervene(refundOrderId,RefundOrder.SELLER_PLATFORM_INTERVENE);
	}

	/**
	 * 更改平台介入状态为买家平台介入中
	 * @param refundOrderId
	 */
	public void storeApplyPlatformIntervene(long refundOrderId) {
		applyPlatformIntervene(refundOrderId,RefundOrder.CUSTOMER_PLATFORM_INTERVENE);
	}
	
	/**
	 * 更改平台介入状态
	 * @param refundOrderId
	 * @param platformInterveneState
	 */
	private void applyPlatformIntervene(long refundOrderId,int platformInterveneState) {
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setId(refundOrderId);
		refundOrder.setPlatformInterveneState(platformInterveneState);
		refundOrder.setPlatformInterveneTime(System.currentTimeMillis());
		refundOrderMapper.updateById(refundOrder);
	}

	public RefundOrder getRefundOrderByitemIdUnderWay(Long itemId) {
		List<Integer> status = new ArrayList<Integer>();
		status.add(RefundStatus.CUSTOMER_APPLY_REFUND.getIntValue());
		status.add(RefundStatus.CUSTOMER_DELIVERY.getIntValue());
		status.add(RefundStatus.SELLER_ACCEPT.getIntValue());
		Wrapper<RefundOrder> wrapper = new EntityWrapper<RefundOrder>().eq("order_item_id", itemId)
				                                                       .in("refund_status",status);
		List<RefundOrder> list = refundOrderMapper.selectList(wrapper);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
		
		
	}
    
	public RefundOrder getRefundOrderByitemIdUnderWayOrSuccess(Long itemId) {
		List<Integer> status = new ArrayList<Integer>();
		status.add(RefundStatus.CUSTOMER_APPLY_REFUND.getIntValue());
		status.add(RefundStatus.CUSTOMER_DELIVERY.getIntValue());
		status.add(RefundStatus.SELLER_ACCEPT.getIntValue());
		status.add(RefundStatus.REFUND_SUCCESS.getIntValue());
		Wrapper<RefundOrder> wrapper = new EntityWrapper<RefundOrder>().eq("order_item_id", itemId)
				                                                       .in("refund_status",status);
		List<RefundOrder> list = refundOrderMapper.selectList(wrapper);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
		
	}
	public RefundOrder getRefundOrderByOrderNoUnderWayOrSuccess(Long orderNo) {
		List<Integer> status = new ArrayList<Integer>();
		status.add(RefundStatus.CUSTOMER_APPLY_REFUND.getIntValue());
		status.add(RefundStatus.CUSTOMER_DELIVERY.getIntValue());
		status.add(RefundStatus.SELLER_ACCEPT.getIntValue());
		status.add(RefundStatus.REFUND_SUCCESS.getIntValue());
		Wrapper<RefundOrder> wrapper = new EntityWrapper<RefundOrder>().eq("order_no", orderNo)
				                                                       .in("refund_status",status);
		List<RefundOrder> list = refundOrderMapper.selectList(wrapper);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
		
	}

	public void updateExpressInfo(String expressNo, String expressSupplier, Long refundOrderId, String expressSupplierCNName) {
		//获取售后订单信息
		RefundOrder order = refundOrderMapper.selectById(refundOrderId);
		//查询供应商信息
		UserNew userNew = userNewMapper.selectById(order.getSupplierId());
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setCustomerExpressNo(expressNo);//买家发货快递号
		refundOrder.setId(refundOrderId);//售后订单号
		refundOrder.setCustomerExpressCompany(expressSupplier);//快递公司
		refundOrder.setCustomerExpressCompanyName(expressSupplierCNName);//快递公司中文名称
		refundOrder.setCustomerReturnTime(System.currentTimeMillis());//买家发货时间
		refundOrder.setRefundStatus(RefundStatus.CUSTOMER_DELIVERY.getIntValue());//售后状态
		
		/**
		 * 3.5注释掉
		 */
//		refundOrder.setReceiver(userNew.getReceiver());//供应商收货人姓名
//		refundOrder.setSupplierReceiveAddress(userNew.getSupplierReceiveAddress());//供应商收货地址
//		refundOrder.setReceiverPhone(userNew.getReceiverPhone());//收货人电话
		
		/**
		 * 3.5添加，主要为控制3.5版本之前，供应商已经同意的售后单，（3.5之前在卖家发货时绑定售后收货信息，3.5在供应商同意受理时绑定售后收货信息）
		 */
		if (StringUtils.isEmpty(refundOrder.getReceiver())) {
			SupplierDeliveryAddress defaultAddress = deliveryAddressService.getDefaultAddress(order.getSupplierId());
			refundOrder.setReceiver(defaultAddress.getReceiverName());//供应商收货人姓名
			refundOrder.setSupplierReceiveAddress(defaultAddress.getAddress());//供应商收货地址
			refundOrder.setReceiverPhone(defaultAddress.getPhoneNumber());//收货人电话
		}
		refundOrderMapper.updateById(refundOrder);
		
		
		int i = 0;
		if( i == -1){
			throw new RuntimeException("保存买家发货信息失败！");
		}
		
		
	}
	
	public static void main(String[] args) {
		
	}
	/**
	 * 计算退款费用
	 * 买家订单实付款 = 商品总金额 + 运费 - 店铺优惠额 - 平台优惠额
	 * 卖家销售收入 = 商品总金额 + 运费 - 店铺优惠额
	 * 最大可退款额 = 退款商品金额 - ？店铺优惠额 - ？平台优惠额
	 * @return
	 */
	/**
	 * 
	 * 打折券、满减、红包
	 * 
	 * 
	 * @param orderProductTotalCost 未退款商品费用
	 * @param postCost 邮寄费用
	 * @param storeCouponCost 店铺优惠券优惠限制
	 * @param storeCouponCost 店铺优惠券优惠金额
	 * @param storeCouponCost 平台优惠券优惠限制
	 * @param storeCouponCost 平台优惠券优惠金额
	 * @return
	 */
	public double calculateRefundCost(
			double orderProductTotalCost , 
			double itemProductSingleCost ,
			int itemProductRefundCount ,
			double postCost,
			double storeCouponlimit,
			double storeCouponCost,
			double platformCouponLimit,
			double platformCouponCost){
		double refundCost = 0;
		if(storeCouponCost != 0 ){//卖家应付退款x = 退款商品金额a - 退店铺优惠额b
			
		}
		refundCost = itemProductSingleCost * itemProductRefundCount - storeCouponCost;
		
		if(platformCouponCost != 0 ){//卖家应付退款x = 退款商品金额a - 退店铺优惠额b
			
		}
		
		refundCost = refundCost - platformCouponCost;
		return refundCost;
	}
    /**
     * 更改订单退款状态以及更改退款金额
     * @param orderNo
     * @param orderStatus
     */
	public void updateOrderRefundStatusAndRefundCost(Long orderNo, int orderStatus,Double refundCost) {
		StoreOrderNew storeOrderNew1 = supplierOrderMapper.selectById(orderNo);
		StoreOrderNew storeOrderNew = new StoreOrderNew();
		//订单开启
		storeOrderNew.setRefundUnderway(StoreOrderNew.REFUND_NOT_UNDERWAY);
		//添加延长自动确认收货时间
		long currentTime = System.currentTimeMillis();
		long startTime = storeOrderNew1.getRefundStartTime();
		long autoTakeGeliveryPauseTimeLength = currentTime-startTime;
		autoTakeGeliveryPauseTimeLength += storeOrderNew1.getAutoTakeGeliveryPauseTimeLength();
		storeOrderNew.setAutoTakeGeliveryPauseTimeLength(autoTakeGeliveryPauseTimeLength);
		//添加自动确认收货总暂停时长				storeOrderNew.setRefundStartTime(0L);//售后开始时间
		storeOrderNew.setOrderNo(orderNo);
		// 通过订单状态来区分退款后订单的流转状态
		// 已付款.. 退款金额为全部退款 （商品金额 + 运费） 订单关闭条件 退款金额  = 商品金额 + 运费
		// 已发货.. 退款金额最大只能是商品金额            订单关闭条件  退款金额 = 订单商品金额
		double totalMoney = storeOrderNew1.getTotalPay();
		BigDecimal refundCostBig = new BigDecimal(refundCost).setScale(2,BigDecimal.ROUND_HALF_UP);
		BigDecimal totalMoneyBig = new BigDecimal(totalMoney);

		if(storeOrderNew1.getOrderStatus() == OrderStatus.PAID.getIntValue()){
			// 已付款
			totalMoneyBig = totalMoneyBig.add(new BigDecimal(storeOrderNew1.getTotalExpressMoney()));
		}
		// 取俩位小数
		totalMoneyBig = totalMoneyBig.setScale(2,BigDecimal.ROUND_HALF_UP);
		//如果是全额退款就关闭订单 加上运费
		if(refundCostBig.compareTo(totalMoneyBig) == 0 ){
			//更改订单状态
			storeOrderNew.setOrderStatus(OrderStatus.CLOSED.getIntValue());
		}//更改退款开始时间
		storeOrderNew.setRefundStartTime(0L);
		StoreOrderNew order = supplierOrderMapper.selectById(orderNo);
		double totalRefundCost = order.getTotalRefundCost();
		totalRefundCost = DoubleUtil.add(totalRefundCost, refundCost);
		//添加订单的总退款金额
		storeOrderNew.setTotalRefundCost(totalRefundCost);
		storeOrderNew.setUpdateTime(System.currentTimeMillis());
		supplierOrderMapper.updateById(storeOrderNew);
	}
    /**
     * 更改售后订单的售后状态以及时间
     * @param refundOrderId
     */
	public void updateRefundOrderInfoWhenRefunded(Long refundOrderId,boolean flag) {
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setId(refundOrderId);
		refundOrder.setRefundStatus(RefundStatus.REFUND_SUCCESS.getIntValue());
		refundOrder.setRefundTime(System.currentTimeMillis());
		refundOrder.setStoreAllowRefundTime(System.currentTimeMillis());
		//如果平台介入
		if(flag){
			refundOrder.setPlatformInterveneState(RefundOrder.CLOSE_SELLER_PLATFORM_INTERVENE);
		}
		refundOrderMapper.updateById(refundOrder);
	}

	@Override
	public List<RefundOrder> getRefundSuccessListByOrderNo(Long orderNo) {
		Wrapper<RefundOrder> wrapper = new EntityWrapper<RefundOrder>();
		wrapper.eq("order_no", orderNo).eq("refund_status", RefundStatus.REFUND_SUCCESS.getIntValue());
		return refundOrderMapper.selectList(wrapper);
	}

	//获取该订单中已成功退款的售后单
	@Override
	public List<RefundOrder> getRefundOrderWhenSuccess(Long orderNo) {
		//获取该订单中已成功退款的售后单
		Wrapper<RefundOrder> refundOrderWrapper = new EntityWrapper<RefundOrder>();
		refundOrderWrapper.eq("order_no", orderNo).eq("refund_status", RefundStatus.REFUND_SUCCESS);
		List<RefundOrder> refundOrderList = refundOrderMapper.selectList(refundOrderWrapper);
		return refundOrderList;
	}

	/**
	 * 获取未结束的售后订单个数
	 */
	@Override
	public int getUnfinishedRefundOrderCount(long supplierId) {
		Wrapper<RefundOrder> refundOrderWrapper = new EntityWrapper<RefundOrder>();
		refundOrderWrapper.eq("supplier_id", supplierId).and(" (refund_status<=3 or platform_intervene_state in (1,2)) ", null);
		return refundOrderMapper.selectCount(refundOrderWrapper);
	}
	
	/**
	 * 根据订单编号获取进行中的售后订单
	 * @param orderNo
	 * @return
	 */
	public List<RefundOrder> getUnderWayRefundOrderList(long orderNo) {
		//获取该订单中已成功退款的售后单
		Wrapper<RefundOrder> refundOrderWrapper = new EntityWrapper<RefundOrder>();
		refundOrderWrapper.eq("order_no", orderNo).and(" (refund_status  in (1,2,3)) ", null);
		return refundOrderMapper.selectList(refundOrderWrapper);
	}
	
}