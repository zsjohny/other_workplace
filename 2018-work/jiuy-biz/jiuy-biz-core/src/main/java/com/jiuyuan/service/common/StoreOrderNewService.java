package com.jiuyuan.service.common;

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

import com.jiuyuan.constant.MemberPackageType;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.newentity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.mapper.supplier.OrderItemNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.StoreCouponNewMapper;
import com.jiuyuan.dao.mapper.supplier.StoreCouponUseLogNewMapper;
import com.jiuyuan.dao.mapper.supplier.StoreExpressInfoMapper;
import com.jiuyuan.dao.mapper.supplier.StoreMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class StoreOrderNewService implements IStoreOrderNewService {

	private static final Log logger = LogFactory.get("供应商订单Service"); 
	
	private static final int NORMAL_WORK = 0;
	
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
	private StoreCouponNewMapper storeCouponNewMapper;
	
	
	@Autowired
	private StoreCouponUseLogNewMapper storeCouponUseLogNewMapper;
	@Autowired
	private RefundOrderAdminFacade refundOrderAdminFacade;
	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#getSupplierOrderList(long, long, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public List<StoreOrderNew> getSupplierOrderList(long userId, long orderNo, int orderStatus, String phoneNumber, String clothesNumbers,
			String updateTimeStart, String updateTimeEnd, Page<StoreOrderNew> page) {
			Wrapper<StoreOrderNew> wrapper = 
					new EntityWrapper<StoreOrderNew>().eq("status", 0).eq("supplierId",userId).orderBy("UpdateTime", false);
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
			if(!StringUtils.isEmpty(updateTimeStart)){
				wrapper.ge("UpdateTime", getCurrentTimeMillis(updateTimeStart));
			}
			if(!StringUtils.isEmpty(updateTimeEnd)){
				wrapper.ge("UpdateTime", getCurrentTimeMillis(updateTimeEnd));
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

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#getSupplierOrderCount(long)
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
		
		return supplierOrderCount;
	}

	//获取待处理个数（待发货）
	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#getSupplierOrderCountUnDealWithCount(long)
	 */
	@Override
	public Object getSupplierOrderCountUnDealWithCount(long userId) {
		Wrapper<StoreOrderNew> wrapperAllCount = 
				new EntityWrapper<StoreOrderNew>().eq("status", 0).eq("supplierId",userId).eq("OrderStatus", OrderStatus.PAID.getIntValue());
		return supplierOrderMapper.selectCount(wrapperAllCount);
	}
    //获取已发货订单个数
	public Object getSupplierOrderCountHasDelivered(long supplierId){
		Wrapper<StoreOrderNew> wrapper = 
				new EntityWrapper<StoreOrderNew>().eq("status", NORMAL_WORK).eq("supplierId", supplierId).eq("OrderStatus", OrderStatus.DELIVER.getIntValue());
		return supplierOrderMapper.selectCount(wrapper);
	}
	//获取已退款订单个数
	public Object getSupplierOrderCountRefunded(long supplierId){
		Wrapper<StoreOrderNew> wrapper = 
				new EntityWrapper<StoreOrderNew>().eq("status", NORMAL_WORK).eq("supplierId", supplierId).eq("OrderStatus", OrderStatus.REFUNDED.getIntValue());
		return supplierOrderMapper.selectCount(wrapper);
	}
	//获取今日新增个数
	private Object getSupplierOrderCountTodayNewCount(long userId) {
		Wrapper<StoreOrderNew> wrapperAllCount = 
				new EntityWrapper<StoreOrderNew>().eq("status", 0).eq("supplierId",userId).gt("CreateTime", getTodayZeroTimeInMillis());
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
				new EntityWrapper<StoreOrderNew>().eq("status", 0).eq("supplierId",userId);
		return supplierOrderMapper.selectCount(wrapperAllCount);
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#getSupplierOrderByOrderNo(long)
	 */
	@Override
	public Map<String,Object> getSupplierOrderByOrderNo(long orderNo) {
		Map<String,Object> result = new HashMap<String,Object>();
		StoreOrderNew storeOrder =  supplierOrderMapper.selectById(orderNo);
		result.put("orderNo", storeOrder.getOrderNo());
		result.put("totalBuyCount", storeOrder.getTotalBuyCount());
		result.put("orderStatus", OrderStatus.getNameByValue(storeOrder.getOrderStatus()));
		result.put("totalPay", storeOrder.getTotalPay());
		result.put("remark", storeOrder.getRemark());
		result.put("createTime", storeOrder.getCreateTime());
		result.put("storeId", storeOrder.getStoreId());
		result.put("supplierId", storeOrder.getSupplierId());
		result.put("PaymentType", storeOrder.getPaymentType());
		
		StoreExpressInfo entity = new StoreExpressInfo();
		entity.setOrderNo(orderNo);
		entity.setStatus(0);
		StoreExpressInfo storeExpressInfo = storeExpressInfoMapper.selectOne(entity);
		if(storeExpressInfo!=null){
			result.put("expressNo", storeExpressInfo.getExpressOrderNo());
			result.put("expressCompamyName", storeExpressInfo.getExpressSupplier());
		}else{
			result.put("expressNo", "");
			result.put("expressCompamyName", "");
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#getSupplierOrderItemByOrderNo(long)
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
			long productId = storeOrderItem.getProductId();
//			ProductNew product = productNewMapper.getProductById(productId);
			
			ProductNew product = productNewMapper.selectById(productId);
			item.put("firstDetailImage", product.getFirstDetailImage());
			item.put("name", product.getName());
			item.put("clothesNumber", product.getClothesNumber());
			item.put("buyCount", storeOrderItem.getBuyCount());
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
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#deliverGoods(long, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deliverGoods(long orderNo, String expressCompamyName, String expressNo, String remark) {
		long time = System.currentTimeMillis();
		StoreOrderNew storeOrder = new StoreOrderNew();
		storeOrder.setOrderNo(orderNo);
		storeOrder.setOrderStatus(OrderStatus.DELIVER.getIntValue());
//		storeOrder.setExpressCompamyName(expressCompamyName);
//		storeOrder.setExpressNo(expressNo);
		storeOrder.setRemark(remark);
		storeOrder.setUpdateTime(time);
		storeOrder.setSendTime(time);
		supplierOrderMapper.updateById(storeOrder);
		
		StoreExpressInfo storeExpressInfo = new StoreExpressInfo();
		storeExpressInfo.setOrderNo(orderNo);
		storeExpressInfo.setExpressSupplier(expressCompamyName);
		storeExpressInfo.setExpressOrderNo(expressNo);
		storeExpressInfo.setExpressUpdateTime(time);
		storeExpressInfo.setStatus(0);
		storeExpressInfo.setCreateTime(time);
		storeExpressInfo.setUpdateTime(time);
		storeExpressInfoMapper.insert(storeExpressInfo);
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#chargeback(long, java.lang.String)
	 */
	@Override
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
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#getSupplierCustomerList(long, java.lang.String, double, double, int, int, java.lang.String, java.lang.String, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public List<Map<String,Object>> getSupplierCustomerList(long userId, String businessName,String phoneNumber, double moneyMin,
			double moneyMax, int countMin, int countMax, String province, String city, Page<Map<String, Object>> page) {
		System.out.println(page);
		return supplierOrderMapper.getSupplierCustomerList(userId,businessName,phoneNumber, moneyMin,moneyMax,countMin,countMax,province,city,page);
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#getSupplierCustomerAllCount(long)
	 */
	@Override
	public int getSupplierCustomerAllCount(long userId) {
		return supplierOrderMapper.getSupplierCustomerAllCount(userId);
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#getSupplierCustomerTodayNewCount(long)
	 */
	@Override
	public int getSupplierCustomerTodayNewCount(long userId) {
		return supplierOrderMapper.getSupplierCustomerTodayNewCount(userId,getTodayZeroTimeInMillis());
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#deliveryExcel(long, long, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public List<Map<String, Object>> deliveryExcel(long userId, long orderNo, int orderStatus, String phoneNumber,
			String clothesNumbers, String updateTimeStart, String updateTimeEnd, Page<StoreOrderNew> page) {
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
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#orderItemMapOfOrderNos(java.util.Collection)
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
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#orderNewMapOfOrderNos(java.util.Collection)
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
		
		String orderStatus = "";
		switch (orderNew.getOrderStatus()) {
		case 0:
			orderStatus = "未付款";
			break;
		case 5:
			orderStatus = "所有";
			break;
		case 10:
			orderStatus = "已付款";
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
	 * @see com.jiuyuan.service.common.IStoreOrderNewService#getAddrByExp(java.util.List, java.lang.String)
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
     * 使用规则优惠券规则
     * 1.在有效期内
     * 2.该品牌可用
     * 3.当前品牌订单金额符合限额
     * 4.当前
     * 4.若是平台优惠券，未被当前页中的其他订单选中的
     * @param storeId
     * @param brandId 
     * @param orderAmount
     */
	@Override
	public List<StoreCouponNew> availableCoupon(long storeId, long brandId, double orderAmount) {
//			List<StoreCouponNew> resultList = new ArrayList<StoreCouponNew>();
		    //获取对应商家的可用优惠券
			List<StoreCouponNew> list = storeCouponNewMapper.availableCoupon( storeId, String.valueOf(brandId), orderAmount);
//			//获取对应的品牌的或者通用的
//			for(StoreCouponNew storeCouponNew :list){
//				int rangeType = storeCouponNew.getRangeType();
//				if(rangeType == 2){
//					resultList.add(storeCouponNew);
//				}else if(rangeType == 5){
//					String[] rangeTypeIds = storeCouponNew.getRangeTypeIds().split(",");
//					List<String> ids = new ArrayList<String>();
//					for(String id:rangeTypeIds){
//						if(!"".equals(id)){
//							ids.add(id);
//						}
//					}
//					if(ids.contains(String.valueOf(brandId))){
//						resultList.add(storeCouponNew);
//					}
//				}
//				
//			}
			
		return list;
	}
	/**
	 * 获取优惠券信息
	 */
	@Override
	public StoreCouponNew getCouponById(long couponId) {
		return storeCouponNewMapper.selectById(couponId);
	}

	/**
	 * 修改已使用优惠券信息
	 */
	@Override
	public void updateCouponUsed(String[] cidArr, long orderNo) {
		StoreCouponNew entity = new StoreCouponNew();
		entity.setOrderNo(orderNo);
		entity.setStatus(1);
		entity.setUpdateTime(System.currentTimeMillis());
		//修改指定用户类型的优惠券为已使用并绑定订单编号
		Wrapper<StoreCouponNew> wrapper = 
				new EntityWrapper<StoreCouponNew>().eq("Type", 0).eq("Status", 0).in("Id", cidArr);
		int record = storeCouponNewMapper.update(entity, wrapper);
	}

	@Override
	public void insertCouponUseLog(StoreCouponUseLogNew entity) {
		int record = storeCouponUseLogNewMapper.insert(entity);
	}

	@Override
	public List<StoreOrderNew> getStoreOrderByOrderStatus(int intValue) {
		Wrapper<StoreOrderNew> wrapper = new EntityWrapper<StoreOrderNew>();
		wrapper.eq("OrderStatus", intValue);
		List<StoreOrderNew> list =  supplierOrderMapper.selectList(wrapper);
		return list;
	}

	/**
	 * 设置订单为售后中
	 */
	@Override
	public void addRefundSign(long orderNo) {
		StoreOrderNew storeOrderNew = new StoreOrderNew();
		storeOrderNew.setOrderNo(orderNo);
		storeOrderNew.setRefundUnderway(StoreOrderNew.REFUND_UNDERWAY);
		//售后开始时间时间戳
		storeOrderNew.setRefundStartTime(System.currentTimeMillis());
		int i = supplierOrderMapper.updateById(storeOrderNew);
		if(i == -1){
			logger.error("添加售后标志失败！");
			throw new RuntimeException("创建售后失败！");
		}
	}

	@Override
	public StoreOrderNew getStoreOrderByOrderNo(long orderNo) {
		return supplierOrderMapper.selectById(orderNo);
	}

	@Override
	public List<StoreOrderNew> ordersOfOrderNos(Set<Long> freezeOrderNos1) {
		Wrapper<StoreOrderNew> wrapper = new EntityWrapper<StoreOrderNew>();
		wrapper.in("OrderNo", freezeOrderNos1);
		return supplierOrderMapper.selectList(wrapper);
	}
	
	/**
	 * 售后订单退全部金额并且有优惠券时退优惠券
	 */
	@Override
	public void retreatingCoupons(StoreOrderNew order, double refundMoney){
		//售后订单退全部金额并且有优惠券时才可退优惠券
		if(refundMoney>=order.getTotalPay()){
			long orderNo = order.getOrderNo();
			//获取所有该订单已使用的个人平台优惠券
			Wrapper<StoreCouponNew> couponWrapper = new EntityWrapper<StoreCouponNew>().eq("OrderNo", orderNo).eq("Status", 1).eq("supplier_id", 0);
			List<StoreCouponNew> couponList = storeCouponNewMapper.selectList(couponWrapper);
			if(couponList != null && couponList.size() > 0){
				//修改个人优惠券为未使用
				couponWrapper.eq("Type", 0);
				StoreCouponNew entity = new StoreCouponNew();
				entity.setStatus(0);
				entity.setUpdateTime(System.currentTimeMillis());
				entity.setOrderNo(0L);
				storeCouponNewMapper.update(entity, couponWrapper);
				for(StoreCouponNew coupon : couponList){
					//生成返还优惠券的日志
		    		Wrapper<StoreCouponUseLogNew> couponUseLogWrapper = new EntityWrapper<StoreCouponUseLogNew>().eq("OrderNo", orderNo)
		    				.eq("CouponId", coupon.getId()).eq("UserId", order.getStoreId()).eq("Status", 0);
		    		List<StoreCouponUseLogNew> couponUseLogList = storeCouponUseLogNewMapper.selectList(couponUseLogWrapper);
		    		for (StoreCouponUseLogNew storeCouponUseLogNew : couponUseLogList) {
		    			StoreCouponUseLogNew couponUseLog = new StoreCouponUseLogNew();
		    			couponUseLog.setId(storeCouponUseLogNew.getId());
			    		couponUseLog.setStatus(1);
			    		couponUseLog.setCreateTime(System.currentTimeMillis());
//			    		storeCouponUseLogNewMapper.insert(couponUseLog);
			    		storeCouponUseLogNewMapper.updateById(couponUseLog);
					}
				}
			}
			//获取所有该订单已使用的通用平台优惠券
			Wrapper<StoreCouponUseLogNew> couponUseLogWrapper = new EntityWrapper<StoreCouponUseLogNew>().eq("OrderNo", orderNo).eq("Status", 0).eq("supplier_id", 0);
			List<StoreCouponUseLogNew> couponUseLogList = storeCouponUseLogNewMapper.selectList(couponUseLogWrapper);
			for (StoreCouponUseLogNew storeCouponUseLogNew : couponUseLogList) {
				StoreCouponUseLogNew couponUseLog = new StoreCouponUseLogNew();
				couponUseLog.setId(storeCouponUseLogNew.getId());
				couponUseLog.setStatus(1);
				couponUseLog.setCreateTime(System.currentTimeMillis());
				storeCouponUseLogNewMapper.updateById(couponUseLog);
			}
//			List<Long> couponId = new ArrayList<Long>();
//			for (StoreCouponUseLogNew storeCouponUseLogNew : couponUseLogList) {
//				couponId.add(storeCouponUseLogNew.getCouponId());
//			}
//			Wrapper<StoreCouponNew> couponAllMemberWrapper = new EntityWrapper<StoreCouponNew>().in("Id", couponId).eq("Type", 1);
//			List<StoreCouponNew> couponAllMemberList = storeCouponNewMapper.selectList(couponAllMemberWrapper);
//			if(couponAllMemberList != null && couponAllMemberList.size() > 0){
//				for(StoreCouponNew coupon : couponAllMemberList){
//					//生成返还优惠券的日志
//					StoreCouponUseLogNew couponUseLog = new StoreCouponUseLogNew();
//					couponUseLog.setOrderNo(orderNo);
//					couponUseLog.setUserId(order.getStoreId());
//					couponUseLog.setCouponId(coupon.getId());
//					couponUseLog.setStatus(1);
//					couponUseLog.setActualDiscount(coupon.getMoney());
//					couponUseLog.setCreateTime(System.currentTimeMillis());
//					storeCouponUseLogNewMapper.insert(couponUseLog);
//				}
//			}
		}
	}
	/**
	 * 关闭订单
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void closeOrder(StoreOrderNew storeOrder) {
		StoreOrderNew orderNew = new StoreOrderNew();
		orderNew.setOrderNo(storeOrder.getOrderNo());
		orderNew.setOrderStatus(100);//交易关闭
		orderNew.setOrderCloseType(105);//关闭类型：平台关闭
		orderNew.setOrderCloseTime(System.currentTimeMillis());
		orderNew.setCancelReason("平台关闭");
		supplierOrderMapper.updateById(orderNew);
		//获取售后单
		List<RefundOrder> list = refundOrderAdminFacade.getNotCloseRefundOrderListByOrderNo(storeOrder.getOrderNo());
		if (list.size()>0) {
			//关闭售后单
			for ( RefundOrder refundOrder : list) {
				refundOrderAdminFacade.platformCloseRefundOrder(refundOrder.getId(), "");//不写原因
			}
		}
	}
	/**
	 * 挂起订单
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void hangUpOrder(StoreOrderNew storeOrder) {
		StoreOrderNew orderNew = new StoreOrderNew();
		orderNew.setOrderNo(storeOrder.getOrderNo());
		orderNew.setHangUp(1);//挂起
		supplierOrderMapper.updateById(orderNew);
	}
    /**
     * 锁住订单，不可以改价
     */
	@Override
	public void lockStoreOrder(long orderNo, long userId) {
	    //获取该母订单下的所有订单，包含母订单	
		Wrapper<StoreOrderNew> wrapper = new EntityWrapper<StoreOrderNew>();
		wrapper.eq("StoreId", userId)
		       .eq("orderNo", orderNo)
		       .or()
		       .eq("StoreId", userId)
		       .eq("ParentId", orderNo);
		List<StoreOrderNew> list = supplierOrderMapper.selectList(wrapper);
		//全部锁定
		for(StoreOrderNew order :list){
			StoreOrderNew storeOrderNew = new StoreOrderNew();
			storeOrderNew.setOrderNo(order.getOrderNo());
			storeOrderNew.setLockingOrder(StoreOrderNew.LOCKING_ORDER);
			supplierOrderMapper.updateById(storeOrderNew);
		}
	}
    /**
     * 解锁订单,可以改价
     */
	@Override
	public void unlockStoreOrder(long orderNo, long userId) {
	    //获取该母订单下的所有订单，包含母订单	
		Wrapper<StoreOrderNew> wrapper = new EntityWrapper<StoreOrderNew>();
		wrapper.eq("StoreId", userId)
		       .eq("orderNo", orderNo)
		       .or()
		       .eq("ParentId", orderNo)
		       .eq("StoreId", userId);
		List<StoreOrderNew> list = supplierOrderMapper.selectList(wrapper);
		//全部解锁
		for(StoreOrderNew order :list){
			StoreOrderNew storeOrderNew = new StoreOrderNew();
			storeOrderNew.setOrderNo(order.getOrderNo());
			storeOrderNew.setLockingOrder(StoreOrderNew.UNLOCKING_ORDER);
			supplierOrderMapper.updateById(storeOrderNew);
		}
	}

	@Override
	public List<StoreOrderNew> getSuborderByParentOrder(long orderNo) {
		Wrapper<StoreOrderNew> wrapper = new EntityWrapper<StoreOrderNew>();
		wrapper.eq("ParentId", orderNo);
		List<StoreOrderNew> lists = supplierOrderMapper.selectList(wrapper);
		return lists;
	}

	@Override
	public List<Map<String, Object>> exportOrderData(long beginTime, long endTime) {
		return  supplierOrderMapper.exportOrderData(beginTime,endTime);
	
	}

	@Override
	public int getRestrictionActivityProductAllBuyCount(long restrictionActivityProductId, long storeId) {
		Integer restrictionActivityProductAllBuyCount = supplierOrderMapper.getRestrictionActivityProductAllBuyCount(restrictionActivityProductId,storeId);
		if(restrictionActivityProductAllBuyCount != null){
			return restrictionActivityProductAllBuyCount;
		}else{
			return 0;
		}
	}

	@Override
	public void updatePaytime(Long orderNo) {
		StoreOrderNew storeOrderNew = new StoreOrderNew();
		storeOrderNew.setOrderNo(orderNo);
		storeOrderNew.setPayTime(System.currentTimeMillis());
		supplierOrderMapper.updateById(storeOrderNew);
		
	}



	/**
	 * 查询用户未支付的会员订单
	 *
	 * @param user user
	 * @param memberPackageType memberPackageType
	 * @param waitPayTotalPrice waitPayTotalPrice
	 * @param clientPlatform clientPlatform
	 * @param ip ip
	 * @return com.jiuyuan.entity.newentity.StoreOrderNew
	 * @author Charlie
	 * @date 2018/8/18 13:01
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public StoreOrderNewSon findNoPayMemberOrder(StoreBusiness user, Integer memberPackageType, Double waitPayTotalPrice, ClientPlatform clientPlatform, String ip) {
		logger.info("会员下单判断-查询已有未支付订单,如果有则直接返回 id={},type={}",user.getId (), memberPackageType);
		//查询用户未支付的订单 最新订单
		StoreOrderNewSon noPayOrder = supplierOrderMapper.findLastNoPayAndNoPastDueMemberOrder (user.getId (), memberPackageType);
		if (noPayOrder == null) {
			logger.info("会员下单判断-订单为空 id={},type={}",user.getId (), memberPackageType);
			return null;
		}
		//到期时间 小于等于 当前时间 则订单失效
		if (noPayOrder.getExpiredTime () <= System.currentTimeMillis () ) {
			logger.info("会员下单判断-订单过期 删除当前订单及细目 orderNo={},orderItemId={}",noPayOrder.getOrderNo(), noPayOrder.getOrderItemId());
			supplierOrderMapper.deleteOrderByOrderNo(noPayOrder.getOrderNo());
			supplierOrderMapper.deleteOrderItemById(noPayOrder.getOrderItemId());
			return null;
		}else {
			return noPayOrder;
		}
/*//   ==================    重复利用历史订单号(未测试)    =====================
		StoreOrderNew noPayOrder = supplierOrderMapper.findLastNoPayAndNoPastDueMemberOrder (user.getId (), packageConfig.getType ());
		if (noPayOrder == null) {
			return null;
		}
		//避免频繁操作订单表, 如果商品还有10min过期, 则直接返回订单
		//十分钟
		long allowTime = 1000*60*10;
		if (noPayOrder.getExpiredTime () - System.currentTimeMillis () > allowTime) {
			return noPayOrder;
		}

		//初始化订单表
		StoreOrderNew orderNew = OrderCreateDataFacade.buildDefaultMemberOrder (user, waitPayTotalPrice, clientPlatform, ip);
		orderNew.setOrderNo (noPayOrder.getOrderNo ());
		orderNew.setParentId (noPayOrder.getOrderNo ());
		int rec = supplierOrderMapper.salvageHistoryOrder (orderNew);
		if (rec != 1) {
			logger.error ("循环利用用户已有的未支付会员订单失败,将为用户创建一个新的订单 orderNo[{}]", orderNew.getOrderNo ());
			return null;
		}
		//初始化订单详情表
		StoreOrderItemNew query = new StoreOrderItemNew ();
		query.setOrderNo (noPayOrder.getOrderNo ());
		StoreOrderItemNew historyOrderItem = orderItemNewMapper.selectOne (query);
		StoreOrderItemNew orderItemNew = OrderCreateDataFacade.buildDefaultMemberOrderItem (waitPayTotalPrice, orderNew, packageConfig);
		orderItemNew.setId (historyOrderItem.getId ());
		rec = orderItemNewMapper.salvageHistoryOrderItem (orderItemNew);
		if (rec != 1) {
			logger.error ("循环利用用户已有的未支付会员订单Item失败 orderNo[{}]", orderNew.getOrderNo ());
			throw new RuntimeException ("更新订单详情失败");
		}
		return orderNew;*/

	}

	/**
	 * 查询用户已购买的会员
	 *
	 * @param storeId storeId
	 * @param packageType packageType
	 * @return java.util.List<com.jiuyuan.entity.newentity.StoreOrderNew>
	 * @author Charlie
	 * @date 2018/9/26 10:45
	 */
	@Override
	public List<StoreOrderNew> findHistorySuccessMemberOrder(Long storeId, Integer packageType) {
		return supplierOrderMapper.findHistorySuccessMemberOrder (storeId, packageType);
	}


}