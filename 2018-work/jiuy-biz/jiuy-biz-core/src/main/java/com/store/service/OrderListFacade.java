package com.store.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.newentity.RefundOrder;
import com.store.dao.mapper.RefundMapperNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.dao.mapper.supplier.OrderNewLogMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.service.common.ILOWarehouseNewService;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.jiuyuan.util.CollectionUtil;
import com.store.dao.mapper.OrderItemMapper;
import com.store.entity.OrderProduct;
import com.store.entity.ShopStoreOrder;
import com.store.entity.ShopStoreOrderItem;
import com.store.entity.ShopStoreOrderItemNewVO;
import com.store.entity.ShopStoreOrderItemVO;
import com.store.entity.ShopStoreOrderVo;
import com.store.service.brand.ShopBrandService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class OrderListFacade {
	
	private static final Log logger = LogFactory.get();
	@Autowired
	private OrderItemMapper orderItemMapper;
	 
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private IProductNewService productNewService;
	@Autowired
	private ProductNewMapper productNewMapper;
	
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ShopGlobalSettingService globalSettingService;
	
	@Autowired
	private ShoppingCartFacade shoppingCartFacade;
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private StoreBusinessServiceShop storeBusinessService;
	
	@Autowired
	private ShopBrandService shopBrandService;
	
	@Autowired
	private ILOWarehouseNewService loWarehouseService;
	
	@Autowired
	private ProductServiceShop productService;
	
	@Autowired
	private LOLocationService loLocationService;
	
	@Autowired
	private LOPostageService loPostageService;
	
	@Autowired
	private ProductSKUService productSKUService;
	
	@Autowired
	private ProductPropAssemblerShop productPropAssembler;
	
	@Autowired
	private OrderNewLogMapper orderNewLogMapper;
	
	@Autowired(required = false)
    private List<OrderHandler> orderHandlers;
	
	@Autowired
	private ProductAssembler productAssembler;

	@Autowired
	private RefundMapperNew refundMapperNew;

	/**
	 * 获取子订单map :
	 * 		订单详情表 orderItem: {productId, money, buyCount, skuSnaphot, brandId, color, size}
     *	    商品表 product: {clothesNumber, productName, mainImg, brandName}
	 *
	 * @param userDetail
	 * @param orderNos
	 * @return
	 */
	public Map<Long, List<ShopStoreOrderItemNewVO>> getOrderNewItemVOMap(UserDetail userDetail, Collection<Long> orderNos,Long userId) {

    	//在OrderItem里获取具体的订单中的商品
    	List<ShopStoreOrderItem> orderItems = orderService.getOrderNewItems(userId, orderNos);
    	Map<Long, List<ShopStoreOrderItemNewVO>> result = new HashMap<> ();
    	for (ShopStoreOrderItem orderItem : orderItems) {
    		ShopStoreOrderItemNewVO vo = buildShopStoreOrderItemNewVO(orderItem);

    		long orderNo = orderItem.getOrderNo();
    		List<ShopStoreOrderItemNewVO> list = result.get(orderNo);
    		if (list == null) {
    			list = new ArrayList<>();
    			result.put(orderNo, list);
    		}
    		list.add(vo);
    	}
    	return result;
    }

	private ShopStoreOrderItemNewVO buildShopStoreOrderItemNewVO(ShopStoreOrderItem orderItem) {
		ShopStoreOrderItemNewVO vo = new ShopStoreOrderItemNewVO();
//		vo.setOrderId(orderItem.getOrderId());
		vo.setProductId(orderItem.getProductId());
		vo.setMoney(orderItem.getMoney());
		vo.setBuyCount(orderItem.getBuyCount());
		String skuSnapshot = orderItem.getSkuSnapshot ();
		vo.setSkuSnapshot(skuSnapshot);
		vo.setBrandId(orderItem.getBrandId());
		vo.setSizeStr(skuSnapshot.split("  ")[1].trim());
		vo.setColorStr(skuSnapshot.split("  ")[0].trim());
		//新添加的代码
		//获取到该商品的skuid
		long skuId = orderItem.getSkuId();
		//获取该商品的orderno
		long orderNo = orderItem.getOrderNo();
		RefundOrder refundOrder = refundMapperNew.selectRefundOrder(orderNo, skuId);
		if (refundOrder!=null){
			vo.setRefundStatus(refundOrder.getRefundStatus());//售后状态
//			'售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、
//			 6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)',
		}else {
			vo.setRefundStatus(0);//该订单没收售后信息,显示可以申请退款
		}
		//添加订单的状态
		ProductNew productNew = productNewMapper.selectById(orderItem.getProductId());
		if(productNew != null){
			String platformProductState = productNewService.getPlatformProductState(productNew);//  平台商品状态:0已上架、1已下架、2已删除
			vo.setPlatformProductState(platformProductState);
			vo.setClothesNumber(productNew.getClothesNumber());//商品款号
			vo.setProductName(productNew.getName());//	商品名称
			vo.setProductMainImg(productNew.getMainImg());//	商品主图
			vo.setBrandName(productNew.getBrandName());//	品牌名称
		}
		return vo;
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
	/*
	public List<ShopStoreOrderItemNewVO> getShopStoreOrderItemNewVO(UserDetail userDetail, Collection<Long> orderNOs) {
    	List<ShopStoreOrderItemNewVO> itemVoList = new ArrayList<ShopStoreOrderItemNewVO>();

    	List<ShopStoreOrderItem> itemList = orderItemMapper.getOrderNewItems(userDetail.getId(), orderNOs);
    	for (ShopStoreOrderItem orderItem : itemList) {
    		ShopStoreOrderItemNewVO vo = buildShopStoreOrderItemNewVO(orderItem);
    		itemVoList.add(vo);
    	}
    	return itemVoList;
    }


	public Map<Long, List<OrderProduct>> getOrderProductMap(UserDetail userDetail, Collection<Long> orderNOs) {

    	List<OrderProduct> orderProductList = orderService.getOrderProductsByOrderNoBatch(userDetail.getId(), orderNOs);
    	List<Product> productList = orderService.getProductsByOrderNoBatch(userDetail.getId(), orderNOs);
    	Map<Long, List<OrderProduct>> result = new HashMap<Long, List<OrderProduct>>();
    	for (OrderProduct orderProduct: orderProductList) {
    		for(Product product :productList){
    			if(product.getId() == orderProduct.getProductId()){
    				orderProduct.setProduct(product);
    			}
    		}
    		long orderNo = orderProduct.getOrderNo();
    		List<OrderProduct> list = result.get(orderNo);
    		if (list == null) {
    			list = new ArrayList<OrderProduct>();
    			result.put(orderNo, list);
    		}
    		list.add(orderProduct);
    	}
    	return result;
    }
	private void copyCopyProperties(ShopStoreOrderItemVO vo, ShopStoreOrderItem orderItem) {
		vo.setId(orderItem.getId());
		vo.setOrderNo(orderItem.getOrderNo());
		vo.setOrderId(orderItem.getOrderId());
		vo.setStoreId(orderItem.getStoreId());
		vo.setProductId(orderItem.getProductId());
		vo.setSkuId(orderItem.getSkuId());
		vo.setTotalMoney(orderItem.getTotalMoney());
		vo.setTotalExpressMoney(orderItem.getTotalExpressMoney());
		vo.setMoney(orderItem.getMoney());
		vo.setExpressMoney(orderItem.getExpressMoney());
		vo.setTotalUnavalCoinUsed(orderItem.getTotalUnavalCoinUsed());
		vo.setUnavalCoinUsed(orderItem.getUnavalCoinUsed());
		vo.setAfterSaleFlag(orderItem.getAfterSaleFlag());
		
		vo.setBuyCount(orderItem.getBuyCount());
		vo.setSkuSnapshot(orderItem.getSkuSnapshot());
		vo.setPosition(orderItem.getPosition());
		vo.setStatus(orderItem.getStatus());
		vo.setTotalPay(orderItem.getTotalPay());
		vo.setTotalMarketPrice(orderItem.getTotalMarketPrice());
		vo.setMarketPrice(orderItem.getMarketPrice());
	}*/
}
