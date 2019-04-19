package com.store.service;

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

import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.shopping.DiscountInfo;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.jiuyuan.util.BeanUtil;
import com.store.entity.ProductPropVO;
import com.store.entity.ProductVOShop;
import com.store.entity.ShopCategory;
import com.store.entity.StoreCartItem;
import com.store.entity.StoreCartItemVO;



@Service
public class StoreShoppingCartFacade {
    
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProductServiceShop productService;

    @Autowired
    private ProductSKUService productSKUService;

    @Autowired
    private ProductPropAssemblerShop productPropAssembler;
    

    @Autowired
    private ShopGlobalSettingService globalSettingService;
    
    @Autowired
    private ProductCategoryService productCategoryService;

    public List<StoreCartItemVO> getCartItemVOs(UserDetail userDetail) {
        List<StoreCartItem> cartItems = shoppingCartService.getCartItems(userDetail.getId());
        if(CollectionUtils.isEmpty(cartItems)) {
            return new ArrayList<StoreCartItemVO>();
        }
        
        Set<Long> productIds = new HashSet<Long>();
        Set<Long> skuIds = new HashSet<Long>();
        for (StoreCartItem cartItem : cartItems) {
            productIds.add(cartItem.getProductId());
            skuIds.add(cartItem.getSkuId());
        }

        Map<Long, ProductVOShop> productMap = productService.getProductMap(productIds);
        List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuIds);
        //新增字段,获取商品对应的虚拟分类,不存在则为null
        Map<Long, ShopCategory> categoryMap = productCategoryService.getProductVirtualCategory(productIds);

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

        List<StoreCartItemVO> result = new ArrayList<StoreCartItemVO>();
        for (StoreCartItem cartItem : cartItems) {
        	ProductVOShop product = productMap.get(cartItem.getProductId());
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

            StoreCartItemVO vo = new StoreCartItemVO();
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
		List<StoreCartItem> cartItems = shoppingCartService.getCartItems(userId);
		if(cartItems == null || cartItems.size() < 1) {
			return new ArrayList<Long>();
		}
		
		Set<Long> productIds = new HashSet<Long>();
        Set<Long> skuIds = new HashSet<Long>();
        for (StoreCartItem cartItem : cartItems) {
           productIds.add(cartItem.getProductId());
           skuIds.add(cartItem.getSkuId());
        }
        
        return productService.getBrandIds(productIds);
	}

	public Map<String, Object> getAd(UserDetail userDetail) {
		Map<String, Object> ad = new HashMap<String, Object>();
		
//		ad.put("guessProduct", ProductUtil.getProductSimpleList(dataminingAdapter.getBuyGuessProduct186(userDetail, new PageQuery(1, 4))));
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
