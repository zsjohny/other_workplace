package com.yujj.business.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.FloorType;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.MemcachedService;
//import com.store.entity.HomeFloorVO;
import com.yujj.dao.mapper.CouponTemplateMapper;
import com.yujj.dao.mapper.HomeFloorVOMapper;
import com.yujj.dao.mapper.OrderCouponMapper;
import com.yujj.dao.mapper.ProductMapper;
import com.yujj.entity.homepage.HomeFloorVO;
import com.yujj.entity.order.CouponTemplate;
import com.yujj.entity.product.Product;
//import com.yujj.web.controller.delegate.OrderDelegator;

@Service
public class HomeFloorService {

	@Autowired
	private HomeFloorVOMapper homeFloorMap;
	
    @Autowired
    private MemcachedService memcachedService;
    
    @Autowired
    private ProductMapper productMapper;
		
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderCouponMapper orderCouponMapper;
    
    @Autowired
    private CouponTemplateMapper couponTemplateMapper;
    
	@SuppressWarnings("unchecked")
	public List<HomeFloorVO> getHomeFloors(PageQuery pageQuery) {
        String groupKey = MemcachedKey.GROUP_KEY_HOME_FLOOR_185;
        
        String key = pageQuery.getPage() + "";
        Object obj = memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	return (List<HomeFloorVO>) obj;
        } else {
        	List<HomeFloorVO> homeFloors = homeFloorMap.getHomeFloors185(pageQuery);
        	String content = "";
        	
        	//String regEx = "(" + imgHead + "[^>^http:]*"+ ".[a-z_A-Z_]*)";
        	String regEx = "\"id\":\"([0-9_]*)\",\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*\"cash\":\"[0-9.]*\",\"jiuCoin\":\"[0-9.]*\"";
        	//String str = "[{\"banner\":{\"bannerImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684050000641468405000064.jpg\",\"skipType\":\"4\",\"skipContent\":\"40\",\"categoryName\":\"上装\"},\"seeMore\":{\"bannerImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684050117301468405011730.png\",\"skipType\":\"4\",\"skipContent\":\"40\",\"categoryName\":\"上装\"},\"products\":[{\"promotionImage\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684046658361468404665836.png\",\"id\":\"1854\",\"clothesNum\":\"15X239\",\"name\":\"撞色拼接连体裤\",\"marketPrice\":\"429\",\"marketPriceMin\":\"0\",\"marketPriceMax\":\"0\",\"cash\":\"43\",\"jiuCoin\":\"43\",\"price\":\"0\",\"payAmountInCents\":\"0\",\"defaultImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14676218577491467621857749.jpg\"},{\"promotionImage\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684047091721468404709172.png\",\"id\":\"1827\",\"clothesNum\":\"16X162\",\"name\":\"针织长T\",\"marketPrice\":\"429\",\"marketPriceMin\":\"0\",\"marketPriceMax\":\"0\",\"cash\":\"43\",\"jiuCoin\":\"43\",\"price\":\"0\",\"payAmountInCents\":\"0\",\"defaultImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14671829224751467182922475.JPG\"},{\"promotionImage\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684047795161468404779516.png\",\"id\":\"1773\",\"clothesNum\":\"6037\",\"name\":\"立体字母时尚T\",\"marketPrice\":\"459\",\"marketPriceMin\":\"0\",\"marketPriceMax\":\"0\",\"cash\":\"46\",\"jiuCoin\":\"46\",\"price\":\"0\",\"payAmountInCents\":\"0\",\"defaultImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14663266991871466326699187.JPG\"},{\"promotionImage\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684047448211468404744821.png\",\"id\":\"1837\",\"clothesNum\":\"16X172\",\"name\":\"圆领短T\",\"marketPrice\":\"269\",\"marketPriceMin\":\"0\",\"marketPriceMax\":\"0\",\"cash\":\"27\",\"jiuCoin\":\"27\",\"price\":\"0\",\"payAmountInCents\":\"0\",\"defaultImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14672670125021467267012502.jpg\"},{\"promotionImage\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684048247601468404824760.png\",\"id\":\"1669\",\"clothesNum\":\"SYM4852503\",\"name\":\"个性拼接精美小衫\",\"marketPrice\":\"319\",\"marketPriceMin\":\"0\",\"marketPriceMax\":\"0\",\"cash\":\"32\",\"jiuCoin\":\"32\",\"price\":\"0\",\"payAmountInCents\":\"0\",\"defaultImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14663194624011466319462401.jpg\"}]}]";    
        	Pattern pat = Pattern.compile(regEx);
        	Matcher mat;
      
        	Set<Long> productIdSet = new HashSet<Long>();
        	
        	List<Long> idList = new ArrayList<Long>(); 
        	List<String> proStrList = new ArrayList<String>(); 
    	
        	
        	if(homeFloors != null && homeFloors.size() >0){
        		
        		for(HomeFloorVO homeFloor : homeFloors ){
        			content = homeFloor.getContent();
        			mat = pat.matcher(content);
            		while (mat.find()) {
            			productIdSet.add(Long.parseLong(mat.group(1)));       			
            		}
	
        		}
        		
        		if(productIdSet != null && productIdSet.size() > 0 ){
        			
        			Map<Long, Product> productMap = productMapper.getProductByIds(productIdSet);
    				for(HomeFloorVO homeFloor : homeFloors ){
    					
    					content = homeFloor.getContent();
    					proStrList =  new ArrayList<String>(); 
    					idList =  new ArrayList<Long>(); 
    					mat = pat.matcher(content);
    					while (mat.find()) {
    						proStrList.add(mat.group());
    						idList.add(Long.parseLong(mat.group(1)));
    					}
    					for(int i = 0; i < proStrList.size(); i++ ){
    						if(productMap.get(idList.get(i)) != null){
    							
    							content = content.replaceAll("(\"id\":\"" + idList.get(i) + "\",\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*\"cash\":\")[0-9.]*(\",\"jiuCoin\":\"[0-9.]*\")", "$1" + (int)productMap.get(idList.get(i)).getCash() + "$2");
    							content = content.replaceAll("(\"id\":\"" + idList.get(i) + "\",\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*,\"jiuCoin\":\")[0-9.]*(\")", "$1" + productMap.get(idList.get(i)).getJiuCoin() + "$2");
    						}
    						
    					}
    					homeFloor.setContent(content);
    				}
        		}
        	}
        	
        	
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, homeFloors);
        	
        	return homeFloors;
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<HomeFloorVO> getHomeFloorsBefore185(PageQuery pageQuery) {
		String groupKey = MemcachedKey.GROUP_KEY_HOME_FLOOR;
		
		String key = pageQuery.getPage() + "";
		Object obj = memcachedService.get(groupKey, key);
		
		if (obj != null) {
			return (List<HomeFloorVO>) obj;
		} else {
			List<HomeFloorVO> homeFloors = homeFloorMap.getHomeFloors(pageQuery);
			Iterator<HomeFloorVO> stuIter = homeFloors.iterator();  
		    while (stuIter.hasNext()) {  
		    	HomeFloorVO homeFloor = stuIter.next();  
		        if (homeFloor.getTemplateName() != null && homeFloor.getTemplateName().length() > 0) {
		        	String numStr = homeFloor.getTemplateName().replaceAll("模板", "");
		        	try {
						if(Integer.parseInt(numStr) > 12){
							stuIter.remove();//这里要使用Iterator的remove方法移除当前对象
						}
					} catch (Exception e) {
						//System.err.println("模板号转数字出错");
						stuIter.remove();//模板号转数字出错也移除
					}
		        }
		    }  
			String content = "";
			
			//String regEx = "(" + imgHead + "[^>^http:]*"+ ".[a-z_A-Z_]*)";
			String regEx = "\"id\":\"([0-9_]*)\",\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*\"cash\":\"[0-9.]*\",\"jiuCoin\":\"[0-9.]*\"";
			//String str = "[{\"banner\":{\"bannerImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684050000641468405000064.jpg\",\"skipType\":\"4\",\"skipContent\":\"40\",\"categoryName\":\"上装\"},\"seeMore\":{\"bannerImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684050117301468405011730.png\",\"skipType\":\"4\",\"skipContent\":\"40\",\"categoryName\":\"上装\"},\"products\":[{\"promotionImage\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684046658361468404665836.png\",\"id\":\"1854\",\"clothesNum\":\"15X239\",\"name\":\"撞色拼接连体裤\",\"marketPrice\":\"429\",\"marketPriceMin\":\"0\",\"marketPriceMax\":\"0\",\"cash\":\"43\",\"jiuCoin\":\"43\",\"price\":\"0\",\"payAmountInCents\":\"0\",\"defaultImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14676218577491467621857749.jpg\"},{\"promotionImage\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684047091721468404709172.png\",\"id\":\"1827\",\"clothesNum\":\"16X162\",\"name\":\"针织长T\",\"marketPrice\":\"429\",\"marketPriceMin\":\"0\",\"marketPriceMax\":\"0\",\"cash\":\"43\",\"jiuCoin\":\"43\",\"price\":\"0\",\"payAmountInCents\":\"0\",\"defaultImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14671829224751467182922475.JPG\"},{\"promotionImage\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684047795161468404779516.png\",\"id\":\"1773\",\"clothesNum\":\"6037\",\"name\":\"立体字母时尚T\",\"marketPrice\":\"459\",\"marketPriceMin\":\"0\",\"marketPriceMax\":\"0\",\"cash\":\"46\",\"jiuCoin\":\"46\",\"price\":\"0\",\"payAmountInCents\":\"0\",\"defaultImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14663266991871466326699187.JPG\"},{\"promotionImage\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684047448211468404744821.png\",\"id\":\"1837\",\"clothesNum\":\"16X172\",\"name\":\"圆领短T\",\"marketPrice\":\"269\",\"marketPriceMin\":\"0\",\"marketPriceMax\":\"0\",\"cash\":\"27\",\"jiuCoin\":\"27\",\"price\":\"0\",\"payAmountInCents\":\"0\",\"defaultImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14672670125021467267012502.jpg\"},{\"promotionImage\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14684048247601468404824760.png\",\"id\":\"1669\",\"clothesNum\":\"SYM4852503\",\"name\":\"个性拼接精美小衫\",\"marketPrice\":\"319\",\"marketPriceMin\":\"0\",\"marketPriceMax\":\"0\",\"cash\":\"32\",\"jiuCoin\":\"32\",\"price\":\"0\",\"payAmountInCents\":\"0\",\"defaultImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14663194624011466319462401.jpg\"}]}]";    
			Pattern pat = Pattern.compile(regEx);
			Matcher mat;
			
			Set<Long> productIdSet = new HashSet<Long>();
			
			List<Long> idList = new ArrayList<Long>(); 
			List<String> proStrList = new ArrayList<String>(); 
			
			
			if(homeFloors != null && homeFloors.size() >0){
				
				for(HomeFloorVO homeFloor : homeFloors ){
					content = homeFloor.getContent();
					mat = pat.matcher(content);
					while (mat.find()) {
						productIdSet.add(Long.parseLong(mat.group(1)));       			
					}
					
				}
				if(productIdSet != null && productIdSet.size() > 0 ){
					
					Map<Long, Product> productMap = productMapper.getProductByIds(productIdSet);
					
					for(HomeFloorVO homeFloor : homeFloors ){
						
						content = homeFloor.getContent();
						proStrList =  new ArrayList<String>(); 
						idList =  new ArrayList<Long>(); 
						mat = pat.matcher(content);
						while (mat.find()) {
							proStrList.add(mat.group());
							idList.add(Long.parseLong(mat.group(1)));
						}
						for(int i = 0; i < proStrList.size(); i++ ){
							if(productMap.get(idList.get(i)) != null){								
								content = content.replaceAll("(\"id\":\"" + idList.get(i) + "\",\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*\"cash\":\")[0-9.]*(\",\"jiuCoin\":\"[0-9.]*\")", "$1" + (int)productMap.get(idList.get(i)).getCash() + "$2");
								content = content.replaceAll("(\"id\":\"" + idList.get(i) + "\",\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*,\"jiuCoin\":\")[0-9.]*(\")", "$1" + productMap.get(idList.get(i)).getJiuCoin() + "$2");
							}
							
						}
						homeFloor.setContent(content);
					}
				}
				
			}
			
			memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, homeFloors);
			
			return homeFloors;
		}
	}

	public int getHomeFloorCount() {
		String groupKey = MemcachedKey.GROUP_KEY_HOME_FLOOR;
        
        String key = "home_floor_count";
        Object obj = memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	return (int) obj;
        } else {
        	int count = homeFloorMap.getHomeFloorCount();	
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, count);
        	
        	return count;
        }
		
	}
	
	public int getHomeFloorCount185() {
		String groupKey = MemcachedKey.GROUP_KEY_HOME_FLOOR_185;
		
		String key = "home_floor_count";
		Object obj = memcachedService.get(groupKey, key);
		
		if (obj != null) {
			return (int) obj;
		} else {
			int count = homeFloorMap.getHomeFloorCount185();	
			memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, count);
			
			return count;
		}
		
	}

	public int getHomeFloorCount186(Long activityPlaceId) {
		String groupKey = MemcachedKey.GROUP_KEY_HOME_FLOOR_186;
		
		String key = "home_floor_count";
		Object obj = memcachedService.get(groupKey, key);
		
		if (obj != null) {
			return (int) obj;
		} else {
			int count = homeFloorMap.getHomeFloorCount186(activityPlaceId);	
			memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, count);
			
			return count;
		}
	}

	@SuppressWarnings("unchecked")
	public List<HomeFloorVO> getHomeFloors186(PageQuery pageQuery, Long activityPlaceId) {
		String groupKey = MemcachedKey.GROUP_KEY_HOME_FLOOR_186;
        
        String key = pageQuery.getPage() + "";
        Object obj = memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	return (List<HomeFloorVO>) obj;
        } else {
        	List<HomeFloorVO> homeFloors = homeFloorMap.getHomeFloors186(pageQuery, activityPlaceId);
        	String content = "";
        	
        	String regEx = "\"id\":\"([0-9_]*)\",\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*\"cash\":\"[0-9.]*\",\"jiuCoin\":\"[0-9.]*\"";
        	Pattern pat = Pattern.compile(regEx);
        	Matcher mat;
      
        	Set<Long> productIdSet = new HashSet<Long>();
        	
        	List<Long> idList = new ArrayList<Long>(); 
        	List<String> proStrList = new ArrayList<String>(); 
    	
        	
        	if(homeFloors != null && homeFloors.size() >0){
        		
        		for(HomeFloorVO homeFloor : homeFloors ){
        			content = homeFloor.getContent();
        			mat = pat.matcher(content);
            		while (mat.find()) {
            			productIdSet.add(Long.parseLong(mat.group(1)));       			
            		}
	
        		}
        		if(productIdSet != null && productIdSet.size() > 0 ){
        			Map<Long, Product> productMap = productMapper.getProductByIds(productIdSet);
        			if(homeFloors != null && homeFloors.size() >0){
	        			for(HomeFloorVO homeFloor : homeFloors ){
	        				
	        				content = homeFloor.getContent();
	        				proStrList =  new ArrayList<String>(); 
	        				idList =  new ArrayList<Long>(); 
	        				mat = pat.matcher(content);
	        				while (mat.find()) {
	        					proStrList.add(mat.group());
	        					idList.add(Long.parseLong(mat.group(1)));
	        				}
	        				for(int i = 0; i < proStrList.size(); i++ ){
	        					if(productMap.get(idList.get(i)) != null){
	        						content = content.replaceAll("(\"id\":\"" + idList.get(i) + "\",\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*\"cash\":\")[0-9.]*(\",\"jiuCoin\":\"[0-9.]*\")", "$1" + (int)productMap.get(idList.get(i)).getCash() + "$2");
	        						content = content.replaceAll("(\"id\":\"" + idList.get(i) + "\",\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*,\"jiuCoin\":\")[0-9.]*(\")", "$1" + productMap.get(idList.get(i)).getJiuCoin() + "$2");
	        					}
	        					
	        				}
	        				homeFloor.setContent(content);
	        			}
        			}
        		}
        	}
        	
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, homeFloors);
        	
        	return homeFloors;
        }
	}

	public int getHomeFloorCount187(FloorType floorType, Long relatedId) {
		String groupKey = MemcachedKey.GROUP_KEY_HOME_FLOOR_186;
		
		String key = "home_floor_count" + floorType.getStringValue() + relatedId;
		Object obj = memcachedService.get(groupKey, key);
		
		if (obj != null) {
			return (int) obj;
		} else {
			int count = homeFloorMap.getHomeFloorCount187(floorType.getTypeValue(), relatedId);	
			memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, count);
			
			return count;
		}
	}

	@SuppressWarnings("unchecked")
	public Set<Long> getPonintTemplateProudctIds() {
		String groupKey = MemcachedKey.GROUP_KEY_HOME_FLOOR_186;
        String key = "ponint_template_proudct_ids";
        Object obj = memcachedService.get(groupKey, key);
		
        if (obj != null) {
        	return (Set<Long>) obj;
        } else {
        
			List<HomeFloorVO> homeFloors = homeFloorMap.getHomeFloors187(null, 2, 0L);
	    		
			String content = "";
			
			String regEx = "\"productId\":([0-9_]*)";
			Pattern pat = Pattern.compile(regEx);
			Matcher mat;
			
			Set<Long> productIdSet = new HashSet<Long>();
			
			if(homeFloors != null && homeFloors.size() > 0) {
				
				for(HomeFloorVO homeFloor : homeFloors ){
					content = homeFloor.getContent();
					mat = pat.matcher(content);
					while (mat.find()) {
						productIdSet.add(Long.parseLong(mat.group(1)));       			
					}
				}
			}
			memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, productIdSet);
			return productIdSet;
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<HomeFloorVO> getHomeFloors187(PageQuery pageQuery, FloorType floorType, Long relatedId) {
		String groupKey = MemcachedKey.GROUP_KEY_HOME_FLOOR_186;
        
        String key = pageQuery.getPage() + floorType.getStringValue() + relatedId;
        Object obj = memcachedService.get(groupKey, key);
      //  Object obj = null;// memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	return (List<HomeFloorVO>) obj;
        } else {
        	List<HomeFloorVO> homeFloors = homeFloorMap.getHomeFloors187(pageQuery, floorType.getTypeValue(), relatedId);
        	
        	//积分商城信息替换
        	if(floorType != null && floorType.getTypeValue() == 2){
        		
        		String content = "";
    			
    			String regEx = "\"productId\":([0-9_]*)";
    			String regExCoupon = "\"couponTemplateId\":([0-9_]*)";
    			Pattern pat = Pattern.compile(regEx);
    			Pattern patCoupon = Pattern.compile(regExCoupon);
    			Matcher mat;
    			
    			Set<Long> productIdSet = new HashSet<Long>();
    			Set<Long> couponIdSet = new HashSet<Long>();
    			
    			List<Long> idList = new ArrayList<Long>(); 
    			List<String> proStrList = new ArrayList<String>(); 
    			
    			
    			if(homeFloors != null && homeFloors.size() >0){
    				
    				for(HomeFloorVO homeFloor : homeFloors ){
    					content = homeFloor.getContent();
    					mat = pat.matcher(content);
    					while (mat.find()) {
    						productIdSet.add(Long.parseLong(mat.group(1)));       			
    					}
    					mat = patCoupon.matcher(content);
    					while (mat.find()) {
    						couponIdSet.add(Long.parseLong(mat.group(1)));       			
    					}
    					
    				}
    				
    				//商品信息替换
    				if(productIdSet != null && productIdSet.size() > 0 ){
    					Map<Long, Product> productMap = productMapper.getProductByIds(productIdSet);
    					if(homeFloors != null && homeFloors.size() >0){
    						for(HomeFloorVO homeFloor : homeFloors ){
    							
    							content = homeFloor.getContent();
    							proStrList =  new ArrayList<String>(); 
    							idList =  new ArrayList<Long>(); 
    							mat = pat.matcher(content);
    							while (mat.find()) {
    								proStrList.add(mat.group());
    								idList.add(Long.parseLong(mat.group(1)));
    							}
    							Product productTemp;
    							for(int i = 0; i < proStrList.size(); i++ ){
    								if(productMap.get(idList.get(i)) != null){
    									productTemp = productMap.get(idList.get(i));
    									content = content.replaceAll("(\"productId\":" + idList.get(i) + ")", "$1" +  
    											",\"clothesNum\":\"" + productTemp.getClothesNumber() + "\",\"name\":\"" + productTemp.getName() + "\",\"promotionImage\":\"" + 
    											productTemp.getPromotionImage()+"\",\"brandIdentity\":\"" + productTemp.getBrandId() + "\",\"marketPrice\":\"" + 
    											productTemp.getMarketPrice()+"\",\"price\":\"" + productTemp.getCurrenCash() + "\",\"point\":\"" + productTemp.getJiuCoin() + "\",\"defaultImg\":" + 
    											productTemp.getDetailImages() + ",\"currentJiuCoin\":" + productTemp.getCurrentJiuCoin());
    									//+"proIntroduce\":\"\",\"id\":\"351\",\"clothesNum\":\"JC15174262\",\"name\":\"条纹活页连衣裙\",\"promotionImage\":\"http:\",\"brandIdentity\":\"http\",\"marketPrice\":\"459\",\"marketPriceMin\":\"0\",\"marketPriceMax\":\"0\",\"price\":\"46\",\"defaultImg\":111\""
    								}
    							}
    							homeFloor.setContent(content);
    						}
    					}
    					
    				}
    				//优惠券ID替换
    				if(couponIdSet != null && couponIdSet.size() > 0 ){
    					Map<Long, CouponTemplate> couponMap = couponTemplateMapper.searchMap(couponIdSet);
    					
//    					orderService.getCouponLimitContentMap(couponMap);
    					if(homeFloors != null && homeFloors.size() >0){
    						for(HomeFloorVO homeFloor : homeFloors ){
    							
    							content = homeFloor.getContent();
    							proStrList =  new ArrayList<String>(); 
    							idList =  new ArrayList<Long>(); 
    							mat = patCoupon.matcher(content);
    							while (mat.find()) {
    								proStrList.add(mat.group());
    								idList.add(Long.parseLong(mat.group(1)));
    							}
    							CouponTemplate couponTemp;
    							for(int i = 0; i < proStrList.size(); i++ ){
    								if(couponMap.get(idList.get(i)) != null){
    									couponTemp = couponMap.get(idList.get(i));
    									int avaliableCount = couponTemp.getExchangeLimitTotalCount() - couponTemp.getExchangeCount();
    									content = content.replaceAll("(\"couponTemplateId\":" + idList.get(i) + ")", "$1" +
    											",\"validityStartTime\":\"" + couponTemp.getValidityStartTime() + "\",\"validityEndTime\":\"" + couponTemp.getValidityStartTime() + "\",\"isLimit\":\"" + couponTemp.getIsLimit() +  "\",\"couponDesc\":\"" + (couponTemp.getLimitMoney() > 0 ? "满"+couponTemp.getLimitMoney()+"可用" : "无金额使用限制" )  +  
    											"\",\"limitMoney\":\"" + couponTemp.getLimitMoney() + "\",\"coexist\":\"" + couponTemp.getCoexist() + "\",\"availableCount\":\"" + avaliableCount + "\",\"publishCount\":\"" + couponTemp.getPublishCount() 
    											+ "\",\"exchangeJiuCoinCost\":\"" + couponTemp.getExchangeJiuCoinCost() 
    											+ "\",\"isPromotion\":\"" + couponTemp.getIsPromotion() 
    											+ "\",\"promotionJiuCoin\":\"" + couponTemp.getPromotionJiuCoin()
    											+ "\",\"money\":\"" + couponTemp.getMoney() + "\",\"name\":\"" + couponTemp.getName() + "\",\"type\":\"" + couponTemp.getType() 
    											+ "\",\"rangeType\":\"" + couponTemp.getRangeType() + "\",\"exchangeJiuCoinSetting\":\"" + couponTemp.getExchangeJiuCoinSetting()
    											+ "\",\"exchangeStartTime\":\"" + couponTemp.getExchangeStartTime()
    											+ "\",\"exchangeEndTime\":\"" + couponTemp.getExchangeEndTime()  
    											+ "\",\"exchangeStatus\":\"" + couponTemp.getExchangeStatus() 
    											+ "\",\"exchangeStatusDesc\":\"" + couponTemp.getExchangeStatusDesc() 
    											+ "\",\"exchangeLimitSingleCount\":\"" + couponTemp.getExchangeLimitSingleCount() 
    											+ "\",\"exchangeCount\":\"" + couponTemp.getExchangeCount() + "\"");
    									//+"proIntroduce\":\"\",\"id\":\"351\",\"clothesNum\":\"JC15174262\",\"name\":\"条纹活页连衣裙\",\"promotionImage\":\"http:\",\"brandIdentity\":\"http\",\"marketPrice\":\"459\",\"marketPriceMin\":\"0\",\"marketPriceMax\":\"0\",\"price\":\"46\",\"defaultImg\":111\""
    								}
    								
    							}
    							homeFloor.setContent(content);
    						}
    					}
    				
    					
    				}
    			}
    			
    			memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, homeFloors);
    			
    			
    		}else{	
    			//其它模板替换
    			String content = "";
    			//[{"titles":{"cctitle":"舒适阔腿裤|简约有范超百搭"},"products":[{"marketPriceMax":0,"marketPrice":499,"code":"2600","promotionImage":"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882673311361488267331136.jpg","clothesNum":"XBS58009","summaryImages":"[\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882673311361488267331136.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881778567541488177856754.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881778641331488177864133.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881778690721488177869072.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881778777951488177877795.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881778845201488177884520.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881778914281488177891428.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881778973611488177897361.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881779024991488177902499.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881779136851488177913685.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881779169731488177916973.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881779207971488177920797.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881779237851488177923785.jpg\"]","remainCount":2,"payAmountInCents":0,"defaultImg":"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882673311361488267331136.jpg","marketPriceMin":0,"jiuCoin":0,"price":0,"brandId":708,"name":"时尚百搭韩版阔腿裤","id":3286,"displayImg":"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882673311361488267331136.jpg","brandIdentity":"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881728311821488172831182.jpg","cash":220},{"marketPriceMax":0,"marketPrice":439,"code":"2603","promotionImage":"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882674269361488267426936.jpg","clothesNum":"XBS58023","summaryImages":"[\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882674269361488267426936.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882490860751488249086075.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882490887281488249088728.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882490955141488249095514.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491081141488249108114.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491101591488249110159.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491143031488249114303.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491190141488249119014.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491284341488249128434.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491355131488249135513.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491417991488249141799.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491453211488249145321.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491485811488249148581.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491543491488249154349.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491622361488249162236.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491794131488249179413.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491863441488249186344.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882491946051488249194605.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882492000781488249200078.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882492037531488249203753.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882492091351488249209135.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882492153671488249215367.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882492175811488249217581.jpg\"]","remainCount":8,"payAmountInCents":0,"defaultImg":"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882674269361488267426936.jpg","marketPriceMin":0,"jiuCoin":0,"price":0,"brandId":708,"name":"舒适条纹阔腿裤","id":3304,"displayImg":"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882674269361488267426936.jpg","brandIdentity":"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14881728311821488172831182.jpg","cash":193},{"marketPriceMax":0,"marketPrice":228,"code":"2604","promotionImage":"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882675887221488267588722.jpg","clothesNum":"MZX713","summaryImages":"[\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882675887221488267588722.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14872172102061487217210206.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14872172142701487217214270.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14872172165101487217216510.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14872172196801487217219680.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14872172247231487217224723.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14872172285451487217228545.jpg\",\"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14872172347581487217234758.jpg\"]","remainCount":150,"payAmountInCents":0,"defaultImg":"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882675887221488267588722.jpg","marketPriceMin":0,"jiuCoin":0,"price":0,"brandId":610,"name":"欧美风竖条纹阔腿七分裤","id":2957,"displayImg":"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14882675887221488267588722.jpg","brandIdentity":"","cash":39}],"banner":{"bannerImg":"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14865472046281486547204628.jpg","skipType":"1","code":"2378","skipContent":"","categoryName":""},"seeMore":{"categoryName":"裤装","bannerImg":"https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14773588802111477358880211.png","code":"215","skipType":"4","skipContent":"46"}}]
    			String regEx = "\"id\":([0-9_]*),\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*\"cash\":[0-9.]*,\"jiuCoin\":[0-9.]*";
    			String regExNew = "\"productId\":([0-9_]*)";
    			Pattern pat = Pattern.compile(regEx);
    			Pattern patNew = Pattern.compile(regExNew);
    			Matcher mat;
    			
    			Set<Long> productIdSet = new HashSet<Long>();
    			
    			List<Long> idList = new ArrayList<Long>(); 
    			List<String> proStrList = new ArrayList<String>(); 
    			
    			
    			if(homeFloors != null && homeFloors.size() >0){
    				
    				for(HomeFloorVO homeFloor : homeFloors ){
    					content = homeFloor.getContent();
    					mat = pat.matcher(content);
    					while (mat.find()) {
    						productIdSet.add(Long.parseLong(mat.group(1)));       			
    					}
    					mat = patNew.matcher(content);
    					while (mat.find()) {
    						productIdSet.add(Long.parseLong(mat.group(1)));       			
    					}
    					//19  ,20 模板排序
    					//System.out.println(homeFloor.getTemplateName());
    					if(homeFloor.getTemplateName() != null && (homeFloor.getTemplateName().equals("模板8-20") || homeFloor.getTemplateName().equals("模板8-19"))){
    						
    						String carouseljson = homeFloor.getContent();
    						
    						JSONArray jsonArray = StringUtils.isBlank(carouseljson) ? new JSONArray() : JSON.parseArray(carouseljson);
    						
    						JSONArray newJsonArray = generateNewJSON(jsonArray);
    						
    						homeFloor.setContent(newJsonArray.toJSONString());
    						
    					}
    					
    				}
    				if(productIdSet != null && productIdSet.size() > 0 ){
    					Map<Long, Product> productMap = productMapper.getProductByIds(productIdSet);
    					if(homeFloors != null && homeFloors.size() >0){
    						for(HomeFloorVO homeFloor : homeFloors ){
    							
    							content = homeFloor.getContent();
    							proStrList =  new ArrayList<String>(); 
    							idList =  new ArrayList<Long>(); 
    							mat = pat.matcher(content);
    							while (mat.find()) {
    								proStrList.add(mat.group());
    								idList.add(Long.parseLong(mat.group(1)));
    							}
    							for(int i = 0; i < proStrList.size(); i++ ){
    								
    								if(productMap.get(idList.get(i)) != null){
    									content = content.replaceAll("(\"id\":" + idList.get(i) + ",\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*\"cash\":)[0-9.]*(,\"jiuCoin\":[0-9.]*)", "$1" + (double)productMap.get(idList.get(i)).getCurrenCash() + "$2");
    									content = content.replaceAll("(\"id\":" + idList.get(i) + ",\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*,\"jiuCoin\":)[0-9.]*", "$1" + productMap.get(idList.get(i)).getCurrentJiuCoin());
    									
    								}
    								
    							}
//    							//新productId格式替换
//    							proStrList =  new ArrayList<String>(); 
//    							idList =  new ArrayList<Long>(); 
//    							mat = patNew.matcher(content);
//    							while (mat.find()) {
//    								proStrList.add(mat.group());
//    								idList.add(Long.parseLong(mat.group(1)));
//    							}
//    							Product productTemp;
//    							for(int i = 0; i < proStrList.size(); i++ ){
//    								
//    								if(productMap.get(idList.get(i)) != null){
//    									productTemp = productMap.get(idList.get(i));
//    									content = content.replaceAll("(\"productId\":" + idList.get(i) + ")", "$1" +  
//    											",\"clothesNum\":\"" + productTemp.getClothesNumber() + "\",\"name\":\"" + productTemp.getName() + "\",\"promotionImage\":\"" + 
//    											productTemp.getPromotionImage()+"\",\"brandIdentity\":\"" + productTemp.getBrandId() + "\",\"marketPrice\":\"" + 
//    											productTemp.getMarketPrice()+"\",\"price\":0" + ",\"point\":\"" + productTemp.getJiuCoin() + "\",\"defaultImg\":" + 
//    											productTemp.getDetailImages() + ",\"currentJiuCoin\":" + productTemp.getCurrentJiuCoin()+ ",\"promotionCash\":" + productTemp.getPromotionCash() + ",\"promotionStartTime\":" + productTemp.getPromotionStartTime() + ",\"promotionEndTime\":" + productTemp.getPromotionEndTime() + ",\"promotionStatus\":\"" + productTemp.getPromotionStatus()+ "\"");
//    								}
//    								
//    							}
    							homeFloor.setContent(content);
    						}
    					}
    					
    				}
    			}
    			
    			memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, homeFloors);
    			
    		}
        	return homeFloors;
        	
        }
	}
	
	private JSONArray generateNewJSON(JSONArray jsonArray){
		//积分商城模板
        if(jsonArray.size()==0){
        	return jsonArray;
        }
        
        JSONObject object = jsonArray.getJSONObject(0);
    	if(!object.containsKey("jiuCoinMallType")){
    		return jsonArray;
    	}
    	
    	Integer jiuCoinMallType = (Integer) object.get("jiuCoinMallType");
		if(jiuCoinMallType == 1){	//商品
			if(object.containsKey("productList")){
				ArrayList<Product> products = new ArrayList<Product>();
				ArrayList<Product> productOnsale = new ArrayList<Product>();
				
				ArrayList<Product> productOther = new ArrayList<Product>();
				JSONArray productArray = object.getJSONArray("productList");
				for(int i=0;i<productArray.size();i++){
					JSONObject jsonObject = productArray.getJSONObject(i);
					if(jsonObject.containsKey("productId")){
						
						Long productId = jsonObject.getLong("productId");
						String productImg = jsonObject.getString("promotionImage");
						long code = 0L;
						if(jsonObject.containsKey("code")){
							code = jsonObject.getLong("code");
						}
						Product product = productMapper.getProductById(productId);
						product.setCode(code);
						if(productImg != null && productImg.trim().length() > 0){
							product.setPromotionImage(productImg);
						}
						//products.add(product);
						//System.out.println(product.getId()+product.getPromotionStatus());
						if(product.getPromotionStatus().equals("进行中")){
							productOnsale.add(product);
							
						}else{
							productOther.add(product);
						}
					}
				}
//				for(Product product : productOnsale){
//					System.out.println(product.getId()+"@before:"+product.getPromotionLastTime());
//				}
				Collections.sort(productOnsale, new Comparator<Product>() {
					public int compare(Product o1, Product o2) {
						if(o1.getPromotionLastTime() > o2.getPromotionLastTime()){
							return 1;
						}else if(o1.getPromotionLastTime() < o2.getPromotionLastTime()){
							return -1;
						}else return 0;
						
					}
				});
				Collections.sort(productOther, new Comparator<Product>() {
					public int compare(Product o1, Product o2) {
						if(o1.getPromotionSortTime() > o2.getPromotionSortTime()){
							return 1;
						}else if(o1.getPromotionSortTime() < o2.getPromotionSortTime()){
							return -1;
						}else return 0;
						
					}
				});
				
//				for(Product product: products){
//					System.out.println(product.getId()+"$$$"+product.getPromotionStartTime());
//					
//				}
				products.addAll(productOnsale);
//				for(Product product : productOnsale){
//					System.out.println(product.getId()+"@after:"+product.getPromotionLastTime());
//				}
				products.addAll(productOther);
//				for(Product product : products){
//					System.out.println(product.getId());
//				}
				object.put("productList", products);
				
			}
		} 
		
		return jsonArray;
	}
}
