package com.store.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.FloorType;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ShopHomeTemplate;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.MemcachedService;
import com.store.dao.mapper.HomeFloorVOMapper;
import com.store.dao.mapper.ProductMapper;
import com.store.dao.mapper.coupon.ShopCouponTemplateMapper;
import com.store.entity.HomeFloorVOShop;
import com.store.entity.ProductVOShop;
import com.store.entity.coupon.ShopCouponTemplate;
import com.yujj.entity.product.Product;
import com.yujj.util.uri.UriBuilder;

@Service
public class HomeFloorService {

	@Autowired
	private HomeFloorVOMapper homeFloorMap;
	
    @Autowired
    private MemcachedService memcachedService;
    
    @Autowired
    private ProductServiceShop productService;

    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
	private IncomeAssembler incomeAssembler;
    
    @Autowired
    private ShopCouponTemplateMapper shopCouponTemplateMapper;

	@SuppressWarnings("unchecked")
	public List<HomeFloorVOShop> getHomeFloors187(PageQuery pageQuery, FloorType floorType,  UserDetail userDetail) {
		String groupKey = MemcachedKey.GROUP_KEY_HOME_FLOOR_186;
        
        String key = pageQuery.getPage() + floorType.getStringValue()+ ":" + userDetail.getId();
        Object obj = memcachedService.get(groupKey, key);
//        Object obj = null;// memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	return (List<HomeFloorVOShop>) obj;
        } else {
        	List<HomeFloorVOShop> homeFloors = homeFloorMap.getHomeFloors187(pageQuery, floorType.getTypeValue());
        	
			if(homeFloors != null && homeFloors.size() >0){
				
				for(HomeFloorVOShop homeFloor : homeFloors ){
					String carouseljson = homeFloor.getContent();
					
					JSONArray jsonArray = StringUtils.isBlank(carouseljson) ? new JSONArray() : JSON.parseArray(carouseljson);
		
					
					
					for(int i = 0; i < jsonArray.size(); i++){
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						
						if(jsonObject.get("title")!=null && jsonObject.get("title").equals("店铺数据概况")){
							long nowTime =System.currentTimeMillis();
							
							long todayStart = UtilDate.getStartTime().getTime();  
							
							long monthAgo = UtilDate.getPlanDayFromDate(new Date(), -30).getTime();  //待时间校准
							
							int  productNum = homeFloorMap.getStoreProductNum(userDetail.getId());   //在架商品数
							
							int  productValue = homeFloorMap.getStoreProductValue(userDetail.getId());   //在架商品货值
							
							int  favoriteAddToday = homeFloorMap.getStoreFavoriteToday(userDetail.getId(), todayStart, 0);  //今日想要 增加数
							
							int  favoriteDelToday = homeFloorMap.getStoreFavoriteToday(userDetail.getId(), todayStart, -1);  //今日想要 减少数
							
							int  pvToday = homeFloorMap.getStorePVCountToday(userDetail.getId(), todayStart);   //今日访问量
							
							int  memberTotal = homeFloorMap.getStoreMemberTotal(userDetail.getId());  //总会员数
							
							int  favoriteMonth = homeFloorMap.getStoreFavoriteMonth(userDetail.getId(), monthAgo);  //30天想要
							
							int  pvMonth = homeFloorMap.getStorePVMonth(userDetail.getId(), monthAgo);  //30天访问量
							
							int  pvTotal = homeFloorMap.getStorePVTotal(userDetail.getId());  //总访问量

							JSONArray dataList = jsonObject.get("items") == null ? new JSONArray() : JSON.parseArray( jsonObject.get("items").toString());
							for(int j = 0; j < dataList.size(); j++){
								if(dataList.getJSONObject(j) != null && dataList.getJSONObject(j).get("name").equals("商品总数")){
									dataList.getJSONObject(j).put("count", productNum);
								}
								if(dataList.getJSONObject(j) != null && dataList.getJSONObject(j).get("name").equals("今日想要")){
									dataList.getJSONObject(j).put("count", favoriteAddToday - favoriteDelToday);
								}
								if(dataList.getJSONObject(j) != null && dataList.getJSONObject(j).get("name").equals("今日访问量")){
									dataList.getJSONObject(j).put("count", pvToday);
								}
								if(dataList.getJSONObject(j) != null && dataList.getJSONObject(j).get("name").equals("总会员数")){
									dataList.getJSONObject(j).put("count", memberTotal);
								}
								if(dataList.getJSONObject(j) != null && dataList.getJSONObject(j).get("name").equals("商品总货值")){
									dataList.getJSONObject(j).put("count", productValue);
								}
								if(dataList.getJSONObject(j) != null && dataList.getJSONObject(j).get("name").equals("30天想要")){
									dataList.getJSONObject(j).put("count", favoriteMonth);
								}
								if(dataList.getJSONObject(j) != null && dataList.getJSONObject(j).get("name").equals("30天访问量")){
									dataList.getJSONObject(j).put("count", pvMonth);
								}
								if(dataList.getJSONObject(j) != null && dataList.getJSONObject(j).get("name").equals("总访问量")){
									dataList.getJSONObject(j).put("count", pvTotal);
								}
								System.out.println(dataList.getJSONObject(j).get("name") + ":" + dataList.getJSONObject(j).get("count"));
								
								
							}
//							JSONObject jsonObj = new JSONObject();
//							jsonObj.put("name", "nameeee");
//							jsonObj.put("six", "7777");
//							dataList.add(jsonObj);
							jsonObject.put("items", dataList);
						}
						if(jsonObject.get("title")!=null && jsonObject.get("title").equals("精品货源")){
//							jsonObject.put("nextDataUrl", Constants.SERVER_URL + "/miniapp/mainview/boutique.do");
							
							JSONArray dataList = jsonObject.get("items") == null ? new JSONArray() : JSON.parseArray( jsonObject.get("items").toString());
							
							PageQuery productPageQuery = new PageQuery();
							List<ProductVOShop> prodcutList = productService.getUserBestSellerProductList186(userDetail, productPageQuery , 0);
					        int totalCount = productService.getBestSellerProductCount();
					        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(productPageQuery, totalCount);
					       
					        jsonObject.put("productList", getProductListIndex(prodcutList, userDetail));
					        jsonObject.put("productPageQuery", pageQueryResult);
					        if (pageQueryResult.isMore()) {
					            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/miniapp/mainview/boutique.json");
					            builder.set("page", pageQueryResult.getPage() + 1);
					            builder.set("pageSize", pageQueryResult.getPageSize());
					           
					            builder.set("guide_flag", 0);
					            jsonObject.put("nextDataUrl", builder.toUri());
					        }

						}
//						System.out.println("title:"+jsonObject.get("title"));
					}
//					JSONObject jsonObj = new JSONObject();
//					jsonObj.put("name", "nameeee");
//					jsonObj.put("six", "7777");
//					jsonArray.add(jsonObj);
//					System.out.println("@@"+);
					
					
					JSONArray newJsonArray = generateNewJSON(jsonArray);
					
					homeFloor.setContent(newJsonArray.toJSONString());
	
				}


			}else{
//				JSONArray jsonArray = new JSONArray();
//				JSONObject jsonObj = new JSONObject();
//				jsonObj.put("name", "在架商品数");
//				jsonObj.put("count", productNum);
//				jsonArray.add(jsonObj);
//				
//				jsonObj = new JSONObject();
//				jsonObj.put("name", "今日想要");
//				jsonObj.put("count", favoriteAddToday - favoriteDelToday);
//				jsonArray.add(jsonObj);
//				
//				jsonObj = new JSONObject();
//				jsonObj.put("name", "今日访问量");
//				jsonObj.put("count", pvToday);
//				jsonArray.add(jsonObj);
//				
//				jsonObj = new JSONObject();
//				jsonObj.put("name", "总会员数");
//				jsonObj.put("count", memberTotal);
//				jsonArray.add(jsonObj);
//				
//				jsonObj = new JSONObject();
//				jsonObj.put("name", "在架商品货值");
//				jsonObj.put("count", productValue);
//				jsonArray.add(jsonObj);
//				
//				jsonObj = new JSONObject();
//				jsonObj.put("name", "30天想要");
//				jsonObj.put("count", favoriteMonth);
//				jsonArray.add(jsonObj);
//				
//				jsonObj = new JSONObject();
//				jsonObj.put("name", "30天访问量");
//				jsonObj.put("count", pvMonth);
//				jsonArray.add(jsonObj);
//				
//				jsonObj = new JSONObject();
//				jsonObj.put("name", "总访问量");
//				jsonObj.put("count", pvTotal);
//				jsonArray.add(jsonObj);
//
//				System.out.println(jsonArray.toJSONString());
				
			}
			
    			
    			memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_MINUTE, homeFloors);
    			
    		
        	return homeFloors;
        	
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<HomeFloorVOShop> getHomeFloors187(PageQuery pageQuery, FloorType floorType, Long relatedId) {
		String groupKey = MemcachedKey.GROUP_KEY_HOME_FLOOR_186;
        
        String key = pageQuery.getPage() + floorType.getStringValue() + relatedId;
        Object obj = memcachedService.get(groupKey, key);
      //  Object obj = null;// memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	return (List<HomeFloorVOShop>) obj;
        } else {
        	List<HomeFloorVOShop> homeFloors = homeFloorMap.getHomeFloors188(pageQuery, floorType.getTypeValue(), relatedId);
        	
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
    				
    				for(HomeFloorVOShop homeFloor : homeFloors ){
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
    					Map<Long, ProductVOShop> productMap = productMapper.getProductByIds(productIdSet);
    					if(homeFloors != null && homeFloors.size() >0){
    						for(HomeFloorVOShop homeFloor : homeFloors ){
    							
    							content = homeFloor.getContent();
    							proStrList =  new ArrayList<String>(); 
    							idList =  new ArrayList<Long>(); 
    							mat = pat.matcher(content);
    							while (mat.find()) {
    								proStrList.add(mat.group());
    								idList.add(Long.parseLong(mat.group(1)));
    							}
    							ProductVOShop productTemp;
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
    					Map<Long, ShopCouponTemplate> couponMap = shopCouponTemplateMapper.searchMap(couponIdSet);
    					
//    					orderService.getCouponLimitContentMap(couponMap);
    					if(homeFloors != null && homeFloors.size() >0){
    						for(HomeFloorVOShop homeFloor : homeFloors ){
    							
    							content = homeFloor.getContent();
    							proStrList =  new ArrayList<String>(); 
    							idList =  new ArrayList<Long>(); 
    							mat = patCoupon.matcher(content);
    							while (mat.find()) {
    								proStrList.add(mat.group());
    								idList.add(Long.parseLong(mat.group(1)));
    							}
    							ShopCouponTemplate couponTemp;
    							for(int i = 0; i < proStrList.size(); i++ ){
    								if(couponMap.get(idList.get(i)) != null){
    									couponTemp = couponMap.get(idList.get(i));
//    									int avaliableCount = couponTemp.getExchangeLimitTotalCount() - couponTemp.getExchangeCount();
    									content = content.replaceAll("(\"couponTemplateId\":" + idList.get(i) + ")", "$1" +
    											",\"validityStartTime\":\"" + couponTemp.getValidityStartTime() + "\",\"validityEndTime\":\"" + couponTemp.getValidityStartTime() 
//    											+ "\",\"isLimit\":\"" + couponTemp.getIsLimit() 
    											+  "\",\"couponDesc\":\"" + (couponTemp.getLimitMoney() > 0 ? "满"+couponTemp.getLimitMoney()+"可用" : "无金额使用限制" )  +  
    											"\",\"limitMoney\":\"" + couponTemp.getLimitMoney() 
//    											+ "\",\"coexist\":\"" + couponTemp.getCoexist() + "\",\"availableCount\":\"" + avaliableCount 
    											+ "\",\"publishCount\":\"" + couponTemp.getPublishCount() 
//    											+ "\",\"exchangeJiuCoinCost\":\"" + couponTemp.getExchangeJiuCoinCost() 
//    											+ "\",\"isPromotion\":\"" + couponTemp.getIsPromotion() 
//    											+ "\",\"promotionJiuCoin\":\"" + couponTemp.getPromotionJiuCoin()
    											+ "\",\"money\":\"" + couponTemp.getMoney() + "\",\"name\":\"" + couponTemp.getName() 
//    											+ "\",\"type\":\"" + couponTemp.getType() 
//    											+ "\",\"rangeType\":\"" + couponTemp.getRangeType() + "\",\"exchangeJiuCoinSetting\":\"" + couponTemp.getExchangeJiuCoinSetting()
//    											+ "\",\"exchangeStartTime\":\"" + couponTemp.getExchangeStartTime()
//    											+ "\",\"exchangeEndTime\":\"" + couponTemp.getExchangeEndTime()  
//    											+ "\",\"exchangeStatus\":\"" + couponTemp.getExchangeStatus() 
//    											+ "\",\"exchangeStatusDesc\":\"" + couponTemp.getExchangeStatusDesc() 
//    											+ "\",\"exchangeLimitSingleCount\":\"" + couponTemp.getExchangeLimitSingleCount() 
//    											+ "\",\"exchangeCount\":\"" + couponTemp.getExchangeCount() + "\""
    											);
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
    				
    				for(HomeFloorVOShop homeFloor : homeFloors ){
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
    					Map<Long, ProductVOShop> productMap = productMapper.getProductByIds(productIdSet);
    					if(homeFloors != null && homeFloors.size() >0){
    						for(HomeFloorVOShop homeFloor : homeFloors ){
    							
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
						Product product = new Product();// = productMapper.getProductById(productId);
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
	
	
    private List<Map<String, Object>> getProductListIndex(List<ProductVOShop> products, UserDetail userDetail) {

//    	incomeAssembler.assemble(products, userDetail);
        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        for (ProductVOShop product : products) {
            productList.add(product.toSimpleMapIndex());
        }
        return productList;
    }

	public int getHomeFloorCount187(FloorType floorType, Long relatedId) {
		// TODO Auto-generated method stub
		String groupKey = MemcachedKey.GROUP_KEY_HOME_FLOOR_186;
		
		String key = "home_floor_count" + floorType.getStringValue() + relatedId;
		Object obj = memcachedService.get(groupKey, key);
		
		if (obj != null) {
			return (int) obj;
		} else {
			int count = homeFloorMap.getHomeFloorCount187(floorType.getTypeValue(), relatedId);	
			memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_MINUTE*2, count);
			return count;
		}
	}

	@SuppressWarnings("unchecked")
	public List<HomeFloorVOShop> getHomeFloors188(PageQuery pageQuery, FloorType floorType, Long relatedId, UserDetail userDetail) {
		// TODO Auto-generated method stub
		String groupKey = MemcachedKey.GROUP_KEY_HOME_FLOOR_186;
        
        String key = pageQuery.getPage() + floorType.getStringValue() + relatedId;
        Object obj = memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	return (List<HomeFloorVOShop>) obj;
        } else {
        	List<HomeFloorVOShop> homeFloors = homeFloorMap.getHomeFloors188(pageQuery, floorType.getTypeValue(), relatedId);
        	String content = "";
        	
//        	String regEx = "\"id\":([0-9_]*),\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*\"cash\":[0-9.]*";
        	String regEx = "\"id\":([0-9_]*),[^}]*\"cash\":[0-9.]*";
        	Pattern pat = Pattern.compile(regEx);
        	Matcher mat;
      
        	Set<Long> productIdSet = new HashSet<Long>();
        	
        	List<Long> idList = new ArrayList<Long>(); 
        	List<String> proStrList = new ArrayList<String>(); 
    	
        	
        	if(homeFloors != null && homeFloors.size() >0){
        		
        		for(HomeFloorVOShop homeFloor : homeFloors ){
        			content = homeFloor.getContent();
//        			content = "\"name\":\"貉子毛连帽街头风帅气长款保暖羽绒服\",\"id\":2435,\"displayImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14799880947011479988094701.JPG\",\"brandIdentity\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14689206683501468920668350.jpg\",\"cash\":100";
    				
        			mat = pat.matcher(content);
            		while (mat.find()) {
            			productIdSet.add(Long.parseLong(mat.group(1)));       			
            		}
	
        		}
        		if(productIdSet != null && productIdSet.size() > 0 ){
        			Map<Long, ProductVOShop> productMap = productMapper.getProductByIds(productIdSet);
        			incomeAssembler.assemble(productMap, userDetail);
        			if(homeFloors != null && homeFloors.size() >0){
	        			for(HomeFloorVOShop homeFloor : homeFloors ){
	        				
	        				content = homeFloor.getContent();
//	        				content = "\"name\":\"貉子毛连帽街头风帅气长款保暖羽绒服\",\"id\":2435,\"displayImg\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14799880947011479988094701.JPG\",\"brandIdentity\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14689206683501468920668350.jpg\",\"cash\":100";
	        				proStrList =  new ArrayList<String>(); 
	        				idList =  new ArrayList<Long>(); 
	        				mat = pat.matcher(content);
	        				while (mat.find()) {
	        					proStrList.add(mat.group());
	        					idList.add(Long.parseLong(mat.group(1)));
	        				}

	        					for(int i = 0; i < proStrList.size(); i++ ){

	        					if(productMap.get(idList.get(i)) != null){
//	        						System.out.println("content1:"+idList.get(i)+content);  
	        						content = content.replaceAll("(\"id\":" + idList.get(i) + ",[^}]*\"cash\":)[0-9.]*", "$1" + (double)productMap.get(idList.get(i)).getWholeSaleCash()+",\"income\":"+(double)productMap.get(idList.get(i)).getIncome());//,\"income\":\""+(double)productMap.get(idList.get(i)).getIncome()
//	        						content = content.replaceAll("(\"id\":" + idList.get(i) + ",[^}]*\"cash\":)[0-9.]*", "$1" + (double)productMap.get(idList.get(i)).getCash()+",\"income\":"+(double)productMap.get(idList.get(i)).getIncome());//,\"income\":\""+(double)productMap.get(idList.get(i)).getIncome()
//	        						System.out.println("content2:"+idList.get(i)+content);
//	        						content = content.replaceAll("(\"id\":" + idList.get(i) + "[^}]*\"cash\":\")[0-9.]*(\")", "$1" + (double)productMap.get(idList.get(i)).getCash()+"\",\"income\":\""+(double)productMap.get(idList.get(i)).getIncome()+ "$2");
//	        						content = content.replaceAll("(\"id\":" + idList.get(i) + ",\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*,\"jiuCoin\":\")[0-9.]*(\")", "$1" + productMap.get(idList.get(i)).getCurrentJiuCoin() + "$2");
	        						content = content.replaceAll("(\"id\":" + idList.get(i) + ",[^}]*\"cash\":)[0-9.]*", "$1" + (double)productMap.get(idList.get(i)).getWholeSaleCash()+",\"ladderPriceJson\":"+productMap.get(idList.get(i)).getLadderPriceJson());//,\"income\":\""+(double)productMap.get(idList.get(i)).getIncome()
	        						content = content.replaceAll("(\"id\":" + idList.get(i) + ",[^}]*\"cash\":)[0-9.]*", "$1" + (double)productMap.get(idList.get(i)).getWholeSaleCash()+",\"minLadderPrice\":"+productMap.get(idList.get(i)).getMinLadderPrice());//,\"income\":\""+(double)productMap.get(idList.get(i)).getIncome()
	        						content = content.replaceAll("(\"id\":" + idList.get(i) + ",[^}]*\"cash\":)[0-9.]*", "$1" + (double)productMap.get(idList.get(i)).getWholeSaleCash()+",\"maxLadderPrice\":"+productMap.get(idList.get(i)).getMaxLadderPrice());//,\"income\":\""+(double)productMap.get(idList.get(i)).getIncome()
	        						content = content.replaceAll("(\"id\":" + idList.get(i) + ",[^}]*\"cash\":)[0-9.]*", "$1" + (double)productMap.get(idList.get(i)).getWholeSaleCash()+",\"supplierId\":"+productMap.get(idList.get(i)).getSupplierId());//,\"income\":\""+(double)productMap.get(idList.get(i)).getIncome()
	        					}
	        					
	        				}
	        				homeFloor.setContent(content);
	        			}
        			}
        			
        		}
        	}
        	
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_MINUTE, homeFloors);
        	
        	return homeFloors;
        }
	}

	/**
	 * 判断当前模板中是否有模板21
	 * @param floorType
	 * @param relatedId
	 * @param userDetail
	 * @return
	 */
	public boolean getHasTag(FloorType floorType, Long relatedId, UserDetail userDetail) {
		List<HomeFloorVOShop> homeFloors = homeFloorMap.getHomeFloors188(null, floorType.getTypeValue(), relatedId);
		boolean result = false;
		try {
			for (HomeFloorVOShop homeFloor : homeFloors) {
				if("模板8-21".equals(homeFloor.getTemplateName())){
					result = true;
					break;
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ShopHomeTemplate getHomeFloorTemplate(long nextHomeTemplateId) {
		return homeFloorMap.getHomeFloorTemplate(nextHomeTemplateId);
	}
}
