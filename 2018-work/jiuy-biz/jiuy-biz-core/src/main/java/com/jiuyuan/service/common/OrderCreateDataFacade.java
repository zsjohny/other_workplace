package com.jiuyuan.service.common;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.enums.StoreBillEnums;
import com.google.common.base.Joiner;
import com.jiuyuan.common.CouponRbRef;
import com.jiuyuan.dao.mapper.CommonRefMapper;
import com.jiuyuan.dao.mapper.supplier.*;
import com.jiuyuan.constant.SystemPlatform;
import com.jiuyuan.entity.common.DataDictionary;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.entity.order.ShopMemberOrderItem;
import com.jiuyuan.service.common.express.IExpressModelService;
import com.jiuyuan.util.BizException;
import com.jiuyuan.util.BizUtil;
import com.store.service.ChargeFacade;
import com.yujj.entity.product.YjjMember;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.OrderNewLog;
import com.jiuyuan.entity.newentity.AddressNew;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductNewVo;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.ProductSkuNewVo;
import com.jiuyuan.entity.newentity.RestrictionActivityProduct;
import com.jiuyuan.entity.newentity.RestrictionActivityProductSku;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.StoreCouponNew;
import com.jiuyuan.entity.newentity.StoreCouponUseLogNew;
import com.jiuyuan.entity.newentity.StoreOrderItemNew;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.StoreOrderNewVo;
import com.jiuyuan.entity.newentity.StoreOrderProduct;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.util.ClientUtil;
import com.jiuyuan.util.CollectionUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import javax.swing.plaf.metal.MetalTheme;

import static com.enums.StoreBillEnums.PLATFORM_INSTEAD_OF_SEND_GOODS_SUCCESS;
import static com.jiuyuan.service.common.DataDictionaryService.MEMBER_PACKAGE_TYPE_GROUP_KEY;
import com.jiuyuan.constant.order.PaymentType;

/**
 * app生成订单构建数据专用Facade
 */
@Service
public class OrderCreateDataFacade{

	private static final Log logger = LogFactory.get(OrderCreateDataFacade.class);
	/**
	 * 会员订单的有效期:45分钟
	 */
	private static final long MEMBER_ORDER_EXPIRED_TIME_STAMP = 1000 * 60 * 45;
	//===========================================================================================================

	@Autowired
	private YunXinSmsService yunXinSmsService;
	@Autowired
	private UserNewMapper userNewMapper;
	@Autowired
	private ShopMemberOrderNewMapper shopMemberOrderNewMapper;
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;
	@Autowired
	private RefundOrderMapper refundOrderMapper;
	@Autowired
	private DataDictionaryService dataDictionaryService;
	@Autowired
	private ISuperOrderService superOrderService;
	@Autowired
	private ShopGlobalSettingService globalSettingService;
	@Autowired
	private ProductSkuNewMapper productSkuNewMapper;
	@Autowired
	private IProductNewService productNewService;
	@Autowired
	private IUserNewService userNewService;

	@Autowired
	private OrderNewLogMapper orderNewLogMapper;
//	@Autowired(required = false)
//    private List<OrderHandler> orderHandlers;
	@Autowired
	private OrderItemNewMapper orderItemNewMapper;
	@Autowired
	private StoreOrderProductMapper storeOrderProductMapper;
//	@Autowired
//	private OrderService orderService;
//	@Autowired
//	private ShoppingCartService shoppingCartService;
	@Autowired
	private IAddressNewService addressNewService;
	@Autowired
	private IStoreOrderNewService storeOrderNewService;

	@Autowired
	private ShoppingCartNewService shoppingCartNewServicel;

//	@Autowired
//	private StoreBusinessNewMapper storeBusinessNewMapper;

	@Autowired
	private StoreMapper storeMapper;

    @Autowired
    private IStoreCouponNewService storeCouponNewService;

    @Autowired
    private StoreCouponUseLogNewMapper storeCouponUseLogNewMapper;

    @Autowired
    private StoreCouponNewMapper storeCouponNewMapper;

    @Autowired
    private MemcachedService memcachedService;

    @Autowired
    private RestrictionActivityProductMapper restrictionActivityProductMapper;
    @Autowired
    private RestrictionActivityProductSkuMapper restrictionActivityProductSkuMapper;

    @Autowired
	private IYjjMemberService memberService;
    @Autowired
	private IExpressModelService expressModelService;

    @Autowired
	private CommonRefMapper commonRefMapper;

	/**
	 * 创建订单
	 * @param storeOrderNewVoList
	 * @param addressId 收货地址ID
	 * @param storeId 卖家ID
	 * @param waitPayTotalPrice 用于做校验
	 * cartIds 购物车ID集合，英文逗号分隔
	 * @param restrictionActivityProductId
	 * @param skuIdCount
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	synchronized public StoreOrderNew orderCreate(List<StoreOrderNewVo> storeOrderNewVoList ,long addressId, long storeId,
			double waitPayTotalPrice,String cartIds,ClientPlatform clientPlatform,String ip, long restrictionActivityProductId, String skuIdCount) {
		logger.info("创建订单开始storeId:"+storeId+";restrictionActivityProductId:"+restrictionActivityProductId);
		StoreOrderNew parentStoreOrderNew = null;

		if(restrictionActivityProductId>0){
			//限购活动商品的库存
			boolean rushResult = rushRestrictionActivityProductRemainCount(restrictionActivityProductId,skuIdCount,storeId);
			if(!rushResult){
				logger.error("限购活动商品的库存不足restrictionActivityProductId:"+restrictionActivityProductId);
				throw new RuntimeException("限购活动商品的库存不足");
			}
		}

		//1、先生成子订单（待核对逻辑）
		for(StoreOrderNewVo storeOrderNewVo : storeOrderNewVoList){
			StoreOrderNew storeOrderNew = addOrderInfo(storeOrderNewVo,addressId, storeId, clientPlatform, ip,restrictionActivityProductId);
			storeOrderNewVo.setOrder(storeOrderNew);
		}
		//2、在生成母订单（如果一个子订单则将其母订单ID改为自身，如果多个子订单则生成母订单）
		int orderCount = storeOrderNewVoList.size();

		if(orderCount == 0 ){
			logger.info("组装的订单数量为0，请排查问题！！");
		}else if(orderCount == 1 ){//
			//2.1、一个订单时则直接修改该订单的父订单为自身
			StoreOrderNewVo storeOrderNewVo  = storeOrderNewVoList.get(0);
			parentStoreOrderNew = storeOrderNewVo.getOrder();
			updateOrderParentId(parentStoreOrderNew.getOrderNo(),parentStoreOrderNew.getOrderNo());
		}else{//多个订单时则生成新母订单
			//2.2、组装母订单
			StoreOrderNewVo storeOrderNewVo = getParentStoreOrderNewVo(storeOrderNewVoList);
			parentStoreOrderNew = addOrderInfo( storeOrderNewVo,addressId, storeId, clientPlatform, ip,restrictionActivityProductId);


			//2.3、批量修复子订单的父订单编号
			for(StoreOrderNewVo orderVo : storeOrderNewVoList){
				StoreOrderNew order = orderVo.getOrder();
				logger.info("更改子订单的父订单ID开始，order.getOrderNo()："+order.getOrderNo()+",parentStoreOrderNew.getOrderNo():"+parentStoreOrderNew.getOrderNo());
				updateOrderParentId(order.getOrderNo(),parentStoreOrderNew.getOrderNo());
				logger.info("更改子订单的父订单ID结束，order.getOrderNo()："+order.getOrderNo()+",parentStoreOrderNew.getOrderNo():"+parentStoreOrderNew.getOrderNo());
			}

			//2.4修改母订单的父Id为-1,-1表示已经拆单
			updateOrderParentId(parentStoreOrderNew.getOrderNo(),-1);

			updateParentOrderExpress(parentStoreOrderNew);
		}

			//如果订单金额为0，直接生成，并减库存和增销量
			//4、减库存
			for(StoreOrderNewVo storeOrderNewVo : storeOrderNewVoList){
				List<ProductNewVo> productNewVoList = storeOrderNewVo.getProductNewVoList();
				for(ProductNewVo productNewVo : productNewVoList){
					 List<ProductSkuNewVo> skuVoList = productNewVo.getProductSkuNewVoList();
//					 int totalBuyCount = 0;
					 int allBuyCount = 0;
					 boolean flag = true;
					 for(ProductSkuNewVo skuVo : skuVoList){
						 ProductSkuNew sku = skuVo.getProductSkuNew();
						 long skuId = sku.getId();
						 int buyCount = skuVo.getBuyCount();
						 allBuyCount += buyCount;
//						 totalBuyCount += buyCount;
						 int currentRemainCount = 0;
						 if(restrictionActivityProductId>0){//限购活动商品减库存
							 Wrapper<RestrictionActivityProductSku> wrapper = new EntityWrapper<RestrictionActivityProductSku>()
									 .eq("activity_product_id", restrictionActivityProductId).eq("product_sku_id", skuId);
							 RestrictionActivityProductSku restrictionActivityProductSku = restrictionActivityProductSkuMapper.selectList(wrapper).get(0);
							 long restrictionActivityProductSkuId = restrictionActivityProductSku.getId();
							 currentRemainCount = restrictionActivityProductSku.getRemainCount();
							 //当前库存大于等于购买量时进行减库存
							 if(currentRemainCount >= buyCount){
								 logger.info("减库存开始，restrictionActivityProductSkuId："+restrictionActivityProductSku.getId()+",buyCount:"+buyCount);
								 restrictionActivityProductSku = new RestrictionActivityProductSku();
								 restrictionActivityProductSku.setId(restrictionActivityProductSkuId);
								 int remainCountNew = currentRemainCount - buyCount;
								 restrictionActivityProductSku.setRemainCount(remainCountNew);
								 restrictionActivityProductSkuMapper.updateById(restrictionActivityProductSku);
								 logger.info("减库存结束，restrictionActivityProductSkuId："+skuId+",buyCount:"+buyCount);
							 }else{
								 flag = false;
								 logger.info("减库存出现异常，请尽快排查原因");
							 }
						 }else{//普通商品减库存
							 ProductSkuNew currentSku = productSkuNewMapper.selectById(skuId);
							 currentRemainCount = currentSku.getRemainCount();
							 //当前库存大于等于购买量时进行减库存
							 if(currentRemainCount >= buyCount){
								 logger.info("减库存开始，skuId："+skuId+",buyCount:"+buyCount);
								 productSkuNewMapper.reduceRemainCount(skuId,buyCount);
								 logger.info("减库存结束，skuId："+skuId+",buyCount:"+buyCount);
							 }else{
								 logger.info("减库存出现异常，请尽快排查原因");
							 }
						 }
						 if(restrictionActivityProductId>0 && flag){//限购活动商品减库存
							 int record = restrictionActivityProductMapper.updateRemainCountById(restrictionActivityProductId,allBuyCount);
							 logger.info("====限购活动商品减库存:record:"+record);
							 if(record!=1){
								//减少缓存中剩余活动商品数量
								String groupKey = MemcachedKey.GROUP_KEY_restrictionActivityProductId;
								String key = "_restrictionActivityProductId_"+String.valueOf(restrictionActivityProductId)+"_skuId_"+String.valueOf(skuId);
								Object count = memcachedService.getCommon(groupKey, key);
								long memcachedServiceAfterIncrCount = memcachedService.incrCommon(groupKey, key, buyCount);
								logger.info("====抢购限购失败加活动商品库存rushRestrictionActivityProductRemainCount结果,memcachedServiceAfterDecrCount："+memcachedServiceAfterIncrCount
										+",count:"+count);
								if(memcachedServiceAfterIncrCount>=0){
									logger.info("====抢购限购失败加活动商品库存rushRestrictionActivityProductRemainCount,抢购限购失败加活动商品库存成功restrictionActivityProductId："+restrictionActivityProductId
											+",skuId:"+skuId+",storeId:"+storeId+",加后memcachedServiceAfterDecrCount："+memcachedServiceAfterIncrCount+",count:"+count);
								}else{
									count = memcachedService.getCommon(groupKey, key);
									logger.info("====抢购限购失败加活动商品库存rushRestrictionActivityProductRemainCount,抢购限购失败加活动商品库存失败restrictionActivityProductId："+restrictionActivityProductId
											+",skuId:"+skuId+",storeId:"+storeId+",加后memcachedServiceAfterDecrCount："+memcachedServiceAfterIncrCount+",count:"+count);
								}
								throw new RuntimeException("限购活动商品下单减库存失败,库存不足");
							 }
						 }
					 }

				}
			}



		if(restrictionActivityProductId<1){
			//3、删除购物车
			removeshoppingCart(storeId, cartIds);
		}
		//4、订单零元购添加支付时间
		for(StoreOrderNewVo storeOrderNewVo : storeOrderNewVoList){
			//订单支付金额小于时添加支付时间
			StoreOrderNew order = storeOrderNewVo.getOrder();
			double payPrice = order.getTotalPay() + order.getTotalExpressMoney() ;
			if(payPrice <= 0.001){//添加支付时间
				storeOrderNewService.updatePaytime(order.getOrderNo());
			}
		}

		//5、增加商品销量
		for(StoreOrderNewVo storeOrderNewVo : storeOrderNewVoList){
				//订单支付金额小于时进行直接增加销量
				StoreOrderNew order = storeOrderNewVo.getOrder();
				double payPrice = order.getTotalPay() + order.getTotalExpressMoney() ;
				if(payPrice <= 0.001){//增加销量
					List<ProductNewVo> productNewVoList = storeOrderNewVo.getProductNewVoList();
					for(ProductNewVo productNewVo : productNewVoList){
						 productNewService.updateSaleCount(productNewVo.getProductId(),productNewVo.getBuyCount());
					}
				}
		}
		//6、商家优惠券使用，要标记为已使用  modify by hyq 20180829 没有必要使用了。注释
//		for(StoreOrderNewVo storeOrderNewVo : storeOrderNewVoList){
//			//订单优惠券金额小于0时进行直接记为已使用
//			StoreOrderNew order = storeOrderNewVo.getOrder();
//			double payPrice = order.getTotalPay() + order.getTotalExpressMoney() ;
//			if(payPrice <= 0.001){
//				long orderNo = order.getOrderNo();
//				Wrapper<StoreCouponUseLogNew> wrapper = new EntityWrapper<StoreCouponUseLogNew>();
//				wrapper.eq("OrderNo", orderNo)
//				.eq("Status", 0)
//				.ne("supplier_id", 0);
//				List<StoreCouponUseLogNew> storeCouponUseLogNewList = storeCouponUseLogNewMapper.selectList(wrapper);
//				if(storeCouponUseLogNewList.size()>0){
//					StoreCouponNew storeCouponNew = storeCouponNewMapper.selectById(storeCouponUseLogNewList.get(0).getCouponId());
//					long couponTemplateId = storeCouponNew.getCouponTemplateId();
//					storeCouponNewService.doStatisticsByCouponTemplateIdWhenUse(couponTemplateId);
//				}
//			}
//		}
		logger.info("创建订单结束storeId:"+storeId+";restrictionActivityProductId:"+restrictionActivityProductId);
		//7、返回母订单
		return parentStoreOrderNew;
	}


	@Transactional(rollbackFor = Exception.class)
	synchronized public StoreOrderNew orderCreate4InstanceOfSendGoods(List<StoreOrderNewVo> storeOrderNewVoList, ShopMemberOrder shopOrder, long storeId,
																	  ClientPlatform clientPlatform, String ip, double waitPayTotalPrice) {
		StoreOrderNew parentStoreOrderNew = null;

		//1、先生成子订单（待核对逻辑）
		for(StoreOrderNewVo storeOrderNewVo : storeOrderNewVoList){
			StoreOrderNew storeOrderNew = addOrderInfo4InstanceOfSendGoods(storeOrderNewVo,shopOrder, storeId, clientPlatform, ip);
//			StoreOrderNew storeOrderNew = addOrderInfo(storeOrderNewVo,addressId, storeId, clientPlatform, ip,restrictionActivityProductId);
			storeOrderNewVo.setOrder(storeOrderNew);
		}
		//2、在生成母订单（如果一个子订单则将其母订单ID改为自身，如果多个子订单则生成母订单）
		int orderCount = storeOrderNewVoList.size();

		if(orderCount == 0 ){
			logger.info("组装的订单数量为0，请排查问题！！");
		}else if(orderCount == 1 ){//
			//2.1、一个订单时则直接修改该订单的父订单为自身
			StoreOrderNewVo storeOrderNewVo  = storeOrderNewVoList.get(0);
			parentStoreOrderNew = storeOrderNewVo.getOrder();
			updateOrderParentId(parentStoreOrderNew.getOrderNo(),parentStoreOrderNew.getOrderNo());
		}else{//多个订单时则生成新母订单
			//2.2、组装母订单
			StoreOrderNewVo storeOrderNewVo = getParentStoreOrderNewVo(storeOrderNewVoList);
			parentStoreOrderNew = addOrderInfo4InstanceOfSendGoods( storeOrderNewVo,shopOrder, storeId, clientPlatform, ip);
//			parentStoreOrderNew = addOrderInfo( storeOrderNewVo,addressId, storeId, clientPlatform, ip);

			//2.3、批量修复子订单的父订单编号
			for(StoreOrderNewVo orderVo : storeOrderNewVoList){
				StoreOrderNew order = orderVo.getOrder();
				logger.info("更改子订单的父订单ID开始，order.getOrderNo()："+order.getOrderNo()+",parentStoreOrderNew.getOrderNo():"+parentStoreOrderNew.getOrderNo());
				updateOrderParentId(order.getOrderNo(),parentStoreOrderNew.getOrderNo());
				logger.info("更改子订单的父订单ID结束，order.getOrderNo()："+order.getOrderNo()+",parentStoreOrderNew.getOrderNo():"+parentStoreOrderNew.getOrderNo());
			}

			//2.4修改母订单的父Id为-1,-1表示已经拆单
			updateOrderParentId(parentStoreOrderNew.getOrderNo(),-1);

			updateParentOrderExpress(parentStoreOrderNew);
		}


		//3、校验支付价格
		double totalPay = parentStoreOrderNew.getTotalPay();//优惠后商品总价格   订单金额折后总价，不包含邮费
		//邮费总价格
		double totalExpressMoney = parentStoreOrderNew.getTotalExpressMoney();
		//待支付总金额
		double totalOrderPayPrice = BigDecimal.valueOf(totalPay).add(BigDecimal.valueOf(totalExpressMoney)).doubleValue();
		//需要针对核对各种情况客户端计算的价格是否和接口计算的待支付价格一直
		if(waitPayTotalPrice != totalOrderPayPrice){
			throw BizException.me("支付价格不匹配,需要支付" + totalOrderPayPrice + "元");
		}

		//如果订单金额为0，直接生成，并减库存和增销量
		//4、减库存
		for(StoreOrderNewVo storeOrderNewVo : storeOrderNewVoList){
			List<ProductNewVo> productNewVoList = storeOrderNewVo.getProductNewVoList();
			for(ProductNewVo productNewVo : productNewVoList){
				List<ProductSkuNewVo> skuVoList = productNewVo.getProductSkuNewVoList();
//					 int totalBuyCount = 0;
				for(ProductSkuNewVo skuVo : skuVoList){
					ProductSkuNew sku = skuVo.getProductSkuNew();
					long skuId = sku.getId();
					int buyCount = skuVo.getBuyCount();
					ProductSkuNew currentSku = productSkuNewMapper.selectById(skuId);
					//当前库存大于等于购买量时进行减库存
					if(currentSku.getRemainCount() >= buyCount){
						logger.info("减库存开始，skuId："+skuId+",buyCount:"+buyCount);
						productSkuNewMapper.reduceRemainCount(skuId,buyCount);
						logger.info("减库存结束，skuId："+skuId+",buyCount:"+buyCount);
					}else{
						logger.info("减库存出现异常，请尽快排查原因");
					}
				}

			}
		}
		//4、订单零元购添加支付时间
		for(StoreOrderNewVo storeOrderNewVo : storeOrderNewVoList){
			//订单支付金额小于时添加支付时间
			StoreOrderNew order = storeOrderNewVo.getOrder();
			double payPrice = order.getTotalPay() + order.getTotalExpressMoney() ;
			if(payPrice <= 0.001){//添加支付时间
				storeOrderNewService.updatePaytime(order.getOrderNo());
			}
		}

		//5、增加商品销量
		for(StoreOrderNewVo storeOrderNewVo : storeOrderNewVoList){
			//订单支付金额小于时进行直接增加销量
			StoreOrderNew order = storeOrderNewVo.getOrder();
			double payPrice = BigDecimal.valueOf(order.getTotalPay()).add(BigDecimal.valueOf(order.getTotalExpressMoney())).doubleValue() ;
			if(payPrice <= 0.001){//增加销量
				List<ProductNewVo> productNewVoList = storeOrderNewVo.getProductNewVoList();
				for(ProductNewVo productNewVo : productNewVoList){
					productNewService.updateSaleCount(productNewVo.getProductId(),productNewVo.getBuyCount());
				}
			}
		}
		//7、返回母订单
		return parentStoreOrderNew;
	}


	private void updateParentOrderExpress(StoreOrderNew parentStoreOrderNew){
		// 查询子订单
		Wrapper<StoreOrderNew> wrapper = new EntityWrapper<>();
		List<StoreOrderNew> orderItems = supplierOrderMapper.selectList(wrapper.eq("ParentId",parentStoreOrderNew.getOrderNo()));
		Double total = 0d;
		for (StoreOrderNew orderItem : orderItems) {
			total +=BizUtil.hasEmpty(orderItem.getTotalExpressMoney())?0 :orderItem.getTotalExpressMoney();
		}

		parentStoreOrderNew.setTotalExpressMoney(total);

		StoreOrderNew parentStoreOrderNewTag = new StoreOrderNew();
		parentStoreOrderNewTag.setTotalExpressMoney(total);
		parentStoreOrderNewTag.setOrderNo(parentStoreOrderNew.getOrderNo());
		supplierOrderMapper.updateById(parentStoreOrderNewTag);
	}




	/**
	 * 限购活动商品的库存
	 * @param restrictionActivityProductId
	 * @param restrictionActivityProductId
	 * @param skuIdCount
	 * @param storeId
	 * @return
	 */
	private boolean rushRestrictionActivityProductRemainCount(long restrictionActivityProductId, String skuIdCount, long storeId) {
		logger.info("抢购限购活动商品skuIdCount："+skuIdCount+",storeId:"+storeId);
		//1、用下杠_进行第一次分隔
		String[] skuIdCountArr = skuIdCount.split("_");
		for(String skuIdCountStr : skuIdCountArr){
			//1、用冒号:进行第二次分隔
			String[] skuIdCountItemArr = skuIdCountStr.split(":");
			long skuId = Long.parseLong(skuIdCountItemArr[0]);
			int buyCount = Integer.parseInt(skuIdCountItemArr[1]);

			//减少缓存中剩余活动商品数量
			String groupKey = MemcachedKey.GROUP_KEY_restrictionActivityProductId;
			String key = "_restrictionActivityProductId_"+String.valueOf(restrictionActivityProductId)+"_skuId_"+String.valueOf(skuId);
			int count = Integer.parseInt((String) memcachedService.getCommon(groupKey, key)) ;
			long memcachedServiceAfterDecrCount = memcachedService.decrCommon(groupKey, key, buyCount);
			logger.info("====抢购限购活动商品rushRestrictionActivityProductRemainCount结果,memcachedServiceAfterDecrCount："+memcachedServiceAfterDecrCount
					+",count:"+count);
			//if(memcachedServiceAfterDecrCount>=0){之前用这句话判断，但是memcache自减无法小于0
			if(count>0){
				logger.info("====抢购限购活动商品rushRestrictionActivityProductRemainCount,抢购限购活动商品成功restrictionActivityProductId："+restrictionActivityProductId
						+",skuId:"+skuId+",storeId:"+storeId+",抢后memcachedServiceAfterDecrCount："+memcachedServiceAfterDecrCount+",count:"+count);
				//参与人数加1
				return true;
			}else{
//				Object count = memcachedService.getCommon(groupKey, key);
				logger.info("====抢购限购活动商品rushRestrictionActivityProductRemainCount,抢购限购活动商品失败restrictionActivityProductId："+restrictionActivityProductId
						+",skuId:"+skuId+",storeId:"+storeId+",抢后memcachedServiceAfterDecrCount："+memcachedServiceAfterDecrCount+",count:"+count);
				return false;
			}
		}
		//skuIdCountArr为空,不能生成订单
		return false;
	}


	private StoreOrderNew addOrderInfo4InstanceOfSendGoods(StoreOrderNewVo storeOrderNewVo,ShopMemberOrder shopOrder, long storeId, ClientPlatform clientPlatform, String ip) {
		List<ProductNewVo> productNewVoList = storeOrderNewVo.getProductNewVoList();

		//1、组装对象并插入订单
		StoreOrderNew storeOrderNew = insertOrderObj4InstanceOfSendGoods(storeOrderNewVo,storeId,shopOrder,clientPlatform,ip);
//		StoreOrderNew storeOrderNew = insertOrderObj(storeOrderNewVo,storeId,addressId,clientPlatform,ip,restrictionActivityProductId);

		//2、组装对象并插入订单明细
		insertOrderItemObj(storeOrderNew,productNewVoList);

		//3、组装对象并插入订单商品
		insertOrderProductObj(storeOrderNew,productNewVoList);

		//3、返回订单对象
		return storeOrderNew;
	}

	private StoreOrderNew insertOrderObj4InstanceOfSendGoods(StoreOrderNewVo storeOrderNewVo, long storeId, ShopMemberOrder shopOrder, ClientPlatform clientPlatform, String ip) {
		long time = System.currentTimeMillis();

		//1、组装数据
		StoreOrderNew storeOrderNew = new StoreOrderNew();
		storeOrderNew.setStoreId(storeId);
		storeOrderNew.setTotalMoney(storeOrderNewVo.getTotalProductPrice());//商品总价格，不含邮费
		if (BizUtil.isNotEmpty (storeOrderNewVo.getCoupon ())) {
			storeOrderNewVo.getCoupon().setTotalMonye(BigDecimal.valueOf(storeOrderNewVo.getTotalProductPrice()));
		}
		storeOrderNew.setType(1);
		storeOrderNew.setShopMemberOrderId(shopOrder.getId());
		storeOrderNew.setTotalPay(storeOrderNewVo.getTotalProductPriceAfterCoupon());//优惠后商品总价格
		storeOrderNew.setPlatformTotalPreferential(storeOrderNewVo.getPlatformTotalPreferential()==null?0:storeOrderNewVo.getPlatformTotalPreferential());//平台优惠
		storeOrderNew.setSupplierTotalPreferential(storeOrderNewVo.getSupplierTotalPreferential()==null?0:storeOrderNewVo.getSupplierTotalPreferential());//商家店铺总优惠
		storeOrderNew.setSupplierPreferential(storeOrderNewVo.getSupplierPreferential()==null?0:storeOrderNewVo.getSupplierPreferential());//商家店铺优惠
		storeOrderNew.setSupplierChangePrice(storeOrderNewVo.getSupplierChangePrice()==null?0:storeOrderNewVo.getSupplierChangePrice());//商家店铺改价
		storeOrderNew.setOriginalPrice(storeOrderNewVo.getOriginalPrice()==null?0:storeOrderNewVo.getOriginalPrice());//优惠后原始待付款金额

		storeOrderNew.setTotalBuyCount(storeOrderNewVo.getTotalBuyCount());

		String receiverName = shopOrder.getReceiverName();
		String receiverPhone = shopOrder.getReceiverPhone();
		String receiverAddress = shopOrder.getReceiverAddress();
		storeOrderNew.setExpressInfo(Joiner.on(", ").join(receiverName, receiverPhone, receiverAddress));//邮寄信息
		storeOrderNew.setExpressName(receiverName);//收件人姓名
		storeOrderNew.setExpressPhone(receiverPhone);//收件人号码
		storeOrderNew.setExpressAddress(receiverAddress);//收件人地址
		UserNew supplier = storeOrderNewVo.getSupplier();
		if(supplier != null){//母订单是没供应商的
			storeOrderNew.setSupplierId(supplier.getId());
			storeOrderNew.setLOWarehouseId(supplier.getLowarehouseId());//仓库ID
		}
		storeOrderNew.setParentId(0L);
		storeOrderNew.setRemark("");//备注
		storeOrderNew.setStatus(StoreOrderNew.status_normal);
		storeOrderNew.setHasWithdrawed(1);//默认未提现
		storeOrderNew.setCreateTime(time);
		storeOrderNew.setUpdateTime(time);
		storeOrderNew.setOrderType(StoreOrderNew.OrderType_normal);
		storeOrderNew.setCoinUsed(0);//使用玖币的金额
		storeOrderNew.setRestrictionActivityProductId(0L);//限购活动商品id

		//计算运费
//		expressModelService.countOrderExpress(storeOrderNewVo,storeOrderNew,addressId);
		expressModelService.countOrderExpress4InstantOfSendGoods(storeOrderNewVo, storeOrderNew, retrieveProvinceOfAddress(receiverAddress));

//    	storeOrderNew.setTotalMarketPrice(originalMarketPrice);//市场价格
		//计算订单支付过期时间
		JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
		int expiredTime = 24 * 60;
		for(Object obj : jsonArray) {
			expiredTime = (int) ((JSONObject)obj).get("overdueMinutes");
			break;
		}

		//获取对应店铺的地推人员ID和上级IDS，封装进订单
		Wrapper<StoreBusiness> wrapper = new EntityWrapper<StoreBusiness>().eq("id", storeId);
		List<StoreBusiness> selectList = storeMapper.selectList(wrapper);
		if(selectList.size()>0){
			StoreBusiness storeBusinessNew = selectList.get(0);
			storeOrderNew.setGroundUserId(storeBusinessNew.getGroundUserId());
			storeOrderNew.setSuperIds(storeBusinessNew.getSuperIds());
		}
		//限购活动商品订单过期时间为30分钟
		storeOrderNew.setExpiredTime(System.currentTimeMillis() + expiredTime * 60 * 1000);

		storeOrderNew.setPlatform(clientPlatform.getPlatform().getValue());
		storeOrderNew.setPlatformVersion(clientPlatform.getVersion());
		//比较该版本是否3.5以上版本，如果不是，不支持改价功能
		String version = clientPlatform.getVersion();
		if(!StringUtils.isEmpty(version) && ClientUtil.compareTo(version, "3.5.0") < 0){
			storeOrderNew.setLockingOrder(StoreOrderNew.VERSION_UNSUPPORT);
		}
		storeOrderNew.setIp(ip);
		double totalConsume = storeOrderNew.getTotalPay()+storeOrderNew.getTotalExpressMoney();
		//2、添加订单信息
		if(totalConsume > 0.001) {
			storeOrderNew.setOrderStatus(OrderStatus.UNPAID.getIntValue());
			supplierOrderMapper.insert(storeOrderNew);
		}else{
			//如果待支付金额为0元则订单直接改为待支付和进行支付完成的操作
			storeOrderNew.setOrderStatus(OrderStatus.PAID.getIntValue());
			supplierOrderMapper.insert(storeOrderNew);
			//添加订单状态变更日志
			addUpdOrderStateLog(storeOrderNew.getStoreId(),storeOrderNew.getOrderNo());
		}
		//3、设置优惠券已用且记录优惠券使用日志
		updateCouponInfo(storeOrderNewVo.getCoupon(), storeOrderNew.getOrderNo(),storeId);
		return storeOrderNew;
	}

	public static String retrieveProvinceOfAddress(String receiverAddress) {
		int end = - 1;
		if (StringUtils.isNotBlank(receiverAddress)) {
			int i = 0;
			for (char[] c = receiverAddress.toCharArray(); i < c.length - 1; i++) {
				String index = String.valueOf(c[i]);
				if ("市".equals(index) || "省".equals(index) || "区".equals(index)) {
					end = i;
					break;
				}
			}
		}
		String address = end < 0 ? null : receiverAddress.substring(0, end + 1);
		logger.info("收货地址--{}", address);
		return address;
	}


	/**
	 * 添加订单相关信息记录（订单信息、订单明细信息、订单商品信息）
	 * @param storeOrderNewVo
	 * @param addressId
	 * @param storeId
	 * @param clientPlatform
	 * @param ip
	 * @param restrictionActivityProductId
	 * @return
	 */
	private StoreOrderNew addOrderInfo(StoreOrderNewVo storeOrderNewVo,long addressId, long storeId, ClientPlatform clientPlatform, String ip,
			long restrictionActivityProductId) {
		List<ProductNewVo> productNewVoList = storeOrderNewVo.getProductNewVoList();

		//1、组装对象并插入订单
		StoreOrderNew storeOrderNew = insertOrderObj(storeOrderNewVo,storeId,addressId,clientPlatform,ip,restrictionActivityProductId);

		//2、组装对象并插入订单明细
		insertOrderItemObj(storeOrderNew,productNewVoList);

		//3、组装对象并插入订单商品
		insertOrderProductObj(storeOrderNew,productNewVoList);

		//3、返回订单对象
		return storeOrderNew;
	}


	/**
	 * 修改订单的父订单ID
	 * 	UPDATE store_Order SET ParentId=?, UpdateTime=? WHERE OrderNo=?
	 * 	将自己 parentId 设置成自己的 orderNo
	 * @param orderNo
	 * @param parentId
	 */
	private void updateOrderParentId(long orderNo,long parentId) {


		long time = System.currentTimeMillis();
		StoreOrderNew newOrder = new StoreOrderNew();
		newOrder.setOrderNo(orderNo);
		newOrder.setParentId(parentId);
		newOrder.setUpdateTime(time);
		supplierOrderMapper.updateById(newOrder);
		logger.info("修改子订单的父订单ID");
	}
	/**
	 * 添加订单记录
	 * @param storeOrderNewVo
	 * @param storeId
	 * @param addressId
	 * @param clientPlatform
	 * @param ip
	 * @param restrictionActivityProductId
	 * @return
	 */
	private StoreOrderNew insertOrderObj(StoreOrderNewVo storeOrderNewVo,long storeId,long addressId,ClientPlatform clientPlatform,String ip,
			long restrictionActivityProductId) {
		AddressNew address = addressNewService.getAddressById(addressId);
		if(address == null){
			logger.info("根据收货地址ID获取收货地址信息为空，addressId："+addressId);
			throw new RuntimeException("请选择收货地址后再试！");
		}

		long time = System.currentTimeMillis();

		//1、组装数据
		StoreOrderNew storeOrderNew = new StoreOrderNew();
//		storeOrderNew.setOrderNo(Long.parseLong(DateUtil.format(time, "HHmmssSSS") + RandomStringUtils.randomNumeric(8)));//yyyyMMddHHmmssSSS
		storeOrderNew.setStoreId(storeId);
		storeOrderNew.setTotalMoney(storeOrderNewVo.getTotalProductPrice());//商品总价格，不含邮费
//		storeOrderNewVo.getCoupon().setTotalMonye(BigDecimal.valueOf(storeOrderNewVo.getTotalProductPrice()));
		if (BizUtil.isNotEmpty (storeOrderNewVo.getCoupon ())) {
			storeOrderNewVo.getCoupon().setTotalMonye(BigDecimal.valueOf(storeOrderNewVo.getTotalProductPrice()));
		}

		storeOrderNew.setTotalPay(storeOrderNewVo.getTotalProductPriceAfterCoupon());//优惠后商品总价格
//		storeOrderNewVo.getPayPrice();//订单支付价格（优惠后商品总价格+邮费） 建议数据库添加该字段
//		storeOrderNewVo.getCouponOffsetPrice();//优惠券抵扣价格（优惠券抵扣） 建议数据库添加该字段
		/**
		 * V3.6 2018.03.19 改价功能添加字段，添加
		 *
		 */
		storeOrderNew.setPlatformTotalPreferential(storeOrderNewVo.getPlatformTotalPreferential()==null?0:storeOrderNewVo.getPlatformTotalPreferential());//平台优惠
		storeOrderNew.setSupplierTotalPreferential(storeOrderNewVo.getSupplierTotalPreferential()==null?0:storeOrderNewVo.getSupplierTotalPreferential());//商家店铺总优惠
		storeOrderNew.setSupplierPreferential(storeOrderNewVo.getSupplierPreferential()==null?0:storeOrderNewVo.getSupplierPreferential());//商家店铺优惠
		storeOrderNew.setSupplierChangePrice(storeOrderNewVo.getSupplierChangePrice()==null?0:storeOrderNewVo.getSupplierChangePrice());//商家店铺改价
		storeOrderNew.setOriginalPrice(storeOrderNewVo.getOriginalPrice()==null?0:storeOrderNewVo.getOriginalPrice());//优惠后原始待付款金额

		storeOrderNew.setTotalBuyCount(storeOrderNewVo.getTotalBuyCount());
		storeOrderNew.setExpressInfo(address.getExpressInfo());//邮寄信息
		storeOrderNew.setExpressName(address.getReceiverName());//收件人姓名
		storeOrderNew.setExpressPhone(StringUtils.defaultString(address.getTelephone(), address.getFixPhone()));//收件人号码
		storeOrderNew.setExpressAddress(address.getAddrFull());//收件人地址
		UserNew supplier = storeOrderNewVo.getSupplier();
		if(supplier != null){//母订单是没供应商的
			storeOrderNew.setSupplierId(supplier.getId());
			storeOrderNew.setLOWarehouseId(supplier.getLowarehouseId());//仓库ID
		}
		storeOrderNew.setParentId(0L);
    	storeOrderNew.setRemark("");//备注
    	storeOrderNew.setStatus(StoreOrderNew.status_normal);
		storeOrderNew.setHasWithdrawed(1);//默认未提现
		storeOrderNew.setCreateTime(time);
		storeOrderNew.setUpdateTime(time);
		storeOrderNew.setOrderType(StoreOrderNew.OrderType_normal);
    	storeOrderNew.setCoinUsed(0);//使用玖币的金额
    	storeOrderNew.setRestrictionActivityProductId(restrictionActivityProductId);//限购活动商品id

		//计算运费
		expressModelService.countOrderExpress(storeOrderNewVo,storeOrderNew,addressId);



//    	storeOrderNew.setTotalMarketPrice(originalMarketPrice);//市场价格
    	//计算订单支付过期时间
    	JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
    	int expiredTime = 24 * 60;
    	for(Object obj : jsonArray) {
    		expiredTime = (int) ((JSONObject)obj).get("overdueMinutes");
    		break;
    	}

    	//获取对应店铺的地推人员ID和上级IDS，封装进订单
    	Wrapper<StoreBusiness> wrapper = new EntityWrapper<StoreBusiness>().eq("id", storeId);
		List<StoreBusiness> selectList = storeMapper.selectList(wrapper);
		if(selectList.size()>0){
			StoreBusiness storeBusinessNew = selectList.get(0);
			storeOrderNew.setGroundUserId(storeBusinessNew.getGroundUserId());
			storeOrderNew.setSuperIds(storeBusinessNew.getSuperIds());
		}
		//限购活动商品订单过期时间为30分钟
		if(restrictionActivityProductId>0){
			storeOrderNew.setExpiredTime(System.currentTimeMillis() + 30 * 60 * 1000);
		}else{
			storeOrderNew.setExpiredTime(System.currentTimeMillis() + expiredTime * 60 * 1000);
		}

    	storeOrderNew.setPlatform(clientPlatform.getPlatform().getValue());
    	storeOrderNew.setPlatformVersion(clientPlatform.getVersion());
    	//比较该版本是否3.5以上版本，如果不是，不支持改价功能
    	String version = clientPlatform.getVersion();
    	if(!StringUtils.isEmpty(version) && ClientUtil.compareTo(version, "3.5.0") < 0){
    		storeOrderNew.setLockingOrder(StoreOrderNew.VERSION_UNSUPPORT);
    	}
    	storeOrderNew.setIp(ip);
    	double totalConsume = storeOrderNew.getTotalPay()+storeOrderNew.getTotalExpressMoney();
    	//2、添加订单信息
	    if(totalConsume > 0.001) {
	    	storeOrderNew.setOrderStatus(OrderStatus.UNPAID.getIntValue());
	    	supplierOrderMapper.insert(storeOrderNew);
	    }else{
			//如果待支付金额为0元则订单直接改为待支付和进行支付完成的操作
	    	storeOrderNew.setOrderStatus(OrderStatus.PAID.getIntValue());
	    	supplierOrderMapper.insert(storeOrderNew);
			//添加订单状态变更日志
	        addUpdOrderStateLog(storeOrderNew.getStoreId(),storeOrderNew.getOrderNo());
	    }
		// TODO: 2019/1/18 因年前放假 该改动暂时放
//		long timeMillis = System.currentTimeMillis();
//		String string2 = storeOrderNew.getStoreId().toString();
//		int random = (int)(Math.random()*900 + 100);
//		String substring = string2.substring(1, string2.length());
//		String string3=timeMillis + substring + random;
//		Long newOrderNo=null;
//		if (string3.length()>18){
//			String substring1 = string3.substring(0, 19);
//			newOrderNo= Long.parseLong(substring1);
//		}else {
//			newOrderNo = Long.parseLong(string3);
//		}
//		Long orderNo = storeOrderNew.getOrderNo();
//		storeOrderNew.setOrderNo(newOrderNo);
//		supplierOrderMapper.updateOrderId(orderNo,storeOrderNew);

	    //3、设置优惠券已用且记录优惠券使用日志
	    updateCouponInfo(storeOrderNewVo.getCoupon(), storeOrderNew.getOrderNo(),storeId);
		return storeOrderNew;
	}

	/**
	 * 修改优惠券信息，添加优惠券使用日志
	 * @param storeId
	 */
	private void updateCouponInfo(CouponRbRef coupon, long orderNo, long storeId) {
			if(coupon != null) {//该订单使用了优惠券
				long couponId = coupon.getId();
	    		//String[] cidArr = {String.valueOf(couponId)};//couponId.split(",");
	    		//设置优惠券为已使用，
	    		// storeOrderNewService.updateCouponUsed(cidArr , orderNo);

				// 修改优惠券到已经使用
				int rs = commonRefMapper.updateCouponStatusMoney(couponId,orderNo,0,1,coupon.getTotalMonye(),coupon.getSubMoney());
				if(rs==0) {
					throw new RuntimeException("修改优惠券状态失败");
				}
	    		logger.info("该订单使用了优惠券！更改优惠券已使用完成，orderNo"+orderNo+",coupon:"+JSON.toJSONString(coupon));
	    		//添加优惠券使用日志
				StoreCouponUseLogNew storeCouponUseLogNew = new StoreCouponUseLogNew();
				storeCouponUseLogNew.setOrderNo(orderNo);
				storeCouponUseLogNew.setUserId(storeId);
				storeCouponUseLogNew.setCouponId(couponId);
				storeCouponUseLogNew.setStatus(0);
				//代金券实际抵扣金额order.getActualDiscount()
				storeCouponUseLogNew.setActualDiscount(coupon.getSubMoney().doubleValue());
				storeCouponUseLogNew.setCreateTime(System.currentTimeMillis());
				storeCouponUseLogNew.setSupplierId(coupon.getPublishUserId());
				storeOrderNewService.insertCouponUseLog(storeCouponUseLogNew);
	    	}else{
	    		logger.info("该订单未使用优惠券！orderNo"+orderNo);
	    	}
	}


	/**
	 * 添加订单状态变更日志
	 * @param storeId
	 * @param orderNo
	 */
	private void addUpdOrderStateLog(long storeId ,long orderNo) {
		//添加订单状态变更日志
		OrderNewLog orderNewLog = new OrderNewLog();
		orderNewLog.setStoreId(storeId);
		orderNewLog.setOrderNo(orderNo);
		orderNewLog.setNewStatus(OrderStatus.PAID.getIntValue());
		orderNewLog.setOldStatus(OrderStatus.UNPAID.getIntValue());
		orderNewLog.setCreateTime(System.currentTimeMillis());
		orderNewLogMapper.addOrderLog(orderNewLog);
	}



	/**
	 * 组装订单明细记录
	 *   store_OrderProduct 表插入一条记录
	 * @param storeOrderNew
	 * @param productNewVoList
	 * @return
	 */
	private void insertOrderProductObj(StoreOrderNew storeOrderNew,
			 List<ProductNewVo> productNewVoList) {
//		StoreBusiness storeBusiness = userNewService.getStoreBusinessByStoreId(storeOrderNew.getStoreId());
		for(ProductNewVo productNewVo : productNewVoList){
			//根据购买数量获取对应阶梯价格
			double currentLadderPrice = ProductNew.buildCurrentLadderPriceByBuyCount(productNewVo.getProduct().getLadderPriceJson(),productNewVo.getBuyCount());
			long time = System.currentTimeMillis();
			StoreOrderProduct storeOrderProduct = new StoreOrderProduct();
			storeOrderProduct.setOrderNo(storeOrderNew.getOrderNo());
			storeOrderProduct.setBrandId(productNewVo.getBrandId());
			storeOrderProduct.setBuyCount(productNewVo.getBuyCount());
			storeOrderProduct.setCommission(Double.parseDouble(String.valueOf(0)));//提成金额product.getIncome()
			storeOrderProduct.setCreateTime(time);
			storeOrderProduct.setMarketPrice(Double.parseDouble(String.valueOf(0)));
			storeOrderProduct.setMoney(currentLadderPrice);
			storeOrderProduct.setProductId(productNewVo.getProductId());
			storeOrderProduct.setStoreId(storeOrderNew.getStoreId());
			storeOrderProduct.setTotalCommission(Double.parseDouble(String.valueOf(0)));//product.getIncome()*product.getCount()
			storeOrderProduct.setTotalMarketPrice(Double.parseDouble(String.valueOf(0)));//该字段已经废弃  product.getMarketPrice()*product.getCount()
			storeOrderProduct.setTotalMoney(currentLadderPrice * productNewVo.getBuyCount());//
			storeOrderProduct.setImage(productNewVo.getProduct().getMainImg());
			storeOrderProduct.setBrandLogo(productNewVo.getBrand().getBrandIdentity());
			storeOrderProduct.setWarehouseId(storeOrderNew.getLOWarehouseId());
			storeOrderProduct.setStatus(0);//状态:-1删除，0正常
			storeOrderProduct.setUpdateTime(time);
			storeOrderProduct.setCreateTime(time);
			storeOrderProductMapper.insert(storeOrderProduct);
		}
	}




	/**
	 * 根据子订单组装母订单
	 * @param storeOrderNewVoList
	 */
	private StoreOrderNewVo getParentStoreOrderNewVo(List<StoreOrderNewVo> storeOrderNewVoList) {
		List<ProductNewVo> productNewVoList = new ArrayList<ProductNewVo>();
		double couponOffsetPrice = 0;//母订单优惠券抵扣价格（优惠券抵扣）
		double totalProductPrice = 0;//母订单商品总价格（所有商品的商品总价相加（商品总价=商品单价*购买数量））不包含邮费
		double totalProductPriceAfterCoupon = 0;//母订单优惠后商品总价格（商品总价格 -优惠券价格）
		double payPrice = 0;//母订单订单支付价格（优惠后商品总价格+邮费）
		double postage = 0;//母订单总邮费
		for(StoreOrderNewVo storeOrderNewVo : storeOrderNewVoList){
			couponOffsetPrice = couponOffsetPrice + storeOrderNewVo.getCouponOffsetPrice();//订单优惠价格
			totalProductPrice = totalProductPrice + storeOrderNewVo.getTotalProductPrice();
			totalProductPriceAfterCoupon = totalProductPriceAfterCoupon + storeOrderNewVo.getTotalProductPriceAfterCoupon();
			payPrice = payPrice + storeOrderNewVo.getPayPrice();
			postage = postage + storeOrderNewVo.getPostage();
			productNewVoList.addAll(storeOrderNewVo.getProductNewVoList());
		}
		StoreOrderNewVo orderNewVo = storeOrderNewVoList.get(0);
//		AddressNew address = orderNewVo.getAddress();
		//创建订单Vo对象
		StoreOrderNewVo storeOrderNewVo = new StoreOrderNewVo();
//		long time = System.currentTimeMillis();
//		storeOrderNewVo.setOrderNo(Long.parseLong(DateUtil.format(time, "HHmmssSSS") + RandomStringUtils.randomNumeric(8)));//yyyyMMddHHmmssSSS
//		storeOrderNewVo.setSupplier(supplier);//供应商
//		storeOrderNewVo.setBrand(brand);//品牌
//		storeOrderNewVo.setCoupon(coupon);//优惠券
		storeOrderNewVo.setDeliveryTypeName(orderNewVo.getDeliveryTypeName());//配送方式名称
		storeOrderNewVo.setFreePostage(orderNewVo.getFreePostage());//是否免邮，0(不免邮)、1(免邮)
		storeOrderNewVo.setFreePostageName(orderNewVo.getFreePostageName());//是否免邮名称，0(不免邮)、1(免邮)
		storeOrderNewVo.setPostage(postage);//总邮费
		storeOrderNewVo.setTotalBuyCount(orderNewVo.getTotalBuyCount());//订单总购买数量
		storeOrderNewVo.setProductNewVoList(productNewVoList);//订单商品VO列表
		storeOrderNewVo.setCouponOffsetPrice(couponOffsetPrice);//优惠券抵扣价格（优惠券抵扣）
		storeOrderNewVo.setTotalProductPrice(totalProductPrice);//商品总价格（所有商品的商品总价相加（商品总价=商品单价*购买数量））不包含邮费
		storeOrderNewVo.setTotalProductPriceAfterCoupon(totalProductPriceAfterCoupon);//优惠后商品总价格（商品总价格 -优惠券价格）
		storeOrderNewVo.setPayPrice(payPrice);//订单支付价格（优惠后商品总价格+邮费）
		return storeOrderNewVo;
	}

	/**
	 * 组装订单明细记录
	 * 每个sku生成一个订单明细表
	 * @param order
	 * @param productNewVoList
	 * @return
	 */
	private void insertOrderItemObj(StoreOrderNew order, List<ProductNewVo> productNewVoList) {
		StoreBusiness storeBusiness = userNewService.getStoreBusinessByStoreId(order.getStoreId());
		double totalMoney = order.getTotalMoney();//订单金额原价总价，不包含邮费
		double totalPay = order.getTotalPay();//订单金额折后总价，不包含邮费
		double scale = totalPay / totalMoney;//实付款比例
		for(ProductNewVo productNewVo : productNewVoList){
			List<ProductSkuNewVo> skuList = productNewVo.getProductSkuNewVoList();
			for(ProductSkuNewVo skuVo : skuList){
				ProductSkuNew sku = skuVo.getProductSkuNew();
				int buyCount = skuVo.getBuyCount();//购买数量
				long time = System.currentTimeMillis();
				double orderUnitPrice =  productNewVo.getOrderUnitPrice() ;//
				//该sku的实付总价格（计算公式：sku单价 * sku购买数量 * 实付款比例）
				double skuTotalPay =  BizUtil.savepoint(orderUnitPrice * buyCount * scale,2);
				StoreOrderItemNew item = new StoreOrderItemNew();
				item.setOrderNo(order.getOrderNo());
				item.setStoreId(order.getStoreId());
				item.setProductId(sku.getProductId());
				item.setBrandId(productNewVo.getBrandId());
				item.setLOWarehouseId(order.getLOWarehouseId());
				item.setSkuId(sku.getId());
				item.setSupplierId(order.getSupplierId());
				item.setMoney(productNewVo.getOrderUnitPrice());
				item.setUnavalCoinUsed(0);
				item.setTotalMoney(productNewVo.getOrderUnitPrice() * buyCount);
				item.setMarketPrice(productNewVo.getProduct().getMaxLadderPrice());//限购活动商品原来价格
				item.setTotalPay(skuTotalPay);//该sku的实付总价格
				item.setTotalAvailableCommission(storeBusiness.getCommissionPercentage() * item.getTotalPay()/100);
				item.setBuyCount(buyCount);
				item.setExpressMoney(Double.valueOf("0"));
				item.setTotalExpressMoney(Double.valueOf(String.valueOf(0)));
//				item.setMarketPrice(0);//该字段废弃
//				item.setTotalMarketPrice(0);//改字段废弃
				item.setTotalUnavalCoinUsed(0);
				item.setUnavalCoinUsed(0);
				item.setPosition(sku.getPosition());
				item.setSkuSnapshot(buildSkuSnapshot(sku));
				item.setStatus(0);
				item.setCreateTime(time);
				item.setUpdateTime(time);
				orderItemNewMapper.insert(item);
				logger.info("添加订单明细完成"+ "item:"+JSON.toJSONString(item));
			}
		}

	}
		/**
		 * 颜色:冰蓝色  尺码:6XL
		 * @param sku
		 * @return
		 */
	  private String buildSkuSnapshot(ProductSkuNew sku) {
		  return "颜色:"+ sku.getColorName() +"  尺码:"+sku.getSizeName()+" ";
//	        StringBuilder builder = new StringBuilder();
//	        for (ProductPropVO prop : skuProps) {
//	            builder.append(prop.toString());
//	            builder.append("  ");
//	        }
//	        return builder.toString();
	    }


	/**
	 * 删除购物车
	 * @param storeId
	 * @param cartIds
	 */
	private void removeshoppingCart(long storeId, String cartIds) {
		if (StringUtils.isNotEmpty(cartIds)) {
			String[] cartIdStrArr = cartIds.split(",");
			Long[] cartIdLongArr = new Long[cartIdStrArr.length];;
			for (int i = 0; i < cartIdStrArr.length; i++) {
				cartIdLongArr[i] = Long.valueOf(cartIdStrArr[i]);
	        }
			shoppingCartNewServicel.removeCartItems(storeId, CollectionUtil.createSet(cartIdLongArr));
    	}
	}


	@Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createMemberOrder(Integer packageType, Double productPrice, Long storeId, ClientPlatform clientPlatform, String ip) {
		logger.info ("会员套餐下单,创建订单 套餐类型[{}],创建时间[{}].storeId[{}]",packageType,new Date(), storeId);
		/*MemberPackageType packageConfig = MemberPackageType.getByType (packageType);
		if (packageConfig == null) {
			throw BizException.defulat ().msg ("未知的套餐类型");
		}*/
//		DataDictionary dict = dataDictionaryService.getByGroupAndCode (packageConfig.getDictGroupCode (), packageConfig.getDictCode ());
		List<DataDictionary> dictList = dataDictionaryService.getByGroupAndLikeComment (MEMBER_PACKAGE_TYPE_GROUP_KEY, "\"memberType\":\""+packageType+"\"");
		if (dictList.isEmpty ()) {
			throw BizException.defulat ().msg ("未找到会员套餐的支付信息");
		}
		if (dictList.size () > 1) {
			throw BizException.defulat ().msg ("下单失败,找到多个套餐类型");
		}
		DataDictionary dict = dictList.get (0);
		Map<String, Object> result = new HashMap<>(4);

		//小程序未到期,就不能购买
		StoreBusiness user = storeMapper.selectById (storeId);
		if (user == null) {
			throw BizException.defulat ().msg ("未找到用户信息");
		}
		boolean isCanOrder = isCanDoMemberOrder (user);
		if (! isCanOrder) {
			result.put ("orderNo", "");
			result.put ("totalPay", "");
			result.put ("isCanOrder", false);
			result.put ("isPaySuccess", false);
			return result;
		}

		//套餐价格,价格是0的只能购买一次
		double waitPayTotalPrice = Double.parseDouble (dict.getVal ());
		if (waitPayTotalPrice == 0) {
			List<StoreOrderNew> history = storeOrderNewService.findHistorySuccessMemberOrder (storeId, packageType);
			if (! history.isEmpty ()) {
				logger.info ("0元购,用户已购买一次");
				throw BizException.defulat ().msg ("0元订单您已购买过一次,仅支持一次购买");
			}
		}

		StoreOrderNewSon noPayOrder = storeOrderNewService.findNoPayMemberOrder (user, packageType, waitPayTotalPrice, clientPlatform, ip);
		//查询已有未支付订单,如果有则直接返回
		if (noPayOrder != null) {
			logger.info ("会员服务下单,返回用户未支付的订单 orderNo[{}]", noPayOrder.getOrderNo ());
			result.put ("orderNo", noPayOrder.getOrderNo ());
			result.put ("totalPay", noPayOrder.getTotalPay ());
			result.put ("isCanOrder", true);
			return result;
		}

		//创建订单
        StoreOrderNew order = doCreateMemberOrder (clientPlatform, ip, packageType, user, waitPayTotalPrice);

		//碎片
		SuperOrder superOrder = new SuperOrder ();
		superOrder.setCreateTime (new Date ());
		superOrder.setStoreOrderNo (order.getOrderNo ()+"");
		superOrderService.insert (superOrder);

		//如果是0元购的,直接支付成功
		boolean isPaySuccess = false;
		if (waitPayTotalPrice == 0) {
			logger.info ("0元购,直接支付成功 orderNo:{}", order.getOrderNo ());
			order.setPayTime (System.currentTimeMillis ());
			order.setOrderStatus (OrderStatus.SUCCESS.getIntValue ());
			supplierOrderMapper.updateById (order);
			memberService.buyMemberPackageOK (SystemPlatform.STORE.getCode (), storeId, 0D, packageType, order.getOrderNo ()+"");
			isPaySuccess = true;
		}


			//返回
		result.put ("orderNo", order.getOrderNo ());
		result.put ("totalPay", order.getTotalPay ());
		result.put ("isCanOrder", true);
		result.put ("isPaySuccess", isPaySuccess);
		return result;
	}

    /**
     * 创建一个会员订单
     *
     * @param clientPlatform clientPlatform
     * @param ip 用户ip
     * @param memberPackageType 会员套餐类型
     * @param user 用户信息
     * @param waitPayTotalPrice 待支付金额
     * @return com.jiuyuan.entity.newentity.StoreOrderNew
     * @author Charlie
     * @date 2018/8/18 12:38创建订单明细
     */
    private StoreOrderNew doCreateMemberOrder(ClientPlatform clientPlatform, String ip, Integer memberPackageType, StoreBusiness user, Double waitPayTotalPrice) {
		DataDictionary dict = dataDictionaryService.getByGroupAndCode ("memberOrderDiedTime", "memberOrder");

        StoreOrderNew order = buildDefaultMemberOrder (user, waitPayTotalPrice, clientPlatform, ip,Long.parseLong(dict.getVal()));
        Integer rec = supplierOrderMapper.insert (order);
        if (rec != 1) {
            logger.error ("生成订单失败 order:"+JSON.toJSONString(order));
            throw BizException.defulat ().msg ("生成订单失败");
        }
        logger.info ("生成订单成功 orderNo[{}]"+order.getOrderNo ());
        updateOrderParentId (order.getOrderNo (), order.getOrderNo ());

        //创建订单明细
        StoreOrderItemNew item = buildDefaultMemberOrderItem (waitPayTotalPrice, order, memberPackageType);
        rec = orderItemNewMapper.insert (item);
        if (rec != 1) {
            logger.error ("生成订单明细失败 order:"+JSON.toJSONString(item));
            throw BizException.defulat ().msg ("生成订单明细失败");
        }
        logger.info("添加订单明细完成 item[{}]", JSON.toJSONString(item));
        return order;
    }



    /**
	 * 用户是否可以下单
	 *
	 * @param user user
	 * @return boolean
	 * @author Charlie
	 * @date 2018/8/16 19:34
	 */
	private boolean isCanDoMemberOrder(StoreBusiness user) {
		//当前逻辑是: 是会员不可以购买会员服务
		YjjMember member = memberService.findMemberByUserId (SystemPlatform.STORE, user.getId ());
		if (member == null) {
			//没有买过会员
			return true;
		}
		if (member.getMemberLevel () == 0) {
			//普通用户
			return true;
		}
		if (member.getEndTime () < System.currentTimeMillis ()) {
			//会员过期
			return true;
		}
        if (! member.getDelState ().equals (0)) {
        	//这个关闭是运营,不能购买
            return false;
        }
        if (member.getType () == 4) {
		    //如果买的是0元的,还可以非0元的
            return true;
        }
		return false;
	}


    /**
     * 创建一个默认的订单详情对象
     *
     * @param waitPayTotalPrice waitPayTotalPrice
     * @param order order
     * @param memberPackageType memberPackageType
     * @return com.jiuyuan.entity.newentity.StoreOrderItemNew
     * @author Charlie
     * @date 2018/8/16 19:38
     */
	public static StoreOrderItemNew buildDefaultMemberOrderItem( double waitPayTotalPrice, StoreOrderNew order, Integer memberPackageType) {
		//创建订单明细
		Integer buyCount = 1;
		StoreOrderItemNew item = new StoreOrderItemNew();
		item.setOrderNo(order.getOrderNo());
		item.setStoreId(order.getStoreId());
		item.setProductId(0L);
		item.setBrandId(0L);
		item.setLOWarehouseId(0L);
		item.setSkuId(0L);
		item.setSupplierId(0L);
		item.setMoney(waitPayTotalPrice);
		item.setUnavalCoinUsed(0);
		item.setTotalMoney(waitPayTotalPrice);
		//限购活动商品原来价格
		item.setMarketPrice(waitPayTotalPrice);
		//该sku的实付总价格
		item.setTotalPay(waitPayTotalPrice);
		item.setTotalAvailableCommission(0d);
		item.setBuyCount(buyCount);
		item.setExpressMoney(0d);
		item.setTotalExpressMoney(0d);
		item.setTotalUnavalCoinUsed(0);
		item.setUnavalCoinUsed(0);
		item.setPosition("");
		item.setSkuSnapshot("");
		item.setStatus(0);
		item.setCreateTime(System.currentTimeMillis ());
		item.setUpdateTime(System.currentTimeMillis ());
		item.setMemberPackageType (memberPackageType);
		return item;
	}


	/**
	 * 初始化一个默认的会员订单
	 *
	 * @param user 用户
	 * @param waitPayTotalPrice 待支付金额
	 * @param clientPlatform clientPlatform
	 * @param ip ip
	 * @return com.jiuyuan.entity.newentity.StoreOrderNew
	 * @author Charlie
	 * @date 2018/8/15 14:36
	 */
	public static StoreOrderNew buildDefaultMemberOrder(StoreBusiness user, double waitPayTotalPrice, ClientPlatform clientPlatform, String ip,Long diedTime) {
		//1、组装数据
		StoreOrderNew order = new StoreOrderNew();
		order.setStoreId(user.getId ());
		//商品总价格，不含邮费
		order.setTotalMoney(waitPayTotalPrice);
		//优惠后商品总价格
		order.setTotalPay(waitPayTotalPrice);
		//平台优惠
		order.setPlatformTotalPreferential(0d);
		//商家店铺总优惠
		order.setSupplierTotalPreferential(0d);
		//商家店铺优惠
		order.setSupplierPreferential(0d);
		//商家店铺改价
		order.setSupplierChangePrice(0d);
		//优惠后原始待付款金额
		order.setOriginalPrice(waitPayTotalPrice);
		//购买数量
		order.setTotalBuyCount(1);
		//邮寄信息 todo...
		order.setExpressInfo("");
		//收件人姓名
		order.setExpressName(user.getBusinessName ());
		//收件人号码
		order.setExpressPhone(user.getPhoneNumber ());
		//收件人地址(这里填用户的商家地址)
		order.setExpressAddress(user.getBusinessAddress () == null ? "": user.getBusinessAddress ());
		//供应商id
		order.setSupplierId(0L);
		//仓库id
		order.setLOWarehouseId(0L);
		order.setParentId(0L);
		//备注
		order.setRemark("");
		order.setStatus(StoreOrderNew.status_normal);
		order.setOrderStatus (OrderStatus.UNPAID.getIntValue());
		//默认未提现
		order.setHasWithdrawed(1);
		long time = System.currentTimeMillis ();
		order.setCreateTime(time);
		order.setUpdateTime(time);
		order.setOrderType(StoreOrderNew.OrderType_normal);
		//使用玖币的金额
		order.setCoinUsed(0);
		//限购活动商品id
		order.setRestrictionActivityProductId(0L);
		//计算运费
		order.setTotalExpressMoney(0d);
//    	order.setTotalMarketPrice(originalMarketPrice);//市场价格
		//计算订单支付过期时间
		if (diedTime==0L){
			diedTime=MEMBER_ORDER_EXPIRED_TIME_STAMP;
		}
		order.setExpiredTime (time + diedTime);
		order.setPlatform(clientPlatform.getPlatform().getValue());
		String version = clientPlatform.getVersion ();
		order.setPlatformVersion(version);
		//比较该版本是否3.5以上版本，如果不是，不支持改价功能
		if(!StringUtils.isEmpty(version) && ClientUtil.compareTo(version, "3.5.0") < 0){
			order.setLockingOrder(StoreOrderNew.VERSION_UNSUPPORT);
		}
		order.setIp(ip);
		order.setClassify (2);
		return order;
	}


    /**
     * 用户是否可以支付,用户已是会员不让支付
     *
     * @param storeId storeId
     * @return boolean
     * @author Charlie
     * @date 2018/8/18 12:24
     */
    public boolean couldPay4MemberOrder(Long storeId) {
        StoreBusiness user = storeMapper.selectById (storeId);
        return isCanDoMemberOrder (user);
    }


	@Transactional(rollbackFor = Exception.class)
	public StoreOrderNew pay4sendGoodOrder(List<StoreOrderNewVo> storeOrderNewVoList, ShopMemberOrder shopOrder, long storeId,
								  ClientPlatform clientPlatform, String ip, double waitPayTotalPrice, List<ShopMemberOrderItem> items ) {
		ShopMemberOrder updOrder = new ShopMemberOrder();
		updOrder.setId(shopOrder.getId());
		updOrder.setUpdateTime(System.currentTimeMillis());
		Integer updRec = shopMemberOrderNewMapper.updateById(updOrder);
		if (updRec != 1) {
			throw BizException.me("订单状态异常");
		}

		Wrapper<StoreOrderNew> orderQuery = new EntityWrapper<>();
		orderQuery.eq("shop_member_order_id", shopOrder.getId());
		orderQuery.eq("type", 1);
//		orderQuery.in("OrderStatus", Arrays.asList(10, 50, 70));
		List<StoreOrderNew> history = supplierOrderMapper.selectList(orderQuery);
		if (! history.isEmpty()) {
			throw BizException.me("订单已发货");
		}

		//扣除流水
		double operMoney = waitPayTotalPrice;
		YjjStoreBusinessAccountNew account = refundOrderMapper.selectMoney(storeId);
		BigDecimal waitInMoney = BigDecimal.valueOf(account.getWaitInMoney());
		BigDecimal space = BigDecimal.valueOf(operMoney).subtract(waitInMoney);
		if (space.compareTo(BigDecimal.ZERO) > 0) {
			throw BizException.me("资金不够,账户还需要资金" + space);
		}
		int subAccountRec = supplierOrderMapper.subWaitMoney(account.getId(), operMoney);
		if (subAccountRec != 1) {
			throw BizException.me("更新账户失败");
		}

		logger.info("记录流水");
		YjjStoreBusinessAccountLogNew log = new YjjStoreBusinessAccountLogNew();
		log.setAboutOrderNo(shopOrder.getOrderNumber());
		log.setInOutType(1);
		log.setType(PLATFORM_INSTEAD_OF_SEND_GOODS_SUCCESS.getCode());
		log.setOperMoney(operMoney);
		log.setRemainderMoney(BigDecimal.ZERO.subtract(space).doubleValue());
		log.setRemarks(StoreBillEnums.PLATFORM_INSTEAD_OF_SEND_GOODS_SUCCESS.getValue());
		log.setUserId(storeId);
		int logInsertRec = refundOrderMapper.insertInto(log);
		if (logInsertRec != 1) {
			throw BizException.me("记录账户流水失败");
		}

		//生成订单
		logger.info("创建订单");
		StoreOrderNew storeOrderNew = orderCreate4InstanceOfSendGoods(storeOrderNewVoList,shopOrder,storeId,clientPlatform, ip, waitPayTotalPrice);

		long time = System.currentTimeMillis();
		PaymentType paymentType = PaymentType.ACCOUNT_WAIT_MONEY;
		long orderNo = storeOrderNew.getOrderNo();
		// 如果是普通直购订单，则将状态更新为已支付状态
		refundOrderMapper.updateOrderPayStatus(orderNo, null, paymentType.getIntValue(), OrderStatus.PAID.getIntValue(), OrderStatus.UNPAID.getIntValue(), time);

		Map<Long, Integer> productByCount = items.stream().collect(Collectors.toMap(ShopMemberOrderItem::getProductId, ShopMemberOrderItem::getCount, (i, j) -> i + j));
		for (Map.Entry<Long, Integer> pCount : productByCount.entrySet()) {
			refundOrderMapper.updateSaleCount(pCount.getKey(), pCount.getValue());
		}

		//通知供应商发货
		UserNew userNew = userNewMapper.selectById(storeOrderNew.getSupplierId());
		sendText(userNew.getPhone(),userNew.getBusinessName(),ChargeFacade.templateId);
		return storeOrderNew;
	}

	//发送短信
	private void sendText(String phoneNumber, String businessName, int templateNumber) {
		if (phoneNumber == null || "".equals(phoneNumber)) {
			logger.info("该门店号码为空");
		} else {
			JSONArray param = new JSONArray();
			param.add(businessName);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			param.add(sdf.format(new Date()));
			yunXinSmsService.sendNotice(phoneNumber, param, templateNumber);
		}
	}


}
