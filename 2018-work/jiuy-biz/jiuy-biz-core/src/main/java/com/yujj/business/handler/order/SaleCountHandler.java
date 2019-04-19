package com.yujj.business.handler.order;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yujj.business.service.ProductSKUService;
import com.yujj.business.service.ProductService;
import com.yujj.entity.order.Order;
import com.yujj.entity.order.OrderItem;
import com.yujj.entity.order.OrderNew;
import com.yujj.entity.product.ProductSKU;

@Service
public class SaleCountHandler implements OrderHandler {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSKUService productSKUService;

    @Override
    public void postOrderCreation(Order order, String version) {
    	Map<Long, Integer> productCountMap = new HashMap<Long, Integer>();
        Set<Long> skuIds = new HashSet<Long>();
        for (OrderItem item : order.getOrderItems()) {
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
        for (OrderItem item : order.getOrderItems()) {
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
//        Map<Long, Integer> productCountMap = new HashMap<Long, Integer>();
//        for (OrderItem item : order.getOrderItems()) {
//            Integer count = productCountMap.get(item.getProductId());
//            if (count == null) {
//                count = 0;
//            }
//            count = count + item.getBuyCount();
//            productCountMap.put(item.getProductId(), count);
//
//            productSKUService.updateRemainCount(item.getSkuId(), -item.getBuyCount());
//        }
        
        for (Map.Entry<Long, Integer> entry : productCountMap.entrySet()) {
            productService.updateSaleCount(entry.getKey(), entry.getValue());
        }
    }
    //删除旧表
//    @Override
//    public void postOrderCancel(Order order, String version) {
//        for (OrderItem item : order.getOrderItems()) {
//            productSKUService.updateRemainCount(item.getSkuId(), item.getBuyCount());
//        }
//    }
    
    @Override
    public void postOrderNewCancel(OrderNew order, String version) {
//    	for (OrderItem item : order.getOrderItems()) {
//    		productSKUService.updateRemainCount(item.getSkuId(), item.getBuyCount());
//    	}
    	
    	Set<Long> skuIds = new HashSet<Long>();
        for (OrderItem item : order.getOrderItems()) {
        	skuIds.add(item.getSkuId());
//            productSKUService.updateRemainCount(item.getSkuId(), item.getBuyCount());
        }
        List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuIds);
        if(productSkus != null && productSkus.size() > 0){
        for (OrderItem item : order.getOrderItems()) {
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
