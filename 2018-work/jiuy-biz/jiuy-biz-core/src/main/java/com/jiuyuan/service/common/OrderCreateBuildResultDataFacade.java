package com.jiuyuan.service.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuyuan.entity.newentity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * （开发完成）
 * app生成订单构建数据专用Facade
 */
@Service
public class OrderCreateBuildResultDataFacade{

	private static final Log logger = LogFactory.get(OrderCreateBuildResultDataFacade.class);
	@Autowired
	private IAddressNewService addressNewService;
	@Autowired
	private IStoreOrderNewService storeOrderNewService;
	@Autowired
	private IUserNewService supplierUserService;
	
	/**
	 * 统计计算费用并封装成Map返回前端
	 * @param restrictionActivityProductId 
	 * @return resultMap;
	 */
	public Map<String, Object> getOrderConfirmResultMap(List<StoreOrderNewVo> storeOrderNewVoList,long addressId,long storeId, long restrictionActivityProductId) {
		List<Map<String,Object>> orderList = new ArrayList<>();
		double waitPayTotalPrice = 0;//待付款总金额
		for(StoreOrderNewVo storeOrderNewVo : storeOrderNewVoList){
			//1、计算待支付总金额（去除优惠，包含邮费）
			waitPayTotalPrice = BigDecimal.valueOf(waitPayTotalPrice).add(BigDecimal.valueOf(storeOrderNewVo.getPayPrice())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			//2、组装显示商品订单信息
			Map<String,Object> orderMap = new HashMap<String,Object>();
			BrandNew brand = storeOrderNewVo.getBrand();//品牌
			orderMap.put("brandId", brand.getBrandId());//品牌ID
			orderMap.put("brandName", brand.getBrandName());//品牌名称
			orderMap.put("brandLogo", brand.getBrandIdentity());//品牌logo
			orderMap.put("deliveryTypeName", storeOrderNewVo.getDeliveryTypeName());//配送方式名称
			orderMap.put("freePostage", storeOrderNewVo.getFreePostage());//是否免邮，0(不免邮)、1(免邮)
			orderMap.put("freePostageName", storeOrderNewVo.getFreePostageName());//是否免邮名称，0(不免邮)、1(免邮)
			orderMap.put("postage", storeOrderNewVo.getPostage());//邮费
			orderMap.put("totalProductPrice",storeOrderNewVo.getTotalProductPrice());//商品总价格
			orderMap.put("payPrice", storeOrderNewVo.getPayPrice());//合计支付价格
			orderMap.put("matchWholesaleLimit", storeOrderNewVo.getMatchWholesaleLimit());//是否符合混批限制：0不符合、1符合

			try{
				ProductNewVo productNewVo = storeOrderNewVo.getProductNewVoList().get(0);
				ProductNew productNew = productNewVo.getProduct();
				orderMap.put("productId",productNew.getId().toString());
				orderMap.put("category",productNew.getCategoryId().toString());
				orderMap.put("supplierId",storeOrderNewVo.getSupplier().getId());
				orderMap.put("restrictionActivityProductId",restrictionActivityProductId);
			}catch (Exception e) {
			    e.printStackTrace();
			}

			 //4、获取订单商品列表
			 List<ProductNewVo>  productVoList = storeOrderNewVo.getProductNewVoList();
			 orderMap.put("productList", getProductMapList(productVoList));//商品列表
			 
			 orderList.add(orderMap);
		}
		
		//5、组拼返回数据
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("orderList", orderList);//订单列表
		resultMap.put("defAddressInfo", getDefAddressMap(storeId));//默认收货地址信息
		resultMap.put("payTypeName", "在线支付");//支付方式名称
		resultMap.put("waitPayTotalPrice", waitPayTotalPrice);//待付款总金额
		return resultMap;
	}
	
	/**
	 * 获取订单可选优惠券列表
	 * @return
	 */
	private List<Map<String,Object>> getCouponList(long storeId,long brandId,double totalProductPrice) {
		//1、获取可用优惠券列表
		logger.info("---------------获取可用优惠券列表: " + brandId);
		List<StoreCouponNew> counponlist = storeOrderNewService.availableCoupon(storeId,brandId,totalProductPrice);
		
		//2、组装返回数据
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(StoreCouponNew coupon : counponlist){
			Map<String,Object> couponMap = new HashMap<String,Object>();
			long couponId = coupon.getId();
			couponMap.put("couponId", String.valueOf(couponId));//优惠券ID
			couponMap.put("couponName", coupon.getTemplateName());//优惠券名称
			couponMap.put("couponPrice", coupon.getMoney());//优惠券面值
			list.add(couponMap);
		}
		return list;
	}
	/**
	 * 获取门店默认收货地址信息
	 * @param storeId
	 * @return
	 */
	private Map<String,String> getDefAddressMap(long storeId) {
		//1、获取用户默认收货地址信息
		AddressNew defAddress = addressNewService.getDefAddress(storeId);
		
		String addressId = "";
		String receiverName = "";
	    String telephone = "";
	    String addrFull = "";
		if(defAddress!= null){
			addressId = String.valueOf(defAddress.getAddrId());
			receiverName= defAddress.getReceiverName();//收件人姓名
			telephone = defAddress.getTelephone();//地址
			addrFull = defAddress.getAddrFull();//完整收货地址
		}
		
		//2、组装返回数据信息
		Map<String, String> defAddressMap = new HashMap<String,String>();
		defAddressMap.put("addressId", addressId);//收货地址ID
		defAddressMap.put("receiverName", receiverName);//收件人姓名
		defAddressMap.put("telephone", telephone);//联系人手机号
		defAddressMap.put("addrFull",addrFull);//收货地址
		return defAddressMap;
	}

	/**
	 * 获取商品列表
	 * @param
	 * @return
	 */
	private List<Map<String,Object>> getProductMapList(List<ProductNewVo>  productVoList ) {
	
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(ProductNewVo productVo :productVoList){
			Map<String,Object> productMap = new HashMap<String,Object>();
			//1、组装商品信息
			ProductNew product = productVo.getProduct();
			productMap.put("productId",product.getId());//商品主图，全路径
			productMap.put("productMainImg",product.getMainImg());//商品主图，全路径
			productMap.put("productName",   product.getName());//商品名称
			productMap.put("orderUnitPrice", productVo.getOrderUnitPrice());//订单单价
			productMap.put("clothesNumber",product.getClothesNumber());//款号ID
			productMap.put("buyCount",productVo.getBuyCount());//购买数量
			
			//2、组装商品SKU详细信息
			productMap.put("productSkuInfo",getProductSkuInfo(productVo));//商品SKU详细信息
			
			list.add(productMap);
		}
		return list;
	}

	/**
	 * 获取商品sku明细信息（用于点击商品查看商品SKU明细）
	 * @param productVo
	 * @return
	 */
	private Object getProductSkuInfo(ProductNewVo productVo) {
		//1、组装商品基本信息
		Map<String,Object> productSkuInfo = new HashMap<String,Object>();
		ProductNew product = productVo.getProduct();
		productSkuInfo.put("productId", product.getId());//商品ID（即款号ID）
		productSkuInfo.put("productName", product.getName());//商品名称
		productSkuInfo.put("mainImg", product.getMainImg());//商品主图
		productSkuInfo.put("clothesNumber", product.getClothesNumber());//款号
		productSkuInfo.put("unitPrice", productVo.getOrderUnitPrice());//购买商品单价
		productSkuInfo.put("productPrice", product.getMaxLadderPrice());//购买商品原来价格
		productSkuInfo.put("buyCount", productVo.getBuyCount());//购买数量
		
		//2、组装商品颜色和尺码列表
		Set<String> colorNameList = new HashSet<String>();
		Set<String> sizeNameList = new HashSet<String>();
		
		//3、组装商品SKU列表
		List<Map<String,Object>> productSkuList = new ArrayList<Map<String,Object>>();

		List<ProductSkuNewVo> productSkuNewVoList = productVo.getProductSkuNewVoList();
		for(ProductSkuNewVo productSkuNewVo : productSkuNewVoList){
			ProductSkuNew productSkuNew = productSkuNewVo.getProductSkuNew();
			long skuId = productSkuNew.getId();
			String sizeName = productSkuNew.getSizeName();
			String colorName = productSkuNew.getColorName();
			long buyCount = productSkuNewVo.getBuyCount();
			colorNameList.add(colorName);
			sizeNameList.add(sizeName);
			Map<String,Object> productSkuMap = new HashMap<String,Object>();
			productSkuMap.put("skuId", skuId);//SKUID
			productSkuMap.put("sizeName", sizeName);//尺寸名称
			productSkuMap.put("colorName", colorName);//颜色名称
			productSkuMap.put("buyCount", buyCount);//购买数量
			productSkuList.add(productSkuMap);
		}

		productSkuInfo.put("colorNameList", colorNameList);//颜色名称列表
		productSkuInfo.put("sizeNameList", sizeNameList);//尺码名称列表
		productSkuInfo.put("productSkuList", productSkuList);//商品SKU信息列表
		return productSkuInfo;
	}

	public Map<String,Object> getSendGoodsOrderConfirmResultMap(List<StoreOrderNewVo> storeOrderNewVoList,ShopMemberOrder shopOrder) {
		List<Map<String,Object>> orderList = new ArrayList<>();
		double waitPayTotalPrice = 0;//待付款总金额
		for(StoreOrderNewVo storeOrderNewVo : storeOrderNewVoList){
			//1、计算待支付总金额（去除优惠，包含邮费）
			waitPayTotalPrice = BigDecimal.valueOf(waitPayTotalPrice).add(BigDecimal.valueOf(storeOrderNewVo.getPayPrice())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			//2、组装显示商品订单信息
			Map<String,Object> orderMap = new HashMap<>(16);
			BrandNew brand = storeOrderNewVo.getBrand();//品牌
			orderMap.put("brandId", brand.getBrandId());//品牌ID
			orderMap.put("brandName", brand.getBrandName());//品牌名称
			orderMap.put("brandLogo", brand.getBrandIdentity());//品牌logo
			orderMap.put("deliveryTypeName", storeOrderNewVo.getDeliveryTypeName());//配送方式名称
			orderMap.put("freePostage", storeOrderNewVo.getFreePostage());//是否免邮，0(不免邮)、1(免邮)
			orderMap.put("freePostageName", storeOrderNewVo.getFreePostageName());//是否免邮名称，0(不免邮)、1(免邮)
			orderMap.put("postage", storeOrderNewVo.getPostage());//邮费
			orderMap.put("totalProductPrice",storeOrderNewVo.getTotalProductPrice());//商品总价格
			orderMap.put("payPrice", storeOrderNewVo.getPayPrice());//合计支付价格
			orderMap.put("matchWholesaleLimit", storeOrderNewVo.getMatchWholesaleLimit());//是否符合混批限制：0不符合、1符合

			try{
				ProductNewVo productNewVo = storeOrderNewVo.getProductNewVoList().get(0);
				ProductNew productNew = productNewVo.getProduct();
				orderMap.put("productId",productNew.getId().toString());
				orderMap.put("category",productNew.getCategoryId().toString());
				orderMap.put("supplierId",storeOrderNewVo.getSupplier().getId());
				orderMap.put("restrictionActivityProductId",0);
			}catch (Exception e) {
				e.printStackTrace();
			}

			//4、获取订单商品列表
			List<ProductNewVo>  productVoList = storeOrderNewVo.getProductNewVoList();
			orderMap.put("productList", getProductMapList(productVoList));//商品列表
			orderList.add(orderMap);
		}

		//5、组拼返回数据
		Map<String,Object> resultMap = new HashMap<>(4);
		resultMap.put("orderList", orderList);//订单列表

		Map<String, String> defAddressMap = new HashMap<>(4);
		defAddressMap.put("addressId", "0");//收货地址ID
		defAddressMap.put("receiverName", shopOrder.getReceiverName());//收件人姓名
		defAddressMap.put("telephone", shopOrder.getReceiverPhone());//联系人手机号
		defAddressMap.put("addrFull",shopOrder.getReceiverAddress());//收货地址
		resultMap.put("defAddressInfo", defAddressMap);//默认收货地址信息

		resultMap.put("payTypeName", "在线支付");//支付方式名称
		resultMap.put("waitPayTotalPrice", waitPayTotalPrice);//待付款总金额
		return resultMap;
	}
}