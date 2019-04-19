package com.jiuyuan.service.common;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.ShopMemberOrderStatus;
import com.jiuyuan.dao.mapper.supplier.*;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.util.*;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.util.CallBackUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admin.core.support.HttpKit;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.mapper.shop.StoreOrderLogMapper;
import com.jiuyuan.dao.mapper.shop.SupplierOrderProductMapper;
import com.jiuyuan.entity.OrderNewLog;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.logistics.LOWarehouse;
//import com.jiuyuan.entity.newentity.StoreOrder;
import com.jiuyuan.entity.storeorder.StoreOrderLog;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class OrderNewService implements IOrderNewService {

	private static final Log logger = LogFactory.get("供应商订单Service");

	@Autowired
	private ShopMemberOrderNewMapper shopMemberOrderNewMapper;
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;

	@Autowired
	private OrderItemNewMapper orderItemNewMapper;

	@Autowired
	private StoreMapper storeMapper;

	@Autowired
	private ProductNewMapper productNewMapper;

	@Autowired
	private IProductNewService productNewService;

	@Autowired
	private IAddressNewService addressNewService;

	@Autowired
	private ILOWarehouseNewService loWarehouseService;

	@Autowired
	private IBrandNewService brandService;

	@Autowired
	private StoreExpressInfoMapper storeExpressInfoMapper;

	@Autowired
	private SupplierExpressMapper supplierExpressMapper;

	@Autowired
	private ProductSkuNewMapper productSkuNewMapper;

	@Autowired
	private SupplierOrderProductMapper supplierOrderProductMapper;

	@Autowired
	private GroundBonusGrantFacade groundBonusGrantFacade;

	@Autowired
	private GroundConditionRuleService groundConditionRuleService;

	@Autowired
	private OrderNewLogMapper orderNewLogMapper;

	@Autowired
	private StoreOrderLogMapper storeOrderLogMapper;

	@Autowired
	private AddressNewMapper addressNewMapperp;

	@Autowired
	private RefundOrderMapper refundOrderMapper;

	@Autowired
	private UserNewMapper userNewMapper;

	@Autowired
	private ShopNotificationMapper shopNotificationMapper;

	@Autowired
	private SupplierChangeOrderPriceActionLogMapper supplierChangeOrderPriceActionLogMapper;

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#getSupplierOrderList(long, long, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public List<StoreOrderNew> getSupplierOrderList(long userId, long orderNo, int orderStatus, String phoneNumber, String clothesNumbers,
			long updateTimeStart, long updateTimeEnd, Page<StoreOrderNew> page) {
			Wrapper<StoreOrderNew> wrapper =
					new EntityWrapper<StoreOrderNew>().eq("status", 0).eq("supplierId",userId).orderBy("UpdateTime", false)
					.gt("OrderStatus", OrderStatus.UNPAID.getIntValue()).lt("OrderStatus", OrderStatus.CLOSED.getIntValue()).gt("ParentId", 0);
			if(orderNo>0){
				wrapper.like("OrderNo", orderNo+"");
			}
			if(orderStatus==OrderStatus.PAID.getIntValue() || orderStatus==OrderStatus.DELIVER.getIntValue()){
				wrapper.eq("OrderStatus", orderStatus);
			}
			if(orderStatus==OrderStatus.REFUNDED.getIntValue()){
				wrapper.andNew("OrderStatus="+OrderStatus.REFUNDING.getIntValue()+" or OrderStatus="+OrderStatus.REFUNDED.getIntValue());
			}
			if(!StringUtils.isEmpty(phoneNumber)){
				Wrapper<StoreBusiness> storeBusinessWrapper =
						new EntityWrapper<StoreBusiness>().eq("status", 0).like("PhoneNumber", phoneNumber);
				List<StoreBusiness> storeBusinessList = storeMapper.selectList(storeBusinessWrapper);
				List<Long> storeIdList = new ArrayList<Long>();
				for (StoreBusiness storeBusiness : storeBusinessList) {
					storeIdList.add(storeBusiness.getId());
				}
				wrapper.in("StoreId", storeIdList);
			}
			if(!StringUtils.isEmpty(clothesNumbers)){
				String[] clothesNumberArr = clothesNumbers.split(",");
				Wrapper<ProductNew> productWrapper =
						new EntityWrapper<ProductNew>().eq("status", 0).in("ClothesNumber", clothesNumberArr);
				List<ProductNew> productList = productNewMapper.selectList(productWrapper);
				List<Long> productIdList = new ArrayList<Long>();
				for (ProductNew product : productList) {
					productIdList.add(product.getId());
				}
				Wrapper<StoreOrderItemNew> supplierOrderItemWrapper =
						new EntityWrapper<StoreOrderItemNew>().eq("status", 0).in("ProductId", productIdList);
				List<StoreOrderItemNew> storeOrderItemList = orderItemNewMapper.selectList(supplierOrderItemWrapper);
				List<Long> orderNoList = new ArrayList<Long>();
				for (StoreOrderItemNew storeOrderItem : storeOrderItemList) {
					orderNoList.add(storeOrderItem.getOrderNo());
				}
				wrapper.in("OrderNo", orderNoList);
			}
			if(updateTimeStart>0){
				wrapper.ge("UpdateTime", updateTimeStart);
			}
			if(updateTimeEnd>0){
				wrapper.le("UpdateTime", updateTimeEnd);
			}
			List<StoreOrderNew> selectList = supplierOrderMapper.selectPage(page, wrapper);
//			SmallPage smallPage = new SmallPage(page);
//			smallPage.setRecords(selectList);
			return selectList;
	}

	/**
	 * 获取对应时间的毫秒值
	 * @param time
	 * @return
	 */
	private long getCurrentTimeMillis(String time){
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long longTime = simpleDateFormat.parse(time).getTime();
			return longTime;
		} catch (Exception e) {
			logger.error("更新时间格式不正确:"+e.getMessage());
			throw new RuntimeException("更新时间格式不正确:"+e.getMessage());
		}
	}

	/**
	 * 剩余卖家自动确认收货时间毫秒数
	 * @param sendTime 买家发货时间
	 * @param refundStartTime 售后开始时间时间戳,使用方法,当该订单发起第一笔售后或者平台介入就记录时间戳,当该订单售后完全结束且平台介入结束时，该字段变为0
	 * @param autoTakeDeliveryPauseTimeLength 卖家自动确认收货总暂停时长（毫秒）
	 * @return
	 */

	@Override
	public long buildSurplusSupplierAutoTakeTime(long sendTime, long refundStartTime, long autoTakeDeliveryPauseTimeLength) {
		if(sendTime == 0 ){//未发货则返回0
			return 0;
		}

		//15天
		long maxTime = 14 * 24 * 60 * 60 * 1000;

		long time = 0;//
		if(refundStartTime == 0){//未暂停
			time = System.currentTimeMillis();//当前时间
		}else{//卖家申请平台介入售后订单暂停
			time = refundStartTime;//暂停时间
		}

		long supplierAutoTakeTime = sendTime + maxTime + autoTakeDeliveryPauseTimeLength;//自动确认收货时间节点
		long surplusSupplierAutoTakeTime = 0;//剩余卖家确认时间
		surplusSupplierAutoTakeTime = supplierAutoTakeTime - time; //剩余自动确认收货时间  =  自动确认收货时间节点  - 当前时间或暂停时间
		if(surplusSupplierAutoTakeTime < 0){
			surplusSupplierAutoTakeTime = 0;
		}
		return surplusSupplierAutoTakeTime;
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#getSupplierOrderCount(long)
	 */
	@Override
	public Map<String, Object> getSupplierOrderCount(long userId) {
		Map<String,Object> supplierOrderCount = new HashMap<String,Object>();

		//获取订单总数
		supplierOrderCount.put("allCount", getSupplierOrderCountAllCount(userId));

		//获取今日新增个数
		supplierOrderCount.put("todayNewCount", getSupplierOrderCountTodayNewCount(userId));

		//获取待处理个数
		supplierOrderCount.put("unDealWithCount", getSupplierOrderCountUnDealWithCount(userId));

		//获取售后中订单个数
		supplierOrderCount.put("refundOrderUnDealCount", getSupplierOrderCountUnDealRefundOrderCount(userId));

		return supplierOrderCount;
	}

	//获取售后中订单个数
	@Override
	public Object getSupplierOrderCountUnDealRefundOrderCount(long userId) {
		Wrapper<StoreOrderNew> wrapperAllCount =
				new EntityWrapper<StoreOrderNew>().eq("status", 0).eq("supplierId",userId).eq("refund_underway", StoreOrderNew.REFUND_UNDERWAY).gt("ParentId", 0);
		return supplierOrderMapper.selectCount(wrapperAllCount);
	}

	//获取待处理个数
	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#getSupplierOrderCountUnDealWithCount(long)
	 */
	@Override
	public Object getSupplierOrderCountUnDealWithCount(long userId) {
		Wrapper<StoreOrderNew> wrapperAllCount =
				new EntityWrapper<StoreOrderNew>().eq("status", 0).eq("supplierId",userId).eq("OrderStatus", OrderStatus.PAID.getIntValue()).gt("ParentId", 0);
		return supplierOrderMapper.selectCount(wrapperAllCount);
	}

	//获取今日新增个数
	private Object getSupplierOrderCountTodayNewCount(long userId) {
		Wrapper<StoreOrderNew> wrapperAllCount =
				new EntityWrapper<StoreOrderNew>().eq("status", 0).eq("supplierId",userId).ge("CreateTime", getTodayZeroTimeInMillis())
				.gt("ParentId", 0);
		return supplierOrderMapper.selectCount(wrapperAllCount);
	}

	//获取今日零时毫秒数
	private long getTodayZeroTimeInMillis(){
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

	//获取订单总数
	private int getSupplierOrderCountAllCount(long userId) {
		Wrapper<StoreOrderNew> wrapperAllCount =
				new EntityWrapper<StoreOrderNew>().eq("status", 0).eq("supplierId",userId)
				.gt("ParentId", 0).orderBy("PayTime", false);
		return supplierOrderMapper.selectCount(wrapperAllCount);
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
		case 5:
			orderStatus = "所有";
			break;
		case 10:
			orderStatus = "待发货";
			break;
		case 20:
			orderStatus = "待审核";
			break;
		case 30:
			orderStatus = "已审核";
			break;
		case 40:
			orderStatus = "审核不通过";
			break;
		case 50:
			orderStatus = "已发货";
			break;
		case 60:
			orderStatus = "已签收";
			break;
		case 70:
			orderStatus = "交易成功";
			break;
		case 80:
			orderStatus = "退款中";
			break;
		case 90:
			orderStatus = "退款成功";
			break;
		case 100:
			orderStatus = "交易关闭";
			break;
		}
		return orderStatus;
	}

	/**
	 * 根据int状态值获取供应商订单列表对应的中文状态值
	 * @param status
	 * @return
	 */
	private String getSupplierOrderStatus(int status){
		String orderStatus = "";
		switch (status) {
		case 0:
		case 5:
		case 10:
		case 20:
		case 30:
		case 40:
			orderStatus = "未处理";
			break;
		case 50:
		case 60:
		case 70:
			orderStatus = "已发货";
			break;
		case 80:
		case 90:
		case 100:
			orderStatus = "已退单";
			break;
		}
		return orderStatus;
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#getSupplierOrderByOrderNo(long)
	 */
	@Override
	public Map<String,Object> getSupplierOrderByOrderNo(long orderNo) {
		Map<String,Object> result = new HashMap<String,Object>();
		StoreOrderNew storeOrder =  supplierOrderMapper.selectById(orderNo);
		result.put("orderNo", storeOrder.getOrderNo());
		result.put("totalBuyCount", storeOrder.getTotalBuyCount());
		result.put("orderStatus", getSupplierOrderStatus(storeOrder.getOrderStatus()));
		result.put("totalPay", storeOrder.getTotalPay());
		result.put("remark", storeOrder.getRemark());
		result.put("createTime", storeOrder.getCreateTime());
		Long storeId = storeOrder.getStoreId();
		result.put("storeId", storeId);
		result.put("expressInfo", storeOrder.getExpressInfo());
		//发货信息
		StoreExpressInfo entity = new StoreExpressInfo();
		entity.setOrderNo(orderNo);
		entity.setStatus(0);
		StoreExpressInfo storeExpressInfo = storeExpressInfoMapper.selectOne(entity);
		if(storeExpressInfo!=null){
			String EngName = storeExpressInfo.getExpressSupplier();
			Wrapper<ExpressSupplier> wrapper = new EntityWrapper<ExpressSupplier>();
			wrapper.eq("EngName", EngName);
			List<ExpressSupplier> list = supplierExpressMapper.selectList(wrapper);
			if(list == null || list.size() == 0){
				result.put("expressNo", storeExpressInfo.getExpressOrderNo());
				result.put("expressCompamyName", storeExpressInfo.getExpressSupplier());
				result.put("expressCnName", "");
			}else{
				result.put("expressNo", storeExpressInfo.getExpressOrderNo());
				result.put("expressCompamyName", storeExpressInfo.getExpressSupplier());
				result.put("expressCnName", list.get(0).getCnName());
			}
		}else{
			result.put("expressNo", "");
			result.put("expressCompamyName", "");
			result.put("expressCnName", "");
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#getSupplierOrderItemByOrderNo(long)
	 */
	@Override
	public List<Map<String,Object>> getSupplierOrderItemByOrderNo(long orderNo) {
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		Wrapper<StoreOrderItemNew> wrapper =
				new EntityWrapper<StoreOrderItemNew>().eq("status", 0).eq("OrderNo",orderNo);
		List<StoreOrderItemNew> selectList = orderItemNewMapper.selectList(wrapper);
		for (StoreOrderItemNew storeOrderItem : selectList) {
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("skuId", storeOrderItem.getSkuId());
			item.put("buyCount", storeOrderItem.getBuyCount());
			/**
			 * 对应Product表的id
			 * private Long ProductId;
			 */
			long productId = storeOrderItem.getProductId();
//			ProductNew product = productNewMapper.getProductById(productId);
			ProductNew product = productNewMapper.selectById(productId);
			item.put("firstDetailImage", product.getFirstDetailImage());
			item.put("firstDetailImageArr", product.getFirstDetailImage().split(","));

			item.put("name", product.getName());
			item.put("clothesNumber", product.getClothesNumber());


			item.put("buyCount", storeOrderItem.getBuyCount());
			item.put("money", storeOrderItem.getMoney());
			item.put("totalMoney", storeOrderItem.getTotalMoney());


			String skuSnapshot = storeOrderItem.getSkuSnapshot();
			if(StringUtils.isEmpty(skuSnapshot)){
				item.put("color", "");
				item.put("size", "");
			}else{
				String[] split = skuSnapshot.split("  ");
				String[] color = split[0].split(":");
				String[] size = split[1].split(":");
				item.put("color", color[1]);
				item.put("size", size[1]);
			}
			result.add(item);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#deliverGoods(long, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deliverGoods(long orderNo, String expressCompamyName, String expressNo, String remark, String expressCompanyChineseName) throws Exception {
		logger.info("发货 orderNo={}, expressCompamyName={}, expressNo={}, expressCompamyEnglishName={}");
		StoreOrderNew storeOrder =  supplierOrderMapper.selectById(orderNo);
		if(storeOrder.getRefundUnderway()==1){
			logger.error("供应商订单发货orderNo:"+storeOrder.getOrderNo()+";RefundUnderway:"+storeOrder.getRefundUnderway());
			throw new RuntimeException("该订单还在售后中");
		}
		if(storeOrder.getOrderStatus()==10){
			long time = System.currentTimeMillis();
			StoreExpressInfo storeExpressInfo = new StoreExpressInfo();
			storeExpressInfo.setOrderNo(orderNo);
			storeExpressInfo.setExpressSupplier(expressCompamyName);
			storeExpressInfo.setExpressOrderNo(expressNo);
			storeExpressInfo.setExpressUpdateTime(time);
			storeExpressInfo.setStatus(0);
			storeExpressInfo.setCreateTime(time);
			storeExpressInfo.setUpdateTime(time);
			storeExpressInfoMapper.insert(storeExpressInfo);
			StoreOrderNew entity = new StoreOrderNew();
			entity.setOrderNo(orderNo);
			entity.setOrderStatus(OrderStatus.DELIVER.getIntValue());
//			storeOrder.setExpressCompamyName(expressCompamyName);
//			storeOrder.setExpressNo(expressNo);
			entity.setRemark(remark);
			entity.setUpdateTime(time);
			entity.setSendTime(time);
			supplierOrderMapper.updateById(entity);

			//生成订单日志
			StoreOrderLog storeOrderLog = new StoreOrderLog();
			storeOrderLog.setOrderNo(storeOrder.getOrderNo());
			storeOrderLog.setStoreId(storeOrder.getStoreId());
			storeOrderLog.setOldStatus(storeOrder.getOrderStatus());
			storeOrderLog.setNewStatus(OrderStatus.DELIVER.getIntValue());
			storeOrderLog.setCreateTime(System.currentTimeMillis());
			int record = storeOrderLogMapper.insert(storeOrderLog);
			if(record==1){
				long storeId = storeOrder.getStoreId();
				StoreBusiness storeBusiness = storeMapper.selectById(storeId);
				long supplierId = storeOrder.getSupplierId();
				UserNew userNew = userNewMapper.selectById(supplierId);
				String userCID = storeBusiness.getUserCID();
				boolean ret = GetuiUtil.pushGeTui(userCID,"[俞姐姐门店宝]您在["+userNew.getBusinessName()+"]采购的美丽的衣服[订单号："+storeOrder.getOrderNo()+"]已经发货成功了。", "",
						orderNo+"", "", 9+"" , System.currentTimeMillis()+"");
				if(!ret){
					logger.error("发送推送消息有误userCID:"+userCID);
				}
			}
		}else if(storeOrder.getOrderStatus()==OrderStatus.DELIVER.getIntValue()){
			long time = System.currentTimeMillis();
			StoreExpressInfo storeExpressInfoEntity = new StoreExpressInfo();
			storeExpressInfoEntity.setExpressSupplier(expressCompamyName);
			storeExpressInfoEntity.setExpressOrderNo(expressNo);
			storeExpressInfoEntity.setExpressUpdateTime(time);
			storeExpressInfoEntity.setStatus(0);
			storeExpressInfoEntity.setUpdateTime(time);
			Wrapper<StoreExpressInfo> wrapper = new EntityWrapper<StoreExpressInfo>().eq("OrderNo", orderNo);
			storeExpressInfoMapper.update(storeExpressInfoEntity, wrapper);
			StoreOrderNew storeOrderEntity = new StoreOrderNew();
			storeOrderEntity.setOrderNo(orderNo);
			storeOrderEntity.setOrderStatus(OrderStatus.DELIVER.getIntValue());
//			storeOrder.setExpressCompamyName(expressCompamyName);
//			storeOrder.setExpressNo(expressNo);
			storeOrderEntity.setRemark(remark);
			storeOrderEntity.setUpdateTime(time);
			storeOrderEntity.setSendTime(time);
			supplierOrderMapper.updateById(storeOrderEntity);

			//生成订单日志
			StoreOrderLog storeOrderLog = new StoreOrderLog();
			storeOrderLog.setOrderNo(storeOrder.getOrderNo());
			storeOrderLog.setStoreId(storeOrder.getStoreId());
			storeOrderLog.setOldStatus(storeOrder.getOrderStatus());
			storeOrderLog.setNewStatus(OrderStatus.DELIVER.getIntValue());
			storeOrderLog.setCreateTime(System.currentTimeMillis());
			storeOrderLogMapper.insert(storeOrderLog);
		}else{
			logger.info("供应商订单发货:请确认订单状态:orderNo:"+orderNo+",storeOrder.getOrderStatus()"+storeOrder.getOrderStatus());
			throw new RuntimeException("该订单状态不能编辑发货信息");
		}

		//平台代发的小程序订单也发货
		Integer type = storeOrder.getType();
		Long shopMemberOrderId = storeOrder.getShopMemberOrderId();
		logger.info("平台订单type={}代发的小程序订单={}也发货", type, shopMemberOrderId);
		if (type.equals(1) && shopMemberOrderId > 0) {
			ShopMemberOrder shopMemberOrder = shopMemberOrderNewMapper.selectById(shopMemberOrderId);
			if(null != shopMemberOrder &&
					shopMemberOrder.getOrderStatus() != ShopMemberOrderStatus.UNDELIVERED.getIntValue() &&
					shopMemberOrder.getOrderStatus() != ShopMemberOrderStatus.DELIVERED.getIntValue() &&
					shopMemberOrder.getOrderStatus() != ShopMemberOrderStatus.FINISH_ORDER.getIntValue()){
				logger.info("该订单不在发货期间不能进行发货！");
				throw new RuntimeException("该订单不在发货期间不能进行发货！");
			}

			ShopMemberOrder upd = new ShopMemberOrder();
			upd.setId(shopMemberOrderId);
			upd.setOrderStatus(ShopMemberOrderStatus.DELIVERED.getIntValue());
			upd.setExpressSupplier(expressCompamyName);
			upd.setExpreeSupplierCnname(expressCompanyChineseName);
			upd.setExpressNo(expressNo);
			upd.setDeliveryTime(System.currentTimeMillis());
			shopMemberOrderNewMapper.updateById(upd);

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("shopMemberOrderId", shopMemberOrderId);
			jsonObject.put("type", 1);
			CallBackUtil.send(jsonObject.toJSONString(), "/order/order/storeOrder/registerSendGoodAfter15DaysJob", "get");
		}
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#chargeback(long, java.lang.String)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void chargeback(long orderNo, String cancelReason) {
		long time = System.currentTimeMillis();
		StoreOrderNew storeOrder = new StoreOrderNew();
		storeOrder.setOrderNo(orderNo);
		storeOrder.setOrderStatus(OrderStatus.REFUNDING.getIntValue());
		storeOrder.setCancelReason(cancelReason);
		storeOrder.setUpdateTime(time);
		storeOrder.setSendTime(time);
		supplierOrderMapper.updateById(storeOrder);

	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#getSupplierCustomerList(long, java.lang.String, double, double, int, int, java.lang.String, java.lang.String, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public List<Map<String,Object>> getSupplierCustomerList(long userId,  String businessName,String phoneNumber,double moneyMin,
			double moneyMax, int countMin, int countMax, String province, String city, Page<Map<String, Object>> page) {
		System.out.println(page);





		return supplierOrderMapper.getSupplierCustomerList(userId,businessName,phoneNumber,moneyMin,moneyMax,countMin,countMax,province,city,page);
	}

	@Override
	public StoreOrderNew selectById(long orderNo){
		return supplierOrderMapper.selectById(orderNo);
	}

	@Override
	public List<StoreRefundOrderActionLog> selectRefundLog(Long orderNo) {

		return supplierOrderMapper.selectRefundLog(orderNo);
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#getSupplierCustomerAllCount(long)
	 */
	@Override
	public int getSupplierCustomerAllCount(long userId) {
		return supplierOrderMapper.getSupplierCustomerAllCount(userId);
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#getSupplierCustomerTodayNewCount(long)
	 */
	@Override
	public int getSupplierCustomerTodayNewCount(long userId) {
		return supplierOrderMapper.getSupplierCustomerTodayNewCount(userId,getTodayZeroTimeInMillis());
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#deliveryExcel(long, long, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public List<Map<String, Object>> deliveryExcel(long userId, long orderNo, int orderStatus, String phoneNumber,
			String clothesNumbers, long updateTimeStart, long updateTimeEnd, Page<StoreOrderNew> page) {
		List<StoreOrderNew> selfMergedOrderNews = getSupplierOrderList(userId,orderNo,orderStatus,phoneNumber,clothesNumbers,
				updateTimeStart,updateTimeEnd,page);
//		List<List<StoreOrderVO>> selfMergedOrderNews = searchStoreOrders(null, "", "", "",
//        		"", 0, -1L, "", "", 0L, 0L, -1);
//        Map<Long, List<StoreOrder>> parentMergedOrderNewMap = storeOrderFacade.parentMergedMap(startTime, endTime);

        Set<Long> orderNos = getRelatedOrderNos(selfMergedOrderNews);

        Map<Long, List<StoreOrderItemNew>> orderItemByNo = orderItemMapOfOrderNos(orderNos); //!!!STORE OrderItem

        Set<Long> productIds = new HashSet<Long>();
        for(List<StoreOrderItemNew> orderItems : orderItemByNo.values()) {
        	for(StoreOrderItemNew orderItem : orderItems) {
        		productIds.add(orderItem.getProductId());
        	}
        }
        Map<Long, ProductNew> productMap = productNewService.productMapOfIds(productIds);
        Map<Long, StoreOrderNew> orderNewsMap = orderNewMapOfOrderNos(orderNos);
        Set<Long> userIds = new HashSet<Long>();
        for (StoreOrderNew orderNew : orderNewsMap.values()) {
            userIds.add(orderNew.getStoreId());
        }
        Map<Long, List<Address>> addressMap = addressNewService.addressMapOfUserIdsStore(userIds);

        Map<Long, LOWarehouse> warehouseMap = loWarehouseService.getWarehouseMap(null);
        Map<Long, BrandLogo> brandMap = brandService.getBrandMap();

        return assembleExcel(selfMergedOrderNews, orderItemByNo, productMap, orderNewsMap,
            addressMap, warehouseMap, brandMap);
	}

	/**
	 * 获取List中的所有OrderNo
	 * @param selfMergedOrderNews
	 * @return
	 */
	private Set<Long> getRelatedOrderNos(List<StoreOrderNew> selfMergedOrderNews) {
		Set<Long> orderNos = new HashSet<>();
        for(StoreOrderNew orderNew : selfMergedOrderNews) {
        	orderNos.add(orderNew.getOrderNo());
        }
		return orderNos;
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#orderItemMapOfOrderNos(java.util.Collection)
	 */
	@Override
	public Map<Long, List<StoreOrderItemNew>> orderItemMapOfOrderNos(Collection<Long> orderNos) {
		if(orderNos.size() < 1) {
			return new HashMap<Long, List<StoreOrderItemNew>>();
		}

		Map<Long, List<StoreOrderItemNew>> map = new HashMap<Long, List<StoreOrderItemNew>>();
		List<StoreOrderItemNew> orderItems = orderItemNewMapper.orderItemsOfOrderNos(orderNos);

		long lastOrderNo = 0;
		List<StoreOrderItemNew> orderItems2 = null;
		for(StoreOrderItemNew orderItem : orderItems) {
			if(orderItem.getOrderNo() != lastOrderNo) {
				lastOrderNo = orderItem.getOrderNo();
				orderItems2 = new ArrayList<StoreOrderItemNew>();
				map.put(lastOrderNo, orderItems2);
			}
			orderItems2.add(orderItem);
		}

		return map;
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#orderNewMapOfOrderNos(java.util.Collection)
	 */
	@Override
	public Map<Long, StoreOrderNew> orderNewMapOfOrderNos(Collection<Long> orderNos) {
        if (orderNos.size() < 1) {
            return new HashMap<Long, StoreOrderNew>();
        }
        List<StoreOrderNew> orderNews = supplierOrderMapper.storeOrdersOfOrderNos(orderNos);

        Map<Long, StoreOrderNew> map = new HashMap<Long, StoreOrderNew>();
        for (StoreOrderNew orderNew : orderNews) {
            map.put(orderNew.getOrderNo(), orderNew);
        }

        return map;
    }

	/**
	 * 封装数据成可以导出EXCEL的格式
	 * @param selfMergedOrderNews
	 * @param orderItemByNo
	 * @param productMap
	 * @param orderNewsMap
	 * @param addressMap
	 * @param warehouseMap
	 * @param brandMap
	 * @return
	 */
	private List<Map<String, Object>> assembleExcel(List<StoreOrderNew> selfMergedOrderNews,
			Map<Long, List<StoreOrderItemNew>> orderItemByNo, Map<Long, ProductNew> productMap, Map<Long, StoreOrderNew> orderNewsMap,
			Map<Long, List<Address>> addressMap, Map<Long, LOWarehouse> warehouseMap, Map<Long, BrandLogo> brandMap) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(StoreOrderNew orderNew : selfMergedOrderNews) {
            List<StoreOrderItemNew> orderItems = orderItemByNo.get(orderNew.getOrderNo());
            if(orderItems==null){
            	continue;
            }
            for (StoreOrderItemNew orderItem : orderItems) {
                assembleExcelItem(orderItem, list, productMap, addressMap, orderNewsMap, warehouseMap, brandMap);
            }
		}

        return list;
	}

	private void assembleExcelItem(StoreOrderItemNew orderItem, List<Map<String, Object>> list, Map<Long, ProductNew> productMap,
			Map<Long, List<Address>> addressMap, Map<Long, StoreOrderNew> orderNewsMap, Map<Long, LOWarehouse> warehouseMap,
			Map<Long, BrandLogo> brandMap) {
		long productId = orderItem.getProductId();
		long orderNo = orderItem.getOrderNo();
		long warehouseId = orderItem.getLOWarehouseId();

//		if (AdminConstants.ERP_WAREHOUSE_ID_LIST.contains(warehouseId)) {
//			return;
//		}

		ProductNew product = productMap.get(productId);
		// 补差价商品不输出
		if (product != null && product.getId() == 856) {
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (product == null) {
			map.put("productName", "商品id为 " + productId + " 未找到");
			map.put("clothNum", "该商品不存在或已被删除");
		} else {
			map.put("productName", product.getName());
			map.put("clothNum", product.getClothesNumber());
		}
		map.put("buyCount", orderItem.getBuyCount());
		map.put("warehouse", warehouseMap.get(warehouseId).getName());
		map.put("orderNo", orderItem.getOrderNo());
		map.put("expressOrderNo", "");
		if (orderItem.getPosition() != null && orderItem.getPosition().length() > 0) {

			map.put("position", orderItem.getPosition().replaceAll("--", "号") + "排");
		}
		BrandLogo brandLogo = brandMap.get(orderItem.getBrandId());
		if (brandLogo == null) {
			map.put("brandName", "id" + orderItem.getBrandId() + "该品牌不存在");
		} else {
			map.put("brandName", brandLogo.getBrandName());
		}

		StoreOrderNew orderNew = orderNewsMap.get(orderNo);
		String expressInfo = orderNew.getExpressInfo();
		map.put("expressInfo", expressInfo);
		List<Address> addresses = addressMap.get(orderNew.getStoreId());
		Address address = getAddrByExp(addresses, expressInfo);

		if (address != null) {
			map.put("receiver", address.getReceiverName());
			map.put("province", address.getProvinceName());
			map.put("city", address.getCityName());
			map.put("district", address.getDistrictName());
			map.put("phone", address.getTelephone());
		} else {
			map.put("receiver", "未找到");
			map.put("province", "未找到");
			map.put("city", "未找到");
			map.put("district", "未找到");
			map.put("phone", "未找到");
		}

		String sku = orderItem.getSkuSnapshot();
		String skuSnapShot = sku.replaceAll("颜色:", "").replaceAll("尺码:", "").trim();
		String[] skuSnap = skuSnapShot.split(" ");
		if (skuSnap.length > 2) {
			map.put("color", skuSnap[0]);
			map.put("size", skuSnap[2]);
		}

		map.put("productId", productId+"");
		map.put("storeId", orderItem.getStoreId()+"");
		map.put("money", orderItem.getMoney()+"");
		map.put("remark", orderNew.getRemark()==null?"":orderNew.getRemark());

		String orderStatus = getOrderStatus(orderNew.getOrderStatus());
//		switch (orderNew.getOrderStatus()) {
//		case 0:
//			orderStatus = "未付款";
//			break;
//		case 5:
//			orderStatus = "所有";
//			break;
//		case 10:
//			orderStatus = "已付款";
//			break;
//		case 20:
//			orderStatus = "待审核";
//			break;
//		case 30:
//			orderStatus = "已审核";
//			break;
//		case 40:
//			orderStatus = "审核不通过";
//			break;
//		case 50:
//			orderStatus = "已发货";
//			break;
//		case 60:
//			orderStatus = "已签收";
//			break;
//		case 70:
//			orderStatus = "交易成功";
//			break;
//		case 80:
//			orderStatus = "退款中";
//			break;
//		case 90:
//			orderStatus = "退款成功";
//			break;
//		case 100:
//			orderStatus = "交易关闭";
//			break;
//		}
		map.put("orderStatus", orderStatus);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		map.put("createTime", simpleDateFormat.format(new Date(orderNew.getCreateTime())));
		map.put("payTime", simpleDateFormat.format(new Date(orderNew.getCreateTime())));

		StoreExpressInfo entity = new StoreExpressInfo();
		entity.setOrderNo(orderNo);
		entity.setStatus(0);
		StoreExpressInfo storeExpressInfo = storeExpressInfoMapper.selectOne(entity);
		if(storeExpressInfo!=null){
			map.put("expressNo", storeExpressInfo.getExpressOrderNo());
			map.put("expressCompamyName", storeExpressInfo.getExpressSupplier());
		}else{
			map.put("expressNo", "");
			map.put("expressCompamyName", "");
		}

		list.add(map);

	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IOrderNewService#getAddrByExp(java.util.List, java.lang.String)
	 */
	@Override
	public Address getAddrByExp(List<Address> addresses, String expressInfo) {
    	if(addresses == null){
    		return null;
    	}
        for (Address address : addresses) {
            if (StringUtils.contains(expressInfo, address.getAddrFull())) {
                return address;
            }
        }
        return null;
    }

	/**
	 * 新的写法的确认收货
	 * @param
	 * @param
	 * @param
	 * @param
	 */
	public void updateOrderNewStatus(StoreOrderNew order, OrderStatus newStatus, OrderStatus oldStatus,
			long time) {
//		logger.info("updateOrderNewStatus:"+order.getOrderNo()+newStatus.getIntValue()+oldStatus.getIntValue()+time);
		int today = DateUtil.getToday();

		//修改订单状态
		Wrapper<StoreOrderNew> wrapper =
				new EntityWrapper<StoreOrderNew>().eq("OrderNo", order.getOrderNo()).eq("OrderStatus", oldStatus.getIntValue());
		StoreOrderNew entity = new StoreOrderNew();
		entity.setOrderStatus(newStatus.getIntValue());
		entity.setUpdateTime(time);
		entity.setConfirmSignedDate(today);
		entity.setConfirmSignedTime(time);
    	int count = supplierOrderMapper.update(entity, wrapper);
//    	logger.info("updateOrderNewStatus:"+count);
    	if (count != 1) {
    		String msg = "udpate order status error!, order id:" + order.getOrderNo();
    		logger.error(msg);
    		throw new IllegalStateException(msg);
    	}

    	//更新门店库存
    	if(newStatus == OrderStatus.SUCCESS){

//        	logger.info("updateOrderNewStatus:"+1);
            List<StoreOrderItemNew> storeOrderItems = this.getOrderNewItemsByOrderNO(order.getStoreId(), order.getOrderNo());
//    		int afterSaleCount = afterSaleService.getOrderAfterSaleCount(order.getStoreId(), order.getOrderNo());
//        	logger.info("updateOrderNewStatus:"+3);
//    		if(afterSaleCount > 0){
//    			logger.info("updateOrderNewStatus:"+4);
//    			Map<Long, OrderAfterSaleCountVO> afterSaleMap = afterSaleService.getOrderAfterSaleMap(order.getStoreId(), order.getOrderNo());
//    			logger.info("updateOrderNewStatus:"+5);
//    			for(ShopStoreOrderItem storeOrderItem : storeOrderItems){
//    				if(afterSaleMap.containsKey(storeOrderItem.getId())){
//    					storeOrderItem.setBuyCount(storeOrderItem.getBuyCount() - afterSaleMap.get(storeOrderItem.getId()).getNum());
//    				}
//
//    			}
//
//    		}
//    		logger.info("updateOrderNewStatus:"+2+":"+storeOrderItems);
    		if(storeOrderItems != null && storeOrderItems.size() > 0){
    			for (StoreOrderItemNew storeOrderItemNew : storeOrderItems) {
    				Wrapper<StoreProductNew> storeProductNewWrapper =
    						new EntityWrapper<StoreProductNew>().eq("StoreId", storeOrderItemNew.getStoreId()).eq("SkuId", storeOrderItemNew.getSkuId());
    				List<StoreProductNew> selectList = supplierOrderProductMapper.selectList(storeProductNewWrapper);
    				if(selectList.size()>0){
    					StoreProductNew storeProductNew = selectList.get(0);
    					StoreProductNew storeProductNewEntity = new StoreProductNew();
    					storeProductNewEntity.setOffSaleCount(storeProductNew.getOffSaleCount()+storeOrderItemNew.getBuyCount());
    					storeProductNewEntity.setUpdateTime(System.currentTimeMillis());
    					supplierOrderProductMapper.update(storeProductNewEntity, storeProductNewWrapper);
    				}else{
    					StoreProductNew storeProductNew = new StoreProductNew();
        				storeProductNew.setStoreId(storeOrderItemNew.getStoreId());
        				storeProductNew.setProductId(storeOrderItemNew.getProductId());
        				storeProductNew.setSkuId(storeOrderItemNew.getSkuId());
        				storeProductNew.setOnSaleCount(0);
        				storeProductNew.setOffSaleCount(storeOrderItemNew.getBuyCount());
        				storeProductNew.setStatus(0);
        				storeProductNew.setCreateTime(System.currentTimeMillis());
        				storeProductNew.setUpdateTime(storeProductNew.getCreateTime());
            			supplierOrderProductMapper.insertAllColumn(storeProductNew);
    				}
				}
//    			logger.info("updateOrderNewStatus:"+3);
    		}

    		//门店订单确认收货时发放个人门店激活奖金和团队激活奖金和修改个人门店订单交易奖金和团队订单交易奖金时间
    		grantBonuses(order.getOrderNo(),order.getStoreId(),order.getGroundUserId());

    	}

//    	logger.info("updateOrderNewStatus:"+4);
    	OrderNewLog orderNewLog = new OrderNewLog();
    	orderNewLog.setStoreId(order.getStoreId());
    	orderNewLog.setOrderNo(order.getOrderNo());
    	orderNewLog.setNewStatus(newStatus.getIntValue());
    	orderNewLog.setOldStatus(oldStatus.getIntValue());
    	orderNewLog.setCreateTime(time);
//    	logger.info("updateOrderNewStatus:"+5+":"+orderNewLog.toString());
    	orderNewLogMapper.addOrderLog(orderNewLog);
//    	logger.info("updateOrderNewStatus:"+6);
	}

	/**
	 * 门店订单确认收货时发放个人门店激活奖金和团队激活奖金和修改个人门店订单交易奖金和团队订单交易奖金时间
	 * @param orderNo
	 * @param storeId
	 * @param groundUserId
	 */
	public void grantBonuses(long orderNo,long storeId,Long groundUserId){
		if(groundUserId == null || groundUserId == 0){
//			logger.info("该订单没有订单人员，无需发放奖金");
			return ;
		}
		//1、准备数据
		StoreBusiness storeBusinessNew = storeMapper.selectById(storeId);
		if(storeBusinessNew!=null){
			//2、修改交易奖金可提现时间（修改个人门店订单交易奖金和团队订单交易奖金时间）
			groundBonusGrantFacade.updateDealBonusAllowGetOutTime(orderNo,storeId);

			//3、发放激活奖金
			//3.1判断是否需要发放激活奖金（用户未激活且在123阶段则进行发放激活奖金）
			if(storeBusinessNew.getActivationTime()==0){
				//3.2、获取激活条件金额
				double limitMoney = groundConditionRuleService.getActiveRuleCost();
//				logger.info("发放奖金limitMoney:"+limitMoney);
                //3.3、获取用户累计订单商品实付总计金额
                double accumilateSum = supplierOrderMapper.getAllOrderAccumulatedSum(storeId);
                //获取用户累计订单商品退款金额
                double totalRefundCostSum = supplierOrderMapper.getAllOrderTotalRefundCostSum(storeId);
//				logger.info("该商家订单商品实付总计金额accumilateSum:"+accumilateSum);
				if((accumilateSum-totalRefundCostSum) >= limitMoney){
					//3.4、发放激活奖金
					groundBonusGrantFacade.grantActivateBonus(groundUserId,storeId,orderNo);
//					logger.info("发放激活奖金完成");
					//3.5、修改门店激活状态
					StoreBusiness store = new  StoreBusiness();
					store.setId(storeId);
					store.setActivationTime(System.currentTimeMillis());
					storeMapper.updateById(store);
//					logger.info("修改门店激活时间完成");
				}else{
					logger.info("该商家订单商品实付总计金额没有达到激活条件");
				}
			}else{
				logger.info("不需要发放激活奖金，该用户已经激活或未其他阶段");
			}
		}

	}

	/**
	 * 根据订单OrderNo和StoreId获取对应的OrderItem
	 * @param storeId
	 * @param orderNo
	 * @return
	 */
	@Override
	public List<StoreOrderItemNew> getOrderNewItemsByOrderNO(long storeId, long orderNo) {
		Wrapper<StoreOrderItemNew> wrapper =
				new EntityWrapper<StoreOrderItemNew>().eq("OrderNo", orderNo).eq("StoreId", storeId);
    	return orderItemNewMapper.selectList(wrapper);
    }

	/**
	 * 获取供应商待发货订单列表
	 */
	@Override
	public List<Map<String,Object>> getSupplierOrderListPendingDelivery(long userId, long orderNo, String addresseeName,
			String addresseeTelePhone, String clothesNumbers, String payTimeBegin, String payTimeEnd, String remark,
			String customerName, String customerPhone, String expressNo, Page<Map<String,Object>> page) {
			//获取供应商待发货订单列表封装wrapper
			Wrapper<StoreOrderNew> wrapper = this.getSupplierOrderListPendingDeliveryWrapper(userId,orderNo,addresseeName,addresseeTelePhone,clothesNumbers,
					payTimeBegin,payTimeEnd,remark,customerName,customerPhone,expressNo);
			List<StoreOrderNew> selectList = supplierOrderMapper.selectPage(page, wrapper);
			//封装供应商待发货订单数据
			List<Map<String,Object>> supplierOrderListPendingDelivery = this.encapsulatedSupplierOrderListPendingDelivery(selectList);
			return supplierOrderListPendingDelivery;
	}

	/**
	 * 封装供应商待发货订单数据
	 * @param selectList
	 * @return
	 */
	private List<Map<String, Object>> encapsulatedSupplierOrderListPendingDelivery(List<StoreOrderNew> selectList) {
		List<Map<String, Object>> supplierOrderListPendingDelivery = new ArrayList<Map<String,Object>>();
		for (StoreOrderNew storeOrderNew : selectList) {
			Map<String,Object> supplierOrder = new HashMap<String,Object>();
			Long orderNo = storeOrderNew.getOrderNo();
			supplierOrder.put("orderNo", orderNo);
			Wrapper<StoreOrderItemNew> storeOrderItemNewWrapper =
					new EntityWrapper<StoreOrderItemNew>().eq("OrderNo", orderNo);
			List<StoreOrderItemNew> storeOrderItemNewList = orderItemNewMapper.selectList(storeOrderItemNewWrapper);
			int skuCount = 0;
			for (StoreOrderItemNew storeOrderItemNew : storeOrderItemNewList) {
				skuCount += storeOrderItemNew.getBuyCount();
			}

            //添加的新字段
            Double totalRefundCost = storeOrderNew.getTotalRefundCost();
            supplierOrder.put("totalRefundCost",totalRefundCost);

			supplierOrder.put("type", storeOrderNew.getType());

			supplierOrder.put("skuCount", skuCount);
			supplierOrder.put("orderStatus", getOrderStatus(storeOrderNew.getOrderStatus()));
			supplierOrder.put("price", BizUtil.savepoint(storeOrderNew.getTotalMoney()+storeOrderNew.getTotalExpressMoney(),2));
			String remark = storeOrderNew.getOrderSupplierRemark();
			supplierOrder.put("remark", StringUtils.isEmpty(remark)?"无":remark);
			Wrapper<StoreExpressInfo> storeExpressInfoWrapper =
					new EntityWrapper<StoreExpressInfo>().eq("OrderNo", orderNo).eq("Status", 0).orderBy("ExpressUpdateTime", false);
			List<StoreExpressInfo> storeExpressInfoList = storeExpressInfoMapper.selectList(storeExpressInfoWrapper);
			if(storeExpressInfoList.size()>0){
				supplierOrder.put("expressNo", storeExpressInfoList.get(0).getExpressOrderNo());
			}else{
				supplierOrder.put("expressNo", "无");
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			supplierOrder.put("payTime", simpleDateFormat.format(new Date(storeOrderNew.getPayTime())));
			supplierOrderListPendingDelivery.add(supplierOrder);
		}
		return supplierOrderListPendingDelivery;
	}

	/**
	 * 获取供应商待发货订单列表封装wrapper
	 * @param userId
	 * @param orderNo
	 * @param addresseeName
	 * @param addresseeTelePhone
	 * @param clothesNumbers
	 * @param payTimeBegin
	 * @param payTimeEnd
	 * @param remark
	 * @param customerName
	 * @param customerPhone
	 * @param expressNo
	 * @return
	 */
	private Wrapper<StoreOrderNew> getSupplierOrderListPendingDeliveryWrapper(long userId, long orderNo,
			String addresseeName, String addresseeTelePhone, String clothesNumbers, String payTimeBegin,
			String payTimeEnd, String remark, String customerName, String customerPhone, String expressNo) {
		try {
			Wrapper<StoreOrderNew> wrapper =
					new EntityWrapper<StoreOrderNew>().eq("status", 0).eq("supplierId",userId)
					.eq("OrderStatus", OrderStatus.PAID.getIntValue()).gt("ParentId",0)
                            .eq("refund_underway", StoreOrderNew.REFUND_NOT_UNDERWAY)
					.orderBy("PayTime", false);
			if(orderNo>0){
				wrapper.like("OrderNo", orderNo+"");
			}
			if(!StringUtils.isEmpty(addresseeName)){
				wrapper.like("express_name", addresseeName);
			}
			if(!StringUtils.isEmpty(addresseeTelePhone)){
				wrapper.like("express_phone", addresseeTelePhone);
			}
			if(!StringUtils.isEmpty(clothesNumbers)){
				String[] clothesNumberArr = clothesNumbers.split(",");
				Wrapper<ProductNew> productNewWrapper =
						new EntityWrapper<ProductNew>().eq("status", 0).in("ClothesNumber",clothesNumberArr);
				List<ProductNew> productNewList = productNewMapper.selectList(productNewWrapper);
				List<Long> productNewIdList = new ArrayList<Long>();
				for (ProductNew productNew : productNewList) {
					productNewIdList.add(productNew.getId());
				}

				Wrapper<StoreOrderItemNew> orderItemNewWapper =
						new EntityWrapper<StoreOrderItemNew>().eq("status", 0).in("ClothesNumber",clothesNumberArr);
				List<StoreOrderItemNew> orderItemNewList = orderItemNewMapper.selectList(orderItemNewWapper);
				List<Long> orderNewIdList = new ArrayList<Long>();
				for (StoreOrderItemNew storeOrderItemNew : orderItemNewList) {
					orderNewIdList.add(storeOrderItemNew.getStoreId());
				}
				wrapper.in("OrderNo", orderNewIdList);
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(!StringUtils.isEmpty(payTimeBegin)){
				long payTimeBeginLong = simpleDateFormat.parse(payTimeBegin).getTime();
				wrapper.ge("PayTime", payTimeBeginLong);
			}
			if(!StringUtils.isEmpty(payTimeEnd)){
				long payTimeEndLong = simpleDateFormat.parse(payTimeEnd).getTime();
				wrapper.le("PayTime", payTimeEndLong);
			}
			if(!StringUtils.isEmpty(remark)){
				wrapper.like("order_supplier_remark", remark);
			}
			if(!StringUtils.isEmpty(customerName)){
				Wrapper<StoreBusiness> storeBusinesswrapper =
						new EntityWrapper<StoreBusiness>().eq("Status", 0).like("BusinessName",customerName);
				List<StoreBusiness> storeBusinessList = storeMapper.selectList(storeBusinesswrapper);
				List<Long> storeBusinessIdList = new ArrayList<Long>();
				for (StoreBusiness storeBusiness : storeBusinessList) {
					storeBusinessIdList.add(storeBusiness.getId());
				}
				wrapper.in("StoreId", storeBusinessIdList);
			}
			if(!StringUtils.isEmpty(customerPhone)){
				Wrapper<StoreBusiness> storeBusinesswrapper =
						new EntityWrapper<StoreBusiness>().eq("Status", 0).like("PhoneNumber",customerPhone);
				List<StoreBusiness> storeBusinessList = storeMapper.selectList(storeBusinesswrapper);
				List<Long> storeBusinessIdList = new ArrayList<Long>();
				for (StoreBusiness storeBusiness : storeBusinessList) {
					storeBusinessIdList.add(storeBusiness.getId());
				}
				wrapper.in("StoreId", storeBusinessIdList);
			}
			if(!StringUtils.isEmpty(expressNo)){
				Wrapper<StoreExpressInfo> storeExpressInfowrapper =
						new EntityWrapper<StoreExpressInfo>().eq("Status", 0).in("ExpressOrderNo",expressNo);
				List<StoreExpressInfo> storeExpressInfoList = storeExpressInfoMapper.selectList(storeExpressInfowrapper);
				List<Long> orderNoList = new ArrayList<Long>();
				for (StoreExpressInfo storeExpressInfo : storeExpressInfoList) {
					orderNoList.add(storeExpressInfo.getOrderNo());
				}
				wrapper.in("OrderNo", orderNoList);
			}
			return wrapper;
		} catch (Exception e) {
			logger.error("获取供应商待发货订单列表封装wrapper:"+e.getMessage());
			throw new RuntimeException("获取供应商待发货订单列表封装wrapper:"+e.getMessage());
		}
	}

	/**
	 * 导出供应商待发货订单列表EXCEL
	 */
	@Override
	public List<Map<String, Object>> outPendingDeliveryOrderExcel(long userId, long orderNo, String addresseeName,
			String addresseeTelePhone, String clothesNumbers, String payTimeBegin, String payTimeEnd, String remark,
			String customerName, String customerPhone, String expressNo) {
		//获取供应商待发货订单列表封装wrapper
		Wrapper<StoreOrderNew> wrapper = this.getSupplierOrderListPendingDeliveryWrapper(userId,orderNo,addresseeName,addresseeTelePhone,clothesNumbers,
				payTimeBegin,payTimeEnd,remark,customerName,customerPhone,expressNo);
		List<StoreOrderNew> selectList = supplierOrderMapper.selectList(wrapper);
		//封装供应商待发货订单数据
		List<Map<String,Object>> supplierOrderListPendingDelivery = this.encapsulatedOutPendingDeliveryOrderExcel(selectList);
		return supplierOrderListPendingDelivery;
	}

	/**
	 * 封装导出供应商待发货订单列表EXCEL
	 * @param selectList
	 * @return
	 */
	private List<Map<String, Object>> encapsulatedOutPendingDeliveryOrderExcel(List<StoreOrderNew> selectList) {
		List<Map<String, Object>> supplierOrderListPendingDelivery = new ArrayList<Map<String,Object>>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		for (StoreOrderNew storeOrderNew : selectList) {
			Long orderNo = storeOrderNew.getOrderNo();
			Wrapper<StoreOrderItemNew> storeOrderItemNewWrapper =
					new EntityWrapper<StoreOrderItemNew>().eq("OrderNo", orderNo);
			List<StoreOrderItemNew> storeOrderItemList = orderItemNewMapper.selectList(storeOrderItemNewWrapper);
			Wrapper<StoreExpressInfo> storeExpressInfoWrapper =
					new EntityWrapper<StoreExpressInfo>().eq("OrderNo", orderNo).eq("Status", 0).orderBy("ExpressUpdateTime", false);
			List<StoreExpressInfo> storeExpressInfoList = storeExpressInfoMapper.selectList(storeExpressInfoWrapper);
			String expressNo = "";
			String expressName = "";
			if(storeExpressInfoList.size()>0){
				StoreExpressInfo storeExpressInfo = storeExpressInfoList.get(0);
				expressNo = storeExpressInfo.getExpressOrderNo();
				Wrapper<ExpressSupplier> expressSupplierWrapper =
						new EntityWrapper<ExpressSupplier>().eq("EngName", storeExpressInfo.getExpressSupplier()).eq("Status", 0);
				List<ExpressSupplier> expressSupplierList = supplierExpressMapper.selectList(expressSupplierWrapper);
				if(expressSupplierList.size()>0){
					expressName = expressSupplierList.get(0).getCnName();
				}
			}

			// 平台优惠金额
			Double patformTotalPreferential =  storeOrderNew.getPlatformTotalPreferential();
			// 供应商优惠金额
			Double supplierTotalPreferential  = storeOrderNew.getSupplierTotalPreferential();
			Double totalExpressMoney = storeOrderNew.getTotalExpressMoney();

			boolean patform = false;
			boolean supplire = false;
			boolean expressFlag = false;

			for (StoreOrderItemNew storeOrderItemNew : storeOrderItemList){
				Map<String,Object> supplierOrder = new HashMap<String,Object>();
				supplierOrder.put("orderNo", orderNo);
				supplierOrder.put("storeId", storeOrderNew.getStoreId());
				supplierOrder.put("productId", storeOrderItemNew.getProductId());
				Long skuId = storeOrderItemNew.getSkuId();
				ProductSkuNew productSkuNew = productSkuNewMapper.selectById(skuId);
				supplierOrder.put("productName", productSkuNew.getName());
				supplierOrder.put("colorName", productSkuNew.getColorName());
				supplierOrder.put("sizeName", productSkuNew.getSizeName());
				supplierOrder.put("saleCount", storeOrderItemNew.getBuyCount());
				String remark = storeOrderNew.getRemark();
				supplierOrder.put("remark", StringUtils.isEmpty(remark)?"":remark);
				supplierOrder.put("orderStatus", getOrderStatus(storeOrderNew.getOrderStatus()));
				supplierOrder.put("createTime", simpleDateFormat.format(new Date(storeOrderNew.getCreateTime())));
				supplierOrder.put("payTime", simpleDateFormat.format(new Date(storeOrderNew.getPayTime())));
				supplierOrder.put("expressName", storeOrderNew.getExpressName());
				supplierOrder.put("expressPhone", storeOrderNew.getExpressPhone());
				supplierOrder.put("expressAddress",storeOrderNew.getExpressAddress());
				supplierOrder.put("expressNo", "No:"+expressNo);
				supplierOrder.put("expressCompanyName", expressName);
				Double totalMoney = storeOrderItemNew.getTotalMoney();
				supplierOrder.put("totalMoney",totalMoney);
				if(!expressFlag){
					expressFlag = true;
					supplierOrder.put("totalExpressMoney",totalExpressMoney);
				}else{
					supplierOrder.put("totalExpressMoney",0);
				}
				if(!patform) {
					patform = true;
					supplierOrder.put("patformTotalPreferential",patformTotalPreferential);
				}else {
					supplierOrder.put("patformTotalPreferential",0);
				}
				if(!supplire) {
					supplierOrder.put("supplierTotalPreferential",supplierTotalPreferential);
					supplire = true;
				}else{
					supplierOrder.put("supplierTotalPreferential",0);
				}
				supplierOrderListPendingDelivery.add(supplierOrder);
			}
		}
		return supplierOrderListPendingDelivery;
	}

	/**
	 * 获取供应商列表
	 */
	@Override
	public List<Map<String,Object>> getSupplierOrderList(long userId, long orderNo, int orderStatus, String addresseeName,
			String addresseeTelePhone, String clothesNumbers, String updateTimeBegin, String updateTimeEnd,
			String remark, String customerName, String customerPhone, String expressNo, String createTimeBegin,
			String createTimeEnd, int refundUnderway, Page<Map<String, Object>> page) {
		//获取供应商订单列表封装wrapper
		Wrapper<StoreOrderNew> wrapper = this.getSupplierOrderListWrapper(userId,orderNo,orderStatus,addresseeName,addresseeTelePhone,clothesNumbers,
				updateTimeBegin,updateTimeEnd,remark,customerName,customerPhone,expressNo,createTimeBegin,createTimeEnd,refundUnderway);
		List<StoreOrderNew> selectList = supplierOrderMapper.selectPage(page, wrapper);
		//封装供应商订单数据
		List<Map<String,Object>> supplierOrderList = this.encapsulatedSupplierOrderList(selectList);
		return supplierOrderList;
	}

	/**
	 * 封装供应商订单数据
	 * @param selectList
	 * @return
	 */
	private List<Map<String, Object>> encapsulatedSupplierOrderList(List<StoreOrderNew> selectList) {
		List<Map<String, Object>> supplierOrderList = new ArrayList<Map<String,Object>>();
		for (StoreOrderNew storeOrderNew : selectList) {
			Map<String,Object> supplierOrder = new HashMap<String,Object>();
			Long orderNo = storeOrderNew.getOrderNo();
			supplierOrder.put("orderNo", orderNo.toString());
			Wrapper<StoreOrderItemNew> storeOrderItemNewWrapper =
					new EntityWrapper<StoreOrderItemNew>().eq("OrderNo", orderNo);
			List<StoreOrderItemNew> storeOrderItemNewList = orderItemNewMapper.selectList(storeOrderItemNewWrapper);
			int skuCount = 0;
			for (StoreOrderItemNew storeOrderItemNew : storeOrderItemNewList) {
				skuCount += storeOrderItemNew.getBuyCount();
			}
			supplierOrder.put("skuCount", skuCount);
			supplierOrder.put("orderStatus", getOrderStatus(storeOrderNew.getOrderStatus()));
			double price = BizUtil.savepoint(storeOrderNew.getTotalMoney()+storeOrderNew.getTotalExpressMoney(),2);
			price = new BigDecimal(price).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			supplierOrder.put("price",price);

			/**
			 * 添加字段
			 */
			//Double totalRefundCost = supplierOrderNewMapper.select(orderNo);

			Double totalRefundCost = storeOrderNew.getTotalRefundCost();
			supplierOrder.put("totalRefundCost",totalRefundCost);

			String remark = storeOrderNew.getOrderSupplierRemark();
			supplierOrder.put("remark", StringUtils.isEmpty(remark)?"无":remark);
			Wrapper<StoreExpressInfo> storeExpressInfoWrapper =
					new EntityWrapper<StoreExpressInfo>().eq("OrderNo", orderNo).eq("Status", 0).orderBy("ExpressUpdateTime", false);
			List<StoreExpressInfo> storeExpressInfoList = storeExpressInfoMapper.selectList(storeExpressInfoWrapper);
			if(storeExpressInfoList.size()>0){
				supplierOrder.put("expressNo", storeExpressInfoList.get(0).getExpressOrderNo());
			}else{
				supplierOrder.put("expressNo", "无");
			}

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			supplierOrder.put("type", storeOrderNew.getType());
			supplierOrder.put("updateTime", simpleDateFormat.format(new Date(storeOrderNew.getUpdateTime())));
			supplierOrder.put("createTime", simpleDateFormat.format(new Date(storeOrderNew.getCreateTime())));
			supplierOrder.put("refundUnderway", storeOrderNew.getRefundUnderway()==StoreOrderNew.REFUND_UNDERWAY ? "售后中":"非售后中");
			supplierOrderList.add(supplierOrder);
		}
		return supplierOrderList;
	}

	/**
	 * 获取供应商订单列表封装wrapper
	 * @param userId
	 * @param orderNo
	 * @param orderStatus
	 * @param addresseeName
	 * @param addresseeTelePhone
	 * @param clothesNumbers
	 * @param updateTimeBegin
	 * @param updateTimeEnd
	 * @param remark
	 * @param customerName
	 * @param customerPhone
	 * @param expressNo
	 * @param createTimeBegin
	 * @param createTimeEnd
	 * @param
	 * @return
	 */
	private Wrapper<StoreOrderNew> getSupplierOrderListWrapper(long userId, long orderNo, int orderStatus,
			String addresseeName, String addresseeTelePhone, String clothesNumbers, String updateTimeBegin,
			String updateTimeEnd, String remark, String customerName, String customerPhone, String expressNo,
			String createTimeBegin, String createTimeEnd, int refundUnderway) {
		try {
			Wrapper<StoreOrderNew> wrapper =
					new EntityWrapper<StoreOrderNew>().eq("status", 0).eq("supplierId",userId)
					.gt("ParentId", 0).orderBy("PayTime", false);
			if(orderNo>0){
				wrapper.like("OrderNo", orderNo+"");
			}
			if(orderStatus>-1){
				wrapper.eq("OrderStatus", orderStatus);
			}
			if(!StringUtils.isEmpty(addresseeName)){
				wrapper.like("express_name", addresseeName);
			}
			if(!StringUtils.isEmpty(addresseeTelePhone)){
				wrapper.like("express_phone", addresseeTelePhone);
			}
			if(!StringUtils.isEmpty(clothesNumbers)){
				String[] clothesNumberArr = clothesNumbers.split(",");
				Wrapper<ProductNew> productNewWrapper =
						new EntityWrapper<ProductNew>().eq("status", 0).in("ClothesNumber",clothesNumberArr);
				List<ProductNew> productNewList = productNewMapper.selectList(productNewWrapper);
				List<Long> productNewIdList = new ArrayList<Long>();
				for (ProductNew productNew : productNewList) {
					productNewIdList.add(productNew.getId());
				}

				Wrapper<StoreOrderItemNew> orderItemNewWapper =
						new EntityWrapper<StoreOrderItemNew>().eq("status", 0).in("ClothesNumber",clothesNumberArr);
				List<StoreOrderItemNew> orderItemNewList = orderItemNewMapper.selectList(orderItemNewWapper);
				List<Long> orderNewIdList = new ArrayList<Long>();
				for (StoreOrderItemNew storeOrderItemNew : orderItemNewList) {
					orderNewIdList.add(storeOrderItemNew.getStoreId());
				}
				wrapper.in("OrderNo", orderNewIdList);
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(!StringUtils.isEmpty(updateTimeBegin)){
				long updateTimeBeginLong = simpleDateFormat.parse(updateTimeBegin).getTime();
				wrapper.ge("UpdateTime", updateTimeBeginLong);
			}
			if(!StringUtils.isEmpty(updateTimeEnd)){
				long updateTimeEndLong = simpleDateFormat.parse(updateTimeEnd).getTime();
				wrapper.le("UpdateTime", updateTimeEndLong);
			}
			if(!StringUtils.isEmpty(remark)){
				wrapper.like("order_supplier_remark", remark);
			}
			if(!StringUtils.isEmpty(customerName)){
				Wrapper<StoreBusiness> storeBusinesswrapper =
						new EntityWrapper<StoreBusiness>().eq("Status", 0).like("BusinessName",customerName);
				List<StoreBusiness> storeBusinessList = storeMapper.selectList(storeBusinesswrapper);
				List<Long> storeBusinessIdList = new ArrayList<Long>();
				for (StoreBusiness storeBusiness : storeBusinessList) {
					storeBusinessIdList.add(storeBusiness.getId());
				}
				if(storeBusinessIdList.size()== 0){
					storeBusinessIdList.add(-100L);
					wrapper.in("StoreId", storeBusinessIdList);
				}else {
					wrapper.in("StoreId", storeBusinessIdList);
				}

			}
			if(!StringUtils.isEmpty(customerPhone)){
				Wrapper<StoreBusiness> storeBusinesswrapper =
						new EntityWrapper<StoreBusiness>().eq("Status", 0).like("PhoneNumber",customerPhone);
				List<StoreBusiness> storeBusinessList = storeMapper.selectList(storeBusinesswrapper);
				List<Long> storeBusinessIdList = new ArrayList<Long>();
				for (StoreBusiness storeBusiness : storeBusinessList) {
					storeBusinessIdList.add(storeBusiness.getId());
				}
				wrapper.in("StoreId", storeBusinessIdList);
			}
			if(!StringUtils.isEmpty(expressNo)){
				Wrapper<StoreExpressInfo> storeExpressInfowrapper =
						new EntityWrapper<StoreExpressInfo>().eq("Status", 0).in("ExpressOrderNo",expressNo);
				List<StoreExpressInfo> storeExpressInfoList = storeExpressInfoMapper.selectList(storeExpressInfowrapper);
				List<Long> orderNoList = new ArrayList<Long>();
				for (StoreExpressInfo storeExpressInfo : storeExpressInfoList) {
					orderNoList.add(storeExpressInfo.getOrderNo());
				}

				if(orderNoList.size() == 0) {
					orderNoList.add(-100L);
				}
				wrapper.in("OrderNo", orderNoList);

			}
			if(!StringUtils.isEmpty(createTimeBegin)){
				long createTimeBeginLong = simpleDateFormat.parse(createTimeBegin).getTime();
				wrapper.ge("CreateTime", createTimeBeginLong);
			}
			if(!StringUtils.isEmpty(createTimeEnd)){
				long createTimeEndLong = simpleDateFormat.parse(createTimeEnd).getTime();
				wrapper.le("CreateTime", createTimeEndLong);
			}
			if(refundUnderway>-1){
				wrapper.eq("refund_underway", refundUnderway);
			}
			return wrapper;
		} catch (Exception e) {
			logger.error("获取供应商待发货订单列表封装wrapper:"+e.getMessage());
			throw new RuntimeException("获取供应商待发货订单列表封装wrapper:"+e.getMessage());
		}
	}

	/**
	 * 导出供应商订单列表EXCEL
	 */
	@Override
	public List<Map<String, Object>> outExcel(long userId, long orderNo, int orderStatus, String addresseeName,
			String addresseeTelePhone, String clothesNumbers, String updateTimeBegin, String updateTimeEnd,
			String remark, String customerName, String customerPhone, String expressNo, String createTimeBegin,
			String createTimeEnd,int refundUnderway) {
		//获取供应商订单列表封装wrapper
		Wrapper<StoreOrderNew> wrapper = this.getSupplierOrderListWrapper(userId,orderNo,orderStatus,addresseeName,addresseeTelePhone,clothesNumbers,
				updateTimeBegin,updateTimeEnd,remark,customerName,customerPhone,expressNo,createTimeBegin,createTimeEnd,refundUnderway);
		List<StoreOrderNew> selectList = supplierOrderMapper.selectList(wrapper);
		//封装供应商订单数据
		List<Map<String,Object>> supplierOrderList = this.encapsulatedOutPendingDeliveryOrderExcel(selectList);
		return supplierOrderList;
	}

	/**
	 * 修改订单供应商备注
	 * @param orderNo
	 * @param orderSupplierRemark
	 */
	@Override
	public void updateOrderSupplierRemark(long orderNo, String orderSupplierRemark){
		StoreOrderNew order =  new StoreOrderNew();
		order.setOrderNo(orderNo);
		order.setOrderSupplierRemark(orderSupplierRemark);
		supplierOrderMapper.updateById(order);
	}

	/**
	 * app获取供应商订单列表
	 * @param orderStatus
	 * @param
	 * @param userDetail
	 * @return
	 */
	@Override
    public List<StoreOrderNew> getSupplierOrderListByOrderStatus(int orderStatus, Page<StoreOrderNew> page,
                                                                 UserDetail<StoreBusiness> userDetail) {
    	long supplierId = userDetail.getUserDetail().getSupplierId();
    	if(supplierId<0){
    		throw new RuntimeException("供应商ID为空，请确认");
    	}
		Wrapper<StoreOrderNew> wrapper =
				new EntityWrapper<StoreOrderNew>().eq("status", 0).eq("supplierId",supplierId)
				.gt("ParentId", 0).orderBy("OrderNo", false).ne("MergedId", -1).eq("OrderType", StoreOrderNew.OrderType_normal);
		if(orderStatus!=OrderStatus.ALL.getIntValue()){
			wrapper.eq("OrderStatus", orderStatus);
		}
		List<StoreOrderNew> orderNews = supplierOrderMapper.selectPage(page, wrapper);
    	return orderNews;
    }

	/**
	 * 获取到供应商订单详情
	 */
	@Override
	public StoreOrderNew getSupplierOrderDetail(UserDetail<StoreBusiness> userDetail, long orderNo) {
		StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(orderNo);
		return storeOrderNew;
	}

    /**
     * 供应商修改订单价格
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeOrderPrice(long orderNo, long userId, double changePrice) {
		StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(orderNo);
		//校验金额
		if(changePrice <= 0){
			logger.info("订单价格必须大于0！");
			throw new TipsMessageException("订单价格必须大于0！");
		}
		//判定该订单是否能够被该供应商修改
		if(storeOrderNew.getSupplierId() != userId){
			logger.info("该供应商没有权限修改该订单！orderNo="+orderNo+"supplierId="+userId);
			throw new TipsMessageException("没有权限修改该订单！");
		}
		//判断该订单是否能够改价
		if(storeOrderNew.getOrderStatus() != OrderStatus.UNPAID.getIntValue()){
			logger.info("该订单不是未付款订单，无法改价！orderNo="+orderNo+"supplierId="+userId);
			throw new TipsMessageException("该订单不是未付款订单，无法改价！");
		}
		if(storeOrderNew.getLockingOrder() == StoreOrderNew.LOCKING_ORDER){
			logger.info("该订单正在支付中，无法进行改价！orderNo="+orderNo+"supplierId="+userId);
			throw new TipsMessageException("该订单正在支付中，无法进行改价！请联系客户，取消第三方支付订单，再申请改价！");
		}
		if(storeOrderNew.getLockingOrder() == StoreOrderNew.VERSION_UNSUPPORT){
			logger.info("用户下单的APP版本低于3.5.0，不支持改价功能!orderNo="+orderNo+"supplierId="+userId);
			throw new TipsMessageException("用户下单的APP版本低于3.5.0，不支持改价功能!");
		}
		//开始改变价格
		changePrice(storeOrderNew,changePrice);
		//添加操作日志
		addActionLog(storeOrderNew,changePrice,userId, SupplierChangeOrderPriceActionLog.ACTION_NAME_CHANGE_PRICE);
	}
    /**
     * 开始改变价格,涉及到子母订单，所以必须两边都进行处理
     */
	private void changePrice(StoreOrderNew storeOrderNew, double changePrice) {
//		long currentTime = System.currentTimeMillis();
//		//改变价格
//		double supplierChangePrice = DoubleUtil.sub(storeOrderNew.getOriginalPrice(), changePrice);
//		double supplierTotalPreferential = DoubleUtil.add(supplierChangePrice, storeOrderNew.getSupplierPreferential());
//		//更新订单
//		StoreOrderNew storeOrderNew2 = new StoreOrderNew();
//		storeOrderNew2.setOrderNo(storeOrderNew.getOrderNo());//设置订单号
//		storeOrderNew2.setTotalPay(changePrice);//设置实付金额
//		storeOrderNew2.setSupplierChangePrice(supplierChangePrice);//改价差额 originalPrice-changePrice
//		storeOrderNew2.setSupplierTotalPreferential(supplierTotalPreferential);//供应商总优惠可以为负，商家把价格改高
//		storeOrderNew2.setUpdateTime(currentTime);//设置更新时间
//		supplierOrderMapper.updateById(storeOrderNew2);//更新订单
		long orderNo = storeOrderNew.getOrderNo();//子订单号
		long parentId = storeOrderNew.getParentId();//母订单号


        //更改订单价格
		supplierOrderMapper.changePriceByOrderNo(orderNo, changePrice, parentId);
		//更改订单详情价格
		changeTotalPayInOrderItemByOrderNo(storeOrderNew,changePrice);

	}
	//差额
	private void changeTotalPayInOrderItemByOrderNo(StoreOrderNew storeOrderNew, double changePrice) {
		Double totalMoney = storeOrderNew.getTotalMoney();
		Double scale = DoubleUtil.div(changePrice, totalMoney);
		scale = BizUtil.savepoint(scale,2);
		//获取orderItem集合
		List<Map<String,Object>> list = orderItemNewMapper.getOrederItemIdAndTotalPayByOrderNo(storeOrderNew.getOrderNo());
		for(Map<String,Object> storeOrderItemNew :list){
			long orderItemId = (long)storeOrderItemNew.get("Id");
			double orderItemTotalPay = ((BigDecimal)storeOrderItemNew.get("TotalMoney")).doubleValue();
			orderItemTotalPay = DoubleUtil.mul(orderItemTotalPay, scale);//该sku的实付总价格（计算公式： * 实付款比例）
			StoreOrderItemNew storeOrderItemNew2 = new StoreOrderItemNew();
			storeOrderItemNew2.setId(orderItemId);
			storeOrderItemNew2.setTotalPay(orderItemTotalPay);
			orderItemNewMapper.updateById(storeOrderItemNew2);
		}


	}

	/**
	 * 添加改价操作日志
	 * @param ActionName 供应商改价    供应商恢复价格
	 */
	private void addActionLog(StoreOrderNew storeOrderNew, double changePrice, long userId , String ActionName) {
		SupplierChangeOrderPriceActionLog supplierChangeOrderPriceActionLog = new SupplierChangeOrderPriceActionLog();
		//添加操作日志
		long currentTime = System.currentTimeMillis();
		supplierChangeOrderPriceActionLog.setActionName(ActionName);//操作名称
		supplierChangeOrderPriceActionLog.setOrderNo(storeOrderNew.getOrderNo());//订单号
		supplierChangeOrderPriceActionLog.setSupplierId(userId);//供应商ID
		supplierChangeOrderPriceActionLog.setActionTime(currentTime);//操作时间
		supplierChangeOrderPriceActionLog.setOldTotalPay(storeOrderNew.getTotalPay());//订单改价前实付金额（不包含邮费）
		supplierChangeOrderPriceActionLog.setNewTotalPay(changePrice);//订单改价后实付金额（不包含邮费）
		supplierChangeOrderPriceActionLog.setOriginalTotalPay(storeOrderNew.getOriginalPrice());//订单原始的实付金额（不包含邮费）
		String ip = HttpKit.getRequest().getRemoteHost();//获取IP
		supplierChangeOrderPriceActionLog.setIp(ip);//ip
		//组成订单号
		StringBuffer stringBuffer = new StringBuffer("S");
		stringBuffer.append(storeOrderNew.getOrderNo()).append(storeOrderNew.getOrderNoAttachmentStr());
		supplierChangeOrderPriceActionLog.setOutTradeNo(stringBuffer.toString());//支付时订单号
		supplierChangeOrderPriceActionLogMapper.insert(supplierChangeOrderPriceActionLog);

	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void restoreOrderPrice(long orderNo, long userId) {
		StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(orderNo);
		//判定该订单是否能够被该供应商修改
		if(storeOrderNew.getSupplierId() != userId){
			logger.info("该供应商没有权限修改该订单！orderNo="+orderNo+"supplierId="+userId);
			throw new TipsMessageException("没有权限修改该订单！");
		}
		//判断该订单是否能够改价
		if(storeOrderNew.getOrderStatus() != OrderStatus.UNPAID.getIntValue()){
			logger.info("该订单不是未付款订单，无法改价！orderNo="+orderNo+"supplierId="+userId);
			throw new TipsMessageException("该订单不是未付款订单，无法改价！");
		}
		if(storeOrderNew.getLockingOrder() == StoreOrderNew.LOCKING_ORDER){
			logger.info("该订单正在支付中，无法进行改价！orderNo="+orderNo+"supplierId="+userId);
			throw new TipsMessageException("该订单正在支付中，无法恢复价格！请联系客户，取消第三方支付订单，再申请恢复价格！");
		}
		if(storeOrderNew.getLockingOrder() == StoreOrderNew.VERSION_UNSUPPORT){
			logger.info("用户下单的APP版本低于3.5.0，不支持改价功能!orderNo="+orderNo+"supplierId="+userId);
			throw new TipsMessageException("用户下单的APP版本低于3.5.0，不支持改价功能!");
		}
		//恢复订单价格
		changePrice(storeOrderNew,storeOrderNew.getOriginalPrice());
		//添加操作日志
		addActionLog(storeOrderNew, storeOrderNew.getOriginalPrice(), userId, SupplierChangeOrderPriceActionLog.ACTION_NAME_RESTORE_PRICE);

	}

//    /**
//     * 恢复订单价格，涉及到子母订单
//     */
//	private void restorePrice(StoreOrderNew storeOrderNew) {
//		long currentTime = System.currentTimeMillis();
//		//恢复价格
//		double supplierChangePrice = 0;
//		double supplierTotalPreferential = DoubleUtil.add(supplierChangePrice, storeOrderNew.getSupplierPreferential());
//		//更新订单
//		StoreOrderNew storeOrderNew2 = new StoreOrderNew();
//		storeOrderNew2.setOrderNo(storeOrderNew.getOrderNo());//设置订单号
//		storeOrderNew2.setTotalPay(storeOrderNew.getOriginalPrice());//设置实付金额
//		storeOrderNew2.setSupplierChangePrice(supplierChangePrice);//改价差额 originalPrice-changePrice
//		storeOrderNew2.setSupplierTotalPreferential(supplierTotalPreferential);//供应商总优惠可以为负，商家把价格改高
//		storeOrderNew2.setUpdateTime(currentTime);//设置更新时间
//		supplierOrderMapper.updateById(storeOrderNew2);//更新订单
//	}



//	/**
//	 * 修复storeOrder表收件人姓名收件人号码收件人地址数据
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public void a() {
//		Wrapper<StoreOrderNew> wrapper = 
//				new EntityWrapper<StoreOrderNew>().eq("status", 0).gt("ParentId", 0);
//		List<StoreOrderNew> selectList = supplierOrderMapper.selectList(wrapper);
//		for (StoreOrderNew storeOrderNew : selectList) {
//			String expressInfo = storeOrderNew.getExpressInfo();
//			System.out.println(storeOrderNew.getOrderNo()+":"+expressInfo);
//			if(!StringUtils.isEmpty(expressInfo)){
//				String[] split = expressInfo.split(",");
//				StoreOrderNew storeOrder = new StoreOrderNew();
//				storeOrder.setOrderNo(storeOrderNew.getOrderNo());
//				storeOrder.setExpressName(split[0]);
//				storeOrder.setExpressPhone(split[1]);
//				storeOrder.setExpressAddress(split[2]);
//				supplierOrderMapper.updateById(storeOrder);
//			}
//		}
//	}

}