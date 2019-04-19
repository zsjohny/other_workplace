package com.jiuy.web.delegate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.jiuy.core.business.facade.OrderFacade;
import com.jiuy.core.business.facade.OrderNewFacade;
import com.jiuy.core.dao.OrderItemDao;
import com.jiuy.core.dao.mapper.QMOrderDao;
import com.jiuy.core.dao.mapper.QMOrderItemDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.account.User;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.order.OrderItem;
import com.jiuy.core.meta.order.OrderNew;
import com.jiuy.core.service.AddressService;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.ProductCategoryService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.PropertyService;
import com.jiuy.core.service.UserManageService;
import com.jiuy.core.service.order.OrderItemService;
import com.jiuy.core.service.order.OrderOldService;
import com.jiuy.core.service.product.ProductSKUService;
import com.jiuy.util.JsonUtil;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.constant.PropertyName;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.qianmi.QMOrder;
import com.jiuyuan.entity.qianmi.QMOrderItem;
import com.jiuyuan.util.WdtSkuUtil;

@Service
public class ErpDelegator {
    
	private static final Logger logger = LoggerFactory.getLogger("WDT");

    @Autowired
    private OrderNewFacade orderNewFacade;

//    @Autowired
//    private OrderFacade orderFacade;

    @Autowired
    private OrderOldService orderNewService;
    
    @Autowired
    private UserManageService userManageService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ProductService productService;
    
	@Autowired
	private ProductSKUService productSKUService;

    @Autowired
    private OrderItemService orderItemService;
    
    @Autowired
    private OrderItemDao orderItemDao;

//    @Autowired
//    private OrderService orderService;
    
    @Autowired
    private ProductCategoryService productCategoryService;
    
    @Autowired
    private BrandLogoServiceImpl brandLogoServiceImpl;
    
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private OrderNewDelegator orderNewDelegator;
    
    @Autowired
    private QMOrderDao qMOrderDao;
    
    @Autowired
    private QMOrderItemDao qMOrderItemDao;
    
//    @Autowired
//    private OrderNewDao orderNewDao;
    
    public List<Map<String, Object>> getToBePushedOrders(long startTime, long endTime) {
        
        List<OrderNew> selfMergedOrderNews = orderNewService.selfMergedOrderNew(startTime, endTime,1);
        Map<Long, List<OrderNew>> parentMergedOrderNewMap = orderNewFacade.parentMergedMap(startTime, endTime,1);

        return assemblePushedOrder(selfMergedOrderNews, parentMergedOrderNewMap);

    }

    public List<Map<String, Object>> assemblePushedOrder(List<OrderNew> selfMergedOrderNews, Map<Long, List<OrderNew>> parentMergedOrderNewMap) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for(OrderNew orderNew : selfMergedOrderNews) {
            List<OrderNew> list2 = new ArrayList<OrderNew>();
            list2.add(orderNew);
            //添加了推送给erp所有的父订单map<Long, Parent>
            parentMergedOrderNewMap.put(orderNew.getOrderNo(), list2);
        }

        Set<Long> orderNos = new HashSet<Long>();
        for(List<OrderNew> orderNews : parentMergedOrderNewMap.values()) {
            for (OrderNew orderNew : orderNews) {
        		orderNos.add(orderNew.getOrderNo());
            }
        }
        
        //包括父组合自身的orderNo
        orderNos.addAll(parentMergedOrderNewMap.keySet());

        List<OrderItem> orderItems = orderItemService.getOrderItemByOrderNos(orderNos);
        //<Long, Child>
        Map<Long, OrderNew> orderNewsMap = orderNewService.orderNewMapOfOrderNos(orderNos);
        
        Set<Long> userIds = new HashSet<Long>();
        for (OrderNew orderNew : orderNewsMap.values()) {
            userIds.add(orderNew.getUserId());
        }

        Set<Long> skuIds = new HashSet<Long>();

        long lastOrderNo = 0;
        List<OrderItem> orderItems2 = null;
        // Map<Long, OrderItem> 子订单OrderNo对应orderItems
        Map<Long, List<OrderItem>> orderItemOfOrderNoMap = new HashMap<Long, List<OrderItem>>();
        for (OrderItem orderItem : orderItems) {
            long orderNo = orderItem.getOrderNo();
            if (orderNo != lastOrderNo) {
                lastOrderNo = orderNo;
                orderItems2 = new ArrayList<OrderItem>();
                orderItemOfOrderNoMap.put(lastOrderNo, orderItems2);
            }
            orderItems2.add(orderItem);

            // 拼装其他
            skuIds.add(orderItem.getSkuId());
        }

        Map<Long, User> userMap = userManageService.usersMapOfIds(userIds);
        Map<Long, List<Address>> addressMap = addressService.addressMapOfUserIds(userIds);
        Map<Long, ProductSKU> skuMap = productSKUService.skuMapOfIds(skuIds);
        
        Set<Long> productIds = new HashSet<Long>();

        for (Map.Entry<Long, ProductSKU> entry : skuMap.entrySet()) {
            ProductSKU productSKU = entry.getValue();
            productIds.add(productSKU.getProductId());
        }
        Map<Long, Product> productMap = productService.productMapOfIds(productIds);

        for (Map.Entry<Long, List<OrderNew>> entry : parentMergedOrderNewMap.entrySet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            long key = entry.getKey();
            List<OrderNew> orderNews = entry.getValue();
            
            //目前只推百事汇通的
            if(orderNews.size() < 1) {
                continue;
            }
            
            //如果不是ERP所属的仓库，则不推
            if(!AdminConstants.ERP_WAREHOUSE_ID_LIST.contains(orderNews.get(0).getlOWarehouseId())) {
            	continue;
            }

            OrderNew orderNew = orderNewsMap.get(key);
            User user = userMap.get(orderNew.getUserId());

            String expressInfo = orderNew.getExpressInfo();

            List<Address> addresses = addressMap.get(user.getUserId());
            //删除orderFacade时将该方法移到了orderNewFacade
            Address address = orderNewFacade.getAddrByExp(addresses, expressInfo);

            String orderNo = String.format("%09d", key);
            map.put("orderNo", orderNo);
            map.put("trade_time", orderNew.getCreateTime());
            map.put("pay_time", orderNew.getUpdateTime());
            map.put("buyer_nick", user.getUserNickname());
            map.put("buyer_email", user.getUserRelatedName());
            if (address != null) {
                map.put("receiver_name", address.getReceiverName().replace("\"", ""));
                map.put("receiver_province", address.getProvinceName().replace("\"", ""));
                map.put("receiver_city", address.getCityName().replace("\"", ""));
                map.put("receiver_district", address.getDistrictName().replace("\"", ""));
                map.put("receiver_mobile", address.getTelephone().replace("\"", ""));
                map.put("receiver_address", address.getAddrDetail().replaceAll("\r|\n", ""));
            } else {
                logger.error("com.jiuy.web.delegate.ErpDelegator : " + user.getUserId() + " Address Not Found! The Detail Address Is: " + expressInfo);
                map.put("receiver_name", "未找到");
                map.put("receiver_province", "未找到");
                map.put("receiver_city", "未找到");
                map.put("receiver_district", "未找到");
                map.put("receiver_mobile", "未找到");
                map.put("receiver_address", expressInfo);
            }
            
            //不包含邮费
            double paid = getTotalPay(orderNews);
            double postage = getMaxPostage(orderNews);
            DecimalFormat df = new DecimalFormat("#.##"); 
            double totalPay = paid + postage;

            map.put("paid", df.format(totalPay));
            map.put("post_amount", postage);
            
            int i = 0;
            List<Map<String, Object>> subOrderList = new ArrayList<Map<String, Object>>();
            //最后一个orderItem的totalPay不按比例,总的totalPay减去除了最后一个orderItemTotalPay的和
            int size = orderNews.size();
            int sort = 0;
            double sumTotalPay = 0;
            for (OrderNew orderNew2 : orderNews) {
            	if(orderNew2.getOrderStatus() != 10) {
            		continue;
            	}
                List<OrderItem> orderItems3 = orderItemOfOrderNoMap.get(orderNew2.getOrderNo());
                
                sort++;
                int orderItemSize = orderItems3.size();
                int orderItemSort = 0;
                for (OrderItem orderItem : orderItems3) {
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    long skuId = orderItem.getSkuId();
                    ProductSKU productSKU = skuMap.get(skuId);
                    Product product = productMap.get(productSKU.getProductId());

                    String sku = orderItem.getSkuSnapshot();
                    String skuSnapShot = sku.replaceAll("颜色:", "").replaceAll("尺码:", "").trim().replace(" ", "");

                    orderItemSort++;
                    double orderItemTotalPay = 0;
                    if (sort == size && orderItemSize == orderItemSort) {
                    	orderItemTotalPay = paid - sumTotalPay;
					} else {
						sumTotalPay += orderItem.getTotalPay();
						orderItemTotalPay= orderItem.getTotalPay();
					}
                    
                    double discount = orderItem.getTotalMoney() - orderItemTotalPay;
                    int buyCount = orderItem.getBuyCount();

                    map2.put("oid", orderNo + "-" + (++i));
                    map2.put("num", buyCount);
                    map2.put("price", orderItem.getMoney());
                    map2.put("spec_id", skuId);
                    map2.put("spec_no", productSKU.getSkuNo());
                    map2.put("goods_name", product.getName());
                    map2.put("spec_name", skuSnapShot);
                    map2.put("discount", Double.parseDouble(String.format("%.2f", discount)));
                    map2.put("share_discount", 0);

                    subOrderList.add(map2);
                }
            }
            //防止子订单都被取消，而父订单是待发货状态
            if(subOrderList.size() > 0) {
            	map.put("order_list", subOrderList);
            	list.add(map);
            }
        }
        return list;

    }

    private double getTotalPay(List<OrderNew> orderNews) {
        double totalPay = 0.00;
        for (OrderNew orderNew : orderNews) {
            totalPay += orderNew.getTotalPay();
        }

        return totalPay;
    }

    private double getMaxPostage(List<OrderNew> orderNews) {
        double maxPostage = 0.00;

        for (OrderNew orderNew : orderNews) {
            if (orderNew.getTotalExpressMoney() - maxPostage > 0.001) {
                maxPostage = orderNew.getTotalExpressMoney();
            }
        }

        return maxPostage;
    }

    @Transactional(rollbackFor = Exception.class)
	public void updateExpressInfo(List<Map<String, Object>> list) {
    	if (list.size() < 1) {
			return;
		}
    	
    	Set<Long> orderNos = new HashSet<Long>();
//    	Set<Long> allOrderNos = new HashSet<Long>();
//    	Set<Long> combinationOrderNos = new HashSet<Long>();
    	
    	for(Map<String, Object> map : list) {
    		long orderNo = Long.parseLong((String) map.get("orderNo"));
    		orderNos.add(orderNo);
//    		allOrderNos.add(orderNo);
    	}
    	
    	Map<Long, OrderNew> orderNewMap = orderNewService.orderNewMapOfOrderNos(orderNos);
    	

//    	long currentTime = System.currentTimeMillis();

//    	for(Map<String, Object> map : list) {
//    		long orderNo = Long.parseLong((String) map.get("orderNo"));
//    		OrderNew orderNew = orderNewMap.get(orderNo);
//    		map.put("userId", orderNew.getUserId());
//    		map.put("currentTime", currentTime);
    		
    		//记录组合订单orderNo
//    		if(orderNew.getMergedId() == -1) {
//    			combinationOrderNos.add(orderNo);	
//    		} 
//    	}
    	
//    	Map<Long, List<OrderNew>> childOfCombinationMap = orderNewService.parentMergedMap(combinationOrderNos);

    	//遍历出所有的orderNo
//    	for(Map.Entry<Long, List<OrderNew>> entry : childOfCombinationMap.entrySet()) {
//    		List<OrderNew> orderNews = entry.getValue();
//    		
//    		for(OrderNew orderNew : orderNews) {
//    			allOrderNos.add(orderNew.getOrderNo());
//    		}
//    	}
    	
//    	Map<Long, List<OrderItem>> orderItenMapOfOrderNo = orderItemService.OrderItemMapOfOrderNos(allOrderNos);
    	
    	//更新ordernew表
//        orderNewService.updateOrderStatus(allOrderNos, OrderStatus.DELIVER.getIntValue());
    	
    	//和后台发货接口共用
    	for(Map<String, Object> map : list) {
    		long orderNo = Long.parseLong((String) map.get("orderNo"));
    		String logistics_name = (String) map.get("expressSupplier");
    		String logistics_no = (String) map.get("expressOrderNo");
    		
/*    		if(combinationOrderNos.contains(orderNo)) {
    			//组合订单则更新里面的普通订单
    			List<OrderNew> orderNews = childOfCombinationMap.get(orderNo);
    			for(OrderNew orderNew : orderNews) {
    				long orderNo1 = orderNew.getOrderNo();
    				List<OrderItem> orderItems = orderItenMapOfOrderNo.get(orderNo1);
        			long orderId = 0;
        			long groupId = 0;
    				for(OrderItem orderItem : orderItems) {
        				//老的商品发货
    					orderId = orderItem.getOrderId();
    					groupId = orderItem.getGroupId();
    					break;
        			}
    				orderService.bindOrder(orderId, groupId, logistics_name, logistics_no, true, orderNo1);
    			}
    		} else {
    			//非组合订单直接取orderNo
    			
    			List<OrderItem> orderItems = orderItenMapOfOrderNo.get(orderNo);
    			for(OrderItem orderItem : orderItems) {
    				//老的商品发货
    				orderService.bindOrder(orderItem.getOrderId(), orderItem.getGroupId(), logistics_name, logistics_no, true, orderItem.getOrderNo());
    			}
    		} 
*/
    		
    		OrderNew orderNew = orderNewMap.get(orderNo);
    		if (orderNew == null) {
				throw new ParameterErrorException("ErpDelegator -> 未找到改订单！订单号：" + orderNo);
			}
    		if (orderNew.getStatus() != OrderStatus.PAID.getIntValue()) {
				continue;
			}
    		AdminUser adminUser = new AdminUser();
    		adminUser.setUserName("erp");
    		adminUser.setUserId(-1L);
    		orderNewDelegator.delivery(orderNo, orderNew.getUserId(), logistics_name, logistics_no, adminUser,1);
    	}
	}
    
    public void addPushTime(Collection<Long> orderNos) {
        Map<Long, OrderNew> orderNewsByNo = orderNewService.orderNewMapOfOrderNos(orderNos);
        
        Set<Long> allOrderNos = new HashSet<Long>();
        Set<Long> combinationNos = new HashSet<Long>();

        for (Long orderNo : orderNos) {
            OrderNew orderNew = orderNewsByNo.get(orderNo);
            if (orderNew.getMergedId() == -1) {
                combinationNos.add(orderNo);
            }
            allOrderNos.add(orderNo);
        }

        List<OrderNew> subOrderNews = orderNewService.childOfCombinationOrderNos(combinationNos);
        for (OrderNew subOrderNew : subOrderNews) {
            allOrderNos.add(subOrderNew.getOrderNo());
        }

        orderNewService.addPushTime(allOrderNos, DateUtil.getERPTime());

    }

    public Map<Long, Integer> getExpectedSale() {
        List<Map<String, Object>> list = orderItemDao.getExpectedSaleCount();

        Map<Long, Integer> expectedSaleBySkuNo = new HashMap<Long, Integer>();
        for (Map<String, Object> map : list) {
            Long skuNo = (Long) map.get("SkuNo");
            Integer totalBuyCount = Integer.parseInt(map.get("TotalBuyCount").toString());
            expectedSaleBySkuNo.put(skuNo, totalBuyCount);
        }

        return expectedSaleBySkuNo;
    }

    public List<Map<String, Object>> getPushedProduct() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<Long, List<ProductSKU>> skuByProdcuctId = productSKUService.pushedSkusOfProductIdMap();
        
        Set<Long> productIds = skuByProdcuctId.keySet();
        Map<Long, String> categoryByProductId = productCategoryService.erpCatByProductId(productIds);
        Map<Long, Product> productById = productService.productMapOfIds(productIds);
        Map<Long, BrandLogo> brandById = brandLogoServiceImpl.getBrandMap();
		
        List<ProductPropValue> colors = (List<ProductPropValue>) propertyService.getPropertyByNameId(PropertyName.COLOR);
		List<ProductPropValue> sizes = (List<ProductPropValue>) propertyService.getPropertyByNameId(PropertyName.SIZE);
		
        for(Map.Entry<Long, List<ProductSKU>> entry : skuByProdcuctId.entrySet()) {
        	Map<String, Object> map = new HashMap<String, Object>();

        	long productId = entry.getKey();
        	Product product = productById.get(productId);

        	if(product == null) {
        		throw new ParameterErrorException("product not exist : " + productId);
        	}
        	
        	List<ProductSKU> skus = entry.getValue();
        	if(skus.size() < 1) {
        		continue;
        	}

        	String brandName = brandById.get(product.getBrandId()).getCnName();
        	map.put("goods_no", skus.get(0).getClothesNumber());
        	map.put("goods_type", 1);
        	map.put("goods_name", product.getName());
        	map.put("class_name", categoryByProductId.get(productId));
        	map.put("brand_name", brandName);
        	
        	List<Map<String, Object>> sub_list = new ArrayList<Map<String, Object>>();
        	map.put("spec_list", sub_list);
        	
        	for(ProductSKU productSKU : skus) {
        		Map<String, Object> sub_map = new HashMap<String, Object>();
        		long skuNo = productSKU.getSkuNo();
        		
        		sub_map.put("spec_no", skuNo);
        		sub_map.put("barcode", skuNo);
        		sub_map.put("spec_name", assembleSkuSnapShot(productSKU.getPropertyIds(), colors, sizes));
        		sub_map.put("retail_price", productSKU.getMarketPrice());
        	
        		sub_list.add(sub_map);
        	}
        	list.add(map);

        }
        
        return list;
    }

    //转换:例如 7:530;8:426 --> 黑色M
	private String assembleSkuSnapShot(String propertyIds, List<ProductPropValue> colors, List<ProductPropValue> sizes) {
		
		Map<Long, String> colorMap = new HashMap<Long, String>();
		Map<Long, String> sizeMap = new HashMap<Long, String>();
		
		for(ProductPropValue color : colors) {
			colorMap.put(color.getId(), color.getPropertyValue());
		}
		
		for(ProductPropValue size : sizes) {
			sizeMap.put(size.getId(), size.getPropertyValue());
		}
		
		String[] properties = propertyIds.split(";");
		long colorId = Long.parseLong(properties[0].split(":")[1]);
		long sizeId = Long.parseLong(properties[1].split(":")[1]);
		
		StringBuilder builder = new StringBuilder();
		builder.append(colorMap.get(colorId));
		builder.append(sizeMap.get(sizeId));
		
		return builder.toString();
		
	}

	@SuppressWarnings("unchecked")
	public void pushProducts(List<Map<String, Object>> list, Set<String> clothesNos) {
		for(Map<String, Object> map : list) {
	        StringBuilder builder = new StringBuilder();

	        builder.append("[{\"goods_no\":");
	        builder.append("\"" + map.get("goods_no") + "\",");

	        builder.append("\"goods_type\":");
	        builder.append("" + map.get("goods_type") + ",");
	        
	        builder.append("\"goods_name\":");
	        builder.append("\"" + map.get("goods_name") + "\",");

	        builder.append("\"class_name\":");
	        builder.append("\"" + map.get("class_name") + "\",");

	        builder.append("\"brand_name\":");
	        builder.append("\"" + map.get("brand_name") +"\",");
	        
	        builder.append("\"spec_list\": [");
	        for(Map<String, Object> map2 : (List<Map<String, Object>>) map.get("spec_list")) {
	            builder.append("{\"spec_no\": ");
	            builder.append("\"" + map2.get("spec_no") + "\",");
	            
	            builder.append("\"barcode\":");
	            builder.append("\"" + map2.get("barcode") + "\",");

	            builder.append("\"spec_name\":");
	            builder.append("\"" + map2.get("spec_name") + "\",");
	            
	            builder.append("\"retail_price\": ");
	            //skuId
	            builder.append("\"" + map2.get("retail_price") + "\"");
	            builder.append("},");
	        }
	        builder.append("]}]");
	        Map<String, String> map3 = new HashMap<String, String>();
	        
	        int index = builder.lastIndexOf(",");
	        builder.deleteCharAt(index);
	        
	        map3.put("goods_list", builder.toString());
	        
	        String result = WdtSkuUtil.send(AdminConstants.WDT_GOODS_PUSH_URL, map3);
	        
	        Object object = JSON.parse(result);
            JSONObject jsonObject = (JSONObject) object;
            int code = jsonObject.getInteger("code");
            if (code == 0) {
                String clothesNo = (String) map.get("goods_no");
                clothesNos.add(clothesNo);
            } else {
            	logger.error("com.jiuy.web.delegate.ErpDelegator : fail to push orderNo: " + (String) map.get("goods_no") + ", detail: " + result);
            }

		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void synchronizationCount() {
		List<ProductSKU> skus = productSKUService.skusOfWarehouse(AdminConstants.ERP_WAREHOUSE_ID_LIST);
		
		List<Long> skuNos = new ArrayList<Long>();
		for(ProductSKU productSKU : skus) {
			skuNos.add(productSKU.getSkuNo());
		}
		
//		校验		
//		List<ProductSKU> skus2 = productSKUService.skusOfWarehouse(CollectionUtil.createList(14L));
//		
//		List<Long> skuNos2 = new ArrayList<Long>();
//		for(ProductSKU productSKU : skus2) {
//			skuNos2.add(productSKU.getSkuNo());
//		}
		
 		int pageSize = AdminConstants.WDT_STOCK_QUERY_PAGE_SIZE;

        Map<String, String> map = new HashMap<String, String>();

        map.put("page_no", "0");
        map.put("page_size", pageSize + "");
        map.put("warehouse_no", "CK001");

        String result = WdtSkuUtil.send(AdminConstants.WDT_STOCK_QUERY_URL, map);
        Object object = JsonUtil.getValue(result, "total_count");
        String totalCountStr = (String) object;
        int totalCount = Integer.parseInt(totalCountStr);
        int pageNo = totalCount / pageSize;
        pageNo = totalCount % pageSize == 0 ? pageNo : pageNo + 1;

        logger.error("erpDelegator: pageNo:" + pageNo);
        
        Set<Long> saleSkuNos = new HashSet<Long>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuilder builder_ids = new StringBuilder();
        StringBuilder builder_conditions = new StringBuilder();
        builder_ids.append("(");
        for (int i = 0; i < pageNo; i++) {
            map.put("page_no", i + "");
            String skuInfo = WdtSkuUtil.send(AdminConstants.WDT_STOCK_QUERY_URL, map);

            JSONArray jsonArray = (JSONArray) JsonUtil.getValue(skuInfo, "stocks");
            Iterator<Object> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
            	Map<String, Object> map_sku = new HashMap<String, Object>();
                JSONObject jsonObject = (JSONObject) iterator.next();
                // skuNo
                Long spec_no = Long.parseLong((String) jsonObject.get("spec_no"));
                // 库存量
                int stock_num = (int)Double.parseDouble((String) jsonObject.get("stock_num"));
                // 预订单量
                int subscribe_num = (int)Double.parseDouble((String) jsonObject.get("subscribe_num"));
                // 待审核量
                int order_num = (int)Double.parseDouble((String) jsonObject.get("order_num"));
                // 待发货量
                int sending_num = (int)Double.parseDouble((String) jsonObject.get("sending_num"));
                //采购到货量
                int purchase_arrive_num = (int)Double.parseDouble((String) jsonObject.get("purchase_arrive_num"));
                // 锁定库存 
                int lock_num = (int)Double.parseDouble((String) jsonObject.get("lock_num"));
                
//            	校验
//            	if(skuNos2.contains(spec_no)) {
//            		System.out.println("not sync " + spec_no);
//            	}
                
                if(!skuNos.contains(spec_no)) {
                	continue;
                }
                
                //ERP待确认
                int remainCount = stock_num - subscribe_num - order_num - sending_num - lock_num + purchase_arrive_num;
                
                //本地坑位：未推送 & 取消订单
                Map<Long, Integer> expectedSaleBySkuNo = getExpectedSale();
                
                
                //减去本地销售库存
                if (expectedSaleBySkuNo.get(spec_no) != null) {
                    int expectedSale = expectedSaleBySkuNo.get(spec_no);
                    remainCount -= expectedSale;
                }
                
                if (remainCount > 0) {
                	saleSkuNos.add(spec_no);
				}
                
                map_sku.put("skuNo", spec_no);
                map_sku.put("remainCount", remainCount);
                list.add(map_sku); 
                
                builder_ids.append( spec_no + ",");
                builder_conditions.append("WHEN " + spec_no + " THEN " + remainCount + "\n");
            }
        }
        int length = builder_ids.length();
        if(length > 1 ) {
        	builder_ids.replace(length - 1, length, ")"); 
        }
        productSKUService.syncRemainCount(builder_ids.toString(), builder_conditions.toString());
        productSKUService.recoverSale(saleSkuNos);
	}

    public void pushOrders(List<Map<String, Object>> list, Set<Long> successOrderNos) {
		for(Map<String, Object> map : list) {
//            String result = WdtSkuUtil.send(Constants.WDT_TRADE_PUSH_URL, map3);
	        String result = WdtSkuUtil.send(AdminConstants.WDT_TRADE_PUSH_URL, assembleErpForm(map));
	        handlePushOrderResult(map, result, successOrderNos);
		}
	}

    private void handlePushOrderResult(Map<String, Object> map, String result, Set<Long> successOrderNos) {
    	 Object object = JSON.parse(result);
         JSONObject jsonObject = (JSONObject) object;
//         int code = (int) (jsonObject.get("code"));
         int code = jsonObject.getInteger("code");
         if (code == 0) {
             long orderNo = Long.parseLong((String) map.get("orderNo"));
             successOrderNos.add(orderNo);
         } else {
         	logger.error("com.jiuy.core.service.task.WdtOrderJob: Fail to push orderNo, " + (String) map.get("orderNo") + ", detail: " + result);
         }
	}

	/**
     * 数据涉及：支付方式 、款号、货品数量、订单类别、支付方式、仓库名称、玖币、店铺名称
     * 
     * @param map
     * @return
     */
	@SuppressWarnings("unchecked")
	private Map<String, String> assembleErpForm(Map<String, Object> map) {
		StringBuilder builder = new StringBuilder();

        builder.append("[{\"tid\":");
        builder.append("\"" + map.get("orderNo") + "\",");

        builder.append("\"trade_status\": 30,");
        builder.append("\"pay_status\": 2,");
        builder.append("\"delivery_term\": 1,");
        builder.append("\"trade_time\":");
        //下单时间
        builder.append("\"" + DateUtil.convertMSEL((Long)map.get("trade_time")) + "\",");
        builder.append("\"pay_time\":");
        // 付款时间
        builder.append("\"" + DateUtil.convertMSEL((Long)map.get("pay_time")) + "\",");
        builder.append("\"buyer_nick\":");
        builder.append("\"\",");
        builder.append("\"buyer_email\":");
        builder.append("\"" + map.get("buyer_email") +"\",");
        builder.append("\"pay_id\": ");
        builder.append("\"11111\",");
        builder.append("\"pay_account\":");
        builder.append("\"\",");
        builder.append("\"receiver_name\":");
        builder.append("\"" + map.get("receiver_name") + "\",");
        builder.append("\"receiver_province\":");
        builder.append("\"" + map.get("receiver_province") + "\",");
        builder.append("\"receiver_city\":");
        builder.append("\"" + map.get("receiver_city") + "\",");
        builder.append("\"receiver_district\":");
        builder.append("\"" + map.get("receiver_district") + "\",");
        builder.append("\"receiver_address\":");
        builder.append("\"" + map.get("receiver_address") + "\",");
        builder.append("\"receiver_mobile\":");
        builder.append("\"" + map.get("receiver_mobile") + "\",");
        builder.append("\"receiver_telno\": \"\",");
        builder.append("\"receiver_zip\": \"\",");
        builder.append("\"logistics_type\": \"-1\",");
        builder.append("\"invoice_type\": 0,");
        builder.append("\"invoice_title\": \"\",");
        builder.append("\"buyer_message\": \"\",");
        builder.append("\"seller_memo\": \"\",");
        builder.append("\"seller_flag\": \"0\",");
        builder.append("\"post_amount\": ");
        //邮费
        builder.append("\"" + map.get("post_amount") + "\",");
        builder.append("\"cod_amount\": 0,");
        builder.append("\"ext_cod_fee\": 0,");
        //其他金额 demo没有
//        builder.append("\"other_amount\": 0,");
        builder.append("\"paid\": ");
        //已支付金额
        builder.append("" + map.get("paid") + ",");
        
        builder.append("\"order_list\": [");
        for(Map<String, Object> map2 : (List<Map<String, Object>>) map.get("order_list")) {
            builder.append("{\"oid\": ");
            builder.append("\"" + map2.get("oid") + "\",");
            builder.append("\"num\":");
            //购买数量
            builder.append("" + map2.get("num") + ",");
            builder.append("\"price\":");
            //标价,折扣前的价格
            builder.append("" + map2.get("price") + ",");
            builder.append("\"status\": 30,");
            builder.append("\"refund_status\": 0,");
            builder.append("\"goods_id\": \"\",");
            builder.append("\"spec_id\": ");
            //skuId
            builder.append("\"" + map2.get("spec_id") + "\",");
            builder.append("\"goods_no\":\"\",");
            builder.append("\"spec_no\":");
            //skuNo(done)
            builder.append("\"" + map2.get("spec_no") + "\",");
            builder.append("\"goods_name\":");
            //货品名称
            builder.append("\"" + map2.get("goods_name") + "\",");
            builder.append("\"spec_name\": ");
            //规格名称
            builder.append("\"" + map2.get("spec_name") + "\",");
            builder.append("\"adjust_amount\": 0,");
            builder.append("\"discount\": ");
            //下单总折扣
            builder.append("" + map2.get("discount") + ",");
            builder.append("\"share_discount\": ");
            builder.append("" + map2.get("share_discount") + ",");
            builder.append("\"cid\": ");
            builder.append("\"\"");
            builder.append("},");
        }
        builder.append("]}]");
        Map<String, String> result = new HashMap<String, String>();
        
        int index = builder.lastIndexOf(",");
        builder.deleteCharAt(index);
        
        result.put("shop_no", "001");
        result.put("trade_list", builder.toString());
        
        
        return result;
	}

	public List<Map<String ,Object>> assembleQMOrders(Long startTime, Long endTime) {
		List<QMOrder> qmOrders = qMOrderDao.getUnpushedMergedQMOrders(startTime, endTime);

		List<Map<String ,Object>> list = new ArrayList<>();
		for (QMOrder qmOrder : qmOrders) {
			Map<String, Object> map = assembleQMOrder(qmOrder);
			list.add(map);
		}
		
		return list;
	}

	private Map<String, Object> assembleQMOrder(QMOrder qmOrder) {
		Map<String, Object> map = new HashMap<>();
		assembleQMOrderAttr(map, qmOrder);
		
		List<ProductPropValue> colors = (List<ProductPropValue>) propertyService.getPropertyByNameId(PropertyName.COLOR);
		List<ProductPropValue> sizes = (List<ProductPropValue>) propertyService.getPropertyByNameId(PropertyName.SIZE);
		int order_position = 0;
		List<Map<String, Object>> subList = new ArrayList<>();
		double sum_pay = 0.00;
		for (QMOrder order : getRelatedQMOrders(qmOrder)) {
			order_position ++;
			int orderItem_position = 0;
			for (QMOrderItem qmOrderItem : qMOrderItemDao.search(order.getTid())) {
				orderItem_position ++;
				
				double discount = 0.00;
                if (orderItem_position == qMOrderItemDao.search(order.getTid()).size() && order_position == getRelatedQMOrders(qmOrder).size()) {
                	discount = qmOrderItem.getTotalMoney() - (qmOrder.getTotalMoney() - sum_pay);
				} else {
					sum_pay += qmOrderItem.getTotalPay();
					discount = qmOrderItem.getTotalMoney() - qmOrderItem.getTotalPay();
				}
                
				subList.add(assembleQMOrderItem(qmOrderItem, discount, colors, sizes));
			}
		}
		map.put("order_list", subList);
		
		return map;
	}

	private Map<String, Object> assembleQMOrderItem(QMOrderItem qmOrderItem, double discount, List<ProductPropValue> colors, List<ProductPropValue> sizes) {
		ProductSKU productSKU = productSKUService.searchById(qmOrderItem.getSkuId());
		
		Map<String, Object> sub_map = new HashMap<>();
		sub_map.put("oid", qmOrderItem.getOid());
		sub_map.put("num", qmOrderItem.getBuyCount());
		sub_map.put("price", qmOrderItem.getTotalMoney()/qmOrderItem.getBuyCount());
		sub_map.put("spec_id", qmOrderItem.getSkuId());
		sub_map.put("spec_no", productSKU.getSkuNo());
		sub_map.put("goods_name", productSKUService.getProductBySkuId(qmOrderItem.getSkuId()).getName());
		sub_map.put("spec_name", assembleSkuSnapShot(productSKU.getPropertyIds(), colors, sizes));
		sub_map.put("discount", Double.parseDouble(String.format("%.2f", discount)));
		sub_map.put("share_discount", 0);
		
		return sub_map;
	}

	private void assembleQMOrderAttr(Map<String, Object> map, QMOrder qmOrder) {
		map.put("orderNo", "QM" + String.format("%07d", qmOrder.getOrderNo()));
		map.put("trade_time", qmOrder.getCreateTime());
		map.put("pay_time", qmOrder.getCreateTime());
		map.put("buyer_nick", qmOrder.getBuyerNick());
		map.put("buyer_email", "");
		map.put("receiver_name", getQMOrderExpressPartInfo(qmOrder.getExpressInfo(), "name"));
		map.put("receiver_province", getQMOrderExpressPartInfo(qmOrder.getExpressInfo(), "province"));
		map.put("receiver_city", getQMOrderExpressPartInfo(qmOrder.getExpressInfo(), "city"));
		map.put("receiver_district", getQMOrderExpressPartInfo(qmOrder.getExpressInfo(), "district"));
		map.put("receiver_mobile", qmOrder.getMobile());
		map.put("receiver_address", qmOrder.getExpressInfo());
		map.put("paid", sumElement(getRelatedQMOrders(qmOrder), "totalPay"));
		map.put("post_amount", 0.00);
	}

	private Object sumElement(List<QMOrder> qmOrders, String key) {
		switch (key) {
		case "totalPay":
			DecimalFormat df = new DecimalFormat("#.##");
			Double totalPay = 0.00;
			for (QMOrder order : qmOrders) {
				totalPay += order.getTotalPay();
			}
			return df.format(totalPay);
			
		default:
			return null;
		}
	}
	
	private List<QMOrder> getRelatedQMOrders(QMOrder qmOrder) {
		List<QMOrder> qmOrders = new ArrayList<>();
		if (qmOrder.getOrderNo().equals(qmOrder.getMergedId())) {
			qmOrders.add(qmOrder); 
		} else {
			qmOrders = qMOrderDao.search(10, qmOrder.getOrderNo(), null);
		}
		
		return qmOrders;
	}

	private String getQMOrderExpressPartInfo(String expressInfo, String key) {
		switch (key) {
		case "name":
			return expressInfo.split(",")[1];
		case "province":
			return expressInfo.split("-")[0];
		case "city":
			return expressInfo.split("-")[1];
		case "district":
			return expressInfo.split("-")[2];
		default:
			return "";
		}
	}
	
    public Set<Long> pushQMOrders(List<Map<String, Object>> list) {
    	Set<Long> successOrderNos = new HashSet<>();
		for(Map<String, Object> map : list) {
	        String result = WdtSkuUtil.send(AdminConstants.WDT_TRADE_PUSH_URL, assembleErpForm(map));
	        successOrderNos.addAll(handlePushQMOrderResult(map, result));
		}
		
		return successOrderNos;
	}
    
    private Collection<Long> handlePushQMOrderResult(Map<String, Object> map, String result) {
    	Set<Long> successOrderNos = new HashSet<>();
    	JSONObject jsonObject = (JSONObject) JSON.parse(result);
        int code = jsonObject.getInteger("code");
        if (code == 0) {
        	//为了区分普通订单号，千米推送给ERP的订单号形式，如"QM0001234"。故回写时，删除字母。
            long orderNo = Long.parseLong(((String) map.get("orderNo")).substring(2));
            successOrderNos.add(orderNo);
        } else {
         	logger.error("com.jiuy.core.service.task.WdtOrderJob: Fail to push orderNo, " + (String) map.get("orderNo") + ", detail: " + result);
         }
         
         return successOrderNos;
	}
    
	public void syncLogistics(int rec_id, boolean isSuccess) {
		String json = "";
		if(isSuccess) {
			json = "[{\"rec_id\": \"" + rec_id + "\",\"status\": 0,\"message\": \"同步成功\"}]";
		} else {
			json = "[{\"rec_id\": \"" + rec_id + "\",\"status\": 1,\"message\": \"记录不存在\"}]";
		}
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("logistics_list", json);
		WdtSkuUtil.send(AdminConstants.WDT_LOGISTICS_SYN_ACK_URL, map);
	}
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}
}
