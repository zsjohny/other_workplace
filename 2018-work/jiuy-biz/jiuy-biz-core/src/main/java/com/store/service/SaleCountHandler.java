package com.store.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductMapper;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductSkuMapper;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.newentity.RestrictionActivityProduct;
import com.jiuyuan.entity.newentity.RestrictionActivityProductSku;
import com.jiuyuan.service.common.MemcachedService;
import com.store.entity.ShopStoreOrder;
import com.store.entity.ShopStoreOrderItem;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class SaleCountHandler implements OrderHandler {
	private static final Log logger = LogFactory.get();

    @Autowired
    private ProductServiceShop productService;

    @Autowired
    private ProductSKUService productSKUService;
    
    @Autowired
    private RestrictionActivityProductSkuMapper restrictionActivityProductSkuMapper;
    
    @Autowired
    private RestrictionActivityProductMapper restrictionActivityProductMapper;
    
    @Autowired
    private MemcachedService memcachedService;

    @Override
    public void postOrderCreation(ShopStoreOrder order, String version) {
        Map<Long, Integer> productCountMap = new HashMap<Long, Integer>();
        Set<Long> skuIds = new HashSet<Long>();
        for (ShopStoreOrderItem item : order.getOrderItems()) {
        	skuIds.add(item.getSkuId());
            Integer count = productCountMap.get(item.getProductId());
            if (count == null) {
                count = 0;
            }
            count = count + item.getBuyCount();
            productCountMap.put(item.getProductId(), count);

//            productSKUService.updateRemainCount(item.getSkuId(), -item.getBuyCount());
        }
        List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuIds);
        if(productSkus != null && productSkus.size() > 0){
	        for (ShopStoreOrderItem item : order.getOrderItems()) {
	        	for(ProductSKU sku: productSkus){
	        		if(sku.getId() == item.getSkuId()){
	        			if(sku.getlOWarehouseId2() > 0 && sku.getlOWarehouseId2() == item.getlOWarehouseId()){
	        				productSKUService.updateRemainCountSecond(item.getSkuId(), -item.getBuyCount());
	        			}else{
	        				productSKUService.updateRemainCount(item.getSkuId(), -item.getBuyCount());
	        				
	        			}
	            		
	        		}
	        	}
	        		 
	        }
        	
        }
        
//        for (Map.Entry<Long, Integer> entry : productCountMap.entrySet()) {
//            productService.updateSaleCount(entry.getKey(), entry.getValue());
//        }
    }
    
    public void updateSaleCount(ShopStoreOrder order, String version) {
    	Map<Long, Integer> productCountMap = new HashMap<Long, Integer>();
        Set<Long> skuIds = new HashSet<Long>();
        for (ShopStoreOrderItem item : order.getOrderItems()) {
        	skuIds.add(item.getSkuId());
            Integer count = productCountMap.get(item.getProductId());
            if (count == null) {
                count = 0;
            }
            count = count + item.getBuyCount();
            productCountMap.put(item.getProductId(), count);

//            productSKUService.updateRemainCount(item.getSkuId(), -item.getBuyCount());
        }
    	for (Map.Entry<Long, Integer> entry : productCountMap.entrySet()) {
            productService.updateSaleCount(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void postOrderCancel(ShopStoreOrder order, String version) {
    	Set<Long> skuIds = new HashSet<Long>();
    	Map<Long,Integer> skuIdAndBuyCount = new HashMap<Long,Integer>();
        for (ShopStoreOrderItem item : order.getOrderItems()) {
        	skuIds.add(item.getSkuId());
        	skuIdAndBuyCount.put(item.getSkuId(), item.getBuyCount());
//            productSKUService.updateRemainCount(item.getSkuId(), item.getBuyCount());
        }
        long restrictionActivityProductId = order.getRestriction_activity_product_id();
        if(restrictionActivityProductId>0){
        	Wrapper<RestrictionActivityProductSku> wrapper = new EntityWrapper<RestrictionActivityProductSku>()
        			.eq("activity_product_id", restrictionActivityProductId);
        	List<RestrictionActivityProductSku> restrictionActivityProductSkuList = restrictionActivityProductSkuMapper.selectList(wrapper);
        	int allBuyCount = 0;
        	for (RestrictionActivityProductSku restrictionActivityProductSku : restrictionActivityProductSkuList) {
        		long productSkuId = restrictionActivityProductSku.getProductSkuId();
        		int remainCount = restrictionActivityProductSku.getRemainCount();//剩余活动商品数量
				if(skuIds.contains(productSkuId)){
					long restrictionActivityProductSkuId = restrictionActivityProductSku.getId();
					int buyCount = skuIdAndBuyCount.get(productSkuId);
					remainCount = remainCount + buyCount;
					RestrictionActivityProductSku restrictionActivityProductSkuNew = new RestrictionActivityProductSku();
					restrictionActivityProductSkuNew.setId(restrictionActivityProductSkuId);
					restrictionActivityProductSkuNew.setRemainCount(remainCount);
					restrictionActivityProductSkuMapper.updateById(restrictionActivityProductSkuNew);
					logger.info("从数据库中获取剩余活动商品数量surplusActivityProductCount："+remainCount
							+",restrictionActivityProductSkuId："+restrictionActivityProductSku.getId());
	        		//初始化活动剩余商品数量到缓存
					String groupKey = MemcachedKey.GROUP_KEY_restrictionActivityProductId;
					String key = "_restrictionActivityProductId_"+String.valueOf(restrictionActivityProductId)+"_skuId_"
							+String.valueOf(restrictionActivityProductSku.getProductSkuId());
					Object obj = memcachedService.getCommon(groupKey, key);
					//将剩余活动商品数量放入缓存
					int time = 7*24*60*60;//有效期1周，注意这里有效期不能大于30天
					boolean flag = memcachedService.setCommon(groupKey, key, time , String.valueOf(remainCount).trim());//注意这里必须转为string否则不能加减
					logger.info("从缓存中获取剩余活动商品数量并更新groupKey："+groupKey+",productSkuId："+productSkuId
							+",obj："+obj+",flag："+flag);
				}
				allBuyCount = allBuyCount + remainCount;
			}
        	RestrictionActivityProduct restrictionActivityProduct = new RestrictionActivityProduct();
        	restrictionActivityProduct.setId(restrictionActivityProductId);
        	restrictionActivityProduct.setRemainCount(allBuyCount);
			restrictionActivityProductMapper.updateById(restrictionActivityProduct);
        }else{
        	List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuIds);
            if(productSkus != null && productSkus.size() > 0){
            for (ShopStoreOrderItem item : order.getOrderItems()) {
            	for(ProductSKU sku: productSkus){
            		if(sku.getId() == item.getSkuId()){
            			if(sku.getlOWarehouseId2() > 0 && sku.getlOWarehouseId2() == item.getlOWarehouseId()){
            				productSKUService.updateRemainCountSecond(item.getSkuId(), item.getBuyCount());
            			}else{
            				productSKUService.updateRemainCount(item.getSkuId(), item.getBuyCount());
            				
            			}
                		
            		}
            	} 
            }
        }
        }
    }
    
}
