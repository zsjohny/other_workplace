package com.jiuy.web.delegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.jiuy.core.business.facade.OrderFacade;
import com.jiuy.core.business.facade.OrderNewFacade;
import com.jiuy.core.dao.ClickStatisticsDao;
import com.jiuy.core.dao.CouponDao;
import com.jiuy.core.dao.CouponUseLogDao;
import com.jiuy.core.dao.ExpressInfoDao;
import com.jiuy.core.dao.ExpressSupplierDao;
import com.jiuy.core.dao.OrderDiscountLogDao;
import com.jiuy.core.dao.OrderItemDao;
import com.jiuy.core.dao.OrderMessageBoardDao;
import com.jiuy.core.dao.OrderNewDao;
import com.jiuy.core.dao.OrderNewLogDao;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.dao.StoreFinanceLogDao;
import com.jiuy.core.dao.UserBankCardPayDiscountDao;
import com.jiuy.core.dao.mapper.BrandExpressInfoDao;
import com.jiuy.core.dao.mapper.BrandOrderDao;
import com.jiuy.core.dao.mapper.BrandOrderItemDao;
import com.jiuy.core.dao.modelv2.ProductSKUMapper;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.account.User;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.brandexpress.BrandExpressInfo;
import com.jiuy.core.meta.brandorder.BrandOrder;
import com.jiuy.core.meta.brandorder.BrandOrderItem;
import com.jiuy.core.meta.brandorder.BrandOrderSO;
import com.jiuy.core.meta.brandorder.BrandOrderUO;
import com.jiuy.core.meta.coupon.CouponUseLog;
import com.jiuy.core.meta.member.BrandBusinessSO;
//import com.jiuy.core.meta.order.Order;
import com.jiuy.core.meta.order.OrderDetailVO;
import com.jiuy.core.meta.order.OrderDiscountLog;
import com.jiuy.core.meta.order.OrderItem;
import com.jiuy.core.meta.order.OrderItemDetailVO;
import com.jiuy.core.meta.order.OrderMessageBoard;
import com.jiuy.core.meta.order.OrderNew;
import com.jiuy.core.meta.order.OrderNewSO;
import com.jiuy.core.meta.order.OrderNewUO;
import com.jiuy.core.meta.order.OrderNewVO;
import com.jiuy.core.service.AddressService;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.ExpressInfoService;
import com.jiuy.core.service.ExpressSupplierService;
//import com.jiuy.core.service.OrderService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.UserCoinService;
import com.jiuy.core.service.UserManageService;
import com.jiuy.core.service.UserService;
import com.jiuy.core.service.brandorder.BrandOrderService;
import com.jiuy.core.service.comment.CommentService;
import com.jiuy.core.service.coupon.CouponUseLogService;
import com.jiuy.core.service.logistics.LOWarehouseService;
import com.jiuy.core.service.member.BrandBusinessService;
import com.jiuy.core.service.notifacation.NotifacationService;
import com.jiuy.core.service.order.OrderItemService;
import com.jiuy.core.service.order.OrderMessageBoardService;
import com.jiuy.core.service.order.OrderNewLogService;
import com.jiuy.core.service.order.OrderOldService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.constant.coupon.CouponUseStatus;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentTypeDetail;
import com.jiuyuan.entity.BrandBusiness;
import com.jiuyuan.entity.ExpressInfo;
import com.jiuyuan.entity.OrderNewLog;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.StoreFinanceLog;
import com.jiuyuan.entity.UserBankCardPayDiscount;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.newentity.ExpressSupplier;
import com.jiuyuan.entity.query.PageQuery;
//import com.jiuyuan.entity.Address;
import com.jiuyuan.util.CollectionUtil;

@Service
public class OrderNewDelegator {
	
	private static final Logger logger = Logger.getLogger(OrderNewDelegator.class);
	
	private Set<Long> globalOrderNos = new HashSet<Long>();

    @Autowired
    private OrderNewFacade orderNewFacade;

//    @Autowired
//    private OrderFacade orderFacade;
    
	@Autowired
    private ExpressInfoDao expressInfoDaoSqlImpl;

    @Autowired
    private OrderOldService orderNewService;
    
    @Autowired
    private OrderNewDao orderNewDao;

    @Autowired
    private ProductSKUMapper productSKUMapper;

    @Autowired
    private OrderItemService orderItemService;
    
    @Autowired
    private OrderItemDao orderItemDao;
    
    @Autowired
    private ExpressInfoService expressInfoService;
    
    @Autowired
    private ExpressInfoDao expressInfoDao;
    
//    @Autowired
//    private OrderService orderService;
    
    @Autowired
    private OrderNewLogService orderNewLogService;
    
    @Autowired
    private OrderNewLogDao orderNewLogDao;

    @Autowired
    private UserCoinService userCoinService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private OrderDiscountLogDao orderDiscountLogDao;
   /*
    @Autowired
    private OrderDao orderDao;*/
    
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderMessageBoardService orderMessageBoardService;

    @Autowired
    private LOWarehouseService loWarehouseService;

    @Autowired
    private BrandLogoServiceImpl baBrandLogoServiceImpl;
    
    @Autowired
    private UserBankCardPayDiscountDao userBankCardPayDiscountDao;
    
    @Autowired
    private ExpressSupplierDao expressSupplierDao;
    
	@Autowired
	private ExpressSupplierService expressSupplierService;
	
	@Autowired
	private UserManageService userManageService;
	
	@Autowired
	private CouponUseLogDao couponUseLogDao;
	
	@Autowired
	private CouponUseLogService couponUseLogService;
	
	@Autowired
	private CouponDao couponDao;
	
	@Autowired
	private ClickStatisticsDao csDao;
	
	@Autowired
	private StoreBusinessDao storeBusinessDao;
	
	@Autowired
	private StoreFinanceLogDao storeFinanceLogDao;
	
    @Autowired
	private BrandOrderService brandOrderService;

    @Autowired
    private UserService userService;

	@Autowired
	private BrandOrderDao brandOrderDao;
	
	@Autowired
	private BrandOrderItemDao brandOrderItemDao;
	
	@Autowired
	private BrandBusinessService brandBusinessService;
	
	@Autowired
	private OrderMessageBoardDao orderMessageBoardDao;
	
	@Autowired
	private BrandExpressInfoDao brandExpressInfoDao;
	
	@Autowired
	private NotifacationService notificationServiceImpl;
	
	public List<List<OrderNewVO>> searchOrderNews(PageQuery pageQuery, String orderNo, String clothesNum,
			String expressOrderNo, String skuNo, int orderType, long yJJNumber, String receiver, String phone,
			long startTime, long endTime, int orderStatus,String code,int sendType) {

		Set<Long> orderNos = new HashSet<Long>();
		if(isFilter(expressOrderNo, clothesNum, skuNo, code)) {
			//快递单号、款号、sku编号筛选出的初步符合的OrderNo		新增统计识别码
			orderNos = (Set<Long>) filterOrderNos(expressOrderNo, clothesNum, skuNo,code);
			if(orderNos.size() < 1) {
				return new ArrayList<List<OrderNewVO>>();
			}
		}

		String no = orderNo;
		//这个判断在修复后可以删除，为了观察前端是否有老订单编号在使用
		if(orderNo.length() > 10) {
//			no = transNo(orderNo);
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================老订单表已经删除请不要使用老订单表中的订单编号，赶快修复功能，orderNo："+orderNo );
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
		}
		
		long userId = -1;
		if (yJJNumber != -1) {
			List<User> users = userManageService.search(CollectionUtil.createList(yJJNumber));
			if (users.size() < 1) {
				return new ArrayList<>();
			}
			User user = users.get(0);
			userId = user.getUserId();
		}

        //获取原始数据
        List<OrderNewVO> list = orderNewFacade.searchOrderNews(pageQuery, no, orderType, userId, receiver, phone,
            startTime, endTime, orderStatus, orderNos,sendType);
        
        //全局变量赋值，为了给searchOrderNewsCount()调用
        globalOrderNos = orderNos;
		
		List<List<OrderNewVO>> lists = new ArrayList<List<OrderNewVO>>();
		Map<Long, List<OrderNewVO>> map = new HashMap<Long, List<OrderNewVO>>();
		
		//快递Map<OrderNo, ExpressInfo>
		Map<Long, ExpressInfo> exMap = null;
        
		// 全部/组合/拆分
		Set<Long> allOrderNos = new HashSet<Long>();
		Set<Long> combinationOrderNos = new HashSet<Long>();
		Set<Long> splitOrderNos = new HashSet<Long>();
		Map<Long, OrderNewVO> splitOrderNewMap = new HashMap<Long, OrderNewVO>();
		Map<Long, OrderNewVO> combinationOrderNewMap = new HashMap<Long, OrderNewVO>();
		Map<Long, OrderNewVO> normalOrderNewMap = new HashMap<Long, OrderNewVO>();

		assemble(list, splitOrderNos, splitOrderNewMap, combinationOrderNos, combinationOrderNewMap, normalOrderNewMap, allOrderNos);

		Map<Long, List<OrderNew>> splitOrderNewsMap = orderNewFacade.splitMapOfOrderNos(splitOrderNos);
		Map<Long, List<OrderNew>> combinationOrderNewsMap = orderNewFacade.combinationMapOfOrderNos(combinationOrderNos);
		
		//获取所有相关的订单购买数量
		assembleAllOrderNos(allOrderNos, splitOrderNewsMap, combinationOrderNewsMap);
		
		Map<Long, Integer> buyCountMapOfOrderNo = orderItemService.buyCountMapOfOrderNo(allOrderNos);
		exMap = expressInfoService.expressInfoMapOfOrderNos(allOrderNos);
		Map<String, ExpressSupplier> suMap = expressSupplierService.itemByEngName();
		
		if(orderType == 0 || orderType == 2) {
			//封装普通订单
			assembleNormal(normalOrderNewMap, buyCountMapOfOrderNo, exMap, map, suMap);
        }
		if(orderType == 0 || orderType == 3) {
			//封装拆分订单
			assembleSplit(splitOrderNewsMap, splitOrderNewMap, buyCountMapOfOrderNo, exMap, map, suMap);
		}
		if(orderType == 0 || orderType == 1) {
			//封装组合订单
			assembleCombination(combinationOrderNewsMap, combinationOrderNewMap, buyCountMapOfOrderNo, exMap, map, suMap);
		}
		
		//这是唯一排序过的,以此为顺序依据进行封装
		//这里的封装会忽略"组合订单的子订单"和"拆分订单的子订单"
		if(orderStatus == OrderStatus.PAID.getIntValue() 
		                ||orderStatus == OrderStatus.DELIVER.getIntValue()) {
		    for(OrderNewVO orderNewVO : list) {
                long orderNewNo = orderNewVO.getOrderNo();
                
                if (orderNewVO.getParentId() != -1) {
                    lists.add(map.get(orderNewNo));
                }
            }

		} else {
		    for(OrderNewVO orderNewVO : list) {
		        long orderNewNo = orderNewVO.getOrderNo();
	            lists.add(map.get(orderNewNo));
		    }
		}
		
		return lists;
	}
	/**
	 * 将老订单编号（生成字符串2015091320112110237409242）转换为新订单编号（17917订单表主键）
	 * 老订单表删除时代码可可删除
	 * @param oldOrderNo
	 * @return
	 
	private String transNo(String oldOrderNo) {
		if(!StringUtils.equals("", oldOrderNo)) {
			Order order = orderDao.orderOfNo(oldOrderNo);
			if(order != null) {
				long orderId = order.getId();
				Map<Long, Long> oldBynew = orderFacade.associateNewOrder(CollectionUtil.createList(orderId));
				return oldBynew.get(orderId) + "";
			}
		}
		return "";
	}*/

	private void assembleCombination(Map<Long, List<OrderNew>> combinationOrderNewsMap,
			Map<Long, OrderNewVO> combinationOrderNewMap, Map<Long, Integer> buyCountMapOfOrderNo,
			Map<Long, ExpressInfo> exMap, Map<Long, List<OrderNewVO>> map, Map<String, ExpressSupplier> suMap) {
		
		for(Map.Entry<Long, List<OrderNew>> entry : combinationOrderNewsMap.entrySet()) {
			long orderNewNo = entry.getKey();
			List<OrderNew> orderNews = entry.getValue();
			
			OrderNewVO orderNewVO2 = combinationOrderNewMap.get(orderNewNo);
			List<OrderNewVO> orderNewVOs = new ArrayList<OrderNewVO>();
			orderNewVOs.add(orderNewVO2);
			int totalBuyCount = 0;
			for(OrderNew orderNew : orderNews) {
				OrderNewVO orderNewVO = new OrderNewVO();
				BeanUtils.copyProperties(orderNew, orderNewVO);
				
				Integer buyCount = buyCountMapOfOrderNo.get(orderNewVO.getOrderNo()) == null ? 0 : buyCountMapOfOrderNo.get(orderNewVO.getOrderNo());
				orderNewVO.setBuyCount(buyCount);
				ExpressInfo expressInfo = exMap.get(orderNewVO.getOrderNo());
				if(expressInfo != null) {
					assembleExpressInfo(expressInfo, orderNewVO, suMap);
				}
				
				orderNewVOs.add(orderNewVO);
				totalBuyCount += buyCount;
			}
			orderNewVO2.setBuyCount(totalBuyCount);
			map.put(orderNewNo, orderNewVOs);
		}
		
	}

	private void assembleExpressInfo(ExpressInfo expressInfo, OrderNewVO orderNewVO, Map<String, ExpressSupplier> suMap) {
		ExpressSupplier expressSupplier = suMap.get(expressInfo.getExpressSupplier());
		if (expressSupplier == null && StringUtils.isNotEmpty(expressInfo.getExpressOrderNo()) ) {
			logger.error("找不到对应的物流供应商。" + expressInfo.getExpressSupplier());
		}
		if(expressInfo != null){
			orderNewVO.setExpressOrderNo(expressInfo.getExpressOrderNo());
		}
		if(expressSupplier != null){
			orderNewVO.setExpressSupplier(expressSupplier.getCnName());
		}
	}

	private void assembleSplit(Map<Long, List<OrderNew>> splitOrderNewsMap, Map<Long, OrderNewVO> splitOrderNewMap, Map<Long, Integer> buyCountMapOfOrderNo,
			Map<Long, ExpressInfo> exMap, Map<Long, List<OrderNewVO>> map, Map<String, ExpressSupplier> suMap) {
		
		for(Map.Entry<Long, List<OrderNew>> entry : splitOrderNewsMap.entrySet()) {
			long orderNewNo = entry.getKey();
			List<OrderNew> orderNews = entry.getValue();
			
			OrderNewVO orderNewVO2 = splitOrderNewMap.get(orderNewNo);
			List<OrderNewVO> orderNewVOs = new ArrayList<OrderNewVO>();
			orderNewVOs.add(orderNewVO2);
			int totalBuyCount = 0;
			for(OrderNew orderNew : orderNews) {
				OrderNewVO orderNewVO = new OrderNewVO();
				BeanUtils.copyProperties(orderNew, orderNewVO);
				
				int buyCount = buyCountMapOfOrderNo.get(orderNewVO.getOrderNo());
				orderNewVO.setBuyCount(buyCount);
				ExpressInfo expressInfo = exMap.get(orderNewVO.getOrderNo());
				if(expressInfo != null) {
					assembleExpressInfo(expressInfo, orderNewVO, suMap);
				}
				
				orderNewVOs.add(orderNewVO);
				totalBuyCount += buyCount;
			}
			orderNewVO2.setBuyCount(totalBuyCount);
			map.put(orderNewNo, orderNewVOs);
		}
		
	}

	private void assembleNormal(Map<Long, OrderNewVO> normalOrderNewMap, Map<Long, Integer> buyCountMapOfOrderNo,
			Map<Long, ExpressInfo> exMap, Map<Long, List<OrderNewVO>> map, Map<String, ExpressSupplier> suMap) {
		
		for (Map.Entry<Long, OrderNewVO> entry : normalOrderNewMap.entrySet()) {
    		long orderNewNo = entry.getKey();
    		OrderNewVO orderNewVO = entry.getValue();
    		
    		Integer buyCount = buyCountMapOfOrderNo.get(orderNewNo);
    		if(buyCount == null) {
    			orderNewVO.setBuyCount(-1);
    		} else {
    			orderNewVO.setBuyCount(buyCount);
    		}
    		ExpressInfo expressInfo = exMap.get(orderNewVO.getOrderNo());
    		if(expressInfo != null) {
    			assembleExpressInfo(expressInfo, orderNewVO, suMap);
    		}
    		map.put(orderNewNo, CollectionUtil.createList(orderNewVO));

    		
    	}
		
	}

	private void assembleAllOrderNos(Set<Long> allOrderNos, Map<Long, List<OrderNew>> splitOrderNewsMap,
			Map<Long, List<OrderNew>> combinationOrderNewsMap) {
		
		for(Map.Entry<Long, List<OrderNew>> entry : splitOrderNewsMap.entrySet()) {
			for(OrderNew orderNew : entry.getValue()) {
				allOrderNos.add(orderNew.getOrderNo());
			}
		}
		for(Map.Entry<Long, List<OrderNew>> entry : combinationOrderNewsMap.entrySet()) {
			for(OrderNew orderNew : entry.getValue()) {
				allOrderNos.add(orderNew.getOrderNo());
			}
		}
		
	}

	private void assemble(List<OrderNewVO> list, Set<Long> splitOrderNos, Map<Long, OrderNewVO> splitOrderNewMap,
			Set<Long> combinationOrderNos, Map<Long, OrderNewVO> combinationOrderNewMap,
			Map<Long, OrderNewVO> normalOrderNewMap, Set<Long> allOrderNos) {
		
		for(OrderNewVO orderNewVO : list) {
			long orderNewNo = orderNewVO.getOrderNo();
            if (orderNewVO.getParentId() == -1) {
				splitOrderNos.add(orderNewNo);
				splitOrderNewMap.put(orderNewVO.getOrderNo(), orderNewVO);
			} else if(orderNewVO.getMergedId() == -1) {
				combinationOrderNos.add(orderNewNo);
				combinationOrderNewMap.put(orderNewVO.getOrderNo(), orderNewVO);
            } else {
                normalOrderNewMap.put(orderNewVO.getOrderNo(), orderNewVO);
               // System.out.println(orderNewVO.getOrderNo());
			}
			allOrderNos.add(orderNewNo);
		}
		
	}

	private boolean isFilter(String expressOrderNo, String clothesNum, String skuNo, String code) {
		if(StringUtils.equals("", skuNo) && StringUtils.equals("", clothesNum) && StringUtils.equals("", expressOrderNo) && StringUtils.equals("", code)) {
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
	public Collection<Long> filterOrderNos(String expressOrderNo, String clothesNum, String skuNo, String code) {
        Set<Long> expressOrderNos = new HashSet<Long>();

		//物流查订单
        if (!StringUtils.equals("", expressOrderNo)) {
            List<ExpressInfo> expressInfos = expressInfoDao.expressInfoOfBlurOrderNo(expressOrderNo);
            for (ExpressInfo expressInfo : expressInfos) {
                expressOrderNos.add(expressInfo.getOrderNo());
            }
        }
        
        //识别码查OrderNo
        List<OrderItem> sIdOrderItems = new ArrayList<OrderItem>();
        if(!StringUtils.equals("", code)){
        	List<Long> statisticsIds = csDao.getIdsOfCode(code);
        	if(statisticsIds.size()>0){
        		if(expressOrderNos.size()>0){
        			sIdOrderItems= orderItemService.orderItemsOfStatisticsId(statisticsIds, expressOrderNos);
            	}else{
            		sIdOrderItems = orderItemService.orderItemsOfStatisticsId(statisticsIds, null);
            	}
        	}
        }
        for(OrderItem orderItem : sIdOrderItems){
        	expressOrderNos.add(orderItem.getOrderNo());
        }
 
        //款号和skuNo查orderNo
        Set<Long> skuIds = new HashSet<Long>();
        if (!StringUtils.equals("", clothesNum) || !StringUtils.equals("", skuNo)) {
            List<ProductSKU> productSKUs = productSKUMapper.productSkuOfBlurClothesNo8SkuNo(clothesNum, skuNo);
            for (ProductSKU productSKU : productSKUs) {
                skuIds.add(productSKU.getId());
            }
        }
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        Set<Long> orderNos = new HashSet<Long>();
        if(expressOrderNos.size() > 0) {
        	orderNos.addAll(expressOrderNos);
            orderItems = orderItemService.orderItemsOfSkuIds(skuIds, expressOrderNos);
        } else {
            orderItems = orderItemService.orderItemsOfSkuIds(skuIds, null);
        }
        for (OrderItem orderItem : orderItems) {
            orderNos.add(orderItem.getOrderNo());
        }
        
        return orderNos;
	}

	public int searchOrderNewsCount(String orderNo, String clothesNum, String expressOrderNo, String skuNo,
			int orderType, long yJJNumber, String receiver, String phone, long startTime, long endTime, int orderStatus,String code,int sendType) {
		Set<Long> orderNos = globalOrderNos;
		
		if(isFilter(expressOrderNo, clothesNum, skuNo, code)) {
			if(orderNos.size() < 1) {
				return 0;
			}
		}
		
		String no = orderNo;
		if(orderNo.length() > 10) {
//			no = transNo(orderNo);
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================老订单表已经删除请不要使用老订单表中的订单编号，赶快修复功能，orderNo："+orderNo );
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
			logger.error("====================OrderNewDelegator.searchOrderNews is error，重要日志请尽快处理程序================");
		}
		
		long userId = -1;
		if (yJJNumber != -1) {
			List<User> users = userManageService.search(CollectionUtil.createList(yJJNumber));
			if (users.size() < 1) {
				return 0;
			}
			User user = users.get(0);
			userId = user.getUserId();
		}
		
		return orderNewFacade.searchOrderNewsCount(no, orderType, userId, receiver, phone,
	            startTime, endTime, orderStatus, orderNos,sendType);
	}

	public OrderDetailVO loadOrderNewDetailInfo(long orderNo) {
		List<OrderNew> orderNews = orderNewDao.orderNewsOfOrderNos(CollectionUtil.createList(orderNo));
		
		if(orderNews.size() < 1) {
			return new OrderDetailVO();
		}
		
		OrderNew orderNew = orderNews.get(0);
		
		long userId = orderNew.getUserId();
        double totalPay = orderNew.getTotalPay();
        double totalExpressMoney = orderNew.getTotalExpressMoney();
        
        OrderDetailVO orderDetailVO = new OrderDetailVO();

        User user = userManageService.getByUserId(userId);
		orderDetailVO.setUser(user);
		
		orderDetailVO.setUserId(userId);
		orderDetailVO.setOrderNo(orderNo);
		orderDetailVO.setCreateTime(orderNew.getCreateTime());
        orderDetailVO.setTotalPay(totalPay + totalExpressMoney);
        orderDetailVO.setPostage(totalExpressMoney);
		
        List<OrderDiscountLog> orderDiscountLogs = orderDiscountLogDao.getByNo(orderNo);
		if(orderDiscountLogs .size() > 0) {
			orderDetailVO.setOrderDiscountLogs(orderDiscountLogs);
		}
		
		UserBankCardPayDiscount userBankCardPayDiscount = userBankCardPayDiscountDao.search(orderNo);
		if(userBankCardPayDiscount != null) {
			orderDetailVO.setPayDiscount(userBankCardPayDiscount.getDiscountAmt());
		}
		
		List<OrderNewLog> orderNewLogs = orderNewLogService.orderNewLogPayOfOrderNos(CollectionUtil.createList(orderNo));
		if(orderNewLogs.size() == 1) {
			orderDetailVO.setPayTime(orderNewLogs.get(0).getCreateTime());
		}
		
		List<ExpressInfo> expressInfos = expressInfoDao.expressInfoOfOrderNos(CollectionUtil.createList(orderNo));
		Map<String, ExpressSupplier> supplierMap = expressSupplierDao.itemByEngName();
		if(expressInfos.size() == 1) {
			ExpressInfo expressInfo = expressInfos.get(0);
			ExpressSupplier expressSupplier = supplierMap.get(expressInfo.getExpressSupplier());
			
			if (expressSupplier == null) {
				throw new ParameterErrorException("找不到对应的快递公司英文名EngName！");
			}
			
			String logisticsInfo = expressSupplier.getCnName();
			logisticsInfo += expressInfo.getExpressOrderNo();
			orderDetailVO.setLogisticsInfo(logisticsInfo);
		} 
		
		long splitOrderNo = orderNew.getParentId();
		long combinationNo = orderNew.getMergedId();
		orderDetailVO.setCombinationOrderNo(combinationNo); 
		orderDetailVO.setSplitOrderNo(splitOrderNo);
		
		//向下兼容，获取相关的所有订单
		Set<Long> orderNos = new HashSet<Long>();
		Set<Long> childOfSplit = new HashSet<Long>();
		Set<Long> childOfCombination = new HashSet<Long>();
		
		orderDetailVO.setNormalOrderNos(childOfSplit);
		orderDetailVO.setCombinationRelativeOrderNos(childOfCombination);
		
		orderNos.add(orderNo);
		if(splitOrderNo == -1) {
			List<OrderNew> orderNews2 = orderNewService.childOfSplitOrderNos(CollectionUtil.createSet(orderNo));
			for(OrderNew orderNew2 : orderNews2) {
				long orderNo2 = orderNew2.getOrderNo();
				orderNos.add(orderNo2);
				childOfSplit.add(orderNo2);
			}
			
        } else if (combinationNo == -1) {
			List<OrderNew> orderNews2 = orderNewService.childOfCombinationOrderNos(CollectionUtil.createList(orderNo));
			for(OrderNew orderNew2 : orderNews2) {
				long orderNo2 = orderNew2.getOrderNo();
				orderNos.add(orderNo2);
				childOfCombination.add(orderNo2);
			}
        } else if (combinationNo > 0) {
			List<OrderNew> orderNews2 = orderNewService.childOfCombinationOrderNos(CollectionUtil.createList(combinationNo));
			for(OrderNew orderNew2 : orderNews2) {
				long orderNo2 = orderNew2.getOrderNo();
				childOfCombination.add(orderNo2);
			}
			childOfCombination.remove(orderNo);
		}
		
		List<Map<String, Object>> resultMaps = orderItemService.srchSelfParamsOfOrderNos(orderNos);
		List<OrderItemDetailVO> orderItemDetailVOs = new ArrayList<OrderItemDetailVO>();
		orderDetailVO.setOrderItemDetailVOs(orderItemDetailVOs);

		for(Map<String, Object> resultMap : resultMaps) {
			OrderItemDetailVO orderItemDetailVO = new OrderItemDetailVO();
			orderItemDetailVOs.add(orderItemDetailVO);
			
			orderItemDetailVO.setPreviewImg((String)resultMap.get("DetailImages"));
			orderItemDetailVO.setClothesNum((String)resultMap.get("ClothesNumber"));
			orderItemDetailVO.setProductId((Long)resultMap.get("ProductId"));
			orderItemDetailVO.setTotalMoney(Double.parseDouble(resultMap.get("TotalMoney").toString()));
			orderItemDetailVO.setBuyCount((Integer)resultMap.get("BuyCount"));
			orderItemDetailVO.setTotalPay(Double.parseDouble(resultMap.get("TotalPay").toString()));
			orderItemDetailVO.setSkuNo((Long)resultMap.get("SkuNo"));
			orderItemDetailVO.setProductName((String)resultMap.get("ProductName"));
			orderItemDetailVO.setJiuCoin((Integer)resultMap.get("UnavalCoinUsed"));
			orderItemDetailVO.setBrandName((String)resultMap.get("BrandName"));
			
			orderItemDetailVO.setPreviewImg((String)resultMap.get("DetailImages"));
			String skuSnapshot = (String)resultMap.get("SkuSnapshot");
			String[] attrs = skuSnapshot.split(" ");
			orderItemDetailVO.setColor(attrs[0].split(":")[1]);
			orderItemDetailVO.setSize(attrs[2].split(":")[1]);
			
			orderItemDetailVO.setCode((String)resultMap.get("Code"));
		}
		
		String expressInfo = orderNew.getExpressInfo();

		orderDetailVO.setUserName(expressInfo.split(",")[0]);
		orderDetailVO.setPhone(expressInfo.split(",")[1].trim());
		orderDetailVO.setAddress(expressInfo.split(",")[2]);
		orderDetailVO.setCredit(getClientCredit(userId) + "%");
		
		return orderDetailVO;
	}

	//客户信誉度计算
	private int getClientCredit(long userId) {
		int buyCountOfCancel = orderItemDao.getBuyCountOfOrderStatus(userId, CollectionUtil.createSet(OrderStatus.REFUNDED.getIntValue()));
		
		Set<Integer> orderStatus = new HashSet<Integer>();
		orderStatus.add(OrderStatus.PAID.getIntValue());
		orderStatus.add(OrderStatus.DELIVER.getIntValue());
		orderStatus.add(OrderStatus.SUCCESS.getIntValue());
		
		int buyCountOfSuccess = orderItemDao.getBuyCountOfOrderStatus(userId, orderStatus);
		if(buyCountOfCancel + buyCountOfSuccess == 0) {
			return 100;
		}
		
		return buyCountOfSuccess/(buyCountOfCancel + buyCountOfSuccess) * 100;
	}

	@Transactional(rollbackFor = Exception.class)
    public void delivery(long orderNo, long userId, String supplier, String expressNo, AdminUser adminUser,int type) {
		OrderNew orderNew = orderNewDao.orderNewOfOrderNo(orderNo);

		if (orderNew.getMergedId() != 0 && orderNew.getMergedId() != orderNo && orderNew.getMergedId() != -1) {
			throw new ParameterErrorException("不是可发货单位!");
		}
		
		//写入OrderNew表(包括OrderNewLog、发送发货通知)
		int newOrderStatus = OrderStatus.DELIVER.getIntValue();
		if(type == 2){
			newOrderStatus = OrderStatus.SUCCESS.getIntValue();
		}
        orderNewFacade.updateOrderStatus(orderNo, OrderStatus.PAID.getIntValue(),newOrderStatus , true,expressNo);

        //维护旧表Order & 旧表OrderItemGroup & 写入ExpressInfo 
        Set<Long> orderNos = new HashSet<Long>();
        if(orderNew.getMergedId() == -1) {
        	//组合订单
        	List<OrderNew> orderNews = orderNewDao.childOfCombinationOrderNos(CollectionUtil.createList(orderNo));
        	for(OrderNew orderNew2 : orderNews) {
        		orderNos.add(orderNew2.getOrderNo());
        	}
        } else {
        	//普通订单
        	orderNos.add(orderNo);
        	
        }
        
        List<OrderItem> orderItems = orderItemDao.orderItemsOfOrderNos(orderNos);
        
        Set<Long> groupIds = new HashSet<Long>();
        for(OrderItem orderItem : orderItems) {
        	long groupId = orderItem.getGroupId();
        	//这个是老的订单编号可以不用
        	long orderId = orderItem.getOrderId();
        	long orderItemOrderNo = orderItem.getOrderNo(); 

//        	if(groupIds.contains(groupId)) {
//        		continue;
//        	}
        	//老订单service已删除
//        	orderService.bindOrder(orderId, groupId, supplier, expressNo, true, orderNo2);
        	
        	//添加发货地址
        	long orderUserId = orderNew.getUserId();
        	addExpressInfo(orderId,orderUserId,groupId,supplier,expressNo,orderItemOrderNo);
        	groupIds.add(groupId);
        }
        
        //添加订单发货订单留言板信息
        OrderMessageBoard orderMessageBoard = new OrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), 1, "发货", "");
        orderMessageBoardService.add(orderMessageBoard);
        
	}
	
	
	/**
	 * 添加邮寄记录信息
	 * @param orderId
	 * @param orderUserId
	 * @param groupId
	 * @param supplier
	 * @param expressNo
	 * @param orderNo
	 */
	public void addExpressInfo(long orderId,long orderUserId,long groupId,String supplier,String expressNo,long orderNo){
		  // 维护新表
        ExpressInfo info = new ExpressInfo();
        info.setUserId(orderUserId);
        info.setOrderId(orderId);
        info.setExpressOrderNo(expressNo);
        info.setOrderItemGroupId(groupId);
        info.setExpressSupplier(supplier);
        info.setUpdateTime(System.currentTimeMillis());
        info.setCreateTime(System.currentTimeMillis());
        info.setExpressUpdateTime(System.currentTimeMillis());
        info.setOrderNo(orderNo);
        
        //根据OrderNo 插入更新ExpressInfo表
        expressInfoDaoSqlImpl.addItem(info);

        /*ExpressInfo existinfo = expressInfoDaoSqlImpl.getExpressInfoByGroupId(groupId);
        if(existinfo !=null) {
        	int updateRow = expressInfoDaoSqlImpl.updateByGroupId(info);
        	if(updateRow < 1) {
//            	return ResultCode.COMMON_ERROR_BAD_PARAMETER.getIntValue();
        		logger.error("邮寄信息表失败，请尽快排查问题==================================");
            }
        } else {
        	@SuppressWarnings("unused")
			ExpressInfo expressInfo = expressInfoDaoSqlImpl.addItem(info);
        }*/
	}
	

	@Transactional(rollbackFor = Exception.class)
    public ResultCode cancel(long orderNo, long userId, int oldStatus, String message, AdminUser adminUser,HttpServletRequest request) {
		OrderNew orderNew = orderNewDao.orderNewOfOrderNo(orderNo);
		Set<Long> orderNos = new HashSet<Long>();
		orderNos.add(orderNo);
		
		int orderStatus = orderNew.getOrderStatus();
		
		if(orderStatus != 10 && orderStatus != 50) {
			throw new ParameterErrorException("order cancel exception : no such paid orderNo :" + orderNo);
		} else if (oldStatus != orderStatus) {
			throw new ParameterErrorException("order cancel exception : 'old orderStatus'" + orderStatus + "is not same with parameter of 'old_order_status'" + oldStatus);
		} else if (userId != orderNew.getUserId()) {
			throw new ParameterErrorException("order cancel exception : 'userId'" + orderNew.getUserId() + "is not same with parameter of 'user_id'" + userId);
		}
		
		List<OrderNew> orderNewsForCoupon = new ArrayList<>();
		if(orderNew.getMergedId() == -1) {
			List<OrderNew> orderNews = orderNewDao.childOfCombinationOrderNos(CollectionUtil.createList(orderNo));
			for(OrderNew orderNew2 : orderNews) {
				orderNos.add(orderNew2.getOrderNo());
			}
			orderNewsForCoupon.addAll(orderNews);
		} else {
			orderNewsForCoupon.add(orderNew);
		}
		
	
		List<OrderItem> orderItems = orderItemDao.orderItemsOfOrderNos(orderNos);
		Set<Long> groupIds = new HashSet<Long>();
		for(OrderItem orderItem : orderItems) {
        	long groupId = orderItem.getGroupId();
        	long orderId = orderItem.getOrderId();
        	if(groupIds.contains(groupId)) {
        		continue;
        	}
        	groupIds.add(groupId);
        	//老的订单表已经删除，所以这里不需要了
        	//取消订单 - 旧表, groupStatus没有用到
//    		orderFacade.businessCancel(orderId, groupId, -1);
    		//取消评论
			commentService.removeComment(orderId);
        }
		
		//写入OrderNew表(包括OrderNewLog)
        orderNewFacade.updateOrderStatus(orderNo, oldStatus, OrderStatus.REFUNDED.getIntValue(), true);
		
		// 退回用户的玖币
		int coinUsed = orderNew.getCoinUsed();
		userCoinService.updateUserCoin(userId, 0, coinUsed, "" + orderNo, System.currentTimeMillis(),
				UserCoinOperation.ORDER_CANCEL);
		
        OrderMessageBoard orderMessageBoard =
            new OrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), 1, "取消订单", message);
        orderMessageBoardService.add(orderMessageBoard);

        retunCoupon(orderNewsForCoupon); 
        
//       reduceStoreIncome(orderNew);//确认打款时处理
        
        if (orderItems.size() > 0) {
        	long orderId = orderItems.get(0).getOrderId();
//        	restoreSKU(orderId);
        	restoreSKUNew(orderItems);
		}
        
        //写入财务工单中
        if(orderNew.getTotalPay() > 0){
        	orderNewFacade.addFinanceTicketFromRevoke(orderNo,message,request);
        }
        return ResultCode.COMMON_SUCCESS;
	}
	
	private void restoreSKUNew(List<OrderItem> orderItems) {
		for (OrderItem item : orderItems) {
        	ProductSKU sku = productSKUMapper.searchById(item.getSkuId());
			if(sku.getlOWarehouseId2() > 0 && sku.getlOWarehouseId2() == item.getlOWarehouseId()){
				productSKUMapper.updateRemainCountSecond(item.getSkuId(), item.getBuyCount());
			}else {
				productSKUMapper.updateRemainCount(item.getSkuId(), item.getBuyCount());
        	} 
        }
	}

	private void reduceStoreIncome(OrderNew orderNew) {
		if (orderNew.getCommission() > 0 && orderNew.getBelongBusinessId() > 0) {
			long current = System.currentTimeMillis();
			storeBusinessDao.reduceIncome(orderNew.getBelongBusinessId(), orderNew.getCommission());
			
			StoreFinanceLog storeFinanceLog = new StoreFinanceLog();
			storeFinanceLog.setCash(orderNew.getCommission());
			storeFinanceLog.setCreateTime(current);
			storeFinanceLog.setRelatedId(orderNew.getOrderNo());
			storeFinanceLog.setStoreId(orderNew.getBelongBusinessId());
			storeFinanceLog.setType(6);
			storeFinanceLog.setUpdateTime(current);
			storeFinanceLog.setUserId(orderNew.getUserId());
			storeFinanceLogDao.addFinanceLogon(storeFinanceLog);
		}
	}

	/**
	 * 返回库存 - 要求：com.jiuy.core.service.task.OrderDisposingJob.restoreSKU() 保持一致
	 * @param orderId
	 */
    private void restoreSKU(long orderId) {
        Map<Long, Integer> productCountMap = new HashMap<Long, Integer>();
        List<OrderItem> orderItems = orderItemService.orderItemsOfOrderId(CollectionUtil.createList(orderId));
        for (OrderItem item : orderItems) {
            Integer count = productCountMap.get(item.getSkuId());
            if (count == null) {
                count = 0;
            }
            count = count + item.getBuyCount();
            productCountMap.put(item.getSkuId(), count);
        }
        Set<Entry<Long,Integer>> entries = productCountMap.entrySet();
        Iterator<Entry<Long,Integer>> i = entries.iterator();
        while(i.hasNext()){
            Entry<Long,Integer> curEntry = i.next();
            productService.updateProductSKU(curEntry.getKey(),curEntry.getValue());
        }
    }

	/**
	 * 返还代金券(满足条件的话)
	 * @param orderNewsForCoupon
	 */
	@Transactional(rollbackFor = Exception.class)
	public void retunCoupon(List<OrderNew> orderNewsForCoupon) {
		Set<Long> parentOrderNos = new HashSet<>();
		for (OrderNew orderNew : orderNewsForCoupon) {
			parentOrderNos.add(orderNew.getParentId());
		}
		List<CouponUseLog> couponUseLogs = couponUseLogService.search(parentOrderNos, 0, null);
		if (couponUseLogs.size() < 1) {
			return;
		}
		for (CouponUseLog couponUseLog : couponUseLogs) {
			long orderNo = couponUseLog.getOrderNo();
			List<OrderNew> orderNews = orderNewService.childOfSplitOrderNos(CollectionUtil.createList(orderNo));
			for (OrderNew orderNew : orderNews) {
				OrderStatus orderStatus = OrderStatus.getByIntValue(orderNew.getOrderStatus());
				if (!AdminConstants.CANCEL_SITUATION.contains(orderStatus)) {
					break;
				}
			}
			
			//返还代金券 
			couponDao.returnCoupon(couponUseLog.getCouponId(), 0, 0L);
			
			couponUseLog.setStatus(CouponUseStatus.GIVE_BACK);
			couponUseLogDao.add(couponUseLog);
		}
	}

	@Transactional(rollbackFor = Exception.class)
    public ResultCode revoke(long orderNo, long userId, int oldStatus, AdminUser adminUser) {
		OrderNew orderNew = orderNewDao.orderNewOfOrderNo(orderNo);
		if(orderNew.getParentId() == -1) {
			throw new ParameterErrorException("order revoke exception : 'ParentId' " + orderNo + " is parent type");
		}
		Set<Long> orderNos = new HashSet<Long>();
		orderNos.add(orderNo);
		
		int orderStatus = orderNew.getOrderStatus();
		
		if(orderStatus != 50) {
			throw new ParameterErrorException("order revoke exception : no such paid orderNo :" + orderNo);
		} else if (oldStatus != orderStatus) {
			throw new ParameterErrorException("order revoke exception : 'old orderStatus' " + orderStatus + " is not same with parameter of 'old_order_status'" + oldStatus);
		} else if (userId != orderNew.getUserId()) {
			throw new ParameterErrorException("order revoke exception : 'userId' " + orderNew.getUserId() + " is not same with parameter of 'user_id'" + userId);
		}
		
		if(orderNew.getMergedId() == -1) {
			List<OrderNew> orderNews = orderNewDao.childOfCombinationOrderNos(CollectionUtil.createList(orderNo));
			for(OrderNew orderNew2 : orderNews) {
				orderNos.add(orderNew2.getOrderNo());
			}
		}
//		老订单表已经删除，所以该处注释掉
//		List<OrderItem> orderItems = orderItemDao.orderItemsOfOrderNos(orderNos);
//		Set<Long> groupIds = new HashSet<Long>();
//		for(OrderItem orderItem : orderItems) {
//        	long groupId = orderItem.getGroupId();
//        	long orderId = orderItem.getOrderId();
//        	if(groupIds.contains(groupId)) {
//        		continue;
//        	}
//        	groupIds.add(groupId);
//        	orderFacade.deliveryCancel(orderId, groupId);
//        }
		
		//写入OrderNew表(包括OrderNewLog)
        orderNewFacade.updateOrderStatus(orderNo, oldStatus, OrderStatus.PAID.getIntValue(), true);
        
        //逻辑删除物流记录
        expressInfoService.remove(orderNos);

        OrderMessageBoard orderMessageBoard =
            new OrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), 1, "撤销已️发货", "");
        orderMessageBoardService.add(orderMessageBoard);
		
		return ResultCode.COMMON_SUCCESS;
	}

	public List<List<OrderNewVO>> searchUndelivered(PageQuery pageQuery, String orderNo, String clothesNum,
			String expressOrderNo, String skuNo, long yJJNumber, String receiver, String phone, long startTime,
			long endTime,String code,int orderType) {
		
		Set<Long> orderNos = new HashSet<Long>();

		if(isFilter(expressOrderNo, clothesNum, skuNo,code)) {
			//快递单号、款号、sku编号筛选出的初步符合的OrderNo
			orderNos = (Set<Long>) filterOrderNos(expressOrderNo, clothesNum, skuNo,code);
			if(orderNos.size() < 1) {
				return new ArrayList<List<OrderNewVO>>();
			}
		}
		
		long userId = -1;
		if (yJJNumber != -1) {
			List<User> users = userManageService.search(CollectionUtil.createList(yJJNumber));
			if (users.size() < 1) {
				return new ArrayList<>();
			}
			User user = users.get(0);
			userId = user.getUserId();
		}

        //获取原始数据
        List<OrderNewVO> list = orderNewFacade.searchUndelivered(pageQuery, orderNo, userId, receiver, phone,
            startTime, endTime, orderNos,orderType);
        
      //全局变量赋值，为了给searchOrderNewsCount()调用
        globalOrderNos = orderNos;
		
		List<List<OrderNewVO>> lists = new ArrayList<List<OrderNewVO>>();
		Map<Long, List<OrderNewVO>> map = new HashMap<Long, List<OrderNewVO>>();
		
		//快递Map<OrderNo, ExpressInfo>
		Map<Long, ExpressInfo> exMap = null;
        
		// 全部/组合/拆分
		Set<Long> allOrderNos = new HashSet<Long>();
		Set<Long> combinationOrderNos = new HashSet<Long>();
		Set<Long> splitOrderNos = new HashSet<Long>();
		Map<Long, OrderNewVO> splitOrderNewMap = new HashMap<Long, OrderNewVO>();
		Map<Long, OrderNewVO> combinationOrderNewMap = new HashMap<Long, OrderNewVO>();
		Map<Long, OrderNewVO> normalOrderNewMap = new HashMap<Long, OrderNewVO>();

		assemble(list, splitOrderNos, splitOrderNewMap, combinationOrderNos, combinationOrderNewMap, normalOrderNewMap, allOrderNos);

		//为了兼容同个方法,初始化意思一下
		Map<Long, List<OrderNew>> splitOrderNewsMap = new HashMap<Long, List<OrderNew>>();
		Map<Long, List<OrderNew>> combinationOrderNewsMap = orderNewFacade.combinationMapOfOrderNos(combinationOrderNos);
		
		//获取所有相关的订单购买数量
		assembleAllOrderNos(allOrderNos, splitOrderNewsMap, combinationOrderNewsMap);
		
		Map<Long, Integer> buyCountMapOfOrderNo = orderItemService.buyCountMapOfOrderNo(allOrderNos);
		exMap = expressInfoService.expressInfoMapOfOrderNos(allOrderNos);
		Map<String, ExpressSupplier> suMap = expressSupplierService.itemByEngName();
		
		//封装普通订单
		assembleNormal(normalOrderNewMap, buyCountMapOfOrderNo, exMap, map, suMap);
		//封装组合订单
		assembleCombination(combinationOrderNewsMap, combinationOrderNewMap, buyCountMapOfOrderNo, exMap, map, suMap);
		
		//这是唯一排序过的,以此为顺序依据进行封装
		//这里的封装会忽略"组合订单的子订单"和"拆分订单的子订单"
		for(OrderNewVO orderNewVO : list) {
			long orderNewNo = orderNewVO.getOrderNo();

            if (orderNewVO.getParentId() == -1) {
                lists.add(map.get(orderNewNo));
            } else if (orderNewVO.getMergedId() == -1) {
                lists.add(map.get(orderNewNo));
            } else {
                lists.add(map.get(orderNewNo));
            }
		}
		
		return lists;
	}

	public int searchUndeliveredCount(String orderNo, String clothesNum, String expressOrderNo, String skuNo,
			long yJJNumber, String receiver, String phone, long startTime, long endTime,String code,int orderType) {
		Set<Long> orderNos = globalOrderNos;
		
		if(isFilter(expressOrderNo, clothesNum, skuNo,code)) {
			if(orderNos.size() < 1) {
				return 0;
			}
		}
		
		long userId = -1;
		if (yJJNumber != -1) {
			List<User> users = userManageService.search(CollectionUtil.createList(yJJNumber));
			if (users.size() < 1) {
				return 0;
			}
			User user = users.get(0);
			userId = user.getUserId();
		}
		
		return orderNewFacade.searchUndeliveredCount(orderNo, userId, receiver, phone,
	            startTime, endTime, orderNos,orderType);
    }

    @Transactional(rollbackFor = Exception.class)
    public void recovery(long orderNo, long mergedId, long parentId, long userId, int oldStatus) {
        if (oldStatus != OrderStatus.REFUNDED.getIntValue()) {
            logger.error("revovery orderstatus: " + oldStatus + ", orderNo: " + orderNo);
            throw new ParameterErrorException("revovery orderstatus: " + oldStatus + ", orderNo: " + orderNo);
        } else if (parentId == 0) {
        	logger.error("revovery type wrong. parentId: 0" + ", orderNo: " + orderNo);
            throw new ParameterErrorException("revovery parentId: 0" + ", orderNo: " + orderNo);
        }

        Set<Long> orderNos = new HashSet<Long>();
        orderNos.add(orderNo);
        if (mergedId == -1) {
            List<OrderNew> orderNews = orderNewService.childOfCombinationOrderNos(CollectionUtil.createList(orderNo));

            for (OrderNew orderNew : orderNews) {
                orderNos.add(orderNew.getOrderNo());
            }
        }

        orderNewDao.updateOrderStatus(orderNos, OrderStatus.PAID.getIntValue());
        orderNewLogDao.updateLog(userId, orderNos, OrderStatus.REFUNDED.getIntValue(), OrderStatus.PAID.getIntValue(),
            System.currentTimeMillis());

        //老订单表已经删除，所以该处注释掉
        // 旧表
//        List<OrderItem> orderItems = orderItemService.orderItemsOfOrderNos(orderNos);
//        Set<Long> groupIds = new HashSet<Long>();
//        for (OrderItem orderItem : orderItems) {
//            groupIds.add(orderItem.getGroupId());
//        }
//        orderFacade.recoveryOrderStatus(groupIds);
    }

	public List<Map<String, Object>> deliveryExcel(int type, long startTime, long endTime, int orderType) {
		List<OrderNew> selfMergedOrderNews = orderNewService.selfMergedOrderNew(startTime, endTime,orderType);
        Map<Long, List<OrderNew>> parentMergedOrderNewMap = orderNewFacade.parentMergedMap(startTime, endTime,orderType);
        
        Set<Long> orderNos = getRelatedOrderNos(selfMergedOrderNews, parentMergedOrderNewMap);
        
        Map<Long, List<OrderItem>> orderItemByNo = orderItemService.OrderItemMapOfOrderNos(orderNos);
        
        Set<Long> productIds = new HashSet<Long>();
        for(List<OrderItem> orderItems : orderItemByNo.values()) {
        	for(OrderItem orderItem : orderItems) {
        		productIds.add(orderItem.getProductId());
        	}
        }
        Map<Long, Product> productMap = productService.productMapOfIds(productIds);
        
        Map<Long, OrderNew> orderNewsMap = orderNewService.orderNewMapOfOrderNos(orderNos);
        Set<Long> userIds = new HashSet<Long>();
        for (OrderNew orderNew : orderNewsMap.values()) {
            userIds.add(orderNew.getUserId());
        }
        Map<Long, List<Address>> addressMap = addressService.addressMapOfUserIds(userIds);
        
        Map<Long, LOWarehouse> warehouseMap = loWarehouseService.getWarehouseMap(null);
        Map<Long, BrandLogo> brandMap = baBrandLogoServiceImpl.getBrandMap();

        return assembleExcel(selfMergedOrderNews, parentMergedOrderNewMap, orderItemByNo, productMap, orderNewsMap,
            addressMap, warehouseMap, brandMap, type);
	}
	
	private List<Map<String, Object>> assembleExcel(List<OrderNew> selfMergedOrderNews, Map<Long, List<OrderNew>> parentMergedOrderNewMap, 
			Map<Long, List<OrderItem>> orderItemByNo, Map<Long, Product> productMap, Map<Long, OrderNew> orderNewsMap, 
			Map<Long, List<Address>> addressMap, Map<Long, LOWarehouse> warehouseMap, Map<Long, BrandLogo> brandMap, int type) {
		
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(OrderNew orderNew : selfMergedOrderNews) {
            List<OrderItem> orderItems = orderItemByNo.get(orderNew.getOrderNo());
            for (OrderItem orderItem : orderItems) {
                assembleExcelItem(orderItem, list, productMap, addressMap, orderNewsMap, warehouseMap, brandMap, type);
            }
		}

        for (List<OrderNew> orderNews : parentMergedOrderNewMap.values()) {
            for (OrderNew orderNew : orderNews) {
                List<OrderItem> orderItems = orderItemByNo.get(orderNew.getOrderNo());
                if (orderItems == null) {
					continue;
				}
                for (OrderItem orderItem : orderItems) {
                    assembleExcelItem(orderItem, list, productMap, addressMap, orderNewsMap, warehouseMap, brandMap, type);
                }
            }
        }

        return list;
	}

    private void assembleExcelItem(OrderItem orderItem, List<Map<String, Object>> list, Map<Long, Product> productMap,
                                   Map<Long, List<Address>> addressMap, Map<Long, OrderNew> orderNewsMap,
                                   Map<Long, LOWarehouse> warehouseMap, Map<Long, BrandLogo> brandMap, int type) {
        long productId = orderItem.getProductId();
        long orderNo = orderItem.getOrderNo();
        long warehouseId = orderItem.getlOWarehouseId();

		if(AdminConstants.ERP_WAREHOUSE_ID_LIST.contains(warehouseId)) {
			return;
		}
		
		if(type == 0 && !AdminConstants.OWN_WAREHOUSE_ID_LIST.contains(warehouseId)) {
			return;
		}
		
		if(type == 1 && AdminConstants.OWN_WAREHOUSE_ID_LIST.contains(warehouseId)) {
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
        if(orderItem.getPosition() != null && orderItem.getPosition().length() > 0){
			
			map.put("position", orderItem.getPosition().replaceAll("--", "号")+"排");
		}
        BrandLogo brandLogo = brandMap.get(orderItem.getBrandId());
        if (brandLogo == null) {
            map.put("brandName", "id" + orderItem.getBrandId() + "该品牌不存在");
        } else {
            map.put("brandName", brandLogo.getBrandName());
        }

        OrderNew orderNew = orderNewsMap.get(orderNo);
        String expressInfo = orderNew.getExpressInfo();
        map.put("expressInfo", expressInfo);
        List<Address> addresses = addressMap.get(orderNew.getUserId());
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

        list.add(map);

    }

    public Address getAddrByExp(List<Address> addresses, String expressInfo) {
        for (Address address : addresses) {
            if (StringUtils.contains(expressInfo, address.getAddrFull())) {
                return address;
            }
        }
        return null;
    }

    private Set<Long> getRelatedOrderNos(List<OrderNew> selfMergedOrderNews, Map<Long, List<OrderNew>> parentMergedOrderNewMap) {
		Set<Long> orderNos = new HashSet<>();
        for(OrderNew orderNew : selfMergedOrderNews) {
        	orderNos.add(orderNew.getOrderNo());
        }
        for(List<OrderNew> orderNews : parentMergedOrderNewMap.values()) {
            for (OrderNew orderNew : orderNews) {
            	if (orderNew.getOrderStatus() == OrderStatus.PAID.getIntValue()) {
            		orderNos.add(orderNew.getOrderNo());
				}
            }
        }
		return orderNos;
	}

	public List<List<Map<String, Object>>> searchUndeliveredNew(OrderNewSO so, PageQuery pageQuery) {
		Set<Long> orderNos = GatherOrderNos(so);
		if (orderNos.size() == 0) {
			orderNos = null;
		}
		//组合订单集合
		Set<Long> combine_order_nos = new HashSet<>();
		//组合订单的子订单&普通订单的集合&未合并的订单
		Set<Long> order_nos = new HashSet<>();
		
		Map<Long, OrderNew> orderNewMap = orderNewService.searchUndeliveredMap(so, pageQuery, orderNos);
		Iterator<OrderNew> iterator = orderNewMap.values().iterator();
		while(iterator.hasNext()) {
			OrderNew item = iterator.next();
			if (item.getMergedId() == -1) {
				combine_order_nos.add(item.getOrderNo());
			}
			order_nos.add(item.getOrderNo());
		}
		
		Map<Long, List<OrderNew>> combineOrderMap = orderNewService.getMergedChildren(combine_order_nos);
		
		for (List<OrderNew> item_list : combineOrderMap.values()) {
			for (OrderNew item : item_list) {
				order_nos.add(item.getOrderNo());
			}
		}
		
		Map<Long, Integer> productCountMap = orderItemService.getProductCountMap(order_nos);
		List<OrderNew> sList = orderNewService.orderNewsOfOrderNos(order_nos);
		List<Long> brand_order_nos = new ArrayList<>();
		for (OrderNew orderNew : sList) {
			if (orderNew.getBrandOrder() > 0) {
				brand_order_nos.add(orderNew.getBrandOrder());
			}
		}
		Map<Long, BrandExpressInfo> exMap = brandExpressInfoDao.expressInfoMapOfOrderNos(order_nos);
		
		List<List<Map<String, Object>>> results = new ArrayList<>();
		for (OrderNew item : orderNewMap.values()) {
			List<Map<String, Object>> item_list = new ArrayList<>();
			results.add(item_list);
			
			if (item.getMergedId() == item.getOrderNo()) {
				Map<String, Object> result = new HashMap<>();
				assembleUndelivered(result, item, productCountMap, exMap);
				item_list.add(result);
			} else if (item.getMergedId() == -1) {
				OrderNew combine_order = orderNewMap.get(item.getOrderNo());
				List<OrderNew> sub_orders = combineOrderMap.get(item.getOrderNo());
				if (sub_orders == null || sub_orders.size() == 0) {
					logger.error("error: mergedId:" + item.getOrderNo() + " has no children orders.");
					continue;
				}
				assembleCombineUndelivered(item_list, combine_order, sub_orders, productCountMap, exMap);
			}
		}
		
		return results;
	}
	
	private void assembleCombineUndelivered(List<Map<String, Object>> item_list, OrderNew combine_order,
			List<OrderNew> sub_orders, Map<Long, Integer> productCountMap, Map<Long, BrandExpressInfo> exMap) {

		//组合订单的信息
		Map<String, Object> merged_order_map = new HashMap<>();
		assembleUndelivered(merged_order_map, combine_order, productCountMap, exMap);
		
		//将合并订单放到首位
		item_list.add(merged_order_map);
		
		int total_count = 0;
		for (OrderNew item : sub_orders) {
			Map<String, Object> item_map = new HashMap<>();
			total_count += assembleUndelivered(item_map, item, productCountMap, exMap);
			item_list.add(item_map);
		}
		merged_order_map.put("buy_count", total_count);
		merged_order_map.put("pay_type", "多订单支付");
	}

	private int assembleUndelivered(Map<String, Object> result, OrderNew item, Map<Long, Integer> productCountMap, Map<Long, BrandExpressInfo> exMap) {
		
		result.put("user_id", item.getUserId());
		result.put("deal_time", DateUtil.convertMSEL(item.getUpdateTime()));
		result.put("order_no", item.getOrderNo());
		result.put("pay_type", PaymentTypeDetail.getByIntValue(item.getPaymentType()));
		result.put("money", item.getTotalMoney() + item.getTotalExpressMoney());
		result.put("express_info", item.getExpressInfo());
		result.put("order_type", item.getMergedId() == -1L ? "组合订单" : "普通"); 
		result.put("buy_count", productCountMap.get(item.getOrderNo()) == null ? 0 : productCountMap.get(item.getOrderNo()));
		result.put("order_status", OrderStatus.getByIntValue(item.getOrderStatus()));
		BrandExpressInfo eInfo = exMap.get(item.getBrandOrder());
		result.put("express_no", eInfo == null ? "" : eInfo.getExpressOrderNo());
		result.put("express_supplier", eInfo == null ? "" : eInfo.getExpressSupplier());
		result.put("own_warehouse", AdminConstants.OWN_WAREHOUSE_ID_LIST.contains(item.getlOWarehouseId()));
		result.put("brand_order_no", item.getBrandOrder());
		LOWarehouse warehouse = loWarehouseService.loadById(item.getlOWarehouseId());
		result.put("warehouse_name", warehouse == null ? "" : warehouse.getName());
		
		return productCountMap.get(item.getOrderNo()) == null ? 0 : productCountMap.get(item.getOrderNo());
	}

	private Set<Long> GatherOrderNos(OrderNewSO so) {
		Set<Long> orderNos = new HashSet<>();
		if (so.getExpressNo() != null) {
			List<ExpressInfo> expressInfos = expressInfoDao.expressInfoOfBlurOrderNo(so.getExpressNo());
            for (ExpressInfo expressInfo : expressInfos) {
            	orderNos.add(expressInfo.getOrderNo());
            }
		}
		if (so.getClothesNum() != null) {
			List<Product> products = productService.getByClothesNums(Arrays.asList(new String[]{so.getClothesNum()}));
			Set<Long> productIds = new HashSet<>();
			for (Product product : products) {
				productIds.add(product.getId());
			}
			List<OrderItem> orderItems = orderItemService.orderItemsOfProductIds(productIds);
            for (OrderItem orderItem : orderItems) {
            	orderNos.add(orderItem.getOrderNo());
			}
		}
		if (so.getBrandOrderStatus() != null) {
			BrandOrderSO brandOrderSO = new BrandOrderSO();
			brandOrderSO.setOrderStatus(OrderStatus.getByIntValue(so.getBrandOrderStatus()));
			brandOrderSO.setRelatedOrderType(2);
			
			List<BrandOrder> brandOrders = brandOrderService.search(brandOrderSO);
			for (BrandOrder brandOrder : brandOrders) {
				orderNos.add(brandOrder.getRelatedOrderNo());
			}
		}
		if (so.getyJJNumber() != null) {
			User user = userService.getByYjjNumber(so.getyJJNumber());
			List<OrderNew> orderNews = orderNewService.getByUserIdStatus(user.getUserId(), OrderStatus.PAID);
			for (OrderNew orderNew : orderNews) {
				orderNos.add(orderNew.getOrderNo());
			}
		}
		if (so.getSkuNo() != null) {
			List<ProductSKU> productSKUs = productSKUMapper.skuOfNo(Arrays.asList(new Long[]{so.getSkuNo()}));
			Set<Long> skuIds = new HashSet<>();
            for (ProductSKU productSKU : productSKUs) {
                skuIds.add(productSKU.getId());
            }
            List<OrderItem> orderItems = orderItemService.orderItemsOfSkuIds(skuIds, null);
            for (OrderItem orderItem : orderItems) {
            	orderNos.add(orderItem.getOrderNo());
			}
		}
		if (so.getWarehouseId() != null) {
			List<Product> products = new ArrayList<>();
			if (so.getWarehouseId() == 1) {
				products = productService.getByWarehouseIds(AdminConstants.OWN_WAREHOUSE_ID_LIST);
			} else if (so.getWarehouseId() == 2) {
				products = productService.getByNotInWarehouseIds(AdminConstants.PUSH_TO_ERP_WAREHOUSE_ID_LIST);
			}
			Set<Long> productIds = new HashSet<>();
			for (Product product : products) {
				productIds.add(product.getId());
			}
			
			if (productIds.size() > 0) {
				List<OrderItem> orderItems = orderItemService.orderItemsOfProductIds(productIds);
				for (OrderItem orderItem : orderItems) {
					orderNos.add(orderItem.getOrderNo());
				}
			}
		}
		
		return orderNos;
	}

	public int searchUndeliveredNewCount(OrderNewSO so) {
		Set<Long> orderNos = GatherOrderNos(so);
		if (orderNos.size() == 0) {
			orderNos = null;
		}
		return orderNewService.searchUndeliveredNewCount(so, orderNos);
	}

	@Transactional(rollbackFor = Exception.class)
	public void dispatchOrder(long orderNo, String remark, AdminUser adminUser) {
		long current = System.currentTimeMillis();
		
		OrderNew orderNew = orderNewDao.orderNewOfOrderNo(orderNo);
		
		BrandBusinessSO so = new BrandBusinessSO();
		so.setLoWarehouseId(orderNew.getlOWarehouseId());
		BrandBusiness brandBusiness = brandBusinessService.get(so);
		
		List<BrandOrder> brandOrders = new ArrayList<>();
		brandOrders.add(assembleBrandOrder(orderNew, brandBusiness, current));
		//插入brand_order表
		List<BrandOrder> return_brand_orders = new ArrayList<>();
//		List<BrandOrder> return_brand_orders = brandOrderDao.add(brandOrders);	

		//更新本表对应订单，子订单不更新
		OrderNewUO uo = new OrderNewUO();
		uo.setBrandOrder(return_brand_orders.get(0).getOrderNo());
		uo.setUpdateTime(current);
		orderNewDao.update(uo, orderNo);

		List<BrandOrderItem> brandOrderItems = new ArrayList<>();
		
		if (orderNew.getMergedId() == -1) {
			List<OrderNew> orderNews = orderNewDao.orderNewsOfParentMergedOrderNos(Arrays.asList(orderNew.getOrderNo()));
			for (OrderNew item : orderNews) {
				brandOrders = new ArrayList<>();
				brandOrders.add(assembleBrandOrder(item, brandBusiness, current));
//				return_brand_orders = brandOrderDao.add(brandOrders);	
				
				assembleBrandOrderItems(orderItemService.getOrderItemByOrderNos(Arrays.asList(new Long[]{item.getOrderNo()})), brandBusiness, brandOrderItems, current, return_brand_orders.get(0).getOrderNo());
			}
		} else {
			assembleBrandOrderItems(orderItemService.getOrderItemByOrderNos(Arrays.asList(new Long[]{orderNew.getOrderNo()})), brandBusiness, brandOrderItems, current, return_brand_orders.get(0).getOrderNo());
		}
				
		//插入brand_orderItem表
		brandOrderItemDao.add(brandOrderItems);
		
		//记录表
		OrderMessageBoard orderMessageBoard = new OrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), 2, null, remark);
		orderMessageBoardDao.add(orderMessageBoard);
		
	}
	
	private void assembleBrandOrderItems(List<OrderItem> orderItems, BrandBusiness brandBusiness, List<BrandOrderItem> brandOrderItems, long current, long brandOrderNo) {
		for (OrderItem item : orderItems) {
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

	private BrandOrder assembleBrandOrder(OrderNew orderNew, BrandBusiness brandBusiness, long current) {
		BrandOrder brandOrder = new BrandOrder();
		BeanUtils.copyProperties(orderNew, brandOrder);
		brandOrder.setOrderNo(0); 
		brandOrder.setlOWarehouseId(brandBusiness.getlOWarehouseId());
		brandOrder.setBrandBusinessId(brandBusiness.getId());
		brandOrder.setCreateTime(current);
		brandOrder.setUpdateTime(current);
		brandOrder.setRelatedOrderNo(orderNew.getOrderNo());
		brandOrder.setRelatedOrderType(1);
		brandOrder.setOrderStatus(orderNew.getOrderStatus());
		brandOrder.setOrderType(orderNew.getOrderType());
		brandOrder.setPaymentType(orderNew.getPaymentType());
		
		return brandOrder;
	}

	@Transactional(rollbackFor = Exception.class)
	public void revokeDispatchOrder(long orderNo, String remark, AdminUser adminUser) {
		List<Long> orderNos = new ArrayList<>();
		orderNos.add(orderNo);
		
		BrandOrderSO so = new BrandOrderSO();
		so.setOrderNo(orderNo);
		List<BrandOrder> brandOrders = brandOrderService.search(so);
		if (brandOrders.get(0).getMergedId() == -1) {
			so = new BrandOrderSO();
			so.setMergedId(orderNo);
			List<BrandOrder> sub_orders = brandOrderService.search(so);
			for (BrandOrder sub_item : sub_orders) {
				orderNos.add(sub_item.getOrderNo());
			}
		}
		BrandOrderUO uo = new BrandOrderUO();
		uo.setUpdateTime(System.currentTimeMillis());
		uo.setStatus(-1);
		uo.setOrderNos(orderNos);
		
		brandOrderDao.update(uo);
		
		//记录表
		OrderMessageBoard orderMessageBoard = new OrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), 3, null, remark);
		orderMessageBoardDao.add(orderMessageBoard);
	}

}