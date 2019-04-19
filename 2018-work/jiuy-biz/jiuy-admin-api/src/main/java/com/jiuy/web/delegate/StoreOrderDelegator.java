package com.jiuy.web.delegate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.business.facade.StoreOrderFacade;
import com.jiuy.core.dao.ExpressSupplierDao;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.dao.StoreExpressInfoDao;
import com.jiuy.core.dao.StoreOrderDao;
import com.jiuy.core.dao.StoreOrderDiscountLogDao;
import com.jiuy.core.dao.StoreOrderItemDao;
import com.jiuy.core.dao.StoreOrderLogDao;
import com.jiuy.core.dao.StoreOrderMessageBoardDao;
import com.jiuy.core.dao.UserBankCardPayDiscountDao;
import com.jiuy.core.dao.mapper.BrandExpressInfoDao;
import com.jiuy.core.dao.mapper.BrandOrderDao;
import com.jiuy.core.dao.mapper.BrandOrderItemDao;
import com.jiuy.core.dao.mapper.BrandOrderLogDao;
import com.jiuy.core.dao.modelv2.ProductSKUMapper;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.brandexpress.BrandExpressInfo;
import com.jiuy.core.meta.brandorder.BrandOrder;
import com.jiuy.core.meta.brandorder.BrandOrderItem;
import com.jiuy.core.meta.brandorder.BrandOrderSO;
import com.jiuy.core.meta.brandorder.BrandOrderUO;
import com.jiuy.core.meta.member.BrandBusinessSO;
import com.jiuy.core.service.AddressService;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.ExpressInfoService;
import com.jiuy.core.service.ExpressSupplierService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.StoreExpressInfoService;
import com.jiuy.core.service.StoreOrderLogService;
import com.jiuy.core.service.brandorder.BrandOrderService;
import com.jiuy.core.service.logistics.LOWarehouseService;
import com.jiuy.core.service.member.BrandBusinessService;
import com.jiuy.core.service.order.OrderItemService;
import com.jiuy.core.service.storeorder.StoreOrderItemService;
import com.jiuy.core.service.storeorder.StoreOrderMessageBoardService;
import com.jiuy.core.service.storeorder.StoreOrderService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentTypeDetail;
import com.jiuyuan.constant.order.StoreOrderStatus;
import com.jiuyuan.dao.mapper.supplier.StoreExpressInfoMapper;
import com.jiuyuan.dao.mapper.supplier.StoreMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.BrandBusiness;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.UserBankCardPayDiscount;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.newentity.ExpressSupplier;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.StoreExpressInfo;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeorder.StoreOrder;
import com.jiuyuan.entity.storeorder.StoreOrderDetailVO;
import com.jiuyuan.entity.storeorder.StoreOrderDiscountLog;
import com.jiuyuan.entity.storeorder.StoreOrderItem;
import com.jiuyuan.entity.storeorder.StoreOrderItemDetailVO;
import com.jiuyuan.entity.storeorder.StoreOrderLog1;
import com.jiuyuan.entity.storeorder.StoreOrderMessageBoard;
import com.jiuyuan.entity.storeorder.StoreOrderSO;
import com.jiuyuan.entity.storeorder.StoreOrderVO;
//import com.jiuyuan.entity.Address;
import com.jiuyuan.util.CollectionUtil;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午4:28:28
*/
@Service
public class StoreOrderDelegator {
	
	private static final Logger logger = Logger.getLogger(StoreOrderDelegator.class);
	
	private Set<Long> globalOrderNos = new HashSet<Long>();
	
	@Resource
	private StoreOrderService storeOrderService;
	
	@Resource
	private StoreOrderItemService storeOrderItemService;
	
	@Resource
	private StoreOrderDao storeOrderDao;
	
	@Resource
	private StoreOrderItemDao storeOrderItemDao;
	
	@Resource
	private StoreOrderFacade storeOrderFacade;
	
	@Resource
	private StoreBusinessDao storeBusinessDao;
	
    @Autowired
    private ProductSKUMapper productSKUMapper;
    
    @Autowired
    private ProductService productService;
    
    @Resource
    private StoreOrderLogDao storeOrderLogDao;
	
    @Resource
    private StoreExpressInfoDao storeExpressInfoDao;
    
    @Resource
    private StoreExpressInfoService storeExpressInfoService;
    
	@Autowired
	private ExpressSupplierService expressSupplierService;
	
    @Autowired
    private ExpressSupplierDao expressSupplierDao;
	
	@Resource
	private StoreOrderDiscountLogDao storeOrderDiscountLogDao;
	
    @Autowired
    private UserBankCardPayDiscountDao userBankCardPayDiscountDao;
    
    @Resource
    private StoreOrderLogService storeOrderLogService;

    @Resource
    private StoreOrderMessageBoardService storeOrderMessageBoardService;
    
    @Autowired
    private OrderItemService orderItemService;
    
    @Autowired
	private BrandOrderService brandOrderService;
    
    @Autowired
    private BrandLogoServiceImpl baBrandLogoServiceImpl;
   
	
	@Autowired
	private BrandBusinessService brandBusinessService;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private ExpressInfoService expressInfoService;
	
	@Autowired
	private BrandOrderDao brandOrderDao;

	@Autowired
	private BrandOrderItemDao brandOrderItemDao;
	
	@Autowired
	private StoreOrderMessageBoardDao storeOrderMessageBoardDao;

    @Autowired
    private LOWarehouseService loWarehouseService;
    
    @Autowired
    private BrandExpressInfoDao brandExpressInfoDao;
    
    @Autowired
    private BrandOrderLogDao brandOrderLogDao;
    
    @Autowired
    private StoreExpressInfoMapper storeExpressInfoMapper;
    
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;
    
	public List<List<StoreOrderVO>> searchStoreOrders(PageQuery pageQuery, String orderNo, String clothesNum,
			String expressOrderNo, String skuNo, int orderType, long storeNumber, String receiver, String phone,
			long startTime, long endTime, int orderStatus) {
		
		Set<Long> orderNos = new HashSet<Long>();
		if(isFilter(expressOrderNo,clothesNum,skuNo)){
			//快递单号、款号、sku编号筛选出的初步符合的OrderNo
			orderNos = (Set<Long>)filterOrderNos(expressOrderNo,clothesNum,skuNo);
		}
		
		//根据商家号查找门店id
		long storeId = -1;
		if (storeNumber != -1) {
			long resultId = storeBusinessDao.getIdByBusinessNumber(storeNumber); 
			if(resultId<1){
				return new ArrayList<>();
			}
			storeId = resultId;
		}
		
		//获取原始数据
		List<StoreOrderVO> list = storeOrderFacade.searchStoreOrders(pageQuery, orderNo, orderType, storeId, receiver, phone,
	            startTime, endTime, orderStatus, orderNos);
		
		//全局变量赋值,为了给searchStoreOrderCount()调用
		globalOrderNos = orderNos;
		
		List<List<StoreOrderVO>> lists = new ArrayList<List<StoreOrderVO>>();
		Map<Long, List<StoreOrderVO>> map = new HashMap<Long, List<StoreOrderVO>>();
		
		//快递
		Map<Long, StoreExpressInfo> exMap = null;
		
		// 全部/组合/拆分
		Set<Long> allOrderNos = new HashSet<Long>();
		Set<Long> combinationOrderNos = new HashSet<Long>();
		Set<Long> splitOrderNos = new HashSet<Long>();
		Map<Long, StoreOrderVO> splitStoreOrderMap = new HashMap<Long, StoreOrderVO>();
		Map<Long, StoreOrderVO> combinationStoreOrderMap = new HashMap<Long,StoreOrderVO>();
		Map<Long, StoreOrderVO> normalStoreOrderMap = new HashMap<Long, StoreOrderVO>();
		
		assemble(list, splitOrderNos, splitStoreOrderMap, combinationOrderNos, combinationStoreOrderMap, normalStoreOrderMap, allOrderNos);
		
		Map<Long, List<StoreOrder>> splitStoreOrdersMap = storeOrderFacade.splitMapOfOrderNos(splitOrderNos);
		Map<Long, List<StoreOrder>> combinationStoreOrdersMap = storeOrderFacade.combinationMapOfOrderNos(combinationOrderNos);
		
		//获取所有相关的订单购买数量
		assembleAllOrderNos(allOrderNos, splitStoreOrdersMap,combinationStoreOrdersMap);
		
		Map<Long, Integer> buyCountMapOfOrderNo = storeOrderItemService.buyCountMapOfOrderNo(allOrderNos);
		exMap = storeExpressInfoService.expressInfoMapOfOrderNos(allOrderNos);
		Map<String, ExpressSupplier> suMap = expressSupplierService.itemByEngName();
		
		if(orderType == 0 || orderType ==2){
			//封装普通订单
			assembleNormal(normalStoreOrderMap, buyCountMapOfOrderNo, exMap, map, suMap);
		}
		if(orderType == 0 || orderType == 3){
			//封装拆分订单
			assembleSplit(splitStoreOrdersMap, splitStoreOrderMap, buyCountMapOfOrderNo, exMap, map, suMap);
		}
		if(orderType == 0 || orderType == 1){
			//封装组合订单
			assembleCombination(combinationStoreOrdersMap, combinationStoreOrderMap, buyCountMapOfOrderNo, exMap, map, suMap);
		}
		
		if(orderStatus == StoreOrderStatus.PAID.getIntValue() 
				|| orderStatus == StoreOrderStatus.DELIVER.getIntValue()){
			for(StoreOrderVO storeOrderVO : list){
				long storeOrderNo = storeOrderVO.getOrderNo();
				if(storeOrderVO.getParentId() != -1){
					lists.add(map.get(storeOrderNo));
				}
			}
		} else {
			for(StoreOrderVO storeOrderVO : list){
				long storeOrderNo = storeOrderVO.getOrderNo();
				lists.add(map.get(storeOrderNo));
			}
		}
		
		return lists;
	}

	public int searchStoreOrdersCount(String orderNo, String clothesNum, String expressOrderNo, String skuNo,
			int orderType, long storeNumber, String receiver, String phone, long startTime, long endTime,
			int orderStatus) {
		Set<Long> orderNos = globalOrderNos;
		
		if(isFilter(expressOrderNo, clothesNum, skuNo)) {
			if(orderNos.size() < 1) {
				return 0;
			}
		}
		
		//根据商家号查找门店id
		long storeId = -1;
		if (storeNumber != -1) {
			long resultId = storeBusinessDao.getIdByBusinessNumber(storeNumber); 
			if(resultId<1){
				return 0;
			}
			storeId = resultId;
		}
		
		return storeOrderFacade.searchStoreOrdersCount(orderNo, orderType, storeId, receiver, phone,
	            startTime, endTime, orderStatus, orderNos);
	}
	
	private boolean isFilter(String expressOrderNo, String clothesNum, String skuNo) {
		if(StringUtils.equals("", skuNo) && StringUtils.equals("", clothesNum) && StringUtils.equals("", expressOrderNo)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 通过筛选条件过滤
	 * @param expressOrderNo 快递单号
	 * @param clothesNum 衣服款号
	 * @param skuNo sku编码
	 * @return
	 */
	public Collection<Long> filterOrderNos(String expressOrderNo, String clothesNum, String skuNo) {
        Set<Long> expressOrderNos = new HashSet<Long>();

		//物流查订单
        if (!StringUtils.equals("", expressOrderNo)) {
            List<StoreExpressInfo> expressInfos = storeExpressInfoDao.expressInfoOfBlurOrderNo(expressOrderNo);
            for (StoreExpressInfo expressInfo : expressInfos) {
                expressOrderNos.add(expressInfo.getOrderNo());
            }
        }
 
        //款号和skuNo查orderNo
        Set<Long> skuIds = new HashSet<Long>();
        if (!StringUtils.equals("", clothesNum) || !StringUtils.equals("", skuNo)) {
            List<ProductSKU> productSKUs = productSKUMapper.productSkuOfBlurClothesNo8SkuNo(clothesNum, skuNo);
            for (ProductSKU productSKU : productSKUs) {
                skuIds.add(productSKU.getId());
            }
        }
        List<StoreOrderItem> orderItems = new ArrayList<StoreOrderItem>();
        Set<Long> orderNos = new HashSet<Long>();
        if(expressOrderNos.size() > 0) {
        	orderNos.addAll(expressOrderNos);
            orderItems = storeOrderItemService.orderItemsOfSkuIds(skuIds, expressOrderNos);
        } else {
            orderItems = storeOrderItemService.orderItemsOfSkuIds(skuIds, null);
        }
        for (StoreOrderItem orderItem : orderItems) {
            orderNos.add(orderItem.getOrderNo());
        }
        
        return orderNos;
	}
	
	private void assemble(List<StoreOrderVO> list, Set<Long> splitOrderNos, Map<Long, StoreOrderVO> splitStoreOrderMap,
			Set<Long> combinationOrderNos, Map<Long, StoreOrderVO> combinationStoreOrderMap,
			Map<Long, StoreOrderVO> normalStoreOrderMap, Set<Long> allOrderNos) {
		
		for(StoreOrderVO storeOrderVO : list){
			long storeOrderNo = storeOrderVO.getOrderNo();
			if(storeOrderVO.getParentId() == -1){
				splitOrderNos.add(storeOrderNo);
				splitStoreOrderMap.put(storeOrderVO.getOrderNo(), storeOrderVO);
			}else if(storeOrderVO.getMergedId() == -1){
				combinationOrderNos.add(storeOrderNo);
				combinationStoreOrderMap.put(storeOrderVO.getOrderNo(), storeOrderVO);
			} else {
				normalStoreOrderMap.put(storeOrderVO.getOrderNo(), storeOrderVO);
			}
			
			allOrderNos.add(storeOrderNo);
		}
		
	}
	
	private void assembleAllOrderNos(Set<Long> allOrderNos, Map<Long, List<StoreOrder>> splitStoreOrdersMap,
			Map<Long, List<StoreOrder>> combinationStoreOrdersMap) {
		
		for(Map.Entry<Long, List<StoreOrder>> entry : splitStoreOrdersMap.entrySet()) {
			for(StoreOrder storeOrder : entry.getValue()) {
				allOrderNos.add(storeOrder.getOrderNo());
			}
		}
		for(Map.Entry<Long, List<StoreOrder>> entry : combinationStoreOrdersMap.entrySet()) {
			for(StoreOrder storeOrder : entry.getValue()) {
				allOrderNos.add(storeOrder.getOrderNo());
			}
		}
		
	}
	
	private void assembleNormal(Map<Long, StoreOrderVO> normalStoreOrderMap, Map<Long, Integer> buyCountMapOfOrderNo,
			Map<Long, StoreExpressInfo> exMap, Map<Long, List<StoreOrderVO>> map, Map<String, ExpressSupplier> suMap) {
		
		for (Map.Entry<Long, StoreOrderVO> entry : normalStoreOrderMap.entrySet()) {
    		long storeOrderNo = entry.getKey();
    		StoreOrderVO storeOrderVO = entry.getValue();
    		
    		Integer buyCount = buyCountMapOfOrderNo.get(storeOrderNo);
    		if(buyCount == null) {
    			storeOrderVO.setBuyCount(-1);
    		} else {
    			storeOrderVO.setBuyCount(buyCount);
    		}
    		StoreExpressInfo storeExpressInfo = exMap.get(storeOrderVO.getOrderNo());
    		if(storeExpressInfo != null) {
    			assembleStoreExpressInfo(storeExpressInfo, storeOrderVO, suMap);
    		}
    		
    		map.put(storeOrderNo, CollectionUtil.createList(storeOrderVO));
    	}
		
	}
	
	private void assembleSplit(Map<Long, List<StoreOrder>> splitStoreOrdersMap, Map<Long, StoreOrderVO> splitStoreOrderMap, Map<Long, Integer> buyCountMapOfOrderNo,
			Map<Long, StoreExpressInfo> exMap, Map<Long, List<StoreOrderVO>> map, Map<String, ExpressSupplier> suMap) {
		
		for(Map.Entry<Long, List<StoreOrder>> entry : splitStoreOrdersMap.entrySet()) {
			long storeOrderNo = entry.getKey();
			List<StoreOrder> storeOrders = entry.getValue();
			
			StoreOrderVO storeOrderVO2 = splitStoreOrderMap.get(storeOrderNo);
			List<StoreOrderVO> storeOrderVOs = new ArrayList<StoreOrderVO>();
			storeOrderVOs.add(storeOrderVO2);
			int totalBuyCount = 0;
			for(StoreOrder storeOrder : storeOrders) {
				StoreOrderVO storeOrderVO = new StoreOrderVO();
				BeanUtils.copyProperties(storeOrder, storeOrderVO);
				
				int buyCount = buyCountMapOfOrderNo.get(storeOrderVO.getOrderNo());
				storeOrderVO.setBuyCount(buyCount);
				StoreExpressInfo storeExpressInfo = exMap.get(storeOrderVO.getOrderNo());
				if(storeExpressInfo != null) {
					assembleStoreExpressInfo(storeExpressInfo, storeOrderVO, suMap);
				}
				
				storeOrderVOs.add(storeOrderVO);
				totalBuyCount += buyCount;
			}
			storeOrderVO2.setBuyCount(totalBuyCount);
			map.put(storeOrderNo, storeOrderVOs);
		}
		
	}
	
	private void assembleCombination(Map<Long, List<StoreOrder>> combinationStoreOrdersMap,
			Map<Long, StoreOrderVO> combinationStoreOrderMap, Map<Long, Integer> buyCountMapOfOrderNo,
			Map<Long, StoreExpressInfo> exMap, Map<Long, List<StoreOrderVO>> map, Map<String, ExpressSupplier> suMap) {
		
		for(Map.Entry<Long, List<StoreOrder>> entry : combinationStoreOrdersMap.entrySet()) {
			long storeOrderNo = entry.getKey();
			List<StoreOrder> storeOrders = entry.getValue();
			
			StoreOrderVO storeOrderVO2 = combinationStoreOrderMap.get(storeOrderNo);
			List<StoreOrderVO> storeOrderVOs = new ArrayList<StoreOrderVO>();
			storeOrderVOs.add(storeOrderVO2);
			int totalBuyCount = 0;
			for(StoreOrder storeOrder : storeOrders) {
				StoreOrderVO storeOrderVO = new StoreOrderVO();
				BeanUtils.copyProperties(storeOrder, storeOrderVO);
				
				int buyCount = buyCountMapOfOrderNo.get(storeOrderVO.getOrderNo());
				storeOrderVO.setBuyCount(buyCount);
				StoreExpressInfo storeExpressInfo = exMap.get(storeOrderVO.getOrderNo());
				if(storeExpressInfo != null) {
					assembleStoreExpressInfo(storeExpressInfo, storeOrderVO, suMap);
				}
				
				storeOrderVOs.add(storeOrderVO);
				totalBuyCount += buyCount;
			}
			storeOrderVO2.setBuyCount(totalBuyCount);
			map.put(storeOrderNo, storeOrderVOs);
		}
		
	}
	
	private void assembleStoreExpressInfo(StoreExpressInfo storeExpressInfo, StoreOrderVO storeOrderVO, Map<String, ExpressSupplier> suMap) {
		ExpressSupplier expressSupplier = suMap.get(storeExpressInfo.getExpressSupplier());
		if (expressSupplier == null) {
			logger.error("找不到对应的物流供应商。" + storeExpressInfo.getExpressSupplier());
		}
		storeOrderVO.setExpressOrderNo(storeExpressInfo.getExpressOrderNo());
		storeOrderVO.setExpressSupplier("");
	}

	public StoreOrderDetailVO loadStoreOrderDetailInfo(long orderNo) {
		List<StoreOrder> storeOrders = storeOrderDao.storeOrdersOfOrderNos(CollectionUtil.createList(orderNo));
		
		if(storeOrders.size()<1){
			return new StoreOrderDetailVO();
		}
		
		StoreOrder storeOrder = storeOrders.get(0);
		long storeId = storeOrder.getStoreId();
		double totalPay = storeOrder.getTotalPay();
		double totalExpressMoney = storeOrder.getTotalExpressMoney();
		
		StoreOrderDetailVO storeOrderDetailVO = new StoreOrderDetailVO();
		
		StoreBusiness storeBusiness = storeBusinessDao.getByStoreId(storeId);
		storeOrderDetailVO.setStoreBusiness(storeBusiness);
		storeOrderDetailVO.setOrderNo(orderNo);
		storeOrderDetailVO.setCreateTime(storeOrder.getCreateTime());
		storeOrderDetailVO.setTotalPay(totalPay+totalExpressMoney);
		storeOrderDetailVO.setPostage(totalExpressMoney);
		storeOrderDetailVO.setPayTime(storeOrder.getPayTime());
		storeOrderDetailVO.setCommission(storeOrder.getCommission());
		
		List<StoreOrderDiscountLog> storeOrderDiscountLogs = storeOrderDiscountLogDao.getByNo(orderNo);
		if(storeOrderDiscountLogs.size() > 0){
			storeOrderDetailVO.setStoreOrderDiscountLogs(storeOrderDiscountLogs);
		}
		
		UserBankCardPayDiscount userBankCardPayDiscount = userBankCardPayDiscountDao.search(orderNo);
		if(userBankCardPayDiscount != null){
			storeOrderDetailVO.setPayDiscount(userBankCardPayDiscount.getDiscountAmt());
		}
		
		if (storeOrder.getBrandOrder() > 0) {
			storeOrderDetailVO.setBrandOrderInfo(assembleBrandOrderInfo(storeOrder.getBrandOrder()));
		}
		
		List<StoreOrderLog1> storeOrderLogs = storeOrderLogService.storeOrderLogPayOfOrderNos(CollectionUtil.createList(orderNo));
		if(storeOrderLogs.size() == 1){
			StoreOrderLog1 log = storeOrderLogs.get(0);
			long createTime = log.getCreateTime();
			storeOrderDetailVO.setPayTime(createTime);
		}
		
		List<StoreExpressInfo> storeExpressInfos = storeExpressInfoDao.expressInfoOfOrderNos(CollectionUtil.createList(orderNo));
		Map<String, ExpressSupplier> supplierMap = expressSupplierDao.itemByEngName();
		//获取订单的物流信息
		if(storeExpressInfos.size() == 1){
			//若有
			StoreExpressInfo storeExpressInfo = storeExpressInfos.get(0);
			//获取该物流的物流商
			ExpressSupplier expressSupplier = supplierMap.get(storeExpressInfo.getExpressSupplier());
			String logisticsInfo = "";
			//倘若查不到物流供应商就应该报警告
			if(expressSupplier != null){
				logisticsInfo = logisticsInfo + expressSupplier.getCnName();
				logisticsInfo = logisticsInfo + storeExpressInfo.getExpressOrderNo();
				storeOrderDetailVO.setLogisticsInfo(logisticsInfo);
//				throw new ParameterErrorException("找不到对应的快递公司英文名EngName！");
			}else{
				logger.info("根据快递名称【"+storeExpressInfo.getExpressSupplier()+"】找不到对应快递公司信息，请注意排除问题！！！");
				storeOrderDetailVO.setLogisticsInfo(logisticsInfo);
			}
			
		}
		
		long splitOrderNo = storeOrder.getParentId();
		long combinationNo = storeOrder.getMergedId();
		storeOrderDetailVO.setSplitOrderNo(splitOrderNo);
		storeOrderDetailVO.setCombinationOrderNo(combinationNo);
		
		//获取相关的所有订单
		Set<Long> orderNos = new HashSet<>();
		Set<Long> childOfSplit = new HashSet<>();
		Set<Long> childOfCombination = new HashSet<>();
		
		storeOrderDetailVO.setNormalOrderNos(childOfSplit);
		storeOrderDetailVO.setCombinationRelativeOrderNos(childOfCombination);
		
		orderNos.add(orderNo);
		if(splitOrderNo == -1){
			List<StoreOrder> storeOrders2 =  storeOrderService.childOfSplitOrderNos(CollectionUtil.createSet(orderNo));
			for(StoreOrder storeOrder2 : storeOrders2){
				long orderNo2 = storeOrder2.getOrderNo();
				orderNos.add(orderNo2);
				childOfSplit.add(orderNo2);
			}
			orderNos.remove(orderNo);//*********zhanyh
		} else if(combinationNo == -1){
			List<StoreOrder> storeOrders2 = storeOrderService.childOfCombinationOrderNos(CollectionUtil.createList(orderNo));
			for(StoreOrder storeOrder2 : storeOrders2){
				long orderNo2 = storeOrder2.getOrderNo();
				orderNos.add(orderNo2);
				childOfCombination.add(orderNo2);
			}
		} else if(combinationNo > 0){
			List<StoreOrder> storeOrders2 = storeOrderService.childOfCombinationOrderNos(CollectionUtil.createList(combinationNo));
			for(StoreOrder storeOrder2 : storeOrders2){
				long orderNo2 = storeOrder2.getOrderNo();
				childOfCombination.add(orderNo2);
			}
			childOfCombination.remove(orderNo);
		}
		
		List<Map<String, Object>> resultMaps = storeOrderItemService.srchSelfParamsOfOrderNos(orderNos);
		List<StoreOrderItemDetailVO> storeOrderItemDetailVOs = new ArrayList<StoreOrderItemDetailVO>();
		storeOrderDetailVO.setStoreOrderItemDetailVOs(storeOrderItemDetailVOs);
		
		for(Map<String, Object> resultMap : resultMaps){
			StoreOrderItemDetailVO storeOrderItemDetailVO = new StoreOrderItemDetailVO();
			storeOrderItemDetailVOs.add(storeOrderItemDetailVO);
			
			storeOrderItemDetailVO.setPreviewImg((String)resultMap.get("DetailImages"));
			storeOrderItemDetailVO.setClothesNum((String)resultMap.get("ClothesNumber"));
			storeOrderItemDetailVO.setProductId((Long)resultMap.get("ProductId"));
			storeOrderItemDetailVO.setTotalMoney(Double.parseDouble(resultMap.get("TotalMoney").toString()));
			storeOrderItemDetailVO.setBuyCount((Integer)resultMap.get("BuyCount"));
			storeOrderItemDetailVO.setTotalPay(Double.parseDouble(resultMap.get("TotalPay").toString()));
			storeOrderItemDetailVO.setSkuNo((Long)resultMap.get("SkuNo"));
			storeOrderItemDetailVO.setProductName((String)resultMap.get("ProductName"));
			storeOrderItemDetailVO.setJiuCoin((Integer)resultMap.get("UnavalCoinUsed"));
			storeOrderItemDetailVO.setBrandName((String)resultMap.get("BrandName"));
			
			String skuSnapshot = (String)resultMap.get("SkuSnapshot");
			String[] attrs = skuSnapshot.split(" ");
			storeOrderItemDetailVO.setColor(attrs[0].split(":")[1]);
			storeOrderItemDetailVO.setSize(attrs[2].split(":")[1]);
		}
		
		String expressInfo = storeOrder.getExpressInfo();
		storeOrderDetailVO.setUserName(expressInfo.split(",")[0]);
		storeOrderDetailVO.setPhone(expressInfo.split(",")[1].trim());
		storeOrderDetailVO.setAddress(expressInfo.split(",")[2]);
		storeOrderDetailVO.setCredit(getClientCredit(storeId) + "%");
		
		//3.5版本 添加改价信息
		StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(orderNo);
		
		double supplierPreferential = storeOrderNew.getSupplierPreferential();//商家店铺优惠
		double supplierChangePrice = storeOrderNew.getSupplierChangePrice();//商家店铺改价
		double supplierAddPrice = 0;
		if(supplierChangePrice >= 0 ){
			supplierPreferential += supplierChangePrice;
		}else{
			supplierAddPrice = 0-supplierChangePrice;
		}
		
		storeOrderDetailVO.setTotalMoney(storeOrderNew.getTotalMoney());//订单原价
		storeOrderDetailVO.setTotalPayWithoutExpress(storeOrderNew.getTotalPay());//订单待付款金额
		storeOrderDetailVO.setSupplierPreferential(supplierPreferential);//订单商家优惠
		storeOrderDetailVO.setSupplierAddPrice(supplierAddPrice);//订单加价金额
		storeOrderDetailVO.setPlatformPreferential(storeOrderNew.getPlatformTotalPreferential());//订单平台优惠
		
		int orderStatus = storeOrder.getOrderStatus();	//定单状态
		storeOrderDetailVO.setOrderStatus(orderStatus);
		
		return storeOrderDetailVO;
	}
	
	private Map<String, Object> assembleBrandOrderInfo(long brandOrderNo) {
		Map<String, Object> result = new HashMap<>();
		BrandOrder brandOrder = brandOrderDao.getByOrderNo(brandOrderNo);
		BrandBusiness brandBusiness = brandBusinessService.getById(brandOrder.getBrandBusinessId());
		
		List<BrandOrder> brandOrders = new ArrayList<>();
		if (brandOrder.getMergedId() == -1) {
			result.put("parent_no", brandOrder.getOrderNo());
			BrandOrderSO so = new BrandOrderSO();
			so.setMergedId(brandOrderNo);
			brandOrders = brandOrderService.search(so);
		} else if (brandOrder.getMergedId() == brandOrder.getOrderNo()) {
			result.put("parent_no", brandOrder.getOrderNo());
			brandOrders.add(brandOrder);
		}
		result.put("brand_orders_deatils", assembleBrandOrders(brandOrders, brandOrder.getRelatedOrderNo(), brandBusiness));
		
		return result;
	}

	private List<Map<String, Object>> assembleBrandOrders(List<BrandOrder> brandOrders, long storeOrderNo, BrandBusiness brandBusiness) {
		List<Map<String, Object>> results = new ArrayList<>();
		for (BrandOrder brandOrder : brandOrders) {
			Map<String, Object> item = new HashMap<>();
			item.put("brand_name", brandBusiness.getUserName());
			item.put("related_order", brandOrder.getOrderNo());
			item.put("pay_time", DateUtil.convertMSEL(brandOrder.getCreateTime()));
			item.put("status", getBrandOrderStatus(brandOrder, storeOrderNo));
		}
		return results;
	}

	private String getBrandOrderStatus(BrandOrder brandOrder, long storeOrderNo) {
		StoreOrderMessageBoard sBoard = storeOrderMessageBoardDao.getLastByOrderNo(storeOrderNo);
		switch (sBoard.getType()) {
		case 2:
			return "品牌发单";
		case 3:
			return "审核通过";
		case 4:
			return "发货审核不通过";
		case 5:
			return "品牌取消订单";
		case 6:
			return "修改物流信息";
		case 7:
			return "后台撤回发单";
		}
		
		return "";
	}

	private int getClientCredit(long storeId){
		int buyCountOfCancel = storeOrderItemDao.getBuyCountOfOrderStatus(storeId,CollectionUtil.createSet(StoreOrderStatus.CANCELED.getIntValue()));
		
		Set<Integer> storeOrderStatus = new HashSet<>();
		storeOrderStatus.add(StoreOrderStatus.PAID.getIntValue());
		storeOrderStatus.add(StoreOrderStatus.DELIVER.getIntValue());
		storeOrderStatus.add(StoreOrderStatus.SUCCESS.getIntValue());
		
		int buyCountOfSuccess = storeOrderItemDao.getBuyCountOfOrderStatus(storeId, storeOrderStatus);
		if(buyCountOfCancel + buyCountOfSuccess == 0) {
			return 100;
		}
		
		return buyCountOfSuccess/(buyCountOfCancel + buyCountOfSuccess) * 100;
	}

	@Transactional(rollbackFor = Exception.class)
	public void delivery(long orderNo, long storeId, String supplier, String expressNo, AdminUser adminUser) {
		StoreOrder storeOrder = storeOrderDao.orderNewOfOrderNo(orderNo);
		
		if (storeOrder.getMergedId() != 0 && storeOrder.getMergedId() != orderNo && storeOrder.getMergedId() != -1) {
			throw new ParameterErrorException("不是可发货单位!");
		}
		
		//写入StoreOrder表(包括StoreOrderLog)
		storeOrderFacade.updateSotreOrderStatus(orderNo,StoreOrderStatus.PAID.getIntValue(),StoreOrderStatus.DELIVER.getIntValue(),true);
		
		HashSet<Long> orderNos = new HashSet<Long>();
		if(storeOrder.getMergedId() == -1){
			//组合订单
			List<StoreOrder> storeOrders = storeOrderDao.childOfCombinationOrderNos(CollectionUtil.createList(orderNo));
			for(StoreOrder storeOrder2 : storeOrders){
				orderNos.add(storeOrder2.getOrderNo());
			}
		}else{
			//普通订单
			orderNos.add(orderNo);
		}
		
		List<StoreOrderItem> storeOrderItems = storeOrderItemDao.orderItemsOfOrderNos(orderNos);
		
		for(StoreOrderItem storeOrderItem : storeOrderItems){
			StoreOrder storeOrder2 = storeOrderDao.orderNewOfOrderNo(storeOrderItem.getOrderNo());
			
			StoreExpressInfo storeExpressInfo = new StoreExpressInfo();
			storeExpressInfo.setOrderNo(storeOrder2.getOrderNo());
			storeExpressInfo.setExpressSupplier(supplier);
			storeExpressInfo.setExpressOrderNo(expressNo);
			storeExpressInfo.setUpdateTime(System.currentTimeMillis());
			storeExpressInfo.setCreateTime(System.currentTimeMillis());
			storeExpressInfo.setExpressUpdateTime(System.currentTimeMillis());
			
			storeExpressInfoService.addItem(storeExpressInfo);
		}
		
		StoreOrderMessageBoard storeOrderMessageBoard = new StoreOrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), 1, "发货", "");
		storeOrderMessageBoardService.add(storeOrderMessageBoard);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public ResultCode cancel(long orderNo, long storeId, int oldStatus, String message, AdminUser adminUser,HttpServletRequest request){
		StoreOrder storeOrder = storeOrderDao.storeOrderOfOrderNo(orderNo);
		Set<Long> orderNos = new HashSet<Long>();
		orderNos.add(orderNo);
		
		int orderStatus = storeOrder.getOrderStatus();
		
		if(orderStatus != StoreOrderStatus.PAID.getIntValue() && 
				orderStatus != StoreOrderStatus.DELIVER.getIntValue()){
			throw new ParameterErrorException("order cancel exception : no such paid orderNo :" + orderNo);
		} else if(oldStatus != orderStatus){
			throw new ParameterErrorException("order cancel exception : 'old orderStatus'" + orderStatus + "is not same with parameter of 'old_order_status'" + oldStatus);
		} else if(storeId != storeOrder.getStoreId()){
			throw new ParameterErrorException("order cancel exception : 'storeId'" + storeOrder.getStoreId() + "is not same with parameter of 'store_id'" + storeId);
		}
		
		if(storeOrder.getMergedId() == -1){
			List<StoreOrder> storeOrders = storeOrderDao.childOfCombinationOrderNos(CollectionUtil.createList(orderNo));
			for(StoreOrder storeOrder2 : storeOrders){
				orderNos.add(storeOrder2.getOrderNo());
			}
		}
		
		List<StoreOrderItem> storeOrderItems = storeOrderItemDao.orderItemsOfOrderNos(orderNos);
		
		//写入StoreOrder表(包括StoreOrderLog)
		storeOrderFacade.updateSotreOrderStatus(orderNo, oldStatus, StoreOrderStatus.CANCELED.getIntValue(), true);
		StoreOrderMessageBoard storeOrderMessageBoard = 
				new StoreOrderMessageBoard(orderNo,adminUser.getUserId(),adminUser.getUserName(),1,"取消订单",message);
		storeOrderMessageBoardService.add(storeOrderMessageBoard);
		
		if(storeOrderItems.size()>0){
			restoreSKUNew(storeOrderItems);
		}
		//restoreSKU(orderNo);
		
		//写入财务工单记录
		storeOrderFacade.addFinanceTicketFromRevoke(orderNo, message,request);
		
		return ResultCode.COMMON_SUCCESS;
	}
	
	// 主 副仓退货
	private void restoreSKUNew(List<StoreOrderItem> orderItems){
		for(StoreOrderItem item : orderItems){
			ProductSKU sku = productSKUMapper.searchById(item.getSkuId());
			if(sku.getlOWarehouseId2() > 0 && sku.getlOWarehouseId2() == item.getlOWarehouseId()){
				productSKUMapper.updateRemainCountSecond(item.getSkuId(), item.getBuyCount());
			} else {
				productSKUMapper.updateRemainCount(item.getSkuId(), item.getBuyCount());
			}
		}
	}
	
	//返回库存
	private void restoreSKU(long orderNo){
		Map<Long, Integer> productCountMap = new HashMap<Long,Integer>();
		List<StoreOrderItem> storeOrderItems = storeOrderItemDao.orderItemsOfOrderNos(CollectionUtil.createList(orderNo));
		for(StoreOrderItem item : storeOrderItems){
			Integer count = productCountMap.get(item.getSkuId());
			if(count==null){
				count = 0;
			}
			count = count + item.getBuyCount();
			productCountMap.put(item.getSkuId(), count);
		}
		Set<Entry<Long, Integer>> entries = productCountMap.entrySet();
		Iterator<Entry<Long,Integer>> iterator = entries.iterator();
		while(iterator.hasNext()){
			Entry<Long, Integer> curEntry = iterator.next();
			productService.updateProductSKU(curEntry.getKey(), curEntry.getValue());
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public ResultCode revoke(long orderNo, long storeId, int oldStatus, AdminUser adminUser){
		StoreOrder storeOrder = storeOrderDao.storeOrderOfOrderNo(orderNo);
		if(storeOrder.getParentId() == -1){
			throw new ParameterErrorException("order revoke exception : 'ParentId' " + orderNo + " is parent type");
		}
		Set<Long> orderNos = new HashSet<>();
		orderNos.add(orderNo);
		
		int orderStatus = storeOrder.getOrderStatus();
		
		if(orderStatus != StoreOrderStatus.DELIVER.getIntValue()){
			throw new ParameterErrorException("order revoke exception : no such paid orderNo :" + orderNo);
		} else if(oldStatus != orderStatus){
			throw new ParameterErrorException("order revoke exception : 'old orderStatus' " + orderStatus + " is not same with parameter of 'old_order_status'" + oldStatus);
		} else if(storeId != storeOrder.getStoreId()){
			throw new ParameterErrorException("order revoke exception : 'storeId' " + storeOrder.getStoreId() + " is not same with parameter of 'store_id'" + storeId);
		}
		
		if(storeOrder.getMergedId() == -1){
			List<StoreOrder> storeOrders = storeOrderDao.childOfCombinationOrderNos(CollectionUtil.createList(orderNo));
			for(StoreOrder storeOrder2 : storeOrders){
				orderNos.add(storeOrder2.getOrderNo());
			}
		}
		//写入StoreOrder表(包括StoreOrderLog)
		storeOrderFacade.updateSotreOrderStatus(orderNo, oldStatus, StoreOrderStatus.PAID.getIntValue(), true);
		//逻辑删除物流记录
		storeExpressInfoService.remove(orderNos);
		
		StoreOrderMessageBoard storeOrderMessageBoard = new StoreOrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), 1, "撤销已发货", "");
		storeOrderMessageBoardService.add(storeOrderMessageBoard);
		
		return ResultCode.COMMON_SUCCESS;
	}

	//搜索未发货订单
	public List<List<StoreOrderVO>> searchUndelivered(PageQuery pageQuery, String orderNo, String clothesNum,
			String expressOrderNo, String skuNo, long storeNumber, String receiver, String phone, long startTime,
			long endTime) {
		
		Set<Long> orderNos = new HashSet<Long>();
		
		if(isFilter(expressOrderNo, clothesNum, skuNo)){
			orderNos = (Set<Long>) filterOrderNos(expressOrderNo, clothesNum, skuNo);
			if(orderNos.size() < 1){
				return new ArrayList<List<StoreOrderVO>>();
			}
		}
		
		//根据商家号查找门店id
		long storeId = -1;
		if (storeNumber != -1) {
			long resultId = storeBusinessDao.getIdByBusinessNumber(storeNumber); 
			if(resultId<1){
				return new ArrayList<>();
			}
			storeId = resultId;
		}
		
		//获取原始数据
		List<StoreOrderVO> list = storeOrderFacade.searchUndelivered(pageQuery, orderNo, storeId, receiver, phone,
			            startTime, endTime, orderNos);
		
		globalOrderNos = orderNos;
		
		List<List<StoreOrderVO>> lists = new ArrayList<List<StoreOrderVO>>();
		Map<Long, List<StoreOrderVO>> map = new HashMap<Long,List<StoreOrderVO>>();
		
		//快递
		Map<Long, StoreExpressInfo> exMap = null;
		
		// 全部/组合/拆分
		Set<Long> allOrderNos = new HashSet<Long>();
		Set<Long> combinationOrderNos = new HashSet<Long>();
		Set<Long> splitOrderNos = new HashSet<Long>();
		Map<Long, StoreOrderVO> splitStoreOrderMap = new HashMap<Long, StoreOrderVO>();
		Map<Long, StoreOrderVO> combinationStoreOrderMap = new HashMap<Long,StoreOrderVO>();
		Map<Long, StoreOrderVO> normalStoreOrderMap = new HashMap<Long, StoreOrderVO>();
		
		assemble(list, splitOrderNos, splitStoreOrderMap, combinationOrderNos, combinationStoreOrderMap, normalStoreOrderMap, allOrderNos);
		
		Map<Long, List<StoreOrder>> splitStoreOrdersMap = new HashMap<Long, List<StoreOrder>>();
		Map<Long, List<StoreOrder>> combinationStoreOrdersMap = storeOrderFacade.combinationMapOfOrderNos(combinationOrderNos);
		
		//获取所有相关的订单购买数量
		assembleAllOrderNos(allOrderNos, splitStoreOrdersMap,combinationStoreOrdersMap);
		
		Map<Long, Integer> buyCountMapOfOrderNo = storeOrderItemService.buyCountMapOfOrderNo(allOrderNos);
		exMap = storeExpressInfoService.expressInfoMapOfOrderNos(allOrderNos);
		Map<String, ExpressSupplier> suMap = expressSupplierService.itemByEngName();
		
		//封装普通订单
		assembleNormal(normalStoreOrderMap, buyCountMapOfOrderNo, exMap, map, suMap);
		//封装组合订单
		assembleCombination(combinationStoreOrdersMap, combinationStoreOrderMap, buyCountMapOfOrderNo, exMap, map, suMap);
		
		for(StoreOrderVO storeOrderVO : list){
			long storeOrderNo = storeOrderVO.getOrderNo();
			if(storeOrderVO.getParentId() == -1){
				lists.add(map.get(storeOrderNo));
			} else if(storeOrderVO.getMergedId() == -1){
				lists.add(map.get(storeOrderNo));
			} else{
				lists.add(map.get(storeOrderNo));
			}
		}
		
		return lists;
	}
	
	public int searchUndeliveredCount(String orderNo, String clothesNum, String expressOrderNo, String skuNo,
			long storeNumber, String receiver, String phone, long startTime, long endTime) {
		Set<Long> orderNos = globalOrderNos;
		
		if(isFilter(expressOrderNo, clothesNum, skuNo)){
			orderNos = (Set<Long>) filterOrderNos(expressOrderNo, clothesNum, skuNo);
			if(orderNos.size() < 1){
				return 0;
			}
		}
		
		//根据商家号查找门店id
		long storeId = -1;
		if (storeNumber != -1) {
			long resultId = storeBusinessDao.getIdByBusinessNumber(storeNumber); 
			if(resultId<1){
				return 0;
			}
			storeId = resultId;
		}
		
		return storeOrderFacade.searchUndeliveredCount(expressOrderNo, storeId, receiver, phone, startTime, endTime, orderNos);
	}

	@Transactional(rollbackFor = Exception.class)
	public void recovery(long orderNo, long mergedId, long parentId, long storeId, int oldStatus) {
		if(oldStatus != StoreOrderStatus.CANCELED.getIntValue()){
			logger.error("revovery orderstatus: " + oldStatus + ", orderNo: " + orderNo);
            throw new ParameterErrorException("revovery orderstatus: " + oldStatus + ", orderNo: " + orderNo);
        } else if (parentId == 0) {
        	logger.error("revovery type wrong. parentId: 0" + ", orderNo: " + orderNo);
            throw new ParameterErrorException("revovery parentId: 0" + ", orderNo: " + orderNo);
        }
		
		Set<Long> orderNos = new HashSet<Long>();
		orderNos.add(orderNo);
		if(mergedId == -1){
			List<StoreOrder> storeOrders = storeOrderService.childOfCombinationOrderNos(CollectionUtil.createList(orderNo));
			for(StoreOrder storeOrder : storeOrders){
				orderNos.add(storeOrder.getOrderNo());
			}
		}
		
		storeOrderDao.updateOrderStatus(orderNos, StoreOrderStatus.PAID.getIntValue());
		storeOrderLogDao.updateLog(storeId, orderNos, StoreOrderStatus.CANCELED.getIntValue(), StoreOrderStatus.PAID.getIntValue(), System.currentTimeMillis());
		
	}

	public List<List<Map<String, Object>>> searchWholesaleUndelivered(PageQuery pageQuery, StoreOrderSO so) {
		Set<Long> orderNos = new HashSet<>();
		boolean has_filter = GatherOrderNos(so, orderNos);
		if (has_filter && orderNos.size() == 0) {
			return new ArrayList<>();
		} else if (!has_filter && orderNos.size() == 0) {
			orderNos = null;
		}
		//组合订单集合
		Set<Long> combine_order_nos = new HashSet<>();
		//组合订单的子订单&普通订单的集合&未合并的订单
		Set<Long> order_nos = new HashSet<>();
		
		Map<Long, StoreOrder> storeOrderMap = new HashMap<>();
		if (so.getWarehouseId() == null) {
			storeOrderMap = storeOrderService.searchUndeliveredMap(so, pageQuery, orderNos, null);
		} else if (so.getWarehouseId().equals(2L)){
			storeOrderMap = storeOrderService.searchUndeliveredMap(so, pageQuery, orderNos, AdminConstants.OTHER_WAREHOUSE_ID_LIST);
		} else if(so.getWarehouseId().equals(1L)) {
			storeOrderMap = storeOrderService.searchUndeliveredMap(so, pageQuery, orderNos, AdminConstants.OWN_WAREHOUSE_ID_LIST);
		}
		
		if (storeOrderMap.values().size() < 1) {
			return new ArrayList<>();
		}
		
		Iterator<StoreOrder> iterator = storeOrderMap.values().iterator();
		ArrayList<StoreOrder> sortOrderList = new ArrayList<>();
		while(iterator.hasNext()) {
			StoreOrder item = iterator.next();
			if (item.getMergedId() == -1) {
				combine_order_nos.add(item.getOrderNo());
			}
			order_nos.add(item.getOrderNo());
			sortOrderList.add(item);
		}
		
		Map<Long, List<StoreOrder>> combineOrderMap = storeOrderService.getMergedChildren(combine_order_nos);
		
		for (List<StoreOrder> item_list : combineOrderMap.values()) {
			for (StoreOrder item : item_list) {
				order_nos.add(item.getOrderNo());
			}
		}
		
		Map<Long, Integer> productCountMap = storeOrderItemService.buyCountMapOfOrderNo(order_nos);
		
		List<StoreOrder> sList = storeOrderService.ordersOfOrderNos(order_nos);
		List<Long> brand_order_nos = new ArrayList<>();
		for (StoreOrder storeOrder : sList) {
			if (storeOrder.getBrandOrder() > 0) {
				brand_order_nos.add(storeOrder.getBrandOrder());
			}
		}
		//Map<Long, BrandExpressInfo> exMap = brandExpressInfoDao.expressInfoMapOfOrderNos(order_nos);
		Map<Long,BrandExpressInfo> exMap = new HashMap<>();
		List<List<Map<String, Object>>> results = new ArrayList<>();
		
		Collections.sort(sortOrderList, new MyOrderCompartor());
		
		for (StoreOrder item : sortOrderList) {
			List<Map<String, Object>> item_list = new ArrayList<>();
			results.add(item_list);
			
			if (item.getMergedId() == item.getOrderNo() || item.getMergedId() == 0) {
				Map<String, Object> result = new HashMap<>();
				assembleUndelivered(result, item, productCountMap, exMap);
				item_list.add(result);
			} else if (item.getMergedId() == -1) {
				StoreOrder combine_order = storeOrderMap.get(item.getOrderNo());
				List<StoreOrder> sub_orders = combineOrderMap.get(item.getOrderNo());
				if (sub_orders == null || sub_orders.size() == 0) {
					logger.error("error: mergedId:" + item.getOrderNo() + " has no children orders.");
					continue;
				}
				assembleCombineUndelivered(item_list, combine_order, sub_orders, productCountMap, exMap);
			}
		}
		
		return results;
	}
	
	class MyOrderCompartor implements Comparator<StoreOrder>
	{
	     @Override
	     public int compare(StoreOrder o1, StoreOrder o2)
	    {
	    	
	    	 return o1.getCreateTime() - o2.getCreateTime() >= 0 ? -1 : 1 ;
	    		
	    }
	}

	private void assembleCombineUndelivered(List<Map<String, Object>> item_list, StoreOrder combine_order,
			List<StoreOrder> sub_orders, Map<Long, Integer> productCountMap, Map<Long, BrandExpressInfo> exMap) {

		//组合订单的信息
		Map<String, Object> merged_order_map = new HashMap<>();
		assembleUndelivered(merged_order_map, combine_order, productCountMap, exMap);
		
		//将合并订单放到首位
		item_list.add(merged_order_map);
		
		int total_count = 0;
		for (StoreOrder item : sub_orders) {
			Map<String, Object> item_map = new HashMap<>();
			total_count += assembleUndelivered(item_map, item, productCountMap, exMap);
			item_list.add(item_map);
		}
		merged_order_map.put("buy_count", total_count);
		merged_order_map.put("pay_type", "多订单支付");
	}

	private int assembleUndelivered(Map<String, Object> result, StoreOrder storeOrder, Map<Long, Integer> productCountMap, Map<Long, BrandExpressInfo> exMap) {
		
		result.put("store_id", storeOrder.getStoreId());
		result.put("deal_time", DateUtil.convertMSEL(storeOrder.getCreateTime()));
		result.put("order_no", storeOrder.getOrderNo());
		result.put("pay_type", PaymentTypeDetail.getByIntValue(storeOrder.getPaymentType()));
		result.put("money", storeOrder.getTotalMoney() + storeOrder.getTotalExpressMoney());
		result.put("express_info", storeOrder.getExpressInfo());
		result.put("order_type", storeOrder.getMergedId() == -1L ? "组合订单" : "普通"); 
		result.put("buy_count", productCountMap.get(storeOrder.getOrderNo()) == null ? 0 : productCountMap.get(storeOrder.getOrderNo()));
		result.put("order_status", OrderStatus.getByIntValue(storeOrder.getOrderStatus()));
		BrandExpressInfo eInfo = exMap.get(storeOrder.getBrandOrder());
		result.put("express_no", eInfo == null ? "" : eInfo.getExpressOrderNo());
		result.put("express_supplier", "");

		if (eInfo != null) {
			Map<String, ExpressSupplier> sMap = expressSupplierService.itemByEngName();
			ExpressSupplier expressSupplier = sMap.get(eInfo.getExpressSupplier());
			if (expressSupplier != null) {
				result.put("express_supplier", expressSupplier.getCnName().split("/")[0]);
			}
		}
		
		result.put("own_warehouse", AdminConstants.OWN_WAREHOUSE_ID_LIST.contains(storeOrder.getLoWarehouseId()));
		result.put("brand_order_no", storeOrder.getBrandOrder());
		LOWarehouse warehouse = loWarehouseService.loadById(storeOrder.getLoWarehouseId());
		result.put("warehouse_name", warehouse == null ? "" : warehouse.getName());
		//是否显示发货审核
		result.put("send_check", false);
		//是否审核不通过
		result.put("deny_check", false);
		if (storeOrder.getBrandOrder() > 0) {
			BrandOrder brandOrder = brandOrderService.getByOrderNo(storeOrder.getBrandOrder());
			if (brandOrder != null && brandOrder.getOrderStatus() == 50) {
				result.put("send_check", true);
			}
			if (brandOrder != null && brandOrder.getOrderStatus() == 10) {
				StoreOrderMessageBoard storeOrderMessageBoard  = storeOrderMessageBoardDao.getCheckInfo(brandOrder.getOrderNo());
				if (storeOrderMessageBoard != null && storeOrderMessageBoard.getType() == 4) {
					result.put("deny_check", true);
				}
			}
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//获取最新一条留言
		if(storeOrder.getOrderNo()>0){
			StoreOrderMessageBoard storeOrderMessageBoard = storeOrderMessageBoardDao.getLastByOrderNo(storeOrder.getOrderNo());
			if(storeOrderMessageBoard!=null){
				String message = storeOrderMessageBoard.getMessage();
				if(!StringUtils.isEmpty(message)){
					result.put("message", message);
				}
				String adminName = storeOrderMessageBoard.getAdminName();
				if(!StringUtils.isEmpty(adminName)){
					result.put("adminName", adminName);
				}
				result.put("messageCreateTime", simpleDateFormat.format(new Date(storeOrderMessageBoard.getCreateTime())));
			}
		}
		
		return productCountMap.get(storeOrder.getOrderNo()) == null ? 0 : productCountMap.get(storeOrder.getOrderNo());
	}

	private boolean GatherOrderNos(StoreOrderSO so, Set<Long> orderNos) {
		boolean has_filter = false;
		if (so.getExpressNo() != null) {
			List<StoreExpressInfo> storeExpressInfos = storeExpressInfoDao.expressInfoOfBlurOrderNo(so.getExpressNo());
            for (StoreExpressInfo storeExpressInfo : storeExpressInfos) {
            	orderNos.add(storeExpressInfo.getOrderNo());
            }
            
            has_filter = true;
		}
		if (so.getClothesNum() != null) {
			List<Product> products = productService.getByClothesNums(Arrays.asList(new String[]{so.getClothesNum()}));
			Set<Long> productIds = new HashSet<>();
			for (Product product : products) {
				productIds.add(product.getId());
			}
			List<StoreOrderItem> storeOrderItems = storeOrderItemDao.orderItemsOfProductIds(productIds);
            for (StoreOrderItem item : storeOrderItems) {
            	orderNos.add(item.getOrderNo());
			}
            
            has_filter = true;
		}
		if (so.getBrandOrderStatus() != null) {
			switch (so.getBrandOrderStatus()) {
			case 1:
				BrandOrderSO brandOrderSO = new BrandOrderSO();
				brandOrderSO.setOrderStatus(OrderStatus.PAID);
				List<BrandOrder> brandOrders = brandOrderService.search(brandOrderSO);
				
				for (BrandOrder brandOrder : brandOrders) {
					orderNos.add(brandOrder.getRelatedOrderNo());
				}
				
				break;
			case 2:
				brandOrderSO = new BrandOrderSO();
				brandOrderSO.setOrderStatus(OrderStatus.DELIVER);
				brandOrders = brandOrderService.search(brandOrderSO);
				
				for (BrandOrder brandOrder : brandOrders) {
					orderNos.add(brandOrder.getRelatedOrderNo());
				}
				
				break;
			case 3:
				List<StoreOrderMessageBoard> boards = new ArrayList<>();
				boards = storeOrderMessageBoardDao.getLastType(Arrays.asList(new Integer[]{7}));
				
				for (StoreOrderMessageBoard item : boards) {
					orderNos.add(item.getOrderNo());
				}
				
				break;
			case 4:
				boards = new ArrayList<>();
				boards = storeOrderMessageBoardDao.getLastType(Arrays.asList(new Integer[]{5}));
				
				for (StoreOrderMessageBoard item : boards) {
					orderNos.add(item.getOrderNo());
				}
				
				break;
			}
			
			has_filter = true;
		}
		if (so.getBusinessNumber() != null) {
			List<StoreBusiness> sBusinesses = storeBusinessDao.getBlurByBusinessNumber(so.getBusinessNumber());
			
			if (sBusinesses.size() == 0) {
				orderNos = new HashSet<>();
				return true;
			}
		
			Set<Long> storeIds = new HashSet<>();
			for (StoreBusiness item : sBusinesses) {
				storeIds.add(item.getId());
			}
		
			List<StoreOrder> storeOrders = storeOrderService.getByStoreIds(storeIds);
			
			for (StoreOrder item : storeOrders) {
				orderNos.add(item.getOrderNo());
			}
			
			has_filter = true;
		}
		if (so.getSkuNo() != null) {
			List<ProductSKU> productSKUs = productSKUMapper.skuOfNo(Arrays.asList(new Long[]{so.getSkuNo()}));
			Set<Long> skuIds = new HashSet<>();
            for (ProductSKU productSKU : productSKUs) {
                skuIds.add(productSKU.getId());
            }
            List<StoreOrderItem> storeOrderItems = storeOrderItemDao.getOrderItemsBySkuIds(skuIds);
            for (StoreOrderItem item : storeOrderItems) {
            	orderNos.add(item.getOrderNo());
			}
            
            has_filter = true;
		}
//		if (so.getWarehouseId() != null) {
//			List<Product> products = new ArrayList<>();
//			if (so.getWarehouseId() == 1) {
//				products = productService.getByWarehouseIds(Constants.OWN_WAREHOUSE_ID_LIST);
//			} else if (so.getWarehouseId() == 2) {
//				products = productService.getByNotInWarehouseIds(Constants.PUSH_TO_ERP_WAREHOUSE_ID_LIST);
//			}
//			Set<Long> productIds = new HashSet<>();
//			for (Product product : products) {
//				productIds.add(product.getId());
//			}
//			
//			if (productIds.size() > 0) {
//				List<OrderItem> orderItems = orderItemService.orderItemsOfProductIds(productIds);
//				for (OrderItem orderItem : orderItems) {
//					orderNos.add(orderItem.getOrderNo());
//				}
//			}
//			
//			has_filter = true;
//		}
		
		return has_filter;
	}

	public int searchWholesaleUndeliveredCount(StoreOrderSO so) {
		Set<Long> orderNos = new HashSet<>();
		boolean has_filter = GatherOrderNos(so, orderNos);
		if (has_filter && orderNos.size() == 0) {
			return 0;
		} else if (!has_filter && orderNos.size() == 0) {
			orderNos = null;
		}
		return storeOrderService.searchUndeliveredCount2(so, orderNos);
	}

	@Transactional(rollbackFor = Exception.class)
	public void dispatchOrder(long orderNo, AdminUser adminUser, String remark) {
		long current = System.currentTimeMillis();
		
		StoreOrder storeOrder = storeOrderDao.orderNewOfOrderNo(orderNo);
		
		BrandBusinessSO so = new BrandBusinessSO();
		so.setLoWarehouseId(storeOrder.getLoWarehouseId());
		BrandBusiness brandBusiness = brandBusinessService.get(so); 
		
		
		//插入brand_order表
		BrandOrder return_brand_order = brandOrderDao.add(assembleBrandOrder(storeOrder, brandBusiness, current));		
		
		//更新本表对应订单，子订单不更新
		storeOrderDao.update(return_brand_order.getOrderNo(), current, orderNo);

		List<BrandOrderItem> brandOrderItems = new ArrayList<>();
		if (storeOrder.getMergedId() == -1) { 
			List<StoreOrder> storeOrders = storeOrderDao.orderNewsOfParentMergedOrderNos(Arrays.asList(storeOrder.getOrderNo()));
			for (StoreOrder item : storeOrders) {
				return_brand_order = brandOrderDao.add(assembleBrandOrder(item, brandBusiness, current));	
				
				assembleBrandOrderItems(storeOrderItemDao.getByOrderNo(item.getOrderNo()), brandBusiness, brandOrderItems, current, return_brand_order.getOrderNo());
			}
		} else {
			assembleBrandOrderItems(storeOrderItemDao.getByOrderNo(storeOrder.getOrderNo()), brandBusiness, brandOrderItems, current, return_brand_order.getOrderNo());
		}
		
		//插入brand_orderItem表
		brandOrderItemDao.add(brandOrderItems);
		
		//记录表
		StoreOrderMessageBoard storeOrderMessageBoard = new StoreOrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), 2, null, remark);
		storeOrderMessageBoardDao.add(storeOrderMessageBoard);
		
	}

	private void assembleBrandOrderItems(List<StoreOrderItem> list, BrandBusiness brandBusiness, List<BrandOrderItem> brandOrderItems, long current, long brandOrderNo) {
		for (StoreOrderItem item : list) {
			BrandOrderItem brandOrderItem = new BrandOrderItem();
			BeanUtils.copyProperties(item, brandOrderItem);
			brandOrderItem.setBrandBusinessId(brandBusiness.getId());
			brandOrderItem.setLoWarehouseId(brandBusiness.getlOWarehouseId());
			brandOrderItem.setCreateTime(current);
			brandOrderItem.setUpdateTime(current);
			brandOrderItem.setOrderNo(brandOrderNo);
			
			brandOrderItems.add(brandOrderItem);
		}
	}

	private BrandOrder assembleBrandOrder(StoreOrder storeOrder, BrandBusiness brandBusiness, long current) {
		BrandOrder brandOrder = new BrandOrder();
		BeanUtils.copyProperties(storeOrder, brandOrder);
		brandOrder.setOrderNo(0); 
		brandOrder.setlOWarehouseId(brandBusiness.getlOWarehouseId());
		brandOrder.setBrandBusinessId(brandBusiness.getId());
		brandOrder.setCreateTime(current);
		brandOrder.setUpdateTime(current);
		brandOrder.setRelatedOrderNo(storeOrder.getOrderNo());
		brandOrder.setRelatedOrderType(1);
		brandOrder.setOrderStatus(storeOrder.getOrderStatus());
		brandOrder.setOrderType(storeOrder.getOrderType());
		brandOrder.setPaymentType(storeOrder.getPaymentType());
		
		return brandOrder;
	}

	@Transactional(rollbackFor = Exception.class)
	public void revokeDispatchOrder(long orderNo, AdminUser adminUser, String remark) {
		long current = System.currentTimeMillis();
		
		List<Long> orderNos = new ArrayList<>();
		StoreOrder sOrder = storeOrderService.orderOfOrderNo(orderNo);
		orderNos.add(sOrder.getBrandOrder());
		
		BrandOrderSO so = new BrandOrderSO();
		so.setOrderNo(sOrder.getBrandOrder());
		List<BrandOrder> brandOrders = brandOrderService.search(so);
		if (brandOrders.get(0).getMergedId() == -1) { 
			so = new BrandOrderSO();
			so.setMergedId(sOrder.getBrandOrder());
			List<BrandOrder> sub_orders = brandOrderService.search(so);
			for (BrandOrder sub_item : sub_orders) { 
				orderNos.add(sub_item.getOrderNo());
			}
		}
		BrandOrderUO uo = new BrandOrderUO();
		uo.setUpdateTime(current);
		uo.setStatus(-1);
		uo.setOrderNos(orderNos);
		
		//brandOrder相关订单号删除
		brandOrderDao.update(uo);
		
		//清除订单表关联
		storeOrderDao.update(0, current, orderNo);
		
		//记录表
		StoreOrderMessageBoard storeOrderMessageBoard = new StoreOrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), 7, null, remark);
		storeOrderMessageBoardDao.add(storeOrderMessageBoard);
	}

	public void checkPass(long orderNo, AdminUser adminUser, String remark) {
		StoreOrder sOrder = storeOrderService.orderOfOrderNo(orderNo);
		
		BrandExpressInfo express = brandExpressInfoDao.getByOrderNo(sOrder.getBrandOrder());
		
		delivery(orderNo, sOrder.getStoreId(), express.getExpressSupplier(), express.getExpressOrderNo(), adminUser);
		
		//记录表
		StoreOrderMessageBoard storeOrderMessageBoard = new StoreOrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), 3, null, remark);
		storeOrderMessageBoardDao.add(storeOrderMessageBoard);
	}

	@Transactional(rollbackFor = Exception.class)
	public void checkDenyPass(long orderNo, AdminUser adminUser, String remark) {
		StoreOrder sOrder = storeOrderService.orderOfOrderNo(orderNo);
		BrandOrder bOrder = brandOrderService.getByOrderNo(sOrder.getBrandOrder());
		
		brandExpressInfoDao.removeByOrderNo(sOrder.getBrandOrder());
//		long current = System.currentTimeMillis();
//		brandOrderLogDao.add(new BrandOrderLog(bOrder.getBrandBusinessId(), sOrder.getOrderNo(), 50, 10, current, current));
		
		List<Long> orderNos = new ArrayList<>();
		orderNos.add(bOrder.getOrderNo());
		if (bOrder.getMergedId() == -1) {
			BrandOrderSO so = new BrandOrderSO();
			so.setMergedId(bOrder.getOrderNo());
			List<BrandOrder> bList = brandOrderService.search(so);
			for (BrandOrder brandOrder : bList) {
				orderNos.add(brandOrder.getOrderNo());
			}
		}
		//修改订单状态
		BrandOrderUO uo = new BrandOrderUO();
		uo.setOrderNos(orderNos);
		uo.setOrderStatus(10);
		brandOrderDao.update(uo);
		
		//记录表
		StoreOrderMessageBoard storeOrderMessageBoard = new StoreOrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), 4, null, remark);
		storeOrderMessageBoardDao.add(storeOrderMessageBoard);
	}
	

	public List<Map<String, Object>> deliveryExcel(int type, long startTime, long endTime) {
		List<StoreOrder> selfMergedOrderNews = storeOrderService.selfMergedStoreOrder(startTime, endTime);
//		List<List<StoreOrderVO>> selfMergedOrderNews = searchStoreOrders(null, "", "", "", 
//        		"", 0, -1L, "", "", 0L, 0L, -1);
        Map<Long, List<StoreOrder>> parentMergedOrderNewMap = storeOrderFacade.parentMergedMap(startTime, endTime);
        
        Set<Long> orderNos = getRelatedOrderNos(selfMergedOrderNews, parentMergedOrderNewMap);
        
        Map<Long, List<StoreOrderItem>> orderItemByNo = storeOrderItemService.OrderItemMapOfOrderNos(orderNos); //!!!STORE OrderItem
        
        Set<Long> productIds = new HashSet<Long>();
        for(List<StoreOrderItem> orderItems : orderItemByNo.values()) {
        	for(StoreOrderItem orderItem : orderItems) {
        		productIds.add(orderItem.getProductId());
        	}
        }
        Map<Long, Product> productMap = productService.productMapOfIds(productIds);
        
        Map<Long, StoreOrder> orderNewsMap = storeOrderService.orderNewMapOfOrderNos(orderNos);
        Set<Long> userIds = new HashSet<Long>();
        for (StoreOrder orderNew : orderNewsMap.values()) {
            userIds.add(orderNew.getStoreId());
        }
        Map<Long, List<Address>> addressMap = addressService.addressMapOfUserIdsStore(userIds);
        
        Map<Long, LOWarehouse> warehouseMap = loWarehouseService.getWarehouseMap(null);
        Map<Long, BrandLogo> brandMap = baBrandLogoServiceImpl.getBrandMap();

        return assembleExcel(selfMergedOrderNews, parentMergedOrderNewMap, orderItemByNo, productMap, orderNewsMap,
            addressMap, warehouseMap, brandMap, type);
	}
	
    private Set<Long> getRelatedOrderNos(List<StoreOrder> selfMergedOrderNews, Map<Long, List<StoreOrder>> parentMergedOrderNewMap) {
		Set<Long> orderNos = new HashSet<>();
        for(StoreOrder orderNew : selfMergedOrderNews) {
        	orderNos.add(orderNew.getOrderNo());
        }
        for(List<StoreOrder> orderNews : parentMergedOrderNewMap.values()) {
            for (StoreOrder orderNew : orderNews) {
            	if (orderNew.getOrderStatus() == OrderStatus.PAID.getIntValue()) {
            		orderNos.add(orderNew.getOrderNo());
				}
            }
        }
		return orderNos;
	}
	
	private List<Map<String, Object>> assembleExcel(List<StoreOrder> selfMergedOrderNews, Map<Long, List<StoreOrder>> parentMergedOrderNewMap, 
			Map<Long, List<StoreOrderItem>> orderItemByNo, Map<Long, Product> productMap, Map<Long, StoreOrder> orderNewsMap, 
			Map<Long, List<Address>> addressMap, Map<Long, LOWarehouse> warehouseMap, Map<Long, BrandLogo> brandMap, int type) {
		
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(StoreOrder orderNew : selfMergedOrderNews) {
            List<StoreOrderItem> orderItems = orderItemByNo.get(orderNew.getOrderNo());
            System.out.println(orderItems);
            if(orderItems==null){
            	continue;
            }
            for (StoreOrderItem orderItem : orderItems) {
                assembleExcelItem(orderItem, list, productMap, addressMap, orderNewsMap, warehouseMap, brandMap, type);
            }
		}

        for (List<StoreOrder> orderNews : parentMergedOrderNewMap.values()) {
            for (StoreOrder orderNew : orderNews) {
                List<StoreOrderItem> orderItems = orderItemByNo.get(orderNew.getOrderNo());
                if (orderItems == null) {
					continue;
				}
                for (StoreOrderItem orderItem : orderItems) {
                    assembleExcelItem(orderItem, list, productMap, addressMap, orderNewsMap, warehouseMap, brandMap, type);
                }
            }
        }

        return list;
	}

	private void assembleExcelItem(StoreOrderItem orderItem, List<Map<String, Object>> list, Map<Long, Product> productMap,
			Map<Long, List<Address>> addressMap, Map<Long, StoreOrder> orderNewsMap, Map<Long, LOWarehouse> warehouseMap,
			Map<Long, BrandLogo> brandMap, int type) {
		long productId = orderItem.getProductId();
		long orderNo = orderItem.getOrderNo();
		long warehouseId = orderItem.getlOWarehouseId();
		
		if (AdminConstants.ERP_WAREHOUSE_ID_LIST.contains(warehouseId)) {
			return;
		}

		if (type == 0 && !AdminConstants.OWN_WAREHOUSE_ID_LIST.contains(warehouseId)) {
			return;
		}

		if (type == 1 && AdminConstants.OWN_WAREHOUSE_ID_LIST.contains(warehouseId)) {
			return;
		}

		Product product = productMap.get(productId);
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

		StoreOrder orderNew = orderNewsMap.get(orderNo);
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
}
