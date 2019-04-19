package com.store.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.ProductSKU;
import com.store.dao.mapper.ProductSKUMapper;
import com.store.entity.StoreProductSKUsPropVO;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;




@Service
public class ProductSKUService {

    @Autowired
    private ProductSKUMapper productSKUMapper;
    
    
    public List<ProductSKU> getProductSKUsOfProduct(long productId) {
        return productSKUMapper.getProductSKUsOfProduct(productId);
    }

    public ProductSKU getProductSKU(long skuId) {
        return productSKUMapper.getProductSKU(skuId);
    }

    public List<ProductSKU> getProductSKUs(Collection<Long> skuIds) {
    	if (skuIds.size() < 1) {
			return new ArrayList<>();
		}
        return productSKUMapper.getProductSKUs(skuIds);
    }

    public int updateRemainCount(long skuId, int by) {
        return productSKUMapper.updateRemainCount(skuId, by);
    }
    
    public int updateRemainCountSecond(long skuId, int by) {
    	return productSKUMapper.updateRemainCountSecond(skuId, by);
    }
    
    public List<StoreProductSKUsPropVO> getProductSKUsPropVO() {
        return productSKUMapper.getProductSKUsPropVO();
    }

	public ProductSKU getByProductIdPropertyIds(String propertyIds, long productId) {
		// TODO Auto-generated method stub
		return productSKUMapper.getByProductIdPropertyIds(propertyIds, productId);
	}

	public List<ProductSKU> getAllProductSKUsOfProduct(long productId) {
		// TODO Auto-generated method stub
		return productSKUMapper.getAllProductSKUsOfProduct(productId);
	}
	private static final Log logger = LogFactory.get("ShopProductService");
	
	
	
	 /**
     * 获取平台商品状态
     * @param product
     * @return  平台商品状态:0已上架、1已下架、2已删除
     */
	public String getPlatformProductState_del(long productId) {
		long time4_3_3_1 = System.currentTimeMillis();
//		logger.info("开始获取平台商品状态productId："+productId);
		String platformProductState = "1";//默认为已下架
		List<ProductSKU> productSKUList = getAllProductSKUsOfProduct(productId);
		//如果有一个sku上架则产品状态为上架
		for (ProductSKU productSKU : productSKUList) {
			if(productSKU.getOnSaling()){
				platformProductState = "0";
			}
		}
//		logger.info("结束获取平台商品状态productId："+productId+",platformProductState:"+platformProductState);
		long time4_3_3_2 = System.currentTimeMillis();
		logger.info("获取平台商品状态,会获取商品的所有sku列表time4_3_3_2："+(time4_3_3_2-time4_3_3_1));
		return platformProductState;
	}
    /**
     * 获取上架的商品数目，并排除用户自己上架过的商品
     * @return
     */
	public int getSaleStartProductNums(long storeId) {
		return productSKUMapper.getSaleStartProductNums(storeId);
	}

	/**
	 * 获取上架的限购活动商品
	 * @param id
	 * @return
	 */
	public List<ProductSKU> getProductSKUsByRestrictionActivityProductId(long restrictionActivityProductId) {
		return productSKUMapper.getProductSKUsByRestrictionActivityProductId(restrictionActivityProductId);
	}

}
