package com.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.UserDetail;
import com.store.dao.mapper.OutStockCartMapper;
import com.store.entity.OutStockCart;
import com.store.entity.ParameterErrorException;
import com.store.entity.ProductPropVO;
import com.store.entity.StoreProduct;

/**
 * @author jeff.zhan
 * @version 2016年11月30日 下午6:53:54
 * 
 */

@Service
public class OutStockCartService {
	
	@Autowired
	private OutStockCartMapper outStockCartMapper;

	@Autowired
	private ProductServiceShop productService;
	
	@Autowired
	private ProductSKUService productSKUService;
	
	@Autowired
	private ProductPropAssemblerShop productPropAssembler;
	
	@Autowired
	private StoreProductService storeProductService;
	
	public Map<String, Object> getPannerByStoreId(UserDetail userDetail) {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> item_list = new ArrayList<>();
		List<OutStockCart> carts = outStockCartMapper.getByStoreId(userDetail.getId());
		int total_count = 0;
		for (OutStockCart cart : carts) {
			total_count += cart.getCount();
			
			Map<String, Object> item = new HashMap<>();
			item.put("id", cart.getId());
			item.put("count", cart.getCount());

			Product product = productService.getProductById(cart.getProductId());
			item.put("product_id", product.getId());
			item.put("name", product.getName());
			item.put("clothes_num", product.getClothesNumber());
			item.put("price", cart.getCash());
			item.put("image", product.getImage());
			item.put("sku_id", cart.getSkuId());

			ProductSKU productSKU = productSKUService.getProductSKU(cart.getSkuId());
			item.put("property_ids", productSKU.getPropertyIds());

			List<ProductPropVO> skuPropVOs = productSKU.getProductProps();
	        productPropAssembler.assemble(skuPropVOs);
	        item.put("sku_snap", buildSkuSnapshot(skuPropVOs));
			
			item_list.add(item);
		}
		result.put("item_list", item_list);
		result.put("total_count", total_count);
		
		return result;
	}
	
	private String buildSkuSnapshot(List<ProductPropVO> skuProps) {
        StringBuilder builder = new StringBuilder();
        for (ProductPropVO prop : skuProps) {
            builder.append(prop.toString());
            builder.append("  ");
        }
        return builder.toString();
    }

	public Long add(long storeId, String propertyIds, long productId, int count, double cash) {
		// TODO Auto-generated method stub
		ProductSKU sku = productSKUService.getByProductIdPropertyIds(propertyIds, productId);
		if (sku == null || storeProductService.getByStoreIdSkuId(storeId, sku.getId()) == null) {
			throw new ParameterErrorException("规格不存在");
		}
		int cart_count = 0;
		List<OutStockCart> carts = getByStoreIdSkuId(storeId, sku.getId());
		if (carts.size() > 0) {
			for (OutStockCart outStockCart : carts) {
				cart_count += outStockCart.getCount();
			}
		}
		StoreProduct storeProduct = storeProductService.getByStoreIdSkuId(storeId, sku.getId());
		if ((storeProduct.getOnSaleCount() + storeProduct.getOffSaleCount()) < (count + cart_count)) {
			throw new ParameterErrorException("出库数大于上架数");
		}
		outStockCartMapper.add(storeId, productId, sku.getId(), count, System.currentTimeMillis(), cash);
		return sku.getId();
	}
	
	public List<OutStockCart> getByStoreIdSkuId(long storeId, long skuId) {
		return outStockCartMapper.getByStoreIdSkuId(storeId, skuId);
	}

	public void delete(long storeId, String propertyIds, long productId, double cash) {
		// TODO Auto-generated method stub
		ProductSKU sku = productSKUService.getByProductIdPropertyIds(propertyIds, productId);
		if (sku == null) {
			throw new ParameterErrorException("规格不存在");
		}
		OutStockCart outStockCart = getByStoreIdSkuIdCash(storeId, sku.getId(), cash);
		if (outStockCart == null) {
			throw new ParameterErrorException("购物车不存在");
		}
		outStockCartMapper.delete(outStockCart.getId());
	}

	private OutStockCart getByStoreIdSkuIdCash(long storeId, long skuId, double cash) {
		// TODO Auto-generated method stub
		return outStockCartMapper.getByStoreIdSkuIdCash(storeId, skuId, cash);
	}

}
