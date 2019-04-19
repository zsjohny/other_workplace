package com.yujj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.yujj.constant.Logistics;
//import com.yujj.entity.account.YJJNumber;
//import com.yujj.entity.logistics.LOPostage;
//import com.yujj.entity.order.OrderItem;
//import com.yujj.exception.order.PostageNotFoundException;
//import com.yujj.util.DateUtil;

public class BatchYJJNumber {
    public static void main(String[] args) throws SQLException {
//        fixBrandId();
//        buildOrderItemGroup();
//        transferExpressInfo();
    	
    	DBHelper dbHelper = new DBHelper();
    	int remainUserNum = dbHelper.executeQueryInt("select count(1) from yjj_User where YJJNumber is null ");
    	int times = ( remainUserNum / 10000 ) + 1 ;
    	if(remainUserNum == 0){
    		times = 0;
    	}
    	//for(int i = 1 ; i <= times ; i++ ){
    	for(int i = 1 ; i <= times ; i++ ){
    		BatchAddYjjNumber();
		}
//    	try {
//    		long nowTime = System.currentTimeMillis();
//    		
//			long startTime = DateUtil.parseStrTime2Long("2017-02-14 00:00:00");
//			long endTime = DateUtil.parseStrTime2Long("2017-02-28 23:59:59");
//			System.out.println("startTime"+startTime);
//			System.out.println("endTime"+endTime);
//			System.out.println("nowTime"+nowTime);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		startTime = DateUtil.parseStrTime2Long("2017-02-20 00:00:00");
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
                        //"SELECT`Id`,`NAME`,CategoryId,DetailImages,SummaryImages,`STATUS`,SaleStartTime,SaleEndTime,SaleCurrencyType,SaleTotalCount,SaleMonthlyMaxCount,Price,Favorite,AssessmentCount,ExpressFree,ExpressDetails,CreateTime,UpdateTime,ProductSeq,remark,MarketPrice,SizeTableImage,ClothesNumber,PromotionImage,Weight,BrandId,ShowStatus,BottomPrice,MarketPriceMin,MarketPriceMax,Type,WholeSaleCash,Cash,JiuCoin,RestrictHistoryBuy,RestrictDayBuy,PromotionSetting,PromotionCash,PromotionJiuCoin,PromotionStartTime,PromotionEndTime,RestrictCycle,RestrictHistoryBuyTime,RestrictDayBuyTime,LOWarehouseId,Description,RestrictId,VCategoryId,Together,CartSttstcs,HotSttstcs,SubscriptId,BuyType,DisplayType,PromotionBuyType,PromotionDisplayType,PromotionSaleCount,PromotionVisitCount,LOWarehouseId2,SetLOWarehouseId2,DeductPercent,videoUrl,videoName,vip,videoFileId,state,delState,submitAuditTime,auditTime,auditNoPassReason,upSoldTime,downSoldTime,newTime,skuCount,mainImg,oneCategoryId,oneCategoryName,twoCategoryId,twoCategoryName,threeCategoryId,threeCategoryName,brandName,randLogo,needAudit,ladderPriceJson,maxLadderPrice,minLadderPrice,supplierId,categoryIds,badge_id,badge_name,badge_image,`rank`,brand_type,last_puton_time,vedio_main,memberLevel,member_ladder_price_json,member_ladder_price_max,member_ladder_price_min FROM yjj_product WHERE Id = 3"
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
    //删除
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

    
    private static void BatchAddYjjNumber() throws SQLException {
    	DBHelper dbHelper = new DBHelper();
    	List<Map<String, Object>> yjjNumberListTotal = new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> yjjNumberListTotal1 = new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> yjjNumberListTotal2 = new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> yjjNumberListTotal3 = new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> yjjNumberListTotal4 = new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> yjjNumberListTotal5 = new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> yjjNumberListTotal6 = new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> yjjNumberListTotal7 = new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> yjjNumberListTotal8 = new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> yjjNumberListTotal9 = new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> yjjNumberListTotal10 = new ArrayList<Map<String, Object>>();
    	
    	List<Map<String, Object>> userListTotal = new ArrayList<Map<String, Object>>();
    	
    	int remainUserNum = 0;
    	if(yjjNumberListTotal.size() == 0 && userListTotal.size() == 0){
    		
    		remainUserNum = dbHelper.executeQueryInt("select count(1) from yjj_User where YJJNumber is null ");
    		
    		if(remainUserNum >= 10000){
    			
    			userListTotal = dbHelper.excuteQuery("select * from yjj_User where YJJNumber is null  order by UserId limit 10000", new Object[]{});
    		} else if(remainUserNum > 0){
    			userListTotal = dbHelper.excuteQuery("select * from yjj_User where YJJNumber is null  order by UserId limit "+remainUserNum, new Object[]{});
    			
    		}
    		if(remainUserNum > 0){
    			
    			yjjNumberListTotal1 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 0  and Id <=100000 order by Id limit 1000 ", new Object[]{});
    			yjjNumberListTotal2 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 100000 and Id <=200000 order by Id limit 1000 ", new Object[]{});
    			yjjNumberListTotal3 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 200000 and Id <=300000 order by Id limit 1000 ", new Object[]{});
    			yjjNumberListTotal4 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 300000 and Id <=400000 order by Id limit 1000 ", new Object[]{});
    			yjjNumberListTotal5 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 400000 and Id <=500000 order by Id limit 1000 ", new Object[]{});
    			yjjNumberListTotal6 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 500000 and Id <=600000 order by Id limit 1000 ", new Object[]{});
    			yjjNumberListTotal7 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 600000 and Id <=700000 order by Id limit 1000 ", new Object[]{});
    			yjjNumberListTotal8 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 700000 and Id <=800000 order by Id limit 1000 ", new Object[]{});
    			yjjNumberListTotal9 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 800000 and Id <=900000 order by Id limit 1000 ", new Object[]{});
    			yjjNumberListTotal10 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 900000 and Id <=1000000 order by Id limit 1000 ", new Object[]{});
//    			yjjNumberListTotal1 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 0  order by Id limit 1000 ", new Object[]{});
//    			yjjNumberListTotal2 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 100000  order by Id limit 1000 ", new Object[]{});
//    			yjjNumberListTotal3 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 200000  order by Id limit 1000 ", new Object[]{});
//    			yjjNumberListTotal4 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 300000  order by Id limit 1000 ", new Object[]{});
//    			yjjNumberListTotal5 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 400000  order by Id limit 1000 ", new Object[]{});
//    			yjjNumberListTotal6 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 500000  order by Id limit 1000 ", new Object[]{});
//    			yjjNumberListTotal7 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 600000  order by Id limit 1000 ", new Object[]{});
//    			yjjNumberListTotal8 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 700000  order by Id limit 1000 ", new Object[]{});
//    			yjjNumberListTotal9 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 800000  order by Id limit 1000 ", new Object[]{});
//    			yjjNumberListTotal10 = dbHelper.excuteQuery("select * from yjj_Number where Status = 0 and Id > 900000  order by Id limit 1000 ", new Object[]{});
    			
    			yjjNumberListTotal.addAll(yjjNumberListTotal10);
    			yjjNumberListTotal.addAll(yjjNumberListTotal9);
    			yjjNumberListTotal.addAll(yjjNumberListTotal8);
    			yjjNumberListTotal.addAll(yjjNumberListTotal7);
    			yjjNumberListTotal.addAll(yjjNumberListTotal6);
    			yjjNumberListTotal.addAll(yjjNumberListTotal5);
    			yjjNumberListTotal.addAll(yjjNumberListTotal4);
    			yjjNumberListTotal.addAll(yjjNumberListTotal3);
    			yjjNumberListTotal.addAll(yjjNumberListTotal2);
    			yjjNumberListTotal.addAll(yjjNumberListTotal1);
    			
    			if(remainUserNum < 10000){
    				for(int i = 0 ; i < 10000 - remainUserNum ; i++ ){
    					yjjNumberListTotal.remove(0);
    				}
    			} 
    		}
    		
    	}
    	if(remainUserNum > 0){
    		//    	
	    	StringBuilder userSqlBatch1= new StringBuilder();
	    	StringBuilder numberBatch= new StringBuilder();
	    	userSqlBatch1.append("update yjj_User set YJJNumber = CASE UserId ");
	    	numberBatch.append("update yjj_Number set Status = -2 where Id in ( ");
	    	int randomNum = 0;
	    	String number = "";
	    	String userSql = "update yjj_User set YJJNumber=? where UserId = ? ";
	//    	String userSqlBatch2 = "update yjj_User set YJJNumber=? where UserId = ? ";
	    	String numberSql = "update yjj_Number set Status=? where Id = ? ";
	    	 
	    	for (Map<String, Object> yjjNumber : yjjNumberListTotal) {
	    		if ((long)yjjNumber.get("Id") % 1000 == 0) {
				}
	    		numberBatch.append(" "+yjjNumber.get("Id")+" ,");
	    	}
	    	
	    	numberSql = numberBatch.toString();
	    	numberSql = numberSql.substring(0, numberSql.length()-1);
	    	numberSql += ")";
	    	//处理yjj number为已使用
	    	dbHelper.executeUpdate(numberSql, new Object[]{}); 		
	    	for (Map<String, Object> user : userListTotal) {
	    		randomNum = (int) (Math.random() * yjjNumberListTotal.size()) ;
	    		number = (String) yjjNumberListTotal.get(randomNum).get("Number");
	    		user.put("YJJNumber", number);
	    		yjjNumberListTotal.remove(randomNum);
	    		
	    		userSqlBatch1.append(" WHEN "+user.get("UserId")+" THEN "+number);
	    		
	    		//dbHelper.executeUpdate(userSql, new Object[]{ user.get("YJJNumber"), user.get("UserId")});
	    		if ((long)user.get("UserId") % 5000 == 0) {
				}
	    	}
	    	userSqlBatch1.append(" END ");
	    	userSqlBatch1.append("where UserId in ( ");
	    	for (Map<String, Object> user : userListTotal) {
	    		userSqlBatch1.append(" "+user.get("UserId")+" ,");
	
	    	}
	    	String batchSql = userSqlBatch1.toString();
	    	batchSql = batchSql.substring(0, batchSql.length()-1);
	    	batchSql += ")";
	    	//更新user表YJJNumber
	    	dbHelper.executeUpdate(batchSql, new Object[]{});
    	}
    	dbHelper.close();
    }
    
    
    
    
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

//        private static final String URLSTR =
//            "jdbc:mysql://121.43.232.149:3306/yjj_online?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true";
        private static final String URLSTR =
        		"jdbc:mysql://192.168.1.3:3306/yjj?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true";

//        private static final String USERNAME = "jiuyuan";
//
//        private static final String USERPASSWORD = "daf24fzirewafarqgwba131";
        
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
        
        public int executeQueryInt(String sql) throws SQLException {
        	PreparedStatement preparedStatement = null;
        	try {
        		connnection = this.getConnection();
        		preparedStatement = connnection.prepareStatement(sql);
        		
        		 ResultSet rs = preparedStatement.executeQuery();
        	
        		 rs.next();
        		 int count = rs.getInt(1);
        	
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
