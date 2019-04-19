package com.yujj.web.controller.mobile;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.account.DefaultUserDetail;
import com.jiuyuan.entity.shopping.CartItemVO;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.ShoppingCartFacade;
import com.yujj.entity.account.User;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductPropVO;
import com.yujj.entity.product.ProductSKU;
import com.yujj.test.util.YjjAbstractUnitilsTest;
import com.yujj.web.controller.delegate.ShoppingCartDelegator;

public class MobileShoppingCartControllerTest extends YjjAbstractUnitilsTest{
	@Autowired
	private ShoppingCartDelegator shoppingCartDelegator ;
	
	@Autowired
    private ShoppingCartFacade shoppingCartFacade;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Ignore
	public void testQueryCart() {
		JsonResponse jsonResponse = new JsonResponse();
		DefaultUserDetail userDetail = new DefaultUserDetail();
		User user = new User();
		user.setUserId(1);
		userDetail.setUser(user);
		Map<String,Object> data = shoppingCartDelegator.getCart(userDetail) == null ? new HashMap<String, Object>() : shoppingCartDelegator.getCart(userDetail);
	}
	
//	@Test
//	@Rollback(false)
//	@Ignore
//	public void testAddCart() {
//		JsonResponse jsonResponse = new JsonResponse();
//		DefaultUserDetail userDetail = new DefaultUserDetail();
//		User user = new User();
//		user.setUserId(1);
//		userDetail.setUser(user);
//		int rowCount = shoppingCartDelegator.addCart(221, 273, 2, userDetail);
//	}
	
//	@Test
//	@Rollback(false)
//	public void testSaveProductsInCart() {
//		JsonResponse jsonResponse = new JsonResponse();
//		DefaultUserDetail userDetail = new DefaultUserDetail();
//		User user = new User();
//		user.setUserId(1);
//		userDetail.setUser(user);
//		String strCartItems = "[{\"id\":11,\"productId\":391,\"skuId\":269,\"buyCount\":7,\"isSelected\":1},{\"id\":21,\"productId\":669,\"skuId\":267,\"buyCount\":8,\"isSelected\":1}]";
//		ResultCode resultCode = shoppingCartDelegator.saveProductsInCart(strCartItems, userDetail);
//	}
	
	@Test
	@Ignore
	public void testDeleteCart() {
		JsonResponse jsonResponse = new JsonResponse();
        long userId = 1;
        boolean success = shoppingCartDelegator.deleteCart(userId, 2);
	}
	
	@Test
	@Ignore
	public void testItems() {
		List<CartItemVO> cartItems = shoppingCartFacade.getCartItemVOs(2);
		Map<String, Object> data = new HashMap<String, Object>();
		JsonResponse jsonResponse = new JsonResponse();
	    List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
	    
        data.put("count", cartItems.size());
        data.put("items", itemList);
        
        for (CartItemVO cartItem : cartItems) {
            Product product = cartItem.getProduct();
            Map<String, Object> productMap = new HashMap<String, Object>();
            productMap.put("productId", product.getId());
            productMap.put("productName", product.getName());
            productMap.put("productImage", product.getDetailImageArray().length > 0 ? product.getDetailImageArray()[0]
                            : "");

            ProductSKU sku = cartItem.getSku();
            List<ProductPropVO> skuProps = cartItem.getSkuProps();
            Map<String, Object> skuMap = new HashMap<String, Object>();
            skuMap.put("skuId", sku.getId());
            skuMap.put("price", sku.getPrice());
            skuMap.put("remainCount", sku.getRemainCount());
            skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));

            Map<String, Object> itemMap = new HashMap<String, Object>();
            itemMap.put("product", productMap);
            itemMap.put("sku", skuMap);
            itemMap.put("buyCount", cartItem.getBuyCount());
            itemMap.put("isSelected", cartItem.getIsSelected());
            itemMap.put("id", cartItem.getId());
            itemList.add(itemMap);
        }
	}
	
	 private String buildSkuSnapshot(List<ProductPropVO> skuProps) {
	        StringBuilder builder = new StringBuilder();
	        for (ProductPropVO prop : skuProps) {
	            builder.append(prop.toString());
	            builder.append("  ");
	        }
	        return builder.toString();
	    }
}
