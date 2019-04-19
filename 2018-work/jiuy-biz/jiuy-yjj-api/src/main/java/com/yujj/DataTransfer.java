package com.yujj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.yujj.constant.Logistics;
//import com.yujj.entity.logistics.LOPostage;
//import com.yujj.entity.order.OrderItem;
//import com.yujj.exception.order.PostageNotFoundException;

public class DataTransfer {
    public static void main(String[] args) throws SQLException {
//        fixBrandId();
//        buildOrderItemGroup();
//        transferExpressInfo();
//    	transferToOrderNew();
    }

    private static void fixBrandId() throws SQLException {
        DBHelper dbHelper = new DBHelper();
        Map<Long, Map<String, Object>> productMap = new HashMap<Long, Map<String, Object>>();
        List<Map<String, Object>> orderItems = dbHelper.excuteQuery("select * from yjj_OrderItem", new Object[]{});
        for (Map<String, Object> item : orderItems) {
            Long brandId = (Long) item.get("BrandId");
            if (brandId != null && brandId > 0) {
                continue;
            }
            Long productId = (Long) item.get("ProductId");
            Map<String, Object> product = productMap.get(productId);
            if (product == null) {
                product =
                    dbHelper.executeQuerySingle("SELECT`Id`,`NAME`,CategoryId,DetailImages,SummaryImages,`STATUS`,SaleStartTime,SaleEndTime,SaleCurrencyType,SaleTotalCount,SaleMonthlyMaxCount,Price,Favorite,AssessmentCount,ExpressFree,ExpressDetails,CreateTime,UpdateTime, ProductSeq,remark,MarketPrice,SizeTableImage,ClothesNumber,PromotionImage,Weight,BrandId,ShowStatus,BottomPrice,MarketPriceMin,MarketPriceMax,Type,WholeSaleCash,Cash,JiuCoin,RestrictHistoryBuy,RestrictDayBuy,PromotionSetting,PromotionCash,PromotionJiuCoin,PromotionStartTime,PromotionEndTime,RestrictCycle,RestrictHistoryBuyTime,RestrictDayBuyTime,LOWarehouseId,Description,RestrictId,VCategoryId,Together,CartSttstcs,HotSttstcs,SubscriptId,BuyType,DisplayType,PromotionBuyType,PromotionDisplayType,PromotionSaleCount,PromotionVisitCount,LOWarehouseId2,SetLOWarehouseId2,DeductPercent,videoUrl,videoName,vip,videoFileId,state,delState,submitAuditTime,auditTime,auditNoPassReason,upSoldTime,downSoldTime,newTime,skuCount,mainImg,oneCategoryId,oneCategoryName,twoCategoryId,twoCategoryName,threeCategoryId,threeCategoryName,brandName,brandLogo,needAudit,ladderPriceJson,maxLadderPrice,minLadderPrice,supplierId,categoryIds,badge_id,badge_name as badgeName,badge_image as badgeImage,`rank`,brand_type as brandType,last_puton_time as lastPutonTime,vedio_main as vedioMain,memberLevel,member_ladder_price_json as memberLadderPriceJson,member_ladder_price_max as memberLadderPriceMax ,member_ladder_price_min as memberLadderPriceMin  FROM yjj_product WHERE Id = ?", new Object[]{ productId });
                productMap.put(productId, product);
            }
            try {
                Object[] params =
                    new Object[]{ product == null || product.isEmpty() ? 0 : product.get("BrandId"), item.get("Id") };
                dbHelper.executeUpdate("update yjj_OrderItem set BrandId = ? where Id = ?", params);
            } catch (Exception e) {
            }
        }
        dbHelper.close();
    }
    //删除旧表
//    private static void transferExpressInfo() throws SQLException {
//        DBHelper dbHelper = new DBHelper();
//        List<Map<String, Object>> orders = dbHelper.excuteQuery(
//            "select * from yjj_Order where ExpressOrderNo is not null or ExpressOrderNo != ''", new Object[]{});
//        long time = System.currentTimeMillis();
//        for (Map<String, Object> order : orders) {
//            transferExpressInfo(order, time, dbHelper);
//        }
//        dbHelper.close();
//    }
    //删除旧表
//    private static void transferExpressInfo(Map<String, Object> order, long time, DBHelper dbHelper)
//        throws SQLException {
//        Long orderId = (Long) order.get("Id");
//        List<Map<String, Object>> groups =
//            dbHelper.excuteQuery("select Id from yjj_OrderItemGroup where OrderId = ?", new Object[]{ orderId });
//        if (groups.size() != 1) {
//        } else {
//            Map<String, Object> group = groups.get(0);
//            Map<String, Object> countMap = dbHelper.executeQuerySingle(
//                "select COUNT(*) from yjj_ExpressInfo where OrderId = ? and OrderItemGroupId = ?",
//                new Object[]{ orderId, group.get("Id") });
//            if ((long) countMap.get("COUNT(*)") != 0) {
//                return;
//            }
//
//            try {
//                dbHelper.executeUpdate(
//                    "insert into yjj_ExpressInfo (UserId, OrderId, OrderItemGroupId, ExpressSupplier, ExpressOrderNo, ExpressUpdateTime, CreateTime, UpdateTime) values (?, ?, ?, ?, ?, ?, ?, ?)",
//                    new Object[]{ order.get("UserId"), orderId, group.get("Id"), order.get("ExpressSupplier"),
//                                  order.get("ExpressOrderNo"), order.get("ExpressUpdateTime"), time, time });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
    
    //删除旧表
    //旧订单表数据迁移到新订单表，对多包裹订单进行拆分插入，并更新item表OrderNo为新订单OrderNo //CZY
//    private static void transferToOrderNew() throws SQLException {
//        DBHelper dbHelper = new DBHelper();
//       // List<Map<String, Object>> orderItemListTotal = dbHelper.excuteQuery("select * from yjj_OrderItem  where Id<100", new Object[]{});
//        List<Map<String, Object>> orderItemListTotal = dbHelper.excuteQuery("select * from yjj_OrderItem ", new Object[]{});
//        
//        List<Map<String, Object>> orderListTotal = dbHelper.excuteQuery("select * from yjj_Order  order by Id", new Object[]{});
//        
//        List<Map<String, Object>> orderGroupListTotal = dbHelper.excuteQuery("SELECT * FROM yjj_OrderItemGroup ", new Object[]{});
//        
//        
//        //获取全部订单对应item列表
//        Map<Long, List<Map<String, Object>>> orderItemMap = new HashMap<Long, List<Map<String, Object>>>();
//        for (Map<String, Object> orderItem : orderItemListTotal) {
//            Long orderId = (Long) orderItem.get("OrderId");
//            List<Map<String, Object>> list = orderItemMap.get(orderId);
//            if (list == null) {
//                list = new ArrayList<Map<String, Object>>();
//                orderItemMap.put(orderId, list);
//            }
//            list.add(orderItem);
//        }
//        //获取全部订单对应group列表
//        Map<Long, List<Map<String, Object>>> orderGroupMap = new HashMap<Long, List<Map<String, Object>>>();
//        for (Map<String, Object> orderGroup : orderGroupListTotal) {
//        	Long orderId = (Long) orderGroup.get("OrderId");
//        	List<Map<String, Object>> list = orderGroupMap.get(orderId);
//        	if (list == null) {
//        		list = new ArrayList<Map<String, Object>>();
//        		orderGroupMap.put(orderId, list);
//        	}
//        	list.add(orderGroup);
//        }
//        
//        double totalMoney = 0;
//        double lOWarehouseId = 0;
//        
//        int insertNo = 0;
//        int insertNoMother = 0;
//
//        
//        String sql = "insert into yjj_OrderNew (UserId, OrderStatus, TotalMoney, TotalPay, TotalExpressMoney, ExpressInfo, CoinUsed, Remark, Status, Platform, PlatformVersion, Ip, ParentId, PaymentNo, PaymentType, LOWarehouseId, CreateTime, UpdateTime) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        String itemSqlId = "update yjj_OrderItem set orderNo=? where Id = ? ";
//        String itemSqlOrderId = "update yjj_OrderItem set orderNo=? where OrderId = ? ";
//        for (Map<String, Object> order : orderListTotal) {
//        	if ((long)order.get("Id") % 100 == 0) {
//			}
//        	List<Map<String, Object>> orderGroupList = orderGroupMap.get(order.get("Id"));
//        	List<Map<String, Object>> orderItemList = orderItemMap.get(order.get("Id"));
//        	if(orderGroupList!=null&&orderGroupList.size()>0&&orderItemList!=null&&orderItemList.size()>0){
//        		
//        		totalMoney = 0;
//        		lOWarehouseId = 0;
//        		for (Map<String, Object> orderItem : orderItemList) {
//        			totalMoney += Double.parseDouble(orderItem.get("TotalMoney").toString());
//        			lOWarehouseId = (long)(orderItem.get("LOWarehouseId")==null ? 0 : orderItem.get("LOWarehouseId"));
//        		}
//        		//discount = "";
//        		
//        		if(orderGroupList.size() > 1 &&(int)order.get("OrderStatus")!=100&&(int)order.get("OrderStatus") != 0 ){
//        			//需要拆分子订单
//        			
//        			insertNoMother =	dbHelper.executeInsert(sql, new Object[]{ order.get("UserId"), order.get("OrderStatus"),  totalMoney,  order.get("TotalMoney"), order.get("TotalExpressMoney"), order.get("ExpressInfo"), order.get("UnavalCoinUsed")==null ? 0 :order.get("UnavalCoinUsed"), 
//        					order.get("Remark"), order.get("Status"), order.get("Platform"), order.get("PlatformVersion"), order.get("Ip"), -1, order.get("PaymentNo"), order.get("PaymentType"), 0,
//        					order.get("CreateTime"), order.get("UpdateTime") });
//        			for (Map<String, Object> orderGroup : orderGroupList) {
//        				//生成子订单
//        				insertNo = dbHelper.executeInsert(sql, new Object[]{ order.get("UserId"), order.get("OrderStatus"),  orderGroup.get("TotalMoney"),  orderGroup.get("TotalPay")==null ? 0 : orderGroup.get("TotalPay"), orderGroup.get("TotalExpressMoney"), order.get("ExpressInfo"), orderGroup.get("TotalUnavalCoinUsed")==null ? 0 :orderGroup.get("TotalUnavalCoinUsed"), 
//        		        		order.get("Remark"), order.get("Status"), order.get("Platform"), order.get("PlatformVersion"), order.get("Ip"), insertNoMother, order.get("PaymentNo"), order.get("PaymentType"), orderGroup.get("LOWarehouseId")==null ? 0 :orderGroup.get("LOWarehouseId"),
//        		        		order.get("CreateTime"), order.get("UpdateTime") });
//        				for (Map<String, Object> orderItem : orderItemList) {
//        					if((long)orderItem.get("GroupId") == (long)orderGroup.get("Id") ){
//        						dbHelper.executeUpdate(itemSqlId, new Object[]{ insertNo, orderItem.get("Id")});
//        					}
//        					
//
//        				}
//        			}
//        			
//        			
//        			
//        		}else if(orderGroupList.size() == 1&&(int)order.get("OrderStatus") != 100&&(int)order.get("OrderStatus") != 0){
//        			//不需要拆分子订单
//        			
//        			insertNo = dbHelper.executeInsert(sql, new Object[]{ order.get("UserId"), order.get("OrderStatus"),  totalMoney,  order.get("TotalMoney"), order.get("TotalExpressMoney"), order.get("ExpressInfo"), order.get("UnavalCoinUsed")==null ? 0 :order.get("UnavalCoinUsed"), 
//        					order.get("Remark"), order.get("Status"), order.get("Platform"), order.get("PlatformVersion"), order.get("Ip"), -2, order.get("PaymentNo"), order.get("PaymentType"), lOWarehouseId,
//        					order.get("CreateTime"), order.get("UpdateTime") });
//        			
//        			 dbHelper.executeUpdate(itemSqlOrderId, new Object[]{ insertNo, order.get("Id")});
//
//        			
//        		}else {
//        			//处理订单状态为0或者100的订单
//        			
//        			insertNo = dbHelper.executeInsert(sql, new Object[]{ order.get("UserId"), order.get("OrderStatus"),  totalMoney,  order.get("TotalMoney"), order.get("TotalExpressMoney"), order.get("ExpressInfo"), order.get("UnavalCoinUsed")==null ? 0 :order.get("UnavalCoinUsed"), 
//        					order.get("Remark"), order.get("Status"), order.get("Platform"), order.get("PlatformVersion"), order.get("Ip"), 0, order.get("PaymentNo"), order.get("PaymentType"), 0,
//        					order.get("CreateTime"), order.get("UpdateTime") });
//        			dbHelper.executeUpdate(itemSqlOrderId, new Object[]{ insertNo, order.get("Id")});
//        			
//        		}
//        	}
//        }
//        //批量更新不需要拆单的parentId为自身orderNo
//         sql = "update yjj_OrderNew set ParentId = OrderNo where ParentId = -2";
//        
//         int updateRows = dbHelper.executeUpdate(sql);
//        
//        
// 
//  
//        
//
//
////        Map<Long, Map<Long, List<Map<String, Object>>>> orderBrandMap =
////            new HashMap<Long, Map<Long, List<Map<String, Object>>>>();
////        for (Map.Entry<Long, List<Map<String, Object>>> entry : map.entrySet()) {
////            orderBrandMap.put(entry.getKey(), splitAsBrandMap(entry.getValue()));
////        }
////
////        for (Map.Entry<Long, Map<Long, List<Map<String, Object>>>> entry : orderBrandMap.entrySet()) {
////            Long orderId = entry.getKey();
////            Map<Long, List<Map<String, Object>>> brandMap = entry.getValue();
////            for (Map.Entry<Long, List<Map<String, Object>>> brandEntry : brandMap.entrySet()) {
////                Long brandId = brandEntry.getKey();
////                buildOrderItemGroup(orderId, brandId, brandEntry.getValue(), dbHelper);
////            }
////        }
//        dbHelper.close();
//    }
    
    
    
    
//删除旧表
//    private static void buildOrderItemGroup() throws SQLException {
//        DBHelper dbHelper = new DBHelper();
//        List<Map<String, Object>> orderItems = dbHelper.excuteQuery("select * from yjj_OrderItem", new Object[]{});
//        Map<Long, List<Map<String, Object>>> map = new HashMap<Long, List<Map<String, Object>>>();
//        for (Map<String, Object> orderItem : orderItems) {
//            Long orderId = (Long) orderItem.get("OrderId");
//            List<Map<String, Object>> list = map.get(orderId);
//            if (list == null) {
//                list = new ArrayList<Map<String, Object>>();
//                map.put(orderId, list);
//            }
//            list.add(orderItem);
//        }
//
//        Map<Long, Map<Long, List<Map<String, Object>>>> orderBrandMap =
//            new HashMap<Long, Map<Long, List<Map<String, Object>>>>();
//        for (Map.Entry<Long, List<Map<String, Object>>> entry : map.entrySet()) {
//            orderBrandMap.put(entry.getKey(), splitAsBrandMap(entry.getValue()));
//        }
//
//        for (Map.Entry<Long, Map<Long, List<Map<String, Object>>>> entry : orderBrandMap.entrySet()) {
//            Long orderId = entry.getKey();
//            Map<Long, List<Map<String, Object>>> brandMap = entry.getValue();
//            for (Map.Entry<Long, List<Map<String, Object>>> brandEntry : brandMap.entrySet()) {
//                Long brandId = brandEntry.getKey();
//                buildOrderItemGroup(orderId, brandId, brandEntry.getValue(), dbHelper);
//            }
//        }
//        dbHelper.close();
//    }
    //删除旧表
//    private static void buildOrderItemGroup(Long orderId, Long brandId, List<Map<String, Object>> items,
//                                            DBHelper dbHelper)
//        throws SQLException {
//        Map<String, Object> countMap =
//            dbHelper.executeQuerySingle("select COUNT(*) from yjj_OrderItemGroup where OrderId = ? and BrandId = ?",
//                new Object[]{ orderId, brandId });
//        if ((long) countMap.get("COUNT(*)") != 0) {
//            return;
//        }
//
//        if (items.size() > 1) {
//        }
//        int totalMoney = 0;
//        for (Map<String, Object> tmp : items) {
//            totalMoney += (int) tmp.get("TotalMoney");
//        }
//        Map<String, Object> item = items.get(0);
//        String sql =
//            "insert into yjj_OrderItemGroup (OrderId, UserId, BrandId, Status, CreateTime, UpdateTime, TotalMoney, TotalExpressMoney) values (?, ?, ?, ?, ?, ?, ?, ?)";
//        dbHelper.executeUpdate(sql, new Object[]{ orderId, item.get("UserId"), brandId, item.get("Status"),
//                                                  item.get("CreateTime"), item.get("UpdateTime"), totalMoney, 0 });
//    }

    private static Map<Long, List<Map<String, Object>>> splitAsBrandMap(List<Map<String, Object>> items) {
        Map<Long, List<Map<String, Object>>> result = new HashMap<Long, List<Map<String, Object>>>();
        for (Map<String, Object> item : items) {
            Long brandId = (Long) item.get("BrandId");
            List<Map<String, Object>> list = result.get(brandId);
            if (list == null) {
                list = new ArrayList<Map<String, Object>>();
                result.put(brandId, list);
            }
            list.add(item);
        }
        return result;
    }

    static class DBHelper {
        private static final String DRIVER = "com.mysql.jdbc.Driver";

        private static final String URLSTR =
            "jdbc:mysql://121.43.232.149:3306/yjj?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true";
//        private static final String URLSTR =
//        		"jdbc:mysql://192.168.1.3:3306/yjj?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true";

        private static final String USERNAME = "mysql";

        private static final String USERPASSWORD = "daf24fzirewafarqgwba131";

        private Connection connnection = null;

        static {
            try {
                Class.forName(DRIVER);
            } catch (ClassNotFoundException e) {
            }
        }

        public Connection getConnection() {
            if (connnection != null) {
                return connnection;
            }

            try {
                connnection = DriverManager.getConnection(URLSTR, USERNAME, USERPASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return connnection;
        }
        

        public int executeInsert(String sql, Object[] params) throws SQLException {
            PreparedStatement preparedStatement = null;
            int id = 0;
            try {                
                connnection = this.getConnection();
                preparedStatement = connnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        preparedStatement.setObject(i + 1, params[i]);
                    }
                }
                preparedStatement.execute();
                ResultSet rs = preparedStatement.getGeneratedKeys();

                if (rs != null && rs.next()) {
                	id = rs.getInt(1);
                }
                preparedStatement.close();
            }catch (Exception e) {
            	e.printStackTrace();
            }
            
            finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (Exception e) {
                    }
                }
            }

            return id;
        }

        public int executeUpdate(String sql, Object[] params) throws SQLException {
            PreparedStatement preparedStatement = null;
            try {
                connnection = this.getConnection();
                preparedStatement = connnection.prepareStatement(sql);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        preparedStatement.setObject(i + 1, params[i]);
                    }
                }
                int count = preparedStatement.executeUpdate();
                preparedStatement.close();
                return count;
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        
        public int executeUpdate(String sql) throws SQLException {
        	PreparedStatement preparedStatement = null;
        	try {
        		connnection = this.getConnection();
        		preparedStatement = connnection.prepareStatement(sql);
        		
        		int count = preparedStatement.executeUpdate();
        		preparedStatement.close();
        		return count;
        	} finally {
        		if (preparedStatement != null) {
        			try {
        				preparedStatement.close();
        			} catch (Exception e) {
        			}
        		}
        	}
        }

        public Map<String, Object> executeQuerySingle(String sql, Object[] params) throws SQLException {
            List<Map<String, Object>> list = excuteQuery(sql, params);
            return list.isEmpty() ? new HashMap<String, Object>() : list.get(0);
        }

        public List<Map<String, Object>> excuteQuery(String sql, Object[] params) throws SQLException {
            PreparedStatement preparedStatement = null;
            ResultSet rs = null;
            try {
                connnection = this.getConnection();
                preparedStatement = connnection.prepareStatement(sql);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        preparedStatement.setObject(i + 1, params[i]);
                    }
                }
                rs = preparedStatement.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    for (int i = 1; i <= columnCount; i++) {
                        map.put(rsmd.getColumnLabel(i), rs.getObject(i));
                    }
                    list.add(map);
                }
                return list;
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                    }
                }
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (Exception e) {
                    }
                }
            }
        }

        public void close() {
            if (connnection != null) {
                try {
                    connnection.close();
                } catch (SQLException e) {
                }
            }
        }
    }

}
