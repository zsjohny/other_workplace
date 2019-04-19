package com.jiuyuan.service.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyuan.common.CouponRbRef;
import com.jiuyuan.dao.mapper.CommonRefMapper;
import com.jiuyuan.dao.mapper.supplier.StoreCouponNewMapper;
import com.exceptions.NonMemberException;
import com.jiuyuan.constant.SystemPlatform;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.service.common.express.IExpressModelService;
import com.yujj.entity.product.YjjMember;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductMapper;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductSkuMapper;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * app生成订单构建数据专用Facade（开发完成）
 */
@Service
public class OrderCreateBuildDataFacade{

	private static final Log logger = LogFactory.get(OrderCreateBuildDataFacade.class);
	@Autowired
	private ProductSkuNewMapper productSkuNewMapper;
	@Autowired
	private IProductNewService productNewService;
	@Autowired
	private IBrandNewService brandNewService;
	@Autowired
	private IUserNewService userNewService;
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private OrderNewLogMapper orderNewLogMapper;
//	@Autowired
//	private SupplierOrderMapper supplierOrderMapper;
//	@Autowired(required = false)
//    private List<OrderHandler> orderHandlers;
//	@Autowired
//	private SupplierOrderItemMapper supplierOrderItemMapper;
//	@Autowired
//	private StoreOrderProductMapper storeOrderProductMapper;
//	@Autowired
//	private OrderService orderService;
//	@Autowired
//	private ShoppingCartService shoppingCartService;
	@Autowired
	private IAddressNewService addressNewService;
	@Autowired
	private IStoreOrderNewService storeOrderNewService;
    @Autowired
	private IUserNewService supplierUserService;
    @Autowired
    private IStoreCouponNewService storeCouponNewService;
    
    @Autowired
    private RestrictionActivityProductSkuMapper restrictionActivityProductSkuMapper;
    
    @Autowired
    private RestrictionActivityProductMapper restrictionActivityProductMapper;

    @Autowired
	private IYjjMemberService yjjMemberService;

    @Autowired
	private IExpressModelService expressModelService;

	@Autowired
	private CommonRefMapper commonRefMapper;

	/**
	 * 统计计算费用并封装成Map返回前端
	 * @return resultMap;
	 */
	public Map<String, String> checkWholesaleLimit(List<StoreOrderNewVo> storeOrderNewVoList) {
		Map<String,String> resultMap = new HashMap<String,String>();
		if(storeOrderNewVoList.size() == 1){//单个品牌
			StoreOrderNewVo order = storeOrderNewVoList.get(0);
			int totalBuyCount = order.getTotalBuyCount();//总购买件数
			double totalProductPrice = order.getTotalProductPrice();//商品总价格（所有商品的商品总价相加（商品总价=商品单价*购买数量））不包含邮费 订单金额原价总价，不包含邮费
			//先判定各个商品的起订量
			List<ProductNewVo> productNewVoList = order.getProductNewVoList();
			for(ProductNewVo productNewVo :productNewVoList){
				String ladderPriceJson = productNewVo.getProduct().getLadderPriceJson();
				JSONArray ladderPriceJsonJSONArray = JSON.parseArray(ladderPriceJson);
				Object object = ladderPriceJsonJSONArray.get(0);
				JSONObject ladderPriceJSONObject1 = (JSONObject)object;
				int count = ladderPriceJSONObject1.getIntValue("count");
				int buyCount = productNewVo.getBuyCount();
				logger.info("订单确认");
				logger.info("count:"+count+",buyCount:"+buyCount);
				if(buyCount < count){
					throw new RuntimeException(productNewVo.getProduct().getName()+"最少"+count+"件起订");
				}
				
			}
			//在判断各个品牌的混批设置
			BrandNew brand = order.getBrand();
			UserNew supplierUser = supplierUserService.getSupplierByBrandId(brand.getBrandId());
			int wholesaleCount = supplierUser.getWholesaleCount();//批发起定量 
			double wholesaleCost = supplierUser.getWholesaleCost();//批发起定额
			if(wholesaleCount==0 && wholesaleCost==0){//没有设置，则认为符合混批设置
//				order.setBuyType(StoreOrderNewVo.buyType_common);//购买方式：0常规购买、1混批购买、2活动购买
				order.setMatchWholesaleLimit(0); 
				resultMap.put("matchWholesaleLimit", "1");//是否符合混批限制：0不符合、1符合
				resultMap.put("matchBrands", "");//符合限制条件品牌
				resultMap.put("noMatchBrand", "");//不符合限制条件品牌
				resultMap.put("matchBrandSkuIdCounts", "");//符合限制条件品牌商品skuId数量数组  skuId:数量_skuId:数量
			}else if(totalBuyCount < wholesaleCount){
				throw new RuntimeException(brand.getBrandName()+"最少"+wholesaleCount+"件成单");//xx品牌 最少 5件 成单
			}else if(totalProductPrice < wholesaleCost){
				throw new RuntimeException(brand.getBrandName()+"最少￥"+wholesaleCost+"元成单");//xx品牌 最少 ￥500元 成单
			}else{
//				order.setBuyType(StoreOrderNewVo.buyType_WholesaleLimit);//购买方式：0常规购买、1混批购买、2活动购买
				order.setMatchWholesaleLimit(1);
				resultMap.put("matchWholesaleLimit", "1");//是否符合混批限制：0不符合、1符合
			}
		}else{
			// 校验混批限制、10件、￥1000元 起订
			String  matchBrands = "";//符合限制条件品牌，品牌名称 ，品牌名称2，品牌名称N
			String  noMatchBrand = "";//不符合限制条件品牌，品牌名称 ，品牌名称2，品牌名称N
			String matchBrandSkuIdCounts = "";//符合限制条件品牌商品skuId数量数组  skuId:数量_skuId:数量
			//如果是多个品牌则统计符合和不符合分别统计提示
//			String wholesaleLimit = "0";//是否符合混批限制：0不符合、1符合
			for(StoreOrderNewVo order : storeOrderNewVoList){
				int totalBuyCount = order.getTotalBuyCount();//总购买件数
				double totalProductPrice = order.getTotalProductPrice();//商品总价格（所有商品的商品总价相加（商品总价=商品单价*购买数量））不包含邮费 订单金额原价总价，不包含邮费
				//先判定各个商品的起订量
				List<ProductNewVo> productNewVoList = order.getProductNewVoList();
				for(ProductNewVo productNewVo :productNewVoList){
					String ladderPriceJson = productNewVo.getProduct().getLadderPriceJson();
					JSONArray ladderPriceJsonJSONArray = JSON.parseArray(ladderPriceJson);
					Object object = ladderPriceJsonJSONArray.get(0);
					JSONObject ladderPriceJSONObject1 = (JSONObject)object;
					int count = ladderPriceJSONObject1.getIntValue("count");
					int buyCount = productNewVo.getBuyCount();
					logger.info("订单确认");
					logger.info("count:"+count+",buyCount:"+buyCount);
					if(buyCount < count){
						throw new RuntimeException(productNewVo.getProduct().getName()+"最少"+count+"件起订");
					}
				}
				//在判断各个品牌的混批设置
				BrandNew brand = order.getBrand();
				int wholesaleLimitState = checkWholesaleLimit(brand.getBrandId(),totalBuyCount,totalProductPrice);
				if(wholesaleLimitState== -1 || wholesaleLimitState== 1 ){
					if(wholesaleLimitState== -1){
						order.setMatchWholesaleLimit(0);
//						wholesaleLimit = "1";
//						order.setBuyType(StoreOrderNewVo.buyType_common);//购买方式：0常规购买、1混批购买、2活动购买
					}else if(wholesaleLimitState== 1){
						order.setMatchWholesaleLimit(1);
//						order.setBuyType(StoreOrderNewVo.buyType_WholesaleLimit);//购买方式：0常规购买、1混批购买、2活动购买
//						order.setBuyType(StoreOrderNewVo.buyType_common);//购买方式：0常规购买、1混批购买、2活动购买
//						wholesaleLimit = "1";
					}
					
					//拼接符合条件品牌，品牌名称 ，品牌名称2，品牌名称N
					if(matchBrands.length() >0){
						matchBrands = matchBrands +  "，";
					}
					matchBrands = matchBrands + brand.getBrandName();
					
					//拼接符合条件的skuId
					List<ProductNewVo> ProductNewVoList = order.getProductNewVoList();
					for(ProductNewVo productNewVo : ProductNewVoList ){
						List<ProductSkuNewVo> productSkuNewVoList = productNewVo.getProductSkuNewVoList();
						for(ProductSkuNewVo productSkuNewVo : productSkuNewVoList){
							int buyCount = productSkuNewVo.getBuyCount();
							ProductSkuNew productSkuNew = productSkuNewVo.getProductSkuNew();
							long skuId = productSkuNew.getId();
							if(matchBrandSkuIdCounts.length() >0){
								matchBrandSkuIdCounts = matchBrandSkuIdCounts + "_";
							}
							matchBrandSkuIdCounts = matchBrandSkuIdCounts + skuId+":"+buyCount;
						}
					}
					
				}else{
					order.setMatchWholesaleLimit(0);
					//拼接符合条件品牌，品牌名称 ，品牌名称2，品牌名称N
					if(noMatchBrand.length() >0){
						noMatchBrand = noMatchBrand +  "，";
					}
					noMatchBrand = noMatchBrand + brand.getBrandName();
				}
			}
			if(matchBrands.length() == 0){//没有符合的
				throw new RuntimeException("所选待结算商品均不满足品牌混批条件");
			}else if(noMatchBrand.length() >0 ){ //有符合但也有不符合的
				resultMap.put("matchWholesaleLimit", "0");//是否符合混批限制：0不符合、1符合
//				resultMap.put("matchWholesaleLimit", wholesaleLimit);//是否符合混批限制：0不符合、1符合
				resultMap.put("matchBrands", matchBrands);//符合限制条件品牌
				resultMap.put("noMatchBrand", noMatchBrand);//不符合限制条件品牌
				resultMap.put("matchBrandSkuIdCounts", matchBrandSkuIdCounts);//符合限制条件品牌商品skuId数量数组  skuId:数量_skuId:数量
			}else{//有符合没有不符合则全部符合通过
				logger.info("有符合没有不符合则全部符合通过");
				resultMap.put("matchWholesaleLimit", "1");//是否符合混批限制：0不符合、1符合
//				resultMap.put("matchWholesaleLimit", wholesaleLimit);//是否符合混批限制：0不符合、1符合
				resultMap.put("matchBrands", matchBrands);//符合限制条件品牌
				resultMap.put("noMatchBrand", noMatchBrand);//不符合限制条件品牌
				resultMap.put("matchBrandSkuIdCounts", matchBrandSkuIdCounts);//符合限制条件品牌商品skuId数量数组  skuId:数量_skuId:数量
			}
		}
		
		
		return resultMap;
	}
	
	/**
	 * 校验是否符合混批限制条件
	 * @param brandId
	 * @param totalBuyCount
	 * @param totalProductPrice
	 * @return -1未设置  0不符合  1符合
	 */
	private int checkWholesaleLimit(long brandId,int totalBuyCount,double totalProductPrice) {
		UserNew supplierUser = supplierUserService.getSupplierByBrandId(brandId);
		int wholesaleCount = supplierUser.getWholesaleCount();//批发起定量 
		Double wholesaleCost = supplierUser.getWholesaleCost();//批发起定额
		if(wholesaleCount==0 && wholesaleCost==0){//没有设置
			return -1;
		}
		if(totalBuyCount < wholesaleCount){
			return 0;
		}
		if(totalProductPrice < wholesaleCost){
			return 0;
		}
		return 1;
	}

	/**
	 * 组装订单数据
	 * @param brandIdCouponId 品牌和使用的优惠券Id对应关系，未使用优惠券则优惠券为0，品牌ID:优惠ID_品牌ID:优惠ID,例：223:1_224:2
	 * @param restrictionActivityProductId 
	 * @param storeId 
	 * @return
	 */
	public List<StoreOrderNewVo> buildOrderCreateData(String skuIdCount,String brandIdCouponId,long addressId, long restrictionActivityProductId, long storeId ) {
		//1、对sku进行商品分组（开发完成）
		Map<Long, ProductNewVo> productVoObjMap = null;
		if(restrictionActivityProductId>0){
			productVoObjMap = getRestrictionActivityProductVoObjMap(skuIdCount,restrictionActivityProductId,storeId);
		}else {
			try {
				productVoObjMap = getProductVoObjMap(skuIdCount,storeId);
			}catch (NonMemberException e){
				if (e.getErrorCode().equals("nonMember")){
					throw new NonMemberException(e.getErrorCode(),e.getMessage());
				}
			}

		}
//		logger.info("1、对sku进行商品分组完成，productVoObjMap:"+JSON.toJSONString(productVoObjMap));
		
		//2、校验数据（开发完成）
		checkData(productVoObjMap,restrictionActivityProductId);
		
		logger.info("2、校验数据完成");
		
		//3、组装订单对象列表
		List<StoreOrderNewVo>  storeOrderNewVoList = getStoreOrderNewVoList(productVoObjMap,brandIdCouponId,addressId,restrictionActivityProductId,storeId);
//		logger.info("3、组装订单对象列表storeOrderNewVoList:"+storeOrderNewVoList);
		
		return storeOrderNewVoList;
	}




	public List<StoreOrderNewVo> buildOrderCreateData(String skuIdCount,String brandIdCouponId,String provinceOfAddress, long restrictionActivityProductId, long storeId ) {
		//1、对sku进行商品分组（开发完成）
		Map<Long, ProductNewVo> productVoObjMap = null;
		if(restrictionActivityProductId>0){
			productVoObjMap = getRestrictionActivityProductVoObjMap(skuIdCount,restrictionActivityProductId,storeId);
		}else {
			try {
				productVoObjMap = getProductVoObjMap(skuIdCount,storeId);
			}catch (NonMemberException e){
				if (e.getErrorCode().equals("nonMember")){
					throw new NonMemberException(e.getErrorCode(),e.getMessage());
				}
			}

		}
//		logger.info("1、对sku进行商品分组完成，productVoObjMap:"+JSON.toJSONString(productVoObjMap));

		//2、校验数据（开发完成）
		checkData(productVoObjMap,restrictionActivityProductId);

		logger.info("2、校验数据完成");

		//3、组装订单对象列表
		List<StoreOrderNewVo>  storeOrderNewVoList = getStoreOrderNewVoListWithProvince(productVoObjMap,brandIdCouponId,provinceOfAddress,restrictionActivityProductId,storeId);
//		logger.info("3、组装订单对象列表storeOrderNewVoList:"+storeOrderNewVoList);

		return storeOrderNewVoList;
	}

	private List<StoreOrderNewVo> getStoreOrderNewVoListWithProvince(Map<Long,ProductNewVo> productVoObjMap, String brandIdCouponId, String provinceOfAddress, long restrictionActivityProductId, long storeId) {

		//1、获取获取品牌和使用优惠券ID对应关系
		Map<Long, Long> brandIdCouponIdMap  = null;
		//活动商品不使用优惠券
		if(restrictionActivityProductId>0){
			brandIdCouponIdMap = new HashMap<>();
		}else{
			brandIdCouponIdMap = buildBrandIdCouponIdMap(brandIdCouponId);
		}

		//2、根据仓库进行商品分组（key:仓库ID，value:该仓库下购买商品信息列表）
		Map<Long, List<ProductNewVo>> lowarehouseObjMap = new HashMap<Long, List<ProductNewVo>>();
		for (long productId : productVoObjMap.keySet()) {
			ProductNewVo productNewVo = productVoObjMap.get(productId);
//			long brandId = productNewVo.getBrandId();
//			BrandNew brand = productNewVo.getBrand();//品牌
			//供应商
			UserNew supplier = productNewVo.getSupplier();
			if(supplier == null){
				logger.info("供应商为空，请排查问题，productNewVo"+JSON.toJSONString(productNewVo));
			}
			//仓库ID
			long lowarehouseId = supplier.getLowarehouseId();

			//获取仓库对应的商品列表
			List<ProductNewVo> productVoList = lowarehouseObjMap.get(lowarehouseId);
			if(productVoList == null){
				List<ProductNewVo> productNewVoList = new ArrayList<ProductNewVo>();
				productNewVoList.add(productNewVo);
				lowarehouseObjMap.put(lowarehouseId,productNewVoList);
			}else{
				productVoList.add(productNewVo);
			}
		}

		//3、组装门店订单VO
		List<StoreOrderNewVo> storeOrderNewVoList = new ArrayList<StoreOrderNewVo>();
		for(long lowarehouseId : lowarehouseObjMap.keySet()){

			//供应商
			UserNew supplier = userNewService.getSupplierByLowarehouseId(lowarehouseId);
			long brandId = supplier.getBrandId();
			//品牌
			BrandNew brand = userNewService.getSupplierBrandInfo(brandId);

			List<ProductNewVo> productNewVoList = lowarehouseObjMap.get(lowarehouseId);

			//1、计算订单总购买量、订单商品总价格
			//获取该仓库订单商品总价格（订单中所有商品单价*数量）
			double totalProductPrice = 0;
			//订单总购买数量
			int totalBuyCount = 0;

			StringBuilder skuInfo  = new StringBuilder();
			for(ProductNewVo productNewVo : productNewVoList){
				int bugCount = productNewVo.getBuyCount();
				totalBuyCount = totalBuyCount + bugCount;
				//购买单价
				double orderUnitPrice = productNewVo.getOrderUnitPrice();
				//商品总价格
//				totalProductPrice = totalProductPrice + orderUnitPrice * bugCount;
				BigDecimal totalMoney = BigDecimal.valueOf(orderUnitPrice).multiply(BigDecimal.valueOf(bugCount));
				totalProductPrice = BigDecimal.valueOf(totalProductPrice).add(totalMoney).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
				// 3_1,2_1,3_1
				List<ProductSkuNewVo> productSkuNewVos = productNewVo.getProductSkuNewVoList();
				if(productSkuNewVos!=null && productSkuNewVos.size()>0) {
					for (ProductSkuNewVo productSkuNewVo : productSkuNewVos) {
						skuInfo.append(productSkuNewVo.getProductSkuNew().getId()).append("_").append(productSkuNewVo.getBuyCount()).append(",");
					}
					skuInfo.delete(skuInfo.length()-1,skuInfo.length());
				}
			}
			//2、获得优惠券抵扣价格（优惠券抵扣）
			double couponOffsetPrice = 0;
			CouponRbRef coupon = null;
			if(restrictionActivityProductId<1){
				Long couponId = brandIdCouponIdMap.get(brandId);
				if(couponId != null){
					coupon = commonRefMapper.selectCoupon(couponId,1);
					if(coupon != null){
						// TODO v3.0 做可用校验 （有效期内、是该用户的，未使用的）
						BigDecimal price = coupon.getPrice();
						BigDecimal discount = coupon.getDiscount().divide(BigDecimal.TEN);
						if(discount!=null  && discount.compareTo(BigDecimal.ZERO)>0) {
							couponOffsetPrice = BigDecimal.ONE.subtract(discount).multiply(new BigDecimal(totalProductPrice)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
						} else if (price !=null && price.compareTo(BigDecimal.ZERO)>0) {
							couponOffsetPrice = coupon.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						}
						coupon.setSubMoney(new BigDecimal(couponOffsetPrice));
					}
				}
			}

			//3、计算优惠后商品总价格（商品总价格 -优惠券价格-供应商改价差额）
//			double totalProductPriceAfterCoupon = totalProductPrice - couponOffsetPrice;
			double totalProductPriceAfterCoupon = BigDecimal.valueOf(totalProductPrice).subtract(BigDecimal.valueOf(couponOffsetPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()  ;
			//为负数则设置为0
			if(totalProductPriceAfterCoupon < 0){
				totalProductPriceAfterCoupon = 0;
			}

			//4、邮费等 ;//邮费
			double postage = 0;
			if(skuInfo.length()>0) {
				if(StringUtils.isNotBlank(provinceOfAddress)) {
					postage = expressModelService.countOrderExpressMoneyPovinceName(
							provinceOfAddress,
							skuInfo.toString(),
							null
					).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
			}
			//订单支付价格（优惠后商品总价格+邮费）
			double  payPrice = BigDecimal.valueOf(totalProductPriceAfterCoupon).add(BigDecimal.valueOf(postage)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			//是否免邮，0(不免邮)、1(免邮)
			int freePostage = 1;
			//是否免邮名称，0(不免邮)、1(免邮)
			String freePostageName = "免邮";
			//配送方式名称
			String deliveryTypeName = "快递";

			//5、创建订单Vo对象
			StoreOrderNewVo storeOrderNewVo = new StoreOrderNewVo();
//			storeOrderNewVo.setOrderNo(Long.parseLong(DateUtil.format(time, "HHmmssSSS") + RandomStringUtils.randomNumeric(8)));//yyyyMMddHHmmssSSS
			//供应商
			storeOrderNewVo.setSupplier(supplier);
			//品牌
			storeOrderNewVo.setBrand(brand);
			if(restrictionActivityProductId<1){
				//优惠券
				storeOrderNewVo.setCoupon(coupon);
				//优惠券抵扣价格（优惠券抵扣）
				storeOrderNewVo.setCouponOffsetPrice(couponOffsetPrice);
				//TODO 3.5 2018-02-26 改价功能添加新的字段,平台优惠，商家店铺总优惠，商家店铺优惠，商家改价差额，订单金额优惠后原始待付款价格，不包含邮费
				if(coupon == null|| coupon.getPublishUserId() == 0){
					//TODO 3.5 2018-02-26 平台优惠
					storeOrderNewVo.setPlatformTotalPreferential(couponOffsetPrice);
				}else{
					//3.6 2018-03-19  商家优惠，目前都为0
					storeOrderNewVo.setSupplierPreferential(couponOffsetPrice);
					//3.6 2018-03-19 商家总优惠，目前为0
					storeOrderNewVo.setSupplierTotalPreferential(couponOffsetPrice);
					//3.6 2018-03-19  商家改价差额
					storeOrderNewVo.setSupplierChangePrice(0.0);
				}
			}
//			storeOrderNewVo.setAddress(address);//收货地址
			//配送方式名称
			storeOrderNewVo.setDeliveryTypeName(deliveryTypeName);
			//是否免邮，0(不免邮)、1(免邮)
			storeOrderNewVo.setFreePostage(freePostage);
			//是否免邮名称，0(不免邮)、1(免邮)
			storeOrderNewVo.setFreePostageName(freePostageName);
			//邮费
			storeOrderNewVo.setPostage(postage);
			//订单总购买数量
			storeOrderNewVo.setTotalBuyCount(totalBuyCount);
			storeOrderNewVo.setOriginalPrice(totalProductPriceAfterCoupon);
			//商品总价格（所有商品的商品总价相加（商品总价=商品单价*购买数量））不包含邮费
			storeOrderNewVo.setTotalProductPrice(totalProductPrice);
			//优惠后商品总价格（商品总价格 -优惠券价格-改价差额）
			storeOrderNewVo.setTotalProductPriceAfterCoupon(totalProductPriceAfterCoupon);
			//订单支付价格（优惠后商品总价格+邮费）
			storeOrderNewVo.setPayPrice(payPrice);
			//订单商品VO列表
			storeOrderNewVo.setProductNewVoList(productNewVoList);
			storeOrderNewVoList.add(storeOrderNewVo);
		}
		return storeOrderNewVoList;

	}


	/**
	 * sku进行商品分组
	 * 并做了校验:
	 * 	1.购买数量 大于起订量,小于限购量
	 *  2.商品状态 已上架
	 *  3.活动时间 在活动时间范围内
	 * update by Charlie(唐静) 2018-5-28 12:53:49 version 3.7.5
	 * @param skuIdCount
	 * @param restrictionActivityProductId
	 * @param storeId
	 */
	private Map<Long, ProductNewVo> getRestrictionActivityProductVoObjMap(String skuIdCount,
			long restrictionActivityProductId, long storeId) {
		//获取活动商品和对应的sku信息
		Wrapper<RestrictionActivityProductSku> wrapper = new EntityWrapper<RestrictionActivityProductSku>().eq("activity_product_id", restrictionActivityProductId);
		List<RestrictionActivityProductSku> restrictionActivityProductList = restrictionActivityProductSkuMapper.selectList(wrapper);
		RestrictionActivityProduct actProduct = restrictionActivityProductMapper.selectById(restrictionActivityProductId);

		//活动商品状态
		int activityProductStatus = actProduct.getProductStatus();
		if(activityProductStatus != RestrictionActivityProduct.on_the_shelves){
			//商品状态不为上架状态
			throw new RuntimeException(ResultCode.PRODUCT_UNAVAILABLE.getDesc());
		}

		//活动开始时间
		long activityBeginTime = actProduct.getActivityBeginTime();
		if(activityBeginTime>System.currentTimeMillis()){
			//该活动还未开始
			throw new RuntimeException(ResultCode.ORDER_ERROR_RESTRICTION_ACTIVITY_NOT_BEGIN.getDesc());
		}

		//活动结束时间
		long activityEndTime = actProduct.getActivityEndTime();
		if(activityEndTime < System.currentTimeMillis()){
			//该活动已经结束
			throw new RuntimeException(ResultCode.ORDER_ERROR_RESTRICTION_ACTIVITY_IS_END.getDesc());
		}
		
		/* 校验用户是否可以买该商品 大于起订量, 小于限购量 */
		//供应商预定义限购量
		int restrictionCount = actProduct.getRestrictionCount();
		//已购买量
		int restrictionActivityProductAllBuyCount = storeOrderNewService.getRestrictionActivityProductAllBuyCount(restrictionActivityProductId,storeId);
		//实际购买量 = 活动商品预定义限购数 - 用户已买过数量
		int realPurchaseCount = restrictionCount - restrictionActivityProductAllBuyCount;

		//1.1、将SkuId和购买数量解析成Map
		Map<Long, Integer> skuIdCountMap = buildSkuIdCount(skuIdCount, realPurchaseCount, actProduct.getMiniPurchaseCount());
		logger.info("1.1、将SkuId和购买数量解析成Map完成，,skuIdCountMap:"+JSON.toJSONString(skuIdCountMap));
		
		//1.2、SKU对象列表（测试完成）
		List<ProductSkuNewVo> productSkuNewVoList = getRestrictionActivityProductSkuVoList(skuIdCountMap,restrictionActivityProductList);
		logger.info("1.2、SKU对象列表完成，productSkuNewVoList:"+productSkuNewVoList);
		
		//1.3、对sku进行商品分组
		Map<Long, ProductNewVo> productVoObjMap = new HashMap<Long, ProductNewVo>();
		for (ProductSkuNewVo productSkuNewVo : productSkuNewVoList) {
			ProductSkuNew productSku = productSkuNewVo.getProductSkuNew();
			long productId = productSku.getProductId();
			
			ProductNewVo productNewVo = productVoObjMap.get(productId);
			if(productNewVo== null){//如果不存在则新建商品信息
				ProductNew newProduct = productNewService.getProductById(productId);//商品信息
				//如果是活动商品，将标题和价格替换掉
				newProduct.setName(actProduct.getProductName());//活动商品标题
				newProduct.setMinLadderPrice(actProduct.getActivityProductPrice());//活动价格
				newProduct.setMaxLadderPrice(actProduct.getProductPrice());//商品原来价格
				long brandId = newProduct.getBrandId();
				BrandNew brand = brandNewService.getBrandByBrandId(brandId);//品牌
				UserNew supplier = userNewService.getSupplierByBrandId(brandId);//品牌对应供应商
				
				List<ProductSkuNewVo> skuVoList = new ArrayList<ProductSkuNewVo>();//商品SKU列表
				skuVoList.add(productSkuNewVo);	
				
				int buyCount = productSkuNewVo.getBuyCount();
				ProductNewVo newProductNewVo = new ProductNewVo( productId, newProduct,brandId,brand,supplier, skuVoList, buyCount);
				productVoObjMap.put(productId, newProductNewVo);
			}else{//如果存在
				//增加商品购买数量
				productNewVo.addBuyCount(productSkuNewVo.getBuyCount());
				//添加商品SKU信息列表
				productNewVo.getProductSkuNewVoList().add(productSkuNewVo);
			}
		}
		
		//根据购买数量计算商品单价
		for(long productId : productVoObjMap.keySet()){
			ProductNewVo productVo = productVoObjMap.get(productId);
			ProductNew product = productVo.getProduct();
//			int buyCount = productVo.getBuyCount();
//			String ladderPriceJson = product.getLadderPriceJson();
//			double orderUnitPrice = ProductNew.buildCurrentLadderPriceByBuyCount(ladderPriceJson,buyCount);//商品单价
			double orderUnitPrice = product.getMinLadderPrice();//商品单价
			productVo.setOrderUnitPrice(orderUnitPrice);
		}
		logger.info("1.3、对sku进行商品分组完成，productVoObjMap:"+productVoObjMap);
		return productVoObjMap;
	}
	

	/**
	 * 将SkuId和购买数量解析成Map
	 * 	并判断用户购买数量是否在允许范围内
	 * @param skuIdCount
	 * @param ceilingCount 实际还能购买量
	 * @param floorCount 最小购买数量
	 * @return java.util.Map
	 * update by Charlie(唐静) 2018-5-28 12:27:13 version 3.7.5 ---> 新增约束条件, 最小购买量/起订量
	 */
	private Map<Long, Integer> buildSkuIdCount(String skuIdCount, int ceilingCount, int floorCount) {
		int totalBuyCount = 0;
		if(StringUtils.isBlank(skuIdCount)){
			logger.info("SKU对应购买数量字符串不能为空，请排查问题");
			throw new RuntimeException("SKU对应购买数量字符串不能为空，请排查问题！");
		}


		Map<Long, Integer> skuIdCountMap = new HashMap<Long, Integer>();
		//1、用下杠_进行第一次分隔
		String[] skuIdCountArr = skuIdCount.split("_");
		for(String skuIdCountStr : skuIdCountArr){
			//1、用冒号:进行第二次分隔
			String[] skuIdCountItemArr = skuIdCountStr.split(":");
			long skuId = Long.parseLong(skuIdCountItemArr[0]);
			int buyCount = Integer.parseInt(skuIdCountItemArr[1]);
			//购买数量大于0则参与计算
			if(buyCount > 0 ){
				  skuIdCountMap.put(skuId,buyCount);
			}
			totalBuyCount += buyCount;
		}


		if(totalBuyCount > ceilingCount){
			logger.info("购买数量大于限购活动商品的限购数量");
			throw new RuntimeException("购买数量大于限购活动商品的限购数量");
		}

		if (floorCount > totalBuyCount) {
			logger.info("购买数量小于限购活动商品的起订数量");
			throw new RuntimeException("购买数量小于限购活动商品的起订数量");
		}

		return skuIdCountMap;
	}

	private List<ProductSkuNewVo> getRestrictionActivityProductSkuVoList(Map<Long, Integer> skuIdCountMap, List<RestrictionActivityProductSku> restrictionActivityProductList) {
		//1、获取sku列表
		List<Long> skuIdList = new ArrayList<>();
		skuIdList.addAll(skuIdCountMap.keySet());
		List<ProductSkuNew> skuList = productSkuNewMapper.selectBatchIds(skuIdList);
		Map<Long,Integer> restrictionActivityProductSkuCountMap = new HashMap<>();
		//获取限购活动商品的库存
		for (RestrictionActivityProductSku restrictionActivityProductSku : restrictionActivityProductList) {
			restrictionActivityProductSkuCountMap.put(restrictionActivityProductSku.getProductSkuId(), restrictionActivityProductSku.getRemainCount());
		}
		
		//2、组装sku对象列表
		List<ProductSkuNewVo> productSkuNewVoList = new ArrayList<>();
		for(ProductSkuNew productSku : skuList){
			long skuId = productSku.getId();
			//替换限购活动商品的库存
			int count = restrictionActivityProductSkuCountMap.get(skuId);

			productSku.setRemainCount(count);
			int buyCount = skuIdCountMap.get(skuId);
			ProductSkuNewVo productSkuNewVo = new ProductSkuNewVo(buyCount,productSku);
			productSkuNewVoList.add(productSkuNewVo);
		}
		return productSkuNewVoList;
	}


	/**
	 * 组装订单对象列表
	 * @param brandIdCouponId 品牌和使用的优惠券Id对应关系，未使用优惠券则优惠券为0，品牌ID:优惠ID_品牌ID:优惠ID,例：223:1_224:2
	 * @param restrictionActivityProductId 
	 * @return
	 */
	private List<StoreOrderNewVo> getStoreOrderNewVoList(Map<Long, ProductNewVo> productVoObjMap,String brandIdCouponId,long addressId,
			long restrictionActivityProductId,Long storeId) {
		
		//1、获取获取品牌和使用优惠券ID对应关系
		Map<Long, Long> brandIdCouponIdMap  = null;
		//活动商品不使用优惠券
		if(restrictionActivityProductId>0){
			brandIdCouponIdMap = new HashMap<Long, Long>();
		}else{
			brandIdCouponIdMap = buildBrandIdCouponIdMap(brandIdCouponId);
		}
		
				
		
				
		//2、根据仓库进行商品分组（key:仓库ID，value:该仓库下购买商品信息列表）
		Map<Long, List<ProductNewVo>> lowarehouseObjMap = new HashMap<Long, List<ProductNewVo>>();
		for (long productId : productVoObjMap.keySet()) {
			ProductNewVo productNewVo = productVoObjMap.get(productId);
//			long brandId = productNewVo.getBrandId();
//			BrandNew brand = productNewVo.getBrand();//品牌
			//供应商
			UserNew supplier = productNewVo.getSupplier();
			if(supplier == null){
				logger.info("供应商为空，请排查问题，productNewVo"+JSON.toJSONString(productNewVo));
			}
			//仓库ID
			long lowarehouseId = supplier.getLowarehouseId();
			
			//获取仓库对应的商品列表
			List<ProductNewVo> productVoList = lowarehouseObjMap.get(lowarehouseId);
			if(productVoList == null){
				List<ProductNewVo> productNewVoList = new ArrayList<ProductNewVo>();
				productNewVoList.add(productNewVo);
				lowarehouseObjMap.put(lowarehouseId,productNewVoList);
			}else{
				productVoList.add(productNewVo);
			}
		}
		
		//3、组装门店订单VO
		List<StoreOrderNewVo> storeOrderNewVoList = new ArrayList<StoreOrderNewVo>();
		for(long lowarehouseId : lowarehouseObjMap.keySet()){

			//供应商
			UserNew supplier = userNewService.getSupplierByLowarehouseId(lowarehouseId);
			long brandId = supplier.getBrandId();
			//品牌
			BrandNew brand = userNewService.getSupplierBrandInfo(brandId);
			
			List<ProductNewVo> productNewVoList = lowarehouseObjMap.get(lowarehouseId);
			
			//1、计算订单总购买量、订单商品总价格
			//获取该仓库订单商品总价格（订单中所有商品单价*数量）
			double totalProductPrice = 0;
			//订单总购买数量
			int totalBuyCount = 0;

			StringBuilder skuInfo  = new StringBuilder();
			for(ProductNewVo productNewVo : productNewVoList){
				int bugCount = productNewVo.getBuyCount();
				totalBuyCount = totalBuyCount + bugCount;
				//购买单价
				double orderUnitPrice = productNewVo.getOrderUnitPrice();
				//商品总价格
				totalProductPrice = totalProductPrice + orderUnitPrice * bugCount;
				// 3_1,2_1,3_1
				List<ProductSkuNewVo> productSkuNewVos = productNewVo.getProductSkuNewVoList();
				if(productSkuNewVos!=null && productSkuNewVos.size()>0) {
					for (ProductSkuNewVo productSkuNewVo : productSkuNewVos) {
						skuInfo.append(productSkuNewVo.getProductSkuNew().getId()).append("_").append(productSkuNewVo.getBuyCount()).append(",");
					}
					skuInfo.delete(skuInfo.length()-1,skuInfo.length());
				}
			}
			//2、获得优惠券抵扣价格（优惠券抵扣）
			double couponOffsetPrice = 0;
			CouponRbRef coupon = null;
			if(restrictionActivityProductId<1){
				Long couponId = brandIdCouponIdMap.get(brandId);
				if(couponId != null){
					coupon = commonRefMapper.selectCoupon(couponId,1);
					if(coupon != null){
						// TODO v3.0 做可用校验 （有效期内、是该用户的，未使用的）
						BigDecimal price = coupon.getPrice();
						BigDecimal discount = coupon.getDiscount().divide(BigDecimal.TEN);
						if(discount!=null  && discount.compareTo(BigDecimal.ZERO)>0) {
							couponOffsetPrice = BigDecimal.ONE.subtract(discount).multiply(new BigDecimal(totalProductPrice)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
						} else if (price !=null && price.compareTo(BigDecimal.ZERO)>0) {
							couponOffsetPrice = coupon.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						}
						coupon.setSubMoney(new BigDecimal(couponOffsetPrice));
					}
				}
			}

			//3、计算优惠后商品总价格（商品总价格 -优惠券价格-供应商改价差额）
			double totalProductPriceAfterCoupon = totalProductPrice - couponOffsetPrice;
			//为负数则设置为0
			if(totalProductPriceAfterCoupon < 0){
				totalProductPriceAfterCoupon = 0;
			}
			
			//4、邮费等 ;//邮费
			double postage = 0;
			if(skuInfo.length()>0) {
				AddressNew addressNew;
				if(addressId==0) {
					addressNew = addressNewService.getDefAddress(storeId);
				} else {
					addressNew = addressNewService.getAddressById(addressId);
				}
				if(addressNew!=null) {
					postage = expressModelService.countOrderExpressMoneyPovinceName(
							addressNew.getProvinceName(),
							skuInfo.toString(),
							null
					).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
			}
			//订单支付价格（优惠后商品总价格+邮费）
			double  payPrice = totalProductPriceAfterCoupon + postage;
			//是否免邮，0(不免邮)、1(免邮)
			 int freePostage = 1;
			//是否免邮名称，0(不免邮)、1(免邮)
			 String freePostageName = "免邮";
			//配送方式名称
			 String deliveryTypeName = "快递";

			 //5、创建订单Vo对象
			StoreOrderNewVo storeOrderNewVo = new StoreOrderNewVo();
			long time = System.currentTimeMillis();
//			storeOrderNewVo.setOrderNo(Long.parseLong(DateUtil.format(time, "HHmmssSSS") + RandomStringUtils.randomNumeric(8)));//yyyyMMddHHmmssSSS
			//供应商
			storeOrderNewVo.setSupplier(supplier);
			//品牌
			storeOrderNewVo.setBrand(brand);
			if(restrictionActivityProductId<1){
				//优惠券
				storeOrderNewVo.setCoupon(coupon);
				//优惠券抵扣价格（优惠券抵扣）
				storeOrderNewVo.setCouponOffsetPrice(couponOffsetPrice);
				//TODO 3.5 2018-02-26 改价功能添加新的字段,平台优惠，商家店铺总优惠，商家店铺优惠，商家改价差额，订单金额优惠后原始待付款价格，不包含邮费
				if(coupon == null|| coupon.getPublishUserId() == 0){
					//TODO 3.5 2018-02-26 平台优惠
					storeOrderNewVo.setPlatformTotalPreferential(couponOffsetPrice);
				}else{
					//3.6 2018-03-19  商家优惠，目前都为0
					storeOrderNewVo.setSupplierPreferential(couponOffsetPrice);
					//3.6 2018-03-19 商家总优惠，目前为0
					storeOrderNewVo.setSupplierTotalPreferential(couponOffsetPrice);
					//3.6 2018-03-19  商家改价差额
					storeOrderNewVo.setSupplierChangePrice(0.0);
				}
			}
//			storeOrderNewVo.setAddress(address);//收货地址
			//配送方式名称
			storeOrderNewVo.setDeliveryTypeName(deliveryTypeName);
			//是否免邮，0(不免邮)、1(免邮)
			storeOrderNewVo.setFreePostage(freePostage);
			//是否免邮名称，0(不免邮)、1(免邮)
			storeOrderNewVo.setFreePostageName(freePostageName);
			//邮费
			storeOrderNewVo.setPostage(postage);
			//订单总购买数量
			storeOrderNewVo.setTotalBuyCount(totalBuyCount);
			storeOrderNewVo.setOriginalPrice(totalProductPriceAfterCoupon);
			//商品总价格（所有商品的商品总价相加（商品总价=商品单价*购买数量））不包含邮费
			storeOrderNewVo.setTotalProductPrice(totalProductPrice);
			//优惠后商品总价格（商品总价格 -优惠券价格-改价差额）
			storeOrderNewVo.setTotalProductPriceAfterCoupon(totalProductPriceAfterCoupon);
			//订单支付价格（优惠后商品总价格+邮费）
			storeOrderNewVo.setPayPrice(payPrice);
			//订单商品VO列表
			storeOrderNewVo.setProductNewVoList(productNewVoList);
			storeOrderNewVoList.add(storeOrderNewVo);
		}
		return storeOrderNewVoList;
	}


	
	/**
	 * 校验数据
	 * 		1. 每个非活动商品状态是否上架
	 * 	 	2. 每个商品的每个sku数量是否足够
	 * @param restrictionActivityProductId 
	 */
	private void checkData(Map<Long, ProductNewVo> productNewVoMap, long restrictionActivityProductId) {
		
		for (long productId : productNewVoMap.keySet()) {
			ProductNewVo productNewVo = productNewVoMap.get(productId);
			if(restrictionActivityProductId<1){//不是活动商品
				//1、检查商品是否是上架状态
				ProductNew product = productNewVo.getProduct();
				if(product == null){
					throw new RuntimeException("获取商品对象失败，请尽快排查问题");
				}
				Integer stateInteger = product.getState();
				if(stateInteger == null ){
					logger.error("获取商品状态为空，请尽快排查问题,product.getId():"+product.getId());
					throw new RuntimeException("获取商品状态为空，请尽快排查问题");
				}
				int state = stateInteger;
				if(state != ProductNewStateEnum.up_sold.getIntValue()){//商品状态不为上架状态
					throw new RuntimeException(ResultCode.ORDER_ERROR_PRODUCT_UNAVAILABLE.getDesc());
				}
			}
			//2、校验库存是否充足
			List<ProductSkuNewVo> skuVoList = productNewVo.getProductSkuNewVoList();
			for(ProductSkuNewVo skuVo : skuVoList ){
				ProductSkuNew sku = skuVo.getProductSkuNew();
				int remainCount = sku.getRemainCount();//库存数量
				int buyCount = skuVo.getBuyCount();//购买数量
				if(buyCount > remainCount){//购买量大于库存量则提示库存不足
					throw new RuntimeException(ResultCode.ORDER_ERROR_RESTRICTION_ACTIVITY_REMAIN_COUNT_LESS.getDesc());
				}
			}
			
		}
		
	}
	
	/**
	 * 获取skuId和sku对象map
	 * @param skuIdCountMap
	 * @return List<ProductSkuNewVo>
	 *     	ProductSkuNewVo 比 ProductSkuNew 多了一个buyCount属性, 表示该sku购买数
	 */
	private List<ProductSkuNewVo> getProductSkuVoList(Map<Long, Integer> skuIdCountMap ) {
		//1、获取sku列表
		List<Long> skuIdList = new ArrayList<Long>();
		skuIdList.addAll(skuIdCountMap.keySet());
		List<ProductSkuNew> skuList = productSkuNewMapper.selectBatchIds(skuIdList);
		
		//2、组装sku对象列表
		List<ProductSkuNewVo> productSkuNewVoList = new ArrayList<ProductSkuNewVo>();
		for(ProductSkuNew productSku : skuList){
			long skuId = productSku.getId();
			int buyCount = skuIdCountMap.get(skuId);
			ProductSkuNewVo productSkuNewVo = new ProductSkuNewVo(buyCount,productSku);
			productSkuNewVoList.add(productSkuNewVo);
		}
		return productSkuNewVoList;
	}
	/**
	 * 对sku进行商品分组
	 * @param skuIdCount sku与数量字符串拼接 : 格式 ---> skuId1:sku1Count_skuId2:sku2Count
	 * @return
	 */
	private Map<Long, ProductNewVo> getProductVoObjMap(String skuIdCount,Long storeId ) {
		//1.1、将SkuId和购买数量解析成Map（测试完成）
		Map<Long, Integer> skuIdCountMap = buildSkuIdCount(skuIdCount);
		logger.info("1.1、将SkuId和购买数量解析成Map完成，,skuIdCountMap:"+JSON.toJSONString(skuIdCountMap));
				
		//1.2、SKU对象列表（测试完成）
		List<ProductSkuNewVo> productSkuNewVoList = getProductSkuVoList(skuIdCountMap);
		logger.info("1.2、SKU对象列表完成，productSkuNewVoList:"+productSkuNewVoList);


		//1.3、对sku进行商品分组
		Map<Long, ProductNewVo> productVoObjMap = new HashMap<Long, ProductNewVo>();
		for (ProductSkuNewVo productSkuNewVo : productSkuNewVoList) {
			ProductSkuNew productSku = productSkuNewVo.getProductSkuNew();
			long productId = productSku.getProductId();
			
			ProductNewVo productNewVo = productVoObjMap.get(productId);
			if(productNewVo== null){//如果不存在则新建商品信息
				ProductNew newProduct = productNewService.getProductById(productId);//商品信息
				long brandId = newProduct.getBrandId();
				BrandNew brand = brandNewService.getBrandByBrandId(brandId);//品牌
				UserNew supplier = userNewService.getSupplierByBrandId(brandId);//品牌对应供应商
				
				List<ProductSkuNewVo> skuVoList = new ArrayList<ProductSkuNewVo>();//商品SKU列表
				skuVoList.add(productSkuNewVo);	
				
				int buyCount = productSkuNewVo.getBuyCount();
				ProductNewVo newProductNewVo = new ProductNewVo( productId, newProduct,brandId,brand,supplier, skuVoList, buyCount);
				productVoObjMap.put(productId, newProductNewVo);
			}else{//如果存在
				//增加商品购买数量
				productNewVo.addBuyCount(productSkuNewVo.getBuyCount());
				//添加商品SKU信息列表
				productNewVo.getProductSkuNewVoList().add(productSkuNewVo);
			}
		}
		
		//根据购买数量计算商品单价
		for(long productId : productVoObjMap.keySet()){
			ProductNewVo productVo = productVoObjMap.get(productId);
			ProductNew product = productVo.getProduct();
			int buyCount = productVo.getBuyCount();
			String ladderPriceJson = product.getLadderPriceJson();

			String memberLadderPriceJson = product.getMemberLadderPriceJson();//会员价格
            double orderUnitPrice;
			YjjMember yjjMember = yjjMemberService.findMemberByUserId(SystemPlatform.STORE,storeId);


            if (product.getMemberLevel()>0&&StringUtils.isNotEmpty(memberLadderPriceJson)
					&&yjjMember!=null&&yjjMember.getMemberLevel()>0){
				logger.info("会员购买会员商品 会员商品 id={}",storeId);
                orderUnitPrice = ProductNew.buildCurrentLadderPriceByBuyCount(memberLadderPriceJson,buyCount);//会员商品单价
            }else if ((product.getMemberLevel()>0&&yjjMember==null)||
					(product.getMemberLevel()>0&&yjjMember.getMemberLevel()==0)){
				logger.warn("非会员不可购买 会员商品 id={}",storeId);
				throw new NonMemberException("nonMember","该商品已是会员商品您无法购买");
			}
            else {
				logger.info("非会员购买商品 id={}",storeId);
                orderUnitPrice = ProductNew.buildCurrentLadderPriceByBuyCount(ladderPriceJson,buyCount);//商品单价

            }
			productVo.setOrderUnitPrice(orderUnitPrice);
		}
		logger.info("1.3、对sku进行商品分组完成，productVoObjMap:"+productVoObjMap);
		return productVoObjMap;
	}

	

	/**
	 * 将SkuId和购买数量解析成Map
	 * @param skuIdCount  skuId和购买数量组成的字符串，例：114:1_224:2
	 * @return map key:skuId, value: count
	 */
	private Map<Long, Integer>  buildSkuIdCount(String skuIdCount) {
		if(StringUtils.isEmpty(skuIdCount)){
			logger.info("SKU对应购买数量字符串不能为空，请排查问题");
			throw new RuntimeException("SKU对应购买数量字符串不能为空，请排查问题！");
		}
		Map<Long, Integer> skuIdCountMap = new HashMap<Long, Integer>();
		//1、用下杠_进行第一次分隔
		String[] skuIdCountArr = skuIdCount.split("_");
		for(String skuIdCountStr : skuIdCountArr){
			//1、用冒号:进行第二次分隔
			String[] skuIdCountItemArr = skuIdCountStr.split(":");
			long skuId = Long.parseLong(skuIdCountItemArr[0]);
			int buyCount = Integer.parseInt(skuIdCountItemArr[1]);
			//购买数量大于0则参与计算
			if(buyCount > 0 ){
				  skuIdCountMap.put(skuId,buyCount);
			}
		}
    	return skuIdCountMap;
	}
	
	/**
	 * 将品牌和优惠券ID解析成Map
	 * @param  brandIdCouponId 品牌和使用的优惠券Id对应关系，未使用优惠券则优惠券为0，品牌ID:优惠ID_品牌ID:优惠ID,例：223:1_224:2
	 */
	private Map<Long, Long>  buildBrandIdCouponIdMap(String brandIdCouponId) {
//		logger.info("将品牌和优惠券ID(brandIdCouponId)解析成map");
		Map<Long, Long> brandIdCouponIdMap = new HashMap<Long, Long>();
		if(StringUtils.isEmpty(brandIdCouponId)){
			return brandIdCouponIdMap;
		}
		
		//1、用下杠_进行第一次分隔
		String[] brandIdCouponIdArr = brandIdCouponId.split("_");
		for(String brandIdCouponIdStr : brandIdCouponIdArr){
			//1、用冒号:进行第二次分隔
			String[] brandIdCouponIdItemArr = brandIdCouponIdStr.split(":");
			brandIdCouponIdMap.put(Long.parseLong(brandIdCouponIdItemArr[0]), Long.parseLong(brandIdCouponIdItemArr[1]));
		}
    	return brandIdCouponIdMap;
	}


}