package com.yujj.web.controller.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.order.RestrictProductVO;
import com.jiuyuan.entity.shopping.CartItem;
import com.jiuyuan.entity.shopping.CartItemVO;
import com.jiuyuan.entity.shopping.DiscountInfo;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.assembler.ProductPropAssembler;
import com.yujj.business.facade.ProductFacade;
import com.yujj.business.facade.ShoppingCartFacade;
import com.yujj.business.service.BrandService;
import com.yujj.business.service.CategoryService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.OrderService;
import com.yujj.business.service.ProductSKUService;
import com.yujj.business.service.ShoppingCartService;
import com.yujj.entity.Brand;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.product.Category;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductPropName;
import com.yujj.entity.product.ProductPropNameValuesPair;
import com.yujj.entity.product.ProductPropVO;
import com.yujj.entity.product.ProductSKU;
import com.yujj.web.controller.mobile.MobileProductController;

/**
 * 
 * @author LWS
 *
 */

@Component
public class ShoppingCartDelegator {
	
	private static final Logger logger = Logger.getLogger(MobileProductController.class);
	
	@Autowired
	private ObjectMapper objectMapper;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ShoppingCartFacade shoppingCartFacade;
    
    @Autowired
    private ProductPropAssembler productPropAssembler;
    
    @Autowired
    private BrandService brandService;
    
    @Autowired
    private ProductSKUService productSKUService;
    
    @Autowired
    private ProductFacade productFacade;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private OrderService orderService;

    
    @Autowired
    private GlobalSettingService globalSettingService;

    public Map<String, Object> getCart(UserDetail userDetail) {
        long userId = userDetail.getUserId();

        List<CartItemVO> cartItems = null;
        cartItems = shoppingCartFacade.getCartItemVOs(userId);
        if(cartItems == null) {
        	return new HashMap<String, Object>();
        }
        
        List<Long> brandIds = null;
        brandIds = shoppingCartFacade.getBranIds(userId);

        Map<String, Object> data = new HashMap<String, Object>();
        List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
        data.put("count", cartItems.size());
        data.put("brandids", brandIds);
        data.put("items", map);
        data.put("ad", shoppingCartFacade.getAd(userId));
        
        for(int i = 0; i < brandIds.size(); i++) {
        	Brand brand = brandService.getBrand(brandIds.get(i));
        	if(brand == null) {
        		brand = new Brand();
        		brand.setBrandName("无品牌");
        		brand.setLogo("default.png");
    		}
        	
        	Map<String, Object> entry = new HashMap<String, Object>();
        	List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
        	for (CartItemVO cartItem : cartItems) {
        		Product product = cartItem.getProduct();
        		if(product.getBrandId() == brandIds.get(i)) {
	        		Map<String, Object> productMap = new HashMap<String, Object>();
	        		productMap.put("productId", product.getId());
	        		productMap.put("productName", product.getName());
	        		productMap.put("productImage", product.getDetailImageArray().length > 0 ? product.getDetailImageArray()[0]
	        				: "");
                    productMap.put("marketPrice", product.getMarketPrice());
	        		productMap.put("brandId", product.getBrandId());
	        		
	        		ProductSKU sku = cartItem.getSku();
	        		List<ProductPropVO> skuProps = cartItem.getSkuProps();
	        		Map<String, Object> skuMap = new HashMap<String, Object>();
	        		skuMap.put("skuId", sku.getId());
	        		skuMap.put("price", sku.getPrice());
	        		skuMap.put("remainCount", sku.getRemainCount());
	        		skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));
        		
        			Map<String, Object> itemMap = new HashMap<String, Object>();
        			itemMap.put("id", cartItem.getId());
        			itemMap.put("product", productMap);
        			itemMap.put("sku", skuMap);
        			itemMap.put("buyCount", cartItem.getBuyCount());
        			itemMap.put("isSelected", cartItem.getIsSelected());
        			itemMap.put("skuProps", buildSkuProps(product.getId()).get("skuProps"));
        			itemMap.put("skuMap", buildSkuProps(product.getId()).get("skuMap"));
        			itemList.add(itemMap);
        		}
        	}
        	entry.put("item", itemList);
        	entry.put("brandName", brand.getBrandName());
        	entry.put("brandIdentity", brand.getBrandIdentity());
        	entry.put("logo", "http://www.yujiejie.com/static/app/logo/"+brand.getBrandId()+".png");
        	map.add(entry);
        }

        return data;
    }

    private Map<String, Object> buildSkuProps(long productId) {
    	 List<ProductSKU> skus = productSKUService.getProductSKUsOfProduct(productId);
         Map<String, ProductSKU> skuMap = new HashMap<String, ProductSKU>();
         List<ProductPropVO> productPropVOs = new ArrayList<ProductPropVO>();
         Map<String, Object> data = new HashMap<String, Object>();
         
         for (ProductSKU sku : skus) {
             skuMap.put(sku.getPropertyIds(), sku);
             productPropVOs.addAll(sku.getProductProps());
         }
         
         try {
             data.put("skuMap", objectMapper.writeValueAsString(skuMap));
         } catch (JsonProcessingException e) {
             logger.error("parsing sku map error!");
         }

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
         data.put("skuProps", skuProps);
         return data;
	}

	private String buildSkuSnapshot(List<ProductPropVO> skuProps) {
        StringBuilder builder = new StringBuilder();
        for (ProductPropVO prop : skuProps) {
            builder.append(prop.toString());
            builder.append("  ");
        }
        return builder.toString();
    }

    public ResultCode addCart(long productId, long skuId, int count, UserDetail userDetail, long statisticsId, String logIds) {
        long userId = userDetail.getUserId();
        long time = System.currentTimeMillis();
        
        CartItem cartItem = shoppingCartService.getCartItem(userId, productId, skuId);
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setSkuId(skuId);
            cartItem.setBuyCount(count);
            cartItem.setStatus(0);
            cartItem.setCreateTime(time);
            cartItem.setUpdateTime(time);
            cartItem.setStatisticsId(statisticsId);
            cartItem.setLogIds(logIds);
            shoppingCartService.addCartItem(cartItem);
            
            return ResultCode.COMMON_SUCCESS;
        }
        shoppingCartService.addCount(userId, productId, skuId, count, time);
        
        return ResultCode.COMMON_SUCCESS;
    }

    public boolean removeCart(long productId, long skuId, int count, UserDetail userDetail) {
        long userId = userDetail.getUserId();
        CartItem cartItem = shoppingCartService.getCartItem(userId, productId, skuId);
        if (cartItem == null) {
            return false;
        } else if (count >= cartItem.getBuyCount()) {
            shoppingCartService.removeCartItem(userId, productId, skuId);
        } else {
            long time = System.currentTimeMillis();
            shoppingCartService.addCount(userId, productId, skuId, -count, time);
        }
        return true;
    }

    public boolean deleteCart(long userId, long id) {
        CartItem cartItem = shoppingCartService.getCartItemById(id);
        if (cartItem == null) {
            return false;
        } else {
            shoppingCartService.removeCartItem(userId, id);
        }
        return true;
	}

	@Transactional(rollbackFor = Exception.class)
	public JsonResponse saveProductsInCart(String cartItemsJson, UserDetail userDetail) {
//		新增代码
		JsonResponse jsonResponse = new JsonResponse();
		
		long userId = userDetail.getUserId();

        List<CartItem> list = JSON.parseArray(cartItemsJson, CartItem.class);
        List<CartItem> list_sort = sortSkuId(list);
        List<CartItem> list_cartItem = list_sort; 
        List<Long> repeatIds = new ArrayList<>();
        List<Integer> repeatIndex = new ArrayList<>();
        
        for (int i = 0; i < list_sort.size()-1; i++) {
        	CartItem cartItem0 = (CartItem) list_sort.get(i);
        	CartItem cartItem1 = (CartItem) list_sort.get(i+1);
        	
        	if(cartItem0.getSkuId() == cartItem1.getSkuId()) {
        		repeatIds.add(cartItem0.getId());
        		repeatIndex.add(i);
        		cartItem1.setBuyCount(cartItem0.getBuyCount() + cartItem1.getBuyCount());
        		cartItem1.setIsSelected(cartItem0.getIsSelected() & cartItem1.getIsSelected());
        	}
        }
        
//        限购
        if(list.size() > 0) {
        	Map<Long, Integer> productCountMap = getProductCountMap(sortProductId(list));
        	List<RestrictProductVO> products = productFacade.getRestrictInfo(userId, productCountMap);
        	if(products.size() > 0) {
        		String description = productFacade.restrictDetail(products.get(0));
        		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_RESTRICT).setData(products).setError(description);
        	}
        }
        
        //删除重复数据
        for(int i = repeatIds.size(); i > 0; i--) {
        	list_cartItem.remove(repeatIndex.get(i-1));
            shoppingCartService.removeCartItem(userId, repeatIds.get(i - 1));
        }
        
        //list数据处理完,填充数据
        for(CartItem cartItem : list_cartItem) {
        	shoppingCartService.saveProductInCart(cartItem.getId(), userId, cartItem.getProductId(), cartItem.getSkuId(), cartItem.getBuyCount(), cartItem.getIsSelected(), System.currentTimeMillis());
        }
        
        return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}

	private Map<Long, Integer> getProductCountMap(List<CartItem> sortList) {
		Map<Long, Integer> productCountMap = new HashMap<Long, Integer>();
		long lastProductId = sortList.get(0).getProductId();
        int buyCount = 0;
        
        for (CartItem cartItem : sortList) {
            if(cartItem.getProductId() != lastProductId) {
            	productCountMap.put(lastProductId, buyCount);
            	
            	lastProductId = cartItem.getProductId();
            	buyCount = 0;
            }
            buyCount += cartItem.getBuyCount();
        }
        
        //最后一种productId没被加进去
        productCountMap.put(lastProductId, buyCount);
        
        return productCountMap;
	}

	private List<CartItem> sortProductId(List<CartItem> list) {
		Collections.sort(list, new Comparator<CartItem>() {
            public int compare(CartItem arg0, CartItem arg1) {
                long productId0 = (long) arg0.getProductId();
                long productId1 = (long) arg1.getProductId();
                if (productId0 > productId1) {
                    return 1;
                } else if (productId0 == productId1) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
		
		return list;
	}

	private List<CartItem> sortSkuId(List<CartItem> list) {
		Collections.sort(list, new Comparator<CartItem>() {
            public int compare(CartItem arg0, CartItem arg1) {
            	long skuid0 = (long) arg0.getSkuId();
            	long skuid1 = (long) arg1.getSkuId();
                if (skuid0 > skuid1) {
                    return 1;
                } else if (skuid0 == skuid1) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
		
		return list;
	}

	public Map<String, Object> getCart17(UserDetail userDetail) {
        Map<String, Object> data = new HashMap<String, Object>();
		long userId = 0;
		
		if(userDetail.getUser() == null) {
	        data.put("ad", shoppingCartFacade.getAd(userId));
	        return data;
		}
		userId = userDetail.getUserId();
		
        List<CartItemVO> cartItems = null;
        cartItems = shoppingCartFacade.getCartItemVOs(userId);
        if(cartItems == null) {
        	cartItems = new ArrayList<CartItemVO>();
        }
        
        //获取购物车商品包含的品牌
        Set<Long> brandIds = new HashSet<Long>();

        List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
        data.put("count", cartItems.size());
        data.put("brandids", brandIds);
        data.put("items", groupList);
        data.put("ad", shoppingCartFacade.getAd(userId));
        
        //取出cartItems中分类为虚拟分类的封装。取出cartItems中分类为品牌分类,封装
        Set<Long> virtualCategoryId = new HashSet<Long>();
        Set<Long> productIds = new HashSet<Long>();
        List<CartItemVO> virtualCartItems = new ArrayList<CartItemVO>();
        List<CartItemVO> normalCartItems = new ArrayList<CartItemVO>();
        
        for (CartItemVO cartItem : cartItems) {
        	productIds.add(cartItem.getProductId());
        	if(cartItem.getCategory() != null) {
        		virtualCategoryId.add(cartItem.getCategory().getId());
        		virtualCartItems.add(cartItem);
        	} else {
        		brandIds.add(cartItem.getProduct().getBrandId());
        		normalCartItems.add(cartItem);
        	}
        }
        
        //限购信息 Map<ProductId, RestrictProductVO>
        Map<Long, RestrictProductVO> restrictInfo = new HashMap<Long, RestrictProductVO>();
        if(productIds.size() > 0) {
        	restrictInfo = productFacade.restrictInfoOfProduct(userId, productIds);
        }
        
        for(Iterator<Long> iterator = virtualCategoryId.iterator(); iterator.hasNext();) {
        	Long iteratorCatId = iterator.next();
        	Category category = categoryService.getCategoryById(iteratorCatId);
        	
        	if(category == null) {
        		category = new Category();
        		
        		category.setId(0);
        		category.setCategoryName("无此分类");
        		category.setIsDiscount(0);
        		category.setExceedMoney(0);
        		category.setMinusMoney(0);
    		}

        	Map<String, Object> groupMap = new HashMap<String, Object>();
        	List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
        	for(CartItemVO cartItem : virtualCartItems) {
        		long categoryId = cartItem.getCategory().getId();
        		
        		if(categoryId == iteratorCatId) {
        			Product product = cartItem.getProduct();
        			Map<String, Object> productMap = new HashMap<String, Object>();
        			
	        		productMap.put("productId", product.getId());
	        		productMap.put("productName", product.getName());
	        		productMap.put("productImage", product.getDetailImageArray().length > 0 ? product.getDetailImageArray()[0] : "");
                    productMap.put("marketPrice", product.getMarketPrice());
	        		productMap.put("brandId", product.getBrandId());
	        		productMap.put("bottomPrice", product.getBottomPrice());
	        		productMap.put("marketPriceMin", product.getMarketPriceMin());
	        		productMap.put("marketPriceMax", product.getMarketPriceMax());
	        		productMap.put("payAmountInCents", product.getPayAmountInCents());
	        		productMap.put("cash", product.getCurrenCash()); 
	        		productMap.put("jiuCoin", product.getCurrentJiuCoin());
	        		productMap.put("validate", product.isOnSaling());
	        		productMap.put("restrictInfo", restrictInfo.get(product.getId()));
	        		
	        		ProductSKU sku = cartItem.getSku();
	        		List<ProductPropVO> skuProps = cartItem.getSkuProps();
	        		Map<String, Object> skuMap = new HashMap<String, Object>();
	        		skuMap.put("skuId", sku.getId());
	        		skuMap.put("price", sku.getPrice());
	        		skuMap.put("remainCount", sku.getRemainCount());
	        		skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));
        		
        			Map<String, Object> itemMap = new HashMap<String, Object>();
        			itemMap.put("id", cartItem.getId());
        			itemMap.put("product", productMap);
        			itemMap.put("brand", brandService.getBrand(product.getBrandId()).toSimpleMap());
        			itemMap.put("sku", skuMap);
        			itemMap.put("buyCount", cartItem.getBuyCount());
        			itemMap.put("isSelected", cartItem.getIsSelected());
        			itemMap.put("skuProps", buildSkuProps(product.getId()).get("skuProps"));
        			itemMap.put("skuMap", buildSkuProps(product.getId()).get("skuMap"));
        			itemList.add(itemMap);
        		}
        	}
        	
        	groupMap.put("item", itemList);
        	//新增字段,判断是否是虚拟分类
        	groupMap.put("isVirtualCategory", true);
        	//虚拟分类专属字段,虚拟分类的优惠信息
        	groupMap.put("categoryDiscount", category.toDiscountMap());
        	groupList.add(groupMap);
        }
        
        // 将cartItems里的商品按照品牌id封装
        for(Iterator<Long> brandIdIterator = brandIds.iterator(); brandIdIterator.hasNext();) {
        	long brandId = brandIdIterator.next(); 
        	Brand brand = brandService.getBrand(brandId);
        	if(brand == null) {
        		brand = new Brand();
        		brand.setBrandName("无品牌");
        		brand.setLogo("default.png");
    		}
        	
        	Map<String, Object> entry = new HashMap<String, Object>();
        	List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
        	for (CartItemVO cartItem : normalCartItems) {
        		Product product = cartItem.getProduct();
        		if(product.getBrandId() == brandId) {
	        		Map<String, Object> productMap = new HashMap<String, Object>();
	        		productMap.put("productId", product.getId());
	        		productMap.put("productName", product.getName());
	        		productMap.put("productImage", product.getDetailImageArray().length > 0 ? product.getDetailImageArray()[0]
	        				: "");
                    productMap.put("marketPrice", product.getMarketPrice());
	        		productMap.put("brandId", product.getBrandId()); 
	        		productMap.put("bottomPrice", product.getBottomPrice());
	        		productMap.put("marketPriceMin", product.getMarketPriceMin());
	        		productMap.put("marketPriceMax", product.getMarketPriceMax());
	        		productMap.put("payAmountInCents", product.getPayAmountInCents());
	        		productMap.put("cash", product.getCurrenCash());
	        		productMap.put("jiuCoin", product.getCurrentJiuCoin());
	        		productMap.put("validate", product.isOnSaling());
	        		productMap.put("restrictInfo", restrictInfo.get(product.getId()));
	        		
	        		ProductSKU sku = cartItem.getSku();
	        		List<ProductPropVO> skuProps = cartItem.getSkuProps();
	        		Map<String, Object> skuMap = new HashMap<String, Object>();
	        		skuMap.put("skuId", sku.getId());
	        		skuMap.put("price", sku.getPrice());
	        		skuMap.put("remainCount", sku.getRemainCount());
	        		skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));
        		
        			Map<String, Object> itemMap = new HashMap<String, Object>();
        			itemMap.put("id", cartItem.getId());
        			itemMap.put("product", productMap);
        			itemMap.put("sku", skuMap);
        			itemMap.put("buyCount", cartItem.getBuyCount());
        			itemMap.put("isSelected", cartItem.getIsSelected());
        			itemMap.put("skuProps", buildSkuProps(product.getId()).get("skuProps"));
        			itemMap.put("skuMap", buildSkuProps(product.getId()).get("skuMap"));
        			itemList.add(itemMap);
        		}
        	}
        	entry.put("item", itemList);
        	//新增字段,判断是否是虚拟分类
        	entry.put("isVirtualCategory", false);
        	//按品牌分类的专属字段,品牌的优惠信息
        	entry.put("brand", brand.toSimpleMap());
        	groupList.add(entry);
        }

        return data;
	}
	
	public Map<String, Object> getCart185(UserDetail userDetail) {
		Map<String, Object> data = new HashMap<String, Object>();
		long userId = 0;
		
		if(userDetail.getUser() == null) {

			data.put("ad", shoppingCartFacade.getAd(userId));
			return data;
		}
		userId = userDetail.getUserId();
		
		List<CartItemVO> cartItems = null;
		cartItems = shoppingCartFacade.getCartItemVOs(userId);
		if(cartItems == null) {
			cartItems = new ArrayList<CartItemVO>();
		}
		
		//获取购物车商品包含的品牌
		Set<Long> brandIds = new HashSet<Long>();
		double firstDiscount = globalSettingService.getDouble(GlobalSettingName.FIRST_DISCOUNT);
		
		List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
		data.put("count", cartItems.size());
		data.put("brandids", brandIds);
		data.put("ad", shoppingCartFacade.getAd(userId));
		data.put("firstDiscount", firstDiscount);
		data.put("firstDiscountTips", "新用户首单" + firstDiscount + "元优惠");
		
		int count = orderService.getUserOrderCountForFirstDiscount(userId);
		if (count == 0 ){
			data.put("firstDiscountAble", "YES");
		}else {
			data.put("firstDiscountAble", "NO");
		}
		
		
		//取出cartItems中分类为虚拟分类的封装。取出cartItems中分类为品牌分类,封装
		Set<Long> virtualCategoryId = new HashSet<Long>();
		Set<Long> productIds = new HashSet<Long>();
		List<CartItemVO> virtualCartItems = new ArrayList<CartItemVO>();
		List<CartItemVO> normalCartItems = new ArrayList<CartItemVO>();
		
		for (CartItemVO cartItem : cartItems) {
			productIds.add(cartItem.getProductId());
			if(cartItem.getCategory() != null) {
				virtualCategoryId.add(cartItem.getCategory().getId());
				virtualCartItems.add(cartItem);
			} else {
				brandIds.add(cartItem.getProduct().getBrandId());
				normalCartItems.add(cartItem);
			}
		}
		//取全场满减信息
		Map<String, Object> allGroupMap = new HashMap<String, Object>();
		List<Map<String, Object>> allItemList = new ArrayList<Map<String, Object>>();
		List<DiscountInfo> discountInfoList = new ArrayList<DiscountInfo>();
		List<DiscountInfo> allDiscountInfoList = new ArrayList<DiscountInfo>();
		DiscountInfo discountInfo;
		JSONArray jsonArrayDiscount = globalSettingService.getJsonArray(GlobalSettingName.ALL_DISCOUNT);
		if(jsonArrayDiscount != null && jsonArrayDiscount.size() > 0){
			
			for(Object obj : jsonArrayDiscount) {
				discountInfo = new DiscountInfo();
				discountInfo.setFull(Double.parseDouble(((JSONObject)obj).get("full").toString()));
				discountInfo.setMinus(Double.parseDouble(((JSONObject)obj).get("minus").toString()));
				allDiscountInfoList.add(discountInfo);
			}	 
		}
		int allDsctFlag = 0;
		if(allDiscountInfoList.size() > 0){
			data.put("allDiscountList", allDiscountInfoList);
			data.put("allGroupMap", allGroupMap);
			allDsctFlag = 1;
		}else{
			data.put("items", groupList);
		}
		data.put("allDsctFlag", allDsctFlag);
		
		//限购信息 Map<ProductId, RestrictProductVO>
		Map<Long, RestrictProductVO> restrictInfo = new HashMap<Long, RestrictProductVO>();
		if(productIds.size() > 0) {
			restrictInfo = productFacade.restrictInfoOfProduct(userId, productIds);
		}
		
		Map<Long, List<DiscountInfo>> discountInfoMap =new HashMap<Long, List<DiscountInfo>>();
		if(virtualCategoryId.size() > 0 && allDiscountInfoList.size() == 0){
			discountInfoMap = shoppingCartFacade.queryDiscountInfoListMap(0, virtualCategoryId);  		
		}
		
		
		for(Iterator<Long> iterator = virtualCategoryId.iterator(); iterator.hasNext();) {
			Long iteratorCatId = iterator.next();
			Category category = categoryService.getCategoryById(iteratorCatId);
			
			if(category == null) {
				category = new Category();
				
				category.setId(0);
				category.setCategoryName("无此分类");
				category.setIsDiscount(0);
				category.setExceedMoney(0);
				category.setMinusMoney(0);
			}
			
			Map<String, Object> groupMap = new HashMap<String, Object>();
			List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
			
			
			
			for(CartItemVO cartItem : virtualCartItems) {
				long categoryId = cartItem.getCategory().getId();
				
				if(categoryId == iteratorCatId) {
					Product product = cartItem.getProduct();
					Map<String, Object> productMap = new HashMap<String, Object>();
					
					productMap.put("productId", product.getId());
					productMap.put("productName", product.getName());
					productMap.put("productImage", product.getDetailImageArray().length > 0 ? product.getDetailImageArray()[0] : "");
					productMap.put("marketPrice", product.getMarketPrice());
					productMap.put("brandId", product.getBrandId());
					productMap.put("bottomPrice", product.getBottomPrice());
					productMap.put("marketPriceMin", product.getMarketPriceMin());
					productMap.put("marketPriceMax", product.getMarketPriceMax());
					productMap.put("payAmountInCents", product.getPayAmountInCents());
					productMap.put("cash", product.getCurrenCash()); 
					productMap.put("jiuCoin", product.getCurrentJiuCoin());
					productMap.put("validate", product.isOnSaling());
					productMap.put("restrictInfo", restrictInfo.get(product.getId()));
					productMap.put("promotionStartTime", product.getPromotionStartTime());
					productMap.put("promotionEndTime", product.getPromotionEndTime());
					
					ProductSKU sku = cartItem.getSku();
					List<ProductPropVO> skuProps = cartItem.getSkuProps();
					Map<String, Object> skuMap = new HashMap<String, Object>();
					skuMap.put("skuId", sku.getId());
					skuMap.put("price", sku.getPrice());
					skuMap.put("remainCount", sku.getRemainCount());
					skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));
					
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("id", cartItem.getId());
					itemMap.put("product", productMap);
					if(product.getBrandId() > 0){
						Brand brand = brandService.getBrand(product.getBrandId());
						if(brand != null ){
							itemMap.put("brand", brand.toSimpleMap());	
						}
					}
					itemMap.put("sku", skuMap);
					itemMap.put("buyCount", cartItem.getBuyCount());
					itemMap.put("isSelected", cartItem.getIsSelected());
					itemMap.put("skuProps", buildSkuProps(product.getId()).get("skuProps"));
					itemMap.put("skuMap", buildSkuProps(product.getId()).get("skuMap"));
					itemMap.put("statistics_id", cartItem.getStatisticsId());
					itemList.add(itemMap);
					allItemList.add(itemMap);
				}
			}
			
			groupMap.put("item", itemList);
			//新增字段,判断是否是虚拟分类
			groupMap.put("isVirtualCategory", true);
			//虚拟分类专属字段,虚拟分类的优惠信息@
			groupMap.put("categoryDiscount", category.toDiscountMap());
			
			discountInfoList = new ArrayList<DiscountInfo>();
			discountInfoList = discountInfoMap.get(category.getId());
			
			//虚拟分类复合优惠信息列表
			groupMap.put("categoryDiscountList", discountInfoList);
			
			groupList.add(groupMap);
		}
		
		Map<Long, List<DiscountInfo>> discountInfoMapBrand = new HashMap<Long, List<DiscountInfo>>();
		if(brandIds != null && brandIds.size() > 0 && allDiscountInfoList.size() == 0){ //@
			discountInfoMapBrand = shoppingCartFacade.queryDiscountInfoListMap(1, brandIds);	
		}
		
		// 将cartItems里的商品按照品牌id封装
		for(Iterator<Long> brandIdIterator = brandIds.iterator(); brandIdIterator.hasNext();) {
			long brandId = brandIdIterator.next(); 
			Brand brand = brandService.getBrand(brandId);
			if(brand == null) {
				brand = new Brand();
				brand.setBrandName("无品牌");
				brand.setLogo("default.png");
			}
			
			Map<String, Object> entry = new HashMap<String, Object>();
			List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
			for (CartItemVO cartItem : normalCartItems) {
				Product product = cartItem.getProduct();
				if(product.getBrandId() == brandId) {
					Map<String, Object> productMap = new HashMap<String, Object>();
					productMap.put("productId", product.getId());
					productMap.put("productName", product.getName());
					productMap.put("productImage", product.getDetailImageArray().length > 0 ? product.getDetailImageArray()[0]
							: "");
					productMap.put("marketPrice", product.getMarketPrice());
					productMap.put("brandId", product.getBrandId()); 
					productMap.put("bottomPrice", product.getBottomPrice());
					productMap.put("marketPriceMin", product.getMarketPriceMin());
					productMap.put("marketPriceMax", product.getMarketPriceMax());
					productMap.put("payAmountInCents", product.getPayAmountInCents());
					productMap.put("cash", product.getCurrenCash());
					productMap.put("jiuCoin", product.getCurrentJiuCoin());
					productMap.put("validate", product.isOnSaling());
					productMap.put("restrictInfo", restrictInfo.get(product.getId()));
					
					productMap.put("promotionStartTime", product.getPromotionStartTime());
					productMap.put("promotionEndTime", product.getPromotionEndTime());
					
					ProductSKU sku = cartItem.getSku();
					List<ProductPropVO> skuProps = cartItem.getSkuProps();
					Map<String, Object> skuMap = new HashMap<String, Object>();
					skuMap.put("skuId", sku.getId());
					skuMap.put("price", sku.getPrice());
					skuMap.put("remainCount", sku.getRemainCount());
					skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));
					
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("id", cartItem.getId());
					itemMap.put("product", productMap);
					itemMap.put("sku", skuMap);
					itemMap.put("buyCount", cartItem.getBuyCount());
					itemMap.put("isSelected", cartItem.getIsSelected());
					itemMap.put("skuProps", buildSkuProps(product.getId()).get("skuProps"));
					itemMap.put("skuMap", buildSkuProps(product.getId()).get("skuMap"));
					itemMap.put("statistics_id", cartItem.getStatisticsId());
					itemList.add(itemMap);
					allItemList.add(itemMap);
				}
			}
			entry.put("item", itemList);
			//新增字段,判断是否是虚拟分类
			entry.put("isVirtualCategory", false);
			//按品牌分类的专属字段,品牌的优惠信息
			entry.put("brand", brand.toSimpleMap());
			
			discountInfoList = new ArrayList<DiscountInfo>();
			discountInfoList = discountInfoMapBrand.get(brand.getBrandId());
			
			//优惠信息列表
			entry.put("brandDiscountList", discountInfoList);
			
			groupList.add(entry);
		}
		allGroupMap.put("item", allItemList);
		
		return data;
	}
	
	
	
	public Map<String, Object> getCart572(UserDetail userDetail) {
		Map<String, Object> data = new HashMap<String, Object>();
		long userId = 0;
		
		if(userDetail.getUser() == null) {
			data.put("ad", shoppingCartFacade.getAd(userId));
			return data;
		}
		userId = userDetail.getUserId();
		
		List<CartItemVO> cartItems = null;
		cartItems = shoppingCartFacade.getCartItemVOs(userId);
		if(cartItems == null) {
			cartItems = new ArrayList<CartItemVO>();
		}
		
		//获取购物车商品包含的品牌
		Set<Long> brandIds = new HashSet<Long>();
		double firstDiscount = globalSettingService.getDouble(GlobalSettingName.FIRST_DISCOUNT);
		
		List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
		data.put("count", cartItems.size());
		data.put("brandids", brandIds);
		
		data.put("ad", shoppingCartFacade.getAd(userId));
		data.put("firstDiscount", firstDiscount);
		data.put("firstDiscountTips", "新用户首单" + firstDiscount + "元优惠");
		
		int count = orderService.getUserOrderCountForFirstDiscount(userId);
		if (count == 0 ){
			data.put("firstDiscountAble", "YES");
		}else {
			data.put("firstDiscountAble", "NO");
		}
		
		
		//取出cartItems中分类为虚拟分类的封装。取出cartItems中分类为品牌分类,封装
		Set<Long> virtualCategoryId = new HashSet<Long>();
		Set<Long> productIds = new HashSet<Long>();
		List<CartItemVO> virtualCartItems = new ArrayList<CartItemVO>();
		List<CartItemVO> normalCartItems = new ArrayList<CartItemVO>();
		
		for (CartItemVO cartItem : cartItems) {
			productIds.add(cartItem.getProductId());
			if(cartItem.getCategory() != null) {
				virtualCategoryId.add(cartItem.getCategory().getId());
				virtualCartItems.add(cartItem);
			} else {
				brandIds.add(cartItem.getProduct().getBrandId());
				normalCartItems.add(cartItem);
			}
		}
		//取全场满减信息
		Map<String, Object> allGroupMap = new HashMap<String, Object>();
		List<Map<String, Object>> allItemList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> serviceProductList = new ArrayList<Map<String, Object>>();
		List<DiscountInfo> discountInfoList = new ArrayList<DiscountInfo>();
		List<DiscountInfo> allDiscountInfoList = new ArrayList<DiscountInfo>();
		DiscountInfo discountInfo;
		JSONArray jsonArrayDiscount = globalSettingService.getJsonArray(GlobalSettingName.ALL_DISCOUNT);
		if(jsonArrayDiscount != null && jsonArrayDiscount.size() > 0){
			
			for(Object obj : jsonArrayDiscount) {
				discountInfo = new DiscountInfo();
				discountInfo.setFull(Double.parseDouble(((JSONObject)obj).get("full").toString()));
				discountInfo.setMinus(Double.parseDouble(((JSONObject)obj).get("minus").toString()));
				allDiscountInfoList.add(discountInfo);
			}	 
		}
		int allDsctFlag = 0;
		if(allDiscountInfoList.size() > 0){
			data.put("allDiscountList", allDiscountInfoList);
			data.put("allGroupMap", allGroupMap);
			allDsctFlag = 1;
		}else{
			data.put("items", groupList);
		}
		data.put("allDsctFlag", allDsctFlag);
		
		//限购信息 Map<ProductId, RestrictProductVO>
		Map<Long, RestrictProductVO> restrictInfo = new HashMap<Long, RestrictProductVO>();
		if(productIds.size() > 0) {
			restrictInfo = productFacade.restrictInfoOfProduct(userId, productIds);
		}
		
		Map<Long, List<DiscountInfo>> discountInfoMap =new HashMap<Long, List<DiscountInfo>>();
		if(virtualCategoryId.size() > 0 && allDiscountInfoList.size() == 0){
			discountInfoMap = shoppingCartFacade.queryDiscountInfoListMap(0, virtualCategoryId);  		
		}
		
		
		for(Iterator<Long> iterator = virtualCategoryId.iterator(); iterator.hasNext();) {
			Long iteratorCatId = iterator.next();
			Category category = categoryService.getCategoryById(iteratorCatId);
			
			if(category == null) {
				category = new Category();
				
				category.setId(0);
				category.setCategoryName("无此分类");
				category.setIsDiscount(0);
				category.setExceedMoney(0);
				category.setMinusMoney(0);
			}
			
			Map<String, Object> groupMap = new HashMap<String, Object>();
			List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
			
			
			
			for(CartItemVO cartItem : virtualCartItems) {
				long categoryId = cartItem.getCategory().getId();
				
				if(categoryId == iteratorCatId) {
					Product product = cartItem.getProduct();
					Map<String, Object> productMap = new HashMap<String, Object>();
					
					productMap.put("productId", product.getId());
					productMap.put("productName", product.getName());
					productMap.put("productImage", product.getDetailImageArray().length > 0 ? product.getDetailImageArray()[0] : "");
					productMap.put("marketPrice", product.getMarketPrice());
					productMap.put("brandId", product.getBrandId());
					productMap.put("bottomPrice", product.getBottomPrice());
					productMap.put("marketPriceMin", product.getMarketPriceMin());
					productMap.put("marketPriceMax", product.getMarketPriceMax());
					productMap.put("payAmountInCents", product.getPayAmountInCents());
					productMap.put("cash", product.getCurrenCash()); 
					productMap.put("jiuCoin", product.getCurrentJiuCoin());
					productMap.put("validate", product.isOnSaling());
					productMap.put("restrictInfo", restrictInfo.get(product.getId()));
					
					ProductSKU sku = cartItem.getSku();
					List<ProductPropVO> skuProps = cartItem.getSkuProps();
					Map<String, Object> skuMap = new HashMap<String, Object>();
					skuMap.put("skuId", sku.getId());
					skuMap.put("price", sku.getPrice());
					skuMap.put("remainCount", sku.getRemainCount());
					skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));
					
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("id", cartItem.getId());
					itemMap.put("product", productMap);
					itemMap.put("brand", brandService.getBrand(product.getBrandId()).toSimpleMap());
					itemMap.put("sku", skuMap);
					itemMap.put("buyCount", cartItem.getBuyCount());
					itemMap.put("isSelected", cartItem.getIsSelected());
					itemMap.put("skuProps", buildSkuProps(product.getId()).get("skuProps"));
					itemMap.put("skuMap", buildSkuProps(product.getId()).get("skuMap"));
					itemList.add(itemMap);
					allItemList.add(itemMap);
				}
			}
			
			groupMap.put("item", itemList);
			//新增字段,判断是否是虚拟分类
			groupMap.put("isVirtualCategory", true);
			//虚拟分类专属字段,虚拟分类的优惠信息@
			groupMap.put("categoryDiscount", category.toDiscountMap());
			
			discountInfoList = new ArrayList<DiscountInfo>();
			discountInfoList = discountInfoMap.get(category.getId());
			
			//虚拟分类复合优惠信息列表
			groupMap.put("categoryDiscountList", discountInfoList);
			
			groupList.add(groupMap);
		}
		
		Map<Long, List<DiscountInfo>> discountInfoMapBrand = new HashMap<Long, List<DiscountInfo>>();
		if(brandIds != null && brandIds.size() > 0 && allDiscountInfoList.size() == 0){ //@
			discountInfoMapBrand = shoppingCartFacade.queryDiscountInfoListMap(1, brandIds);	
		}
		
		
		
		// 将cartItems里的商品按照品牌id封装
		for(Iterator<Long> brandIdIterator = brandIds.iterator(); brandIdIterator.hasNext();) {
			long brandId = brandIdIterator.next(); 
			Brand brand = brandService.getBrand(brandId);
			if(brand == null) {
				brand = new Brand();
				brand.setBrandName("无品牌");
				brand.setLogo("default.png");
			}
			
			Map<String, Object> entry = new HashMap<String, Object>();
			List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
			for (CartItemVO cartItem : normalCartItems) {
				Product product = cartItem.getProduct();
				//统计非服务品牌572的商品
				if(product.getBrandId() == brandId && brandId != 572) {
					Map<String, Object> productMap = new HashMap<String, Object>();
					productMap.put("productId", product.getId());
					productMap.put("productName", product.getName());
					productMap.put("productImage", product.getDetailImageArray().length > 0 ? product.getDetailImageArray()[0]
							: "");
					productMap.put("marketPrice", product.getMarketPrice());
					productMap.put("brandId", product.getBrandId()); 
					productMap.put("bottomPrice", product.getBottomPrice());
					productMap.put("marketPriceMin", product.getMarketPriceMin());
					productMap.put("marketPriceMax", product.getMarketPriceMax());
					productMap.put("payAmountInCents", product.getPayAmountInCents());
					productMap.put("cash", product.getCurrenCash());
					productMap.put("jiuCoin", product.getCurrentJiuCoin());
					productMap.put("validate", product.isOnSaling());
					productMap.put("restrictInfo", restrictInfo.get(product.getId()));
					
					ProductSKU sku = cartItem.getSku();
					List<ProductPropVO> skuProps = cartItem.getSkuProps();
					Map<String, Object> skuMap = new HashMap<String, Object>();
					skuMap.put("skuId", sku.getId());
					skuMap.put("price", sku.getPrice());
					skuMap.put("remainCount", sku.getRemainCount());
					skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));
					
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("id", cartItem.getId());
					itemMap.put("product", productMap);
					itemMap.put("sku", skuMap);
					itemMap.put("buyCount", cartItem.getBuyCount());
					itemMap.put("isSelected", cartItem.getIsSelected());
					itemMap.put("skuProps", buildSkuProps(product.getId()).get("skuProps"));
					itemMap.put("skuMap", buildSkuProps(product.getId()).get("skuMap"));
					itemList.add(itemMap);
					allItemList.add(itemMap);
					
					//统计服务品牌572的商品
				} else if(product.getBrandId() == brandId && brandId == 572){
					Map<String, Object> productMap = new HashMap<String, Object>();
					productMap.put("productId", product.getId());
					productMap.put("productName", product.getName());
					productMap.put("productImage", product.getDetailImageArray().length > 0 ? product.getDetailImageArray()[0]
							: "");
					productMap.put("marketPrice", product.getMarketPrice());
					productMap.put("brandId", product.getBrandId()); 
					productMap.put("bottomPrice", product.getBottomPrice());
					productMap.put("marketPriceMin", product.getMarketPriceMin());
					productMap.put("marketPriceMax", product.getMarketPriceMax());
					productMap.put("payAmountInCents", product.getPayAmountInCents());
					productMap.put("cash", product.getCurrenCash());
					productMap.put("jiuCoin", product.getCurrentJiuCoin());
					productMap.put("validate", product.isOnSaling());
					productMap.put("restrictInfo", restrictInfo.get(product.getId()));
					
					ProductSKU sku = cartItem.getSku();
					List<ProductPropVO> skuProps = cartItem.getSkuProps();
					Map<String, Object> skuMap = new HashMap<String, Object>();
					skuMap.put("skuId", sku.getId());
					skuMap.put("price", sku.getPrice());
					skuMap.put("remainCount", sku.getRemainCount());
					skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));
					
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("id", cartItem.getId());
					itemMap.put("product", productMap);
					itemMap.put("sku", skuMap);
					itemMap.put("buyCount", cartItem.getBuyCount());
					itemMap.put("isSelected", cartItem.getIsSelected());
					itemMap.put("skuProps", buildSkuProps(product.getId()).get("skuProps"));
					itemMap.put("skuMap", buildSkuProps(product.getId()).get("skuMap"));
					serviceProductList.add(itemMap);
					
				}
			}
			entry.put("item", itemList);
			//新增字段,判断是否是虚拟分类
			entry.put("isVirtualCategory", false);
			//按品牌分类的专属字段,品牌的优惠信息
			entry.put("brand", brand.toSimpleMap());
			
			discountInfoList = new ArrayList<DiscountInfo>();
			discountInfoList = discountInfoMapBrand.get(brand.getBrandId());
			
			//优惠信息列表
			entry.put("brandDiscountList", discountInfoList);
			
			groupList.add(entry);
		}
		allGroupMap.put("item", allItemList);
		allGroupMap.put("serviceItem", serviceProductList);
		
		return data;
	}


    @Deprecated
	public Map<String, Object> getCart15(UserDetail userDetail) {

        Map<String, Object> data = new HashMap<String, Object>();
		long userId = 0;
		
		if(userDetail.getUser() == null) {
	        data.put("ad", shoppingCartFacade.getAd(userId));
	        return data;
		}
		userId = userDetail.getUserId();

        List<CartItemVO> cartItems = null;
        cartItems = shoppingCartFacade.getCartItemVOs(userId);
        if(cartItems == null) {
        	return new HashMap<String, Object>();
        }
        
        List<Long> brandIds = null;
        brandIds = shoppingCartFacade.getBranIds(userId);

        List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
        data.put("count", cartItems.size());
        data.put("brandids", brandIds);
        data.put("items", map);
        data.put("ad", shoppingCartFacade.getAd(userId));
        
        for(int i = 0; i < brandIds.size(); i++) {
        	Brand brand = brandService.getBrand(brandIds.get(i));
        	if(brand == null) {
        		brand = new Brand();
        		brand.setBrandName("无品牌");
        		brand.setLogo("default.png");
    		}
        	
        	Map<String, Object> entry = new HashMap<String, Object>();
        	List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
        	for (CartItemVO cartItem : cartItems) {
        		Product product = cartItem.getProduct();
        		if(product.getBrandId() == brandIds.get(i)) {
	        		Map<String, Object> productMap = new HashMap<String, Object>();
	        		productMap.put("productId", product.getId());
	        		productMap.put("productName", product.getName());
	        		productMap.put("productImage", product.getDetailImageArray().length > 0 ? product.getDetailImageArray()[0]
	        				: "");
                    productMap.put("marketPrice", product.getMarketPrice());
	        		productMap.put("brandId", product.getBrandId());
	        		productMap.put("bottomPrice", product.getBottomPrice());
	        		productMap.put("marketPriceMin", product.getMarketPriceMin());
	        		productMap.put("marketPriceMax", product.getMarketPriceMax());
	        		productMap.put("payAmountInCents", product.getPayAmountInCents());
	        		productMap.put("cash", 4);
	        		productMap.put("jiuCoin", 4);
	        		
	        		ProductSKU sku = cartItem.getSku();
	        		List<ProductPropVO> skuProps = cartItem.getSkuProps();
	        		Map<String, Object> skuMap = new HashMap<String, Object>();
	        		skuMap.put("skuId", sku.getId());
	        		skuMap.put("price", sku.getPrice());
	        		skuMap.put("remainCount", sku.getRemainCount());
	        		skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));
        		
        			Map<String, Object> itemMap = new HashMap<String, Object>();
        			itemMap.put("id", cartItem.getId());
        			itemMap.put("product", productMap);
        			itemMap.put("sku", skuMap);
        			itemMap.put("buyCount", cartItem.getBuyCount());
        			itemMap.put("isSelected", cartItem.getIsSelected());
        			itemMap.put("skuProps", buildSkuProps(product.getId()).get("skuProps"));
        			itemMap.put("skuMap", buildSkuProps(product.getId()).get("skuMap"));
        			itemList.add(itemMap);
        		}
        	}
        	entry.put("item", itemList);
        	entry.put("brandName", brand.getBrandName());
        	entry.put("brandIdentity", brand.getBrandIdentity());
        	entry.put("logo", "http://www.yujiejie.com/static/app/logo/"+brand.getBrandId()+".png");
        	map.add(entry);
        }

        return data;
	}

    /**
     * 添加到购物车必须保证(购物车中的数量 + 已买) < 限购数量
     * @param userId
     * @param productId
     * @param count
     * @return
     */
	public Map<Long, Integer> getProductCount(long userId, long productId, int count) {
		Map<Long, Integer> productCountMap = new HashMap<Long, Integer>();
		
		int preBuyCount = count;
		List<CartItem> cartItems = shoppingCartService.getCartItems(userId);
		for(CartItem caItem : cartItems) {
			if(caItem.getProductId() == productId) {
				preBuyCount += caItem.getBuyCount();
			}
		}
		productCountMap.put(productId, preBuyCount);
		
		return productCountMap;
	}
	
}
