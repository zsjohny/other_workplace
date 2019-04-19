package com.yujj.business.facade;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.Client;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.entity.BrandVO;
import com.jiuyuan.entity.comment.CommentVO;
import com.jiuyuan.entity.order.RestrictProductVO;
import com.jiuyuan.entity.product.CategoryV0;
import com.jiuyuan.entity.product.Subscript;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.shopping.DiscountInfo;
import com.jiuyuan.util.BeanUtil;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.ProductUtil;
import com.yujj.business.adapter.DataminingAdapter;
import com.yujj.business.assembler.ProductPropAssembler;
import com.yujj.business.service.BrandService;
import com.yujj.business.service.FavoriteService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.ProductCategoryService;
import com.yujj.business.service.ProductPropertyService;
import com.yujj.business.service.ProductSKUService;
import com.yujj.business.service.ProductService;
import com.yujj.entity.Brand;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductProp;
import com.yujj.entity.product.ProductPropName;
import com.yujj.entity.product.ProductPropNameValuesPair;
import com.yujj.entity.product.ProductPropVO;
import com.yujj.entity.product.ProductPropValue;
import com.yujj.entity.product.ProductSKU;
import com.yujj.entity.product.ProductVO;
//import com.yujj.web.controller.mobile.MobileProductController;

@Service
public class ProductFacade {

	public final long DAY_MILLION_SECOND = 24 * 60 * 60 * 1000L;
	
	private static final Logger logger = Logger.getLogger(ProductFacade.class);
	
//    @Autowired
//    private ObjectMapper objectMapper;
	
    @Autowired
    private ProductPropAssembler productPropAssembler;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private DataminingAdapter dataminingAdapter;
    
    @Autowired
    private ProductCategoryService productCategoryService;
    
    @Autowired
    private ProductSKUService productSKUService;
    

    @Autowired
    private ProductPropertyService productPropertyService;

    @Autowired
    private FavoriteService favoriteService;

    
    @Autowired
    private BrandService brandService;
    
        
    @Autowired
    private GlobalSettingService globalSettingService;
    
    @Autowired
    private ShoppingCartFacade shoppingCartFacade;
    
    @Autowired
    private CommentFacade commentFacade;
    
    public List<ProductPropVO> loadProductPropVOs(List<ProductProp> productProps) {
        List<ProductPropVO> productPropVOs = new ArrayList<ProductPropVO>();
        for (ProductProp productProp : productProps) {
            ProductPropVO vo = new ProductPropVO();
            BeanUtil.copyProperties(vo, productProp);
            productPropVOs.add(vo);
        }
        productPropAssembler.assemble(productPropVOs);
        return productPropVOs;
    }
    

    private void sortProducts(List<RestrictProductVO> products, Map<Long, List<RestrictProductVO>> productGroup,
			Set<Long> existProductIds) {
		long lastProductId = 0; 
		List<RestrictProductVO> buyProducts = null;
		
    	//对获取到的商品记录按照id分组,内部集合商品成员按照时间排序
		for(RestrictProductVO product : products) {
			long productId = product.getProductId();
			
			if(productId != lastProductId) {
				if (buyProducts != null) {
					Collections.sort(buyProducts, 
							new Comparator<RestrictProductVO>() {
						public int compare(RestrictProductVO o1, RestrictProductVO o2) {
							return (int) (o2.getCreateTime() - o1.getCreateTime());
						}
					});
				}
				buyProducts = new ArrayList<RestrictProductVO>();
				productGroup.put(productId, buyProducts);
				lastProductId = productId;
				//获取存在记录的商品id,优化下面的循环
				existProductIds.add(productId);
			}
			
			buyProducts.add(product);
		}
		//对最后一个商品排序
		if(productGroup.size() > 0) {
			List<RestrictProductVO> bps = productGroup.get(lastProductId);
			if (bps != null) {
				Collections.sort(bps, 
						new Comparator<RestrictProductVO>() {
					public int compare(RestrictProductVO o1, RestrictProductVO o2) {
						return (int) (o2.getCreateTime() - o1.getCreateTime());
					}
				});
			}
		}
	}

	private Long getStartTime(){  
        Calendar todayStart = Calendar.getInstance();  
        todayStart.set(Calendar.HOUR, 0);  
        todayStart.set(Calendar.MINUTE, 0);  
        todayStart.set(Calendar.SECOND, 0);  
        todayStart.set(Calendar.MILLISECOND, 0);  
        return todayStart.getTime().getTime();  
    }

	/**
	 * 获取相关商品的限购信息
	 * @param userId
	 * @param productIds
	 * @return
	 */
	public Map<Long, RestrictProductVO> restrictInfoOfProduct(long userId, Set<Long> productIds) {
		Map<Long, RestrictProductVO> map = new HashMap<Long, RestrictProductVO>();
		
		//获取用户购买的sku信息
        List<Product> pros = productService.getProductByIds(productIds, false);
        for(Product product : pros) {
        	if(product.getRestrictDayBuy() != -1 || product.getRestrictHistoryBuy() != -1) {
        		RestrictProductVO rVo = new RestrictProductVO();
        		
        		rVo.setProductId(product.getId());
        		rVo.setRestrictDayBuy(product.getRestrictDayBuy());
        		rVo.setRestrictHistoryBuy(product.getRestrictHistoryBuy());
        		rVo.setRestrictCycle(product.getRestrictCycle());
        		rVo.setRestrictHistoryBuyTime(product.getRestrictHistoryBuyTime());
        		rVo.setRestrictDayBuyTime(product.getRestrictDayBuyTime());
        		
        		map.put(product.getId(), rVo);
        	}
        }
        
		//存在购买记录的商品id
		Set<Long> existProductIds = new HashSet<Long>();
		//商品按id分组的组集合
		Map<Long, List<RestrictProductVO>> productGroup = new HashMap<Long, List<RestrictProductVO>>();
		
		//获取用户购买这些商品的记录
		List<RestrictProductVO> products = productService.getBuyerLog(userId, productIds);
		if(products == null) {
			return map;
		}
		//对获取的商品集记录进行按id分组,组内按CreateTime降序排序
		sortProducts(products, productGroup, existProductIds);
		
		for(Long productId : existProductIds) {
			RestrictProductVO rVo = map.get(productId);

			if(rVo == null)
				continue;
			
    		//各自productId的List
    		List<RestrictProductVO> productsItems = productGroup.get(productId);
    		
    		if(productsItems != null && productsItems.size() > 0){
    			int restrictDayBuy = productsItems.get(0).getRestrictDayBuy();
    			int restrictHistoryBuy = productsItems.get(0).getRestrictHistoryBuy();
    			//商品既不今日限购也不历史限购,不记录
    			if(restrictDayBuy == -1 && restrictHistoryBuy == -1) 
    				continue;

    			Map<String, Integer> buyCountLog = new HashMap<String, Integer>();
    			buyCountLog = getBuyCountLog(productsItems);
    			
				BeanUtils.copyProperties(productsItems.get(0), rVo);
				rVo.setDaySum(buyCountLog.get("daySum"));
				rVo.setHistorySum(buyCountLog.get("historySum"));
				
				map.put(rVo.getProductId(), rVo);
    		}
    	}
		
		return map;
	}  
	
	/**
	 * 获取当前商品在限购时间段内的购买件数记录
	 * @param productsItems 
	 * @return
	 */
    private Map<String, Integer> getBuyCountLog(List<RestrictProductVO> productsItems) {
    	Map<String, Integer> map = new HashMap<String, Integer>();
		int restrictCycle = productsItems.get(0).getRestrictCycle();
		
		long restrictDaySetBuyTime = (long) productsItems.get(0).getRestrictDayBuyTime();
		long restrictHistorySetBuyTime = (long) productsItems.get(0).getRestrictHistoryBuyTime();
		
		//获取历史记录的开始时间：取MAX(限购的设置时间, 按规定的周期开始时间) 离当天最近的时间
		long restrictDayTime = restrictDaySetBuyTime > getStartTime() ? restrictDaySetBuyTime : getStartTime();
		long restrictHistoryTime = restrictHistorySetBuyTime > (getStartTime() - restrictCycle * DAY_MILLION_SECOND) ? 
				restrictHistorySetBuyTime : (getStartTime() - (restrictCycle * DAY_MILLION_SECOND));
				
		//今日已购买
		int daySum = 0;
		//历史周期内已购买
		int historySum = 0;
		for(RestrictProductVO product : productsItems) {
			if(product.getCreateTime() >= restrictDayTime) {
				daySum += product.getBuyCount();
			} else {
				break;
			}
		}
		
		for(RestrictProductVO product : productsItems) {
			if (product.getCreateTime() >= restrictHistoryTime) {
				historySum += product.getBuyCount();
			} else {
				break;
			}
		}
		
		map.put("daySum", daySum);
		map.put("historySum", historySum);
		
		return map;
	}


	/**
     * 获取符合限购条件的商品
     * @param userId
     * @param productCountMap 
     * 			参数:Map<productId, buyCount> 
     * @return
     */
    public List<RestrictProductVO> getRestrictInfo(long userId, Map<Long, Integer> productCountMap) {
		List<RestrictProductVO> list = new ArrayList<RestrictProductVO>();
		//存在购买记录的商品id
		Set<Long> existProductIds = new HashSet<Long>();
		//商品按id分组的组集合
		Map<Long, List<RestrictProductVO>> productGroup = new HashMap<Long, List<RestrictProductVO>>();

		//获取用户购买这些商品的记录
		if(productCountMap.keySet().size() == 0) {
			return new ArrayList<RestrictProductVO>();
		}
		List<RestrictProductVO> products = productService.getBuyerLog(userId, productCountMap.keySet());
		
		//对获取的商品集记录进行按id分组,组内按CreateTime降序排序
		sortProducts(products, productGroup, existProductIds);       
		

    	for(Long productId : productCountMap.keySet()) {
    		//各自productId的List
    		List<RestrictProductVO> productsItems = productGroup.get(productId);
    		
    		if(productsItems != null && productsItems.size() > 0) {
    			int restrictDayBuy = productsItems.get(0).getRestrictDayBuy();
    			int restrictHistoryBuy = productsItems.get(0).getRestrictHistoryBuy();
    			int restrictCycle = productsItems.get(0).getRestrictCycle();
    			
    			//商品既不今日限购也不历史限购,不记录
    			if(restrictDayBuy == -1 && restrictHistoryBuy == -1) 
    				continue;
    			
    			Map<String, Integer> buyCountLog = new HashMap<String, Integer>();
    			buyCountLog = getBuyCountLog(productsItems);
				
    			int daySum = buyCountLog.get("daySum");
    			int historySum = buyCountLog.get("historySum");
				int buyCount = productCountMap.get(productId);
				//如果 限购 && (历史购买+当前欲购买数  > 限购上限)
				if(((daySum + buyCount > restrictDayBuy) && restrictDayBuy != -1) ||
						(historySum + buyCount > restrictHistoryBuy) && restrictHistoryBuy != -1) {
					RestrictProductVO restrictProductVO = new RestrictProductVO();
					restrictProductVO.setRestrictCycle(restrictCycle);
					restrictProductVO.setRestrictHistoryBuy(restrictHistoryBuy);
					restrictProductVO.setRestrictDayBuy(restrictDayBuy);
					restrictProductVO.setProductId(productId);
					restrictProductVO.setDaySum(daySum);
					restrictProductVO.setHistorySum(historySum);
					restrictProductVO.setBuyCount(buyCount);
					
					list.add(restrictProductVO);
				}
    		} else {
    			Product product = productService.getProductById(productId);
    			
    			int restrictDayBuy = product.getRestrictDayBuy();
    			int restrictHistoryBuy = product.getRestrictHistoryBuy();
    			int restrictCycle = product.getRestrictCycle();
    			
    			int buyCount = productCountMap.get(productId);
    			//如果 限购 && (历史购买+当前欲购买数  > 限购上限)
				if(((buyCount > restrictDayBuy) && restrictDayBuy != -1) ||
						(buyCount > restrictHistoryBuy) && restrictHistoryBuy != -1) {
					RestrictProductVO restrictProductVO = new RestrictProductVO();
					restrictProductVO.setRestrictCycle(restrictCycle);
					restrictProductVO.setRestrictHistoryBuy(restrictHistoryBuy);
					restrictProductVO.setRestrictDayBuy(restrictDayBuy);
					restrictProductVO.setProductId(productId);
					restrictProductVO.setDaySum(0);
					restrictProductVO.setHistorySum(0);
					restrictProductVO.setBuyCount(buyCount);
					
					list.add(restrictProductVO);
				}
    		}
    	}
    	
    	return list;
    }
    
    public Map<String, Object> getProduct18(Product product, UserDetail userDetail) {
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	// get product main information
    	
    	List<ProductProp> baseProps = productPropertyService.getOrderedProductProperties(product.getId());
    	List<ProductPropVO> basePropVOs = this.loadProductPropVOs(baseProps);
    	data.put("baseProps", basePropVOs);
    	
    	List<ProductSKU> skus = productSKUService.getAllStatusOfProductSKUs(product.getId());
    	Map<String, ProductSKU> skuMap = new HashMap<String, ProductSKU>();
    	List<ProductPropVO> productPropVOs = new ArrayList<ProductPropVO>();
    	for (ProductSKU sku : skus) {
    		skuMap.put(sku.getPropertyIds(), sku);
    		productPropVOs.addAll(sku.getProductProps());
    	}
        data.put("skuMap", skuMap);

    	productPropAssembler.assemble(productPropVOs);
    	
    	List<ProductPropNameValuesPair> skuProps = new ArrayList<ProductPropNameValuesPair>();
    	Map<Long, ProductPropNameValuesPair> skuPropMap = new HashMap<Long, ProductPropNameValuesPair>();
    	for (ProductPropVO propVO : productPropVOs) {
    		ProductPropName propName = propVO.getPropName();
    		ProductPropNameValuesPair skuProp = skuPropMap.get(propName.getId());
    		if (skuProp == null) {
    			skuProp = new ProductPropNameValuesPair(propName);
    			skuPropMap.put(propName.getId(), skuProp);
    			skuProps.add(skuProp);
    		}
    		skuProp.add(propVO.getPropValue());
    	}
    	
    	//按照orderIndex排序
    	for(ProductPropNameValuesPair productPropNameValuesPair : skuProps){
    		
    		Collections.sort(productPropNameValuesPair.getPropValues(), new Comparator<ProductPropValue>(){  
    			
    			public int compare(ProductPropValue o1, ProductPropValue o2) {  
    				if(o1.getOrderIndex() > o2.getOrderIndex()){  
    					return 1;  
    				}  
    				if(o1.getOrderIndex() == o2.getOrderIndex()){  
    					return 0;  
    				}  
    				return -1;  
    			}  
    		}); 
    	}
    	
    	data.put("skuProps", skuProps);
    	data.put("product", product);
    	String remark = product.getRemark();
    	String[] summaryImages = product.getSummaryImageArray();
    	String[] size = product.getSizeTableImageArray();
    	
    	String description = product.getDescription();

    	/* 搭配推荐商品 */
    	String togetherBuyStr = product.getTogether();
    	Set<Long> productIdSet = new HashSet<Long>();
    	if(togetherBuyStr != null && togetherBuyStr.length() > 0){
    		String[] togetherBuyArr = togetherBuyStr.split(",");
    		for(String togetherBuy: togetherBuyArr){
    			try { 
    				productIdSet.add(Long.parseLong(togetherBuy));
				} catch (Exception e) {
    				// ignore ...
				}
    		}
    	}
    	if( !productIdSet.isEmpty()){
    		List<Product> tgProductList = productService.getProductByIds(productIdSet,false);
    		// 过滤未上架商品(补丁) update by Charlie(唐静) 上面接口关联太多, 不建议修改
			if (null != tgProductList){
				tgProductList.removeIf(pdt -> pdt.getStatus() != 6);
			}
    		data.put("togetherProductList", tgProductList);
    	}

    	
    	List<String> imgList = new  ArrayList<String>();
    	
    	String imgHead = Client.OSS_IMG_SERVICE;
    	String regEx = "(" + imgHead + "[^>^http:]*"+ ".[a-z_A-Z_]*)";
    	//String str = "http://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/sdfsdf.png  http://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/sdf dfd/df1212/jpg   http://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/32432 /324.JPG ";    
    	Pattern pat = Pattern.compile(regEx);
    	Matcher mat;
    	
    	if(size != null && size.length > 0){
    		
    		for(int i = 0; i<size.length ; i++){
    			imgList.add(size[i]);
    		}     
    	}
    	
    	if(remark!=null){
    		mat = pat.matcher(remark);
    		while (mat.find()) {
    			imgList.add(mat.group());
    		}      
    		remark = remark.replaceAll("(src=\")(" + imgHead + "[^>^http:]*"+ ".[a-z_A-Z_]*)",  "onclick=\"javascript:window.open('yjj://skuImg/$2')\"; $1$2");
    		
    	}
    	
    	if(description!=null){
    		mat = pat.matcher(description);
    		while (mat.find()) {
    			imgList.add(mat.group());
    		}  
    		
    		description = description.replaceAll("(src=\")(" + imgHead + "[^>^http:]*"+ ".[a-z_A-Z_]*)",  "onclick=\"javascript:window.open('yjj://skuImg/$2')\"; $1$2");
    	}
    	
    	if(summaryImages != null && summaryImages.length > 0){
    		for(int i = 0; i < summaryImages.length ; i++){
    			imgList.add(summaryImages[i]);
    		}        
    	}
    	data.put("imgList", imgList);
    	product.setRemark(remark);
    	product.setDescription(description);
    	
    	long userId = userDetail.getUserId();
    	if (userId > 0) {
    		data.put("userConfig", buildUserMap(userDetail.getUserId(), product.getId()));
    		data.put("restrictInfo", this.restrictInfoOfProduct(userId, CollectionUtil.createSet(product.getId())).get(product.getId()));
    	}
    	
    	Brand brand = brandService.getBrand(product.getBrandId());
    	data.put("brand", brand);
    	
    	
    	//全场满减优惠
    	List<DiscountInfo> allDiscountInfoList = new ArrayList<DiscountInfo>();
		DiscountInfo discountInfoTemp;
		JSONArray jsonArrayDiscount = globalSettingService.getJsonArray(GlobalSettingName.ALL_DISCOUNT);
		if(jsonArrayDiscount != null && jsonArrayDiscount.size() > 0){
			
			for(Object obj : jsonArrayDiscount) {
				discountInfoTemp = new DiscountInfo();
				discountInfoTemp.setFull(Double.parseDouble(((JSONObject)obj).get("full").toString()));
				discountInfoTemp.setMinus(Double.parseDouble(((JSONObject)obj).get("minus").toString()));
				allDiscountInfoList.add(discountInfoTemp);
			}	 
		}
		int allDsctFlag = 0;
		if(allDiscountInfoList.size() > 0){
			data.put("allDiscountList", allDiscountInfoList);
			allDsctFlag = 1;
		}
		data.put("allDsctFlag", allDsctFlag);
		
		if(allDiscountInfoList != null && allDiscountInfoList.size() > 0){
			StringBuilder string = new StringBuilder();
			string.append("全场");
			for(DiscountInfo discountInfo : allDiscountInfoList){
				
				double exceedMoney = discountInfo.getFull();
				if (exceedMoney - (int) exceedMoney < 0.001) {
					string.append(String.format("满%d", (int) exceedMoney));
				} else {
					string.append(String.format("满%.2f", exceedMoney));
				}
				
				double minusMoney = discountInfo.getMinus();
				if (minusMoney - (int) minusMoney < 0.001) {
					string.append(String.format("减%d", (int) minusMoney));
				} else {
					string.append(String.format("减%.2f", minusMoney));
				}
				string.append(" ");
			}	
			data.put("allDsct", string);
		}
		
    	
    	
    	
    	//List<CategoryV0> categoryV0s = productCategoryMapper.getProductVirtualCategory(CollectionUtil.createList(productId));
    	List<CategoryV0> categoryV0s = productCategoryService.getProductVirtualCategoryList(CollectionUtil.createList(product.getId()));
    	if(categoryV0s.size() > 0) {
    		List<DiscountInfo> discountInfoList = shoppingCartFacade.queryDiscountInfoList(0, categoryV0s.get(0).getId());  
    		StringBuilder string = new StringBuilder();
    		if(discountInfoList != null && discountInfoList.size() > 0){
    			for(DiscountInfo discountInfo : discountInfoList){
    				
    				double exceedMoney = discountInfo.getFull();
    				if (exceedMoney - (int) exceedMoney < 0.001) {
    					string.append(String.format("满%d", (int) exceedMoney));
    				} else {
    					string.append(String.format("满%.2f", exceedMoney));
    				}
    				
    				double minusMoney = discountInfo.getMinus();
    				if (minusMoney - (int) minusMoney < 0.001) {
    					string.append(String.format("减%d", (int) minusMoney));
    				} else {
    					string.append(String.format("减%.2f", minusMoney));
    				}
    				string.append(" ");
    			}	
    		}
    		
			data.put("discountDescMulti", string);
			
    		
    		
    		double exceedMoney = categoryV0s.get(categoryV0s.size()-1).getExceedMoney();
    		string = new StringBuilder();
    		if (exceedMoney - (int) exceedMoney < 0.001) {
    			string.append(String.format("满%d", (int) exceedMoney));
    		} else {
    			string.append(String.format("满%.2f", exceedMoney));
    		}
    		
    		double minusMoney = categoryV0s.get(categoryV0s.size()-1).getMinusMoney();
    		if (minusMoney - (int) minusMoney < 0.001) {
    			string.append(String.format("减%d", (int) minusMoney));
    		} else {
    			string.append(String.format("减%.2f", minusMoney));
    		}
    		
    		data.put("virtualCategory", categoryV0s.get(0));
    		if(categoryV0s.get(0).getIsDiscount() == 1) {
    			data.put("discountDesc", string);
    		}
    		
    	} else {
    		List<DiscountInfo> discountInfoList = shoppingCartFacade.queryDiscountInfoList(1, brand.getBrandId());  
    		StringBuilder string = new StringBuilder();
    		if(discountInfoList != null && discountInfoList.size() > 0){
    			for(DiscountInfo discountInfo : discountInfoList){
    				
    				double exceedMoney = discountInfo.getFull();
    				if (exceedMoney - (int) exceedMoney < 0.001) {
    					string.append(String.format("满%d", (int) exceedMoney));
    				} else {
    					string.append(String.format("满%.2f", exceedMoney));
    				}
    				
    				double minusMoney = discountInfo.getMinus();
    				if (minusMoney - (int) minusMoney < 0.001) {
    					string.append(String.format("减%d", (int) minusMoney));
    				} else {
    					string.append(String.format("减%.2f", minusMoney));
    				}
    				string.append(" ");
    			}	
    		}
    		//data.put("virtualCategory", null);
    		data.put("discountDescMulti", string);
    		
    		double exceedMoney = brand.getExceedMoney();
    		string = new StringBuilder();
    		if (exceedMoney - (int) exceedMoney < 0.001) {
    			string.append(String.format("满%d", (int) exceedMoney));
    		} else {
    			string.append(String.format("满%.2f", exceedMoney));
    		}
    		
    		double minusMoney = brand.getMinusMoney();
    		if (minusMoney - (int) minusMoney < 0.001) {
    			string.append(String.format("减%d", (int) minusMoney));
    		} else {
    			string.append(String.format("减%.2f", minusMoney));
    		}
    		
    		data.put("virtualCategory", null);
    		if(brand.getIsDiscount() == 1) {
    			data.put("discountDesc", string);
    		}
    	}
    	
    	if(product.getRestrictDayBuy() != -1 || product.getRestrictHistoryBuy() != -1) {
    		String historyDesc = product.getRestrictHistoryBuy() == -1 ? "" : product.getRestrictCycle() + "天限购" + product.getRestrictHistoryBuy() + "件";
    		String dayDesc = product.getRestrictDayBuy() == -1 ? "" : "单日限购" + product.getRestrictDayBuy() + "件";
    		data.put("restrictDesc", historyDesc + " " + dayDesc);
    	}
    	
    	int aveLiker = 0;
    	List<CommentVO> comments = commentFacade.getCommentList(product.getId(), new PageQuery(1, 1));
    	for (CommentVO commentVO : comments) {
    		aveLiker += commentVO.getLiker();
    	}
    	if (comments.size() != 0)
    		aveLiker /= comments.size();
    	data.put("aveLiker", aveLiker);
    	data.put("comments", comments);
    	Subscript subscript = new Subscript();
    	if(product.getSubscriptId() > 0){
    		subscript = productService.getSubscriptById(product.getSubscriptId());
    	}
    	data.put("subscript", subscript);
    	
    	// 获取后台设置的值 
    	
		JSONObject recommendedProduct = globalSettingService.getJsonObject(GlobalSettingName.RECOMMENDED_PRODUCT);
		
		if (recommendedProduct != null) {
			JSONObject jsonObjbuyGuess = recommendedProduct.getJSONObject("product_guess_like");
			int count = 0;
			try {
				count = jsonObjbuyGuess.getIntValue("count");
			} catch (Exception e) {
				count = 4;
			}
			if (count <= 0) count = 4; 
			jsonObjbuyGuess.put("buyGuessProduct", productService.getProductList15(dataminingAdapter.getBuyGuessProduct186(userId > 0 ? userId : 0, new PageQuery(1, count))));

			JSONObject jsonObjSeeAgain = recommendedProduct.getJSONObject("product_see_again");
			try {
				count = jsonObjSeeAgain.getIntValue("count");
			} catch (Exception e) {
				count = 4;
			}
			if (count <= 0) count = 4; 
			jsonObjSeeAgain.put("seeAgainProduct", productService.getProductList15(dataminingAdapter.getSeeAgainProduct186(product.getId(), new PageQuery(1, count))));
		}
		
    	data.put("recommendedProduct", recommendedProduct);

    	
    	data.put("buyGuessProduct", productService.getProductList15(dataminingAdapter.getBuyGuessProduct186(userId > 0 ? userId : 0, new PageQuery(1, 4))));
    	data.put("seeAgainProduct", productService.getProductList15(dataminingAdapter.getSeeAgainProduct186(product.getId(), new PageQuery(1, 4))));
    	data.put("buyGuessTitle", "你喜欢");
    	data.put("seeAgainTitle", "看又看");
    	
    	
    	//首单优惠
    	//double firstDiscount = 19.9;
    	double firstDiscountCash = globalSettingService.getDouble(GlobalSettingName.FIRST_DISCOUNT);
    	//firstDiscountCash = 19.9;
    	data.put("firstDiscountCash", firstDiscountCash);
    	data.put("firstDiscountDesc", "新用户首单立减￥" + firstDiscountCash);
    	//app下单省钱
    	double appSaveMoney = 1;
    	String appSaveWords = "APP下单更优惠";
    	if(appSaveMoney > 0){
    		data.put("appSaveMoney", appSaveMoney);
        	data.put("appSaveWords", appSaveWords);
    	}else{
    		data.put("appSaveMoney", "");
        	data.put("appSaveWords", "");
    	}
    	
    	String weixinTitle = "俞姐姐";
    	data.put("weixinTitle", weixinTitle);
    	if(product.getDeductPercent() > 0){
    		DecimalFormat    df   = new DecimalFormat("##.##");   
    		String deductStr = (String) globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_DEDUCTION_SETTING).get("activityText");
    		data.put("deductDesc", deductStr + df.format(product.getDeductPercent()) + "%");
    	}
    	
    	return data;
    }
	
    private Map<String, Object> buildUserMap(long userId, long productId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("favorite", favoriteService.getFavorite(userId, productId) != null);
        return map;
    }
    
	public String restrictDetail(RestrictProductVO rProductVO) {
		String description;
    	if(rProductVO.getHistorySum() + rProductVO.getBuyCount() > rProductVO.getRestrictHistoryBuy()) {
    		description ="本商品" + rProductVO.getRestrictCycle() + "天内限购" + rProductVO.getRestrictHistoryBuy() + "件，小朋友不要太调皮啦，给其他小伙伴一点机会吧！";
    	} else {
    		description = "本商品今日限购" + rProductVO.getRestrictDayBuy() +"件，明天再买吧～赶紧先收藏一下";
    	}
    	
    	return description;
	}
    
    public Map<String, Object> getBrandList(String brandName, UserDetail userDetail, PageQuery pageQuery, int type) {
    	
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	Set<Long> brands = new HashSet<Long>();
    	List<BrandVO> brandList = brandService.getBrandListShow(brandName, pageQuery, type, userDetail.getUserId() );
    	if(brandName != null && brandName.trim().length() > 0){
    		for (BrandVO brandVO : brandList) {
    			brands.add(brandVO.getBrandId());
    		}	
    	}
    	Map<Long, List<ProductVO>> productMaps = productService.getProductGroupsByBrandIds(brands, userDetail, type);
    	Iterator<BrandVO> stuIter = brandList.iterator();  
    	while (stuIter.hasNext()) {  
    		BrandVO brandVO = stuIter.next();  
    		brandVO.setProducts(ProductUtil.getLimitNumProductSimpleList(productMaps.get(brandVO.getBrandId()), 3));
    		if(brandVO.getProducts() == null || brandVO.getProducts().size() == 0){
    			stuIter.remove();
    			continue;
    		}
    		if(productMaps.get(brandVO.getBrandId()) != null){
    			brandVO.setProductNum(productMaps.get(brandVO.getBrandId()).size());
    		}
    	} 
    	
    	
//    	for (BrandVO brandVO : brandList) {
//    		brandVO.setProductNum(0);
//    		if(productMaps.get(brandVO.getBrandId()) != null){
//    			brandVO.setProductNum(productMaps.get(brandVO.getBrandId()).size());
//    		}
//    		brandVO.setProducts(ProductUtil.getLimitNumProductSimpleList(productMaps.get(brandVO.getBrandId()), 3));
//		}
 //   	
    
    	map.put("brandList", brandList);
    	return map;
    }
    
}
