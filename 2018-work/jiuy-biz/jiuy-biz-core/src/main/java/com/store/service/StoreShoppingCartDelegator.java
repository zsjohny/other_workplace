package com.store.service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotations.TableField;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.StoreCouponTemplateNewMapper;
import com.jiuyuan.entity.ProductPropName;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.SupplierCustomer;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.service.common.IBrandNewService;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.service.common.IStoreCouponNewService;
import com.jiuyuan.service.common.ISupplierCustomer;
import com.jiuyuan.service.common.IUserNewService;
import com.jiuyuan.service.common.SupplierCustomerService;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.Brand;
import com.store.entity.ProductPropNameValuesPair;
import com.store.entity.ProductPropVO;
import com.store.entity.ProductVOShop;
import com.store.entity.StoreCartItem;
import com.store.entity.StoreCartItemVO;
import com.store.service.brand.ShopBrandService;






/**
 * 
 * @author LWS
 *
 */

@Component
public class StoreShoppingCartDelegator {
	
	private static final Logger logger = Logger.getLogger(StoreShoppingCartDelegator.class);

	
//	@Autowired
//	private ObjectMapper objectMapper;
	
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ShoppingCartFacade shoppingCartFacade;
    
    @Autowired
    private ProductPropAssemblerShop productPropAssembler;
    
    @Autowired
    private ProductSKUService productSKUService;
    @Autowired
    private IProductNewService productNewService;
    
    @Autowired
    private ShopBrandService shopBrandService;

    @Autowired
    private StoreBusinessServiceShop storeBusinessService;
    @Autowired
	private IUserNewService supplierUserService;
    @Autowired
    private ISupplierCustomer supplierCustomerService;
	@Autowired
	private StoreCouponTemplateNewMapper storeCouponTemplateNewMapper;
	@Autowired
	private IBrandNewService brandNewService;
	
    private Map<String, Object> buildSkuProps(long productId) {
    	 List<ProductSKU> skus = productSKUService.getAllProductSKUsOfProduct(productId);
         Map<String, ProductSKU> skuMap = new HashMap<String, ProductSKU>();
         List<ProductPropVO> productPropVOs = new ArrayList<ProductPropVO>();
         Map<String, Object> data = new HashMap<String, Object>();
         
         for (ProductSKU sku : skus) {
             skuMap.put(sku.getPropertyIds(), sku);
             productPropVOs.addAll(sku.getProductProps());
         }
//    	 JSONObject jsonObject = new JSONObject(skuMap)
//       System.out.println(skuMap);
//       System.out.println(JSONObject.toJSONString(skuMap));
         data.put("skuMap", JSONArray.toJSONString(skuMap));
//         data.put("skuMap", skuMap);
         
//         try {
//             data.put("skuMap", objectMapper.writeValueAsString(skuMap));
//         } catch (Exception e) {
//             logger.error("parsing sku map error!");
//         }

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
            builder.append(" ");
        }
        return builder.toString();
    }

    public ResultCode addCart(long productId, long skuId, int count, UserDetail userDetail, long statisticsId) {
        long storeId = userDetail.getId();
        long time = System.currentTimeMillis();
        
        StoreCartItem cartItem = new StoreCartItem(storeId, productId, skuId, count, 0, time, time, 0, statisticsId);
        shoppingCartService.addCartItem(cartItem);
            
        return ResultCode.COMMON_SUCCESS;
    }

    public boolean removeCart(long productId, long skuId, int count, UserDetail userDetail) {
        long storeId = userDetail.getId();
        StoreCartItem cartItem = shoppingCartService.getCartItem(storeId, productId, skuId);
        if (cartItem == null) {
            return false;
        } else if (count >= cartItem.getBuyCount()) {
            shoppingCartService.removeCartItem(storeId, productId, skuId);
        } else {
            long time = System.currentTimeMillis();
            shoppingCartService.addCount(storeId, productId, skuId, -count, time);
        }
        return true;
    }

    public boolean deleteCart(long storeId, long id) {
        StoreCartItem cartItem = shoppingCartService.getCartItemById(id);
        if (cartItem == null) {
            return false;
        } else {
            shoppingCartService.removeById(storeId, id);
        }
        return true;
	}

	@Transactional(rollbackFor = Exception.class)
	public JsonResponse saveProductsInCart(String cartItemsJson, long storeId) {
//		新增代码
		JsonResponse jsonResponse = new JsonResponse();
		long time = System.currentTimeMillis();
		
        List<StoreCartItem> cartItems = JSON.parseArray(cartItemsJson, StoreCartItem.class);
        
        for (StoreCartItem cartItem : cartItems) {
        	cartItem.setStoreId(storeId);
        	cartItem.setCreateTime(time);
        	cartItem.setUpdateTime(time);
		}

        shoppingCartService.removeByStoreId(storeId);
        if (cartItems.size() > 0) {
        	shoppingCartService.addCartItems(cartItems);
		}
        shoppingCartService.removeZeroBuyCount(storeId);
        
        return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}

	
	public Map<String, Object> getCart(UserDetail userDetail) {
		long time1 = System.currentTimeMillis();
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<StoreCartItemVO> cartItems = shoppingCartFacade.getCartItemVOs(userDetail);
//		long time2 = System.currentTimeMillis();
//		logger.info("time2："+(time2-time1));
		if (CollectionUtils.isEmpty(cartItems)) {
			return data;
		}
		
		List<Map<String, Object>> list = assembleCartItems(userDetail.getId(), cartItems);
//		long time3 = System.currentTimeMillis();
//		logger.info("time3："+(time3-time2));
		Set<Long> brandIds = new HashSet<Long>();
		data.put("brand_ids", brandIds);
		Map<Long, List<Map<String, Object>>> resultMap = new HashMap<>();
		for (StoreCartItemVO cartItem : cartItems) {
			brandIds.add(cartItem.getProduct().getBrandId());
			resultMap.put(cartItem.getProduct().getBrandId(), new ArrayList<>());
		}
		
		for (Map<String, Object> item_map : list) {
			List<Map<String, Object>> item_list = resultMap.get(Long.parseLong(item_map.get("brand_id").toString()));
			item_list.add(item_map);
		}
//		long time4 = System.currentTimeMillis();
//		logger.info("time4："+(time4-time3));
		
		List<Map<String, Object>> results = new ArrayList<>();
		for (Entry<Long, List<Map<String, Object>>> entry : resultMap.entrySet()) {
			Map<String, Object> result = new HashMap<>();
			
			long brandId = entry.getKey();
			Brand brand = shopBrandService.getBrand(brandId);
			result.put("brand_info",brand );
			//品牌批发限制
			
			UserNew supplierUser = supplierUserService.getSupplierByBrandId(brandId);
			if(supplierUser == null ){
				logger.info("根据品牌Id,brandId:"+brandId+"获取供应商信息为空");
			}

			result.put("wholesaleLimitTip",supplierUser.buildWholesaleLimitTip1() );//批发限制提示文本, 例子：10件、￥1000元 起订
			
			//是否有优惠券  hasCoupon 0:没有  1:有
			int hasCoupon = 0;
			List<Map<String,Object>> brandCouponList = storeCouponTemplateNewMapper.getSupplierCouponTemplate(brandId);
			if(brandCouponList.size() > 0){
				hasCoupon = 1;
			}
			result.put("hasCoupon", hasCoupon);
			result.put("item_list", entry.getValue());
			StoreBusiness storeBusiness = storeBusinessService.getById(userDetail.getId());
			String phoneNumber = storeBusiness.getPhoneNumber();
			//用户浏览商品权限
			result = brandNewService.checkBrowsePermission(phoneNumber,brandId,result);
			
			results.add(result);
		}
//		long time5 = System.currentTimeMillis();
//		logger.info("time5："+(time5-time4));
		data.put("products_count", list.size());
		data.put("result_list", results);
		
		return data;
	}
	
    private List<Map<String, Object>> assembleCartItems(long storeId, List<StoreCartItemVO> cartItems) {
//    	long time3_1 = System.currentTimeMillis();
    	List<Map<String, Object>> results = new ArrayList<>();
    	StoreBusiness storeBusiness = storeBusinessService.getById(storeId);
//    	long time3_2 = System.currentTimeMillis();
//		logger.info("time3_2："+(time3_2-time3_1));
    	
    	Map<Long, List<StoreCartItemVO>> caMapOfProduct = new HashMap<>();
    	for (StoreCartItemVO cartItem : cartItems) {
    		long productId = cartItem.getProductId();
    		List<StoreCartItemVO> caList = caMapOfProduct.get(productId);
    		if (caList == null) {
				caList = new ArrayList<>();
				caMapOfProduct.put(productId, caList);
    		}
    		caList.add(cartItem);
    	}
//    	long time3_3 = System.currentTimeMillis();
//		logger.info("time3_3："+(time3_3-time3_2));
    	for (Entry<Long, List<StoreCartItemVO>> map : caMapOfProduct.entrySet()) {
    		List<StoreCartItemVO> caItemVOs = map.getValue();
    		Map<String, Object> productMap = new HashMap<String, Object>();
    		List<Map<String, Object>> items = new ArrayList<>();
    		double total_commission = 0.00;
    		int total_buy_count = 0;
    		int is_selected = 0;
    		Set<Long> colors = new HashSet<>();
    		Set<Long> sizes = new HashSet<>();
    		for (StoreCartItemVO cartItem : caItemVOs) {
    			ProductVOShop product = cartItem.getProduct();
    			productMap.put("product_id", product.getId());
    			//平台商品状态:0已上架、1已下架、2已删除
    			
    			productMap.put("platformProductState",productNewService.getPlatformProductState(product.getId()));
    			productMap.put("product_name", product.getName());
    			productMap.put("product_image", product.getDetailImageArray().length > 0 ? product.getDetailImageArray()[0]
    					: "");
    			productMap.put("market_price", product.getMarketPrice());
    			productMap.put("brand_id", product.getBrandId()); 
    			productMap.put("clothes_num", product.getClothesNumber());
    			productMap.put("bottom_price", product.getBottomPrice());
    			productMap.put("market_price_min", product.getMarketPriceMin());
    			productMap.put("market_price_max", product.getMarketPriceMax());
    			productMap.put("cash", product.getCash());
    			productMap.put("wholeSaleCash", product.getWholeSaleCash());
    			productMap.put("minLadderPrice", product.getMinLadderPrice()+"");//最小阶梯价格
    			productMap.put("maxLadderPrice", product.getMaxLadderPrice()+"");//最大阶梯价格
    			productMap.put("ladderPriceJson", product.getLadderPriceJson()+"");//阶梯价格JSON
    			productMap.put("jiu_coin", product.getCurrentJiuCoin());
    			
    			ProductSKU sku = cartItem.getSku();
    			List<ProductPropVO> skuProps = cartItem.getSkuProps();
    			Map<String, Object> skuMap = new HashMap<String, Object>();
    			skuMap.put("skuId", sku.getId());
    			skuMap.put("price", sku.getPrice());
    			skuMap.put("remain_count", sku.getRemainCount());
    			skuMap.put("sku_snapshot", buildSkuSnapshot(skuProps));
    			skuMap.put("property_ids", sku.getPropertyIds());
    			
    			String[] propertyIds = sku.getPropertyIds().split(";");
    			colors.add(Long.parseLong(propertyIds[0].split(":")[1]));
    			sizes.add(Long.parseLong(propertyIds[1].split(":")[1]));
    			
    			Map<String, Object> itemMap = new HashMap<String, Object>();
    			itemMap.put("cart_id", cartItem.getId());
    			itemMap.put("sku", skuMap);
    			itemMap.put("buy_count", cartItem.getBuyCount());
    			itemMap.put("statistics_id", cartItem.getStatisticsId());
    			itemMap.put("commission", String.format("%.2f", storeBusiness.getCommissionPercentage() * product.getCash() / 100));
    			
    			is_selected = is_selected | cartItem.getIsSelected();
    			total_commission += cartItem.getBuyCount() * storeBusiness.getCommissionPercentage() * product.getCash() / 100;
    			total_buy_count += cartItem.getBuyCount();
    			
    			items.add(itemMap);
    		}
//    		long sku_props_time1 = System.currentTimeMillis();
    		productMap.put("sku_props", buildSkuProps(Long.parseLong(productMap.get("product_id").toString())).get("skuProps"));
    		productMap.put("sku_map", buildSkuProps(Long.parseLong(productMap.get("product_id").toString())).get("skuMap"));
//    		long sku_props_time2 = System.currentTimeMillis();
//    		logger.info("sku_props_time2："+(sku_props_time2-sku_props_time1));
    		productMap.put("is_selected", is_selected);//勾选
    		productMap.put("total_commission", String.format("%.2f", total_commission));//收益
    		productMap.put("total_buy_count", total_buy_count);//购买数量
    		productMap.put("itemMaps", items);//
    		productMap.put("color_ids", colors);
    		productMap.put("size_ids", sizes);
    		results.add(productMap);
		}
//    	long time3_4 = System.currentTimeMillis();
//		logger.info("time3_4："+(time3_4-time3_3));
    	return results;
	}
	public void batchDeleteCart(long storeId, long productId) {
        shoppingCartService.batchDeleteCart(storeId, productId);
	}

	public int add(String cartItemsJson, long storeId) {
		long time = System.currentTimeMillis();
        List<StoreCartItem> cartItems = JSON.parseArray(cartItemsJson, StoreCartItem.class);
        for (StoreCartItem cartItem : cartItems) {
        	cartItem.setStoreId(storeId);
        	cartItem.setCreateTime(time);
        	cartItem.setUpdateTime(time);
		}
		return shoppingCartService.batchAdd(cartItems);
	}

	/**
	 * 获取用户购物车款号数量接口
	 * @param storeId
	 * @return
	 */
	public int getClothesNumberCount(long storeId) {
		return shoppingCartService.getClothesNumberCount(storeId);
	}

}
