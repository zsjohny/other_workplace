package com.yujj.business.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.shopping.CartItem;
import com.jiuyuan.entity.shopping.CartItemVO;
import com.jiuyuan.entity.shopping.DiscountInfo;
import com.jiuyuan.util.BeanUtil;
import com.yujj.business.adapter.DataminingAdapter;
import com.yujj.business.assembler.ProductPropAssembler;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.ProductCategoryService;
import com.yujj.business.service.ProductSKUService;
import com.yujj.business.service.ProductService;
import com.yujj.business.service.ShoppingCartService;
import com.yujj.entity.product.Category;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductPropVO;
import com.yujj.entity.product.ProductSKU;

@Service
public class ShoppingCartFacade {
    
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSKUService productSKUService;

    @Autowired
    private ProductPropAssembler productPropAssembler;
    
    @Autowired
    private DataminingAdapter dataminingAdapter;
    
    @Autowired
    private GlobalSettingService globalSettingService;
    
    @Autowired
    private ProductCategoryService productCategoryService;

    public List<CartItemVO> getCartItemVOs(long userId) {
        List<CartItem> cartItems = shoppingCartService.getCartItems(userId);
        if(CollectionUtils.isEmpty(cartItems)) {
            return new ArrayList<CartItemVO>();
        }
        
        Set<Long> productIds = new HashSet<Long>();
        Set<Long> skuIds = new HashSet<Long>();
        for (CartItem cartItem : cartItems) {
            productIds.add(cartItem.getProductId());
            skuIds.add(cartItem.getSkuId());
        }

        Map<Long, Product> productMap = productService.getProductMap(productIds);
        List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuIds);
        //新增字段,获取商品对应的虚拟分类,不存在则为null
        Map<Long, Category> categoryMap = productCategoryService.getProductVirtualCategory(productIds);

        List<ProductPropVO> composites = new ArrayList<ProductPropVO>();
        Map<Long, List<ProductPropVO>> skuPropMap = new HashMap<Long, List<ProductPropVO>>();
        Map<Long, ProductSKU> skuMap = new HashMap<Long, ProductSKU>();
        for (ProductSKU sku : productSkus) {
            skuMap.put(sku.getId(), sku);

            List<ProductPropVO> skuProps = sku.getProductProps();
            composites.addAll(skuProps);
            skuPropMap.put(sku.getId(), skuProps);
        }
        productPropAssembler.assemble(composites);

        List<CartItemVO> result = new ArrayList<CartItemVO>();
        for (CartItem cartItem : cartItems) {
            Product product = productMap.get(cartItem.getProductId());
            if (product == null) {
                continue;
            }
            
            ProductSKU sku = skuMap.get(cartItem.getSkuId());
            if (sku == null) {
                continue;
            }

            List<ProductPropVO> skuProps = skuPropMap.get(cartItem.getSkuId());
            if (skuProps == null) {
                continue;
            }

            CartItemVO vo = new CartItemVO();
            BeanUtil.copyProperties(vo, cartItem);
            vo.setProduct(product);
            vo.setSku(sku);
            vo.setSkuProps(skuProps);
            vo.setCategory(categoryMap.get(product.getId()));
            result.add(vo);
        }

        return result;
    }

	public List<Long> getBranIds(long userId) {
		List<CartItem> cartItems = shoppingCartService.getCartItems(userId);
		if(cartItems == null || cartItems.size() < 1) {
			return new ArrayList<Long>();
		}
		
		Set<Long> productIds = new HashSet<Long>();
        Set<Long> skuIds = new HashSet<Long>();
        for (CartItem cartItem : cartItems) {
           productIds.add(cartItem.getProductId());
           skuIds.add(cartItem.getSkuId());
        }
        
        return productService.getBrandIds(productIds);
	}

	public Map<String, Object> getAd(long userId) {
		Map<String, Object> ad = new HashMap<String, Object>();
		
		JSONObject recommendedProduct = globalSettingService.getJsonObject(GlobalSettingName.RECOMMENDED_PRODUCT);
		
//		if (recommendedProduct != null) {
//			JSONObject jsonObjbuyGuess = recommendedProduct.getJSONObject("shopping_cart_guess_like");
//			
//			int count = 0;
//			try {
//				count = jsonObjbuyGuess.getIntValue("count");
//			} catch (Exception e) {
//				count = 4;
//			}
//			
//			if (count <= 0) count = 4; 
//			jsonObjbuyGuess.put("buyGuessProduct", productService.getProductList15(dataminingAdapter.getBuyGuessProduct186(userId > 0 ? userId : 0, new PageQuery(1, count))));
//		}
		
		ad.put("recommendedProduct", recommendedProduct);
		
		ad.put("guessProduct", dataminingAdapter.getBuyGuessProduct186(userId > 0 ? userId : 0, new PageQuery(1, 4)));
		ad.put("shoppingCartBanner", globalSettingService.getJsonArray(GlobalSettingName.SHOPPINGCARTBANNER));
		
		return ad;
	}
	
	public Map<Long, List<DiscountInfo>> queryDiscountInfoListMap(int type, Collection<Long> ids) {
		Map<Long, List<DiscountInfo>> discountInfoMap = new HashMap<Long, List<DiscountInfo>>();
		List<DiscountInfo> discountInfoList = shoppingCartService.getDiscountInfoListByType(type, ids);
		
		for(DiscountInfo discountInfo : discountInfoList){
			
			List<DiscountInfo> discountInfos = discountInfoMap.get(discountInfo.getRelatedId());
			if (discountInfos == null){
				discountInfos = new ArrayList<DiscountInfo>();
				discountInfoMap.put(discountInfo.getRelatedId(), discountInfos);
			}
			discountInfos.add(discountInfo);
			
		}
		
		
		return discountInfoMap;
	}
	
	public List<DiscountInfo> queryDiscountInfoList(int type, long id) {
		
		List<DiscountInfo> discountInfoList = shoppingCartService.getDiscountInfoListById(type, id);

		return discountInfoList;
	}

}
