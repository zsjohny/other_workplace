package com.jiuyuan.service.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.mapper.supplier.OrderItemNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.StoreOrderItemNew;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.util.DoubleUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class StoreOrderItemNewService {
	private static final Log logger = LogFactory.get(); 
	@Autowired
	private OrderItemNewMapper orderItemNewMapper;

	@Autowired
	private ProductNewMapper productNewMapper;
	
	@Autowired
	private ProductSkuNewMapper productSkuNewMapper;
	
	@Autowired
	private IStoreOrderNewService storeOrderNewService;
	


	public Map<String,Object> getItemInfo(Long itemId, Long storeId) {
		Map<String, Object> map = new HashMap<String,Object>();
		//获取该门店用户的订单商品详情
		StoreOrderItemNew storeOrderItemNew = orderItemNewMapper.selectById(itemId);
		Map<String,Object> orderMap = storeOrderNewService.getSupplierOrderByOrderNo(storeOrderItemNew.getOrderNo());
		 //填充商品数据
	    long productId = storeOrderItemNew.getProductId();
	    ProductNew product = productNewMapper.selectById(productId);
	    map.put("productId", productId);//商品ID
	    map.put("clothesNumber", product.getClothesNumber());//商品款号
	    map.put("productName", product.getName());//商品标题
	    map.put("mainImg", product.getMainImg());//商品主图
	    map.put("brandId", product.getBrandId());//品牌ID
	    map.put("brandName",product.getBrandName());//品牌名称
	    map.put("paymentType", orderMap.get("PaymentType"));//付款方式0:未知，2:支付宝,3:微信
	    OrderStatus orderStatus = (OrderStatus)orderMap.get("orderStatus");
	    if(OrderStatus.PAID.getIntValue() == orderStatus.getIntValue()){
	    	map.put("orderStatus", OrderStatus.PAID.getIntValue());
	    }else if(OrderStatus.DELIVER.getIntValue() == orderStatus.getIntValue() ){
	    	map.put("orderStatus", OrderStatus.DELIVER.getIntValue());
	    }else{
	    	logger.info("该订单无法申请售后！");
	    	throw new RuntimeException("该订单无法申请售后！");
	    }
	    
	    //填充订单明细
	    long orderItemId = storeOrderItemNew.getId();
	    map.put("orderItemId", orderItemId);//订单明细ID
	    map.put("money", storeOrderItemNew.getMoney());//商品订单单价
	    map.put("buyCount", storeOrderItemNew.getBuyCount());//购买数量
	    map.put("totalPay",storeOrderItemNew.getTotalPay());//实付金额不包含邮费，即最多可退
	    map.put("totalExpressMoney", storeOrderItemNew.getTotalExpressMoney());//总邮费
	    map.put("totalMoney", storeOrderItemNew.getTotalMoney());//商品订单总金额
	    map.put("couponMoney", DoubleUtil.sub(storeOrderItemNew.getTotalMoney(), storeOrderItemNew.getTotalPay()));//优惠金额
	    //填充sku数据
	    long skuId = storeOrderItemNew.getSkuId();
	    ProductSkuNew productSkuNew = productSkuNewMapper.selectById(skuId);
	    map.put("colorName", productSkuNew.getColorName());//颜色
	    map.put("sizeName", productSkuNew.getSizeName());//尺码
	    map.put("skuId", skuId);//skuId
		return map;
	}

//	/**
//	 * 更改售后订单详情表中售后状态
//	 * @param orderItemId
//	 */
//	public void updateStoreOrderItemRefundStatus(Long orderItemId) {
//		StoreOrderItemNew storeOrderItemNew = new StoreOrderItemNew();
//		storeOrderItemNew.setId(orderItemId);
//		storeOrderItemNew.setRefund_status(StoreOrderItemNew.NO_REFUND);
//		orderItemNewMapper.updateById(storeOrderItemNew);
//	}

	public List<StoreOrderItemNew> getItemListByOrderNo(long orderNo) {
		Wrapper<StoreOrderItemNew> wrapper = new EntityWrapper<StoreOrderItemNew>().eq("OrderNo", orderNo);
		return orderItemNewMapper.selectList(wrapper);
	}
	
	

}
