package com.jiuy.store.api.tool.controller;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.enums.StoreBillEnums;
import com.google.common.base.Joiner;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.exceptions.NonMemberException;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.express.ExpressUtilVo;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.entity.order.ShopMemberOrderItem;
import com.jiuyuan.service.common.*;
import com.jiuyuan.service.common.express.IExpressModelService;
import com.jiuyuan.util.BizException;
import com.jiuyuan.util.BizUtil;
import com.store.dao.mapper.ShopMemberOrderItemMapper;
import com.store.dao.mapper.ShopMemberOrderMapper;
import com.store.dao.mapper.ShopRefundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.DefaultStoreUserDetail;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import static com.jiuyuan.service.common.OrderCreateDataFacade.retrieveProvinceOfAddress;

/**
* 门店订单Controller
*/
@Controller
@RequestMapping("/shop")
public class ShopOrderCreateController {

	private static final Log logger = LogFactory.get();

	@Autowired
	private ShopMemberOrderItemMapper shopMemberOrderItemMapper;
	@Autowired
	private ShopMemberOrderMapper shopMemberOrderMapper;
	@Autowired
	private OrderCreateDataFacade orderCreateDataFacade;
	@Autowired
	private OrderCreateBuildResultDataFacade orderCreateBuildResultDataFacade;
	@Autowired
	private ShopRefundMapper shopRefundMapper;
	@Autowired
	private RefundOrderMapper refundOrderMapper;
	
	@Autowired
	private OrderCreateBuildDataFacade orderCreateBuildDataFacade;

	@Autowired
	private SupplierOrderMapper supplierOrderMapper;
	@Autowired
	private  IExpressModelService expressModelService;
	@Autowired
	private AddressNewService addressNewService;
	@Autowired
	private ICouponServerNew couponServerNew;



	@RequestMapping( "platformInsteadOfSendGoods/confirm/auth" )
	@ResponseBody
	public JsonResponse applyPlatformInsteadOfSendGoods(
//			@RequestParam(required = true) String skuIdCount,
			//限购活动商品id,如果不是限购活动商品则为0
			Long shopMemberOrderId
	){
		logger.info("平台代发货:确认订单: shopMemberOrderId={}", shopMemberOrderId);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			ShopMemberOrder shopOrder = shopMemberOrderMapper.findWxaOrderDetailById(shopMemberOrderId);
			if (shopOrder == null) {
				return jsonResponse.setError("没有找到订单");
			}
			//地址
			String address = retrieveProvinceOfAddress(shopOrder.getReceiverAddress());
			if (address == null) {
				return jsonResponse.setError("获取收获地址失败");
			}
			//sku
			long storeId = shopOrder.getStoreId();
			List<StoreOrderNewVo> storeOrderNewVoList = new ArrayList<>();
			//获取sku和数量
			Wrapper<ShopMemberOrderItem> itemQuery = new EntityWrapper<>();
			itemQuery.eq("order_id", shopOrder.getId());
			List<ShopMemberOrderItem> items = shopMemberOrderItemMapper.selectList(itemQuery);
			if (items.isEmpty()) {
				return jsonResponse.setError("未找到下单商品信息");
			}
			//校验同款商品才能代发
			ShopMemberOrderItem item = items.get(0);
			Long productId = item.getProductId();
			for (int i = 1; i < items.size(); i++) {
				if (!item.getProductId().equals(productId)) {
					return jsonResponse.setError("只有同款商品才能平台代发货");
				}
			}

			List<YjjStoreBusinessAccountLogNew> logs = refundOrderMapper.selectAccountLogByShopOrderNo(shopOrder.getOrderNumber());
			if (logs.isEmpty()) {
				return jsonResponse.setError("查询收入记录失败");
			}
			BigDecimal remainWaitInMoney = BigDecimal.ZERO;
			for (YjjStoreBusinessAccountLogNew log : logs) {
				Integer type = log.getType();
				Double operMoney = log.getOperMoney();
				if (type.equals(StoreBillEnums.PLATFORM_INSTEAD_OF_SEND_GOODS_SUCCESS.getCode())) {
					return jsonResponse.setError("该订单已经代发货");
				}
				if (type.equals(StoreBillEnums.GOODS_ORDER_SUCCESS.getCode())) {
					logger.info("C端用户下单获得待结算总收益={}",operMoney);
                    remainWaitInMoney = remainWaitInMoney.add(BigDecimal.valueOf(operMoney));
				}
				if (type.equals(StoreBillEnums.APP_REFUND_4.getCode())) {
					logger.info("C端用户申请售后的退款,支出的待结算收益={}",operMoney);
                    remainWaitInMoney = remainWaitInMoney.subtract(BigDecimal.valueOf(operMoney));
				}
			}
			logger.info("该笔订单剩余的待结算收益={}", remainWaitInMoney);

			Map<Long, Integer> map = items.stream()
					.collect(Collectors.toMap(ShopMemberOrderItem::getProductSkuId, ShopMemberOrderItem::getCount, (o, o2) -> o + o2));
			logger.info("小程序订单sku数量 skuIdCount={}", map);

			//校验售后,售后中,售后success不允许发货
            subRefundOrder(shopOrder, storeId);

			String skuIdCount = Joiner.on("_").withKeyValueSeparator(":").join(map);
			logger.info("拼接的sku和数量 skuIdCount={}", skuIdCount);
			try {
				//后面的不管了, 走老接口逻辑 update by charlie
				//1、组装数据
				storeOrderNewVoList = orderCreateBuildDataFacade.buildOrderCreateData(skuIdCount, null, address, 0, storeId);
			} catch (NonMemberException e) {
				if (e.getErrorCode().equals("nonMember")) {
					return jsonResponse.setError(e.getMessage());
				}
			}

			//2、校验混批限制 //限购活动商品默认不符合混批
			Map<String, String> wholesaleLimitMap = orderCreateBuildDataFacade.checkWholesaleLimit(storeOrderNewVoList);
			logger.info("wholesaleLimitMap:" + wholesaleLimitMap);
			////是否符合混批限制：0不符合、1符合
			String matchWholesaleLimit = wholesaleLimitMap.get("matchWholesaleLimit");
//			Map<String, Object> data = orderCreateBuildResultDataFacade.getOrderConfirmResultMap(storeOrderNewVoList, 0L, storeId, restrictionActivityProductId);
			Map<String, Object> data = orderCreateBuildResultDataFacade.getSendGoodsOrderConfirmResultMap(storeOrderNewVoList, shopOrder);
			if (matchWholesaleLimit.equals("1")) {
				//符合
				data.put("matchWholesaleLimit", "1");
			} else {
				//不符合符合
				data.put("matchWholesaleLimit", "0");
				data.put("matchBrands", wholesaleLimitMap.get("matchBrands"));
				data.put("noMatchBrand", wholesaleLimitMap.get("noMatchBrand"));
				data.put("matchBrandSkuIdCounts", wholesaleLimitMap.get("matchBrandSkuIdCounts"));
			}

			//查询是否有优惠券
			data.put("shopMemberCouponCount", 0);

			double waitPayTotalPrice = (double) data.get("waitPayTotalPrice");
			if (BigDecimal.valueOf(waitPayTotalPrice).compareTo(remainWaitInMoney)>0) {
				return jsonResponse.setError(String.format("申请代发货需要%s元,该笔订单剩余待结算金额%s元,不足以支付平台代发金额", waitPayTotalPrice, remainWaitInMoney.setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
			}
			return jsonResponse.setSuccessful().setData(data);
		} catch (BizException e) {
			return jsonResponse.setError(e.getMsg());
		} catch (RuntimeException e) {
			String msg = e.getMessage();
			e.printStackTrace();
			return jsonResponse.setError(msg);
		}
	}

    /**
     * 发货前售后的判断
     *
     * @param shopOrder shopOrder
     * @param storeId storeId
     */
    private void subRefundOrder(ShopMemberOrder shopOrder, long storeId) {
        List<ShopOrderAfterSale> shopOrderAfterSales = shopRefundMapper.selectRefundOrderList(shopOrder.getId(), storeId);
        if (! shopOrderAfterSales.isEmpty()) {
            logger.info("需要减去售后的sku");
            for (ShopOrderAfterSale afterSale : shopOrderAfterSales) {
                if (afterSale.getDelStatus().equals(0)) {
                    Integer type = afterSale.getType();
                    if (type.equals(1) || type.equals(0)) {
                        throw BizException.me("已申请售后的订单不能申请平台发货");
                    }
//						//减去售后成功的商品件数 (现在售后成功,售后中也不能发货)
//						Long skuId = afterSale.getSkuId();
//						Integer refundCount = afterSale.getRefundCount();
//
//						Integer count = map.get(skuId);
//						if (count != null) {
//							count = count - refundCount;
//							if (count <= 0) {
//								map.remove(skuId);
//							}
//							else {
//								map.put(skuId, count);
//							}
//						}
//						else {
//							logger.warn("减去售后的sku, 未知的skuId={}", skuId);
//						}
                }
            }
        }
    }


    //	@Login
	@RequestMapping("pay4sendGoodOrder/auth")
	@ResponseBody
	public JsonResponse pay4sendGoodOrder(
			@RequestParam double waitPayTotalPrice,
			@RequestParam long shopMemberOrderId,
			ClientPlatform clientPlatform,
			@ClientIp String ip) {
		JsonResponse jsonResponse = JsonResponse.getInstance();

		try {

			ShopMemberOrder shopOrder = shopMemberOrderMapper.findWxaOrderDetailById(shopMemberOrderId);
			if (shopOrder == null) {
				return jsonResponse.setError("没有找到订单");
			}
			//地址
			String address = retrieveProvinceOfAddress(shopOrder.getReceiverAddress());
			if (address == null) {
				return jsonResponse.setError("获取收获地址失败");
			}

			long storeId = shopOrder.getStoreId();
			String brandIdCouponId = "";

			//获取sku和数量
			Wrapper<ShopMemberOrderItem> itemQuery = new EntityWrapper<>();
			itemQuery.eq("order_id", shopOrder.getId());
			List<ShopMemberOrderItem> items = shopMemberOrderItemMapper.selectList(itemQuery);
			if (items.isEmpty()) {
				return jsonResponse.setError("未找到下单商品信息");
			}
			Map<Long, Integer> map = items.stream()
					.collect(Collectors.toMap(ShopMemberOrderItem::getProductSkuId, ShopMemberOrderItem::getCount, (o, o2) -> o + o2));
			//校验同款同件
			Long productId = items.get(0).getProductId();
			for (int i = 1; i < items.size(); i++) {
				if (items.get(i).getProductId().equals(productId)) {
					continue;
				}
				return jsonResponse.setError("只有同款商品才可以平台代发");
			}

            //校验售后,售后中,售后success不允许发货
            subRefundOrder(shopOrder, storeId);

            String skuIdCount = Joiner.on("_").withKeyValueSeparator(":").join(map);
			//1、组装订单数据
			List<StoreOrderNewVo> storeOrderNewVoList = orderCreateBuildDataFacade.buildOrderCreateData(skuIdCount, brandIdCouponId, address, 0, storeId);

			//2、校验混批限制
			orderCreateBuildDataFacade.checkWholesaleLimit(storeOrderNewVoList);

			//2、创建订单相关并返回母订单
//		StoreOrderNew storeOrderNew = orderCreateDataFacade.orderCreate(storeOrderNewVoList,address,storeId,waitPayTotalPrice,"",clientPlatform, ip,0,skuIdCount);
			StoreOrderNew orderNew = orderCreateDataFacade.pay4sendGoodOrder(storeOrderNewVoList, shopOrder, storeId, clientPlatform, ip, waitPayTotalPrice, items);

			//再查一次给前端
			StoreOrderNew orderQuery = new StoreOrderNew();
			orderQuery.setOrderNo(orderNew.getOrderNo());
			orderNew = supplierOrderMapper.selectOne(orderQuery);
			Map<String, Object> retVal = new HashMap<>(2);
			retVal.put("orderNo", orderNew.getOrderNo());
			retVal.put("parentId", orderNew.getParentId());
			return jsonResponse.setSuccessful().setData(retVal);
		} catch (BizException e) {
			String msg = e.getMsg();
			logger.info("平台代发货,付款失败 msg={}", msg);
			return jsonResponse.setError(msg);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("支付失败");
		}
	}


	/**
     * 订单生成接口（V3.0）
     * @param skuIdCount skuId和购买数量组成的字符串，格式:skuID：购买数量，例：114:1_224:2 
     * @param brandIdCouponId 品牌和使用的优惠券Id对应关系，格式：品牌ID:优惠ID_品牌ID:优惠ID,例：223:1_224:2
     * @param cartIds 购物车ID集合，英文逗号分隔
      * @param addressId 收货地址ID（必填）
     * @param waitPayTotalPrice 待支付总价格（为了和后台计算的价格进行校验一致）
//       	 	if(storeId == 0){
//       	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
//       	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
//       	 	}
     */
	@Login
    @RequestMapping("/shopOrderCreate/auth")
    @ResponseBody
    public JsonResponse shopOrderCreate(
    		@RequestParam(required = true) String skuIdCount,
    		@RequestParam(required = false, defaultValue = "") String brandIdCouponId,
     		@RequestParam(required = false, defaultValue = "") String cartIds,
    		@RequestParam(required = true) double waitPayTotalPrice,
    		@RequestParam(required = true) long addressId,
    		@RequestParam(required = false,defaultValue="0") long restrictionActivityProductId,//限购活动商品id,如果不是限购活动商品则为0
			@RequestParam(value = "orderType",required = false)Integer orderType,//是否是超长15天售后  为3的时候是超长售后
    		ClientPlatform clientPlatform,@ClientIp String ip, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		logger.info("==============订单生成接口开始，参数：shopOrderCreate,skuIdCount:"+skuIdCount+",brandIdCouponId"+brandIdCouponId
    				+",cartIds:"+cartIds+",waitPayTotalPrice:"+waitPayTotalPrice+",addressId"+addressId);
    		long storeId = userDetail.getId();

    		//1、组装订单数据
    		List<StoreOrderNewVo> storeOrderNewVoList = orderCreateBuildDataFacade.buildOrderCreateData(skuIdCount,brandIdCouponId,addressId,restrictionActivityProductId,storeId);

    		//2、校验混批限制
    		String matchWholesaleLimit = "";
    		Map<String,String> wholesaleLimitMap = null;
    		if(restrictionActivityProductId<1){//如果不是限购活动商品订单,则判断是否混批
    			wholesaleLimitMap = orderCreateBuildDataFacade.checkWholesaleLimit(storeOrderNewVoList);
        		matchWholesaleLimit = wholesaleLimitMap.get("matchWholesaleLimit");////是否符合混批限制：0不符合、1符合
    		}
    		
    		Map<String,Object> data = new HashMap<String,Object>();
    		//2、创建订单相关并返回母订单
    		StoreOrderNew storeOrderNew = orderCreateDataFacade.orderCreate(storeOrderNewVoList,addressId,storeId,waitPayTotalPrice,cartIds,clientPlatform,
    				ip,restrictionActivityProductId,skuIdCount);
    		//3、组装返回数据
    		long orderNo = storeOrderNew.getOrderNo();//storeOrderNew.getOrderNo()
    		double totalPay = storeOrderNew.getTotalPay();//优惠后商品总价格   订单金额折后总价，不包含邮费
    		double totalExpressMoney = storeOrderNew.getTotalExpressMoney();//邮费总价格
    		double totalOrderPayPrice = totalPay + totalExpressMoney;
//    		需要针对核对各种情况客户端计算的价格是否和接口计算的待支付价格一直
    		if(waitPayTotalPrice != totalOrderPayPrice){
    				logger.info("前台和后台计算价格不匹配请尽快排查问题！！！！！！！！！！！！！！！！！！！！！");
    		}else{
    			logger.info("客户端计算的价格是否和接口计算的待支付价格一致没有问题！！！！！！！！！！！！！！！！！！！！！");
    		}
    		data.put("waitPayTotalPrice",totalOrderPayPrice);//待支付总金额
    	    data.put("orderNoStr",String.format("%09d", orderNo) );//订单号补零用于显示 String.format("%09d", this.getOrderNo());
    	    data.put("orderNo", orderNo);//订单号
    		if(matchWholesaleLimit.equals("1") || restrictionActivityProductId>0){//符合或者是限购活动商品订单
    			//2、统计计算费用并封装成Map返回前端
        		data.put("matchWholesaleLimit", "1");//是否符合混批限制：0不符合、1符合
	    	    logger.info("==============订单生成接口完成，返回数据："+JSON.toJSONString(data));
    		}else{//不符合符合
    			data.put("matchWholesaleLimit", "0");//是否符合混批限制：0不符合、1符合
    			data.put("matchBrands", wholesaleLimitMap.get("matchBrands"));//符合限制条件品牌
    			data.put("noMatchBrand", wholesaleLimitMap.get("noMatchBrand"));//不符合限制条件品牌
    			data.put("matchBrandSkuIdCounts", wholesaleLimitMap.get("matchBrandSkuIdCounts"));//符合限制条件品牌商品skuId数量数组  skuId:数量_skuId:数量
    		}
    		return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }


	/**
	 * 购买微信小程序下单
	 *
	 * <p>映射路径与商品下单一致,以后可以通过路径合并接口</p>
	 * @param packageType 微信服务的套餐种类
	 * @param productPrice 小程序套餐价格(暂时不用,以后可以用的到...)
	 * @param userDetail userDetail
	 * @param clientPlatform clientPlatform
	 * @return com.jiuyuan.web.help.JsonResponse
	 * @author Charlie
	 * @date 2018/8/14 20:40
	 * @see
	 */
	@Login
	@RequestMapping("/shopOrderCreate/weiChatApp/auth")
	@ResponseBody
	public JsonResponse shopOrderCreate(Integer packageType, @RequestParam(value = "productPrice", required = false) Double productPrice, UserDetail userDetail, ClientPlatform clientPlatform,@ClientIp String ip){
		Map<String, Object> result = null;
		try {
			result = orderCreateDataFacade.createMemberOrder (packageType, productPrice, userDetail.getId (), clientPlatform, ip);
		} catch (Exception e) {
			e.printStackTrace ();
			if (e instanceof BizException) {
				return JsonResponse.getInstance ().setError (((BizException) e).getMsg ());
			}
			else if (e instanceof RuntimeException) {
				return JsonResponse.getInstance ().setError (e.getMessage ());
			}
			else {
				throw e;
			}
		}
		return JsonResponse.getInstance ().setSuccessful ().setData (result);
	}






	/**
	 * 用户是否可以购买会员
	 * <p>
	 *     用户已是会员,不让支付
	 * </p>
	 * @param userDetail userDetail
	 * @return com.jiuyuan.web.help.JsonResponse
	 * @author Charlie
	 * @date 2018/8/18 12:25
	 */
	@Login
	@RequestMapping( "/shopOrderCreate/weiChatApp/couldPay/auth" )
	@ResponseBody
	public JsonResponse couldPay4MemberOrder( UserDetail userDetail) {
		return JsonResponse.getInstance ().setSuccessful ().setData (orderCreateDataFacade.couldPay4MemberOrder(userDetail.getId ()));
	}


	/**
	 * 获取运费 接口
	 * @param addressId 地址id
	 * @param skuinfos    sku的ids  3_1,2_1,3_1  sku的id和数量的关系
	 * @date:   2018/5/3 20:02
	 * @author: Aison
	 */
	@ResponseBody
	@Login
    @RequestMapping("countOrderExpress")
    public JsonResponse countOrderExpress(Long addressId,String skuinfos ) {
		try{
			AddressNew addressNew = addressNewService.getAddressById(addressId);
			if(addressNew==null) {
			  throw BizException.defulat().msg("地址错误");
			}
			BigDecimal expressMoney = expressModelService.countOrderExpressMoneyPovinceName(addressNew.getProvinceName(),skuinfos,null);
			return new JsonResponse().setSuccessful().setData(expressMoney);
		}catch (Exception e) {
		   return BizUtil.exceptionHandler(e);
		}
	}



	/**
	 * 获取一批次的运费
	 * @date:   2018/5/3 20:02
	 * @author: Aison
	 */
	@ResponseBody
	@Login
	@RequestMapping("countOrderExpresses")
	public JsonResponse countOrderExpresses(ExpressUtilVo expressUtilVo ,
											@RequestParam(value = "shopMemberOrderId", required = false) Long shopMemberOrderId) {
		try{
			Map<String,BigDecimal> expreesMap = expressModelService.countOrderExpressMoneyPovinceNames(expressUtilVo);
			return new JsonResponse().setSuccessful().setData(expreesMap);
		}catch (Exception e) {
			return BizUtil.exceptionHandler(e);
		}
	}

    /**
     * 新订单确认接口（V3.0）
     * 
     * @param skuIdCount skuId和购买数量组成的字符串，例：114:1_224:2
     * @return
     */
	@Login
    @RequestMapping("/shopOrderConfirm/auth")
    @ResponseBody
    public JsonResponse shopOrderConfirm(
    		@RequestParam(required = true) String skuIdCount, 
    		@RequestParam(required = false, defaultValue = "0")long addressId,
			//限购活动商品id,如果不是限购活动商品则为0
    		@RequestParam(required = false,defaultValue="0") long restrictionActivityProductId,
    		ClientPlatform clientPlatform,
    		@ClientIp String ip,
    		UserDetail userDetail){
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		logger.info("============新订单确认接口开始，shopOrderConfirm,skuIdCount:"+skuIdCount+",addressId:"+addressId
    				+",restrictionActivityProductId:"+restrictionActivityProductId);
    		long storeId = userDetail.getId();
			List<StoreOrderNewVo> storeOrderNewVoList = new ArrayList<>();
    		try {
				//1、组装数据
				storeOrderNewVoList = orderCreateBuildDataFacade.buildOrderCreateData(skuIdCount, null, addressId, restrictionActivityProductId, storeId);
			}catch (NonMemberException e){
    			if (e.getErrorCode().equals("nonMember")){
    				return jsonResponse.setError(e.getMessage());
				}
			}
    		Map<String,Object> data ;
    		
    		//2、校验混批限制 //限购活动商品默认不符合混批
    		if(restrictionActivityProductId>0){
    			logger.info("限购活动商品：restrictionActivityProductId："+restrictionActivityProductId);
    			data = orderCreateBuildResultDataFacade.getOrderConfirmResultMap(storeOrderNewVoList,addressId,storeId,restrictionActivityProductId);
				//是否符合混批限制：0不符合、1符合
    			data.put("matchWholesaleLimit", "0");
    		}else{
    			Map<String,String> wholesaleLimitMap = orderCreateBuildDataFacade.checkWholesaleLimit(storeOrderNewVoList);
    			logger.info("wholesaleLimitMap:"+wholesaleLimitMap);
				////是否符合混批限制：0不符合、1符合
        		String matchWholesaleLimit = wholesaleLimitMap.get("matchWholesaleLimit");
        		data = orderCreateBuildResultDataFacade.getOrderConfirmResultMap(storeOrderNewVoList,addressId,storeId,restrictionActivityProductId);
				//符合
        		if(matchWholesaleLimit.equals("1")){
        			//2、统计计算费用并封装成Map返回前端
					//是否符合混批限制：0不符合、1符合
            		data.put("matchWholesaleLimit", "1");
					//不符合符合
        		}else{
					//是否符合混批限制：0不符合、1符合
        			data.put("matchWholesaleLimit", "0");
					//符合限制条件品牌
        			data.put("matchBrands", wholesaleLimitMap.get("matchBrands"));
					//不符合限制条件品牌
        			data.put("noMatchBrand", wholesaleLimitMap.get("noMatchBrand"));
					//符合限制条件品牌商品skuId数量数组  skuId:数量_skuId:数量
        			data.put("matchBrandSkuIdCounts", wholesaleLimitMap.get("matchBrandSkuIdCounts"));
        		}
    		}


    		//查询是否有优惠券
			couponServerNew.fillCoupon(data,storeId,null,CouponSysEnum.APP,false,1,100);

    		return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
	
	
    
   
	

}