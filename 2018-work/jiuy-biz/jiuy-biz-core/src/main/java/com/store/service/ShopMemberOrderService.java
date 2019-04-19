package com.store.service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Condition;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.ShopProductMapper;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.service.common.MyStoreActivityService;
import com.jiuyuan.service.common.area.BizCacheKey;
import com.jiuyuan.service.common.area.IBizCacheService;
import com.jiuyuan.util.BizUtil;
import com.qianmi.open.api.qmcs.embedded.websocket.util.StringUtil;
import com.store.dao.mapper.ShopRefundMapper;
import com.util.CallBackUtil;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ShopMemberOrderStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.dao.mapper.supplier.SecondBuyActivityMapper;
import com.jiuyuan.dao.mapper.supplier.TeamBuyActivityMapper;
import com.jiuyuan.entity.order.ShopMemberOrderItem;
import com.jiuyuan.entity.order.ShopMemberOrderLog;
import com.jiuyuan.service.common.IExpressNewService;
import com.jiuyuan.service.common.IMyStoreActivityService;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.SmallPage;
import com.store.dao.mapper.ShopMemberOrderItemMapper;
import com.store.dao.mapper.ShopMemberOrderLogMapper;
import com.store.dao.mapper.ShopMemberOrderMapper;
import com.store.dao.mapper.coupon.ShopMemberCouponMapper;
import com.store.entity.coupon.ShopMemberCoupon;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * <p>
 * 会员订单表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-09-05
 */
@Service
public class ShopMemberOrderService{
	private static final Log logger = LogFactory.get("ShopMemberOrderService");
	/**
	 * 团购活动条件:件数成团
	 */
	public static final int PRODUCT_2_TEAM = 2;
	@Autowired
    private ShopProductService shopProductService;


	@Autowired
	private ProductSKUService productSKUService;

	@Autowired
	private ShopRefundMapper shopRefundMapper;
	@Autowired
	private ShopMemberOrderMapper shopMemberOrderMapper;

	@Autowired
	private ShopMemberOrderItemMapper shopMemberOrderItemMapper;
	
	@Autowired
	private ShopMemberOrderLogMapper shopMemberOrderLogMapper;
	
	@Autowired
	private ShopMemberCouponMapper shopMemberCouponMapper;
	
	@Autowired
	private IExpressNewService supplierExpressService;
	
	@Autowired
	private TeamBuyActivityMapper teamBuyActivityMapper;
	
	@Autowired
	private SecondBuyActivityMapper secondBuyActivityMapper;
	@Autowired
	private IMyStoreActivityService myStoreActivityService;
	
	@Autowired
	private MemcachedService memcachedService;

	@Autowired
	private IBizCacheService bizCacheService;

	@Autowired
	private ProductSkuNewMapper productSkuNewMapper;

	@Autowired
	private ShopProductMapper shopProductMapper;
	
	
	public ShopMemberOrder getMemberOrderByNum(String orderNumber) {
		Wrapper<ShopMemberOrder> wrapper = new EntityWrapper<ShopMemberOrder>();
		wrapper.eq("order_number", orderNumber);
		List<ShopMemberOrder> list = shopMemberOrderMapper.selectList(wrapper);
		if(list.size() > 0){
			if(list.size() > 1){
				logger.info("出现两个订单编号相同的订单，请尽快排查问题！！！！orderNumber："+orderNumber);
			}
			return list.get(0);
		}else{
			return null;
		}
//		ShopMemberOrder shopMemberOrder = new ShopMemberOrder(); 
//		shopMemberOrder.setOrderNumber(orderNumber);
////		return shopMemberOrderMapper.selectOne(shopMemberOrder);
	}
	public ShopMemberOrder getMemberOrderById(long orderId) {
		return shopMemberOrderMapper.selectById(orderId);
	}

	public List<ShopMemberOrderItem> getMemberOrderItemList(long orderId) {
		Wrapper<ShopMemberOrderItem> wrapper = new EntityWrapper<ShopMemberOrderItem>();
		wrapper.eq("order_id", orderId);
		return shopMemberOrderItemMapper.selectList(wrapper);
	}


	/**
	 * 查询售后商品详情
	 */
	public ShopOrderAfterSale selectRefund(Long order,Long skuId){
		ShopOrderAfterSale shopOrderAfterSale = shopRefundMapper.selectRefund(order, skuId);
		return shopOrderAfterSale;

	}

	
	/**
	 * 生成订单
	 * @param shopMemberOrder
	 * @return
	 */
	public int addShopMemberOrder(ShopMemberOrder shopMemberOrder) {
		return shopMemberOrderMapper.insert(shopMemberOrder);
	}
    
	/**
	 * 根据订单ID获取订单详情
	 * @param orderId
	 * @return
	 */
	public  Map<String,Object> getOrderDetailById(long orderId){
		//1、获取订单对象信息
		ShopMemberOrder shopMemberOrder = shopMemberOrderMapper.selectById(orderId);
		//2、对订单对象进行处理后放入Map
		Map<String,Object> shopMemberOrderMap = new HashMap<String,Object>();
		//2.1、订单状态区,对订单状态order_status进行处理,获取订单号，订单创建时间
		shopMemberOrderMap.put("orderId", shopMemberOrder.getId());
        shopMemberOrderMap.put("orderNumber", shopMemberOrder.getOrderNumber());
        shopMemberOrderMap.put("createTime", "创建时间："+parseLongTime2Str(shopMemberOrder.getCreateTime()));
        Integer orderStatus = shopMemberOrder.getOrderStatus();
        Integer cancelReasonType = shopMemberOrder.getCancelReasonType();
        String orderTypeStr = getOrderStatusName(orderStatus, cancelReasonType);
        shopMemberOrderMap.put("orderStatus", orderStatus);//订单状态：订单状态：订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭（已取消、已关闭、已失效）;4:已成功;5:待发货;6:已发货
        shopMemberOrderMap.put("orderStatusName", orderTypeStr);//订单状态名称：订单状态：订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭（已取消、已关闭、已失效）;4:已成功;5:待发货;6:已发货
        
        //2.2、商品列表区，获取订单明细
        Wrapper<ShopMemberOrderItem> shopMemberOrderItemwrapper = new EntityWrapper<ShopMemberOrderItem>().eq("order_id", orderId);
		List<ShopMemberOrderItem> shopMemberOrderItemList =shopMemberOrderItemMapper.selectList(shopMemberOrderItemwrapper);
		
		//2.3、订单价计算区，获取优惠情况，显示总价，运费，实付款，优惠等款项
		shopMemberOrderMap.put("totalMoney", shopMemberOrder.getTotalMoney());//商品总价
		shopMemberOrderMap.put("couponName", shopMemberOrder.getCouponName());//优惠券名称
		shopMemberOrderMap.put("saleMoney", shopMemberOrder.getSaleMoney());//优惠金额
		shopMemberOrderMap.put("expressMoney", shopMemberOrder.getExpressMoney());//运费
		shopMemberOrderMap.put("payMoney", shopMemberOrder.getPayMoney());//实付款
		
		
		//2.3、物流区,
		shopMemberOrderMap.put("receiverName",shopMemberOrder.getReceiverName());//收件人姓名
		shopMemberOrderMap.put("receiverPhone",shopMemberOrder.getReceiverPhone());//收件人电话
		shopMemberOrderMap.put("receiverAddress",shopMemberOrder.getReceiverAddress());//收件人地址
		shopMemberOrderMap.put("expressSupplier",shopMemberOrder.getExpressSupplier());//邮递公司名称
		shopMemberOrderMap.put("expreeSupplierCnname",shopMemberOrder.getExpreeSupplierCnname());//邮递公司中文名称
		shopMemberOrderMap.put("expressNo",shopMemberOrder.getExpressNo());//邮递运单号
		
		
        //2.3.1、设置会员名
        String userNickname = shopMemberOrder.getUserNickname();
        if(StringUtils.isEmpty(userNickname)){
        	Long MemberId = shopMemberOrder.getMemberId();
        	userNickname="游客_"+MemberId;
        }
        shopMemberOrderMap.put("userNickname",userNickname);//v3.3以后版本该字段废弃
        //2.3.2、设置送货方式
        String orderTypeName = "上门自提";//* 订单类型：到店提货或送货上门(0:到店提货;1:送货上门)
		if(shopMemberOrder.getOrderType() == 1){
			orderTypeName = "送货上门";
		}
		shopMemberOrderMap.put("orderTypeName",orderTypeName);
		
        
		//2.4、订单信息区
		//2.4.1、根据cancelReasonType和orderStatus判断显示的时间信息 。V3.3之后版本废弃这些字段
		getDisplayTime(shopMemberOrderMap,shopMemberOrder);
		
		shopMemberOrderMap.put("memberId", shopMemberOrder.getMemberId());//会员ID
		shopMemberOrderMap.put("member_id", shopMemberOrder.getMemberId());//会员ID（V3.3之前版本的android版本会使用）
        
        //2.4、放入map中,和APP端确定以下字段未使用，所有删除
		shopMemberOrderMap.put("id", shopMemberOrder.getId());
		shopMemberOrderMap.put("storeId", shopMemberOrder.getStoreId());
		shopMemberOrderMap.put("count", shopMemberOrder.getCount());
		shopMemberOrderMap.put("summaryImages", shopMemberOrder.getSummaryImages());
		shopMemberOrderMap.put("paymentNo", shopMemberOrder.getPaymentNo());
		shopMemberOrderMap.put("cancelReasonType",cancelReasonType);
		shopMemberOrderMap.put("cancelReason", shopMemberOrder.getCancelReason());
		

		
		//3、订单明细
		shopMemberOrderMap.put("orderItemList", buildOrderItem(shopMemberOrderItemList));
		
		//4、时间列表，格式为："创建时间：2017-08-28 10:33"
		shopMemberOrderMap.put("dateList", buildDateList(shopMemberOrder));
		
		//获取邮寄公司名称列表
		List<Map<String,Object>> allExpressCompanys = supplierExpressService.getAllExpressCompanyNames();
		shopMemberOrderMap.put("allExpressCompanys", allExpressCompanys);//邮寄公司名称列表
		
		return shopMemberOrderMap;
	}
	
	/**
	 * 订单详情时间列表
	 * @param shopMemberOrder
	 * @return
	 */
	private List<Map<String,String>> buildDateList(ShopMemberOrder shopMemberOrder) {
		List<Map<String,String>> dateList = new ArrayList<Map<String,String>>();
		
		String createTime = DateUtil.parseLongTime2Str(shopMemberOrder.getCreateTime());//创建时间：2017-08-28 10:33
		String payTime = DateUtil.parseLongTime2Str(shopMemberOrder.getPayTime());//付款时间：2017-08-28 10:35
		String takeDeliveryTime = DateUtil.parseLongTime2Str(shopMemberOrder.getTakeDeliveryTime());//提货时间：2017-08-28 15:30:23
		String deliveryTime = DateUtil.parseLongTime2Str(shopMemberOrder.getDeliveryTime());//发货时间：2017-08-28 10:33
		String orderStopTime = parseLongTime2Str(shopMemberOrder.getOrderStopTime());//交易关闭时间
		String confirmSignedTime = parseLongTime2Str(shopMemberOrder.getConfirmSignedTime());//确认收货时间
	    
		int orderType = shopMemberOrder.getOrderType();//订单类型：0到店提货;1送货上门
		int orderStatus = shopMemberOrder.getOrderStatus();//订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭（已取消、已关闭、已失效）;4:订单完成;5:待发货;6:已发货
		int cancelReasonType =shopMemberOrder.getCancelReasonType();//取消类型：0无、1会员取消、2商家取消、3系统自动取消
		
		if(orderStatus == 0){//0:待付款;   待付款：指C端客户已下单，但未付款    
			dateList.add(buildMap("创建时间",createTime));//创建时间：2017-08-28 10:33
		}else if(orderStatus == 1){//1:待提货; 		待提货：C端已付款
			dateList.add(buildMap("创建时间",createTime));//创建时间：2017-08-28 10:33
			dateList.add(buildMap("付款时间",payTime));//付款时间：2017-08-28 10:35
		}else if(orderStatus == 2){//2:退款中;
			logger.info("订单状态退款中暂无，请尽快排查问题！！！！");
		}else if(orderStatus == 3){//3:订单关闭（已取消、已关闭、已失效）
			dateList.add(buildMap("创建时间",createTime));//创建时间：2017-08-28 10:33
			switch(cancelReasonType){
			case 0:
				logger.info("订单状态取消类型无，请尽快排查问题！！！！orderStatus："+orderStatus);
				break;
			case 1:
				dateList.add(buildMap("取消时间",orderStopTime));//取消时间：2017-08-28 10:33
			    break;
			case 2:
				dateList.add(buildMap("付款时间",payTime));//付款时间：2017-08-28 10:35
			    dateList.add(buildMap("关闭时间",orderStopTime));//关闭时间：2017-08-28 15:30
			    break;
			case 3:
				dateList.add(buildMap("失效时间",orderStopTime));//失效时间：2017-08-28 10:33
			    break;
			default:
				logger.info("订单状态取消类型无法识别，请尽快排查问题！！！！orderStatus："+orderStatus+"shopMemberOrder:"+JSON.toJSONString(shopMemberOrder));
				break;
			}
		}else if(orderStatus == 4){//;4:订单完成
			dateList.add(buildMap("创建时间",createTime));//创建时间：2017-08-28 10:33
			dateList.add(buildMap("付款时间",payTime));//付款时间：2017-08-28 10:35
			if(orderType == 0){//订单类型：0到店提货;1送货上门
				dateList.add(buildMap("提货时间",takeDeliveryTime));//提货时间：2017-08-28 15:30:23
			}else{
				dateList.add(buildMap("成功时间",confirmSignedTime));//成功时间：2017-08-28 10:33  确认收货时间
			}
		}else if(orderStatus == 5){//5:待发货
			dateList.add(buildMap("创建时间",createTime));//创建时间：2017-08-28 10:33
			dateList.add(buildMap("付款时间",payTime));//付款时间：2017-08-28 10:35
		}else if(orderStatus == 6){//6:已发货
			dateList.add(buildMap("创建时间",createTime));//创建时间：2017-08-28 10:33
			dateList.add(buildMap("付款时间",payTime));//付款时间：2017-08-28 10:35
			dateList.add(buildMap("发货时间",deliveryTime));//发货时间：2017-08-28 10:33
		}
		return dateList;
	}
	
	private Map<String,String> buildMap(String timeName, String time) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("timeName",timeName );//时间名称，格式为：”创建时间”
		map.put("time", time);//时间，格式为：2017-08-28 10:33”
		return map;
	}
	/**
	 * 包装订单详情字段
	 */
	private Object buildOrderItem(List<ShopMemberOrderItem> itemList) {
		List<Map<String,String>> itemMapList = new ArrayList<Map<String,String>>();
		for(ShopMemberOrderItem item : itemList){
			Map<String,String> itemMap= new HashMap<String,String>();
			long shopProductId = item.getShopProductId();
			itemMap.put("shopProductId", String.valueOf(shopProductId));//商家商品ID
			itemMap.put("productId", String.valueOf(item.getProductId()));//平台商品ID
			itemMap.put("name", item.getName());//商品标题
			itemMap.put("summaryImages", item.getSummaryImages());//商品主图
			itemMap.put("color", item.getColor());//商品颜色名称
			itemMap.put("size", item.getSize()); //商品尺码名称
			itemMap.put("price", String.valueOf(item.getPrice()));//商品价格
			itemMap.put("count", String.valueOf(item.getCount()));//商品数量
			itemMap.put("shopProductState", String.valueOf(shopProductService.getShopProductState(shopProductId)));//平台商品状态:0已上架、1已下架、2已删除
			itemMapList.add(itemMap);
		}
		return itemMapList;
	}
	
	/**
	 * 卖家获取订单列表
	 * orderState ：订单状态：-1:全部、0:待付款、1:待提货、4:订单完成、3:订单关闭、5:待发货;6:已发货
	 */
	public SmallPage getMemberOrderList(int current,int size, long storeId,int orderStatus) {
		//1.获取订单对象信息，分页查询
		Page page = new Page(current,size);
		Wrapper<ShopMemberOrder> shopMemberOrderWrapper = new EntityWrapper<ShopMemberOrder>();
		if(orderStatus != -1){
			shopMemberOrderWrapper.eq("order_status", orderStatus);
		}
		shopMemberOrderWrapper.eq("store_id", storeId).orderBy("id",false);
		List<ShopMemberOrder> shopMemberOrderList = shopMemberOrderMapper.selectPage(page, shopMemberOrderWrapper);
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		//2.获取订单对象的详情

		//判断是否是店中店用户
		Integer type=shopMemberOrderMapper.selectType(storeId);


		for(ShopMemberOrder shopMemberOrder:shopMemberOrderList){

			/**
			 * 查看是否是平台商品
			 */
			List<Integer> list = shopMemberOrderMapper.selectIsSupplierPro(shopMemberOrder.getId());


			Map<String,Object> map = new HashMap<String,Object>();
			long orderId = shopMemberOrder.getId();
			//装载订单明细
			Wrapper<ShopMemberOrderItem> shopMemberOrderItemWrapper = new EntityWrapper<ShopMemberOrderItem>().eq("order_id",orderId);
			List<Map<String,Object>> shopMemberOrderItemList = shopMemberOrderItemMapper.selectMaps(shopMemberOrderItemWrapper);
			map.put("orderItemList", shopMemberOrderItemList);
			List<ShopOrderAfterSale> shopOrderAfterSales = shopRefundMapper.selectRefundOrderList(orderId, storeId);
			Integer integer=0;
			for (ShopOrderAfterSale shopOrderAfterSale : shopOrderAfterSales) {
				if (shopOrderAfterSale.getStatus()!=2){
					integer=1;
				}
			}
			if (shopOrderAfterSales.size()>0&&integer!=1){
				map.put("isRefund",1);
				map.put("refundOrderNo","");//售后订单号
			}else {
				map.put("isRefund",0);
			}
			Long appOrderNo=shopRefundMapper.selectIsAppOrder(shopMemberOrder.getId());

			//然后根据订单判断是否是已经代发过
			if (appOrderNo!=null){
				map.put("display",1);
			}

			//这个订单里面一共有多少件
			Integer count = shopMemberOrder.getCount();
			Integer orderCount=0;
			Integer orderStatusType=0;
			if (list.size()==1&&type==1&&appOrderNo==null){
				if (shopOrderAfterSales.size()>0){
					for (ShopOrderAfterSale shopOrderAfterSale : shopOrderAfterSales) {
						orderCount=shopOrderAfterSale.getRefundCount();
						if (shopOrderAfterSale.getStatus()!=1){
							orderStatusType=1;
						}
						if (count>orderCount&&orderStatusType!=1){
							map.put("isProdxy",1);
						}
						}
					}else {

					map.put("isProdxy",1);
				}

			}else {
				map.put("isProdxy",0);
			}

			//装载订单信息字段
			map.put("id",shopMemberOrder.getId());
			map.put("orderId",shopMemberOrder.getId());
			map.put("createTime",DateUtil.parseLongTime2Str( shopMemberOrder.getCreateTime()));
			map.put("orderStatus",shopMemberOrder.getOrderStatus());//订单状态：订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭（已取消、已关闭、已失效）;4:订单完成;5:待发货;6:已发货
			map.put("orderStatusName", getOrderStatusName(shopMemberOrder.getOrderStatus(),shopMemberOrder.getCancelReasonType()));//订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭（已取消、已关闭、已失效）;4:订单完成;5:待发货;6:已发货
			map.put("count",shopMemberOrder.getCount());// 订单商品总件数
			map.put("payMoney",shopMemberOrder.getPayMoney());//实付金额
			map.put("expressMoney",shopMemberOrder.getExpressMoney());//运费金额
			String userNickname = shopMemberOrder.getUserNickname();
			if(StringUtils.isEmpty(userNickname)){
				userNickname = "游客_"+shopMemberOrder.getMemberId();
			}
			map.put("userNickname",userNickname);//会员昵称  V3.3以后版本该字段废弃
			String orderTypeName = "上门自提";//* 订单类型：到店提货或送货上门(0:到店提货;1:送货上门)
			if(shopMemberOrder.getOrderType() == 1){
				orderTypeName = "送货上门";
			}
			map.put("orderTypeName",orderTypeName);
			map.put("receiverName",shopMemberOrder.getReceiverName());//收件人姓名
			map.put("receiverPhone",shopMemberOrder.getReceiverPhone());//收件人电话
			map.put("receiverAddress",shopMemberOrder.getReceiverAddress());//收件人地址
			map.put("expressSupplier",shopMemberOrder.getExpressSupplier());//邮递公司名称
			map.put("expreeSupplierCnname",shopMemberOrder.getExpreeSupplierCnname());//邮递公司中文名称
			map.put("expressNo",shopMemberOrder.getExpressNo());//邮递运单号
			returnList.add(map);
		}
		page.setRecords(returnList);
		return new SmallPage(page);
	}

	/**
	 * 根据订单状态转换订单状态名称
	 * （说明：用于订单列表显示）
	 * @param orderStatus
	 * @return
	 */
	private String getOrderStatusName(int orderStatus,int cancelReasonType) {
		//订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭;4:订单完成;5:待发货;6:已发货
		String orderStatusName = "";
				switch (orderStatus) {
				case 0://0:待付款;
					orderStatusName = "待付款";
					break;
				case 1://1:待提货;
					orderStatusName = "待提货";
					break;
				case 2://2:退款中;
					orderStatusName = "退款中";
					logger.info("订单状态退款中暂无，请尽快排查问题！！！！");
					break;
				case 3://3:订单关闭;
					orderStatusName = getCancelReasonTypeName(cancelReasonType);
					break;
				case 4://4:订单完成
					orderStatusName = "已成功";
					break;
				case 5://5:待发货;
					orderStatusName = "待发货";
					break;
				case 6://6:已发货
					orderStatusName = "已发货";
					break;
				default:
					logger.info("订单状态无法识别，请尽快排查问题！！！！");
					break;
				}
		
		return orderStatusName;
	}
    
	
	/**
	 * 根据订单取消类型获取订单取消名称
	 * （用于订单列表显示订单状态）
	 * 取消类型：0无、1会员取消、2商家取消、3系统自动取消
	 * @param cancelReasonType
	 * @return
	 */
	private String getCancelReasonTypeName(int cancelReasonType) {
		String cancelReasonTypeName = "";
		switch (cancelReasonType) {
		case 0:
			logger.info("订单状态取消类型无，请尽快排查问题！！！！");
			break;
		case 1:
			cancelReasonTypeName = "已取消";
			break;
		case 2:
			cancelReasonTypeName = "已关闭";
			break;
		case 3:
			cancelReasonTypeName = "已失效";
			break;
		default:
			logger.info("订单状态取消类型无法识别，请尽快排查问题！！！！"+"cancelReasonType:"+cancelReasonType);
			break;
		}
		return cancelReasonTypeName;
	}
	
	/**
	 * 生成订单的item
	 * @param shopMemberOrderItem
	 * @return
	 */
	public int addShopMemberOrderItem(ShopMemberOrderItem shopMemberOrderItem) {
		return shopMemberOrderItemMapper.insert(shopMemberOrderItem);
	}
	
	/**
	 * 生成时间样式为"yyyy-MM-dd HH:mm"
	 */
	private String parseLongTime2Str(long time){
		SimpleDateFormat secFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		secFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return secFormatter.format(new Date(time));
	}
	/**
	 * V3.3之后版本废弃这些字段
	 * 根据cancelReasonType和orderStatus判断显示的时间信息
	 *              
	 * @param shopMemberOrderMap
	 * @param shopMemberOrder
	 */
	private void getDisplayTime(Map<String, Object> shopMemberOrderMap, ShopMemberOrder shopMemberOrder) {
		String payTime = parseLongTime2Str(shopMemberOrder.getPayTime());
		String takeDeliveryTime = parseLongTime2Str(shopMemberOrder.getTakeDeliveryTime());
		String orderStopTime = parseLongTime2Str(shopMemberOrder.getOrderStopTime());
		// 订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭;4:订单完成;5:待发货;6:已发货
		Integer orderStatus =shopMemberOrder.getOrderStatus();
		Integer cancelReasonType =shopMemberOrder.getCancelReasonType();
		if(orderStatus == 1){
			shopMemberOrderMap.put("payTime","付款时间："+payTime);
		}else if(orderStatus == 2){
			logger.info("订单状态退款中暂无，请尽快排查问题！！！！");
		}else if(orderStatus == 3){
			switch(cancelReasonType){
			case 0:
				logger.info("订单状态取消类型无，请尽快排查问题！！！！");
				break;
			case 1:
				shopMemberOrderMap.put("orderStopTime","取消时间：" +orderStopTime);
			    break;
			case 2:
				shopMemberOrderMap.put("payTime", "付款时间："+payTime);
			    shopMemberOrderMap.put("orderStopTime", "关闭时间："+orderStopTime);
			    break;
			case 3:
				shopMemberOrderMap.put("orderStopTime","失效时间："+orderStopTime);
			    break;
			default:
				logger.info("订单状态取消类型无法识别，请尽快排查问题！！！！"+"cancelReasonType:"+cancelReasonType);
				break;
			}
		}else if(orderStatus == 4){
			shopMemberOrderMap.put("payTime","付款时间："+payTime);
			shopMemberOrderMap.put("takeDeliveryTime", "提货时间："+takeDeliveryTime);
		}
	}

	/**
	 * 获取订单总数，未付款订单数量，待提货订单数量
	 * @param memberId
	 * @param storeId
	 * @return
	 */
	public Map<String, String> getOrderCount(long memberId, long storeId) {

		Map<String,String> orderCount = new HashMap<String,String>();
		Integer type = shopRefundMapper.selectStoreBusinessNew(storeId);
		if(type==0){//默认户
		Wrapper<ShopMemberOrder> allCountWrapper = new EntityWrapper<ShopMemberOrder>().eq("member_id", memberId)
				.eq("store_id", storeId);
		int allCount = shopMemberOrderMapper.selectCount(allCountWrapper);//全部

		int pendingPaymentCount = shopMemberOrderMapper.countByOrderStatus (storeId, ShopMemberOrder.order_status_pending_payment, memberId);

		Wrapper<ShopMemberOrder> waitTakeGoodsCountWrapper = new EntityWrapper<ShopMemberOrder>().eq("member_id", memberId)
				.eq("store_id", storeId).eq("order_status", ShopMemberOrder.order_status_paid);
		int waitTakeGoodsCount = shopMemberOrderMapper.selectCount(waitTakeGoodsCountWrapper);//待提货
		
		Wrapper<ShopMemberOrder> pendingDeliveryCountWrapper = new EntityWrapper<ShopMemberOrder>().eq("member_id", memberId)
				.eq("store_id", storeId).eq("order_status", ShopMemberOrder.order_status_pending_delivery);
		int pendingDeliveryCount = shopMemberOrderMapper.selectCount(pendingDeliveryCountWrapper);//待发货
				
		Wrapper<ShopMemberOrder> alreadyShippedCountWrapper = new EntityWrapper<ShopMemberOrder>().eq("member_id", memberId)
				.eq("store_id", storeId).eq("order_status", ShopMemberOrder.order_status_already_shipped);
		int alreadyShippedCount= shopMemberOrderMapper.selectCount(alreadyShippedCountWrapper);//已发货、待收货

		Wrapper<ShopMemberOrder> alreadyCompletedWrapper = new EntityWrapper<ShopMemberOrder>().eq("member_id", memberId)
				.eq("store_id", storeId).eq("order_status", ShopMemberOrder.order_status_order_fulfillment);
		int alreadyCompletedCount= shopMemberOrderMapper.selectCount(alreadyCompletedWrapper);//已完成
		
		orderCount.put("allCount", allCount+"");
		orderCount.put("pendingPaymentCount", pendingPaymentCount+"");  //代付款
		orderCount.put("waitTakeGoodsCount", waitTakeGoodsCount+"");   //代提货
		orderCount.put("pendingDeliveryCount", pendingDeliveryCount+"");//待发货
		orderCount.put("alreadyShippedCount", alreadyShippedCount+"");//已发货、待收货
		Integer integer = shopRefundMapper.selectRefundCount(memberId);
		orderCount.put("refundCount",integer.toString());//售后单数量
		int newA = alreadyShippedCount+waitTakeGoodsCount;
		orderCount.put("newAlreadyShippedCount", newA+"");//待发货
		orderCount.put("newalreadyCompletedCount", alreadyCompletedCount+"");//完成

		}else {//店中店用户

            /**
             * allCount:// 全部订单
             * newAlreadyShippedCount // 待收货
             * newalreadyCompletedCount // 已完成
             * pendingPaymentCount // 待付款
             * pendingDeliveryCount  // 代发货
             * refundCount 退款
             */
			Integer orderStatus=null;
			List<String> listMembers = shopRefundMapper.selectInShopMemberId(memberId);
			if (listMembers.size()==0){
				orderCount.put("allCount", 0+"");//全部
				orderCount.put("pendingPaymentCount", 0+"");//待付款
				orderCount.put("pendingDeliveryCount", 0+"");//待发货
				orderCount.put("newAlreadyShippedCount", 0+"");//待收货
				orderCount.put("newalreadyCompletedCount", 0+"");//完成
				orderCount.put("refundCount",0+"");//售后单数量
			    return orderCount;
            }
//			List<Long> listMembers=new ArrayList<>();
//			for (int i = 0; i <unionIdList.size() ; i++) {
//				listMembers.add(Long.parseLong(unionIdList.get(i)));
//			}
			//获取全部订单数量
			Integer allCounts = shopRefundMapper.selectAllOrderCount(listMembers,orderStatus);
			orderCount.put("allCount", allCounts+"");//全部
			//获取代付款订单数量
			orderStatus=0;
			Integer noPayOrders = shopRefundMapper.selectAllOrderCount(listMembers, orderStatus);
			orderCount.put("pendingPaymentCount", noPayOrders+"");//待付款


			//获取代发货订单数量
			orderStatus=5;
			Integer noSendOrders = shopRefundMapper.selectAllOrderCount(listMembers, orderStatus);
			orderCount.put("pendingDeliveryCount", noSendOrders+"");//待发货

			//获取待收货订单数量
			orderStatus=6;
			Integer noResouseOrders = shopRefundMapper.selectAllOrderCount(listMembers, orderStatus);
			orderCount.put("newAlreadyShippedCount", noResouseOrders+"");//待收货

			//获取已完成订单的数量
			orderStatus=4;
			Integer alreadyOver = shopRefundMapper.selectAllOrderCount(listMembers, orderStatus);
			orderCount.put("newalreadyCompletedCount", alreadyOver+"");//完成

            //获取售后单数
            Integer integer = shopRefundMapper.selectRefundCount(memberId);
            orderCount.put("refundCount",integer.toString());//售后单数量
		}
		return orderCount;
	}

	/**
	 * 获取订单列表
	 * @param memberId
	 * @param page 
	 */
	public SmallPage getWxaMemberOrderList(int orderStatus, long storeId, long memberId, Page<ShopMemberOrder> page) {
		List<ShopMemberOrder> shopMemberOrderList = shopMemberOrderMapper.listByOrderStatus (page, storeId, orderStatus, memberId);
		SmallPage smallPage = new SmallPage(page);
		List<Map<String,Object>> shopMemberOrderDataList = new ArrayList<> ();
		for (ShopMemberOrder shopMemberOrder : shopMemberOrderList) {
			Map<String,Object> shopMemberOrderData = new HashMap<> ();
			shopMemberOrderData.put("orderId", shopMemberOrder.getId());
			shopMemberOrderData.put("orderNumber", shopMemberOrder.getOrderNumber());
			shopMemberOrderData.put("orderStatus", shopMemberOrder.getOrderStatus());
			List<String> imgs = new ArrayList<>();
			List<Map<String,Object>> itemList=new ArrayList<>();
			List<ShopMemberOrderItem> orderItems = shopMemberOrderItemMapper.selectList(Condition.create().eq("order_id",shopMemberOrder.getId()));
			int size = orderItems.size();
			Integer countTest=0;
			for (ShopMemberOrderItem orderItem : orderItems) {
				if(imgs.size()<4){
					imgs.add(orderItem.getSummaryImages());
				}
				Map<String,Object> map=new HashMap<>();
				map.put("skuId",orderItem.getProductSkuId());//商品skuId
				map.put("productId",orderItem.getProductId());//商品Id
				map.put("orderCount",orderItem.getCount());//商品的数量
				map.put("orderName",orderItem.getName());//商品名称
				map.put("orderSize",orderItem.getSize());//商品的尺码
				map.put("orderPrice",orderItem.getPrice());//商品单价
				map.put("orderColor",orderItem.getColor());//商品的颜色
				map.put("orderPhoto",orderItem.getSummaryImages());//商品的图片
				ShopOrderAfterSale shopOrderAfterSale = shopRefundMapper.selectRefund(orderItem.getOrderId(), orderItem.getProductSkuId());
				if (shopOrderAfterSale!=null){
					map.put("refundStatus",shopOrderAfterSale.getStatus());//"退款状态：0 默认 退款中，1 退货成功 2 退款失败  3处理中")
                    if (shopOrderAfterSale.getStatus()!=2){
                        countTest=countTest+1;
                    }
				}else {
				    map.put("refundStatus",4);//4代表不在售后中
                }

				itemList.add(map);
			}
			if (countTest==size){
				shopMemberOrderData.put("banner",1);
			}else {
				  shopMemberOrderData.put("banner",0);//0显示  1不显示
			}

			Long confirmSignedTime = shopMemberOrder.getConfirmSignedTime();//订单收货的时间
			if (confirmSignedTime!=0){
				long timeMillis = System.currentTimeMillis();//现在的时间
				if (timeMillis>confirmSignedTime+1000*60*60*24*7){
					shopMemberOrderData.put("fullApplyTime",1);//超过申请售后的时间,不再显示申请售后按钮
				}else{
					shopMemberOrderData.put("fullApplyTime",0);//没超过申请售后的时间,显示申请售后按钮
				}
			}else {
				shopMemberOrderData.put("fullApplyTime",0);//没超过申请售后的时间,显示申请售后按钮
			}



			shopMemberOrderData.put("itemList",itemList);//商品集合
			shopMemberOrderData.put("image", imgs);
			shopMemberOrderData.put("count", shopMemberOrder.getCount());
			shopMemberOrderData.put("price", shopMemberOrder.getPayMoney());
			//订单类型
			shopMemberOrderData.put("orderType", shopMemberOrder.getOrderType());
			
			shopMemberOrderData.put("buyWay", shopMemberOrder.getBuyWay());//购买方式 1：团购 2：秒杀  0:普通
			if (shopMemberOrder.getBuyWay() == ShopMemberOrder.buy_way_tuangou) {
				//判断是否满足参团人数
//				TeamBuyActivity teamAct = teamBuyActivityMapper.findTeamInFullById(shopMemberOrder.getTeamId());
				TeamBuyActivity teamAct = teamBuyActivityMapper.selectById(shopMemberOrder.getTeamId());
				/*
				if(teamAct.getUserCount()-teamAct.getActivityMemberCount()>0){
					//活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
					if (teamAct.haveActivityStatusInt() == 2) {
						shopMemberOrderData.put("showPayButton", 2);//邀请参团
					}
					if (teamAct.haveActivityStatusInt() == 3) {
						shopMemberOrderData.put("showPayButton", 0);//不显示
					}
				}else{
					shopMemberOrderData.put("showPayButton", 1);//付款
				}*/
 				//是否成团成功
				int buttonStatus = isShowButton(teamAct);
				shopMemberOrderData.put("showPayButton", buttonStatus);
				//仅用于团购时邀请参团
				shopMemberOrderData.put("productId", teamAct.getShopProductId());
				//仅用于团购时邀请参团
				shopMemberOrderData.put("productName", teamAct.getShopProductName());
			}
			//订单为待付款类型时
			if(shopMemberOrder.getOrderStatus()==ShopMemberOrder.order_status_pending_payment){
				//获取订单剩余支付时间
				shopMemberOrderData = getPendingPaymentTime(shopMemberOrder,shopMemberOrderData,storeId,memberId);
//				long overdueTime = shopMemberOrder.getCreateTime()+ 24*60*60*1000;
//				long nowTime = System.currentTimeMillis();
//				long time = overdueTime-nowTime;
//				if(time<=0){
//					int record = storeOrderByOrderId(shopMemberOrder.getId(),storeId,memberId);
//					if(record!=1){
//						logger.error("获取订单列表-小程序取消订单orderId:"+shopMemberOrder.getId());
//					}
//					shopMemberOrderData.put("orderStatus", ShopMemberOrder.order_status_order_closed);
//				}else{
//					shopMemberOrderData.put("pendingPaymentTime", time);
//				}
			}
//
			shopMemberOrderDataList.add(shopMemberOrderData);
		}
		smallPage.setRecords(shopMemberOrderDataList);
		return smallPage;
	}


	/**
	 * 前端按钮状态
	 *
	 * @param teamAct 团购活动
	 * @return 0:不显示, 1:付款, 2邀请参团
	 * @author Charlie
	 * @date 2018/7/31 18:06
	 */
	private int isShowButton(TeamBuyActivity teamAct) {
		if (isTeamSuccess (teamAct)) {
			//拼团成功付款
			return 1;
		}

		//活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
		if (teamAct.haveActivityStatusInt() == 2) {
			//2进行中,邀请参团
			return 2;
		}
		//不显示
		return 0;
	}


	/**
	 * 是否拼团成功
	 *
	 * @param teamAct 活动
	 * @return boolean
	 * @author Charlie
	 * @date 2018/7/31 18:00
	 */
	public static boolean isTeamSuccess(TeamBuyActivity teamAct) {
		if (teamAct.getConditionType () == MyStoreActivityService.TEAM_TYPE_PRODUCT) {
			if (teamAct.getMeetProductCount () <= teamAct.getOrderedProductCount ()) {
				return true;
			}
		}
		else {
			if (teamAct.getUserCount () <= teamAct.getActivityMemberCount ()) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 是否拼团 刚好!! 成功
	 * <p>
	 *     成团人数==参与人数,或成团件数==下单件数
	 * </p>
	 *
	 * @param beforeOrderTeam 下单前活动
	 * @param afterOrderTeam 下单后活动
	 * @return boolean
	 * @author Charlie
	 * @date 2018/7/31 18:00
	 */
	public static boolean isTeamSuccessTransient(TeamBuyActivity beforeOrderTeam ,TeamBuyActivity afterOrderTeam) {
		if (beforeOrderTeam.getConditionType () == MyStoreActivityService.TEAM_TYPE_PRODUCT) {
			//以前件数不满,现在件数满了
			return beforeOrderTeam.getOrderedProductCount () < beforeOrderTeam.getMeetProductCount () && afterOrderTeam.getOrderedProductCount () >= afterOrderTeam.getMeetProductCount ();
		}
		else {
			//人数刚好满团
			return afterOrderTeam.getUserCount ().equals (afterOrderTeam.getActivityMemberCount ());
		}
	}



	/**
	 * 获取订单剩余支付时间
	 * @param shopMemberOrder
	 * @param shopMemberOrderData
	 * @param storeId
	 * @param memberId
	 * @return
	 */
	private Map<String, Object> getPendingPaymentTime(ShopMemberOrder shopMemberOrder,Map<String, Object> shopMemberOrderData,long storeId, long memberId) {
			//订单过期时间
			long overdueTime = 0L;
			long nowTime = System.currentTimeMillis();
			int cancelType = 1;
			String cancelReason = "";
			//普通订单
			if ( shopMemberOrder.getBuyWay() == ShopMemberOrder.buy_way_putong) {
				overdueTime = shopMemberOrder.getCreateTime()+ 24*60*60*1000;
				cancelType = 1;
				cancelReason = "您已取消订单";
			}
			//秒杀订单
			if ( shopMemberOrder.getBuyWay() == ShopMemberOrder.buy_way_miaosha) {
				overdueTime = shopMemberOrder.getCreateTime()+ 45*60*1000;
				SecondBuyActivity secondBuyActivity = secondBuyActivityMapper.selectById(shopMemberOrder.getSecondId());
				//活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
				int haveActivityStatusInt = secondBuyActivity.haveActivityStatusInt();
				//判断活动是否结束(并且是手动结束)
				if (haveActivityStatusInt == 3 && secondBuyActivity.getActivityHandEndTime() > 0) {
					overdueTime =-1;//立即结束
					cancelType = 2;
					cancelReason = "商家手动结束活动，关闭订单";
				}
				//秒杀订单剩余保留时间
				long remainTime = nowTime-shopMemberOrder.getCreateTime();
				if (haveActivityStatusInt == 2 && remainTime > 2*60*60*1000) {
					overdueTime =-1;//立即结束
					cancelType = 3;
					cancelReason = "超过两小时未付款，系统自动取消";
				}
			}
			//团购订单
			if ( shopMemberOrder.getBuyWay() == ShopMemberOrder.buy_way_tuangou) {
				TeamBuyActivity teamBuyActivity = teamBuyActivityMapper.selectById(shopMemberOrder.getTeamId());
				//判断团购订单状态
				//活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
				int haveActivityStatusInt = teamBuyActivity.haveActivityStatusInt();
				if (haveActivityStatusInt == 3 && teamBuyActivity.getActivityHandEndTime() == 0) {
					overdueTime = -1;//立即结束
					cancelType = 3;
					cancelReason = "活动已结束，订单已被系统自动取消";
				}
				if (haveActivityStatusInt == 3 && teamBuyActivity.getActivityHandEndTime() > 0) {
					overdueTime =-1;//立即结束
					cancelType = 2;
					cancelReason = "商家手动结束活动，关闭订单";
				}
				if (haveActivityStatusInt == 2){
					overdueTime = teamBuyActivity.getActivityEndTime();
				}
			}
		
			//剩余时间
			long time = overdueTime-nowTime;
			if(time<=0){
				//释放库存,优惠券
				int record = storeOrderByOrderId(shopMemberOrder.getId(),storeId,memberId,cancelType,cancelReason);
				if(record!=1){
					logger.error("获取订单列表-小程序取消订单orderId:"+shopMemberOrder.getId());
				}
				shopMemberOrderData.put("orderStatus", ShopMemberOrder.order_status_order_closed);
			}else{
				shopMemberOrderData.put("pendingPaymentTime", time);
			}
		return shopMemberOrderData;
	}
	/**
	 * 小程序取消订单
	 * @param orderId
	 * @param storeId
	 * @param memberId
	 * @param cancleType 
	 * @param cancelReason 
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public int storeOrderByOrderId(long orderId, long storeId, long memberId, int cancleType, String cancelReason) {
		ShopMemberOrder shopMemberOrder = shopMemberOrderMapper.selectById(orderId);
		int oldStatus = shopMemberOrder.getOrderStatus();
		long time = System.currentTimeMillis();
		shopMemberOrder.setOrderStatus(ShopMemberOrder.order_status_order_closed);
		shopMemberOrder.setCancelReasonType(cancleType);
		shopMemberOrder.setCancelReason(cancelReason);
		shopMemberOrder.setOrderStopTime(time);
		shopMemberOrder.setUpdateTime(time);
		int record = shopMemberOrderMapper.updateById(shopMemberOrder);
		if(record==1){
			//添加log
			addShopMemberOrderLog(memberId,oldStatus,shopMemberOrder);
			//如果有优惠券,将优惠券设置为未使用
			long couponId = shopMemberOrder.getCouponId();
			if(couponId>0){
				ShopMemberCoupon shopMemberCoupon = new ShopMemberCoupon();
				shopMemberCoupon.setId(couponId);
				shopMemberCoupon.setAdminId(0L);
				shopMemberCoupon.setCheckTime(0L);
				shopMemberCoupon.setCheckMoney(0D);
				shopMemberCoupon.setStatus(ShopMemberCoupon.status_normal);
				shopMemberCoupon.setUpdateTime(time);
				int couponRecord = shopMemberCouponMapper.updateById(shopMemberCoupon);
				if(couponRecord!=1){
					logger.error("小程序取消订单:将优惠券设置为未使用couponId:"+couponId);
					throw new RuntimeException("小程序取消订单:将优惠券设置为未使用couponId:"+couponId);
				}
			}

			//团购。手动取消
			if (shopMemberOrder.getTeamId() != null &&  (cancleType == 2 || cancleType == 3) ) {
				TeamBuyActivity teamBuyActivity = teamBuyActivityMapper.selectById(shopMemberOrder.getTeamId());
				if (teamBuyActivity == null) {
					throw new RuntimeException ("未找到活动信息");
				}
				//团购活动结束,不用回滚,团购未结束,回滚缓存库存
				if (teamBuyActivity.haveActivityStatusInt () == 3) {
					return record;
				}
				//3.7.7
//				TeamBuyActivity newTeamBuyActivity = new TeamBuyActivity();
//				newTeamBuyActivity.setId(teamBuyActivity.getId());
//				newTeamBuyActivity.setActivityMemberCount(teamBuyActivity.getActivityMemberCount()-1);
//				teamBuyActivityMapper.updateById(newTeamBuyActivity);

				//参与人数-1
				logger.info("取消订单,团购库存回滚...");
				Integer type = teamBuyActivity.getConditionType ();
				Integer buyCount = shopMemberOrder.getCount ();
				int dRec = myStoreActivityService.teamActDecreaseUserOrProduct (teamBuyActivity.getId (), type, buyCount);
				if (dRec != 1) {
					throw new RuntimeException ("取消订单,减去团购活动参与人数,成团件数失败 id:"+teamBuyActivity.getId ());
				}
				logger.info ("取消订单,团购库存回滚===>更新商品团购人数,参团件数成功");

				//缓存中相关信息修改
				/*String groupKey = MemcachedKey.GROUP_KEY_activityTeamBuy;
				String key = "_teamBuyActivityId_"+String.valueOf(teamBuyActivity.getId());
				Object obj = memcachedService.getCommon(groupKey, key);
				logger.info("取消团购订单取查询缓存数据groupKey+key:"+groupKey+key);
				if(obj != null){
					logger.info("取消团购订单前缓存中剩余活动商品数量obj:"+obj);
					int count = 1;
					int memcachedcount = Integer.valueOf(String.valueOf (obj).trim());
					if(memcachedcount == 0){
						count = 2;
					}
					logger.info("自动----取消秒杀订单前缓存中剩余活动商品数量count"+count+",memcachedcount:"+memcachedcount);
					memcachedService.incrCommon(groupKey, key, count);
					logger.info("取消团购订单缓存加1");
					logger.info("取消团购订单后缓存中剩余活动商品数量obj:"+memcachedService.getCommon(groupKey, key));
				}else{
					logger.info("取消团购订单增加缓存商品数量失败obj:"+obj);
				}*/
				String key = BizCacheKey.SHOP_TEAM_ACTIVITY_INVENTORY + teamBuyActivity.getId ();
				String inventory = bizCacheService.get (key);
				if (StringUtils.isBlank (inventory)) {
					logger.info("取消团购订单增加缓存商品数量失败obj:"+inventory);
				}
				else {
					Long rec = bizCacheService.incr (key, buyCount);
					logger.info ("取消团购订单增加缓存商品数量成功 回滚库存前[{}].回滚库存数[{}].回滚库存后[{}]",inventory, buyCount, rec);
				}


			}
			//秒杀 手动取消
			if (shopMemberOrder.getSecondId() != null && (cancleType == 2 || cancleType == 3)) {
				SecondBuyActivity secondBuyActivity = secondBuyActivityMapper.selectById(shopMemberOrder.getSecondId());
				//活动结束,不用回滚缓存库存,团购未结束,回滚缓存库存
				if (secondBuyActivity.haveActivityStatusInt () == 3) {
					return record;
				}
//				SecondBuyActivity newSecondBuyActivity = new SecondBuyActivity();
//				newSecondBuyActivity.setId(secondBuyActivity.getId());
				//参与人数-1
//				newSecondBuyActivity.setActivityMemberCount(secondBuyActivity.getActivityMemberCount()-1);
//				logger.info("参与人数减1");
//				secondBuyActivityMapper.updateById(newSecondBuyActivity);
				int rec = myStoreActivityService.secondActDecreaseJoinUser (secondBuyActivity.getId (), shopMemberOrder.getCount ());
				if (rec != 1) {
					throw new RuntimeException ("取消订单,减去秒杀活动参与人数,下单件数失败 id:"+secondBuyActivity.getId ());
				}
				//缓存中相关信息修改
				/*String groupKey = MemcachedKey.GROUP_KEY_activitySecondBuy;
				String key = "_secondBuyActivityId_"+String.valueOf(secondBuyActivity.getId());
				Object obj = memcachedService.getCommon(groupKey, key);
				logger.info("取消秒杀订单取查询缓存数据groupKey+key:"+groupKey+key);
				if (obj != null) {
					logger.info("取消秒杀订单前缓存中剩余活动商品数量obj:"+obj);
					int count = 1;
					String memcachedcountStr = (String)obj;
					int memcachedcount = Integer.valueOf(memcachedcountStr.trim());
					if(memcachedcount == 0){
						count = 2;
					}
					logger.info("自动----取消秒杀订单前缓存中剩余活动商品数量count"+count+",memcachedcount:"+memcachedcount);
					memcachedService.incrCommon(groupKey, key, count);
					logger.info("取消秒杀订单缓存加1成功！");
					logger.info("取消秒杀订单后缓存中剩余活动商品数量obj:"+memcachedService.getCommon(groupKey, key));
				}else{
					logger.info("取消秒杀订单失败obj:"+obj);
				}*/
				String key = BizCacheKey.SHOP_SECOND_ACTIVITY_INVENTORY + secondBuyActivity.getId ();
				String inventory = bizCacheService.get (key);
				if (StringUtils.isBlank (inventory)) {
					logger.info("取消秒杀订单失败inventory:"+inventory);
				}
				else {
					Long decr = bizCacheService.incr (key, shopMemberOrder.getCount ());
					logger.info ("取消秒杀订单增加缓存商品数量成功 回滚库存前[{}].回滚库存后[{}]",inventory, decr);
				}
			}
		}
		return record;
	}




	/**
	 * 小程序订单详情
	 */
	public Map<String, Object> getOrderInfoByOrderId(long orderId, long storeId, long memberId) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ShopMemberOrder shopMemberOrder = shopMemberOrderMapper.findWxaOrderDetailById(orderId);
//		ShopMemberOrder shopMemberOrder = shopMemberOrderMapper.selectById(orderId);
		Map<String,Object> shopMemberOrderInfo = new HashMap<String,Object>();
		shopMemberOrderInfo.put("orderNumber", shopMemberOrder.getOrderNumber());
		shopMemberOrderInfo.put("createTime", simpleDateFormat.format(new Date(shopMemberOrder.getCreateTime())));
		shopMemberOrderInfo.put("orderStatus", shopMemberOrder.getOrderStatus());
		
		//购买方式
		if (shopMemberOrder.getBuyWay()== ShopMemberOrder.buy_way_miaosha) {
			shopMemberOrderInfo.put("buyWay", 2);
		}
		if (shopMemberOrder.getBuyWay()== ShopMemberOrder.buy_way_tuangou) {
			shopMemberOrderInfo.put("buyWay", 1);
		}
		if (shopMemberOrder.getBuyWay()== ShopMemberOrder.buy_way_putong) {
			shopMemberOrderInfo.put("buyWay", 0);
		}
		if (shopMemberOrder.getOrderStatus() == 3) {
			shopMemberOrderInfo.put("orderStopTime", simpleDateFormat.format(new Date(shopMemberOrder.getOrderStopTime())));//订单关闭时间
		}
		//待发货
		if(shopMemberOrder.getOrderType()==ShopMemberOrder.order_type_delivery){
			shopMemberOrderInfo.put("orderType", "送货上门");
			shopMemberOrderInfo.put("orderTypeNum", 1);
		}else if(shopMemberOrder.getOrderType()==ShopMemberOrder.order_type_get_product_at_store){
			shopMemberOrderInfo.put("orderType", "到店提货");
			shopMemberOrderInfo.put("orderTypeNum", 0);
		}
		if (shopMemberOrder.getOrderStatus() == ShopMemberOrder.order_status_already_shipped) {
			shopMemberOrderInfo.put("deliveryTime",  simpleDateFormat.format(new Date(shopMemberOrder.getDeliveryTime())));//发货时间
			}
		if (shopMemberOrder.getOrderStatus() == ShopMemberOrder.order_status_order_fulfillment) {
			shopMemberOrderInfo.put("deliveryTime",simpleDateFormat.format(new Date(shopMemberOrder.getDeliveryTime())) );//发货时间
			shopMemberOrderInfo.put("orderFinishTime", simpleDateFormat.format(new Date(shopMemberOrder.getOrderFinishTime())));//确认收货时间  交易完成时间
			}
		shopMemberOrderInfo.put("paymentNo", shopMemberOrder.getPaymentNo());
		shopMemberOrderInfo.put("payTime", simpleDateFormat.format(new Date(shopMemberOrder.getPayTime())));
		shopMemberOrderInfo.put("couponName", shopMemberOrder.getCouponName());
		
//		Wrapper<ShopMemberOrderItem> wrapper = new EntityWrapper<ShopMemberOrderItem>().eq("order_id", orderId);
//		List<ShopMemberOrderItem> shopMemberOrderItemList = shopMemberOrderItemMapper.selectList(wrapper);
		List<ShopMemberOrderItem> shopMemberOrderItemList = shopMemberOrderItemMapper.findOrderItemByOrderId(orderId);
		List<Map<String,String>> shopMemberOrderItemInfoList = new ArrayList<Map<String,String>>();
		for (ShopMemberOrderItem item : shopMemberOrderItemList) {
			Map<String,String> itemMap = new HashMap<String,String>();
			long shopProductId =  item.getShopProductId();
			itemMap.put("shopProductId", String.valueOf(item.getShopProductId()));
			itemMap.put("name", item.getName());
			itemMap.put("color", item.getColor());
			itemMap.put("size", item.getSize());
			itemMap.put("summaryImage", item.getSummaryImages());
			itemMap.put("count", String.valueOf(item.getCount()));
			boolean isOk = true;

			itemMap.put("isStockEnough", isOk+"");
			
			//活动价
			if (shopMemberOrder.getBuyWay()== ShopMemberOrder.buy_way_miaosha) {
                itemMap.put("activePrice", secondBuyActivityMapper.selectById(shopMemberOrder.getSecondId()).getActivityPrice()+"");
			}
			if (shopMemberOrder.getBuyWay()== ShopMemberOrder.buy_way_tuangou) {
                itemMap.put("activePrice", teamBuyActivityMapper.selectById(shopMemberOrder.getTeamId()).getActivityPrice()+"");
			}
			
			//原价
			itemMap.put("price", String.valueOf(item.getPrice()));
			itemMap.put("shopProductState", shopProductService.getShopProductState(shopProductId));
			shopMemberOrderItemInfoList.add(itemMap);
		}
		shopMemberOrderInfo.put("shopMemberOrderItemInfoList", shopMemberOrderItemInfoList);
		shopMemberOrderInfo.put("allCount", shopMemberOrder.getCount());
		shopMemberOrderInfo.put("totalMoney", shopMemberOrder.getTotalMoney());
		shopMemberOrderInfo.put("saleMoney", shopMemberOrder.getSaleMoney());
		shopMemberOrderInfo.put("payMoney", shopMemberOrder.getPayMoney());
		
		return shopMemberOrderInfo;
	}

	/**
	 * 判断订单状态是否改变
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	public boolean checkMemberOrderStatus(long orderId, int orderStatus) {
		Integer shopMemberOrderStatus = shopMemberOrderMapper.findShopMemberOrderStatus(orderId);
		return shopMemberOrderStatus==orderStatus;
	}
	/**
	 * 修改订单为已支付状态
	 */
	@Transactional(rollbackFor = Exception.class)
	public int updateOrderAlreadyPayStatus(ShopMemberOrder shopMemberOrder, String paymentNo,
			PaymentType paymentType) {
		if(shopMemberOrder.getOrderStatus() != ShopMemberOrder.order_status_pending_payment){
			logger.info("订单不是待支付状态不能改为已支付状态，请排查问题！！！");
			return -1;
		}
		ShopMemberOrder shopMemberOrderUpd = new ShopMemberOrder();
		shopMemberOrderUpd.setId(shopMemberOrder.getId());
		if (ShopMemberOrder.order_type_delivery == shopMemberOrder.getOrderType()) {
			//待发货
			shopMemberOrderUpd.setOrderStatus(ShopMemberOrder.order_status_pending_delivery);
		}
		if (ShopMemberOrder.order_type_get_product_at_store == shopMemberOrder.getOrderType()) {
			//待提货
			shopMemberOrderUpd.setOrderStatus(ShopMemberOrder.order_status_paid);
		}
		shopMemberOrderUpd.setPaymentNo(paymentNo);
		shopMemberOrderUpd.setPaymentType(ShopMemberOrder.paymentType_wxa);
     	long time = System.currentTimeMillis();
		//付款时间
		shopMemberOrderUpd.setPayTime(time);
		shopMemberOrderUpd.setUpdateTime(time);
		int record = shopMemberOrderMapper.updateById(shopMemberOrderUpd);
		if(record!=1){
			throw new RuntimeException("修改订单为已支付状态shopMemberOrderId:"+shopMemberOrderUpd.getId());
		}
		
		addShopMemberOrderLog(shopMemberOrder.getMemberId(), shopMemberOrder.getOrderStatus(), shopMemberOrderUpd);
		return record;
	}
	
	/**
	 * 修改订单支付标识码
	 * @param orderId
	 * @param prepay_id
	 */
	public int updatePrepayId(long orderId, String prepay_id) {
		ShopMemberOrder shopMemberOrderUpd = new ShopMemberOrder();
		shopMemberOrderUpd.setId(orderId);
		shopMemberOrderUpd.setPayFormId(prepay_id);
		return shopMemberOrderMapper.updateById(shopMemberOrderUpd);
	}

	/**
	 * 添加log
	 */
	private void addShopMemberOrderLog(long memberId, int oldStatus, ShopMemberOrder shopMemberOrder){
		ShopMemberOrderLog shopMemberOrderLog = new ShopMemberOrderLog();
		shopMemberOrderLog.setOldStatus(oldStatus);
		shopMemberOrderLog.setMemberId(memberId);
		shopMemberOrderLog.setNewStatus(shopMemberOrder.getOrderStatus());
		shopMemberOrderLog.setOrderId(shopMemberOrder.getId());
		shopMemberOrderLog.setCreateTime(shopMemberOrder.getUpdateTime());
		int record = shopMemberOrderLogMapper.insert(shopMemberOrderLog);
		if(record!=1){
			throw new RuntimeException("添加log失败shopMemberOrderId:"+shopMemberOrderLog.getOrderId());
		}
	}
	/**
     * 申请小程序会员订单发货
     * @param orderNo
     * @param storeId
     */
	public Map<String,Object> applyDelivery(Long orderNo, long storeId) {
		Map<String,Object> map = new HashMap<String,Object>();
		//查询订单信息，获取邮寄信息
		ShopMemberOrder shopMemberOrder = shopMemberOrderMapper.selectById(orderNo);
		//
		map.put("orderNo", orderNo);
		map.put("orderNumber", shopMemberOrder.getOrderNumber());
		String orderStatus = "";
		switch (shopMemberOrder.getOrderStatus()) {
		case 0:orderStatus="待付款";
			break;
		case 1:orderStatus="待提货";
			break;
		case 2:orderStatus="退款中";
			break;
		case 3:orderStatus="订单关闭";
			break;
		case 4:orderStatus="订单完成";
			break;
		case 5:orderStatus="待发货";
			break;
		case 6:orderStatus="已发货";
			break;
		default:
			break;
		}
		map.put("orderStatus", orderStatus);
		String receiverName = shopMemberOrder.getReceiverName();
		String receiverPhone = shopMemberOrder.getReceiverPhone();
		String receiverAddress = shopMemberOrder.getReceiverAddress();

		map.put("receiverName",receiverName);
		map.put("receiverPhone",receiverPhone);
		map.put("receiverAddress",receiverAddress);
		//快递商列表
		//获取邮寄公司名称列表
		List<Map<String,Object>> allExpressCompanys = supplierExpressService.getAllExpressCompanyNames();
		map.put("allExpressCompanys", allExpressCompanys);//邮寄公司名称列表
		return map;
	}
	/**
	 * 提交发货按钮
	 * @param memberOrderNo
	 * @param storeId
	 * @param engName
	 * @param cnName
	 */
	@Transactional(rollbackFor =Exception.class)
	public void submitDelivery(Long memberOrderNo, long storeId, String engName, String cnName,String expressNo) {
		
		//校验该订单是否是待发货
		ShopMemberOrder shopMemberOrder = getShopMemberOrderByOrderNo(memberOrderNo);
		if(null != shopMemberOrder &&
		   shopMemberOrder.getOrderStatus() != ShopMemberOrderStatus.UNDELIVERED.getIntValue() &&
		   shopMemberOrder.getOrderStatus() != ShopMemberOrderStatus.DELIVERED.getIntValue() &&
		   shopMemberOrder.getOrderStatus() != ShopMemberOrderStatus.FINISH_ORDER.getIntValue()){
			logger.info("该订单不在发货期间不能进行发货！");
			throw new RuntimeException("该订单不在发货期间不能进行发货！");
		}
		if(shopMemberOrder.getStoreId() != storeId){
			logger.info("该订单所属人不是该用户！");
			throw new RuntimeException("该订单所属人不是该用户！");
		}
		//更改订单状态并添加快递信息
	    updateOrderStatusAndExpressInfo(memberOrderNo,ShopMemberOrderStatus.DELIVERED.getIntValue(),engName,cnName,expressNo);

		Integer type = shopRefundMapper.selectStoreBusinessNew(storeId);
		if (type.equals(1)){
			Long shopMemberOrderId = shopMemberOrder.getId();
			logger.info("添加店中店订单店家={}自己发货, 15天后的定时结算(shopMemberOrderId={})任务", storeId, shopMemberOrderId);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("shopMemberOrderId", shopMemberOrderId);
			jsonObject.put("type", 0);
			CallBackUtil.send(jsonObject.toJSONString(), "/order/order/storeOrder/registerSendGoodAfter15DaysJob", "get");
		}
	}
	
	/**
	 * 更改订单状态并填写快递信息
	 * @param memberOrderNo
	 */
	public void updateOrderStatusAndExpressInfo(Long memberOrderNo,int orderStatus, String engName, String cnName,String expressNo) {
		ShopMemberOrder shopMemberOrder = new ShopMemberOrder();
		shopMemberOrder.setId(memberOrderNo);
		shopMemberOrder.setOrderStatus(orderStatus);
		shopMemberOrder.setExpressSupplier(engName);
		shopMemberOrder.setExpreeSupplierCnname(cnName);
		shopMemberOrder.setExpressNo(expressNo);
		shopMemberOrder.setDeliveryTime(System.currentTimeMillis());
		
		shopMemberOrderMapper.updateById(shopMemberOrder);
		
	}
	/**
	 * 获取小程序订单
	 * @param memberOrderNo
	 * @return
	 */
	public ShopMemberOrder getShopMemberOrderByOrderNo(Long memberOrderNo) {
		return shopMemberOrderMapper.selectById(memberOrderNo);
	}
	

//	/**
//	    * 判断活动商品是否售罄
//	    * @param activityProductCount
//	    * @param buyActivityMemberCount
//	    * @return
//	    */
//		public boolean isSaleOver(SecondBuyActivity secondBuyActivity) {
//			int activityProductCount = secondBuyActivity.getActivityProductCount();//活动商品数量
//			int activityMemberCount = secondBuyActivity.getActivityMemberCount();//参与人数
//			int surplusActivityProductCount = activityProductCount - activityMemberCount;//剩余活动商品数量
//			if (surplusActivityProductCount <= 0) {
//				return true;
//			}else{
//				return false;
//			}
//		}

		/**
		 * 判断秒杀订单保留时间是否过期
		 * @param shopMemberOrder
		 * @return
		 */
		 public  boolean isOverTime(ShopMemberOrder shopMemberOrder) {
			 long overdueTime = shopMemberOrder.getCreateTime()+ 2*60*60*1000;
				long nowTime = System.currentTimeMillis();
				long time = overdueTime-nowTime;
				if (time < 0) {
					return true;
				}else{
					return false;
				}
		}


	public List<ShopMemberOrderItem> findProductListByOrderId(long orderId) {
		return shopMemberOrderMapper.findProductListByOrderId(orderId);
	}
}